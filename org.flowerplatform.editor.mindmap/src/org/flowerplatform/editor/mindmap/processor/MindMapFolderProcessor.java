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
package org.flowerplatform.editor.mindmap.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristina Constantinescu
 */
public class MindMapFolderProcessor extends MindMapDefaultProcessor {

	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		super.processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, viewDetails);
		
//		List<String> icons = new ArrayList<String>();
//		icons.add("images/folder.gif");
//		viewDetails.put("icons", icons);
	}
}