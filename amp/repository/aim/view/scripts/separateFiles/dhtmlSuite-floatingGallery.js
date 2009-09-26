if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML context menu class
*
*	Created:						January, 12th, 2007
*	@class Purpose of class:		Floating gallery widget
*			
*
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Creates a floating gallery widget.
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.floatingGallery = function()
{
	var collectionModel;		// Reference to an object of class DHTMLSuite.mediaCollection	
	var layoutCSS;
	var divElement;				// Reference to parent div element.
	var divElementImageBoxes;	// Array of div elements for the images	
	var idOfParentElementToGallery;	// Id of parent element
	
	// Strings - callback functions.
	var callBackFunction_onClick;			// Call back on click
	var callBackFunction_onDblClick;		// Call back on dbl click
	var callBackFunction_onMouseOver;		// Call back on mouse over
	var callBackFunction_onMouseMove;		// Call back mouse move
	
	var imageSelectionObj;					// Object of class DHTMLSuite.imageSelection
	var objectIndex;
	
	this.layoutCSS = 'floating-gallery.css';	
	this.divElementImageBoxes = new Array();
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;	
		
}

DHTMLSuite.floatingGallery.prototype = 
{
	// {{{ setMediaCollectionRef()
    /**
     *	Specify reference to DHTMLSuite.mediaCollection
     *
     *	@param Object mediaCollectionRef -Object of class DHTMLSuite.mediaCollection
     *
     * @public	
     */		
	setMediaCollectionRef : function(mediaCollectionRef)
	{
		this.collectionModel = mediaCollectionRef;
	}	
	,
	// {{{ init()
    /**
     *	Initializes the widget
     *
     *
     * @public	
     */			
	init : function()
	{
		try{
			DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	
		}catch(e){
			alert('loadCSS method missing. You need to include dhtmlSuite-common.js');
		}
		this.__createMainDivElement();
		this.__createImageBoxes();	
		this.__initiallyHandleimageSelection();
	}
	// }}}
	,
	// {{{ setTargetId()
    /**
     *	Specify id of element where the gallery will be placed inside.
     *
     *	@param idOfParentElementToGallery - ID of HTML element - this widget will be inserted as child element.
     *
     * @public	
     */		
	setTargetId : function(idOfParentElementToGallery)
	{
		this.idOfParentElementToGallery = idOfParentElementToGallery;
	}
	// }}}
	,
	// {{{ setCallBackFunctionOnClick()
    /**
     *	Specify function to call when users clicks on an image
     *
     *	@param functionName - only the name of the image. an image object(DHTMLSuite.mediaModel) will be sent to this function(representing the image clicked on).
     *
     * @public	
     */		
	setCallBackFunctionOnClick : function(functionName)
	{
		this.callBackFunction_onClick = functionName;
		
	}
	,
	// {{{ setCallBackFunctionOnDblClick()
    /**
     *	Specify function to call when users clicks on an image
     *
     *	@param functionName - only the name of the image. an image object(DHTMLSuite.mediaModel) will be sent to this function(representing the image clicked on).
     *
     * @public	
     */		
	setCallBackFunctionOnDblClick : function(functionName)
	{
		this.callBackFunction_onDblClick = functionName;
		
	}
	,
	// {{{ setCallBackFunctionOnMouseOver()
    /**
     *	Specify function to call when the mouse pointer "enters" an image
     *
     *	@param functionName - only the name of the image. an image object(DHTMLSuite.mediaModel) will be sent to this function(representing the image the mouse rolled over).
     *
     * @public	
     */		
	setCallBackFunctionOnMouseOver : function(functionName)
	{
		this.callBackFunction_onMouseOver = functionName;
		
	}
	,
	// {{{ setCallBackFunctionOnMouseMove()
    /**
     *	Specify function to call when the mouse moves over on an image
     *
     *	@param functionName - only the name of the image. an image object(DHTMLSuite.mediaModel) will be sent to this function(representing the image the mouse rolled over).
     *
     * @public	
     */		
	setCallBackFunctionOnMouseMove : function(functionName)
	{
		this.callBackFunction_onMouseMove = functionName;
		
	}
	// }}}
	,
	// {{{ deleteImageFromGallery()
    /**
     *	Removes an image from the gallery. The image is removed from the view and from the media model.
     *
     *	@param String idOfImage - Id of image/media to delete, example: "image1"
     *
     * @public	
     */			
	deleteImageFromGallery : function(idOfImage)
	{
		var retId = this.collectionModel.__removeImage(idOfImage);
		if(retId){	// media model image exists with this id ?
			var obj = document.getElementById(retId);
			obj.parentNode.removeChild(obj);
		}else{	// id doesn't match id in media collection model. loop through div elements and check each one by id.
			for(var no=0;no<this.divElementImageBoxes.length;no++){
				if(this.divElementImageBoxes[no].id == idOfImage){	// Match found
					var mediaRefId = this.divElementImageBoxes[no].getAttribute('mediaRefId');	// get a media reference.
					if(!mediaRefId)mediaRefId = this.divElementImageBoxes[no].mediaRefId;
					var mediaRef = this.collectionModel.getMediaById(mediaRefId); 
					this.collectionModel.__removeImage(mediaRef.id);	// Remove media from collection mode.
					this.divElementImageBoxes[no].parentNode.removeChild(this.divElementImageBoxes[no]);	// remove image from view.
				}
			}
		}
	}
	// }}}
	,
	// {{{ destroy()
    /**
     *	Delete the gallery HTML elements
     *
     *
     * @public	
     */		
	destroy : function()
	{
		this.divElement.parentNode.removeChild(this.divElement);		
	}
	// }}}
	,
	// {{{ addImageSelectionObject()
    /**
     *	Add image selection feature to this gallery. Argument to this method is an object of class DHTMLSuite.imageSelection
     *
     *	@param Object imageSelectionObj - Object of class DHTMLSuite.imageSelection
     *
     * @private	
     */		
	addImageSelectionObject : function(imageSelectionObj)
	{
		this.imageSelectionObj = imageSelectionObj;		
	}
	// }}}
	,	
	// {{{ __createMainDivElement()
    /**
     *	Create  main div for this widget
     *
     *
     * @private	
     */		
	__createMainDivElement : function()
	{
		this.divElement = document.createElement('DIV');
		this.divElement.className = 'DHTMLSuite_floatingGalleryContainer';
		if(this.idOfParentElementToGallery)
			document.getElementById(this.idOfParentElementToGallery).appendChild(this.divElement);
		else
			document.body.appendChild(this.divElement);
	}
	// }}}
	,
	// {{{ __createImageBoxes()
    /**
     *	Create divs for each image
     *
     *
     * @private	
     */		
	__createImageBoxes : function()
	{
		var ind = this.objectIndex;
		for(var no=0;no<this.collectionModel.mediaObjects.length;no++){
			this.divElementImageBoxes[no] = document.createElement('DIV');
			this.divElementImageBoxes[no].className='DHTMLSuite_floatingGalleryImageBox';
			this.divElementImageBoxes[no].id=this.collectionModel.mediaObjects[no].id;
			this.divElementImageBoxes[no].style.backgroundImage = 'url("' + this.collectionModel.mediaObjects[no].thumbnailPath + '")';
			this.divElementImageBoxes[no].setAttribute('mediaRefId',this.collectionModel.mediaObjects[no].id);
			this.divElementImageBoxes[no].mediaRefId = this.collectionModel.mediaObjects[no].id;
			this.divElement.appendChild(this.divElementImageBoxes[no]);
			
			var titleDiv = document.createElement('DIV');
			titleDiv.className='DHTMLSuite_floatingGalleryImageTitle';
			titleDiv.innerHTML = this.collectionModel.mediaObjects[no].title;
			this.divElementImageBoxes[no].appendChild(titleDiv);
			
			if(this.callBackFunction_onClick)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'click',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('click'," + no+ "); });");
			if(this.callBackFunction_onDblClick)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'dblclick',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('dblClick'," + no+ "); });");
			if(this.callBackFunction_onMouseOver)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'mouseover',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('mouseOver'," + no+ "); });");
			if(this.callBackFunction_onMouseMove)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'mousemove',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('mouseOver'," + no+ "); });");
	
			
		}	
		
		var clearingDiv = document.createElement('DIV');
		clearingDiv.style.clear = 'both';
		this.divElement.appendChild(clearingDiv);	
	}
	,
	// {{{ __parseCallBackFunction()
    /**
     *	Parses callback string
     *
     *	@param String action -Which callback action
     *	@param Integer no - Index of media
     *
     * @private	
     */			
	__parseCallBackFunction : function(action,mediaIndex)
	{
		var callBackString=false;
		switch(action){
			case "click":
				callBackString=this.callBackFunction_onClick;
				break;	
			case "dblClick":
				callBackString=this.callBackFunction_onDblClick;
				break;	
			case "mouseOver":
				callBackString = this.callBackFunction_onMouseOver;
				break;
			case "mouseMove":
				callBackString = this.callBackFunction_onMouseMove;
				break;
		}	
		if(callBackString)callBackString = callBackString + '(this.collectionModel.mediaObjects[' + mediaIndex + '])';
		if(!callBackString)return;
		try{
			eval(callBackString);
		}catch(e){
			alert('Error in callback :\n' + callBackString + '\n' + e.message);
		}		
	}	
	// }}}
	,
	// {{{ __parseCallBackFunction()
    /**
     *	Parses callback string
     *
     *	@param String action -Which callback action
     *	@param Integer no - Index of media
     *
     * @private	
     */		
	__initiallyHandleimageSelection : function()
	{
		if(!this.imageSelectionObj)return;
		this.imageSelectionObj.__setMediaCollectionModelReference(this.collectionModel);
		for(var no=0;no<this.divElementImageBoxes.length;no++){
			this.imageSelectionObj.addSelectableElement(this.divElementImageBoxes[no]);
			this.divElementImageBoxes[no].onselectstart = DHTMLSuite.commonObj.cancelEvent;
			var subs = this.divElementImageBoxes[no].getElementsByTagName('*');
			for(var no2=0;no2<subs.length;no2++)subs[no2].onselectstart = DHTMLSuite.commonObj.cancelEvent;
		}
	}
}