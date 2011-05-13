if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML window scripts
*
*	Created:						November, 26th, 2006
*	@class Purpose of class:		Store metadata about a window
*			
*	Css files used by this script:	
*
*	Demos of this class:			demo-window.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Save metadata about a window. (<a href="../../demos/demo-window.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.windowModel = function(arrayOfProperties)
{
	var id;							// Id of window.
	var title;						// Title of window
	var icon;						// Icon of window
	var isDragable;					// Window is dragable ? (default = true)
	var isResizable;				// Window is resizable ?
	var isMinimizable;				// Window is minimizable ?
	var isClosable;					// Window is closable
	var xPos;						// Current x position of window
	var yPos;						// Current y position of window
	var width;						// Current width of window
	var height;						// Current height of window	
	var cookieName;					// Name of cookie to store x,y,width,height,state,activeTab and zIndex
	var state;						// State of current window(minimized,closed etc.)
	var activeTabId;				// id of active tab
	var tabsVisible;				// Tabs are visible? If not, we will only show a simple window with content and no tabs.
	var zIndex;						// Current z-index of window.
	var minWidth;					// Minimum width of window
	var maxWidth;					// Maximum width of window
	var minHeight;					// Minimum height of window
	var maxHeight;					// Maximum height of window.
	
	var windowContents;				// Array of DHTMLSuite.windowTabModel objects.	
	this.windowContents = new Array();
	this.isDragable = true;
	this.isMinimizable = true;
	this.isResizable = true;
	this.isClosable = true;
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file\n'+ e.message);
	}
		
	if(arrayOfProperties)this.__setInitialWindowProperties(arrayOfProperties);
	
}

DHTMLSuite.windowModel.prototype = {

	// {{{ createWindowModelFromMarkUp()
    /**
     *	Create windows from markup
     *
     *	@param Object - reference to HTML element on page. (id or a direct reference)
     *
     *	Example of markup:
	&lt;div id="myWindow2" windowProperties="title:This is my window,resizable:true,closable:true,maxWidth:900,dragable:true,cookieName:myWindowCookie2,xPos:300,yPos:400,minWidth:405,minHeight:150,activeTabId:OtherWindow1"><BR>
		&lt;div id="OtherWindow1" class="DHTMLSuite_windowContent" tabProperties="tabTitle:Welcome"><BR>
			This is my first window.	<BR>
		&lt;/div><BR>
		&lt;div id="OtherWindow2" class="DHTMLSuite_windowContent" tabProperties="contentUrl:includes/pane-splitter-calendar-1.php,tabTitle:Calendar,active=1"><BR>
		&lt;/div><BR>
		&lt;div id="OtherWindow3" class="DHTMLSuite_windowContent" tabProperties="contentUrl:includes/pane-splitter-paypal.inc,tabTitle:Support us"><BR>
		&lt;/div><BR>
		&lt;div id="OtherWindow4" class="DHTMLSuite_windowContent" tabProperties="contentUrl:includes/pane-splitter-classes.inc,tabTitle:DHTMLSuite classes"><BR>
		&lt;/div><BR>
	&lt;/div>
     *
     *	
     * @public	
     */			
	createWindowModelFromMarkUp : function(referenceToHTMLElement)
	{
		if(typeof referenceToHTMLElement == 'string'){
			referenceToHTMLElement = document.getElementById(referenceToHTMLElement);
		}		
		if(!referenceToHTMLElement){
			alert('Error in windowModel class - Could not get a reference to element ' + referenceToHTMLElement);
			return;
		}		
		
		this.id = referenceToHTMLElement.id;
		var properties = referenceToHTMLElement.getAttribute('windowProperties');
		if(!properties)properties = referenceToHTMLElement.windowProperties;
		this.__setInitialWindowProperties(DHTMLSuite.commonObj.getAssociativeArrayFromString(properties));
		
				
		/* Parse properties of main div */		
		
		var subDivs = referenceToHTMLElement.getElementsByTagName('DIV');
		for(var no=0;no<subDivs.length;no++){
			if(subDivs[no].className.toLowerCase()=='dhtmlsuite_windowcontent'){	/* Window found */
				var index = this.windowContents.length;
				this.windowContents[index] = new DHTMLSuite.windowTabModel();
				this.windowContents[index].__createContentModelFromHTMLElement(subDivs[no]);			
			}				
		}		
	}
	// }}}
	,	
	// {{{ __setInitialWindowProperties()
    /**
     *Save initial window properties sent to the constructor
     *	
     *	@param Array arrayOfProperties - Array of window properties
     *								 
     * @private	
     */		
	__setInitialWindowProperties : function(arrayOfProperties)
	{
		if(arrayOfProperties.cookieName)this.cookieName=arrayOfProperties.cookieName;
		if(arrayOfProperties.title)this.title=arrayOfProperties.title;
		if(arrayOfProperties.icon)this.icon=arrayOfProperties.icon;
		if(arrayOfProperties.width)this.width=arrayOfProperties.width;
		if(arrayOfProperties.height)this.height=arrayOfProperties.width;
		if(arrayOfProperties.isResizable)this.isResizable=arrayOfProperties.isResizable;
		if(arrayOfProperties.isMinimizable)this.isMinimizable=arrayOfProperties.isMinimizable;
		if(arrayOfProperties.isClosable)this.isClosable=arrayOfProperties.isClosable;
		if(arrayOfProperties.state)this.state=arrayOfProperties.state;
		if(arrayOfProperties.xPos)this.xPos=arrayOfProperties.xPos;
		if(arrayOfProperties.yPos)this.xPos=arrayOfProperties.yPos;
		if(arrayOfProperties.activeTabId)this.activeTabId=arrayOfProperties.activeTabId;
		if(arrayOfProperties.minWidth)this.minWidth=arrayOfProperties.minWidth;
		if(arrayOfProperties.maxWidth)this.maxWidth=arrayOfProperties.maxWidth;
		if(arrayOfProperties.minHeight)this.minHeight=arrayOfProperties.minHeight;
		if(arrayOfProperties.maxHeight)this.maxHeight=arrayOfProperties.maxHeight;
	}
	// }}}
	,
	// {{{ __getTitle()
    /**
     * Return title of window
     *								 
     * @private	
     */	
	__getTitle : function()
	{
		return this.title;
	}
	// }}}
	,
	// {{{ __getContentObjects()
    /**
     * Return an array of window content objects.
     *								 
     * @private	
     */		
	__getContentObjects : function()
	{
		return this.windowContents;
	}
	// }}}
	,
	// {{{ __setActiveTabIdAutomatically()
    /**
     * Automatically set active tab id.
     *								 
     * @private	
     */		
	__setActiveTabIdAutomatically : function()
	{
		for(var no=0;no<this.windowContents.length;no++){
			if(!this.windowContents[no].isDeleted){
				this.activeTabId = this.windowContents[no].id;
				return;
			}		
		}		
	}
	// }}}
	,
	// {{{ __setContentUrl()
    /**
     * Set new url of content
     *								 
     * @private	
     */		
	__setContentUrl : function(contentId,url)
	{
		for(var no=0;no<this.windowContents.length;no++){
			if(this.windowContents[no].id==contentId){
				this.windowContents[no].__setContentUrl(url);
				return true;
			}
		}
		return false;	
	}
	// }}}
	,
	// {{{ __getContentObjectById()
    /**
     * Return content object from id.
     *								 
     * @private	
     */		
	__getContentObjectById : function(contentId)
	{
		for(var no=0;no<this.windowContents.length;no++){
			if(this.windowContents[no].id==contentId)return this.windowContents[no];
		}
		return false;			
	}
	// }}}
	,
	// {{{ __setWidth()
    /**
     * Set new width of window
     *	@param Integer width.
     *								 
     * @private	
     */		
	__setWidth : function(newWidth)
	{
		if(this.minWidth && newWidth/1<this.minWidth/1)newWidth = this.minWidth;
		if(this.maxWidth && newWidth/1>this.maxWidth/1)newWidth = this.maxWidth;
		this.width = newWidth;
	}
	// }}}
	,
	// {{{ __setHeight()
    /**
     * Set new height of window
     *	@param Integer height.
     *								 
     * @private	
     */		
	__setHeight : function(newHeight)
	{
		if(this.minHeight && newHeight/1<this.minHeight/1)newHeight = this.minHeight;
		if(this.maxHeight && newHeight/1>this.maxHeight/1)newHeight = this.maxHeight;
		this.height = newHeight;
	}
	// }}}
	,
	// {{{ __setXPos()
    /**
     * Set new x position of window
     *	@param Integer xPos in pixels.
     *								 
     * @private	
     */		
	__setXPos : function(newXPos)
	{
		if(newXPos>DHTMLSuite.clientInfoObj.getBrowserWidth()){
			newXPos = DHTMLSuite.clientInfoObj.getBrowserWidth()-30;	
		}		
		this.xPos = newXPos;
	}
	// }}}
	,
	// {{{ __setYPos()
    /**
     * Set new y position of window
     *	@param Integer yPos in pixels.
     *								 
     * @private	
     */		
	__setYPos : function(newYPos)
	{
		if(newYPos>DHTMLSuite.clientInfoObj.getBrowserHeight()){
			newYPos = DHTMLSuite.clientInfoObj.getBrowserHeight()-30;	
		}			
		this.yPos = newYPos;
	}
	// }}}
	,
	// {{{ __setActiveTabId()
    /**
     * Set new active tab id.
     *	@param String newActiveTabId
     *								 
     * @private	
     */			
	__setActiveTabId : function(newActiveTabId)
	{
		var index = this.__getIndexOfTabById(newActiveTabId);
		if(index!==false && !this.__getIsDeleted(newActiveTabId)){			
			this.activeTabId = newActiveTabId;
			return;
		}
		this.__setActiveTabIdAutomatically();		
	}
	// }}}
	,
	// {{{ __setZIndex()
    /**
     * Set new z-index of window
     *	@param Integer zIndex
     *								 
     * @private	
     */		
	__setZIndex : function(zIndex)
	{
		this.zIndex = zIndex;
	}
	// }}}
	,
	// {{{ __setState()
    /**
     * Set new state of window
     *	@param String state
     *								 
     * @private	
     */		
	__setState : function(state)
	{
		this.state = state;
	}
	// }}}
	,
	// {{{ __getWidth()
    /**
     * Return width of window
     *								 
     * @private	
     */		
	__getWidth : function()
	{
		return this.width;
	}
	// }}}
	,
	// {{{ __getWidth()
    /**
     * Return width of window
     *								 
     * @private	
     */		
	__getHeight : function()
	{
		return this.height;
	}
	// }}}
	,
	// {{{ __getXPos()
    /**
     * Return xPos of window
     *								 
     * @private	
     */		
	__getXPos : function()
	{
		if(this.xPos>DHTMLSuite.clientInfoObj.getBrowserWidth()){
			xPos = DHTMLSuite.clientInfoObj.getBrowserWidth()-30;	
		}
		return this.xPos;
	}
	// }}}
	,
	// {{{ __getYPos()
    /**
     * Return yPos of window
     *								 
     * @private	
     */		
	__getYPos : function()
	{
		return this.yPos;
	}
	// }}}
	,
	// {{{ __getActiveTabId()
    /**
     * Return active tab id
     *								 
     * @private	
     */		
	__getActiveTabId : function()
	{
		return this.activeTabId;
	}
	// }}}
	,
	// {{{ __getZIndex()
    /**
     * Return z-index of window
     *								 
     * @private	
     */		
	__getZIndex : function()
	{
		return this.zIndex;
	}
	// }}}
	,
	// {{{ __getState()
    /**
     * Return state of window
     *								 
     * @private	
     */		
	__getState : function()
	{
		return this.state;
	}
	// }}}
	,
	// {{{ __deleteTab()
    /**
     * delete a tab
     *	@param String id of tab to delete
     *								 
     * @private	
     */		
	__deleteTab : function(idOfTab)
	{
		var index = this.__getIndexOfTabById(idOfTab);
		if(index!==false){
			this.windowContents[index].__setDeleted(true);
			return true;
		}
		return false;		
	}
	// }}}
	,
	// {{{ __restoreTab()
    /**
     * Restore a deleted tab
     *	@param String id of tab to restore.
     *								 
     * @private	
     */		
	__restoreTab : function(idOfTab)
	{
		var index = this.__getIndexOfTabById(idOfTab);
		if(index!==false){
			this.windowContents[index].__setDeleted(false);
			return true;
		}
		return false;	
	}
	// }}}
	,
	// {{{ __getIndexOfTabById()
    /**
     * Return index of tab by id
     *	@param String idOfTab
     *								 
     * @private	
     */		
	__getIndexOfTabById : function(idOfTab)
	{
		for(var no=0;no<this.windowContents.length;no++){
			if(this.windowContents[no].id==idOfTab)return no;
		}		
		return false;
	}
	// }}}
	,
	// {{{ __getIsDeleted()
    /**
     * Return true if tab is flagged as deleted
     *								 
     * @private	
     */		
	__getIsDeleted : function(idOfTab)
	{
		var index = this.__getIndexOfTabById(idOfTab);
		if(index!==false){
			return this.windowContents[index].isDeleted;
		}
	}
	// }}}
	,
	// {{{ __addTab()
    /**
     * Add a new tab to the model
     *
     *	@return A reference to the model or false if a tab with the specified id already exists.
     *								 
     * @private	
     */		
	__addTab : function(properties)
	{
		for(var no=0;no<this.windowContents.length;no++){	// Check if the window already exists.
			if(this.windowContents[no].id==properties.id)return false;			
		}
		var newIndex = this.windowContents.length;
		this.windowContents[newIndex] = new DHTMLSuite.windowTabModel(properties);
		return this.windowContents[newIndex];
	}
}

/************************************************************************************************************
*	DHTML window scripts
*
*	Created:						November, 26th, 2006
*	@class Purpose of class:		Store metadata about the content of a window or only a tab of a window.
*									THIS WIDGET IS NOT YET FINISHED.
*			
*	Css files used by this script:	
*
*	Demos of this class:			demo-window.html
*
* 	Update log:
*
************************************************************************************************************/

DHTMLSuite.windowTabModel = function(tabProperties)
{
	var tabTitle;
	var textContent;
	var id;	
	var htmlElementId;
	var contentUrl;
	var isDeleted;
	
	if(tabProperties)this.__setInitialTabProperties(tabProperties);
}

DHTMLSuite.windowTabModel.prototype = 
{
	// {{{ __createContentModelFromHTMLElement()
    /**
     * Parse HTML element and get attributes from it.
     *								 
     * @private	
     */			
	__createContentModelFromHTMLElement : function(elementReference)
	{
		if(typeof elementReference == 'string'){
			elementReference = document.getElementById(elementReference);
		}			
		this.textContent = elementReference.innerHTML;
		var properties = elementReference.getAttribute('tabProperties');
		if(!properties)properties = referenceToHTMLElement.tabProperties;		
		this.id = elementReference.id;
		this.htmlElementId = elementReference.id;
		this.__setInitialTabProperties(DHTMLSuite.commonObj.getAssociativeArrayFromString(properties));	
		
	}	
	// }}}
	,
	// {{{ __setInitialTabProperties()
    /**
     * Set initial tab properties
     *								 
     * @private	
     */		
	__setInitialTabProperties : function(arrayOfProperties)
	{		
		if(arrayOfProperties.tabTitle)this.tabTitle = arrayOfProperties.tabTitle;	
		if(arrayOfProperties.contentUrl)this.contentUrl = arrayOfProperties.contentUrl;	
		if(arrayOfProperties.id)this.id = arrayOfProperties.id;
		if(arrayOfProperties.textContent)this.textContent = arrayOfProperties.textContent;
		if(arrayOfProperties.htmlElementId)this.htmlElementId = arrayOfProperties.htmlElementId;
		if(arrayOfProperties.isDeleted)this.htmlElementId = arrayOfProperties.isDeleted;
				
	}
	// }}}
	,
	// {{{ __setContentUrl()
    /**
     * Set url of tab
     *	@param String url
     *								 
     * @private	
     */		
	__setContentUrl : function(url)
	{
		this.contentUrl = url;
	}
	// }}}
	,
	// {{{ __setDeleted()
    /**
     * Specify if tab is deleted or not
     *	@param Boolean isDeleted
     *								 
     * @private	
     */		
	__setDeleted : function(isDeleted)
	{
		this.isDeleted = isDeleted;
	}
	
}

/**
* @constructor
* @class Create a window widget.
*			
* @param Object windowModel - object of class DHTMLSuite.windowModel
* @version				1.0
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/

DHTMLSuite.windowWidget = function(windowModel)
{
	var windowModel;	
	var layoutCSS;
	var objectIndex;
	
	var divElement;					// parent element of divElementInner and eventual iframe(old MSIE)
	var divElementInner;			// Div element for all the content of an iframe
	var divTitleBar;				// Div element for the title bar.
	var divElementContent;			// Div for the content 
	var divCloseButton;				// Close button
	var divMinimizeButton;			// Minimize button
	var divStatusBarTxt;			// Text element in status bar
	var divResizeHandle;			// Div element for the resize handle
	var iframeElement;				// Iframe element used to cover select boxes in IE.
	var divElementTitle_txt;
	var referenceToDragDropObject;	// Reference to object of class dragDropSimple.
	var contentDivs;				// Array of div elements for the content
	var resizeObj;					// Reference to object of class DHTMLSuite.resize
	var slideSpeed;					// Slide speed when window is "sliding" into position by a windowCollection call.
	
	this.layoutCSS = 'window.css';
	this.contentDivs = new Array();	
	this.slideSpeed = 25;
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}	
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;

	if(windowModel)this.addWindowModel(windowModel);
	
	
}

DHTMLSuite.windowWidget.prototype = {
	
	// {{{ addWindowModel()
    /**
     * Connect window widget with a DHTMLSuite.windowModel
     *	@param Object windowModel - Object of class DHTMLSuite.windowModel.
     *								 
     * @public	
     */		
	addWindowModel : function(windowModel)
	{
		this.windowModel = windowModel;
	}
	// }}}
	,
	// {{{ setLayoutCss()
    /**
     * Specify new css file for the window. default is window/window.css ( located inside the css_dhtmlsuite folder)
     * @public	
     */		
	setLayoutCss : function(cssFileName)
	{
		this.layoutCSS = cssFileName;
	}
	// }}}
	,
	// {{{ setStatusBarText()
    /**
     * Specify a text in the status bar
     *	@param String statusbar text
     * @public	
     */		
	setStatusBarText : function(text)
	{
		this.divStatusBarTxt.innerHTML = text;
	}
	// }}}
	,
	// {{{ setSlideSpeed()
    /**
     * Specify a text in the status bar
     *	@param Integer - slidespeed ( default = 25 ) - This property is only used when a windowCollection is cascading or tiling windows.
     * @public	
     */		
	setSlideSpeed : function(slideSpeed)
	{
		this.slideSpeed = slideSpeed;
	}
	// }}}
	,
	// {{{ init()
    /**
     * Initializes the widget.
	 *
     * @public	
     */			
	init : function()
	{
		var ind = this.objectIndex;
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		this.__getWindowPropertiesFromCookie();
		if(!this.windowModel.activeTabId)this.windowModel.__setActiveTabIdAutomatically();
		this.__createPrimaryDivElements();
		this.__createIframeElement();
		this.__createTitleBar();
		this.__createTabRow();
		this.__createContentArea();
		this.__createStatusBar();
		
		this.__initiallyPopulateContentArea();
		this.__displayActiveContent();
		
		this.__populateTabRow();		
		this.__populateTitleBar();
		
		
		this.__showHideButtonElements();
		this.__makeWindowDragable();
		this.__makeWindowResizable();
		this.__initiallySetPositionAndSizeOfWindow();
		setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" +ind + "].__setSizeOfDivElements()",100);
	}
	// }}}
	,
	// {{{ loadContent()
    /**
     * The purpose of this method is to load content into a tab	 
     *
	 *	@param String - id of content object/tab
	 *	@param url - Url to load into the tab.
     * @public	
     */			
	loadContent : function(idOfWindowContentObject,url)
	{
		this.windowModel.__setContentUrl(idOfWindowContentObject,url);
		var dynContent = new DHTMLSuite.dynamicContent();
		var ref = this.windowModel.__getContentObjectById(idOfWindowContentObject);		
		if(ref)dynContent.loadContent(ref.htmlElementId,url);
	}
	// }}}
	,
	/* Activate a different tab */
	// {{{ activateTab()
    /**
     * Make a specific tab active
     *
	 *	@param String - id of content object/tab
     * @public	
     */		
	activateTab : function(idOfContent)
	{
		this.windowModel.__setActiveTabId(idOfContent);
		this.__setLayoutOfTabs();
		this.__displayActiveContent();
		this.__saveCookie();		
	}	
	// }}}
	,
	// {{{ deleteTab()
    /**
     * Delete a tab (ps! The tab can be restored by the restoreTab() method
     *
	 *	@param String - id of content object/tab
     * @public	
     */		
	deleteTab : function(idOfTab)
	{
		this.windowModel.__deleteTab(idOfTab);	
		if(this.windowModel.__getActiveTabId()==idOfTab)this.windowModel.__setActiveTabIdAutomatically();	
		this.__populateTabRow();
		this.__setLayoutOfTabs();
		this.__displayActiveContent();		
	}
	// }}}
	,
	// {{{ restoreTab()
    /**
     * Restore a deleted tab 
     *
	 *	@param String idOfTab - id of content object/tab
     * @public	
     */		
	restoreTab : function(idOfTab)
	{
		this.windowModel.__restoreTab(idOfTab);	
		this.__populateTabRow();
		this.__setLayoutOfTabs();
		this.__displayActiveContent();					
	}
	// }}}
	,
	// {{{ addTab()
    /**
     * Adds a new tab to the window
     *
	 *	@param Array tabProperties - Properties for the new tab(associative array with the keys: id,tabTitle,textContent(optional),htmlElementId(optional) and isDeleted(optional)
     * @public	
     */		
	addTab : function(tabProperties)
	{
		var contentObj = this.windowModel.__addTab(tabProperties);
		if(contentObj){
			this.__createContentForATab(contentObj);
			this.__populateTabRow();
			this.__setLayoutOfTabs();
			this.__displayActiveContent();	
		}	
		
	}
	// }}}
	,
	// {{{ setWidthOfWindow()
    /**
     * Set new width of window
     *
	 *	@param Integer width - New width of window. NB! Not larger than eventual set maxWidth
     * @public	
     */		
	setWidthOfWindow : function(newWidth)
	{
		this.windowModel.__setWidth(newWidth);
		this.divElement.style.width = this.windowModel.__getWidth() + 'px';
		this.__updateWindowModel();				
	}
	// }}}
	,
	// {{{ setHeightOfWindow()
    /**
     * Set new height of window
     *
	 *	@param Integer height - New height of window. NB! Not larger than eventual set maxHeight
     * @public	
     */		
	setHeightOfWindow : function(newHeight)
	{
		this.windowModel.__setHeight(newHeight);
		this.divElement.style.height = this.windowModel.__getHeight() + 'px';
		this.__setSizeOfDivElements();
		this.__updateWindowModel();		
	}
	// }}}
	,	
	// {{{ __createPrimaryDivElements()
    /**
     * Create main div elements for the widget
     *
     * @private	
     */		
	__createPrimaryDivElements : function()
	{
		this.divElement = document.createElement('DIV');
		this.divElement.className = 'DHTMLSuite_window';
		document.body.appendChild(this.divElement);		

		this.divElementInner = document.createElement('DIV');
		this.divElementInner.className = 'DHTMLSuite_windowInnerDiv';
		this.divElementInner.style.zIndex = 5;
		this.divElement.appendChild(this.divElementInner);
	}
	// }}}
	,
	// {{{ __createIframeElement()
    /**
     * Create iframe element for the widget - used to cover select boxes in IE.
     *
     * @private	
     */		
	__createIframeElement : function()
	{
		if(DHTMLSuite.clientInfoObj.isMSIE){
			this.iframeElement = document.createElement('<IFRAME src="about:blank" frameborder=0>');
			this.iframeElement.style.position = 'absolute';
			this.iframeElement.style.top = '0px';
			this.iframeElement.style.left = '0px';
			this.iframeElement.style.width = '105%';
			this.iframeElement.style.height = '105%';
			this.iframeElement.style.zIndex = 1;
			this.iframeElement.style.visibility = 'visible';		
			this.divElement.appendChild(this.iframeElement);
		}		
	}
	// }}}
	,
	// {{{ __createTitleBar()
    /**
     * Create title bar element
     *
     * @private	
     */	
	__createTitleBar : function()
	{
		var ind = this.objectIndex;
		
		this.divTitleBar = document.createElement('DIV');	// Creating title bar div
		this.divTitleBar.className = 'DHTMLSuite_windowTitleBar';
		this.divElementInner.appendChild(this.divTitleBar);
		
		var buttonDiv = document.createElement('DIV');		// Creating parent div for buttons
		buttonDiv.className = 'DHTMLSuite_windowButtonDiv';
		this.divTitleBar.appendChild(buttonDiv);
		
		
		this.divCloseButton = document.createElement('DIV');		// Creating close button
		this.divCloseButton.onmouseover = this.__mouseoverButton;
		this.divCloseButton.onmouseout = this.__mouseoutButton;
		this.divCloseButton.className='DHTMLSuite_windowCloseButton';
		this.divCloseButton.onclick = function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].deleteWindow(); }
		buttonDiv.appendChild(this.divCloseButton);
		DHTMLSuite.commonObj.__addEventElement(this.divCloseButton);	

		this.divMinimizeButton = document.createElement('DIV');		// Creating minimize button
		this.divMinimizeButton.onmouseover = this.__mouseoverButton;
		this.divMinimizeButton.onmouseout = this.__mouseoutButton;
		this.divMinimizeButton.className='DHTMLSuite_windowMinimizeButton';
		this.divMinimizeButton.onclick = function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].minimizeWindow(); }
		buttonDiv.appendChild(this.divMinimizeButton);
		DHTMLSuite.commonObj.__addEventElement(this.divMinimizeButton);	

		this.divMaximizeButton = document.createElement('DIV');		// Creating maximize button
		this.divMaximizeButton.onmouseover = this.__mouseoverButton;
		this.divMaximizeButton.onmouseout = this.__mouseoutButton;
		this.divMaximizeButton.className='DHTMLSuite_windowMaximizeButton';
		this.divMaximizeButton.onclick = function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].maximizeWindow(); }
		buttonDiv.appendChild(this.divMaximizeButton);
		this.divMaximizeButton.style.display='none';
		DHTMLSuite.commonObj.__addEventElement(this.divMaximizeButton);	
			
		this.divElementTitle_txt = document.createElement('DIV');		// Creating div element holding the title text
		this.divElementTitle_txt.className = 'DHTMLSuite_windowTitleInTitleBar';
		this.divTitleBar.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
		this.divTitleBar.appendChild(this.divElementTitle_txt);	
		DHTMLSuite.commonObj.__addEventElement(this.divTitleBar);		
	}
	// }}}
	,
	// {{{ __createTabRow()
    /**
     * Create tab row
     *
     * @private	
     */	
	__createTabRow : function()
	{
		this.divElementTabRow = document.createElement('DIV');
		this.divElementTabRow.className = 'DHTMLSuite_windowTabRow';
		this.divElementInner.appendChild(this.divElementTabRow);
		
	}
	// }}}
	,
	// {{{ __createContentArea()
    /**
     * Create content area
     *
     * @private	
     */	
	__createContentArea : function()
	{
		this.divElementContent = document.createElement('DIV');
		this.divElementContent.className = 'DHTMLSuite_windowContent';
		this.divElementInner.appendChild(this.divElementContent);		
	}
	// }}}
	,
	// {{{ __createStatusBar()
    /**
     * Create status bar div
     *
     * @private	
     */		
	__createStatusBar : function()
	{
		this.divStatusBar = document.createElement('DIV');
		this.divStatusBar.className = 'DHTMLSuite_windowStatusBar';
		this.divElementInner.appendChild(this.divStatusBar);	
		
		this.divResizeHandle = document.createElement('DIV');
		this.divResizeHandle.className = 'DHTMLSuite_windowResizeHandle';
		this.divStatusBar.appendChild(this.divResizeHandle);
		
		this.divStatusBarTxt = document.createElement('DIV');
		this.divStatusBarTxt.className = 'DHTMLSuite_windowStatusBarText';
		this.divStatusBar.appendChild(this.divStatusBarTxt);		
		
	}
	// }}}
	,
	// {{{ __populateTitleBar()
    /**
     * Populate title bar
     *
     * @private	
     */		
	__populateTitleBar : function()
	{
		this.divElementTitle_txt.innerHTML = this.windowModel.__getTitle();	
	}
	// }}}
	,
	// {{{ __populateTitleBar()
    /**
     * The purpose of this method is to initally populate content area with tab contents
     *
     * @private	
     */		
	__initiallyPopulateContentArea : function()
	{
		var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
		for(var no=0;no<contentObjects.length;no++){
			this.__createContentForATab(contentObjects[no]);
		}		
	}
	// }}}
	,
	// {{{ __createContentForATab()
    /**
     * Create or move div for a new tab.
     *
     *	@param Object contentObject - object of class windowTabModel
     *
     * @private	
     */		
	__createContentForATab : function(contentObject)
	{
		if(contentObject.htmlElementId){
			if(document.getElementById(contentObject.htmlElementId)){
				this.contentDivs[contentObject.id] = document.getElementById(contentObject.htmlElementId);										
			}else{
				this.contentDivs[contentObject.id] = document.createElement('DIV');
				this.contentDivs[contentObject.id].id = contentObject.htmlElementId;
			}	
			this.divElementContent.appendChild(this.contentDivs[contentObject.id]);			
		}	
		if(contentObject.contentUrl){
			this.loadContent(contentObject.id,contentObject.contentUrl);
		}			
		this.contentDivs[contentObject.id].className = 'DHTMLSuite_windowContentInner';
		this.contentDivs[contentObject.id].style.display='none';		
		
	}
	// }}}
	,
	// {{{ __populateTitleBar()
    /**
     * The purpose of this method is to display active content div
     *
     * @private	
     */		
	__displayActiveContent : function()
	{
		var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
		for(var no=0;no<contentObjects.length;no++){
			if(contentObjects[no].id==this.windowModel.activeTabId)this.contentDivs[contentObjects[no].id].style.display='block'; else this.contentDivs[contentObjects[no].id].style.display='none';			
		}			
	}
	// }}}
	,
	// {{{ __populateTabRow()
    /**
     * The purpose of this method is to create the clickable tabs in the tab row. Finally, it calls the __setLayoutOfTabs which takes care of the styling based on active and inactive tabs
     *
     * @private	
     */		
	__populateTabRow : function()
	{
		var ind = this.objectIndex;
		
		this.divElementTabRow.innerHTML = '';	// Clear existing content from the tab row	
		var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
		
		if(DHTMLSuite.clientInfoObj.isMSIE){
			var table = document.createElement('<TABLE cellpadding="0" cellspacing="0" border="0">');
		}else{
			var table = document.createElement('TABLE');
			table.setAttribute('cellpadding',0);
			table.setAttribute('cellspacing',0);
			table.setAttribute('border',0);
		}
		
		this.divElementTabRow.appendChild(table);
		var row = table.insertRow(0);
		for(var no=0;no<contentObjects.length;no++){
			if(!this.windowModel.__getIsDeleted(contentObjects[no].id)){
				var cell = row.insertCell(-1);
				cell.className = 'DHTMLSuite_windowATab';
				cell.id = 'windowTab_' + contentObjects[no].id;
				cell.setAttribute('contentId',contentObjects[no].id);
				cell.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__activateTabByClick(e); };
				DHTMLSuite.commonObj.__addEventElement(cell);	
				var innerDiv = document.createElement('DIV');
				innerDiv.className = 'DHTMLSuite_windowATabInnerDiv';
				innerDiv.innerHTML = contentObjects[no].tabTitle;
				cell.appendChild(innerDiv);
			}
		}
		
		this.__setLayoutOfTabs();
	}	
	// }}}
	,
	// {{{ __clearActiveAndInactiveStylingFromTabs()
    /**
     * The purpose of this method is to clear added css classes from active and inactive tabs.
     *
     * @private	
     */	
	__clearActiveAndInactiveStylingFromTabs : function()
	{
		var cells = this.divElementTabRow.getElementsByTagName('TD');	// Get an array of <td> elements.
		var divs = this.divElementTabRow.getElementsByTagName('DIV');	// Get an array of <td> elements.
		for(var no=0;no<cells.length;no++){
			cells[no].className = cells[no].className.replace('DHTMLSuite_windowActiveTabCell','');
			cells[no].className = cells[no].className.replace('DHTMLSuite_windowInactiveTabCell','');		
			cells[no].style.left = '0px';	
		}		
		for(var no=0;no<divs.length;no++){
			divs[no].className = divs[no].className.replace(' DHTMLSuite_windowActiveTabCellContent','');
			divs[no].className = divs[no].className.replace(' DHTMLSuite_windowInactiveTabCellContent','');			
		}		
		
	}
	// }}}
	,
	// {{{ __clearActiveAndInactiveStylingFromTabs()
    /**
     * The purpose of this method is to set layout of active and inactive tabs in the tab row and also adjust the left position of each tab so that they overlap each other nicely
     *
     * @private	
     */		
	__setLayoutOfTabs : function()
	{
		this.__clearActiveAndInactiveStylingFromTabs();	// Clear layout of tab
		
		var cells = this.divElementTabRow.getElementsByTagName('TD');	// Get an array of <td> elements.
		var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
		
		var activeTabIndex = 0;
		
		for(var no=0;no<cells.length;no++){
			if(cells[no].id=='windowTab_' + this.windowModel.activeTabId){
				activeTabIndex = no;
				break;
			}			
		}		
		var leftPadding = 0;
		if(activeTabIndex>0){
			leftPadding = -7;	
		}
		for(var no=1;no<activeTabIndex;no++){
			cells[no].style.left = leftPadding +'px';
			cells[no].style.zIndex = 100-no;
			leftPadding-=7;			
		}
		
		for(var no=activeTabIndex;no<cells.length;no++){
			cells[no].style.left = leftPadding +'px';
			cells[no].style.zIndex = 100-no;
			leftPadding-=7;
		}
		
		cells[activeTabIndex].style.zIndex=200;

		
		for(var no=0;no<cells.length;no++){
			var div = cells[no].getElementsByTagName('DIV')[0];
			if(no==activeTabIndex){
				cells[no].className = cells[no].className + ' DHTMLSuite_windowActiveTabCell';
				div.className = div.className + ' DHTMLSuite_windowActiveTabCellContent';
			}else{
				cells[no].className = cells[no].className + ' DHTMLSuite_windowInactiveTabCell';	
				div.className = div.className + ' DHTMLSuite_windowInactiveTabCellContent';
			}
		}		
	}
	// }}}
	,
	// {{{ __setSizeOfDivElements()
    /**
     * Set size of content area relative to window size.
     *
     * @private	
     */		
	__setSizeOfDivElements : function()
	{
		try{
			this.divElementContent.style.height = (this.divElementInner.offsetHeight - (this.divTitleBar.offsetHeight + this.divStatusBar.offsetHeight + this.divElementTabRow.offsetHeight + 8)) + 'px';
		}catch(e){
			this.divElementContent.style.height = '1px';
		}
		
		try{
			if(this.windowModel.__getState()=='minimized')this.divElement.style.height = (this.divTitleBar.offsetHeight + this.divElementTabRow.offsetHeight + this.divStatusBar.offsetHeight) + 'px';
		}catch(e)
		{
		}
	}
	// }}}
	,
	// {{{ __activateTabByClick()
    /**
     * Click on tab
     *
     * @private	
     */		
	__activateTabByClick : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);	
		if(src.tagName.toLowerCase()=='div')src = src.parentNode;
		var idOfContent = src.getAttribute('contentId');
		this.activateTab(idOfContent);
		
	}
	// }}}
	,
	// {{{ __updateWindowModel()
    /**
     * Update window model after resize, drag etc.
     *
     * @private	
     */		
	__updateWindowModel : function()
	{
		this.windowModel.__setWidth(this.divElement.style.width.replace('px','')/1);
		if(this.windowModel.__getState()!='minimized')this.windowModel.__setHeight(this.divElement.style.height.replace('px','')/1);
		this.windowModel.__setXPos(this.divElement.style.left.replace('px','')/1);
		this.windowModel.__setYPos(this.divElement.style.top.replace('px','')/1);	
		this.windowModel.__setZIndex(this.divElement.style.zIndex);	
		this.__saveCookie();
	}
	// }}}
	,	
	// {{{ __saveCookie()
    /**
     * The purpose of this function is to save cookies 
     *
     * @private	
     */		
	__saveCookie : function()
	{
		if(!this.windowModel.cookieName)return;
		var cookieValue = 'width:' + this.windowModel.__getWidth();
		cookieValue+=',height:' + this.windowModel.__getHeight();
		
		cookieValue+=',xPos:' + this.windowModel.__getXPos();
		cookieValue+=',yPos:' + this.windowModel.__getYPos();
		cookieValue+=',zIndex:' + this.divElement.style.zIndex;
		cookieValue+=',activeTabId:' + this.windowModel.__getActiveTabId();
		cookieValue+=',state:' + this.windowModel.__getState();
		DHTMLSuite.commonObj.setCookie(this.windowModel.cookieName,cookieValue,500);	
	}
	// }}}
	,
	// {{{ __getWindowPropertiesFromCookie()
    /**
     * Get window properties from cookie
     *
     * @private	
     */		
	__getWindowPropertiesFromCookie : function()
	{
		if(!this.windowModel.cookieName)return;	
		var cookieValue = DHTMLSuite.commonObj.getCookie(this.windowModel.cookieName);
		var propertyArray = DHTMLSuite.commonObj.getAssociativeArrayFromString(cookieValue);
		if(!propertyArray)return;
		if(propertyArray.width)this.windowModel.__setWidth(propertyArray.width);
		if(propertyArray.height)this.windowModel.__setHeight(propertyArray.height);
		if(propertyArray.xPos)this.windowModel.__setXPos(propertyArray.xPos);
		if(propertyArray.yPos)this.windowModel.__setYPos(propertyArray.yPos);
		if(propertyArray.zIndex)this.windowModel.__setZIndex(propertyArray.zIndex);
		if(propertyArray.state)this.windowModel.__setState(propertyArray.state);
		if(propertyArray.activeTabId)this.windowModel.__setActiveTabId(propertyArray.activeTabId);
	}
	// }}}
	,
	// {{{ __initiallySetPositionAndSizeOfWindow()
    /**
     * Set initial size and position of window.
     *
     * @private	
     */		
	__initiallySetPositionAndSizeOfWindow : function()
	{
		this.divElement.style.position='absolute';
			
		var width = this.windowModel.__getWidth();
		var height = this.windowModel.__getHeight();
		var xPos = this.windowModel.__getXPos();
		var yPos = this.windowModel.__getYPos();
		var zIndex = this.windowModel.__getZIndex();
		var state = this.windowModel.__getState();
		if(width && width!='0')this.divElement.style.width = width + 'px';
		if(height && height!='0')this.divElement.style.height = height + 'px';
		if(xPos)this.divElement.style.left = xPos + 'px';
		if(yPos)this.divElement.style.top = yPos + 'px';
		if(zIndex)this.divElement.style.zIndex = zIndex;
		if(state && state=='minimized')this.minimizeWindow();
		
	}
	// }}}
	,
	// {{{ __initiallySetPositionAndSizeOfWindow()
    /**
     * The purpose of this method is to figure out which tab was clicked an call the deleteTab method with the id of that tab as only argument
     *
     * @private	
     */		
	__deleteTabByClick : function()
	{		
		// NOT YET IMPLEMENTED
	}	
	// }}}
	,
	// {{{ __makeWindowResizable()
    /**
     * The purpose of this method is to make the window resizable
     *
     * @private	
     */		
	__makeWindowResizable : function()
	{
		if(!this.windowModel.isResizable)return;
		var ind = this.objectIndex;
		this.resizeObj = new DHTMLSuite.resize({ minWidth:this.windowModel.minWidth,minHeight:this.windowModel.minHeight,maxWidth:this.windowModel.maxWidth,maxHeight:this.windowModel.maxHeight } );
		this.resizeObj.setElementRoResize(this.divElement);
		this.resizeObj.addResizeHandle(this.divResizeHandle,'southeast');
		this.resizeObj.setCallbackOnBeforeResize('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__isOkToResize');
		this.resizeObj.setCallbackOnAfterResize('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__updateWindowModel');
		this.resizeObj.setCallbackOnDuringResize('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__setSizeOfDivElements');
		this.resizeObj.init();		
		
		this.divStatusBarTxt.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
		this.divStatusBar.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
		
		DHTMLSuite.commonObj.__addEventElement(this.divStatusBarTxt);
		DHTMLSuite.commonObj.__addEventElement(this.divStatusBar);
		
	}
	// }}}
	,
	// {{{ __isOkToResize()
    /**
     * The purpose of this method is to return true or false if it's ok to resize the window(true if it's maximized, false if it's minimized)
     *
     * @private	
     */		
	__isOkToResize : function()
	{
		if(this.windowModel.__getState()=='minimized')return false;
		return true;
	}
	// }}}
	,
	// {{{ __makeWindowDragable()
    /**
     * Make window dragable if that option is true
     *
     * @private	
     */			
	__makeWindowDragable : function()
	{
		var ind = this.objectIndex;
		if(this.windowModel.isDragable){	// Add drag and drop features to the window.
			this.referenceToDragDropObject = new DHTMLSuite.dragDropSimple(this.divElement,false,0,0,false);	
			this.referenceToDragDropObject.setCallbackOnAfterDrag('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__updateWindowModel');			
			this.referenceToDragDropObject.setCallbackOnBeforeDrag('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__updateWindowModel');			
			this.referenceToDragDropObject.addDragHandle(this.divTitleBar);
			this.divTitleBar.style.cursor = 'move';
			this.referenceToDragDropObject.__setNewCurrentZIndex(this.windowModel.__getZIndex());					
		}					
	}
	// }}}		
	,
	// {{{ deleteWindow()
    /**
     * Delete window, i.e. hide it
     *
     * @public	
     */		
	deleteWindow : function()
	{
		this.divElement.style.display='none';
	}
	// }}}
	,
	// {{{ restoreWindow()
    /**
     * Restore a deleted window, i.e. show it.
     *
     * @public	
     */		
	restoreWindow : function()
	{
		this.divElement.style.display='block';
	}
	// }}}
	,
	// {{{ minimizeWindow()
    /**
     * Minimize a window
     *
     * @public	
     */		
	minimizeWindow : function()
	{
		this.windowModel.__setState('minimized');
		
		this.divElementContent.style.display='none';
		this.divStatusBar.style.display='none';
		this.divElementTabRow.style.display='none';
		
		this.divMinimizeButton.style.display='none';
		this.divMaximizeButton.style.display='block';
		this.__setSizeOfDivElements();
		this.__saveCookie();
	}
	// }}}
	,
	// {{{ maximizeWindow()
    /**
     * maximize a window
     *
     * @public	
     */		
	maximizeWindow : function()
	{
		this.windowModel.__setState('maximized');
		this.divElementContent.style.display='block';
		this.divStatusBar.style.display='block';
		this.divElementTabRow.style.display='block';		
		this.divMinimizeButton.style.display='block';
		this.divMaximizeButton.style.display='none';
		this.divElement.style.height = this.windowModel.__getHeight() + 'px';
		this.__setSizeOfDivElements();
		this.__saveCookie();
	}
	// }}}
	,
	// {{{ slideWindowToXAndY()
    /**
     * Slide a window to a specific coordinate
     *
     *	Int toX - Slide to (x-coordinate in pixels)
     *	Int toY - Slide to (y-coordinate in pixels)
     *
     * @public	
     */			
	slideWindowToXAndY : function(toX,toY)
	{
		var slideFactors = this.__getSlideFactors(toX,toY);
		var slideDirections = this.__getSlideDirections(toX,toY);
		var slideTo = new Array();
		slideTo.x = toX;
		slideTo.y = toY;
		var currentPos = new Array();
		currentPos.x = this.windowModel.__getXPos();
		currentPos.y = this.windowModel.__getYPos();
		if(currentPos.x==slideTo.x && currentPos.y==slideTo.y)return;
		this.__performSlide(slideTo,currentPos,slideFactors,slideDirections);				
	}
	// }}}
	,
	// {{{ __performSlide()
    /**
     * Execute slide process
     *
     * @private	
     */		
	__performSlide : function(slideTo,currentPos,slideFactors,slideDirections)
	{
		var ind = this.objectIndex;
		currentPos.x = currentPos.x/1 + (this.slideSpeed * slideFactors.y * slideDirections.x);
		currentPos.y = currentPos.y/1 + (this.slideSpeed * slideFactors.x * slideDirections.y);
		
		repeatSlide = false;
		
		if(slideDirections.x<0){
			if(currentPos.x<=slideTo.x){
				currentPos.x = slideTo.x;
			}else{
				repeatSlide=true;
			}
		}else{
			if(currentPos.x>=slideTo.x){
				currentPos.x = slideTo.x;
			}else{
				repeatSlide=true;
			}
		}
		if(slideDirections.y<0){
			if(currentPos.y<=slideTo.y){
				currentPos.y = slideTo.y;
			}else{
				repeatSlide=true;
			}
		}else{
			if(currentPos.y>=slideTo.y){
				currentPos.y = slideTo.y;
			}else{
				repeatSlide=true;
			}
		}
		
		this.divElement.style.left = Math.round(currentPos.x) + 'px';
		this.divElement.style.top = Math.round(currentPos.y) + 'px';

		//alert('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__performSlide({x:' + slideTo.x + ',y:' + slideTo.y + '},{x:' + currentPos.x + ',y:' + currentPos.y + '},{x:' + slideFactors + ',y:' + slideFactors.y + '},{ x:' + slideDirections.x + ',y:' + slideDirections.y + '})');
		if(repeatSlide){
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__performSlide({x:' + slideTo.x + ',y:' + slideTo.y + '},{x:' + currentPos.x + ',y:' + currentPos.y + '},{x:' + slideFactors.x + ',y:' + slideFactors.y + '},{ x:' + slideDirections.x + ',y:' + slideDirections.y + '})',10); 
		}else{
			this.__updateWindowModel();	
		}
	}
	// }}}
	,
	// {{{ __getSlideFactors()
    /**
     * Return slide factor (x relative to y)
     *
     * @private	
     */		
	__getSlideFactors : function(toX,toY)
	{
		var retArray = new Array();
		
		var currentX = this.windowModel.__getXPos();
		var currentY = this.windowModel.__getYPos();
		
		var distance_x = Math.abs(toX-currentX);
		var distance_y = Math.abs(toY-currentY);
		
		if(distance_x<distance_y){
			retArray.x = distance_y/distance_x;
			retArray.y = 1;			
		}else{
			retArray.y = distance_x/distance_y;
			retArray.x = 1;					
		}
		return retArray;		
	}
	// }}}
	,
	// {{{ __getSlideDirections()
    /**
     * Return slide directions for the x and y axis( returns an associative array for x and y. value will be -1 or 1)
     *
     * @private	
     */		
	__getSlideDirections : function(toX,toY)
	{
		var retArray = new Array();
		if(toX<this.windowModel.__getXPos())retArray.x = -1; else retArray.x = 1;
		if(toY<this.windowModel.__getYPos())retArray.y = -1; else retArray.y = 1;
		return retArray;
	}
	// }}}
	,
	// {{{ __showHideButtonElements()
    /**
     * Show or hide button elements
     *
     * @private	
     */		
	__showHideButtonElements : function()
	{
		if(this.windowModel.isClosable)this.divCloseButton.style.display='block'; else this.divCloseButton.style.display='none';	
		
	}	
	// }}}
	,
	// {{{ __mouseoverButton()
    /**
     * Mouse over effect - buttons
     *
     * @private	
     */			
	__mouseoverButton : function()
	{
		this.className = this.className + ' DHTMLSuite_windowButtonOver';	
	}
	// }}}
	,
	// {{{ __mouseoutButton()
    /**
     * Mouse out effect - buttons
     *
     * @private	
     */		
	__mouseoutButton : function()
	{
		this.className = this.className.replace(' DHTMLSuite_windowButtonOver','');	
	}
	
}

DHTMLSuite.windowCollection = function()
{
	var windowWidgets;	
	var spaceBetweenEachWindowWhenCascaded;
	var numberOfColumnsWhenTiled;
	var divWindowsArea;							// Eventual div where the windows should be positioned
	this.windowWidgets = new Array();
	this.spaceBetweenEachWindowWhenCascaded = 20;
	this.numberOfColumnsWhenTiled = 2;
	
}

DHTMLSuite.windowCollection.prototype = 
{
	// {{{ addWindow()
    /**
     * Add a window to a window collection
     *	@param Object wnidowWidgetReference - object of class DHTMLSuite.windowWidget
     *
     * @public	
     */			
	addWindow : function(windowWidgetReference)	
	{
		this.windowWidgets[this.windowWidgets.length] = windowWidgetReference;
		
	}
	// }}}
	,
	// {{{ tile()
    /**
     * Tile all windows in collection.
     *
     * @public	
     */		
	tile : function()
	{
		this.windowWidgets = this.windowWidgets.sort(this.__sortItems);
		var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth()-20;
		var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight()-20;
		
		var offsetX = 10;
		var offsetY = 10;
		
		if(this.divWindowsArea){
			browserWidth = this.divWindowsArea.clientWidth;
			browserHeight = this.divWindowsArea .clientHeight;
			offsetX = DHTMLSuite.commonObj.getLeftPos(this.divWindowsArea);
			offsetY = DHTMLSuite.commonObj.getTopPos(this.divWindowsArea);
		}
				
		var windowWidth = Math.floor(browserWidth/this.numberOfColumnsWhenTiled);
		var windowHeight = Math.floor(browserHeight / Math.ceil(this.windowWidgets.length / this.numberOfColumnsWhenTiled));
		

		for(var no=0;no<this.windowWidgets.length;no++){
			this.windowWidgets[no].setWidthOfWindow(windowWidth);
			this.windowWidgets[no].setHeightOfWindow(windowHeight-5);
			var xPos = offsetX + (windowWidth * (no % this.numberOfColumnsWhenTiled));
			var yPos = offsetY + (windowHeight * Math.floor((no) / this.numberOfColumnsWhenTiled));
			this.windowWidgets[no].slideWindowToXAndY(xPos,yPos);
			
		}			
	}
	// }}}
	,
	// {{{ cascade()
    /**
     * Cascade all windows in collection.
     *
     * @public	
     */	
	cascade : function()
	{
		this.windowWidgets = this.windowWidgets.sort(this.__sortItems);
		var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth() - 50;
		var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight() - 50;
		
		var offsetX = 10;
		var offsetY = 10;		
		if(this.divWindowsArea){
			browserWidth = this.divWindowsArea.clientWidth;
			browserHeight = this.divWindowsArea .clientHeight;
			offsetX = DHTMLSuite.commonObj.getLeftPos(this.divWindowsArea);
			offsetY = DHTMLSuite.commonObj.getTopPos(this.divWindowsArea);
		}
		
		var windowWidth = browserWidth - ((this.windowWidgets.length-1) * this.spaceBetweenEachWindowWhenCascaded);
		var windowHeight = browserHeight - ((this.windowWidgets.length-1) * this.spaceBetweenEachWindowWhenCascaded);
		
		
		for(var no=0;no<this.windowWidgets.length;no++){
			this.windowWidgets[no].setWidthOfWindow(windowWidth);
			this.windowWidgets[no].setHeightOfWindow(windowHeight);
			this.windowWidgets[no].slideWindowToXAndY(offsetX + this.spaceBetweenEachWindowWhenCascaded*(no),offsetY + this.spaceBetweenEachWindowWhenCascaded*(no));
		}		
	}
	// }}}
	,
	// {{{ setNumberOfColumnsWhenTiled()
    /**
     * Number of columns in "tile" mode
     *	@param Integer - number of columns ( default = 2 )
     *
     * @public	
     */		
	setNumberOfColumnsWhenTiled : function(numberOfColumnsWhenTiled)
	{
		this.numberOfColumnsWhenTiled = numberOfColumnsWhenTiled;
	}
	// }}}
	,
	// {{{ setDivWindowsArea()
    /**
     * Specify a HTML element where windows will be tiled or cascaded on, i.e. The windows will not go outside the boundaries of these elements when they are being cascaded or tiled.
     *	@param Object divWindowsArea - Id or direct reference to HTML element on your page.
     *
     * @public	
     */		
	setDivWindowsArea : function(divWindowsArea)
	{
		if(typeof divWindowsArea == 'string'){
			divWindowsArea = document.getElementById(divWindowsArea);
		}		
		this.divWindowsArea = divWindowsArea;
	}
	// }}}
	,
	// {{{ __sortItems()
    /**
     *	Private sort method used to sort window widgets.
     *	
     * @private	
     */		
	__sortItems : function(a,b)
	{
		return a.windowModel.__getZIndex()-b.windowModel.__getZIndex();
	}	
	
	
}