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
package org.flowerplatform.web.projects;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;

/**
 * Parent node = Working Directories virtual node (i.e. Pair<File, String>).<br/>
 * Child node is a {@link WorkingDirectory}.
 * 
 * @author Cristian Spiescu
 */
public class WorkingDirectory_WorkingDirectoriesChildrenProvider implements IChildrenProvider {

	public static final String NODE_TYPE_WORKING_DIRECTORY = "workingDirectory";

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		// the file points to the org dir
		@SuppressWarnings("unchecked")
		File organizationFile = ((Pair<File, String>) node).a;

		List<WorkingDirectory> list = ProjectsService.getInstance().getWorkingDirectoriesForOrganizationName(organizationFile.getName());
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(list.size());
		
		for (WorkingDirectory wd : list) {
			Pair<Object, String> child = new Pair<Object, String>(wd, NODE_TYPE_WORKING_DIRECTORY);
			result.add(child);
		}
		return result;		
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}