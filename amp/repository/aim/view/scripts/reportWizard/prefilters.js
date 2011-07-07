function Filters (filterPanelName, connectionFailureMessage, filterProblemsMessage, loadingDataMessage, 
				savingDataMessage, cannotSaveFiltersMessage) {
	this.connectionFailureMessage	= connectionFailureMessage;
	this.filterProblemsMessage		= filterProblemsMessage;
	this.loadingDataMessage			= loadingDataMessage;
	this.savingDataMessage			= savingDataMessage;
	this.cannotSaveFiltersMessage	= cannotSaveFiltersMessage;
	
	this.resetString				= "&doreset=true";
	
	this.filterPanel = new YAHOOAmp.widget.Panel("new", {
			width:"750px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true} );
	this.filterPanel.setHeader(filterPanelName);
	this.filterPanel.setBody("");
	this.filterPanel.render(document.body);
	
	this.listFiltersDiv				= document.getElementById("listFiltersDiv");
	
}

Filters.prototype.success	= function (o) {
	if ( o.responseText.length > 2 ) {
		this.filterPanel.hide();
		this.filterPanel.setBody( o.responseText );
		this.filterTabs	= new YAHOOAmp.widget.TabView('tabview_container');
		
		YAHOOAmp.amptab.afterFiltersLoad();
		this.filterPanel.cfg.setProperty("height", "400px" );
		
		this.filterPanel.show();
		
		this.saveFilters	= new SaveFilters(this);
		
		initCalendar();
		YAHOOAmp.util.Event.addListener( "filterPickerSubmitButton", "click", this.saveFilters.saveFilters, this.saveFilters ) ;
		
	}
	else {
		this.filterPanel.setBody ( "<font color='red'>" + this.filterProblemsMessage + "</font>");
	}
}
Filters.prototype.failure	= function (o) {
	this.filterPanel.setBody( "<font color='red'>" + this.connectionFailureMessage + "</font>");
}

Filters.prototype.showFilters	= function() {
	var avoidIECacheParam 	=	"&time=" + new Date().getTime(); 
	this.filterPanel.setBody( "<div style='text-align: center'>" + this.loadingDataMessage + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>" );
	this.filterPanel.show();
	YAHOOAmp.util.Connect.asyncRequest("GET", "/aim/reportsFilterPicker.do?sourceIsReportWizard=true"+ avoidIECacheParam +this.resetString, this);
	this.resetString		= "";
}

function SaveFilters (filterObj) {
	this.filterObj	= filterObj;
}

SaveFilters.prototype.saveFilters	= function (e, obj) {
	YAHOOAmp.util.Connect.setForm( document.getElementsByName("aimReportsFilterPickerForm")[0] );
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true", obj);
	obj.filterObj.filterPanel.setBody( "<div style='text-align: center'>" + obj.filterObj.savingDataMessage + 
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>" );
}

SaveFilters.prototype.success	= function (o) {
	//alert ("saveFilters: " + o.responseText);
	if ( o.responseText.length > 0 ) {
		this.filterObj.filterPanel.hide();
		this.filterObj.listFiltersDiv.innerHTML	= o.responseText;
		repManager.showUseFilters(true);
	}
	else
		this.filterObj.filterPanel.setBody (this.filterObj.cannotSaveFiltersMessage);
}
SaveFilters.prototype.failure	= function (o) {
	this.filterObj.filterPanel.setBody( "<font color='red'>" + this.connectionFailureMessage + "</font>");
}

/*
function submitFilters() {;}
*/