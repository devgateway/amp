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
	this.enableToolbarButton(btn);
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
	this.disableToolbarButton(btn);
	
	if ( tabIndex < YAHOO.amp.reportwizard.numOfSteps-1 )
		this.disableTab(tabIndex+1);
	
	if ( tabIndex == YAHOO.amp.reportwizard.numOfSteps-1 ) {
		this.disableSave();
	}
}

NormalReportManager.prototype.enableSave	= function () {
	btns	= document.getElementsByName("save");
	for ( var i=0; i<btns.length; i++ ) {
		this.enableToolbarButton( btns[i] );
	}
}

NormalReportManager.prototype.disableSave	= function () {
	btns	= document.getElementsByName("save");
	for ( var i=0; i<btns.length; i++ ) {
		this.disableToolbarButton( btns[i] );
	}
}

NormalReportManager.prototype.enableToolbarButton	= function (btn) {
	if ( btn.disabled ) {
		var imgEl		= btn.getElementsByTagName("img")[0];
		var imgSrc		= imgEl.src.replace("_dis.png", ".png");
		
		imgEl.src		= imgSrc;
		btn.disabled	= false;
		( new YAHOO.util.Element(btn) ).replaceClass('toolbar-dis', 'toolbar');
	}
}

NormalReportManager.prototype.disableToolbarButton	= function (btn) {
	if ( !btn.disabled ) {
		var imgEl		= btn.getElementsByTagName("img")[0];
		var imgSrc		= imgEl.src.replace(".png", "_dis.png");
		imgEl.src		= imgSrc;
		
		btn.disabled	= true;
		( new YAHOO.util.Element(btn) ).replaceClass('toolbar', 'toolbar-dis');
	}
}

NormalReportManager.prototype.checkSteps	= function () {
	if ( this.checkReportDetails() )
		if ( this.checkColumns() )
			if ( this.checkHierarchies() )
				if ( this.checkMeasures() )
						this.checkReportName() ;
}

NormalReportManager.prototype.checkReportDetails	= function () {
	this.enableTab(1);
	return true;

}

NormalReportManager.prototype.checkMeasures	= function () {
	var ulEl			= document.getElementById("dest_measures_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	measuresMustEl		= document.getElementById("measuresMust");
	if ( items.length > 0 ) {
		
		measuresMustEl.style.visibility="hidden";
		this.enableSave();
		return true;
	}
	else {
		measuresMustEl.style.visibility="";
		this.disableSave();
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
		return true;
	}
	else {
		columnsMustEl	= document.getElementById("columnsMust");
		columnsMustEl.style.visibility="";
		this.disableTab(2);
		return false;
	}
}

NormalReportManager.prototype.checkReportName	= function () { 
	var btn 	= document.getElementById("last_save_button");
	var btnY	= new YAHOO.util.Element(btn);
	var strTitle	= getReportTitle();
	strTitle		= strTitle.replace(/\s*/, "");
	if ( strTitle == "" ) {
		btn.disabled		= true;
		btnY.setStyle("color", "lightgrey");
		return false;
	}
	else {
		btn.disabled		= false;
		btnY.setStyle("color", "black");
		return true;
	}
}

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
NormalReportManager.prototype.addStyleToButton	= function ( id ) { 
	function addClass(e) {
		var btnY	= new YAHOO.util.Element( this );
		btnY.addClass( "ieover" );
	}
	function removeClass(e) {
		var btnY	= new YAHOO.util.Element( this );
		btnY.removeClass( "ieover" );
	}

	YAHOO.util.Event.addListener(id, "mouseover", addClass) ;
	YAHOO.util.Event.addListener(id, "mouseout", removeClass) ;
}
NormalReportManager.prototype.showUseFilters	= function ( check ) {
	document.getElementById("useFiltersCheckbox").checked	= check;
	
	var spanElem				= document.getElementById("spanUseFilters");
	spanElem.style.visibility	= "visible";
}
NormalReportManager.prototype.hideUseFilters	= function ( check ) {
	document.getElementById("useFiltersCheckbox").checked	= check;
	var spanElem				= document.getElementById("spanUseFilters");
	spanElem.style.visibility	= "hidden";
}
NormalReportManager.prototype.decideStrikeFilters	= function () {
	var checkboxElem						= document.getElementById("useFiltersCheckbox");
	var filtersDivElem						= document.getElementById( "listFiltersDiv" ) ;
	if ( checkboxElem.checked ) {
		filtersDivElem.style.textDecoration		= "";
	}
	else
		filtersDivElem.style.textDecoration		= "line-through";
}
NormalReportManager.prototype.cancelWizard	= function () {
	window.location = "/viewTeamReports.do?tabs=false";
}

TabReportManager.prototype					= new NormalReportManager();
TabReportManager.prototype.constructor		= TabReportManager;
function TabReportManager() {
	;
}
TabReportManager.prototype.checkColumns	= function () {
	var ulEl			= document.getElementById("dest_col_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	
	if ( items.length > 0 && items.length <= 3 ) {
		columnsMustEl	= document.getElementById("columnsMust");
		columnsMustEl.style.visibility="hidden";
		columnsLimitEl	= document.getElementById("columnsLimit");
		columnsLimitEl.style.visibility="hidden";
		this.enableTab(2);
		return true;
	}
	else {
		
		columnsMustEl	= document.getElementById("columnsMust");
		columnsLimitEl	= document.getElementById("columnsLimit");
		if ( items.length == 0 )
			columnsMustEl.style.visibility="visible";
		else
			columnsMustEl.style.visibility="hidden";
		if ( items.length > 3 )
			columnsLimitEl.style.visibility="visible";
		else
			columnsLimitEl.style.visibility="hidden";
		this.disableTab(2);
		return false;
	}
}
TabReportManager.prototype.checkMeasures	= function () {
	var ulEl			= document.getElementById("dest_measures_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	measuresMustEl		= document.getElementById("measuresMust");
	measuresLimitEl		= document.getElementById("measuresLimit");
	if ( items.length > 0 && items.length <= 2) {
		measuresMustEl.style.visibility="hidden";
		measuresLimitEl.style.visibility="hidden";
		this.enableSave();
		return true;
	}
	else {
		if ( items.length == 0 )
			measuresMustEl.style.visibility		= "visible";
		else
			measuresMustEl.style.visibility		= "hidden";
		if ( items.length > 2 )
			measuresLimitEl.style.visibility	= "visible";
		else
			measuresLimitEl.style.visibility	= "hidden";
		this.disableSave();
		return false;
	}
}
TabReportManager.prototype.cancelWizard	= function () {
	window.location = "/viewTeamReports.do?tabs=true";
}

