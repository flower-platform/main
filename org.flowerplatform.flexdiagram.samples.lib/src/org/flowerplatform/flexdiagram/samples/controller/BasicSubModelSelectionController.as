package org.flowerplatform.flexdiagram.samples.controller {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class BasicSubModelSelectionController extends ControllerBase implements ISelectionController {
		
		public function BasicSubModelSelectionController(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
		
		public function setSelectedState(model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void {
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model));
			
			var extraInfo:Object = diagramShell.modelToExtraInfoMap[model];
			
			if (extraInfo.selected != isSelected) {
				modelExtraInfoController.setSelected(model, isSelected);
			}
			
			if (renderer == null) {
				return;
			}
			if (isSelected) {
				SubModelIconItemRenderer(renderer).setStyle("alternatingItemColors", [isMainSelection ? "green" : "gray"]);
			} else {
				SubModelIconItemRenderer(renderer).setStyle("alternatingItemColors", []);
			}
		}
		
		public function associatedModelToSelectionRenderer(model:Object, renderer:IVisualElement):void {			
			setSelectedState(model, renderer, 
				diagramShell.modelToExtraInfoMap[model].selected, 
				diagramShell.mainSelectedItem == model);
		}
		
		public function unassociatedModelFromSelectionRenderer(model:Object, renderer:IVisualElement):void {
			
		}
	}
}