//form3 = aimReportsFilterPickerForm3;// document.getElementsByName('aimReportsFilterPickerForm3')[0];
// is this file ever used?

function Filters (filterPanelName, connectionFailureMessage, filterProblemsMessage, loadingDataMessage, 
				savingDataMessage, cannotSaveFiltersMessage, doReset,settingsPanelName, validationMsgs) {
	this.connectionFailureMessage	= connectionFailureMessage;
	this.filterProblemsMessage		= filterProblemsMessage;
	this.loadingDataMessage			= loadingDataMessage;
	this.savingDataMessage			= savingDataMessage;
	this.cannotSaveFiltersMessage	= cannotSaveFiltersMessage;
	this.validationMsgs = validationMsgs;
        this.settingsPanelName=settingsPanelName;
        this.filterPanelName=filterPanelName;
    this.additionalParameter="";
	
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
	
	this.listFiltersDiv			= document.getElementById("listFiltersDiv");
	this.hasFilters				= document.getElementById("hasFilters");
}

Filters.prototype.success	= function (o) {
	if (o.responseText.length > 2) {
		this.filterPanel.setBody( o.responseText );
		this.filterTabs	= new YAHOO.widget.TabView('tabview_container');
		
		YAHOO.amptab.afterFiltersLoad();
		this.filterPanel.cfg.setProperty("height", "482px" );
		
		this.filterPanel.show();
		
		this.saveFilters	= new SaveFilters(this, false);
		
		//initCalendar();
		document.getElementById("filterPickerSubmitButton").onclick	= function() { return false;};
		YAHOO.util.Event.removeListener("filterPickerSubmitButton", "click");
		YAHOO.util.Event.addListener( "filterPickerSubmitButton", "click", this.saveFilters.saveFilters, this.saveFilters, this.saveFilters ) ;
		
	} else {
		this.filterPanel.setBody("<font color='red'>" + this.filterProblemsMessage + "</font>");
	}
};

Filters.prototype.failure = function (o) {
	var isLogged = false;
	
	var getUserInformation = {
		success : function(o) {
			var response = [];
			if (o.responseText !== undefined) {
				try {
	                // parse the json data
					response = YAHOO.lang.JSON.parse(o.responseText);
					// get the json attribute value of the 'logged' attribute
					isLogged = response['logged'];
	            }  catch (x) {
	            	alert("Error occured: " + x);
	            	return;
	            }
			}
		},
		failure : function(o) {
			alert("The URL is unreachble " + o.responseText);
		}
	};
	
	// get the information about the user 
	YAHOO.util.Connect.asyncRequest("GET", "/rest/security/layout", getUserInformation);
	
	if (isLogged) {
		// could be some connection issues
		this.filterPanel.setBody("<font color='red'>" + this.filterProblemsMessage + "</font>");
	} else {
		// timeout to redirect
		var timeout = 5;
		
		// build the error message
		var errorMessage = "<font color='red'><digi:trn jsFriendly='true'>The user is logged out</digi:trn>. ";
		errorMessage +="<digi:trn jsFriendly='true'>You will be redirected in</digi:trn> " + timeout + " <digi:trn jsFriendly='true'>seconds</digi:trn></font>.";
		
		this.filterPanel.setBody(errorMessage);
		
		var timer = setTimeout(function() {
			window.location = '/aim/index.do'
		}, timeout * 1000);
	}
};

Filters.prototype.showFilters	= function(reportContextId) {
	var avoidIECacheParam 	=	"&time=" + new Date().getTime(); 
	this.filterPanel.setBody( "<div style='text-align: center'>" + this.loadingDataMessage + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>" );

	this.filterPanel.cfg.setProperty("height", "482px" );
	this.filterPanel.cfg.setProperty("width", "870px" );
        this.settingsPanel.setHeader(this.filterPanelName);
	this.filterPanel.center();
	this.filterPanel.show();
	YAHOO.util.Connect.asyncRequest("GET", "/aim/reportsFilterPicker.do?sourceIsReportWizard=true&reportContextId=" + reportContextId + avoidIECacheParam +this.resetString+this.additionalParameter, this);
	this.resetString		= "";
	
	// Fix z-index problem on Public Report Generator without changing css loading order.
	this.fixZIndex("#new_mask", 3);
};

Filters.prototype.showSettings	= function() {
	initFormatPopup();
	this.saveFilters	= new SaveFilters(this, true);
	var element = document.getElementById("customFormat");
	element.style.display = "inline";
        this.settingsPanel.setHeader(this.settingsPanelName);
	this.settingsPanel.setBody(element);
	this.settingsPanel.center();
	this.settingsPanel.show();
	
	YAHOO.util.Event.removeListener("applyFormatBtn", "click");
	document.getElementById("applyFormatBtn").onclick	= function() { return false;};
	YAHOO.util.Event.addListener("applyFormatBtn", "click", this.saveFilters.validateAndSaveFilters, this.saveFilters, this.saveFilters);
	
	// Fix z-index problem on Public Report Generator without changing css loading order.
	this.fixZIndex("#new2_mask", 3);
};

Filters.prototype.fixZIndex = function(id, index) {
	$(id).attr('style', function(i,s) { return s + "z-index: " + index + " !important;" });
}

function SaveFilters (filterObj, showSettings) {
	this.filterObj		= filterObj;
	this.showSettings	= showSettings==null?false:showSettings;
	this.panel		= this.showSettings?filterObj.settingsPanel:filterObj.filterPanel;
};

SaveFilters.prototype.validateAndSaveFilters	= function (e, obj) {
	if (this.validateFormat()){
		this.saveFilters(e, obj);
	}
};
SaveFilters.prototype.validateFormat = function(){
		var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
			decimalSymbol=("custom"==decimalSymbol.toLowerCase())?document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value:decimalSymbol;
		
		var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
			customDecimalPlaces=("-2"==customDecimalPlaces.toLowerCase())?document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value:customDecimalPlaces;
		
		var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;
		
		var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
			customGroupCharacter=("custom"==customGroupCharacter.toLowerCase())?document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value:customGroupCharacter;
		
		var customGroupSize=document.aimReportsFilterPickerForm3.customGroupSize.value;
		
		if ((decimalSymbol==customGroupCharacter)&&(customUseGrouping)){
		        var msg= this.filterObj.validationMsgs[0];
				alert(msg);
				return false;
		}
		var validNumbers="0123456789";
		
		if (decimalSymbol=="" || customGroupCharacter==""){
			 var msg= this.filterObj.validationMsgs[1];
			alert(msg);
			return false;
		}
		
		
		if ((validNumbers.indexOf(decimalSymbol)!=-1)||(validNumbers.indexOf(customGroupCharacter)!=-1)){
			     var msg= this.filterObj.validationMsgs[2];
				alert(msg);
				return false;
		}
		
		if ((customGroupSize < 1) && (document.aimReportsFilterPickerForm3.customUseGrouping.checked == true)) {
			  var msg= this.filterObj.validationMsgs[3];
				alert(msg);
				return false;
		}
		
		var yearStart = $('#renderStartYear') ? $('#renderStartYear').val() : null;
		var yearEnd = $('#renderEndYear') ? $('#renderEndYear').val() : null;
		if (yearStart && yearEnd){
			yearStart = parseInt(yearStart);
			yearEnd = parseInt(yearEnd);
			if( yearStart > 0 && yearEnd > 0 && yearStart > yearEnd ){
				var msg= this.filterObj.validationMsgs[4]; 
				alert(msg);
				return false;
			};
		}
		return true;
};

SaveFilters.prototype.saveFilters	= function (e, obj) {
	var avoidIECacheParam 	=	"&time=" + new Date().getTime(); 
	var formName	= "aimReportsFilterPickerForm" + (this.showSettings?"3":"");
	YAHOO.util.Connect.setForm( document.getElementsByName(formName)[0] );
	var additionalParams	= this.showSettings?"&sourceIsReportWizard=true":"";
	if ( this.showSettings ) 
		YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true&applyFormat=Apply%20Format" + avoidIECacheParam + additionalParams, obj);
	else
		YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true" + avoidIECacheParam + additionalParams, obj);
	
	
	if ( this.showSettings ) {
		
        var element = document.getElementById("customFormat");
        element.parentNode.removeChild(element);
        
        var hiddenDiv=document.getElementById("myHiddenDiv"); //document.createElement('DIV');
        //hiddenDiv.setAttribute('style','display:none;');
        //hiddenDiv.setAttribute('id','hiddenDiv');
        hiddenDiv.appendChild(element);        
        //document.body.appendChild(hiddenDiv);
        
        //document.body.appendChild(element);
    }
	this.panel.setBody( "<div style='text-align: center'>" + obj.filterObj.savingDataMessage + 
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>" );
	
	return false;
};

SaveFilters.prototype.success	= function (o) {
	//alert ("saveFilters: " + o.responseText);
	if ( o.responseText.length > 0 ) {
		this.panel.hide();
		if (this.filterObj.hasFilters !== null && this.filterObj.hasFilters !== undefined) {
			this.filterObj.hasFilters.value = true;
		}
		this.filterObj.listFiltersDiv.innerHTML	= o.responseText;
		if (!(typeof repManager === 'undefined')) {
			repManager.showUseFilters(true);
		}
	}
	else
		this.panel.setBody (this.filterObj.cannotSaveFiltersMessage);
};

SaveFilters.prototype.failure	= function (o) {
	this.panel.setBody( "<font color='red'>" + this.connectionFailureMessage + "</font>");
};

function initFormatPopup(){
	
	
	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
	if (decimalSymbol.toLowerCase()=="custom"){
		document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=false;
	
	}else{
		document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value="";
		document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=true;
	}

	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
	if (customDecimalPlaces.toLowerCase()=="-2"){
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
	document.aimReportsFilterPickerForm3.customGroupCharacterTxt.disabled=((!customUseGrouping) || ("custom"!=customGroupCharacter.toLowerCase()));

	changeFormat();
}

function changeFormat(){
	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol=(decimalSymbol.toLowerCase()=="custom")?document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value:decimalSymbol;
	
	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces=(customDecimalPlaces.toLowerCase()=="-2")?document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value:customDecimalPlaces;
	
	var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;
	
	
	var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter=(customGroupCharacter.toLowerCase()=="custom")?document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value:customGroupCharacter;
	
	
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
	aimReportsFilterPickerForm3.customAmountinThousands.options.selectedIndex = 0;
	aimReportsFilterPickerForm3.calendar.value =aimReportsFilterPickerForm3.initialCal.value;
	initFormatPopup();
	document.aimReportsFilterPickerForm3.renderStartYear.value=-1;
	document.aimReportsFilterPickerForm3.renderEndYear.value=-1;
	if (aimReportsFilterPickerForm3.currency)
		aimReportsFilterPickerForm3.currency.value=aimReportsFilterPickerForm3.defaultCurrency.value;
}



