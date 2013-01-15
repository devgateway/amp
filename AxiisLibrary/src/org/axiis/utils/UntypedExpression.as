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
	 * UntypedExpression provides a convenient place to store the results of
	 * calculations used in multiple places within an MXML document.
	 * By binding the results of a complex expression to an
	 * UntypedExpression's value property, you allow other objects to bind
	 * on that value without the overhead of re-running the original
	 * expression.
	 */
	public class UntypedExpression extends EventDispatcher
	{
		/**
		 * Constructor.
		 */
		public function UntypedExpression()
		{
			super();
		}
		
		[Bindable(event="valueChange")]
		/**
		 * The stored Boolean value. By using binding to set this property and
		 * then binding other properties to it, you can reduce needless
		 * computations.
		 */
		public function get value():*
		{
			return _value;
		}
		public function set value(value:*):void
		{
			if (value != _value)
			{
				_value = value;
				dispatchEvent (new Event("valueChange"));
			}
		}
		private var _value:*;

	}
}