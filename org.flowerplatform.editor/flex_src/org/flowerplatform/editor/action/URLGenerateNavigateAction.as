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
				var foundEditorDescriptor:BasicEditorDescriptor = EditorPlugin.getInstance().getFirstEditorDescriptorForNode(ctIndex);
				if (foundEditorDescriptor == null || !foundEditorDescriptor.canCalculateFriendlyEditableResourcePath())
					return false; // node is not openable
			}			
			return true;
		}
		
		override public function run():void {
			var generateUrlView:URLGenerateNavigateView = new URLGenerateNavigateView();
			generateUrlView.explorerSelection = selection;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(generateUrlView)
				.setWidth(550)
				.setHeight(400)
				.show();
		}
	}
}