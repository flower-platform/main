package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
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
		
		public function MindMapDiagramShell() {
			super();			
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
			var model:Object = ParentAwareArrayList(rootModel).getItemAt(0);
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
			var children:ArrayList = getModelController(model).getChildrenBasedOnSide(model, side);
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
			
			changeChildrenCoordinates(model, side, getModelController(model).getParent(model) == null);				
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
				var children:ArrayList = getModelController(model).getChildrenBasedOnSide(model, side);				
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
				var children:ArrayList = getModelController(parent).getChildrenBasedOnSide(parent, side);				
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
			var children:ArrayList = getModelController(model).getChildrenBasedOnSide(model, side);				
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				getModelController(child).setY(child, getModelController(child).getY(child) + diff);					
				changeSiblingChildrenCoordinates(child, diff, side);
			}
		}
		
	}
}