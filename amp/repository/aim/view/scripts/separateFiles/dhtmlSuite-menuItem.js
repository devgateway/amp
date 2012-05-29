if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML menu item class
*
*	Created:						October, 21st, 2006
*	@class Purpose of class:		Creates the HTML for a single menu item.
*			
*	Css files used by this script:	menu-item.css
*
*	Demos of this class:			demo-menu-strip.html
*
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class Purpose of class:	Creates the div(s) for a menu item. This class is used by the menuBar class. You can 
*	also create a menu item and add it where you want on your page. the createItem() method will return the div
*	for the item. You can use the appendChild() method to add it to your page. 
*
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/


DHTMLSuite.menuItem = function()
{
	var layoutCSS;	
	var divElement;							// the <div> element created for this menu item
	var expandElement;						// Reference to the arrow div (expand sub items)
	var cssPrefix;							// Css prefix for the menu items.
	var modelItemRef;						// Reference to menuModelItem
	
	this.layoutCSS = 'menu-item.css';
	this.cssPrefix = 'DHTMLSuite_';
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	
	var objectIndex;
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;

	
}

DHTMLSuite.menuItem.prototype = 
{
	
	/*
	*	Create a menu item.
	*
	*	@param menuModelItem menuModelItemObj = An object of class menuModelItem
	*/
	createItem : function(menuModelItemObj)
	{
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	// Load css
		
		DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
			
		this.modelItemRef = menuModelItemObj;
		this.divElement = document.createElement('DIV');	// Create main div
		this.divElement.id = 'DHTMLSuite_menuItem' + menuModelItemObj.id;	// Giving this menu item it's unque id
		this.divElement.className = this.cssPrefix + 'menuItem_' + menuModelItemObj.type + '_regular'; 
		this.divElement.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
		if(menuModelItemObj.helpText){	// Add "title" attribute to the div tag if helpText is defined
			this.divElement.title = menuModelItemObj.helpText;
		}
		
		// Menu item of type "top"
		if(menuModelItemObj.type=='top'){			
			this.__createMenuElementsOfTypeTop(this.divElement);
		}

		if(menuModelItemObj.type=='sub'){
			this.__createMenuElementsOfTypeSub(this.divElement);
		}
		
		if(menuModelItemObj.separator){
			this.divElement.className = this.cssPrefix + 'menuItem_separator_' + menuModelItemObj.type;
			this.divElement.innerHTML = '<span></span>';
		}else{		
			/* Add events */
			var tmpVar = this.objectIndex/1;
			this.divElement.onclick = function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[tmpVar].__navigate(e); }
			this.divElement.onmousedown = this.__clickMenuItem;			// on mouse down effect
			this.divElement.onmouseup = this.__rolloverMenuItem;		// on mouse up effect
			this.divElement.onmouseover = this.__rolloverMenuItem;		// mouse over effect
			this.divElement.onmouseout = this.__rolloutMenuItem;		// mouse out effect.
			DHTMLSuite.commonObj.__addEventElement(this.divElement);
		}
		return this.divElement;
	}
	// }}}
	,
	// {{{ setLayoutCss()
    /**
     *	Creates the different parts of a menu item of type "top".
     *
     *  @param String newLayoutCss = Name of css file used for the menu items.
     *
     * @public	
     */		
	setLayoutCss : function(newLayoutCss)
	{
		this.layoutCSS = newLayoutCss;
		
	}
	// }}}
	,
	// {{{ __createMenuElementsOfTypeTop()
    /**
     *	Creates the different parts of a menu item of type "top".
     *
     *  @param menuModelItem menuModelItemObj = Object of type menuModelItemObj
     *  @param Object parentEl = Reference to parent element
     *
     * @private	
     */		
	__createMenuElementsOfTypeTop : function(parentEl){
		if(this.modelItemRef.itemIcon){
			var iconDiv = document.createElement('DIV');
			iconDiv.innerHTML = '<img src="' + this.modelItemRef.itemIcon + '">';
			iconDiv.id = 'menuItemIcon' + this.modelItemRef.id
			parentEl.appendChild(iconDiv);		
		}
		if(this.modelItemRef.itemText){
			var div = document.createElement('DIV');
			div.innerHTML = this.modelItemRef.itemText;	
			div.className = this.cssPrefix + 'menuItem_textContent';
			div.id = 'menuItemText' + this.modelItemRef.id;	
			parentEl.appendChild(div);
		}
		/* Create div for the arrow -> Show sub items */
		var div = document.createElement('DIV');
		div.className = this.cssPrefix + 'menuItem_top_arrowShowSub';
		div.id = 'DHTMLSuite_menuBar_arrow' + this.modelItemRef.id;
		parentEl.appendChild(div);
		this.expandElement = div;
		if(!this.modelItemRef.hasSubs)div.style.display='none';
				
	}
	// }}}
	,	
	
	// {{{ __createMenuElementsOfTypeSub()
    /**
     *	Creates the different parts of a menu item of type "sub".
     *
     *  @param menuModelItem menuModelItemObj = Object of type menuModelItemObj
     *  @param Object parentEl = Reference to parent element
     *
     * @private	
     */		
	__createMenuElementsOfTypeSub : function(parentEl){		
		if(this.modelItemRef.itemIcon){
			parentEl.style.backgroundImage = 'url(\'' + this.modelItemRef.itemIcon + '\')';	
			parentEl.style.backgroundRepeat = 'no-repeat';
			parentEl.style.backgroundPosition = 'left center';	
		}
		if(this.modelItemRef.itemText){
			var div = document.createElement('DIV');
			div.className = 'DHTMLSuite_textContent';
			div.innerHTML = this.modelItemRef.itemText;	
			div.className = this.cssPrefix + 'menuItem_textContent';
			div.id = 'menuItemText' + this.modelItemRef.id;
			parentEl.appendChild(div);
		}
		
		/* Create div for the arrow -> Show sub items */
		var div = document.createElement('DIV');
		div.className = this.cssPrefix + 'menuItem_sub_arrowShowSub';
		parentEl.appendChild(div);		
		div.id = 'DHTMLSuite_menuBar_arrow' + this.modelItemRef.id;
		this.expandElement = div;
		
		if(!this.modelItemRef.hasSubs){
			div.style.display='none';	
		}else{
			div.previousSibling.style.paddingRight = '15px';
		}	
	}
	// }}}
	,
	// {{{ setCssPrefix()
    /**
     *	Set css prefix for the menu item. default is 'DHTMLSuite_'. This is useful in case you want to have different menus on a page with different layout.
     *
     *  @param String cssPrefix = New css prefix. 
     *
     * @public	
     */		
	setCssPrefix : function(cssPrefix)
	{
		this.cssPrefix = cssPrefix;
	}
	// }}}
	,
	// {{{ setMenuIcon()
    /**
     *	Replace menu icon.
     *
     *	@param String newPath - Path to new icon (false if no icon);
     *
     * @public	
     */		
	setIcon : function(newPath)
	{
		this.modelItemRef.setIcon(newPath);
		if(this.modelItemRef.type=='top'){	// Menu item is of type "top"
			var div = document.getElementById('menuItemIcon' + this.modelItemRef.id);	// Get a reference to the div where the icon is located.
			var img = div.getElementsByTagName('IMG')[0];	// Find the image
			if(!img){	// Image doesn't exists ?
				img = document.createElement('IMG');	// Create new image
				div.appendChild(img);
			}
			img.src = newPath;	// Set image path
			if(!newPath)img.parentNode.removeChild(img);	// No newPath defined, remove the image.			
		}
		if(this.modelItemRef.type=='sub'){	// Menu item is of type "sub"
			this.divElement.style.backgroundImage = 'url(\'' + newPath + '\')';		// Set backgroundImage for the main div(i.e. menu item div)	
		}		
	}
	// }}}
	,
	// {{{ setText()
    /**
     *	Replace the text of a menu item
     *
     *	@param String newText - New text for the menu item.
     *
     * @public	
     */		
	setText : function(newText)
	{
		this.modelItemRef.setText(newText);
		document.getElementById('menuItemText' + this.modelItemRef.id).innerHTML = newText;
		
		
	}
	
	// }}}
	,
	// {{{ __clickMenuItem()
    /**
     *	Effect - click on menu item
     *
     *
     * @private	
     */		
	__clickMenuItem : function()
	{
		this.className = this.className.replace('_regular','_click');
		this.className = this.className.replace('_over','_click');
	}
	// }}}	
	,	
	// {{{ __rolloverMenuItem()
    /**
     *	Roll over effect
     *
     *
     * @private	
     */		
	__rolloverMenuItem : function()
	{
		this.className = this.className.replace('_regular','_over');
		this.className = this.className.replace('_click','_over');
	}	
	// }}}
	,	
	// {{{ __rolloutMenuItem()
    /**
     *	Roll out effect
     *
     *
     * @private	
     */		
	__rolloutMenuItem : function()
	{
		this.className = this.className.replace('_over','_regular');
		
	}
	// }}}
	,	
	// {{{ setState()
    /**
     *	Set state of a menu item.
     *
     *	@param String newState = New state for the menu item
     *
     * @public	
     */		
	setState : function(newState)
	{
		this.divElement.className = this.cssPrefix + 'menuItem_' + this.modelItemRef.type + '_' + newState; 		
		this.modelItemRef.setState(newState);
	}
	// }}}
	,
	// {{{ getState()
    /**
     *	Return state of a menu item. 
     *
     *
     * @public	
     */		
	getState : function()
	{
		var state = this.modelItemRef.getState();
		if(!state){
			if(this.divElement.className.indexOf('_over')>=0)state = 'over';	
			if(this.divElement.className.indexOf('_click')>=0)state = 'click';	
			this.modelItemRef.setState(state);		
		}
		return state;
	}	
	// }}}
	,
	// {{{ __setHasSub()
    /**
     *	Update the item, i.e. show/hide the arrow if the element has subs or not. 
     *
     *
     * @private	
     */	
    __setHasSub : function(hasSubs)
    {
    	this.modelItemRef.hasSubs = hasSubs;
    	if(!hasSubs){
    		document.getElementById(this.cssPrefix +'menuBar_arrow' + this.modelItemRef.id).style.display='none';    		
    	}else{
    		document.getElementById(this.cssPrefix +'menuBar_arrow' + this.modelItemRef.id).style.display='block';
    	}    	
    }
    // }}}	
    ,
	// {{{ hide()
    /**
     *	Hide the menu item.
     *
     *
     * @public	
     */	    
    hide : function()
    {
    	this.modelItemRef.setVisibility(false);
    	this.divElement.style.display='none';    	
    }    
    ,
 	// {{{ show()
    /**
     *	Show the menu item.
     *
     *
     * @public	
     */	     
    show : function()
    {
    	this.modelItemRef.setVisibility(true);
    	this.divElement.style.display='block';    	
    }    
	// }}}
	,
	// {{{ __hideGroup()
    /**
     *	Hide the group the menu item is a part of. Example: if we're dealing with menu item 2.1, hide the group for all sub items of 2
     *
     *
     * @private	
     */			
	__hideGroup : function()
	{		
		if(this.modelItemRef.parentId){
			this.divElement.parentNode.style.visibility='hidden';	
			if(DHTMLSuite.clientInfoObj.isMSIE){
				try{
					var tmpId = this.divElement.parentNode.id.replace(/[^0-9]/gi,'');
					document.getElementById('DHTMLSuite_menuBarIframe_' + tmpId).style.visibility = 'hidden';
				}catch(e){
					// IFRAME hasn't been created.
				}	
			}
		}	

	}
	// }}}	
	,
	// {{{ __navigate()
    /**
     *	Navigate after click on a menu item.
     *
     *
     * @private	
     */		
	__navigate : function(e)
	{
		/* Check to see if the expand sub arrow is clicked. if it is, we shouldn't navigate from this click */
		if(document.all)e = event;
		if(e){
			var srcEl = DHTMLSuite.commonObj.getSrcElement(e);
			if(srcEl.id.indexOf('arrow')>=0)return;
		}
		if(this.modelItemRef.state=='disabled')return;
		if(this.modelItemRef.url){
			location.href = this.modelItemRef.url;
		}
		if(this.modelItemRef.jsFunction){
			try{
				eval(this.modelItemRef.jsFunction);
			}catch(e){
				alert('Defined Javascript code for the menu item( ' + this.modelItemRef.jsFunction + ' ) cannot be executed');
			}
		}
	} 
}