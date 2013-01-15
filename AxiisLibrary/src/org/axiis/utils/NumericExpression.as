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

package org.axiis.utils
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	/**
	 * Dispatched when the value property has changed.
	 */
	[Event(name="valueChanged")]
	
	/**
	 * NumericExpression provides a convenient place to store the results of
	 * numeric calculations used in multiple places within an MXML document.
	 * By binding the results of a complex numeric computation to a
	 * NumericExpression's value property, you allow other objects to bind
	 * on that value without the overhead of re-running the original
	 * computation. 
	 * 
	 * <p>
	 * For example, this markup will populate two labels with the text
	 * "1.57079633" but <code>Math.PI / 2</code> will only have been evaluated
	 * once.
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * &lt;axiis:NumericExpression id="ne0" value="{Math.PI / 2}" /&gt;
	 * &lt;mx:Label value="{ne0.expression}" /&gt;
	 * &lt;mx:Label value="{ne0.expression}" /&gt;
	 * </code>
	 * </pre> 
	 */
	public class NumericExpression extends EventDispatcher
	{
		/**
		 * Constructor.
		 */
		public function NumericExpression()
		{
			super();
		}

		[Bindable(event="valueChanged")]
		/**
		 * The stored Number. By using binding to set this property and
		 * then binding other properties to it, you can reduce needless
		 * computations.
		 */
		public function get value():Number
		{
			return _value;
		}
		public function set value(num:Number):void
		{
			//if (_value != num)
			{
				_value = num;
				dispatchEvent(new Event("valueChanged"));
			}
		}
		private var _value:Number;
	}
}