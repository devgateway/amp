package org.axiis.ui
{
	import com.degrafa.GeometryGroup;
	import com.degrafa.core.IGraphicsFill;
	import com.degrafa.core.IGraphicsStroke;
	import com.degrafa.geometry.CalloutBalloon;
	import com.degrafa.paint.*;
	
	import flash.events.Event;
	import flash.filters.DropShadowFilter;
	import flash.geom.Rectangle;
	
	import mx.core.Application;
	import mx.core.IFactory;
	import mx.core.UIComponent;
	
	import org.axiis.core.IAxiisRenderer;

	/**
	 * A data tip is a tool tip that appears when the user mouses over an AxiisSprite.
	 */
	public class DataTip extends UIComponent implements IAxiisRenderer
	{
		/**
		 * Constructor.
		 */
		public function DataTip()
		{
			super();
			mouseEnabled = false;
			mouseChildren = false;
		}
		
		public var paddingLeft:Number=8;
		public var paddingRight:Number=8;
		public var paddingTop:Number=8;
		public var paddingBottom:Number=8;
		
		/**
		 * overides default fill of callout
		 */
		public var backgroundFill:IGraphicsFill;
		
		/**
		 * overides default stroke of callout
		 */
		public var backgroundStroke:IGraphicsStroke;

		
		private var contentFactoryDirty:Boolean = true;
		
		/**
		 * @private
		 */
		protected var content:UIComponent;
		
		/**
		 * @private
		 */
		protected var backgroundGeometry:GeometryGroup;
		
		/**
		 * @private
		 */
		protected var callout:CalloutBalloon;
		
		[Bindable(event="labelChange")]
		/**
		 * @inheritDoc IAxiisRenderer#label
		 */
		public function get label():String
		{
			return _label;
		}
		public function set label(value:String):void
		{
			if(value != _label)
			{
				_label = value;
				invalidateProperties();
				invalidateDisplayList();
				dispatchEvent(new Event("labelChange"));
			}
		}
		private var _label:String;
		
		[Bindable(event="dataChange")]
		/**
		 * @inheritDoc IAxiisRenderer#data
		 */
		public function get data():Object
		{
			return _data;
		}
		public function set data(value:Object):void
		{
			if(value != _data)
			{
				_data = value;
				invalidateProperties();
				invalidateDisplayList();
				dispatchEvent(new Event("dataChange"));
			}
		}
		private var _data:Object;
		
		[Bindable(event="valueChange")]
		/**
		 * @inheritDoc IAxiisRenderer#value
		 */
		public function get value():Object
		{
			return _value;
		}
		public function set value(value:Object):void
		{
			if(value != _value)
			{
				_value = value;
				invalidateProperties();
				invalidateDisplayList();
				dispatchEvent(new Event("valueChange"));
			}
		}
		private var _value:Object;
		
		[Bindable(event="indexChange")]
		/**
		 * @inheritDoc IAxiisRenderer#index
		 */
		public function get index():int
		{
			return _index;
		}
		public function set index(value:int):void
		{
			if(value != _index)
			{
				_index = value;
				invalidateProperties();
				invalidateDisplayList();
				dispatchEvent(new Event("indexChange"));
			}
		}
		private var _index:int;
		
		[Bindable]
		/**
		 * A static component that gets passed to any instance of this data tip - gets set by the layout
		 */
		public var contentComponent:UIComponent;
		
		[Bindable(event="contentFactoryChange")]
		/**
		 * A ClassFactory that is used as this data tip's content. The default content renderer is the TextDataTipContent,
		 * which simply renders the label for the datum.
		 */
		public function get contentFactory():IFactory
		{
			return _contentFactory;
		}
		public function set contentFactory(value:IFactory):void
		{
			if(value != _contentFactory)
			{
				_contentFactory = value;
				contentFactoryDirty = true;
				invalidateProperties();
				invalidateDisplayList();
				dispatchEvent(new Event("contentFactoryChange"));
			}
		}
		private var _contentFactory:IFactory;
		
		[Bindable(event="calloutWidthChange")]
		/**
		 * The width of the tail of the background callout balloon.
		 */
		public function get calloutWidth():Number
		{
			return _calloutWidth;
		}
		public function set calloutWidth(value:Number):void
		{
			if(value != _calloutWidth)
			{
				_calloutWidth = value;
				invalidateDisplayList();
				dispatchEvent(new Event("calloutWidthChange"));
			}
		}
		private var _calloutWidth:Number = 25;
		
		[Bindable(event="calloutHeightChange")]
		/**
		 * The height of the tail of the background callout balloon.
		 */
		public function get calloutHeight():Number
		{
			return _calloutHeight;
		}
		public function set calloutHeight(value:Number):void
		{
			if(value != _calloutHeight)
			{
				_calloutHeight = value;
				dispatchEvent(new Event("calloutHeightChange"));
			}
		}
		private var _calloutHeight:Number = 25;
		
		override protected function createChildren() : void
		{
			super.createChildren();
			if(!backgroundGeometry)
			{
				backgroundGeometry = new GeometryGroup();
				
				callout = new CalloutBalloon();
				callout.topLeftRadius = 8;
				callout.topRightRadius = 8;
				callout.bottomLeftRadius = 8;
				callout.bottomRightRadius = 8;
				
				var fill:LinearGradientFill = new LinearGradientFill();
				fill.gradientStops = [new GradientStop(0xAAAAAA),new GradientStop(0xEEEEEE)];
				fill.angle = 90;
				callout.fill = fill;
				callout.filters=[new DropShadowFilter(4,45,0,.5,20,20)];
				
				var stroke:SolidStroke = new SolidStroke(0x999999);
				callout.stroke = stroke;
				
				backgroundGeometry.geometry = [callout];
				
				addChild(backgroundGeometry); 
			}
		}
		
		override protected function commitProperties() : void
		{
			super.commitProperties();
			if(contentFactoryDirty)
			{
				if(content)
				{
					removeChild(content);
					content = null;
				}
				if (contentComponent) 
					content=contentComponent;
				else if(contentFactory)
					content = UIComponent(contentFactory.newInstance());
				else
					content = new TextDataTipContent();
				addChild(content);
				contentFactoryDirty = false;
			}
			if(content is IAxiisRenderer)
			{
				IAxiisRenderer(content).data = data;
				IAxiisRenderer(content).value = value;
				IAxiisRenderer(content).label = label;
				IAxiisRenderer(content).index = index;
			}
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number) : void
		{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			
			this.graphics.clear();
			
			if(!content)
				return;
			
			//this.graphics.lineStyle(4,0xff0000);
			//this.graphics.drawCircle(0,0,10);
			
			var screen:Rectangle = Application(Application.application).screen;
			var contentWidth:Number = Math.max(content.measuredWidth,content.width);
			var contentHeight:Number = Math.max(content.measuredHeight,content.height);
			var contentX:Number = Math.max(-x,Math.min(screen.width - contentWidth - x,calloutWidth));
			var contentY:Number = Math.max(-y,Math.min(screen.height - contentHeight - y,calloutHeight));
			if(contentX < 0)
				contentX = -contentWidth - 24;
			if(contentY < 0)
				contentY = -contentHeight - 8;
			content.move(contentX,contentY);
			content.setActualSize(contentWidth,contentHeight);
			
			if (backgroundFill) callout.fill=backgroundFill;
			if (backgroundStroke) callout.stroke=backgroundStroke;
			
			/*this.graphics.lineStyle(1,0);
			this.graphics.beginFill(0xffffff,.5);
			this.graphics.drawRect(contentX,contentY,content.width,content.height);
			this.graphics.endFill();*/
			
			callout.x = contentX - paddingLeft;
			callout.y = contentY - paddingRight;
			callout.width = contentWidth + paddingLeft + paddingRight;
			callout.height = contentHeight + paddingTop + paddingBottom;
		}
	}
}