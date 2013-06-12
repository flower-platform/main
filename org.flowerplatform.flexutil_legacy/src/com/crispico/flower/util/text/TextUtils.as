package com.crispico.flower.util.text
{
	import flash.events.FocusEvent;
	
	import mx.controls.TextArea;
	import mx.controls.TextInput;
	import mx.core.UIComponent;
	import mx.core.UITextField;
	import mx.utils.StringUtil;

	public class TextUtils {
		
		/**
		 * Given a text component, this method will add behavior to it so that when the component doesn't have focus
		 * it will show a special hint message for the user.
		 * <p>
		 * The component may be a TextArea, TextInput, UITextField. If the component is not recognized, the <code>textProperty</code>
		 * parameter is used to set and get the <code>hint</code>. It is though advised to update the implementation of this method.
		 * 
		 * @author Sorin
		 */ 
		public static function setTextComponentHint(uiComponent:UIComponent, hint:String, textProperty:String = null):void {
			if (textProperty == null) {
				if (uiComponent is TextArea || uiComponent is TextInput || UITextField) 
					textProperty = "text";
			}
			
			if (textProperty == null)
				throw "Impossible to add hint to " + uiComponent + " because textProperty was not passed or could not be determined";
			
			var focusFunction:Function = 
				function(event:FocusEvent):void {
					var currentText:String = uiComponent[textProperty] as String;
					if (event.type == FocusEvent.FOCUS_OUT && StringUtil.trim(currentText).length == 0) {
						uiComponent[textProperty] = hint;
						uiComponent.setStyle("color", 0x999999);
					} else if (event.type == FocusEvent.FOCUS_IN && currentText.replace(/\r|\n|\r\n/g, "") == hint.replace(/\r|\n|\r\n/g, "")) { // without \r or \n characters because the textarea component may choose to modify the hint string
						uiComponent[textProperty] = "";
						uiComponent.setStyle("color", 0x000000);
					}
				}
			
			focusFunction(new FocusEvent(FocusEvent.FOCUS_OUT)); // Run at least once to set the hint at the beginning
			uiComponent.addEventListener(FocusEvent.FOCUS_OUT, focusFunction);
			uiComponent.addEventListener(FocusEvent.FOCUS_IN, focusFunction);
		}
	}
}