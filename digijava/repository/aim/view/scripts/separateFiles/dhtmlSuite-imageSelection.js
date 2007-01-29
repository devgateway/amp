if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML Image enlarger.
*
*	Created:						January, 14th, 2006
*	@class Purpose of class:		Tool for selecting and dragging images
*			
*	Css files used by this script:	image-selection.css
*
*	Demos of this class:			
*
* 	Update log:
*
************************************************************************************************************/
/**
* @constructor
* @class Purpose of class:	Tool to drag rectangle around images
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.imageSelection = function(){
	
	var layoutCSS;
	
	var callBackFunction_onDrop;		// Call back function on drop
	
	var objectIndex;
	var divElementSelection;
	var divElementSelection_transparent;
	var selectableElements;
	var selectedElements;			// Array of selected elements
	var selectableElementsScreenProperties;
	var collectionModelReference;	// Reference to media collection ( data source for images )
	var destinationElements;		// Array of destination elements.
	var currentDestinationElement;	// Reference to current destination element
	
	var selectionStatus;			// -1 when selection isn't in progress, 0 when it's being initialized, 5 when it's ready
	var dragStatus;					// -1 = drag not started, 0-5 = drag initializing, 5 = drag in process.
	var startCoordinates;
	var selectionResizeInProgress;	// variable which is true when code for the selection area is running
	var selectionStartArea;			// Selection can only starts within this element
	var selectionOrDragStartEl;		// Element triggering drag or selection
	
	var cssTextForSources;			// Manual css text for sources - you can use this instead of the imageSelection class. This may be useful when you have a lot of nodes
									// since IE sometimes becomes slow when dealing with css classes.	
	
	this.cssTextForSources = '';
	this.selectionResizeInProgress = false;
	this.selectionStatus = -1;
	this.dragStatus = -1;
	this.startCoordinates = new Array();
	this.layoutCSS = 'image-selection.css';
	this.selectableElements = new Array();
	this.destinationElements = new Array();
	this.collectionModelReference = false;
	this.selectableElementsScreenProperties = new Array();
	this.selectedElements = new Array();
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}	

	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;	
	
}

DHTMLSuite.imageSelection.prototype = {
	// {{{ init()
    /**
     *	Initializes the script
     *
     *
     * @public	
     */		
	init : function()
	{
		try{
			DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		}catch(e){
			alert('Unable to load css file dynamically. You need to include dhtmlSuite-common.js');
		}
		this.__createdivElementsForSelection();
		this.__createdivElementsForDrag();
		this.__addEvents();
		this.__setSelectableElementsScreenProperties();
	}
	// }}}
	,
	// {{{ addDestinationElement()
    /**
     *	Add single destination element
     *
     *	Object elementReference - Element reference, id of element or the element it's self.
     *
     * @public	
     */		
	addDestinationElement : function(elementReference)
	{
		if(typeof elementReference == 'string'){
			elementReference = document.getElementById(elementReference);
		}
		this.destinationElements[this.destinationElements.length] = elementReference;
	}
	// }}}
	,
	// {{{ addSelectableElements()
    /**
     *	Add selectable elements
     *
     *	@param Object parentElementReference - id of parent html element or the parent element it's self. all direct children will be dragable
     *	@param String tagName - Which tag, example "td","li" or "a"
     *	@param String className - optional element
     *	@return Boolean success - true if parent element was found, false otherwise.
     *
     * @public	
     */		
	addDestinationElementsByTagName : function(parentElementReference,tagName,className)
	{
		if(typeof parentElementReference == 'string'){
			parentElementReference = document.getElementById(parentElementReference);
		}		
		
		
		if(!parentElementReference){
			return false;
		}
		
		var subs = parentElementReference.getElementsByTagName(tagName);
		for(var no=0;no<subs.length;no++){
			if(className && subs[no].className!=className)continue;
			this.destinationElements[this.destinationElements.length] = subs[no];	
		}
		this.__addEventsToDestinationElements(subs);
		return true;
		
	}
	// }}}
	,
	// {{{ addSelectableElements()
    /**
     *	Add selectable elements
     *
     *	Object parentElementReference - id of parent html element or the parent element it's self. all direct children will be dragable
     *
     * @public	
     */		
	addSelectableElements : function(parentElementReference)
	{
		var obj;
		if(typeof parentElementReference == 'string')obj = document.getElementById(parentElementReference);else obj = parentElementReference;
		var subElement = obj.getElementsByTagName('*')[0];
		
		while(subElement){
			this.selectableElements[this.selectableElements.length] = subElement;
			this.__addPropertiesToSelectableElement(subElement);
			subElement = subElement.nextSibling;	
		}		
	}	
	// }}}
	,
	// {{{ addSelectableElement()
    /**
     *	Add single selectable element
     *
     *	Object elementReference - id of html element or the reference to the element it's self. all direct children will be dragable
     *
     * @public	
     */		
	addSelectableElement : function(elementReference)
	{
		if(typeof elementReference == 'string'){
			this.selectableElements[this.selectableElements.length] = document.getElementById(elementReference);
		}else{
			this.selectableElements[this.selectableElements.length] = elementReference;
		}	
		this.__addPropertiesToSelectableElement(elementReference);
		
	}	
	// }}}
	,
	// {{{ setCallBackFunctionOnDrop()
    /**
     *	Specify call back function - on drop 
     *	This function will be called when elements are dropped on a destination node
     *	Arguments to this function will be an array of the dragged elements and a reference to the destionation object.
     *
     *
     * @public	
     */		
	setCallBackFunctionOnDrop : function(functionName)
	{
		this.callBackFunction_onDrop = functionName;
	}
	// }}}
	,
	// {{{ setSelectionStartArea()
    /**
     *	Restrict where the selection may start.
     *
     *
     * @public	
     */		
	setSelectionStartArea : function(elementReference)
	{
		if(typeof elementReference == 'string'){
			elementReference = document.getElementById(elementReference);
		}
					
		this.selectionStartArea = elementReference;
		
	}
	// }}}
	,
	// {{{ __createdivElementSelectionsForSelection()
    /**
     *	Create div elements for the selection
     *
     *
     * @private	
     */		
	__createdivElementsForSelection : function()
	{
		/* Div elements for selection */
		this.divElementSelection = document.createElement('DIV');
		this.divElementSelection.style.display='none';
		this.divElementSelection.id = 'DHTMLSuite_imageSelectionSel';	
		
		document.body.insertBefore(this.divElementSelection,document.body.firstChild);
		this.divElementSelection_transparent = document.createElement('DIV');
		this.divElementSelection_transparent.id = 'DHTMLSuite_imageSelection_transparentDiv';
		this.divElementSelection.appendChild(this.divElementSelection_transparent);
	}
	// }}}
	,
	// {{{ __setMediaCollectionModelReference()
    /**
     *	Specify media collection model reference. 
     *
     *
     * @private	
     */			
	__setMediaCollectionModelReference : function(collectionModelReference){
		this.collectionModelReference = collectionModelReference;
	}
	,
	// {{{ __createdivElementsForDrag()
    /**
     *	Create div elements for the drag
     *
     *
     * @private	
     */		
	__createdivElementsForDrag : function()
	{
		/* Div elements for selection */
		this.divElementDrag = document.createElement('DIV');
		this.divElementDrag.style.display='none';
		this.divElementDrag.id = 'DHTMLSuite_imageSelectionDrag';
		document.body.insertBefore(this.divElementDrag,document.body.firstChild);	
		
		this.divElementDragContent = document.createElement('DIV');
		this.divElementDragContent.id = 'DHTMLSuite_imageSelectionDragContent';
		this.divElementDrag.appendChild(this.divElementDragContent);
		
		var divElementTrans = document.createElement('DIV');
		divElementTrans.className = 'DHTMLSuite_imageSelectionDrag_transparentDiv';
		this.divElementDrag.appendChild(divElementTrans);
		
	
	}
	// }}}
	,
	// {{{ __initImageSelection()
    /**
     *	Mouse down - start selection or drag.
     *
     *
     * @private	
     */		
	__initImageSelection : function(e)
	{
		var initImageSelector;
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		
		if(src.onmousedown){	/* Exception for the drag */		
			if(src.onmousedown.toString().indexOf('initImageSelector')<0)return;	
		}
		if(src.className.indexOf('paneSplitter_vertical')>=0 || src.className.indexOf('paneSplitter_horizontal')>=0 ){	/* Exception for the drag */
			return;	
		}
		
		this.selectionOrDragStartEl = src;
		this.startCoordinates['x'] = e.clientX + document.documentElement.scrollLeft + 3;
		this.startCoordinates['y'] = e.clientY + document.documentElement.scrollTop + 3; 
					
		if(!this.__isReadyForDrag(e)){	/* Image selection */
			if(!e.shiftKey && !e.ctrlKey)this.__clearSelectedElementsArray();
			this.selectionStatus = 0;
			this.divElementSelection.style.left = this.startCoordinates['x'] + 'px';
			this.divElementSelection.style.top = this.startCoordinates['y'] + 'px';
			this.divElementSelection.style.width = '1px';
			this.divElementSelection.style.height = '1px';
			this.__setSelectableElementsScreenProperties();
			this.__countDownToSelectionStart();
			
		}else{	/* Drag selected images */
			this.divElementDrag.style.left = this.startCoordinates['x'] + 'px';
			this.divElementDrag.style.top = this.startCoordinates['y'] + 'px';			
			this.dragStatus = 0;	
			this.__countDownToDragStart();
			
			
		}	
		
		return false;	
	}
	// }}}
	,
	// {{{ __isReadyForDrag()
    /**
     *	A small delay before selection starts.
     *
     *
     * @private	
     */		
	__isReadyForDrag : function(e)
	{
		var src = DHTMLSuite.commonObj.getObjectByAttribute(e,'DHTMLSuite_selectableElement');
		if(!src)return false;
		if(this.selectedElements.length>0)return true;
		return false;
		
	}
	// }}}
	,
	// {{{ __countDownToDragStart()
    /**
     *	A small delay before drag starts.
     *
     *
     * @private	
     */		
	__countDownToDragStart : function()
	{
		if(this.dragStatus>=0 && this.dragStatus<5){
			var ind = this.objectIndex;
			this.dragStatus++;
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__countDownToDragStart()',30);				
		}
		if(this.dragStatus==5){
			this.__fillDragBoxWithSelectedItems();
			this.divElementDrag.style.display='block';	// Show selection box.
		}
		
	}
	// }}}
	,
	// {{{ __fillDragBoxWithSelectedItems()
    /**
     *	Fill drag box with selected items.
     *
     *
     * @private	
     */			
	__fillDragBoxWithSelectedItems : function()
	{
		this.divElementDragContent.innerHTML = '';
		if(this.collectionModelReference){	/* Media model exists */
			
			for(var no=0;no<this.selectedElements.length;no++){
				var obj = this.selectedElements[no];
				var mediaRefId = obj.getAttribute('mediaRefId');
				if(!mediaRef)mediaRef = obj.mediaRefId;
				var mediaRef = this.collectionModelReference.getMediaById(mediaRefId);
			

				var div = document.createElement('DIV');
				div.className = 'DHTMLSuite_imageSelectionDragBox';
				div.style.backgroundImage = 'url(\'' + mediaRef.thumbnailPathSmall + '\')';
				this.divElementDragContent.appendChild(div);
			}			
		}else{	/* No media model - Just clone the node - May have to figure out something more clever here as this hasn't been tested fully yet */
			for(var no=0;no<this.selectedElements.length;no++){
				var el = this.selectedElements.cloneNode(true);	
				this.divElementDragContent.appendChild(el);				
			}			
		}		
	}
	// }}}
	,
	// {{{ __countDownToSelectionStart()
    /**
     *	A small delay before selectino starts.
     *
     *
     * @private	
     */		
	__countDownToSelectionStart : function()
	{
		if(this.selectionStatus>=0 && this.selectionStatus<5){
			var ind = this.objectIndex;
			this.selectionStatus++;
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__countDownToSelectionStart()',10);	
		}
		if(this.selectionStatus==5)this.divElementSelection.style.display='block';	// Show selection box.
		return false;
	}
	// }}}
	,
	// {{{ __moveDragBox()
    /**
     *	Move div with the dragged elements
     *
     *
     * @private	
     */		
	__moveDragBox : function(e)
	{		
		if(this.dragStatus<5)return;	
		if(document.all)e = event;
		this.divElementDrag.style.left = (this.startCoordinates['x'] + (e.clientX + 3 - this.startCoordinates['x'])) + 'px';			
		this.divElementDrag.style.top = (this.startCoordinates['y'] + (e.clientY + 3 - this.startCoordinates['y'])) + 'px';			
	}
	// }}}	
	,
	// {{{ __resizeSelectionDivBox()
    /**
     *	Resize selection div box.
     *
     *
     * @private	
     */		
	__resizeSelectionDivBox : function(e)
	{
		if(this.selectionStatus<5)return;	// Selection in progress ?
		if(this.selectionResizeInProgress)return;	// If this function is allready running, don't start another iteration until it's finished.
					
		this.selectionResizeInProgress = true;	// Selection code is running!

		if(document.all)e = event;
		var width = e.clientX - this.startCoordinates['x'];
		var height = e.clientY + document.documentElement.scrollTop - this.startCoordinates['y'];
		
		if(width>0){
			this.divElementSelection.style.left = (this.startCoordinates['x']) + 'px'; 	
			this.divElementSelection.style.width = width + 'px'; 	
				
		}else{
			this.divElementSelection.style.width = (this.startCoordinates['x'] - (this.startCoordinates['x'] + width)) + 'px'; 	
			this.divElementSelection.style.left =(this.startCoordinates['x'] + width) + 'px'; 
		}			
		if(height>0){
			this.divElementSelection.style.top = (this.startCoordinates['y']) + 'px'; 	
			this.divElementSelection.style.height = height + 'px'; 
		}else{
			this.divElementSelection.style.height = (this.startCoordinates['y'] - (this.startCoordinates['y'] + height)) + 'px'; 	
			this.divElementSelection.style.top =(this.startCoordinates['y'] + height) + 'px'; 
		}
		// this.__clearSelectedElementsArray();
		this.__highlightElementsWithinSelectionArea();
		this.selectionResizeInProgress = false;
	}
	// }}}
	,
	// {{{ __clearSingleElementFromSelectedArray()
    /**
     *	Clear a single element from the selected array
     *
     *  @param Object - HTML element
     *
     * @private	
     */		
	__clearSingleElementFromSelectedArray : function(el)
	{
		for(var no=0;no<this.selectedElements.length;no++){
			if(this.selectedElements[no]==el){
				this.selectedElements[no].className = this.selectedElements[no].className.replace(' imageSelection','');
				this.selectedElements.splice(no,1);
				return;
			}
		}		
	}
	// }}}
	,
	// {{{ __clearSelectedElementsArray()
    /**
     *	Remove highlight effect from all previous selected elements.
     *
     *
     * @private	
     */		
	__clearSelectedElementsArray : function()
	{
		
		for(var no=0;no<this.selectedElements.length;no++){
			if(this.selectedElements[no].className.indexOf('imageSelection')>=0)this.selectedElements[no].className = this.selectedElements[no].className.replace(' imageSelection','');
		}	
		this.selectedElements = new Array();	
	}
	// }}}
	,
	// {{{ __highlightElementsWithinSelectionArea()
    /**
     *	Loop through selectable elements and highlight those within the selection area.
     *
     *
     * @private	
     */		
	__highlightElementsWithinSelectionArea : function()
	{		
		var x1 = this.divElementSelection.style.left.replace('px','')/1;
		var y1 = this.divElementSelection.style.top.replace('px','')/1;
		var x2 = x1 + this.divElementSelection.style.width.replace('px','')/1;
		var y2 = y1 + this.divElementSelection.style.height.replace('px','')/1;
		for(var no=0;no<this.selectableElements.length;no++){
			if(this.__isElementWithinSelectionArea(this.selectableElements[no],x1,y1,x2,y2)){
				this.__addSelectedElement(this.selectableElements[no]);
			}else{
				this.__clearSingleElementFromSelectedArray(this.selectableElements[no]);
			}
		}	
	}
	// }}}
	,
	// {{{ __isElementInSelectedArray()
    /**
     *	Is element allready added to the selected item array ?
     *
     *
     * @private	
     */		
	__isElementInSelectedArray : function(el)
	{
		for(var no=0;no<this.selectedElements.length;no++){	/* element allready added ? */
			if(this.selectedElements[no]==el)return true;
		}		
		return false;		
	}
	,
	// {{{ __addSelectedElement()
    /**
     *	Highlight element and add it to the collection of selected elements.
     *
     *
     * @private	
     */		
	__addSelectedElement : function(el)
	{
		if(el.className.indexOf('imageSelection')==-1){
			if(el.className)
				el.className = el.className + ' imageSelection';	// Adding " imageSelection" to the class name
			else
				el.className = 'imageSelection';
		}
		if(this.__isElementInSelectedArray(el))return;	
		this.selectedElements[this.selectedElements.length] = el;	// Add element to selected elements array
		
	}
	// }}}
	,
	// {{{ __setSelectableElementsScreenProperties()
    /**
     *	Save selectable elements x,y, width and height - this is done when the selection process is initiated. 
     *
     *	@return Boolean element within selection area. If the selection box is over an element, return true, otherwise return false
     *
     * @private	
     */	
	__isElementWithinSelectionArea : function(el,x1,y1,x2,y2)
	{		
		var elX1 = this.selectableElementsScreenProperties[el.id].x;
		var elY1 = this.selectableElementsScreenProperties[el.id].y;
		var elX2 = this.selectableElementsScreenProperties[el.id].x + this.selectableElementsScreenProperties[el.id].width;
		var elY2 = this.selectableElementsScreenProperties[el.id].y + this.selectableElementsScreenProperties[el.id].height;
		
		/*		
		ILLUSTRATION - Image boxes within the boundaries of a selection area.
		
		|-----------|   |-----------|   |-----------|
		|	BOX		|	|	BOX		|	|	BOX		|
		|	|-----------------------------------|	|
		|	|		|	|			|	|		|	|
		|---|-------|   |-----------|	|-----------|
			|	SELECTION AREA					|	
			|									|	
		|-----------|	|-----------|	|-----------|
		|	|	BOX	|	|	BOX		|	|	BOX	|	|
		|	|		|	|			|	|		|	|
		|	|		|	|			|	|		|	|
		|-----------|	|-----------|	|-----------|
			|									|
			|									|	
		|-----------|	|-----------|	|-----------|
		|	|		|	|			|	|		|	|
		|	|-------|---|-----------|---|-------|	|
		|BOX		|	|BOX		|	|	BOX	|	|
		|-----------|	|-----------|	|-----------|		
		
		*/
		if(elX2<x1)return false;
		if(elY2<y1)return false;
		if(elX1>x2)return false;
		if(elY1>y2)return false;
		if((elY1<=y1 && elY2>=y1) || (elY1>=y1 && elY2<=y2) || (elY1<=y2 && elY2>=y2)){	/* Y coordinates of element within selection area */
			if(elX1<=x1 && elX2>=x1)return true;	/* left edge of element at the left of selection area, but right edge within */
			if(elX1>=x1 && elX2<=x2)return true;	/* Both left and right edge of element within selection area */
			if(elX1<=x2 && elX2>=x2)return true;	/* Left edge of element within selection area, but right element outside */	
		}
		
		return false;
		
	}
	// }}}
	,
	// {{{ __setSelectableElementsScreenProperties()
    /**
     *	Save selectable elements x,y, width and height - this is done when the selection process is initiated. 
     *
     *
     * @private	
     */		
	__setSelectableElementsScreenProperties : function()
	{

		for(var no=0;no<this.selectableElements.length;no++){
			var obj = this.selectableElements[no];
			var id = obj.id;
			this.selectableElementsScreenProperties[id] = new Array();
			var ref = this.selectableElementsScreenProperties[id];
			ref.x = DHTMLSuite.commonObj.getLeftPos(obj);
			ref.y = DHTMLSuite.commonObj.getTopPos(obj);
			ref.width = obj.offsetWidth;
			ref.height = obj.offsetHeight;
		}
			
	}
	// }}}
	,
	// {{{ __endImageSelection()
    /**
     *	Mouse up event - hide the rectangle
     *
     *
     * @private	
     */		
	__endImageSelection : function(e)
	{
		if(document.all)e = event;
		if(this.selectionStatus>=0){
			this.divElementSelection.style.display='none';
			if(this.__isReadyForDrag(e) && this.selectionStatus==-1)this.__clearSelectedElementsArray();
			this.selectionStatus = -1;	
		}
		if(this.dragStatus>=0){			
			var src = DHTMLSuite.commonObj.getSrcElement(e);
			if(this.currentDestinationElement)this.__handleCallBackFunctions('drop');	
			this.divElementDrag.style.display='none';				
			if(src!=this.selectionOrDragStartEl || !src.className)this.__clearSelectedElementsArray();	
			this.__deselectDestinationElement();	
			this.dragStatus = -1;				
		}		
	}
	// }}}
	,
	// {{{ __handleCallBackFunctions()
    /**
     *	Handle call back function, i.e. evaluate js
     *	 
     *	String action - Which call back
     *
     * @private	
     */		
	__handleCallBackFunctions : function(action)
	{
		var callbackString = '';
		switch(action)
		{
			case 'drop':
				if(this.callBackFunction_onDrop)callbackString = this.callBackFunction_onDrop;
				break;
				
		}
		
		if(callbackString)eval(callbackString + '(this.selectedElements,this.currentDestinationElement)');
		
	}
	// }}}
	,
	// {{{ __deselectDestinationElement()
    /**
     *	Mouse away from destination element
     *	Deselect it and clear the property currentDestinationElement  
     *
     *
     * @private	
     */		
	__deselectDestinationElement : function(e)
	{
		if(this.dragStatus<5)return;
		if(!this.currentDestinationElement)return;
		if(document.all)e = event;
		
		if(e && !DHTMLSuite.commonObj.isObjectClicked(this.currentDestinationElement,e))return;
		
		this.currentDestinationElement.className = this.currentDestinationElement.className.replace(' imageSelection','');
		this.currentDestinationElement.className = this.currentDestinationElement.className.replace('imageSelection','');
		this.currentDestinationElement = false;
	}
	// }}}
	,
	// {{{ __selectDestinationElement()
    /**
     *	Mouse over a destination element. 
     *
     *
     * @private	
     */		
	__selectDestinationElement : function(e){
		if(this.dragStatus<5)return;
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getObjectByAttribute(e,'imageSelectionDestination');
		this.currentDestinationElement = src;
		if(this.currentDestinationElement.className)
			this.currentDestinationElement.className = this.currentDestinationElement.className + ' imageSelection';
		else
			this.currentDestinationElement.className = 'imageSelection';		
	}
	// }}}
	,
	// {{{ __selectSingleElement()
    /**
     *	Mouse down on a specific element
     *
     *
     * @private	
     */		
	__selectSingleElement : function(e,eventType)
	{
		if(document.all) e = event;
		var src = DHTMLSuite.commonObj.getObjectByAttribute(e,'DHTMLSuite_selectableElement');
		if(eventType=='mousedown' && 1==2){
			this.__addSelectedElement(src);	
		}else{		
			var elementAllreadyInSelectedArray =this.__isElementInSelectedArray(src); 
			if(!e.ctrlKey && !elementAllreadyInSelectedArray)this.__clearSelectedElementsArray();
			if(e.ctrlKey && elementAllreadyInSelectedArray){
				this.__clearSingleElementFromSelectedArray(src);
			}else
				this.__addSelectedElement(src);	
		}
		
	}
	// }}}
	,
	// {{{ __addPropertiesToSelectableElement()
    /**
     *	Add mouse down event and assigne custom property to selectable element.
     *
     *
     * @private	
     */		
	__addPropertiesToSelectableElement : function(elementReference)
	{
		var ind = this.objectIndex;
		DHTMLSuite.commonObj.addEvent(elementReference,'mousedown',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__selectSingleElement(e,'mousedown'); });	// Add click event to single element
		//DHTMLSuite.commonObj.addEvent(elementReference,'click',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__selectSingleElement(e,'click'); });	// Add click event to single element
		elementReference.setAttribute('DHTMLSuite_selectableElement','1');	
		this.__addOnScrolleventsToSelectableElements(elementReference);		
	}	
	// }}}
	,
	// {{{ __addEventsToDestinationElements()
    /**
     *	Add mouse over event to destination objects.
     *	
     *	@param Array inputElements - optional - if given, only these elements will be parsed, if not give, all destination elements will be parsed
     * @private	
     */		
	__addEventsToDestinationElements : function(inputElements)
	{
		var ind = this.objectIndex;
		
		if(inputElements){
			for(var no=0;no<inputElements.length;no++){
				inputElements[no].onmouseover = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__selectDestinationElement(e); };
				inputElements[no].onmouseout = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__deselectDestinationElement(e); };
				inputElements[no].setAttribute('imageSelectionDestination','1');
				inputElements[no].imageSelectionDestination = '1';		
			}			
		}else{
			for(var no=0;no<this.destinationElements.length;no++){
				this.destinationElements[no].onmouseover = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__selectDestinationElement(e); };
				this.destinationElements[no].onmouseout = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__deselectDestinationElement(e); };
				this.destinationElements[no].setAttribute('imageSelectionDestination','1');
				this.destinationElements[no].imageSelectionDestination = '1';
				
			}
		}
		
	}
	// }}}
	,
	// {{{ __addOnScrolleventsToSelectableElements()
    /**
     *	Don't allow selection on scroll
     *
     *
     * @private	
     */			
	__addOnScrolleventsToSelectableElements : function(el)
	{
		var ind = this.objectIndex;
		var src = el;
		while(src && src.tagName.toLowerCase()!='body'){
			src = src.parentNode;	
			if(!src.onscroll)DHTMLSuite.commonObj.addEvent(src,'scroll',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endImageSelection(e); });
		}			
	}
	// }}}}
	,
	// {{{ __addEvents()
    /**
     *	Add basic events for this widget
     *
     *
     * @private	
     */		
	__addEvents : function()
	{
		var ind = this.objectIndex;
		document.documentElement.onselectstart = DHTMLSuite.commonObj.cancelEvent;	// disable text selection
		
		if(this.selectionStartArea){
			DHTMLSuite.commonObj.addEvent(this.selectionStartArea,'mousedown',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initImageSelection(e); });
		}else{
			DHTMLSuite.commonObj.addEvent(document.documentElement,'mousedown',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initImageSelection(e); });
		}
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__resizeSelectionDivBox(e); });
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragBox(e); });
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endImageSelection(e); });
		DHTMLSuite.commonObj.addEvent(window,'resize',function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setSelectableElementsScreenProperties(); });
		
		var imgs = document.getElementsByTagName('IMG');
		for(var no=0;no<imgs.length;no++){
			imgs[no].ondragstart = DHTMLSuite.commonObj.cancelEvent;
			if(!imgs[no].onmousedown)imgs[no].onmousedown = DHTMLSuite.commonObj.cancelEvent;
			DHTMLSuite.commonObj.__addEventElement(imgs[no]);
		}
		
		this.__addEventsToDestinationElements();

	}
	
}