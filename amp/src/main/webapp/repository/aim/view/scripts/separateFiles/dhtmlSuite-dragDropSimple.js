if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Drag and drop class - simple class - used by the DHTMLSuite.imageEnlarger script
*
*	Created:			January, 8th, 2007
*
* 	Update log:
*
************************************************************************************************************/
var DHTMLSuite_dragDropSimple_curZIndex = 100000;
var DHTMLSuite_dragDropSimple_curObjIndex=false;
/**
* @constructor
* @class Purpose of class:	A very simple drag and drop script. It makes a single element dragable but doesn't offer other features.
*		<br>
* @version				1.0
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/
DHTMLSuite.dragDropSimple = function(elementRef,elementId,initOffsetX,initOffsetY,cloneNode)
{
	var divElement;
	var dragTimer;	// -1 no drag, 0-4 = initializing , 5 = drag in process
	var cloneNode;
	this.cloneNode = true;
	if(cloneNode===false || cloneNode)this.cloneNode=cloneNode;

	var callbackOnAfterDrag;
	var callbackOnBeforeDrag;
	
	var mouse_x;
	var mouse_y;
	var positionSet;
	var dragHandle;	// If a specific element is specified to be a drag handle.
	
	this.positionSet = false;
	this.dragHandle = new Array();
	var initOffsetX;
	var initOffsetY;
	if(!initOffsetX)initOffsetX = 0;
	if(!initOffsetY)initOffsetY = 0;
	
	this.initOffsetX = initOffsetX;
	this.initOffsetY = initOffsetY;
	this.callbackOnAfterDrag = false;
	this.callbackOnBeforeDrag = false;
	
	this.dragStatus = -1;
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
		
	if(!elementRef && elementId)elementRef = document.getElementById(elementId);
	
	this.divElement = elementRef;
	
	var objectIndex;
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;

	
	this.__init();
	
}

DHTMLSuite.dragDropSimple.prototype = {
	
	// {{{ __init()
    /**
     * Initializes the script
     * 
     * @private
     */		
	__init : function()
	{
		var ind = this.objectIndex;		
		
		
		this.divElement.objectIndex = ind;
		this.divElement.setAttribute('objectIndex',ind);
		
		this.divElement.style.padding = '0px';
		this.divElement.style.left = (DHTMLSuite.commonObj.getLeftPos(this.divElement) + this.initOffsetX) + 'px';
		this.divElement.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElement) + this.initOffsetY) + 'px';
		this.divElement.style.position = 'absolute';
		this.divElement.style.margin = '0px';
			
		if(this.divElement.style.zIndex && this.divElement.style.zIndex/1>DHTMLSuite_dragDropSimple_curZIndex)DHTMLSuite_dragDropSimple_curZIndex=this.divElement.style.zIndex/1;		
		DHTMLSuite_dragDropSimple_curZIndex = DHTMLSuite_dragDropSimple_curZIndex/1 + 1;
		this.divElement.style.zIndex = DHTMLSuite_dragDropSimple_curZIndex;	
				
		if(this.cloneNode){
			var copy = this.divElement.cloneNode(true);		
			this.divElement.parentNode.insertBefore(copy,this.divElement);
			copy.style.visibility = 'hidden';
			document.body.appendChild(this.divElement);
		}
		this.divElement.onmousedown = this.__initDragProcess;
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragableElement(e); });
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__stopDragProcess(e); });
				
		if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
	}
	// }}}
	,
	// {{{ setCallbackOnAfterDrag()
    /**
     * Specify name of function to execute after drag is completed.
     *	
     *	@param String functionName - Name of function to execute
     * 
     * @private
     */		
	setCallbackOnAfterDrag : function(functionName)
	{
		this.callbackOnAfterDrag = functionName;
	}
	// }}}
	,
	// {{{ setCallbackOnBeforeDrag()
    /**
     * Specify name of function to execute before drag is executed.
     *	
     *	@param String functionName - Name of function to execute
     * 
     * @private
     */		
	setCallbackOnBeforeDrag : function(functionName)
	{
		this.callbackOnBeforeDrag = functionName;
	}
	// }}}
	,
	// {{{ addDragHandle()
    /**
     * Specify a drag handle
     *	
     *	@param Object HTML Element - element inside the dragable element specified to act as a drag handle.
     * 
     * @private
     */		
	addDragHandle : function(dragHandle)
	{
		this.dragHandle[this.dragHandle.length] = dragHandle;
	}
	// }}}
	,
	// {{{ __initDragProcess()
    /**
     * Initializes drag process
     * 
     * @private
     */		
	__initDragProcess : function(e)
	{
		if(document.all)e = event;			
		var ind = this.getAttribute('objectIndex');
		if(!ind)ind=this.objectIndex;
		DHTMLSuite_dragDropSimple_curObjIndex = ind;	
			
		var thisObject = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind];
		if(!DHTMLSuite.commonObj.isObjectClicked(thisObject.divElement,e))return;	

		if(thisObject.divElement.style.zIndex && thisObject.divElement.style.zIndex/1>DHTMLSuite_dragDropSimple_curZIndex)DHTMLSuite_dragDropSimple_curZIndex=thisObject.divElement.style.zIndex/1;		
		DHTMLSuite_dragDropSimple_curZIndex = DHTMLSuite_dragDropSimple_curZIndex/1 +1;
		thisObject.divElement.style.zIndex = DHTMLSuite_dragDropSimple_curZIndex;	
				
		if(thisObject.callbackOnBeforeDrag){
			thisObject.__handleCallback('beforeDrag');	
		}
		
		if(thisObject.dragHandle.length>0){	// Drag handle specified?		
			var objectFound;
			for(var no=0;no<thisObject.dragHandle.length;no++){
				if(!objectFound)objectFound = DHTMLSuite.commonObj.isObjectClicked(thisObject.dragHandle[no],e);
			}	
			if(!objectFound)return;
		}			
		
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
		
		thisObject.mouse_x = e.clientX;
		thisObject.mouse_y = e.clientY;
		
		thisObject.el_x = thisObject.divElement.style.left.replace('px','')/1;
		thisObject.el_y = thisObject.divElement.style.top.replace('px','')/1;
		
		thisObject.dragTimer = 0;
		thisObject.__waitBeforeDragProcessStarts();
		return false;				
	}	
	// }}}
	,
	// {{{ __waitBeforeDragProcessStarts()
    /**
     * Small delay from mouse is pressed down till drag starts.
     * 
     * @private
     */		
	__waitBeforeDragProcessStarts : function()
	{
		var ind = this.objectIndex;
		if(this.dragTimer>=0 && this.dragTimer<5){
			this.dragTimer++;			
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__waitBeforeDragProcessStarts()',5);
		}	
	}
	// }}}
	,
	// {{{ __moveDragableElement()
    /**
     * Move dragable element if drag is in process
     *
     *	@param Event e - Event object - since this method is triggered by an event
     *
     * @private
     */		
	__moveDragableElement : function(e)
	{	
		
		if(DHTMLSuite_dragDropSimple_curObjIndex===false)return false;
		var thisObj = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[DHTMLSuite_dragDropSimple_curObjIndex];
		
		if(thisObj.dragTimer==5){	
			
			thisObj.divElement.style.left = (e.clientX - thisObj.mouse_x + thisObj.el_x) + 'px';	
			thisObj.divElement.style.top = (e.clientY - thisObj.mouse_y + thisObj.el_y) + 'px';	
		}	
		return false;
	}
	// }}}
	,
	// {{{ __stopDragProcess()
    /**
     * Stop the drag process
     * 
     * @private
     */		
	__stopDragProcess : function()
	{
		if(DHTMLSuite_dragDropSimple_curObjIndex===false)return;
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
		var thisObj = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[DHTMLSuite_dragDropSimple_curObjIndex];	
		if(thisObj.dragTimer==5){
			thisObj.__handleCallback('afterDrag');
		}	
		thisObj.dragTimer = -1;
	}
	// }}}
	,
	// {{{ __handleCallback()
    /**
     * Execute callback function
     *	@param String action - callback action
     * 
     * @private
     */		
	__handleCallback : function(action)
	{
		var callbackString = '';
		switch(action){
			case "afterDrag":
				callbackString = this.callbackOnAfterDrag;
				break;	
			case "beforeDrag":
				callbackString = this.callbackOnBeforeDrag;
				break;
		}
		if(callbackString){
			callbackString = callbackString + '()';
			try{
				eval(callbackString);
			}catch(e){
				alert('Could not execute callback function(' + callbackstring + ') after drag');
			}
		}
		
	}
	// }}}
	,
	// {{{ __setNewCurrentZIndex()
    /**
     * Updates current z index. 
     *	@param Integer zIndex - This method is called by the window script when z index has been read from cookie
     * 
     * @private
     */		
	__setNewCurrentZIndex : function(zIndex)
	{
		if(zIndex > DHTMLSuite_dragDropSimple_curZIndex){
			DHTMLSuite_dragDropSimple_curZIndex = zIndex/1+1;
		}	
	}
		
}