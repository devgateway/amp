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

package org.axiis.core
{
	/**
	 * @private
	 */
	public class PropertySetter
	{
		public function PropertySetter(target:Object = null,property:Object = null,value:Object = null)
		{
			this.target = target;
			this.property = property;
			this.value = value;
		}
		
		public var target:Object;
		
		public var property:Object;
		
		
		[Bindable(event="valueChange")]
		public function get value():Object
		{
			return _value;
		}
		public function set value(value:Object):void
		{
			//trace(property,value)
			_value = value;
		}
		private var _value:Object;

		
		public function apply():void
		{
			if(target && property)
				target[property] = value;
		}
		
		public function clone():PropertySetter
		{
			return new PropertySetter(target,property,value);
		}
		
		public function toString():String
		{
			return target + '["'+property+'"] = ' + value;
		}
	}
}