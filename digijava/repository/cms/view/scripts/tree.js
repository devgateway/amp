function buldTree(xmlDoc, container) {
	var treeSrc = "";
	processedNodes = 0;
	totalNodeCount = xmlDoc.nodeCount;
	treeSrc = recMake (xmlDoc.rootNode);
	container.innerHTML = treeSrc;
}

var expandIcon = new Image();
var collapseIcon = new Image();
var spacer = new Image();
var rootIcon = new Image();
var defaultIcon = new Image();
var commentIcon = new Image();


var expandIconSrc;
var collapseIconSrc;
var spacerSrc;
var rootIconSrc;
var defaultIconSrc;
var commentIconSrc;

function setExpandIconSrc(path){
	expandIconSrc = path;
	expandIcon.src = path;
}
function setCollapseIconSrc(path){
	collapseIconSrc = path;
	collapseIcon.src = path;
}
function setSpacerSrc(path){
	spacerSrc = path;
	spacer.src = path;
}
function setRootIconSrc(path){
	rootIconSrc = path;
	rootIcon.src = path;
}
function setDefaultIconSrc(path){
	defaultIconSrc = path;
	defaultIcon.src = path;
}
function setCommentIconSrc(path){
	commentIconSrc = path;
	commentIconSrc.src = path;
}


var totalNodeCount = 0;
var processedNodes = 0;
var processedProgress = 0;

function notifyTreeBuildProgressChange (val) {
}

//type map
function Type (id, icon) {
	this.id = id;
	this.icon = icon;
	this.iconObject = new Image();
	this.iconObject.src = icon;
	this.addToTagNameAttrVals = [];
	this.skipName = false;
	this.nameExtraSeparator = "'";
	this.useExtraDifferentStyle = true;
}

Type.prototype.addNameExtra = function (attrName) {
	this.addToTagNameAttrVals[this.addToTagNameAttrVals.length] = attrName;
}

var types = [];

function addType (newType) {
	types[types.length] = newType;
}

function getTypeByID (typeId) {
	retVal = null;
	if (types != null && types.length > 0) {
		for (tIndex = 0; tIndex < types.length; tIndex++){
			if (types[tIndex].id == typeId) {
				retVal = types[tIndex];
				break;
			}
		}
	}
	return retVal;
}

//runtime functions
var selItem = null;

function touggle (obj) {
	var childContainer = obj.parentNode.parentNode.parentNode.rows[1].cells[1].childNodes[0];
	if (childContainer.style.display == "inline") {
		obj.src = expandIcon.src;
		childContainer.style.display = "none"
	} else {
		obj.src = collapseIcon.src;
		childContainer.style.display = "inline"
	}
	
}

function expand (obj) {
	var childContainer = obj.parentNode.parentNode.parentNode.rows[1].cells[1].childNodes[0];
	obj.src = collapseIcon.src;
	childContainer.style.display = "inline";
}

function nodeMouseOver (obj){
	if (obj != selItem) {
		obj.className = "treeNodeMouseOver";
	} else {
		obj.className = "treeNodeSelectedMouseOver";
	}
}
function nodeMouseOut (obj){
	if (obj != selItem) {
		obj.className = "treeNodeDefault";
	} else {
		obj.className = "treeNodeSelected";
	}
}

function nodeSelect (obj){
	removeSelected (selItem);
	setSelected (obj)
}

function setSelected (obj){
	selItem = obj;
	obj.className = "treeNodeSelected";
	
	node = xmlDoc.getNodeById(obj.id);
	userSelect (node);	
}

function removeSelected (obj){
	if (selItem != null) {
		selItem.className = "treeNodeDefault";
	}
}

function setSelectedById (id){
	nodeId = "gkTreeNode" + id;
	removeSelected (selItem);
	obj = document.getElementById(nodeId);
	if (obj != null) {
		obj.scrollIntoView(false);
		setSelected (obj);
	}
}


function setSelectedByRealId (id){
	obj = document.getElementById(id);
	removeSelected (selItem);
	if (obj != null) {
	obj.scrollIntoView(false);
		setSelected (obj);
	}
}

function expandByRealId (id){
	obj = document.getElementById(id);
	if (obj != null) {
		upLevel = obj;
		while (upLevel != null) {
		var ownerContainer = obj.parentNode.parentNode.parentNode.parentNode.parentNode;
			if (ownerContainer != null) {
				var collapser = ownerContainer.parentNode.parentNode.parentNode.rows[0].cells[0].childNodes[0];
				var upLevel = ownerContainer.parentNode.parentNode.parentNode.rows[0].cells[2];
				obj = upLevel;
				if (upLevel == null) break;
				if (collapser != null) {
					expand (collapser);
				}
			}
		}
		
	}
}

var skipped = 0;
var skipCount = 0;
function searchTree (attrName, attrValue, skipCountVal) {
	skipCount = skipCountVal;
	curNode = xmlDoc.rootNode;
	skipped = 0;
	retVal = recSearch (curNode, attrName, attrValue);
	return retVal; 
}

function recSearch (parentNode, attrName, attrValue) {
	var retVal = null;
	if (parentNode.children != null && parentNode.children.length > 0) {
	var index = 0;
		for (index = 0; index < parentNode.children.length; index ++) {
			var node = parentNode.children[index];
			var attr = node.getNamedAttribute(attrName);
			
			var matches = false;
			if (attr != null && contains (attr.value, attrValue)) {
				matches = true;
			}
			
			if (matches && skipped == skipCount){
				retVal = node.id;
				break;
			} else if (matches && skipped < skipCount){
				retVal = null;
				skipped ++;			
			} 
			
			if (retVal == null) {
				retVal = recSearch (node, attrName, attrValue)
			}
		}
	}
	return retVal;
}

function contains (srcString, searchString){
	var result = false;
	if (srcString != null && 
		searchString != null && 
		searchString.length <= srcString.length) {
		if (srcString.toLowerCase() == searchString.toLowerCase()) {
			result = true;
		} else {
			var maxOffset = srcString.length - searchString.length;
			for (off = 0; off <= maxOffset; off ++){
				if (searchString.toLowerCase() == srcString.substring(off, searchString.length+off).toLowerCase()){
					result = true;
					break;
				}
			}
		}
	}
	return result;
}

function userSelect (selNode){
}

function recMake (curNode) {
	processedNodes ++;

	tmpBuldedPerc = Math.round((processedNodes*100/totalNodeCount));
	
	if (tmpBuldedPerc != processedProgress) {
		processedProgress = tmpBuldedPerc;
		notifyTreeBuildProgressChange (tmpBuldedPerc);
	}	
	
		
	var treeSrc = "";
	if (curNode != null) {
		treeSrc += "<table ";
		treeSrc += "border='0' cellpadding='0' cellspacing='0'><tr><td width='1' nowrap align='center' valign='middle'>"
		if (curNode.children.length>0) {
			treeSrc += "<img src='" + expandIconSrc + "' onClick='touggle(this)' align='absmiddle'>";
		} else {
			treeSrc += "<img src='" + spacerSrc + "' width='12' height='12' align='absmiddle'>";
		}
		treeSrc += "</td><td nowrap align='center'>";

		var type = getTypeByID(curNode.type);


		if (curNode.parent != null) {
			if (curNode.isComment){
				treeSrc += "<img src='" + commentIcon.src + "'>&nbsp;";
			} else {
				var iconSrc;
				if (type != null) {
					iconSrc = type.iconObject.src;
				} else {
					iconSrc = defaultIcon.src;
				}
				
				treeSrc += "<img src='" + iconSrc + "'>&nbsp;";
			}
		} else {
			treeSrc += "<img src='" + rootIcon.src + "'>&nbsp;";
		}
//		treeSrc += "&nbsp;";
		
		treeSrc += "</td><td nowrap ";
		treeSrc += "id='" + curNode.id +"' ";
		if (!curNode.isComment) {
			treeSrc +="class='treeNodeDefault' ";
			treeSrc += "onmouseover='nodeMouseOver(this)' onmouseout='nodeMouseOut(this)' onclick='nodeSelect(this)'";
		} else {
			treeSrc +="class='treeNodeComment' ";
		}
		treeSrc += ">";
		if (type == null || !type.skipName) {
			treeSrc += "&nbsp;" + curNode.tagName;	
		}
		
		if (type != null && type.addToTagNameAttrVals.length > 0) {
			for (extraIndex = 0; 
				 extraIndex < type.addToTagNameAttrVals.length;
				 extraIndex ++) {
					extra = type.addToTagNameAttrVals[extraIndex];
					extraAttr = curNode.getNamedAttribute(extra);
					
					if (extraAttr != null) {
						treeSrc += "&nbsp;";
						if (type.useExtraDifferentStyle == true) {
							treeSrc += "<font class='extraVal'>" + type.nameExtraSeparator;
						}
					
						treeSrc += extraAttr.value;
						if (type.useExtraDifferentStyle == true) {
							treeSrc += type.nameExtraSeparator + "</font>";
						}						
					}
				}
		}	
		/* 
		if (curNode.isComment) {
			treeSrc += " comment ";
		}
		*/

		treeSrc += "&nbsp;</td><td width='99%'>&nbsp;"
		if (curNode.children.length>0) {
			treeSrc += "<tr><td>";
			treeSrc += "<img src='" + spacerSrc + "'>";
			treeSrc += "</td><td colspan='3'>";
			treeSrc += "<div style='display:none'>";
			var childIndex = 0;
			for (childIndex = 0; 
				 childIndex < curNode.children.length; 
				 childIndex++){
				 treeSrc += recMake (curNode.children[childIndex], treeSrc);
			}
			treeSrc += "</div>";
			treeSrc += "</td></tr>";
		}
		treeSrc += "</td></tr></table>\n\r";
	}
	return treeSrc;
}




