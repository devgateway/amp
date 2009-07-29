if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML menu model item class
*
*	Created:						October, 30th, 2006
*	@class Purpose of class:		Save data about a menu item.
*			
*
*
* 	Update log:
*
************************************************************************************************************/

DHTMLSuite.menuModelItem = function()
{
	var id;					// id of this menu item.
	var itemText;			// Text for this menu item
	var itemIcon;			// Icon for this menu item.
	var url;				// url when click on this menu item
	var parentId;			// id of parent element
	var separator;			// is this menu item a separator
	var jsFunction;			// Js function to call onclick
	var depth;				// Depth of this menu item.
	var hasSubs;			// Does this menu item have sub items.
	var type;				// Menu item type - possible values: "top" or "sub". 
	var helpText;			// Help text for this item - appear when you move your mouse over the item.
	var state;
	var submenuWidth;		// Width of sub menu items.
	var visible;			// Visibility of menu item.
	
	this.state = 'regular';
}

DHTMLSuite.menuModelItem.prototype = {

	setMenuVars : function(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth)	
	{
		this.id = id;
		this.itemText = itemText;
		this.itemIcon = itemIcon;
		this.url = url;
		this.parentId = parentId;
		this.jsFunction = jsFunction;
		this.separator = false;
		this.depth = false;
		this.hasSubs = false;
		this.helpText = helpText;
		this.submenuWidth = submenuWidth;
		this.visible = true;
		if(!type){
			if(this.parentId)this.type = 'top'; else this.type='sub';
		}else this.type = type;
		

	}
	// }}}	
	,
	// {{{ setState()
    /**
     *	Update the state attribute of a menu item.
     *
     *  @param String newState New state of this item
     * @public	
     */		
	setAsSeparator : function(id,parentId)
	{
		this.id = id;
		this.parentId = parentId;
		this.separator = true;	
		this.visible = true;
		if(this.parentId)this.type = 'top'; else this.type='sub';		
	}
	// }}}	
	,
	// {{{ setState()
    /**
     *	Update the visible attribute of a menu item.
     *
     *  @param Boolean visible true = visible, false = hidden.
     * @public	
     */		
	setVisibility : function(visible)
	{
		this.visible = visible;
	}
	// }}}	
	,
	// {{{ getState()
    /**
     *	Return the state attribute of a menu item.
     *
     * @public	
     */		
	getState : function()
	{
		return this.state;
	}
	// }}}	
	,
	// {{{ setState()
    /**
     *	Update the state attribute of a menu item.
     *
     *  @param String newState New state of this item
     * @public	
     */		
	setState : function(newState)
	{
		this.state = newState;
	}
	// }}}	
	,
	// {{{ setSubMenuWidth()
    /**
     *	Specify width of direct subs of this item.
     *
     *  @param int newWidth Width of sub menu group(direct sub of this item)
     * @public	
     */		
	setSubMenuWidth : function(newWidth)
	{
		this.submenuWidth = newWidth;
	}
	// }}}	
	,
	// {{{ setIcon()
    /**
     *	Specify new menu icon
     *
     *  @param String iconPath Path to new menu icon
     * @public	
     */		
	setIcon : function(iconPath)
	{
		this.itemIcon = iconPath;
	}
	// }}}	
	,
	// {{{ setText()
    /**
     *	Specify new text for the menu item.
     *
     *  @param String newText New text for the menu item.
     * @public	
     */		
	setText : function(newText)
	{
		this.itemText = newText;
	}
}

/************************************************************************************************************
*	DHTML menu model class
*
*	Created:						October, 30th, 2006
*	@class Purpose of class:		Saves menu item data
*			
*
*	Demos of this class:			demo-menu-strip.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Organize menu items for different menu widgets. demos of menus: (<a href="../../demos/demo-menu-strip.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/


DHTMLSuite.menuModel = function()
{
	var menuItems;					// Array of menuModelItem objects
	var menuItemsOrder;			// This array is needed in order to preserve the correct order of the array above. the array above is associative
									// And some browsers will loop through that array in different orders than Firefox and IE.
	var submenuType;				// Direction of menu items(one item for each depth)
	var mainMenuGroupWidth;			// Width of menu group - useful if the first group of items are listed below each other
	this.menuItems = new Array();
	this.menuItemsOrder = new Array();
	this.submenuType = new Array();
	this.submenuType[1] = 'top';
	for(var no=2;no<20;no++){
		this.submenuType[no] = 'sub';
	}
	try{		
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
}

DHTMLSuite.menuModel.prototype = {
	// {{{ addItem()
    /**
     *	Add separator (special type of menu item)
     *
 	 *
     *
     *  @param int id of menu item
     *  @param string itemText = text of menu item
     *  @param string itemIcon = file name of menu icon(in front of menu text. Path will be imagePath for the DHTMLSuite + file name)
     *  @param string url = Url of menu item
     *  @param int parent id of menu item     
     *  @param String jsFunction Name of javascript function to execute. It will replace the url param. The function with this name will be called and the element triggering the action will be 
     *					sent as argument. Name of the element which triggered the menu action may also be sent as a second argument. That depends on the widget. The context menu is an example where
     *					the element triggering the context menu is sent as second argument to this function.    
     *
     * @public	
     */			
	addItem : function(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth)
	{
		if(!id)id = this.__getUniqueId();	// id not present - create it dynamically.
		try{
			this.menuItems[id] = new DHTMLSuite.menuModelItem();
		}catch(e){
			alert('Error: You need to include dhtmlSuite-menuModel.js in your html file');
		}
		this.menuItems[id].setMenuVars(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth);
		this.menuItemsOrder[this.menuItemsOrder.length] = id;
		return this.menuItems[id];
	}
	,
	// {{{ addItemsFromMarkup()
    /**
     *	This method creates all the menuModelItem objects by reading it from existing markup on your page.
     *	Example of HTML markup:
     *<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&lt;ul id="menuModel">
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="50000" itemIcon="../images/disk.gif">&lt;a href="#" title="Open the file menu">File&lt;/a>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ul width="150">
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="500001" jsFunction="saveWork()" itemIcon="../images/disk.gif">&lt;a href="#" title="Save your work">Save&lt;/a>&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="500002">&lt;a href="#">Save As&lt;/a>&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="500004" itemType="separator">&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="500003">&lt;a href="#">Open&lt;/a>&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ul>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="50001">&lt;a href="#">View&lt;/a>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ul width="130">
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="500011">&lt;a href="#">Source&lt;/a>&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="500012">&lt;a href="#">Debug info&lt;/a>&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="500013">&lt;a href="#">Layout&lt;/a>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ul width="150">
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="5000131">&lt;a href="#">CSS&lt;/a>&nbsp;&nbsp;
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="5000132">&lt;a href="#">HTML&lt;/a>&nbsp;&nbsp;
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="5000133">&lt;a href="#">Javascript&lt;/a>&nbsp;&nbsp;
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ul>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ul>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="50003" itemType="separator">&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;li id="50002">&lt;a href="#">Tools&lt;/a>&lt;/li>
		<br>&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ul>&nbsp;&nbsp;     
     *
     *  @param String ulId = ID of <UL> tag on your page.
     *
     * @public	
     */		
	addItemsFromMarkup : function(ulId)
	{
		if(!document.getElementById(ulId)){
			alert('<UL> tag with id ' + ulId + ' does not exist');
			return;
		}
		var ulObj = document.getElementById(ulId);
		var liTags = ulObj.getElementsByTagName('LI');		
		for(var no=0;no<liTags.length;no++){	// Walking through all <li> tags in the <ul> tree
			
			var id = liTags[no].id.replace(/[^0-9]/gi,'');	// Get id of item.
			if(!id)id = this.__getUniqueId();
			try{
				this.menuItems[id] = new DHTMLSuite.menuModelItem();	// Creating new menuModelItem object
			}catch(e){
				alert('Error: You need to include dhtmlSuite-menuModel.js in your html file');
			}
			this.menuItemsOrder[this.menuItemsOrder.length] = id;
			// Get the attributes for this new menu item.	
			
			var parentId = 0;	// Default parent id
			if(liTags[no].parentNode!=ulObj)parentId = liTags[no].parentNode.parentNode.id;	// parent node exists, set parentId equal to id of parent <li>.
						
			/* Checking type */
			var type = liTags[no].getAttribute('itemType');			
			if(!type)type = liTags[no].itemType;
			if(type=='separator'){	// Menu item of type "separator"
				this.menuItems[id].setAsSeparator(id,parentId);
				continue;	
			}
			if(parentId)type='sub'; else type = 'top';						
	
			var aTag = liTags[no].getElementsByTagName('A')[0];	// Get a reference to sub <a> tag
			if(!aTag){
				continue;
			}			
			if(aTag)var itemText = aTag.innerHTML;	// Item text is set to the innerHTML of the <a> tag.
			var itemIcon = liTags[no].getAttribute('itemIcon');	// Item icon is set from the itemIcon attribute of the <li> tag.
			var url = aTag.href;	// url is set to the href attribute of the <a> tag
			if(url=='#' || url.substr(url.length-1,1)=='#')url='';	// # = empty url.
			
			var jsFunction = liTags[no].getAttribute('jsFunction');	// jsFunction is set from the jsFunction attribute of the <li> tag.

			var submenuWidth = false;	// Not set from the <li> tag. 
			var helpText = aTag.getAttribute('title');	
			if(!helpText)helpText = aTag.title;
			
			this.menuItems[id].setMenuVars(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth);			
		}		
		var subUls = ulObj.getElementsByTagName('UL');
		for(var no=0;no<subUls.length;no++){
			var width = subUls[no].getAttribute('width');
			if(!width)width = subUls[no].width;	
			if(width){
				var id = subUls[no].parentNode.id.replace(/[^0-9]/gi,'');
				this.setSubMenuWidth(id,width);
			}
		}		
		ulObj.style.display='none';
		
	}	
	// }}}	
	,
	// {{{ setSubMenuWidth()
    /**
     *	This method specifies the width of a sub menu group. This is a useful method in order to get a correct width in IE6 and prior.
     *
     *  @param int id = ID of parent menu item
     *  @param String newWidth = Width of sub menu items.
     * @public	
     */		
	setSubMenuWidth : function(id,newWidth)
	{
		this.menuItems[id].setSubMenuWidth(newWidth);
	}	
	,
	// {{{ setMainMenuGroupWidth()
    /**
     *	Add separator (special type of menu item)
     *
     *  @param String newWidth = Size of a menu group
     *  @param int parent id of menu item
     * @public	
     */			
	setMainMenuGroupWidth : function(newWidth)
	{
		this.mainMenuGroupWidth = newWidth;
	}
	,
	// {{{ addSeparator()
    /**
     *	Add separator (special type of menu item)
     *
     *  @param int parent id of menu item
     * @public	
     */		
	addSeparator : function(parentId)
	{
		id = this.__getUniqueId();	// Get unique id
		if(!parentId)parentId = 0;
		try{
			this.menuItems[id] = new DHTMLSuite.menuModelItem();
		}catch(e){
			alert('Error: You need to include dhtmlSuite-menuModel.js in your html file');
		}
		this.menuItems[id].setAsSeparator(id,parentId);
		this.menuItemsOrder[this.menuItemsOrder.length] = id;
		return this.menuItems[id];
	}	
	,
	// {{{ init()
    /**
     *	Initilizes the menu model. This method should be called when all items has been added to the model.
     *
     *
     * @public	
     */		
	init : function()
	{
		this.__getDepths();	
		this.__setHasSubs();	
		
	}
	// }}}	
	,
	// {{{ setMenuItemVisibility()
    /**
     *	Save visibility of a menu item.
     * 	
     *	@param int id = Id of menu item..
     *	@param Boolean visible = Visibility of menu item.
     *
     * @public	
     */		
	setMenuItemVisibility : function(id,visible)
	{
		this.menuItems[id].setVisibility(visible);		
	}
	// }}}
	,
	// {{{ setSubMenuType()
    /**
     *	Set menu type for a specific menu depth.
     * 	
     *	@param int depth = 1 = Top menu, 2 = Sub level 1...
     *	@param String newType = New menu type(possible values: "top" or "sub")
     *
     * @public	
     */		
	setSubMenuType : function(depth,newType)
	{
		this.submenuType[depth] = newType;	
		
	}
	// }}}		
	,
	// {{{ __getDepths()
    /**
     *	return an array of all menu items or a branch of menu items.
     * 	
     *
     * @public	
     */		
	getItems : function(parentId,returnArray)
	{
		if(!parentId)return this.menuItems;
		if(!returnArray)returnArray = new Array();
		for(var no=0;no<this.menuItemsOrder.length;no++){
			var id = this.menuItemsOrder[no];
			if(!id)continue;
			if(this.menuItems[id].parentId==parentId){
				returnArray[returnArray.length] = this.menuItems[id];
				if(this.menuItems[id].hasSubs)return this.getItems(this.menuItems[id].id,returnArray);
			}
		}
		return returnArray;
		
	}
	// }}}
	,
	// {{{ __getUniqueId()
    /**
     *	Returns a unique id for a menu item. This method is used by the addSeparator function in case an id isn't sent to the method.
     * 	
     *
     * @private	
     */	    	
	__getUniqueId : function()
	{
		var num = Math.random() + '';
		num = num.replace('.','');	
		num = '99' + num;		
		num = num /1;		
		while(this.menuItems[num]){
			num = Math.random() + '';
			num = num.replace('.','');	
			num = num /1;				
		}
		return num;
	}
	// }}}	
	,
	// {{{ __getDepths()
    /**
     *	Create variable for the depth of each menu item.
     * 	
     *
     * @private	
     */	
    __getDepths : function()
    {    	
    	for(var no=0;no<this.menuItemsOrder.length;no++){
    		var id = this.menuItemsOrder[no];
    		if(!id)continue;
    		this.menuItems[id].depth = 1;
    		if(this.menuItems[id].parentId){
    			this.menuItems[id].depth = this.menuItems[this.menuItems[id].parentId].depth+1;    
 	
    		}  
    		this.menuItems[id].type = this.submenuType[this.menuItems[id].depth];	// Save menu direction for this menu item.  		
    	}    	
    }	
    // }}}
    ,	    
    // {{{ __setHasSubs()
    /**
     *	Create variable for the depth of each menu item.
     * 	
     *
     * @private	
     */	
    __setHasSubs : function()
    {    	
    	for(var no=0;no<this.menuItemsOrder.length;no++){
    		var id = this.menuItemsOrder[no];
    		if(!id)continue;    		
    		if(this.menuItems[id].parentId){
    			this.menuItems[this.menuItems[id].parentId].hasSubs = 1;
    			
    		}    		
    	}    	
    }	
    // }}}
    ,
	// {{{ __hasSubs()
    /**
     *	Does a menu item have sub elements ?
     * 	
     *
     * @private	
     */	
	// }}}	
	__hasSubs : function(id)
	{
		for(var no=0;no<this.menuItemsOrder.length;no++){
			var id = this.menuItemsOrder[no];
			if(!id)continue;
			if(this.menuItems[id].parentId==id)return true;		
		}
		return false;	
	}
	// }}}
	,
	// {{{ __deleteChildNodes()
    /**
     *	Deleting child nodes of a specific parent id
     * 	
     *	@param int parentId
     *
     * @private	
     */	
	// }}}		
	__deleteChildNodes : function(parentId,recursive)
	{
		var itemsToDeleteFromOrderArray = new Array();
		for(var prop=0;prop<this.menuItemsOrder.length;prop++){
    		var id = this.menuItemsOrder[prop];
    		if(!id)continue;    
    					
			if(this.menuItems[id].parentId==parentId && parentId){
				this.menuItems[id] = false;
				itemsToDeleteFromOrderArray[itemsToDeleteFromOrderArray.length] = id;				
				this.__deleteChildNodes(id,true);	// Recursive call.
			}	
		}	
		
		if(!recursive){
			for(var prop=0;prop<itemsToDeleteFromOrderArray.length;prop++){
				if(!itemsToDeleteFromOrderArray[prop])continue;
				this.__deleteItemFromItemOrderArray(itemsToDeleteFromOrderArray[prop]);
			}
		}
		this.__setHasSubs();
	}
	// }}}
	,
	// {{{ __deleteANode()
    /**
     *	Deleting a specific node from the menu model
     * 	
     *	@param int id = Id of node to delete.
     *
     * @private	
     */	
	// }}}		
	__deleteANode : function(id)
	{
		this.menuItems[id] = false;	
		this.__deleteItemFromItemOrderArray(id);	
	}
	,
	// {{{ __deleteItemFromItemOrderArray()
    /**
     *	Deleting a specific node from the menuItemsOrder array(The array controlling the order of the menu items).
     * 	
     *	@param int id = Id of node to delete.
     *
     * @private	
     */	
	// }}}		
	__deleteItemFromItemOrderArray : function(id)
	{
		for(var no=0;no<this.menuItemsOrder.length;no++){
			var tmpId = this.menuItemsOrder[no];
			if(!tmpId)continue;		
			if(this.menuItemsOrder[no]==id){
				this.menuItemsOrder.splice(no,1);
				return;
			}
		}
		
	}
	// }}}
	,	
	// {{{ __appendMenuModel()
    /**
     *	Replace the sub items of a menu item with items from a new menuModel.
     * 	
     *	@param menuModel newModel = An object of class menuModel - the items of this menu model will be appended to the existing menu items.
     *	@param Int parentId = Id of parent element of the appended items.
     *
     * @private	
     */	
	// }}}		
	__appendMenuModel : function(newModel,parentId)
	{
		if(!newModel)return;
		var items = newModel.getItems();
		for(var no=0;no<newModel.menuItemsOrder.length;no++){
			var id = newModel.menuItemsOrder[no];
			if(!id)continue;
			if(!items[id].parentId)items[id].parentId = parentId;
			this.menuItems[id] = items[id];	
			for(var no2=0;no2<this.menuItemsOrder.length;no2++){	// Check to see if this item allready exists in the menuItemsOrder array, if it does, remove it. 
				if(!this.menuItemsOrder[no2])continue;
				if(this.menuItemsOrder[no2]==items[id].id){
					this.menuItemsOrder.splice(no2,1);
				}
			}
			this.menuItemsOrder[this.menuItemsOrder.length] = items[id].id;		
		}
		this.__getDepths();		
		this.__setHasSubs();		
	}
	// }}}
}