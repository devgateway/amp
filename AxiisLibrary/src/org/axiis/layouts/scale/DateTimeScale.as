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
	 * A scale that can be used to convert Dates to layout space.
	 */
	public class DateTimeScale extends ContinuousScale implements IScale
	{
		override public function validate() : void
		{
			super.validate();
			if(dataProvider != null)
				_computedAverage = new Date(computedSum.valueOf() / dataProvider.length);
			else
				_computedAverage = null;
		}
		
		override protected function computeSum():*
		{
			var sum:Number = 0;
			for each(var o:* in collection)
			{
				var currValue:Number = getProperty(o,dataField).valueOf();
				sum += currValue;
			}
			return new Date(sum);
		}
		
		/**
		 * @inheritDoc IScale#valueToLayout
		 */
		public function valueToLayout(value:*, invert:Boolean=false, clamp:Boolean = false):*
		{
			if(!(value is Date))
				throw new Error("To use valueToLayout the value parameter must be a Date");
			
			if(invalidated)
				validate();
			
			//trace(minVal)
			var minDateMilli:Number = (minValue as Date).valueOf();
			var maxDateMilli:Number = (maxValue as Date).valueOf();
			var valueMilli:Number = (value as Date).valueOf();
			var percent:Number = ScaleUtils.inverseLerp(valueMilli,minDateMilli,maxDateMilli);
			if(invert)
				percent = 1 - percent;
			if(clamp)
				percent = Math.max(0,Math.min(1,percent));
			var toReturn:Number = ScaleUtils.lerp(percent,minLayout,maxLayout);
			return toReturn;
		}
		
		/**
		 * @inheritDoc IScale#layoutToValue
		 */
		public function layoutToValue(layout:*, invert:Boolean = false, clamp:Boolean = false):*
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
			return new Date(toReturn);
		}
	}
}