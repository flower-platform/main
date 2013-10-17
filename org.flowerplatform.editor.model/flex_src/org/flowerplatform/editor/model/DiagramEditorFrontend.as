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
package org.flowerplatform.editor.model {
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.containers.HBox;
	import mx.containers.HDividedBox;
	import mx.events.CollectionEvent;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.properties.PropertiesPlugin;

	public class DiagramEditorFrontend extends EditorFrontend implements IViewContent, IFocusManagerComponent, ISelectionProvider, ISelectionForServerProvider {
	
		public var diagramShell:DiagramShell;
		
		protected var _viewHost:IViewHost;
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:Event):void {
			FlexUtilGlobals.getInstance().selectionManager.selectionChanged(viewHost, this);
		}
		
		protected function getDiagramShellInstance():DiagramShell {
			throw new Error("This should be implemented by subclasses!");
		}
				
		override protected function createChildren():void {
			var diagramContainer:HDividedBox = new HDividedBox();
			
			var scroller:InfiniteScroller = new InfiniteScroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			diagramContainer.addChild(scroller);
			editor = diagramContainer;
			
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
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
		}
		
		public function convertSelectionToSelectionForServer(selection:IList):IList {
			if (selection == null) 
				return selection;
			
			var selectedItems:ArrayCollection = new ArrayCollection();
			for (var i:int = 0; i < selection.length; i++) {
				var node:Node = Node(selection.getItemAt(i));//.id / 
				var diagramEditableResourcePath:String = NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).editableResourcePath;
				var xmiID:String = node.idAsString;
				var serviceID:String = NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).getStatefulServiceId();
				var diagramViewType:String = node.viewType;
				
				selectedItems.addItem(new DiagramSelectedItem(xmiID, diagramEditableResourcePath, serviceID, diagramViewType));
			}			
			
			return selectedItems;
		}
	}
}
