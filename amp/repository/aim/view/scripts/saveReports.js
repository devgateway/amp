function getTabTitleEl() {
	var divEl	= document.getElementById("titlePanelBody");
	var titleEl	= divEl.getElementsByTagName("input")[0];
	return titleEl;
}

function getTabTitle() {
	return getTabTitleEl().value;
}

/* Check also reportsScripts.jsp for some initialization of this class. 
   The translated messages and the saveReprotEngine variables are declared there.
*/
function SaveReportEngine (isTab) {
	this.saveButton				= new ExtendedButton("last_save_button");
	this.overwritingReport		= false;
	this.isTab					= isTab;
}

SaveReportEngine.prototype.getOriginalReportName	= function () {
	return document.getElementById("saveOriginalReportName").value;
};

SaveReportEngine.prototype.getReportName	= function () {
	var reportName = getTabTitle(); 
	if(reportName){
		reportName = trim(reportName);
	}
	return reportName;	
};

SaveReportEngine.prototype.checkTabName	= function () {
	var saveBtn = document.getElementById("last_save_button");
	var saveBtnY = new YAHOO.util.Element(saveBtn);
	var strTitle	= getTabTitle();
	strTitle		= strTitle.replace(/\s*/, "");
	if ( strTitle == "" ) {
		saveBtn.disabled = true;
		saveBtnY.removeClass("buttonx");
		saveBtnY.addClass("buttonx_dis");
		//btnY.setStyle("color", "lightgrey");
		return false;
	}
	else
	{
		saveBtn.disabled = false;
		saveBtnY.removeClass("buttonx_dis");
		saveBtnY.addClass("buttonx");
		//btnY.setStyle("color", "");
		return true;
	}
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
	if ( this.titlePanel == null ) {
		document.getElementById("titlePanel").style.display	= "block";
		this.titlePanel	= new YAHOO.widget.Panel("titlePanel", 
				{ 	visible:true,
					width: "400px", 
					constraintoviewport:true, 
					fixedcenter: true, 
					modal: true,
					close:true, 
					effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
					visible:false, 
					draggable:true } );
		this.titlePanel.render( document.body );
	}
	this.titlePanel.show();
	getTabTitleEl().focus();
	
	};
SaveReportEngine.prototype.closePanel		= function () {
	this.titlePanel.close();
}
SaveReportEngine.prototype.success		= function (o) {
	if ( o.responseText.length > 2 ) {
		this.titlePanel.setBody( o.responseText );
	}
	else {
		var message		= null;
		if ( this.isTab ){
			if ( this.overwritingReport )
				message	= SaveReportEngine.tabSavedMessage;
			else
				message	= SaveReportEngine.doneCopyMessage + SaveReportEngine.checkTabsMessage;
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
		
		this.titlePanel.setBody( message );
		this.doneCopyMessage;
	
	}
};

function refresh(isTab) {
	if (isTab) {
		document.forms[0].action = "/showDesktop.do";
		document.forms[0].submit();
	} else {
		this.close();
	}
}

SaveReportEngine.prototype.failure		= function (o) {
	this.titlePanel.setFooter("");
	this.titlePanel.setBody(SaveReportEngine.connectionErrorMessage);
};

SaveReportEngine.prototype.saveReport		= function () {
	this.titlePanel.setFooter ("");
	if ( this.getReportName() == this.getOriginalReportName() ) {
		this.overwritingReport	= true;
	}
	else { 
		this.overwritingReport	= false;
	}

	this.saveButton.disable();
	this.titlePanel.setFooter( SaveReportEngine.savingMessage + "...<br />  <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/>" );
	var postString		= "dynamicSaveReport=true" +
						"&reportTitle="+getTabTitle() + 
						"&reportId="+this.getReportId()+
						"&forceNameOverwrite=" + this.overwritingReport+
						"&useFilters=true";
	//alert (postString);
	YAHOO.util.Connect.asyncRequest("POST", "/aim/reportWizard.do", this, postString);
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
