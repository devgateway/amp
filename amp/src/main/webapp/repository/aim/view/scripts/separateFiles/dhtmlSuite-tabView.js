if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Tab view class
*
*	Created:			August, 21st, 2006
*
* 	Update log:
*
************************************************************************************************************/

	
var refToTabViewObjects = new Array();	// Reference to objects of this class. 
										// We need this because the script doesn't allways know which object to use

/**
* @constructor
* @class Purpose of class:	Tab view class - transfors plain HTML into tabable layers.<br>
* (See <a target="_blank" href="../../demos/demo-tabs-1.html">demo 1</A> and <a target="_blank" href="../../demos/demo-tabs-2.html">demo 2</A>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/

DHTMLSuite.tabView = function()
{
	var textPadding;				// Tab spacing
	var strictDocType ; 			// Using a strict document type, i.e. <!DOCTYPE>

	var DHTMLSuite_tabObj;		// Reference to div surrounding the tab set
	var activeTabIndex;				// Currently displayed tab(index - 0 = first tab)
	var initActiveTabIndex;			// Initially displayed tab(index - 0 = first tab)
	var ajaxObjects;				// Reference to ajax objects
	var tabView_countTabs;
	var tabViewHeight;
	var tabSetParentId;				// Id of div surrounding the tab set.
	var tabTitles;					// Tab titles
	var width;						// width of tab view
	var height;						// height of tab view
	var layoutCSS;
	var outsideObjectRefIndex;		// Which index of refToTabViewObjects refers to this object.
	var maxNumberOfTabs;
	var dynamicContentObj;	
	var closeButtons;
	
	// Default variable values
	this.textPadding = 3;
	this.strictDocType = true; 	
	this.ajaxObjects = new Array();
	this.tabTitles = new Array();
	this.layoutCSS = 'tab-view.css';
	this.maxNumberOfTabs = 6;
	this.dynamicContentObj = false;
	this.closeButtons = new Array();
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
}

DHTMLSuite.tabView.prototype = {
	// {{{ init()
    /**
     * Initialize the script
     * 
     * @public
     */	
	init : function()
	{
		
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		this.outsideObjectRefIndex = refToTabViewObjects.length;
		refToTabViewObjects[this.outsideObjectRefIndex] = this;
		try{
			this.dynamicContentObj = new DHTMLSuite.dynamicContent();
		}catch(e){
			alert('You need to include DHTMLSuite-dynamicContent.js');
		}
		this.__initializeAndParseTabs(false,false);
		
	}
	// }}}
	,
	// {{{ getMaximumNumberOfTabs()
    /**
     * Return maximum number of tabs
     * 
     * @return Int maximumNumberOfTabs = Maximum number of tabs
     *
     * @public
     */		
	getMaximumNumberOfTabs : function()
	{
		return this.maxNumberOfTabs;
	}
	// }}}	
	,
	// {{{ setMaximumTabs()
    /**
     * Set maximum number of tabs
     * 
     * @param Int maximumNumberOfTabs = Maximum number of tabs
     *
     * @public
     */	
	setMaximumTabs : function(maximumNumberOfTabs)
	{
		this.maxNumberOfTabs = maximumNumberOfTabs;
	}       
    // }}}	
    ,
	// {{{ setParentId()
    /**
     * Set padding on tabs
     * 
     * @param String idOfParentHTMLElement = id of parent div
     *
     * @public
     */	
	setParentId : function(idOfParentHTMLElement)
	{
		this.tabSetParentId = idOfParentHTMLElement;
		this.DHTMLSuite_tabObj = document.getElementById(idOfParentHTMLElement);
	}       
    // }}}	
    ,
	// {{{ setWidth()
    /**
     * Set width of tab view
     * 
     * @param String Width of tab view
     *
     * @public
     */	
	setWidth : function(newWidth)
	{
		this.width = newWidth;
	}   
    
    // }}}	
    ,
	// {{{ setHeight()
    /**
     * Set height of tab view on tabs
     * 
     * @param String Height of tab view
     *
     * @public
     */	
	setHeight : function(newHeight)
	{
		this.height = newHeight;
	}   
    // }}}	
    ,	
	// {{{ setIndexActiveTab()
    /**
     * Set index of initially active tab
     * 
     * @param Int indexOfNewActiveTab = Index of active tab(0 = first tab)
     *
     * @public
     */	
	setIndexActiveTab : function(indexOfNewActiveTab)
	{
		this.initActiveTabIndex = indexOfNewActiveTab;
	}   
    
    // }}}	
    ,	
	// {{{ setTabTitles()
    /**
     * Set title of tabs
     * 
     * @param Array titleOfTabs = Title of tabs
     *
     * @public
     */	
	setTabTitles : function(titleOfTabs)
	{
		this.tabTitles = titleOfTabs;
	}    
    
    // }}}	
    ,	
	// {{{ setCloseButtons()
    /**
     * Specify which tabs that should have close buttons
     * 
     * @param Array closeButtons = Array of true or false
     *
     * @public
     */	
	setCloseButtons : function(closeButtons)
	{
		this.closeButtons = closeButtons;
	}    
    
    // }}}	
	,
	// {{{ createNewTab()
    /**
     * 
     * Creates new tab dynamically
     *
     * @param String parentId = Id of tabset
     * @param String tabTitle = Title of new tab
     * @param String tabContent = Content of new tab(Optional)
     * @param String tabContentUrl = Url to content of new tab(Optional) - Ajax is used to get this content
     *
     * @public
     */		
	createNewTab : function(parentId,tabTitle,tabContent,tabContentUrl,closeButton)
	{
		if(this.tabView_countTabs>=this.maxNumberOfTabs)return;	// Maximum number of tabs reached - return
		var div = document.createElement('DIV');	// Create new tab content div.
		div.className = 'DHTMLSuite_aTab';	// Assign new tab to CSS class DHTMLSuite_aTab
		this.DHTMLSuite_tabObj.appendChild(div);			// Appending new tab content div to main tab view div
		var tabId = this.__initializeAndParseTabs(true,tabTitle,closeButton);	// Call the init method in order to create tab header and tab images
		if(tabContent)div.innerHTML = tabContent;	// Static tab content specified, put it into the new div
		if(tabContentUrl){	// Get content from external file	
			this.dynamicContentObj.loadContent('tabView' + parentId +'_' + tabId,tabContentUrl);
		}				
	}	
	// }}}	    
    ,	
 	// {{{ deleteTab()
    /**
     *
     * Delete a tab 
     *
     * @param String tabLabel = Label of tab to delete(Optional)
     * @param Int tabIndex = Index of tab to delete(Optional)
     *
     * @public
     */		
	deleteTab : function(tabLabel,tabIndex)
	{		
		if(tabLabel){	// Delete tab by tab title
			var index = this.getTabIndexByTitle(tabLabel);	// Get index of tab
			if(index!=-1){	// Tab exists if index<>-1
				this.deleteTab(false,index);
			}
			
		}else if(tabIndex>=0){	// Delete tab by tab index.
			if(document.getElementById('tabTab' + this.tabSetParentId + '_' + tabIndex)){
				var obj = document.getElementById('tabTab' + this.tabSetParentId + '_' + tabIndex);
				var id = obj.parentNode.parentNode.id;
				obj.parentNode.removeChild(obj);
				var obj2 = document.getElementById('tabView' + this.tabSetParentId + '_' + tabIndex);
				obj2.parentNode.removeChild(obj2);
				this.__resetTabIds(this.tabSetParentId);
				this.initActiveTabIndex=-1;
				var newIndex = 0;
				if(refToTabViewObjects[this.outsideObjectRefIndex].activeTabIndex==tabIndex)refToTabViewObjects[this.outsideObjectRefIndex].activeTabIndex=-1;
				this.__showTab(this.tabSetParentId,newIndex,this.outsideObjectRefIndex);
			}			
		}		
	}
	// }}}	
	,  	// {{{ addContentToTab()
    /**
     * Add content to a tab dynamically.
     * 
     * @param String tabLabel = Label of tab to delete(Optional)
     * @param String filePath = Path to file you want to show inside the tab.
     *
     * @public
     */		
	addContentToTab : function(tabLabel,filePath)
	{		
		var index = this.getTabIndexByTitle(tabLabel);	// Get index of tab
		if(index!=-1){	// Tab found
			this.dynamicContentObj.loadContent('tabView' + this.tabSetParentId + '_' + index,filePath);		
		}
	}
	// }}}	
	, 
 	// {{{ displayATab()
    /**
     * Display a tab manually
     * 
     * @param String tabTitle = Label of tab to show(Optional)
     * @param Int tabIndex = Index of tab to show(Optional)
     *
     * @public
     */		

	displayATab : function(tabLabel,tabIndex)
	{		
		if(tabLabel){	// Delete tab by tab title
			var index = this.getTabIndexByTitle(tabLabel);	// Get index of tab
			if(index!=-1){	// Tab exists if index<>-1
				this.initActiveTabIndex = index;
			}else return false;
			
		}else{
			this.initActiveTabIndex = tabIndex;
		}

		this.__showTab(this.tabSetParentId,this.initActiveTabIndex,this.outsideObjectRefIndex)
	}	
	// }}}	
	,  	
 	// {{{ getTabIndex()
    /**
     * Return index of active tab
     * 
     * @type Integer tabIndex = Index of active tab(0 = first tab)
     *
     * @public
     */		
	getTabIndex : function()
	{
		var divs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');
		var tabIndex = 0;
		for(var no=0;no<divs.length;no++){
			if(divs[no].id.indexOf('tabTab')>=0){
				if(divs[no].className!='tabInactive')return tabIndex;
				tabIndex++;		
			}
		}		
		//tabInactive	
		return tabIndex;		
	}	
	// }}}
	,   
	
	// {{{ __setPadding()
    /**
     * Set padding on tabs
     * 
     * @private
     */		
	__setPadding : function(obj,padding){
		var span = obj.getElementsByTagName('SPAN')[0];
		span.style.paddingLeft = padding + 'px';	
		span.style.paddingRight = padding + 'px';	
	}	
	// }}}	
	,
	// {{{ __showTab()
    /**
     * Set padding
     * 
     * @param String parentId = id of parent div
     * @param Int tabIndex = Index of tab to show
     * @param Int objectIndex = Index of refToTabViewObjects, reference to the object of this class.
     *
     * @private
     */		
	__showTab : function(parentId,tabIndex,objectIndex)
	{
		var parentId_div = parentId + "_";
		if(!document.getElementById('tabView' + parentId_div + tabIndex)){			
			return;
		}
		
		if(refToTabViewObjects[objectIndex].activeTabIndex>=0){
			if(refToTabViewObjects[objectIndex].activeTabIndex==tabIndex){
				return;
			}	
			var obj = document.getElementById('tabTab'+parentId_div + refToTabViewObjects[objectIndex].activeTabIndex);	
			if(!obj){
				refToTabViewObjects[objectIndex].activeTabIndex = 0;
				var obj = document.getElementById('tabTab'+parentId_div + refToTabViewObjects[objectIndex].activeTabIndex);	
			}
			obj.className='tabInactive';
			obj.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_inactive.gif' + '\')';
			var imgs = obj.getElementsByTagName('IMG');
			var img = imgs[imgs.length-1];
			img.src = DHTMLSuite.configObj.imagePath + 'tab_right_inactive.gif';
			document.getElementById('tabView' + parentId_div + refToTabViewObjects[objectIndex].activeTabIndex).style.display='none';
		}
		
		var thisObj = document.getElementById('tabTab'+ parentId_div +tabIndex);	
			
		thisObj.className='tabActive';
		thisObj.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_active.gif' + '\')';
		var imgs = thisObj.getElementsByTagName('IMG');
		var img = imgs[imgs.length-1];		
		img.src = DHTMLSuite.configObj.imagePath + 'tab_right_active.gif';
		
		document.getElementById('tabView' + parentId_div + tabIndex).style.display='block';
		refToTabViewObjects[objectIndex].activeTabIndex = tabIndex;
		

		var parentObj = thisObj.parentNode;
		var aTab = parentObj.getElementsByTagName('DIV')[0];
		countObjects = 0;
		var startPos = 2;
		var previousObjectActive = false;
		while(aTab){
			if(aTab.tagName=='DIV'){
				if(previousObjectActive){
					previousObjectActive = false;
					startPos-=2;
				}
				if(aTab==thisObj){
					startPos-=2;
					previousObjectActive=true;
					refToTabViewObjects[objectIndex].__setPadding(aTab,refToTabViewObjects[objectIndex].textPadding+1);
				}else{
					refToTabViewObjects[objectIndex].__setPadding(aTab,refToTabViewObjects[objectIndex].textPadding);
				}
				
				aTab.style.left = startPos + 'px';
				countObjects++;
				startPos+=2;
			}			
			aTab = aTab.nextSibling;
		}
		
		return;
	}
	// }}}	
	,
	// {{{ tabClick()
    /**
     * Set padding
     * 
     * @param String parentId = id of parent div
     * @param Int tabIndex = Index of tab to show
     *
     * @private
     */	
	__tabClick : function(inputObj,index)
	{
		var idArray = inputObj.id.split('_');	
		var parentId = inputObj.getAttribute('parentRefId');
		if(!parentId)parentId=  inputObj.parentRefId;
		this.__showTab(parentId,idArray[idArray.length-1].replace(/[^0-9]/gi,''),index);
		
	}	
	// }}}
	,
	// {{{ rolloverTab()
    /**
     * Set padding
     * 
     *
     * @private
     */		
	__rolloverTab : function()
	{
		if(this.className.indexOf('tabInactive')>=0){
			this.className='inactiveTabOver';
			this.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_over.gif' + '\')';
			var imgs = this.getElementsByTagName('IMG');
			var img = imgs[imgs.length-1];
			
			img.src = DHTMLSuite.configObj.imagePath + 'tab_right_over.gif';
		}
		
	}	
	// }}}
	,	
	// {{{ rolloutTab()
    /**
     * 
     *
     * @private
     */			
	__rolloutTab : function()
	{
		if(this.className ==  'inactiveTabOver'){
			this.className='tabInactive';
			this.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_inactive.gif' + '\')';
			var imgs = this.getElementsByTagName('IMG');
			var img = imgs[imgs.length-1];
			img.src = DHTMLSuite.configObj.imagePath + 'tab_right_inactive.gif';
		}		
	}
	// }}}
	,
	// {{{ __initializeAndParseTabs()
    /**
     * 
     * @param Int additionalTab = Additional tabs to the existing
     * @param String nameOfAdditionalTab = Title of additional tab.
     *
     * @private
     */	
	__initializeAndParseTabs : function(additionalTab,nameOfAdditionalTab,additionalCloseButton)
	{
		this.DHTMLSuite_tabObj.className = ' DHTMLSuite_tabWidget';
		
		window.refToThisTabSet = this;
		if(!additionalTab || additionalTab=='undefined'){			
			this.DHTMLSuite_tabObj = document.getElementById(this.tabSetParentId);
			this.width = this.width + '';
			if(this.width.indexOf('%')<0)this.width= this.width + 'px';
			this.DHTMLSuite_tabObj.style.width = this.width;
						
			this.height = this.height + '';
			if(this.height.length>0){
				if(this.height.indexOf('%')<0)this.height= this.height + 'px';
				this.DHTMLSuite_tabObj.style.height = this.height;
			}
			
			var tabDiv = document.createElement('DIV');		
			var firstDiv = this.DHTMLSuite_tabObj.getElementsByTagName('DIV')[0];	
			
			this.DHTMLSuite_tabObj.insertBefore(tabDiv,firstDiv);	
			tabDiv.className = 'DHTMLSuite_tabContainer';			
			this.tabView_countTabs = 0;
			var tmpTabTitles = this.tabTitles;	// tmpTab titles set to current tab titles - this variable is used in the loop below
												// We don't want to loop through all the tab titles in the object when we add a new one manually.
			
		}else{	// A new tab being created dynamically afterwards.
			var tabDiv = this.DHTMLSuite_tabObj.getElementsByTagName('DIV')[0];
			var firstDiv = this.DHTMLSuite_tabObj.getElementsByTagName('DIV')[1];
			this.initActiveTabIndex = this.tabView_countTabs;		
			var tmpTabTitles = Array(nameOfAdditionalTab);	// tmpTab titles set to only the new tab
		}		
		
		
		
		for(var no=0;no<tmpTabTitles.length;no++){
			var aTab = document.createElement('DIV');
			aTab.id = 'tabTab' + this.tabSetParentId + "_" +  (no + this.tabView_countTabs);
			aTab.onmouseover = this.__rolloverTab;
			aTab.onmouseout = this.__rolloutTab;
			aTab.setAttribute('parentRefId',this.tabSetParentId);
			aTab.parentRefId = this.tabSetParentId;
			var numIndex = window.refToThisTabSet.outsideObjectRefIndex+'';
			aTab.onclick = function() { window.refToThisTabSet.__tabClick(this,numIndex); };
			DHTMLSuite.commonObj.__addEventElement(aTab);
			aTab.className='tabInactive';
			aTab.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_inactive.gif' + '\')';
			tabDiv.appendChild(aTab);
			var span = document.createElement('SPAN');
			span.innerHTML = tmpTabTitles[no];
			aTab.appendChild(span);

			if(this.closeButtons[no] || additionalCloseButton){
				var closeButton = document.createElement('IMG');
				closeButton.src = DHTMLSuite.configObj.imagePath + 'tab-view-close.gif';
				closeButton.style.position='absolute';
				closeButton.style.top = '4px';
				closeButton.style.right = '2px';
				closeButton.onmouseover = this.__mouseOverEffectCloseButton;
				closeButton.onmouseout = this.__mouseOutEffectForCloseButton;
				DHTMLSuite.commonObj.__addEventElement(closeButton);
				span.innerHTML = span.innerHTML + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';	
				
				var deleteTxt = span.innerHTML+'';

				// function() { window.refToThisTabSet.__tabClick(this,numIndex); };
				closeButton.onclick = function(){ refToTabViewObjects[numIndex].deleteTab( this.parentNode.innerHTML) };
				span.appendChild(closeButton);
			}
						
			var img = document.createElement('IMG');
			img.valign = 'bottom';
			img.src = DHTMLSuite.configObj.imagePath + 'tab_right_inactive.gif';
			// IE5.X FIX
			if((DHTMLSuite.clientInfoObj.navigatorVersion && DHTMLSuite.clientInfoObj.navigatorVersion<6) || (DHTMLSuite.clientInfoObj.isMSIE && !this.strictDocType)){
				img.style.styleFloat = 'none';
				img.style.position = 'relative';	
				img.style.top = '4px'
				span.style.paddingTop = '4px';
				aTab.style.cursor = 'hand';
			}	// End IE5.x FIX
			aTab.appendChild(img);
		}

		var tabs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');
		var divCounter = 0;
		for(var no=0;no<tabs.length;no++){
			if(tabs[no].className=='DHTMLSuite_aTab' && tabs[no].parentNode == this.DHTMLSuite_tabObj){
				if(this.height.length>0){
					if(this.height.indexOf('%')==-1){
						var tmpHeight = (this.height.replace('px','')/1 - 22);
						tabs[no].style.height = tmpHeight + 'px';
					}else
						tabs[no].style.height = this.height;
				}
				tabs[no].style.display='none';
				tabs[no].id = 'tabView' + this.tabSetParentId + "_" + divCounter;
				divCounter++;
			}			
		}	
		this.tabView_countTabs = this.tabView_countTabs + this.tabTitles.length;	
		this.__showTab(this.tabSetParentId,this.initActiveTabIndex,this.outsideObjectRefIndex);

		return this.activeTabIndex;
	}
	// }}}	
	,
	// {{{ __mouseOutEffectForCloseButton()
    /**
     * 
     *
     * @private
     */	    	
	__mouseOutEffectForCloseButton : function()
	{
		this.src = this.src.replace('close-over.gif','close.gif');	
	}	
	// }}}	
	,	
	// {{{ __mouseOverEffectCloseButton()
    /**
     * 
     *
     * @private
     */	    	
	__mouseOverEffectCloseButton : function()
	{
		this.src = this.src.replace('close.gif','close-over.gif');	
	}	
	// }}}	
	,	
	
	// {{{ __fillTabWithContentFromAjax()
    /**
     * 
      * @param Int ajaxIndex = Index of Ajax array
      * @param String objId = Id of element where content from Ajax should be displayed
      * @param Int tabId = Id of element where content from Ajax should be displayed
     *
     * @private
     */	    	
	__fillTabWithContentFromAjax : function(ajaxIndex,objId,tabId)
	{
		var obj = document.getElementById('tabView'+objId + '_' + tabId);
		obj.innerHTML = this.ajaxObjects[ajaxIndex].response;		
	}	
	// }}}	
	,
	// {{{ __resetTabIds()
    /**
     * 
     *
     * @private
     */		
	__resetTabIds : function(parentId)
	{
		var tabTitleCounter = 0;
		var tabContentCounter = 0;		
		var divs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');	

		for(var no=0;no<divs.length;no++){
			if(divs[no].className=='DHTMLSuite_aTab' && divs[no].parentNode==this.DHTMLSuite_tabObj){
				divs[no].id = 'tabView' + parentId + '_' + tabTitleCounter;
				tabTitleCounter++;
			}
			if(divs[no].id.indexOf('tabTab')>=0 && divs[no].parentNode.parentNode==this.DHTMLSuite_tabObj){
				divs[no].id = 'tabTab' + parentId + '_' + tabContentCounter;	
				tabContentCounter++;
			}		
						
		}	
		this.tabView_countTabs = tabContentCounter;
	}
	// }}}	

	,
	// {{{ getTabIndexByTitle()
    /**
     * 
     *
     * @private
     */		
	getTabIndexByTitle : function(tabTitle)
	{
		tabTitle = tabTitle.replace(/(.*?)&nbsp.*$/gi,'$1');
		var divs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');
		
		for(var no=0;no<divs.length;no++){
			if(divs[no].id.indexOf('tabTab')>=0){
				var span = divs[no].getElementsByTagName('SPAN')[0];	
				var spanTitle = span.innerHTML.replace(/(.*?)&nbsp.*$/gi,'$1');
				if(spanTitle == tabTitle){
					var tmpId = divs[no].id.split('_');					
					return tmpId[tmpId.length-1].replace(/[^0-9]/g,'')/1;
				}		
			}
		}
	
		
		return -1;		
	}
	// }}}				
}