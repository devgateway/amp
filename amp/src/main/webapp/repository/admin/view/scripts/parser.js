var srcLength = 0;
var parsedPerc = 100;


var xmlString;
var uniqueIdCounter = 0;

function notifyParseProgressChange (val){

}

function process (parentTag, document){
	uniqueIdCounter ++;
	
	tmpParsedPerc = Math.round(100 - (xmlString.length*100/srcLength));
	
	if (tmpParsedPerc != parsedPerc) {
		parsedPerc = tmpParsedPerc;
		notifyParseProgressChange (parsedPerc);
	}
	
	var node = null;
	if (xmlString != null && xmlString.length > 0) {
		var tagStartPos = 0;
		var tagEndPos = 0;
		var node = null;
		while (tagStartPos > -1 && tagEndPos > -1) {
			tagStartPos = xmlString.indexOf ('<');
			tagEndPos = xmlString.indexOf ('>', tagStartPos);
			var nextTagStartPos = xmlString.indexOf ('<', tagEndPos);
			var tag = null;
			var value = null;
			if (nextTagStartPos > tagEndPos+1) {
				value = xmlString.substring(tagEndPos+1, nextTagStartPos);
			}
			tag = xmlString.substring(tagStartPos, tagEndPos + 1);
			if (tag.indexOf('/')!=1 && node == null) {
				if (tag.length == 0) break;
				node = processTag(tag);
				node.parent = parentTag;
				node.id = "gkTreeNode" + uniqueIdCounter;
				
				node.value = value;
				xmlString = xmlString.substring(tagEndPos+1);

				tagRule = document.docRules.getRuleByTagName (node.tagName);
				if (tagRule != null) {
					node.type = tagRule.id;
				}
				
				//Self terminated tag
				if (tag.lastIndexOf('/')== tag.length-2){
					break;
				}
				//Comment
				if (tag.indexOf('!')==1) {
					node.isComment = true;
					break;
				}
			} else if (tag.indexOf('/')!=1) {
				var childNode = process (node, document);
				node.addChild (childNode);
			} else if (tag.indexOf('/')==1) {
				xmlString = xmlString.substring(tagEndPos+1);
				break;
			}
		}
	}
	return node;
}



function processTag(tagSrc){
	//Find tag name
	if (tagSrc.indexOf('!')!=1) {
		tagNameEndPos = tagSrc.indexOf(' ');
		if (tagNameEndPos == -1) tagNameEndPos = tagSrc.indexOf('>');
		tagName = tagSrc.substring (1, tagNameEndPos);
	} else {
		tagName = tagSrc.substring (4, tagSrc.length-3);
	}
	newNode = new XMLNode (tagName, null);	
	//is not a comment
	if (tagSrc.indexOf('!')!=1) {
		//Process attributes
		if (tagSrc.indexOf(' ') != -1) {
			attrStart = 0;
			while (attrStart >-1) {
				attrStart = tagSrc.indexOf(' ', attrStart + 1);
				if (attrStart == -1) break;
				attrNameEndPos = tagSrc.indexOf('=', attrStart + 1);
				
				if (attrNameEndPos > attrStart) {
					attrValueStartPos = tagSrc.indexOf('\"', attrNameEndPos + 1) + 1;
					attrValueEndPos = tagSrc.indexOf('\"', attrValueStartPos);
					attrName = tagSrc.substring (attrStart + 1, attrNameEndPos);
					attrValue = tagSrc.substring (attrValueStartPos, attrValueEndPos);
					newAttr = new XMLAttribute (attrName, attrValue);
					newNode.addAttribute (newAttr);
				}
			}
		}
	}
	return newNode;
}

function stringTrimmer (str) {
	retVal = str;
	if (retVal != null && retVal.length > 0) {
		//From start
		while (retVal.substring (0,1) == " " ||
				retVal.substring (0,1) == "\n" ||
				retVal.substring (0,1) == "\r" ||
				retVal.substring (0,1) == "\t") {
			retVal = retVal.substring (1, retVal.length);
		}

		//From end
		while (retVal.substring (retVal.length - 1, retVal.length) == " " ||
				retVal.substring (retVal.length - 1, retVal.length) == "\n" ||
				retVal.substring (retVal.length - 1, retVal.length) == "\r" ||
				retVal.substring (retVal.length - 1, retVal.length) == "\t") {
			retVal = retVal.substring (0, retVal.length - 1);
		}		
	}
	return retVal;
}

//Document object
function XMLDocument (xmlSrc) {
	this.xmlSrc = xmlSrc;
	this.rootNode = null;
	this.nodeCount = 0;
	this.docRules = new XMLParserRules();
}

XMLDocument.prototype.parse = function (){
	xmlString = this.xmlSrc;
	srcLength = xmlString.length;
	this.rootNode = process (null, this);
	this.nodeCount = uniqueIdCounter;
}

XMLDocument.prototype.getNodeById = function (id){
	return getNodeByIdRec(this.rootNode, id);
}

function getNodeByIdRec (node, id){
	var retVal = null;
	if (node.id == id) {
		retVal = node;
	} else {
		if (node.children != null && node.children.length > 0) {
			var chIndex = 0;
			for (chIndex = 0; 
				 chIndex < node.children.length; 
				 chIndex ++) {
				 retVal = getNodeByIdRec(node.children[chIndex], id);
				 if (retVal != null) break;
			}
		}
	}
	return retVal;
}

//Node object
function XMLNode(tagName, parentTag){
	this.id;
	this.type = 0;
	this.isComment = false;
	this.tagName = tagName;
	this.attributes = [];
	this.children = [];
	this.parent = parentTag;
	this.type = -1;
	this.tagValue = null;
}

XMLNode.prototype.addAttribute = function (newAttr) {
	this.attributes[this.attributes.length] = newAttr;
}

XMLNode.prototype.addChild = function (childNode) {
	this.children[this.children.length] = childNode;
}

XMLNode.prototype.getNamedAttribute = function (name) {
	retVal = null;

	if (this.attributes != null && this.attributes.length > 0) {
		for (attrIndex = 0; 
			 attrIndex < this.attributes.length;
			 attrIndex ++) {
			 if (this.attributes[attrIndex].name == name) {
			 	retVal = this.attributes[attrIndex];
				break;
			}
		}
	}
	return retVal;
}

//Attribute object
function XMLAttribute (attrName, attrVal) {
	this.name = attrName;
	this.value = attrVal
}

//parser rules
function XMLParserRules (){
	this.rules = [];
}

XMLParserRules.prototype.addRule = function (newRule){
	this.rules[this.rules.length] = newRule;
}

XMLParserRules.prototype.getRuleByTagName = function (tagName){
	retVal = null;
	if (this.rules != null && this.rules.length > 0){
		var rIndex;
		for (rIndex = 0; rIndex < this.rules.length; rIndex ++) {
			if (this.rules[rIndex].tagName == tagName) {
				retVal = this.rules[rIndex];
				break;
			}
		}
	}
	return retVal;
}

function XMLParserRuleItem (id, tagName) {
	this.id = id;
	this.tagName = tagName;
}
