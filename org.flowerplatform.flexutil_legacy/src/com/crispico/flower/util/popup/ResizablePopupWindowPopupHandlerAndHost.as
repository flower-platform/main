package com.crispico.flower.util.popup {
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	public class ResizablePopupWindowPopupHandlerAndHost extends ResizablePopupWindow implements IPopupHandler, IPopupHost	{
		
		protected var popupContent:IPopupContent;
		
		public function ResizablePopupWindowPopupHandlerAndHost() {
			super();
		}
		
		public function setHeight(value:int):IPopupHandler	{
			height = value;
			return this;
		}
		
		public function setTitle(value:String):IPopupHandler {
			title = value;
			return this;
		}
		
		public function setPopupContent(value:IPopupContent):IPopupHandler {
			popupContent = value;
			popupContent.percentHeight = 100;
			popupContent.percentWidth = 100;
			return this;
		}
		
		public function get activePopupContent():IPopupContent {
			return popupContent;
		}
		
		public function setWidth(value:int):IPopupHandler {
			width = value;
			return this;
		}
		
		public function show(modal:Boolean = true):void {
			IPopupContent(popupContent).popupHost = this;
			addElement(popupContent);
			showPopup(NaN, NaN, null, modal);
		}
		
		/**
		 * In this case, this class, ResizablePopupWindow is not used any more.
		 * The existing ModalSpinner mechanism is used for displaying the popup content.
		 */
		public function showModalOverAllApplication():void {
			var modalSpinner:ModalSpinner = new ModalSpinnerPopupHost(popupContent);
			ModalSpinner.addGlobalModalSpinner(null, modalSpinner);
		}
		
		public function refreshActions(popupContent:IPopupContent):void {
			// not supported			
		}
		
		public function setIcon(value:Object):void {
			titleIconURL = String(value);
		}
		
		public function setLabel(value:String):void {
			title = value;
		}
		
	}
}