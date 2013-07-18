package org.flowerplatform.editor.mindmap {
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.editor.mindmap.controller.MindMapNodeDragController;
	import org.flowerplatform.editor.mindmap.controller.MindMapNodeInplaceEditorController;
	import org.flowerplatform.editor.mindmap.controller.MindMapNodeRendererController;
	import org.flowerplatform.editor.mindmap.renderer.MindMapNodeRenderer;
	import org.flowerplatform.editor.mindmap.renderer.MindMapNodeSelectionRenderer;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_children.ParentAwareArrayListModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NotationMindMapDiagramShell extends MindMapDiagramShell implements IMindMapControllerProvider {
		
		private var mindMapNodeController:IMindMapModelController;
		
		private var mindMapNodeAbsoluteRectangleController:IAbsoluteLayoutRectangleController;
		private var mindMapNodeDragController:MindMapNodeDragController;
		private var mindMapNodeInplaceEditorController:MindMapNodeInplaceEditorController;
		private var mindMapNodeSelectionController:SelectionController;
		private var mindMapNodeExtraInfoController:IModelExtraInfoController;
		
		private var absoluteLayoutVisualChildrenController:AbsoluteLayoutVisualChildrenController;
		private var arrayListNodeChildrenController:ParentAwareArrayListModelChildrenController;
		private var mindMapNodeRendererController:MindMapNodeRendererController;
		
		public function NotationMindMapDiagramShell() {
			super();
			
			//mindMapModelController = ;
			mindMapNodeAbsoluteRectangleController = new MindMapAbsoluteLayoutRectangleController(this);
			mindMapNodeDragController = new MindMapNodeDragController(this);
			mindMapNodeSelectionController = new SelectionController(this, MindMapNodeSelectionRenderer);
			mindMapNodeExtraInfoController = new DynamicModelExtraInfoController(this);
			mindMapNodeInplaceEditorController = new MindMapNodeInplaceEditorController(this);
			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController(this);
			arrayListNodeChildrenController = new ParentAwareArrayListModelChildrenController(this, true);
			mindMapNodeRendererController = new MindMapNodeRendererController(this, MindMapNodeRenderer);
		}
		
		public function getMindMapModelController(model:Object):IMindMapModelController {
			return mindMapNodeController;
		}
		
		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
			if (model is MindMapNode) {
				return mindMapNodeAbsoluteRectangleController;
			}
			return null;
		}
		
		public function getDragController(model:Object):IDragController {
			if (model is MindMapNode) { 
				return mindMapNodeDragController;
			}
			return null;
		}
		
		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController {			
			return null;
		}
		
		public function getInplaceEditorController(model:Object):IInplaceEditorController {
			if (model is MindMapNode) {
				return mindMapNodeInplaceEditorController;
			}
			return null;
		}
		
		public function getModelChildrenController(model:Object):IModelChildrenController {			
			if (model is ArrayList) {
				return arrayListNodeChildrenController;
			}
			return null;
		}
		
		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {
			return mindMapNodeExtraInfoController;			
		}
		
		public function getRendererController(model:Object):IRendererController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getResizeController(model:Object):IResizeController {			
			return null;
		}
		
		public function getSelectOrDragToCreateElementController(model:Object):ISelectOrDragToCreateElementController {			
			return null;
		}
		
		public function getSelectionController(model:Object):ISelectionController {
			if (model is MindMapNode) {
				return mindMapNodeSelectionController;
			}
			return null;
		}
		
		public function getVisualChildrenController(model:Object):IVisualChildrenController {			
			if (model is ArrayList) {
				return absoluteLayoutVisualChildrenController;
			} 
			return null;
		}
		
		
		
	}
}