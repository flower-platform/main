package org.flowerplatform.flexdiagram.controller {
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.tool.controller.IDragController;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ComposedControllerProvider implements IControllerProvider {
		
		public var visualChildrenController:IVisualChildrenController;
		public var modelExtraInfoController:IModelExtraInfoController;
		public var modelChildrenController:IModelChildrenController;
		public var absoluteLayoutRectangleController:IAbsoluteLayoutRectangleController;
		public var rendererController:IRendererController;
		public var selectionController:ISelectionController;
		
		public var inplaceEditorController:IInplaceEditorController;
		public var resizeController:IResizeController;
		public var dragToCreateRelationController:IDragToCreateRelationController;
		public var dragController:IDragController;
		public var selectOrDragToCreateElementController:ISelectOrDragToCreateElementController;
		
		public function getVisualChildrenController(model:Object):IVisualChildrenController {
			return visualChildrenController;
		}
		
		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {
			return modelExtraInfoController;
		}
		
		public function getModelChildrenController(model:Object):IModelChildrenController {
			return modelChildrenController;
		}
		
		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
			return absoluteLayoutRectangleController;
		}
		
		public function getRendererController(model:Object):IRendererController {
			return rendererController;
		}
		
		public function getSelectionController(model:Object):ISelectionController {
			return selectionController;
		}
		
		public function getInplaceEditorController(model:Object):IInplaceEditorController {
			return inplaceEditorController;
		}
		
		public function getResizeController(model:Object):IResizeController {
			return resizeController;
		}
		
		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController {
			return dragToCreateRelationController;
		}
		
		public function getDragController(model:Object):IDragController {
			return dragController;
		}
		
		public function getSelectOrDragToCreateElementController(model:Object):ISelectOrDragToCreateElementController {
			return selectOrDragToCreateElementController;
		}
	}
}