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
package org.flowerplatform.properties.property_renderer {
	import com.flextras.mobile.dropDownList.DropDownList;
	
	import flash.events.FocusEvent;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.properties.remote.Property;
	
	import spark.components.DropDownList;
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DropDownListPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var dropDownList:spark.components.DropDownList;
		
		/**
		 * Signature: function getDataProviderHandler(callbackObject:Object, callbackFunction:Function):void		 
		 */ 
		public var requestDataProviderHandler:Function;
		
		/**
		 * Signature: function myLabelFunction(item:Object):String
		 */ 
		public var labelFunction:Function;
		
		/**
		 * Signature: function getItemIndexFromList(item:Object, list:ArrayCollection):int
		 */ 
		public var getItemIndexFromList:Function;
		
		public function DropDownListPropertyRenderer() {
			super();
		}
		
		override protected function createChildren():void {			
			super.createChildren();
			
			if (FlexUtilGlobals.getInstance().isMobile) {
				dropDownList = new com.flextras.mobile.dropDownList.DropDownList();										
			} else {
				dropDownList = new spark.components.DropDownList();											
			}
						
			dropDownList.percentWidth = 100;
			dropDownList.percentHeight = 100;		
			dropDownList.labelFunction = labelFunction;
			
			//get data to fill dropDownList
			requestDataProviderHandler(this, requestDataProviderCallbackHandler);
			
			addElement(dropDownList);			
		}
		
		override public function set data(value:Object):void {
			super.data = value;			
			dropDownList.enabled = !Property(data).readOnly;
			
			if (!data.readOnly) {				
				handleListeningOnEvent(IndexChangeEvent.CHANGE, this, dropDownList);
			}			
		}
		
		private function requestDataProviderCallbackHandler(result:ArrayCollection):void {
			dropDownList.dataProvider = result;		
			if (result.length > 0) {
				dropDownList.selectedIndex = getItemIndexFromList(Property(data).value, result);
			}
		}
		
		override protected function getValue():Object {
			return dropDownList.selectedItem;	
		}
		
	}
}