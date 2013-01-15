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
	import flash.events.*;
	import flash.geom.*;
	import flash.utils.*;
	
	import mx.core.IFactory;
	
	import org.axiis.events.LayoutItemEvent;
	import org.axiis.states.State;
	
	// TODO This event should be moved to AbstractLayout
	/**
	 * Dispatched when invalidate is called so the DataCanvas that owns this
	 * layout can being the process of redrawing the layout.
	 */
	[Event(name="invalidateLayout", type="flash.events.Event")]
	
	/**
	 * Dispatched at the beginning of the render method. This event allowing
	 * listening objects the chance to perform any computations that will
	 * affect the layout's render process.
	 */
	[Event(name="preRender", type="flash.events.Event")]
	
	/**
	 * Dispatched before each individual child is rendered.
	 */
	[Event(name="itemPreDraw", type="flash.events.Event")]
	
	/**
	 * Dispatched when an AxiisSprite is clicked.
	 */
	[Event(name="itemClick", type="org.axiis.events.LayoutItemEvent")]
	
	/**
	 * Dispatched when an AxiisSprite is double clicked.
	 */
	[Event(name="itemDoubleClick", type="org.axiis.events.LayoutItemEvent")]
	
	/**
	 * Dispatched when an AxiisSprite is selected
	 *
	 */
	[Event(name="itemSelected", type="org.axiis.events.LayoutItemEvent")]
	
	/**
	 * Dispatched when an AxiisSprite is unselected
	 *
	 */
	[Event(name="itemUnSelected", type="org.axiis.events.LayoutItemEvent")]
	
	
	/**
	 * Dispatched when an AxiisSprite is mousedOver.
	 */
	[Event(name="itemMouseOver", type="org.axiis.events.LayoutItemEvent")]
	
	/**
	 * Dispatched when an AxiisSprite is mousedOut.
	 */
	[Event(name="itemMouseOut", type="org.axiis.events.LayoutItemEvent")]
	
	/**
	 * Dispatched when an AxiisSprite is mousedOver.
	 */
	[Event(name="itemMouseMove", type="org.axiis.events.LayoutItemEvent")]
	
	/**
	 * Dispatched when an AxiisSprite is mousedOver.
	 */
	[Event(name="itemDataTip", type="org.axiis.events.LayoutItemEvent")]
	
	
	/**
	 * Dispatched when layout has completed its render cycle
	 *
	 */
	[Event(name="renderComplete", type="flash.events.Event")]
	
	// TODO Is "AxiisLayout" a better name for BaseLayout 
	/**
	 * BaseLayout is a data driven layout engine that uses GeometryRepeaters
	 * and PropertyModifiers to transform geometries before drawing them to
	 * the screen.
	 */
	public class BaseLayout extends AbstractLayout
	{
		/**
		 * Constructor.
		 */
		public function BaseLayout()
		{
			super();
		}
		
		/**
		 * The value of the buttonMode flag to set for each child Sprite
		 */
		public var buttonMode:Boolean = false;
		
		/**
		 * The value of the useHandCursor flag to set each child Sprite
		 */
		public var useHandCursor:Boolean = false;
		
		private var allStates:Array = [];
		
		/**
		 * Used to determine how many child layouts are being rendered. If 0 then all are rendered, or none was started to render,
		 * or there are no child layouts
		 */
		private var pendingChildLayouts:int = 0;
		
		[Bindable(event="scaleFillChange")]
		/**
		 * Whether or not the fills in this geometry should be scaled within the
		 * bounds rectangle.
		 */
		public function get scaleFill():Boolean
		{
			return _scaleFill;
		}
		public function set scaleFill(value:Boolean):void
		{
			if(value != _scaleFill)
			{
				_scaleFill = value;
				this.invalidate();
				dispatchEvent(new Event("scaleFillChange"));
			}
		}	
		private var _scaleFill:Boolean;
		
		/**
		 * Whether or not the drawingGeometries should should have their initial
		 * bounds set to the currentReference of the parent layout.
		 */
		public var inheritParentBounds:Boolean = true;
		
		/**
		 * @private
		 */
		override public function set dataTipContentClass(value:IFactory) : void
		{
			super.dataTipContentClass = value;
			invalidate();
		}
		
		/**
		 * @private
		 */
		override public function get childSprites():Array {
			if (sprite)
				return sprite.drawingSprites;	
			else
				return [];
		}
		
		/**
		 * @private
		 */
		override public function set visible(value:Boolean):void
		{
			super.visible = value;
			if(sprite)
				sprite.visible = visible;
		}
		
		/** 
		 * Draws this layout to the specified AxiisSprite, tracking all changes
		 * made by data binding or the referenceRepeater. 
		 * 
		 * <p>
		 * If no sprite is provided this layout will use the last AxiisSprite
		 * it rendered to, if such an AxiisSprite exists. Otherwise this returns
		 * immediately.
		 * </p>
		 * 
		 * <p>
		 * The render cycle occurs in several stages. By watching for these
		 * events or by binding onto the currentReference, currentIndex, or the
		 * currentDatum properties, you can inject your own logic into the
		 * render cycle.  For example, if you bind a drawingGeometry's x
		 * position to currentReference.x and use a GeometryRepeater that
		 * adds 5 to the x property of the reference, the layout will render
		 * one geometry for each item in the dataProvider at every 5 pixels. 
		 * </p>
		 * 
		 * @param sprite The AxiisSprite this layout should render to.
		 */
		override public function render(newSprite:AxiisSprite = null):void 
		{
			if (!visible || !this.dataItems)
			{
				
				if (newSprite)
					newSprite.visible = false;
				return;
			}

			//if (itemCount==0) {
			//	trimChildSprites();
			//	return;
			//}

			if (newSprite)
				newSprite.visible=true;
			
			if(newSprite)
				this.sprite = newSprite;
			
			_rendering = true;
			
			if(!sprite || !_referenceGeometryRepeater)
				return;		
			
			dispatchEvent(new Event("preRender"));	
			
			_referenceGeometryRepeater.millisecondsPerFrame=this.msPerRenderFrame;
			
			trimChildSprites();
			
			if (inheritParentBounds && parentLayout)
			{
				_bounds = new Rectangle(parentLayout.currentReference.x + (isNaN(x) ? 0 : x),
					parentLayout.currentReference.y + (isNaN(y) ? 0 : y),
					parentLayout.currentReference.width,
					parentLayout.currentReference.height);
			}
			else
			{
				_bounds = new Rectangle((isNaN(x) ? 0:x),(isNaN(y) ? 0:y),width,height);
			}
			sprite.x = isNaN(_bounds.x) ? 0 :_bounds.x;
			sprite.y = isNaN(_bounds.y) ? 0 :_bounds.y;
			
			if (_dataItems)
			{
				var topLayout:AbstractLayout = findTopLayout();
				allStates = findAllStatesInLayoutTree(topLayout);
				
				_currentDatum = null;
				_currentValue = null;
				_currentLabel = null
				_currentIndex = -1;
					
				_referenceGeometryRepeater.repeat(itemCount, preIteration, postIteration, repeatComplete, canIterate);
			}
		}
		
		/**
		 * @private
		 */
		protected function findTopLayout():AbstractLayout
		{
			var topLayout:AbstractLayout = this;
			while(topLayout.parentLayout != null)
			{
				topLayout = topLayout.parentLayout;
			}
			return topLayout;
		}
		
		/**
		 * @private
		 */
		protected function findAllStatesInLayoutTree(layout:AbstractLayout):Array
		{
			var toReturn:Array = [];
			for each(var state:State in layout.states)
			{
				toReturn.push(state);
			}
			for each(var childLayout:AbstractLayout in layout.layouts)
			{
				var childLayoutStates:Array = findAllStatesInLayoutTree(childLayout);
				toReturn = toReturn.concat(childLayoutStates);
			}
			return toReturn;
		}
		
		/**
		 * The callback method called by the referenceRepeater before it applies
		 * the PropertyModifiers on each iteration. This method updates the
		 * currentIndex, currentDatum, currentValue, and currentLabel
		 * properties.  It is recommended that subclasses override this method
		 * to perform any custom data-driven computations that affect the
		 * drawingGeometries.
		 */
		protected function preIteration():void
		{
			_currentIndex = referenceRepeater.currentIteration;
			_currentDatum = dataItems[_currentIndex];			
			if (_currentDatum) {
				_currentValue= getProperty(_currentDatum,dataField);
				_currentLabel = getProperty(_currentDatum,labelField);
			}
			else {
				_currentValue=null;
				_currentLabel=null;
			}
		}
		
		/**
		 * The callback method called by the referenceRepeater after it applies
		 * the PropertyModifiers on each iteration. This method updates the
		 * currentReference property and creates or updates the AxiisSprite that
		 * renders the currentDatum.  It is recommended that subclasses
		 * override this method to perform any computations that affect the
		 * drawingGeometries that are based on the drawingGeometries themselves.
		 */
		protected function postIteration():void
		{
			_currentReference = referenceRepeater.geometry;
			
			// Add a new Sprite if there isn't one available on the display list.
			if(_currentIndex > sprite.drawingSprites.length - 1)
			{
				var newChildSprite:AxiisSprite = createChildSprite(this);	 
				newChildSprite.addEventListener(MouseEvent.CLICK,sprite_onClick);
				newChildSprite.addEventListener(MouseEvent.MOUSE_OVER,sprite_onMouseOver); //Only add this to real sprites versus layout sprites.			
				newChildSprite.addEventListener(MouseEvent.MOUSE_OUT,sprite_onMouseOut);
				newChildSprite.addEventListener(MouseEvent.DOUBLE_CLICK,sprite_onDoubleClick);
				newChildSprite.addEventListener(AxiisSprite.EVENT_SELECTED,sprite_onSelected);
				newChildSprite.addEventListener(AxiisSprite.EVENT_UNSELECTED,sprite_onUnSelected);
				newChildSprite.addEventListener(MouseEvent.MOUSE_MOVE,sprite_onMouseMove); 
				sprite.addDrawingSprite(newChildSprite);
			}
			var currentChild:AxiisSprite = AxiisSprite(sprite.drawingSprites[currentIndex]);
			currentChild.data = currentDatum;
			currentChild.label = currentLabel;
			currentChild.value = currentValue;
			currentChild.index = currentIndex;
			
			dispatchEvent(new Event("itemPreDraw"));
			
			currentChild.useHandCursor = useHandCursor;
			currentChild.buttonMode = buttonMode;
			currentChild.bounds = bounds;
			currentChild.scaleFill = scaleFill;
			var newAnchor:Point=new Point();
			if (dataTipAnchorPoint == null) newAnchor=null else {newAnchor.x=dataTipAnchorPoint.x; newAnchor.y=dataTipAnchorPoint.y};
			currentChild.dataTipAnchorPoint = newAnchor;
			currentChild.dataTipContentClass = dataTipContentClass;
			
			currentChild.storeGeometries(drawingGeometries);
			for each(var state:State in allStates)
			{
				state.apply();
				currentChild.storeGeometries(drawingGeometries,state);
				state.remove();
			}
			currentChild.states = states;
			currentChild.render();
			
			renderChildLayouts(currentChild);
		}
				
		/**
		 * Calls the render method on all child layouts. 
		 */
		protected function renderChildLayouts(child:AxiisSprite):void
		{
			var i:int=0;
			for each(var layout:AbstractLayout in layouts)
			{
				// When we have multiple peer layouts the AxiisSprite needs to
				// differentiate between child drawing sprites and child layout sprites
				layout.parentLayout = this;
				if (child.layoutSprites.length-1 < i)
				{
					var ns:AxiisSprite = createChildSprite(this);
					child.addLayoutSprite(ns);
				}
				
				//when the layout starts rendering we increase the number of children that are being rendered
				//when the rendering is complete we decrease it. When pendingChildLayouts becomes 0 all layouts were rendered
				if(layout is BaseLayout)
				{
					layout.addEventListener("preRender", function():void{pendingChildLayouts++});
					layout.addEventListener("renderComplete", function():void{pendingChildLayouts--});
				}
				
				layout.render(child.layoutSprites[i]);
				i++;
			}
		}
		
		/**
		 * The callback method called by the referenceRepeater after it finishes
		 * its final iteration. Stop tracking changes to the drawingGeometries
		 * properties.
		 */
		protected function repeatComplete():void
		{
			sprite.visible = visible;
			_rendering = false;
			this.dispatchEvent(new Event("renderComplete"));
		}
		
		/**
		 * The callback method called by the referenceRepeater before each iteration
		 * to check if it is possible to do the iteration.
		 * This method returns true if there are no child layouts pending to be rendered, 
		 * returns false if some child layouts have started rendering but haven't already finnished, 
		 * in that case the referenceRepeater will try aggain later to iterate
		 */
		protected function canIterate():Boolean
		{
			return pendingChildLayouts == 0; 
		}
		
		private function createChildSprite(layout:AbstractLayout):AxiisSprite
		{
			var newChildSprite:AxiisSprite = new AxiisSprite();
			newChildSprite.doubleClickEnabled=true;
			newChildSprite.layout = layout;
			return newChildSprite;
		}
		
		private function trimChildSprites():void
		{
			if (!sprite || _itemCount < 0)
				return;

		
			/*
			var trim:int = sprite.drawingSprites.length-_itemCount-1;
			
			for (var i:int=0; i <=trim;i++)
				{
					var s:AxiisSprite = AxiisSprite(sprite.removeChild(sprite.drawingSprites[sprite.drawingSprites.length-1]));
					if (s)
						s.dispose();
					s=null;
				}
			*/

			while(sprite.drawingSprites.length > _itemCount)
			{
				var s:AxiisSprite = AxiisSprite(sprite.removeChild(sprite.drawingSprites.pop()));
				s.dispose();
				s=null;
			}

		}
		
		
		private function sprite_onMouseMove(e:Event):void {
			this.dispatchEvent(new LayoutItemEvent("itemMouseMove",AxiisSprite(e.currentTarget),e));
		}
		
		private function sprite_onMouseOut(e:Event):void {
			this.dispatchEvent(new LayoutItemEvent("itemMouseOut",AxiisSprite(e.currentTarget),e));
		}
		
		private function sprite_onMouseOver(e:Event):void {
			this.dispatchEvent(new LayoutItemEvent("itemMouseOver",AxiisSprite(e.currentTarget),e));
			if (showDataTips) {
				this.dispatchEvent(new LayoutItemEvent("itemDataTip",AxiisSprite(e.currentTarget),e));
				e.stopPropagation();
			}
		}
		
		private function sprite_onClick(e:Event):void {
			e.stopPropagation();
			this.dispatchEvent(new LayoutItemEvent("itemClick",AxiisSprite(e.currentTarget),e));
		}
		
		
		private function sprite_onDoubleClick(e:Event):void {
			e.stopPropagation();
			this.dispatchEvent(new LayoutItemEvent("itemDoubleClick",AxiisSprite(e.currentTarget),e));
		}
		
		private function sprite_onSelected(e:Event):void {
			this.dispatchEvent(new LayoutItemEvent("itemSelected",AxiisSprite(e.currentTarget),e));
		}
		
		private function sprite_onUnSelected(e:Event):void {
			this.dispatchEvent(new LayoutItemEvent("itemUnSelected",AxiisSprite(e.currentTarget),e));
		}
	}
}