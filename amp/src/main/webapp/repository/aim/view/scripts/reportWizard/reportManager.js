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
		fakeDivEl.style.height		= "50px";
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
		
//		debugger;
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
		
		columnsDragAndDropObject	= new ColumnsDragAndDropObject('source_col_div','dest_col_ul');
		columnsDragAndDropObject.createDragAndDropItems();
		new YAHOO.util.DDTarget('source_measures_ul');
		new YAHOO.util.DDTarget('dest_measures_ul');
		new YAHOO.util.DDTarget('source_hierarchies_ul');
		new YAHOO.util.DDTarget('dest_hierarchies_ul');
		measuresDragAndDropObject	= new MyDragAndDropObject('source_measures_ul','dest_measures_ul');
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
											rmParams.loadingDataMessage, rmParams.savingDataMessage, rmParams.cannotSaveFiltersMessage, !rmParams.reportUsesFilters, rmParams.settingsPanelName,
											rmParams.validationMsgs) ;
		
		if(rmParams.pledgedReport == true){
			repFilters.additionalParameter = "&pledged=true";
		}
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
		
		// calling function from filters.js
		toggleWorkspaceLinkedVisibility();
		//Weird events to catch onchange in IE
		YAHOO.util.Event.on("publicReportChkBox","blur", toggleWorkspaceLinkedVisibility); 
		YAHOO.util.Event.on("publicReportChkBox","click", toggleWorkspaceLinkedVisibility);
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

NormalReportManager.prototype.maxHierarchies	= 3;

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
};

NormalReportManager.prototype.disableTab	= function (tabIndex) {
	if ( tabIndex < YAHOO.amp.reportwizard.numOfSteps ) {
		var tab			= YAHOO.amp.reportwizard.tabView.getTab(tabIndex);
		if ( !tab.get("disabled") ) {
			tab.set("disabled", true);
			var labelEl		= document.getElementById( YAHOO.amp.reportwizard.tabLabels[tabIndex] );
                        if(labelEl!=null){
                            (new YAHOO.util.Element(labelEl)).replaceClass('unsel', 'disabled');
                        }
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
};

NormalReportManager.prototype.enableSave	= function () {
	btns	= document.getElementsByName("save");
	for ( var i=0; i<btns.length; i++ ) {
		this.enableToolbarButton( btns[i] );
	}
};

NormalReportManager.prototype.disableSave	= function () {
	btns	= document.getElementsByName("save");
	for ( var i=0; i<btns.length; i++ ) {
		this.disableToolbarButton( btns[i] );
	}
};

NormalReportManager.prototype.enableToolbarButton	= function (btn) {
	if ( btn.disabled ) {
		var imgEl		= btn.getElementsByTagName("img")[0];
		//var imgSrc		= imgEl.src.replace("_dis.png", ".png");
		
		//imgEl.src		= imgSrc;
		btn.disabled	= false;
		( new YAHOO.util.Element(btn) ).replaceClass('buttonx_dis', 'buttonx');
	}
};

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
};

NormalReportManager.prototype.checkSteps	= function () {
	createPreview();
	if ( this.checkReportDetails() )
		if ( this.checkColumns() )
			if ( this.checkHierarchies() )
				if ( this.checkMeasures() ) {
						this.checkReportName() ;
						return;
				}
	
	// If any of the checks above fails the save should be disabled
	this.disableSave();
};


NormalReportManager.prototype.callbackRepType = function (type) {
	this.callbackRepTypeCall.success =$.proxy(this.callbackRepTypeCall.success,this);
	var transaction = YAHOO.util.Connect.asyncRequest('GET', "/aim/reportWizard.do?action=getJSONrepType&repType=" + type, this.callbackRepTypeCall, null);
};


NormalReportManager.prototype.callbackRepTypeCall = {
	  success: function(o) {
		  try {
			    var results = YAHOO.lang.JSON.parse(o.responseText);
			    var ulElem = document.getElementById("source_measures_ul");
				var innerText = "";
				for(var i = 0; i < results.children.length; i++){
	    			innerText += "<li class='list1' id='measure_"+results.children[i].ID+"'>";
	    			innerText += "<input type='checkbox' value='"+results.children[i].name+"' style='line-height:15px; margin-top:6px;'/>";
	    			innerText += results.children[i].nameTrn;
					if (results.children[i].description.length > 0){
	    				innerText += "<img src= '../ampTemplate/images/help.gif' border='0' title='"+results.children[i].description+"'>";
	    			}
					innerText += "</li>";
	    		}
				ulElem.innerHTML = innerText;

				var destination = document.getElementById("dest_measures_ul");
				// clean-up the destination
				destination.innerHTML = "";

				var measuresDragAndDropObject = new MyDragAndDropObject('source_measures_ul','dest_measures_ul');
                measuresDragAndDropObject.createDragAndDropItems();
                this.checkMeasures ();
                /* Commented scenario below is workable, but has 2 bugs:
                1. Items are duplicated while dropping.
                2. Cannot Drug an item back from the destination */
				 /* var results = YAHOO.lang.JSON.parse(o.responseText);
					var newDDObj = new MyDragAndDropObject("source_measures_ul","dest_measures_ul");
					var hierUlEl = document.getElementById("source_measures_ul") ;
                	for (var i=0; i < results.children.length; i++ ) {
                        var innerText = results.children[i].nameTrn;
                        if (results.children[i].description.length > 0){
                            innerText += "<img src= '../ampTemplate/images/help.gif' border='0' title='"+results.children[i].description+"'>";
                        }
                        var newObj = ColumnsDragAndDropObject.generateLi(
                                    "measure_"+results.children[i].ID,
                                    "list1",
                                    "msr_chk_" + results.children[i].ID,
                                    results.children[i].name,
                                    "selectedMeasures", innerText
                        );
                        hierUlEl.appendChild( newObj );
                        var newDDProxy				= new YAHOO.util.DDProxy( newObj.id );
                        newDDObj.addActions( newDDProxy );
                	} */
			}
			catch (e) {
			    alert("Invalid respose." + e);
			}
	  },
	  failure: function(o) {//Fail silently
		  }
	};


NormalReportManager.prototype.checkReportDetails	= function () {
	this.enableTab(1);
	return true;

};

NormalReportManager.prototype.checkMeasures	= function () {
	// show the "you should select actual commitmens" message in case we're doing a Donor Report with "also show pledes"
	var actualCommitmentsMustEl = $('#actualCommitmentsMust');
	var alsoIncludePledgesCheckBox = $('input[name="alsoShowPledges"]').prop('checked');
	var showActualCommitmentsMessage = false;
	if (alsoIncludePledgesCheckBox) {
		var actualCommitmentsMeasureSelected = $('#dest_measures_ul span[original_measure_name="Actual Commitments"]').length > 0;
		showActualCommitmentsMessage = !actualCommitmentsMeasureSelected;
	};
	if (showActualCommitmentsMessage) {
		actualCommitmentsMustEl.show();
		return false;
	}
	actualCommitmentsMustEl.hide();
	
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
};

NormalReportManager.prototype.checkHierarchies	= function () {
	var ulEl			= document.getElementById("dest_hierarchies_ul") ;
	var colsUlEl		= document.getElementById("dest_col_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	var colItems		= colsUlEl.getElementsByTagName("li");
	var incompatible = false;
	var imcomplist = new Array();
	
	var retValue 			= true;
	var hierarchiesMustEl;
	
	for ( var int = 0; int < items.length; int++) {
		if (checkincompatiblehierarchies(getColDbId(items[int]))){
			imcomplist.push(items[int]);
		}
		if(imcomplist.length > 1){
			incompatible = true;
		}
	}
	if (incompatible){
		hierarchiesMustEl					= document.getElementById("incompatiblehierarchies");
		hierarchiesMustEl.style.visibility	= "";
		retValue	= false;
	}else {
		hierarchiesMustEl					= document.getElementById("incompatiblehierarchies");
		hierarchiesMustEl.style.visibility	= "hidden";
		//this.enableTab(3);
	}
	
	if ( items.length > this.maxHierarchies ) {
		hierarchiesMustEl					= document.getElementById("hierarchiesMust");
		hierarchiesMustEl.style.visibility	= "";
		retValue							= false;
//		this.disableTab(3);
//		return false;
	}
	else {
		hierarchiesMustEl					= document.getElementById("hierarchiesMust");
		hierarchiesMustEl.style.visibility	= "hidden";
//		this.enableTab(3);
//		return true;
	}
	if (items.length > 0 && items.length == colItems.length && !getHideActivities() ) {
		hierarchiesMustEl					= document.getElementById("hierarchiesSummaryMust");
		hierarchiesMustEl.style.visibility	= "";
		retValue							= false;
	}
	else {
		hierarchiesMustEl					= document.getElementById("hierarchiesSummaryMust");
		hierarchiesMustEl.style.visibility	= "hidden";
	}
	
	if ( retValue ) {
		this.enableTab(3);
		return true;
	}
	else {
		this.disableTab(3);
		return false;
	}
	
};

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
};

NormalReportManager.prototype.checkReportName	= function () {
	var saveBtn = document.getElementById("last_save_button");
	var saveBtnY = new YAHOO.util.Element(saveBtn);
	
	var saveAndOpenBtnY = null;		
	var saveAndOpenBtn = document.getElementById("last_save_and_open_button");
	if (saveAndOpenBtn != null)
		saveAndOpenBtnY = new YAHOO.util.Element(saveAndOpenBtn);
	
	var strTitle	= getReportTitle();
	strTitle		= strTitle.replace(/\s*/, "");
	if ( strTitle == "" ) {
		//saveBtn.disabled = true;
		//saveBtnY.removeClass("buttonx");
		//saveBtnY.addClass("buttonx_dis");
		//btnY.setStyle("color", "lightgrey");
		
		if (saveAndOpenBtn != null)
		{
			//saveAndOpenBtn.disabled = true;
			//saveAndOpenBtnY.removeClass("buttonx");
			//saveAndOpenBtnY.addClass("buttonx_dis");
		}
		return false;
	}
	else
	{
//		saveBtn.disabled = false;
//		saveBtnY.removeClass("buttonx_dis");
//		saveBtnY.addClass("buttonx");
		//btnY.setStyle("color", "");

		if (saveAndOpenBtn != null)
		{
//			saveAndOpenBtn.disabled = false;
//			saveAndOpenBtnY.removeClass("buttonx_dis");
//			saveAndOpenBtnY.addClass("buttonx");
		}
		return true;
	}
};

NormalReportManager.prototype.isAnyChecked		= function ( inputs ) {
	for (var i=0; i<inputs.length; i++) {
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
