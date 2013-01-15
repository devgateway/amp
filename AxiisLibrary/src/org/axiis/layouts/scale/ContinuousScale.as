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
	
	import org.axiis.utils.ObjectUtils;

	/**
	 * The base class for scales that deal with numerical and date data.
	 */
	public class ContinuousScale extends AbstractScale
	{
		// These are untyped because we need to support average of Numbers and Dates
		[Bindable("computedAverageChange")]
		/**
		 * The average value in the dataProvider.
		 */
		public function get computedAverage():*
		{
			if(invalidated)
				validate();
			return __computedAverage;
		}
		/**
		 * @private
		 */
		protected function set _computedAverage(value:*):void
		{
			if(value != __computedAverage)
			{
				__computedAverage = value;
				dispatchEvent(new Event("computedAverageChange"));
			}
		}
		private var __computedAverage:*;
		
		[Bindable("computedSumChange")]
		/**
		 * The sum of the value in the dataProvider.
		 */
		public function get computedSum():*
		{
			if(invalidated)
				validate();
			return __computedSum;
		}
		/**
		 * @private
		 */
		protected function set _computedSum(value:*):void
		{
			if(value != __computedSum)
			{
				__computedSum = value;
				dispatchEvent(new Event("computedSumChange"));
			}
		}
		private var __computedSum:*;
		
		/**
		 * @inheritDoc IScale#validate  
		 */
		override public function validate():void
		{
			super.validate();
			_computedMinValue = computeMin();
			_computedMaxValue = computeMax();
			_computedSum = computeSum();
			if(dataProvider)
				_computedAverage = computedSum / dataProvider.length;
			else
				_computedAverage = NaN;
		}
		
		/**
		 * Returns the minimum value from the dataProvider. 
		 */
		protected function computeMin():*
		{
			if(collection == null || collection.length == 0)
				return NaN;
			
			var newMin:*;
			for each(var o:* in collection)
			{
				var currValue:* = getProperty(o,dataField);
			//	trace("currvalue=" + currValue);
				if (! isNaN(currValue)) {
					if(newMin == null)
						newMin = currValue;
					else
						newMin = currValue < newMin ? currValue : newMin;
				}
			}
			return newMin;
		}
		
		/**
		 * Returns the maximum value from the dataProvider. 
		 */
		protected function computeMax():*
		{
			if(collection == null || collection.length == 0)
				return NaN;
			
			var newMax:*;
			for each(var o:* in collection)
			{
				var currValue:* = getProperty(o,dataField);
				if (! isNaN(currValue)) {
					if(newMax == null)
						newMax = currValue;
					else
						newMax = currValue > newMax ? currValue : newMax;
				}
			}
			return newMax;
		}
		
		/**
		 * Returns the sum of the items in the dataProvider.
		 */
		protected function computeSum():*
		{
			if(collection == null || collection.length == 0)
				return NaN;
				
			var sum:Number = 0;
			for each(var o:* in collection)
			{
				var currValue:Number = getProperty(o,dataField);
				sum += currValue;
			}
			return sum;
		}
		
		/**
		 * @private
		 */
		protected function getProperty(obj:Object, propertyName:Object):*
		{ 
			return ObjectUtils.getProperty(this,obj,propertyName);
		}
	}
}