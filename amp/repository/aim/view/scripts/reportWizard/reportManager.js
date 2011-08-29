function createPreview () {
	var divElWrapper	= document.getElementById("previewSectionDiv");
	var divEl			= document.getElementById("previewBodySectionDiv");
	if (divEl == null)
		return;
	var fakeDivEl	= document.getElementById("fakePreviewSectionDiv");
	divEl.innerHTML	= "";
	
	var colArray		= getSelectedFieldsNames("dest_col_ul");
	var hierArray		= getSelectedFieldsNames("dest_hierarchies_ul");
	var summary			= getHideActivities();
	
	if ( (colArray.length != 0 && !summary) || hierArray.length != 0 ) {
		divElWrapper.style.display		= "";
		fakeDivEl.style.display		= "";
		new ReportPreviewEngine(populateRPS(new ReportPreviewSettings())).renderTable('previewBodySectionDiv');		
	}
	else {
		fakeDivEl.style.display		= "none";
		divElWrapper.style.display	= "none";
	}
}

function populateRPS(rpSettings) {
	var colArray		= getSelectedFieldsNames("dest_col_ul");
	var hierArray		= getSelectedFieldsNames("dest_hierarchies_ul");
	var measArray		= getSelectedFieldsNames("dest_measures_ul");
	for ( var i=0; i<hierArray.length; i++ ) {
		var hier	= hierArray[i];
		for (var j = 0; j < colArray.length; j++) {
			var col = colArray[j];
			if ( hier == col ) {
				colArray.splice(j, 1);
				break;
			}
		}
	}
	if ( measArray == null || measArray.length == 0 ) {
		measArray.push(repManagerParams.previewUnselectedMeasureTrn);
	}
	
	var period			= getReportPeriod();
	if (  period == "M" ) {
		rpSettings.months		= true;
	}
	else
		rpSettings.months		= false;
	if (  period == "Q" ) {
		rpSettings.quarters		= true;
	}
	else
		rpSettings.quarters		= false;
	if (  period == "N" ) {
		rpSettings.totalsOnly		= true;
	}
	else
		rpSettings.totalsOnly		= false;
	
	
	rpSettings.columns			= colArray;
	rpSettings.hierarchies		= hierArray;
	rpSettings.measures			= measArray;
	rpSettings.summary			= getHideActivities();
	
	
	
	return rpSettings;
}

function togglePreview () {
	var divElWrapper	= document.getElementById("previewSectionDiv");
	var divEl			= document.getElementById("previewBodySectionDiv");
	var headerImgEl		= document.getElementById("previewHeaderSectionImg");
	if (divEl == null)
		return;
	var fakeDivEl	= document.getElementById("fakePreviewSectionDiv");
	
	if ( divEl.style.display	== "none" ) {
		divElWrapper.style.height	= "220px";
		fakeDivEl.style.height		= "230px";
		divEl.style.display			= "";
		headerImgEl.src				= "/TEMPLATE/ampTemplate/images/preview_open.gif";
	}
	else {
		divElWrapper.style.height	= "45px";
		fakeDivEl.style.height		= "50px";
		divEl.style.display	= "none";
		headerImgEl.src		= "/TEMPLATE/ampTemplate/images/preview_close.gif";
	}
}

function continueInitialization( e, rmParams ){
	
		if (rmParams.onePager)
			new KeepWithScroll("toolbarMarkerDiv", "toolbarDivStep0" );
	
		aimReportWizardForm.reportDescriptionClone.value	= unescape(aimReportWizardForm.reportDescription.value);
		treeObj = new DHTMLSuite.JSDragDropTree();
		treeObj.setTreeId('dhtmlgoodies_tree');
		treeObj.init();
		//treeObj.minusImage = 'DHTMLSuite_plus.gif';
		treeObj.showHideNode(false,'dhtmlgoodies_tree');

		
		
		if ( rmParams.desktopTab )
			if ( rmParams.onePager )
				repManager	= new OPTabReportManager();
			else
				repManager		= new TabReportManager();
		else
			if ( rmParams.onePager )
				repManager		= new OPNormalReportManager();
			else
				repManager		= new NormalReportManager();
		
		repManager.hierarchiesVisibility	= new ShowHideElement("hierarchies_step_div");
		repManager.hFieldsetVisibility		= new ShowHideElement("hierarchiesInfoFieldset");
		
		var saveBtns		= document.getElementsByName("save");	
		for (var i=0; i<saveBtns.length; i++  ) {
			repManager.addStyleToButton(saveBtns[i]);
		}
		for (var i=0; i<YAHOO.amp.reportwizard.numOfSteps; i++) {
			repManager.addStyleToButton("step"+ i +"_prev_button");
			repManager.addStyleToButton("step"+ i +"_next_button");
			repManager.addStyleToButton("step"+ i +"_add_filters_button");
			repManager.addStyleToButton("step"+ i +"_add_settings_button");
			repManager.addStyleToButton("step"+ i +"_cancel");
		}
		
		columnsDragAndDropObject	= new ColumnsDragAndDropObject('source_col_div');
		columnsDragAndDropObject.createDragAndDropItems();
		new YAHOO.util.DDTarget('source_measures_ul');
		new YAHOO.util.DDTarget('dest_measures_ul');
		new YAHOO.util.DDTarget('source_hierarchies_ul');
		new YAHOO.util.DDTarget('dest_hierarchies_ul');
		measuresDragAndDropObject	= new MyDragAndDropObject('source_measures_ul');
		measuresDragAndDropObject.createDragAndDropItems();
		
		//createDragAndDropItems('source_ul');
		//createDragAndDropItems('dest_col_ul');
		//new YAHOO.util.DDTarget('dest_li_1');
		//new YAHOO.util.DD('logDiv');
		if ( !rmParams.onePager ) {
			for (var i=1; i<YAHOO.amp.reportwizard.numOfSteps; i++) {
				tab		= YAHOO.amp.reportwizard.tabView.getTab(i);
				tab.set("disabled", true);
			}
			tab2	= YAHOO.amp.reportwizard.tabView.getTab(2);
			tab2.addListener("beforeActiveChange", generateHierarchies);
		}
		ColumnsDragAndDropObject.selectObjsByDbId ("source_col_div", "dest_col_ul", selectedCols);
		generateHierarchies();
		MyDragAndDropObject.selectObjsByDbId ("source_hierarchies_ul", "dest_hierarchies_ul", selectedHiers);
		MyDragAndDropObject.selectObjsByDbId ("source_measures_ul", "dest_measures_ul", selectedMeas);

		repFilters					= new Filters(rmParams.filterPanelName, rmParams.failureMessage, rmParams.filterProblemsMessage, 
											rmParams.loadingDataMessage, rmParams.savingDataMessage, rmParams.cannotSaveFiltersMessage, !rmParams.reportUsesFilters ) ;
		
		saveReportEngine			= new SaveReportEngine(rmParams.savingRepTabMessage,rmParams.failureMessage);
											
		var dg			= document.getElementById("DHTMLSuite_treeNode1");
		var cn			= dg.childNodes;
		
		for (var i=0; i<cn.length; i++) {
			if ( cn[i].nodeName.toLowerCase()=="input" || cn[i].nodeName.toLowerCase()=="img" ||
				cn[i].nodeName.toLowerCase()=="a" )
				cn[i].style.display		= "none";
		}
		repManager.checkSteps();
		repManager.disableToolbarButton( document.getElementById("step3_next_button") );
		
	}

function toggleMoreSettings() {
	var infoDivSH	= new ShowHideElement("moreSettingsInfoDiv");
	var tableSH		= new ShowHideElement("moreSettingsTable");
	var legendEl	= document.getElementById("moreSettingsLegend");
	
	infoDivSH.toggle();
	var visible	= tableSH.toggle();
	
	if (visible) {
		legendEl.innerHTML	= "-" + legendEl.innerHTML.substring(1);
	}
	else
		legendEl.innerHTML	= "+" + legendEl.innerHTML.substring(1);
}

function NormalReportManager () {
	this.hierarchiesVisibility	= null;
	this.hFieldsetVisibility	= null;
}
NormalReportManager.prototype.enableTab		= function (tabIndex) {
	if ( tabIndex < YAHOO.amp.reportwizard.numOfSteps ) {
		var tab			= YAHOO.amp.reportwizard.tabView.getTab(tabIndex);
		if ( tab.get("disabled") ) {
			tab.set("disabled", false);
			var labelId		= YAHOO.amp.reportwizard.tabLabels[tabIndex];
			var labelEl		= new YAHOO.util.Element(labelId);
			labelEl.replaceClass('disabled', 'unsel');
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
		//var imgSrc		= imgEl.src.replace("_dis.png", ".png");
		
		//imgEl.src		= imgSrc;
		btn.disabled	= false;
		( new YAHOO.util.Element(btn) ).replaceClass('buttonx_dis', 'buttonx');
	}
}

NormalReportManager.prototype.disableToolbarButton	= function (btn) {
	if ( btn != null ) {
		if ( !btn.disabled ) {
			var imgEl		= btn.getElementsByTagName("img")[0];
			//var imgSrc		= imgEl.src.replace(".png", "_dis.png");
			//imgEl.src		= imgSrc;
			
			btn.disabled	= true;
			( new YAHOO.util.Element(btn) ).replaceClass('buttonx', 'buttonx_dis');
		}
	}
}

NormalReportManager.prototype.checkSteps	= function () {
	createPreview();
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
		//btnY.setStyle("color", "lightgrey");
		btnY.removeClass("buttonx");
		btnY.addClass("buttonx_dis");
		return false;
	}
	else {
		btn.disabled		= false;
		//btnY.setStyle("color", "");
		btnY.removeClass("buttonx_dis");
		btnY.addClass("buttonx");
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

NormalReportManager.prototype.nextStep		= function (step) {
	var curStep		= YAHOO.amp.reportwizard.tabView.get("activeIndex");
	if ( curStep < YAHOO.amp.reportwizard.numOfSteps-1 )
			YAHOO.amp.reportwizard.tabView.set("activeIndex", curStep+1);
	else 
		if ( curStep == YAHOO.amp.reportwizard.numOfSteps-1 )
			saveReportEngine.saveReport();
	
	//document.getElementById("rgTitle").innerHTML = "Step " + step + " of 4";
}
NormalReportManager.prototype.previousStep		= function (step) {
	var curStep		= YAHOO.amp.reportwizard.tabView.get("activeIndex");
	if ( curStep > 0 )
			YAHOO.amp.reportwizard.tabView.set("activeIndex", curStep-1);
	else
		window.location.replace("/aim/showDesktop.do");
	
	//document.getElementById("rgTitle").innerHTML = "Step " + step + " of 4";
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
NormalReportManager.prototype.showHideHierarchies	= function(){};


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

OPTabReportManager.prototype.showHideHierarchies	= function(){};



OPNormalReportManager.prototype					= new NormalReportManager();
OPNormalReportManager.prototype.constructor		= OPNormalReportManager;
function OPNormalReportManager() {
	;
}


OPNormalReportManager.prototype.enableTab		= function(tabIndex){
}
OPNormalReportManager.prototype.disableTab		= function(tabIndex){
}
OPNormalReportManager.prototype.showHideHierarchies	= function(){
	var availableHierchies	= document.getElementById("source_hierarchies_ul").getElementsByTagName("li");
	var selectedHierchies	= document.getElementById("dest_hierarchies_ul").getElementsByTagName("li");
	if (availableHierchies.length > 0 || selectedHierchies.length > 0 ) {
		this.hFieldsetVisibility.hide();
		this.hierarchiesVisibility.show();
	}
	else {
		this.hierarchiesVisibility.hide();
		this.hFieldsetVisibility.show();
	}
};




OPTabReportManager.prototype					= new TabReportManager();
OPTabReportManager.prototype.constructor		= OPTabReportManager;
function OPTabReportManager() {
	;
}


OPTabReportManager.prototype.enableTab		= function(tabIndex){
}
OPTabReportManager.prototype.disableTab		= function(tabIndex){
}

OPTabReportManager.prototype.showHideHierarchies	= function(){
	var availableHierchies	= document.getElementById("source_hierarchies_ul").getElementsByTagName("li");
	var selectedHierchies	= document.getElementById("dest_hierarchies_ul").getElementsByTagName("li");
	if (availableHierchies.length > 0 || selectedHierchies.length > 0 ) {
		this.hFieldsetVisibility.hide();
		this.hierarchiesVisibility.show();
	}
	else {
		this.hierarchiesVisibility.hide();
		this.hFieldsetVisibility.show();
	}
};






function ShowHideElement ( elId ) {
	this.elId	= elId;
}

ShowHideElement.prototype.show	= function(  ) {
	var el	= document.getElementById(this.elId);
	el.style.display	= "";
};

ShowHideElement.prototype.hide	= function(  ) {
	var el		= document.getElementById(this.elId);
	el.style.display	= "none";
};

ShowHideElement.prototype.toggle	= function( ) {
	var el	= document.getElementById(this.elId);
	if ( el.style.display	== "none") {
		el.style.display	= "";
		return true;
	}
	else {
		var currH	= YAHOO.util.Dom.getStyle(el, "height");
		el.style.display	= "none";
		return false;
	}
};


