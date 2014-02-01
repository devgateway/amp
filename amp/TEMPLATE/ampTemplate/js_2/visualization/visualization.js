function initializePage(){
	YAHOO.util.Event.onAvailable("region_dropdown_id", callbackChildren);
	YAHOO.util.Event.onAvailable("org_group_dropdown_id", callbackChildren);
	YAHOO.util.Event.onAvailable("sector_dropdown_id", callbackChildren);
	YAHOO.util.Event.onAvailable("sector_config_dropdown_id", callbackChildren);
	myTabs = new YAHOO.widget.TabView("demo");
	myTabs.selectTab(0);
	initPopin();
	initializeLoadingPanel();
}
function initializeLoadingPanel(){
	yuiLoadingPanel = function(conf){
	    conf = conf == undefined ? new Array() : conf;
	    conf.id = conf.id == undefined ? 'yuiLoadingPanel':confi.id;
	    conf.header = conf.header == undefined ? trnLoading:conf.header;
	    conf.width = conf.width == undefined ? '440px':conf.width;
	    this.conf = conf;
	    this.cancelEvent = new YAHOO.util.CustomEvent("cancelEvent", this);
	    this.init();
		
	};
	yuiLoadingPanel.prototype = {
		    init:function(){
		        var loadingPanel = new YAHOO.widget.Panel(this.conf.id,{
		            width:this.conf.width,
			    fixedcenter:true,
		            close:false,
		            draggable:false,
		            modal:true,
		            visible:false
		        });
		    
		       loadingPanel.setBody(this.conf.header + '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
		       loadingPanel.render(document.body);
		       $D.addClass(loadingPanel.id, 'tcc_lightboxLoader');
		       var cancelLink = document.createElement('a');
		       $D.setStyle(cancelLink, 'cursor', 'pointer');
		       cancelLink.appendChild(document.createTextNode(trnCancel));
		       $E.on(cancelLink, 'click', function(e, o){
		           o.self.loadingPanel.hide();
		           o.self.cancelEvent.fire();
		           if(navigator.appName == "Microsoft Internet Explorer")
			           window.document.execCommand('Stop');
		           else
			           window.stop();
		       }, {self:this});
		       loadingPanel.appendToBody(document.createElement('br'));
		       loadingPanel.appendToBody(cancelLink);
		       $D.setStyle(loadingPanel.body, 'text-align', 'center');
//		               $D.addClass(document.body, 'yui-skin-sam');
		        this.loadingPanel = loadingPanel;
		    },
		    show:function(text){
		        if(text != undefined){
		            this.loadingPanel.setHeader(text);
		        }else{
			    this.loadingPanel.setHeader(this.conf.header);
			}
			this.loadingPanel.show();
		    },
		    hide:function(){
		        this.loadingPanel.hide();
		    }
		};
	loadingPanel = new yuiLoadingPanel();
}


function showFullList(objectType){
	var sUrl="/visualization/dataDispatcher.do?action=getFullList&objectType=" + objectType;
	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, updateFullList);
	var divFullName = "";
	var divTopName = "";
	switch(objectType){
		case "sectors":
			divFullName = "divFullSectors";
			divTopName = "divTopSectors";
			break;
		case "organizations":
			divFullName = "divFullOrganizations";
			divTopName = "divTopOrganizations";
			break;
		case "regions":
			divFullName = "divFullRegions";
			divTopName = "divTopRegions";
			break;
		case "NPOs":
			divFullName = "divFullNPOs";
			divTopName = "divTopNPOs";
			break;
		case "programs":
			divFullName = "divFullPrograms";
			divTopName = "divTopPrograms";
			break;
		case "secondaryPrograms":
			divFullName = "divFullSecondaryPrograms";
			divTopName = "divTopSecondaryPrograms";
			break;
		case "projects":
			divFullName = "divFullProjects";
			divTopName = "divTopProjects";
			break;
	}
	
	var divFull = document.getElementById(divFullName);
	divFull.innerHTML = trnLoading + "<br/> <img src=\"/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif\" />"; 
	var divTop = document.getElementById(divTopName);
	divFull.style.display = "";
	divTop.style.display = "none";
}

var updateFullList = {
		  success: function(o) {
			  	var dashboardType = document.getElementById("dashboardType").value;
				var results = YAHOO.lang.JSON.parse(o.responseText);
				var child = results.children[0];
				switch(child.type){
					case "ProjectsList":
						if (child.list.length==0){
							inner = "<b>"+trnNoDataToShow+"</b> <br />";
						} else {
							inner = "<a href='javascript:hideFullProjects()' style='float:right;'>"+trnShowTop+"</a> <br />";
							var isPublicView = document.getElementById("fromPublicView").value;
							for(var i = 0; i < child.list.length; i++){
								inner = inner + (i+1) + ". " + "";
								if (isPublicView == "false"){
									inner = inner + "<a target='_blank' href='/aim/viewActivityPreview.do~pageId=2~activityId=" + child.list[i].id + "~isPreview=1'>"  + child.list[i].name + "</a>" + "  <b>(" + child.list[i].value + ")</b> <hr />";
								} else {
									inner = inner + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
								}
							}
							inner = inner + "<a href='javascript:hideFullProjects()' style='float:right;'>"+trnShowTop+"</a>";
						}
						var div = document.getElementById("divFullProjects");
						if (div!=null)
							div.innerHTML = inner;
						break;
					case "OrganizationsList":
						//if (dashboardType!=1) {
							if (child.list.length==0){
								inner = "<b>"+trnNoDataToShow+"</b> <br />";
							} else {
								inner = "<a href='javascript:hideFullOrganizations()' style='float:right;'>"+trnShowTop+"</a> <br />";
								for(var i = 0; i < child.list.length; i++){
									inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
								}
								inner = inner + "<a href='javascript:hideFullOrganizations()' style='float:right;'>"+trnShowTop+"</a>";
							}
							var div = document.getElementById("divFullOrganizations");
							if (div!=null)
								div.innerHTML = inner;
						//}
						break;
					case "NPOsList":
						//if (dashboardType!=1) {
							if (child.list.length==0){
								inner = "<b>"+trnNoDataToShow+"</b> <br />";
							} else {
								inner = "<a href='javascript:hideFullNPOs()' style='float:right;'>"+trnShowTop+"</a> <br />";
								for(var i = 0; i < child.list.length; i++){
									inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
								}
								inner = inner + "<a href='javascript:hideFullNPOs()' style='float:right;'>"+trnShowTop+"</a>";
							}
							var div = document.getElementById("divFullNPOs");
							if (div!=null)
								div.innerHTML = inner;
						//}
						break;
					case "ProgramsList":
						//if (dashboardType!=1) {
							if (child.list.length==0){
								inner = "<b>"+trnNoDataToShow+"</b> <br />";
							} else {
								inner = "<a href='javascript:hideFullPrograms()' style='float:right;'>"+trnShowTop+"</a> <br />";
								for(var i = 0; i < child.list.length; i++){
									inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
								}
								inner = inner + "<a href='javascript:hideFullPrograms()' style='float:right;'>"+trnShowTop+"</a>";
							}
							var div = document.getElementById("divFullPrograms");
							if (div!=null)
								div.innerHTML = inner;
						//}
						break;
					case "SecondaryProgramsList":
						//if (dashboardType!=1) {
							if (child.list.length==0){
								inner = "<b>"+trnNoDataToShow+"</b> <br />";
							} else {
								inner = "<a href='javascript:hideFullSecondaryPrograms()' style='float:right;'>"+trnShowTop+"</a> <br />";
								for(var i = 0; i < child.list.length; i++){
									inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
								}
								inner = inner + "<a href='javascript:hideFullSecondaryPrograms()' style='float:right;'>"+trnShowTop+"</a>";
							}
							var div = document.getElementById("divFullSecondaryPrograms");
							if (div!=null)
								div.innerHTML = inner;
						//}
						break;
					case "SectorsList":
						//if (dashboardType!=3) {
							if (child.list.length==0){
								inner = "<b>"+trnNoDataToShow+"</b> <br />";
							} else {
								inner = "<a href='javascript:hideFullSectors()' style='float:right;'>"+trnShowTop+"</a> <br />";
								for(var i = 0; i < child.list.length; i++){
									inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
								}
								inner = inner + "<a href='javascript:hideFullSectors()' style='float:right;'>"+trnShowTop+"</a>";
							}
							var div = document.getElementById("divFullSectors");
							if (div!=null)
								div.innerHTML = inner;
						//}
						break;
					case "RegionsList":
						//if (dashboardType!=2) {
							if (child.list.length==0){
								inner = "<b>"+trnNoDataToShow+"</b> <br />";
							} else {
								inner = "<a href='javascript:hideFullRegions()' style='float:right;'>"+trnShowTop+"</a> <br />";
								for(var i = 0; i < child.list.length; i++){
									inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
								}
								inner = inner + "<a href='javascript:hideFullRegions()' style='float:right;'>"+trnShowTop+"</a>";
							}
							var div = document.getElementById("divFullRegions");
							if (div!=null)
								div.innerHTML = inner;
							inner = "";
						//}
						break;
					default:
						break;
				}
		  },
		  failure: function(o) {
			  //alert("problema");
		  }
		};

function hideFullProjects(){
	var divFull = document.getElementById("divFullProjects");
	var divTop = document.getElementById("divTopProjects");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullSectors(){
	var divFull = document.getElementById("divFullSectors");
	var divTop = document.getElementById("divTopSectors");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullRegions(){
	var divFull = document.getElementById("divFullRegions");
	var divTop = document.getElementById("divTopRegions");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullNPOs(){
	var divFull = document.getElementById("divFullNPOs");
	var divTop = document.getElementById("divTopNPOs");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullPrograms(){
	var divFull = document.getElementById("divFullPrograms");
	var divTop = document.getElementById("divTopPrograms");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullSecondaryPrograms(){
	var divFull = document.getElementById("divFullSecondaryPrograms");
	var divTop = document.getElementById("divTopSecondaryPrograms");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullOrganizations(){
	var divFull = document.getElementById("divFullOrganizations");
	var divTop = document.getElementById("divTopOrganizations");
	divFull.style.display = "none";
	divTop.style.display = "";
}

$D = YAHOO.util.Dom;
$E = YAHOO.util.Event;
var yuiLoadingPanel;

function checkUncheckRelatedEntities(option,name,id){
	uncheckAllRelatedEntities(name);
	checkRelatedEntities(option,name,id);
}
function allOptionChecked(option,name,subname){
	if(option.checked)
	{
		// set all
		$("input[name='"+name+"']").attr('checked', 'checked');
		$("input[name='"+subname+"']").attr('checked', 'checked');
//		option.checked=true;
	}
	else
	{
		// remove all
		$("input[name='"+name+"']").removeAttr('checked');
		$("input[name='"+subname+"']").removeAttr('checked');		
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


function toggleHeader(button, containerId){
	var container = document.getElementById(containerId);
	var imgShow = "<img src=\"/TEMPLATE/ampTemplate/img_2/ico_perm_open.gif\" vspace=\"5\" align=\"absMiddle\"/>"; 
	var imgHide = "<img src=\"/TEMPLATE/ampTemplate/img_2/ico_perm_close.gif\" vspace=\"5\" align=\"absMiddle\"/>"; 
	if(container.style.display == "none"){
		container.style.display = "block";
		button.innerHTML = imgHide + " " + trnHideSettings;
	}
	else if(container.style.display == "block"){
		container.style.display = "none";	
		button.innerHTML = imgShow + " " + trnShowSettings;
	}
	
}


YAHOO.namespace("YAHOO.amp");
popinPanels = new Array();

var myPanel = new YAHOO.widget.Panel("newPanel", {
	width:"750px",
	maxHeight:"500px",
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
		width:"750px",
		maxHeight:"500px",
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

function showExport() {
	if ( popinPanels['Panel2'] == null ) {
		popinPanels['Panel2'] = new YAHOO.widget.Panel('Panel2', {
		width:"750px",
		maxHeight:"500px",
		fixedcenter: true,
	    constraintoviewport: false,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    });
		popinPanels['Panel2'].render(document.body);
		var msg=trnExportOptions;
		popinPanels['Panel2'].setHeader(msg);
		var element = document.getElementById("exportPopin");
		element.style.display 	= "inline";
		popinPanels['Panel2'].setBody(element);
	}
	popinPanels['Panel2'].show();
}
function hideExport() {
	popinPanels['Panel2'].hide();
}

function showInstallFlashPopin() {
	if ( popinPanels['Panel3'] == null ) {
		popinPanels['Panel3'] = new YAHOO.widget.Panel('Panel3', {
		width:"750px",
		maxHeight:"500px",
		fixedcenter: true,
	    constraintoviewport: false,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    });
		popinPanels['Panel3'].render(document.body);
		var msg=trnInstallFlash;
		popinPanels['Panel3'].setHeader(msg);
		var element = document.getElementById("installFlashPopin");
		element.style.display 	= "inline";
		popinPanels['Panel3'].setBody(element);
	}
	popinPanels['Panel3'].show();
}

function doExport(){
	var options = "?";
	options += "typeOpt=" + getOptionChecked("export_type_");
	options += "&summaryOpt=" + getOptionChecked("export_summary_");
	options += "&ODAGrowthOpt=" + getOptionChecked("export_ODAGrowth_");
	options += "&fundingOpt=" + getOptionChecked("export_Fundings_");
	options += "&aidPredicOpt=" + getOptionChecked("export_AidPredictability_");
	options += "&aidPredicQuarterOpt=" + getOptionChecked("export_AidPredictabilityQuarter_");
	options += "&budgetBreakdownOpt=" + getOptionChecked("export_BudgetBreakdown_");
	options += "&aidTypeOpt=" + getOptionChecked("export_AidType_");
	options += "&financingInstOpt=" + getOptionChecked("export_AidModality_");
	options += "&organizationOpt=" + getOptionChecked("export_OrganizationProfile_");
	options += "&beneficiaryAgencyOpt=" + getOptionChecked("export_BeneficiaryAgencyProfile_");
	options += "&sectorOpt=" + getOptionChecked("export_SectorProfile_");
	options += "&regionOpt=" + getOptionChecked("export_RegionProfile_");
	options += "&NPOOpt=" + getOptionChecked("export_NPOProfile_");
	options += "&programOpt=" + getOptionChecked("export_ProgramProfile_");
	options += "&secondaryProgramOpt=" + getOptionChecked("export_SecondaryProgramProfile_");
	
	var type = "" + getOptionChecked("export_type_");
	if (type=="0") {
		document.visualizationform.action= urlPdfExport + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	} 
	if (type=="1") {
		document.visualizationform.action= urlWordExport + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	}
	if (type=="2") {
		document.visualizationform.action= urlExcelExport + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	}
	hideExport();
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
	
	var dashboardType = document.getElementById("dashboardType").value;
	
	unCheckOptions("org_grp_check");
	unCheckOptions("region_check");
	unCheckOptions("sector_config_check");
	unCheckOptions("sector_check");
	unCheckOptions("organization_check");
	unCheckOptions("zone_check");
	unCheckOptions("sub_sector_check");
	unCheckOptions("status_check");
	if (dashboardType==4){
		unCheckOptions("beneficiary_agency_check");
		unCheckOptions("implementing_agency_check");
		unCheckOptions("secondary_program_check");
	}
	if (dashboardType==1) {
		document.getElementById("agencyType").value = document.getElementById("agencyTypeDefault").value;
		document.getElementById("agencyType_dropdown").value = document.getElementById("agencyTypeDefault").value;
		document.getElementById("agencyTypeQuickFilter_dropdown").value = document.getElementById("agencyTypeDefault").value;
	}
	
	document.getElementById("currencyId").value = document.getElementById("currencyIdDefault").value;
	document.getElementById("currencies_dropdown_ids").value = document.getElementById("currencyIdDefault").value;
	document.getElementById("currencyQuickFilter_dropdown").value = document.getElementById("currencyIdDefault").value;
	
	document.getElementById("showAmountsInThousands").value = document.getElementById("showAmountsInThousandsDefault").value;
	document.getElementById("show_amounts_in_thousands").value = document.getElementById("showAmountsInThousandsDefault").value;
	
	document.getElementById("decimalsToShow_dropdown").selectedIndex = 2;
	document.getElementById("topLists_dropdown").selectedIndex = 0;
	
	document.getElementById("commitments_visible").checked = true;
	document.getElementById("disbursements_visible").checked = true;
	if (document.getElementById("expenditures_visible")!=null){
		document.getElementById("expenditures_visible").checked = true;
	}
	if (document.getElementById("pledge_visible")!=null){
		document.getElementById("pledge_visible").checked = true;
	}
	if (document.getElementById("workspace_only")!=null){
		document.getElementById("workspace_only").checked = false;
	}
	
	if (dashboardType==4){ // if it is a Deal Dashboard
		document.getElementById("implementing_agency_dropdown_id").selectedIndex = 0;
		document.getElementById("beneficiary_agency_dropdown_id").selectedIndex = 0;
		document.getElementById("secondary_program_dropdown_id").selectedIndex = 0;
	}
	document.getElementById("transaction_type").selectedIndex = 1;
	document.getElementById("adjustment_type").selectedIndex = 1;
	if (dashboardType!=4){
		document.getElementById("org_group_dropdown_id").selectedIndex = 0;
		removeOptionsDropdown("org_dropdown_id");
	}
	document.getElementById("region_dropdown_id").selectedIndex = 0;
	document.getElementById("sector_dropdown_id").selectedIndex = 0;
	document.getElementById("sector_config_dropdown_id").selectedIndex = 0;
	callbackChildren.call(document.getElementById("sector_config_dropdown_id"), null);
	removeOptionsDropdown("zone_dropdown_id");
	removeOptionsDropdown("sector_dropdown_id");
	removeOptionsDropdown("sub_sector_dropdown_id");
	document.getElementById("filterOrganizations").innerHTML = trnAll;
	document.getElementById("filterOrgGroups").innerHTML = trnAll;
	document.getElementById("filterSectors").innerHTML = trnAll;
	document.getElementById("filterSubSectors").innerHTML = trnAll;
	document.getElementById("filterZones").innerHTML = trnAll;
	document.getElementById("filterSectorConfiguration").innerHTML = trnPrimary;
	document.getElementById("filterRegions").innerHTML = trnAll;
	//setSelectedValue("show_amounts_in_thousands", 2); // Show amounts in millions
	document.getElementById("startYearQuickFilter_dropdown").value = document.getElementById("defaultStartYear").value;
	document.getElementById("endYearQuickFilter_dropdown").value = document.getElementById("defaultEndYear").value;
	document.getElementById("startYear_dropdown").value = document.getElementById("defaultStartYear").value;
	document.getElementById("endYear_dropdown").value = document.getElementById("defaultEndYear").value;
	//document.getElementById("SectorProfileItemId").value = -1;
	applyFilterPopin();
}

function removeOptionsDropdown(object){
	obj = document.getElementById(object);
	for(var i = 1; i < obj.options.length; i++){
		obj.options[i].remove;
	}
	//save the 'All' option text (in different languages it differs)
	var allText = obj.options[0].text;
	obj.options.length = 0;
	obj.options[0] = new Option(allText, -1);
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
	for(var i=0;i<7;i++){
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
	$("#beneficiaryAgencyDivContent").css("display","none");
	$("#implementingAgencyDivContent").css("display","none");
	$("#secondaryProgramDivContent").css("display","none");
	
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
		$("#beneficiaryAgencyDivContent").css("display","block");
		break;
	case 5:
		$("#implementingAgencyDivContent").css("display","block");
		break;
	case 6:
		$("#secondaryProgramDivContent").css("display","block");
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
			    		orgDropdown.value = currentOrg;
			    		break;
				    case "OrganizationGroup":
			    		var orgGrpDropdown = document.getElementById("org_group_dropdown_id");
			    		orgGrpDropdown.options.length = 0;
			    		orgGrpDropdown.options[0] = new Option(trnAll, -1);
			    		var currentOrgGrpInOptions = false;
			    		for(var i = 0; i < results.children.length; i++){
			    			orgGrpDropdown.options[orgGrpDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    			if(currentOrgGroup == results.children[i].ID)
			    				currentOrgGrpInOptions = true;
			    		}
			    		if(currentOrgGroup == "-1" || currentOrgGrpInOptions)
			    			orgGrpDropdown.value = currentOrgGroup;
			    		else
			    		{
			    			orgGrpDropdown.value = "-1";
			    			callbackApplyFilter();
			    		}	
			    		break;
				    case "Sector":
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
			    		if (subSectorDropdown!=null){
				    		subSectorDropdown.options.length = 0;
				    		subSectorDropdown.options[0] = new Option(trnAll, -1);
				    		for(var i = 0; i < results.children.length; i++){
				    			subSectorDropdown.options[subSectorDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
				    		}
			    		}
			    		break;
			    	case "Region":
			    		var zonesDropdown = document.getElementById("zone_dropdown_id");
			    		if (zonesDropdown!=null){
					    		zonesDropdown.options.length = 0;
				    		zonesDropdown.options[0] = new Option(trnAll, -1);
				    		for(var i = 0; i < results.children.length; i++){
				    			zonesDropdown.options[zonesDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
				    		}
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
			    		var flashSliderLabels = "";
			    		var endYearDropdown = document.getElementById("endYear_dropdown");
			    		if (startYearQuickFilterDropdown!=null)
			    			startYearQuickFilterDropdown.options.length = 0;
			    		if (endYearQuickFilterDropdown!=null)
			    			endYearQuickFilterDropdown.options.length = 0;
			    		startYearDropdown.options.length = 0;
					    endYearDropdown.options.length = 0;
					    for(var i = 0; i < results.children.length; i++){
					    	if (startYearQuickFilterDropdown!=null)
				    			startYearQuickFilterDropdown.options[startYearQuickFilterDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
					    	if (endYearQuickFilterDropdown!=null)
				    			endYearQuickFilterDropdown.options[endYearQuickFilterDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
			    			startYearDropdown.options[startYearDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
			    			endYearDropdown.options[endYearDropdown.options.length] = new Option(results.children[i].key, results.children[i].value);
			    			flashSliderLabels = flashSliderLabels+results.children[i].key+",";
			    		}
					    //document.getElementById("flashSliderLabels").value = flashSliderLabels;
					    if (startYearQuickFilterDropdown!=null)
			    			startYearQuickFilterDropdown.selectedIndex = startYearSelectedIndex;
					    if (endYearQuickFilterDropdown!=null)
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
				//document.getElementById("SectorProfileItemId").value = parentId;
			}
			catch(e){
					
			}
			break;
		case "region_dropdown_id":
			objectType = "Region";
			//try to set the SectorProfileItemId from select:
			try {
				//document.getElementById("RegionProfileItemId").value = parentId;
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

function closeLoadingPanel()
{
    loadingPanel.hide();
    loadingPanel.cancelEvent.fire();
    panelLoaded = true;
    if(navigator.appName == "Microsoft Internet Explorer")
        window.document.execCommand('Stop');
    else
        window.stop();
}

var callbackApplyFilterCall = {
		  success: function(o) {
			  //loadingPanel.hide();
			 // alert('panel loaded!');
			  closeLoadingPanel();
			  closeLoadingPanel();
			  refreshBoxes(o);
			  refreshGraphs();
			  if (document.getElementById("dashboardType").value!=4)
				  refreshDropdowns();
		  },
		  failure: function(o) {
			  loadingPanel.hide();
		  }
		};

var currentOrgGroup = "-1";
var currentOrg = "-1";

var refreshDropdowns = function(){
	// Reload the Organization Groups
	// Reassign the selected organization group
	// If there's an organization group selected, then load the children and select the appropriate ones.
	// Find out: Where is the list of Organization Groups stored: DashboardFilter.orgGroups()
	// Where is the list of Organizations stored: not stored anywhere, it's loaded dynamically in DataDispatcher.getJSONObject()
	// Where is the currently selected organization group: visualizationForm.getFilter().setSelOrgGroupIds
	// document.forms[1].org_group_dropdown_id.value
	// where is the currently selected organization selected: visualizationForm.getFilter().setSelOrgIds
	// document.forms[1].org_dropdown_id.value
	currentOrgGroup = document.getElementById("org_group_dropdown_id").value;
	currentOrg = document.getElementById("org_dropdown_id").value;
	var objectType = "OrganizationGroup";

	var transactionOrgGroup = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getJSONObject&objectType=" + objectType, callbackChildrenCall, null);
	
	if (currentOrgGroup != null && currentOrgGroup != "-1"){
		objectType = "Organization";
		var transactionOrg = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getJSONObject&objectType=" + objectType + "&parentId=" + currentOrgGroup, callbackChildrenCall, null);
	}
};

function hasFlash(){
	var hasFlash = false;
	try {
	  var fo = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
	  if(fo) hasFlash = true;
	}catch(e){
	  if(navigator.mimeTypes ["application/x-shockwave-flash"] != undefined) hasFlash = true;
	}
	return hasFlash;
}

function callbackApplyFilter(e){
	if (!hasFlash()){
		showInstallFlashPopin();
		return;
	} 
	var dashboardType = document.getElementById("dashboardType").value;
	panelLoaded = false;
	if (document.getElementById("workspaceOnlyQuickFilter")!=null){
		document.getElementById("workspaceOnly").value = document.getElementById("workspaceOnlyQuickFilter").checked;
		document.getElementById("workspace_only").checked = document.getElementById("workspaceOnlyQuickFilter").checked;
	}
	document.getElementById("currencyId").value = document.getElementById("currencyQuickFilter_dropdown").value;
	//if (dashboardType!=4) {
		document.getElementById("adjustmentType").value = document.getElementById("adjustment_type_quick").value;
		document.getElementById("adjustment_type").value = document.getElementById("adjustment_type_quick").value;//document.getElementById("adjustment_type_quick").options[document.getElementById("adjustment_type").selectedIndex].value;
		document.getElementById("transactionType").value = document.getElementById("transactionType_dropdown").value;
		document.getElementById("transaction_type").value = document.getElementById("transactionType_dropdown").value;
		
	//}
	document.getElementById("currencies_dropdown_ids").value = document.getElementById("currencyQuickFilter_dropdown").value;
	document.getElementById("startYear").value = document.getElementById("startYearQuickFilter_dropdown").value;
	document.getElementById("endYear").value = document.getElementById("endYearQuickFilter_dropdown").value;
	document.getElementById("startYear_dropdown").value = document.getElementById("startYearQuickFilter_dropdown").value;
	document.getElementById("endYear_dropdown").value = document.getElementById("endYearQuickFilter_dropdown").value;
	document.getElementById("showAmountsInThousands").value = getSelectedValue("show_amounts_in_thousands");
	if (dashboardType==1) {
		document.getElementById("agencyType").value = document.getElementById("agencyTypeQuickFilter_dropdown").value;
		document.getElementById("agencyType_dropdown").value = document.getElementById("agencyTypeQuickFilter_dropdown").value;
	}
	
	if(document.getElementById("endYear").value < document.getElementById("startYear").value){
		alert(alertBadDate);	
		return;
	}
	var startYearSelectedIndex = document.getElementById("startYear_dropdown").selectedIndex;
	var endYearSelectedIndex = document.getElementById("endYear_dropdown").selectedIndex;
	var flashSliderLabels = "";
	for ( var i=startYearSelectedIndex; i<=endYearSelectedIndex; i++) {
		var label = document.getElementById("startYear_dropdown").options[i].text;
		flashSliderLabels = flashSliderLabels+label+",";
	}
	document.getElementById("flashSliderLabels").value = flashSliderLabels;
	
	if(document.getElementById("org_group_dropdown_id")!=null){
		if (document.getElementById("org_group_dropdown_id").value!=-1){
			unCheckOptions("org_grp_check");
			document.getElementById("org_grp_check_"+document.getElementById("org_group_dropdown_id").value).checked = true;
		} else {
			unCheckOptions("org_grp_check");
		}
	}
	
	if(document.getElementById("org_dropdown_id")!=null){
		if (document.getElementById("org_dropdown_id").value!=-1){
			unCheckOptions("organization_check");
			document.getElementById("organization_check_"+document.getElementById("org_dropdown_id").value).checked = true;
		} else {
			unCheckOptions("organization_check");
		}
	}
	
	if(document.getElementById("sector_dropdown_id")!=null){
		if (document.getElementById("sector_dropdown_id").value!=-1){
			unCheckOptions("sector_check");
			document.getElementById("sector_check_"+document.getElementById("sector_dropdown_id").value).checked = true;
		} else {
			unCheckOptions("sector_check");
		}
	}
	
	if(document.getElementById("sub_sector_dropdown_id")!=null){
		if (document.getElementById("sub_sector_dropdown_id").value!=-1){
			unCheckOptions("sub_sector_check");
			document.getElementById("sub_sector_check_"+document.getElementById("sub_sector_dropdown_id").value).checked = true;
		} else {
			unCheckOptions("sub_sector_check");
		}
	}
	
	if(document.getElementById("region_dropdown_id")!=null){
		if (document.getElementById("region_dropdown_id").value!=-1){
		 	unCheckOptions("region_check");
			document.getElementById("region_check_"+document.getElementById("region_dropdown_id").value).checked = true;
		} else {
			unCheckOptions("region_check");
		}
	}
	
	if(document.getElementById("zone_dropdown_id")!=null){
		if (document.getElementById("zone_dropdown_id").value!=-1){
			unCheckOptions("zone_check");
			document.getElementById("zone_check_"+document.getElementById("zone_dropdown_id").value).checked = true;
		} else {
			unCheckOptions("zone_check");
		}
	}
	
	if (dashboardType==4) {
		if(document.getElementById("beneficiary_agency_dropdown_id")!=null){
			if (document.getElementById("beneficiary_agency_dropdown_id").value!=-1){
			 	unCheckOptions("beneficiary_agency_check");
				document.getElementById("beneficiary_agency_check_"+document.getElementById("beneficiary_agency_dropdown_id").value).checked = true;
			} else {
				unCheckOptions("beneficiary_agency_check");
			}
		}
		
		if(document.getElementById("implementing_agency_dropdown_id")!=null){
			if (document.getElementById("implementing_agency_dropdown_id").value!=-1){
				unCheckOptions("implementing_agency_check");
				document.getElementById("implementing_agency_check_"+document.getElementById("implementing_agency_dropdown_id").value).checked = true;
			} else {
				unCheckOptions("implementing_agency_check");
			}
		}
		
		if(document.getElementById("secondary_program_dropdown_id")!=null){
			if (document.getElementById("secondary_program_dropdown_id").value!=-1){
				unCheckOptions("secondary_program_check");
				document.getElementById("secondary_program_check_"+document.getElementById("secondary_program_dropdown_id").value).checked = true;
			} else {
				unCheckOptions("secondary_program_check");
			}
		}
	}
	
	var params = "";
	params = params + "&orgGroupIds=" + getQueryParameter("orgGroupIds");
	params = params + "&orgIds=" + getQueryParameter("orgIds");
	params = params + "&regionIds=" + getQueryParameter("regionIds");
	params = params + "&zoneIds=" + getQueryParameter("zoneIds");
	params = params + "&selSectorConfigId=" + getQueryParameter("selSectorConfigId");
	params = params + "&sectorIds=" + getQueryParameter("sectorIds");
	params = params + "&subSectorIds=" + getQueryParameter("subSectorIds");
	params = params + "&statusIds=" + getQueryParameter("statusIds");
	params = params + "&beneficiaryAgencyIds=" + getQueryParameter("beneficiaryAgencyIds");
	params = params + "&implementingAgencyIds=" + getQueryParameter("implementingAgencyIds");
	params = params + "&secondaryProgramIds=" + getQueryParameter("secondaryProgramIds");

	loadingPanel.show();

	YAHOO.util.Connect.setForm('visualizationform');

	// loading stuff
	var sUrl="/visualization/dataDispatcher.do?action=applyFilter" + params;

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);

	loadingPanel.loadingPanel.setBody("");
	refreshLoadingPanel();
	if(document.body.innerText){
		if (document.getElementById("dashboardType").value!=4){
			document.getElementById("filterOrgGroups").innerText = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
			document.getElementById("filterOrganizations").innerText = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
		}
		document.getElementById("filterSectorConfiguration").innerText = document.getElementById("sector_config_dropdown_id").options[document.getElementById("sector_config_dropdown_id").selectedIndex].text;
		document.getElementById("filterSectors").innerText = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
		document.getElementById("filterSubSectors").innerText = document.getElementById("sub_sector_dropdown_id").options[document.getElementById("sub_sector_dropdown_id").selectedIndex].text;
		document.getElementById("filterRegions").innerText = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
		document.getElementById("filterZones").innerText = document.getElementById("zone_dropdown_id").options[document.getElementById("zone_dropdown_id").selectedIndex].text;
		document.getElementById("filterStartYear").innerText = document.getElementById("startYearQuickFilter_dropdown").options[document.getElementById("startYearQuickFilter_dropdown").selectedIndex].text;
		document.getElementById("filterEndYear").innerText = document.getElementById("endYearQuickFilter_dropdown").options[document.getElementById("endYearQuickFilter_dropdown").selectedIndex].text;
	}
	else
	{
		if (document.getElementById("dashboardType").value!=4){
			document.getElementById("filterOrgGroups").textContent = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
			document.getElementById("filterOrganizations").textContent = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
		}
		document.getElementById("filterSectorConfiguration").textContent = document.getElementById("sector_config_dropdown_id").options[document.getElementById("sector_config_dropdown_id").selectedIndex].text;
		document.getElementById("filterSectors").textContent = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
		document.getElementById("filterSubSectors").textContent = document.getElementById("sub_sector_dropdown_id").options[document.getElementById("sub_sector_dropdown_id").selectedIndex].text;
		document.getElementById("filterRegions").textContent = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
		document.getElementById("filterZones").textContent = document.getElementById("zone_dropdown_id").options[document.getElementById("zone_dropdown_id").selectedIndex].text;
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

function closeLoadingPanel()
{
    loadingPanel.hide();
    loadingPanel.cancelEvent.fire();
    panelLoaded = true;
    if(navigator.appName == "Microsoft Internet Explorer")
        window.document.execCommand('Stop');
    else
        window.stop();	
}

var callbackUpdateLoadingPanel = {
		  success: function(o) {
			   loadingPanel.loadingPanel.setBody(o.responseText + '<br/> <img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
			   loadingPanel.loadingPanel.render(document.body);
		       var cancelLink = document.createElement('a');
		       $D.setStyle(cancelLink, 'cursor', 'pointer');
		       cancelLink.appendChild(document.createTextNode(trnCancel));
		       $E.on(cancelLink, 'click', function(e, o){
		    	   closeLoadingPanel();
		           }, {self:this});
		       loadingPanel.loadingPanel.appendToBody(document.createElement('br'));
		       loadingPanel.loadingPanel.appendToBody(cancelLink);
		       $D.setStyle(loadingPanel.body, 'text-align', 'center');

//			  loadingPanel.loadingPanel.setBody( + '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
			  if(!panelLoaded)
			  	setTimeout(refreshLoadingPanel, 1000);
		  },
		  failure: function(o) {
		//  alert("error 45");
		  }
		};

function applyFilterPopin(e){
//var allGraphs = document.getElementsByName("flashContent");
	document.getElementById("topLists").value = document.getElementById("topLists_dropdown").options[document.getElementById("topLists_dropdown").selectedIndex].value;
	document.getElementById("decimalsToShow").value = document.getElementById("decimalsToShow_dropdown").options[document.getElementById("decimalsToShow_dropdown").selectedIndex].value;
	document.getElementById("startYear").value = document.getElementById("startYear_dropdown").options[document.getElementById("startYear_dropdown").selectedIndex].value;
	document.getElementById("endYear").value = document.getElementById("endYear_dropdown").options[document.getElementById("endYear_dropdown").selectedIndex].value;
	//Copy the values of the start/end year from the Advanced to the quick
	document.getElementById("startYearQuickFilter_dropdown").value = document.getElementById("startYear_dropdown").options[document.getElementById("startYear_dropdown").selectedIndex].value;
	document.getElementById("endYearQuickFilter_dropdown").value = document.getElementById("endYear_dropdown").options[document.getElementById("endYear_dropdown").selectedIndex].value;
	
	var dashboardType = document.getElementById("dashboardType").value;
	if (dashboardType==1) {
		document.getElementById("agencyType").value = document.getElementById("agencyType_dropdown").options[document.getElementById("agencyType_dropdown").selectedIndex].value;
		document.getElementById("agencyTypeQuickFilter_dropdown").value = document.getElementById("agencyType_dropdown").options[document.getElementById("agencyType_dropdown").selectedIndex].value;
	}
	
	//document.getElementById("yearToCompare").value = document.getElementById("yearToCompare_dropdown").options[document.getElementById("yearToCompare_dropdown").selectedIndex].value;
	document.getElementById("currencyId").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("currencyQuickFilter_dropdown").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("adjustmentType").value = document.getElementById("adjustment_type").options[document.getElementById("adjustment_type").selectedIndex].value;
	document.getElementById("adjustment_type_quick").value = document.getElementById("adjustment_type").options[document.getElementById("adjustment_type").selectedIndex].value;
	document.getElementById("fiscalCalendarId").value = document.getElementById("fiscalCalendar_dropdown_Id").options[document.getElementById("fiscalCalendar_dropdown_Id").selectedIndex].value;
	document.getElementById("commitmentsVisible").value = document.getElementById("commitments_visible").checked;
	document.getElementById("disbursementsVisible").value = document.getElementById("disbursements_visible").checked;
	if (document.getElementById("expenditures_visible")!=null){
		document.getElementById("expendituresVisible").value = document.getElementById("expenditures_visible").checked;
	}
	if (document.getElementById("pledge_visible")!=null){
		document.getElementById("pledgeVisible").value = document.getElementById("pledge_visible").checked;
	}
	if (document.getElementById("workspace_only")!=null){
		document.getElementById("workspaceOnly").value = document.getElementById("workspace_only").checked;
		document.getElementById("workspaceOnlyQuickFilter").checked = document.getElementById("workspace_only").checked;
	}
	document.getElementById("showAmountsInThousands").value = getSelectedValue("show_amounts_in_thousands");
	document.getElementById("showMonochrome").value = document.getElementById("show_monochrome").checked;
	
	document.getElementById("transactionType").value = document.getElementById("transaction_type").options[document.getElementById("transaction_type").selectedIndex].value;
	document.getElementById("transactionType_dropdown").value = document.getElementById("transactionType").value;
	document.getElementById("adjustment_type_quick").value = document.getElementById("adjustmentType").value;
	
	var params = "";
	params = params + "&orgGroupIds=" + getSelectionsFromElement("org_grp_check",false);
	params = params + "&orgIds=" + getSelectionsFromElement("organization_check",false);
	params = params + "&regionIds=" + getSelectionsFromElement("region_check",false);
	params = params + "&zoneIds=" + getSelectionsFromElement("zone_check",false);
	params = params + "&selSectorConfigId=" + getSelectionsFromElement("sector_config_check",false);
	params = params + "&sectorIds=" + getSelectionsFromElement("sector_check",false);
	params = params + "&subSectorIds=" + getSelectionsFromElement("sub_sector_check",false);
	params = params + "&statusIds=" + getSelectionsFromElement("status_check",false);
	if (dashboardType==4) {
		params = params + "&beneficiaryAgencyIds=" + getSelectionsFromElement("beneficiary_agency_check",false);
		params = params + "&implementingAgencyIds=" + getSelectionsFromElement("implementing_agency_check",false);
		params = params + "&secondaryProgramIds=" + getSelectionsFromElement("secondary_program_check",false);
	}

	if(document.getElementById("endYear").value < document.getElementById("startYear").value){
		alert(alertBadDate);	
		return;
	}
	var startYearSelectedIndex = document.getElementById("startYear_dropdown").selectedIndex;
	var endYearSelectedIndex = document.getElementById("endYear_dropdown").selectedIndex;
	var flashSliderLabels = "";
	for ( var i=startYearSelectedIndex; i<=endYearSelectedIndex; i++) {
		var label = document.getElementById("startYear_dropdown").options[i].text;
		flashSliderLabels = flashSliderLabels+label+",";
	}
	document.getElementById("flashSliderLabels").value = flashSliderLabels;
	
	loadingPanel.show();
	YAHOO.util.Connect.setForm('visualizationform');

	var sUrl="/visualization/dataDispatcher.do?action=applyFilter" + params;

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
var nonRefreshedMovies = [];

function refreshGraphs(){
	//Get array of graphs
	var allGraphs = getElementsByName_iefix("div", "flashContent");
	//Push it into an array that will be emptied as they become available
	for(var idx = 0; idx < allGraphs.length; idx++){
		nonRefreshedMovies.push(allGraphs[idx].children[0]);
	}
	//hardcode for hidden ODA growth chart when beneficiary agency is selected
	var agencyType = document.getElementById("agencyType") ? document.getElementById("agencyType").value : null;
	var odaElement = document.getElementById("ODAGrowthTitleLegend");

	if (odaElement!=null){
		if (agencyType==2) {
			odaElement.parentElement.parentElement.style.display='none';
		} else {
			odaElement.parentElement.parentElement.style.display='block';
		}
	}
	refreshAsync();
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
function refreshAsync(){
//	console.log("Refreshing graphs. Number of graphs to refresh: " + nonRefreshedMovies.length);
	if(nonRefreshedMovies.length > 0){
		//Get one flash movie, try to refresh it, if it doesn't work, push it back again
		var currentMovie = nonRefreshedMovies.shift();
//		console.log("popping one: " + currentMovie.id);
		try
		{
			if (YAHOO.env.ua.gecko <= 1.92 && YAHOO.env.ua.gecko != 0) {
				currentMovie.scrollIntoView(true);
			}
			currentMovie.refreshGraph();
			
//			console.log("success: " + currentMovie.id);
		}
		catch(e)
		{
//			console.log("back inside: " + currentMovie.id);
			nonRefreshedMovies.push(currentMovie);
		}
		setTimeout(refreshAsync, 500);
	}
	else
	{
		scroll(0,0);
		loadingPanel.hide();	
	}	
}
function refreshBoxes(o){
	var isThousands = false;
	if (document.getElementById("showAmountsInThousands").value=="1")
		isThousands = true;
	else
		isThousands = false;
	var dashboardType = document.getElementById("dashboardType").value;
	var results = YAHOO.lang.JSON.parse(o.responseText);
	var inner = "";
	var inner2 = "";
	var valTotalCommits="";
	var valTotalDisbs="";
	var valNumOfProjs="";
	var valNumOfSecs="";
	var valNumOfRegs="";
	var valAvgProjSize="";
	var fromGenerator = document.getElementById("fromGenerator").value;
	document.getElementById("info_link").style.display = "none";
	$("#additional_info").hide();

	for(var j = 0; j < results.children.length; j++){
		var child = results.children[j];
		switch(child.type){
			case "ProjectsList":
				inner = "";
				if (child.top.length==0){
					inner = "<b>"+trnNoDataToShow+"</b> <br />";
				} else {
					var isPublicView = document.getElementById("fromPublicView").value;
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". ";
						inner = inner + "<a target='_blank' href='/aim/viewActivityPreview.do~pageId=2~activityId=" + child.top[i].id + "~isPreview=1'>" + child.top[i].name + "</a>" + "  <b>(" + child.top[i].value + ")</b> <hr />";
						
					}
					inner = inner + "<a href='javascript:showFullList(\"projects\")' style='float:right;'>"+trnShowFullList+"</a>";
				}
				var div = document.getElementById("divTopProjects");
				if (div!=null)
					div.innerHTML = inner;
				break;
			case "OrganizationsList":
				//if (dashboardType!=1) {
					inner = "";
					if (child.top.length==0){
						inner = "<b>"+trnNoDataToShow+"</b> <br />";
					} else {
						for(var i = 0; i < child.top.length; i++){
							inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
						}
						inner = inner + "<a href='javascript:showFullList(\"organizations\")' style='float:right;'>"+trnShowFullList+"</a>";
					}
					var div = document.getElementById("divTopOrganizations");
					if (div!=null)
						div.innerHTML = inner;
				//}
				break;
			case "SectorsList":
				//if (dashboardType!=3) {
					inner = "";
					if (child.top.length==0){
						inner = "<b>"+trnNoDataToShow+"</b> <br />";
					} else {
							for(var i = 0; i < child.top.length; i++){
							inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
						}
						inner = inner + "<a href='javascript:showFullList(\"sectors\")' style='float:right;'>"+trnShowFullList+"</a>";
					}
					var div = document.getElementById("divTopSectors");
					if (div!=null)
						div.innerHTML = inner;
				//}
				break;
			case "RegionsList":
				//if (dashboardType!=2) {
					inner = "";
					if (child.top.length==0){
						inner = "<b>"+trnNoDataToShow+"</b> <br />";
					} else {
							for(var i = 0; i < child.top.length; i++){
							inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
						}
						inner = inner + "<a href='javascript:showFullList(\"regions\")' style='float:right;'>"+trnShowFullList+"</a>";
					}
					var div = document.getElementById("divTopRegions");
					if (div!=null)
						div.innerHTML = inner;
				//}
				break;
			case "NPOsList":
				//if (dashboardType!=4) {
					inner = "";
					if (child.top.length==0){
						inner = "<b>"+trnNoDataToShow+"</b> <br />";
					} else {
							for(var i = 0; i < child.top.length; i++){
							inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
						}
						inner = inner + "<a href='javascript:showFullList(\"NPOs\")' style='float:right;'>"+trnShowFullList+"</a>";
					}
					var div = document.getElementById("divTopNPOs");
					if (div!=null)
						div.innerHTML = inner;
				//}
				break;
			case "ProgramsList":
				//if (dashboardType!=4) {
					inner = "";
					if (child.top.length==0){
						inner = "<b>"+trnNoDataToShow+"</b> <br />";
					} else {
							for(var i = 0; i < child.top.length; i++){
							inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
						}
						inner = inner + "<a href='javascript:showFullList(\"programs\")' style='float:right;'>"+trnShowFullList+"</a>";
					}
					var div = document.getElementById("divTopPrograms");
					if (div!=null)
						div.innerHTML = inner;
				//}
				break;
			case "SecondaryProgramsList":
				//if (dashboardType!=4) {
					inner = "";
					if (child.top.length==0){
						inner = "<b>"+trnNoDataToShow+"</b> <br />";
					} else {
							for(var i = 0; i < child.top.length; i++){
							inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
						}
						inner = inner + "<a href='javascript:showFullList(\"secondaryPrograms\")' style='float:right;'>"+trnShowFullList+"</a>";
					}
					var div = document.getElementById("divTopSecondaryPrograms");
					if (div!=null)
						div.innerHTML = inner;
				//}
				break;
			case "SelOrgGroupsList":
				if (dashboardType!=4) {
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("org_grp_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("org_group_list_id");
					div.innerHTML = inner;
					document.getElementById("filterOrgGroups").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("org_group_dropdown_id").style.display = "none";
					} else {
						document.getElementById("org_group_list_id").style.display = "none";
						document.getElementById("org_group_dropdown_id").style.display = "";
					}
				}
				break;
			case "SelOrgsList":
				//if (dashboardType!=4) {
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("organization_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("org_list_id");
					div.innerHTML = inner;
					document.getElementById("filterOrganizations").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("org_dropdown_id").style.display = "none";
					} else {
						document.getElementById("org_list_id").style.display = "none";
						document.getElementById("org_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelImpAgList":
				if (document.getElementById("imp_ag_list_id")!=null){
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("implementing_agency_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("imp_ag_list_id");
					div.innerHTML = inner;
					document.getElementById("filterImpAgencies").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("implementing_agency_dropdown_id").style.display = "none";
					} else {
						document.getElementById("imp_ag_list_id").style.display = "none";
						document.getElementById("implementing_agency_dropdown_id").style.display = "";
					}
				}
				break;
			case "SelBenAgList":
				if (document.getElementById("ben_ag_list_id")!=null){
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("beneficiary_agency_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("ben_ag_list_id");
					div.innerHTML = inner;
					document.getElementById("filterBenAgencies").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("beneficiary_agency_dropdown_id").style.display = "none";
					} else {
						document.getElementById("ben_ag_list_id").style.display = "none";
						document.getElementById("beneficiary_agency_dropdown_id").style.display = "";
					}
				}
				break;
			case "SelSecProgList":
				if (document.getElementById("sec_prog_list_id")!=null){
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("secondary_program_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("sec_prog_list_id");
					div.innerHTML = inner;
					document.getElementById("filterSecPrograms").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("secondary_program_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sec_prog_list_id").style.display = "none";
						document.getElementById("secondary_program_dropdown_id").style.display = "";
					}
				}
				break;
			case "SelRegionsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("region_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("region_list_id");
					div.innerHTML = inner;
					document.getElementById("filterRegions").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("region_dropdown_id").style.display = "none";
					} else {
						document.getElementById("region_list_id").style.display = "none";
						document.getElementById("region_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelZonesList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("zone_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("zone_list_id");
					div.innerHTML = inner;
					document.getElementById("filterZones").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("zone_dropdown_id").style.display = "none";
					} else {
						document.getElementById("zone_list_id").style.display = "none";
						document.getElementById("zone_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelStatusList":
				if (child.list.length > 0) {
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("status_check",child.list[i].id);
					}
					document.getElementById("filterStatus").innerHTML = inner2;
				}
				break;
			case "SelSectorConfig":
					if (child.list.length > 0) {
					inner ="<hr/>"+child.list[0].name;
					var div = document.getElementById("sector_config_list_id");
					inner = inner + "<hr/>";
					div.innerHTML = inner;
					document.getElementById("filterSectorConfiguration").innerHTML = inner;
					div.style.display = "";
					document.getElementById("sector_config_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sector_config_list_id").style.display = "none";
						document.getElementById("sector_config_dropdown_id").style.display = "";
					}
				break;
			case "SelSectorsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("sector_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("sector_list_id");
					div.innerHTML = inner;
					document.getElementById("filterSectors").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("sector_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sector_list_id").style.display = "none";
						document.getElementById("sector_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelSubSectorsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "<hr/>";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>";
						inner2 = inner2 + child.list[i].name + " - ";
						if (fromGenerator=="true")
							checkOptionByNameAndValue("sub_sector_check",child.list[i].id);
					}
					inner = inner + "<hr/>";
					var div = document.getElementById("sub_sector_list_id");
					div.innerHTML = inner;
					document.getElementById("filterSubSectors").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("sub_sector_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sub_sector_list_id").style.display = "none";
						document.getElementById("sub_sector_dropdown_id").style.display = "";
					}
				//}
				break;
			case "TotalComms":
				valTotalCommits = child.value + " " + (isThousands ? trnThousands : trnMillions) + " "  + child.curr;
				var textAmounts = isThousands ? trnAllAmountsInThousands:trnAllAmountsInMillions;
				
				inner = "<i><font size='2' color='red'>" + textAmounts + " - " + child.curr + "</font></i>";
				document.getElementById("currencyCode").value = child.curr;
				var div = document.getElementById("currencyInfo");
				div.innerHTML = inner;
				
				var div = document.getElementById("filterCurrency");
				div.innerHTML = "" + child.curr;
				
				break;
			case "TotalDisbs":
				valTotalDisbs = child.value + " " + (isThousands ? trnThousands : trnMillions) + " "  + child.curr;
				break;
			case "NumberOfProjs":
				valNumOfProjs = child.value;
				break;
			case "NumberOfDons":
				valNumOfDons = child.value;
				break;
			case "NumberOfSecs":
				valNumOfSecs = child.value;
				break;
			case "NumberOfRegs":
				valNumOfRegs = child.value;
				break;
			case "AvgProjSize":
				valAvgProjSize = child.value + " " + (isThousands ? trnThousands : trnMillions) + " "  + child.curr;
				break;
			case "SelOrgContact":
				if (child.list.length ==1) {
					var contact=child.list[0];
					var contactMarkup = new Array();
					contactMarkup.push("<table class=\"inside\">");
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnTitle + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					contactMarkup.push(contact.title);
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnName + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					contactMarkup.push(contact.name);
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnEmails + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactEmails=contact.email;
				
					for(var i=0;i<conactEmails.length;i++){
						contactMarkup.push(conactEmails[i].value);
						contactMarkup.push("<br/>");
						
					}
		
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnPhones + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactPhones=contact.phones;
					for(var i=0;i<conactPhones.length;i++){
						contactMarkup.push(conactPhones[i].value);
						contactMarkup.push("<br/>");
						
					}
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnFaxes + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactFaxes=contact.faxes;
					for(var i=0;i<conactFaxes.length;i++){
						contactMarkup.push(conactFaxes[i].value);
						contactMarkup.push("<br/>");
						
					}
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					contactMarkup.push("</table>");
					$("#tab2").html(contactMarkup.join(""));
					
				}
				else{
					$("#tab2").html(trnNoContactInfo);
				}
				break;
			case "SelAdditionalInfo":
				if (typeof child.additionalInfo.info != 'undefined') {
					var info=child.additionalInfo.info;
					var trnBackground = "";
					switch(info.type){
						case "Organization":
							trnBackground = trnBackgroundOrganization;
							//Show Contacts Tab. Organizations can have contacts.
							$("#contact_info_tab").show();
							myPanel.setHeader("\n" + trnOrgInfo);
							break;
						case "OrganizationGroup":
							trnBackground = trnBackgroundOrganizationGroup;
							//Hide Contacts Tab. Organization Groups cannot have contacts.
							$("#contact_info_tab").hide()
							myPanel.setHeader("\n" + trnOrgGrpInfo);
							break;
					}
					disabledInfo = (urlSaveAdditional != "") ? "": "disabled='disabled'";
					var infoMarkup = new Array();
					infoMarkup.push("<div id=\"saveResultMsg\"></div><table class=\"inside\"><tbody>");
					
					if (document.getElementById("BackgroundOrganizationVisible")!=null){
						infoMarkup.push("<tr>");
						infoMarkup.push("<td class=\"inside\">" + trnBackground +":</td>");
						infoMarkup.push("<td class=\"inside\">");
						infoMarkup.push("<textarea cols=\"40\" rows=\"3\" id=\"background\" " + disabledInfo + ">");
						infoMarkup.push(info.background);
						infoMarkup.push("</textarea>");
						infoMarkup.push("</td>");
						infoMarkup.push("</tr>");
					}
					
					if (document.getElementById("OrgDescriptionVisible")!=null){
						infoMarkup.push("<tr>");
						infoMarkup.push("<td class=\"inside\">" + trnDescription + ":</td>");
						infoMarkup.push("<td class=\"inside\">");
						infoMarkup.push("<textarea cols=\"40\" rows=\"3\" id=\"description\" " + disabledInfo + ">");
						infoMarkup.push(info.description);
						infoMarkup.push("</textarea>");
						infoMarkup.push("</td>");
						infoMarkup.push("</tr>");
					}
					
					if (document.getElementById("KeyAreasFocusVisible")!=null){
						infoMarkup.push("<tr>");
						infoMarkup.push("<td class=\"inside\">" + trnKeyAreas + ":</td>");
						infoMarkup.push("<td class=\"inside\">");
						infoMarkup.push("<textarea cols=\"40\" rows=\"3\" id=\"keyAreas\" " + disabledInfo + ">");
						infoMarkup.push(info.keyAreas);
						infoMarkup.push("</textarea>");
						infoMarkup.push("</td>");
						infoMarkup.push("</tr>");
					}
					
					infoMarkup.push("<tr>");
					infoMarkup.push("<td class=\"inside\" colspan=\"2\">");
					infoMarkup.push("<input type=\"button\" value=\"" + trnSave + "\" onclick=\"saveAdditionalInfo("+info.id+",'" + info.type +"')\"/>");
					infoMarkup.push("</td>");
					infoMarkup.push("</tr>");
					infoMarkup.push("</tbody></table>");
					var markup=infoMarkup.join("");
					$("#tab3").html(markup);
					
					var infoBox = new Array();
					infoBox.push("<table class=\"inside\"><tbody>");
					infoBox.push("<tr>");
					infoBox.push("<td class=\"inside\"><strong>" + trnBackground + ":</strong>");
					infoBox.push(info.background);
					infoBox.push("</td>");
					infoBox.push("</tr>");

					infoBox.push("<tr>");
					infoBox.push("<td class=\"inside\"><strong>" + trnDescription + ":</strong>");
					infoBox.push(info.description);
					infoBox.push("</td>");
					infoBox.push("</tr>");

					infoBox.push("<tr>");
					infoBox.push("<td class=\"inside\"><strong>" + trnKeyAreas + ":</strong>");
					infoBox.push(info.keyAreas);
					infoBox.push("</td>");
					infoBox.push("</tr>");
					infoBox.push("</tbody></table>");
					$("#additional_info_box").html(infoBox.join(""));
					$("#additional_info").show();
				}
				else{
					$("#tab3").html(trnNoAdditionalInfo);
				}
				break; 
				
		}
	}
	inner = "<a title='" + trnTotalCommitsDescription + "' style='color: black;'>" + trnTotalCommitments + "</a> <b class='dashboard_total_num'>" + valTotalCommits + "</b><span class='breadcrump_sep'>|</span>";
	
	inner = inner + "<a title='" + trnTotalDisbsDescription + "' style='color: black;'>" + trnTotalDisbs + "</a> <b class='dashboard_total_num'>" + valTotalDisbs + "</b><br />";
	
	
/*	inner = trnTotalCommitments + " <b class='dashboard_total_num'>" + valTotalCommits + "</b>";
	inner = inner + trnTotalDisbs + " <b class='dashboard_total_num'>" + valTotalDisbs + "</b>";
	var div = document.getElementById("divTotals");
	div.innerHTML = inner;*/
	
	inner = inner + "<a title='" + trnNumOfProjsDescription + "' style='color: black;'>" + trnNumOfProjs + "</a> <b>" + valNumOfProjs + "</b><span class='breadcrump_sep'>|</span>";
	//if (dashboardType!=1) {
		inner = inner + "<a title='" + trnNumOfDonsDescription + "' style='color: black;'>" + trnNumOfDons + "</a> <b>" + valNumOfDons + "</b><span class='breadcrump_sep'>|</span>";
	//}
	//if (dashboardType!=3) {
		inner = inner + "<a title='" + trnNumOfSecsDescription + "' style='color: black;'>" + trnNumOfSecs + "</a> <b>" + valNumOfSecs + "</b><span class='breadcrump_sep'>|</span>";
	//}
	//if (dashboardType!=2) {
		inner = inner + "<a title='" + trnNumOfRegsDescription + "' style='color: black;'>" + trnNumOfRegs + "</a> <b>" + valNumOfRegs + "</b>";
	//}
	inner = inner + "<br /><a title='" + trnAvgProjSizeDescription + "' style='color: black;'>" + trnAvgProjSize + "</a> <b>" + valAvgProjSize + "</b>";
	var div = document.getElementById("divSummaryInfo");
	div.innerHTML = inner;

	document.getElementById("fromGenerator").value = false;
	
	var namePlaceholder = document.getElementById("dashboard_name");
	if (dashboardType==1) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("org_grp_check",true)==""){
			if (document.getElementById("org_group_dropdown_id").selectedIndex != 0) {
				name1 = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
				document.getElementById("info_link").style.display = "block";
			}
		} else {
			if (getSelectionsFromElement("org_grp_check",false).indexOf(',') !=-1) {
				name1 = trnMultipleOrgGrp;
			} else {
				name1 = getSelectionsFromElement("org_grp_check",true);
				document.getElementById("info_link").style.display = "block";
			}
		}
		if (getSelectionsFromElement("organization_check",true)==""){
			if (document.getElementById("org_dropdown_id").selectedIndex != 0) {
				name2 = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("organization_check",false).indexOf(',') !=-1) {
				name2 = trnMultipleOrgs;
			} else {
				name2 = getSelectionsFromElement("organization_check",true);
				document.getElementById("info_link").style.display = "block";
			}
		}
		if (name1 == "") {
			name1 = trnAllOrgGroups;
		}
		name1 = name1.replace(/</g, "< ");
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	if (dashboardType==3) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("sector_check",true)==""){
			if (document.getElementById("sector_dropdown_id").selectedIndex != 0) {
				name1 = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("sector_check",false).indexOf(',') !=-1) {
				name1 = trnMultipleSector;
			} else {
				name1 = getSelectionsFromElement("sector_check",true);
			}
		}
		if (getSelectionsFromElement("sub_sector_check",true)==""){
			if (document.getElementById("sub_sector_dropdown_id").selectedIndex != 0) {
				name2 = document.getElementById("sub_sector_dropdown_id").options[document.getElementById("sub_sector_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("sub_sector_check",false).indexOf(',') !=-1) {
				name2 = trnMultipleSubSector;
			} else {
				name2 = getSelectionsFromElement("sub_sector_check",true);
			}
		}
		if (name1 == "") {
			name1 = trnAllSectors;
		}
		name1 = name1.replace(/</g, "< ");
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	if (dashboardType==2) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("region_check",true)==""){
			if (document.getElementById("region_dropdown_id").selectedIndex != 0) {
				name1 = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("region_check",false).indexOf(',') !=-1) {
				name1 = trnMultipleRegion;
			} else {
				name1 = getSelectionsFromElement("region_check",true);
			}
		}
		if (getSelectionsFromElement("zone_check",true)==""){
			if (document.getElementById("zone_dropdown_id").selectedIndex != 0) {
				name2 = document.getElementById("zone_dropdown_id").options[document.getElementById("zone_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("zone_check",false).indexOf(',') !=-1) {
				name2 = trnMultipleZones;
			} else {
				name2 = getSelectionsFromElement("zone_check",true);
			}
		}
		if (name1 == "") {
			name1 = trnAllRegions;
		}
		name1 = name1.replace(/</g, "< ");
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	
	updateTitles();
	
	var startYear = document.getElementById("startYear").value;
	var endYear = document.getElementById("endYear").value;

	div = document.getElementById("topProjectsTitle");
	if (startYear == endYear) {
		inner = trnTopProjects + " (" + startYear + ")";
	} else {
		inner = trnTopProjects + " (" + startYear + "-" + endYear + ")";
	}
	if (div!=null)
		div.innerHTML = inner;
		
	div = document.getElementById("topSectorsTitle");
	if (startYear == endYear) {
		inner = trnTopSectors + " (" + startYear + ")";
	} else {
		inner = trnTopSectors + " (" + startYear + "-" + endYear + ")";
	}
	if (div!=null)
		div.innerHTML = inner;
		
	div = document.getElementById("topOrganizationsTitle");
	if (startYear == endYear) {
		inner = trnTopOrganizations + " (" + startYear + ")";
	} else {
		inner = trnTopOrganizations + " (" + startYear + "-" + endYear + ")";
	}
	if (div!=null)
		div.innerHTML = inner;
	
	div = document.getElementById("topRegionsTitle");
	if (startYear == endYear) {
		inner = trnTopRegions + " (" + startYear + ")";
	} else {
		inner = trnTopRegions + " (" + startYear + "-" + endYear + ")";
	}
	if (div!=null)
		div.innerHTML = inner;
	
	div = document.getElementById("topNPOsTitle");
	if (startYear == endYear) {
		inner = trnTopNPOs + " (" + startYear + ")";
	} else {
		inner = trnTopNPOs + " (" + startYear + "-" + endYear + ")";
	}
	if (div!=null)
		div.innerHTML = inner;
	
	div = document.getElementById("topProgramsTitle");
	if (startYear == endYear) {
		inner = trnTopPrograms + " (" + startYear + ")";
	} else {
		inner = trnTopPrograms + " (" + startYear + "-" + endYear + ")";
	}
	if (div!=null)
		div.innerHTML = inner;
	
	div = document.getElementById("topSecondaryProgramsTitle");
	if (startYear == endYear) {
		inner = trnTopSecondaryPrograms + " (" + startYear + ")";
	} else {
		inner = trnTopSecondaryPrograms + " (" + startYear + "-" + endYear + ")";
	}
	if (div!=null)
		div.innerHTML = inner;
	
}

function updateTitles(){
	var adjType = document.getElementById("adjustmentType").value;
	var fundType = "Actual ";
	if (adjType == 'Actual')
		fundType = ""+trnActual+" ";
	else if (adjType == 'Planned')
		fundType = ""+trnPlanned+" ";
	var type = document.getElementById("transactionType").value;
	if (type==0) {
		fundType += trnCommitments;
	}
	if (type==1) {
		fundType += trnDisbursements;
	}
	if (type==2) {
		fundType += trnExpenditures;
	}
	if (type==3) {
		fundType = trnMTEFProjections;
	}
	
	var titlesObj = getTitlesObjects();
	for ( var i = 0; i < titlesObj.length; i++) {
		var id = titlesObj[i].getAttribute("id");
		if(id!=null && id.indexOf("Legend")!=-1){
			var divide =  document.getElementById(id.substr(0,id.indexOf("TitleLegend"))+"Divide").checked;
			var inThousands =  document.getElementById("showAmountsInThousands").value;
			var anmtIn = trnInMillions;
			if (divide && inThousands=="1")
				anmtIn = trnInMillions;
			if (!divide && inThousands=="1")
				anmtIn = trnInThousands;
			if (divide && inThousands=="2")
				anmtIn = trnInBillions;
			if (!divide && inThousands=="2")
				anmtIn = trnInMillions;
			var input = document.getElementById(id.substr(0,id.indexOf("Legend")));
			var trnTitle = document.getElementById(id+"Trn").value + " - " + fundType + " (" + anmtIn + ")";
			titlesObj[i].innerHTML = trnTitle;
			if (input != null)
				input.value = trnTitle;
		}
	}
}

function getTitlesObjects(){
	var objs = document.getElementsByTagName("span");
	var arrReturnElements = new Array();
	for ( var i = 0; i < objs.length; i++) {
		if(navigator.appName == "Microsoft Internet Explorer")
			var att = objs[i].getAttribute("className");
		else
			var att = objs[i].getAttribute("class");
		if (att=="legend_label"){
			arrReturnElements.push(objs[i]);
		}
	}
	return (arrReturnElements);
}

if(typeof graphId === 'undefined'){//showing the whole dashboard
	YAHOO.util.Event.onDOMReady(initializeTranslations);
	YAHOO.util.Event.onDOMReady(initializeGlobalVariables);
	YAHOO.util.Event.onDOMReady(initializePage);
	YAHOO.util.Event.onDOMReady(initDashboard);
	YAHOO.util.Event.onDOMReady(initPanel);
	YAHOO.util.Event.addListener("region_dropdown_id", "change", callbackChildren);
	YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
	YAHOO.util.Event.addListener("sector_dropdown_id", "change", callbackChildren);
	YAHOO.util.Event.addListener("sector_config_dropdown_id", "change", callbackChildren);
	YAHOO.util.Event.addListener("fiscalCalendar_dropdown_Id", "change", callbackChildren);
	YAHOO.util.Event.addListener("applyButton", "click", callbackApplyFilter);
	YAHOO.util.Event.addListener("applyButtonPopin", "click", applyFilterPopin);
	YAHOO.util.Event.addListener("visualizationDiv", "click", refreshGraphs);
}else{//showing only a graph
	YAHOO.util.Event.onDOMReady(initializeLoadingPanel);
	YAHOO.util.Event.onDOMReady(initGraph);
}

var initialized = false;
function initDashboard(){

	var allGraphs = getElementsByName_iefix("div", "flashContent");
	for(var idx = 0; idx < allGraphs.length; idx++){
		var id = allGraphs[idx].children[0].getAttribute("id");
		drawGraph(id);
	}
	callbackApplyFilter();
}

function initGraph(){
	drawGraph(graphId);

	var callbackApplyFilterCall = {
			  success: function(o) {
				  refreshGraphs();
				  updateTitles();
			  },
			  failure: function(o) {
			  }
			};

	YAHOO.util.Connect.setForm('visualizationform');

	var sUrl="/visualization/dataDispatcher.do?action=applyFilter";
	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
}

function drawGraph(id){
	
	if (id.indexOf("Sector")!=-1)
		changeChart(null, 'donut', id, true);
	else if (id.indexOf("Region")!=-1)
		changeChart(null, 'line', id, true);
	else if (id.indexOf("Budget")!=-1)
		changeChart('start', 'donut', id, true);
	else if (id.indexOf("SecondaryProgram")!=-1)
		changeChart('start', 'donut', id, true);
	else if (id.indexOf("Profile")!=-1)
		changeChart(null, 'bar_profile', id, true);
	else if (id.indexOf("Growth")!=-1)
		changeChart(null, 'bar_growth', id, true);
	else
		changeChart(null, 'bar', id, true);
}

function  saveAdditionalInfo(id, type){
    var postString = "background=" + document.getElementById("background").value +
	    "&description=" + document.getElementById("description").value + 
	    "&keyAreas=" + document.getElementById("keyAreas").value + 
        "&id=" + id + "&type=" + type;
        $("#saveResultMsg").html(trnSavingInformation);
        YAHOO.util.Connect.asyncRequest("POST", urlSaveAdditional, additionalInfoCallback, postString);
    }

    var additionalInfoResponseSuccess = function(o){
    	$("#saveResultMsg").html(trnSavedInformation);
    	var results = YAHOO.lang.JSON.parse(o.responseText);
		var trnBackground = "";
		switch(results.type){
			case "Organization":
				trnBackground = trnBackgroundOrganization;
				break;
			case "OrganizationGroup":
				trnBackground = trnBackgroundOrganizationGroup;
				break;
		}
		var infoBox = new Array();
		infoBox.push("<table class=\"inside\"><tbody>");
		infoBox.push("<tr>");
		infoBox.push("<td class=\"inside\"><strong>" + trnBackground + ":</strong>");
		infoBox.push(results.background);
		infoBox.push("</td>");
		infoBox.push("</tr>");

		infoBox.push("<tr>");
		infoBox.push("<td class=\"inside\"><strong>" + trnDescription + ":</strong>");
		infoBox.push(results.description);
		infoBox.push("</td>");
		infoBox.push("</tr>");

		infoBox.push("<tr>");
		infoBox.push("<td class=\"inside\"><strong>" + trnKeyAreas + ":</strong>");
		infoBox.push(results.keyAreas);
		infoBox.push("</td>");
		infoBox.push("</tr>");
		infoBox.push("</tbody></table>");
		$("#additional_info_box").html(infoBox.join(""));
    	
    	
    }

    var additionalInfoResponseFailure = function(o){
    	$("#saveResultMsg").html(trnFailedSave);
    }
    var additionalInfoCallback =
        {
        success:additionalInfoResponseSuccess,
        failure:additionalInfoResponseFailure
    };

function getValueToFlash(idContainer, field){
	
	if (field == 'Currency'){
		return document.getElementById("currencyCode").value;
	}
	if (field == 'DecimalsToShow'){
		return document.getElementById("decimalsToShow").value;
	}
	if (field == 'SliderLabels'){
		return document.getElementById("flashSliderLabels").value;
	}
	if (field == 'sliderThumbCount'){ //hardcode to show only one thumb in slider year selector for quarter graph
		if (idContainer == 'AidPredictabilityQuarter') {
			return 1;
		} else {
			return 2;
		}
	}
	var inputObject = document.getElementById(idContainer + field);
	var returnValue;
	
	if(inputObject != undefined && inputObject.type == "checkbox"){
		returnValue = (inputObject == undefined) ? "" : inputObject.checked;
	}
	else
	{
		returnValue = (inputObject == undefined) ? "" : inputObject.value;
	}
	return returnValue;
}


function updateGraph(e, chartName){

	var adjType = document.getElementById("adjustmentType").value;
	var fundType = "Actual ";
	if (adjType == 'Actual')
		fundType = ""+trnActual+" ";
	else if (adjType == 'Planned')
		fundType = ""+trnPlanned+" ";
	var type = document.getElementById("transactionType").value;
	if (type==0) {
		fundType += trnCommitments;
	}
	if (type==1) {
		fundType += trnDisbursements;
	}
	if (type==2) {
		fundType += trnExpenditures;
	}
	if (type==3) {
		fundType = trnMTEFProjections;
	}	
	//Get array of graphs
	var allGraphs = getElementsByName_iefix("div", "flashContent");
	//Iterate and refresh the graph
	var divide =  document.getElementById(chartName+"Divide").checked;
	var inThousands =  document.getElementById("showAmountsInThousands").value;
	var anmtIn = trnInMillions;
	if (divide && inThousands=="1")
		anmtIn = trnInMillions;
	if (!divide && inThousands=="1")
		anmtIn = trnInThousands;
	if (divide && inThousands=="2")
		anmtIn = trnInBillions;
	if (!divide && inThousands=="2")
		anmtIn = trnInMillions;
	
	for(var idx = 0; idx < allGraphs.length; idx++){
		// Get flash object and refresh it by calling internal
		if(allGraphs[idx].children[0].id.toLowerCase() == chartName.toLowerCase()){
			var trnTitle = "";
			var text = document.getElementById(chartName + "Title").value;
			if (text.indexOf(" (" + trnInThousands + ")")!=-1)
				text = text.substring(0,text.indexOf(" (" + trnInThousands + ")")) + text.substring((text.indexOf(" (" + trnInThousands + ")")+((" (" + trnInThousands + ")").length)), text.length);
			if (text.indexOf(" (" + trnInMillions + ")")!=-1)
				text = text.substring(0,text.indexOf(" (" + trnInMillions + ")")) + text.substring((text.indexOf(" (" + trnInMillions + ")")+((" (" + trnInMillions + ")").length)), text.length);
			if (text.indexOf(" (" + trnInBillions + ")")!=-1)
				text = text.substring(0,text.indexOf(" (" + trnInBillions + ")")) + text.substring((text.indexOf(" (" + trnInBillions + ")")+((" (" + trnInBillions + ")").length)), text.length);

			trnTitle = text+ " (" + anmtIn + ")";
			//alert("trnTitle: "+trnTitle);
			document.getElementById(chartName + "Title").value = trnTitle;
			document.getElementById(chartName + "TitleLegend").innerHTML = trnTitle;
			allGraphs[idx].children[0].refreshGraph();
			break;
		}
	}
}

function changeChart(e, chartType, container, useGeneric){
	var startMovie = false;
	//Get the calling object to select it and remove style.
	if(e != null){
		if (e == "start"){
			startMovie = true; 
		} else {
			var caller = e.target || e.srcElement;
			var linkBar = caller.parentNode.getElementsByTagName("A");
		    for(var i in linkBar)
		    {
		    	linkBar[i].className = "";
		    }
		    caller.className = "sel_sm_b";
		    startMovie = true; // This is set to true if it comes from clicking in the top right of the charts
		}
	}

	var palette = "0xFF6600,0x7EAE58,0x88BFF5,0xBE0035,0x8B007E,0x99431C,0xFF6666,0x94FF29,0x2929FF,0xFF29FF";
	if (document.getElementById("show_monochrome") && document.getElementById("show_monochrome").checked){
		palette = "0x000000,0x969696,0x191919,0xAFAFAF,0x323232,0xC8C8C8,0x4B4B4B,0xE1E1E1,0x646464,0xFAFAFA,0x7D7D7D";
	}

	var decimalSeparator = document.getElementById("decimalSeparator").value;
	var groupSeparator = document.getElementById("groupSeparator").value;
	var decimalsToShow = document.getElementById("decimalsToShow").value;
	var currCode = document.getElementById("currencyCode").value;
	var divide =  document.getElementById(container+"Divide").checked;
	
	
	
	var flashvars = { 
			decimalSeparator: decimalSeparator, 
			groupSeparator: groupSeparator,
			decimalsToShow: decimalsToShow,
			currCode: currCode,
			palette: palette, 
			id: container,
			start: (startMovie ? "true" : "false"),
			divide: "true"
		};
	var params = {
			wmode: "transparent"
		};
	var attributes = {};
	attributes.id = container;
	//Setting for cache in development mode
//	var cache = "?rnd=" + Math.floor(Math.random()*50000);
	var cache = "";

	switch(chartType){
		case "bar":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "bar_growth":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_Growth.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "bar_profile":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_Profile.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "donut":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/PieChartSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/PieChart_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "line":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "dataview":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
	}
	updateChartSettings(container, chartType);
	//updateGraph (e,container);
	return false;
}

function updateChartSettings(container, chartType){
	var title = document.getElementById(container + "Title") == undefined ? "" : document.getElementById(container + "Title");
	var fontSize = document.getElementById(container + "FontSize") == undefined ? "" : document.getElementById(container + "FontSize");
	var boldTitle = document.getElementById(container + "Bold") == undefined ? "" : document.getElementById(container + "Bold");
	var showLegend = document.getElementById(container + "ShowLegend") == undefined ? "" : document.getElementById(container + "ShowLegend");
	var showLegendLabel = document.getElementById(container + "ShowLegendLabel") == undefined ? "" : document.getElementById(container + "ShowLegendLabel");
	var showDataLabel = document.getElementById(container + "ShowDataLabel") == undefined ? "" : document.getElementById(container + "ShowDataLabel");
	var rotateDataLabel = document.getElementById(container + "RotateDataLabel") == undefined ? "" : document.getElementById(container + "RotateDataLabel");
	var rotateDataLabelLabel = document.getElementById(container + "RotateDataLabelLabel") == undefined ? "" : document.getElementById(container + "RotateDataLabelLabel");
	var divide = document.getElementById(container + "Divide") == undefined ? "" : document.getElementById(container + "Divide");
	var divideLabel = document.getElementById(container + "DivideLabel") == undefined ? "" : document.getElementById(container + "DivideLabel");
	var ignore = document.getElementById(container + "Ignore") == undefined ? "" : document.getElementById(container + "Ignore");
	var ignoreLabel = document.getElementById(container + "IgnoreLabel") == undefined ? "" : document.getElementById(container + "IgnoreLabel");

	switch(chartType){
	case "bar_growth":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.style.display = "none";
		showLegendLabel.style.display = "none";
		showDataLabel.disabled = false;
		rotateDataLabel.style.display = "none";
		rotateDataLabelLabel.style.display = "none";
		divide.style.display = "none";
		divideLabel.style.display = "none";
		ignore.style.display = "";
		ignoreLabel.style.display = "";
		break;
	case "bar":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = false;
		showDataLabel.disabled = false;
		rotateDataLabel.disabled = false;
		divide.disabled = false;
		break;
	case "bar_profile":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = true;
		showDataLabel.disabled = false;
		rotateDataLabel.disabled = false;
		divide.disabled = false;
		break;
	case "donut":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = false;
		showDataLabel.disabled = true;
		rotateDataLabel.disabled = true;
		divide.disabled = true;
		break;
	case "line":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = false;
		showDataLabel.disabled = true;
		rotateDataLabel.disabled = true;
		divide.disabled = false;
		break;
	case "dataview":
		title.disabled = true;
		fontSize.disabled = true;
		boldTitle.disabled = true;
		showLegend.disabled = true;
		showDataLabel.disabled = true;
		rotateDataLabel.disabled = true;
		divide.disabled = true;
		break;
}
	
	
}
//var mySelec = new YAHOO.widget.TabView("selectDiv");
//mySelec.selectTab(0);


function reloadGraphs(){
	var dashboardType = document.getElementById("dashboardType").value;
	var allGraphs = getElementsByName_iefix("div", "flashContent");
	for(var idx = 0; idx < allGraphs.length; idx++){
		var id = allGraphs[idx].children[0].getAttribute("id");
		if (id.indexOf("Sector")!=-1)
			changeChart('start', 'donut', id, true);
		else if (id.indexOf("Region")!=-1)
			changeChart('start', 'line', id, true);
		else if (id.indexOf("Profile")!=-1)
			changeChart('start', 'bar_profile', id, true);
		else if (id.indexOf("Growth")!=-1)
			changeChart('start', 'bar_growth', id, true);
		else
			changeChart('start', 'bar', id, true);
	}
	
}

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

function callbackGetGraphs(id,baseType) {
	document.getElementById("dashboardType").value = baseType;
	var elems = document.getElementsByName("dsbd");
	for ( var i = 0; i < elems.length; i++) {
		var id2 = elems[i].getAttribute("id");
		if (id2==id) {
			$("#"+id2).addClass("side_opt_sel");	
		} else {
			$("#"+id2).removeClass("side_opt_sel");	
		}
	}
	var typeSel1 = document.getElementById("agencyTypeSelector1");
	var typeSel2 = document.getElementById("agencyTypeSelector2");
	var benAgencyLI = document.getElementById("benAgencyLI");
	var impAgencyLI = document.getElementById("impAgencyLI");
	var secProgramLI = document.getElementById("secProgramLI");
	
	if (baseType==1 || baseType=='undefined') {
		typeSel1.style.display='';
		typeSel2.style.display='';
	} else {
		typeSel1.style.display='none';
		typeSel2.style.display='none';
	}
	
	if (baseType==4 || baseType=='undefined') {
		benAgencyLI.style.display='';
		impAgencyLI.style.display='';
		secProgramLI.style.display='';
	} else {
		benAgencyLI.style.display='none';
		impAgencyLI.style.display='none';
		secProgramLI.style.display='none';
	}
	
	
	/*switch(baseType){
	case 0:
		var elems = document.getElementsByName("org_grp_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		var elems = document.getElementsByName("region_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		var elems = document.getElementsByName("sector_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		break;
	case 1:
		var elems = document.getElementsByName("org_grp_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="radio";
		}
		var elems = document.getElementsByName("region_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		var elems = document.getElementsByName("sector_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		break;
	case 2:
		var elems = document.getElementsByName("org_grp_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		var elems = document.getElementsByName("region_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="radio";
		}
		var elems = document.getElementsByName("sector_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		break;
	case 3:
		var elems = document.getElementsByName("org_grp_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		var elems = document.getElementsByName("region_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="checkbox";
		}
		var elems = document.getElementsByName("sector_check");
		for ( var i = 0; i < elems.length; i++) {
			elems[i].type="radio";
		}
		break;
	}*/
	if (id != null){
		var transaction = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getGraphsFromDashboard&id=" + id, callbackGetGraphsCall, null);
	}
}


var callbackGetGraphsCall = {
	  success: function(o) {
		  try {
			    var results = YAHOO.lang.JSON.parse(o.responseText);
	       		var graphList = document.getElementById("graphList");
	    		var inner = "";
	    		for(var i = 0; i < results.children.length; i++){
	    			inner = inner+"<li><input type='checkbox' checked='checked' value='"+results.children[i].ID+"' name='graphChech'><label>"+results.children[i].name+"</label></li>";
	    		}
	    		graphList.innerHTML = inner;
			}
			catch (e) {
			    alert("Invalid respose.");
			}
	  },
	  failure: function(o) {
		  	alert("faileD");
		  }
	};


function launchDashboard(){
	var dashboardType = document.getElementById("dashboardType").value;
	var graphList = document.getElementsByName("graphChech");
	var graphs = "";
	for(var i = 0; i < graphList.length; i++){
		if (graphList[i].checked==true)
			graphs = graphs + graphList[i].value + ",";
	}
	if(graphs.length==0){
		alert(selectOneGraph);
		return;
	}
	document.getElementById("topLists").value = document.getElementById("topLists_dropdown").options[document.getElementById("topLists_dropdown").selectedIndex].value;
	document.getElementById("decimalsToShow").value = document.getElementById("decimalsToShow_dropdown").options[document.getElementById("decimalsToShow_dropdown").selectedIndex].value;
	document.getElementById("startYear").value = document.getElementById("startYear_dropdown").options[document.getElementById("startYear_dropdown").selectedIndex].value;
	document.getElementById("endYear").value = document.getElementById("endYear_dropdown").options[document.getElementById("endYear_dropdown").selectedIndex].value;
	document.getElementById("agencyType").value = document.getElementById("agencyType_dropdown").options[document.getElementById("agencyType_dropdown").selectedIndex].value;
	
	document.getElementById("currencyId").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("fiscalCalendarId").value = document.getElementById("fiscalCalendar_dropdown_Id").options[document.getElementById("fiscalCalendar_dropdown_Id").selectedIndex].value;
	document.getElementById("commitmentsVisible").value = document.getElementById("commitments_visible").checked;
	document.getElementById("disbursementsVisible").value = document.getElementById("disbursements_visible").checked;
	if (document.getElementById("expenditures_visible")!=null){
		document.getElementById("expendituresVisible").value = document.getElementById("expenditures_visible").checked;
	}
	if (document.getElementById("pledge_visible")!=null){
		document.getElementById("pledgeVisible").value = document.getElementById("pledge_visible").checked;
	}
	if (document.getElementById("workspace_only")!=null){
		document.getElementById("workspaceOnly").value = document.getElementById("workspace_only").checked;
	}
	document.getElementById("showAmountsInThousands").value = getSelectedValue("show_amounts_in_thousands");
	
	if (document.getElementById("transaction_type_0").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_0").value;
	}
	if (document.getElementById("transaction_type_1").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_1").value;
	}
	if (document.getElementById("transaction_type_2")!=null){
		if (document.getElementById("transaction_type_2").checked == true) {
			document.getElementById("transactionType").value = document.getElementById("transaction_type_2").value;
		}
	}
	if (document.getElementById("transaction_type_3")!=null){
		if (document.getElementById("transaction_type_3").checked == true) {
			document.getElementById("transactionType").value = document.getElementById("transaction_type_3").value;
		}
	}
	
	var adTypes = document.getElementById("adjustment_type");
	for ( var i = 0; i < adTypes.length; i++) {
		if (adTypes[i].checked == true){
			document.getElementById("adjustmentType").value = adTypes[i].value;
		}
	}

	if (document.getElementById("show_projects_ranking")!=null)
		document.getElementById("showProjectsRanking").value = document.getElementById("show_projects_ranking").checked;
	if (document.getElementById("show_organizations_ranking")!=null)
		document.getElementById("showOrganizationsRanking").value = document.getElementById("show_organizations_ranking").checked;
	if (document.getElementById("show_sectors_ranking")!=null)
		document.getElementById("showSectorsRanking").value = document.getElementById("show_sectors_ranking").checked;
	if (document.getElementById("show_regions_ranking")!=null)
		document.getElementById("showRegionsRanking").value = document.getElementById("show_regions_ranking").checked;
	if (document.getElementById("show_NPO_ranking")!=null)
		document.getElementById("showNPORanking").value = document.getElementById("show_NPO_ranking").checked;
	if (document.getElementById("show_programs_ranking")!=null)
		document.getElementById("showProgramsRanking").value = document.getElementById("show_programs_ranking").checked;
	
	var params = "";
	if (document.getElementById("org_grp_check_all").checked!=true){
		params = params + "&orgGroupIds=" + getSelectionsFromElement("org_grp_check",false);
		params = params + "&orgIds=" + getSelectionsFromElement("organization_check",false);
	}
	params = params + "&regionIds=" + getSelectionsFromElement("region_check",false);
	params = params + "&zoneIds=" + getSelectionsFromElement("zone_check",false);
	params = params + "&selSectorConfigId=" + getSelectionsFromElement("sector_config_check",false);
	params = params + "&sectorIds=" + getSelectionsFromElement("sector_check",false);
	params = params + "&subSectorIds=" + getSelectionsFromElement("sub_sector_check",false);
	params = params + "&statusIds=" + getSelectionsFromElement("status_check",false);
	if (dashboardType=="4") {
		params = params + "&beneficiaryAgencyIds=" + getSelectionsFromElement("beneficiary_agency_check",false);
		params = params + "&implementingAgencyIds=" + getSelectionsFromElement("implementing_agency_check",false);
		params = params + "&secondaryProgramIds=" + getSelectionsFromElement("secondary_program_check",false);
	}

	if(document.getElementById("endYear").value < document.getElementById("startYear").value){
		alert(alertBadDate);	
		return;
	}
	var launchDashboardURL= urlLaunchDashboard + "&graphs=" + graphs + params;
	  document.visualizationform.action = launchDashboardURL;
	  document.visualizationform.target = "_blank";
	  document.visualizationform.submit();
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
	        end = queryString.length;
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

function scrollToGraph(graph) {
	document.getElementById('visualizationTab').click();
	document.getElementById(graph).scrollIntoView(true);
}

function showOrgInfo(){
	YAHOO.amptab.init();
	var element = document.getElementById("divOrgInfo");
	element.style.display = "inline";
	myPanel.setBody(element);
	myPanel.center();
	myPanel.show();
}

var myPanel = new YAHOO.widget.Panel("new", {
	width:"450px",
    fixedcenter: true,
    constraintoviewport: true,
    underlay:"none",
    close:true,
    visible:false,
    modal:true,
    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
    draggable:true} );

function initPopin() {
	
	var msg='\n' + trnOrgInfo;
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.render(document.body);
	
}