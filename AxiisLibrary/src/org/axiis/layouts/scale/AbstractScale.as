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
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.events.CollectionEvent;

	/**
	 * Dispatched when the data in the becomes invalid and the new min, max, sum, and average need to be computed.
	 */
	[Event(name="invalidate",type="flash.events.Event")]
	
	/**
	 * An abstract base class that scales can extend. It provides stubs for
	 * methods defined in IScale, and sets up the necessary getters and setters
	 * to allow a user to overide minimums and maximums values. A user can set
	 * the minValue using the public setter "minValue". The protected setter
	 * "computedMinValue" allows a subclass to set the value that it determines
	 * for the minimum based only on the data. The "minValue" getter
	 * will return the user specified value if it exists. Otherwise it returns
	 * the computed value. The analogous procedure is used for maxValue.
	 */
	public class AbstractScale extends EventDispatcher
	{
		/**
		 * Indicates that the scale should recalculate it's minimum and maximum
		 * values and layouts the next time either layoutToValue or valueToLayout
		 * is called. 
		 */
		public function get invalidated():Boolean
		{
			return _invalidated;
		}
		private var _invalidated:Boolean = false;
		 
		[Bindable(event="dataProviderChange")]		
		/**
		 * @copy IScale#dataProvider
		 */
		public function get dataProvider():Object
		{
			return rawDataProvider;
		}
		public function set dataProvider(value:Object):void
		{
			if(value != rawDataProvider)
			{
				rawDataProvider = value;
				
				if(collection)
					collection.removeEventListener(CollectionEvent.COLLECTION_CHANGE,onCollectionChange)
					
				collection = new ArrayCollection();
				if(value == null)
				{
					collection.source = [];
				}
				else if(value is Array)
				{
					collection.source = (value as Array);
				}
				else if(value is ArrayCollection)
				{
					collection.source = ArrayCollection(value).toArray();
				}
				
				collection.addEventListener(CollectionEvent.COLLECTION_CHANGE,onCollectionChange);
				invalidate();
				
				dispatchEvent(new Event("dataProviderChange"));
			}
		}
		private var rawDataProvider:Object;
		
		protected var collection:ArrayCollection;
		
		[Bindable(event="dataFieldChange")]
		/**
		 * @copy IScale#dataField
		 */
		public function get dataField():String
		{
			return _dataField;
		}
		public function set dataField(value:String):void
		{
			if(value != _dataField)
			{
				_dataField = value;
				invalidate();
				dispatchEvent(new Event("dataFieldChange"));
			}
		}
		private var _dataField:String;
		
		//---------------------------------------------------------------------
		// minValue
		//---------------------------------------------------------------------
		
		[Bindable(event="minValueChange")]
		/**
		 * @copy IScale#minValue
		 */
		public function get minValue():*
		{
			return userMinValue == null ? computedMinValue : userMinValue;
		}
		public function set minValue(value:*):void
		{
			if(value != userMinValue)
			{
				userMinValue = value;
				dispatchEvent(new Event("minValueChange"));
			}
		}
		
		[Bindable(event="computedMinValueChange")]
		/**
		 * The minimum value in the dataProvider.
		 */
		public function get computedMinValue():*
		{
			if(invalidated)
				validate();
			return __computedMinValue;
		}
		protected function set _computedMinValue(value:*):void
		{
			if(value != __computedMinValue)
			{
				__computedMinValue = value;
				dispatchEvent(new Event("computedMinValueChange"));
			}
		}
		private var __computedMinValue:*;
		
		/**
		 * The minimum value as specified by the user.
		 */
		protected var userMinValue:*;
		
		//---------------------------------------------------------------------
		// maxValue
		//---------------------------------------------------------------------
		
		[Bindable(event="maxValueChange")]
		/**
		 * @copy IScale#maxValue
		 */
		public function get maxValue():*
		{
			return userMaxValue == null ? computedMaxValue : userMaxValue;
		}
		public function set maxValue(value:*):void
		{
			if(value != userMaxValue)
			{
				userMaxValue = value;
				dispatchEvent(new Event("maxValueChange"));
			}
		}
		
		[Bindable("computedMaxValueChange")]
		/**
		 * The maximum value in the dataProvider.
		 */
		public function get computedMaxValue():*
		{
			if(invalidated)
				validate();
			return __computedMaxValue;
		}
		protected function set _computedMaxValue(value:*):void
		{
			if(value != __computedMaxValue)
			{
				__computedMaxValue = value;
				dispatchEvent(new Event("computedMaxValueChange"));
			}
		}
		private var __computedMaxValue:*;
		
		/**
		 * The maximum value as specified by the user.
		 */
		protected var userMaxValue:*;
		
		//---------------------------------------------------------------------
		
		[Bindable]
		/**
		 * @copy IScale#minLayout
		 */
		public function get minLayout():Number
		{
			return _minLayout;
		}
		public function set minLayout(value:Number):void
		{
			if(value != _minLayout)
			{
				_minLayout = value;
				dispatchEvent(new Event("minLayoutChange"));
			}
		}
		private var _minLayout:Number = NaN;
		
		[Bindable(event="maxLayoutChange")]
		/**
		 * @copy IScale#maxLayout
		 */
		public function get maxLayout():Number
		{
			return _maxLayout;
		}
		public function set maxLayout(value:Number):void
		{
			if(value != _maxLayout)
			{
				_maxLayout = value;				
				dispatchEvent(new Event("maxLayoutChange"));
			}
		}
		private var _maxLayout:Number = NaN;
		
		/**
		 * Marks this IScale as needing its minValue and maxValue recomputed.
		 */
		public function invalidate():void
		{
			_invalidated = true;
			dispatchEvent(new Event("invalidate"));
		}
		
		/**
		 * Initiates the computation of minValue and maxValue.
		 */
		public function validate():void
		{
			_invalidated = false;
		}
		
		/**
		 * @private
		 */
		protected function onCollectionChange(event:CollectionEvent):void
		{
			invalidate();
		}	
	}
}