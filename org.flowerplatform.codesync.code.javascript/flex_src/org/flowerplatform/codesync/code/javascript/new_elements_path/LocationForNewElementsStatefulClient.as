package org.flowerplatform.codesync.code.javascript.new_elements_path {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class LocationForNewElementsStatefulClient extends GenericTreeStatefulClient {
		
		public static const DIAGRAM_EDITABLE_RESOURCE_PATH_KEY:String = "diagramEditableResourcePath";
		public static const NEW_ELEMENTS_PATH_KEY:String = "newElementsPath";
		
		public function LocationForNewElementsStatefulClient() {
			super();
			
			statefulServiceId = "locationForNewElementsStatefulService";
			clientIdPrefix = "Location For New Elements Tree";
		
			requestDataOnSubscribe = false;
			requestDataOnServer = false;	
			
			context[WHOLE_TREE_KEY] = true;
		}
				
		[RemoteInvocation]
		public function setAdditionalInfo(additionalPath:String, pathsToOpen:ArrayCollection):void {
			LocationForNewElementsDialog(treeList.parent).additionalPath.text = additionalPath;
			if (pathsToOpen != null) {				
				for each (var pathToOpen:Object in pathsToOpen) {
					treeList.refreshLinearizedDataProvider();
					var existingNode:TreeNode = TreeNode.getNodeByPath(ArrayCollection(pathToOpen), TreeNode(treeList.rootNode));
					
					// search for the wrapper and its index
					var existingWrapper:HierarchicalModelWrapper = null;
					var index:int;				
					for (var i:int = 0; i<treeList.dataProvider.length; i++) {
						var wrapper:HierarchicalModelWrapper = HierarchicalModelWrapper(treeList.dataProvider.getItemAt(i));
						if (wrapper.treeNode == existingNode) {
							existingWrapper = wrapper;
							index = i;
							break;
						}
					}				
					if (!existingWrapper.expanded) {
						existingWrapper.expanded = !wrapper.expanded;									
					}	
					var vector:Vector.<int> = new Vector.<int>();
					vector.push(index);
					treeList.selectedIndices = vector;		
				}
				pathsToOpen = null;
			}
		}
	}
}