if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*
*	DHTML slider
*
*	Created:					August, 25th, 2006
*	@class Purpose of class:	Display a slider on a web page.
*		
*	Demos of this class:		demo-slider-1.html
*	
* 	Update log:
*
************************************************************************************************************/

DHTMLSuite.sliderObjects = new Array();	// Array of slider objects. Used in events when "this" refers to the tag trigger the event and not the object.
DHTMLSuite.indexOfCurrentlyActiveSlider = false;	// Index of currently active slider(i.e. index in the DHTMLSuite.sliderObjects array)
DHTMLSuite.slider_generalMouseEventsAdded = false;	// Only assign mouse move and mouseup events once. This variable make sure that happens.


/**
* @constructor
* @class Purpose of class:	Display a DHTML slider on a web page. This slider could either be displayed horizontally or vertically(<a href="../../demos/demo-slider-1.html" target="_blank">demo 1</a> and <a href="../../demos/demo-slider-1.html" target="_blank">demo 2</a>).
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*
**/
DHTMLSuite.slider = function()
{
	var width;							// Width of slider
	var height;							// height of slider
	var targetObj;						// Object where slider will be added.
	var sliderWidth;					// Width of slider image. this is needed in order to position the slider and calculate values correctly.
	var sliderDirection;				// Horizontal or vertical
	var functionToCallOnChange; 		// Function to call when the slider is moved.
	var layoutCss;	
	var sliderMaxValue;					// Maximum value to return from slider
	var sliderMinValue;					// Minimum value to return from slider
	var initialValue;					// Initial value of slider
	var sliderSize;						// Size of sliding area.
	var directionOfPointer;				// Direction of slider pointer;
	var slideInProcessTimer;			// Private variable used to determine if slide is in progress or not.
	var indexThisSlider;				// Index of object in the DHTMLSuite.sliderObjects array
	var numberOfSteps;					// Hardcoded steps
	var stepLinesVisibility;			// Visibility of lines indicating where the slider steps are
	
	var slide_event_pos;					// X position of mouse when slider drag starts
	var slide_start_pos;					// X position of slider when drag starts
	var sliderHandleImg;				// Reference to the small slider handle
	var sliderName;						// A name you can use to identify a slider. Useful if you have more than one slider, but only one onchange event.
	var sliderValueReversed;			// Variable indicating if the value of the slider is reversed(i.e. max at left and min at right)
	this.sliderWidth = 9;				// Initial width of slider.
	this.layoutCss = 'slider.css';		// Default css file
	this.sliderDirection = 'hor';		// Horizontal is default
	this.width = 0;						// Initial widht
	this.height = 0;					// Initial height
	this.sliderMaxValue = 100; 			// Default max value to return from slider
	this.sliderMinValue = 0;			// Default min value to return from slider
	this.initialValue = 0;				// Default initial value of slider
	this.targetObj = false;				// Set target obj to false initially.
	this.directionOfPointer = 'up';		// Default pointer direction for slider handle.
	this.slideInProcessTimer = -1;
	this.sliderName = '';
	this.numberOfSteps = false;
	this.stepLinesVisibility = true;
	this.sliderValueReversed = false;	// Default value of sliderValueReversed, i.e. max at right, min at left or max at top, min at bottom.
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	var objectIndex;
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;	

}

DHTMLSuite.slider.prototype = {
	
	// {{{ init()
    /**
     *	Initializes the script, i.e. creates the slider
     * 	
     *
     * @public
     */			
	
	init : function()	// Initializes the script
	{
		if(!this.targetObj){
			alert('Error! - No target for slider specified');
			return;
		}
	
		this.__setWidthAndHeightDynamically();

		DHTMLSuite.commonObj.loadCSS(this.layoutCss);		
		
		this.__createSlider();
				
		
	}	
	// }}}
	,
	// {{{ setSliderTarget(divId)
    /**
     *	Specify where to insert the slider
     * 	
     *	@param String targetId - Id of element where the slider will be created inside(There shouldn't be any content inside this div)
     *
     * @public	
     */	
	setSliderTarget : function(targetId)
	{
		this.targetObj = document.getElementById(targetId);		
	}
	// }}}	
	,
	// {{{ setSliderDirection(newDirection)
    /**
     *	Specify where to insert the slider
     * 	
     *	@param String newDirection - New slider direction. Possible valuse: "hor" or "ver"
     *
     * @public	
     */		
	setSliderDirection : function(newDirection)
	{
		newDirection = newDirection + '';
		newDirection = newDirection.toLowerCase();
		if(newDirection!='hor' && newDirection!='ver'){
			alert('Invalid slider direction - possible values: "hor" or "ver"');
			return;
		}
		this.sliderDirection = newDirection;		
	}
	// }}}	
	,
	// {{{ setSliderWidth(newWidth)
    /**
     *	Specify width of slider - if now width is specified, the script will try to measure the height of width of the element where it is inserted.
     * 	
     *	@param String newWidth - Slider width(numeric or percentage) example: 100 or 90%
     *
     * @public	
     */		
	setSliderWidth : function(newWidth)
	{
		newWidth = newWidth + '';
		if(newWidth.indexOf('%')==-1)newWidth = newWidth + 'px';
		this.width = newWidth;	
	}
	// }}}	
	,
	// {{{ setSliderHeight(newHeight)
    /**
     *	Specify height of slider - if now width is specified, the script will try to measure the height of width of the element where it is inserted.
     * 	
     *	@param String newHeight - Slider width(numeric or percentage) example: 100 or 90%
     *
     * @public	
     */		
	setSliderHeight : function(newHeight)
	{
		newHeight = newHeight + '';
		if(newHeight.indexOf('%')==-1)newHeight = newHeight + 'px';
		this.height = height;	
	}
	// }}}	
	,	
	// {{{ setSliderReversed()
    /**
     *	Reverse slider, i.e. max at left instead of right or at bottom instead of top
     * 	
     *
     * @public	
     */		
	setSliderReversed : function()
	{
		this.sliderValueReversed = true;
	}
	// }}}	
	,
	// {{{ setOnChangeEvent(nameOfFunction)
    /**
     *	Specify which function to call when the slider has been moved.
     * 	
     *	@param String nameOfFunction - Name of function to call.
     *
     * @public	
     */	
    setOnChangeEvent : function(nameOfFunction)
    {
    	this.functionToCallOnChange = nameOfFunction;
    	
    }	
	// }}}	
	,		
	// {{{ setSliderMaxValue(newMaxValue)
    /**
     *	Set maximum value of slider
     * 	
     *	@param int newMaxValue - New slider max value
     *
     * @public	
     */	
    setSliderMaxValue : function(newMaxValue)
    {
    	this.sliderMaxValue = newMaxValue;
    	
    }	
	// }}}		
	,		
	// {{{ setSliderMinValue(newMinValue)
    /**
     *	Set minimum value of slider
     * 	
     *	@param int newMinValue - New slider min value
     *
     * @public	
     */	
    setSliderMinValue : function(newMinValue)
    {
    	this.sliderMinValue = newMinValue;
    	
    }	
	// }}}	
	,	
	// {{{ setSliderName(nameOfSlider)
    /**
     *	Specify name of slider.
     * 	
     *	@param String nameOfSlider - Name of function to call.
     *
     * @public	
     */	
    setSliderName : function(nameOfSlider)
    {
    	this.sliderName = nameOfSlider;
    	
    }	
	// }}}	
	,
	// {{{ setLayoutCss(nameOfNewCssFile)
    /**
     *	Specify a new CSS file for the slider(i.e. not using default css file which is slider.css)
     * 	
     *	@param String nameOfNewCssFile - Name of new css file.
     *
     * @public	
     */	
    setLayoutCss : function(nameOfNewCssFile)
    {
    	this.layoutCss = nameOfNewCssFile;
    }
	// }}}
	,
	// {{{ setLayoutCss(nameOfNewCssFile)
    /**
     *	Specify a new CSS file for the slider(i.e. not using default css file which is slider.css)
     * 	
     *	@param String nameOfNewCssFile - Name of new css file.
     *
     * @public	
     */	
    setInitialValue : function(newInitialValue)
    {
    	this.initialValue = newInitialValue;
    }
	// }}}		
	,
	// {{{ setSliderPointerDirection(directionOfPointer)
    /**
     *	In which direction should the slider handle point. 
     * 	
     *	@param String directionOfPointer - In which direction should the slider handle point(possible values: 'up','down','left','right'
     *
     * @public	
     */	
    setSliderPointerDirection : function(directionOfPointer)
    {
    	this.directionOfPointer = directionOfPointer;
    }
	// }}}	
	,	
	// {{{ setSliderValue(newValue)
    /**
     *	Set new position of slider manually
     * 	
     *	@param Int newValue - New value of slider
     *
     * @public	
     */	
    setSliderValue : function(newValue)
    {
    	var position = Math.floor((newValue / this.sliderMaxValue) * this.sliderSize);
    	if(this.sliderDirection=='hor'){
    		this.sliderHandleImg.style.left = position + 'px';	
    	}else{
    		this.sliderHandleImg.style.top = position + 'px';	
    	}
    }
	// }}}	
	,
	// {{{ setNumberOfSliderSteps(numberOfSteps)
    /**
     *	Divide slider into steps, i.e. instead of having a smooth slide.
     * 	
     *	@param Int numberOfSteps - Number of steps
     *
     * @public	
     */	
    setNumberOfSliderSteps : function(numberOfSteps)
    {
    	this.numberOfSteps = numberOfSteps;
    }
	// }}}	
	,
	// {{{ setStepLinesVisible(visible)
    /**
     *	Divide slider into steps. 
     * 	
     *	@param Boolean visible - When using static steps, make lines indicating steps visible or hidden(true = visible(default), false = hidden)
     *
     * @public	
     */	
    setStepLinesVisible : function(visible)
    {
    	this.stepLinesVisibility = visible;
    }
	// }}}	
	,
	// {{{ __setWidthAndHeightDynamically()
    /**
     *	Automatically set height and width of slider.
     * 	
     *
     * @private	
     */		
	__setWidthAndHeightDynamically : function()
	{
		// No width or height specified - try to measure it from the size of parent box

		if(!this.width || this.width==0)this.width = this.targetObj.clientWidth+'';
		if(!this.height || this.height==0)this.height = this.targetObj.clientHeight+'';
		if(!this.width || this.width==0)this.width = this.targetObj.offsetWidth+'';
		if(!this.height || this.height==0)this.height = this.targetObj.offsetHeight+'';

		if(this.width==0)return;
		if(this.height==0)return;
		
	
		if(this.width.indexOf('px')==-1 && this.width.indexOf('%')==-1)this.width = this.width + 'px';
		if(this.height.indexOf('px')==-1 && this.height.indexOf('%')==-1)this.height = this.height + 'px';		

		
	}
	// }}}	
	,
	// {{{ __createSlider()
    /**
     *	Creates the HTML for the slider dynamically
     * 	
     *
     * @private	
     */		
    __createSlider : function(initWidth)
    {
    	if(this.targetObj.clientWidth==0 || initWidth==0){
    		var timeoutTime = 100;
    		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + this.objectIndex + '].__createSlider(' + this.targetObj.clientWidth + ')',timeoutTime);
    		return;    		
    	}    	
    	this.__setWidthAndHeightDynamically();
    	this.indexThisSlider = DHTMLSuite.sliderObjects.length;
    	DHTMLSuite.sliderObjects[this.indexThisSlider] = this;
    	
    	window.refToThisObject = this;
    	
    	// Creates a parent div for the slider
    	var div = document.createElement('DIV');
    	
    	div.style.width = this.width;
    	div.style.cursor = 'default';
    	div.style.height = this.height;
    	div.style.position = 'relative';
    	div.id = 'sliderNumber' + this.indexThisSlider;	// the numeric part of this id is used inside the __setPositionFromClick method
    	div.onmousedown = this.__setPositionFromClick;
    	DHTMLSuite.commonObj.__addEventElement(div);    	
    	this.targetObj.appendChild(div);
    	
    	var sliderObj = document.createElement('DIV');
    	
    	
    	if(this.sliderDirection=='hor'){	// Horizontal slider.
    		sliderObj.className='DHTMLSuite_slider_horizontal';
    		sliderObj.style.width = div.clientWidth + 'px';
    		this.sliderSize = div.offsetWidth - this.sliderWidth;
    		    		
    		// Creating slider handle image.
    		var sliderHandle = document.createElement('IMG');
    		var srcHandle = 'slider_handle_down.gif';
    		sliderHandle.style.bottom = '2px';
    		if(this.directionOfPointer=='up'){
    			srcHandle = 'slider_handle_up.gif';
    			sliderHandle.style.bottom = '0px';    			
    		}    		
    		sliderHandle.src = DHTMLSuite.configObj.imagePath + srcHandle;
    		div.appendChild(sliderHandle);
    		
    		// Find initial left position of slider
    		var leftPos;
    		if(this.sliderValueReversed){
    			leftPos = Math.round(((this.sliderMaxValue - this.initialValue) / this.sliderMaxValue) * this.sliderSize) -1;
    		}else{
    			leftPos = Math.round((this.initialValue / this.sliderMaxValue) * this.sliderSize);
    		}
			sliderHandle.style.left =  leftPos + 'px';	
    		
    		
    	}else{
    		sliderObj.className='DHTMLSuite_slider_vertical';
    		sliderObj.style.height = div.clientHeight + 'px';
    		this.sliderSize = div.clientHeight - this.sliderWidth;
    		
    		// Creating slider handle image.
    		var sliderHandle = document.createElement('IMG');
    		var srcHandle = 'slider_handle_right.gif';
    		sliderHandle.style.left = '0px';
    		if(this.directionOfPointer=='left'){
    			srcHandle = 'slider_handle_left.gif';
    			sliderHandle.style.left = '0px';    			
    		}    		
    		sliderHandle.src = DHTMLSuite.configObj.imagePath + srcHandle;
    		div.appendChild(sliderHandle);
    		
    		// Find initial left position of slider
    		var topPos;
    		if(!this.sliderValueReversed){
    			topPos = Math.floor(((this.sliderMaxValue - this.initialValue) / this.sliderMaxValue) * this.sliderSize);
    		}else{
    			topPos = Math.floor((this.initialValue / this.sliderMaxValue) * this.sliderSize);
    		}
    		
    		sliderHandle.style.top = topPos + 'px';	
			    		
    		
    	}
    	
    	sliderHandle.id = 'sliderForObject' + this.indexThisSlider;
    	sliderHandle.style.position = 'absolute';
    	sliderHandle.style.zIndex = 5;
    	sliderHandle.onmousedown = this.__initializeSliderDrag;
    	sliderHandle.ondragstart = function() { return DHTMLSuite.commonObj.cancelEvent() };	
    	sliderHandle.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };	
    	DHTMLSuite.commonObj.__addEventElement(sliderHandle);
		this.sliderHandleImg = sliderHandle;
		
		if(!DHTMLSuite.slider_generalMouseEventsAdded){
	    	// Adding onmousemove event to the <html> tag
	    	DHTMLSuite.commonObj.addEvent(document.documentElement,"mousemove",this.__moveSlider);
	    	// Adding onmouseup event to the <html> tag.
	    	DHTMLSuite.commonObj.addEvent(document.documentElement,"mouseup",this.__stopSlideProcess);
    		DHTMLSuite.slider_generalMouseEventsAdded = true;
		}
    	
    	
    	sliderObj.innerHTML = '<span style="cursor:default"></span>';	// In order to get a correct height/width of the div
    	div.appendChild(sliderObj);  			
    	
    	if(this.numberOfSteps && this.stepLinesVisibility){	// Number of steps defined, create graphical lines
    		var stepSize = this.sliderSize / this.numberOfSteps;
    		for(var no=0;no<=this.numberOfSteps;no++){
    			var lineDiv = document.createElement('DIV');
    			lineDiv.style.position = 'absolute';
    			lineDiv.innerHTML = '<span></span>';
    			div.appendChild(lineDiv);
    			if(this.sliderDirection=='hor'){
    				lineDiv.className='DHTMLSuite_smallLines_vertical';
    				lineDiv.style.left = Math.floor((stepSize * no) + (this.sliderWidth/2)) + 'px';
    			}else{
    				lineDiv.className='DHTMLSuite_smallLines_horizontal';
    				lineDiv.style.top = Math.floor((stepSize * no) + (this.sliderWidth/2)) + 'px';
    				lineDiv.style.left = '14px';
    			}	
    			
    		}
    	}
    	
    }
	// }}}	
	,
	// {{{ __initializeSliderDrag()
    /**
     *	Init slider drag
     * 	
     *
     * @private	
     */	
    __initializeSliderDrag : function(e)
    {
    	if(document.all)e = event;
    	var numIndex = this.id.replace(/[^0-9]/gi,'');	// Get index in the DHTMLSuite.sliderObject array. We get this from the id of the slider image(i.e. "this").
    	var sliderObj = DHTMLSuite.sliderObjects[numIndex];
    	DHTMLSuite.indexOfCurrentlyActiveSlider = numIndex;
    	sliderObj.slideInProcessTimer = 0;
    	if(sliderObj.sliderDirection=='hor'){
    		sliderObj.slide_event_pos = e.clientX;	// Get start x position of mouse pointer
    		sliderObj.slide_start_pos = this.style.left.replace('px','')/1;	// Get x position of slider
    	}else{
    		sliderObj.slide_event_pos = e.clientY;	// Get start x position of mouse pointer
    		sliderObj.slide_start_pos = this.style.top.replace('px','')/1;	// Get x position of slider
    	}
    	
    	sliderObj.__waitBeforeSliderDragStarts();
    	return false;	// Firefox need this line.
    }
	// }}}	
	,
	// {{{ __setPositionFromClick()
    /**
     *	Set position from click - click on slider - move handle to the mouse pointer
     * 	
     *
     * @private	
     */	
    __setPositionFromClick : function(e)
    {
    	if(document.all)e = event;
		// Tag of element triggering this event. If it's something else than a <div>, return without doing anything, i.e. mouse down on slider handle.
		if (e.target) srcEvent = e.target;
			else if (e.srcElement) srcEvent = e.srcElement;
			if (srcEvent.nodeType == 3) // defeat Safari bug
				srcEvent = srcEvent.parentNode;
		if(srcEvent.tagName!='DIV')return;		
				
    	var numIndex = this.id.replace(/[^0-9]/gi,'');	// Get index in the DHTMLSuite.sliderObject array. We get this from the id of the slider image(i.e. "this").
 		var sliderObj = DHTMLSuite.sliderObjects[numIndex];
 		
     	if(sliderObj.numberOfSteps){
    		modValue = sliderObj.sliderSize / sliderObj.numberOfSteps;	// Find value to calculate modulus by	
    	}	
    	
    	if(sliderObj.sliderDirection=='hor'){
    		var handlePos = (e.clientX - DHTMLSuite.commonObj.getLeftPos(this) - sliderObj.sliderWidth);
    	}else{
    		var handlePos = (e.clientY - DHTMLSuite.commonObj.getTopPos(this) - sliderObj.sliderWidth);
    	}
		if(sliderObj.numberOfSteps){	// Static steps defined
			var mod = handlePos % modValue;	// Calculate modulus
			if(mod>(modValue/2))mod = modValue-mod; else mod*=-1;	// Should we move the slider handle left or right?
			handlePos = handlePos + mod;
		}
		if(handlePos<0)handlePos = 0;	// Don't allow negative values
		if(handlePos > sliderObj.sliderSize)handlePos = sliderObj.sliderSize; // Don't allow values larger the slider size	
    	
   		if(sliderObj.sliderDirection=='hor'){
 			sliderObj.sliderHandleImg.style.left = handlePos + 'px';		
 			if(!sliderObj.sliderValueReversed){
				returnValue = Math.round((handlePos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}else{
				returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}						
 		}else{
 			sliderObj.sliderHandleImg.style.top = handlePos + 'px';	 			
			if(sliderObj.sliderValueReversed){
				returnValue = Math.round((topPos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}else{
				returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}		
 		}	 		
 		returnValue = returnValue + sliderObj.sliderMinValue;
 		if(sliderObj.functionToCallOnChange)eval(sliderObj.functionToCallOnChange + '(' + returnValue + ',"' + sliderObj.sliderName + '")');
 			
    }
	// }}}	
	,
	// {{{ __waitBeforeSliderDragStarts()
    /**
     *	A small delay before the drag process starts.
     * 	
     *
     * @private	
     */	
    __waitBeforeSliderDragStarts : function()
    {
		if(this.slideInProcessTimer<10 && this.slideInProcessTimer>=0){
			this.slideInProcessTimer = this.slideInProcessTimer +1;
			window.refToThisSlider = this;
			setTimeout('window.refToThisSlider.__waitBeforeSliderDragStarts()',5);
		}
		
    }
    // }}}	
	,
	// {{{ __moveSlider()
    /**
     *	Move the slider
     * 	
     *
     * @private	
     */	
    __moveSlider : function(e)
    {
    	if(DHTMLSuite.indexOfCurrentlyActiveSlider===false)return;
    	var sliderObj = DHTMLSuite.sliderObjects[DHTMLSuite.indexOfCurrentlyActiveSlider];
    	if(document.all)e = event;
		if(sliderObj.slideInProcessTimer<10)return;
		
    	var returnValue;
    	
    	// Static steps defined ?
    	if(sliderObj.numberOfSteps){	// Find value to calculate modulus by
    		modValue = sliderObj.sliderSize / sliderObj.numberOfSteps;	
    	}	
    	
    	if(sliderObj.sliderDirection=='hor'){
    		var handlePos = e.clientX - sliderObj.slide_event_pos + sliderObj.slide_start_pos;
    	}else{
    		var handlePos = e.clientY - sliderObj.slide_event_pos + sliderObj.slide_start_pos;
    	}
		if(sliderObj.numberOfSteps){
 			var mod = handlePos % modValue;
 			if(mod>(modValue/2))mod = modValue-mod; else mod*=-1; 				
				handlePos = handlePos + mod;
		}    	
		if(handlePos<0)handlePos = 0;
		if(handlePos > sliderObj.sliderSize)handlePos = sliderObj.sliderSize;		
		
		if(sliderObj.sliderDirection=='hor'){
			sliderObj.sliderHandleImg.style.left = handlePos + 'px';		
			if(!sliderObj.sliderValueReversed){
				returnValue = Math.round((handlePos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}else{
				returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}
		}else{
			sliderObj.sliderHandleImg.style.top = handlePos + 'px';
			if(sliderObj.sliderValueReversed){
				returnValue = Math.round((handlePos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}else{
				returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
			}
						
		}		
			
		returnValue = returnValue + sliderObj.sliderMinValue;
		
		
		if(sliderObj.functionToCallOnChange)eval(sliderObj.functionToCallOnChange + '(' + returnValue + ',"' + sliderObj.sliderName + '")');
		
    }
    // }}}	
	,
	// {{{ __stopSlideProcess()
    /**
     *	Stop the drag process
     * 	
     *
     * @private	
     */	
    __stopSlideProcess : function(e)
    {
    	if(!DHTMLSuite.indexOfCurrentlyActiveSlider)return;
    	var sliderObj = DHTMLSuite.sliderObjects[DHTMLSuite.indexOfCurrentlyActiveSlider];
    	sliderObj.slideInProcessTimer = -1;	
    }	
}