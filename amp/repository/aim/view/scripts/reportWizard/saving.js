
function getReportType() {
	var radioEls		= aimReportWizardForm.reportType;
	for (var i=0; i < radioEls.length +1; i++) {
		if ( radioEls[i].checked ) {
			return radioEls[i].value;
		}
	}
	//if there only one options this is not a collection!
	return radioEls.value;
}

function getPublicReport() {
	if (aimReportWizardForm.publicReport == null)
			return false;
	var checkboxObject		= aimReportWizardForm.publicReport;
	return checkboxObject.checked;
}

function getWorkspaceLinked() {
	if (aimReportWizardForm.workspaceLinked == null)
			return false;
	var checkboxObject		= aimReportWizardForm.workspaceLinked;
	return checkboxObject.checked;
}

function getAlsoShowPledges() {
	if (aimReportWizardForm.alsoShowPledges == null)
			return false;
	var checkboxObject		= aimReportWizardForm.alsoShowPledges;
	return checkboxObject.checked;
}

function getReportTitleEl() {
	var divEl	= document.getElementById("titlePanelBody");
	var titleEl	= divEl.getElementsByTagName("input")[0];
	return titleEl;
}

function getReportTitle() {
	return getReportTitleEl().value;
}

/**
 * returns a ready-to-be-appended-to-URI list
 * somewhat ugly hack, should be rewritten to use jQuery / YUI ajax functionality
 */
function getReportTitles()
{
	var ser =  multilingual_serialize('AmpReports_name');
	if (ser == null)
		return null;
	return '&' + ser;
}

function getReportCategory() {
	var categoryEl 	= document.getElementById("repCat");
	if (categoryEl != null ) {
		var category	= categoryEl.value; 
		return category;
	}
	return 0;
}

function getReportDescription() {
	return aimReportWizardForm.reportDescriptionClone.value;
}

function getHideActivities() {
	if ( aimReportWizardForm.hideActivities == null )
		return false;
	return aimReportWizardForm.hideActivities.checked;
}

function getReportPeriod() {
	if ( aimReportWizardForm.reportPeriod == null )
				return "A";
	var radioEls		= aimReportWizardForm.reportPeriod;
	for (var i=0; i<radioEls.length; i++) {
		if ( radioEls[i].checked ) {
			return radioEls[i].value;
		}
	}
}

function getUseFilters() {
	if ( aimReportWizardForm.useFilters!=null && aimReportWizardForm.useFilters.checked )
		return true;
	return false;
}

function getAllowEmptyFundingColumns() {
	if ( aimReportWizardForm.allowEmptyFundingColumns!=null && aimReportWizardForm.allowEmptyFundingColumns.checked )
		return true;
	return false;
}

function getDesktopTab() {
	return aimReportWizardForm.desktopTab.value;
}

function getSelectedFields( ulId, name ) {
	var ret			= "";
	var ulEl		= document.getElementById( ulId );
	
	var fields		= ulEl.getElementsByTagName( "input" );
	for ( var i=0; i<fields.length; i++ ) {
		ret			+= name+"="+fields[i].value;
		if ( i < fields.length-1 )
			ret		+= "&";
	}
	if ( fields.length == 0 ) { 
			ret	= name + "=0";
	}
	return ret;	
}
function getSelectedFieldsNames( ulId ) {
	var ret			= new Array();
	var ulEl		= document.getElementById( ulId );
	
	var fields		= ulEl.getElementsByTagName( "input" );
	for ( var i=0; i<fields.length; i++ ) {
		ret.push(fields[i].parentNode.innerHTML.replace(/<[^>]*>/g, "").replace(/^\s+|\s+$/g, '') );
	}
	return ret;	
}

function SaveReportEngine ( savingMessage, failureMessage ) {
	this.failureMessage	= failureMessage;
	this.savingMessage	= savingMessage;
	this.divEl			=  document.getElementById("savingReportDiv");
	this.titlePanel		= null;
	this.forceOverwrite		= false;
}

SaveReportEngine.prototype.checkEnter		= function (e) {
	if (e != null) {
		var keyCode	= -1;
		if (e.which != null)
			keyCode	= e.which;
		if (e.keyCode != null)
			keyCode	= e.keyCode;
		if ( keyCode == 13 ) {
			if ( getReportTitle().replace(/\s*/, "") != "" )
				saveReportEngine.saveReport();
			return false;
		}
	}
	return true;
};

SaveReportEngine.prototype.openReport = function(reportId)
{
	globalOpenPopup(null, '/viewNewAdvancedReport.do?view=reset&widget=false&resetSettings=true&ampReportId=' + reportId);
};

SaveReportEngine.prototype.success		= function (o) {
	var response = '';
	if ( o.responseText.length > 2 ) {
		response = o.responseText;
	}
	var shouldOpenReport = (response.length > 2) && (/openReportId/i.test(response));
	var shouldShowError = (response.length > 2) && (!shouldOpenReport);
	if (shouldShowError)
	{
		this.divEl.innerHTML = response;
		if (response.indexOf("duplicateName") >= 0 ) {
			getReportTitleEl().value	= "";
		}
		return;
	}
	
	// got till here -> no error
	if (shouldOpenReport && getDesktopTab()=="false")
	{
		//if it's not a tab we do open it
		var arr = response.split('=');
		this.openReport(arr[1]); // open report in a new browser tab/window
	}
	
	window.location.replace("/aim/viewTeamReports.do?tabs=" + getDesktopTab()); // open tabs/reports list in current window
};

SaveReportEngine.prototype.failure			= function(o) {
	this.divEl.innerHTML			= this.failureMessage;
};

SaveReportEngine.prototype.saveNoOverwrite	= function () // actually this is the "Save" button
{
	this.forceOverwrite = false;
	this.showTitlePanel();
};


SaveReportEngine.prototype.saveOverwrite	= function () // actually this is the "Save" button
{
	this.forceOverwrite = true;
	this.showTitlePanel();
};



SaveReportEngine.prototype.showTitlePanel	= function () {
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
	
	this.titlePanel.subscribe("show", function () {getReportTitleEl().focus(); });
};


SaveReportEngine.prototype.saveReport = function()
{
	this.saveAndOrOpenReport(false);
};

SaveReportEngine.prototype.saveAndOpenReport = function()
{
	this.saveAndOrOpenReport(true);
};

SaveReportEngine.prototype.saveAndOrOpenReport	= function (openReport) {	
	//debugger;
	if ( this.titlePanel != null )
		this.titlePanel.hide();
	this.divEl.style.visibility		= "";
	this.divEl.innerHTML			= this.savingMessage + 
			"... <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/>";
	
	var reportTitles = getReportTitles();
	var noReportNameSupplied = "";
	if (reportTitles == null) {
		reportTitles = "";
		noReportNameSupplied = "&noReportNameSupplied=true";
	}
	if (reportTitles != null) {
		var postString		= "reportTitle=dummy&reportDescription="+encodeURIComponent(getReportDescription()) + "&reportPeriod="+getReportPeriod() + 
							"&reportType="+getReportType() + "&" + getSelectedFields("dest_col_ul", "selectedColumns") + 
							"&reportCategory="+ getReportCategory()+
							"&forceNameOverwrite=" + this.forceOverwrite+
							"&desktopTab="+getDesktopTab() +
							"&publicReport="+getPublicReport() +
							"&workspaceLinked="+getWorkspaceLinked() +
							"&alsoShowPledges="+getAlsoShowPledges() +
							//"&onlyShowProjectsRelatedPledges=" + getOnlyShowProjectsRelatedPledges() + 
							"&hideActivities="+getHideActivities() +
							"&useFilters="+getUseFilters()+
							"&openReport=" + openReport + 
							reportTitles +
							noReportNameSupplied + 
							//"&reportContextId="+getReportContextId()+
							"&allowEmptyFundingColumns="+getAllowEmptyFundingColumns()+
							"&" + getSelectedFields ("dest_measures_ul","selectedMeasures")+ "&" + getSelectedFields("dest_hierarchies_ul","selectedHierarchies");
		
		//alert(postString);
		YAHOO.util.Connect.asyncRequest("POST", "/aim/reportWizard.do", this, postString);
	}
	else {
		
	}
};
