///////////////////////////////////////////////////////////////////////////////
//	Copyright (c) 2009 Team Axiis
//
//	Permission is hereby granted, free of charge, to any person
//	obtaining a copy of this software and associated documentation
//	files (the "Software"), to deal in the Software without
//	restriction, including without limitation the rights to use,
//	copy, modify, merge, publish, distribute, sublicense, and/or sell
//	copies of the Software, and to permit persons to whom the
//	Software is furnished to do so, subject to the following
//	conditions:
//
//	The above copyright notice and this permission notice shall be
//	included in all copies or substantial portions of the Software.
//
//	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//	OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//	HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//	WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//	OTHER DEALINGS IN THE SOFTWARE.
///////////////////////////////////////////////////////////////////////////////

package org.axiis.layouts.scale
{
	import flash.events.Event;

	/**
	 * A scale that converts logarithmic data to layout space.
	 */
	public class LogScale extends ContinuousScale implements IScale
	{
		public function LogScale()
		{
			super();
			base = 10;
		}
		
		private var logOfBase:Number;
		
		[Bindable(event="baseChange")]
		/**
		 * The base of the logarithm used in the scale
		 */
		public function get base():Number
		{
			return _base;
		}
		public function set base(value:Number):void
		{
			if(value != _base)
			{
				_base = value;
				logOfBase = Math.log(_base);
				invalidate();
				dispatchEvent(new Event("baseChange"));
			}
		}
		private var _base:Number;
		
		/**
		 * @inheritDoc IScale#valueToLayout
		 */
		public function valueToLayout(value:*,invert:Boolean=false,clamp:Boolean = false):*
		{
			//Commented out 2 lines below as some of the layouts call this at component initialization when they do not have valid values
			//Tg 10/1/09
			
			//if(!(value is Number))
			//	throw new Error("To use valueToLayout the value parameter must be a Number");
				
			if(invalidated)
				validate();
			
			// We offset the min, max, and value so the new minimum is at 1 and the scale factor remains the same.
			var logMinValue:Number = Math.log(1) / logOfBase;
			var logMaxValue:Number = Math.log(maxValue - minValue + 1) / logOfBase;
			var logValue:Number = Math.log(value - minValue + 1) / logOfBase;
			
			var percent:Number = ScaleUtils.inverseLerp(logValue,logMinValue,logMaxValue);
			if(invert)
				percent = 1 - percent;
			if(clamp)
				percent = Math.max(0,Math.min(1,percent));
			var toReturn:Number = ScaleUtils.lerp(percent,minLayout,maxLayout);
			
			//trace("base =",base,"logOfBase =",logOfBase,"value =",value,"logValue =",logValue,"% =",percent,"toReturn =",toReturn);
			return toReturn;
		}
		
		/**
		 * @inheritDoc IScale#layoutToValue
		 */
		public function layoutToValue(layout:*,invert:Boolean = false,clamp:Boolean = false):*
		{
			if(!(layout is Number))
				throw new Error("To use layoutToValue the layout parameter must be a Number");

			if (invalidated)
				validate();
				
			var percent:Number = ScaleUtils.inverseLerp(layout,minLayout,maxLayout);
			if(invert)
				percent = 1 - percent;
			if(clamp)
				percent = Math.max(0,Math.min(1,percent));
			percent = Math.pow(percent,base);
			var toReturn:Number = ScaleUtils.lerp(percent,minValue,maxValue);
			return toReturn;
		}
	}
}