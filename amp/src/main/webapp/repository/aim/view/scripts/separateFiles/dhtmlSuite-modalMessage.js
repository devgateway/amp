if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML modal dialog box
*
*	Created:						August, 26th, 2006
*	@class Purpose of class:		Display a modal dialog box on the screen.
*			
*	Css files used by this script:	modal-message.css
*
*	Demos of this class:			demo-modal-message-1.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Display a modal DHTML message on the page. All other page controls will be disabled until the message is closed(<a href="../../demos/demo-modal-message-1.html" target="_blank">demo</a>).
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.modalMessage = function()
{
	var url;								// url of modal message
	var htmlOfModalMessage;					// html of modal message
	
	var divs_transparentDiv;				// Transparent div covering page content
	var divs_content;						// Modal message div.
	var layoutCss;							// Name of css file;
	var width;								// Width of message box
	var height;								// Height of message box
	
	var existingBodyOverFlowStyle;			// Existing body overflow css
	var dynContentObj;						// Reference to dynamic content object
	var cssClassOfMessageBox;				// Alternative css class of message box - in case you want a different appearance on one of them
	var shadowDivVisible;					// Shadow div visible ? 
	var shadowOffset; 						// X and Y offset of shadow(pixels from content box)
	
	var objectIndex;
	
		
	this.url = '';							// Default url is blank
	this.htmlOfModalMessage = '';			// Default message is blank
	this.layoutCss = 'modal-message.css';	// Default CSS file
	this.height = 200;						// Default height of modal message
	this.width = 400;						// Default width of modal message
	this.cssClassOfMessageBox = false;		// Default alternative css class for the message box
	this.shadowDivVisible = true;			// Shadow div is visible by default
	this.shadowOffset = 5;					// Default shadow offset.
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}

	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
	var ind = this.objectIndex;
	
	var variableStorage = DHTMLSuite.variableStorage;
	
	DHTMLSuite.commonObj.addEvent(window,"resize",function() {
		if(variableStorage != undefined){
			variableStorage.arrayOfDhtmlSuiteObjects[ind].__resizeTransparentDiv();
		}
	});	
	
	
}

DHTMLSuite.modalMessage.prototype = {
	// {{{ setSource(urlOfSource)
    /**
     *	Set source of the modal dialog box
     * 	
     *
     * @public	
     */		
	setSource : function(urlOfSource)
	{
		this.url = urlOfSource;
		
	}	
	// }}}	
	,
	// {{{ setHtmlContent(newHtmlContent)
    /**
     *	Setting static HTML content for the modal dialog box.
     * 	
     *	@param String newHtmlContent = Static HTML content of box
     *
     * @public	
     */		
	setHtmlContent : function(newHtmlContent)
	{
		this.htmlOfModalMessage = newHtmlContent;
		
	}
	// }}}		
	,
	// {{{ setSize(width,height)
    /**
     *	Set the size of the modal dialog box
     * 	
     *	@param int width = width of box
     *	@param int height = height of box
     *
     * @public	
     */		
	setSize : function(width,height)
	{
		if(width)this.width = width;
		if(height)this.height = height;		
	}
	// }}}		
	,		
	// {{{ setCssClassMessageBox(newCssClass)
    /**
     *	Assign the message box to a new css class.(in case you wants a different appearance on one of them)
     * 	
     *	@param String newCssClass = Name of new css class (Pass false if you want to change back to default)
     *
     * @public	
     */		
	setCssClassMessageBox : function(newCssClass)
	{
		this.cssClassOfMessageBox = newCssClass;
		if(this.divs_content){
			if(this.cssClassOfMessageBox)
				this.divs_content.className=this.cssClassOfMessageBox;
			else
				this.divs_content.className='modalDialog_contentDiv';	
		}
					
	}
	// }}}		
	,	
	// {{{ setShadowOffset(newShadowOffset)
    /**
     *	Specify the size of shadow
     * 	
     *	@param Int newShadowOffset = Offset of shadow div(in pixels from message box - x and y)
     *
     * @public	
     */		
	setShadowOffset : function(newShadowOffset)
	{
		this.shadowOffset = newShadowOffset
					
	}
	// }}}		
	,	
	// {{{ setWaitMessage(newMessage)
    /**
     *	Set a wait message when Ajax is busy inserting content
     * 	
     *	@param String newMessage = New wait message
     *
     * @public	
     */		
	setWaitMessage : function(newMessage)
	{
		if(!this.dynContentObj){
			try{
				this.dynContentObj = new DHTMLSuite.dynamicContent();	// Creating dynamic content object if it doesn't already exist.
			}catch(e){
				alert('You need to include dhtmlSuite-dynamicContent.js');
			}
		}	
		this.dynContentObj.setWaitMessage(newMessage);	// Calling the DHTMLSuite.dynamicContent setWaitMessage
	}
	// }}}		
	,	
	// {{{ setWaitImage(newImage)
    /**
     *	Set a wait Image when Ajax is busy inserting content
     * 	
     *	@param String newImage = New wait Image
     *
     * @public	
     */		
	setWaitImage : function(newImage)
	{
		if(!this.dynContentObj){
			try{
				this.dynContentObj = new DHTMLSuite.dynamicContent();	// Creating dynamic content object if it doesn't already exist.
			}catch(e){
				alert('You need to include dhtmlSuite-dynamicContent.js');
			}
		}	
		this.dynContentObj.setWaitImage(newImage);	// Calling the DHTMLSuite.dynamicContent setWaitImage
	}
	// }}}		
	,	
	// {{{ setCache()
    /**
     *	Enable or disable cache for the ajax object
     * 	
     *	@param Boolean cacheStatus = false = off, true = on
     *
     * @public	
     */		
	setCache : function(cacheStatus)
	{
		if(!this.dynContentObj){
			try{
				this.dynContentObj = new DHTMLSuite.dynamicContent();	// Creating dynamic content object if it doesn't already exist.
			}catch(e){
				alert('You need to include dhtmlSuite-dynamicContent.js');				
			}
		}	
		this.dynContentObj.setCache(cacheStatus);	// Calling the DHTMLSuite_dynamicContent setCache
		
	}
	// }}}		
	,
	// {{{ display()
    /**
     *	Display the modal dialog box
     * 	
     *
     * @public	
     */		
	display : function()
	{
		if(!this.divs_transparentDiv){
			DHTMLSuite.commonObj.loadCSS(this.layoutCss);
			this.__createDivElements();
		}	
		
		// Redisplaying divs
		this.divs_transparentDiv.style.display='block';
		this.divs_content.style.display='block';
		this.divs_shadow.style.display='block';		
			
		this.__resizeAndPositionDivElements();
		
		/* Call the __resizeAndPositionDivElements method twice in case the css file has changed. The first execution of this method may not catch these changes */
		window.refToThisModalBoxObj = this;		
		setTimeout('window.refToThisModalBoxObj.__resizeAndPositionDivElements()',50);
		
		this.__addHTMLContent();	// Calling method which inserts content into the message div.
	}
	// }}}		
	,
	// {{{ ()
    /**
     *	Display the modal dialog box
     * 	
     *
     * @public	
     */		
	setShadowDivVisible : function(visible)
	{
		this.shadowDivVisible = visible;
	}
	// }}}	
	,
	// {{{ close()
    /**
     *	Close the modal dialog box
     * 	
     *
     * @public	
     */		
	close : function()
	{
		document.documentElement.style.overflow = '';	// Setting the CSS overflow attribute of the <html> tag back to default.
		/* Hiding divs */
		this.divs_transparentDiv.style.display='none';
		this.divs_content.style.display='none';
		this.divs_shadow.style.display='none';
		
	}
	// }}}	
	,
	// {{{ __createDivElements()
    /**
     *	Create the divs for the modal dialog box
     * 	
     *
     * @private	
     */		
	__createDivElements : function()
	{
		// Creating transparent div
		this.divs_transparentDiv = document.createElement('DIV');
		this.divs_transparentDiv.className='DHTMLSuite_modalDialog_transparentDivs';
		this.divs_transparentDiv.style.left = '0px';
		this.divs_transparentDiv.style.top = '0px';
		document.body.appendChild(this.divs_transparentDiv);
		// Creating content div
		this.divs_content = document.createElement('DIV');
		this.divs_content.className = 'DHTMLSuite_modalDialog_contentDiv';
		this.divs_content.id = 'DHTMLSuite_modalBox_contentDiv';
		document.body.appendChild(this.divs_content);
		// Creating shadow div
		this.divs_shadow = document.createElement('DIV');
		this.divs_shadow.className = 'DHTMLSuite_modalDialog_contentDiv_shadow';
		document.body.appendChild(this.divs_shadow);

	}
	// }}}	
	,
	// {{{ __resizeAndPositionDivElements()
    /**
     *	Resize the message divs
     * 	
     *
     * @private	
     */	
    __resizeAndPositionDivElements : function()
    {
    	var topOffset = Math.max(document.body.scrollTop,document.documentElement.scrollTop);
		if(this.cssClassOfMessageBox)
			this.divs_content.className=this.cssClassOfMessageBox;
		else
			this.divs_content.className='DHTMLSuite_modalDialog_contentDiv';	
    	if(!this.divs_transparentDiv)return;
    	document.documentElement.style.overflow = 'hidden';
    	
    	var bodyWidth = document.documentElement.clientWidth;
    	var bodyHeight = document.documentElement.clientHeight;
    	// Setting width and height of content div
      	this.divs_content.style.width = this.width + 'px';
    	this.divs_content.style.height= this.height + 'px';  	
    	
    	// Creating temporary width variables since the actual width of the content div could be larger than this.width and this.height(i.e. padding and border)
    	var tmpWidth = this.divs_content.offsetWidth;	
    	var tmpHeight = this.divs_content.offsetHeight;
    	
		
		
    	this.divs_content.style.left = 300 + 'px';
    	this.divs_content.style.top =  (topOffset + 100) + 'px';
    	this.divs_shadow.style.left = (this.divs_content.style.left.replace('px','')/1 + this.shadowOffset) + 'px';
    	this.divs_shadow.style.top = (this.divs_content.style.top.replace('px','')/1 + this.shadowOffset) + 'px';
    	this.divs_shadow.style.height = tmpHeight + 'px';
    	this.divs_shadow.style.width = tmpWidth + 'px';
    	if(!this.shadowDivVisible)this.divs_shadow.style.display='none';	// Hiding shadow if it has been disabled
    	this.__resizeTransparentDiv();
    	
    }	
    // }}}
    ,
   	// {{{ __resizeTransparentDiv()
    /**
     *	Resize transparent div
     * 	
     *
     * @private	
     */	 
    __resizeTransparentDiv : function()
    {
    	if(!this.divs_transparentDiv)return;
    	var divHeight = Math.max(document.documentElement.offsetHeight,document.body.offsetHeight);
    	var divWidth = Math.max(document.documentElement.offsetWidth,document.body.offsetWidth);
    	this.divs_transparentDiv.style.height = divHeight +'px';   
    	this.divs_transparentDiv.style.width = divWidth + 'px';   	
    	
    }
	// }}}	
	,
	// {{{ __addHTMLContent()
    /**
     *	Insert content into the content div
     * 	
     *
     * @private	
     */	
    __addHTMLContent : function()
    {
		if(!this.dynContentObj){// dynamic content object doesn't exists?
			try{
				this.dynContentObj = new DHTMLSuite.dynamicContent();	// Create new DHTMLSuite_dynamicContent object.
			}catch(e){
				alert('You need to include dhtmlSuite-dynamicContent.js');
			}
		}
		if(this.url){	// url specified - load content dynamically
			this.dynContentObj.loadContent('DHTMLSuite_modalBox_contentDiv',this.url);
		}else{	// no url set, put static content inside the message box
			this.divs_content.innerHTML = this.htmlOfModalMessage;	
		}
    }		
}