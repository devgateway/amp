function AbstractDynamicList (containerEl, thisObjName, fDivId) {
	this.filterLabels	= new Array();
	this.containerEl	= containerEl;
	this.thisObjName	= thisObjName;
	this.fDivId			= fDivId;
	
	this.filterDocTypeIds	= "";
	this.filterFileTypes	= "";
	
	this.reqString		= "";
	
	this.fPanel				= null;
}

AbstractDynamicList.prototype.sendRequest		= function () {
	this.reqString		= "";
	this.createFilterString();
	this.createReqString();
	
	var callbackObj		= getCallbackForOtherDocuments(this.containerEl);
//	alert(this.reqString);
	YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/documentManager.do?ajaxDocumentList=true&dynamicList='+this.thisObjName+
			this.reqString, callbackObj );
}

AbstractDynamicList.prototype.retrieveFilterData	= function (divId) {
	var divEl	= document.getElementById(divId);
	var form	= divEl.getElementsByTagName("form")[0];
	for (var field in this) {
		if ( field.indexOf("filter") == 0 && field!="filterLabels" ) {
			var fieldValue	= form.elements[field].value;
			this[field] = fieldValue;
		}
	}
}

AbstractDynamicList.prototype.createFilterString	= function () {
	this.retrieveFilterData(this.fDivId);
	for (var field in this) {
		if ( field.indexOf("filter") == 0 && field!="filterLabels" && this[field] != null && this[field].length > 0 ) {
			this.reqString	+= "&"+field+"="+this[field];
		}
	}
	for (var i=0; i<this.filterLabels.length; i++) {
		this.reqString	+= "&filterLabelsUUID=" + this.filterLabels[i] ;
	}
}

AbstractDynamicList.prototype.getFilterPanel	= function (buttonId,divId) {
	if ( this.fPanel == null) {
		var divEl		= document.getElementById(divId);
		
		var panel 		= 
			new YAHOO.widget.Panel("FilterPanel"+divId, { width:"400px", visible:true, draggable:true, close:true, modal:false, 
				context:[buttonId,"tl","bl"]} );
		panel.setHeader('Filters');
		panel.setBody(divEl);
		panel.render(document.body);
		
		this.fPanel		= panel;
		
		divEl.style.display	= "";
		var buttonEls	= divEl.getElementsByTagName("button");
		YAHOO.util.Event.on(buttonEls[0],"click", this.sendRequest, this, true);
		YAHOO.util.Event.on(buttonEls[1],"click", this.fPanel.hide, this.fPanel, true);
	}
	return this.fPanel;
}

/**
 * DynamicList class
 */
DynamicList.prototype				= new AbstractDynamicList();
DynamicList.prototype.parent		= AbstractDynamicList;
DynamicList.prototype.constructor	= DynamicList;

function DynamicList(containerEl, thisObjName,fDivId, teamId, username) {
	
	this.parent.call(this, containerEl, thisObjName, fDivId);
	
	this.teamId			= teamId;
	this.username		= username;
	
}

DynamicList.prototype.createReqString	= function () {
	this.reqString 	+= "&otherTeamId="+this.teamId;
	if ( this.username != null ) 
		this.reqString	+= "&otherUsername="+this.username;
	
	return this.reqString;
}

DynamicList.prototype.addRemoveLabelUUID			= function ( labelUUID ) {
	for (var i=0; i<this.filterLabels.length; i++) {
		if (this.filterLabels[i] == labelUUID ) {
			this.filterLabels.splice(i, 1);
			return;
		}
	}
	this.filterLabels.push (labelUUID);
}
AbstractDynamicList.prototype.emptyLabelUUIDs			= function () {
	this.filterLabels	= new Array();
}

/**
 * SharedDynamicList class
 */
SharedDynamicList.prototype				= new AbstractDynamicList();
SharedDynamicList.prototype.parent		= AbstractDynamicList;
SharedDynamicList.prototype.constructor	= SharedDynamicList;

/**
 * 
 * @param containerEl
 * @returns {SharedDynamicList}
 */
function SharedDynamicList(containerEl, thisObjName, fDivId) {
	this.parent.call(this, containerEl, thisObjName, fDivId);
}

