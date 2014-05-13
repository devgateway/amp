if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
	@fileoverview
	DHTML Suite for Applications.
	Copyright (C) 2006  Alf Magne Kalleland(post@dhtmlgoodies.com)<br>
	<br>
	This library is free software; you can redistribute it and/or<br>
	modify it under the terms of the GNU Lesser General Public<br>
	License as published by the Free Software Foundation; either<br>
	version 2.1 of the License, or (at your option) any later version.<br>
	<br>
	This library is distributed in the hope that it will be useful,<br>
	but WITHOUT ANY WARRANTY; without even the implied warranty of<br>
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU<br>
	Lesser General Public License for more details.<br>
	<br>
	You should have received a copy of the GNU Lesser General Public<br>
	License along with this library; if not, write to the Free Software<br>
	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA<br>
	<br>
	<br>
	www.dhtmlgoodies.com<br> 
	Alf Magne Kalleland<br>

************************************************************************************************************/


/**
 * 
 * @package DHTMLSuite for applications
 * @copyright Copyright &copy; 2006, www.dhtmlgoodies.com
 * @author Alf Magne Kalleland <post@dhtmlgoodies.com>
 */


/****** 
Some prototypes:
**/

// Creating a trim method
if(!String.trim)String.prototype.trim = function() { return this.replace(/^\s+|\s+$/, ''); };

/************************************************************************************************************
*
* Global variables
*
************************************************************************************************************/

// {{{ DHTMLSuite.createStandardObjects()
/**
 * Create objects used by all scripts
 *
 * @public
 */

var DHTMLSuite = new Object();

var standardObjectsCreated = false;	// The classes below will check this variable, if it is false, default help objects will be created
DHTMLSuite.eventElements = new Array();	// Array of elements that has been assigned to an event handler.

DHTMLSuite.createStandardObjects = function()
{
	DHTMLSuite.clientInfoObj = new DHTMLSuite.clientInfo();	// Create browser info object
	DHTMLSuite.clientInfoObj.init();	
	if(!DHTMLSuite.configObj){	// If this object isn't allready created, create it.
		DHTMLSuite.configObj = new DHTMLSuite.config();	// Create configuration object.
		DHTMLSuite.configObj.init();
	}
	DHTMLSuite.commonObj = new DHTMLSuite.common();	// Create configuration object.
	DHTMLSuite.variableStorage = new DHTMLSuite.globalVariableStorage();;	// Create configuration object.
	DHTMLSuite.commonObj.init();
	DHTMLSuite.domQueryObj = new DHTMLSuite.domQuery();
	window.onunload = function() { 
		if (DHTMLSuite.commonObj == undefined) // Checks if the objects were initiated.
			DHTMLSuite.createStandardObjects(); 
		DHTMLSuite.commonObj.__clearMemoryGarbage(); 
	}
	
	standardObjectsCreated = true;

	
}

    


/************************************************************************************************************
*	Configuration class used by most of the scripts
*
*	Created:			August, 19th, 2006
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Store global variables/configurations used by the classes below. Example: If you want to  
*		 change the path to the images used by the scripts, change it here. An object of this   
*		 class will always be available to the other classes. The name of this object is 
*		"DHTMLSuite.configObj".	<br><br>
*			
*		If you want to create an object of this class manually, remember to name it "DHTMLSuite.configObj"
*		This object should then be created before any other objects. This is nescessary if you want
*		the other objects to use the values you have put into the object. <br>
* @version				1.0
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/
DHTMLSuite.config = function()
{
	var imagePath;	// Path to images used by the classes. 
	var cssPath;	// Path to CSS files used by the DHTML suite.	

	var defaultCssPath;
	var defaultImagePath;
}


DHTMLSuite.config.prototype = {
	// {{{ init()
	/**
	 * 	Initializes the config object - the config class is used to store global properties used by almost all widgets
	 *
	 * @public
	 */
	init : function()
	{
//		this.imagePath = '../images_dhtmlsuite/';	// Path to images		
//		this.cssPath = '../css_dhtmlsuite/';	// Path to images	
		
		this.imagePath = '/repository/aim/view/images/images_dhtmlsuite/';	// Path to images		
		this.cssPath = '/repository/aim/view/css/css_dhtmlsuite/';	// Path to images	
		
		this.defaultCssPath = this.cssPath;
		this.defaultImagePath = this.imagePath;
		//expandAll();
			
	}	
	// }}}
	,
	// {{{ setCssPath()
    /**
     * This method will save a new CSS path, i.e. where the css files of the dhtml suite are located(the folder).
     *
     * @param string newCssPath = New path to css files(folder - remember to have a slash(/) at the end)
     * @public
     */
    	
	setCssPath : function(newCssPath)
	{
		this.cssPath = newCssPath;
	}
	// }}}
	,
	// {{{ resetCssPath()
    /**
     * Resets css path back to default value which is ../css_dhtmlsuite/
     *
     * @public
     */    	
	resetCssPath : function()
	{
		this.cssPath = this.defaultCssPath;
	}
	// }}}
	,
	// {{{ resetImagePath()
    /**
     * Resets css path back to default path which is ../images_dhtmlsuite/
     *
     * @public
     */    	
	resetImagePath : function()
	{
		this.imagePath = this.defaultImagePath;
	}
	// }}}
	,
	// {{{ setImagePath()
    /**
     * This method will save a new image file path, i.e. where the image files used by the dhtml suite ar located
     *
     * @param string newImagePath = New path to image files (remember to have a slash(/) at the end)
     * @public
     */
	setImagePath : function(newImagePath)
	{
		this.imagePath = newImagePath;
	}
	// }}}
}



DHTMLSuite.globalVariableStorage = function()
{
	var menuBar_highlightedItems;	// Array of highlighted menu bar items
	this.menuBar_highlightedItems = new Array();
	
	var arrayOfDhtmlSuiteObjects;	// Array of objects of class menuItem.
	this.arrayOfDhtmlSuiteObjects = new Array();
	
	var ajaxObjects;
	this.ajaxObjects = new Array();
}

DHTMLSuite.globalVariableStorage.prototype = {
	
}


/************************************************************************************************************
*	A class with general methods used by most of the scripts
*
*	Created:			August, 19th, 2006
*	Purpose of class:	A class containing common method used by one or more of the gui classes below, 
* 						example: loadCSS. 
*						An object("DHTMLSuite.commonObj") of this  class will always be available to the other classes. 
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class A class containing common method used by one or more of the gui classes below, example: loadCSS. An object("DHTMLSuite.commonObj") of this  class will always be available to the other classes. 
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/

DHTMLSuite.common = function()
{
	var loadedCSSFiles;	// Array of loaded CSS files. Prevent same CSS file from being loaded twice.
	var cssCacheStatus;	// Css cache status
	var eventElements;
	var isOkToSelect;	// Boolean variable indicating if it's ok to make text selections
	
	this.okToSelect = true;
	this.cssCacheStatus = true;	// Caching of css files = on(Default)
	this.eventElements = new Array();	
}

DHTMLSuite.common.prototype = {
	
	// {{{ init()
    /**
     * This method initializes the DHTMLSuite_common object.
     *	This class contains a lot of useful methods used by most widgets.
     *
     * @public
     */    	
	init : function()
	{
		this.loadedCSSFiles = new Array();
	}	
	// }}}
	,
	// {{{ loadCSS()
    /**
     * This method loads a CSS file(Cascading Style Sheet) dynamically - i.e. an alternative to <link> tag in the document.
     *
     * @param string cssFileName = Name of css file. It will be loaded from the path specified in the DHTMLSuite.common object
     * @public
     */	
	loadCSS : function(cssFileName)
	{
		
		if(!this.loadedCSSFiles[cssFileName]){
			this.loadedCSSFiles[cssFileName] = true;
			var linkTag = document.createElement('LINK');
			if(!this.cssCacheStatus){
				if(cssFileName.indexOf('?')>=0)cssFileName = cssFileName + '&'; else cssFileName = cssFileName + '?';
				cssFileName = cssFileName + 'rand='+ Math.random();	// To prevent caching
			}
			
			linkTag.href = DHTMLSuite.configObj.cssPath + cssFileName;
			linkTag.rel = 'stylesheet';
			linkTag.media = 'screen';
			linkTag.type = 'text/css';
			document.getElementsByTagName('HEAD')[0].appendChild(linkTag);	
			
		}
	}	
	// }}}
	,
	// {{{ getTopPos()
    /**
     * This method will return the top coordinate(pixel) of an HTML element/tag
     *
     * @param Object inputObj = Reference to HTML element
     * @public
     */	
	getTopPos : function(inputObj)
	{		
	  var returnValue = inputObj.offsetTop;
	  while((inputObj = inputObj.offsetParent) != null){
	  	if(inputObj.tagName!='HTML'){
	  		returnValue += (inputObj.offsetTop - inputObj.scrollTop);
	  		if(document.all)returnValue+=inputObj.clientTop;
	  	}
	  } 
	  return returnValue;
	}
	// }}}
	,

	getTopPosCalendar : function(el){
		if(el.getBoundingClientRect){	// IE
			var box = el.getBoundingClientRect();
			return (box.top/1 + Math.max(document.body.scrollTop,document.documentElement.scrollTop));
		}
		if(document.getBoxObjectFor){
			if(el.tagName!='INPUT' && el.tagName!='SELECT' && el.tagName!='TEXTAREA')return document.getBoxObjectFor(el).y
		}

		var returnValue = el.offsetTop;
		while((el = el.offsetParent) != null){
			if(el.tagName!='HTML'){
				returnValue += (el.offsetTop - el.scrollTop);
				if(document.all)returnValue+=el.clientTop;
			}
		} 
		return returnValue;
	}	,
	// {{{ __setOkToMakeTextSelections()
    /**
     * Is it ok to make text selections ?
     *
     * @param Boolean okToSelect 
     * @private
     */		
	__setOkToMakeTextSelections : function(okToSelect){
		this.okToSelect = okToSelect;
	}
	// }}}
	,
	// {{{ __setOkToMakeTextSelections()
    /**
     * Returns true if it's ok to make text selections, false otherwise.
     *
     * @return Boolean okToSelect 
     * @private
     */		
	__getOkToMakeTextSelections : function()
	{
		return this.okToSelect;
	}
	// }}}	
	,	
	// {{{ setCssCacheStatus()
    /**
     * Specify if css files should be cached or not. 
     *
     *	@param Boolean cssCacheStatus = true = cache on, false = cache off
     *
     * @public
     */	
	setCssCacheStatus : function(cssCacheStatus)
	{		
	  this.cssCacheStatus = cssCacheStatus;
	}
	// }}}	
	,
	// {{{ getLeftPos()
    /**
     * This method will return the left coordinate(pixel) of an HTML element
     *
     * @param Object inputObj = Reference to HTML element
     * @public
     */	
	getLeftPos : function(inputObj)
	{	  
	  var returnValue = inputObj.offsetLeft;
	  while((inputObj = inputObj.offsetParent) != null){
	  	if(inputObj.tagName!='HTML'){
	  		returnValue += inputObj.offsetLeft;
	  		if(document.all)returnValue+=inputObj.clientLeft;
	  	}
	  }
	  return returnValue;
	}
	// }}}
	,
	
	// {{{ getCookie()
    /**
     *
     * 	These cookie functions are downloaded from 
	 * 	http://www.mach5.com/support/analyzer/manual/html/General/CookiesJavaScript.htm
	 *
     *  This function returns the value of a cookie
     *
     * @param String name = Name of cookie
     * @param Object inputObj = Reference to HTML element
     * @public
     */	
	getCookie : function(name) { 
	   var start = document.cookie.indexOf(name+"="); 
	   var len = start+name.length+1; 
	   if ((!start) && (name != document.cookie.substring(0,name.length))) return null; 
	   if (start == -1) return null; 
	   var end = document.cookie.indexOf(";",len); 
	   if (end == -1) end = document.cookie.length; 
	   return unescape(document.cookie.substring(len,end)); 
	} 	
	// }}}
	,	
	// {{{ setCookie()
    /**
     *
     * 	These cookie functions are downloaded from 
	 * 	http://www.mach5.com/support/analyzer/manual/html/General/CookiesJavaScript.htm
	 *
     *  This function creates a cookie. (This method has been slighhtly modified)
     *
     * @param String name = Name of cookie
     * @param String value = Value of cookie
     * @param Int expires = Timestamp - days
     * @param String path = Path for cookie (Usually left empty)
     * @param String domain = Cookie domain
     * @param Boolean secure = Secure cookie(SSL)
     * 
     * @public
     */	
	setCookie : function(name,value,expires,path,domain,secure) { 
		expires = expires * 60*60*24*1000;
		var today = new Date();
		var expires_date = new Date( today.getTime() + (expires) );
	    var cookieString = name + "=" +escape(value) + 
	       ( (expires) ? ";expires=" + expires_date.toGMTString() : "") + 
	       ( (path) ? ";path=" + path : "") + 
	       ( (domain) ? ";domain=" + domain : "") + 
	       ( (secure) ? ";secure" : ""); 
	    document.cookie = cookieString; 
	}
	// }}}
	,
	// {{{ deleteCookie()
    /**
	 *
     *  This function deletes a cookie. (This method has been slighhtly modified)
     *
     * @param String name = Name of cookie
     * @param String path = Path for cookie (Usually left empty)
     * @param String domain = Cookie domain
     * 
     * @public
     */	
	deleteCookie : function( name, path, domain ) 
	{
		if ( this.getCookie( name ) ) document.cookie = name + "=" +
		( ( path ) ? ";path=" + path : "") +
		( ( domain ) ? ";domain=" + domain : "" ) +
		";expires=Thu, 01-Jan-1970 00:00:01 GMT";
	}

	// }}}
	,
	// {{{ cancelEvent()
    /**
     *
     *  This function only returns false. It is used to cancel selections and drag
     *
     * 
     * @public
     */	
    	
	cancelEvent : function()
	{
		return false;
	}
	// }}}	
	,
	// {{{ addEvent()
    /**
     *
     *  This function adds an event listener to an element on the page.
     *
     *	@param Object whichObject = Reference to HTML element(Which object to assigne the event)
     *	@param String eventType = Which type of event, example "mousemove" or "mouseup" (NOT "onmousemove")
     *	@param functionName = Name of function to execute. 
     * 
     * @public
     */	
	addEvent : function(whichObject,eventType,functionName,suffix)
	{ 
	  if(!suffix)suffix = '';
	  if(whichObject.attachEvent){ 
	    whichObject['e'+eventType+functionName+suffix] = functionName; 
	    whichObject[eventType+functionName+suffix] = function(){whichObject['e'+eventType+functionName+suffix]( window.event );} 
	    whichObject.attachEvent( 'on'+eventType, whichObject[eventType+functionName+suffix] ); 
	  } else 
	    whichObject.addEventListener(eventType,functionName,false); 	    
	  this.__addEventElement(whichObject);
	} 
	// }}}	
	,	
	// {{{ removeEvent()
    /**
     *
     *  This function removes an event listener from an element on the page.
     *
     *	@param Object whichObject = Reference to HTML element(Which object to assigne the event)
     *	@param String eventType = Which type of event, example "mousemove" or "mouseup"
     *	@param functionName = Name of function to execute. 
     * 
     * @public
     */		
	removeEvent : function(whichObject,eventType,functionName)
	{ 
	  if(whichObject.detachEvent){ 
	    whichObject.detachEvent('on'+eventType, whichObject[eventType+functionName]); 
	    whichObject[eventType+functionName] = null; 
	  } else 
	    whichObject.removeEventListener(eventType,functionName,false); 
	} 
	// }}}
	,
	// {{{ __clearMemoryGarbage()
    /**
     *
     *  This function is used for Internet Explorer in order to clear memory when the page unloads.
     *
     * 
     * @private
     */	
    __clearMemoryGarbage : function()
    {
   		/* Example of event which causes memory leakage in IE 
   		
   		DHTMLSuite.commonObj.addEvent(expandRef,"click",function(){ window.refToMyMenuBar[index].__changeMenuBarState(this); })
   		
   		We got a circular reference.
   		
   		*/
   		
    	if(!DHTMLSuite.clientInfoObj.isMSIE)return;
   	
    	for(var no in DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects){
    		DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[no] = false;    			
    	}

    	for(var no=0;no<DHTMLSuite.eventElements.length;no++){
    		DHTMLSuite.eventElements[no].onclick = null;
    		DHTMLSuite.eventElements[no].onmousedown = null;
    		DHTMLSuite.eventElements[no].onmousemove = null;
    		DHTMLSuite.eventElements[no].onmouseout = null;
    		DHTMLSuite.eventElements[no].onmouseover = null;
    		DHTMLSuite.eventElements[no].onmouseup = null;
    		DHTMLSuite.eventElements[no].onfocus = null;
    		DHTMLSuite.eventElements[no].onblur = null;
    		DHTMLSuite.eventElements[no].onkeydown = null;
    		DHTMLSuite.eventElements[no].onkeypress = null;
    		DHTMLSuite.eventElements[no].onkeyup = null;
    		DHTMLSuite.eventElements[no].onselectstart = null;
    		DHTMLSuite.eventElements[no].ondragstart = null;
    		DHTMLSuite.eventElements[no].oncontextmenu = null;
    		DHTMLSuite.eventElements[no].onscroll = null;
    		
    	}
    	window.onunload = null;
    	DHTMLSuite = null;

    }		
    // }}}
    ,
	// {{{ __addEventElement()
    /**
     *
     *  Add element to garbage collection array. The script will loop through this array and remove event handlers onload in ie.
     *
     * 
     * @private
     */	    
    __addEventElement : function(el)
    {
    	DHTMLSuite.eventElements[DHTMLSuite.eventElements.length] = el;    
    }
    // }}}
    ,
	// {{{ getSrcElement()
    /**
     *
     *  Returns a reference to the HTML element which triggered an event.
     *	@param Event e = Event object
     *
     * 
     * @public
     */	       
    getSrcElement : function(e)
    {
    	var el;
		if (e.target) el = e.target;
			else if (e.srcElement) el = e.srcElement;
			if (el.nodeType == 3) // defeat Safari bug
				el = el.parentNode;
		return el;	
    }	
    // }}}	
    ,
	// {{{ isObjectClicked()
    /**
     *
     *  Returns true if an object is clicked, false otherwise. This method will also return true if you clicked on a sub element
     *	@param Object obj = Reference to HTML element
     *	@param Event e = Event object
     *
     * 
     * @public
     */	      
	isObjectClicked : function(obj,e)
	{
		var src = this.getSrcElement(e);
		var string = src.tagName + '(' + src.className + ')';
		if(src==obj)return true;
		while(src.parentNode && src.tagName.toLowerCase()!='html'){
			src = src.parentNode;
			string = string + ',' + src.tagName + '(' + src.className + ')';
			if(src==obj)return true;			
		}		
		return false;		
	}
	// }}}
	,
	// {{{ getObjectByClassName()
    /**
     *
     *  Walks up the DOM tree and returns first found object with a given class name
     *
     *	@param Event e = Event object
     *	@param String className = CSS - Class name
     *
     * 
     * @public
     */	 	
	getObjectByClassName : function(e,className)
	{
		var src = this.getSrcElement(e);
		if(src.className==className)return src;
		while(src && src.tagName.toLowerCase()!='html'){
			src = src.parentNode;	
			if(src.className==className)return src;
		}
		return false;
	}
	//}}}
	,
	// {{{ getObjectByAttribute()
    /**
     *
     *  Walks up the DOM tree and returns first found object with a given attribute set
     *
     *	@param Event e = Event object
     *	@param String attribute = Custom attribute
     *
     * 
     * @public
     */	 	
	getObjectByAttribute : function(e,attribute)
	{
		var src = this.getSrcElement(e);
		var att = src.getAttribute(attribute);
		if(!att)att = src[attribute];
		if(att)return src;
		while(src && src.tagName.toLowerCase()!='html'){
			src = src.parentNode;	
			var att = src.getAttribute('attribute');
			if(!att)att = src[attribute];		
			if(att)return src;
		}
		return false;
	}
	//}}}
	,
	// {{{ getUniqueId()
    /**
     *
     *  Returns a unique numeric id
     *
     *
     * 
     * @public
     */		
	getUniqueId : function()
	{
		var no = Math.random() + '';
		no = no.replace('.','');
		
		var no2 = Math.random() + '';
		no2 = no2.replace('.','');
		
		return no + no2;		
	}
	// }}}
	,
	// {{{ getAssociativeArrayFromString()
    /**
     *
     *  Returns an associative array from a comma delimited string
     *  @param String propertyString - commaseparated string(example: "id:myid,title:My title,contentUrl:includes/tab.inc")
     *
     *	@return Associative array of keys + property value(example: key: id, value : myId)
     * @public
     */		
	getAssociativeArrayFromString : function(propertyString)
	{
		if(!propertyString)return;
		var retArray = new Array();
		var items = propertyString.split(/,/g);
		for(var no=0;no<items.length;no++){
			var tokens = items[no].split(/:/);	
			retArray[tokens[0]] = tokens[1];			
		}	
		return retArray;	
	}
		
}

/************************************************************************************************************
*	Client info class
*
*	Created:			August, 18th, 2006
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class Purpose of class: Provide browser information to the classes below. Instead of checking for
*		 browser versions and browser types in the classes below, they should check this
*		 easily by referncing properties in the class below. An object("DHTMLSuite.clientInfoObj") of this 
*		 class will always be accessible to the other classes. * @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/


DHTMLSuite.clientInfo = function()
{
	var browser;			// Complete user agent information
	
	var isOpera;			// Is the browser "Opera"
	var isMSIE;				// Is the browser "Internet Explorer"
	var isOldMSIE;			// Is this browser and older version of Internet Explorer ( by older, we refer to version 6.0 or lower)	
	var isFirefox;			// Is the browser "Firefox"
	var navigatorVersion;	// Browser version
}
	
DHTMLSuite.clientInfo.prototype = {
	
	// {{{ init()
    /**
     *
	 *
     *  This method initializes the clientInfo object. This is done automatically when you create a widget object.
     *
     * 
     * @public
     */	    	
	init : function()
	{
		this.browser = navigator.userAgent;	
		this.isOpera = (this.browser.toLowerCase().indexOf('opera')>=0)?true:false;
		this.isFirefox = (this.browser.toLowerCase().indexOf('firefox')>=0)?true:false;
		this.isMSIE = (this.browser.toLowerCase().indexOf('msie')>=0)?true:false;
		this.isOldMSIE = (this.browser.toLowerCase().match(/msie [0-6]/gi))?true:false;
		this.isSafari = (this.browser.toLowerCase().indexOf('safari')>=0)?true:false;
		this.navigatorVersion = navigator.appVersion.replace(/.*?MSIE (\d\.\d).*/g,'$1')/1;

	}	
	// }}}		
	,
	// {{{ getBrowserWidth()
    /**
     *
	 *
     *  This method returns the width of the browser window(i.e. inner width)
     *
     * 
     * @public
     */		
	getBrowserWidth : function()
	{
		return document.documentElement.offsetWidth;		
	}
	// }}}
	,
	// {{{ getBrowserHeight()
    /**
     *
	 *
     *  This method returns the height of the browser window(i.e. inner height)
     *
     * 
     * @public
     */		
	getBrowserHeight: function()
	{
		return document.documentElement.offsetHeight;
	}
}



/************************************************************************************************************
*	DOM query class 
*
*	Created:			August, 31th, 2006
*
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class Purpose of class:	Gives you a set of methods for querying elements on a webpage. When an object
*		 of this class has been created, the method will also be available via the document object.
*		 Example: var elements = document.getElementsByClassName('myClass');
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/

DHTMLSuite.domQuery = function()
{
	// Make methods of this class a member of the document object. 
	document.getElementsByClassName = this.getElementsByClassName;
	document.getElementsByAttribute = this.getElementsByAttribute;
}



	
DHTMLSuite.domQuery.prototype = {
	
	// {{{ getElementsByClassName()
    /**
     *	This method will return an array of all elements of a specific class.
     *
	 *	@param String className = Class to search for
	 *	@param Object inputObj = Optional - Which element to search from(i.e. search only in sub elements of this one) if ommited, search all.
     *	@return Array objects = An array of references to HTML elements on the page. 
     *  @type Array
     *
     * @public
     */	
    	
	getElementsByClassName : function(className,inputObj)
	{
		var returnArray = new Array();
		if(inputObj)
			var allElements = inputObj.getElementsByTagName('*');
		else
			var allElements = document.getElementsByTagName('*');
		for(var no=0;no<allElements.length;no++){
			if(allElements[no].className==className)returnArray[returnArray.length] = allElements[no];	
		}
		return returnArray;
	}	
	// }}}		
	,
	// {{{ getElementsByAttribute()
    /**
     *	This method will return an array of all elements where a specific attribute is set.
     *
	 *	@param String attribute = Attribute to search for
	 *	@param String attributeValue = Optional - only search for elements where the attribute is set to this value
	 *	@param Object inputObj = Optional - Which element to search from(i.e. search only in sub elements of this one) if ommited, search all.
	 *
     *	@return Array objects = An array of references to HTML elements on the page. 
     *	@type Array
     * 
     * @public
     */	    	
	getElementsByAttribute : function(attribute,attributeValue,inputObj)
	{
		var returnArray = new Array();
		if(inputObj)
			var allElements = inputObj.getElementsByTagName('*');
		else
			var allElements = document.getElementsByTagName('*');
		for(var no=0;no<allElements.length;no++){
			var att = allElements[no].getAttribute(attribute);
			if(!attributeValue){
				if(att)returnArray[returnArray.length] = allElements[no];
			}
			else
				if(att==attributeValue)returnArray[returnArray.length] = allElements[no];
		}
		return returnArray;
	}	
	// }}}			

}