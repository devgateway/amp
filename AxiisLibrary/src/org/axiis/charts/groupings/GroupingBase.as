package org.axiis.charts.groupings
{
	import com.degrafa.core.IGraphicsFill;
	import com.degrafa.core.IGraphicsStroke;
	
	import flash.events.Event;
	
	import mx.events.PropertyChangeEvent;
	
	import org.axiis.core.BaseLayout;

	/**
	 * GroupingBase is the base class for BarCluster, BarStack, ColumnCluster,
	 * and ColumnStack, and it contains definitions of properties that these 
	 * subclasses leverage.
	 */
	public class GroupingBase extends BaseLayout
	{
		public function GroupingBase()
		{
			super();
		}
		
		[Bindable(event="strokeChange")]
		/**
		 * The stroke that should outline each drawingGeometry.
		 */
		public function get stroke():IGraphicsStroke
		{
			return _stroke; 
		}
		public function set stroke(value:IGraphicsStroke):void
		{
			if(value != _stroke)
			{
				if(stroke)
					stroke.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				_stroke = value;
				if(stroke)
					stroke.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				dispatchEvent(new Event("strokeChange"));
			}
		}
		private var _stroke:IGraphicsStroke;
		
		[Bindable(event="fillChange")]
		/**
		 * The fill applied to the drawingGeometries.
		 */
		public function get fill():IGraphicsFill
		{
			return _fill;
		}
		public function set fill(value:IGraphicsFill):void
		{
			if(value != _fill)
			{
				if(fill)
					fill.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				_fill = value;
				if(fill)
					fill.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,handlePropertyChange);
				dispatchEvent(new Event("fillChange"));
			}
		}
		private var _fill:IGraphicsFill;
		
		[Bindable(event="fontFamilyChange")]
		/**
		 * The name of the font used to render the labels.
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
				//invalidate();
			}
		}
		private var _fontFamily:String = "Arial";
	
		[Bindable(event="fontSizeChange")] 
		/**
		 * The size of the font used to render the labels.
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
			//	invalidate();
			}
		}
		private var _fontSize:Number = 12;
	
		[Bindable(event="fontColorChange")]
		/**
		 * The color of the labels.
		 */
		public function get fontColor():*
		{
			return _fontColor;
		}
		public function set fontColor(value:*):void
		{
			if(value != _fontColor)
			{
				if(value is String && String(value).indexOf("#") != -1)
				{
					_fontColor = parseInt(String(value).substr(1),16);
				}
				else
				{
					_fontColor = value;
				}
				dispatchEvent(new Event("fontColorChange"));
				//invalidate();
			}
		}
		private var _fontColor:* = 0;
	
		[Bindable(event="fontWeightChange")]
		/**
		 * The weight ("normal" or "bold") of the labels.
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
				//invalidate();
			}
		}
		private var _fontWeight:String = "normal";
		
		
		[Bindable(event="dataFontFamilyChange")]
		/**
		 * The name of the dataFont used to render the labels.
		 */
		public function get dataFontFamily():String
		{
			return _dataFontFamily;
		}
		public function set dataFontFamily(value:String):void
		{
			if(value != _dataFontFamily)
			{
				_dataFontFamily = value;
				dispatchEvent(new Event("dataFontFamilyChange"));
				//invalidate();
			}
		}
		private var _dataFontFamily:String = "Arial";
	
		[Bindable(event="dataFontSizeChange")]
		/**
		 * The size of the dataFont used to render the labels.
		 */
		public function get dataFontSize():Number
		{
			return _dataFontSize;
		}
		public function set dataFontSize(value:Number):void
		{
			if(value != _dataFontSize)
			{
				_dataFontSize = value;
				dispatchEvent(new Event("dataFontSizeChange"));
				//invalidate();
			}
		}
		private var _dataFontSize:Number = 12;
	
		[Bindable(event="dataFontColorChange")]
		/**
		 * The color of the labels.
		 */
		public function get dataFontColor():Number
		{
			return _dataFontColor;
		}
		public function set dataFontColor(value:Number):void
		{
			if(value != _dataFontColor)
			{
				_dataFontColor = value;
				dispatchEvent(new Event("dataFontColorChange"));
			//	invalidate();
			}
		}
		private var _dataFontColor:Number = 0;
	
		[Bindable(event="dataFontWeightChange")]
		/**
		 * The weight ("normal" or "bold") of the labels.
		 */
		public function get dataFontWeight():String
		{
			return _dataFontWeight;
		}
		public function set dataFontWeight(value:String):void
		{
			if(value != "normal" && value != "bold")
				return;
				
			if(value != _dataFontWeight)
			{
				_dataFontWeight = value;
				dispatchEvent(new Event("dataFontWeightChange"));
				//invalidate();
			}
		}
		private var _dataFontWeight:String = "normal";
		
		[Bindable(event="showLabelChange")]
		/**
		 * Whether or not labels are shown for each drawingGeometry.
		 */
		public function get showLabel():Boolean
		{
			return _showLabel;
		}
		public function set showLabel(value:Boolean):void
		{
			if(value != _showLabel)
			{
				_showLabel = value;
				dispatchEvent(new Event("showLabelChange"));
				//invalidate();
			}
		}
		private var _showLabel:Boolean = true;
		
		
		[Bindable(event="showDataLabelChange")]
		/**
		 * Whether or not labels are shown for each drawingGeometry.
		 */
		public function get showDataLabel():Boolean
		{
			return _showDataLabel; 
		}
		public function set showDataLabel(value:Boolean):void
		{
			if(value != _showDataLabel)
			{
				_showDataLabel = value;
				dispatchEvent(new Event("showDataLabelChange"));
				//invalidate();
			}
		}
		private var _showDataLabel:Boolean = true;
		
		private function handlePropertyChange(event:Event):void
		{
			//if (!this.rendering)
			//	invalidate();
		}
		
		
	}
}