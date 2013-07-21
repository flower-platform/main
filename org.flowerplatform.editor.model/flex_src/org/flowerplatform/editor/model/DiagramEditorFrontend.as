package org.flowerplatform.editor.model {
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
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
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.Button;
	import spark.components.Label;
	import spark.components.TextInput;
	
	public class DiagramEditorFrontend extends EditorFrontend implements IPopupContent, IFocusManagerComponent {
	
		public var diagramShell:DiagramShell;
		
		protected var _popupHost:IPopupHost;
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:CollectionEvent):void {
			if (popupHost) {
				popupHost.refreshActions(this);
			}
		}
		
		protected function getDiagramShellInstance():DiagramShell {
			throw new Error("This should be implemented by subclasses!");
		}
				
		override protected function createChildren():void {
			var scroller:InfiniteScroller = new InfiniteScroller();
			editor = scroller;
			
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
			scroller.viewport = diagramRenderer;
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
			
			diagramShell = getDiagramShellInstance();
			diagramShell.diagramRenderer = diagramRenderer;
						
			super.createChildren();			
		}
		
		override public function executeContentUpdateLogic(content:Object, isFullContent:Boolean):void {
		}

		override public function disableEditing():void {
		}
		
		override public function enableEditing():void {
		}
		
		public function getActions(selection:IList):Vector.<IAction>{			
			return null;
		}
		
		public function getSelection():IList	{		
			return diagramShell.selectedItems;
		}
		
		public function get popupHost():IPopupHost {
			return _popupHost;
		}
		
		public function set popupHost(value:IPopupHost):void {
			_popupHost = value;
		}
		
		override protected function focusInHandler(event:FocusEvent):void {
			super.focusInHandler(event);
			popupHost.activePopupContent = this;
		}
		
	}
}
