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
package com.crispico.flower.mp.codesync.code.java.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.file.IFileAccessController;

import com.crispico.flower.mp.codesync.code.adapter.AbstractFileModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to files with the extension <code>java</code>. Chidren are {@link ASTNode}s.
 * 
 * @author Mariana
 */
public class JavaFileModelAdapter extends AbstractFileModelAdapter {

	@Override
	public Object createChildOnContainmentFeature(Object file, Object feature, Object correspondingChild) {
		CompilationUnit cu = getOrCreateCompilationUnit(file);
		ASTNode node = (ASTNode) JavaTypeModelAdapter.createCorrespondingModelElement(cu.getAST(), (CodeSyncElement) correspondingChild);
		cu.types().add(node);
		return node;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		((ASTNode) child).delete();
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return getOrCreateCompilationUnit(modelElement).types();
	}
	
	private CompilationUnit getOrCreateCompilationUnit(Object file) {
		return (CompilationUnit) getOrCreateFileInfo(file);
	}
	
	/**
	 * Creates a new compilation unit from the file's content.
	 */
	@Override
	protected Object createFileInfo(Object file) {
		
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		Map options = new HashMap<>(JavaCore.getOptions());
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
		parser.setCompilerOptions(options);
		boolean fileExists = EditorPlugin.getInstance().getFileAccessController().exists(file);
		char[] initialContent = fileExists ? getFileContent(file) : new char[0];
		parser.setSource(initialContent);
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		return astRoot;
	}

	private char[] getFileContent(Object file) {
		IFileAccessController fileAccessController = EditorPlugin.getInstance().getFileAccessController();
		return fileAccessController.readFileToString(file).toCharArray();
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		CompilationUnit cu = (CompilationUnit) fileInfo;
		return cu.rewrite(document, null);
	}
	
}