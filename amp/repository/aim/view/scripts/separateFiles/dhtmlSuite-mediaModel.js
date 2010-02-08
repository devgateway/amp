if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML context menu class
*
*	Created:						January, 7th, 2007
*	@class Purpose of class:		Data sources for image gallery scripts
*			
*
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Creates a image object
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.mediaModel = function(inputArray)
{
	var id;
	var thumbnailPathSmall;
	var thumbnailPath;
	var largeImagePath;
	var title;
	var caption;	
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	
	if(inputArray)this.addItem(inputArray);
}

DHTMLSuite.mediaModel.prototype = 
{
	// {{{ addItem()
    /**
     *	Display image
     *
     *	@param Array mediaProperties - associative array of properties. keys: id, thumbnailPath, thumnbailPath_small,largeImagePath,title,caption
     *
     * @public	
     */		
	addItem : function(inputArray)
	{
		this.id = inputArray['id'];
		if(inputArray['thumbnailPathSmall'])this.thumbnailPathSmall = inputArray['thumbnailPathSmall'];
		if(inputArray['thumbnailPath'])this.thumbnailPath = inputArray['thumbnailPath'];
		if(inputArray['largeImagePath'])this.largeImagePath = inputArray['largeImagePath'];
		if(inputArray['title'])this.title = inputArray['title'];
		if(inputArray['caption'])this.caption = inputArray['caption'];
	}	
}

DHTMLSuite.mediaCollection = function()
{
	var mediaObjects;		// Array of DHTMLSuite.mediaModel objects
	this.mediaObjects = new Array();
}

DHTMLSuite.mediaCollection.prototype = {
	
	// {{{ addItemsFromMarkup()
    /**
     *	Create image objects from HTML markup(UL,LI) list on the page
     *	The ul element will be hidden by this method.
     *	
     *	@param String elementId - Reference to UL tag on the page
     *
     * @public	
     */		
	addItemsFromMarkup : function(elementId)
	{
		var ul = document.getElementById(elementId);
		var lis = ul.getElementsByTagName('LI');
		for(var no=0;no<lis.length;no++){
			
			var img = lis[no].getElementsByTagName('IMG')[0];
			var index = this.mediaObjects.length;	
			
			var mediaArray = new Array();
			mediaArray.id = lis[no].id;
			if(img){
				mediaArray.thumbnailPath = img.src;	

			}
			mediaArray.title = lis[no].title;
			mediaArray.caption = lis[no].getAttribute('caption');	
			mediaArray.largeImagePath = lis[no].getAttribute('largeImagePath');		
			mediaArray.thumbnailPathSmall = lis[no].getAttribute('thumbnailPathSmall');		
			this.mediaObjects[index] = new DHTMLSuite.mediaModel(mediaArray);
			
		}	
		ul.parentNode.removeChild(ul);
	}
	// }}}
	,
	// {{{ __removeImage()
    /**
     *	Remove an image from the gallery
     *	
     *	@param String idOfMedia - Id of image
     *
     * @private	
     */			
	__removeImage : function(idOfMedia)
	{
		for(var no=0;no<this.mediaObjects.length;no++){
			if(this.mediaObjects[no].id==idOfMedia){
				var retVal = this.mediaObjects[no].id;
				this.mediaObjects.splice(no,1);
				return retVal;
			}
		}	
		return false;	
	}
	// }}}
	,
	// {{{ getMediaById()
    /**
     *	Return reference to media by id
     *	
     *	@param String idOfMedia - Id of image
     *
     * @public	
     */		
	getMediaById : function(idOfMedia)
	{
		for(var no=0;no<this.mediaObjects.length;no++){
			if(this.mediaObjects[no].id==idOfMedia){
				return this.mediaObjects[no];
			}
		}	
		return false;			
		
	}
	
}