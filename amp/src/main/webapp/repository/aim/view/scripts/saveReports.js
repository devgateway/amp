/* Check also reportsScripts.jsp for some initialization of this class. 
   The translated messages and the saveReprotEngine variables are declared there.
*/
function SaveReportEngine (isTab) {
	this.saveButton				= new ExtendedButton("dynamic_save_button");
	this.overwritingReport		= false;
	this.isTab					= isTab;
}

SaveReportEngine.prototype.getReportName	= function () {
	var reportName = document.getElementById("saveReportName").value;
	if(reportName){
		reportName = trim(reportName);
	}
	return reportName;	
};

SaveReportEngine.prototype.getReportId		= function () {
	return document.getElementById("saveReportId").value;
};

SaveReportEngine.prototype.getOverwrite		= function () {
	return document.getElementById("overwriteReport").checked;
};

SaveReportEngine.prototype.checkTyping		= function (e) {
	if ( SaveReportEngine.enterPressed(e) )
			return;
	if ( this.getReportName() == "" ) {
		this.saveButton.disable();
	}
	else {
		this.saveButton.enable();
	}
};

SaveReportEngine.prototype.checkEnter		= function (e) {
	if ( SaveReportEngine.enterPressed(e) && this.getReportName() != "" ) {
			saveReportEngine.saveReport();
			return false;
	}
	return true;	
};

SaveReportEngine.prototype.showPanel		= function () {
	if (this.panel == null) {
 	 	document.getElementById("saveTitlePanel").style.display = "";
 	 	this.panel = new YAHOO.widget.Panel("saveTitlePanel", 
				{ 	visible:true,
					width: "400px", 
					constraintoviewport:true, 
					fixedcenter: true, 
					underlay: 'shadow',
					modal: true,
					close:true, 
					effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
					visible:false, 
					draggable:true } );
		this.panel.render(document.body);
		this.innerBody = this.panel.body.innerHTML;
	} else
		this.panel.setBody(this.innerBody);
	this.panel.setFooter("");
	this.panel.show();
	initMultilingualInput('AmpReports_name');
	this.panel.subscribe("show", function () {$('#titlePanelBody input').first().focus(); });
};
	
SaveReportEngine.prototype.closePanel		= function () {
	debugger;
	this.panel.close();
};

SaveReportEngine.prototype.success		= function (o) {
	this.panel.setFooter("");
	if ( o.responseText.length > 2 ) {
		this.panel.setBody( o.responseText );
	}
	else {
		var message		= null;
		if ( this.isTab ){
			if ( this.overwritingReport )
				message	= SaveReportEngine.tabSavedMessage;
			else
				message	= SaveReportEngine.doneCopyMessage + " " + SaveReportEngine.checkTabsMessage;
		} else {
			if ( this.overwritingReport )
				message	= SaveReportEngine.reportSavedMessage;
			else {
				message	= SaveReportEngine.doneCopyMessage + SaveReportEngine.checkReportsMessage;
				if ( window.opener.location.href != null )
						window.opener.location.replace("/viewTeamReports.do?tabs=false");
			}
		}
		message += "<br />" +
				"<div align='center'>" +
				"<digi:trn><input class='buttonx' type='reset' value='OK' onClick='return refresh("+this.isTab+");'></digi:trn>" +
				"</div>";	
		
		this.panel.setBody( message );
		this.doneCopyMessage;
	
	}
};

function refresh(isTab) {
	if (isTab) {
		document.forms[0].action = "/showDesktop.do";
		document.forms[0].submit();
	} else {
	    this.close();
	    if (window.opener.location.href) {
	        window.opener.location.href = window.opener.location.href;
		}
	}
}

SaveReportEngine.prototype.failure		= function (o) {
	this.panel.setFooter("");
	this.panel.setBody(SaveReportEngine.connectionErrorMessage);
};

/**
 * returns a ready-to-be-appended-to-URI list
 * somewhat ugly hack, should be rewritten to use jQuery / YUI ajax functionality
 * SHAMELESS COPYPASTE off saving.js - made until this mess is sorted out and saveReports.js is merged with saving.js
 */
function getReportTitles()
{
	var ser =  multilingual_serialize('AmpReports_name');
	if (ser == null)
		return null;
	return '&' + ser;
}

SaveReportEngine.prototype.saveReport		= function () {
	//this.titlePanel.setFooter ("");
	var isTitleEntered = check_multilingual_value_entered('AmpReports_name');
	if (!isTitleEntered)
	{
		this.panel.setFooter( '<font size="2" color="red"><digi:trn>Please enter a title</digi:trn></font><br />');
		this.saveButton.enable();
		return;
	}
	
	var reportChangedName =  true; //this.getReportName() == this.getOriginalReportName();
	if (reportChangedName) {
		this.overwritingReport	= true;
	}
	else { 
		this.overwritingReport	= false;
	}

	this.saveButton.disable();
	var reportTitles = getReportTitles();
	
	if (reportTitles == null) {
		this.panel.setFooter( SaveReportEngine.savingMessage + "...<br />  Error: please enter a value in at least one language " );		
	}
	else {
		this.panel.setFooter( SaveReportEngine.savingMessage + "...<br />  <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/>" );
		var postString		= "dynamicSaveReport=true" +
							reportTitles + 
							"&reportTitle=dummy" + 
							"&desktopTab=" + this.isTab +
							"&reportId="+this.getReportId() +
							"&forceNameOverwrite=" + this.overwritingReport +
							"&useFilters=true";
		//alert (postString);
		YAHOO.util.Connect.asyncRequest("POST", "/aim/reportWizard.do", this, postString);
	}
};

SaveReportEngine.enterPressed		= function (e) {
	if (e != null) {
		var keyCode	= -1;
		if (e.which != null)
			keyCode	= e.which;
		if (e.keyCode != null)
			keyCode	= e.keyCode;
			
		if ( keyCode == 13 ) {
			return true;
		}
	}
	return false;
};

function ExtendedButton( buttonId ) {
	this.buttonId	= buttonId;
}
ExtendedButton.prototype.enable				= function () {
	var btn 	= document.getElementById( this.buttonId );
	var btnY	= new YAHOO.util.Element(btn);
	
	btn.disabled		= false;
	btnY.setStyle("color", "black");
};

ExtendedButton.prototype.disable			= function () {
	var btn 	= document.getElementById( this.buttonId );
	var btnY	= new YAHOO.util.Element(btn);
	btn.disabled		= true;
	btnY.setStyle("color", "lightgrey");
};



function initSaveReportEngine( isTab) {
	if (saveReportEngine == null)
		saveReportEngine	= new SaveReportEngine( isTab );
}
