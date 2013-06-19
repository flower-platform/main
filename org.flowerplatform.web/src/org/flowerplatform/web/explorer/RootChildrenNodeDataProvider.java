package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.DelegatingGenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;

public class RootChildrenNodeDataProvider implements INodeDataProvider {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(((File) source).getName());
		destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/organization.png"));
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		return DelegatingGenericTreeStatefulService.ROOT_NODE_MARKER;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		return new PathFragment(((File) node).getName(), nodeType);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		return new File(RootChildrenProvider.getWorkspaceRoot(), pathFragment.getName());
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		if (fullPath == null || fullPath.size() != 1) {
			throw new IllegalArgumentException("We were expecting a path with 1 item, but we got: " + fullPath);
		}
		return new File(RootChildrenProvider.getWorkspaceRoot(), fullPath.get(0).getName());
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return ((File) node).getName();
	}

	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		throw new UnsupportedOperationException();
	}

}
