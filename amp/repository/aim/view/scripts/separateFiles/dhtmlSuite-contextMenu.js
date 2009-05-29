if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML context menu class
*
*	Created:						November, 4th, 2006
*	@class Purpose of class:		Creates a context menu
*			
*	Css files used by this script:	context-menu.css
*
*	Demos of this class:			demo-context-menu.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Creates a context menu. (<a href="../../demos/demo-context-menu.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

var referenceToDHTMLSuiteContextMenu;


DHTMLSuite.contextMenu = function()
{
	var menuModels;
	var menuItems;	
	var menuObject;			// Reference to context menu div
	var layoutCSS;
	var menuUls;			// Array of <ul> elements
	var width;				// Width of context menu
	var srcElement;			// Reference to the element which triggered the context menu, i.e. the element which caused the context menu to be displayed.
	var indexCurrentlyDisplayedMenuModel;	// Index of currently displayed menu model.
	
	this.menuModels = new Array();
	this.menuObject = false;
	this.layoutCSS = 'context-menu.css';
	this.menuUls = new Array();
	this.width = 100;
	this.srcElement = false;
	this.indexCurrentlyDisplayedMenuModel = false;
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
}

DHTMLSuite.contextMenu.prototype = 
{
	// {{{ setWidth()
    /**
     *	Set width of context menu
     *
     *  @param Integer newWidth - Width of context menu
     *
     * @public	
     */		
	setWidth : function(newWidth)
	{
		this.width = newWidth;
	}
	// }}}
	,	
	// {{{ setLayoutCss()
    /**
     *	Add menu items
     *
     *  @param String cssFileName Name of css file 	
     *
     * @public	
     */		
	setLayoutCss : function(cssFileName)
	{
		this.layoutCSS = cssFileName;	
	}	
	// }}}	
	,	
	// {{{ attachToElement()
    /**
     *	Add menu items
     *
     *  @param Object HTML Element = Reference to html element
     *  @param String elementId = String id of element(optional). An alternative to HTML Element	
     *
     * @public	
     */		
	attachToElement : function(element,elementId,menuModel)
	{
		window.refToThisContextMenu = this;
		if(!element && elementId)element = document.getElementById(elementId);
		if(!element.id){
			element.id = 'context_menu' + Math.random();
			element.id = element.id.replace('.','');
		}
		this.menuModels[element.id] = menuModel;
		element.oncontextmenu = this.__displayContextMenu;
		element.onmousedown = function() { window.refToThisContextMenu.__setReference(window.refToThisContextMenu); };
		DHTMLSuite.commonObj.__addEventElement(element)
		DHTMLSuite.commonObj.addEvent(document.documentElement,"click",this.__hideContextMenu);		
	}	
	// }}}
	,
	// {{{ __setReference()
    /**
     *	Creates a reference to current context menu object. (Note: This method should be deprecated as only one context menu object is needed)
     *
     *  @param Object context menu object = Reference to context menu object
     *
     * @private	
     */		
	__setReference : function(obj)
	{	
		referenceToDHTMLSuiteContextMenu = obj;	
	}
	,
	// {{{ __displayContextMenu()
    /**
     *	Displays the context menu
     *
     *  @param Event e
     *
     * @private	
     */		
	__displayContextMenu : function(e)
	{
		if(document.all)e = event;		
		var ref = referenceToDHTMLSuiteContextMenu;
		ref.srcElement = DHTMLSuite.commonObj.getSrcElement(e);
		
		if(!ref.indexCurrentlyDisplayedMenuModel || ref.indexCurrentlyDisplayedMenuModel!=this.id){			
			if(!ref.indexCurrentlyDisplayedMenuModel)DHTMLSuite.commonObj.loadCSS(ref.layoutCSS);			
			if(ref.indexCurrentlyDisplayedMenuModel){
				ref.menuObject.innerHTML = '';				
			}else{
				ref.__createDivs();
			}
			ref.menuItems = ref.menuModels[this.id].getItems();			
			ref.__createMenuItems();	
		}
		ref.indexCurrentlyDisplayedMenuModel=this.id;
		
		ref.menuObject.style.left = (e.clientX + Math.max(document.body.scrollLeft,document.documentElement.scrollLeft)) + 'px';
		ref.menuObject.style.top = (e.clientY + Math.max(document.body.scrollTop,document.documentElement.scrollTop)) + 'px';
		ref.menuObject.style.display='block';
		return false;
			
	}
	// }}}
	,
	// {{{ __displayContextMenu()
    /**
     *	Add menu items
     *
     *  @param Event e
     *
     * @private	
     */		
	__hideContextMenu : function()
	{
		var ref = referenceToDHTMLSuiteContextMenu;
		if(!ref)return;
		if(ref.menuObject)ref.menuObject.style.display = 'none';
		
		
	}
	// }}}
	,
	// {{{ __createDivs()
    /**
     *	Creates general divs for the menu
     *
     *
     * @private	
     */		
	__createDivs : function()
	{
		var firstChild = false;
		var firstChilds = document.getElementsByTagName('DIV');
		if(firstChilds.length>0)firstChild = firstChilds[0];
		this.menuObject = document.createElement('DIV');
		this.menuObject.className = 'DHTMLSuite_contextMenu';
		this.menuObject.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'context-menu-gradient.gif' + '\')';
		this.menuObject.style.backgroundRepeat = 'repeat-y';
		if(this.width)this.menuObject.style.width = this.width + 'px';
		
		if(firstChild){
			firstChild.parentNode.insertBefore(this.menuObject,firstChild);
		}else{
			document.body.appendChild(this.menuObject);
		}
							
		
	}
	// }}}
	,
	
	// {{{ __mouseOver()
    /**
     *	Display mouse over effect when moving the mouse over a menu item
     *
     *
     * @private	
     */		
	__mouseOver : function()
	{
		this.className = 'DHTMLSuite_item_mouseover';	
		if(!document.all){
			this.style.backgroundPosition = 'left center';
		}
									
	}
	// }}}
	,
	// {{{ __mouseOut()
    /**
     *	Remove mouse over effect when moving the mouse away from a menu item
     *
     *
     * @private	
     */		
	__mouseOut : function()
	{
		this.className = '';
		if(!document.all){
			this.style.backgroundPosition = '1px center';
		}		
	}
	// }}}
	,
	// {{{ __createMenuItems()
    /**
     *	Create menu items
     *
     *
     * @private	
     */		
	__createMenuItems : function()
	{
		window.refToContextMenu = this;	// Reference to menu strip object
		this.menuUls = new Array();
		for(var no in this.menuItems){	// Looping through menu items		
			if(!this.menuUls[0]){	// Create main ul element
				this.menuUls[0] = document.createElement('UL');
				this.menuObject.appendChild(this.menuUls[0]);
			}
			
			if(this.menuItems[no].depth==1){

				if(this.menuItems[no].separator){
					var li = document.createElement('DIV');
					li.className = 'DHTMLSuite_contextMenu_separator';
				}else{				
					var li = document.createElement('LI');
					if(this.menuItems[no].jsFunction){
						this.menuItems[no].url = this.menuItems[no].jsFunction + '(this,referenceToDHTMLSuiteContextMenu.srcElement)';
					}
					if(this.menuItems[no].itemIcon){
						li.style.backgroundImage = 'url(\'' + this.menuItems[no].itemIcon + '\')';
						if(!document.all)li.style.backgroundPosition = '1px center';

					}
					
					if(this.menuItems[no].url){
						var url = this.menuItems[no].url + '';
						li.onclick = function(){ eval(url); };
					}
					
					li.innerHTML = '<a href="#" onclick="return false">' + this.menuItems[no].itemText + '</a>';
					li.onmouseover = this.__mouseOver;
					li.onmouseout = this.__mouseOut;
					DHTMLSuite.commonObj.__addEventElement(li);
				}				
				this.menuUls[0].appendChild(li);			
			}		
		}		
	}	
}