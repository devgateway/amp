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

DHTMLSuite.windowDataModel = function()
{
	var title;						// Title of window
	var icon;						// Icon of window
	var resizable;					// Window is resizable ?
	var minimizable;				// Window is minimizable ?
	var closable;					// Window is closable
	var xPos;						// Current x position of window
	var yPos;						// Current y position of window
	var width;						// Current width of window
	var height;						// Current height of window
	var zIndex;						// Current z-index of window.
	var cookieName;					// Name of cookie to store x,y,width,height,state,activeTab and zIndex
	var state;						// State of current window(minimized,closed etc.)
	var activeTabId;				// id of active tab
	var tabsVisible;				// Tabs are visible? If not, we will only show a simple window with content and no tabs.
	
	var windowDataModelContent;			// Array of DHTMLSuite.windowDataContent objects.
	
	this.windowDataModelContent = new Array();
	
}

DHTMLSuite.windowDataModel.prototype = {
	
	// {{{ setTitle()
    /**
     *	Specify title of window
     *
     *  @param String newTitle - New title of window content(a tab).
     *
     * @public	
     */			
	setTitle : function(newTitle)
	{
		this.title = newTitle;		
	}
	// }}}
	,
	// {{{ setIcon()
	/**
	*
	*	Specify path to window icon
	*
	*	@param String newIcon - Path to new icon
	*
	*	@public
	*/
	setIcon : function(newIcon)
	{
		this.icon = newIcon;
	}
	// }}}
	,
	// {{{ setResizable()
	/**
	*
	*	Specify if the window should be resizable or not
	*
	*	@param Boolean resizable - true or false, true if the window is resizable, false otherwise.
	*
	*	@public
	*/	
	setResizable : function(resizable)
	{
		this.resizable = resizable;
	}
	// }}}
	,
	// {{{ setMinimizable()
	/**
	*
	*	Specify if the window should be resizable or not
	*
	*	@param Boolean resizable - true or false, true if the window is minimizable, false otherwise.
	*
	*	@public
	*/		
	setMinimizable : function(minimizable)
	{
		this.minimizable = minimizable;		
	}
	// }}}
	,
	// {{{ setClosable()
	/**
	*
	*	Specify if you should be able to close the window(close icon) or not
	*
	*	@param Boolean resizable - Specify if you should be able to close the window(close icon) or not
	*
	*	@public
	*/		
	setClosable : function(closable)
	{
		this.closable = closable;
	}
	// }}}
	,
	// {{{ setCookieName()
	/**
	*
	*	Specify name of cookie for this window(i.e where to store variables such as x and y pos, width and height, state and z-index
	*
	*	@param String cookieName - New cookie name
	*
	*	@public
	*/		
	setCookieName : function(cookieName)
	{
		this.cookieName = cookieName;
	}
	// }}}
	,
	// {{{ setTabTitles()
	/**
	*
	*	Specify title of window tabs. Remember that the tab objects has to be added before you call this method. 
	*	
	*	@param Array newTabTitles - Array of strings. the setTitle method of windowDataContent will be called for each tab in this window
	*
	*	@public
	*/
	setTabTitles : function(newTabTitles)
	{
		for(var no=0;no<newTabTitles.length;no++){
			if(this.windowDataContents[no])this.windowDataContents[no].setTitle(newTabTitles[no]);
		}		
	}
	// }}}
	,
	// {{{ setTabIcons()
	/**
	*
	*	Specify path of window tabs icons. Remember that the tab objects has to be added before you call this method. 
	*	
	*	@param Array newTabIcons - Array of strings. the setIcon method of windowDataContent will be called for each tab in this window
	*
	*	@public
	*/
	setTabIcons : function(newTabIcons)
	{
		for(var no=0;no<newTabIcons.length;no++){
			if(this.windowDataContents[no])this.windowDataContents[no].setIcon(newTabIcons[no]);
		}		
	}
	// }}}
	,
	// {{{ createWindowsFromMarkup()
	/**
	*
	*	Create windows from markup. Window data will be set based on markup on your page.
	*	
	*	@param String elementId - ID of parent element of the windows. Remember that window content divs has to have class name "DHTMLSuite_windowContent"
	*
	*	Example of syntax for the markup
	*
	*	<div id="myWindow" title="This is my window" icon="../images/icon.gif">
	*		<div id="tabOne" class="DHTMLSuite_windowContent" title="first tab">
	*			Content of the first tab
	*		
	*		</div>
	*		<div id="tabTwo" class="DHTMLSuite_windowContent" title="second tab">
	*			Content of the second tab
	*		
	*		</div>
	*		<div id="tabThree" class="DHTMLSuite_windowContent" title="third tab">
	*			Content of the third tab.
	*		
	*		</div>
	*	</div>
	*
	*	@public
	*/
	createWindowsFromMarkup : function(elementId)
	{
		var obj = document.getElementById(elementId);
		if(!obj){	// Object exists.
			alert('Object with id ' + elementId + ' does not exists');
			return;
		}
		obj.style.display='none';	// Hiding the content since the window is created dynamically.
		var divs = obj.getElementsByTagName('DIV');
		for(var no=0;no<divs.length;no++){
			if(divs[no].className=='DHTMLSuite_windowContent'){
				var index = this.windowDataModelContent.length;
				this.windowDataModelContent[index] = new DHTMLSuite.windowDataModelContent();
				this.windowDataModelContent[index].setId(divs[no].id);
				
				
			}	
			
		}
		
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

DHTMLSuite.windowDataModelContent = function()
{
	var title;
	var icon;
	var textContent;
	var id;	
	var visible;	
}

DHTMLSuite.windowDataModelContent.prototype = 
{
	// {{{ setTitle()
    /**
     *	Specify title of a tab
     *
     *  @param String newTitle - New title of window content(a tab).
     *
     * @public	
     */		
	setTitle : function(newTitle)
	{
		this.title = newTitle;
	}
	// }}}
	,
	// {{{ setIcon()
	/**
	*	Specify path to window tab icon
	*
	*	@param String newIcon
	*
	*	@public
	*/
	setIcon : function(newIcon)
	{
		this.icon = newIcon;
	}
	// }}}
	,
	// {{{ setId()
	/**
	*	Specify id of window
	*
	*	@param String newId
	*
	*	@public
	*/
	setId : function(newId)
	{
		this.id = newId;
	}
	
	
	
}