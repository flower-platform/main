package org.flowerplatform.flexutil.popup {
	import mx.collections.IList;
	import mx.messaging.AbstractConsumer;

	public class ActionBase implements IAction {

		private var _id:String;
		private var _parentId:String;
		private var _orderIndex:int;
		private var _preferShowOnActionBar:Boolean;
		private var _visible:Boolean = true;
		private var _enabled:Boolean = true;
		private var _label:String;
		private var _icon:Object;
		private var _selection:IList;
		
		public function get id():String
		{
			return _id;
		}
		
		public function set id(value:String):void
		{
			_id = value;
		}
		
		public function get parentId():String
		{
			return _parentId;
		}
		
		public function set parentId(value:String):void
		{
			_parentId = value;
		}
	
		public function get orderIndex():int
		{
			return _orderIndex;
		}

		public function set orderIndex(value:int):void
		{
			_orderIndex = value;
		}

		public function get preferShowOnActionBar():Boolean
		{
			return _preferShowOnActionBar;
		}

		public function set preferShowOnActionBar(value:Boolean):void
		{
			_preferShowOnActionBar = value;
		}

		public function get visible():Boolean
		{
			return _visible;
		}

		public function set visible(value:Boolean):void
		{
			_visible = value;
		}

		public function get enabled():Boolean
		{
			return _enabled;
		}

		public function set enabled(value:Boolean):void
		{
			_enabled = value;
		}

		public function get label():String
		{
			return _label;
		}

		public function set label(value:String):void
		{
			_label = value;
		}

		public function get icon():Object
		{
			return _icon;
		}

		public function set icon(value:Object):void
		{
			_icon = value;
		}
		
		public function get selection():IList
		{
			return _selection;
		}
		
		public function set selection(value:IList):void
		{
			_selection = value;
		}
		
		public function run():void {
		}
		
	}
}