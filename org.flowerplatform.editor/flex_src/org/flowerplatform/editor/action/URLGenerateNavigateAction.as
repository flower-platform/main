package org.flowerplatform.editor.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.link.URLGenerateNavigateView;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Cristina Constatinescu
	 */ 
	public class URLGenerateNavigateAction extends ActionBase  {
				
		public function URLGenerateNavigateAction() {
			label = "URL Generate / Navigate";
			icon = EditorPlugin.getInstance().getResourceUrl("images/external_link.png");
		}
		
		override public function get visible():Boolean {
			if (selection.length == 0)  
				return false; // At least a node must be selected
			
			var currentEditorDescriptor:BasicEditorDescriptor;
			for (var i:int = 0; i < selection.length; i++) {
				// top level action: Open => search for the first editorDescriptor
				var treeNode:TreeNode = TreeNode(selection.getItemAt(0));
				var ctIndex:int = treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE];
				var ctDescriptor:ContentTypeDescriptor = EditorPlugin.getInstance().contentTypeDescriptors[ctIndex];
				currentEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(String(ctDescriptor.compatibleEditors.getItemAt(0)));
				
				if (currentEditorDescriptor == null || !currentEditorDescriptor.canCalculateFriendlyEditableResourcePath()) {
					return false;
				}
			}			
			return true;
		}
		
		override public function run():void {
			var generateUrlView:URLGenerateNavigateView = new URLGenerateNavigateView();
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(generateUrlView)
				.setWidth(550)
				.setHeight(400)
				.show();
		}
	}
}