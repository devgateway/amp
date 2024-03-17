$D = YAHOO.util.Dom;
$E = YAHOO.util.Event;
var yuiLoadingPanel;

function checkUncheckRelatedEntities(option,name,id){
	uncheckAllRelatedEntities(name);
	checkRelatedEntities(option,name,id);
}

function allDescendantsChecked(option, id){
if (option.checked) {
	$('#'+id+' :checkbox').attr('checked', 'checked');
}
else {
	$('#'+id+' :checkbox').removeAttr('checked');
}
}

function allOptionChecked(option,name,subname){
	if(option.checked)
	{
		// set all
		$("input[name='"+name+"']").attr('checked', 'checked');
		$("input[name='"+subname+"']").attr('checked', 'checked');
		option.checked=true;
	}
	else
	{
		// remove all
		$("input[name='"+name+"']").removeAttr('checked');
		$("input[name='"+subname+"']").removeAttr('checked');
		option.checked = false;
	}
}

function uncheckAllRelatedEntities(name){
	$("input[name='"+name+"']").removeAttr('checked');
}
function checkRelatedEntities(option,name,id){
	var options=$("input[class='"+name+"_"+id+"']");
	if(option.checked){
		options.attr('checked','checked');
	}
	else{
		options.removeAttr('checked');
	}
	
}
function uncheckAllOption(name){
	$("#"+name+"_all").removeAttr('checked');
}
function checkParentOption(name, id){
	var options = document.getElementsByName(name);
	for ( var i = 0; i < options.length; i++) {
		if (options[i].value == id){
			options[i].checked = true;
		}
	}
}

var currentIndex=-1;
var searchterm;
var searchResult;
function findNext(divId){
	if(currentIndex==-1){
		searchterm=$("#"+divId+"_search").val();
		searchResult=$("#"+divId+" span:containsi('"+searchterm+"')");
		searchResult.css("font-weight","bold");
	}
	if(searchResult.length-1>currentIndex){
		searchResult.css("color","black");
		currentIndex++;
		var currentSpan=searchResult.eq(currentIndex);
		currentSpan.css("color","red");
		currentSpan.prev().focus(); 
	}
}
function findPrev(divId){
	if(currentIndex==-1){
		searchterm=$("#"+divId+"_search").val();
		searchResult=$("#"+divId+" span:containsi('"+searchterm+"')");
		searchResult.css("font-weight","bold");
	}
	if(currentIndex>0){	
		searchResult.css("color","black");
		currentIndex--;
		var currentSpan=searchResult.eq(currentIndex);
		currentSpan.css("color","red");
		currentSpan.prev().focus(); 
	}
}
function clearSearch(divId){
	currentIndex=-1;
	$("#"+divId+" span").css("color","black").css("font-weight","normal");
}

YAHOO.namespace("YAHOO.amp");
popinPanels = new Array();

var myPanel = new YAHOO.widget.Panel("newPanel", {
	width:"800px",
	maxHeight:"520px",
	fixedcenter: true,
    constraintoviewport: false,
    underlay:"none",
    close:false,
    visible:false,
    modal:true,
    draggable:true,
    context: ["showbtn", "tl", "bl"]
    });
var panelStart=0;

function initPanel() {
	
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

function clearAllLocalSearchResults(){
	$("#orgGrpDivList span").css("color","black").css("font-weight","normal");
	$("#orgGrpDivList_search").val('');
	$("#regionDivList span").css("color","black").css("font-weight","normal");
	$("#regionDivList_search").val('');
	$("#sectorDivList span").css("color","black").css("font-weight","normal");
	$("#sectorDivList_search").val('');
	currentIndex=-1;
}

function showPopin() {
	if ( popinPanels['Panel1'] == null ) {
		popinPanels['Panel1'] = new YAHOO.widget.Panel('Panel1', {
		width:"800px",
		maxHeight:"520px",
		fixedcenter: true,
	    constraintoviewport: false,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    });
		popinPanels['Panel1'].render(document.body);
		var msg=trnAdvancedFilter;
		popinPanels['Panel1'].setHeader(msg);
		var element = document.getElementById("dialog2");
		element.style.display 	= "inline";
		popinPanels['Panel1'].setBody(element);
	}
	popinPanels['Panel1'].show();
	changeTab(0);
}

function hidePopin() {
	popinPanels['Panel1'].hide();
}

function getOptionChecked (elements){
	var cnt = 0;
	while (document.getElementById("" + elements + cnt) != null) {
		if (document.getElementById("" + elements + cnt).checked == true) {
			return document.getElementById("" + elements + cnt).value;
		}
		cnt++;
	}
	return 0;
}

function resetToDefaults(){
	//loadingPanel.show();
	unCheckOptions("org_grp_check");
	unCheckOptions("region_check");
	unCheckOptions("sector_config_check");
	unCheckOptions("sector_check");
	unCheckOptions("organization_check");
	unCheckOptions("zone_check");
	unCheckOptions("sub_sector_check");
	unCheckOptions("structures_check");
	unCheckOptions("aidmodality_check");
	unCheckOptions("typeofassis_check");
	unCheckOptions("projectst_check");
	unCheckOptions("orgtypes_check");
	unCheckOptions("selectedNatPlanObj");
	unCheckOptions("selectedPrimaryPrograms");
	unCheckOptions("selectedSecondaryPrograms");
	
	
	if (document.getElementById("workspace_only")!=null){
		document.getElementById("workspace_only").checked = false;
	}
	
	
	document.getElementById("transaction_type_0").checked = true;
	document.getElementById("transaction_type_1").checked = false;
	if (document.getElementById("transaction_type_127")!=null){
		document.getElementById("transaction_type_127").checked = false;
	}
	
	document.getElementById("currencies_dropdown_ids").value = document.getElementById("defaultCurrencyId").value;
	document.getElementById("fiscalCalendar_dropdown_Id").value = document.getElementById("defaultFiscalCalendarId").value;
	if (document.getElementById("selected_Peacebuilding_Marker_Id") != null) {
		document.getElementById("selected_Peacebuilding_Marker_Id").selectedIndex  = 0;
	}
	document.getElementById("budget_dropdown").value = document.getElementById("selectedBudget").value;
	document.getElementById("startYear_dropdown").value = document.getElementById("defaultStartYear").value;
	document.getElementById("endYear_dropdown").value = document.getElementById("defaultEndYear").value;
	document.getElementById("budget_dropdown").selectedIndex = 0;
	applyFilterPopin();
}

function removeOptionsDropdown(object){
	obj = document.getElementById(object);
	for(var i = 1; i < obj.options.length; i++){
		obj.options[i].remove;
	}
	obj.options.length = 0;
	obj.options[0] = new Option("All", -1);
}
function removeOptions (obj){
	var div = document.getElementById(obj);
	div.innerHTML = "";
}

function unCheckOptions (obj){
	var elems = document.getElementsByName(obj);
	for(i=0;i<elems.length;i++){
		elems[i].checked=false;
	}
}

function changeTab (selected){
	for(var i=0;i<9;i++){
		if(i!=selected){
			$("#general_selector_"+i).removeClass("side_opt_sel");	
		}
		else{
			$("#general_selector_"+i).addClass("side_opt_sel");	
		}
	}
	$("#generalInfoId").css("display","none");
	$("#orgGrpContent").css("display","none");
	$("#regionDivContent").css("display","none");
	$("#sectorDivContent").css("display","none");
	$("#structuresDivContent").css("display","none");
	$("#aidmodalityDivContent").css("display","none");
	$("#typeofassisDivContent").css("display","none");
	$("#projectstatusDivContent").css("display","none");
	$("#orgtypeDivContent").css("display","none");
	$("#programDivContent").css("display","none");
	if(selected!=0){
		clearAllLocalSearchResults();
	}
	
	
	switch (selected) {
	case 0:
		$("#generalInfoId").css("display","block");
		break;
	case 1:
		$("#orgGrpContent").css("display","block");
		break;
	case 2:
		$("#regionDivContent").css("display","block");
		break;
	case 3:
		$("#sectorDivContent").css("display","block");
		break;
	case 4:
		$("#orgtypeDivContent").css("display","block");
		break;
	case 9:
		$("#structuresDivContent").css("display","block");
		break;
	case 5:
		$("#aidmodalityDivContent").css("display","block");
		break;
	case 6:
		$("#typeofassisDivContent").css("display","block");
		break;
	case 7:
		$("#projectstatusDivContent").css("display","block");
		break;
	case 8:
		$("#programDivContent").css("display","block");
		break;
	default:
		break;
	}
}


		
function getChecked (checkName){
	var count = 0;
	var id = 0;
	if (checkName!=null){
		var checks = document.getElementsByName(checkName);
		for (i=0; i<checks.length; i++) {
		  if (checks[i].checked) {
		    count++;
		    id = checks[i].value;
		  }
		}
	}
	if (count > 1) {
		return -1;
	} else {
		return id;
	}
}


function toggleSettings(){
	
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = trnShowFilterSetttings;
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = trnHideFilterSetttings;
	}
}

var callbackChildrenCall = {
	  success: function(o) {
		  try {
			    var results = YAHOO.lang.JSON.parse(o.responseText);
			    switch(results.objectType)
			    {
				    case "Organization":
			    		var orgDropdown = document.getElementById("org_dropdown_id");
			    		orgDropdown.options.length = 0;
			    		orgDropdown.options[0] = new Option(trnAll, -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
				    case "Sector":
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
			    		subSectorDropdown.options.length = 0;
			    		subSectorDropdown.options[0] = new Option(trnAll, -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			subSectorDropdown.options[subSectorDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Region":
			    		var zonesDropdown = document.getElementById("zone_dropdown_id");
			    		zonesDropdown.options.length = 0;
			    		zonesDropdown.options[0] = new Option(trnAll, -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			zonesDropdown.options[zonesDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Config":
			    		var sectorDropdown = document.getElementById("sector_dropdown_id");
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
			    		subSectorDropdown.options.length = 0;
			    		sectorDropdown.options.length = 0;
			    		subSectorDropdown.options[0] = new Option(trnAll, -1);
			    		sectorDropdown.options[0] = new Option(trnAll, -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			sectorDropdown.options[sectorDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "FiscalCalendar":
			    		var startYearSelectedIndex = document.getElementById("startYear_dropdown").selectedIndex;
			    		var endYearSelectedIndex = document.getElementById("endYear_dropdown").selectedIndex;
			    		var startYearQuickFilterDropdown = document.getElementById("startYearQuickFilter_dropdown");
			    		var endYearQuickFilterDropdown = document.getElementById("endYearQuickFilter_dropdown");
			    		var startYearDropdown = document.getElementById("startYear_dropdown");
			    		var endYearDropdown = document.getElementById("endYear_dropdown");
			    		startYearQuickFilterDropdown.options.length = 0;
			    		endYearQuickFilterDropdown.options.length = 0;
			    		startYearDropdown.options.length = 0;
			    		endYearDropdown.options.length = 0;
			    		for(var i = 0; i < results.children.length; i++){
			    			startYearQuickFilterDropdown.options[startYearQuickFilterDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
			    			endYearQuickFilterDropdown.options[endYearQuickFilterDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
			    			startYearDropdown.options[startYearDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
			    			endYearDropdown.options[endYearDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
			    		}
			    		startYearQuickFilterDropdown.selectedIndex = startYearSelectedIndex;
			    		endYearQuickFilterDropdown.selectedIndex = endYearSelectedIndex;
			    		startYearDropdown.selectedIndex = startYearSelectedIndex;
			    		endYearDropdown.selectedIndex = endYearSelectedIndex;
			    		break;
			    		
			    }
			}
			catch (e) {
			    alert("Invalid respose.");
			}
	  },
	  failure: function(o) {//Fail silently
		  }
	};

function callbackChildren(e) {
	var parentId, targetId, targetObj;
	//if (e == undefined){
		parentId = this.value;
		targetId = this.id;
		targetObj = this;
	//}
	//else
	//{
	//	parentId = e.target.value;
	//	targetId = e.target.id;
	//	targetObj = e.target;
	//}
	
	var objectType = "";

	switch(targetId){
		case "sector_config_dropdown_id":
		objectType = "Config";
		break;
		case "sector_dropdown_id":
			objectType = "Sector";
			//try to set the SectorProfileItemId from select:
			try {
				document.getElementById("SectorProfileItemId").value = parentId;
			}
			catch(e){
					
			}
			break;
		case "region_dropdown_id":
			objectType = "Region";
			//try to set the SectorProfileItemId from select:
			try {
				document.getElementById("RegionProfileItemId").value = parentId;
			}
			catch(e){
					
			}
			break;
		case "org_group_dropdown_id":
			objectType = "Organization";
			break;
		case "fiscalCalendar_dropdown_Id":
			objectType = "FiscalCalendar";
			break;
	}

	if (parentId != "" && objectType != ""){
		var transaction = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getJSONObject&objectType=" + objectType + "&parentId=" + parentId, callbackChildrenCall, null);
	}
}

function countSelected (selector){
	var count = 0;
	if (selector!=null){
		for (i=0; i<selector.options.length; i++) {
		  if (selector.options[i].selected) {
		    count++;
		  }
		}
	}
	return count;
}

var callbackApplyFilterCall = {
		  success: function(o) {
			  try {
				  var refreshHighlight = false;
				  //if the highlight layer is activated, refresh it
				  if(map.getLayer("highlightMap") !== undefined && map.getLayer("highlightMap").visible){
					  refreshHighlight = true;
					}
				  getStructures(true);
				  getSelectedFilter();
				  getActivities(true);
				  if (map.getLayer("regionalFeatureLayer") != null) {
				  	createPeaceBuildingFeatureLayer(map);
					}
				}
				catch (e) {
				    alert("Invalid response.");
				}
		  },
		  failure: function(o) {
			  loadingPanel.hide();
		  }
		};



function callbackApplyFilter(e){
	if (!hasFlash()){
		showInstallFlashPopin();
		return;
	}
	panelLoaded = false;
	if (document.getElementById("workspaceOnlyQuickFilter")!=null){
		document.getElementById("workspaceOnly").value = document.getElementById("workspaceOnlyQuickFilter").checked;
		document.getElementById("workspace_only").checked = document.getElementById("workspaceOnlyQuickFilter").checked;
	}
	document.getElementById("currencyId").value = document.getElementById("currencyQuickFilter_dropdown").value;
	document.getElementById("currencies_dropdown_ids").value = document.getElementById("currencyQuickFilter_dropdown").value;
	document.getElementById("startYear").value = document.getElementById("startYearQuickFilter_dropdown").value;
	document.getElementById("endYear").value = document.getElementById("endYearQuickFilter_dropdown").value;
	document.getElementById("startYear_dropdown").value = document.getElementById("startYearQuickFilter_dropdown").value;
	document.getElementById("endYear_dropdown").value = document.getElementById("endYearQuickFilter_dropdown").value;
	document.getElementById("transactionType").value = document.getElementById("transactionType_dropdown").value;
	document.getElementById("showAmountsInThousands").value = getSelectedValue("show_amounts_in_thousands");
	var dashboardType = document.getElementById("dashboardType").value;
	if (dashboardType==1) {
		document.getElementById("agencyType").value = document.getElementById("agencyTypeQuickFilter_dropdown").value;
		document.getElementById("agencyType_dropdown").value = document.getElementById("agencyTypeQuickFilter_dropdown").value;
	}
	
	if(document.getElementById("endYear").value < document.getElementById("startYear").value){
		alert(alertBadDate);	
		return;
	}
	
	var params = "";
	params = params + "&orgGroupIds=" + getQueryParameter("orgGroupIds");
	params = params + "&orgIds=" + getQueryParameter("orgIds");
	params = params + "&regionIds=" + getQueryParameter("regionIds");
	params = params + "&zoneIds=" + getQueryParameter("zoneIds");
	params = params + "&selSectorConfigId=" + getQueryParameter("selSectorConfigId");
	params = params + "&sectorIds=" + getQueryParameter("sectorIds");
	params = params + "&subSectorIds=" + getQueryParameter("subSectorIds");
	params = params + "&selectedNatPlanObj=" + getQueryParameter("selectedNatPlanObj");
	params = params + "&selectedPrimaryPrograms=" + getQueryParameter("selectedPrimaryPrograms");
	params = params + "&selectedSecondaryPrograms=" + getQueryParameter("selectedSecondaryPrograms");

	// visualization form and url will not work since old dashboards were removed in AMP-23740

	loadingPanel.show();

	YAHOO.util.Connect.setForm('visualizationform');

	var sUrl="/visualization/dataDispatcher.do?action=applyFilter" + params;

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);

	loadingPanel.loadingPanel.setBody("");
	refreshLoadingPanel();
	if(document.body.innerText){
		document.getElementById("filterOrgGroups").innerText = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
		document.getElementById("filterOrganizations").innerText = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
		document.getElementById("filterSectorConfiguration").innerText = document.getElementById("sector_config_dropdown_id").options[document.getElementById("sector_config_dropdown_id").selectedIndex].text;
		document.getElementById("filterSectors").innerText = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
		document.getElementById("filterRegions").innerText = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
		document.getElementById("filterStartYear").innerText = document.getElementById("startYearQuickFilter_dropdown").options[document.getElementById("startYearQuickFilter_dropdown").selectedIndex].text;
		document.getElementById("filterEndYear").innerText = document.getElementById("endYearQuickFilter_dropdown").options[document.getElementById("endYearQuickFilter_dropdown").selectedIndex].text;
	}
	else
	{
		document.getElementById("filterOrgGroups").textContent = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
		document.getElementById("filterOrganizations").textContent = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
		document.getElementById("filterSectorConfiguration").textContent = document.getElementById("sector_config_dropdown_id").options[document.getElementById("sector_config_dropdown_id").selectedIndex].text;
		document.getElementById("filterSectors").textContent = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
		document.getElementById("filterRegions").textContent = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
		document.getElementById("filterStartYear").textContent = document.getElementById("startYearQuickFilter_dropdown").options[document.getElementById("startYearQuickFilter_dropdown").selectedIndex].text;
		document.getElementById("filterEndYear").textContent = document.getElementById("endYearQuickFilter_dropdown").options[document.getElementById("endYearQuickFilter_dropdown").selectedIndex].text;
	}

}
var panelLoaded = false;
function refreshLoadingPanel(){
	if(!panelLoaded){
		var sUrl="/visualization/dataDispatcher.do?action=getProgress&rnd=" + Math.floor(Math.random()*50000);
		var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackUpdateLoadingPanel);
	}
}
var callbackUpdateLoadingPanel = {
		  success: function(o) {
			   loadingPanel.loadingPanel.setBody(o.responseText + '<br/> <img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
			   loadingPanel.loadingPanel.render(document.body);
		       var cancelLink = document.createElement('a');
		       $D.setStyle(cancelLink, 'cursor', 'pointer');
		       cancelLink.appendChild(document.createTextNode(trnCancel));
		       $E.on(cancelLink, 'click', function(e, o){
		           loadingPanel.loadingPanel.hide();
		           loadingPanel.cancelEvent.fire();
		           panelLoaded = true
		           if(navigator.appName == "Microsoft Internet Explorer")
			           window.document.execCommand('Stop');
		           else
			           window.stop();
		           }, {self:this});
		       loadingPanel.loadingPanel.appendToBody(document.createElement('br'));
		       loadingPanel.loadingPanel.appendToBody(cancelLink);
		       $D.setStyle(loadingPanel.body, 'text-align', 'center');

//			  loadingPanel.loadingPanel.setBody( + '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
			  if(!panelLoaded)
			  	setTimeout(refreshLoadingPanel, 1000);
		  },
		  failure: function(o) {
//			  alert("error");
		  }
		};

function applyFilterPopin(e){
	
	document.getElementById("startYear").value = document.getElementById("startYear_dropdown").options[document.getElementById("startYear_dropdown").selectedIndex].value;
	document.getElementById("endYear").value = document.getElementById("endYear_dropdown").options[document.getElementById("endYear_dropdown").selectedIndex].value;
	document.getElementById("currencyId").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("fiscalCalendarId").value = document.getElementById("fiscalCalendar_dropdown_Id").options[document.getElementById("fiscalCalendar_dropdown_Id").selectedIndex].value;
	
	if (document.getElementById("transaction_type_0").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_0").value;
	}
	if (document.getElementById("transaction_type_1").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_1").value;
	}
	if (document.getElementById("transaction_type_3") != null && document.getElementById("transaction_type_3").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_3").value;
	}	
	if (document.getElementById("transaction_type_127")!=null){
		if (document.getElementById("transaction_type_127").checked == true) {
			document.getElementById("transactionType").value = document.getElementById("transaction_type_127").value;
		}
	}
	document.getElementById("selectedBudget").value = document.getElementById("budget_dropdown").options[document.getElementById("budget_dropdown").selectedIndex].value;
	if (document.getElementById("selected_Peacebuilding_Marker_Id")!= null){
		document.getElementById("selectedPeacebuildingMarkerId").value = document.getElementById("selected_Peacebuilding_Marker_Id").options[document.getElementById("selected_Peacebuilding_Marker_Id").selectedIndex].value;
	}
	
	
	
	var params = "";
	params = params + "&orgGroupIds=" + getSelectionsFromElement("org_grp_check",false);
	params = params + "&orgIds=" + getSelectionsFromElement("organization_check",false);
	params = params + "&regionIds=" + getSelectionsFromElement("region_check",false);
	params = params + "&zoneIds=" + getSelectionsFromElement("zone_check",false);
	params = params + "&selSectorConfigId=" + getSelectionsFromElement("sector_config_check",false);
	params = params + "&sectorIds=" + getSelectionsFromElement("sector_check",false);
	params = params + "&subSectorIds=" + getSelectionsFromElement("sub_sector_check",false);
	params = params + "&structuresIds=" + getSelectionsFromElement("structures_check",false);
	params = params + "&aidmodalityIds=" + getSelectionsFromElement("aidmodality_check",false);
	params = params + "&typeofassissIds=" + getSelectionsFromElement("typeofassis_check",false);
	params = params + "&projectstIds=" + getSelectionsFromElement("projectst_check",false);
	params = params + "&orgtypesIds=" + getSelectionsFromElement("orgtypes_check",false);
	params = params + "&selectedNatPlanObj=" + getSelectionsFromElement("selectedNatPlanObj",false);
	params = params + "&selectedPrimaryPrograms=" + getSelectionsFromElement("selectedPrimaryPrograms",false);
	params = params + "&selectedSecondaryPrograms=" + getSelectionsFromElement("selectedSecondaryPrograms",false);
	params = params + "&nationalProjectsToo=" + getSelectionsFromElement("national_region_check",false);

	if(document.getElementById("endYear").value < document.getElementById("startYear").value){
		alert(alertBadDate);	
		return;
	}
	
	//loadingPanel.show();
	var ddf = document.getElementById("datadispatcherform_real_one"); // there are two forms with the same name of "datadispatcherform", so we should choose by ID instead - one of them has the id, while the other doesn't
	YAHOO.util.Connect.setForm(ddf);
	var sUrl="/esrigis/datadispatcher.do?applyfilter=true" + params;
	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
	hidePopin();
}

function getSelectionsFromElement(elementId, text){
	var sels = "";
	var cnt = 0;
	var elems=document.getElementsByName(elementId);
	for(i=0;i<elems.length;i++){
		if (elems[i].checked==true){
			if(sels != ""){
				sels = sels + ",";
			}
			if(text){
				sels = sels + elems[i].title;
			} else {
				sels = sels + elems[i].value;
			}
			cnt++;
		}
	}
	return sels;
}

function getElementsByName_iefix(tag, name) {
  var elem = document.getElementsByTagName(tag);
  var arr = new Array();
  for(i = 0,iarr = 0; i < elem.length; i++) {
       att = elem[i].getAttribute("name");
       if(att == name) {
            arr[iarr] = elem[i];
            iarr++;
       }
  }
  return arr;
}

YAHOO.util.Event.onDOMReady(initializeTranslations);
YAHOO.util.Event.onDOMReady(initPanel);
YAHOO.util.Event.addListener("region_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("sector_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("sector_config_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("applyButton", "click", callbackApplyFilter);
//YAHOO.util.Event.addListener("applyButtonPopin", "click", applyFilterPopin);



function itemClick(id, type, startYear, endYear){
	  if(id) {
		  openNewWindow(600, 400);
		  var urlItemClick= urlShowList + "&id=" + id + "&type=" + type + "&startYear=" + startYear + "&endYear=" + endYear;
		  document.visualizationform.action = urlItemClick;
		  document.visualizationform.target = popupPointer.name;
		  document.visualizationform.submit();
	  }

		//var transaction = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getActivitiesList&id=" + id + "&type=" + type + "&year=" + year, showListPopinCall, null);
}


function getQueryParameter ( parameterName ) {
	  var queryString = window.top.location.search.substring(1);
	  var parameterName = parameterName + "=";
	  if ( queryString.length > 0 ) {
	    begin = queryString.indexOf ( parameterName );
	    if ( begin != -1 ) {
	      begin += parameterName.length;
	      end = queryString.indexOf ( "&" , begin );
	        if ( end == -1 ) {
	        end = queryString.length
	      }
	      return unescape ( queryString.substring ( begin, end ) );
	    }
	  }
	  return "null";
	}

function checkOptionByNameAndValue (checkName, value) {
	var checks = document.getElementsByName(checkName);
	for ( var i = 0; i < checks.length; i++) {
		if (checks[i].value == value){
			checks[i].checked = true;
		}
	}
}
function manageSectorEntities(option,configId,sectorId){
	$("li[id^='config_']").each(function() {
		if(this.id!='config_'+configId){
			$(this).find("input[name='sub_sector_check']").removeAttr('checked');
			$(this).find("input[name='sector_check']").removeAttr('checked');
			$(this).find("#config_"+configId+"_radio").removeAttr('checked');;
		}
		else{
			$(this).find("#config_"+configId+"_radio").attr('checked','checked');
		}
	  });
	if(sectorId!=null){
		var options=$("input[class='sub_sector_check_"+sectorId+"']");
		if(option.checked){
			options.attr('checked','checked');
		}
		else{
			options.removeAttr('checked');
		}
	}
	
}

function toggleRelatedLike (option,name) {
	var options=$("input[class^='"+name+"']");
	if(option.checked){
		options.attr('checked','checked');
	}
	else{
		options.removeAttr('checked');
	}
	
}

