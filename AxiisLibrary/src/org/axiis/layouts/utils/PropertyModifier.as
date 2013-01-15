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

package org.axiis.layouts.utils
{	
	import com.degrafa.geometry.RegularRectangle;
	import flash.events.*;

	/**
	* PropertyModifier is used to specify changes that should 
	* occur on a Geometry or related objects as it is being repeated.
	*/
	public class PropertyModifier extends EventDispatcher
	{
		/**
		* Constructor.
		*/
		public function PropertyModifier()
		{
			super();
		}
		
		private var target:Object;
		
		private var originalValue:Object;
		
		/**
		 * The property that should be modified as this PropertyModifier is
		 * applied. If you wanted to modify the x position of a Geometry, you
		 * would set the property to "x", for example. 
		 */
		public var property:String;
		
		/**
		 * The modification to apply to the property each time <code>apply</code>
		 * is called. This property can be a Number, a Function, or an Array of
		 * Numbers.
		 * 
		 * <ul>
		 * <li><strong>Number</strong>: Each time modify is called, the
		 * modifyOperator will be applied to the property with this value as an
		 * argument. For example, if this modifier is 10, and the
		 * modifierOperator is "add", the property will be incremented by 10
		 * each time <code>apply</code> is called.</li>
		 * <li><strong>Function</strong>: Calls this function each time
		 * <code>apply</code> is called and sets the property to the return
		 * value of the function. The function takes two arguments, the
		 * current iteration as of the PropertyModifier and the current value
		 * of the property.</li>
		 * <li><strong>Array of Numbers</strong>: Uses each item from the
		 * array in the same way that a numeric argument would be used. To
		 * determine which item from the array to use, takes
		 * <code>iteration % arr.length</code> where arr is the array.</li> 
		 * </ul> 
		 */
		public var modifier:Object;
		
		/**
		* The current iteration of an active modification. This is the number of
		* times <code>apply</code> has been called since a call to
		* <code>beginModify</code>. If the PropertyModifier has not been started
		* or has ended, the value of this property is -1.
		*/
	   	public function get iteration():Number
	   	{
	   		return _iteration;
	   	}
	   	private var _iteration:Number = -1;
			
		[Inspectable(category="General", enumeration="add,subtract,multiply,divide,none")]
		[Bindable(event="modifierOperatorChange")]
		/**
		 * How to apply the modifier for each iteration. Accepted values are
		 * "add", "subtract", "multiply", "divide", and "none". When
		 * <code>modifierOperator</code> is one of the four arithmetic opertors
		 * the respective operator from {+=, -=,
		 * ~~=, /=} is applied to the property's
		 * current value each time <code>apply</code> is called.
		 * 
		 * <p>
		 * For example, if <code>modifierOperator</code> is "subtract",
		 * <code>modifier</code> is 100, and the property is 1000,
		 * the property will be changed to 900, 800,
		 * and 700 over the course of 3 calls to <code>apply</code>. 
		 * </p>
		 * 
		 * When <code>modifierOperator</code> is "none" the target property will
		 * be assigned to the <code>modifier</code> each time <code>apply</code>
		 * is called. This behavior is ideal when used in conjuction with a
		 * function call used as a <code>modifier</code>.
		 */
		public function get modifierOperator():String
		{
			return _modifierOperator;
		}
		public function set modifierOperator(value:String):void
		{
			if(value != "add" &&
				value != "subtract" &&
				value != "multiply" &&
				value != "divide" &&
				value != "none")
			{
				trace('warning: attempting to set modifierOperator to ' + value + '.  '
					+ 'The only legal values are add, subtract, multiple, divide, and none.  '
					+ 'modifierOperator will remain as "'+ modifierOperator + '".');
				return;
			}
			if(value != _modifierOperator)
			{
				_modifierOperator = value;
				dispatchEvent(new Event("modifierOperatorChange"));
			}
		}
		private var _modifierOperator:String = "add";
		
		/**
		* Indicates that the PropertyModifier should operate on the
		* <code>target</code>.
		* 
		* @param target The object that should have its property modified
		* by this PropertyModifier.
		*/
		public function beginModify(target:Object):void
		{
			if (iteration != -1)
				end();
			this.target = target;
			originalValue = target[property];
			_iteration = 0;
		}
		
		/**
		* Ends the modification process and returns the target property to its
		* original value. 
		*/
		public function end():void
		{
			if(!target)
				return;
			target[property] = originalValue;
			_iteration = -1;
		}
		
		/**
		* Applies the <code>modifier</code> to the <code>modifyOperator</code>
		* and sets the target property to the result. Increments the iteration
		* by 1 each time this method is called. See the documentation for
		* <code>modifier</code> and <code>modifyOperator</code> for details.
		*/
		public function apply():void
		{
			if (modifier != null)
			{
				if(_modifierOperator == "none")
				{
					var temp:Object;
					if (modifier is Array)
						temp = modifier[iteration % modifier.length];
					else if (modifier is Function)
						temp = modifier(iteration,target[property]);
					else if (modifier is Number)
						temp = Number(modifier);
					else
						temp = Object(modifier);
					target[property] = temp;
				}
				else if(_iteration > 0)
				{
					var tempNumber:Number;
					if (modifier is Array)
						tempNumber = Number(modifier[iteration % modifier.length]);
					else if (modifier is Function)
						tempNumber = Number(modifier(iteration,target[property]));
					else
						tempNumber = Number(modifier);
					
					if (_modifierOperator == "add")  
						target[property] += tempNumber;
					else if (_modifierOperator == "subtract")
						target[property] -= tempNumber;
					else if (_modifierOperator == "multiply")
						target[property] *= tempNumber;
					else if (_modifierOperator == "divide")
						target[property] /= tempNumber;
				}
			}
			_iteration++;
		}
	}
}