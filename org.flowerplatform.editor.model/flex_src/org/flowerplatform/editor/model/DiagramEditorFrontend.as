package org.flowerplatform.editor.model {
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.messaging.errors.NoChannelAvailableError;
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.DragToCreateRelationTool;
	import org.flowerplatform.flexdiagram.tool.DragTool;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ResizeTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.SelectOrDragToCreateElementTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	
	import spark.components.Button;
	import spark.components.Label;
	import spark.components.TextInput;
	
	public class DiagramEditorFrontend extends EditorFrontend {
	
		public var diagramShell:NotationDiagramShell;
		
		override protected function createChildren():void {
			var scroller:InfiniteScroller = new InfiniteScroller();
			editor = scroller;
			
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
			scroller.viewport = diagramRenderer;
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
			
			diagramShell = new NotationDiagramShell();
			diagramShell.diagramRenderer = diagramRenderer;
			diagramShell.editorStatefulClient = DiagramEditorStatefulClient(editorStatefulClient);
			diagramShell.registerTools([
				ScrollTool, SelectOnClickTool, InplaceEditorTool, ResizeTool, 
				DragToCreateRelationTool, DragTool/*, SelectOrDragToCreateElementTool*/, ZoomTool]);

			super.createChildren();
			
			var testButton:Button = new Button();
			resourceStatusBar.addChild(testButton);
			testButton.label = "Test/Rename class";
			
			var textInput:TextInput = new TextInput();
			resourceStatusBar.addChild(textInput);
			textInput.text = "MyTest";
			
			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
				DiagramEditorStatefulClient(editorStatefulClient).service_setInplaceEditorText(View(diagramShell.selectedItems.getItemAt(0)).id, textInput.text);
			});
			
			testButton = new Button();
			resourceStatusBar.addChild(testButton);
			testButton.label = "Test/Expand attributes";
			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
				DiagramEditorStatefulClient(editorStatefulClient).service_expandCompartment(View(diagramShell.selectedItems.getItemAt(0)).id);
			});
		}
		
		override public function executeContentUpdateLogic(content:Object, isFullContent:Boolean):void {
		}

		override public function disableEditing():void {
		}
		
		override public function enableEditing():void {
		}
	}
}