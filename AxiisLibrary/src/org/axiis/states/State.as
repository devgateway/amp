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

package org.axiis.states
{
	import mx.controls.Alert;

	/**
	 * States describe modifications to properties that should occur as a result
	 * of an event and provides a mechanicsm to make and undo those changes.
	 * A State uses three parallel Arrays -- targets, properties, and values --
	 * to define these modifications.  For example, this declaration
	 * 
	 * <pre>
	 * <code>
	 * &lt;axiis:State id="myState"
	 *		enterStateEvent="mouseOver"
	 *		exitStateEvent="mouseOut"
	 *		targets="{[myTarget1,myTarget1,myTarget2]}"
	 *		properties="{['width','height','radius']}"
	 *		values="{[w,h,r]}" /&gt;
	 * </code>
	 * </pre>
	 * 
	 * indicates that <code>myTarget1.width</code> and <code>myTarget1.height</code>
	 * should be set to <code>w</code> and <code>h</code>, respectively and
	 * <code>myTarget2.radius</code> should be set to <code>r</code> when the
	 * owner of this state is moused over.  These values should be unset when
	 * the mouse is moved off of the owner.
	 * 
	 * <p>
	 * Within Axiis, an Array of States can be set to any Layout's <code>states</code>
	 * property. As Layouts create children, each child sets up listeners on
	 * itself for the Layout's states' <code>enterStateEvent</code> and
	 * <code>exitStateEvent</code> events. When those events are triggered, the
	 * relevant state's apply and remove methods are called, respectively. This
	 * is usually used to modify the <code>drawingGeometry</code> of the Layout. 
	 * </p>
	 */
	public class State
	{
		/**
		 * Constructor.
		 */
		public function State()
		{
			super();
		}
		
		public var enabled:Boolean=true;

		/**
		 * The values of the properties before they were modified.
		 */
		private var oldValues:Array = [];

		/**
		 * An Array of Objects that should have one or more of their properties
		 * modified by this state.
		 */
		public var targets:Array = [];

		/**
		 * An Array of Strings that define the properties on the corresponding
		 * Objects in the targets Array that should be modified.
		 */
		public var properties:Array = [];
		
		/**
		 * An Array containing the values that the corresponding target's
		 * properties should be set to when this State is applied.
		 */
		public var values:Array = [];

		/**
		 * The eventType of the Event indicating that this state should be
		 * activated.
		 */
		public var enterStateEvent:String;

		/**
		 * The eventType of the Event indicating that this state should be
		 * removed.
		 */
		public var exitStateEvent:String;

		/**
		 * A flag indicating that when an AxiisSprite enters this state all of
		 * its descendents should enter the state as well.
		 */
		public var propagateToDescendents:Boolean = true;

		/**
		 * A flag indicating that when an AxiisSprite enters this state all of
		 * its siblings should enter the state as well.
		 */
		public var propagateToSiblings:Boolean = false;

		/**
		 * A flag indicating that when an AxiisSprite enters this state all of
		 * its AxiisSprite ancestors should enter the state as well.
		 */
		public var propagateToAncestors:Boolean = false;

		/**
		 * A flag indicating that when an AxiisSprite enters this state all of
		 * its AxiisSprite ancestors and their children should enter the state
		 * as well.
		 */
		public var propagateToAncestorsSiblings:Boolean = false;

		/**
		 * Modifies each Object at index <code>i</code> in the targets Array
		 * by setting its corresponding property, properties[i],
		 * to value[i].
		 * 
		 * <p>
		 * In pseudo-code, this amounts to <code>targets[i][properties[i]] = values[i];</code>
		 * for all <code>i</code>.
		 * </p>
		 */
		public function apply():void
		{
			if (targets.length != properties.length)
				return;

			oldValues = [];

			for (var i:int = 0; i < targets.length; i++)
			{
				var obj:Object = targets[i];
				if (String(properties[i]).indexOf(".") > 0)
				{
					var lastValidProperty:Object = new Object();
					var property:Object = getProperty(obj, properties[i], lastValidProperty);
					oldValues.push(property[lastValidProperty.name]);
					property[lastValidProperty.name] = (values[i] is Function) ? values[i].call(this, property[lastValidProperty.name]) : values[i];
				}
				else
				{
					oldValues.push(obj[properties[i]]);
					obj[properties[i]] = (values[i] is Function) ? values[i].call(this, obj[properties[i]]) : values[i];
				}
			}
		}

		/**
		 * Returns the targets' properties to their original values, undoing the
		 * effects of the previous call to apply.
		 */
		public function remove():void
		{
			if (targets.length != properties.length || properties.length != oldValues.length)
				return;

			for (var i:int = 0; i < targets.length; i++)
			{
				var obj:Object = targets[i];

				if (String(properties[i]).indexOf(".") > 0)
				{
					var lastValidProperty:Object = new Object();
					var property:Object = getProperty(obj, properties[i], lastValidProperty);
					oldValues.push(property[lastValidProperty.name]);
					property[lastValidProperty.name] = oldValues[i];
				}
				else
				{
					obj[properties[i]] = oldValues[i];
				}
			}
		}

		private function getProperty(obj:Object, propertyName:String, lastValidProperty:Object):Object
		{
			if (obj == null)
				return null;

			var chain:Array = propertyName.split(".");
			if (chain.length <= 2)
			{
				lastValidProperty.name = chain[1];
				return obj[chain[0]];
			}
			else
			{
				return getProperty(obj[chain[0]], chain.slice(1, chain.length).join("."), lastValidProperty);
			}
		}
	}
}