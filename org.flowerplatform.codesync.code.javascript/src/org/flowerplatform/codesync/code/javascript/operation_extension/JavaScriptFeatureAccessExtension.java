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
package org.flowerplatform.codesync.code.javascript.operation_extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;
import org.flowerplatform.codesync.config.extension.FeatureAccessExtension;
import org.flowerplatform.codesync.config.extension.NamedElementFeatureAccessExtension;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavaScriptFeatureAccessExtension extends FeatureAccessExtension {

	public JavaScriptFeatureAccessExtension(List<CodeSyncElementDescriptor> descriptors) {
		super(descriptors);
		
		for (CodeSyncElementDescriptor descriptor : descriptors) {
			for (String feature : descriptor.getFeatures()) {
				if (getFeature(feature) == null) {
					addFeature(feature, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
				}
			}
		}
	}

	@Override
	public Object getValue(CodeSyncElement codeSyncElement, String featureName) {
		Object value = super.getValue(codeSyncElement, featureName);
		if (RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters().equals(getFeature(featureName))) {
			return getParameter(value, featureName);
		}
		return value;
	}
	
	@Override
	public void setValue(CodeSyncElement codeSyncElement, String featureName, Object newValue) {
		Object fromClient = newValue;
		Object value = super.getValue(codeSyncElement, featureName);
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		if (featureName.equals(descriptor.getKeyFeature())) {
			// change the file name
			CodeSyncElement parent = (CodeSyncElement) codeSyncElement.eContainer();
			if (parent != null && parent.getType().equals(CodeSyncPlugin.FILE)) {
				String fileName = newValue + "." + descriptor.getExtension();
				CodeSyncOperationsService.getInstance().setFeatureValue(parent, 
						NamedElementFeatureAccessExtension.NAME, fileName);
			}
		}
		if (RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters().equals(getFeature(featureName))) {
			newValue = setParameter(value, featureName, (String) fromClient);
		}
		super.setValue(codeSyncElement, featureName, newValue);
	}

	protected Object getParameter(Object value, String featureName) {
		Collection<RegExAstNodeParameter> parameters = value == null ? null : 
			EcoreUtil.copyAll((Collection<RegExAstNodeParameter>) value);
		if (parameters != null) {
			for (RegExAstNodeParameter parameter : parameters) {
				if (parameter.getName().equals(featureName)) {
					return parameter.getValue();
				}
			}
		}
		return null;
	}
	
	protected Object setParameter(Object value, String featureName, String parameterValue) {
		Collection<RegExAstNodeParameter> parameters = value == null ? new ArrayList<RegExAstNodeParameter>() : 
			EcoreUtil.copyAll((Collection<RegExAstNodeParameter>) value);
		boolean foundParameter = false;
		for (RegExAstNodeParameter parameter : parameters) {
			if (parameter.getName().equals(featureName)) {
				if (!parameter.getValue().equals(parameterValue)) {
					parameter.setValue(parameterValue);
				}
				foundParameter = true;
				break;
			}
		}
		if (!foundParameter) {
			RegExAstNodeParameter parameter = RegExAstFactory.eINSTANCE.createRegExAstNodeParameter();
			parameter.setName(featureName);
			parameter.setValue(parameterValue);
			parameters.add(parameter);
		}
		return parameters;
	}
		
}
