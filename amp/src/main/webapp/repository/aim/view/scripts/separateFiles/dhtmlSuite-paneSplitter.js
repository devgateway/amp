if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML pane splitter pane
*
*	Created:						November, 28th, 2006
*	@class Purpose of class:		Creates a pane for the pane splitter ( This is a private class )
*			
*	Css files used by this script:	pane-splitter.css
*
*	Demos of this class:			demo-pane-splitter.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Creates the content for a pane in the pane splitter widget( This is a private class )
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.paneSplitterPane = function(parentRef)
{
	var divElement;		// Reference to a div element for the content
	var divElementCollapsed;	// Reference to the div element for the content ( collapsed state )
	var divElementCollapsedInner;	// Reference to the div element for the content ( collapsed state )
	var contentDiv;		// Div for the content
	var headerDiv;		// Reference to the header div
	var titleSpan;		// Reference to the <span> tag for the title
	var paneModel;		// An array of paneSplitterPaneView objects
	var resizeDiv;		// Div for the resize handle
	var tabDiv;			// Div for the tabs
	var divTransparentForResize;	// This transparent div is used to cover iframes when resize is in progress.
	var parentRef;		// Reference to paneSplitter object
	
	var divClose;		// Reference to close button
	var divCollapse;	// Reference to collapse button
	var divExpand;		// Reference to expand button
	var divRefresh;		// Reference to refresh button
	
	var slideIsInProgress;		// Internal variable used by the script to determine if slide is in progress or not
	var zIndexCounter;			// Incremental value used when setting z-indexes.
	var reloadIntervalHandlers;	// Array of setInterval objects, one for each content of this pane
	
	var contentScrollTopPositions;	// Array of contents scroll top positions in pixels.
	
	this.contents = new Array();
	this.reloadIntervalHandlers = new Array();
	this.contentScrollTopPositions = new Array();
	
	this.parentRef = parentRef;
	var activeContentIndex;	// Index of active content(default = 0)
	this.activeContentIndex = false;
	this.slideIsInProgress = false;
	var objectIndex;			// Index of this object in the variableStorage array
	this.zIndexCounter = 1;
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
	
		
	
}
DHTMLSuite.paneSplitterPane.prototype =
{
	
// {{{ addDataSource()
	/**
	*	Add a data source to the pane
	*
	*	@param paneModelRef - Object of class DHTMLSuite.paneSplitterpaneModelRef
	*	@public
	*/		
	addDataSource : function(paneModelRef)
	{
		this.paneModel = paneModelRef;
	}
	// }}}
	,
	// {{{ addContent()
	/**
	* 	 Add a content model to the pane. 
	*
	*	@param DHTMLSuite.paneSplitterpaneContentModelObject paneContentModelObject - Object of class DHTMLSuite.paneSplitterpaneContentModelObject
	*	@param String jsCodeToExecuteWhenComplete - JS code to execute/evaluate when content has been successfully loaded.
	*	@return Boolean Success - true if content were added, false otherwise (i.e. content already exists)
	*
	*	@public
	*/		 
	addContent : function(paneContentModelObject,jsCodeToExecuteWhenComplete)
	{
		var retValue = this.paneModel.addContent(paneContentModelObject);
		
		if(!retValue)return false;	// Content already exists - return from this method.
		this.__addOneContentDiv(paneContentModelObject,jsCodeToExecuteWhenComplete);
		this.__updateTabContent();
		this.__updateView();
		if(this.paneModel.getCountContent()==1)this.showContent(paneContentModelObject.id);
		return retValue
	}
	// }}}	
	,	
	// {{{ showContent()
	/**
	* 	Display content - the content with this id will be activated.(if content id doesn't exists, nothing is done)
	*
	*	@param String idOfContentObject - Id of the content to show
	*
	*	@public
	*/		
	showContent : function(idOfContentObject)
	{
		for(var no=0;no<this.paneModel.contents.length;no++){
			if(this.paneModel.contents[no].id==idOfContentObject){
				this.__updatePaneView(no);				
				return;
			}
		}	
	}
	// }}}
	,
	// {{{ loadContent()
	/**
	* 	loads content into a pane
	*
	*	@param String idOfContentObject - Id of the content object - where new content should be appended
	*	@param String url - url to content
	*	@param Integer refreshAfterSeconds		- Reload url after number of seconds. 0 = no refresh ( also default)
	*	@param internalCall Boolean	- Always false ( true only if this method is called by the script it's self )
	*	@param String onCompleteJsCode - Js code to execute when content has been successfully loaded.
	*
	*	@public
	*/		
	loadContent : function(idOfContentObject,url,refreshAfterSeconds,internalCall,onCompleteJsCode)
	{
		if(!url)return;
		for(var no=0;no<this.paneModel.contents.length;no++){
			if(this.paneModel.contents[no].id==idOfContentObject){
				if(internalCall && !this.paneModel.contents[no].refreshAfterSeconds)return;	// Refresh rate has been cleared - no reload.				
				var ajaxWaitMessage = this.parentRef.waitMessage;	// Please wait message
				//if(this.paneModel.contents[no].contentUrl==url)ajaxWaitMessage='';	// If content is being reloaded, don't display any wait message
				this.paneModel.contents[no].__setContentUrl(url);
				if(refreshAfterSeconds && !internalCall){
					this.paneModel.contents[no].__setRefreshAfterSeconds(refreshAfterSeconds);					
				}
				if(refreshAfterSeconds)this.__handleContentReload(idOfContentObject,refreshAfterSeconds);
				try{
					var dynContent = new DHTMLSuite.dynamicContent();
				}catch(e){
					alert('You need to include dhtmlSuite-dynamicContent.js');
				}
				dynContent.setWaitMessage(ajaxWaitMessage);
				dynContent.loadContent(this.paneModel.contents[no].htmlElementId,url,onCompleteJsCode);		
				dynContent = false;	
				return;
			}
		}	
	}
	// }}}
	,
	// {{{ isUrlLoadedInPane()
	/**
	* 	Check if url is allready loaded into a pane.
	*
	*	@param String idOfContentObject - Id of content
	*	@param String url - url to check
	*
	*	@public
	*/		
	isUrlLoadedInPane : function(idOfContentObject,url)
	{
		var contentIndex = this.paneModel.__getIndexById(idOfContentObject);
		if(contentIndex!==false){
			if(this.paneModel.contents[contentIndex].contentUrl==url)return true;
		}
		return false;
		
	}
	// }}}
	,
	// {{{ reloadContent()
	/**
	* 	Reloads content for a pane ( AJAX )
	*
	*	@param String idOfContentObject - Id of the content object - where new content should be appended
	*
	*	@public
	*/		
	reloadContent : function(idOfContentObject)
	{
		var contentIndex = this.paneModel.__getIndexById(idOfContentObject);
		if(contentIndex!==false){
			this.loadContent(idOfContentObject,this.paneModel.contents[contentIndex].contentUrl);	
		}		
	}
	// }}}
	,
	// {{{ setRefreshAfterSeconds()
	/**
	* 	Reloads content into a pane - sets a timeout for a new call to loadContent
	*
	*	@param String idOfContentObject - Id of the content object - id of content
	*	@param Integer refreshAfterSeconds - When to reload content, 0 = no reload of content.
	*
	*	@public
	*/	
	setRefreshAfterSeconds : function(idOfContentObject,refreshAfterSeconds)
	{
		for(var no=0;no<this.paneModel.contents.length;no++){
			if(this.paneModel.contents[no].id==idOfContentObject){
				if(!this.paneModel.contents[no].refreshAfterSeconds){
					this.loadContent(idOfContentObject,this.paneModel.contents[no].contentUrl,refreshAfterSeconds);	
				}
				this.paneModel.contents[no].__setRefreshAfterSeconds(refreshAfterSeconds);	
				this.__handleContentReload(idOfContentObject);			
			}
		}
	}
	// }}}
	,
	// {{{ setContentTitle()
	/**
	* 	New tab title of content - i.e. the heading
	*
	*	@param String idOfContent - Id of content object
	*	@param String newTitle - New tab title
	*
	*	@public
	*/		
	setContentTitle : function(idOfContent, newTitle)
	{
		var contentModelIndex = this.paneModel.__getIndexById(idOfContent);	// Get a reference to the content object
		if(contentModelIndex!==false){
			var contentModelObj = this.paneModel.contents[contentModelIndex];
			contentModelObj.__setTitle(newTitle);
			this.__updateHeaderBar(this.activeContentIndex);
		}
	}
	// }}}
	,
	// {{{ setContentTabTitle()
	/**
	* 	New tab title for a specific tab(the clickable tab)
	*
	*	@param String idOfContent - Id of content object
	*	@param String newTitle - New tab title
	*
	*	@public
	*/		
	setContentTabTitle : function(idOfContent, newTitle)
	{
		var contentModelIndex = this.paneModel.__getIndexById(idOfContent);	// Get a reference to the content object
		if(contentModelIndex!==false){
			var contentModelObj = this.paneModel.contents[contentModelIndex];
			contentModelObj.__setTabTitle(newTitle);
			this.__updateTabContent();
		}
	}
	// }}}
	,
	// {{{ hidePane()
	/**
	* 	Hides the pane
	*
	*
	*	@public
	*/		
	hidePane : function()
	{
		this.paneModel.__setVisible(false);	// Update the data source property
		this.divElement.style.display='none';
		this.__executeCallBack("hide",this.paneModel.contents[this.activeContentIndex]);		
	}	
	// }}}
	,
	// {{{ showPane()
	/**
	* 	Make a pane visible
	*
	*
	*	@public
	*/		
	showPane : function()
	{
		this.paneModel.__setVisible(true);	
		this.divElement.style.display='block';	
		this.__executeCallBack("show",this.paneModel.contents[this.activeContentIndex]);
	}	
	// }}}
	,
	// {{{ collapse()
	/**
	* 	Collapses a pane
	*
	*
	*	@public
	*/		
	collapse : function()
	{
		this.__collapse();
	}
	,
	// {{{ expand()
	/**
	* 	Expands a pane
	*
	*
	*	@public
	*/		
	expand : function()
	{
		this.__expand();		
	}
	// }}}
	,
	// {{{ getIdOfCurrentlyDisplayedContent()
	/**
	* 	Returns id of the content currently being displayed - active tab.
	*
	*	@return String id of currently displayed content (active tab).
	*
	*	@private
	*/		
	getIdOfCurrentlyDisplayedContent : function()
	{
		return this.paneModel.contents[this.activeContentIndex].id;	
	}
	// }}}
	,
	// {{{ __getSizeOfPaneInPixels()
	/**
	*	Returns pane width in pixels
	*	@return Array - associative array with the keys "width" and "height"
	*
	*	@private
	*/		
	__getSizeOfPaneInPixels : function()
	{
		var retArray = new Array();
		retArray.width = this.divElement.offsetWidth;
		retArray.height = this.divElement.offsetHeight;
		return retArray;
	}
	// }}}
	,
	// {{{ __reloadDisplayedContent()
	/**
	*	Reloads the displayed content if it got content url.
	*
	*	@private
	*/		
	__reloadDisplayedContent : function()
	{
		this.reloadContent(this.paneModel.contents[this.activeContentIndex].id);		
	}
	,	
	// {{{ __getReferenceToMainDivElement()
	/**
	* 	Returns a reference to the main div element for this pane
	*
	*	@param String divElement - Reference to pane div element(top div of pane)
	*
	*	@private
	*/	
	__getReferenceToMainDivElement : function()
	{
		return this.divElement;		
	}
	// }}}	
	,
	// {{{ __executeResizeCallBack()
	/**
	* 	Execute a resize pane call back - this method is called from the pane splitter
	*
	*	@private
	*/		
	__executeResizeCallBack : function()
	{
		this.__executeCallBack('resize');
	}
	,	
	// {{{ __executeCallBack()
	/**
	* 	Execute a call back function
	*
	*	@parem whichCallBackAction - which call back - event.
	*
	*	@private
	*/	
	__executeCallBack : function(whichCallBackAction,contentObj)
	{
		
		var callbackString = false;
		switch(whichCallBackAction){	/* Which call back string */			
			case "show":
				if(!this.paneModel.callbackOnShow)return;
				callbackString = this.paneModel.callbackOnShow;
				break;
			case "collapse":
				if(!this.paneModel.callbackOnCollapse)return;
				callbackString = this.paneModel.callbackOnCollapse;
				break;	
			case "expand":
				if(!this.paneModel.callbackOnExpand)return;
				callbackString = this.paneModel.callbackOnExpand;
				break;	
			case "hide":
				if(!this.paneModel.callbackOnHide)return;
				callbackString = this.paneModel.callbackOnHide;
				break;	
			case "slideIn":
				if(!this.paneModel.callbackOnSlideIn)return;
				callbackString = this.paneModel.callbackOnSlideIn;
				break;	
			case "slideOut":				
				if(!this.paneModel.callbackOnSlideOut)return;
				callbackString = this.paneModel.callbackOnSlideOut;				
				break;	
			case "closeContent":
				if(!this.paneModel.callbackOnCloseContent)return;
				callbackString = this.paneModel.callbackOnCloseContent;				
				break;
			case "beforeCloseContent": 
				if(!this.paneModel.callbackOnBeforeCloseContent)return true;
				callbackString = this.paneModel.callbackOnBeforeCloseContent;	
				break;
			case "tabSwitch":
				if(!this.paneModel.callbackOnTabSwitch)return;
				callbackString = this.paneModel.callbackOnTabSwitch;				
				break;	
			case "resize":
				if(!this.paneModel.callbackOnResize)return;
				callbackString = this.paneModel.callbackOnResize;
				break;			
		}		
		if(!callbackString)return;
		if(!contentObj)contentObj=false;
		callbackString = this.__getCallBackString(callbackString,whichCallBackAction,contentObj);
		return this.__executeCallBackString(callbackString,contentObj);
	}
	// }}}
	,
	// {{{ __getCallBackString()
	/**
	* 	Parse a call back string. If parantheses are present, return it as it is, otherwise return  the name of the function and the paneModel as argument to that function
	*
	*	@param String callbackString - Call back string to parse
	*	@param String whichCallBackAction - Which callback action
	*	@param Object contentObj - Reference to pane content object(model)
	*
	*	@private
	*/		
	__getCallBackString : function(callbackString,whichCallBackAction,contentObj)
	{
		if(callbackString.indexOf('(')>=0)return callbackString;	
		if(contentObj){	
			callbackString = callbackString + '(this.paneModel,"' + whichCallBackAction + '",contentObj)';
		}else{
			callbackString = callbackString + '(this.paneModel,"' + whichCallBackAction + '")';
		}
		callbackString = callbackString;
		return callbackString;	
	}
	// }}}
	,
	// {{{ __executeCallBackString()
	/**
	* 	Reloads content into a pane - sets a timeout for a new call to loadContent
	*
	*	@param String callbackString - Call back string to execute
	*	@param Object contentObj - Reference to pane content object(model)
	*
	*	@private
	*/		
	__executeCallBackString : function(callbackString,contentObj)
	{
		try{
			return eval(callbackString);
		}catch(e){
			alert('Could not execute specified call back function:\n' + callbackString + '\n\nError:\n' + e.name + '\n' + e.message + '\n' + '\nMake sure that there aren\'t any errors in your function.\nAlso remember that contentObj would not be present when you click close on the last tab\n(In case a close tab event triggered this callback function)');
		}			
	}	
	,
	// {{{ __handleContentReload()
	/**
	* 	Reloads content into a pane - sets a timeout for a new call to loadContent
	*
	*	@param String id - Id of the content object - id of content
	*
	*	@private
	*/			
	__handleContentReload : function(id)
	{
		var ind = this.objectIndex;		
		var contentIndex = this.paneModel.__getIndexById(id);
		if(contentIndex!==false){
			var contentRef = this.paneModel.contents[contentIndex];
			if(contentRef.refreshAfterSeconds){
				if(this.reloadIntervalHandlers[id])clearInterval(this.reloadIntervalHandlers[id]);
				this.reloadIntervalHandlers[id] = setInterval('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].loadContent("' + id + '","' + contentRef.contentUrl + '",' + contentRef.refreshAfterSeconds + ',true)',(contentRef.refreshAfterSeconds*1000));
			}else{
				if(this.reloadIntervalHandlers[id])clearInterval(this.reloadIntervalHandlers[id]);
			}
		}		
	}
	// }}}
	,
	// {{{ __createPane()
	/**
	*	This method creates the div for a pane
	*
	*
	*	@private
	*/		
	__createPane : function()
	{
		this.divElement = document.createElement('DIV');	// Create the div for a pane.
		this.divElement.style.position = 'absolute';
		this.divElement.className = 'DHTMLSuite_pane';
		this.divElement.id = 'DHTMLSuite_pane_' + this.paneModel.getPosition();
		document.body.appendChild(this.divElement);

		
		this.__createHeaderBar();	// Create the header
		this.__createContentPane();	// Create content pane.
		this.__createTabBar();	// Create content pane.
		this.__createCollapsedPane();	// Create div element ( collapsed state)
		this.__createTransparentDivForResize();	// Create transparent div for the resize;
		this.__updateView();	// Update the view
		
		this.__addContentDivs();
		this.__setSize();
	}
	// }}}
	,
	// {{{ __createTransparentDivForResize()
	/**
	*	Create div element used when content is being resized
	*
	*
	*	@private
	*/		
	__createTransparentDivForResize : function()
	{
		this.divTransparentForResize = document.createElement('DIV');
		var ref = this.divTransparentForResize;
		ref.style.opacity = '0';
		ref.style.display='none';
		ref.style.filter = 'alpha(opacity=0)';
		ref.style.position = 'absolute';
		ref.style.left = '0px';
		ref.style.top = this.headerDiv.offsetHeight + 'px';
		ref.style.height = '90%';
		ref.style.width = '100%';
		ref.style.backgroundColor='#FFF';
		ref.style.zIndex = '1000';
		this.divElement.appendChild(ref);
		
	}
	// }}}
	,
	// {{{ __createCollapsedPane()
	/**
	*	Creates the div element - collapsed state
	*
	*
	*	@private
	*/		
	__createCollapsedPane : function()
	{
		var ind = this.objectIndex;
		var pos = this.paneModel.getPosition();
		var buttonSuffix = 'Vertical';	// Suffix to the class names for the collapse and expand buttons
		if(pos=='west' || pos=='east')buttonSuffix = 'Horizontal';
		if(pos=='center')buttonSuffix = '';
				
		this.divElementCollapsed = document.createElement('DIV');	
		var obj = this.divElementCollapsed;

		obj.className = 'DHTMLSuite_pane_collapsed_' + pos;
		obj.style.visibility='hidden';
		obj.style.position = 'absolute';
		
		this.divElementCollapsedInner = document.createElement('DIV');
		this.divElementCollapsedInner.className= 'DHTMLSuite_pane_collapsedInner';
		this.divElementCollapsedInner.onmouseover = this.__mouseoverHeaderButton;
		this.divElementCollapsedInner.onmouseout = this.__mouseoutHeaderButton;
		this.divElementCollapsedInner.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__slidePane(e); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementCollapsedInner);	
		
		obj.appendChild(this.divElementCollapsedInner);
			
		var buttonDiv = document.createElement('DIV');
		buttonDiv.className='buttonDiv';
		
		this.divElementCollapsedInner.appendChild(buttonDiv);
		// Creating expand button
		this.divExpand = document.createElement('DIV');
		if(pos=='south' || pos=='east')
			this.divExpand.className='collapseButton' + buttonSuffix;			
		else
			this.divExpand.className='expandButton' + buttonSuffix;	
		this.divExpand.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__expand(); } ;
		this.divExpand.onmouseover = this.__mouseoverHeaderButton;
		this.divExpand.onmouseout = this.__mouseoutHeaderButton;		
		DHTMLSuite.commonObj.__addEventElement(this.divExpand);		
		buttonDiv.appendChild(this.divExpand);
			
		document.body.appendChild(obj);	

	}
	// }}}	
	,
	// {{{ __autoSlideInPane()
	/**
	*	Automatically slide in a pane when click outside of the pane. This will happen if the pane is currently in "slide out" mode.
	*
	*
	*	@private
	*/	
	__autoSlideInPane : function(e)
	{
		if(document.all)e = event;
		var state = this.paneModel.__getState();	// Get state of pane
		if(state=='collapsed' && this.divElement.style.visibility!='hidden'){	// Element is collapsed but showing(i.e. temporary expanded)
			if(!DHTMLSuite.commonObj.isObjectClicked(this.divElement,e))this.__slidePane(e,true);	// Slide in pane if element clicked is not the expanded pane			
		}
	}
	// }}}
	,	
	// {{{ __slidePane()
	/**
	*	The purpose of this method is to slide out a pane, but the state of the pane is still collapsed
	*
	*	@param Event e - Reference to event object
	*	@param Boolean forceSlide - force the slide action no matter which element the user clicked on. 
	*
	*	@private
	*/		
	__slidePane : function(e,forceSlide)
	{
		if(this.slideIsInProgress)return;
		this.zIndexCounter++;
		if(document.all)e = event;	// IE
		var src = DHTMLSuite.commonObj.getSrcElement(e);	// Get a reference to the element triggering the event
		if(src.className.indexOf('collapsed')<0 && !forceSlide)return;	// If a button on the collapsed pane triggered the event->Return from the method without doing anything.
		
		this.slideIsInProgress = true;
		var state = this.paneModel.__getState();	// Get state of pane.
		
		var hideWhenComplete = true;
		if(this.divElement.style.visibility=='hidden'){	// The pane is currently not visible, i.e. not slided out.
			this.__executeCallBack('slideOut',this.paneModel.contents[this.activeContentIndex]);
			this.__setSlideInitPosition();
			this.divElement.style.visibility='visible';
			this.divElement.style.zIndex = 16000 + this.zIndexCounter;
			this.divElementCollapsed.style.zIndex = 16000 + this.zIndexCounter;
		
			var slideTo = this.__getSlideToCoordinates(true);	// Get coordinate, where to slide to
			hideWhenComplete = false;
			var slideSpeed = this.__getSlideSpeed(true); 
			
		}else{
			this.__executeCallBack('slideIn',this.paneModel.contents[this.activeContentIndex]);
			var slideTo = this.__getSlideToCoordinates(false);	// Get coordinate, where to slide to
			var slideSpeed = this.__getSlideSpeed(false);
		}		
		
		this.__processSlideByPixels(slideTo,slideSpeed*this.parentRef.slideSpeed,this.__getCurrentCoordinateInPixels(),hideWhenComplete);
		
	}
	// }}}
	,
	// {{{ __setSlideInitPosition()
	/**
	*	Set position of pane before slide.
	*
	*
	*	@private
	*/		
	__setSlideInitPosition : function()
	{
		var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
		var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
		var pos = this.paneModel.getPosition();
		switch(pos){
			case "west":
				this.divElement.style.left = (0 - this.paneModel.size)+ 'px';				
				break;	
			case "east":
				this.divElement.style.left = browserWidth + 'px';
				break;
			case "north":
				this.divElement.style.top = (0 - this.paneModel.size)+ 'px';	
				break;
			case "south":
				this.divElement.style.top = browserHeight + 'px';
				break;
		}		
	}
	// }}}
	,
	// {{{ __getCurrentCoordinateInPixels()
	/**
	*	Return pixel coordinates for this pane. For left and east, it would be the left position. For top and south, it would be the top position.
	*
	*	@return Integer currentCoordinate	= Current coordinate for a pane ( top or left)
	*
	*	@private
	*/		
	__getCurrentCoordinateInPixels : function()
	{
		var pos = this.paneModel.getPosition();
		switch(pos){
			case "west": return this.divElement.style.left.replace('px','')/1;	
			case "east": return this.divElement.style.left.replace('px','')/1;	
			case "south": return this.divElement.style.top.replace('px','')/1;	
			case "north": return this.divElement.style.top.replace('px','')/1;				
		}		
	}
	// }}}
	,
	// {{{ __getSlideSpeed()
	/**
	*	Return pixel steps for the slide.
	*
	*	@param Boolean slideOut	= true if the element should slide out, false if it should slide back, i.e. be hidden.
	*
	*	@private
	*/	
	__getSlideSpeed : function(slideOut)
	{
		var pos = this.paneModel.getPosition();
		switch(pos){
			case "west": 
			case "north":
				if(slideOut)return 1;else return -1;
				break;
			case "south":
			case "east":
				if(slideOut)return -1;else return 1;	
		}				
	}
	
	// }}} 
	,
	// {{{ __processSlideByPixels()
	/**
	*	Slides in our out a pane - this method creates that animation
	*
	*	@param Integer slideTo	- coordinate where to slide to(top or left)	
	*	@param Integer slidePixels	- pixels to slide in each iteration of this method
	*	@param Integer currentPos	- current slide position
	*	@param Boolean hideWhenComplete	- Hide pane when completed ?
	*
	*	@private
	*/	
	__processSlideByPixels : function(slideTo,slidePixels,currentPos,hideWhenComplete)
	{
		var pos = this.paneModel.getPosition();
		currentPos = currentPos + slidePixels;
		var repeatSlide = true;	// Repeat one more time ?
		if(slidePixels>0 && currentPos>slideTo){
			currentPos = slideTo;
			repeatSlide = false;
		}
		if(slidePixels<0 && currentPos<slideTo){
			currentPos = slideTo;
			repeatSlide = false;
		}
		
		switch(pos){
			case "west":
			case "east":
				this.divElement.style.left = currentPos + 'px';
				break;
			case "north":
			case "south":
				this.divElement.style.top = currentPos + 'px';	
		}
		
		if(repeatSlide){
			var ind = this.objectIndex;			
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__processSlideByPixels(' + slideTo + ',' + slidePixels + ',' + currentPos + ',' + hideWhenComplete + ')',10);
		}else{
			if(hideWhenComplete){
				this.divElement.style.visibility='hidden';
				this.divElement.style.zIndex = 11000;
				this.divElementCollapsed.style.zIndex = 12000;
			}				
			this.slideIsInProgress = false;
		}		
	}
	// }}}
	,
	// {{{ __getSlideToCoordinates()
	/**
	*	Return target coordinate for the slide, i.e. where to slide to
	*
	*	@param Boolean slideOut	= true if the element should slide out, false if it should slide back, i.e. be hidden.
	*
	*	@private
	*/
	__getSlideToCoordinates : function(slideOut)
	{
		var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
		var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();		
		var pos = this.paneModel.getPosition();
			
		
		switch(pos){
			case "west":	
				if(slideOut)
					return this.parentRef.paneSizeCollapsed;	// End position is
				else
					return (0 - this.paneModel.size);
			case "east":
				if(slideOut)
					return browserWidth - this.parentRef.paneSizeCollapsed - this.paneModel.size;
				else
					return browserWidth;
			case "north":
				if(slideOut)
					return this.parentRef.paneSizeCollapsed;	// End position is
				else
					return (0 - this.paneModel.size);
			case "south":
				if(slideOut)	
					return browserHeight - this.parentRef.paneSizeCollapsed  - this.paneModel.size;
				else
					return browserHeight;
		}
		
	}
	
	// }}}	
	,
	// {{{ __updateCollapsedSize()
	/**
	*	Automatically figure out the size of the pane when it's collapsed(the height or width of the small bar)
	*
	*
	*	@private
	*/		
	__updateCollapsedSize : function()
	{
		var pos = this.paneModel.getPosition();
		var size;
		if(pos=='west' || pos=='east')size = this.divElementCollapsed.offsetWidth;
		if(pos=='north' || pos=='south')size = this.divElementCollapsed.offsetHeight;
		if(size)this.parentRef.__setPaneSizeCollapsed(size);		
	}
	// }}}
	,
	// {{{ __createHeaderBar()
	/**
	*	Creates the header bar for a pane
	*
	*
	*	@private
	*/	
	__createHeaderBar : function()
	{
		var ind = this.objectIndex;	// Making it into a primitive variable
		var pos = this.paneModel.getPosition();	// Get position of this pane
		var buttonSuffix = 'Vertical';	// Suffix to the class names for the collapse and expand buttons
		if(pos=='west' || pos=='east')buttonSuffix = 'Horizontal';
		if(pos=='center')buttonSuffix = '';
		
		this.headerDiv = document.createElement('DIV');
		this.headerDiv.className = 'DHTMLSuite_paneHeader';
		this.headerDiv.style.position = 'relative';
		this.divElement.appendChild(this.headerDiv);
		
		this.titleSpan = document.createElement('SPAN');
		this.titleSpan.className = 'paneTitle';
		this.headerDiv.appendChild(this.titleSpan);
		
		var buttonDiv = document.createElement('DIV');
		buttonDiv.style.position = 'absolute';
		buttonDiv.style.right = '0px';
		buttonDiv.style.top = '0px';
		buttonDiv.className = 'DHTMLSuite_paneHeader_buttonDiv';
		this.headerDiv.appendChild(buttonDiv);
	
		// Creating close button
		this.divClose = document.createElement('DIV');
		this.divClose.className = 'closeButton';
		this.divClose.onmouseover = this.__mouseoverHeaderButton;
		this.divClose.onmouseout = this.__mouseoutHeaderButton;
		this.divClose.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__close(); } ;
		DHTMLSuite.commonObj.__addEventElement(this.divClose);	
		buttonDiv.appendChild(this.divClose);
		


		
		// Creating collapse button
		if(pos!='center'){
			this.divCollapse = document.createElement('DIV');
			if(pos=='south' || pos=='east')
				this.divCollapse.className='expandButton' + buttonSuffix;
			else
				this.divCollapse.className='collapseButton' + buttonSuffix;
				
			this.divCollapse.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__collapse(); } ;
			this.divCollapse.onmouseover = this.__mouseoverHeaderButton;
			this.divCollapse.onmouseout = this.__mouseoutHeaderButton;
			this.divCollapse.style.display='none';			
			DHTMLSuite.commonObj.__addEventElement(this.divCollapse);		
			buttonDiv.appendChild(this.divCollapse);
		}

		
		// Creating refresh button
		this.divRefresh = document.createElement('DIV');
		this.divRefresh.className='refreshButton';
		this.divRefresh.onmouseover = this.__mouseoverHeaderButton;
		this.divRefresh.onmouseout = this.__mouseoutHeaderButton;
		this.divRefresh.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__reloadDisplayedContent(); } ;		
		DHTMLSuite.commonObj.__addEventElement(this.divRefresh);	
		buttonDiv.appendChild(this.divRefresh);
				
		
		this.headerDiv.onselectstart = DHTMLSuite.commonObj.cancelEvent;
			
	}
	// }}}
	,
	// {{{ __mouseoverHeaderButton()
	/**
	*	Mouse over effect - buttons
	*
	*
	*	@private
	*/		
	__mouseoverHeaderButton : function()
	{
		if(this.className.indexOf('Over')==-1)this.className=this.className + 'Over';
	}
	// }}}
	,
	// {{{ __mouseoutHeaderButton()
	/**
	*	Mouse out effect - buttons
	*
	*
	*	@private
	*/		
	__mouseoutHeaderButton : function()
	{
		this.className=this.className.replace('Over','');
	}
	,
	// {{{ __close()
	/**
	*	Close a pane
	*
	*	@param Event e = Reference to Event object
	*
	*	@private
	*/	
	__close : function(e)
	{
		// Check to see if there's an callbackOnBeforeCloseContent event
		var id = this.paneModel.contents[this.activeContentIndex].id;
		var ok = this.__getOnBeforeCloseResult(this.activeContentIndex);
		if(!ok)return;
		
		if(id){
			
			this.__executeCallBack('closeContent',this.paneModel.contents[this.activeContentIndex]);
			document.getElementById(this.paneModel.contents[this.activeContentIndex].htmlElementId).parentNode.removeChild(document.getElementById(this.paneModel.contents[this.activeContentIndex].htmlElementId));
		}
		this.activeContentIndex = this.paneModel.__deleteContent(this.activeContentIndex);			
		this.__updatePaneView(this.activeContentIndex);
		
	}
	// }}}
	,
	// {{{ __closeAllClosableTabs()
	/**
	*	Close all closable tabs.
	*
	*
	*	@private
	*/	
	__closeAllClosableTabs : function()
	{
		for(var no=0;no<this.paneModel.contents.length;no++){
			var closable = this.paneModel.contents[no].__getClosable();
	
			if(closable){
				var id = this.paneModel.contents[no].id;
				document.getElementById(this.paneModel.contents[no].htmlElementId).parentNode.removeChild(document.getElementById(this.paneModel.contents[no].htmlElementId));
				this.activeContentIndex = this.paneModel.__deleteContent(no);			
				this.__updatePaneView(this.activeContentIndex);		
				no--;		
			}		
		}			
	}
	// }}}
	,
	// {{{ __getOnBeforeCloseResult()
	/**
	*	Return result of onBeforeClose callback
	*
	*	@param Integer contentIndex - Index of content to close.
	*
	*	@private
	*/		
	__getOnBeforeCloseResult : function(contentIndex)
	{
		return this.__executeCallBack('beforeCloseContent',this.paneModel.contents[contentIndex]);
		
	}
	// }}}
	,
	// {{{ __deleteContentByIndex()
	/**
	*	Close a pane
	*
	*	@param Integer index of content to delete
	*
	*	@private
	*/		
	__deleteContentByIndex : function(contentIndex)
	{
		if(this.paneModel.getCountContent()==0)return;	// No content to delete
		if(!this.__getOnBeforeCloseResult(contentIndex))return;
		
		var htmlElementId = this.paneModel.contents[contentIndex].htmlElementId;
		if(htmlElementId){
			try{
				document.getElementById(htmlElementId).parentNode.removeChild(document.getElementById(htmlElementId));
			}catch(e){
			}
		}
		
		var tmpIndex = this.paneModel.__deleteContent(contentIndex);		
		if(contentIndex==this.activeContentIndex)this.activeContentIndex = tmpIndex;
		if(this.activeContentIndex > contentIndex)this.activeContentIndex--;
		if(tmpIndex===false)this.activeContentIndex=false;
			
		this.__updatePaneView(this.activeContentIndex);		
		
	}
	// }}}
	,
	// {{{ __deleteContentById()
	/**
	*	Close/Delete content
	*
	*	@param String id = Id of content to delete/close
	*
	*	@private
	*/		
	__deleteContentById : function(id)
	{
		var index = this.paneModel.__getIndexById(id);
		if(index!==false)this.__deleteContentByIndex(index);		
	}
	// }}}
	,
	// {{{ __collapse()
	/**
	*	Collapse a pane.
	*
	*
	*	@private
	*/		
	__collapse : function()
	{
		this.__updateCollapsedSize();
		this.paneModel.__setState('collapsed');		// Updating the state property
		this.divElementCollapsed.style.visibility='visible';
		this.divElement.style.visibility='hidden';
		this.__updateView();
		this.parentRef.__hideResizeHandle(this.paneModel.getPosition());
		this.parentRef.__positionPanes();	// Calling the positionPanes method of parent object
		this.__executeCallBack("collapse",this.paneModel.contents[this.activeContentIndex]);
	}
	,
	// {{{ __expand()
	/**
	*	Expand a pane
	*
	*
	*	@private
	*/		
	__expand : function()
	{
		this.paneModel.__setState('expanded');		// Updating the state property
		this.divElementCollapsed.style.visibility='hidden';
		this.divElement.style.visibility='visible';
		this.__updateView();		
		this.parentRef.__showResizeHandle(this.paneModel.getPosition());
		this.parentRef.__positionPanes();	// Calling the positionPanes method of parent object
		this.__executeCallBack("expand",this.paneModel.contents[this.activeContentIndex]);
	}
	// }}}
	,
	// {{{ __updateHeaderBar()
	/**
	*	This method will automatically update the buttons in the header bare depending on the setings specified for currently displayed content.
	*
	*	@param Integer index - Index of currently displayed content
	*
	*	@private
	*/	
	__updateHeaderBar : function(index)
	{
		if(index===false){	// No content in this pane
			this.divClose.style.display='none';	// Hide close button
			this.divRefresh.style.display='none';
			if(this.paneModel.getPosition()!='center')this.divCollapse.style.display='block';else this.divCollapse.style.display='none';	// Make collapse button visible for all panes except center
			this.titleSpan.innerHTML = '';	// Set title bar empty
			return;	// Return from this method.
		}
		this.divClose.style.display='block';
		this.divRefresh.style.display='block';
		
		if(this.divCollapse)this.divCollapse.style.display='block';	// Center panes doesn't have collapse button, that's the reason for the if-statement
		this.titleSpan.innerHTML = this.paneModel.contents[index].title;
		var contentObj = this.paneModel.contents[index];
		if(!contentObj.closable)this.divClose.style.display='none';
		if(!contentObj.displayRefreshButton || !contentObj.contentUrl)this.divRefresh.style.display='none';
		if(!this.paneModel.collapsable){	// Pane is collapsable
			if(this.divCollapse)this.divCollapse.style.display='none';	// Center panes doesn't have collapse button, that's the reason for the if-statement
			this.divExpand.style.display='none';
		}

	}
	// }}}
	,
	// {{{ __showButtons()
	/**
	*	Show the close and resize button - it is done by showing the parent element of these buttons
	*
	*
	*	@private
	*/		
	__showButtons : function()
	{
		var div = this.headerDiv.getElementsByTagName('DIV')[0];
		div.style.visibility='visible';		
		
	}
	// }}}
	,
	// {{{ __hideButtons()
	/**
	*	Hides the close and resize button - it is done by hiding the parent element of these buttons
	*
	*
	*	@private
	*/		
	__hideButtons : function()
	{
		var div = this.headerDiv.getElementsByTagName('DIV')[0];
		div.style.visibility='hidden';
		
	}
	// }}}
	,
	// {{{ __updateView()
	/**
	* 	Hide or shows header div and tab div based on content
	*
	*
	*	@private
	*/		
	__updateView : function()
	{
		if(this.paneModel.getCountContent()>0 && this.activeContentIndex===false)this.activeContentIndex = 0;	// No content existed, but content has been added.
		this.tabDiv.style.display='block';
		this.headerDiv.style.display='block';	
		
		var pos = this.paneModel.getPosition();
		if(pos=='south' || pos=='north')this.divElementCollapsed.style.height = this.parentRef.paneSizeCollapsed;

		if(this.paneModel.getCountContent()<2)this.tabDiv.style.display='none';		
		if(this.activeContentIndex!==false)if(!this.paneModel.contents[this.activeContentIndex].title)this.headerDiv.style.display='none';	// Active content without title, hide header bar.
		
		if(this.paneModel.state=='expanded')this.__showButtons();else this.__hideButtons();
				
		this.__setSize();
	}
	// }}}
	,
	// {{{ __createContentPane()
	/**
	* 	Creates the content pane
	*
	*
	*	@private
	*/		
	__createContentPane : function()
	{
		this.contentDiv = document.createElement('DIV');
		this.contentDiv.className = 'DHTMLSuite_paneContent';
		this.contentDiv.id = 'DHTMLSuite_paneContent' + this.paneModel.getPosition();
		if(!this.paneModel.scrollbars)this.contentDiv.style.overflow='hidden';
		this.divElement.appendChild(this.contentDiv);		

	}
	// }}}
	,
	// {{{ __createTabBar()
	/**
	* 	Creates the top bar of a pane
	*
	*
	*	@private
	*/		
	__createTabBar : function()
	{
		this.tabDiv = document.createElement('DIV');
		this.tabDiv.className = 'DHTMLSuite_paneTabs';
		this.divElement.appendChild(this.tabDiv);
		this.__updateTabContent();
	}
	// }}}
	,
	// {{{ __updateTabContent()
	/**
	* 	Reset and repaint the tabs of this pane
	*
	*
	*	@private
	*/			
	__updateTabContent : function()
	{
		this.tabDiv.innerHTML = '';	
		var tableObj = document.createElement('TABLE');
		
		tableObj.style.padding = '0px';
		tableObj.style.margin = '0px';
		tableObj.cellPadding = 0;
		tableObj.cellSpacing = 0;
		this.tabDiv.appendChild(tableObj);
		var tbody = document.createElement('TBODY');
		tableObj.appendChild(tbody);
		
		var row = tbody.insertRow(0);
		
		var contents = this.paneModel.getContents();
		var ind = this.objectIndex;
		for(var no=0;no<contents.length;no++){
			var cell = row.insertCell(-1);			
			var divTag = document.createElement('DIV');
			divTag.className = 'paneSplitterInactiveTab';			
			cell.appendChild(divTag);			
			var aTag = document.createElement('A');
			aTag.title = contents[no].tabTitle;	// Setting title of tab - useful when the tab isn't wide enough to show the label.
			contents[no].tabTitle = contents[no].tabTitle + '';
			aTag.innerHTML = contents[no].tabTitle.replace(' ','&nbsp;') + '';
			aTag.id = 'paneTabLink' + no;
			aTag.href='#';
			aTag.onclick = function(e) { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__tabClick(e); } ;
			divTag.appendChild(aTag);
			DHTMLSuite.commonObj.__addEventElement(aTag);			
			divTag.appendChild(document.createElement('SPAN'));		
		}		
		this.__updateTabView(0);
	}	
	// }}}
	,	
	// {{{ __updateTabView()
	/**
	* 	 Updates the tab view. Sets inactive and active tabs.
	*
	*	@param Integer activeTab - Index of active tab.
	*
	*	@private
	*/		
	__updateTabView : function(activeTab)
	{
		var tabDivs = this.tabDiv.getElementsByTagName('DIV');
		for(var no=0;no<tabDivs.length;no++){
			if(no==activeTab){
				tabDivs[no].className = 'paneSplitterActiveTab';
			}else tabDivs[no].className = 'paneSplitterInactiveTab';			
		}		
	}
	// }}}
	,	
	// {{{ __tabClick()
	/**
	* 	 Click on a tab
	*
	*	@param Event e - Reference to the object triggering the event. Content index is the numeric part of this elements id.
	*
	*	@private
	*/	
	__tabClick : function(e)
	{
		// contentScrollTopPositions
		if(document.all)e = event;
		
		var inputObj = DHTMLSuite.commonObj.getSrcElement(e);
		if(inputObj.tagName!='A')inputObj = inputObj.parentNode;
		
		var numericIdOfClickedTab = inputObj.id.replace(/[^0-9]/gi,'');
		if(numericIdOfClickedTab!=this.activeContentIndex)this.__updatePaneContentScrollTopPosition(this.activeContentIndex,numericIdOfClickedTab);
		
		
		
		this.__updatePaneView(numericIdOfClickedTab);
		this.__executeCallBack("tabSwitch",this.paneModel.contents[this.activeContentIndex]);
		return false;
	}
	// }}}
	,
	// {{{ __updatePaneContentScrollTopPosition()
	/**
	* 	Changes the scrollTop position of the pane. This is useful when you move from tab to tab. This object remembers the scrollTop position of all it's tab and changes the
	*	scrollTop attribute 
	*
	*	@param String idOfContentToHide of content element to hide 
	*	@param String idOfContentToShow of content element to show 
	*
	*	@public
	*/		
	__updatePaneContentScrollTopPosition : function(idOfContentToHide,idOfContentToShow)
	{
		var newScrollTop = 0;
		if(this.contentScrollTopPositions[idOfContentToShow])newScrollTop = this.contentScrollTopPositions[idOfContentToShow];
		var contentParentContainer = document.getElementById(this.paneModel.contents[idOfContentToHide].htmlElementId).parentNode;
		this.contentScrollTopPositions[idOfContentToHide] = contentParentContainer.scrollTop;
		setTimeout('document.getElementById("' + contentParentContainer.id + '").scrollTop = ' + newScrollTop,20);	// A small delay so that content can be inserted into the div first.
	}
	// }}}	
	,
	// {{{ __addContentDivs()
	/**
	* 	Add content div to a pane.
	*
	*	@param String onCompleteJsCode - Js code to execute when content has been succesfully loaded into the pane
	*
	*	@public
	*/		
	__addContentDivs : function(onCompleteJsCode)
	{
		var contents = this.paneModel.getContents();
		for(var no=0;no<contents.length;no++){
			this.__addOneContentDiv(this.paneModel.contents[no],onCompleteJsCode);
		}			
		this.__updatePaneView(this.activeContentIndex);	// Display initial data
	}
	,
	// {{{ __addSingleContentToPane()
	/**
	* 	
	*
	*	@param Object contentObj PaneSplitterContentModel object.
	*
	*	@private
	*/		
	__addOneContentDiv : function(contentObj,onCompleteJsCode)
	{
		var htmlElementId = contentObj.htmlElementId;	// Get a reference to content id
		var contentUrl = contentObj.contentUrl;	// Get a reference to content id
		var refreshAfterSeconds = contentObj.refreshAfterSeconds;	// Get a reference to content id
		if(htmlElementId){
			try{
				this.contentDiv.appendChild(document.getElementById(htmlElementId));
				document.getElementById(htmlElementId).className = 'DHTMLSuite_paneContentInner';
				document.getElementById(htmlElementId).style.display='none';		
			}catch(e){
			}		
		}
		if(contentUrl){	/* Url present */
			if(!contentObj.htmlElementId || htmlElementId.indexOf('dynamicCreatedDiv__')==-1){	// Has this content been loaded before ? Might have to figure out a smarter way of checking this.
				if(!document.getElementById(htmlElementId)){				
					this.__createAContentDivDynamically(contentObj);
					this.loadContent(contentObj.id,contentUrl,refreshAfterSeconds,false,onCompleteJsCode);
				}
			}
		}		
	}
	// }}}
	,
	// {{{ __createAContentDivDynamically()
	/**
	* 	Create the div for a tab dynamically (in case no content exists, i.e. content loaded from external file)
	*
	*	@param Object contentObj PaneSplitterContentModel object.
	*
	*	@private
	*/		
	__createAContentDivDynamically : function(contentObj)
	{
		var d = new Date();	// Create unique id for a new div
		var divId = 'dynamicCreatedDiv__' + d.getSeconds() + (Math.random()+'').replace('.','');
		if(!document.getElementById(contentObj.id))divId = contentObj.id;	// Give it the id of the element it's self if it doesn't alredy exists on the page.
		contentObj.__setIdOfContentElement(divId);
		var div = document.createElement('DIV');
		div.id = divId;
		div.className = 'DHTMLSuite_paneContentInner';
		if(contentObj.contentUrl)div.innerHTML = this.parentRef.waitMessage;	// Content url present - Display wait message until content has been loaded.
		this.contentDiv.appendChild(div);
		div.style.display='none';			
	}
	// }}}
	,
	// {{{ __showHideContentDiv()
	/**
	* 	Updates the pane view. New content has been selected. call methods for update of header bars, content divs and tabs.
	*
	*	@param Integer index Index of active content ( false = no content exists)
	*
	*	@private
	*/			
	__updatePaneView : function(index)
	{
		if(!index && index!==0)index=this.activeContentIndex;
		this.__updateTabContent();
		this.__updateView();
		this.__updateHeaderBar(index);
		this.__showHideContentDiv(index);

		this.__updateTabView(index);
		this.activeContentIndex = index;
	}
	// }}}
	,
	// {{{ __showHideContentDiv()
	/**
	*	Switch between content divs(the inner div inside a pane )
	*
	*	@param Integer index Index of content to show(if false, then do nothing --- because there aren't any content in this pane)
	*
	*	@private
	*/		
	__showHideContentDiv : function(index)
	{
		if(index!==false){	// Still content in this pane			
			var htmlElementId = this.paneModel.contents[this.activeContentIndex].htmlElementId;	
			try{
				document.getElementById(htmlElementId).style.display='none';	
			}catch(e){
				
			}			
			var htmlElementId = this.paneModel.contents[index].htmlElementId;
			if(htmlElementId){
				try{
					document.getElementById(htmlElementId).style.display='block';		
				}catch(e){
				}
			}
		}		
	}
	// }}}	
	,
	// {{{ __setSize()
	/**
	*	Set some size attributes for the panes
	*
	*	@param Boolean recursive
	*
	*	@private
	*/			
	__setSize : function(recursive)
	{
		var pos = this.paneModel.getPosition().toLowerCase();
		if(pos=='west' || pos=='east'){
			this.divElement.style.width = this.paneModel.size + 'px';	
		}
		if(pos=='north' || pos=='south'){
			this.divElement.style.height = this.paneModel.size + 'px';	
			this.divElement.style.width = '100%';
		}
		
		try{
			this.contentDiv.style.height = (this.divElement.clientHeight - this.tabDiv.offsetHeight - this.headerDiv.offsetHeight) + 'px';
		}catch(e){
		}
		
		if(!recursive){
			window.obj = this;
			setTimeout('window.obj.__setSize(true)',100);
		}
		
	}	
	// }}}
	,
	// {{{ __setTopPosition()
	/**
	*	Set new top position for the pane
	*
	*	@param Integer newTop
	*
	*	@private
	*/		
	__setTopPosition : function(newTop)
	{
		this.divElement.style.top = newTop + 'px';
	}
	// }}}
	,
	// {{{ __setLeftPosition()
	/**
	*	Set new left position for the pane
	*
	*	@param Integer newLeft
	*
	*	@private
	*/		
	__setLeftPosition : function(newLeft)
	{
		this.divElement.style.left = newLeft + 'px';
	}
	// }}}
	,
	// {{{ __setWidth()
	/**
	*	Set width for the pane
	*
	*	@param Integer newWidth
	*
	*	@private
	*/		
	__setWidth : function(newWidth)
	{
		if(this.paneModel.getPosition()=='west' || this.paneModel.getPosition()=='east')this.paneModel.setSize(newWidth);
		newWidth = newWidth + '';
		if(newWidth.indexOf('%')==-1)newWidth = Math.max(1,newWidth) + 'px';
		this.divElement.style.width = newWidth;		
	}
	// }}}
	,
	// {{{ __setHeight()
	/**
	*	Set height for the pane
	*
	*	@param Integer newHeight
	*
	*	@private
	*/		
	__setHeight : function(newHeight)
	{
		if(this.paneModel.getPosition()=='north' || this.paneModel.getPosition()=='south')this.paneModel.setSize(newHeight);
		this.divElement.style.height = Math.max(1,newHeight) + 'px';		
		this.__setSize();	// Set size of inner elements.	
	}
	,
	// {{{ __showTransparentDivForResize()
    /**
     *	Show transparent div used to cover iframes during resize 
     *
     *
     * @private	
     */			
	__showTransparentDivForResize : function()
	{
		this.divTransparentForResize.style.display = 'block';
		var ref = this.divTransparentForResize;
		ref.style.height = this.contentDiv.clientHeight + 'px';
		ref.style.width = this.contentDiv.clientWidth + 'px';	
		
	}
	// }}}
	,
	// {{{ __hideTransparentDivForResize()
    /**
     *	Hide transparent div used to cover iframes during resize  
     *
     *
     * @private	
     */		
	__hideTransparentDivForResize : function()
	{
		this.divTransparentForResize.style.display = 'none';
	}
	// }}}	
}


/************************************************************************************************************
*	DHTML pane splitter
*
*	Created:						November, 28th, 2006
*	@class Purpose of class:		Creates a pane splitter
*			
*	Css files used by this script:	pane-splitter.css
*
*	Demos of this class:			demo-pane-splitter.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Creates a pane splitter. (<a href="../../demos/demo-pane-splitter.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.paneSplitter = function()
{
	var dataModel;				// An object of class DHTMLSuite.paneSplitterModel
	var panes;					// An array of DHTMLSuite.paneSplitterPane objects.
	var panesAssociative;		// An associative array of panes. used to get a quick access to the panes
	var paneContent;			// An array of DHTMLSuite.paneSplitterPaneView objects.
	var layoutCSS;				// Name/Path of css file
	
	var horizontalSplitterSize;	// Height of horizontal splitter
	var horizontalSplitterBorderSize;	// Height of horizontal splitter
	
	var verticalSplitterSize;	// 

	var paneSplitterHandles;				// Associative array of pane splitter handles
	var paneDivsCollapsed;					// Associative array of divs ( collapsed state of pane )
	var paneSplitterHandleOnResize;
	
	var resizeInProgress;					// Variable indicating if resize is in progress
	
	var resizeCounter;						// Internal variable used while resizing (-1 = no resize, 0 = waiting for resize)
	var currentResize;						// Which pane is currently being resized ( string, "west", "east", "north" or "south"
	var currentResize_min;
	var currentResize_max;
	
	var paneSizeCollapsed;					// Size of pane when it's collapsed ( the bar )
	var paneBorderLeftPlusRight;			// Sum of border left and right for panes ( needed in a calculation)
	
	var slideSpeed;							// Slide of pane slide	
	var waitMessage;						// Ajax wait message
	
	this.resizeCounter = -1;
	this.horizontalSplitterSize = 5;
	this.verticalSplitterSize = 5;
	this.paneBorderLeftPlusRight = 2;		// 1 pixel border at the right of panes, 1 pixel to the left
	this.slideSpeed = 10;
	
	this.horizontalSplitterBorderSize = 1;
	this.resizeInProgress = false;
	this.paneSplitterHandleOnResize = false;
	this.paneSizeCollapsed = 26;
	
	this.paneSplitterHandles = new Array();
	this.paneDivsCollapsed = new Array();
	
	this.dataModel = false;		// Initial value
	this.layoutCSS = 'pane-splitter.css';
	this.waitMessage = 'Loading content - please wait';
	this.panes = new Array();
	this.panesAssociative = new Array();
	
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	var objectIndex;			// Index of this object in the variableStorage array
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
		
}

DHTMLSuite.paneSplitter.prototype = 
{
	// {{{ addModel()
	/**
	*	Add datasource for the pane splitter
	*
	*	@param Object newModel - Data source, object of class DHTMLSuite.paneSplitterModel
	*
	*	@public
	*/		
	addModel : function(newModel)
	{
		this.dataModel = newModel;		
	}
	// }}}
	,
	// {{{ setLayoutCss()
	/**
	*	Specify name/path to a css file(default is 'pane-splitter.css')
	*
	*	@param String layoutCSS = Name(or relative path) of new css path
	*	@public
	*/		
	setLayoutCss : function(layoutCSS)
	{
		this.layoutCSS = layoutCSS;
	}
	// }}}
	,
	// {{{ setAjaxWaitMessage()
	/**
	*	Specify ajax wait message - message displayed in the pane when content is being loaded from the server.
	*
	*	@param String newWaitMessage = Wait message - plain text or HTML.
	*
	*	@public
	*/			
	setAjaxWaitMessage : function(newWaitMessage)
	{
		this.waitMessage = newWaitMessage;	
		
	}	
	// }}}
	,
	// {{{ setSlideSpeed()
	/**
	*	Set speed of slide animation.
	*
	*	@param Integer slideSpeed = new slide speed ( higher = faster ) - default = 10
	*
	*	@public
	*/		
	setSlideSpeed : function(slideSpeed)
	{
		this.slideSpeed = slideSpeed;
	}
	,
	// {{{ init()
	/**
	*	Initializes the script
	*
	*
	*	@public
	*/		
	init : function()
	{
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	// Load css.	
		this.__createPanes();	// Create the panes	
		this.__positionPanes();	// Position panes
		this.__createResizeHandles();
		this.__addEvents();
		this.__initCollapsePanes();
		setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + this.objectIndex + "].__positionPanes();",100);
		setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + this.objectIndex + "].__positionPanes();",500);
		setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + this.objectIndex + "].__positionPanes();",1500);
	}
	// }}}
	,

	// {{{ isUrlLoadedInPane()
    /**
     *	This method returns true if content with a specific url exists inside a specific content container.
     *
     *	@param String id 		- id of content object
     *	@param String url		- Url of file (Url to check on)
     *
     * @public	
     */		
    	
	isUrlLoadedInPane : function(id,url)
	{		
		var ref = this.__getPaneReferenceFromContentId(id);	// Get a reference to the pane where the content is.
		if(ref){	// Pane found
			return ref.isUrlLoadedInPane(id,url);
		}else return false;
	}
	// }}}
	,
	// {{{ loadContent()
    /**
     *	This method loads content from server and inserts it into the pane with the given id 
     *	If you want the content to be displayed directly, remember to call the showContent method too.
     *
     *	@param String id - id of content object where the element should be inserted
     *	@param String url		- Url of file (the content of this file will be inserted into the define pane)
	 *	@param Integer refreshAfterSeconds		- Reload url after number of seconds. 0 = no refresh ( also default)
     *	@param String onCompleteJsCode - Js code to evaluate when content has been successfully loaded(Callback) - example: "myFunction()". This string will be avaluated.
     *
     * @public	
     */		
    	
	loadContent : function(id,url,refreshAfterSeconds,onCompleteJsCode)
	{		
		var ref = this.__getPaneReferenceFromContentId(id);	// Get a reference to the pane where the content is.
		if(ref){	// Pane found
			ref.loadContent(id,url,refreshAfterSeconds,false,onCompleteJsCode);		// Call the loadContent method of this object. 
		}
	}
	// }}}
	,
	// {{{ reloadContent()
    /**
     *	Reloads ajax content
     *
     *	@param String id - id of content object to reload.
     *
     * @public	
     */		
	reloadContent : function(id)
	{
		var ref = this.__getPaneReferenceFromContentId(id);	// Get a reference to the pane where the content is.
		if(ref){	// Pane found
			ref.reloadContent(id);		// Call the loadContent method of this object. 
		}		
	}
	// }}}
	,
	// {{{ setRefreshAfterSeconds()
    /**
     *	Specify a new value for when content should be reloaded. 
     *
     *	@param String idOfContentObject - id of content to add the value to
     *	@param Integer refreshAfterSeconds - Refresh rate of content (0 = no refresh)
     *
     * @public	
     */	
	setRefreshAfterSeconds : function(idOfContentObject,refreshAfterSeconds)
	{
		var ref = this.__getPaneReferenceFromContentId(idOfContentObject);	// Get a reference to the pane where the content is.
		if(ref){	// Pane found
			ref.setRefreshAfterSeconds(idOfContentObject,refreshAfterSeconds);		// Call the loadContent method of this object. 
		}		
		
	}	
	// }}}
	,
	// {{{ setContentTabTitle()
    /**
     *	New title of tab - i.e. the text inside the clickable tab.
     *
     *	@param String idOfContentObject - id of content object
     *	@param String newTitle - New title of tab	
     *
     * @public	
     */		
	setContentTabTitle : function(idOfContentObject,newTitle)
	{
		var ref = this.__getPaneReferenceFromContentId(idOfContentObject);	// Get a reference to the pane where the content is.
		if(ref)ref.setContentTabTitle(idOfContentObject,newTitle);
		
	}
	// }}}
	,
	// {{{ setContentTitle()
    /**
     *	New title of content - i.e. the heading
     *
     *	@param String idOfContentObject - id of content object
     *	@param String newTitle - New title of tab	
     *
     * @public	
     */		
	setContentTitle : function(idOfContentObject,newTitle)
	{
		var ref = this.__getPaneReferenceFromContentId(idOfContentObject);	// Get a reference to the pane where the content is.
		if(ref)ref.setContentTitle(idOfContentObject,newTitle);
		
	}
	// }}}
	,
	// {{{ showContent()
    /**
     *	Makes content with a specific id visible 
     *
     *	@param String id - id of content to make visible(remember to have unique id's on each of your content objects)
     *
     * @public	
     */		
	showContent : function(id)
	{
		var ref = this.__getPaneReferenceFromContentId(id);
		if(ref)ref.showContent(id);
	}	
	// }}}
	,
	// {{{ closeAllClosableTabs()
	/**
	*	Close all closable tabs, i.e. tabs where the closable attribute is set to true.
	*
	*	@param String panePosition
	*
	*	@public
	*/	
	closeAllClosableTabs : function(panePosition)
	{
		if(this.panesAssociative[panePosition.toLowerCase()]) return this.panesAssociative[panePosition.toLowerCase()].__closeAllClosableTabs(); else return false;
		
	}
	// }}}
	,
	// {{{ addContent()
	/**
	*	Add content to a pane
	*
	*	@param String panePosition - Position of pane(west,north,center,east or south)
	*	@param Object contentModel - Object of type DHTMLSuite.paneSplitterContentModel
	*	@param String onCompleteJsCode - Js code to execute when content is successfully loaded.
	*	@return Boolean Success - true if content were added successfully, false otherwise - false means that the pane don't exists or that content with this id allready has been added.
	*	@public
	*/		
	addContent : function(panePosition,contentModel,onCompleteJsCode)
	{
		if(this.panesAssociative[panePosition.toLowerCase()]) return this.panesAssociative[panePosition.toLowerCase()].addContent(contentModel,onCompleteJsCode); else return false;
		
	}	
	// }}}
	,
	// {{{ deleteContentById()
	/**
	*	Delete content from a pane by index
	*
	*	@param String id - Id of content to delete.
	*
	*	@public
	*/		
	deleteContentById : function(id)
	{
		var ref = this.__getPaneReferenceFromContentId(id);
		if(ref)ref.__deleteContentById(id);
	}
	// }}}
	,
	// {{{ deleteContentByIndex()
	/**
	*	Delete content from a pane by index
	*
	*	@param String panePosition - Position of pane(west,north,center,east or south)
	*	@param Integer	contentIndex
	*
	*	@public
	*/		
	deleteContentByIndex: function(panePosition,contentIndex)
	{
		if(this.panesAssociative[panePosition]){//Pane exists
			this.panesAssociative[panePosition].__deleteContentByIndex(contentIndex);		
		}		
	}	
	// }}}
	,
	// {{{ hidePane()
	/**
	*	Hide a pane
	*
	*	@param String panePosition - Position of pane(west,north,center,east or south)
	*
	*	@public
	*/	
	hidePane : function(panePosition)
	{
		if(this.panesAssociative[panePosition] && panePosition!='center'){
			this.panesAssociative[panePosition].hidePane();				 // Call method in paneSplitterPane class
			if(this.paneSplitterHandles[panePosition])this.paneSplitterHandles[panePosition].style.display='none'; // Hide resize handle
			this.__positionPanes();										 // Reposition panes 	
		}else return false;
		
	}	
	,
	// {{{ showPane()
	/**
	*	Show a previously hidden pane
	*
	*	@param String panePosition - Position of pane(west,north,center,east or south)
	*
	*	@public
	*/	
	showPane : function(panePosition)
	{
		if(this.panesAssociative[panePosition] && panePosition!='center'){
			this.panesAssociative[panePosition].showPane();					// Call method in paneSplitterPane class
			if(this.paneSplitterHandles[panePosition])this.paneSplitterHandles[panePosition].style.display='block';	// Show resize handle
			this.__positionPanes();											// Reposition panes
		}else return false;			
	}	
	// }}}
	,
	// {{{ getReferenceToMainDivElementOfPane()
	/**
	*	Get reference to main div element of a pane. This can for example be useful if you're using the imageSelection class and want
	*	To restrict the area for a selection. Maybe you only want your users to start selection within the center pane, not the other panes.
	*
	*	@param String panePosition - Position of pane(west,north,center,east or south)
	*
	*	@public
	*/	
	getReferenceToMainDivElementOfPane : function(panePosition)
	{
		if(this.panesAssociative[panePosition])return this.panesAssociative[panePosition].__getReferenceToMainDivElement();
		return false;		
	}
	// }}}
	,
	// {{{ getIdOfCurrentlyDisplayedContent()
	/**
	* 	Returns id of the content currently being displayed - active tab.
	*
	*	@param String position - which pane. ("west","east","center","north","south")
	*	@return String id of currently displayed content (active tab).
	*
	*	@public
	*/		
	getIdOfCurrentlyDisplayedContent : function(panePosition)
	{
		if(this.panesAssociative[panePosition])return this.panesAssociative[panePosition].getIdOfCurrentlyDisplayedContent();
		return false;		
	}
	// }}}
	,
	// {{{ getSizeOfPaneInPixels()
	/**
	* 	Returns id of the content currently being displayed - active tab.
	*
	*	@param String position - which pane. ("west","east","center","north","south")
	*	@return Array - Assocative array representing width and height of the pane(keys in array: "width" and "height").
	*
	*	@public
	*/		
	getSizeOfPaneInPixels : function(panePosition)
	{
		if(this.panesAssociative[panePosition])return this.panesAssociative[panePosition].__getSizeOfPaneInPixels();
		return false;			
		
	}
	// }}}
	,
	// {{{ __setPaneSizeCollapsed()
	/**
	*	Automatically set size of collapsed pane ( called by a pane - the size is the offsetWidth or offsetHeight of the pane in collapsed state)
	*
	*
	*	@private
	*/		
	__setPaneSizeCollapsed : function(newSize)
	{
		if(newSize>this.paneSizeCollapsed)this.paneSizeCollapsed = newSize;
	}
	// }}}
	// }}}
	,
	// {{{ __createPanes()
	/**
	*	Creates the panes
	*
	*
	*	@private
	*/			
	__createPanes : function()
	{
		var dataObjects = this.dataModel.getItems();	// An array of data source objects, i.e. panes.
		for(var no=0;no<dataObjects.length;no++){
			var index = this.panes.length;
			this.panes[index] = new DHTMLSuite.paneSplitterPane(this);
			this.panes[index].addDataSource(dataObjects[no]);
			this.panes[index].__createPane();				

			this.panesAssociative[dataObjects[no].position.toLowerCase()] = this.panes[index];	// Save this pane in the associative array			
		}		
	}	
	// }}}
	,
	// {{{ __createResizeHandles()
    /**
     *	Positions the resize handles correctly
     *
     *
     * @private	
     */			
	__createResizeHandles : function()
	{
		var ind = this.objectIndex;
		// Create splitter for the north pane
		if(this.panesAssociative['north'] && this.panesAssociative['north'].paneModel.resizable){
			this.paneSplitterHandles['north'] = document.createElement('DIV');
			var obj = this.paneSplitterHandles['north'];
			obj.className = 'DHTMLSuite_paneSplitter_horizontal';
			obj.style.position = 'absolute';
			obj.style.height = this.horizontalSplitterSize + 'px';
			obj.style.width = '100%';
			obj.style.zIndex = 10000;
			DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('north'); });	
			document.body.appendChild(obj);			
		}
		// Create splitter for the west pane
		if(this.panesAssociative['west'] && this.panesAssociative['west'].paneModel.resizable){
			this.paneSplitterHandles['west'] = document.createElement('DIV');
			var obj = this.paneSplitterHandles['west'];
			obj.className = 'DHTMLSuite_paneSplitter_vertical';
			obj.style.position = 'absolute';
			obj.style.width = this.verticalSplitterSize + 'px';
			obj.style.zIndex = 11000;
			DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('west'); });	
			document.body.appendChild(obj);			
		}
		
		// Create splitter for the east pane
		if(this.panesAssociative['east'] && this.panesAssociative['east'].paneModel.resizable){
			this.paneSplitterHandles['east'] = document.createElement('DIV');
			var obj = this.paneSplitterHandles['east'];
			obj.className = 'DHTMLSuite_paneSplitter_vertical';
			obj.style.position = 'absolute';
			obj.style.width = this.verticalSplitterSize + 'px';
			obj.style.zIndex = 11000;		
			DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('east'); });			
			document.body.appendChild(obj);			
		}
		
		
		// Create splitter for the south pane
		if(this.panesAssociative['south'] && this.panesAssociative['south'].paneModel.resizable){
			this.paneSplitterHandles['south'] = document.createElement('DIV');
			var obj = this.paneSplitterHandles['south'];
			obj.className = 'DHTMLSuite_paneSplitter_horizontal';
			obj.style.position = 'absolute';
			obj.style.height = this.horizontalSplitterSize + 'px';
			obj.style.width = '100%';
			obj.style.zIndex = 10000;
			DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('south'); });	
			document.body.appendChild(obj);			
		}		
		
		// Create onresize handle
		this.paneSplitterHandleOnResize = document.createElement('DIV');
		var obj = this.paneSplitterHandleOnResize;
		obj.className = 'DHTMLSuite_paneSplitter_onResize';	
		obj.style.position = 'absolute';
		obj.style.zIndex = 955000;
		obj.style.display='none';
		document.body.appendChild(obj);
		
	}	
	// }}}
	,
	// {{{ __getPaneReferenceFromContentId()
    /**
     *	Returns a reference to a pane from content id
     *	
     *	@param String id - id of content
     *
     * @private	
     */		
	__getPaneReferenceFromContentId : function(id)
	{
		for(var no=0;no<this.panes.length;no++){
			var contents = this.panes[no].paneModel.getContents();
			for(var no2=0;no2<contents.length;no2++){
				if(contents[no2].id==id)return this.panes[no];
			}
		}
		return false;
		
	}
	// }}}
	,
	// {{{ __initCollapsePanes()
    /**
     *	Initially collapse panes
     *	
     *
     * @private	
     */			
	__initCollapsePanes : function()
	{
		for(var no=0;no<this.panes.length;no++){	/* Loop through panes */			
			if(this.panes[no].paneModel.state=='collapsed'){	// State set to collapsed ?
				this.panes[no].__collapse();					
			}		
		}
	}	
	// }}}
	,
	// {{{ __getMinimumPos()
    /**
     *	Returns mininum pos in pixels
     *	
     *	@param String pos ("west","north","east","south")
     *
     * @private	
     */		
	__getMinimumPos : function(pos)
	{
		var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
		var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
				
		if(pos=='west' || pos == 'north'){
			return 	this.panesAssociative[pos].paneModel.minSize;
		}else{
			if(pos=='east')return 	browserWidth - this.panesAssociative[pos].paneModel.maxSize;
			if(pos=='south')return 	browserHeight - this.panesAssociative[pos].paneModel.maxSize;
		}
	}
	// }}}
	,	
	// {{{ __getMaximumPos()
    /**
     *	Returns maximum pos in pixels
     *	
     *	@param String pos ("west","north","east","south")
     *
     * @private	
     */				
	__getMaximumPos : function(pos)
	{
		var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
		var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
				
		if(pos=='west' || pos == 'north'){
			return 	this.panesAssociative[pos].paneModel.maxSize;
		}else{
			if(pos=='east')return 	browserWidth - this.panesAssociative[pos].paneModel.minSize;
			if(pos=='south')return 	browserHeight - this.panesAssociative[pos].paneModel.minSize;
		}
	}
	// }}}	
	,
	// {{{ __initResizePane()
    /**
     *	Mouse down on resize handle.
     *	
     *	@param String pos ("west","north","east","south")
     *
     * @private	
     */		
	__initResizePane : function(pos)
	{
		this.currentResize = pos;
		this.currentResize_min = this.__getMinimumPos(pos);
		this.currentResize_max = this.__getMaximumPos(pos);
		
		
		this.paneSplitterHandleOnResize.style.left = this.paneSplitterHandles[pos].style.left; 
		this.paneSplitterHandleOnResize.style.top = this.paneSplitterHandles[pos].style.top; 
		this.paneSplitterHandleOnResize.style.width = this.paneSplitterHandles[pos].offsetWidth + 'px'; 
		this.paneSplitterHandleOnResize.style.height = this.paneSplitterHandles[pos].offsetHeight + 'px'; 
		this.paneSplitterHandleOnResize.style.display='block';
		this.resizeCounter = 0;
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
		this.__timerResizePane(pos);
		
	}
	// }}}	
	,
	// {{{ __timerResizePane()
    /**
     *	A small delay between mouse down and resize start
     *	
     *	@param String pos - which pane to resize.
     *
     * @private	
     */		
	__timerResizePane : function(pos)
	{
		if(this.resizeCounter>=0 && this.resizeCounter<5){
			this.resizeCounter++;
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + this.objectIndex + '].__timerResizePane()',2);

		}	
		if(this.resizeCounter==5){
			this.__showTransparentDivForResize('show');
		}	
	}
	// }}}
	,
	// {{{ __showTransparentDivForResize()
    /**
     *	Show transparent divs used to cover iframes during resize
     *
     *	This is a solution to the problem where you're unable to drag the resize handle over iframes.
     *
     * @private	
     */		
	__showTransparentDivForResize : function()
	{
		if(DHTMLSuite.clientInfoObj.isOpera)return;	// Opera doesn't support transparent div the same way as FF and IE.
		if(this.panesAssociative['west'])this.panesAssociative['west'].__showTransparentDivForResize();
		if(this.panesAssociative['south'])this.panesAssociative['south'].__showTransparentDivForResize();
		if(this.panesAssociative['east'])this.panesAssociative['east'].__showTransparentDivForResize();
		if(this.panesAssociative['north'])this.panesAssociative['north'].__showTransparentDivForResize();
		if(this.panesAssociative['center'])this.panesAssociative['center'].__showTransparentDivForResize();
		
	}
	// }}}
	,
	// {{{ __hideTransparentDivForResize()
    /**
     *	Hide transparent divs used to cover iframes during resize
     *
     *
     * @private	
     */		
	__hideTransparentDivForResize : function()
	{
		if(this.panesAssociative['west'])this.panesAssociative['west'].__hideTransparentDivForResize();
		if(this.panesAssociative['south'])this.panesAssociative['south'].__hideTransparentDivForResize();
		if(this.panesAssociative['east'])this.panesAssociative['east'].__hideTransparentDivForResize();
		if(this.panesAssociative['north'])this.panesAssociative['north'].__hideTransparentDivForResize();
		if(this.panesAssociative['center'])this.panesAssociative['center'].__hideTransparentDivForResize();
		
	}
	// }}}
	,
	// {{{ __resizePane()
    /**
     *	Position the resize handle 
     *
     *
     * @private	
     */		
	__resizePane : function(e)
	{
		if(document.all)e = event;	// Get reference to event object.

		if(DHTMLSuite.clientInfoObj.isMSIE && e.button!=1)this.__endResize();
	
		if(this.resizeCounter==5){	/* Resize in progress */
			if(this.currentResize=='west' || this.currentResize=='east'){
				var leftPos = e.clientX;
				if(leftPos<this.currentResize_min)leftPos = this.currentResize_min;
				if(leftPos>this.currentResize_max)leftPos = this.currentResize_max;
				this.paneSplitterHandleOnResize.style.left = leftPos + 'px';
			}else{
				var topPos = e.clientY;
				if(topPos<this.currentResize_min)topPos = this.currentResize_min;
				if(topPos>this.currentResize_max)topPos = this.currentResize_max;				
				this.paneSplitterHandleOnResize.style.top = topPos + 'px';
			}	
		}		
	}
	// }}}
	,	
	// {{{ __endResize()
    /**
     *	End resizing	(mouse up event )
     *
     *
     * @private	
     */		
	__endResize : function()
	{
		
		if(this.resizeCounter==5){	// Resize completed 
			this.__hideTransparentDivForResize();
			var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
			var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();		
			var obj = this.panesAssociative[this.currentResize];
			switch(this.currentResize){
				case "west": 
					obj.__setWidth(this.paneSplitterHandleOnResize.style.left.replace('px','')/1);					
					break;	
				case "north":					
					obj.__setHeight(this.paneSplitterHandleOnResize.style.top.replace('px','')/1);
					break;
				case "east":
					obj.__setWidth(browserWidth - this.paneSplitterHandleOnResize.style.left.replace('px','')/1);
					break;
				case "south":
					obj.__setHeight(browserHeight - this.paneSplitterHandleOnResize.style.top.replace('px','')/1);
					break;				
			}					
			this.__positionPanes();
			obj.__executeResizeCallBack();

			this.paneSplitterHandleOnResize.style.display='none';
			this.resizeCounter = -1;
			DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
					
		}		
		

	}
	// }}}	
	,
	// {{{ __hideResizeHandle()
    /**
     *	Hide resize handle.
     *
     *
     * @private	
     */			
	__hideResizeHandle : function(pos){
		if(this.paneSplitterHandles[pos])this.paneSplitterHandles[pos].style.display='none';
	}
	// }}}
	,
	// {{{ __showResizeHandle()
    /**
     *	Make resize handle visible
     *
     *
     * @private	
     */		
	__showResizeHandle : function(pos){
		if(this.paneSplitterHandles[pos])this.paneSplitterHandles[pos].style.display='block';
	}
	// }}}
	,
	// {{{ __positionResizeHandles()
    /**
     *	Positions the resize handles correctly
     *	This method is called by the __positionPanes method. 
     *
     *
     * @private	
     */		
	__positionResizeHandles : function()
	{
		if(this.paneSplitterHandles['north']){	// Position north splitter handle
			this.paneSplitterHandles['north'].style.top = this.panesAssociative['north'].divElement.style.height.replace('px','')  + 'px';	
		}
		var heightHandler = this.panesAssociative['center'].divElement.offsetHeight+1;	// Initial height
		var topPos=0;
		if(this.panesAssociative['center'])topPos +=this.panesAssociative['center'].divElement.style.top.replace('px','')/1;
		
		if(this.paneSplitterHandles['west']){
			if(this.paneSplitterHandles['east'])heightHandler+=this.horizontalSplitterBorderSize/2;
			this.paneSplitterHandles['west'].style.left = this.panesAssociative['west'].divElement.offsetWidth + 'px';	
			this.paneSplitterHandles['west'].style.height = heightHandler + 'px';
			this.paneSplitterHandles['west'].style.top = topPos + 'px';
		}
		if(this.paneSplitterHandles['east']){
			var leftPos = this.panesAssociative['center'].divElement.style.left.replace('px','')/1 + this.panesAssociative['center'].divElement.offsetWidth;
			this.paneSplitterHandles['east'].style.left = leftPos + 'px';	
			this.paneSplitterHandles['east'].style.height = heightHandler + 'px';
			this.paneSplitterHandles['east'].style.top = topPos + 'px';
		}
		if(this.paneSplitterHandles['south']){			
			var topPos = this.panesAssociative['south'].divElement.style.top.replace('px','')/1;
			topPos = topPos - this.horizontalSplitterSize - this.horizontalSplitterBorderSize;
			this.paneSplitterHandles['south'].style.top = topPos + 'px';	
		}
		this.resizeInProgress = false;		
		
	}
	// }}}
	,
	// {{{ __positionPanes()
    /**
     *	Positions the panes correctly
     *
     *
     * @private	
     */		
	__positionPanes : function()
	{
		if(this.resizeInProgress)return;
		var ind = this.objectIndex;
		this.resizeInProgress = true;
		var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
		var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
		
		// Position north pane
		var posTopMiddlePanes = 0;
		if(this.panesAssociative['north'] && this.panesAssociative['north'].paneModel.visible){
			if(this.panesAssociative['north'].paneModel.state=='expanded'){
				posTopMiddlePanes = this.panesAssociative['north'].divElement.offsetHeight;
				if(this.paneSplitterHandles['north'])posTopMiddlePanes+=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
				this.panesAssociative['north'].__setHeight(this.panesAssociative['north'].divElement.offsetHeight);
			}else{
				posTopMiddlePanes+=this.paneSizeCollapsed;
			}	
		}
		
		// Set top position of center,west and east pa
		if(this.panesAssociative['center'])this.panesAssociative['center'].__setTopPosition(posTopMiddlePanes);
		if(this.panesAssociative['west'])this.panesAssociative['west'].__setTopPosition(posTopMiddlePanes);
		if(this.panesAssociative['east'])this.panesAssociative['east'].__setTopPosition(posTopMiddlePanes);
		
		if(this.panesAssociative['west'])this.panesAssociative['west'].divElementCollapsed.style.top = posTopMiddlePanes + 'px';
		if(this.panesAssociative['east'])this.panesAssociative['east'].divElementCollapsed.style.top = posTopMiddlePanes + 'px';
		
		// Position center pane
		var posLeftCenterPane = 0;
		if(this.panesAssociative['west']){
			if(this.panesAssociative['west'].paneModel.state=='expanded'){	// West panel is expanded.
				posLeftCenterPane = this.panesAssociative['west'].divElement.offsetWidth;	
				this.panesAssociative['west'].__setLeftPosition(0);	
				posLeftCenterPane+=(this.verticalSplitterSize);		
			}else{	// West panel is not expanded.
				posLeftCenterPane+=this.paneSizeCollapsed  ;
			}	
		}

		this.panesAssociative['center'].__setLeftPosition(posLeftCenterPane);

		// Set size of center pane		
		var sizeCenterPane = browserWidth;
		if(this.panesAssociative['west'] && this.panesAssociative['west'].paneModel.visible){	// Center pane exists and is visible - decrement width of center pane
			if(this.panesAssociative['west'].paneModel.state=='expanded')
				sizeCenterPane -= this.panesAssociative['west'].divElement.offsetWidth;
			else
				sizeCenterPane -= this.paneSizeCollapsed;
		}
		
		if(this.panesAssociative['east'] && this.panesAssociative['east'].paneModel.visible){	// East pane exists and is visible - decrement width of center pane
			 if(this.panesAssociative['east'].paneModel.state=='expanded')
			 	sizeCenterPane -= this.panesAssociative['east'].divElement.offsetWidth;
			 else
			 	sizeCenterPane -= this.paneSizeCollapsed;
			 	
		}
		sizeCenterPane-=this.paneBorderLeftPlusRight;
		if(this.paneSplitterHandles['west'] && this.panesAssociative['west'].paneModel.state=='expanded')sizeCenterPane-=(this.verticalSplitterSize);
		if(this.paneSplitterHandles['east'] && this.panesAssociative['east'].paneModel.state=='expanded')sizeCenterPane-=(this.verticalSplitterSize);
		
		this.panesAssociative['center'].__setWidth(sizeCenterPane);
		
		
		// Position east pane
		var posEastPane = posLeftCenterPane + this.panesAssociative['center'].divElement.offsetWidth;
		if(this.paneSplitterHandles['east'])posEastPane+=(this.verticalSplitterSize);
		if(this.panesAssociative['east']){
			if(this.panesAssociative['east'].paneModel.state=='expanded')this.panesAssociative['east'].__setLeftPosition(posEastPane);
			this.panesAssociative['east'].divElementCollapsed.style.left = (posEastPane - this.verticalSplitterSize) + 'px';
		}
		// Set height of middle panes
		var heightMiddleFrames = browserHeight;
		if(this.panesAssociative['north'] && this.panesAssociative['north'].paneModel.visible){
			if(this.panesAssociative['north'].paneModel.state=='expanded'){
				heightMiddleFrames-= this.panesAssociative['north'].divElement.offsetHeight;	
				if(this.paneSplitterHandles['north'])heightMiddleFrames-=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
			}else
				heightMiddleFrames-= this.paneSizeCollapsed;
			
		}
		if(this.panesAssociative['south'] && this.panesAssociative['south'].paneModel.visible){
			if(this.panesAssociative['south'].paneModel.state=='expanded'){
				heightMiddleFrames-=this.panesAssociative['south'].divElement.offsetHeight;
				if(!this.paneSplitterHandles['south'])heightMiddleFrames+=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
			}else
				heightMiddleFrames-=this.paneSizeCollapsed;
		}
		
		if(this.panesAssociative['center'])this.panesAssociative['center'].__setHeight(heightMiddleFrames);
		if(this.panesAssociative['west'])this.panesAssociative['west'].__setHeight(heightMiddleFrames);
		if(this.panesAssociative['east'])this.panesAssociative['east'].__setHeight(heightMiddleFrames);		
		
		// Position south pane
		var posSouth = 0;
		if(this.panesAssociative['north']){	/* Step 1 - get height of north pane */
			if(this.panesAssociative['north'].paneModel.state=='expanded'){
				posSouth = this.panesAssociative['north'].divElement.offsetHeight;	
			}else
				posSouth = this.paneSizeCollapsed;
		}
			
		posSouth += heightMiddleFrames;			

		if(this.paneSplitterHandles['south'] && this.panesAssociative['south'].paneModel.state=='expanded'){
			posSouth+=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
		}		
		
		if(this.panesAssociative['south']){
			this.panesAssociative['south'].__setTopPosition(posSouth);
			this.panesAssociative['south'].divElementCollapsed.style.top = posSouth + 'px';
			this.panesAssociative['south'].__setWidth('100%');
		}
		try{
			if(this.panesAssociative['west']){
				this.panesAssociative['west'].divElementCollapsed.style.height = (heightMiddleFrames) + 'px';
				this.panesAssociative['west'].divElementCollapsedInner.style.height = (heightMiddleFrames -5) + 'px';
			}
		}catch(e){
		
		}
		if(this.panesAssociative['east']){
			this.panesAssociative['east'].divElementCollapsed.style.height = heightMiddleFrames + 'px';
			this.panesAssociative['east'].divElementCollapsedInner.style.height = (heightMiddleFrames - 5) + 'px';
		}
		if(this.panesAssociative['south']){
			this.panesAssociative['south'].divElementCollapsed.style.width = browserWidth + 'px';
			this.panesAssociative['south'].divElementCollapsedInner.style.width = (browserWidth - 4) + 'px';
			
			if(this.panesAssociative['south'].paneModel.state=='collapsed' && this.panesAssociative['south'].divElementCollapsed.offsetHeight){	// Increasing the size of the southern pane
				
				var rest = browserHeight -  this.panesAssociative['south'].divElementCollapsed.style.top.replace('px','')/1 - this.panesAssociative['south'].divElementCollapsed.offsetHeight;

				if(rest>0)this.panesAssociative['south'].divElementCollapsed.style.height = (this.panesAssociative['south'].divElementCollapsed.offsetHeight + rest) + 'px';
			}
			
		}
		
		if(this.panesAssociative['north']){
			this.panesAssociative['north'].divElementCollapsed.style.width = browserWidth + 'px';
			this.panesAssociative['north'].divElementCollapsedInner.style.width = (browserWidth - 4) + 'px';
		}
		
	
		
		
		this.__positionResizeHandles();
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__positionResizeHandles()',50);	// To get the tabs positioned correctly.
	}
	// }}}
	,
	// {{{ __autoSlideInPanes()
    /**
     *	Automatically slide in panes .
     *
     *
     * @private	
     */		
	__autoSlideInPanes : function(e)
	{
		if(document.all)e = event;
		if(this.panesAssociative['south'])this.panesAssociative['south'].__autoSlideInPane(e);	
		if(this.panesAssociative['west'])this.panesAssociative['west'].__autoSlideInPane(e);	
		if(this.panesAssociative['north'])this.panesAssociative['north'].__autoSlideInPane(e);	
		if(this.panesAssociative['east'])this.panesAssociative['east'].__autoSlideInPane(e);	
		
	}
	// }}}
	,	
	// {{{ __addEvents()
    /**
     *	Add basic events for the paneSplitter widget
     *
     *
     * @private	
     */		
	__addEvents : function()
	{
		var ind = this.objectIndex;
		DHTMLSuite.commonObj.addEvent(window,'resize',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__positionPanes(); });
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endResize(); });
		DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__resizePane(e); });		
		DHTMLSuite.commonObj.addEvent(document.documentElement,'click',function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__autoSlideInPanes(e); });	
		document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
		DHTMLSuite.commonObj.__addEventElement(window);
	}
}