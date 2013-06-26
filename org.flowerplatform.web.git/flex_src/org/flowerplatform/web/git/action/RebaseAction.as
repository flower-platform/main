package org.flowerplatform.web.git.action {
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.dto.GitActionDto;
	import org.flowerplatform.web.git.ui.RebaseWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RebaseAction extends ActionBase {
		
		private var node:TreeNode;
		
		public function RebaseAction() {
			label = GitPlugin.getInstance().getMessage("git.action.rebase.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/rebase.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.rebase.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_LOCAL_BRANCH;
			}
			return false;
		}
		
		private function getNodeAdditionalDataCallbackHandler(result:GitActionDto):void {
			if (result != null) {
				var popup:RebaseWindow = new RebaseWindow();
				popup.dto = result;
				popup.dto.repositoryNode = node.parent.parent;
				popup.showPopup();
			}
		}
		
		override public function run():void {
			node = TreeNode(selection.getItemAt(0));
			GitPlugin.getInstance().service.getNodeAdditionalData(node, this, getNodeAdditionalDataCallbackHandler);
		}
	}
}