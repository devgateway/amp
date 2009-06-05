if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/**
* @constructor
* @class Purpose of class:	Store metadata about panes
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

/************************************************************************************************************
*	Data model for a pane splitter
*
*	Created:						November, 28th, 2006
*	@class Purpose of class:		Data source for the pane splitter
*			
*	Css files used by this script:	
*
*	Demos of this class:			
*
* 	Update log:
*
************************************************************************************************************/


DHTMLSuite.paneSplitterModel = function()
{
	
	var panes;		// Array of paneSplitterPaneModel objects
	
	this.panes = new Array();	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
}

DHTMLSuite.paneSplitterModel.prototype = 
{
	// {{{ addPane()
	/**
	*	Add a pane to the paneSplitterModel
	*
	*	@param paneModelRef paneModelRefect of class DHTMLSuite.paneSplitterPaneModel
	*
	*	@public
	*/		
	addPane : function(paneModelRef)
	{
		this.panes[this.panes.length] = paneModelRef;	
	}
	// }}}
	,
	// {{{ getItems()
	/**
	*	Add a pane to the paneSplitterModel
	*
	*	@return Array of DHTMLSuite.paneSplitterPaneModel objects
	*
	*	@public
	*/		
	getItems : function()
	{
		return this.panes;		
	}
	
	
}


/**
* @constructor
* @class Purpose of class:	Store metadata about a pane
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

/************************************************************************************************************
*	Data model for a pane 
*
*	Created:						November, 28th, 2006
*	@class Purpose of class:		Data source for the pane splitter
*			
*	Css files used by this script:	
*
*	Demos of this class:			
*
* 	Update log:
*
************************************************************************************************************/
DHTMLSuite.paneSplitterPaneModel = function(inputArray)
{
	var id;					// Unique id of pane, in case you want to perform operations on this particular pane.
	var position;			// Position, possible values: "West","East","North","South" and "Center"
	var size;				// Current size of pane(for west and east, the size is equal to width, for south and north, size is equal to height)
	var minSize;			// Minimum size(height or width) of the pane
	var maxSize;			// Maximum size(height or width) of the pane.
	var resizable;			// Boolean - true or false, is the pane resizable
	var visible;			// Boolean - true or false, is the pane visible?
	var scrollbars;			// Boolean - true or false, visibility of scrollbars when content size is bigger than visible area(default = true)
	var contents;			// Array of paneSplitterContentModel objects
	var collapsable;		// Boolean - true or false, is this pane collapsable
	var state;				// State of a pane, possible values, "expanded","collapsed"; (default = expanded)
	var callbackOnCollapse;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnHide;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnShow;		// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnExpand;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnSlideOut;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnSlideIn;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnCloseContent;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnBeforeCloseContent;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnTabSwitch;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
	var callbackOnResize;	// Call back function - called whenever a tab has been resized manually by dragging the "handle".
	
	this.contents = new Array();
	this.scrollbars = true;
	this.resizable = true;
	this.collapsable = true;
	this.state = 'expanded';
	this.visible = true;
	if(inputArray)this.setData(inputArray);
	
}

DHTMLSuite.paneSplitterPaneModel.prototype = 
{
	// {{{ setData()
	/**
	*	One method which makes it possible to set all properties
	*
	*	@param Array inputArray associative array of properties
	*			properties: id,position,title,tabTitle,closable,resizable,size,minSize,maxSize,htmlElementId,contentUrl,collapsable,state(expanded or collapsed),visible
	*						callbackOnCollapse,callbackOnHide,callbackOnShow,callbackOnExpand,callbackOnSlideIn,
	*						callbackOnSlideOut,callbackOnCloseContent,callbackOnBeforeCloseContent,callbackOnTabSwitch,callbackOnResize
	*
	*	@public
	*/		
	setData : function(inputArray)
	{
		if(inputArray["id"])this.id = inputArray["id"];
		if(inputArray["position"])this.position = inputArray["position"];
		if(inputArray["resizable"]===false || inputArray["resizable"]===true)this.resizable = inputArray["resizable"];
		if(inputArray["size"])this.size = inputArray["size"];
		if(inputArray["minSize"])this.minSize = inputArray["minSize"];
		if(inputArray["maxSize"])this.maxSize = inputArray["maxSize"];
		if(inputArray["visible"]===false || inputArray["visible"]===true)this.visible = inputArray["visible"];	
		if(inputArray["collapsable"]===false || inputArray["collapsable"]===true)this.collapsable = inputArray["collapsable"];	
		if(inputArray["scrollbars"]===false || inputArray["scrollbars"]===true)this.scrollbars = inputArray["scrollbars"];	
		if(inputArray["state"])this.state = inputArray["state"];
		if(inputArray["callbackOnCollapse"])this.callbackOnCollapse = inputArray["callbackOnCollapse"];
		if(inputArray["callbackOnHide"])this.callbackOnHide = inputArray["callbackOnHide"];
		if(inputArray["callbackOnShow"])this.callbackOnShow = inputArray["callbackOnShow"];
		if(inputArray["callbackOnExpand"])this.callbackOnExpand = inputArray["callbackOnExpand"];		
		if(inputArray["callbackOnSlideIn"])this.callbackOnSlideIn = inputArray["callbackOnSlideIn"];		
		if(inputArray["callbackOnSlideOut"])this.callbackOnSlideOut = inputArray["callbackOnSlideOut"];		
		if(inputArray["callbackOnCloseContent"])this.callbackOnCloseContent = inputArray["callbackOnCloseContent"];		
		if(inputArray["callbackOnBeforeCloseContent"])this.callbackOnBeforeCloseContent = inputArray["callbackOnBeforeCloseContent"];		
		if(inputArray["callbackOnTabSwitch"])this.callbackOnTabSwitch = inputArray["callbackOnTabSwitch"];		
		if(inputArray["callbackOnResize"])this.callbackOnResize = inputArray["callbackOnResize"];		
	}
	// }}}
	,
	// {{{ setSize()
	/**
	*	Set size of pane
	*
	*	@param Integer newSizeInPixels = Size of new pane ( for "west" and "east", it would be width, for "north" and "south", it's height.
	*
	*	@public
	*/		
	setSize : function(newSizeInPixels)
	{
		this.size = newSizeInPixels;
	}
	// }}}
	,
	// {{{ addContent()
	/**
	*	Add content to a pane.
	*	This method should only be called before the paneSplitter has been initialized, i.e. before it has been displayed on the screen
	*	After it has been displayed, use the addContent method of the DHTMLSuite.paneSplitter class to add content to panes.
	*
	*	@param Object paneSplitterContentObj = An object of class DHTMLSuite.paneSplitterContentModel
	*	@return Boolean Success = true if content were added, false otherwise, i.e. if conten allready exists
	*	@private
	*/		
	addContent : function(paneSplitterContentObj)
	{
		// Check if content with this id allready exists. if it does, escape from the function.
		for(var no=0;no<this.contents.length;no++){
			if(this.contents[no].id==paneSplitterContentObj.id)return false;	
		}
		this.contents[this.contents.length] = paneSplitterContentObj;	// Add content to the array of content objects.
		return true;
	}
	// }}}
	,
	// {{{ getContents()
	/**
	*	Return an array of content objects
	*
	*	@return Array of DHTMLSuite.paneSplitterContentModel objects
	*
	*	@public
	*/	
	getContents : function()
	{
		return this.contents;
	}
	// }}}
	,
	// {{{ getCountContent()
	/**
	*	Return number of content objects inside this paneModel
	*
	*	@return Integer Number of DHTMLSuite.paneSplitterContentModel objects
	*
	*	@public
	*/	
	getCountContent : function()
	{
		return this.contents.length;
	}
	// }}}
	,
	// {{{ getPosition()
	/**
	*	Return position of this pane
	*
	*	@return String Position of pane ( lowercase, "north","west","east","south" or "center" )
	*
	*	@public
	*/		
	getPosition : function()
	{
		return this.position.toLowerCase();
	}
	
	,
	// {{{ __setState()
	/**
	*	Update the state attribute
	*
	*	@param String state = state of pane ( "expanded" or "collapsed" )
	*
	*	@private
	*/		
	__setState : function(state)
	{
		this.state = state;
	}
	// }}
	,
	// {{{ __getState()
	/**
	*	Update the state attribute
	*
	*	@return String state - state of pane
	*	@private
	*/		
	__getState : function(state)
	{
		return this.state;
	}
	,
	// {{{ __deleteContent()
	/**
	*	Delete content from a pane.
	*
	*	@param Integer indexOfContentObjectToDelete - Content index
	*	@return Integer - Index of new active pane.
	*	@private
	*/		
	__deleteContent : function(indexOfContentObjectToDelete)
	{
		try{
			this.contents.splice(indexOfContentObjectToDelete,1);
		}catch(e)
		{
		}
		
				
		var retVal = indexOfContentObjectToDelete;
		if(this.contents.length>(indexOfContentObjectToDelete-1))retVal--;
		if(retVal<0 && this.contents.length==0)return false;
		if(retVal<0)retVal=0;		
		return retVal;
	}
	// }}}
	,
	// {{{ __getIndexById()
	/**
	*	Return index of content with a specific content id
	*
	*	@param String id - id of content
	*	@return Integer index - Index of content
	*	@private
	*/		
	__getIndexById : function(id)
	{
		for(var no=0;no<this.contents.length;no++){
			if(this.contents[no].id==id)return no;
		}
		return false;
		
	}
	// }}}
	,
	// {{{ __setVisible()
	/**
	*	Set pane visibility
	*
	*	@param Boolean visible - true = visible, false = hidden
	*	
	*	@private
	*/		
	__setVisible : function(visible)
	{
		this.visible = visible;
	}
}

/**
* @constructor
* @class Purpose of class:	Store metadata about the content of a pane
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

/************************************************************************************************************
*	Data source for the content of a pane splitter pane
*
*	Created:						November, 28th, 2006
*	@class Purpose of class:		Data source for the pane splitter pane
*			
*	Css files used by this script:	
*
*	Demos of this class:			
*
* 	Update log:
*
************************************************************************************************************/




DHTMLSuite.paneSplitterContentModel = function(inputArray)
{
	var id;					// Unique id of pane, in case you want to perform operations on this particular pane.
	var htmlElementId;		// Id of element on the page - if present, the content of this pane will be set to the content of this element
	var title;				// Title of pane
	var tabTitle;			// If more than one pane is present at this position, what's the tab title of this one.	
	var closable;			// Boolean - true or false, should it be possible to close this pane
	var contentUrl;			// Url to content - used in case you want the script to fetch content from the server. the path is relative to your html page.
	this.closable = true;	// Default value
	var refreshAfterSeconds;
	var displayRefreshButton;
	
	this.displayRefreshButton = false;
	this.refreshAfterSeconds = 0;
	
	if(inputArray)this.setData(inputArray);	// Input array present, call the setData method.
}

DHTMLSuite.paneSplitterContentModel.prototype = 
{
	// {{{ setData()
	/**
	*	One method which makes it possible to set all properties
	*
	*	@param Array associative array of properties
	*			properties: id,position,title,tabTitle,closable,htmlElementId,contentUrl,refreshAfterSeconds
	*
	*	@public
	*/		
	setData : function(inputArray)
	{
		if(inputArray["id"])this.id = inputArray["id"]; else this.id = inputArray['htmlElementId'];
		if(inputArray["closable"]===false || inputArray["closable"]===true)this.closable = inputArray["closable"];
		if(inputArray["displayRefreshButton"]===false || inputArray["displayRefreshButton"]===true)this.displayRefreshButton = inputArray["displayRefreshButton"];
		if(inputArray["title"])this.title = inputArray["title"];
		if(inputArray["tabTitle"])this.tabTitle = inputArray["tabTitle"];
		if(inputArray["contentUrl"])this.contentUrl = inputArray["contentUrl"];
		if(inputArray["htmlElementId"])this.htmlElementId = inputArray["htmlElementId"];	
		if(inputArray["refreshAfterSeconds"])this.refreshAfterSeconds = inputArray["refreshAfterSeconds"];	
	}
	// }}}
	,
	// {{{ __setTitle()
	/**
	* 	 Set new title
	*
	*	@param String newTitle - New tab title
	*
	*	@private
	*/		
	__setTitle : function(newTitle)
	{
		this.title = newTitle;
	}
	// }}}
	,
	// {{{ __setTabTitle()
	/**
	* 	 Set new tab title
	*
	*	@param String newTabTitle - New tab title
	*
	*	@private
	*/		
	__setTabTitle : function(newTabTitle)
	{
		this.tabTitle = newTabTitle;
	}
	// }}}
	,
	// {{{ __setIdOfContentElement()
	/**
	* 	 Specify contentId
	*
	*	@param String htmlElementId - Id of content ( HTML Element on the page )
	*
	*	@private
	*/		
	__setIdOfContentElement : function(htmlElementId)
	{
		this.htmlElementId = htmlElementId;
	}
	// }}}
	,
	// {{{ __setRefreshAfterSeconds()
	/**
	* 	 Set reload content value ( seconds )
	*
	*	@param Integer refreshAfterSeconds - Refresh rate in seconds
	*
	*	@private
	*/		
	__setRefreshAfterSeconds : function(refreshAfterSeconds)
	{
		this.refreshAfterSeconds = refreshAfterSeconds;
	}
	// }}}
	,
	// {{{ __setContentUrl()
	/**
	* 	 Specifies external url for content
	*
	*	@param String contentUrl - Url of content
	*
	*	@private
	*/		
	__setContentUrl : function(contentUrl)
	{
		this.contentUrl = contentUrl;
	}
	/// }}}
	,
	// {{{ __getClosable()
	/**
	*	Return the closable attribute
	*	@private
	*/		
	__getClosable : function()
	{
		return this.closable;
	}
}