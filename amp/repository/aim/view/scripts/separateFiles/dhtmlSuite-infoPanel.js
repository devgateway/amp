if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML dynamic tooltip script
*
*	Created:						September, 26th, 2006
*	@class Purpose of class:		Transforms a regular div into a expandable info pane.
*			
*	Css files used by this script:	info-pane.css
*
*	Demos of this class:			demo-info-pane-1.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Transforms a regular div into a expandable info pane. (<a href="../../demos/demo-info-pane-1.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

	
DHTMLSuite.infoPanel = function()
{
	
	var xpPanel_slideActive;			// Slide down/up active?
	var xpPanel_slideSpeed ;			// Speed of slide
	var xpPanel_onlyOneExpandedPane;	// Only one pane expanded at a time ?	
	var savedActivePane;
	var savedActiveSub;
	var xpPanel_currentDirection;	
	var cookieNames;
	var layoutCSS;
	var arrayOfPanes;
	var dynamicContentObj;
	var paneHeights;					// Array of info pane heights.
	
	var currentlyExpandedPane = false;
	this.xpPanel_slideActive = true;	// Slide down/up active?
	this.xpPanel_slideSpeed = 20;	// Speed of slide
	this.xpPanel_onlyOneExpandedPane = false;	// Only one pane expanded at a time ?	
	this.savedActivePane = false;
	this.savedActiveSub = false;
	this.xpPanel_currentDirection = new Array();	
	this.cookieNames = new Array();	
	this.currentlyExpandedPane = false;
//	this.layoutCSS = 'info-pane.css';	// Default css file for this widget.
	this.layoutCSS = '/repository/aim/view/css/css_dhtmlsuite/info-pane.css';	// Default css file for this widget.
	this.arrayOfPanes = new Array();
	this.paneHeights = new Array();
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	try{
		this.dynamicContentObj = new DHTMLSuite.dynamicContent();
	}catch(e){
		alert('You need to include dhtmlSuite-dynamicContent.js');
	}
			
	
		
		
	
}

	
	
DHTMLSuite.infoPanel.prototype = {	
	// {{{ addPane()
    /**
     *	Define a pane.
     * 	
     *	@param String idOfPane = Id of the element you want to transfor into a info pane
     *  @param String labelOfPane = The label you want to set for this pane
     *  @param Boolean State = Initial state of pane, expanded or collapsed(true = Expanded, false = collapsed)
     *  @param nameOfCookie = Name of cookie for this pane, i.e. saving states
     *  @param Int width = Width of pane(Optional)
     *
     * @public	
     */		
	addPane : function(idOfPane,labelOfPane,state,nameOfCookie,width)
	{
		var index = this.arrayOfPanes.length;
		this.arrayOfPanes[index] = [idOfPane,labelOfPane,state,nameOfCookie,width];
	}
	// }}}	
	,
	// {{{ addContentToPane()
    /**
     *	Replace content inside a pane with content from an external file.
     * 	
     *	@param String idOfPane = Id of the element you want to transfor into a info pane
     *  @param String pathToExternalFile = Relative path to file. The content of this file will be placed inside the info pane.
     *
     * @public	
     */		
	addContentToPane : function(idOfPane,pathToExternalFile)
	{
		var obj = document.getElementById(idOfPane);
		var subDivs = obj.getElementsByTagName('DIV');
		for(var no=0;no<subDivs.length;no++){
			if(subDivs[no].className=='DHTMLSuite_infoPaneContent'){
				window.refToThisPane = this;		
				this.__slidePane(this.xpPanel_slideSpeed,subDivs[no].id);			
				this.dynamicContentObj.loadContent(subDivs[no].id,pathToExternalFile,"window.refToThisPane.__resizeAndRepositionPane('" + idOfPane + "')");					
				if(subDivs[no].parentNode.style.display=='none' || subDivs[no].parentNode.style.height=='0px'){	// Pane is collapsed, expand it
					var topBarObj = DHTMLSuite.domQueryObj.getElementsByClassName('DHTMLSuite_infoPaneTopBar',subDivs[no].parentNode.parentNode);
					this.__showHidePaneContent(topBarObj[0]);
				}	
				return;	
			}		
		}
	}
	// }}}	
	,
	// {{{ addStaticContentToPane()
    /**
     *	Replace content inside a pane with some new static content.
     * 	
     *	@param String idOfPane = Id of the element you want to transfor into a info pane
     *  @param String newContent = New content. (Static html).
     *
     * @public	
     */		
	addStaticContentToPane : function(idOfPane,newContent)
	{
		var obj = document.getElementById(idOfPane);
		var subDivs = obj.getElementsByTagName('DIV');
		for(var no=0;no<subDivs.length;no++){
			if(subDivs[no].className=='DHTMLSuite_infoPaneContent'){			
				window.refToThisPane = this;		
				this.__slidePane(this.xpPanel_slideSpeed,subDivs[no].id);			
				subDivs[no].innerHTML = newContent;						
				if(subDivs[no].parentNode.style.display=='none' || subDivs[no].parentNode.style.height=='0px'){	// Pane is collapsed, expand it
					var topBarObj = DHTMLSuite.domQueryObj.getElementsByClassName('DHTMLSuite_infoPaneTopBar',subDivs[no].parentNode.parentNode);
					this.__showHidePaneContent(topBarObj[0]);
				}	
				this.__resizeAndRepositionPane(idOfPane);
				return;		
			}		
		}
	}
	// }}}	
	,	
	// {{{ init()
    /**
     *	Initializes the script. This method should be called after you have added all your panes.
     * 	
     *	@param Object inputobject = Reference to element on web page.
     *
     * @public	
     */	
	init : function()
	{
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		
		for(var no=0;no<this.arrayOfPanes.length;no++){		// Loop through panes	
			var tmpDiv = document.getElementById(this.arrayOfPanes[no][0]);	// Creating reference to pane div.
			tmpDiv.className = 'DHTMLSuite_panel';	// Assigning it to class DHTMLSuite_panel
			var panelTitle = this.arrayOfPanes[no][1];	
			var panelDisplayed = this.arrayOfPanes[no][2];
			var nameOfCookie = this.arrayOfPanes[no][3];
			var widthOfPane = this.arrayOfPanes[no][4];
					
			if(widthOfPane)tmpDiv.style.width = widthOfPane;
			
			var outerContentDiv = document.createElement('DIV');	
			var contentDiv = tmpDiv.getElementsByTagName('DIV')[0];
			contentDiv.className = 'DHTMLSuite_infoPaneContent';
			contentDiv.id = 'infoPaneContent' + no;
			outerContentDiv.appendChild(contentDiv);	
			this.cookieNames[this.cookieNames.length] = nameOfCookie;
			
			outerContentDiv.id = 'paneContent' + no;
			outerContentDiv.className = 'DHTMLSuite_panelContent';
			outerContentDiv.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'xp-info-pane-bg_pane_right.gif' + '\')';			
			
			var topBar = document.createElement('DIV');
			topBar.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
			DHTMLSuite.commonObj.__addEventElement(topBar);
			var span = document.createElement('SPAN');				
			span.innerHTML = panelTitle;
			topBar.appendChild(span);
			topBar.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'xp-info-pane-bg_panel_top_right.gif' + '\')';
			window.refToXpPane = this;
			topBar.onclick = function(){ window.refToXpPane.__showHidePaneContent(this) };
			if(document.all)topBar.ondblclick = function(){ window.refToXpPane.__showHidePaneContent(this) };;
			topBar.onmouseover = this.__mouseoverTopbar;	// Adding mouseover effect to heading
			topBar.onmouseout = this.__mouseoutTopbar;	// Adding mouseout effect to heading
			topBar.style.position = 'relative';	// Relative positioning of heading

			var img = document.createElement('IMG');	// Adding arrow image
			img.id = 'showHideButton' + no;
			img.src = DHTMLSuite.configObj.imagePath + 'xp-info-pane-arrow_up.gif';				
			topBar.appendChild(img);
			
			if(nameOfCookie){	// Cookie defined?
				cookieValue =  DHTMLSuite.commonObj.getCookie(nameOfCookie);	
				if(cookieValue)panelDisplayed = cookieValue==1?true:false; // Cookie value exists? -> Expand or collapse pane.
				
			}
			
			if(!panelDisplayed){	// Hide pane initially.
				outerContentDiv.style.height = '0px';
				contentDiv.style.top = 0 - contentDiv.offsetHeight + 'px';
				if(document.all)outerContentDiv.style.display='none';
				img.src = DHTMLSuite.configObj.imagePath + 'xp-info-pane-arrow_down.gif';
			}
							
			topBar.className='DHTMLSuite_infoPaneTopBar';
			topBar.id = 'infoPane_topBar' + no;
			tmpDiv.appendChild(topBar);				
			tmpDiv.appendChild(outerContentDiv);	
		}
	}		
	// }}}			
	,	
	// {{{ __resizeAndRepositionPane()
    /**
     *	Fixes the layout of a pane after content has been added to it dynamically.
     * 	
     *	@param String idOfPane = Id of the pane
     *
     * @private	
     */		
	__resizeAndRepositionPane : function(idOfPane)
	{
		var obj = document.getElementById(idOfPane);
		var subDivs = obj.getElementsByTagName('DIV');
		for(var no=0;no<subDivs.length;no++){
			if(subDivs[no].className=='DHTMLSuite_panelContent'){	
				subDivs[no].style.overflow = 'auto';	
				subDivs[no].style.height = '';		
				var contentDiv = subDivs[no].getElementsByTagName('DIV')[0];				
				var tmpHeight = subDivs[no].clientHeight;
				tmpHeight = subDivs[no].offsetHeight;				
				subDivs[no].style.height = tmpHeight + 'px';
				if(tmpHeight)this.paneHeights[subDivs[no].id] = tmpHeight;
				subDivs[no].style.top = '0px';
				subDivs[no].style.overflow = 'hidden';
				var subSub = subDivs[no].getElementsByTagName('DIV')[0];
				subSub.style.top = '0px';
			}		
		}
	}
	// }}}	
	,	
	// {{{ __showHidePaneContent()
    /**
     *	Expand or collapse a frame
     * 	
     *	@param Object inputobject = Reference to element on web page.
     *	@param String methodWhenFinished = Method to execute when slide is finished(optional)
     *
     * @private	
     */	
	__showHidePaneContent : function(inputObj,methodWhenFinished)
	{
		var img = inputObj.getElementsByTagName('IMG')[0];
		var numericId = img.id.replace(/[^0-9]/g,'');
		var obj = document.getElementById('paneContent' + numericId);
		if(img.src.toLowerCase().indexOf('up')>=0){
			this.currentlyExpandedPane = false;
			img.src = img.src.replace('up','down');
			if(this.xpPanel_slideActive){
				obj.style.display='block';
				this.xpPanel_currentDirection[obj.id] = (this.xpPanel_slideSpeed*-1);
				this.__slidePane((this.xpPanel_slideSpeed*-1), obj.id,methodWhenFinished);
			}else{
				obj.style.display='none';
			}
			if(this.cookieNames[numericId])DHTMLSuite.commonObj.setCookie(this.cookieNames[numericId],'0',100000);
		}else{
			if(inputObj){
				if(this.currentlyExpandedPane && this.xpPanel_onlyOneExpandedPane)this.__showHidePaneContent(this.currentlyExpandedPane);
				this.currentlyExpandedPane = inputObj;	
			}
			img.src = img.src.replace('down','up');
			if(this.xpPanel_slideActive){
				if(document.all){
					obj.style.display='block';
				}
				this.xpPanel_currentDirection[obj.id] = this.xpPanel_slideSpeed;
				this.__slidePane(this.xpPanel_slideSpeed,obj.id,methodWhenFinished);
			}else{
				obj.style.display='block';
				subDiv = obj.getElementsByTagName('DIV')[0];
				obj.style.height = subDiv.offsetHeight + 'px';
			}
			if(this.cookieNames[numericId])DHTMLSuite.commonObj.setCookie(this.cookieNames[numericId],'1',100000);
		}	
		return true;	
	}
	// }}}	
	,	
	// {{{ __slidePane()
    /**
     *	Animating expand/collapse
     * 	
     *	@param Int slideValue = Positive or negative value, positive when expanding, negative when collapsing
     *  @param String id = Id of the pane currently being expanded/collapsed.
     *	@param String methodWhenFinished = Method to execute when slide is finished(optional)
     *
     * @private	
     */		
	__slidePane : function(slideValue,id,methodWhenFinished)
	{
		if(slideValue!=this.xpPanel_currentDirection[id]){
			return false;
		}
		var activePane = document.getElementById(id);
		if(activePane==this.savedActivePane){
			var subDiv = this.savedActiveSub;
		}else{
			var subDiv = activePane.getElementsByTagName('DIV')[0];
		}
		this.savedActivePane = activePane;
		this.savedActiveSub = subDiv;
		
		var height = activePane.offsetHeight;
		var innerHeight = subDiv.offsetHeight;
		if(this.paneHeights[activePane.id])innerHeight = this.paneHeights[activePane.id];
		height+=slideValue;
		if(height<0)height=0;
		if(height>innerHeight)height = innerHeight;
		
		if(document.all){
			activePane.style.filter = 'alpha(opacity=' + Math.round((height / innerHeight)*100) + ')';
		}else{
			var opacity = (height / innerHeight);
			if(opacity==0)opacity=0.01;
			if(opacity==1)opacity = 0.99;
			activePane.style.opacity = opacity;
		}			
		window.refToThisInfoPane = this;
		if(slideValue<0){			
			activePane.style.height = height + 'px';
			subDiv.style.top = height - innerHeight + 'px';
			if(height>0){
				setTimeout('window.refToThisInfoPane.__slidePane(' + slideValue + ',"' + id + '","' + methodWhenFinished + '")',10);
			}else{
				if(document.all)activePane.style.display='none';
				if(methodWhenFinished)eval(methodWhenFinished);
			}
		}else{			
			subDiv.style.top = height - innerHeight + 'px';
			activePane.style.height = height + 'px';
			if(height<innerHeight){				
				setTimeout('window.refToThisInfoPane.__slidePane(' + slideValue + ',"' + id + '","' + methodWhenFinished + '")',10);				
			}else{
				if(methodWhenFinished)eval(methodWhenFinished);
			}		
		}		
	}
	,
	// {{{ __mouseoverTopbar()
    /**
     *	Toolbar mouse over effect.
     * 	
     *
     * @private	
     */			
	__mouseoverTopbar : function()
	{
		var img = this.getElementsByTagName('IMG')[0];
		var src = img.src;
		img.src = img.src.replace('.gif','_over.gif');
		
		var span = this.getElementsByTagName('SPAN')[0];
		span.style.color='#428EFF';		
		
	}
	// }}}	
	,
	// {{{ __mouseoutTopbar()
    /**
     *	Toolbar mouse out effect.
     * 	
     *
     * @private	
     */			
	__mouseoutTopbar : function()
	{
		var img = this.getElementsByTagName('IMG')[0];
		var src = img.src;
		img.src = img.src.replace('_over.gif','.gif');		
		
		var span = this.getElementsByTagName('SPAN')[0];
		span.style.color='';
	}
}