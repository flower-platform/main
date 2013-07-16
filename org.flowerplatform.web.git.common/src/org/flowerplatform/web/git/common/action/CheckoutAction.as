package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.CheckoutView;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CheckoutAction extends ActionBase	{
		
		public function CheckoutAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.checkout.label");	
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/checkout.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.checkout.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				var node:TreeNode = TreeNode(selection.getItemAt(0));
				return node.pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE_BRANCHES ||
					node.pathFragment.type == GitCommonPlugin.NODE_TYPE_TAG;
			}
			return false;
		}
		
		private function getName(fullName:String):String {
			return fullName.substring(fullName.lastIndexOf("/") + 1);
		}
		
		override public function run():void {		
			var popup:CheckoutView = new CheckoutView();
			popup.node = TreeNode(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(popup)
				.setWidth(400)
				.setHeight(350)
				.show();			
		}
	}
}