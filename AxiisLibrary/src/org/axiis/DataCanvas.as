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

package org.axiis
{
	import com.degrafa.IGeometryComposition;
	
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.Canvas;
	import mx.core.IFactory;
	
	import org.axiis.core.AbstractLayout;
	import org.axiis.core.AxiisSprite;
	import org.axiis.events.LayoutItemEvent;
	import org.axiis.managers.IDataTipManager;
	import org.axiis.ui.DataTip;
	
	/**
	 * DataCanvas manages the placement and the rendering of layouts.
	 */
	public class DataCanvas extends Canvas
	{
		[Bindable]
		/**
		 * A placeholder for fills. Modifying this property has no
		 * effect on the rendering of the DataCanvas.
		 */
		public var fills:Array = [];
		
		[Bindable]
		/**
		 * A placeholder for strokes. Modifying this property has no
		 * effect on the rendering of the DataCanvas.
		 */
		public var strokes:Array = [];
		
		[Bindable]
		/**
		 * A placeholder for palettes. Modifying this property has no
		 * effect on the rendering of the DataCanvas.
		 */
		public var palettes:Array = [];
		
		/**
		 * Constructor.
		 */
		public function DataCanvas()
		{
			super();
		}
		
		//TODO Do we need this on the DataCanvas level
		/**
		 * @private
		 */
		public var labelFunction:Function;
		
		//TODO Do we need this on the DataCanvas level
		/**
		 * @private
		 */
		public var dataFunction:Function;
		
		/**
		 * Whether or not data tips should be shown when rolling the mouse over
		 * items in the DataCanvas's layouts
		 */
		public var showDataTips:Boolean = true;
		
		// TODO This is currently unused
		/**
		 * @private
		 */
		public var toolTipClass:IFactory;
		
		/**
		 * @private
		 */
		public var hitRadius:Number = 0;
		
		private var toolTips:Array = [];
		
		// TODO This isn't doing anything.  We should cut it.
		[Bindable(event="dataProviderChange")]
		/**
		 * A placeholder for data used by layouts managed by this DataCanvas.
		 * Setting this value re-renders the layouts.
		 */
		public function get dataProvider():Object
		{
			return _dataProvider;
		}
		public function set dataProvider(value:Object):void
		{
			if(value != _dataProvider)
			{
				_dataProvider = value;
				invalidateDisplayList();
				dispatchEvent(new Event("dataProviderChange"));
			}
		}
		private var _dataProvider:Object;
		
		/**
		 * An Array of ILayouts that this DataCanvas should render. Layouts
		 * appearing later in the array will render on top of earlier layouts.
		 */
		public var layouts:Array;
		
		/**
		 * An array of geometries that should be rendered behind the layouts.
		 */
		public var backgroundGeometries:Array;
		
		/**
		 * An array of geometries that should be rendered in front of the
		 * layouts.
		 */
		public var foregroundGeometries:Array;
		
		private var invalidatedLayouts:Array = [];
		
		private var _backgroundSprites:Array = [];
		
		private var _foregroundSprites:Array = [];
		
		private var _background:AxiisSprite;
		
		private var _foreground:AxiisSprite;
		
		/**
		 * @private
		 */
		override protected function createChildren():void
		{
			super.createChildren();
			
			_background=new AxiisSprite();
			this.rawChildren.addChild(_background);

			for each(var layout:AbstractLayout in layouts)
			{
				layout.registerOwner(this);
				var sprite:Sprite = layout.getSprite(this);
				this.rawChildren.addChild(sprite);
				
				layout.addEventListener("layoutInvalidate",handleLayoutInvalidate);
				layout.addEventListener("itemDataTip",onItemDataTip);

			
				invalidatedLayouts.push(layout);
			}
			
			_foreground=new AxiisSprite();
			this.rawChildren.addChild(_foreground);
		}
		
		/**
		 * @private
		 */
		override protected function commitProperties():void
		{
			super.commitProperties();
			var s:AxiisSprite;
			var i:int;
			if (backgroundGeometries && _backgroundSprites.length < backgroundGeometries.length) {
				for (i = _backgroundSprites.length-1; i<backgroundGeometries.length; i++) {
					s=new AxiisSprite();
					_backgroundSprites.push(s);
					_background.addChild(s);
				}
			}
			
			if (foregroundGeometries && _foregroundSprites.length < foregroundGeometries.length ) {
				for (i=_foregroundSprites.length-1; i<foregroundGeometries.length; i++) {
					s=new AxiisSprite();
					_foregroundSprites.push(s);
					_foreground.addChild(s);
				}
			}
		}
		
		private var _invalidated:Boolean=false;
		
		/**
		 * @private
		 */
		override public function invalidateDisplayList():void
		{
			if (!_invalidated)
				invalidateAllLayouts();
			
			_invalidated = true;
		} 
		
		/**
		 * @private
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
	
			//Render layouts first, as they may autoadjust Scales, etc that the background/foreground rely upon
			while(invalidatedLayouts.length > 0)
			{
				var layout:AbstractLayout = AbstractLayout(invalidatedLayouts.pop());
				layout.render();
			}
			
			
			_background.graphics.clear();
			
			var i:int=0;
			for each (var bg:Object in backgroundGeometries) {
				_backgroundSprites[i].graphics.clear();
				if (bg is AbstractLayout) {
					AbstractLayout(bg).render(_backgroundSprites[i])
				}
				else if (bg is IGeometryComposition) {
					bg.preDraw();
					bg.draw(_backgroundSprites[i].graphics,bg.bounds);
				}
				i++;
			}
			
			
			i=0;
			_foreground.graphics.clear();
			for each (var fg:Object in foregroundGeometries) {
				_foregroundSprites[i].graphics.clear();
				if (fg is AbstractLayout) {
					AbstractLayout(fg).render(_foregroundSprites[i])
				}
				else if (fg is IGeometryComposition) {
					fg.preDraw();
					fg.draw(_foregroundSprites[i].graphics,fg.bounds);
				}
				i++;
			}
			
			
			/* this.graphics.clear();
			this.graphics.beginFill(0xff,.1);
			this.graphics.drawRect(0,0,width,height);
			this.graphics.endFill(); */
			
			_invalidated = false;
		}
		
		/**
		 * Handler for when a layout's layoutInvalidated event has been caught.
		 * Invalidates the display list so the layout can be re-rendered. 
		 */
		protected function handleLayoutInvalidate(event:Event):void
		{
			var layout:AbstractLayout = event.target as AbstractLayout;
			if(invalidatedLayouts.indexOf(layout) == -1)
			{
				invalidatedLayouts.push(layout);
				super.invalidateDisplayList();
			}
		}
		
		/**
		 * Invalidates all layouts that this DataCanvas managers. 
		 */
		protected function invalidateAllLayouts():void
		{
			for each(var layout:AbstractLayout in layouts)
			{
				invalidatedLayouts.push(layout);
			}
			super.invalidateDisplayList();
		}
		
		/**
		 * @private
		 */
		private function onItemDataTip(e:LayoutItemEvent):void
		{
			
			var dataTips:Array=new Array();
			
		
			//var axiisSprite:AxiisSprite = AxiisSprite(targetObject);
			
			var axiisSprite:AxiisSprite = e.item;
			
			if(axiisSprite.layout == null)
				return;
				
			var axiisSprites:Array=getHitSiblings(axiisSprite);
			
			for each (var a:AxiisSprite in axiisSprites) {
			
			var dataTip:DataTip = new DataTip();

				dataTip.data = axiisSprite.data;
				if(axiisSprite.layout.dataTipLabelFunction != null)
					dataTip.label = axiisSprite.layout.dataTipLabelFunction(axiisSprite);
				else
					dataTip.label = axiisSprite.label;
				dataTip.value = axiisSprite.value;
				dataTip.index = axiisSprite.index;
				dataTip.backgroundFill=e.item.layout.dataTipFill;
				dataTip.backgroundStroke=e.item.layout.dataTipStroke;
				dataTip.contentComponent = axiisSprite.layout.dataTipContentComponent;
				
				//This seems weird, why does axxisSprite need to know about the dataTipContent class?
				//Not sure a factory pattern is what we want here
				dataTip.contentFactory = axiisSprite.dataTipContentClass;
				
				
				dataTips.push(dataTip);
			}
			
			var dataTipManager:IDataTipManager=axiisSprite.layout.dataTipManager;
			
			dataTipManager.createDataTip(dataTips,this,axiisSprite);

		}
		
		/**
		 * @private
		 */
		public function onItemMouseOut(e:MouseEvent):void
		{
			var axiisSprite:AxiisSprite = e.target as AxiisSprite;
			if(!axiisSprite)
				return;
			
		}
		
		
		
		private function getHitSiblings(axiisSprite:AxiisSprite):Array
		{
			var toReturn:Array = [];
			toReturn.push(axiisSprite);
			
			
			/*
			var s:Sprite = new Sprite();
			s.graphics.clear();
			s.graphics.beginFill(0,0);
			s.graphics.drawCircle(mouseX,mouseY,hitRadius);
			s.graphics.endFill();
			addChild(s);
			*/
			
			/*var siblings:Array = axiisSprite.layout.childSprites;
			for each(var sibling:AxiisSprite in siblings)
			{
				if(sibling.hitTestObject(s))
				{
					toReturn.push(sibling);
				}
			}*/
			
			return toReturn;
		}
		
	}
}