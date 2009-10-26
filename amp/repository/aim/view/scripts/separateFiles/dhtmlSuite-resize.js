if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML window scripts
*
*	Created:						January, 27th, 2006
*	@class Purpose of class:		Resize element widget
*			
*	Css files used by this script:	
*
*	Demos of this class:			demo-resize-1.html
*
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class Resize widget
*		
* @param Array - Associative array of resize properties(possible keys: minWidth,maxWidth,minHeight,maxHeight,preserveRatio,callbackOnBeforeResize,callbackOnAfterResize,callbackOnDuringResize,resizeInWhichDirections)
* @version				1.0
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/
DHTMLSuite.resize = function(propertyArray)
{
	var resizeWhichElement;
	var resizeHandles;

	this.resizeHandles = new Array();
	var preserveRatio;
	var minWidth;
	var maxWidth;
	var minHeight;
	var maxHeight;
	var callbackOnBeforeResize;
	var callbackOnAfterResize;
	var callbackOnDuringResize;
	var resizeTimer;
	var resizeInWhichDirections;
	var resizeHandleelativePath;
	var objectIndex;
	
	var mouseStartPos;					// Position of mouse pointer when the resize process starts
	var initElementSize;
	var currentResizeDirection;			// In which direction are we currently resizing the element(example: "west","east","southeast")
	var classNameOfResizeHandles;		// Class name of resize handles, in case they are created dynamically
	var layoutCSS;
	var resizeHandlerOffsetInPixels;
	var elementToResizeIsAbsolutePositioned;
	var sizeOfWidthRelativeToHeight;
	
	
	this.minWidth = 0;
	this.minHeight = 0;
	this.maxWidth = 150000;
	this.maxHeight = 150000;
	this.classNameOfResizeHandles = 'DHTMLSuite_resize_handle';
	this.layoutCSS = 'resize.css';
	this.resizeHandleelativePath = 'resize/small_square.gif';
	this.resizeTimer = -1;
	this.mouseStartPos = new Array();
	this.initElementSize = new Array();
	this.resizeHandlerOffsetInPixels = 4;
	this.elementToResizeIsAbsolutePositioned = false;
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}	
	if(propertyArray)this.__setInitialProperties(propertyArray);
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;	
	
	
}
DHTMLSuite.resize.prototype = 
{
	// {{{ setLayoutCss()
    /**
     *	Specify new css file for the resize widget, default = resize.css
     *	
     * @public	
     */		
	setLayoutCss : function(cssFileName)
	{
		this.layoutCSS = cssFileName;
	}
	// }}}
	,
	// {{{ setLayoutCss()
    /**
     *	Specify new css file for the resize widget, default = resize.css
     *	
     * @private	
     */		
	setCssClassNameForResizeHandles : function(classNameOfResizeHandles)
	{
		this.classNameOfResizeHandles = classNameOfResizeHandles;
	}
	// }}}
	,	
	// {{{ setElementRoResize()
    /**
     *	Specify which element you want to resize
     *	
     *	@param Object elementReference - id or direct reference to HTML element(this element should be either absolute or relative positioned)
     *	
     * @public	
     */		
	setElementRoResize : function(elementReference)
	{
		if(typeof elementReference == 'string'){
			elementReference = document.getElementById(elementReference);
		}
		this.resizeWhichElement = elementReference;				
	}
	// }}}
	,
	// {{{ addResizeHandle()
    /**
     *	Specify a sub element of the reized element which acts as a resize handle
     *	
     *	@param Object resizeHandle - id or direct reference to HTML element
     *	@param String direction - Resize direction(possible values "all", "south","west","east","north","northwest","northheast","southwest","southeast")
     *	
     * @public	
     */			
	addResizeHandle : function(resizeHandle,direction)
	{
		if(typeof resizeHandle == 'string'){
			resizeHandle = document.getElementById(resizeHandle);
		}
		var index = this.resizeHandles.length;
		this.resizeHandles[index] = new Array();
		this.resizeHandles[index].element = resizeHandle;
		this.resizeHandles[index].direction = direction;				
	}
	// }}}
	,
	// {{{ setMinWidthInPixels()
    /**
     *	Specify minimum width
     *	
     *	@param Integer pixels - Minimum width of resized element in pixels. i.e. restrict the resize widget from making it smaller.
     *	
     * @public	
     */		
	setMinWidthInPixels : function(pixels)
	{
		this.minWidth = pixels;
		
	}
	// }}}
	,
	// {{{ setMaxWidthInPixels()
    /**
     *	Specify maximum width
     *	
     *	@param Integer pixels - Maximum width of resized element in pixels. i.e. restrict the resize widget from making it larger.
     *	
     * @public	
     */		
	setMaxWidthInPixels : function(pixels)
	{
		this.maxWidth = pixels;
	}
	// }}}
	,
	// {{{ setMinHeightInPixels()
    /**
     *	Specify minimum width
     *	
     *	@param Integer pixels - minimum width of resized element in pixels. i.e. restrict the resize widget from making it smaller.
     *	
     * @public	
     */	
	setMinHeightInPixels : function(pixels)
	{
		this.minHeight = pixels;
	}
	// }}}
	,
	// {{{ setMaxHeightInPixels()
    /**
     *	Specify maximum height
     *	
     *	@param Integer pixels - maximum height of resized element in pixels. i.e. restrict the resize widget from making it smaller.
     *	
     * @public	
     */	
	setMaxHeightInPixels : function(pixels)
	{
		this.maxHeight = pixels;
		
	}
	// }}}
	,
	// {{{ setCallbackOnBeforeResize()
    /**
     *	Specify name of call back function which will be executed before restarts starts
     *	
     *	@param String functionName - Only the name of the function to execute, example. "myCallbackFunction", a reference to this resize object will be sent as only argument to this function.
     *	
     * @public	
     */	
	setCallbackOnBeforeResize : function(functionName)
	{
		this.callbackOnBeforeResize = functionName;
	}
	// }}}
	,
	// {{{ setCallbackOnAfterResize()
    /**
     *	Specify name of call back function which will be executed when restarts ends(i.e. mouse up)
     *	
     *	@param String functionName - Only the name of the function to execute, example. "myCallbackFunction", a reference to this resize object will be sent as only argument to this function.
     *	
     * @public	
     */	
	setCallbackOnAfterResize : function(functionName)
	{
		this.callbackOnAfterResize = functionName;
	}
	// }}}
	,
	// {{{ setCallbackOnDuringResize()
    /**
     *	Specify name of call back function which will be executed during resize
     *	
     *	@param String functionName - Only the name of the function to execute, example. "myCallbackFunction", a reference to this resize object will be sent as only argument to this function.
     *	
     * @public	
     */	
	setCallbackOnDuringResize : function(functionName)
	{
		this.callbackOnDuringResize = functionName;		
	}
	// }}}
	,
	// {{{ setResizeHandlerOffsetInPixels()
    /**
     *	Specify offset in pixels for automatically created resize handles. If you don't add a resize handle manually, automatic resize handles will be created for you. These will be placed along the edge of the resizable element. You can move these handles by specifying an offset value.
     *	
     *	@param Integer offsetInPx - Offset in pixels
     *	
     * @public	
     */		
	setResizeHandlerOffsetInPixels : function(offsetInPx)
	{
		this.resizeHandlerOffsetInPixels = offsetInPx;		
	}
	// }}}
	,
	// {{{ setIsResizeElementAbsolutePositioned()
    /**
     *	Specify if resized element is absolute positioned.
     *	
     *	@param Boolean absolutePositioned - Is the resizable element absolute positioned on your page ?
     *	
     * @public	
     */	
	setIsResizeElementAbsolutePositioned : function(absolutePositioned)
	{
		this.elementToResizeIsAbsolutePositioned = absolutePositioned;
	}
	// }}}
	,
	// {{{ getReferenceToResizedElement()
    /**
     *	Returns a reference to the resizable element
     *	
     *	
     * @public	
     */	
	getReferenceToResizedElement : function()
	{
		return this.resizeWhichElement;
	}
	// }}}
	,
	// {{{ init()
    /**
     *	Initializes the widget. Call this method after you are finished with your set-method calls.
     *	
     *	
     * @public	
     */	
	init : function()
	{
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		this.__setAspectRatio();
		this.__createResizeHandlesAutomatically();
		this.__setCursorOfResizeHandles();
		this.__addEventsToResizeHandles();
		this.__addBasicEvents();
	}
	// }}}
	,
	// {{{ __setAspectRatio()
    /**
     *	Determine aspect ratio
     *	
     * @private	
     */	
	__setAspectRatio : function()
	{
		this.sizeOfWidthRelativeToHeight = this.resizeWhichElement.offsetWidth / this.resizeWhichElement.offsetHeight;		
	}
	// }}}
	,
	// {{{ __setInitialProperties()
    /**
     *	Save initial properties sent to the constructor
     *	@param Array - associative array of properties.
     *	
     * @private	
     */	
	__setInitialProperties : function(propertyArray)
	{
		if(propertyArray.minWidth)this.minWidth = propertyArray.minWidth;	
		if(propertyArray.maxWidth)this.maxWidth = propertyArray.maxWidth;	
		if(propertyArray.minHeight)this.minHeight = propertyArray.minHeight;	
		if(propertyArray.maxHeight)this.maxHeight = propertyArray.maxHeight;		
		if(propertyArray.preserveRatio)this.preserveRatio = propertyArray.preserveRatio;		
		if(propertyArray.callbackOnBeforeResize)this.callbackOnBeforeResize = propertyArray.callbackOnBeforeResize;		
		if(propertyArray.callbackOnAfterResize)this.callbackOnAfterResize = propertyArray.callbackOnAfterResize;		
		if(propertyArray.callbackOnDuringResize)this.callbackOnDuringResize = propertyArray.callbackOnDuringResize;		
		if(propertyArray.resizeInWhichDirections)this.resizeInWhichDirections = propertyArray.resizeInWhichDirections;		
	}
	// }}}
	,
	// {{{ __createResizeHandlesAutomatically()
    /**
     *	Create resize handles automatically.
     *	
     * @private	
     */	
	__createResizeHandlesAutomatically : function()
	{
		if(this.resizeHandles.length>0)return;
		if(!this.resizeInWhichDirections || this.resizeInWhichDirections=='all')this.resizeInWhichDirections = 'west,east,north,south,southeast,southwest,northwest,northeast';
		
		var directions = this.resizeInWhichDirections.split(/,/g);
		for(var no=0;no<directions.length;no++){
			this.resizeHandles[no] = new Array();
			this.resizeHandles[no].element = document.createElement('DIV');
			this.resizeHandles[no].element.className = this.classNameOfResizeHandles;
			this.resizeWhichElement.appendChild(this.resizeHandles[no].element);
			this.resizeHandles[no].direction = directions[no];
			var el = this.resizeHandles[no].element;
			
			el.style.top = '50%';
			el.style.left = '50%';
			if(directions[no].indexOf('west')>=0)el.style.left = (0-this.resizeHandlerOffsetInPixels) + 'px';
			if(directions[no].indexOf('east')>=0){
				el.style.right = (0-this.resizeHandlerOffsetInPixels) + 'px';
				el.style.left = '';
			}
			if(directions[no].indexOf('north')>=0)el.style.top = (0-this.resizeHandlerOffsetInPixels) + 'px';
			if(directions[no].indexOf('south')>=0){
				el.style.bottom = (0-this.resizeHandlerOffsetInPixels) + 'px';
				el.style.top = '';
			}
			
			if(el.style.top=='50%')el.style.marginTop = '-' + Math.round(el.offsetHeight/2) + 'px';		
			if(el.style.left=='50%')el.style.marginLeft = '-' + Math.round(el.offsetWidth/2) + 'px';		
		}			
	}	
	// }}}
	,
	// {{{ __setCursorOfResizeHandles()
    /**
     *	Set css cursor attributes for all the resize handles depending of how they resize the element
     *	
     * @private	
     */	
	__setCursorOfResizeHandles : function()
	{
		for(var no=0;no<this.resizeHandles.length;no++){
			
			switch(this.resizeHandles[no].direction){
				case "west":
				case "east":
					this.resizeHandles[no].element.style.cursor = 'e-resize';	
					break;
				case "north":
				case "south":
					this.resizeHandles[no].element.style.cursor = 's-resize';
					break;
				case "northeast":
					this.resizeHandles[no].element.style.cursor = 'ne-resize';
					break;
				case "northwest":
					this.resizeHandles[no].element.style.cursor = 'nw-resize';
					break;
				case "southwest":
					this.resizeHandles[no].element.style.cursor = 'sw-resize';
					break;
				case "southeast":
					this.resizeHandles[no].element.style.cursor = 'se-resize';
					break;				
			}				
		}		
	}
	// }}}
	,
	// {{{ __addEventsToResizeHandles()
    /**
     * Add events to resize handles.
     *	
     * @private	
     */	
	__addEventsToResizeHandles : function()
	{
		var ind = this.objectIndex;
		for(var no=0;no<this.resizeHandles.length;no++){
			this.resizeHandles[no].element.onmousedown = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResize(e); }
			this.resizeHandles[no].element.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };	
			DHTMLSuite.commonObj.__addEventElement(this.resizeHandles[no].element);
			this.resizeHandles[no].element.setAttribute('resizeInDirection',this.resizeHandles[no].direction);		
			this.resizeHandles[no].element.resizeInDirection = this.resizeHandles[no].direction;			
		}		
	}
	// }}}
	,
	// {{{ __addBasicEvents()
    /**
     * Add basic events for the widget
     *	
     * @private	
     */	
	__addBasicEvents : function()
	{
		var ind = this.objectIndex;
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__processResize(e); },ind);
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endResize(e); },ind);	
		if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };	
		
	}
	// }}}
	,
	// {{{ __initResize()
    /**
     * Resize process starts
     *	
     * @private	
     */	
	__initResize : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		if(this.callbackOnBeforeResize){
			var ok = this.__handleCallback('beforeResize');
			if(!ok)return;
		}
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
		this.resizeTimer = 0;
		this.mouseStartPos.x = e.clientX;
		this.mouseStartPos.y = e.clientY;
		this.initElementSize.width = this.resizeWhichElement.offsetWidth;
		this.initElementSize.height = this.resizeWhichElement.offsetHeight;
		this.initElementSize.top = this.resizeWhichElement.style.top.replace('px','')/1;
		if(this.elementToResizeIsAbsolutePositioned && !this.initElementSize.top)this.initElementSize.top = DHTMLSuite.commonObj.getTopPos(this.resizeWhichElement);
		this.initElementSize.left = this.resizeWhichElement.style.left.replace('px','')/1;
		if(this.elementToResizeIsAbsolutePositioned && !this.initElementSize.left)this.initElementSize.left = DHTMLSuite.commonObj.getLeftPos(this.resizeWhichElement);
		
		this.currentResizeDirection = src.getAttribute('resizeInDirection');
		this.__delayBeforeResize();
		return false;	
	}
	// }}}
	,
	// {{{ __delayBeforeResize()
    /**
     * A small delay from mouse is pressed down to the resize starts.
     *	
     * @private	
     */	
	__delayBeforeResize : function()
	{
		if(this.resizeTimer>=0 && this.resizeTimer<5){
			var ind = this.objectIndex;
			this.resizeTimer++;
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__delayBeforeResize()',20);
			return;
		}		
	}
	// }}}
	,
	// {{{ __processResize()
    /**
     * Process resize, i.e. mouse has been pressed down and the mouse pointer is moving.
     *	
     * @private	
     */	
	__processResize : function(e)
	{
		if(document.all)e = event;
		if(this.resizeTimer<5)return;
		
		var newWidth = this.initElementSize.width;
		var newHeight = this.initElementSize.height;
		
		var newTop = this.initElementSize.top;
		var newLeft = this.initElementSize.left;
		
		switch(this.currentResizeDirection){
			case "east":
			case "northeast":
			case "southeast":
				newWidth = (this.initElementSize.width + e.clientX - this.mouseStartPos.x);			
				break;	
		}
		switch(this.currentResizeDirection){
			case "south":
			case "southeast":
			case "southwest":
				newHeight = (this.initElementSize.height + e.clientY - this.mouseStartPos.y)
				break;
			
		}
		
		if(this.currentResizeDirection.indexOf('north')>=0){
			newTop = this.initElementSize.top  + e.clientY - this.mouseStartPos.y;
			newHeight = newHeight - (newTop - this.initElementSize.top);
			if(this.preserveRatio && this.currentResizeDirection=='north')newWidth = Math.round(newHeight * this.sizeOfWidthRelativeToHeight);
			if(newHeight<this.minHeight){
				newTop-=(this.minHeight-newHeight);
			}			
		}
		if(this.currentResizeDirection.indexOf('west')>=0){
			newLeft = this.initElementSize.left  + e.clientX - this.mouseStartPos.x;
			newWidth = newWidth - (newLeft - this.initElementSize.left);
			if(this.preserveRatio && this.currentResizeDirection=='west')newHeight = Math.round(newWidth / this.sizeOfWidthRelativeToHeight);
			if(newWidth<this.minWidth){
				newLeft-=(this.minWidth-newWidth);
			}	
			if(newWidth>this.maxWidth){
				newLeft+=(newWidth-this.maxWidth);
			}		
		}
				
		if(newWidth<this.minWidth)newWidth = this.minWidth;
		if(newHeight<this.minHeight)newHeight = this.minHeight;
		if(this.maxWidth && newWidth>this.maxWidth)newWidth = this.maxWidth;
		if(this.maxHeight && newHeight>this.maxHeight)newHeight = this.maxHeight;

		if(this.currentResizeDirection.indexOf('east')>=0 && this.preserveRatio){
			newHeight = Math.round(newWidth / this.sizeOfWidthRelativeToHeight);
		}	
		if(this.currentResizeDirection.indexOf('south')>=0 && this.preserveRatio){
			newWidth = Math.round(newHeight * this.sizeOfWidthRelativeToHeight);
		}			
		if(this.currentResizeDirection=='northwest' && this.preserveRatio){
			if(newWidth/newHeight > this.sizeOfWidthRelativeToHeight){
				newHeight = Math.round(newWidth / this.sizeOfWidthRelativeToHeight);
			}else{
				newWidth = Math.round(newHeight * this.sizeOfWidthRelativeToHeight);
			}		
		}
		
		this.resizeWhichElement.style.width = newWidth + 'px';
		this.resizeWhichElement.style.height = newHeight + 'px';
		this.resizeWhichElement.style.top = newTop + 'px';
		this.resizeWhichElement.style.left = newLeft + 'px';
		if(this.callbackOnDuringResize)this.__handleCallback('duringResize');
	
	}
	// }}}
	,
	// {{{ __endResize()
    /**
     * Resize process ends.
     *	
     * @private	
     */	
	__endResize : function(e)
	{
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
		if(this.resizeTimer==5){
			this.__handleCallback('afterResize');
		}
		this.resizeTimer=-1;
	}
	// }}}
	,
	// {{{ __handleCallback()
    /**
     * Execute eventual callback functions.
     *	
     * @private	
     */	
	__handleCallback : function(action)
	{
		var ind = this.objectIndex;
		var callbackString = '';
		switch(action){
			case "afterResize":
				callbackString = this.callbackOnAfterResize;
				break;
			case "duringResize":
				callbackString = this.callbackOnDuringResize;
				break;
			case "beforeResize":
				callbackString = this.callbackOnBeforeResize;
				break;
			
		}
		
		if(callbackString)callbackString = callbackString + '(DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '])';
		
		try{
			return eval(callbackString);
		}catch(e){
			alert('Could not execute call back string after resize');
		}		
	}
}