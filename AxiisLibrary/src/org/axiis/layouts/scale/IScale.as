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
	
	
	[Bindable]
	/**
	 * IScale is an interface the defines the methods required to translate
	 * between a range of values and screen (layout) coordinate space.
	 */
	public interface IScale
	{
		/**
		 * The DataSet, ArrayCollection, Array, XML, etc. that is used to
		 * determine the range of this scale.
		 */
		function get dataProvider():Object;
		function set dataProvider(value:Object):void;
		
		/**
		 * The name of the property of the Objects in the dataProvider
		 * that should be used when computing min, max, sum, and average values.
		 * If this property is not set, the implementing class should
		 * use the Objects themselves.
		 */
		function get dataField():String;
		function set dataField(value:String):void;
		
		/**
		 * The minimum value allowed in this scale. If this property is
		 * not set, the implementer should compute an appropriate minimum
		 * by analyzing the contents of the dataProvider.
		 */
		function get minValue():*;
		function set minValue(value:*):void;
		
		/**
		 * The maximum value allowed in this scale. If this property is
		 * not set, the implementer should compute an appropriate maximum
		 * by analyzing the contents of the dataProvider.
		 */
		function get maxValue():*;
		function set maxValue(value:*):void;
		
		/**
		 * The minimum layout position.
		 */
		function get minLayout():Number;
		function set minLayout(value:Number):void;
		
		/**
		 * The maximum layout position.
		 */
		function get maxLayout():Number;
		function set maxLayout(value:Number):void;
		
		/**
		 * Converts a value to a position in layout space.
		 * 
		 * @param value The value to be converted into layout space.
		 * @param invert Whether the minValue translates to minLayout (false) or to maxLayout (true). 
		 */
		function valueToLayout(value:*, invert:Boolean=false, clamp:Boolean = false):*;
		
		/**
		 * Converts a layout position to a value that would arise in the
		 * space defined by the implementing class. For example, LinearScale
		 * converts a layout position to a Number between minValue and
		 * maxValue, whether or not that Number is actually present within
		 * the dataProvider. CategoricalScale, on the other hand, will only
		 * convert layout positions to values found within the dataProvider.
		 * 
		 * @param The layout position to translate into a value.
		 */
		function layoutToValue(layout:*,invert:Boolean = false,clamp:Boolean = false):*;
		
		/**
		 * Initiates the computation of minValue and maxValue.
		 */
		function validate():void;
		
		/**
		 * Marks this IScale as needing its minValue and maxValue recomputed.
		 */
		function invalidate():void;
	}
}