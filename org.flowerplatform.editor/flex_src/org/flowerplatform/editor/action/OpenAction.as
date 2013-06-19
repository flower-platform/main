package  org.flowerplatform.editor.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * Opens an editor. Exists directly in the context menu or
	 * as a child of "Open with" menu.
	 * 
	 * @author Cristi
	 * @author Mariana
     * @author Sorin
	 * @flowerModelElementId _c1aTwE2iEeGsUPSh9UfXpw
	 */
	public class OpenAction extends ActionBase {
		
		public static const ICON_URL:String = EditorPlugin.getInstance().getResourceUrl("images/open_resource.png");
		
		private var editorDescriptor:BasicEditorDescriptor;
		
		public var forceNewEditor:Boolean;
		
		/**
		 * There will be an editorEntry only when this action is added in the Open With submenu
		 * @flowerModelElementId _wb413FJdEeGnQ71Q1-0lCg
		 */ 
		public function OpenAction(editorDescriptor:BasicEditorDescriptor, forceNewEditor:Boolean):void {
			if (editorDescriptor == null) {
				// top level action: Open
				label = EditorPlugin.getInstance().getMessage("editor.open");
				icon = ICON_URL;
				preferShowOnActionBar = true;
			} else {
				// child action of Open With
				this.editorDescriptor = editorDescriptor;
				this.forceNewEditor = forceNewEditor;
				label = editorDescriptor.getTitle();
				icon = editorDescriptor.getIcon();
				parentId = EditorTreeActionProvider.OPEN_WITH_ACTION_ID;
			}
		}
		
		/**
		 * @flowerModelElementId _wb414FJdEeGnQ71Q1-0lCg
		 */
		override public function run():void {
			var currentEditorDescriptor:BasicEditorDescriptor = editorDescriptor;
			for (var i:int = 0; i < selection.length; i++) {
				if (currentEditorDescriptor == null) {
					// top level action: Open => search for the first editorDescriptor
					var treeNode:TreeNode = TreeNode(selection.getItemAt(0));
					var ctIndex:int = treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE];
					var ctDescriptor:ContentTypeDescriptor = EditorPlugin.getInstance().contentTypeDescriptors[ctIndex];
					currentEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(String(ctDescriptor.compatibleEditors.getItemAt(0)));
				}
				var editableResourcePath:String = EditorPlugin.getInstance().getEditableResourcePathFromTreeNode(TreeNode(selection.getItemAt(i)));
				currentEditorDescriptor.openEditor(editableResourcePath, forceNewEditor);
			}
		}
	}
}