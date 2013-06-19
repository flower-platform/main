package org.flowerplatform.flexdiagram.samples.controller
{
	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	
	import mx.core.IDataRenderer;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	
	import spark.components.TextInput;
	import spark.components.supportClasses.StyleableTextField;
	
	public class BasicSubModelInplaceEditorController extends ControllerBase implements IInplaceEditorController {
		
		public function BasicSubModelInplaceEditorController(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			var renderer:DisplayObject = DisplayObject(diagramShell.getRendererForModel(model));
			var textField:TextInput = new TextInput();
						
			diagramShell.diagramRenderer.addElement(textField);
			
			var bounds:Rectangle = renderer.getBounds(DisplayObject(diagramShell.diagramRenderer));
			textField.x = bounds.x + 2;
			textField.y = bounds.y;
			textField.width = bounds.width;
			textField.height = bounds.height;
			textField.text = BasicSubModel(model).name;
			textField.callLater(textField.setFocus);
			
			diagramShell.modelToExtraInfoMap[model].inplaceEditor = textField;
		}
		
		public function commit(model:Object):void {		
			var textField:TextInput = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			BasicSubModel(model).name = textField.text;
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function abort(model:Object):void {
			// here can be placed a warning
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			var textField:TextInput = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			diagramShell.diagramRenderer.removeElement(textField);
			
			delete diagramShell.modelToExtraInfoMap[model].inplaceEditor;			
		}		
	}
	
}