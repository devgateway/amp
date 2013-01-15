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

package org.axiis.paint
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import org.axiis.core.AbstractLayout;

	/**
	 * LayoutPalette will generates an Array of colors for a Layout. By default,
	 * LayoutPalette uses the Flex 3 Halo color scheme, but this can be
	 * overridden using the colors property. As the currentIndex of the Layout
	 * driving this palette changes, the currentColor property is updated to
	 * the color at the currentIndex in the array.
	 * If the layout needs more colors than the array contains,
	 * the colors wraps around the array using the mod operator.
	 */
	public class LayoutPalette extends EventDispatcher
	{
		/**
		 * Constructor.
		 */
		public function LayoutPalette()
		{
			super();
		}
		

		[Bindable]
		/**
		 * The Layout this LayoutPalette should use to determine how many colors
		 * to produce and to determine which color is the "current" one.
		 */
		public function get layout():AbstractLayout
		{
			return _layout
		}
		private function set layout(value:AbstractLayout):void
		{
			if (_layout)
				_layout.removeEventListener("currentIndexChange", onIndexChanged);
			_layout = value;
			if (_layout)
				_layout.addEventListener("currentIndexChange", onIndexChanged);		
		}
		private var _layout:AbstractLayout;

		[Bindable]
		/**
		 * The gradient of colors produced by this LayoutPalette.
		 */
		public function get colors():Array
		{
			return _colors;
		}
		public function set colors(value:Array):void
		{
			_colors = value;
		}
		
		//Halo default colors from Flex 3
		private var _colors:Array = [	 0xE48701
										,0xA5BC4E
										,0x1B95D9
										,0xCACA9E
										,0x6693B0
										,0xF05E27
										,0x86D1E4
										,0xE4F9A0
										,0xFFD512
										,0x75B000
										,0x0662B0
										,0xEDE8C6
										,0xCC3300
										,0xD1DFE7
										,0x52D4CA
										,0xC5E05D
										,0xE7C174
										,0xFFF797
										,0xC5F68F
										,0xBDF1E6
										,0x9E987D
										,0xEB988D
										,0x91C9E5
										,0x93DC4A
										,0xFFB900
										,0x9EBBCD
										,0x009797
										,0x0DB2C2
									];

		[Bindable(event="currentColorChange")]
		/**
		 * The color at index layout.currentIndex in the colors Array.
		 */
		public function get currentColor():Number
		{
			return _currentColor;
		}
		private function set _currentColor(value:Number):void
		{
			if (value != _currentColor)
			{
				__currentColor = value;
				dispatchEvent(new Event("currentColorChange"));
			}
		}
		private function get _currentColor():Number
		{
			return __currentColor;
		}
		private var __currentColor:Number;


		private function onIndexChanged(e:Event):void
		{
			
			_currentColor = _colors[_layout.currentIndex % (_colors.length)];
		}
	}
}