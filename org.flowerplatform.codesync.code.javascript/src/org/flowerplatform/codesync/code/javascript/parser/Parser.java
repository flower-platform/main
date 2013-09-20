/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.code.javascript.parser;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.flowerplatform.codesync.code.javascript.regex_ast.Node;
import org.flowerplatform.codesync.code.javascript.regex_ast.Parameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.common.regex.RegexUtil;
import org.flowerplatform.common.regex.RegexWithAction;

/**
 * @author Mariana Gheorghe
 */
public class Parser {

	public static final String JS_FILE = "jsFile";
	
	public static final String JS_REQUIRE 			= "jsRequire";
	public static final String JS_REQUIRE_REGEX 	= "var\\s*(\\S+?)\\s*=\\s*require\\((\\S+)\\)";
	
	public static final String JS_CLASS 			= "jsClass";
	public static final String JS_CLASS_REGEX 		= "return\\s*(\\S+?)\\.extend\\s*\\(\\s*\\{";
	
	public static final String JS_NAMED_CLASS		= "jsNamedClass";
	public static final String JS_NAMED_CLASS_REGEX= "(\\S+?)\\s*=\\s*(\\S+?)\\.extend\\s*\\(\\s*\\{";
	
	public static final String JS_FUNCTION 		= "jsFunction";
	public static final String JS_FUNCTION_REGEX 	= "function\\s*\\((.*?)\\)\\s*\\{";
	
	public static final String JS_NAMED_FUNCTION	= "jsNamedFunction";
	public static final 
		String JS_NAMED_FUNCTION_REGEX				= "(\\S+?)\\s*[:=]\\s*" + JS_FUNCTION_REGEX;
	public static final
		String JS_NAMED_FUNCTION_REGEX2				= "function\\s*(\\S+?)\\s*\\((.*?)\\)\\s*\\{";
	
	public static final String JS_EVENTS 			= "jsEvents";
	public static final String JS_EVENTS_REGEX 	= "events\\s*:\\s*\\{";
	
	public static final String JS_EVENT 			= "jsEvent";
	public static final String JS_EVENT_REGEX 		= "\"(\\S+?)\\s*(\\S*?)\"\\s*:\\s*\"(\\S+?)\"";
	
	public static final String JS_ATTRIBUTE 		= "jsAttribute";
	public static final String JS_ATTRIBUTE_REGEX 	= "(\\w+)\\s*+[:=]";
	
	public static final String JS_DOC				= "jsDoc";
	public static final String JS_DOC_REGEX		= "/\\*\\*(.+?)\\*/";
	
	public static final String JS_REQUIRE_CATEGORY				= "require";
	public static final String JS_MAIN_CLASS_CATEGORY 			= "main class";
	public static final String JS_SUB_CLASSES_CATEGORY			= "sub classes";
	public static final String JS_CLASS_FUNCTIONS_CATEGORY 	= "functions";
	public static final String JS_CLASS_ATTRIBUTES_CATEGORY 	= "attributes";
	public static final String JS_EVENTS_CATEGORY 				= "events";
	
	public static final String NAME = "name";
	
	public static RegexEngineState[] statesStack = new RegexEngineState[100];
	public static RegexEngineState currentState;

	protected String input;
	
	protected Node jsDoc;
	
	public Node parse(File file) {
		try {
			input = FileUtils.readFileToString(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Node root = createNode(JS_FILE, NAME, false, 0, 0);
		addParameter(root, NAME, file.getName(), 0, 0);
		
		RegexConfiguration config = new RegexConfiguration();
		buildConfig(config);
		RegexProcessingSession session = config.startSession(input);
		
		enterState(session, JS_FILE, root, 0);
		
		try {
			while (session.find()) {}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		print("", root);
		
		// adding to resource to avoid UNDEFINED values during sync
		// see EObjectModelAdapter.getValueFeatureValue()
		Resource resource = new ResourceImpl();
		resource.getContents().add(root);
		
		return root;
	}
	
	protected void buildConfig(RegexConfiguration config) {
		config
		.add(new RegexWithAction(JS_DOC, JS_DOC_REGEX) {

			@Override
			public void executeAction(RegexProcessingSession session) {
				jsDoc = createNode(JS_DOC, null, false, session.getMatcher().start(), session.getMatcher().end());
				addParameter(jsDoc, "text", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
			}
			
		})
		.add(new RegexWithAction.IfFindThisSkip("multiline commment", RegexUtil.MULTI_LINE_COMMENT))
		.add(new RegexWithAction.IfFindThisSkip("single line comment", RegexUtil.SINGLE_LINE_COMMENT))
		.add(new RegexWithAction("closing bracket", "[\\}\\]]") {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				exitState(session);
			}
		})
		.add(new RegexWithAction(JS_REQUIRE, JS_REQUIRE_REGEX) {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
//				if (currentState.category.equals(JS_FILE)) {
					Node require = addToCategory(JS_REQUIRE_CATEGORY, currentState.node, NAME, JS_REQUIRE, session.getMatcher().start(), session.getMatcher().end());
					addParameter(require, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(require, "path", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
					
					attachJsDoc(require);
//				}
			}
		})
		.add(new RegexWithAction(JS_NAMED_CLASS, JS_NAMED_CLASS_REGEX) {

			@Override
			public void executeAction(RegexProcessingSession session) {
				Node cls = addToCategory(JS_SUB_CLASSES_CATEGORY, currentState.node, NAME, JS_NAMED_CLASS, session.getMatcher().start(), 0);
				addParameter(cls, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
				addParameter(cls, "superClass", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
				
				enterState(session, JS_CLASS, cls, 1);
				
				attachJsDoc(cls);
			}
		})
		.add(new RegexWithAction(JS_CLASS, JS_CLASS_REGEX) {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				Node cls = createNode(JS_MAIN_CLASS_CATEGORY, NAME, true, session.getMatcher().start(), 0);
				currentState.node.getChildren().add(cls);
				addParameter(cls, "superClass", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
				
				enterState(session, JS_CLASS, cls, 1);
				
				attachJsDoc(cls);
			}
		})
		.add(new RegexWithAction(JS_NAMED_FUNCTION, JS_NAMED_FUNCTION_REGEX) {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				Node function = null;
				if (currentState.category.equals(JS_CLASS) || currentState.category.equals(JS_FILE)) {
					function = addToCategory(JS_CLASS_FUNCTIONS_CATEGORY, currentState.node, NAME, JS_FUNCTION, session.getMatcher().start(), 0);
					
					enterState(session, JS_FUNCTION, function, 1);
				} else if (currentState.category.equals(JS_FUNCTION)) {
					function = createNode(JS_FUNCTION, NAME, false, session.getMatcher().start(), 0);
					currentState.node.getChildren().add(function);
					
					enterState(session, JS_FUNCTION, function, 1);
				}
				if (function != null) {
					addParameter(function, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(function, "signature", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
					
					attachJsDoc(function);
				} else {
					session.currentNestingLevel++;
				}
			}
		})
		.add(new RegexWithAction(JS_NAMED_FUNCTION, JS_NAMED_FUNCTION_REGEX2) {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				Node function = null;
				if (currentState.category.equals(JS_CLASS) || currentState.category.equals(JS_FILE)) {
					function = addToCategory(JS_CLASS_FUNCTIONS_CATEGORY, currentState.node, NAME, JS_FUNCTION, session.getMatcher().start(), 0);
					
					enterState(session, JS_FUNCTION, function, 1);
				} else if (currentState.category.equals(JS_FUNCTION)) {
					function = createNode(JS_FUNCTION, NAME, false, session.getMatcher().start(), 0);
					currentState.node.getChildren().add(function);
					
					enterState(session, JS_FUNCTION, function, 1);
				}
				if (function != null) {
					addParameter(function, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(function, "signature", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
				
					attachJsDoc(function);
				} else {
					session.currentNestingLevel++;
				}
			}
		})
//		.add(new RegexWithAction(JS_FUNCTION, JS_FUNCTION_REGEX) {
//
//			@Override
//			public void executeAction(RegexProcessingSession session) {
//				Node function = null;
//				if (currentState.category.equals(JS_CLASS) || currentState.category.equals(JS_FILE)) {
//					function = addToCategory(JS_CLASS_FUNCTIONS_CATEGORY, currentState.node, NAME, JS_FUNCTION, session.getMatcher().start(), 0);
//				} else if (currentState.category.equals(JS_FUNCTION)) {
//					function = createNode(JS_FUNCTION, NAME, false, session.getMatcher().start(), 0);
//					currentState.node.getChildren().add(function);
//				}
//				if (function != null) {
//					enterState(session, JS_FUNCTION, function, 1);
//					addParameter(function, "signature", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
//				
//					attachJsDoc(function);
//				} else {
//					session.currentNestingLevel++;
//				}
//			}
//			
//		})
		.add(new RegexWithAction(JS_EVENTS, JS_EVENTS_REGEX) {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(JS_CLASS)) {
					Node events = addToCategory(JS_CLASS_ATTRIBUTES_CATEGORY, currentState.node, NAME, JS_EVENTS, session.getMatcher().start(), session.getMatcher().end());
					
					enterState(session, JS_EVENTS, currentState.node, 1);
					
					attachJsDoc(events);
				}
			}
		})
		.add(new RegexWithAction(JS_EVENT, JS_EVENT_REGEX) {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(JS_EVENTS)) {
					Node event = addToCategory(JS_EVENTS_CATEGORY, currentState.node, NAME, JS_EVENT, session.getMatcher().start(), session.getMatcher().end());
					addParameter(event, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(event, "selector", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
					addParameter(event, "handler", session.getCurrentSubMatchesForCurrentRegex()[2], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 3), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 3));
				}
			}
		})
		.add(new RegexWithAction(JS_ATTRIBUTE, JS_ATTRIBUTE_REGEX) {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(JS_CLASS) || currentState.category.equals(JS_FILE)) {
					Node attr = addToCategory(JS_CLASS_ATTRIBUTES_CATEGORY, currentState.node, NAME, JS_ATTRIBUTE, session.getMatcher().start(), session.getMatcher().end());
					addParameter(attr, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					
					enterState(session, JS_ATTRIBUTE, attr, 1);
					
					attachJsDoc(attr);
				}
			}
		})
		.add(new RegexWithAction("opening bracket", "[\\{\\[]") {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				session.currentNestingLevel++;
			}
		})
		.add(new RegexWithAction("", "[,;]") {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(JS_ATTRIBUTE) && statesStack[session.currentNestingLevel] != null
						&& statesStack[session.currentNestingLevel].category.equals(JS_ATTRIBUTE)) {
					addParameter(currentState.node, "value", input.substring(currentState.node.getOffset() + currentState.node.getLength(), session.getMatcher().start()), 
							currentState.node.getOffset() + currentState.node.getLength(), session.getMatcher().start());
					currentState.node.setLength(session.getMatcher().start() - currentState.node.getOffset());
					exitState(session);
				}
			}
		})
		.compile(Pattern.DOTALL);
	}
	
	protected static void enterState(RegexProcessingSession session, String state, Node node, int increment) {
		session.currentNestingLevel += increment;
		currentState = new RegexEngineState(state, node);
		statesStack[session.currentNestingLevel] = currentState;
	}
	
	protected static void exitState(RegexProcessingSession session) {
		if (statesStack[session.currentNestingLevel--] != null) {
			currentState.node.setLength(session.getMatcher().start() - currentState.node.getOffset() + 1);
			statesStack[session.currentNestingLevel + 1] = null;
			int index = session.currentNestingLevel;
			while (statesStack[index] == null) {
				index--;
			}
			currentState = statesStack[index];
		}
	}
	
	protected static Node createNode(String type, String keyParameter, boolean isCategoryNode, int start, int end) {
		Node node = RegExAstFactory.eINSTANCE.createNode();
		node.setType(type);
		node.setKeyParameter(keyParameter);
		node.setCategoryNode(isCategoryNode);
		node.setOffset(start);
		node.setLength(end - start);
		return node;
	}
	
	protected Node addToCategory(String category, Node parent, String keyParameter, String type, int start, int end) {
		Node node = createNode(type, keyParameter, false, start, end - start);
		getCategory(parent, category).getChildren().add(node);
		return node;
	}
	
	protected Node getCategory(Node parent, String category) {
		for (Node node : parent.getChildren()) {
			if (node.isCategoryNode() && node.getType().equals(category)) {
				return node;
			}
		}
		Node node = createNode(category, null, true, 0, 0);
		node.setKeyParameter(NAME);
		addParameter(node, NAME, category + " category", 0, 0);
		parent.getChildren().add(node);
		return node;
	}
	
	protected void addParameter(Node node, String name, String value, int start, int end) {
		Parameter parameter = RegExAstFactory.eINSTANCE.createParameter();
		parameter.setName(name);
		parameter.setValue(value);
		parameter.setValue(value);
		parameter.setOffset(start);
		parameter.setLength(end - start);
		node.getParameters().add(parameter);
	}
	
	protected void attachJsDoc(Node node) {
		if (jsDoc != null) {
			node.getChildren().add(jsDoc);
			jsDoc = null;
		}
	}
	
	private void print(String indent, Node node) {
		String label = new String(node.getType() + " ");
		label += getParameterValue(node, node.getKeyParameter());
		if (node.getOffset() != 0 || node.getLength() != 0) {
			label += " (" + node.getOffset() + ", " + node.getLength() + ") ";
		}
		label += ": ";
		for (Parameter parameter : node.getParameters()) {
			if (parameter.getOffset() != 0 || parameter.getLength() != 0) {
				label += String.format("(%s = %s) (%s, %s), ", 
						parameter.getName(),
						parameter.getValue().replaceAll("\n", ""),
						parameter.getOffset(),
						parameter.getLength());
			} else {
				label += String.format("(%s = %s), ",
						parameter.getName(),
						parameter.getValue().replace("\n", ""));
			}
		}
		label = label.substring(0, label.length() - 2);
		if (node.isCategoryNode()) {
			label = "[[ " + label + "]]";
		}
		System.out.println(indent + label);
		
		for (Node child : node.getChildren()) {
			print(indent + "	", child);
		}
	}
	
	private String getParameterValue(Node node, String name) {
		for (Parameter parameter : node.getParameters()) {
			if (parameter.getName().equals(name)) {
				return parameter.getValue();
			}
		}
		return "";
	}
}
