/* Check also reportsScripts.jsp for some initialization of this class. 
   The translated messages and the saveReprotEngine variables are declared there.
*/
function SaveReportEngine (isTab) {
	this.saveButton				= new ExtendedButton("dynamic_save_button");
	this.overwritingReport		= false;
	this.isTab					= isTab;
}

SaveReportEngine.prototype.getOriginalReportName	= function () {
	return document.getElementById("saveOriginalReportName").value;
}
SaveReportEngine.prototype.getReportName	= function () {
	return document.getElementById("saveReportName").value;
}
SaveReportEngine.prototype.getReportId		= function () {
	return document.getElementById("saveReportId").value;
}
SaveReportEngine.prototype.getOverwrite		= function () {
	return document.getElementById("overwriteReport").checked;
}

SaveReportEngine.prototype.checkTyping		= function (e) {
	if ( SaveReportEngine.enterPressed(e) )
			return;
	if ( this.getReportName() == "" ) {
		this.saveButton.disable();
	}
	else {
		this.saveButton.enable();
	}
}

SaveReportEngine.prototype.checkEnter		= function (e) {
	if ( SaveReportEngine.enterPressed(e) && this.getReportName() != "" ) {
			saveReportEngine.saveReport();
			return false;
	}
	return true;	
}

SaveReportEngine.prototype.showPanel		= function () {
	if (this.panel == null) {
		document.getElementById("saveTitlePanel").style.display	= "";
		this.panel = new YAHOO.widget.Panel("saveTitlePanel", 
				{ 	visible:true,
					width: "400px", 
					constraintoviewport:true, 
					fixedcenter: true, 
					underlay:"shadow", 
					modal: true,
					close:true, 
					visible:false, 
					draggable:true } );
		this.panel.render(document.body);
		this.innerBody	= this.panel.body.innerHTML;
	}
	else {
		this.panel.setBody(this.innerBody);
	}
	this.panel.setFooter("");
	this.panel.show();
	document.getElementById("saveReportName").focus();
}

SaveReportEngine.prototype.success		= function (o) {
	this.panel.setFooter("");
	if ( o.responseText.length > 2 ) {
		this.panel.setBody( o.responseText );
	}
	else {
		var message		= null;
		if ( this.isTab )
			if ( this.overwritingReport )
				message	= SaveReportEngine.tabSavedMessage;
			else
				message	= SaveReportEngine.doneCopyMessage + SaveReportEngine.checkTabsMessage;
		else {
			if ( this.overwritingReport )
				message	= SaveReportEngine.reportSavedMessage;
			else {
				message	= SaveReportEngine.doneCopyMessage + SaveReportEngine.checkReportsMessage;
				if ( window.opener.location.href != null )
						window.opener.location.replace("/viewTeamReports.do?tabs=false");
			}
		}
			
		this.panel.setBody( message );
		this.doneCopyMessage	
	
	}
}

SaveReportEngine.prototype.failure		= function (o) {
	this.panel.setFooter("");
	this.panel.setBody(SaveReportEngine.connectionErrorMessage);
}
SaveReportEngine.prototype.saveReport		= function () {
	if ( this.getReportName() == this.getOriginalReportName() ) {
		this.overwritingReport	= true;
	}
	else { 
		this.overwritingReport	= false;
	}

	this.saveButton.disable();
	this.panel.setFooter( SaveReportEngine.savingMessage + "...<br />  <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/>" );
	var postString		= "dynamicSaveReport=true" +
						"&reportTitle="+this.getReportName() + 
						"&reportId="+this.getReportId();
	//alert (postString);
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/reportWizard.do", this, postString);
}

SaveReportEngine.enterPressed		= function (e) {
	if (e != null) {
		var keyCode	= -1;
		if (e.which != null)
			keyCode	= e.which;
		if (e.keyCode != null)
			keyCode	= e.keyCode;
			
		if ( keyCode == 13 ) {
			return true
		}
	}
	return false;
}

function ExtendedButton( buttonId ) {
	this.buttonId	= buttonId;
}
ExtendedButton.prototype.enable				= function () {
	var btn 	= document.getElementById( this.buttonId );
	var btnY	= new YAHOOAmp.util.Element(btn);
	
	btn.disabled		= false;
	btnY.setStyle("color", "black");
}
ExtendedButton.prototype.disable			= function () {
	var btn 	= document.getElementById( this.buttonId );
	var btnY	= new YAHOOAmp.util.Element(btn);
	btn.disabled		= true;
	btnY.setStyle("color", "lightgrey");
}


function initSaveReportEngine( isTab) {
	if (saveReportEngine == null)
		saveReportEngine	= new SaveReportEngine( isTab );
}
