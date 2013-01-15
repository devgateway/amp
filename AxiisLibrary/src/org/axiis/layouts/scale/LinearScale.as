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
	/**
	 * A scale that deals with linear numeric data.
	 */
	public class LinearScale extends ContinuousScale implements IScale
	{
		public function LinearScale()
		{
			super();
		}
		
		/**
		 * @inheritDoc IScale#valueToLayout
		 */
		public function valueToLayout(value:*, invert:Boolean=false,clamp:Boolean = true):*
		{
			if(invalidated)
				validate();
				
			var percent:Number = ScaleUtils.inverseLerp(value,minValue,maxValue);
			if(invert)
				percent = 1 - percent;
			if(clamp)
				percent = Math.max(0,Math.min(1,percent));
				
			var toReturn:Number = ScaleUtils.lerp(percent,minLayout,maxLayout);
			
		//	trace("value = " + value + " is percent=" + percent + " of max=" + maxValue + " - min=" + minValue + " to return=" + toReturn);
			
			return toReturn;
		}
		
		/**
		 * @inheritDoc IScale#layoutToValue
		 */
		public function layoutToValue(layout:*,invert:Boolean = false,clamp:Boolean = true):*
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
			var toReturn:Number = ScaleUtils.lerp(percent,minValue,maxValue);
			
			return toReturn;
			
		}
	}
}