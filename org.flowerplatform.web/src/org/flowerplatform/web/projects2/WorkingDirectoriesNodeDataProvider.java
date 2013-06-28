package org.flowerplatform.web.projects2;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.explorer2.AbstractVirtualItemInOrganizationNodeDataProvider;

/**
 * Node is a virtual node, i.e. Pair<parent org, nodeType>.
 * 
 * @author Cristi
 */
public class WorkingDirectoriesNodeDataProvider extends AbstractVirtualItemInOrganizationNodeDataProvider {

	public WorkingDirectoriesNodeDataProvider() {
		super();
		nodeLabel = WebPlugin.getInstance().getMessage("explorer.workingDirectories");
		nodeIcon = WebPlugin.getInstance().getResourceUrl("images/workset.gif");
	}

}
