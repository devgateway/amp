if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML menu bar class
*
*	Created:						October, 21st, 2006
*	@class Purpose of class:		Creates a top bar menu
*			
*	Css files used by this script:	menu-bar.css
*
*	Demos of this class:			demo-menu-bar.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Creates a top bar menu strip. Demos: <br>
*	<ul>
*	<li>(<a href="../../demos/demo-menu-bar-2.html" target="_blank">A menu with a detailed description on how it is created</a>)</li>
*	<li>(<a href="../../demos/demo-menu-bar.html" target="_blank">Demo with lots of menus on the same page</a>)</li>
*	<li>(<a href="../../demos/demo-menu-bar-custom-css.html" target="_blank">Two menus with different layout</a>)</li>
*	<li>(<a href="../../demos/demo-menu-bar-custom-css-file.html" target="_blank">One menu with custom layout/css.</a>)</li>
*	</ul>
*
*	<a href="../images/menu-bar-1.gif" target="_blank">Image describing the classes</a> <br><br>
*
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.menuBar = function()
{
	var menuItemObj;
	var layoutCSS;					// Name of css file
	var menuBarBackgroundImage;		// Name of background image
	var menuItem_objects;			// Array of menu items - html elements.
	var menuBarObj;					// Reference to the main dib
	var menuBarHeight;
	var menuItems;					// Reference to objects of class menuModelItem
	var highlightedItems;			// Array of currently highlighted menu items.
	var menuBarState;				// Menu bar state - true or false - 1 = expand items on mouse over
	var activeSubItemsOnMouseOver;	// Activate sub items on mouse over	(instead of onclick)
	

	var submenuGroups;				// Array of div elements for the sub menus
	var submenuIframes;				// Array of sub menu iframes used to cover select boxes in old IE browsers.
	var createIframesForOldIeBrowsers;	// true if we want the script to create iframes in order to cover select boxes in older ie browsers.
	var targetId;					// Id of element where the menu will be inserted.
	var menuItemCssPrefix;			// Css prefix of menu items.
	var cssPrefix;					// Css prefix for the menu bar
	var menuItemLayoutCss;			// Css path for the menu items of this menu bar
	var objectIndex;			// Global index of this object - used to refer to the object of this class outside
	this.cssPrefix = 'DHTMLSuite_';
	this.menuItemLayoutCss = false;	// false = use default for the menuItem class.
	this.layoutCSS = 'menu-bar.css';
	this.menuBarBackgroundImage = 'menu_strip_bg.jpg';
	this.menuItem_objects = new Array();
	DHTMLSuite.variableStorage.menuBar_highlightedItems = new Array();
	
	this.menuBarState = false;
	
	this.menuBarObj = false;
	this.menuBarHeight = 26;
	this.submenuGroups = new Array();
	this.submenuIframes = new Array();
	this.targetId = false;
	this.activeSubItemsOnMouseOver = false;
	this.menuItemCssPrefix = false;
	this.createIframesForOldIeBrowsers = true;
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;	
	
}





DHTMLSuite.menuBar.prototype = {	
	
	// {{{ init()
    /**
     *	Initilizes the script - This method should be called after your set methods.
     *
     *
     * @public	
     */					
	init : function()
	{
		
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	
		this.__createMainMenuDiv();	// Create general divs
		this.__createMenuItems();	// Create menu items
		this.__setBasicEvents();	// Set basic events.
		window.refToThismenuBar = this;
	}
	// }}}
	,
	// {{{ setTarget()
    /**
     *	Specify where this menu bar will be inserted. the element with this id will be parent of the menu bar.
     *
     *  @param String idOfHTMLElement = Id of element where the menu will be inserted. 
     *
     * @public	
     */		
	setTarget : function(idOfHTMLElement)
	{
		this.targetId = idOfHTMLElement;		
		
	}	
	// }}}	
	,
	// {{{ setLayoutCss()
    /**
     *	Specify the css file for this menu bar
     *
     *  @param String nameOfNewCssFile = Name of new css file. 
     *
     * @public	
     */		
	setLayoutCss : function(nameOfNewCssFile)
	{
		this.layoutCSS = nameOfNewCssFile;		
		
	}	
	// }}}
	,	
	// {{{ setMenuItemLayoutCss()
    /**
     *	Specify the css file for the menu items
     *
     *  @param String nameOfNewCssFile = Name of new css file. 
     *
     * @public	
     */		
	setMenuItemLayoutCss : function(nameOfNewCssFile)
	{
		this.menuItemLayoutCss = nameOfNewCssFile;		
		
	}	
	// }}}
	,	
	// {{{ setCreateIframesForOldIeBrowsers()
    /**
     *	This method specifies if you want to the script to create iframes behind sub menu groups in order to cover eventual select boxes. This
     *	is needed if you have users with older IE browsers(prior to version 7) and when there's a chance that a sub menu could appear on top
     *	of a select box.
     *
     *  @param Boolean createIframesForOldIeBrowsers = true if you want the script to create iframes to cover select boxes in older ie browsers.
     *
     * @public	
     */		
	setCreateIframesForOldIeBrowsers : function(createIframesForOldIeBrowsers)
	{
		this.createIframesForOldIeBrowsers = createIframesForOldIeBrowsers;		
		
	}	
	// }}}
	,
	// {{{ addMenuItems()
    /**
     *	Add menu items
     *
     *  @param DHTMLSuite.menuModel menuModel Object of class DHTMLSuite.menuModel which holds menu data 	
     *
     * @public	
     */			
	addMenuItems : function(menuItemObj)
	{
		this.menuItemObj = menuItemObj;	
		this.menuItems = menuItemObj.getItems();
	}
	// }}}
	,
	// {{{ setActiveSubItemsOnMouseOver()
    /**
     *	 Specify if sub menus should be activated on mouse over(i.e. no matter what the menuState property is). 	
     *
     *	@param Boolean activateSubOnMouseOver - Specify if sub menus should be activated on mouse over(i.e. no matter what the menuState property is).
     *
     * @public	
     */		
	setActiveSubItemsOnMouseOver : function(activateSubOnMouseOver)
	{
		this.activeSubItemsOnMouseOver = activateSubOnMouseOver;	
	}
	// }}}
	,
	// {{{ setMenuItemState()
    /**
     *	This method changes the state of the menu bar(expanded or collapsed). This method is called when someone clicks on the arrow at the right of menu items.
     * 	
     *	@param Number menuItemId - ID of the menu item we want to switch state for
     * 	@param String state - New state(example: "disabled")
     *
     * @public	
     */			
	setMenuItemState : function(menuItemId,state)
	{
		this.menuItem_objects[menuItemId].setState(state);
	}
	// }}}	
	,
	// {{{ setMenuItemCssPrefix()
    /**
     *	Specify prefix of css classes used for the menu items. Default css prefix is "DHTMLSuite_". If you wish have some custom styling for some of your menus, 
     *	create a separate css file and replace DHTMLSuite_ for the class names with your new prefix.  This is useful if you want to have two menus on the same page
     *	with different stylings.
     * 	
     *	@param String newCssPrefix - New css prefix for menu items.
     *
     * @public	
     */		
	setMenuItemCssPrefix : function(newCssPrefix)
	{
		this.menuItemCssPrefix = newCssPrefix;
	}
	// }}}
	,	
	// {{{ setCssPrefix()
    /**
     *	Specify prefix of css classes used for the menu bar. Default css prefix is "DHTMLSuite_" and that's the prefix of all css classes inside menu-bar.css(the default css file). 
     *	If you want some custom menu bars, create and include your own css files, replace DHTMLSuite_ in the class names with your own prefix and set the new prefix by calling
     *	this method. This is useful if you want to have two menus on the same page with different stylings.
     * 	
     *	@param String newCssPrefix - New css prefix for the menu bar classes.
     *
     * @public	
     */		
	setCssPrefix : function(newCssPrefix)
	{
		this.cssPrefix = newCssPrefix;
	}
	// }}}
	,
	// {{{ replaceSubMenus()
    /**
     *	This method replaces existing sub menu items with a new subset (To replace all menu items, pass 0 as idOfParentMenuItem)
     *
     * 	
     *	@param Number idOfParentMenuItem - ID of parent element ( 0 if top node) - if set, all sub elements will be deleted and replaced with the new menu model.
     *	@param menuModel newMenuModel - Reference to object of class menuModel
     *
     * @private	
     */		
	replaceMenuItems : function(idOfParentMenuItem,newMenuModel)
	{		
		this.hideSubMenus();	// Hide all sub menus
		this.__deleteMenuItems(idOfParentMenuItem);	// Delete old menu items.
		this.menuItemObj.__appendMenuModel(newMenuModel,idOfParentMenuItem);	// Appending new menu items to the menu model.
		this.__clearAllMenuItems();
		this.__createMenuItems();
	}
	// }}}	
	,
	// {{{ deleteMenuItems()
    /**
     *	This method deletes menu items from the menu dynamically
     * 	
     *	@param Number idOfParentMenuItem - Parent id - parent id of the elements to delete.
     *	@param Boolean deleteParentElement - Should parent element also be deleted, or only sub elements?
     *
     * @public	
     */		
	deleteMenuItems : function(idOfParentMenuItem,deleteParentElement)
	{
		this.hideSubMenus();	// Hide all sub menus	
		this.__deleteMenuItems(idOfParentMenuItem,deleteParentElement);
		this.__clearAllMenuItems();
		this.__createMenuItems();		
	}
	// }}}	
	,
	// {{{ appendMenuItems()
    /**
     *	This method appends menu items to the menu dynamically
     * 	
     *	@param Integer idOfParentMenuItem - Parent id - where to append the new items.
     *	@param menuModel newMenuModel - Object of type menuModel. This menuModel will be appended as sub elements of defined idOfParentMenuItem
     *
     * @public	
     */		
	appendMenuItems : function(idOfParentMenuItem,newMenuModel)
	{
		this.hideSubMenus();	// Hide all sub menus
		this.menuItemObj.__appendMenuModel(newMenuModel,idOfParentMenuItem);	// Appending new menu items to the menu model.
		this.__clearAllMenuItems();
		this.__createMenuItems();		
	}	
	// }}}	
	,
	// {{{ hideMenuItem()
    /**
     *	This method doesn't delete menu items. it hides them only.
     * 	
     *	@param Number menuItemId - Id of the item you want to hide.
     *
     * @public	
     */		
	hideMenuItem : function(menuItemId)
	{
		this.menuItem_objects[menuItemId].hide();

	}	
	// }}}	
	,
	// {{{ showMenuItem()
    /**
     *	This method shows a menu item. If the item isn't hidden, nothing is done.
     * 	
     *	@param Number menuItemId - Id of the item you want to show
     *
     * @public	
     */		
	showMenuItem : function(menuItemId)
	{
		this.menuItem_objects[menuItemId].show();
	}	
	// }}}
	,
	// {{{ setText()
    /**
     *	Replace the text for a menu item
     * 	
     *	@param Integer menuItemId - Id of menu item.
     *	@param String newText - New text for the menu item.
     *
     * @public	
     */		
	setText : function(menuItemId,newText)
	{
		this.menuItem_objects[menuItemId].setText(newText);
	}		
	// }}}
	,
	// {{{ setIcon()
    /**
     *	Replace menu icon for a menu item. 
     * 	
     *	@param Integer menuItemId - Id of menu item.
     *	@param String newPath - Path to new menu icon. Pass blank or false if you want to clear the menu item.
     *
     * @public	
     */		
	setIcon : function(menuItemId,newPath)
	{
		this.menuItem_objects[menuItemId].setIcon(newPath);
	}	
	// }}}
	,
	// {{{ __clearAllMenuItems()
    /**
     *	Delete HTML elements for all menu items.
     *
     * @private	
     */			
	__clearAllMenuItems : function()
	{
		for(var prop=0;prop<this.menuItemObj.menuItemsOrder.length;prop++){
			var id = this.menuItemObj.menuItemsOrder[prop];
			if(!id)continue;
			if(this.submenuGroups[id]){
				this.submenuGroups[id].parentNode.removeChild(this.submenuGroups[id]);
				this.submenuGroups[id] = false;	
			}
			if(this.submenuIframes[id]){
				this.submenuIframes[id].parentNode.removeChild(this.submenuIframes[id]);
				this.submenuIframes[id] = false;
			}	
		}
		this.menuBarObj.innerHTML = '';		
	}
	// }}}
	,
	// {{{ __deleteMenuItems()
    /**
     *	This method deletes menu items from the menu, i.e. menu model and the div elements for these items.
     * 	
     *	@param Integer idOfParentMenuItem - Parent id - where to start the delete process.
     *	@param Boolean includeParent - Delete parent menu item in addition to the sub menu items.
     *
     * @private	
     */		
	__deleteMenuItems : function(idOfParentMenuItem,includeParent)
	{
		if(includeParent)this.menuItemObj.__deleteANode(idOfParentMenuItem);
		if(!this.submenuGroups[idOfParentMenuItem])return;	// No sub items exists.		
		this.menuItem_objects[idOfParentMenuItem].__setHasSub(false);	// Delete existing sub menu divs.
		this.menuItemObj.__deleteChildNodes(idOfParentMenuItem);	// Delete existing child nodes from menu model
		var groupBox = this.submenuGroups[idOfParentMenuItem];
		groupBox.parentNode.removeChild(groupBox);	// Delete sub menu group box. 
		if(this.submenuIframes[idOfParentMenuItem]){
			this.submenuIframes[idOfParentMenuItem].parentNode.removeChild(this.submenuIframes[idOfParentMenuItem]);
		}
		this.submenuGroups.splice(idOfParentMenuItem,1);
		this.submenuIframes.splice(idOfParentMenuItem,1);
	}
	// }}}	
	,
	// {{{ __changeMenuBarState()
    /**
     *	This method changes the state of the menu bar(expanded or collapsed). This method is called when someone clicks on the arrow at the right of menu items.
     * 	
     *
     * @private	
     */		
	__changeMenuBarState : function(){
		var objectIndex = this.getAttribute('objectRef');
		var obj = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[objectIndex];
		var parentId = this.id.replace(/[^0-9]/gi,'');	
			
			
		var state = obj.menuItem_objects[parentId].getState();		
		
		if(state=='disabled')return;
		obj.menuBarState = !obj.menuBarState;
		if(!obj.menuBarState)obj.hideSubMenus();else{
			obj.hideSubMenus();
			setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + objectIndex + '].__expandGroup(' + parentId+ ')',10);
		}		
	}
	// }}}		
	,
	// {{{ __createDivs()
    /**
     *	Create the main HTML elements for this menu dynamically
     * 	
     *
     * @private	
     */		
	__createMainMenuDiv : function()
	{
		window.refTomenuBar = this;	// Reference to menu strip object
		
		this.menuBarObj = document.createElement('DIV');	
		this.menuBarObj.className = this.cssPrefix + 'menuBar_' + this.menuItemObj.submenuType[1];
		
		if(!document.getElementById(this.targetId)){
			alert('No target defined for the menu object');
			return;
		}
		// Appending menu bar object as a sub of defined target element.
		var target = document.getElementById(this.targetId);
		target.appendChild(this.menuBarObj);				
	}
	// }}}
	,
	// {{{ hideSubMenus()
    /**
     *	Deactivate all sub menus ( collapse and set state back to regular )
     *	In case you have a menu inside a scrollable container, call this method in an onscroll event for that element
     *	example document.getElementById('textContent').onscroll = menuBar.__hideSubMenus;
     * 	
     *	@param Event e - this variable is present if this method is called from an event. You will never use this parameter
     *
     * @public	
     */		
	hideSubMenus : function(e)
	{
		if(this && this.tagName){	/* Method called from event */
			if(document.all)e = event;
			var srcEl = DHTMLSuite.commonObj.getSrcElement(e);
			if(srcEl.tagName.toLowerCase()=='img')srcEl = srcEl.parentNode;
			if(srcEl.className && srcEl.className.indexOf('arrow')>=0){
				return;
			}
			
		}
		for(var no=0;no<DHTMLSuite.variableStorage.menuBar_highlightedItems.length;no++){
			DHTMLSuite.variableStorage.menuBar_highlightedItems[no].setState('regular');	// Set state back to regular
			DHTMLSuite.variableStorage.menuBar_highlightedItems[no].__hideGroup();	// Hide eventual sub menus
		}	
		DHTMLSuite.variableStorage.menuBar_highlightedItems = new Array();			
	}
	,
	// {{{ __hideSubMenusAfterSmallDelay()
    /**
     *	Hide sub menu items after a small delay - mouse down event for the HTML elemnet
     *
     *
     * @private	
     */		
	__hideSubMenusAfterSmallDelay : function()
	{
		var ind = this.objectIndex;		
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].hideSubMenus()',15);		
	}
	// }}}
	,
	// {{{ __expandGroup()
    /**
     *	Expand a group of sub items.
     *
     * 	@param integer idOfParentMenuItem - Id of parent element
     *
     * @private	
     */			
	__expandGroup : function(idOfParentMenuItem)
	{
	
		var groupRef = this.submenuGroups[idOfParentMenuItem];
		var subDiv = groupRef.getElementsByTagName('DIV')[0];
		
		var numericId = subDiv.id.replace(/[^0-9]/g,'');
		
		groupRef.style.visibility='visible';	// Show menu group.
		if(this.submenuIframes[idOfParentMenuItem])this.submenuIframes[idOfParentMenuItem].style.visibility = 'visible';	// Show iframe if it exists.
		DHTMLSuite.variableStorage.menuBar_highlightedItems[DHTMLSuite.variableStorage.menuBar_highlightedItems.length] = this.menuItem_objects[numericId];
		this.__positionSubMenu(idOfParentMenuItem);
		
		if(DHTMLSuite.clientInfoObj.isOpera){	/* Opera fix in order to get correct height of sub menu group */
			var subDiv = groupRef.getElementsByTagName('DIV')[0];	/* Find first menu item */
			subDiv.className = subDiv.className.replace('_over','_over');	/* By "touching" the class of the menu item, we are able to fix a layout problem in Opera */
		}
	}
	
	,
	// {{{ __activateMenuElements()
    /**
     *	Traverse up the menu items and highlight them.
     * 	
     *	@param HTMLElement parentMenuItemObject - Reference to the DIV tag(menu element) triggering the event. This would be the parent menu item of the sub menu to expand
     *	@param Object menuBarObjectRef - Reference to this menuBar object
     *	@param Boolean firstIteration - First iteration of this method, i.e. not recursive ? 
     *
     * @private	
     */			
	__activateMenuElements : function(parentMenuItemObject,menuBarObjectRef,firstIteration)
	{
		
		if(!this.menuBarState && !this.activeSubItemsOnMouseOver)return;	// Menu is not activated and it shouldn't be activated on mouse over.
		var numericId = parentMenuItemObject.id.replace(/[^0-9]/g,'');	// Get a numeric reference to current menu item.
		
		var state = this.menuItem_objects[numericId].getState();	// Get state of this menu item.
		if(state=='disabled')return;	// This menu item is disabled - return from function without doing anything.		
		
		if(firstIteration && DHTMLSuite.variableStorage.menuBar_highlightedItems.length>0){
			this.hideSubMenus();	// First iteration of this function=> Hide other sub menus. 
		}	
		// What should be the state of this menu item -> If it's the one the mouse is over, state should be "over". If it's a parent element, state should be "active".
		var newState = 'over';
		if(!firstIteration)newState = 'active';	// State should be set to 'over' for the menu item the mouse is currently over.
			
		this.menuItem_objects[numericId].setState(newState);	// Switch state of menu item.
		if(this.submenuGroups[numericId]){	// Sub menu group exists. call the __expandGroup method. 
			this.__expandGroup(numericId);	// Expand sub menu group
		}
		DHTMLSuite.variableStorage.menuBar_highlightedItems[DHTMLSuite.variableStorage.menuBar_highlightedItems.length] = this.menuItem_objects[numericId];	// Save this menu item in the array of highlighted elements.
		if(menuBarObjectRef.menuItems[numericId].parentId){	// A parent element exists. Call this method over again with parent element as input argument.
			this.__activateMenuElements(menuBarObjectRef.menuItem_objects[menuBarObjectRef.menuItems[numericId].parentId].divElement,menuBarObjectRef,false);
		}
	}
	// }}}	
	,
	// {{{ __createMenuItems()
    /**
     *	Creates the HTML elements for the menu items.
     * 	
     *
     * @private	
     */		
	__createMenuItems : function()
	{

	
		var index = this.objectIndex;
		// Find first child of the body element. trying to insert the element before first child instead of appending it to the <body> tag, ref: problems in ie
		var firstChild = false;
		var firstChilds = document.getElementsByTagName('DIV');
		if(firstChilds.length>0)firstChild = firstChilds[0]
		
		for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){	// Looping through menu items		
			var indexThis = this.menuItemObj.menuItemsOrder[no];				
			if(!this.menuItems[indexThis].id)continue;		
			try{
				this.menuItem_objects[this.menuItems[indexThis].id] = new DHTMLSuite.menuItem(); 
			}catch(e){
				alert('Error: You need to include dhtmlSuite-menuItem.js in your html file');
			}
			if(this.menuItemCssPrefix)this.menuItem_objects[this.menuItems[indexThis].id].setCssPrefix(this.menuItemCssPrefix);	// Custom css prefix set
			if(this.menuItemLayoutCss)this.menuItem_objects[this.menuItems[indexThis].id].setLayoutCss(this.menuItemLayoutCss);	// Custom css file name
			
			var ref = this.menuItem_objects[this.menuItems[indexThis].id].createItem(this.menuItems[indexThis]); // Create div for this menu item.
		
			// Actiave sub elements when someone moves the mouse over the menu item - exception: not on separators.
			if(!this.menuItems[indexThis].separator)DHTMLSuite.commonObj.addEvent(ref,"mouseover",function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].__activateMenuElements(this,DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index],true); });	
			
			if(!this.menuItems[indexThis].jsFunction && !this.menuItems[indexThis].url){
				ref.setAttribute('objectRef',index);	/* saving the index of this object in the DHTMLSuite.variableStorage array as a property of the tag - We need to do this in order to avoid circular references and thus memory leakage in IE */
				ref.onclick = this.__changeMenuBarState;
				DHTMLSuite.commonObj.__addEventElement(ref);	
			}
			
			if(this.menuItem_objects[this.menuItems[indexThis].id].expandElement){	/* Small arrow at the right of the menu item exists - expand subs */
				var expandRef = this.menuItem_objects[this.menuItems[indexThis].id].expandElement;	/* Creating reference to expand div/arrow div */
				var parentId = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].menuItems[indexThis].parentId + '';	// Get parent id.
				var tmpId = expandRef.id.replace(/[^0-9]/gi,'');
				expandRef.setAttribute('objectRef',index);	/* saving the index of this object in the DHTMLSuite.variableStorage array as a property of the tag - We need to do this in order to avoid circular references and thus memory leakage in IE */
				expandRef.objectRef = index;
				if(this.menuItems[indexThis].jsFunction || this.menuItems[indexThis].url){
					expandRef.onclick = this.__changeMenuBarState;
					DHTMLSuite.commonObj.__addEventElement(expandRef);	
				}
			}
			var target = this.menuBarObj;	// Temporary variable - target of newly created menu item. target can be the main menu object or a sub menu group(see below where target is updated).

			if(this.menuItems[indexThis].depth==1 && this.menuItemObj.submenuType[this.menuItems[indexThis].depth]!='top' && this.menuItemObj.mainMenuGroupWidth){	/* Main menu item group width set */
				var tmpWidth = this.menuItemObj.mainMenuGroupWidth + '';
				if(tmpWidth.indexOf('%')==-1)tmpWidth = tmpWidth + 'px';
				target.style.width = tmpWidth;	
			}
		
			if(this.menuItems[indexThis].depth=='1'){	/* Top level item */
				if(this.menuItemObj.submenuType[this.menuItems[indexThis].depth]=='top'){	/* Type = "top" - menu items side by side */
					ref.style.styleFloat = 'left';				
					ref.style.cssText = 'float:left';						
				}			
			}else{
				if(!this.menuItems[indexThis].depth){
					alert('Error in menu model(depth not defined for a menu item). Remember to call the init() method for the menuModel object.');
					return;
				}
				if(!this.submenuGroups[this.menuItems[indexThis].parentId]){	// Sub menu div doesn't exist - > Create it.
					this.submenuGroups[this.menuItems[indexThis].parentId] = document.createElement('DIV');	
					this.submenuGroups[this.menuItems[indexThis].parentId].style.zIndex = 30000;
					this.submenuGroups[this.menuItems[indexThis].parentId].style.position = 'absolute';
					this.submenuGroups[this.menuItems[indexThis].parentId].id = 'DHTMLSuite_menuBarSubGroup' + this.menuItems[indexThis].parentId;
					this.submenuGroups[this.menuItems[indexThis].parentId].style.visibility = 'hidden';	// Initially hidden.
					this.submenuGroups[this.menuItems[indexThis].parentId].className = this.cssPrefix + 'menuBar_' + this.menuItemObj.submenuType[this.menuItems[indexThis].depth];
					

					if(firstChild){
						firstChild.parentNode.insertBefore(this.submenuGroups[this.menuItems[indexThis].parentId],firstChild);
					}else{
						document.body.appendChild(this.submenuGroups[this.menuItems[indexThis].parentId]);
					}
					
					if(DHTMLSuite.clientInfoObj.isMSIE && this.createIframesForOldIeBrowsers){	// Create iframe object in order to conver select boxes in older IE browsers(windows).
						this.submenuIframes[this.menuItems[indexThis].parentId] = document.createElement('<IFRAME src="about:blank" frameborder=0>');
						this.submenuIframes[this.menuItems[indexThis].parentId].id = 'DHTMLSuite_menuBarIframe_' + this.menuItems[indexThis].parentId;
						this.submenuIframes[this.menuItems[indexThis].parentId].style.position = 'absolute';
						this.submenuIframes[this.menuItems[indexThis].parentId].style.zIndex = 9000;
						this.submenuIframes[this.menuItems[indexThis].parentId].style.visibility = 'hidden';
						if(firstChild){
							firstChild.parentNode.insertBefore(this.submenuIframes[this.menuItems[indexThis].parentId],firstChild);
						}else{
							document.body.appendChild(this.submenuIframes[this.menuItems[indexThis].parentId]);
						}						
					}
				}	
				target = this.submenuGroups[this.menuItems[indexThis].parentId];	// Change target of newly created menu item. It should be appended to the sub menu div("A group box").				
			}			
			target.appendChild(ref); // Append menu item to the document.		
			
			if(this.menuItems[indexThis].visible == false)this.hideMenuItem(this.menuItems[indexThis].id);	// Menu item hidden, call the hideMenuItem method.
			if(this.menuItems[indexThis].state != 'regular')this.menuItem_objects[this.menuItems[indexThis].id].setState(this.menuItems[indexThis].state);	// Menu item hidden, call the hideMenuItem method.

		}	
		

		this.__setSizeOfAllSubMenus();	// Set size of all sub menu groups
		this.__positionAllSubMenus();	// Position all sub menu groups.
		if(DHTMLSuite.clientInfoObj.isOpera)this.__fixMenuLayoutForOperaBrowser();	// Call a function which fixes some layout issues in Opera.		
	}
	// }}}
	,
	// {{{ __fixMenuLayoutForOperaBrowser()
    /**
     *	A method used to fix the menu layout in Opera. 
     *
     *
     * @private	
     */		
	__fixMenuLayoutForOperaBrowser : function()
	{
		for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){
			var id = this.menuItemObj.menuItemsOrder[no];
			if(!id)continue;
			this.menuItem_objects[id].divElement.className = this.menuItem_objects[id].divElement.className.replace('_regular','_regular');	// Nothing is done but by "touching" the class of the menu items in Opera, we make them appear correctly
		}		
	}
	
	// }}}	
	,
	// {{{ __setSizeOfAllSubMenus()
    /**
     *	*	Walk through all sub menu groups and call the positioning method for each one of them.
     *
     *
     * @private	
     */		
	__setSizeOfAllSubMenus : function()
	{		
		for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){
			var prop = this.menuItemObj.menuItemsOrder[no];
			if(!prop)continue;
			this.__setSizeOfSubMenus(prop);
		}			
	}	
	// }}}	
	,	
	// {{{ __positionAllSubMenus()
    /**
     *	Walk through all sub menu groups and call the positioning method for each one of them.
     *
     *
     * @private	
     */		
	__positionAllSubMenus : function()
	{
		for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){
			var prop = this.menuItemObj.menuItemsOrder[no];
			if(!prop)continue;
			if(this.submenuGroups[prop])this.__positionSubMenu(prop);
		}		
	}
	// }}}	
	,
	// {{{ __positionSubMenu(parentId)
    /**
     *	Position a sub menu group
     *
     *	@param parentId  	
     *
     * @private	
     */		
	__positionSubMenu : function(parentId)
	{
		try{
			var shortRef = this.submenuGroups[parentId];	
			
			var depth = this.menuItems[parentId].depth;
			var dir = this.menuItemObj.submenuType[depth];
			if(dir=='top'){			
				shortRef.style.left = DHTMLSuite.commonObj.getLeftPos(this.menuItem_objects[parentId].divElement) + 'px';
				shortRef.style.top = (DHTMLSuite.commonObj.getTopPos(this.menuItem_objects[parentId].divElement) + this.menuItem_objects[parentId].divElement.offsetHeight) + 'px';
			}else{
				shortRef.style.left = (DHTMLSuite.commonObj.getLeftPos(this.menuItem_objects[parentId].divElement) + this.menuItem_objects[parentId].divElement.offsetWidth) + 'px';
				shortRef.style.top = (DHTMLSuite.commonObj.getTopPos(this.menuItem_objects[parentId].divElement)) + 'px';		
			}	
			
			if(DHTMLSuite.clientInfoObj.isMSIE){
				var iframeRef = this.submenuIframes[parentId]
				iframeRef.style.left = shortRef.style.left;
				iframeRef.style.top = shortRef.style.top;
				iframeRef.style.width = shortRef.clientWidth + 'px';
				iframeRef.style.height = shortRef.clientHeight + 'px';
			}									
		}catch(e){
			
		}		
	}
	// }}}	
	,
	// {{{ __setSizeOfSubMenus(parentId)
    /**
     *	Set size of a sub menu group
     *
     *	@param parentId  	
     *
     * @private	
     */		
	__setSizeOfSubMenus : function(parentId)
	{
		try{
			var shortRef = this.submenuGroups[parentId];	
			var subWidth = Math.max(shortRef.offsetWidth,this.menuItem_objects[parentId].divElement.offsetWidth);
			if(this.menuItems[parentId].submenuWidth)subWidth = this.menuItems[parentId].submenuWidth;
			if(subWidth>400)subWidth = 150;	// Hack for IE 6 -> force a small width when width is too large.
			subWidth = subWidth + '';
			if(subWidth.indexOf('%')==-1)subWidth = subWidth + 'px';
			shortRef.style.width = subWidth;	
			if(DHTMLSuite.clientInfoObj.isMSIE){
				this.submenuIframes[parentId].style.width = shortRef.style.width;
				this.submenuIFrames[parentId].style.height = shortRef.style.height;
			}
		}catch(e){
			
		}
		
	}
	// }}}	
	,
	// {{{ __repositionMenu()
    /**
     *	Position menu items.
     * 	
     *
     * @private	
     */		
	__repositionMenu : function(inputObj)
	{
		inputObj.menuBarObj.style.top = document.documentElement.scrollTop + 'px';
		
	}	
	// }}}	
	,
	// {{{ __menuItemRollOver()
    /**
     *	Position menu items.
     * 	
     *
     * @private	
     */	
	__menuItemRollOver : function(menuItemHTMLElementRef)
	{
		var numericId = menuItemHTMLElementRef.id.replace(/[^0-9]/g,'');
		menuItemHTMLElementRef.className = 'DHTMLSuite_menuBar_menuItem_over_' + this.menuItems[numericId]['depth'];		
	}
	// }}}	
	,	
	// {{{ __menuItemRollOut()
    /**
     *	Position menu items.
     * 	
     *
     * @private	
     */	
	__menuItemRollOut : function(menuItemHTMLElementRef)
	{		
		var numericId = menuItemHTMLElementRef.id.replace(/[^0-9]/g,'');
		menuItemHTMLElementRef.className = 'DHTMLSuite_menuBar_menuItem_' + this.menuItems[numericId]['depth'];		
	}
	// }}}	
	,
	// {{{ __menuNavigate()
    /**
     *	Navigate by click on a menu item
     * 	
     *
     * @private	
     */	
	__menuNavigate : function(menuItemHTMLElementRef)
	{
		var numericIndex = menuItemHTMLElementRef.id.replace(/[^0-9]/g,'');
		var url = this.menuItems[numericIndex]['url'];
		if(!url)return;	
	}
	// }}}	
	,
	// {{{ __setBasicEvents()
    /**
     *	Set basic events for the menu widget.
     * 	
     *
     * @private	
     */	
	__setBasicEvents : function()
	{
		var ind = this.objectIndex;

		DHTMLSuite.commonObj.addEvent(document.documentElement,"click",this.hideSubMenus);		
		DHTMLSuite.commonObj.addEvent(document.documentElement,"mouseup",this.hideSubMenus);		
	
	}
}