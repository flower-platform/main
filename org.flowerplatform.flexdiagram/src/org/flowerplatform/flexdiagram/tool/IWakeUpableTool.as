package org.flowerplatform.flexdiagram.tool {
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IWakeUpableTool {	
		
		function wakeUp(eventType:String, ctrlPressed:Boolean, shiftPressed:Boolean):Boolean;		
	
	}
}