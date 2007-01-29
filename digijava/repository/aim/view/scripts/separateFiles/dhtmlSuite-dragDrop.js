if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Drag and drop class
*
*	Created:			August, 18th, 2006
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	A general drag and drop class. By creating objects of this class, you can make elements
*		 on your web page dragable and also assign actions to element when an item is dropped on it.
*		 A page should only have one object of this class.<br>
*		<br>
*		IMPORTANT when you use this class: Don't assign layout to the dragable element ids
*		Assign it to classes or the tag instead. example: If you make <div id="dragableBox1" class="aBox">
*		dragable, don't assign css to #dragableBox1. Assign it to div or .aBox instead.<br>
*		(<a href="../../demos/demo-drag-drop-1.html" target="_blank">demo 1</a>, <a href="../../demos/demo-drag-drop-2.html" target="_blank">demo 2</a>),
*		<a href="../../demos/demo-drag-drop-3.html" target="_blank">demo 3</a>, <a href="../../demos/demo-drag-drop-4.html" target="_blank">demo 4</a>
*		and <a href="../../demos/demo-drag-drop-4.html" target="_blank">demo 5</a>)
* @version				1.0
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/


DHTMLSuite.dragDrop = function()
{
	var mouse_x;					// mouse x position when drag is started
	var mouse_y;					// mouse y position when drag is started.
	
	var el_x;						// x position of dragable element
	var el_y;						// y position of dragable element
	
	var dragDropTimer;				// Timer - short delay from mouse down to drag init.
	var numericIdToBeDragged;		// numeric reference to element currently being dragged.
	var dragObjCloneArray;			// Array of cloned dragable elements. every
	var dragDropSourcesArray;		// Array of source elements, i.e. dragable elements.
	var dragDropTargetArray;		// Array of target elements, i.e. elements where items could be dropped.
	var currentZIndex;				// Current z index. incremented on each drag so that currently dragged element is always on top.
	var okToStartDrag;				// Variable which is true or false. It would be false for 1/100 seconds after a drag has been started.
									// This is useful when you have nested dragable elements. It prevents the drag process from staring on
									// parent element when you click on dragable sub element.
	var moveBackBySliding;			// Variable indicating if objects should slide into place moved back to their location without any slide animation.
	var dragX_allowed;				// Possible to drag this element along the x-axis?
	var dragY_allowed;				// Possible to drag this element along the y-axis?
	
	var currentEl_allowX;
	var currentEl_allowY;
	var drag_minX;
	var drag_maxX;
	var drag_minY;
	var drag_maxY;
	var dragInProgress;				// Variable which is true when drag is in progress, false otherwise
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	this.dragX_allowed = true;
	this.dragY_allowed = true;
	this.currentZIndex = 21000;
	this.dragDropTimer = -1;
	this.dragObjCloneArray = new Array();
	this.numericIdToBeDragged = false;	

	this.okToStartDrag = true;
	this.moveBackBySliding = true;	  
	this.dragInProgress = false;
	
	var objectIndex;
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
		
}

DHTMLSuite.dragDrop.prototype = {
	
	// {{{ init()
    /**
     * Initialize the script
     * This method should be called after you have added sources and destinations.
     * 
     * @public
     */	
	init : function()
	{
		this.__initDragDropScript();	

	}
	// }}}	
	,
	// {{{ addSource()
    /**
     * Add dragable element
     *
     * @param String sourceId = Id of source
     * @param boolean slideBackAfterDrop = Slide the item back to it's original location after drop.
     * @param boolean xAxis = Allowed to slide along the x-axis(default = true, i.e. if omitted).
     * @param boolean yAxis = Allowed to slide along the y-axis(default = true, i.e. if omitted).
     * @param String dragOnlyWithinElId = You will only allow this element to be dragged within the boundaries of the element with this id.
     * 
     * @public
     */	
	addSource : function(sourceId,slideBackAfterDrop,xAxis,yAxis,dragOnlyWithinElId)
	{
		if(!this.dragDropSourcesArray)this.dragDropSourcesArray = new Array();
		if(!document.getElementById(sourceId)){
			alert('The source element with id ' + sourceId + ' does not exists. Check your HTML code');
			return;
		}
		if(xAxis!==false)xAxis = true;
		if(yAxis!==false)yAxis = true;
		var obj = document.getElementById(sourceId);
		this.dragDropSourcesArray[this.dragDropSourcesArray.length]  = [obj,slideBackAfterDrop,xAxis,yAxis,dragOnlyWithinElId];		
		obj.setAttribute('dragableElement',this.dragDropSourcesArray.length-1);
		obj.dragableElement = this.dragDropSourcesArray.length-1;
		
	}
	// }}}	
	,
	// {{{ addTarget()
    /**
     * Add drop target
     *
     * @param String targetId = Id of drop target
     * @param String functionToCallOnDrop = name of function to call on drop. 
	 *		Input to this the function specified in functionToCallOnDrop function would be 
	 *		id of dragged element 
	 *		id of the element the item was dropped on.
	 *		mouse x coordinate when item was dropped
	 *		mouse y coordinate when item was dropped     
     * 
     * @public
     */	
	addTarget : function(targetId,functionToCallOnDrop)
	{
		if(!this.dragDropTargetArray)this.dragDropTargetArray = new Array();
		if(!document.getElementById(targetId))alert('The target element with id ' + targetId + ' does not exists.  Check your HTML code');
		var obj = document.getElementById(targetId);
		this.dragDropTargetArray[this.dragDropTargetArray.length]  = [obj,functionToCallOnDrop];		
	}
	// }}}	
	,	
	// {{{ setSlide()
    /**
     * Activate or deactivate sliding animations.
     *
     * @param boolean isSlidingAnimationEnabled = Move element back to orig. location in a sliding animation
     * 
     * @public
     */	
	setSlide : function(isSlidingAnimationEnabled)
	{
		this.moveBackBySliding = isSlidingAnimationEnabled;		
	}
	// }}}	
	,		
	/* Start private methods */
	
	// {{{ __initDragDropScript()
    /**
     * Initialize drag drop script - this method is called by the init() method.
     * 
     * @private
     */	
	__initDragDropScript : function()
	{
		var ind = this.objectIndex;
		var refToThis = this;
		var startIndex = Math.random() + '';
		startIndex = startIndex.replace('.','')/1;
		
		for(var no=0;no<this.dragDropSourcesArray.length;no++){			
			var el = this.dragDropSourcesArray[no][0].cloneNode(true);	
			var el2 = this.dragDropSourcesArray[no][0];
					
			eval("el.onmousedown =function(e,index){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__initializeDragProcess(e," + no + "); }");	
			DHTMLSuite.commonObj.__addEventElement(el);
			var tmpIndex = startIndex + no;
			// el.id = 'DHTMLSuite_dragableElement' + tmpIndex;
			el.id = el2.id;	/* Override the value above */
			el.style.position='absolute';
			el.style.visibility='hidden';
			el.style.display='none';			
			// 2006/12/02 - Changed the line below because of positioning problems.			
			document.body.appendChild(el);			
			//this.dragDropSourcesArray[no][0].parentNode.insertBefore(el,this.dragDropSourcesArray[no][0]);
			
			el.style.top = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[no][0]) + 'px';
			el.style.left = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[no][0]) + 'px';
					
			eval("el2.onmousedown =function(e,index){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__initializeDragProcess(e," + no + "); }");	
			DHTMLSuite.commonObj.__addEventElement(this.dragDropSourcesArray[no][0]);						
			this.dragObjCloneArray[no] = el; 
			
			
		}
		
		eval("DHTMLSuite.commonObj.addEvent(document.documentElement,\"mousemove\",function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__moveDragableElement(e); } )");
		eval("DHTMLSuite.commonObj.addEvent(document.documentElement,\"mouseup\",function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__stopDragProcess(e); } );");
		
		if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
		document.documentElement.ondragstart = function() { return DHTMLSuite.commonObj.cancelEvent() };		

		
		DHTMLSuite.commonObj.__addEventElement(document.documentElement);
		
	}	
	// }}}	
	,		
	// {{{ __initializeDragProcess()
    /**
     * Initialize drag process
     *
     * @param Event e = Event object, used to get x and y coordinate of mouse pointer
     * 
     * @private
     */	
	__initializeDragProcess : function(e,index)
	{
		var ind = this.objectIndex;		
		if(!this.okToStartDrag)return false; 
	
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].okToStartDrag = true;',100);
		if(document.all)e = event;

		this.numericIdToBeDragged = index;
		this.numericIdToBeDragged = this.numericIdToBeDragged + '';

		this.dragDropTimer=0;
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
		this.mouse_x = e.clientX;
		this.mouse_y = e.clientY;
		
		this.currentZIndex = this.currentZIndex + 1;		
		this.dragObjCloneArray[this.numericIdToBeDragged].style.zIndex = this.currentZIndex;		
	
		this.currentEl_allowX = this.dragDropSourcesArray[this.numericIdToBeDragged][2];
		this.currentEl_allowY = this.dragDropSourcesArray[this.numericIdToBeDragged][3];

		var parentEl = this.dragDropSourcesArray[this.numericIdToBeDragged][4];
		this.drag_minX = false;
		this.drag_minY = false;
		this.drag_maxX = false;
		this.drag_maxY = false;
		if(parentEl){
			var obj = document.getElementById(parentEl);
			if(obj){
				this.drag_minX = DHTMLSuite.commonObj.getLeftPos(obj);
				this.drag_minY = DHTMLSuite.commonObj.getTopPos(obj);
				this.drag_maxX = this.drag_minX + obj.clientWidth;
				this.drag_maxY = this.drag_minY + obj.clientHeight;				
			}		
		}			
		
		// Reposition dragable element
		if(this.dragDropSourcesArray[this.numericIdToBeDragged][1]){
			this.dragObjCloneArray[this.numericIdToBeDragged].style.top = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
			this.dragObjCloneArray[this.numericIdToBeDragged].style.left = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
		}
		this.el_x = this.dragObjCloneArray[this.numericIdToBeDragged].style.left.replace('px','')/1;
		this.el_y = this.dragObjCloneArray[this.numericIdToBeDragged].style.top.replace('px','')/1;
				
		this.__waitBeforeDragProcessStarts();
				
		return false;
	}	
	// }}}	
	,	
	// {{{ __waitBeforeDragProcessStarts()
    /**
     * A small delay from mouse down to drag starts 
     * 
     * @private
     */	
	__waitBeforeDragProcessStarts : function()
	{
		var ind = this.objectIndex;
		
		if(this.dragDropTimer>=0 && this.dragDropTimer<5){
			this.dragDropTimer = this.dragDropTimer + 1;
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__waitBeforeDragProcessStarts()',2);
			return;			
		}
		if(this.dragDropTimer>=5){
			if(this.dragObjCloneArray[this.numericIdToBeDragged].style.display=='none'){
				this.dragDropSourcesArray[this.numericIdToBeDragged][0].style.visibility = 'hidden';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.display = 'block';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.visibility = 'visible';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.top = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.left = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
			}
		}		
	}	
	// }}}	
	,
	// {{{ __moveDragableElement()
    /**
     * Move dragable element according to mouse position when drag is in process.
     *
     * @param Event e = Event object, used to get x and y coordinate of mouse pointer
     * 
     * @private
     */	
	__moveDragableElement : function(e)
	{
		var ind = this.objectIndex;		
		if(document.all)e = event;		
		if(this.dragDropTimer<5)return false;	
		if(this.dragInProgress)return false;
		this.dragInProgress = true;
		var dragObj = this.dragObjCloneArray[this.numericIdToBeDragged];
		
		if(this.currentEl_allowX){			
			
			var leftPos = (e.clientX - this.mouse_x + this.el_x);
			if(this.drag_maxX){
				var tmpMaxX = this.drag_maxX - dragObj.offsetWidth;
				if(leftPos > tmpMaxX)leftPos = tmpMaxX
				if(leftPos < this.drag_minX)leftPos = this.drag_minX;				
			}
			dragObj.style.left = leftPos + 'px'; 
		
		}	
		if(this.currentEl_allowY){
			var topPos = (e.clientY - this.mouse_y + this.el_y);
			if(this.drag_maxY){	
				var tmpMaxY = this.drag_maxY - dragObj.offsetHeight;		
				if(topPos > tmpMaxY)topPos = tmpMaxY;
				if(topPos < this.drag_minY)topPos = this.drag_minY;					
			}			
			dragObj.style.top = topPos + 'px'; 
		}
		this.dragInProgress = false;
		return false;
	}
	// }}}	
	,	
	// {{{ __stopDragProcess()
    /**
     * Drag process stopped.
     *
     * @param Event e = Event object, used to get x and y coordinate of mouse pointer
     * 
     * @private
     */	
	__stopDragProcess : function(e)
	{
		if(this.dragDropTimer<5)return false;
		if(document.all)e = event;
			
		// Dropped on which element
		var dropDestination = DHTMLSuite.commonObj.getSrcElement(e);	
		
		var leftPosMouse = e.clientX + Math.max(document.body.scrollLeft,document.documentElement.scrollLeft);
		var topPosMouse = e.clientY + Math.max(document.body.scrollTop,document.documentElement.scrollTop);
		
		if(!this.dragDropTargetArray)this.dragDropTargetArray = new Array();
		// Loop through drop targets and check if the coordinate of the mouse is over it. If it is, call specified drop function.
		for(var no=0;no<this.dragDropTargetArray.length;no++){
			var leftPosEl = DHTMLSuite.commonObj.getLeftPos(this.dragDropTargetArray[no][0]);
			var topPosEl = DHTMLSuite.commonObj.getTopPos(this.dragDropTargetArray[no][0]);
			var widthEl = this.dragDropTargetArray[no][0].offsetWidth;
			var heightEl = this.dragDropTargetArray[no][0].offsetHeight;
			
			if(leftPosMouse > leftPosEl && leftPosMouse < (leftPosEl + widthEl) && topPosMouse > topPosEl && topPosMouse < (topPosEl + heightEl)){
				if(this.dragDropTargetArray[no][1]){
					try{
						eval(this.dragDropTargetArray[no][1] + '("' + this.dragDropSourcesArray[this.numericIdToBeDragged][0].id + '","' + this.dragDropTargetArray[no][0].id + '",' + e.clientX + ',' + e.clientY + ')');
					}catch(e){
						alert('Unable to execute \n' + this.dragDropTargetArray[no][1] + '("' + this.dragDropSourcesArray[this.numericIdToBeDragged][0].id + '","' + this.dragDropTargetArray[no][0].id + '",' + e.clientX + ',' + e.clientY + ')');
					}
				}				
				break;
			}			
		}	
		
		if(this.dragDropSourcesArray[this.numericIdToBeDragged][1]){
			this.__slideElementBackIntoItsOriginalPosition(this.numericIdToBeDragged);
		}		
		// Variable cleanup after drop
		this.dragDropTimer = -1;		
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
		this.numericIdToBeDragged = false;									
	}	
	// }}}	
	,	
	// {{{ __slideElementBackIntoItsOriginalPosition()
    /**
     * Slide an item back to it's original position
     *
     * @param Integer numId = numeric index of currently dragged element	
     * 
     * @private
     */	
	__slideElementBackIntoItsOriginalPosition : function(numId)
	{
		// Coordinates current element position
		var currentX = this.dragObjCloneArray[numId].style.left.replace('px','')/1;
		var currentY = this.dragObjCloneArray[numId].style.top.replace('px','')/1;
		
		// Coordinates - where it should slide to
		var targetX = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[numId][0]);
		var targetY = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[numId][0]);;
		
		if(this.moveBackBySliding){
			// Call the step by step slide method
			this.__processSlideByPixels(numId,currentX,currentY,targetX,targetY);
		}else{
			this.dragObjCloneArray[numId].style.display='none';
			this.dragDropSourcesArray[numId][0].style.visibility = 'visible';			
		}			
	}
	// }}}	
	,	
	// {{{ __processSlideByPixels()
    /**
     * Move the element step by step in this method
     *
     * @param Int numId = numeric index of currently dragged element
     * @param Int currentX = Elements current X position
     * @param Int currentY = Elements current Y position
     * @param Int targetX = Destination X position, i.e. where the element should slide to
     * @param Int targetY = Destination Y position, i.e. where the element should slide to
     * 
     * @private
     */	
	__processSlideByPixels : function(numId,currentX,currentY,targetX,targetY)
	{				
		// Find slide x value
		var slideX = Math.round(Math.abs(Math.max(currentX,targetX) - Math.min(currentX,targetX)) / 10);		
		// Find slide y value
		var slideY = Math.round(Math.abs(Math.max(currentY,targetY) - Math.min(currentY,targetY)) / 10);
		
		if(slideY<3 && Math.abs(slideX)<10)slideY = 3;	// 3 is minimum slide value
		if(slideX<3 && Math.abs(slideY)<10)slideX = 3;	// 3 is minimum slide value
		
		
		if(currentX > targetX) slideX*=-1;	// If current x is larger than target x, make slide value negative<br>
		if(currentY > targetY) slideY*=-1;	// If current y is larger than target x, make slide value negative
		
		// Update currentX and currentY
		currentX = currentX + slideX;	
		currentY = currentY + slideY;

		// If currentX or currentY is close to targetX or targetY, make currentX equal to targetX(or currentY equal to targetY)
		if(Math.max(currentX,targetX) - Math.min(currentX,targetX) < 4)currentX = targetX;
		if(Math.max(currentY,targetY) - Math.min(currentY,targetY) < 4)currentY = targetY;

		// Update CSS position(left and top)
		this.dragObjCloneArray[numId].style.left = currentX + 'px';
		this.dragObjCloneArray[numId].style.top = currentY + 'px';	
		
		// currentX different than targetX or currentY different than targetY, call this function in again in 5 milliseconds
		if(currentX!=targetX || currentY != targetY){
			window.thisRef = this;	// Reference to this dragdrop object
			setTimeout('window.thisRef.__processSlideByPixels("' + numId + '",' + currentX + ',' + currentY + ',' + targetX + ',' + targetY + ')',5);
		}else{	// Slide completed. Make absolute positioned element invisible and original element visible
			this.dragObjCloneArray[numId].style.display='none';
			this.dragDropSourcesArray[numId][0].style.visibility = 'visible';
		}		
	}
}