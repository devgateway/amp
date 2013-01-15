////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2008 The Degrafa Team : http://www.Degrafa.com/team
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////
package com.degrafa.geometry{
	
	import com.degrafa.IGeometryComposition;
	import com.degrafa.IGraphic;
	import com.degrafa.core.DegrafaObject;
	import com.degrafa.core.IDegrafaObject;
	import com.degrafa.core.IGraphicSkin;
	import com.degrafa.core.IGraphicsFill;
	import com.degrafa.core.IGraphicsStroke;
	import com.degrafa.core.ITransformablePaint;
	import com.degrafa.core.collections.DecoratorCollection;
	import com.degrafa.core.collections.DisplayObjectCollection;
	import com.degrafa.core.collections.FilterCollection;
	import com.degrafa.core.collections.GeometryCollection;
	import com.degrafa.events.DegrafaEvent;
	import com.degrafa.geometry.command.CommandStack;
	import com.degrafa.geometry.layout.LayoutConstraint;
	import com.degrafa.states.IDegrafaStateClient;
	import com.degrafa.states.State;
	import com.degrafa.states.StateManager;
	import com.degrafa.transform.ITransform;
	import com.degrafa.triggers.ITrigger;
	
	import flash.display.DisplayObject;
	import flash.display.Graphics;
	import flash.display.Sprite;
	import flash.display.Stage;
	import flash.events.Event;
	import flash.filters.BitmapFilter;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;
	
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.styles.ISimpleStyleClient;
	
	[DefaultProperty("geometry")]
	[Bindable(event="propertyChange")]
		
	/**
 	*  A geometry object is a type of Degrafa object that enables 
 	*  rendering to a graphics context. Degrafa provides a number of 
 	*  ready-to-use geometry objects. All geometry objects inherit 
 	*  from the Geometry class. All geometry objects have a default data
 	*  property that can be used for short hand property setting.
 	**/	
	public class Geometry extends DegrafaObject implements IDegrafaObject, 
	IGeometryComposition, IDegrafaStateClient, ISimpleStyleClient {
		
		private var _invalidated:Boolean;
		/**
		* Specifies whether this object is to be re calculated 
		* on the next cycle. Only property updates which affect the 
		* computation of this object set this property
		**/
		public function get invalidated():Boolean{
			return _invalidated;
		}
		public function set invalidated(value:Boolean):void{
			if(_invalidated != value){
				_invalidated = value;
				
				if(_invalidated && _isRootGeometry){
					drawToTargets();
				}
			}
		}
		
		/**
		* Returns true if this Geometry object is invalidated
		**/
		public function get isInvalidated():Boolean{
			return _invalidated;
		} 
								
		private var _data:Object;
		/**
		* Allows a short hand property setting that is 
		* specific to and parsed by each geometry object. 
		* Look at the various geometry objects to learn what 
		* this setting requires.
		**/	
		public function get data():Object{
			return _data;
		}
		public function set data(value:Object):void{
			_data=value;
		}
		
		private var _visible:Boolean=true;
		/**
		* Controls the visibility of this geometry object. If true, the geometry is visible.
		*
		* When set to false this geometry object will be pre computed nor drawn.
		**/	
		[Inspectable(category="General", enumeration="true,false")]
		public function get visible():Boolean{
			return _visible;
		}
		public function set visible(value:Boolean):void{
			if(_visible != value){
				
				var oldValue:Boolean=_visible;
				_visible=value;
				
				invalidated = true;
				
				//call local helper to dispatch event
				initChange("visible",oldValue,_visible,this);
			}
			
		}
		
		//Dev Note :: Needed to add this speacial case as the parent in 
		//DegrafaObject is of type IDegrafaObject for type safty.
	    /**
		* Provides access to the IGraphic object parent in a nested situation.
		* Set when this object is at the root of a Degrafa 
		* IGraphic object such as GeometryGroup.  
		**/
		private var _IGraphicParent:IGraphic;
	    public function get IGraphicParent():IGraphic{
	    	return _IGraphicParent;
	    }
	    public function set IGraphicParent(value:IGraphic):void{
	    	if (parent==null){
	    		if(_IGraphicParent != value){
	    			_IGraphicParent=value;
	    		}
	    	} 
	    }
	    
	    
	    
	    
		private var _inheritStroke:Boolean=true;
		/**
		* If set to true and no stroke is defined and there is a parent object
		* then this object will walk up through the parents to retrive a stroke 
		* object. 
		**/
		[Inspectable(category="General", enumeration="true,false")]
		public function get inheritStroke():Boolean{
			return _inheritStroke;
		} 
		public function set inheritStroke(value:Boolean):void{
			_inheritStroke=value;
		}
		
		private var _inheritFill:Boolean=true;
		/**
		* If set to true and no fill is defined and there is a parent object
		* then this object will walk up through the parents to retrive a fill 
		* object. 
		**/
		[Inspectable(category="General", enumeration="true,false")]
		public function get inheritFill():Boolean{
			return _inheritFill;
		} 
		public function set inheritFill(value:Boolean):void{
			_inheritFill=value;
		}
		
		
		private var _scaleOnLayout:Boolean=true;
		/**
		* When using layout this flag will determine if you want 
		* Scale to be applied to fit layout rules
		**/	
		[Inspectable(category="General", enumeration="true,false")]
		public function get scaleOnLayout():Boolean{
			return _scaleOnLayout;
		}
		public function set scaleOnLayout(value:Boolean):void{
			_scaleOnLayout=value;
		}
		
		private var _autoClearGraphicsTarget:Boolean=true;
		/**
		* When using a graphicsTarget and if this property is set to true 
		* the draw phase will clear the graphics context before drawing.
		**/	
		[Inspectable(category="General", enumeration="true,false")]
		public function get autoClearGraphicsTarget():Boolean{
			return _autoClearGraphicsTarget;
		}
		public function set autoClearGraphicsTarget(value:Boolean):void{
			_autoClearGraphicsTarget=value;
		}
		
		private var _graphicsTarget:DisplayObjectCollection;
		[Inspectable(category="General", arrayType="flash.display.DisplayObject")]
		[ArrayElementType("flash.display.DisplayObject")]
		/**
		* One or more display object's that this Geometry is to be drawn to. 
		* During the drawing phase this is tested first. If items have been defined 
		* the drawing of the geometry is done on each item(s) graphics context. 
		**/
		public function get graphicsTarget():Array{
			initGraphicsTargetCollection();
			return _graphicsTarget.items;
		}
		public function set graphicsTarget(value:Array):void{
			
			if(!value){return;}
			
			var item:Object;
			for each (item in value){
				if (!item){return;} 
			}
			
			//make sure we don't set anything until all target creation is 
			//complete otherwise we will be getting null items since flex
			//has not finished creation of the target items.
			initGraphicsTargetCollection();
			_graphicsTarget.items = value;
			
			var displayObject:DisplayObject;
			for each (displayObject in _graphicsTarget.items ){
				//for now this process does not include skins
				//to be investigated post b3.
				if(!(displayObject is IGraphicSkin)){
					//only need to call on first render of each target
					//dev note :: does not support runtime addition of targets. To Investigate.
					displayObject.addEventListener(Event.RENDER,onTargetRender);
					
					//required for stand alone player.
					displayObject.addEventListener(Event.ADDED_TO_STAGE,onTargetRender);
					
					if(displayObject is UIComponent){
						displayObject.addEventListener(FlexEvent.UPDATE_COMPLETE,onTargetRender);
					}
				}
			}
			_isRootGeometry = true;
		
			
		}
		
		//Method Queue Work
		
		/**
		* NOTE :: All this code can be moved into the DisplayObjectCollection post b3. 
		* This way only targets that have changed are drawn to.
		**/
		private function onTargetRender(event:Event):void{
			
			//update local stage property
			if(!_stage){
				_stage = event.currentTarget.stage;
			}
			
			if(_stage){
				
				//remove the event listeners no longer needed
				event.currentTarget.removeEventListener(Event.RENDER,onTargetRender);
				event.currentTarget.removeEventListener(Event.ADDED_TO_STAGE,onTargetRender);
				
				//we may want to do this only for displayobject containers
				if(event.currentTarget is UIComponent){
					event.currentTarget.removeEventListener(FlexEvent.UPDATE_COMPLETE,onTargetRender);
				}
				
			}
			else{
				return;
			}

			//and setup the layoutChange watcher for the target
			
			//Only do this for IContainer. Requires extensive testing.
			if(event.currentTarget is UIComponent){
				initLayoutChangeWatcher(event.currentTarget as UIComponent);
			}
			
			//init the draw que
			initDrawQueue();
			
			//make sure we have not missed one draw cycle chance.
			
			//add a draw to the que.
			queueDraw(event.currentTarget,event.currentTarget.graphics,null)
			
		}
		
		//target stage refference
		private var _stage:Stage;
		
		private var methodQueue:Array=[];
		private function initDrawQueue():void{
			//add listener to frame change.
			_stage.addEventListener(Event.ENTER_FRAME, processMethodQueue);
		}
		
		//adds a draw to the queue
		private function queueDraw(...args):void{
			
			//make sure we are not already queued up to draw 
			//to that target otherwise add it
			
			//DEV note: could improve perf here
			for each(var item:Object in methodQueue){
				//we only want to add it one time
				if(item.args[0] == args[0]){
					return;
				}
			}
			
			methodQueue.push({method:drawToTarget, args:args});
				
			if(_stage){	
				_stage.addEventListener(Event.ENTER_FRAME, processMethodQueue,false,0,true);
			}
			else{
				//could have been added runtime
				if(graphicsTarget.length){
					if(graphicsTarget[0].stage){
						_stage = graphicsTarget[0].stage;
						_stage.addEventListener(Event.ENTER_FRAME, processMethodQueue,false,0,true);
					}
				}
			}
		}
		
		
		private function processMethodQueue(event:Event):void{
			
			if(methodQueue.length == 0){return;}
			
			//trace("Queue LENGTH :::" + methodQue.length)
			
			// make local copy so that new calls get through
        	var queue:Array = methodQueue;
        	//methodQueue = [];
        
        	var len:int = queue.length;
			for (var i:int=0;i<len;i++){
				//do the draw
				queue[i].method.apply(null,[queue[i].args[0]]);
			}
			
			methodQueue.length = 0;
			
			queue.length=0;
			
			//no longer needed as all que items have been processed
			//will get re added in queDraw() on as needed basis.
			if(methodQueue.length==0 && _stage){
				_stage.removeEventListener(Event.ENTER_FRAME, processMethodQueue);
			}
		}
		
		//keep a local dictionary so we can compare bounds to the targets
		//enables us to only draw when absolutly required.
		//this does not include a graphics clear on the target
		//although we could eventually store a original bitmapdata and 
		//compare.
		
		private var targetDictionary:Dictionary=new Dictionary(true);
		
		//return the data stored for the given target				
		private function requestTarget(value:UIComponent):Object{
			return targetDictionary[value];
		}	
		
		private function removeTarget(value:UIComponent):void{
			delete targetDictionary[value];
		}
		
		private function addUpdateTarget(value:UIComponent,data:Object):void{
			if(!targetDictionary[value]){
				targetDictionary[value] = []
				targetDictionary[value].data = data;
			}
			else{
				targetDictionary[value].data = data;
			}
		}
		
		//this will need to be called in the layout constraint setter as well.
		private function initLayoutChangeWatcher(container:UIComponent):void{
			
			//if this root geometry has a constraint based layout
			//we have to be aware of changes to the target. In this 
			//case we only care about width and height for now. We also only care
			//if the target is also using a contraint based layout.
			
			var bounds:Rectangle = new Rectangle(container.x,container.y,
			container.width,container.height);
			
			//add a watcher for each property
			var watchers:Array=[]
			
			//could use some work here to only watch specific items related 
			//to the current layout post b3.
			watchers.push(ChangeWatcher.watch(container,"width",onTargetChange,true));
			watchers.push(ChangeWatcher.watch(container,"height",onTargetChange,true));
			watchers.push(ChangeWatcher.watch(container,"x",onTargetChange,true));
			watchers.push(ChangeWatcher.watch(container,"y",onTargetChange,true));
			
			//only add if not there
			if(!requestTarget(container)){
				addUpdateTarget(container,{oldbounds:bounds,watchers:watchers})	
			}
			
		}
						
		private function onTargetChange(event:Event):void{
			
			//see if it's there
			var object:Object = requestTarget(event.currentTarget as UIComponent);
			
			var watchers:Array; 
			if(object){
				watchers = object.data.watchers;
			}
			
			//if we have no layout on this root object and the 
			//first render pass is complete we can clean up the
			//watchers and exit
			if(object){
				if(!hasLayout){
									
					var changeWatcher:ChangeWatcher;
					for each (changeWatcher in watchers){
						changeWatcher.unwatch();
					}
					
					removeTarget(event.currentTarget as UIComponent)
					
					return;
					
				}
			}
			
			//compare and update the dictionary
			if(object){
				var container:UIComponent = event.currentTarget as UIComponent;
				
				//compare the 2 testing will show if we need to do this test 
				//as it may only get the event if it has changed. Though in some cases
				//for example a canvas with a border may have been inited with the 
				//correct bounds
				
				//get the current bounds				
				var bounds:Rectangle = new Rectangle(container.x,container.y,
				container.width,container.height);
				
				//compare
				if(!bounds.equals(object.data.oldbounds)){
					//trace("Requires Redraw::::");
					
					//update with the new bounds for next compare
					addUpdateTarget(container,{oldbounds:bounds,watchers:watchers})
					
					//add a draw to the que.
					queueDraw(container,container.graphics,null)
					
				}
			}
		}
		
		//property that specifies if this is the root geom 
		//object and no other parent geometries exist. Usually 
		//the only on with a target array and set in the targets 
		//setter
		private var _isRootGeometry:Boolean=false;
		/**
		* Returns true if this Geometry object is a root Geometry Object.
		**/
		public function get isRootGeometry():Boolean{
			return _isRootGeometry;
		}
										
		/**
		* Access to the Degrafa target collection object for this geometry object.
		**/
		public function get graphicsTargetCollection():DisplayObjectCollection{
			initGraphicsTargetCollection();
			return _graphicsTarget;
		}
		
		/**
		* Initialize the target graphics collection by creating it and adding an event listener.
		**/
		private function initGraphicsTargetCollection():void{
			if(!_graphicsTarget){
				_graphicsTarget = new DisplayObjectCollection();
				
				//add a listener to the collection
				if(enableEvents){
					_graphicsTarget.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
				}
			}
		}
		
		
		//draws to a single target
		/**
		* @private
		**/
		private  function drawToTarget(target:Object):void{
			if(target){
				if(autoClearGraphicsTarget){
					target.graphics.clear();
				}
				_currentGraphicsTarget = target as Sprite;
				draw(target.graphics,null);				
			}
		}
		
		/**
		* Clears all graphics targets specified in the graphicsTarget array.
		**/
		public function clearGraphicsTargets():void{
    		if(graphicsTarget){
				for each (var target:Object in graphicsTarget){
					if(target){
						target.graphics.clear();
					}
				}
			}
	    }
		
		/**
		* Requests a draw for each graphics target specified in the graphicsTarget array.
		**/
		public function drawToTargets():void{
			
			if(_graphicsTarget){
				for each (var target:Object in _graphicsTarget.items){
					queueDraw(target,target.graphics,null)
				}
			}
			_currentGraphicsTarget=null;
		}
		
		private var _geometry:GeometryCollection;
		[Inspectable(category="General", arrayType="com.degrafa.IGeometryComposition")]
		[ArrayElementType("com.degrafa.IGeometryComposition")]
		/**
		* A array of IGeometryComposition objects. 	
		**/
		public function get geometry():Array{
			initGeometryCollection();
			return _geometry.items;
		}
		public function set geometry(value:Array):void{
			initGeometryCollection();
			_geometry.items = value;
		}
		
		/**
		* Access to the Degrafa geometry collection object for this geometry object.
		**/
		public function get geometryCollection():GeometryCollection{
			initGeometryCollection();
			return _geometry;
		}
		
		/**
		* Initialize the geometry collection by creating it and adding an event listener.
		**/
		private function initGeometryCollection():void{
			if(!_geometry){
				_geometry = new GeometryCollection();
				
				//add the parent so it can be managed by the collection
				_geometry.parent = this;
				
				//add a listener to the collection
				if(enableEvents){
					_geometry.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
				}
			}
		}
				
		protected var _stroke:IGraphicsStroke;
		/**
		* Defines the stroke object that will be used for 
		* rendering this geometry object.
		**/
		public function get stroke():IGraphicsStroke{
			return _stroke;
		}
		public function set stroke(value:IGraphicsStroke):void{
			if(_stroke != value){
				
				var oldValue:Object=_stroke;
				
				
				if(_stroke){
					if(_stroke.hasEventManager){
						_stroke.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
					}
				}
				
				_stroke = value;
				
				if(enableEvents && _stroke){	
					_stroke.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler,false,0,true);
				}
				
				//call local helper to dispatch event
				initChange("stroke",oldValue,_stroke,this);
				
			}	
		}
		
		protected var _fill:IGraphicsFill;
		/**
		* Defines the fill object that will be used for 
		* rendering this geometry object.
		**/
		public function get fill():IGraphicsFill{
			return _fill;
		}
		public function set fill(value:IGraphicsFill):void{
						
			if(_fill != value){
				
				
				var oldValue:Object=_fill;
				
				if(_fill){
					if(_fill.hasEventManager){
						_fill.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
					}
				}
								
				_fill = value;
				
				if(enableEvents){	
					_fill.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler,false,0,true);
				}
												
				//call local helper to dispatch event
				initChange("fill",oldValue,_fill,this);
				
			}	
			
		}
		
		/**
		* Principle event handler for any property changes to a 
		* geometry object or it's child objects.
		**/
		protected function propertyChangeHandler(event:PropertyChangeEvent):void{
			
			if (!parent){
				dispatchEvent(event);
				drawToTargets();	
			}
			else{
				dispatchEvent(event)
			}
		
		}
		
		
		/**
		* Ends the draw phase for geometry objects.
		* 
		* @param graphics The current Graphics context being drawn to. 
		**/		
		public function endDraw(graphics:Graphics):void {
			
			if (fill) {  
				//force a null stroke before closing the fill - 
				//prevents a 'closepath' stroke for unclosed paths
				graphics.lineStyle.apply(graphics, null);  
	        	fill.end(graphics);  
	        } 
			
			//append a null moveTo following a stroke without a  fill 
			//forces a break in continuity with moveTo before the next 
			//path - if we have the last point coords we could use them 
			//instead of null, null or perhaps any value
			if (stroke && !fill) graphics.moveTo.call(graphics, null, null); 

	        //draw children
	        if (geometry){
				for each (var geometryItem:IGeometryComposition in geometry){
					geometryItem.draw(graphics,null);
				}
			}
			CommandStack.unstackAlpha();
			dispatchEvent(new DegrafaEvent(DegrafaEvent.RENDER));
		}		
		
		/**
		* Initialise the stroke for this geometry object. Typically only called by draw 
		* 
		* @param graphics The current context to draw to.
		* @param rc A Rectangle object used for fill bounds.  
		**/
		public function initStroke(graphics:Graphics,rc:Rectangle):void{
			
			//this will only be done one time unless no stroke is found
			if(parent){
				if(inheritStroke && !_stroke && parent is Geometry){
					_stroke = Geometry(parent).stroke;
				}
			}
			
			//setup the stroke
			if (_stroke) {
					//same approach as used for fills: it's required for transform inheritance by some strokes
				if (_stroke is ITransformablePaint) (_stroke as ITransformablePaint).requester = this;
	        	_stroke.apply(graphics, (rc)? rc:null);
				CommandStack.currentStroke = _stroke;
	        }
			else{
				graphics.lineStyle();
				CommandStack.currentStroke = null;
			}
		}
		
		/**
		* Initialise the fill for this geometry object. Typically only called by draw 
		* 
		* @param graphics The current context to draw to.
		* @param rc A Rectangle object used for fill bounds.  
		**/
		public function initFill(graphics:Graphics,rc:Rectangle):void{
			
			
			//this will only be done one time unless no fill is found
			if(parent){
				if(inheritFill && !_fill && parent is Geometry){
					_fill = Geometry(parent).fill;
				}
			}
				
			//setup the fill
	        if (_fill)
	        {   
				//we can't pass a reference to the requesting Geometry in the method signature with IFill - its required for transform inheritance by some fills
				if (_fill is ITransformablePaint) (_fill as ITransformablePaint).requester = this;
	        	_fill.begin(graphics, (rc) ? rc:null);	
				CommandStack.currentFill = _fill;
	        } else CommandStack.currentFill = null;
	        
		}
								
		/**
		* The tight bounds of this element as represented by a Rectangle.
		* The value does not include children. 
		**/
		public function get bounds():Rectangle{
			return commandStack.bounds;
		}
		
		/**
		* Returns a transformed version of this objects bounds as 
		* represented by a Rectangle. If no transform is specified 
		* bounds is returned. The value does not include children.
		**/
		public function get transformBounds():Rectangle{
			return commandStack.transformBounds;
		}
		
		/**
		* @private
		**/
		protected var _layoutRectangle:Rectangle;
		
		/**
		* Returns the constraint based layout rectangle for this object 
		* or bounds if no layout constraint is specified.
		**/
		public function get layoutRectangle():Rectangle{
			return (_layoutRectangle)? _layoutRectangle : bounds;
		} 
			
		/**
		* Returns the point at t(0-1) on this object.
		**/
		public function pointAt(t:Number):Point{
			return commandStack.pathPointAt(t);
		}
		
		/**
		* Returns the angle of a point t(0-1) on the path.
		**/
		public function angleAt(t:Number):Number {
			return commandStack.pathAngleAt(t);
		}
		
		/**
		* Returns geometric length of this object. The value does not 
		* include children.
		**/
		public function get geometricLength():Number{
			return commandStack.pathLength;
		}
						
		/**
		* Performs any pre calculation that is required to successfully render 
		* this element. Including bounds calculations and lower level drawing 
		* command storage. Each geometry object overrides this 
		* and is responsible for it's own pre calculation cycle.
		**/
		public function preDraw():void{
			//overriden by subclasses
		}
				
		private var _commandStack:CommandStack;
		/**
		* Provides access to the command stack. 
		**/
		public function get commandStack():CommandStack{
			
			if(!_commandStack)
				_commandStack = new CommandStack(this);
						
			return _commandStack;
		}	
		public function set commandStack(value:CommandStack):void{
			_commandStack=value;
		}
		
		/**
		* @private
		* The current graphics target being rendered to.
		**/
		public var _currentGraphicsTarget:Sprite;
		
		/**
		* Access to the layout matrix if this Geometry has layout.
		**/
		public var _layoutMatrix:Matrix;
		
		/**
		* Performs the layout calculations if required. 
		* All geometry override this for specifics.
		* 
		* @param childBounds the bounds to be layed out. If not specified a rectangle
		* of (0,0,1,1) is used.
		**/
		public function calculateLayout(childBounds:Rectangle=null):void{

			if(_layoutConstraint){
					
				//setup default
				if(!childBounds){
					childBounds = new Rectangle(0,0,1,1);
				}
				
				//either the layout rect of the parent or the bounds 
				//if no layout depending on the way the nesting is setup.
				//so store this as we go through the tests.
				var idealParentRectangle:Rectangle;
									
				//if we have a geometry parent then layout to bounds or layout rectangle
				//is our first test layoutRectangle call will return either the layout 
				//rectangle or the bounds if no layout is set.
				if(parent && parent is Geometry){
					//if no valid rect then walk up the stack and try to find one
					//until the parent is null meaning the root geometry.
					var currParent:Geometry = Geometry(parent);
					var lastParent:Geometry;
					while(currParent && !idealParentRectangle){
						//CHANGED THE BELOW FROM THIS
						if(!Geometry(parent).layoutRectangle.isEmpty()){
						//TO THIS
						//if(!isNaN(Geometry(parent).layoutRectangle.x) && !isNaN(Geometry(parent).layoutRectangle.y)){
							idealParentRectangle = currParent.layoutRectangle;
						}
						else{
							//store the last parent reached for next test step
							lastParent = currParent;
							currParent = currParent.parent as Geometry;
						}
					}
				}
				
				//this test step will test the last found parent to see if it has a 
				//IGraphicParent and attemp to use that unless the last parent has a
				//_currentGraphicsTarget.
				
				//DEV Note:: should walk the geom groups eventually as well to find the next 
				//parent if empty bounds.
				if(lastParent && !idealParentRectangle){
					if(!lastParent._currentGraphicsTarget){
						if(lastParent.IGraphicParent){
							
							//we are not the root object and we are not doing the drawing
							//so we can try to get the bounds from the IGraphic
							var graphicDisplayObject:DisplayObject = lastParent.IGraphicParent as DisplayObject;
							
							var iGraphicsRect:Rectangle = graphicDisplayObject.getRect(graphicDisplayObject);
							
							if(iGraphicsRect.isEmpty()){
								if(graphicDisplayObject.width !=0 || graphicDisplayObject.height !=0){
									iGraphicsRect.x=graphicDisplayObject.x;
									iGraphicsRect.y=graphicDisplayObject.y;
									iGraphicsRect.width=graphicDisplayObject.width;
									iGraphicsRect.height=graphicDisplayObject.height;
								}
							}
							
							//test for empty here as even that could have nothing set.
							if(!iGraphicsRect.isEmpty()){
								idealParentRectangle=iGraphicsRect.clone();
							}
							
						}
					}
				} 
				
				
				//drawing to a _currentGraphicsTarget attempt to use that.
				if(_currentGraphicsTarget && !idealParentRectangle){
					var graphicsTargetRect:Rectangle = _currentGraphicsTarget.getRect(_currentGraphicsTarget);
					//if empty try explicit as the target may not have anything in it
					//This can happen when you have a percent width/heigh on a canvas and it has no 
					//fill nor border in these cases there is no update event.
					if(graphicsTargetRect.isEmpty()){
						if(_currentGraphicsTarget.width !=0 || _currentGraphicsTarget.height !=0){
							graphicsTargetRect.x=_currentGraphicsTarget.x;
							graphicsTargetRect.y=_currentGraphicsTarget.y;
							graphicsTargetRect.width=_currentGraphicsTarget.width;
							graphicsTargetRect.height=_currentGraphicsTarget.height;
						}
					}
					if(graphicsTargetRect){
						idealParentRectangle=graphicsTargetRect.clone();
					}
				}
				
				//add more rules here or above.
				
				//handle skins this way as they will not always follow the above rules. 
				if(!idealParentRectangle){
					var iGraphicsSkinRect:Rectangle = new Rectangle();
					
					if(lastParent){
						if(lastParent.graphicsTarget.length!=0){
							if (lastParent.graphicsTarget[0] is IGraphicSkin){
								iGraphicsSkinRect.x=lastParent.graphicsTarget[0].x;
								iGraphicsSkinRect.y=lastParent.graphicsTarget[0].y;
								iGraphicsSkinRect.width=lastParent.graphicsTarget[0].width;
								iGraphicsSkinRect.height=lastParent.graphicsTarget[0].height;
							}
							if(iGraphicsSkinRect){
								idealParentRectangle=iGraphicsSkinRect.clone();
							}
						}
					}
					else{
						//if the first graphics taregt is a IGraphicSkin
						if(graphicsTarget.length!=0){
							if (graphicsTarget[0] is IGraphicSkin){
								iGraphicsSkinRect.x=graphicsTarget[0].x;
								iGraphicsSkinRect.y=graphicsTarget[0].y;
								iGraphicsSkinRect.width=graphicsTarget[0].width;
								iGraphicsSkinRect.height=graphicsTarget[0].height;
							}
							if(iGraphicsSkinRect){
								idealParentRectangle=iGraphicsSkinRect.clone();
							}
						}
					}
				}			
				
				//fall back to the document as a last effort 
				if(document && !idealParentRectangle){    //Sometimes the document will not be a display object (like a fill described via MXML)
					idealParentRectangle = new Rectangle(document.x,
					document.y,document.width,document.height);
				}
				
				//finally apply it
				_layoutConstraint.computeLayoutRectangle(childBounds,idealParentRectangle);
				
			}
		}
		
		
		
		/**
		* Begins the draw phase for geometry objects. All geometry objects 
		* override this to do their specific rendering.
		* 
		* @param graphics The current context to draw to.
		* @param rc A Rectangle object used for fill bounds. 
		**/
		public function draw(graphics:Graphics,rc:Rectangle):void{
	
			//don't draw unless visible
			if (!visible) { return; }
			
			//stack the current alpha value
			CommandStack.stackAlpha(alpha);
			
			//Exit if no context specified. Calling draw(null, null) on a geometry
			//can now be used as a pre calculation phase where the predraw and layout will 
			//be done but the object will not be rendered. Also avoids the rte when called
			//with a graphics of null.
			if(!graphics){return;}
			
			//endDraw if not specifically denied from commandStack
			if (!commandStack.draw(graphics,rc)) endDraw(graphics);

  		}		
  		
  		//Decoration related.
  		
  		/**
		* Returns true if this Geometry has decorators.
		**/
  		public var hasDecorators:Boolean;
  		
  		private var _decorators:DecoratorCollection;
		[Inspectable(category="General", arrayType="com.degrafa.decorators.IDecorator")]
		[ArrayElementType("com.degrafa.decorators.IDecorator")]
		/**
		* A array of IDecorator objects to be applied on this Geometry.
		**/
		public function get decorators():Array{
			initDecoratorsCollection();
			return _decorators.items;
		}
		public function set decorators(value:Array):void{			
			initDecoratorsCollection();
			_decorators.items = value;
			
			if(value && value.length!=0){
				hasDecorators = true;
			}
			else{
				hasDecorators = false;
			}
			
		}
		
		/**
		* Access to the Decorator collection object for this Geometry object.
		**/
		public function get decoratorCollection():DecoratorCollection{
			initDecoratorsCollection();
			return _decorators;
		}
		
		/**
		* Initialize the collection by creating it and adding an event listener.
		**/
		private function initDecoratorsCollection():void{
			if(!_decorators){
				_decorators = new DecoratorCollection();
				
				//add a listener to the collection
				if(enableEvents){
					_decorators.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
				}
			}
		}
		
		//END Decoration related.
  		
  		//Transform related.
  		
  		
		private var _transformContext:Matrix;
		/**
		* A reference to the transformation matrix context within which local transforms will be applied.
		* Similar in concept to the concatenatedMatrix on a flash DisplayObjects transform property.
		*/
		public function get transformContext():Matrix{
			return _transformContext;
		}
		public function set transformContext(value:Matrix):void{
			_transformContext = value;
		}
		
		private var _transform:ITransform;
		/**
		* Defines the transform object that will be used for 
		* rendering this geometry object.
		**/
		public function get transform():ITransform{
			return _transform;
		}
		public function set transform(value:ITransform):void
		{
			//get a reference to the transform hierachy
			if (parent && (parent as Geometry).transform)
			{
				_transformContext = (parent as Geometry).transform.getTransformFor(parent as Geometry);
			} 
			if(_transform != value){
			
				var oldValue:Object=_transform;
			
				if(_transform){
					if(_transform.hasEventManager){
						_transform.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
					}
				}
								
				_transform = value;
				
				if(enableEvents){	
					_transform.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler,false,0,true);
				}
				//call local helper to dispatch event
				initChange("transform",oldValue,_transform,this);
			}
			
		}
		//END Transform related.
  		
  		
  		//Layout related.
  		
  		/**
  		* @private
  		**/ 		
  		protected var _layoutConstraint:LayoutConstraint;
		
		/**
		* The layout constraint that is used for positioning/sizing this geometry object.
		**/
		public function get layoutConstraint():LayoutConstraint{
			if(!_layoutConstraint){
				layoutConstraint= new LayoutConstraint();
			}
			return _layoutConstraint;
		}
		public function set layoutConstraint(value:LayoutConstraint):void{
						
			if(_layoutConstraint != value){
				var oldValue:Object=_layoutConstraint;
				
				if(_layoutConstraint){
					if(_layoutConstraint.hasEventManager){
						_layoutConstraint.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
					}
				}
								
				_layoutConstraint = value;
				
				if(enableEvents){	
					_layoutConstraint.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler,false,0,true);
				}
												
				//call local helper to dispatch event
				initChange("layoutConstraint",oldValue,_layoutConstraint,this);
				
				hasLayout=true;
				
			}	
			
		}
				
		/**
		* Returns true if this Geometry has layout.
		**/
		public var hasLayout:Boolean;
				
		//START LAYOUT PROXY PROPERTIES ::
		//The below are proxy properties for contraint based layout. Depending on the 
		//object some of these are overrideen in the respective Geometry subclass. 
		//For example width on a RegularRectangle.
		
		//x,y,width,height are different as we need a getter and a setter
		[PercentProxy("percentWidth")]
		/**
		* Defines the width of the layout.
		* Once left (or percentLeft) or right (or percentRight)
		* is set, the width value no longer applies. If
		* percentWidth exists when width is set, percentWidth
		* will be overridden and be given a value of NaN. This 
		* property also accepts a percent value for example 75%.
		*/
		public function get width():Number{
			return (hasLayout)? _layoutConstraint.width:NaN;
		}
		public function set width(value:Number):void{
			layoutConstraint.width = value;
		}
		
		/**
		* When set, the width of the layout will be
		* set as the value of this property multiplied
		* by the containing width.
		* A value of 0 represents 0% and 1 represents 100%
		* a value of 75 represents 75%.
		*/
		public function get percentWidth():Number{
			return (hasLayout)? layoutConstraint.percentWidth:NaN;
		}
		public function set percentWidth(value:Number):void{
			layoutConstraint.percentWidth = value;
		}
		
		/**
		* The maximum width that can be applied
		* to the layout.
		*/
		public function get maxWidth():Number{
			return (hasLayout)? layoutConstraint.maxWidth:NaN;
		}
		public function set maxWidth(value:Number):void{
			layoutConstraint.maxWidth = value;
		}
		
		/**
		* The minimum width that can be applied
		* to the layout.
		*/
		public function get minWidth():Number{
			return (hasLayout)? layoutConstraint.minWidth:NaN;
		}
		public function set minWidth(value:Number):void{
			layoutConstraint.minWidth = value;
		}
		
		[PercentProxy("percentHeight")]
		/**
		* Defines the height of the layout boundary.
		* Once top (or percentTop) or bottom (or percentBottom)
		* is set, the width value no longer applies. If
		* percentWidth exists when width is set, percentWidth
		* will be overridden and be given a value of NaN. This 
		* property also accepts a percent value for example 75%.
		*/
		public function get height():Number{
			return (hasLayout)? layoutConstraint.height:NaN;
		}
		public function set height(value:Number):void{
			layoutConstraint.height = value;
		}
		
		/**
		* When set, the height of the layout will be
		* set as the value of this property multiplied
		* by the containing height.
		* A value of 0 represents 0% and 1 represents 100%
		* a value of 75 represents 75%.
		*/
		public function get percentHeight():Number{
			return (hasLayout)? layoutConstraint.percentHeight:NaN;
		}
		public function set percentHeight(value:Number):void{
			layoutConstraint.percentHeight = value;
		}
		
		/**
		* The maximum height that can be applied
		* to the layout.
		*/
		public function get maxHeight():Number{
			return (hasLayout)? layoutConstraint.maxHeight:NaN;
		}
		public function set maxHeight(value:Number):void{
			layoutConstraint.maxHeight = value;
		}
		
		/**
		* The minimum height that can be applied
		* to the layout.
		*/
		public function get minHeight():Number{
			return (hasLayout)? layoutConstraint.minHeight:NaN;
		}
		public function set minHeight(value:Number):void{
			layoutConstraint.minHeight = value;
		}
		
		/**
		* Defines the x location (top left) of the layout.
		*/
		public function get x():Number{
			return (hasLayout)? layoutConstraint.x:NaN;
		}
		public function set x(value:Number):void{
			layoutConstraint.x = value;
		}
		
		/**
		* The maximum x location that can be applied
		* to the layout.
		*/
		public function get maxX():Number{
			return (hasLayout)? layoutConstraint.maxX:NaN;
		}
		public function set maxX(value:Number):void{
			layoutConstraint.maxX = value;
		}
		
		/**
		* The minimum x location that can be applied
		* to the layout.
		*/
		public function get minX():Number{
			return (hasLayout)? layoutConstraint.minX:NaN;
		}
		public function set minX(value:Number):void{
			layoutConstraint.minX = value;
		}
		
		/**
		* Defines the y location (top left) of the layout.
		*/
		public function get y():Number{
			return (hasLayout)? layoutConstraint.y:NaN;
		}
		public function set y(value:Number):void{
			layoutConstraint.y = value;
		}
		
		/**
		* The maximum y location that can be applied
		* to the layout.
		*/
		public function get maxY():Number{
			return (hasLayout)? layoutConstraint.maxY:NaN;
		}
		public function set maxY(value:Number):void{
			layoutConstraint.maxY = value;
		}
		
		/**
		* The minimum y location that can be applied
		* to the layout.
		*/
		public function get minY():Number{
			return (hasLayout)? layoutConstraint.minY:NaN;
		}
		public function set minY(value:Number):void{
			layoutConstraint.minY = value;
		}
		
		/**
		 * When set, if left or right is not set, the layout
		 * will be centered horizontally offset by the numeric
		 * value of this property.
		 */
		public function get horizontalCenter():Number{
			return (hasLayout)? layoutConstraint.horizontalCenter:NaN;
		}
		public function set horizontalCenter(value:Number):void{
			layoutConstraint.horizontalCenter = value;
		}
		
		/**
		* When set, if top or bottom is not set, the layout
		* will be centered vertically offset by the numeric
		* value of this property.
		*/
		public function get verticalCenter():Number{
			return (hasLayout)? layoutConstraint.verticalCenter:NaN;
		}
		public function set verticalCenter(value:Number):void{
			layoutConstraint.verticalCenter = value;
		}
		
		/**
		* When set, the top of the layout will be located
		* offset from the top of it's parent.
		*/
		public function get top():Number{
			return (hasLayout)? layoutConstraint.top:NaN;
		}
		public function set top(value:Number):void{
			layoutConstraint.top = value;
		}
		
		/**
		* When set, the bottom of the layout will be located
		* offset from the bottom of it's parent.
		*/
		public function get bottom():Number{
			return (hasLayout)? layoutConstraint.bottom:NaN;
		}
		public function set bottom(value:Number):void{
			layoutConstraint.bottom = value;
		}
		
		/**
		 * When set, the left of the layout will be located
		 * offset by the value of this property multiplied
		 * by the containing width.
		 */
		public function get left():Number{
			return (hasLayout)? layoutConstraint.left:NaN;
		}
		public function set left(value:Number):void{
			layoutConstraint.left = value;
		}
		
		/**
		* When set, the right of the layout will be located
		* offset by the value of this property multiplied
		* by the containing width.
		*/
		public function get right():Number{
			return (hasLayout)? layoutConstraint.right:NaN;
		}
		public function set right(value:Number):void{
			layoutConstraint.right = value;
		}
		
		[Inspectable(category="General", enumeration="true,false")]
		/**
		* When true, the size of the layout will always
		* maintain an aspect ratio relative to the ratio
		* of the current width and height properties, even
		* if those properties are not in control of the
		* height and width of the layout.
		*/
		public function get maintainAspectRatio():Boolean{
			return (hasLayout)? layoutConstraint.maintainAspectRatio:false;
		}
		public function set maintainAspectRatio(value:Boolean):void{
			layoutConstraint.maintainAspectRatio = value;
		}
		
		/**
		* The display object that defines the coordinate system to use.
		* Dev Note:: Not yet implemented as of Beta 3. 
		**/
		public function get targetCoordinateSpace():DisplayObject{
			return (hasLayout)? layoutConstraint.targetCoordinateSpace:null;
		}
		public function set targetCoordinateSpace(value:DisplayObject):void {
			layoutConstraint.targetCoordinateSpace = value;
		}
		
		//END LAYOUT PROXY PROPERTIES
		
		//END Layout related.
  		
  		//Trigger related.
  		
  		/**
		* Returns true if this Geometry has triggers.
		**/
  		public var hasTriggers:Boolean;
  		
  		private var _triggers:Array= [];
	    [Inspectable(arrayType="com.degrafa.triggers.ITrigger")]
	    [ArrayElementType("com.degrafa.triggers.ITrigger")]
	    /**
	    * An array of ITrigger objects that this Geometry object will use.
	    **/
	    public function get triggers():Array{
	    	return _triggers;
	    }
	    public function set triggers(value:Array):void{
	    	_triggers = value;
	    	
	    	if(_triggers){
		    	//make sure each item knows about it's manager
	    		for each (var trigger:ITrigger in _triggers){
	    			trigger.triggerParent = this;
	    		}
	    	}
	    	
	    	if(value && value.length!=0){
				hasTriggers = true;
			}
			else{
				hasTriggers = false;
			}
		}
	    
    	//End Trigger related.
  		
  		//State related.
  		
  		private var _currentState:String="";
	   
	    [Bindable("currentStateChange")]
	    /**
	    * The current view state.
	    **/
	    public function get currentState():String{
	        return (stateManager) ? stateManager.currentState:"";
	    }
	    
	    public function set currentState(value:String):void{
	        stateManager.currentState = value;
	    }
		
		
		private var stateManager:StateManager;
		
		/**
		* Returns true if this Geometry has states.
		**/
		public var hasStates:Boolean;
		
		private var _states:Array= [];
	    [Inspectable(arrayType="com.degrafa.states.State")]
	    [ArrayElementType("com.degrafa.states.State")]
	    /**
		* An array of states defined for this Geometry.
		**/
	    public function get states():Array{
	    	return _states;
	    }
	    public function set states(value:Array):void{
	    	
	    	_states = value;
	    	
	    	if(value){
	    		if(!stateManager){
	    			stateManager = new StateManager(this)
	    			
	    			//make sure each item knows about it's manager
		    		for each (var state:State in _states){
		    			state.stateManager = stateManager;
		    		}
		    	
	    		}
	    	}
	    	else{
	    		stateManager = null;	
	    	}
	    	
	    	if(value && value.length!=0){
				hasStates = true;
			}
			else{
				hasStates = false;
			}
	    }
	 	
	 	
		private var _state:String;
		/**
		* The state at which to draw this object. This property is specific to Skinning.
		**/
		public function get state():String{
			return _state;
		}
		public function set state(value:String):void{
			_state = value;
		}
		
		private var _stateEvent:String;
		/**
		* The state event at which to draw this object. This property is specific to Skinning.
		**/
		public function get stateEvent():String{
			return _stateEvent;
		}
		public function set stateEvent(value:String):void{
			_stateEvent = value;
		}
		
	 	//END state related.
  		
   		//Style related.
   		
  		private var _styleName:Object;
  		/**
  		* The css style name associated with this Geometry. Not yet fully implemented as of Beta 3.
  		**/
  		public function get styleName():Object{
  			return _styleName;
  		} 
   		public function set styleName(value:Object):void{
   			_styleName=value;
   		} 
		
		/**
		* Called when the value of a style property is changed.
		**/
		public function styleChanged(styleProp:String):void{
			//handle change
		} 
		
  		//END Style related.
  		
   		//Filter / Display object related. 
  		 
  		//Any setting of the below items indicate a requirement for a display
  		//object to be used at render time.
  		
  		/**
		* Returns true if this Geometry has filters.
		**/
  		public var hasFilters:Boolean;
  		
   		/**
		* A collection of filters to apply to the geometry.
		*/
		private var _filters:FilterCollection;
		[Inspectable(category="General", arrayType="flash.filters.BitmapFilter")]
		[ArrayElementType("flash.filters.BitmapFilter")]
		/**
		* An array of BitmapFilter objects applied to this Geometry.
		**/
		public function get filters():Array{
			initFilterCollection();
			return _filters.items;
		}
		
		public function set filters(value:Array):void {
			initFilterCollection();
			if(_filters.items != value){
				
				var oldValue:Array=_filters.items;
				_filters.items = value;
			
				//call local helper to dispatch event	
				initChange("filters",oldValue,_filters.items,this);
			}
			
			if(value && value.length!=0){
				hasFilters = true;
			}
			else{
				hasFilters = false;
			}
			
		}
	
		/**
		* Initialize the filter collection by creating it and adding an event listener.
		**/
		private function initFilterCollection():void{
			if(!_filters){
				_filters = new FilterCollection();
				//add the parent so it can be managed by the collection
				_filters.parent = this;
				
				//add a listener to the collection
				if(enableEvents){
					_filters.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
				}
			}
		}
		
		//End Filter related.
  		
  		//Blend Mode related.
  		
  		//private var _blendMode:String=undefined;
		//[Inspectable(category="General", enumeration="normal,layer,multiply,screen,lighten,darken,difference,add,subtract,invert,alpha,erase,overlay,hardlight", defaultValue="normal")]
		/**
		* The blend mode which is used to render the geometry to the target.
		*/
		/*public function get blendMode():String { 
			return _blendMode; 
		}
		public function set blendMode(value:String):void {
			if(_blendMode != value){
				
				var oldValue:String=_blendMode;
				
				_blendMode = value;
			
				//call local helper to dispatch event	
				initChange("blendMode",oldValue,_blendMode,this);
				
			}
			
		}*/
		
		//End Blend Mode related.
  		
  		//Clipping related.
  		
  		private var _clippingRectangle:Rectangle=null;
		/**
		* A clipping rectangle to use when rendering this geometry.
		*/
		public function get clippingRectangle():Rectangle { 
			return _clippingRectangle; 
		}
		public function set clippingRectangle(value:Rectangle):void {
			if(_clippingRectangle != value){
				
				var oldValue:Rectangle=_clippingRectangle;
				
				_clippingRectangle = value;
			
				//call local helper to dispatch event	
				initChange("clippingRectangle",oldValue,_clippingRectangle,this);
			}
		}
		//Clipping related.
  		
  		//Mask related.
  		private var _maskMode:String;
		private var _maskSpace:String;
  		private var _mask:IGeometryComposition;
		
		/**
		* A separate geometry object to use as a mask when rendering this geometry.
		*/
		public function get mask():IGeometryComposition { 
			return _mask; 
		}
		public function set mask(value:IGeometryComposition):void {
			if (_mask != value && value != this) {
				if (_mask) Geometry(_mask).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE,propertyChangeHandler);
				var oldValue:IGeometryComposition=_mask;
				
				_mask = value;
				Geometry(_mask).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertyChangeHandler);
				//call local helper to dispatch event	
				initChange("mask", oldValue, _mask, this);
			}
		}
		
		
		
		
		/**
		* The mode used when this object is being masked by the geometry assigned to the mask property. 
		* The value can either be "clip" or "mask". Clip mode is shape based clipping, alpha mode is alpha based masking.
		* "svgClip" is a mode that mimics svg's clip-path setting with a non-zero clip-rule and userSpaceOnUse clipping units
		*/
		[Inspectable(category="General", enumeration="alpha,clip,svgClip")]
		public function get maskMode():String { 
			return _maskMode?_maskMode:"clip"; 
		}
		public function set maskMode(value:String):void {
			if (value == "alpha" || value=="clip" || value=="svgClip") {
				//only fire propertyChange event if there is a mask assigned.
				if (_mask) initChange("maskMode", maskMode, _maskMode = value, this);
				else _maskMode = value
			}
		}
		
		/**
		* The coordinate space within which the referenced mask geometry is rendered before being applied as a mask (respecting maskMode)
		* to this object.
		*/
		[Inspectable(category="General", enumeration="local,global")]
		public function get maskSpace():String { 
			return _maskSpace?_maskSpace:"local"; 
		}
		public function set maskSpace(value:String):void {
			if (value == "local" || value=="global" ) {
				//only fire propertyChange event if there is a mask assigned.
				if (_mask) initChange("maskSpace", maskSpace, _maskSpace = value, this);
				else _maskSpace = value
			}
		}
		//End mask related.
  		
		//paint modifiers
		
		private var _alpha:Number;
		/**
		* The alpha setting that applies to this object. Actual alpha used when rendering reflects this objects parent chain alpha settings. If this object descends from other
		* geometries with alpha settings less than, the combined effect of the parent alphas is used in conjunction with the setting on this object.
		*/
		public function get alpha():Number { 
			return isNaN(_alpha)?1:_alpha; 
		}
		public function set alpha(value:Number):void {
			if (value !=_alpha ) {
				initChange("alpha", alpha, _alpha = value, this);
			}
		}
		
		//end paint modifiers
		
  	}
}