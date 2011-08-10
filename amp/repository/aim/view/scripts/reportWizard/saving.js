
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

function getReportTitleEl() {
	var divEl	= document.getElementById("titlePanelBody");
	var titleEl	= divEl.getElementsByTagName("input")[0];
	return titleEl;
}

function getReportTitle() {
	return getReportTitleEl().value;
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
}

SaveReportEngine.prototype.checkEnter		= function (e) {
	if (e != null) {
		var keyCode	= -1;
		if (e.which != null)
			keyCode	= e.which;
		if (e.keyCode != null)
			keyCode	= e.keyCode;
		if ( keyCode == 13 && getReportTitle() != "" ) {
			saveReportEngine.saveReport();
			return false;
		}
	}
	return true;
}
SaveReportEngine.prototype.success		= function (o) {
	if ( o.responseText.length > 2 ) {
		this.divEl.innerHTML	= o.responseText;
		if ( o.responseText.indexOf("duplicateName") >= 0 ) {
			getReportTitleEl().value	= "";
		}
	}
	else
		if ( getDesktopTab()=="true" )
			window.location.replace("/aim/viewTeamReports.do?tabs=true");
		else
			window.location.replace("/aim/viewTeamReports.do?tabs=false");
		
}
SaveReportEngine.prototype.failure			= function(o) {
	this.divEl.innerHTML			= this.failureMessage;
}

SaveReportEngine.prototype.decideToShowTitlePanel	= function () {
	if ( getReportTitle() == "" )
			this.showTitlePanel();
	else
			this.saveReport( aimReportWizardForm );
}

SaveReportEngine.prototype.showTitlePanel	= function () {
	if ( this.titlePanel == null ) {
		document.getElementById("titlePanel").style.display	= "";
		this.titlePanel	= new YAHOO.widget.Panel("titlePanel", 
				{ 	visible:true,
					width: "400px", 
					constraintoviewport:true, 
					fixedcenter: true, 
					modal: true,
					close:true, 
					visible:false, 
					draggable:true } );
		this.titlePanel.render( document.body );
	}
	this.titlePanel.show();
	getReportTitleEl().focus();
}

SaveReportEngine.prototype.saveReport	= function () {
	if ( this.titlePanel != null )
		this.titlePanel.hide();
	this.divEl.style.visibility		= "";
	this.divEl.innerHTML			= this.savingMessage + 
			"... <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/>";
	
	var postString		= "reportTitle="+getReportTitle() + "&reportDescription="+getReportDescription() + "&reportPeriod="+getReportPeriod() + 
						"&reportType="+getReportType() + "&" + getSelectedFields("dest_col_ul", "selectedColumns") + 
						"&desktopTab="+getDesktopTab() +
						"&publicReport="+getPublicReport() +
						"&hideActivities="+getHideActivities() +
						"&useFilters="+getUseFilters()+
						"&allowEmptyFundingColumns="+getAllowEmptyFundingColumns()+
						"&" + getSelectedFields ("dest_measures_ul","selectedMeasures")+ "&" + getSelectedFields("dest_hierarchies_ul","selectedHierarchies");
	
	//alert(postString);
	YAHOO.util.Connect.asyncRequest("POST", "/aim/reportWizard.do", this, postString);
	
}