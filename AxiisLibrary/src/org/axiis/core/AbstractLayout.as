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
	import com.degrafa.core.IGraphicsFill;
	import com.degrafa.core.IGraphicsStroke;
	import com.degrafa.geometry.Geometry;
	
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.geom.Rectangle;
	import flash.utils.getTimer;
	
	import mx.collections.ArrayCollection;
	import mx.core.IFactory;
	import mx.core.UIComponent;
	
	import org.axiis.DataCanvas;
	import org.axiis.events.LayoutItemEvent;
	import org.axiis.layouts.utils.GeometryRepeater;
	import org.axiis.managers.AnchoredDataTipManager;
	import org.axiis.managers.IDataTipManager;
	import org.axiis.utils.ObjectUtils;
	
	/**
	 * Dispatched when an AxiisSprite is mousedOver.
	 */
	[Event(name="itemDataTip", type="flash.events.Event")]

	/**
	 * AbstractLayout is an base class that provides definitions of properties and
	 * stubs for methods required by BaseLayout. It is up to the subclass to appropriately
	 * override these implementations.
	 */
	public class AbstractLayout extends EventDispatcher
	{
		/**
		 * Constructor.
		 */
		public function AbstractLayout()
		{
			super();
		}
		
		/**
		 * The IDataTipManager responsible for laying out data tips for children of this layout.
		 */
		public function get dataTipManager():IDataTipManager {
			return _dataTipManager;
		}
		public function set dataTipManager(value:IDataTipManager):void {
			_dataTipManager=value;
		}
		private var _dataTipManager:IDataTipManager=new AnchoredDataTipManager;
		
		
		/** 
		 * I Hate to put this here, but until we rethink DataTips it seems like the best place
		 */
		 public var dataTipFill:IGraphicsFill;
		 public var dataTipStroke:IGraphicsStroke;
		  
		
		[Bindable]
		/**
		 * A placeholder for fills used within this layout. Modifying
		 * this array will not have any effect on the rendering of the layout.
		 */
		public var fills:Array = [];
		
		[Bindable]
		/**
		 * A placeholder for strokes used within this layout. Modifying
		 * this array will not have any effect on the rendering of the layout.
		 */
		public var strokes:Array = [];
		
		[Bindable]
		/**
		 * A placeholder for palettes used within this layout. Modifying
		 * this array will not have any effect on the rendering of the layout.
		 */
		public var palettes:Array = [];
		
		[Bindable]
		/**
		 * Whether or not this layout is visible. Layouts that are not visible
		 * will return from their render methods immediately after they are
		 * called without making any changes to the display list.
		 */
		public function get visible():Boolean
		{
			return _visible;
		}
		public function set visible(value:Boolean):void
		{
			_visible = value;
		}
		private var _visible:Boolean=true;

		/**
		 * A flag that indicates to DataCanvas that it should listen for mouse
		 * events that signal the need to create a data tip.
		 */
		public function get showDataTips():Boolean
		{
			return _showDataTips;
		}
		public function set showDataTips(value:Boolean):void
		{
			_showDataTips=value;
		}
		private var _showDataTips:Boolean = true;
		
		[Bindable]
		/**
		 * A reference to the layout that contains this layout.
		 */
		public function get parentLayout():AbstractLayout
		{
			return _parentLayout;
		}
		public function set parentLayout(value:AbstractLayout):void
		{
			_parentLayout = value;
		}
		private var _parentLayout:AbstractLayout;

		[Bindable(event="boundsChange")]
		/**
		 * A rectangle that acts as the bounding area for this layout
		 */
		public function get bounds():Rectangle
		{
			return _bounds;
		}
		public function set bounds(value:Rectangle):void
		{
			if(value != _bounds)
			{
				_bounds = value;
				dispatchEvent(new Event("boundsChange"));
			}
		}
		/**
		 * @private
		 */
		protected var _bounds:Rectangle;
		
		/**
		 * An array of states that should be applied to this layout.
		 * 
		 * <p>
		 * As Layouts create children, each child sets up listeners on
		 * itself for the Layout's states' <code>enterStateEvent</code> and
		 * <code>exitStateEvent</code> events. When those events are triggered, the
		 * relevant state's apply and remove methods are called, respectively. This
		 * is usually used to modify the <code>drawingGeometry</code> of the Layout.
		 * </p>
		 * 
		 * @see State
		 */
		public function get states():Array
		{
			return _states;
		}
		public function set states(value:Array):void
		{
			if(_states != value)
			{
				_states=value;
				invalidate();
			}
		}
		private var _states:Array = [];
		
		[Bindable(event="itemCountChange")]
		/**
		 * The number of items in the dataProvider.
		 */
		public function get itemCount():int
		{
			return _itemCount;
		}
		/**
		 * @private
		 */
		protected function set _itemCount(value:int):void
		{
			if(value != __itemCount)
			{
				__itemCount = value;
				dispatchEvent(new Event("itemCountChange"));
			}
		}
		/**
		 * @private
		 */
		protected function get _itemCount():int
		{
			return __itemCount;
		}
		private var __itemCount:int;
		
		
		[Bindable(event="dataItemsChange")]
		/**
		 * An array of objects extracted from the dataProvider.
		 */
		public function get dataItems():Array
		{
			return _dataItems;
		}
		/**
		 * @private
		 */
		protected function set _dataItems(value:Array):void
		{
			if(value != __dataItems)
			{
				__dataItems = value;
				dispatchEvent(new Event("dataItemsChange"));
			}
		}
		/**
		 * @private
		 */
		protected function get _dataItems():Array
		{
			return __dataItems;
		}
		private var __dataItems:Array;
		
		[Bindable(event="dataProviderChange")]
		/**
		 * An Array, ArrayCollection, or Object containing the data this layout
		 * should render.
		 * 
		 * <p>
		 * If this property is Array or ArrayCollection the layout should render
		 * each item. If this property is an Object, it should use an array of
		 * the object's properties as they are exposed in a for..each loop.
		 * </p> 
		 */
		public function get dataProvider():Object
		{
			return _dataProvider;
		}
		public function set dataProvider(value:Object):void
		{
			var t:Number=flash.utils.getTimer();
			if(_dataProvider != value)
			{
				_dataProvider = value;
				
				invalidateDataProvider();
				
				dispatchEvent(new Event("dataProviderChange"));
				
			}
		}
		// TODO this should be private
		/**
		 * @private
		 */
		protected var _dataProvider:Object;
		
		//Used when filtering data rows tells external classes what index it is on
		public function get dataFilterIndex():Number {
			return _dataFilterIndex;
		}
		
		/**
		 * @private
		 */
		protected var _dataFilterIndex:Number=-1;
		
		// TODO This should really be renamed. It *validates* the dataProvider more than it invalidates it. Perhaps we could use a method that returns the dataItems rather than setting them directly.
		/**
		 * Iterates over the items in the dataProvider and stores them in
		 * dataItems.
		 *
		 * <p>
		 * If the dataProvider is Array or ArrayCollection dataItems will contain
		 * each item. If dataProvider is an Object, dataItems will contain an
		 * the object's properties as they are exposed in a for..each loop.
		 * </p> 
		 */
		public function invalidateDataProvider():void
		{
			_dataItems=new Array();
			if (dataProvider is ArrayCollection) {
				for (var i:int=0;i<dataProvider.source.length;i++) {
					_dataFilterIndex=i;
					if (dataProvider.source[i] != null) {
						if (dataFilterFunction != null) {
								if (dataFilterFunction.call(this,dataProvider.source[i])) {
									_dataItems.push(dataProvider.source[i]);
								}
							}
						else {
							_dataItems.push(dataProvider.source[i]);
						}
					}
				}
			}
			else if (dataProvider is Array) {
				for (var j:int=0;j<dataProvider.length;j++) {
					_dataFilterIndex=j;
					if (dataProvider[j] != null) {
						if (dataFilterFunction != null) {
							if (dataFilterFunction.call(this,dataProvider[j])) {
								_dataItems.push(dataProvider[j]);
							}
						}
						else {
							_dataItems.push(dataProvider[j]);
						}
					}
				}
			}
			else {
				// If the dataProvider is just a single object we need to push it onto _dataItems
				// because none of the above cases will have taken care of it. This arises when
				// using DataSet.processXML when a node has no siblings.
				_dataItems.push(dataProvider);
			}
			
			_dataFilterIndex=-1;
			_itemCount=_dataItems.length;
			this.deselectChildren();  //Since the data has changed so have our indexes, deselect children
			this.invalidate();
		
		}
		//---------------------------------------------------------------------
		// "Current" properties
		//---------------------------------------------------------------------
		
		[Bindable(event="currentIndexChange")]
		/**
		 * The index of the item in the dataProvider that the layout is
		 * currently rendering.
		 */
		public function get currentIndex():int
		{
			return _currentIndex;
		}
		/**
		 * @private
		 */
		protected function set _currentIndex(value:int):void
		{
			//if(value != __currentIndex)
			{
				__currentIndex = value;
				dispatchEvent(new Event("currentIndexChange"));
			}
		}
		/**
		 * @private
		 */
		protected function get _currentIndex():int
		{
			return __currentIndex;
		}
		private var __currentIndex:int;
		
		[Bindable(event="currentDatumChange")]
		/**
		 * The item in the dataProvider that the layout is currently rendering.
		 */
		public function get currentDatum():*
		{
			return _currentDatum;
		}
		/**
		 * @private
		 */
		protected function set _currentDatum(value:*):void
		{
			//if(value != __currentDatum)
			{
				__currentDatum = value;
				dispatchEvent(new Event("currentDatumChange"));
			}
		}
		/**
		 * @private
		 */
		protected function get _currentDatum():*
		{
			return __currentDatum;
		}
		private var __currentDatum:*;
		
		[Bindable(event="currentValueChange")]
		/**
		 * The value of the item in the dataProvider that the layout is
		 * currently rendering, as determined by taking currentDatum[dataField],
		 * if a dataField is defined.
		 */
		public function get currentValue():*
		{
			return _currentValue;
		}
		/**
		 * @private
		 */
		protected function set _currentValue(value:*):void
		{
			//if(value != __currentValue)
			{
				__currentValue = value;
				dispatchEvent(new Event("currentValueChange"));
			}
		}
		/**
		 * @private
		 */
		protected function get _currentValue():*
		{
			return __currentValue;
		}
		private var __currentValue:*;
		
		[Bindable(event="currentLabelChange")]
		// TODO the label function should be applied somewhere other than the getter
		/**
		 * The label of the item in the dataProvider that the layout is
		 * currently rendering, as determine by taking currentDatum[labelField],
		 * if a labelField is defined.
		 */
		public function get currentLabel():String
		{
			if (owner && owner.labelFunction != null && labelField)
				return owner.labelFunction.call(this,_currentDatum[labelField],_currentDatum);
			else
				return _currentLabel;
		}
		/**
		 * @private
		 */
		protected function set _currentLabel(value:String):void
		{
			//if(value != __currentLabel)
			{
				__currentLabel = value;
				dispatchEvent(new Event("currentLabelChange"));
			}
		}
		/**
		 * @private
		 */
		protected function get _currentLabel():String
		{
			return __currentLabel;
		}
		private var __currentLabel:String;
		
		[Bindable(event="currentReferenceChange")]
		/**
		 * The geometry that is being used to render the current data item as it
		 * appears after the necessary iterations of the referenceRepeater have
		 * been executed. 
		 */
		public function get currentReference():Geometry  
		{
			return _currentReference;
		}
		/**
		 * @private
		 */
		protected function set _currentReference(value:Geometry):void
		{
			//We want this to fire each time so the geometry property changes propogate
			__currentReference = value;
			dispatchEvent(new Event("currentReferenceChange"));
		}
		/**
		 * @private
		 */
		protected function get _currentReference():Geometry
		{
			return __currentReference;
		}
		private var __currentReference:Geometry;
		
		
		[Bindable(event="dataFieldChange")]
		/**
		 * The property within each item in the dataProvider that contains the
		 * field used to determine the value of the item.
		 */
		public function get dataField():Object
		{
			return _dataField;
		}
		public function set dataField(value:Object):void
		{
			if(value != _dataField)
			{
				_dataField = value;
				dispatchEvent(new Event("dataFieldChange"));
			}
		}
		// TODO this should be private
		/**
		 * @private
		 */
		protected var _dataField:Object;
		
		[Bindable(event="labelFieldChange")]
		/**
		 * The property within each item in the dataProvider that contains the
		 * field used to determine the label for the item. 
		 */
		public function get labelField():Object
		{
			return _labelField;
		}
		public function set labelField(value:Object):void
		{
			if(value != _labelField)
			{
				_labelField = value;
				dispatchEvent(new Event("labelFieldChange"));
			}
		}
		// TODO This should be private
		/**
		 * @private
		 */
		protected var _labelField:Object;
		
			
		[Bindable(event="xChange")]
		/**
		 * The horizontal position of the top left corner of this layout within
		 * its parent.
		 */
		public function get x():Number
		{
			return _x;
		}
		public function set x(value:Number):void
		{
			if(value != _x)
			{
				_x = value;
				invalidate();
				dispatchEvent(new Event("xChange"));
			}
		}
		private var _x:Number=0;
		
		[Bindable(event="yChange")]
		/**
		 * The vertical position of the top left corner of this layout within
		 * its parent.
		 */
		public function get y():Number
		{
			return _y;
		}
		public function set y(value:Number):void
		{
			if(value != _y)
			{
				_y = value;
				invalidate();
				dispatchEvent(new Event("yChange"));
			}
		}
		private var _y:Number=0;
		
		[Bindable(event="widthChange")]
		/**
		 * The width of the layout.
		 */
		public function get width():Number
		{
			return _width;
		}
		public function set width(value:Number):void
		{
			if(value != _width)
			{
				_width = value;
				invalidate();
				dispatchEvent(new Event("widthChange"));
			}
		}
		private var _width:Number=0;
		
		[Bindable(event="heightChange")]
		/**
		 * The height of the layout.
		 */
		public function get height():Number
		{
			return _height;
		}
		public function set height(value:Number):void
		{
			if(value != _height)
			{
				_height = value;
				invalidate();
				dispatchEvent(new Event("heightChange"));
			}
		}
		private var _height:Number=0;
		
		/**
		 * Registers a DisplayObject as the owner of this layout.
		 * Throws an error if the layout already has an owner.
		 */
		public function registerOwner(dataCanvas:DataCanvas):void  
		{
			if(!owner)
			{
				owner = dataCanvas;
				for each(var childLayout:AbstractLayout in layouts)
				{
					childLayout.registerOwner(owner);
				}
			}
			else
			{
				throw new Error("Layout already has an owner.");
			}
		}
		/**
		 * @private
		 */
		protected var owner:DataCanvas;
		
		/**
		 * The AxiisSprites this layout has created to render each item in its dataProvider.
		 */
		public function get childSprites():Array
		{
			return _childSprites;
		}
		private var _childSprites:Array = [];
		
		// TODO we can cut this
		/**
		 * A string used to identify this layout.
		 */
		public var name:String = "";
		
		/**
		 * Determines how long (milliseconds) a layout will spend on a given frame to render X number of datums
		 * 
		 */
		 public var msPerRenderFrame:Number=50;
		
		[Bindable(event="layoutsChange")]
		/**
		 * The layouts that should be displayed within this layout. 
		 */
		public function get layouts():Array
		{
			return _layouts;
		}
		public function set layouts(value:Array):void
		{
			if(value != _layouts)
			{

				for each(var layout:AbstractLayout in _layouts) {
					layout.removeEventListener("itemDataTip",onItemDataTip);
				}
				
				_layouts = value;
				
				for each(layout in _layouts) {
					if (layout.showDataTips)
						layout.addEventListener("itemDataTip",onItemDataTip);
				}
				
				
				dispatchEvent(new Event("layoutsChange"));
			}
		}
		private var _layouts:Array = [];
		
		//I hate to put yet another function in here, but I think we want a default data tip function
		private function dataTipFunction(axiisSprite:AxiisSprite):String
		{
			if(dataField && labelField)
			{
				return "<b>" + String(getProperty(axiisSprite.data,labelField)) + "</b><br/>" + String(getProperty(axiisSprite.data,dataField));
			}
			return "";
		}
		
		// TODO This should be cut. DataSet should manage the data.
		
		// I disagree, from a developer workflow this is very convienent - your filters are almost unique to visulization and
		// even though this deviates from OO best practices I think it is a good approach if we always assume we will be processing
		// an array or collection -  tg 8/7/09 
		/**
		 * This provides a way to further refine a layouts dataProvider by
		 * providing access to a custom filter data filter function. This allows
		 * developers to easily visualize subsets of the data without having to
		 * change the underlying data structure.
		 */
		public var dataFilterFunction:Function;
		
		[Bindable(event="dataTipLabelFunctionChange")]
		/**
		 * A method used to determine the text that appears in the data tip for
		 * an item rendered by this layout.
		 * 
		 * <p>
		 * This method takes one argument, the item to determine the label for,
		 * and returns a String, the text to show in the data tip.
		 * </p>
		 */
		public function get dataTipLabelFunction():Function
		{
			return _dataTipLabelFunction;
		}
		public function set dataTipLabelFunction(value:Function):void
		{
			if(value != _dataTipLabelFunction)
			{
				_dataTipLabelFunction = value;
				dispatchEvent(new Event("dataTipLabelFunctionChange"));
			}
		}
		private var _dataTipLabelFunction:Function=dataTipFunction;
		
		[Inspectable(category="General")]
		[Bindable(event="referenceRepeaterChange")]
		/**
		 * A GeometryRepeater that will be applied to the drawingGeometries once
		 * for each item in the dataProvider.
		 */
		public function get referenceRepeater():GeometryRepeater
		{
			return _referenceGeometryRepeater;
		}
		public function set referenceRepeater(value:GeometryRepeater):void
		{
			if(value != _referenceGeometryRepeater)
			{
				_referenceGeometryRepeater = value;
				dispatchEvent(new Event("referenceRepeaterChange"));
			}
		}
		// TODO This should be private
		/**
		 * @private
		 */
		protected var _referenceGeometryRepeater:GeometryRepeater=new GeometryRepeater();
	
		[Bindable(event="geometryChange")]
		/**
		 * An array of geometries that should be drawn for each item in the data
		 * provider. You can modify these items by using GeometryRepeaters
		 * and PropertyModifiers.
		 * 
		 * @see GeometryRepeater
		 * @see PropertyModifier
		 */
		public function get drawingGeometries():Array
		{
			return _geometries;
		}
		public function set drawingGeometries(value:Array):void
		{
			if(value != _geometries)
			{
				_geometries = value;
				dispatchEvent(new Event("geometryChange"));
			}
		}
		private var _geometries:Array;
		
		// TODO We have this property sprite, getSprite(), and render(sprite = null). We should use a single method to manipulating the sprite
		/**
		 * The sprite this layout is currently rendering to.
		 */
		protected function get sprite():AxiisSprite
		{
			return _sprite;
		}
		protected function set sprite(value:AxiisSprite):void
		{
			if(value != _sprite)
			{
				_sprite = value;
				dispatchEvent(new Event("spriteChange"));
			}
		}
		private var _sprite:AxiisSprite;
		
		/**
		 * Returns the Sprite associated with this layout if owner is
		 * in fact the owner of this layout.
		 */
		public function getSprite(owner:DataCanvas):Sprite
		{
			if(!sprite)
				sprite = new AxiisSprite();
			return sprite;
		}
		
		/**
		 * Draws this layout to the specified AxiisSprite.
		 * 
		 * <p>
		 * If no sprite is provided this layout will use the last AxiisSprite
		 * it rendered to, if such an AxiisSprite exists. Otherwise this returns
		 * immediately.
		 * </p> 
		 * 
		 * <p>
		 * This method is meant to be overridden by a subclass.
		 * </p>
		 * 
		 * @param sprite The AxiisSprite this layout should render to.
		 */
		public function render(newSprite:AxiisSprite = null):void
		{
		}
		
		/**
		 * Notifies the DataCanvas that this layout needs to be rendered. 
		 */
		public function invalidate():void
		{
			this.dataTipManager.destroyAllDataTips();
			dispatchEvent(new Event("layoutInvalidate"));
			
		} 
		
		[Bindable(event="renderingChange")]
		/**
		 * Whether or not this layout is currently in a render cycle. Rendering
		 * can take place over several frames. By watching this property you
		 * can take an appropriate action handle artifacts for multiframe
		 * rendering, such as hiding the layout entirely.
		 */

		public function get rendering():Boolean
		{
			return _rendering;
		}
		/**
		 * @private
		 */
		protected function set _rendering(value:Boolean):void
		{
			if(value != __rendering)
			{
				__rendering = value;
				dispatchEvent(new Event("renderingChange"));
			}
		}
		/**
		 * @private
		 */
		protected function get _rendering():Boolean
		{
			return __rendering;
		}
		private var __rendering:Boolean = false;
		
		[Bindable(event="dataTipAnchorPointChange")]
		/**
		 * An Object with x and y values used to determine the location of anchored data tips. By using
		 * the currentReference, you can update this value during the render cycle.
		 */
		public function get dataTipAnchorPoint():Object
		{
			return _dataTipAnchorPoint;
		}
		public function set dataTipAnchorPoint(value:Object):void
		{
			if(value != _dataTipAnchorPoint)
			{
				_dataTipAnchorPoint = value;
				invalidate();
				dispatchEvent(new Event("dataTipAnchorPointChange"));
			}
		}
		private var _dataTipAnchorPoint:Object;

		[Bindable(event="dataTipContentClassChange")]
		// NOTE : Not sure that a factory pattern is what we want here.
		// *  1. It makes it difficult to get access to the concrete class at run time
		// *  2. It might be memory intensive, and easier to pass around a single class that is arleady in memory
		/**
		 * A ClassFactory that creates the UIComponent that should be used in the data tip for this AxiisSprite.  
		 */
		public function get dataTipContentClass():IFactory
		{
			return _dataTipContentClass;
		}
		public function set dataTipContentClass(value:IFactory):void
		{
			if(value != _dataTipContentClass)
			{
				_dataTipContentClass = value;
				dispatchEvent(new Event("dataTipContentClassChange"));
			}
		}
		private var _dataTipContentClass:IFactory;
		
		
		[Bindable(event="dataTipContentComponentChange")]
		/**
		 * A component that gets passed to any data tip.
		 */
		public function get dataTipContentComponent():UIComponent
		{
			return _dataTipContentComponent;
		}
		public function set dataTipContentComponent(value:UIComponent):void
		{
			if(value != _dataTipContentComponent)
			{
				_dataTipContentComponent = value;
				dispatchEvent(new Event("dataTipContentComponentChange"));
			}
		}
		private var _dataTipContentComponent:UIComponent;
		
		//When a chid layout emits an event we want to bubble it
		private function onItemDataTip(e:LayoutItemEvent):void {
			this.dispatchEvent(new LayoutItemEvent("itemDataTip",e.item,e.sourceEvent));
		}
		
		/**
		 * Uses ObjectUtils.getProperty(this,obj,propertyName) to get a property on an object.
		 * 
		 * @see ObjectUtils#getProperty
		 */
		public function getProperty(obj:Object,propertyName:Object):* { 
			return ObjectUtils.getProperty(this,obj,propertyName);
		}
		
		/**
		 * Marks all children as deselected.
		 */
		protected function deselectChildren():void {
			for (var i:int=0; i<childSprites.length;i++) {
				AxiisSprite(childSprites[i]).selected=false;
			}
		}
		
		
		
		

	}
}