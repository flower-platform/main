package com.crispico.flower.flexdiagram.infinitegroup.scroller {
	
	import com.crispico.flower.flexdiagram.infinitegroup.InfiniteGroup;
	
	import mx.core.mx_internal;
	
	import spark.components.HScrollBar;

	use namespace mx_internal;
	
	public class InfiniteHScrollBar extends HScrollBar {
		
		override public function get minimum():Number {
			if (viewport == null) {
				return super.minimum;
			}
			return InfiniteGroup(viewport).horizontalOffset;
		}
		
		override public function changeValueByStep(increase:Boolean = true):void {
			if (stepSize == 0)
				return;
			
			if (!increase && InfiniteGroup(viewport).minContentX == value) {
				return;
			}
			
			var newValue:Number = (increase) ? value + stepSize : value - stepSize;
			calculateMinimum(newValue, increase ? 1 : -1);
			setValue(nearestValidValue(newValue, snapInterval));
		}
		
		override protected function nearestValidValue(value:Number, interval:Number):Number {
			if (interval == 0)
				return Math.max(minimum, Math.min(maximum, value));
			
			var maxValue:Number = maximum - minimum;
			var scale:Number = 1;
			
			value -= minimum;
			
			// If interval isn't an integer, there's a possibility that the floating point 
			// approximation of value or value/interval will be slightly larger or smaller 
			// than the real value.  This can lead to errors in calculations like 
			// floor(value/interval)*interval, which one might expect to just equal value, 
			// when value is an exact multiple of interval.  Not so if value=0.58 and 
			// interval=0.01, in that case the calculation yields 0.57!  To avoid problems, 
			// we scale by the implicit precision of the interval and then round.  For 
			// example if interval=0.01, then we scale by 100.    
			
			if (interval != Math.round(interval)) 
			{ 
				const parts:Array = (new String(1 + interval)).split("."); 
				scale = Math.pow(10, parts[1].length);
				maxValue *= scale;
				value = Math.round(value * scale);
				interval = Math.round(interval * scale);
			}   
			
			var lower:Number = Math.max(InfiniteGroup(viewport).horizontalOffset, Math.floor(value / interval) * interval);
			var upper:Number = Math.min(maxValue, Math.floor((value + interval) / interval) * interval);
			var validValue:Number = ((value - lower) >= ((upper - lower) / 2)) ? upper : lower;
			
			return (validValue / scale) + minimum;
		}
				
		private function calculateMinimum(value:Number, steps:Number):void { 
			if (steps < 0) {
				if (value <= 0) {
					InfiniteGroup(viewport).horizontalOffset += steps;
				} else {
					InfiniteGroup(viewport).horizontalOffset = 0;
				}
			} else {
				if (value <= 0) {
					InfiniteGroup(viewport).horizontalOffset -= steps;
				} else {
					InfiniteGroup(viewport).horizontalOffset = 0;
				}
			}
		}
	}
}