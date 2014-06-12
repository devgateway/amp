function buildLabels() {
	var selectionList 	= createSelectionList();
	queryManager		= new QueryManager(selectionList, "queryLabelsDiv");
	queryManager.render();
	
}

function createSelectionList () {
	var retList		= new Array();
	var divEl		= document.getElementById("myFilter");
	var inputEls	= divEl.getElementsByTagName("input");
	for (var i = 0; i < inputEls.length; i++) {
		var inputEl = inputEls[i];
		if (inputEl.type == "checkbox" && inputEl.checked) {
			var el				= inputEl;
			var parentDivEl		= inputEl;
			while ( parentDivEl.tagName.toLowerCase() != "div" ) {
				parentDivEl		= parentDivEl.parentNode;
				if ( parentDivEl == null ) 
					break;
			}
			if ( parentDivEl == null ) 
				break;
			
			var labelDiv		= parentDivEl.getElementsByTagName("div")[0];
			
			
			var parentTrEl		= inputEl.parentNode.parentNode;
			if (parentTrEl.tagName.toLowerCase() == "tr") {
				var spanEl		= parentTrEl.getElementsByTagName("span")[0];
				var name	= spanEl.innerHTML;
				var ret		= {name: name, el: el, group: labelDiv.innerHTML , onRemoveObj: null};
				retList.push(ret);
			}
		}
		
	}
	return retList;	
}


function QueryManager (selectionList, parentId) {
	var index	= 0;
	this.labels	= new Object();
	for (var i = 0; i < selectionList.length; i++) {
		var item 	= selectionList[i];
		var group	= item.group;
		if (this.labels[group] == null) {
			this.labels[group]	= new Object();
		}
		var ql		= new QueryLabel(item.name, index, item.el, item.group);
		if ( item.onRemoveObj != null )
			ql.listeners.push(item.onRemoveObj);
		this.labels[group][index++]	= ql;
	};
	
	this.parentId	= parentId; 
	
}

QueryManager.prototype.render	= function( ) {
	var parentEl		= document.getElementById(this.parentId);
	var htmlStr			= "";
	for (var group in this.labels) {
		var groupList	= this.labels[group];
		htmlStr			+= group + ":&nbsp;";
		for (var qlId in groupList) {
			var ql	= groupList[qlId];
			ql.render();
			htmlStr			+= ql.htmlStr + "&nbsp;";
		}
		htmlStr			+= "<br />";
	}
	
	parentEl.innerHTML	= htmlStr;
};

QueryManager.prototype.remove	= function(group, id ) {
	this.labels[group][id].onRemove();
};


function QueryLabel (name, id, targetEl, group) {
	this.name		= name;
	this.id			= id;
	this.targetEl	= targetEl;
	this.group		= group;
	
	this.listeners	= new Array();
	this.htmlStr	= "";
	this.spanId		= "queryLabel" + this.id;
}

QueryLabel.prototype.onRemove	= function( ) {
	
	var spanEl				= document.getElementById(this.spanId);
	spanEl.parentNode.removeChild(spanEl);
	for (var i = 0; i < this.listeners.length; i++) {
		var listener = this.listeners[i];
		listener.onRemove(this);		
	}
	this.targetEl.click();
};

QueryLabel.prototype.render	= function( ) {
	this.htmlStr	= 
		"<span id='" + this.spanId + "' style='background: #F2F2F2;padding: 3px;'>" + this.name + 
		"<img align='bottom' src='/TEMPLATE/ampTemplate/images/cancel.png' onclick='queryManager.remove(\"" + 
		this.group  + "\"," + this.id  +
		")' style='cursor:pointer ; height: 14px;' />" +
		"</span>";
};