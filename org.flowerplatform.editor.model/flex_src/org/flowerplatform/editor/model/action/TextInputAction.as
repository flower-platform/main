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
package org.flowerplatform.editor.model.action {
	
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.IMessageBox;
	
	import spark.components.TextArea;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class TextInputAction extends DiagramShellAwareActionBase {
		
		public function TextInputAction() {
			super();
		}
		
		protected function askForTextInput(defaultText:String, title:String, button:String, handler:Function):IMessageBox {
			var textArea:Object;
			var name:String = defaultText;
			var messageBox:Object = FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(title)
				.setText(name)
				.setWidth(200)
				.setHeight(100)
				.setSelectText(true)
				.addButton(button, function(evt:MouseEvent = null):void {
					if (textArea != null) {
						name = textArea.text;
					}
					handler(name);
				})
				.addButton("Cancel");
			if (messageBox.hasOwnProperty("textArea")) {
				textArea = messageBox.textArea;
				TextArea(textArea).editable = true;
			}
			IMessageBox(messageBox).showMessageBox();
			return IMessageBox(messageBox);
		}
	}
}