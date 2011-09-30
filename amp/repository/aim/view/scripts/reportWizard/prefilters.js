function Filters (filterPanelName, connectionFailureMessage, filterProblemsMessage, loadingDataMessage, 
				savingDataMessage, cannotSaveFiltersMessage, doReset) {
	this.connectionFailureMessage	= connectionFailureMessage;
	this.filterProblemsMessage		= filterProblemsMessage;
	this.loadingDataMessage			= loadingDataMessage;
	this.savingDataMessage			= savingDataMessage;
	this.cannotSaveFiltersMessage	= cannotSaveFiltersMessage;
	
	this.resetString				= "&doreset=true";
	if ( !doReset )
		this.resetString	= "";
	
	this.filterPanel = new YAHOO.widget.Panel("new", {
			width:"870px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
		    draggable:true} );
	this.filterPanel.setHeader(filterPanelName);
	this.filterPanel.setBody("");
	this.filterPanel.render(document.body);
	
	this.settingsPanel	= new YAHOO.widget.Panel("new2", {
		width:"450px",
	    fixedcenter: true,
	    constraintoviewport: true,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
	    draggable:true} );
	
	this.settingsPanel.setHeader(filterPanelName);
	this.settingsPanel.setBody("");
	this.settingsPanel.render(document.body);
	
	this.listFiltersDiv				= document.getElementById("listFiltersDiv");
	
}

Filters.prototype.success	= function (o) {
	if ( o.responseText.length > 2 ) {
		//this.filterPanel.hide();
		this.filterPanel.setBody( o.responseText );
		this.filterTabs	= new YAHOO.widget.TabView('tabview_container');
		
		YAHOO.amptab.afterFiltersLoad();
		this.filterPanel.cfg.setProperty("height", "482px" );
		
		this.filterPanel.show();
		
		this.saveFilters	= new SaveFilters(this, false);
		
		initCalendar();
		document.getElementById("filterPickerSubmitButton").onclick	= function() { return false;};
		YAHOO.util.Event.removeListener("filterPickerSubmitButton", "click");
		YAHOO.util.Event.addListener( "filterPickerSubmitButton", "click", this.saveFilters.saveFilters, this.saveFilters, this.saveFilters ) ;
		
	}
	else {
		this.filterPanel.setBody ( "<font color='red'>" + this.filterProblemsMessage + "</font>");
	}
};
Filters.prototype.failure	= function (o) {
	this.filterPanel.setBody( "<font color='red'>" + this.connectionFailureMessage + "</font>");
};

Filters.prototype.showFilters	= function() {
	var avoidIECacheParam 	=	"&time=" + new Date().getTime(); 
	this.filterPanel.setBody( "<div style='text-align: center'>" + this.loadingDataMessage + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>" );

	this.filterPanel.cfg.setProperty("height", "482px" );
	this.filterPanel.cfg.setProperty("width", "870px" );
	this.filterPanel.center();
	this.filterPanel.show();
	YAHOO.util.Connect.asyncRequest("GET", "/aim/reportsFilterPicker.do?sourceIsReportWizard=true"+ avoidIECacheParam +this.resetString, this);
	this.resetString		= "";
};

Filters.prototype.showSettings	= function() {
	initFormatPopup();
	this.saveFilters	= new SaveFilters(this, true);
	var element = document.getElementById("customFormat");
	element.style.display = "inline";
	this.settingsPanel.setBody(element);
	this.settingsPanel.center();
	this.settingsPanel.show();
	
	document.getElementById("applyFormatBtn").onclick	= function() { return false;};
	YAHOO.util.Event.removeListener("applyFormatBtn", "click");
	YAHOO.util.Event.addListener("applyFormatBtn", "click", this.saveFilters.saveFilters, this.saveFilters, this.saveFilters);
};

function SaveFilters (filterObj, showSettings) {
	this.filterObj		= filterObj;
	this.showSettings	= showSettings==null?false:showSettings;
	this.panel		= this.showSettings?filterObj.settingsPanel:filterObj.filterPanel;
};

SaveFilters.prototype.saveFilters	= function (e, obj) {
	var avoidIECacheParam 	=	"&time=" + new Date().getTime(); 
	var formName	= "aimReportsFilterPickerForm" + (this.showSettings?"3":"");
	YAHOO.util.Connect.setForm( document.getElementsByName(formName)[0] );
	var additionalParams	= this.showSettings?"&sourceIsReportWizard=true":"";
	YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true" + avoidIECacheParam + additionalParams, obj);
	
	if ( this.showSettings ) {
		var element = document.getElementById("customFormat");
		element.parentNode.removeChild(element);
		
		document.body.appendChild(element);
	}
	this.panel.setBody( "<div style='text-align: center'>" + obj.filterObj.savingDataMessage + 
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>" );
	
	return false;
};

SaveFilters.prototype.success	= function (o) {
	//alert ("saveFilters: " + o.responseText);
	if ( o.responseText.length > 0 ) {
		this.panel.hide();
		this.filterObj.listFiltersDiv.innerHTML	= o.responseText;
		repManager.showUseFilters(true);
	}
	else
		this.panel.setBody (this.filterObj.cannotSaveFiltersMessage);
};
SaveFilters.prototype.failure	= function (o) {
	this.panel.setBody( "<font color='red'>" + this.connectionFailureMessage + "</font>");
};

function initFormatPopup(){
	
	
	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
	
	if (decimalSymbol=="CUSTOM"){
		document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=false;
	
	}else{
		document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value="";
		document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=true;
	}

	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
	
	if (customDecimalPlaces=="CUSTOM"){
		document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled=false;
	}else{
		document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value="";
		document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled=true;
	}


	var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;

	if (!customUseGrouping){
		document.aimReportsFilterPickerForm3.customGroupCharacter.disabled=true;
	}else{
		document.aimReportsFilterPickerForm3.customGroupCharacter.disabled=false;
		}
	var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
	document.aimReportsFilterPickerForm3.customGroupSize.disabled=!customUseGrouping;
	document.aimReportsFilterPickerForm3.customGroupCharacterTxt.disabled=((!customUseGrouping) || ("CUSTOM"!=customGroupCharacter));

	changeFormat();
}

function changeFormat(){
	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol=("CUSTOM"==decimalSymbol)?document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value:decimalSymbol;
	
	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces=("CUSTOM"==customDecimalPlaces)?document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value:customDecimalPlaces;
	
	var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;
	
	
	var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter=("CUSTOM"==customGroupCharacter)?document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value:customGroupCharacter;
	
	
	var customGroupSize=document.aimReportsFilterPickerForm3.customGroupSize.value;
	
	
	var num=Number(123456789.928);
	
	
	var format=new Format(decimalSymbol,customDecimalPlaces,customUseGrouping,customGroupCharacter,customGroupSize);
	document.getElementById("number").innerHTML="<B>"+num.format(format)+"</B>";
	//alert(num.format(format));
	return true;
}

function ResetCustom(maxFractionDigits) {
	aimReportsFilterPickerForm3.customDecimalSymbol.value = ",";
	aimReportsFilterPickerForm3.customDecimalSymbolTxt.value = "";
	aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = "true";
	aimReportsFilterPickerForm3.customDecimalPlaces.value = maxFractionDigits;
	aimReportsFilterPickerForm3.customDecimalPlacesTxt.value = "";
	aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = "true"
	aimReportsFilterPickerForm3.customUseGrouping.checked = "true";
	aimReportsFilterPickerForm3.customGroupCharacter.value = ".";
	aimReportsFilterPickerForm3.customGroupCharacterTxt.value = "";
	aimReportsFilterPickerForm3.customGroupSize.value = 3;
	//alert(aimReportsFilterPickerForm3.amountinthousands.checked);
	aimReportsFilterPickerForm3.customAmountinThousands.checked = false;
	initFormatPopup();
	document.aimReportsFilterPickerForm3.renderStartYear.value=-1;
	document.aimReportsFilterPickerForm3.renderEndYear.value=-1;
	if (aimReportsFilterPickerForm3.currency)
		aimReportsFilterPickerForm3.currency.value=aimReportsFilterPickerForm3.defaultCurrency.value;
}

