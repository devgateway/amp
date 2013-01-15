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
/*
<!--- The fontFamily of the label -->
	<mx:String id="fontFamily">Arial</mx:String>
	<!--- The font size of the label -->
	<mx:Number id="fontSize">12</mx:Number>
	<!--- The color of the label -->
	<mx:Number id="fontColor">0</mx:Number>
	<!--- The weight of the label -->
	<mx:String id="fontWeight">normal</mx:String>
*/
package org.axiis.charts.axis
{
	import com.degrafa.core.IGraphicsFill;
	import com.degrafa.core.IGraphicsStroke;
	import com.degrafa.paint.SolidFill;
	import com.degrafa.paint.SolidStroke;
	
	import flash.events.Event;
	
	import mx.events.PropertyChangeEvent;
	
	import org.axiis.core.BaseLayout;

	/**
	 * AxisBase is the base class for HAxis and VAxis. It contains properties
	 * used by both classes and includes functionality for setting fills,
	 * strokes, visiblity, and tick mark intervals. 
	 */
	public class AxisBase extends BaseLayout
	{
		/**
		 * Constructor.
		 */
		public function AxisBase()
		{
			super();
			showDataTips = false;
			
		}
		
		[Bindable]
		/**
		 * A function that is used to determine how each item in the
		 * dataProvider should be labeled along the axis.
		 * 
		 * <p>
		 * The function takes a single parameter, the Object to be plotted,
		 * and should return the String that should be placed on the axis.
		 * The default implementation assumes that each Object in the
		 * dataProvider is numeric, rounds that number, and returns the
		 * that value as a String.
		 * </p> 
		 */
		public function get labelFunction():Function
		{
			return _labelFunction;
		}
		public function set labelFunction(value:Function):void
		{
			if(labelFunction != value)
			{
				_labelFunction=value;
				invalidate();
			}
		}
		private var _labelFunction:Function =  internalLabelFunction;
		
		private function internalLabelFunction(value:Object):String
		{
			return (! isNaN(Math.round(Number(value))) ? Math.round(Number(value)).toString():value.toString());
		}
		
		[Bindable(event="gridStrokeChange")]
		/**
		 * The stroke that should be used to render the grid lines
		 * running across this axis.
		 */
		public function get gridStroke():IGraphicsStroke
		{
			return _gridStroke;
		}
		public function set gridStroke(value:IGraphicsStroke):void
		{
			if(value != _gridStroke)
			{
				if(gridStroke)
					gridStroke.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				_gridStroke = value;
				if(gridStroke)
					gridStroke.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				dispatchEvent(new Event("gridStrokeChange"));
			}
		}
		private var _gridStroke:IGraphicsStroke = new SolidStroke(0,1,1);
		
		[Bindable(event="gridFillChange")]
		/**
		 * The fill used to render the grid areas that occur at alternating
		 * major tick marks.
		 */
		public function get gridFill():IGraphicsFill
		{
			return _gridFill;
		}
		public function set gridFill(value:IGraphicsFill):void
		{
			if(value != _gridFill)
			{
				if(gridFill)
					gridFill.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				_gridFill = value;
				if(gridFill)
					gridFill.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				dispatchEvent(new Event("gridFillChange"));
			}
		}
		private var _gridFill:IGraphicsFill = new SolidFill(0xffffff,.1);
		
		[Bindable(event="tickStrokeChange")]
		/**
		 * The stroke used to render each tick mark.
		 */
		public function get tickStroke():SolidStroke
		{
			return _tickStroke;
		}
		public function set tickStroke(value:SolidStroke):void
		{
			if(value != _tickStroke)
			{
				if(tickStroke)
					tickStroke.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				_tickStroke = value;
				if(tickStroke)
					tickStroke.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				dispatchEvent(new Event("tickStrokeChange"));
				invalidate();
			}
		}
		private var _tickStroke:SolidStroke = new SolidStroke();
		
		[Bindable(event="showLabelsChange")]
		/**
		 * Whether or not the labels on this axis are shown.
		 */
		public function get showLabels():Boolean
		{
			return _showLabels;
		}
		public function set showLabels(value:Boolean):void
		{
			if(value != _showLabels)
			{
				_showLabels = value;
				dispatchEvent(new Event("showLabelsChange"));
				invalidate();
			}
		}
		private var _showLabels:Boolean = true;
	
		[Bindable(event="tickGapChange")]
		/**
		 * The number of values between successive minor tick marks.
		 */
		public function get tickGap():Number
		{
			return _tickGap;
		}
		public function set tickGap(value:Number):void
		{
			if(value != _tickGap)
			{
				_tickGap = value;
				dispatchEvent(new Event("tickGapChange"));
				invalidate();
			}
		}
		private var _tickGap:Number = 4;
		
		[Bindable(event="majorTickSpacingChange")]
		/**
		 * The space between successive major tick marks.
		 */
		public function get majorTickSpacing():Number
		{
			return _majorTickSpacing;
		}
		public function set majorTickSpacing(value:Number):void
		{
			if(value != _majorTickSpacing)
			{
				_majorTickSpacing = value;
				dispatchEvent(new Event("majorTickSpacingChange"));
				invalidate();
			}
		}
		private var _majorTickSpacing:Number = 30;
	
		[Bindable(event="majorTickLengthChange")]
		/**
		 * The length of each major tick mark in pixels.
		 */
		public function get majorTickLength():Number
		{
			return _majorTickLength;
		}
		public function set majorTickLength(value:Number):void
		{
			if(value != _majorTickLength)
			{
				_majorTickLength = value;
				dispatchEvent(new Event("majorTickLengthChange"));
				invalidate();
			}
		}
		private var _majorTickLength:Number = 12;
	
		[Bindable(event="minorTickLengthChange")]
		/**
		 * The length of each minor tick mark in pixels.
		 */
		public function get minorTickLength():Number
		{
			return _minorTickLength;
		}
		public function set minorTickLength(value:Number):void
		{
			if(value != _minorTickLength)
			{
				_minorTickLength = value;
				dispatchEvent(new Event("minorTickLengthChange"));
				invalidate();
			}
		}
		private var _minorTickLength:Number = 6;
	
		[Bindable(event="showGridAreaChange")]
		/**
		 * Whether or not grid cells are displayed.
		 */
		public function get showGridArea():Boolean
		{
			return _showGridArea;
		}
		public function set showGridArea(value:Boolean):void
		{
			if(value != _showGridArea)
			{
				_showGridArea = value;
				dispatchEvent(new Event("showGridAreaChange"));
				invalidate();
			}
		}
		private var _showGridArea:Boolean=true;
	
		[Bindable(event="showGridLineChange")]
		/**
		 * Whether or not lines are drawn between grid cells.
		 */
		public function get showGridLine():Boolean
		{
			return _showGridLine;
		}
		public function set showGridLine(value:Boolean):void
		{
			if(value != _showGridLine)
			{
				_showGridLine = value;
				dispatchEvent(new Event("showGridLineChange"));
				invalidate();
			}
		}
		private var _showGridLine:Boolean=false;
		
		[Bindable(event="showZeroLineChange")]
		/**
		 * Whether or not line 0 is drawn.
		 */
		public function get showZeroLine():Boolean
		{
			return _showZeroLine;
		}
		public function set showZeroLine(value:Boolean):void
		{
			if(value != _showZeroLine)
			{
				_showZeroLine = value;
				dispatchEvent(new Event("showZeroLineChange"));
				invalidate();
			}
		}
		private var _showZeroLine:Boolean=false;
		
		[Bindable(event="maxScaleChange")]
		/**
		 * The size of the font used to render the tick mark labels.
		 */
		public function get maxScale():Number
		{
			return _maxScale;
		}
		public function set maxScale(value:Number):void
		{
			if(value != _maxScale)
			{
				_maxScale = value;
				dispatchEvent(new Event("maxScaleChange"));
				invalidate();
			}
		}
		private var _maxScale:Number = 0;
		
		[Bindable(event="maxScaleChange")]
		/**
		 * The size of the font used to render the tick mark labels.
		 */
		public function get minScale():Number
		{
			return _minScale;
		}
		public function set minScale(value:Number):void
		{
			if(value != _minScale)
			{
				_minScale = value;
				dispatchEvent(new Event("minScaleChange"));
				invalidate();
			}
		}
		private var _minScale:Number = 0;
		
		[Bindable(event="fontFamilyChange")]
		/**
		 * The name of the font used to render the tick mark labels.
		 */
		public function get fontFamily():String
		{
			return _fontFamily;
		}
		public function set fontFamily(value:String):void
		{
			if(value != _fontFamily)
			{
				_fontFamily = value;
				dispatchEvent(new Event("fontFamilyChange"));
				invalidate();
			}
		}
		private var _fontFamily:String = "Arial";
	
		[Bindable(event="fontSizeChange")]
		/**
		 * The size of the font used to render the tick mark labels.
		 */
		public function get fontSize():Number
		{
			return _fontSize;
		}
		public function set fontSize(value:Number):void
		{
			if(value != _fontSize)
			{
				_fontSize = value;
				dispatchEvent(new Event("fontSizeChange"));
				invalidate();
			}
		}
		private var _fontSize:Number = 12;
	
		[Bindable(event="fontColorChange")]
		/**
		 * The color of the tick mark labels.
		 */
		public function get fontColor():Number
		{
			return _fontColor;
		}
		public function set fontColor(value:Number):void
		{
			if(value != _fontColor)
			{
				_fontColor = value;
				dispatchEvent(new Event("fontColorChange"));
				invalidate();
			}
		}
		private var _fontColor:Number = 0;
	
		[Bindable(event="fontWeightChange")]
		/**
		 * The weight ("normal" or "bold") of the tick mark labels.
		 */
		public function get fontWeight():String
		{
			return _fontWeight;
		}
		public function set fontWeight(value:String):void
		{
			if(value != "normal" && value != "bold")
				return;
				
			if(value != _fontWeight)
			{
				_fontWeight = value;
				dispatchEvent(new Event("fontWeightChange"));
				invalidate();
			}
		}
		private var _fontWeight:String = "normal";
		
		private function handlePropertyChange(event:Event):void
		{
			invalidate();
		}	
	}
}