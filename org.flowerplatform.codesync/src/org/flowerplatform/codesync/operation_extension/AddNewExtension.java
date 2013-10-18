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
package org.flowerplatform.codesync.operation_extension;

import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService1;
import org.flowerplatform.emf_model.notation.Node;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public interface AddNewExtension {
	
	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String LOCATION = "location";
	
	/**
	 * May populate the {@code parameters} map with values for:
	 * <ul>
	 * 		<li>{@link CodeSyncDiagramOperationsService1#PARENT_CODE_SYNC_ELEMENT}
	 * 		<li>{@link CodeSyncDiagramOperationsService1#PARENT_VIEW}
	 * </ul>
	 */
	String addNew(CodeSyncElement codeSyncElement, Node view, Resource codeSyncMappingResource, Map<String, Object> parameters);
	
}