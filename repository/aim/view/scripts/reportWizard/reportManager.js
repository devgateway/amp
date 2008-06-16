function NormalReportManager () {
	;
}
NormalReportManager.prototype.enableTab		= function (tabIndex) {
	if ( tabIndex < YAHOO.amp.reportwizard.numOfSteps ) {
		var tab			= YAHOO.amp.reportwizard.tabView.getTab(tabIndex);
		if ( tab.get("disabled") ) {
			tab.set("disabled", false);
			var labelEl		= document.getElementById( YAHOO.amp.reportwizard.tabLabels[tabIndex] );
			(new YAHOO.util.Element(labelEl)).replaceClass('disabled', 'unsel');
		}
	}
	
	var btnid		= "step" + (tabIndex-1) + "_next_button";
	var btn 		= document.getElementById(btnid);
	btn.disabled	= false;
	btn.style.color	= "black";
}
NormalReportManager.prototype.disableTab	= function (tabIndex) {
	if ( tabIndex < YAHOO.amp.reportwizard.numOfSteps ) {
		var tab			= YAHOO.amp.reportwizard.tabView.getTab(tabIndex);
		if ( !tab.get("disabled") ) {
			tab.set("disabled", true);
			var labelEl		= document.getElementById( YAHOO.amp.reportwizard.tabLabels[tabIndex] );
			(new YAHOO.util.Element(labelEl)).replaceClass('unsel', 'disabled');
		}
	}
	
	var btnid		= "step" + (tabIndex-1) + "_next_button";
	var btn 		= document.getElementById(btnid);
	btn.disabled	= true;
	btn.style.color	= "lightgrey";
	
	if ( tabIndex < 3 )
		this.disableTab(tabIndex+1);
	
}

NormalReportManager.prototype.checkSteps	= function () {
	if ( this.checkReportDetails() )
		if ( this.checkColumns() )
			if ( this.checkHierarchies() )
				 	this.checkMeasures();
}

NormalReportManager.prototype.checkReportDetails	= function () {
	var spanEl			= document.getElementById("reportTitleSpan");
	var title			= spanEl.getElementsByTagName("textarea")[0].value;
	if ( title.length > 0 ) {
		//var button			= document.getElementById("saveReportButton");
		//button.disabled		= false;
		//button.style.color	= "black";
		this.enableTab(1);
		document.getElementById("reportdetailsMust").style.visibility	= "hidden";
		return true;
	}
	else {
		//var button			= document.getElementById("saveReportButton");
		//button.disabled		= true;
		//button.style.color	= "lightgray";
		this.disableTab(1);
		document.getElementById("reportdetailsMust").style.visibility	= "";
		return false;
	}
}

NormalReportManager.prototype.checkMeasures	= function () {
	var ulEl			= document.getElementById("dest_measures_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	measuresMustEl		= document.getElementById("measuresMust");
	if ( items.length > 0 ) {
		
		measuresMustEl.style.visibility="hidden";
		this.enableTab(4);
		return true;
	}
	else {
		measuresMustEl.style.visibility="";
		this.disableTab(4);
		return false;
	}
}

NormalReportManager.prototype.checkHierarchies	= function () {
	var ulEl			= document.getElementById("dest_hierarchies_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	
	if ( items.length > 3 ) {
		hierarchiesMustEl					= document.getElementById("hierarchiesMust");
		hierarchiesMustEl.style.visibility	= "";
		this.disableTab(3);
		return false;
	}
	else {
		hierarchiesMustEl					= document.getElementById("hierarchiesMust");
		hierarchiesMustEl.style.visibility	= "hidden";
		this.enableTab(3);
		return true;
	}
}

NormalReportManager.prototype.checkColumns	= function () {
	var ulEl			= document.getElementById("dest_col_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	if ( items.length > 0 ) {
		columnsMustEl	= document.getElementById("columnsMust");
		columnsMustEl.style.visibility="hidden";
		this.enableTab(2);
		//this.enableTab(3);
		return true;
	}
	else {
		columnsMustEl	= document.getElementById("columnsMust");
		columnsMustEl.style.visibility="";
		this.disableTab(2);
		//this.disableTab(3);
		return false;
	}
}

/*
NormalReportManager.prototype.checkReportType	= function () {
	//var reportTypeEl			= document.getElementById("reportTypeMust") ;
	var fundingGroupingEl		= document.getElementById("fundingGroupingMust") ;
	//var reportTypeDivEl			= document.getElementById("reportTypeDiv") ;
	//var checkboxes				= reportTypeDivEl.getElementsByTagName("input");
	//if ( !isAnyChecked(checkboxes) ) {
	//	reportTypeEl.style.visibility="";
	//	this.disableTab(1);
	//	return false;
	//}
	//reportTypeEl.style.visibility="hidden";
	
	var reportGroupDivEl		= document.getElementById("reportGroupDiv") ;
	var radios					= reportGroupDivEl.getElementsByTagName("input");
	if ( !this.isAnyChecked(radios) ) {
		fundingGroupingEl.style.visibility="";
		this.disableTab(1);
		return false;
	}
	fundingGroupingEl.style.visibility="hidden";
	this.enableTab(1, "columns_tab_label");
	return true;
} */

NormalReportManager.prototype.isAnyChecked		= function ( inputs ) {
	for (i=0; i<inputs.length; i++) {
		if (inputs[i].checked)
			return true;
	}
	return false;
}

NormalReportManager.prototype.nextStep		= function ( ) {
	var curStep		= YAHOO.amp.reportwizard.tabView.get("activeIndex");
	if ( curStep < YAHOO.amp.reportwizard.numOfSteps-1 )
			YAHOO.amp.reportwizard.tabView.set("activeIndex", curStep+1);
	else 
		if ( curStep == YAHOO.amp.reportwizard.numOfSteps-1 )
			saveReportEngine.saveReport();
}
NormalReportManager.prototype.previousStep		= function ( ) {
	var curStep		= YAHOO.amp.reportwizard.tabView.get("activeIndex");
	if ( curStep > 0 )
			YAHOO.amp.reportwizard.tabView.set("activeIndex", curStep-1);
	else
		window.location.replace("/aim/showDesktop.do");
}

TabReportManager.prototype					= new NormalReportManager();
TabReportManager.prototype.constructor		= TabReportManager;
function TabReportManager() {
	;
}