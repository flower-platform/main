package org.flowerplatform.web.git.action {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.dto.GitActionDto;
	import org.flowerplatform.web.git.ui.MergeWindow;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MergeAction extends ActionBase {
		
		private var node:TreeNode;
		
		public function MergeAction() {
			label = GitPlugin.getInstance().getMessage("git.action.merge.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/merge.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.merge.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_LOCAL_BRANCH;
			}
			return false;
		}
		
		private function getNodeAdditionalDataCallbackHandler(result:GitActionDto):void {
			if (result != null) {
				var popup:MergeWindow = new MergeWindow();
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