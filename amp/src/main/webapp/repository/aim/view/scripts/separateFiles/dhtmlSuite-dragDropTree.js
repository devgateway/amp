if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Drag and drop folder tree
*
*	Created:					August, 23rd, 2006
*	
*	Demos of this class:		demo-drag-drop-folder-tree.html				
*			
* 	Update log:
*
************************************************************************************************************/
	
var JSTreeObj;
var treeUlCounter = 0;
var nodeId = 1;
	
/**
* @constructor
* @class Purpose of class:	Transforms an UL,LI list into a folder tree with drag and drop capabilities(See <a target="_blank" href="../../demos/demo-drag-drop-folder-tree.html">demo</A>).
* @version				1.0
* @version 1.0
* 
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/


DHTMLSuite.JSDragDropTree = function()
{
	var idOfTree;
	var folderImage;
	var plusImage;
	var minusImage;
	var maximumDepth;
	var dragNode_source;
	var dragNode_parent;
	var dragNode_sourceNextSib;
	var dragNode_noSiblings;
	
	var dragNode_destination;
	var floatingContainer;
	var dragDropTimer;
	var dropTargetIndicator;
	var insertAsSub;
	var indicator_offsetX;
	var indicator_offsetX_sub;
	var indicator_offsetY;
	var messageMaximumDepthReached;
	var ajaxObjects;
	var layoutCSS;
	var cookieName;
	
	/* Initial variable values */
	this.folderImage = 'DHTMLSuite_folder.gif';
	this.plusImage = 'DHTMLSuite_plus.gif';
	this.minusImage = 'DHTMLSuite_minus.gif';
	this.maximumDepth = 6;		
	this.layoutCSS = 'drag-drop-folder-tree.css';
	
	this.floatingContainer = document.createElement('UL');
	this.floatingContainer.style.position = 'absolute';
	this.floatingContainer.style.display='none';
	this.floatingContainer.id = 'floatingContainer';
	this.insertAsSub = false;
	document.body.appendChild(this.floatingContainer);
	this.dragDropTimer = -1;
	this.dragNode_noSiblings = false;
	this.cookieName = 'DHTMLSuite_expandedNodes';
	
	if(document.all){
		this.indicator_offsetX = 1;	// Offset position of small black lines indicating where nodes would be dropped.
		this.indicator_offsetX_sub = 3;
		this.indicator_offsetY = 14;
	}else{
		this.indicator_offsetX = 1;	// Offset position of small black lines indicating where nodes would be dropped.
		this.indicator_offsetX_sub = 3;
		this.indicator_offsetY = 5;			
	}
	if(navigator.userAgent.indexOf('Opera')>=0){
		this.indicator_offsetX = 2;	// Offset position of small black lines indicating where nodes would be dropped.
		this.indicator_offsetX_sub = 3;
		this.indicator_offsetY = -7;				
	}

	this.messageMaximumDepthReached = ''; // Use '' if you don't want to display a message 
	this.ajaxObjects = new Array();
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods  
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	var objectIndex;
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
		
	
}


/* DHTMLSuite.JSDragDropTree class */
DHTMLSuite.JSDragDropTree.prototype = {

	// {{{ init()
    /**
     * Initializes the script
     *
     * @public
     */	
    	
	init : function()
	{
		
		var ind = this.objectIndex;
		
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		JSTreeObj = this;
		this.__createDropIndicator();
		
		if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
		
		document.documentElement.ondragstart = document.documentElement.ondragstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
		DHTMLSuite.commonObj.__addEventElement(document.documentElement);
		var nodeId = 0;
		var DHTMLSuite_tree = document.getElementById(this.idOfTree);
		var menuItems = DHTMLSuite_tree.getElementsByTagName('LI');	// Get an array of all menu items
		for(var no=0;no<menuItems.length;no++){
			// No children var set ?
			var noChildren = false;
			var tmpVar = menuItems[no].getAttribute('noChildren');
			if(!tmpVar)tmpVar = menuItems[no].noChildren;
			if(tmpVar=='true')noChildren=true;
			// No drag var set ?
			var noDrag = false;
			var tmpVar = menuItems[no].getAttribute('noDrag');
			if(!tmpVar)tmpVar = menuItems[no].noDrag;
			if(tmpVar=='true')noDrag=true;
					 
			nodeId++;
			var subItems = menuItems[no].getElementsByTagName('UL');
			var img = document.createElement('IMG');
			img.src = DHTMLSuite.configObj.imagePath + this.plusImage;
			img.onclick = function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].showHideNode(e); };
			DHTMLSuite.commonObj.__addEventElement(img);
			if(subItems.length==0)img.style.visibility='hidden';else{
				subItems[0].id = 'tree_ul_' + treeUlCounter;
				treeUlCounter++;
			}
			var aTag = menuItems[no].getElementsByTagName('A')[0];

			if(!noDrag)aTag.onmousedown = this.__initializeDragProcess;
			if(!noChildren){
				// aTag.onmousemove = this.__moveDragableNodes;
				DHTMLSuite.commonObj.addEvent(aTag,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragableNodes(e,'text'); });
				
			}
			DHTMLSuite.commonObj.__addEventElement(aTag);
			menuItems[no].insertBefore(img,aTag);
			menuItems[no].id = 'DHTMLSuite_treeNode' + nodeId;
			var folderImg = document.createElement('IMG');
			if(!noDrag)folderImg.onmousedown = this.__initializeDragProcess;
			if(!noChildren){
				DHTMLSuite.commonObj.addEvent(folderImg,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragableNodes(e,'folder'); });
			}
			if(menuItems[no].className){
				folderImg.src = DHTMLSuite.configObj.imagePath + menuItems[no].className;
			}else{
				folderImg.src = DHTMLSuite.configObj.imagePath + this.folderImage;
			}
			DHTMLSuite.commonObj.__addEventElement(folderImg);
			menuItems[no].insertBefore(folderImg,aTag);
		}	
		
	
		initExpandedNodes = DHTMLSuite.commonObj.getCookie(this.cookieName);
		if(initExpandedNodes){
			var nodes = initExpandedNodes.split(',');
			for(var no=0;no<nodes.length;no++){
				if(nodes[no])this.showHideNode(false,nodes[no]);	
			}			
		}			
		
		DHTMLSuite.commonObj.addEvent(document.documentElement,"mousemove",DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex].__moveDragableNodes);
		DHTMLSuite.commonObj.addEvent(document.documentElement,"mouseup",DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex].__dropDragableNodes);

	}	
	// }}}
	,
	// {{{ setCookieName()
    /**
     * set new CSS file
     *
     * @param String cookieName - Name of cookie (storing expanded nodes ) default value : DHTMLSuite_expandedNodes
     *
     * @public
     */		
	setCookieName : function(cookieName)
	{
		this.cookieName = cookieName;
	}
	// }}}		
	,
	// {{{ setLayoutCss()
    /**
     * set new CSS file
     *
     * @param String cssFileName - name of new css file(example: drag-drop.css). Has to be set before init is called. 
     *
     * @public
     */	
	setLayoutCss : function(cssFileName)
	{
		this.layoutCSS = cssFileName;	
	}	
	// }}}	
	,
	// {{{ setFolderImage()
    /**
     * set new folder image file
     *
     * @param String newFolderImage - name of folder image(example: folder.gif). Has to be set before init is called. 
     *
     * @public
     */		
	setFolderImage : function(newFolderImage)
	{
		this.folderImage = newFolderImage;
	}
	// }}}	
	,
	// {{{ setPlusImage()
    /**
     * set new CSS file
     *
     * @param String newPlusImage - name of new [+] image(example: plus.gif). Has to be set before init is called. 
     *
     * @public
     */		
	setPlusImage : function(newPlusImage)
	{
		this.plusImage = newPlusImage;
	}
	// }}}	
	,
	// {{{ setMinusImage()
    /**
     * set new plus imagee
     *
     * @param String newMinusImage - name of new [-] image(example: minus.gif). Has to be set before init is called. 
     *
     * @public
     */		
	setMinusImage : function(newMinusImage)
	{
		this.minusImage = newMinusImage;
	}
	// }}}	
	,
	// {{{ setMaximumDepth()
    /**
     * set maximum depth of tree. 
     *
     * @param Int maxDepth - new maximum depth of tree. 
     *
     * @public
     */		
	setMaximumDepth : function(maxDepth)
	{
		this.maximumDepth = maxDepth;	
	}
	,setMessageMaximumDepthReached : function(newMessage)
	{
		this.messageMaximumDepthReached = newMessage;
	}
	// }}}	
	,	
	// {{{ setTreeId()
    /**
     * set ID of tree root element
     *
     * @param String idOfTree - Id of UL tag which is root element of the tree. 
     *
     * @public
     */			
	setTreeId : function(idOfTree)
	{
		this.idOfTree = idOfTree;			
	}
	// }}}		
	,
	// {{{ expandAll()
    /**
     * Expand all tree nodes
     *
     *
     * @public
     */		
	expandAll : function()
	{
		var menuItems = document.getElementById(this.idOfTree).getElementsByTagName('LI');
		for(var no=0;no<menuItems.length;no++){
			var subItems = menuItems[no].getElementsByTagName('UL');
			if(subItems.length>0 && subItems[0].style.display!='block'){
				this.showHideNode(false,menuItems[no].id);
			}			
		}
	}
	// }}}		
	,
	// {{{ collapseAll()
    /**
     * Collapse all tree nodes
     *
     *
     * @public
     */		
	collapseAll : function()
	{
		var menuItems = document.getElementById(this.idOfTree).getElementsByTagName('LI');
		for(var no=0;no<menuItems.length;no++){
			var subItems = menuItems[no].getElementsByTagName('UL');
			if(subItems.length>0 && subItems[0].style.display=='block'){
				this.showHideNode(false,menuItems[no].id);
			}			
		}		
	}	
	// }}}	
	,
	// {{{ showHideNode()
    /**
     * Expand a specific node
     *
     * @param boolean e - If you call this method manually, set this argument to false(It's not used)
     * @param string inputId - Id of node to expand/collapse
     *
     * @public
     */	
	showHideNode : function(e,inputId)
	{
		if(inputId){
			if(!document.getElementById(inputId))return;
			thisNode = document.getElementById(inputId).getElementsByTagName('IMG')[0]; 
		}else {
			if(document.all)e = event;	
			var srcEl = DHTMLSuite.commonObj.getSrcElement(e);
			thisNode = srcEl;
			if(srcEl.tagName=='A')thisNode = srcEl.parentNode.getElementsByTagName('IMG')[0];	
			
		}
		if(thisNode==null) return;
		if(thisNode.style.visibility=='hidden')return;		
		var parentNode = thisNode.parentNode;
		inputId = parentNode.id.replace(/[^0-9]/g,'');
		if(thisNode.src.indexOf(this.plusImage)>=0){
			thisNode.src = thisNode.src.replace(this.plusImage,this.minusImage);
			var ul = parentNode.getElementsByTagName('UL')[0];
			ul.style.display='block';
			if(!initExpandedNodes)initExpandedNodes = ',';
			if(initExpandedNodes.indexOf(',' + inputId + ',')<0) initExpandedNodes = initExpandedNodes + inputId + ',';
		}else{
			thisNode.src = thisNode.src.replace(this.minusImage,this.plusImage);
			parentNode.getElementsByTagName('UL')[0].style.display='none';
			initExpandedNodes = initExpandedNodes.replace(',' + inputId,'');
		}	
		DHTMLSuite.commonObj.setCookie(this.cookieName,initExpandedNodes,500);			
		return false;						
	}
	// }}}	
	,
	// {{{ getSaveString()
    /**
     * Return save string 
     * 
     * @param Object initObj - Only for private use inside the method
     * @param String saveString - Only for private use inside the method - you should call this method without arguments.
     *
     * @return String saveString - A string with the format id-parentId,id-parentId,id-parentId
     * @type String
     * @public
     */		
	getSaveString : function(initObj,saveString)
	{
		
		if(!saveString)var saveString = '';
		if(!initObj){
			initObj = document.getElementById(this.idOfTree);

		}
		var lis = initObj.getElementsByTagName('LI');

		if(lis.length>0){
			var li = lis[0];
			while(li){
				if(li.id){
					if(saveString.length>0)saveString = saveString + ',';

					saveString = saveString + li.id.replace(/[^0-9]/gi,'');
					saveString = saveString + '-';
					if(li.parentNode.id!=this.idOfTree)saveString = saveString + li.parentNode.parentNode.id.replace(/[^0-9]/gi,''); else saveString = saveString + '0';
					
					var ul = li.getElementsByTagName('UL');
					if(ul.length>0){
						saveString = this.getSaveString(ul[0],saveString);	
					}	
				}			
				li = li.nextSibling;
			}
		}

		if(initObj.id == this.idOfTree){
			return saveString;						
		}
		return saveString;
	}
	// }}}	
	,	
	// {{{ __initializeDragProcess()
    /**
     * Init a drag process
     *
     * @param event e = Event object
     * @private
     */		
	__initializeDragProcess : function(e)
	{
		if(document.all)e = event;	
		
		var subs = JSTreeObj.floatingContainer.getElementsByTagName('LI');
		if(subs.length>0){
			if(JSTreeObj.dragNode_sourceNextSib){
				JSTreeObj.dragNode_parent.insertBefore(JSTreeObj.dragNode_source,JSTreeObj.dragNode_sourceNextSib);
			}else{
				JSTreeObj.dragNode_parent.appendChild(JSTreeObj.dragNode_source);
			}					
		}
		
		JSTreeObj.dragNode_source = this.parentNode;
		JSTreeObj.dragNode_parent = this.parentNode.parentNode;
		JSTreeObj.dragNode_sourceNextSib = false;

		
		if(JSTreeObj.dragNode_source.nextSibling)JSTreeObj.dragNode_sourceNextSib = JSTreeObj.dragNode_source.nextSibling;
		JSTreeObj.dragNode_destination = false;
		JSTreeObj.dragDropTimer = 0;
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
		JSTreeObj.__waitBeforeDragProcessStarts();
		return false;
	}
	// }}}	
	,
	// {{{ __waitBeforeDragProcessStarts()
    /**
     * A small delay before drag is started
     *
     * @private
     */		
	__waitBeforeDragProcessStarts : function()
	{	
		if(this.dragDropTimer>=0 && this.dragDropTimer<10){
			this.dragDropTimer = this.dragDropTimer + 1;
			setTimeout('JSTreeObj.__waitBeforeDragProcessStarts()',20);
			return;
		}
		if(this.dragDropTimer==10)
		{
			JSTreeObj.floatingContainer.style.display='block';
			JSTreeObj.floatingContainer.appendChild(JSTreeObj.dragNode_source);	
		}
	}
	// }}}	
	,
	// {{{ __moveDragableNodes()
    /**
     * Move dragable nodes
     * @param event e - Event object
     *
     * @private
     */		
	__moveDragableNodes : function(e,tagType)
	{
		if(JSTreeObj.dragDropTimer<10)return;
		if(document.all)e = event;
		dragDrop_x = e.clientX/1 + 5 + document.body.scrollLeft;
		dragDrop_y = e.clientY/1 + 5 + document.documentElement.scrollTop;	
				
		JSTreeObj.floatingContainer.style.left = dragDrop_x + 'px';
		JSTreeObj.floatingContainer.style.top = dragDrop_y + 'px';
		
		var thisObj = DHTMLSuite.commonObj.getSrcElement(e);
		var thisObjOrig = DHTMLSuite.commonObj.getSrcElement(e);
		if(thisObj.tagName=='A' || thisObj.tagName=='IMG')thisObj = thisObj.parentNode;

		JSTreeObj.dragNode_noSiblings = false;
		var tmpVar = thisObj.getAttribute('noSiblings');
		if(!tmpVar)tmpVar = thisObj.noSiblings;
		if(tmpVar=='true')JSTreeObj.dragNode_noSiblings=true;
				
		if(thisObj && tagType)
		{
			JSTreeObj.dragNode_destination = thisObj;
			var img = thisObj.getElementsByTagName('IMG')[1];
			var tmpObj= JSTreeObj.dropTargetIndicator;
			tmpObj.style.display='block';
			
			var eventSourceObj = thisObjOrig;
			if(JSTreeObj.dragNode_noSiblings && eventSourceObj.tagName=='IMG')eventSourceObj = eventSourceObj.nextSibling;
			
			var tmpImg = tmpObj.getElementsByTagName('IMG')[0];
			if(thisObjOrig.tagName=='A' || JSTreeObj.dragNode_noSiblings){
				tmpImg.src = tmpImg.src.replace('ind1','ind2');	
				JSTreeObj.insertAsSub = true;
				tmpObj.style.left = (DHTMLSuite.commonObj.getLeftPos(eventSourceObj) + JSTreeObj.indicator_offsetX_sub) + 'px';
			}else{
				
				tmpImg.src = tmpImg.src.replace('ind2','ind1');
				JSTreeObj.insertAsSub = false;
				tmpObj.style.left = (DHTMLSuite.commonObj.getLeftPos(eventSourceObj) + JSTreeObj.indicator_offsetX) + 'px';
			}
			
			
			tmpObj.style.top = (DHTMLSuite.commonObj.getTopPos(thisObj) + JSTreeObj.indicator_offsetY) + 'px';
		}
		
		return false;
		
	}
	// }}}	
	,
	// {{{ __dropDragableNodes()
    /**
     * Drag process ended - drop nodes
     *
     * @private
     */		
	__dropDragableNodes:function()
	{
		if(JSTreeObj.dragDropTimer<10){				
			JSTreeObj.dragDropTimer = -1;
			DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
			return;
		}
		var showMessage = false;
		if(JSTreeObj.dragNode_destination){	// Check depth
			var countUp = JSTreeObj.__getDepthOfABranchInTheTree(JSTreeObj.dragNode_destination,'up');
			var countDown = JSTreeObj.__getDepthOfABranchInTheTree(JSTreeObj.dragNode_source,'down');
			var countLevels = countUp/1 + countDown/1 + (JSTreeObj.insertAsSub?1:0);		
			
			if(countLevels>JSTreeObj.maximumDepth){
				JSTreeObj.dragNode_destination = false;
				showMessage = true; 	// Used later down in this function
			}
		}
		
		
		if(JSTreeObj.dragNode_destination){			
			if(JSTreeObj.insertAsSub){
				var uls = JSTreeObj.dragNode_destination.getElementsByTagName('UL');
				if(uls.length>0){
					ul = uls[0];
					ul.style.display='block';
					
					var lis = ul.getElementsByTagName('LI');

					if(lis.length>0){	// Sub elements exists - drop dragable node before the first one
						ul.insertBefore(JSTreeObj.dragNode_source,lis[0]);	
					}else {	// No sub exists - use the appendChild method - This line should not be executed unless there's something wrong in the HTML, i.e empty <ul>
						ul.appendChild(JSTreeObj.dragNode_source);	
					}
				}else{
					var ul = document.createElement('UL');
					ul.style.display='block';
					JSTreeObj.dragNode_destination.appendChild(ul);
					ul.appendChild(JSTreeObj.dragNode_source);
				}
				var img = JSTreeObj.dragNode_destination.getElementsByTagName('IMG')[0];					
				img.style.visibility='visible';
				img.src = img.src.replace(JSTreeObj.plusImage,JSTreeObj.minusImage);					
				
				
			}else{
				if(JSTreeObj.dragNode_destination.nextSibling){
					var nextSib = JSTreeObj.dragNode_destination.nextSibling;
					nextSib.parentNode.insertBefore(JSTreeObj.dragNode_source,nextSib);
				}else{
					JSTreeObj.dragNode_destination.parentNode.appendChild(JSTreeObj.dragNode_source);
				}
			}	
			/* Clear parent object */
			var tmpObj = JSTreeObj.dragNode_parent;
			var lis = tmpObj.getElementsByTagName('LI');
			if(lis.length==0){
				var img = tmpObj.parentNode.getElementsByTagName('IMG')[0];
				img.style.visibility='hidden';	// Hide [+],[-] icon
				tmpObj.parentNode.removeChild(tmpObj);						
			}
			
		}else{
			// Putting the item back to it's original location
			
			if(JSTreeObj.dragNode_sourceNextSib){
				JSTreeObj.dragNode_parent.insertBefore(JSTreeObj.dragNode_source,JSTreeObj.dragNode_sourceNextSib);
			}else{
				JSTreeObj.dragNode_parent.appendChild(JSTreeObj.dragNode_source);
			}			
				
		}
		JSTreeObj.dropTargetIndicator.style.display='none';		
		JSTreeObj.dragDropTimer = -1;	
		DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
		if(showMessage && JSTreeObj.messageMaximumDepthReached)alert(JSTreeObj.messageMaximumDepthReached);
	}
	// }}}	
	,
	// {{{ __createDropIndicator()
    /**
     * Create small black lines indicating where items will be dropped
     *
     * @private
     */		
	__createDropIndicator : function()
	{
		this.dropTargetIndicator = document.createElement('DIV');
		this.dropTargetIndicator.style.zIndex = 240000;
		this.dropTargetIndicator.style.position = 'absolute';
		this.dropTargetIndicator.style.display='none';			
		var img = document.createElement('IMG');
		img.src = DHTMLSuite.configObj.imagePath + 'dragDrop_ind1.gif';
		img.id = 'dragDropIndicatorImage';
		this.dropTargetIndicator.appendChild(img);
		document.body.appendChild(this.dropTargetIndicator);
		
	}
	// }}}	
	,
	// {{{ __getDepthOfABranchInTheTree()
    /**
     * Count depth of a branch
     *
     * @private
     */		
	__getDepthOfABranchInTheTree : function(obj,direction,stopAtObject){
		var countLevels = 0;
		if(direction=='up'){
			while(obj.parentNode && obj.parentNode!=stopAtObject){
				obj = obj.parentNode;
				if(obj.tagName=='UL')countLevels = countLevels/1 +1;
			}		
			return countLevels;
		}	
		
		if(direction=='down'){ 
			var subObjects = obj.getElementsByTagName('LI');
			for(var no=0;no<subObjects.length;no++){
				countLevels = Math.max(countLevels,JSTreeObj.__getDepthOfABranchInTheTree(subObjects[no],"up",obj));
			}
			return countLevels;
		}	
	}	
	// }}}		
	,
	// {{{ __cancelSelectionEvent()
    /**
     * Cancel selection when drag is in process
     *
     * @private
     */		
	__cancelSelectionEvent : function()
	{		
		if(JSTreeObj.dragDropTimer<10)return true;
		return false;	
	}
	// }}}	

}