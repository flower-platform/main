package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramShell extends DiagramShell {
		
		public static const NONE:int = 0;
		public static const LEFT:int = -1;
		public static const RIGHT:int = 1;
		
		public static const HORIZONTAL_PADDING_DEFAULT:int = 20;
		public static const VERTICAL_PADDING_DEFAULT:int = 5;
		
		public var horizontalPadding:int = HORIZONTAL_PADDING_DEFAULT;
		public var verticalPadding:int = VERTICAL_PADDING_DEFAULT;
		
		public var diagramChildren:ArrayList = new ArrayList();
		
		public function MindMapDiagramShell() {
			super();			
		}
				
		override public function set rootModel(value:Object):void {
			super.rootModel = value;
			if (rootModel == null) {
				removeModelFromRootChildren(rootModel, true);
			} else {
				var model:Object = getControllerProvider(rootModel).getModelChildrenController(rootModel).getChildren(rootModel).getItemAt(0);
				addModelToRootChildren(model, true);				
			}			
			shouldRefreshVisualChildren(rootModel);			
		}
		
		public function removeModelFromRootChildren(model:Object, removeOnlyChildren:Boolean = false):void {
			var children:ArrayList = new ArrayList();
			children.addAll(getControllerProvider(rootModel).getModelChildrenController(rootModel).getChildren(rootModel));
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				if (model == IMindMapControllerProvider(getControllerProvider(child)).getMindMapModelController(child).getParent(child)) {
					removeModelFromRootChildren(child);			
				}
			}			
			if (!removeOnlyChildren) {						
				ArrayList(getControllerProvider(rootModel).getModelChildrenController(rootModel).getChildren(rootModel)).removeItem(model);	
//				shouldRefreshVisualChildren(rootModel);
			}			
		}
		
		public function addModelToRootChildren(model:Object, addOnlyChildren:Boolean = false):void {			
			if (model.expanded) {
				var children:IList = getControllerProvider(model).getModelChildrenController(model).getChildren(model);
				for (var i:int = 0; i < children.length; i++) {
					addModelToRootChildren(children.getItemAt(i));
				}				
			}
			if (!addOnlyChildren) {
				ArrayList(getControllerProvider(rootModel).getModelChildrenController(rootModel).getChildren(rootModel)).addItem(model);	
//				shouldRefreshVisualChildren(rootModel);
			}		
		}
		
		public function getModelController(model:Object):IMindMapModelController {
			return IMindMapControllerProvider(getControllerProvider(model)).getMindMapModelController(model);
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model);
		}
		
		private function getExpandedHeight(model:Object):Number {
			var expandedHeight:Number = getDynamicObject(model).expandedHeight;
			if (isNaN(expandedHeight)) {
				expandedHeight =getModelController(model).getHeight(model);
			}
			return expandedHeight;
		}
		
		private function getExpandedY(model:Object):Number {
			var expandedY:Number = getDynamicObject(model).expandedY;
			if (isNaN(expandedY)) {
				expandedY = model.y;
			}
			return expandedY;
		}
				
		public function refreshNodePositions(model:Object):void {		
			trace("refresh");
			var oldExpandedHeight:Number = getExpandedHeight(model);
			var oldExpandedHeightLeft:Number = getDynamicObject(model).expandedHeightLeft;			
			var oldExpandedHeightRight:Number = getDynamicObject(model).expandedHeightRight;
			
			calculateRootNodeExpandedHeight(model.side);
		
			var side:int = getModelController(model).getSide(model);
			if (side == NONE || side == LEFT) { 
				if (side == NONE) {
					getDynamicObject(model).expandedHeight = getDynamicObject(model).expandedHeightLeft;
					oldExpandedHeight = oldExpandedHeightLeft;
				}			
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), side == NONE ? LEFT : side);
			}
			if (side == NONE || side == RIGHT) { 
				if (side == NONE) {
					getDynamicObject(model).expandedHeight = getDynamicObject(model).expandedHeightRight;
					oldExpandedHeight = oldExpandedHeightRight;
				}			
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), side == NONE ? RIGHT : side);
			}
		}
		
		private function calculateRootNodeExpandedHeight(side:int):void {
			var model:Object = getControllerProvider(model).getModelChildrenController(rootModel).getChildren(rootModel).getItemAt(0);
			if (side == NONE || side == LEFT) { 
				calculateExpandedHeight(model, LEFT);
				getDynamicObject(model).expandedHeightLeft = getExpandedHeight(model);
			}
			if (side == NONE || side == RIGHT) { 
				calculateExpandedHeight(model, RIGHT);
				getDynamicObject(model).expandedHeightRight = getExpandedHeight(model);
			}
		}
		
		private function calculateExpandedHeight(model:Object, side:int):Number {			
			var expandedHeight:Number = 0;
			var children:IList = IMindMapModelChildrenController(getControllerProvider(model).getModelChildrenController(model)).getChildrenBasedOnSide(model, side);
			if (model.expanded && children.length > 0) {
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					expandedHeight += calculateExpandedHeight(child, side);
					if (i < children.length - 1) { // add padding only between children (not after the last one)
						expandedHeight += verticalPadding;
					}
				}				
			} else {
				expandedHeight = getModelController(model).getHeight(model);				
			}
			getDynamicObject(model).expandedHeight = expandedHeight;
			return expandedHeight;
		}
		
		private function changeCoordinates(model:Object, oldExpandedHeight:Number, newExpandedHeight:Number, side:int):void {			
			getDynamicObject(model).expandedY = getModelController(model).getY(model) - (newExpandedHeight - getModelController(model).getHeight(model))/2;
			
			changeChildrenCoordinates(model, side, true);				
			changeSiblingsCoordinates(model, (newExpandedHeight - oldExpandedHeight)/2, side);
		}		
		
		private function changeChildrenCoordinates(model:Object, side:int, changeOnlyForChildren:Boolean = false):void {	
			if (!changeOnlyForChildren) {	
				getModelController(model).setY(model, getExpandedY(model) + (getExpandedHeight(model) - getModelController(model).getHeight(model))/2);		
				var parent:Object = getModelController(model).getParent(model);
				if (getModelController(model).getSide(model) == LEFT) {
					getModelController(model).setX(model, getModelController(parent).getX(parent) - getModelController(model).getWidth(model) - horizontalPadding);	
				} else {					
					getModelController(model).setX(model, getModelController(parent).getX(parent) + getModelController(parent).getWidth(parent) + horizontalPadding);				
				}
			} else {
				getDynamicObject(model).expandedY = getModelController(model).getY(model) - (getExpandedHeight(model) - getModelController(model).getHeight(model))/2;		
			}
			if (getModelController(model).getExpanded(model)) {				
				var children:IList = IMindMapModelChildrenController(getControllerProvider(model).getModelChildrenController(model)).getChildrenBasedOnSide(model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (i == 0) {
						getDynamicObject(child).expandedY = getExpandedY(model);						
					} else {
						var previousChild:Object = children.getItemAt(i - 1);
						getDynamicObject(child).expandedY = getExpandedY(previousChild) + getExpandedHeight(previousChild) + verticalPadding;
					}					
					changeChildrenCoordinates(child, side);			
				}				
			}
		}
		
		private function changeSiblingsCoordinates(model:Object, diff:Number, side:int):void {
			var parent:Object = getModelController(model).getParent(model);
			if (parent != null) {
				var children:IList = IMindMapModelChildrenController(getControllerProvider(parent).getModelChildrenController(parent)).getChildrenBasedOnSide(parent, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (children.getItemIndex(model) > children.getItemIndex(child)) {				
						getModelController(child).setY(child, getModelController(child).getY(child) - diff);					
						changeSiblingChildrenCoordinates(child, -diff, side);
					} else if (children.getItemIndex(model) < children.getItemIndex(child)) {
						getModelController(child).setY(child, getModelController(child).getY(child) + diff);					
						changeSiblingChildrenCoordinates(child, diff, side);
					}
				}
				changeSiblingsCoordinates(parent, diff, side);
			}
		}
		
		private function changeSiblingChildrenCoordinates(model:Object, diff:Number, side:int):void {
			var children:IList = IMindMapModelChildrenController(getControllerProvider(model).getModelChildrenController(model)).getChildrenBasedOnSide(model, side);				
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				getModelController(child).setY(child, getModelController(child).getY(child) + diff);					
				changeSiblingChildrenCoordinates(child, diff, side);
			}
		}
		
	}
}