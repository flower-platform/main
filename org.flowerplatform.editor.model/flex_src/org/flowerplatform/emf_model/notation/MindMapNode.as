package org.flowerplatform.emf_model.notation {
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
		
	[RemoteClass]
	[Bindable]
	public class MindMapNode extends Node {
		
		public var expanded:Boolean;
		
		public var side:int;
		
		public var hasChildren:Boolean;
		
		private var _x:Number;	
		private var _y:Number;	
		private var _width:Number = 20;		
		private var _height:Number = 20;
		
		[Transient]		
		public function get x():Number {	
			if (isNaN(_x)) {
				if (ReferenceHolder(parentView_RH).referencedObject is Diagram) {
					_x = 0;
				} else {
					_x = MindMapNode(ReferenceHolder(parentView_RH).referencedObject).x +  MindMapNode(ReferenceHolder(parentView_RH).referencedObject).width;
				}
			}
			return _x;
		}
		
		public function setX(value:Number):void {
			var oldValue:Number = _x;
			_x = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "x", oldValue, _x));
		}
		
		[Transient]
		public function get y():Number {		
			if (isNaN(_y)) {
				if (ReferenceHolder(parentView_RH).referencedObject is Diagram) {
					_y = 0;
				} else {
					_y = MindMapNode(ReferenceHolder(parentView_RH).referencedObject).y;
				}
			}
			return _y;
		}
		
		public function setY(value:Number):void {
			var oldValue:Number = _y;
			_y = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "y", oldValue, _y));
		}
		
		[Transient]
		public function get width():Number {			
			return _width;
		}
		
		public function setWidth(value:Number):void {
			var oldValue:Number = _width;
			_width = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "width", oldValue, _width));
		}
		
		[Transient]
		public function get height():Number {			
			return _height;
		}
		
		public function setHeight(value:Number):void {
			var oldValue:Number = _height;
			_height = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "height", oldValue, _height));
		}
	}
}