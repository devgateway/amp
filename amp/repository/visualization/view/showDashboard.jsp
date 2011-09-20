<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<!-- Visualization's Stylesheet-->
<link rel="stylesheet" href="css_2/visualization.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="js_2/yui/tabview/assets/skins/sam/tabview.css">
<digi:ref href="css_2/visualization_yui_tabs.css" type="text/css" rel="stylesheet" />

<style>
	.flashcontent {
		border: solid 1px #000;
		margin:5px 0px 0px 0px;
		padding:0px 0px 0px 0px;
		vertical-align:top;
		width:634px;
		height:400px;
		clear: both;
	    background-color: #FFFFFF;
	}
	.side_opt_sel {
		background-color: rgb(191, 210, 223); 
	}

	.chart_header {
		font-size: 11px;
		padding: 5px 5px 10px 5px;
		margin: 0px 0px 10px 0px;
		font-weight: bold;
	 	border-color: #DADAD6 #C2C1BA #C2C1BA #DADAD6;
	    border-style: solid;
	    border-width: 1px 2px 2px 1px;
	    width:500px;
	    background-color: #FFFFFF;
    }
	.chart_header label {
		font-size: 11px;
    }
    .chartFieldset {
	    background-color: #F4F4F4;
    }	
</style>
<!-- Visualization's Scripts-->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/selector/selector-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/flash/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-contains-ignorecase.js"/>"></script>
<script language="javascript">
<!--



// TODO: Move this to individual script file
$D = YAHOO.util.Dom;
$E = YAHOO.util.Event;

var yuiLoadingPanel = function(conf){
    conf = conf == undefined ? new Array() : conf;
    conf.id = conf.id == undefined ? 'yuiLoadingPanel':confi.id;
    conf.header = conf.header == undefined ? '<digi:trn>Loading, please wait...</digi:trn>':conf.header;
    conf.width = conf.width == undefined ? '240px':conf.width;
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
    
       loadingPanel.setBody(this.conf.header + 
               '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
               loadingPanel.render(document.body);
               $D.addClass(loadingPanel.id, 'tcc_lightboxLoader');
               var cancelLink = document.createElement('a');
               $D.setStyle(cancelLink, 'cursor', 'pointer');
               cancelLink.appendChild(document.createTextNode('<digi:trn>Cancel</digi:trn>'));
               $E.on(cancelLink, 'click', function(e, o){
       	           o.self.loadingPanel.hide();
       	           o.self.cancelEvent.fire();
       	           window.stop();
               }, {self:this});
               loadingPanel.appendToBody(document.createElement('br'));
               loadingPanel.appendToBody(cancelLink);
               $D.setStyle(loadingPanel.body, 'text-align', 'center');
//               $D.addClass(document.body, 'yui-skin-sam');
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
function checkUncheckRelatedEntities(option,name,id){
	uncheckAllRelatedEntities(name);
	checkRelatedEntities(option,name,id);
}
function allOptionChecked(option,name,subname){
	if(option.checked){
		var options=$("input[name='"+name+"']").removeAttr('checked');
		var options=$("input[name='"+subname+"']").removeAttr('checked');
		option.checked=true;
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

-->
</script>

<digi:instance property="visualizationform"/>
<digi:form action="/filters.do">

<!-- BREADCRUMB START
<div class="centering">
 <digi:trn>Dashboards</digi:trn><span class="breadcrump_sep"><b>»</b></span>
 	<c:if test="${visualizationform.filter.dashboardType eq '1' }">
 		<span class="bread_sel"><digi:trn>Organization Profile Dashboard</digi:trn></span>
 	</c:if>
 	<c:if test="${visualizationform.filter.dashboardType eq '2' }">
 		<span class="bread_sel"><digi:trn>Region Profile Dashboard</digi:trn></span>
 	</c:if>
 	<c:if test="${visualizationform.filter.dashboardType eq '3' }">
 		<span class="bread_sel"><digi:trn>Sector Profile Dashboard</digi:trn></span>
 	</c:if>
</div>
<br/>
BREADCRUMB END -->

<!-- POPUPS START -->
<script language="javascript">
<!--

YAHOO.namespace("YAHOO.amp");

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
	
    //var msg='\n<digi:trn>Advanced Filters</digi:trn>';
	//myPanel.setHeader(msg);
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
	var msg='\n<digi:trn>Advanced Filters</digi:trn>';
	myPanel.setHeader(msg);
	var element = document.getElementById("dialog2");
	element.style.display 	= "inline";
	myPanel.setBody(element);
	myPanel.show();
	changeTab(0);
	
	
}
function hidePopin() {
	myPanel.hide();
}

function showExport() {
	var msg='\n<digi:trn jsFriendly="true">Export Options</digi:trn>';
	myPanel.setHeader(msg);
	var element = document.getElementById("exportPopin");
	element.style.display 	= "inline";
	myPanel.setBody(element);
	myPanel.show();
}
function hideExport() {
	myPanel.hide();
}

function doExport(){
	var options = "?";
	options += "typeOpt=" + getOptionChecked("export_type_");
	options += "&summaryOpt=" + getOptionChecked("export_summary_");
	options += "&ODAGrowthOpt=" + getOptionChecked("export_ODAGrowth_");
	options += "&fundingOpt=" + getOptionChecked("export_funding_");
	options += "&aidPredicOpt=" + getOptionChecked("export_aid_pred_");
	options += "&aidTypeOpt=" + getOptionChecked("export_aid_type_");
	options += "&financingInstOpt=" + getOptionChecked("export_fin_inst_");
	options += "&donorOpt=" + getOptionChecked("export_donor_");
	options += "&sectorOpt=" + getOptionChecked("export_sector_");
	options += "&regionOpt=" + getOptionChecked("export_region_");
	var type = "" + getOptionChecked("export_type_");
	if (type=="0") {
		<digi:context name="url1" property="/visualization/pdfExport.do"/>
		document.visualizationform.action="${url1}" + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	} 
	if (type=="1") {
		<digi:context name="url2" property="/visualization/wordExport.do"/>
		document.visualizationform.action="${url2}" + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	}
	if (type=="2") {
		<digi:context name="url3" property="/visualization/excelExport.do"/>
		document.visualizationform.action="${url3}" + options ;
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
	loadingPanel.show();
	var trnAll="<digi:trn jsFriendly='true'>All</digi:trn>";
	
	unCheckOptions("org_grp_check");
	unCheckOptions("region_check");
	unCheckOptions("sector_config_check");
	unCheckOptions("sector_check");
	unCheckOptions("organization_check");
	unCheckOptions("zone_check");
	unCheckOptions("sub_sector_check");
	
	document.getElementById("decimalsToShow_dropdown").selectedIndex = 2;
	document.getElementById("yearsInRange_dropdown").selectedIndex = 4;
	
	document.getElementById("commitments_visible").checked = true;
	document.getElementById("disbursements_visible").checked = true;
	document.getElementById("expenditures_visible").checked = true;
	if (document.getElementById("pledge_visible")!=null){
		document.getElementById("pledge_visible").checked = true;
	}
	if (document.getElementById("workspace_only")!=null){
		document.getElementById("workspace_only").checked = false;
	}
	
	
	document.getElementById("transaction_type_0").value = true;
	document.getElementById("transaction_type_1").value = false;
	document.getElementById("transaction_type_2").value = false;
	document.getElementById("org_group_dropdown_id").selectedIndex = 0;
	document.getElementById("region_dropdown_id").selectedIndex = 0;
	document.getElementById("sector_dropdown_id").selectedIndex = 0;
	document.getElementById("sector_config_dropdown_id").selectedIndex = 0;
	removeOptionsDropdown("org_dropdown_id");
	removeOptionsDropdown("zone_dropdown_id");
	removeOptionsDropdown("sector_dropdown_id");
	removeOptionsDropdown("sub_sector_dropdown_id");
	document.getElementById("filterOrganizations").innerHTML = trnAll;
	document.getElementById("filterOrgGroups").innerHTML = trnAll;
	document.getElementById("filterSectors").innerHTML = trnAll;
	document.getElementById("filterSectorConfiguration").innerHTML = "<digi:trn jsFriendly='true'>Primary</digi:trn>";;
	document.getElementById("filterRegions").innerHTML = trnAll;
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
	document.getElementById("selGeneral").className = "";
	document.getElementById("selOrgs").className = "";
	document.getElementById("selRegions").className = "";
	document.getElementById("selSectors").className = "";

	document.getElementById("generalTab").style.display = "none";
	document.getElementById("organizationsTab").style.display = "none";
	document.getElementById("regionsTab").style.display = "none";
	document.getElementById("sectorsTab").style.display = "none";
	clearAllLocalSearchResults();
	
	switch (selected) {
	case 0:
		document.getElementById("selGeneral").className = "selected";
		document.getElementById("generalTab").style.display = "block";
		break;
	case 1:
		document.getElementById("selOrgs").className = "selected";
		document.getElementById("organizationsTab").style.display = "block";
		break;
	case 2:
		document.getElementById("selRegions").className = "selected";
		document.getElementById("regionsTab").style.display = "block";
		break;
	case 3:
		document.getElementById("selSectors").className = "selected";
		document.getElementById("sectorsTab").style.display = "block";
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
	var trnShowFilterSetttings="<digi:trn jsFriendly='true'>Show filter settings</digi:trn>"; 
	var trnHideFilterSetttings="<digi:trn jsFriendly='true'>Hide filter settings</digi:trn>"; 
	
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
-->
</script>
<table>
<tr>
<td>
<div id="dialog2" class="dialog" title="Advanced Filters">
<div id="popinContent" class="content">
	<c:set var="selectorHeaderSize" scope="page" value="11" />
	<div id="tabview_container" class="yui-navset" style="display: block; overflow: hidden; height: 80%; padding-bottom: 0px;margin-top: 15px;margin-left: 5px;margin-right: 5px">
	<ul class="yui-nav" style="border-bottom: 1px solid #CCCCCC">
		<li id="selGeneral" class="selected"><a href="javascript:changeTab(0)"><div><digi:trn>General</digi:trn></div></a></li>
		<li id="selOrgs"><a href="javascript:changeTab(1)"><div><digi:trn>Organizations</digi:trn></div></a></li>
		<li id="selRegions"><a href="javascript:changeTab(2)"><div><digi:trn>Regions</digi:trn></div></a></li>
		<li id="selSectors"><a href="javascript:changeTab(3)"><div><digi:trn>Sectors</digi:trn></div></a></li>
	</ul>
	<div class="yui-content" style="background-color: #f6faff; height: 92%;margin-top: 10px;" >
		<div id="generalTab" style="height: 91%;">
			<div class="grayBorder">
				<div class="grouping_selector_wrapper" style="float: left; width: 40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 20; border: 1px solid #CCCCCC;border-bottom: 0px;">
						<div class="inside">
							<b class="ins_header"><digi:trn>Grouping Selector</digi:trn></b> 
						</div>
					</div>
					<div style="height: 180;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding:2px; ">		
						<table style="width: 95%;margin-top: 15px;" align="center" class="inside" >
							<tr style="cursor: pointer;" >
								<td class="side_opt_sel" id="general_selector">
									<div class="selector_type_cont">
										<digi:trn>General</digi:trn>
									</div>
								</td>
								
							</tr>
						</table>
					</div>
				</div>
				<div class="member_selector_wrapper" style="margin-left:40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 20;border: 1px solid #CCCCCC;border-bottom: 0px;">
							<div class="inside" style="float: left" >&nbsp;
								<b class="ins_header">
									<digi:trn>Options Selector</digi:trn>
								</b>
							</div>
					</div>
					<div style="height: 145;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 145; padding:20px; " id="generalDivList">
						<c:if test="${!visualizationform.filter.fromPublicView}">
							<html:checkbox  property="filter.workspaceOnly" styleId="workspace_only"><digi:trn>Show Only Data From This Workspace</digi:trn></html:checkbox> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
						</c:if>
						<hr />
						<br />
						<digi:trn>For Time Series Comparison, what data do you want to show</digi:trn>? <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
						<html:checkbox  property="filter.commitmentsVisible" styleId="commitments_visible"><digi:trn>Commitments</digi:trn>&nbsp;&nbsp;</html:checkbox><br />
						<html:checkbox  property="filter.disbursementsVisible" styleId="disbursements_visible"><digi:trn>Disbursements</digi:trn>&nbsp;&nbsp;</html:checkbox><br />
						<feature:display module="Funding" name="Expenditures">
							<html:checkbox  property="filter.expendituresVisible" styleId="expenditures_visible"><digi:trn>Expenditures</digi:trn>&nbsp;&nbsp;</html:checkbox><br />
						</feature:display> 
						<module:display name="Pledges" parentModule="PROJECT MANAGEMENT">
							<html:checkbox  property="filter.pledgeVisible" styleId="pledge_visible"><digi:trn>Pledges</digi:trn>&nbsp;&nbsp;</html:checkbox><br />
						</module:display>
						<hr />
						<br />
						<digi:trn>What data should the dashboard show</digi:trn>?<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
                            <html:radio property="filter.transactionType" styleId="transaction_type_0" value="0"><digi:trn>Commitments</digi:trn></html:radio><br />
                            <html:radio property="filter.transactionType" styleId="transaction_type_1" value="1"><digi:trn>Disbursements</digi:trn></html:radio><br />
                            <feature:display module="Funding" name="Expenditures">
                            	<html:radio property="filter.transactionType" styleId="transaction_type_2" value="2"><digi:trn>Expenditures</digi:trn></html:radio><br />
                            </feature:display>
						<hr />
					</div>
				</div>
			</div>
			<div>
				<table border="0" cellspacing="3" cellpadding="3">
					<tr>
					  	<td><b><digi:trn>Currency Type</digi:trn>:</b></td>
					  	<td>
					  		<html:select property="filter.currencyId" styleId="currencies_dropdown_ids" styleClass="dropdwn_sm" style="width:150px;">
   								<html:optionsCollection property="filter.currencies" value="ampCurrencyId" label="currencyName" />
							</html:select> 	
						</td>
						<td><b><digi:trn>Decimals to show</digi:trn>:</b></td>
					    <td>
							<html:select property="filter.decimalsToShow" styleId="decimalsToShow_dropdown" styleClass="dropdwn_sm" style="width:70px;">
	                            <html:option value="0">0</html:option>
	                            <html:option value="1">1</html:option>
	                            <html:option value="2">2</html:option>
	                            <html:option value="3">3</html:option>
	                            <html:option value="4">4</html:option>
	                            <html:option value="5">5</html:option>
	                        </html:select>
						</td>
				 	</tr>
				    <tr>
				    	<td><b><digi:trn>Fiscal Calendar</digi:trn>:</b></td>
					    <td>
					    	 <html:select property="filter.fiscalCalendarId" styleId="fiscalCalendar_dropdown_Id" styleClass="dropdwn_sm" style="width:150px;">
	                            <html:option value="-1"><digi:trn>None</digi:trn></html:option>
	                            <html:optionsCollection property="filter.fiscalCalendars" label="name" value="ampFiscalCalId" />
	                        </html:select>
						</td>
				  		<td><b><digi:trn>Years range</digi:trn>:</b></td>
					    <td>
							<html:select property="filter.yearsInRange" styleId="yearsInRange_dropdown" styleClass="dropdwn_sm" style="width:70px;">
	                            <html:option value="1">1</html:option>
	                            <html:option value="2">2</html:option>
	                            <html:option value="3">3</html:option>
	                            <html:option value="4">4</html:option>
	                            <html:option value="5">5</html:option>
	                        </html:select>
						</td>
						<td><b><digi:trn>Year To Compare Growth</digi:trn>:</b></td>
				    	<td>
				    		 <html:select property="filter.yearToCompare" styleId="yearToCompare_dropdown" styleClass="dropdwn_sm" style="width:70px;">
	                            <html:optionsCollection property="filter.years" label="wrappedInstance" value="wrappedInstance" />
	                        </html:select>
						</td>
					</tr>
					<tr>
						<td><b><digi:trn>Fiscal Year Start</digi:trn>:</b></td>
				    	<td>
				    		 <html:select property="filter.year" styleId="year_dropdown" styleClass="dropdwn_sm" style="width:70px;">
	                            <html:optionsCollection property="filter.years" label="wrappedInstance" value="wrappedInstance" />
	                        </html:select>
						</td>
						<td><b><digi:trn>Years range for linechart</digi:trn>:</b></td>
					    <td>
							<html:select property="filter.yearsInRangeLine" styleId="yearsInRangeLine_dropdown" styleClass="dropdwn_sm" style="width:70px;">
	                            <html:option value="1">1</html:option>
	                            <html:option value="2">2</html:option>
	                            <html:option value="3">3</html:option>
	                            <html:option value="4">4</html:option>
	                            <html:option value="5">5</html:option>
	                            <html:option value="6">6</html:option>
	                            <html:option value="7">7</html:option>
	                            <html:option value="8">8</html:option>
	                        </html:select>
						</td>
						<td><b><digi:trn>Years range for piechart</digi:trn>:</b></td>
					    <td>
							<html:select property="filter.yearsInRangePie" styleId="yearsInRangePie_dropdown" styleClass="dropdwn_sm" style="width:70px;">
	                            <html:option value="1">1</html:option>
	                            <html:option value="2">2</html:option>
	                            <html:option value="3">3</html:option>
	                            <html:option value="4">4</html:option>
	                            <html:option value="5">5</html:option>
	                            <html:option value="6">6</html:option>
	                            <html:option value="7">7</html:option>
	                        </html:select>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="organizationsTab" style="height: 91%;">
			<div class="grayBorder">
				<div class="grouping_selector_wrapper" style="float: left; width: 40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 25; border: 1px solid #CCCCCC;border-bottom: 0px;">
						<div class="inside">
							<b class="ins_header"><digi:trn>Grouping Selector</digi:trn></b> 
						</div>
					</div>
					<div style="height: 180;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding:2px; ">		
						<table style="width: 95%;margin-top: 15px;" align="center" class="inside" >
							<tr style="cursor: pointer;" >
								<td class="side_opt_sel"  id="org_grp_selector">
									<div class="selector_type_cont">
										<digi:trn>Organization Groups With Organizations</digi:trn>
									</div>
								</td>
								
							</tr>
						</table>
					</div>
				</div>
				<div class="member_selector_wrapper" style="margin-left:40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 25;border: 1px solid #CCCCCC;border-bottom: 0px;">
							<div class="inside" style="float: left" >&nbsp;
								<b class="ins_header">
									<digi:trn>Member Selector</digi:trn>
								</b>
							</div>
								<div style="float: right">
							<input onkeypress="clearSearch('orgGrpDivList')" id="orgGrpDivList_search" type="text"   class="inputx" />
									 <input type="button" class="buttonx" onclick="findPrev('orgGrpDivList')"  value='&lt;&lt;' />
									 <input type="button" onclick="findNext('orgGrpDivList')"  class="buttonx" value="&gt;&gt;"/>
							</div>
							
					</div>
					<div style="height: 145;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 145; padding:20px; " id="orgGrpDivList">
						<ul style="list-style-type: none">
						<li>
							<c:if test="${visualizationform.filter.dashboardType eq '1' }">
								<input type="radio"  value="-1" id="org_grp_check_all" name="org_grp_check" onClick="uncheckAllRelatedEntities('organization_check')"/> 
							</c:if>
							<c:if test="${visualizationform.filter.dashboardType ne '1' }">
								<input type="checkbox" id="org_grp_check_all"  value="-1" name="org_grp_check" onClick="allOptionChecked(this,'org_grp_check','organization_check')"/> 
							</c:if>
							<span><digi:trn>All</digi:trn></span>
						</li>
						<c:forEach items="${visualizationform.filter.orgGroupWithOrgsList}" var="item">
						<li>
							<c:if test="${visualizationform.filter.dashboardType eq '1' }">
								<input type="radio" name="org_grp_check" title="${item.mainEntity.orgGrpName}" value="${item.mainEntity.ampOrgGrpId}" onClick="checkUncheckRelatedEntities(this,'organization_check',${item.mainEntity.ampOrgGrpId})"/> 
							</c:if>
							<c:if test="${visualizationform.filter.dashboardType ne '1' }">
								<input type="checkbox" name="org_grp_check" title="${item.mainEntity.orgGrpName}" value="${item.mainEntity.ampOrgGrpId}" onClick="uncheckAllOption('org_grp_check');checkRelatedEntities(this,'organization_check',${item.mainEntity.ampOrgGrpId})"/> 
							</c:if>
								<span><digi:trn>${item.mainEntity.orgGrpName}</digi:trn></span>
							<br/>
							<ul style="list-style-type: none">
							<c:forEach items="${item.subordinateEntityList}" var="organization">
							<li><input type="checkbox" class="organization_check_${item.mainEntity.ampOrgGrpId}" name="organization_check" title="${organization.name}" value="${organization.ampOrgId}" onclick="uncheckAllOption('org_grp_check');"/>
							<span>${organization.name}</span>
							</li> 
							</c:forEach>
							</ul>
						</li>
						</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div id="regionsTab" style="height: 91%;">
			<div class="grayBorder">
				<div class="grouping_selector_wrapper" style="float: left; width: 40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 25; border: 1px solid #CCCCCC;border-bottom: 0px;">
						<div class="inside">
							<b class="ins_header"><digi:trn>Grouping Selector</digi:trn></b> 
						</div>
					</div>
					<div style="height: 180;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding:2px; ">		
						<table style="width: 95%;margin-top: 15px;" align="center" class="inside" >
							<tr style="cursor: pointer;" >
								<td class="side_opt_sel"  id="region_selector">
									<div class="selector_type_cont">
										<digi:trn>Regions With Zones</digi:trn>
									</div>
								</td>
								
							</tr>
						</table>
					</div>
				</div>
				<div class="member_selector_wrapper" style="margin-left:40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 25;border: 1px solid #CCCCCC;border-bottom: 0px;">
							<div class="inside" style="float: left" >&nbsp;
								<b class="ins_header">
									<digi:trn>Member Selector</digi:trn>
								</b>
									
							</div>
							<div class="inside" style="float: right">
							<input onkeypress="clearSearch('regionDivList')" id="regionDivList_search" type="text"   class="inputx" />
									 <input type="button" class="buttonx" onclick="findPrev('regionDivList')"  value='&lt;&lt;' />
									 <input type="button" onclick="findNext('regionDivList')"  class="buttonx" value="&gt;&gt;"/>
							</div>
					</div>
					<div style="height: 145;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 145; padding:20px; " id="regionDivList">
						<ul style="list-style-type: none">
						<li>
							<c:if test="${visualizationform.filter.dashboardType eq '2' }">
								<input type="radio" id="region_check_all" name="region_check" value="-1"  onClick="uncheckAllRelatedEntities('zone_check')"/> 
							</c:if>
							<c:if test="${visualizationform.filter.dashboardType ne '2' }">
								<input type="checkbox" id="region_check_all" name="region_check" value="-1" onClick="allOptionChecked(this,'region_check','zone_check')"/> 
							</c:if>
							<span><digi:trn>All</digi:trn></span>
						</li>
						<c:forEach items="${visualizationform.filter.regionWithZones}" var="item">
						<li>
							<c:if test="${visualizationform.filter.dashboardType eq '2' }">
								<input type="radio" name="region_check" title="${item.mainEntity.name}" value="${item.mainEntity.id}" onClick="checkUncheckRelatedEntities(this,'zone_check',${item.mainEntity.id})"/> 
							</c:if>
							<c:if test="${visualizationform.filter.dashboardType ne '2' }">
								<input type="checkbox" name="region_check" title="${item.mainEntity.name}" value="${item.mainEntity.id}" onClick="uncheckAllOption('region_check');checkRelatedEntities(this,'zone_check',${item.mainEntity.id})"> 
							</c:if>
							<span>
								<digi:trn>${item.mainEntity.name}</digi:trn>
							</span>
							<br/>
							<ul style="list-style-type: none">
							<c:forEach items="${item.subordinateEntityList}" var="zone">
							<li><input type="checkbox" class="zone_check_${item.mainEntity.id}" name="zone_check" title="${zone.name}" value="${zone.id}" onclick="uncheckAllOption('region_check');"/><span>${zone.name}</span></li> 
							</c:forEach>
							</ul>
						</li>
						</c:forEach>
						</ul>
					</div>
				
				</div>
			</div>
		</div>
		<div id="sectorsTab" style="height: 91%;" >
			<div class="grayBorder">
				<div class="grouping_selector_wrapper" style="float: left; width: 40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 25; border: 1px solid #CCCCCC;border-bottom: 0px;">
						<div class="inside">
							<b class="ins_header"><digi:trn>Grouping Selector</digi:trn></b> 
						</div>
					</div>
					<div style="height: 180;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding:2px; ">		
						<table style="width: 95%;margin-top: 15px;" align="center" class="inside" >
							<tr style="cursor: pointer;" >
								<td class="side_opt_sel"  id="sector_selector">
									<div class="selector_type_cont">
										<digi:trn>Sectors and Sub Sectors</digi:trn>
									</div>
								</td>
								
							</tr>
						</table>
					</div>
				</div>
				<div class="member_selector_wrapper" style="margin-left:40%; padding: 0px; height: 98%;">
					<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding:2px; height: 25;border: 1px solid #CCCCCC;border-bottom: 0px;">
							<div class="inside" style="float: left" >&nbsp;
								<b class="ins_header">
									<digi:trn>Member Selector</digi:trn>
								</b>
							</div>
							<div class="inside" style="float: right" >
								<input onkeypress="clearSearch('sectorDivList')" id="sectorDivList_search" type="text"   class="inputx" />
									 <input type="button" class="buttonx" onclick="findPrev('sectorDivList')"  value='&lt;&lt;' />
									 <input type="button" onclick="findNext('sectorDivList')"  class="buttonx" value="&gt;&gt;"/>
								
							</div>
							
					</div>
					<div style="height: 145;  border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 145; padding:20px; " id="sectorDivList">
						<ul style="list-style-type: none">
						<c:forEach items="${visualizationform.filter.configWithSectorAndSubSectors}" var="item" >
						<c:set var="item" scope="request" value="${item}"/>
						<c:choose>
						<c:when test="${item.mainEntity.name=='Primary'}">
							<field:display name="Primary Sector" feature="Sectors">
							<jsp:include page="sectorPopinHelper.jsp" flush="true" />
						</field:display>
						</c:when>
						<c:when test="${item.mainEntity.name=='Secondary'}">
							<field:display name="Secondary Sector" feature="Sectors">
							<jsp:include page="sectorPopinHelper.jsp" />
						</field:display>
						</c:when>
						<c:when test="${item.mainEntity.name=='Tertiary'}">
						<field:display name="Tertiary Sector" feature="Sectors">
							<jsp:include page="sectorPopinHelper.jsp" />
						</field:display>
						</c:when>
						</c:choose>
						</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>

<input type="button" value="<digi:trn>Apply</digi:trn>" class="buttonx" style="margin-right:10px; margin-top:10px;" id="applyButtonPopin">
<input type="button" value="<digi:trn>Reset to defaults</digi:trn>" onclick="resetToDefaults()" class="buttonx" style="margin-right:10px; margin-top:10px;">
<input type="button" value="<digi:trn>Close</digi:trn>" class="buttonx" onclick="hidePopin()" style="margin-right:10px; margin-top:10px;">


</div>
</td>
</tr>
</table>
<!-- POPUPS END -->

<table>
<tr>
<td>
<div id="exportPopin" class="dialog" title="Export Options">
<div id="popinContent2" class="content">
	<div id="exportDiv" class="yui-navset">
		<table width="100%" height=400 cellpadding="0" cellspacing="0">
			<tr>
				<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Export Type</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.typeOpt" styleId="export_type_0" value="0" ><digi:trn>PDF</digi:trn>  </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_pdf.gif"><br />
		            <html:radio property="exportData.typeOpt" styleId="export_type_1" value="1"><digi:trn>Word</digi:trn>   </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_word.gif"><br />
		        	<html:radio property="exportData.typeOpt" styleId="export_type_2" value="2"><digi:trn>Excel</digi:trn>   </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_exc.gif"><br />
		        </div>
		        </td>
				<c:if test="${visualizationform.filter.dashboardType eq '1' }">
			        <td class="inside" width="30%" >
					<div class="selector_type"><b><digi:trn>ODA Growth</digi:trn></b></div>
					<div>
			            <html:radio property="exportData.ODAGrowthOpt" styleId="export_ODAGrowth_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
			            <html:radio property="exportData.ODAGrowthOpt" styleId="export_ODAGrowth_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
			             <html:radio property="exportData.ODAGrowthOpt" styleId="export_ODAGrowth_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
			            <html:radio property="exportData.ODAGrowthOpt" styleId="export_ODAGrowth_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
			        </div>
			        </td>
		    	</c:if>
				<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Summary</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.summaryOpt" styleId="export_summary_0" value="0"><digi:trn>Exclude Summary</digi:trn></html:radio><br />
		            <html:radio property="exportData.summaryOpt" styleId="export_summary_1" value="1"><digi:trn>Inculde Summary</digi:trn></html:radio><br />
		        </div>
		        </td>
		    </tr>
		    <tr>
				<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Funding</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		        <td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Aid Predictability</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		    	<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Aid Type</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		    </tr>
		    <tr>
				<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Financing Instrument</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		    	<c:if test="${visualizationform.filter.dashboardType ne '1' }">
    			<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Donor Profile</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.donorOpt" styleId="export_donor_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.donorOpt" styleId="export_donor_2" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.donorOpt" styleId="export_donor_1" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.donorOpt" styleId="export_donor_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		        </c:if>
		        <c:if test="${visualizationform.filter.dashboardType ne '3' }">
    			<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Sector</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.sectorOpt" styleId="export_sector_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.sectorOpt" styleId="export_sector_2" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.sectorOpt" styleId="export_sector_1" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.sectorOpt" styleId="export_sector_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		        </c:if>
		        <c:if test="${visualizationform.filter.dashboardType ne '2' }">
    			<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Region</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.regionOpt" styleId="export_region_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.regionOpt" styleId="export_region_2" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.regionOpt" styleId="export_region_1" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.regionOpt" styleId="export_region_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		        </c:if>
		    </tr>
		</table>
	</div>
	</div>

        <input type="button" value="<digi:trn>Export</digi:trn>" class="buttonx" onclick="doExport()" style="margin-right:10px; margin-top:10px;">
        <input type="button" value="<digi:trn>Close</digi:trn>" class="buttonx" onclick="hideExport()" style="margin-right:10px; margin-top:10px;">
		        
</div>
</td>
</tr>
</table>

<table>
<tr>
<td>
<div id="listPopin" class="dialog" title="List of Activities">
	<div id="popinContent3" class="dash_left"  style="max-height: 500px; overflow: auto;">
		
	</div>
</div>
</td>
</tr>
</table>


<!-- MAIN CONTENT PART START -->

<html:hidden property="filter.decimalsToShow" styleId="decimalsToShow" />
<html:hidden property="filter.year" styleId="currentYear"/>
<html:hidden property="filter.yearToCompare" styleId="yearToCompare"/>
<html:hidden property="filter.yearsInRange" styleId="yearsInRange" />
<html:hidden property="filter.yearsInRangeLine" styleId="yearsInRangeLine" />
<html:hidden property="filter.yearsInRangePie" styleId="yearsInRangePie" />
<html:hidden property="filter.dashboardType" styleId="dashboardType" />
<html:hidden property="filter.workspaceOnly" styleId="workspaceOnly"/>
<html:hidden property="filter.showMonochrome" styleId="showMonochrome"/>
<html:hidden property="filter.commitmentsVisible" styleId="commitmentsVisible"/>
<html:hidden property="filter.disbursementsVisible" styleId="disbursementsVisible" />
<html:hidden property="filter.expendituresVisible" styleId="expendituresVisible" />
<html:hidden property="filter.pledgeVisible" styleId="pledgeVisible"/>
<html:hidden property="filter.transactionType" styleId="transactionType" />
<html:hidden property="filter.currencyId" styleId="currencyId" />
<html:hidden property="filter.fiscalCalendarId" styleId="fiscalCalendarId" />
<html:hidden property="filter.groupSeparator" styleId="groupSeparator" />
<html:hidden property="filter.decimalSeparator" styleId="decimalSeparator" />

<div class="dashboard_header">
<!--<div class="dashboard_total"><b class="dashboard_total_num">${visualizationform.summaryInformation.totalCommitments}</b><br /><digi:trn>Total Commitments</digi:trn> ( ${visualizationform.filter.currencyId} )</div>-->
<div class="dashboard_total" id="divTotalComms"></div>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    	<div class="dashboard_name" id="dashboard_name">
    		<!--<c:if test="${visualizationform.filter.dashboardType eq '1' }">
    			<digi:trn>ALL DONORS</digi:trn>
    		</c:if>
    		<c:if test="${visualizationform.filter.dashboardType eq '3' }">
    			<digi:trn>ALL SECTORS</digi:trn>
    		</c:if>
    		<c:if test="${visualizationform.filter.dashboardType eq '2' }">
    			<digi:trn>ALL REGIONS</digi:trn>
    		</c:if>
    	--></div>
    <td>
    	<table>
    		<tr>
    			<td><div class="dash_ico"><img src="/TEMPLATE/ampTemplate/img_2/ico_export.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href="javascript:showExport()" class="l_sm"><digi:trn>Export Options</digi:trn></a></div></div></td>
    		</tr>
    		<tr>
    			<td><div id="currencyInfo"></div></td>
    		</tr>
    	</table>
   	</td>
  </tr>
</table>
<div class="dashboard_stat" id="divSummaryInfo" ></div>
<div class="dashboard_stat" align="right" ><a onClick="toggleSettings();" id="displaySettingsButton"><digi:trn>Show filter settings</digi:trn></a></div>
<div class="dashboard_stat" style="display:none; padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr style="background-color:white;" >
	<td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
	<strong>
	<digi:trn>Selected Filters</digi:trn>:</strong>
	   <i><digi:trn>Currency type</digi:trn>: </i><label id="filterCurrency"></label> | 
	   <i><digi:trn>Fiscal start year</digi:trn>: </i><label id="filterFiscalYear">${visualizationform.filter.year}</label> | 
	   <i><digi:trn>Years range</digi:trn>: </i><label id="filterYearsRange">${visualizationform.filter.yearsInRange}</label> | 
	   <i><digi:trn>Org. groups</digi:trn>: </i><label id="filterOrgGroups"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Organizations</digi:trn>: </i><label id="filterOrganizations"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Configuration</digi:trn>: </i><label id="filterSectorConfiguration"><digi:trn>Primary</digi:trn></label> | 
	   <i><digi:trn>Sectors</digi:trn>: </i><label id="filterSectors"><digi:trn>All</digi:trn></label> |
	   <i><digi:trn>Regions</digi:trn>: </i><label id="filterRegions"><digi:trn>All</digi:trn></label>
	</td>
	</tr>
	<tr>
	</tr>
	</table>
</div>
<!--<div class="dashboard_stat">Total Disbursements: <div id="divTotalDisbs"></div> <span class="breadcrump_sep">|</span>Total Number of Projects: <div id="divNumOfProjs"></div><span class="breadcrump_sep">|</span>Total Number of Sectors: <div id="divNumOfSecs"></div><span class="breadcrump_sep">|</span>Total Number of Regions: <div id="divNumOfRegs"></div><span class="breadcrump_sep">|</span>Average Project Size: <div id="divAvgProjSize"></div></div>-->

</div>


<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:15px;">
  <tr>
    <td width=296 bgcolor="#F4F4F4" valign="top">
	<div style="background-color:#FFFFFF; height:7px;">&nbsp;</div>
	<div class="dash_left">
	<fieldset>
	<legend><span class=legend_label><digi:trn>Quick Filter</digi:trn></span></legend>
<!--	<html:checkbox property="filter.workspaceOnly" styleId="workspace_only"><digi:trn>Show Only Data From This Workspace</digi:trn></html:checkbox>-->
	<hr />
	<html:checkbox  property="filter.showMonochrome" styleId="show_monochrome" onclick="reloadGraphs();"><digi:trn>Show Monochrome</digi:trn></html:checkbox> <img title="<digi:trn>Show all charts in grayscale</digi:trn>" src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
	<hr />
	<table cellspacing="0" cellpadding="0" width="100%"> 
	<c:if test="${visualizationform.filter.dashboardType eq '1' }">
		<tr>
		  <td><digi:trn>Organization Group</digi:trn>:</td>
		  	<td align=right>
		     <html:select property="filter.orgGroupId" styleId="org_group_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
		         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
		         <html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
		     </html:select>
		     <div id="org_group_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
			</td>
		</tr>
		<tr>
			<td><digi:trn>Organization</digi:trn>:</td>
			<td align=right>
			   <html:select property="filter.orgId" styleId="org_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
			       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
			   </html:select>
			   <div id="org_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
			</td>
		</tr>
	</c:if>
	<c:if test="${visualizationform.filter.dashboardType eq '2' }">
	  <tr>
		<td><digi:trn>Region</digi:trn>:</td>
		<td align=right>
		   <html:select property="filter.regionId" styleId="region_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
		       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
		       <html:optionsCollection property="filter.regions" value="id" label="name" />
		   </html:select>
		   <div id="region_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	 </tr> 
	 <tr>
	  	<td><digi:trn>Zone</digi:trn>:</td>
	   	<td align=right>
	      <html:select property="filter.zoneId" styleId="zone_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	          <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	      </html:select>
	      <div id="zone_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr> 
  </c:if>
  <c:if test="${visualizationform.filter.dashboardType eq '3' }">
  <tr>
	<td><digi:trn>Configurations</digi:trn>:</td>
	  <td align="right">
	  <html:select property="filter.selSectorConfigId" styleId="sector_config_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	  <c:forEach var="config" items="${visualizationform.filter.sectorConfigs}">
		<c:choose>
						<c:when test="${config.name=='Primary'}">
							<field:display name="Primary Sector" feature="Sectors">
							         <html:option value="${config.id}"><digi:trn>${config.name}</digi:trn></html:option>
	     					 </field:display>
						</c:when>
						<c:when test="${config.name=='Secondary'}">
							<field:display name="Secondary Sector" feature="Sectors">
							    <html:option value="${config.id}"><digi:trn>${config.name}</digi:trn></html:option>
	     					 </field:display>
						</c:when>
						<c:when test="${config.name=='Tertiary'}">
							<field:display name="Tertiary Sector" feature="Sectors">
							    <html:option value="${config.id}"><digi:trn>${config.name}</digi:trn></html:option>
	     					 </field:display>
						</c:when>
		</c:choose>

        
	  </c:forEach>
	     </html:select>
	     <div id="sector_config_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
	<tr>
	<td><digi:trn>Sector</digi:trn>:</td>
	  <td align="right">
	  <html:select property="filter.sectorId" styleId="sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	         <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
	     </html:select>
	     <div id="sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
	<tr>
	   <td><digi:trn>Sub-Sector</digi:trn>:</td>
	  <td align=right>
	     <html:select property="filter.subSectorId" styleId="sub_sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	     </html:select>
	     <div id="sub_sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
  </c:if>
  
	<c:if test="${visualizationform.filter.dashboardType ne '1' }">
		<tr>
		  <td><digi:trn>Organization Group</digi:trn>:</td>
		  	<td align=right>
		     <html:select property="filter.orgGroupId" styleId="org_group_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
		         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
		         <html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
		     </html:select>
		     <div id="org_group_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
			</td>
		</tr>
		<tr>
			<td><digi:trn>Organization</digi:trn>:</td>
			<td align=right>
			   <html:select property="filter.orgId" styleId="org_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
			       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
			   </html:select>
			   <div id="org_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
			</td>
		</tr>
	</c:if>
	<c:if test="${visualizationform.filter.dashboardType ne '2' }">
	  <tr>
		<td><digi:trn>Region</digi:trn>:</td>
		<td align=right>
		   <html:select property="filter.regionId" styleId="region_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
		       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
		       <html:optionsCollection property="filter.regions" value="id" label="name" />
		   </html:select>
		   <div id="region_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	 </tr> 
	 <tr>
	  	<td><digi:trn>Zone</digi:trn>:</td>
	   	<td align=right>
	      <html:select property="filter.zoneId" styleId="zone_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	          <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	      </html:select>
	      <div id="zone_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr> 
  </c:if>
  <c:if test="${visualizationform.filter.dashboardType ne '3' }">
  <tr>
	<td><digi:trn>Configurations</digi:trn>:</td>
	  <td align="right">
	  <html:select property="filter.selSectorConfigId" styleId="sector_config_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <c:forEach var="config" items="${visualizationform.filter.sectorConfigs}">
		<c:choose>
						<c:when test="${config.name=='Primary'}">
							<field:display name="Primary Sector" feature="Sectors">
							         <html:option value="${config.id}"><digi:trn>${config.name}</digi:trn></html:option>
	     					 </field:display>
						</c:when>
						<c:when test="${config.name=='Secondary'}">
							<field:display name="Secondary Sector" feature="Sectors">
							    <html:option value="${config.id}"><digi:trn>${config.name}</digi:trn></html:option>
	     					 </field:display>
						</c:when>
						<c:when test="${config.name=='Tertiary'}">
							<field:display name="Tertiary Sector" feature="Sectors">
							    <html:option value="${config.id}"><digi:trn>${config.name}</digi:trn></html:option>
	     					 </field:display>
						</c:when>
		</c:choose>
		</c:forEach>
	     </html:select>
	     <div id="sector_config_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
	<tr>
	<td><digi:trn>Sector</digi:trn>:</td>
	  <td align="right">
	  <html:select property="filter.sectorId" styleId="sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	         <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
	     </html:select>
	     <div id="sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
	<tr>
	   <td><digi:trn>Sub-Sector</digi:trn>:</td>
	  <td align=right>
	     <html:select property="filter.subSectorId" styleId="sub_sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	     </html:select>
	     <div id="sub_sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
  </c:if>
  
 </table>

	<center>
	<input type="button" value="<digi:trn>Filter</digi:trn>" class="buttonx" style="margin-top:10px;" id="applyButton">
	<input type="button" value="<digi:trn>Reset</digi:trn>" onclick="resetToDefaults()" class="buttonx" style="margin-right:10px; margin-top:10px;">
	<hr />
	<div class="tab_opt"><div class="tab_opt_cont"><a href="javascript:showPopin()" class="l_sm"><digi:trn>Advanced Filters</digi:trn></a></div></div>
	</center>
</fieldset>

<fieldset>
	<legend><span class=legend_label><digi:trn>Quick Access</digi:trn></span></legend>
	<table cellspacing="0" cellpadding="0" width="100%"> 
		<tr>
			<td>
				<a href="javascript:document.getElementById('FundingChartTitleLegend').scrollIntoView(true);"><digi:trn>Funding Chart</digi:trn></a> - 
				<a href="javascript:document.getElementById('AidPredictabilityTitleLegend').scrollIntoView(true);"><digi:trn>Aid Predictability Chart</digi:trn></a> - 
				<a href="javascript:document.getElementById('AidTypeTitleLegend').scrollIntoView(true);"><digi:trn>Aid Type Chart</digi:trn></a> - 
				<a href="javascript:document.getElementById('FinancingInstrumentTitleLegend').scrollIntoView(true);"><digi:trn>Financing Instrument Chart</digi:trn></a> - 
				<c:if test="${visualizationform.filter.dashboardType ne '1' }">
					<a href="javascript:document.getElementById('DonorProfileTitleLegend').scrollIntoView(true);"><digi:trn>Donor Chart</digi:trn></a> - 
				</c:if>
				<c:if test="${visualizationform.filter.dashboardType ne '3' }">
					<a href="javascript:document.getElementById('SectorProfileTitleLegend').scrollIntoView(true);"><digi:trn>Sector Chart</digi:trn></a> - 
				</c:if>
				<c:if test="${visualizationform.filter.dashboardType ne '2' }">
					<a href="javascript:document.getElementById('RegionProfileTitleLegend').scrollIntoView(true);"><digi:trn>Region Chart</digi:trn></a> - 
				</c:if>
				
			</td>
		</tr>
	</table>
</fieldset>

<fieldset>
	<legend><span id="topProjectsTitle" class=legend_label></span></legend>
	<div id="divTopProjects" class="field_text">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.topProjects}" var="projectItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <a href="/aim/selectActivityTabs.do~ampActivityId=${projectItem.key.ampActivityId}">${projectItem.key}</a> <b>($<c:out value="${projectItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:showFullProjects()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
	</div>
	<div id="divFullProjects" class="field_text" style="display: none;">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.fullProjects}" var="projectItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <a href="/aim/selectActivityTabs.do~ampActivityId=${projectItem.key.ampActivityId}">${projectItem.key}</a>  <b>($<c:out value="${projectItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:hideFullProjects()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
	</div>
</fieldset>
<c:if test="${visualizationform.filter.dashboardType ne '1' }">
	<fieldset>
		<legend><span id="topDonorsTitle" class=legend_label></span></legend>
		<div id="divTopDonors" class="field_text">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.topDonors}" var="donorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:showFullDonors()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
		</div>
		<div id="divFullDonors" class="field_text" style="display: none;">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.fullDonors}" var="donorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:hideFullDonors()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
		</div>
	</fieldset>	
</c:if>
<c:if test="${visualizationform.filter.dashboardType ne '3' }">
	<fieldset>
		<legend><span id="topSectorsTitle" class=legend_label></span></legend>
		<div id="divTopSectors" class="field_text">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.topSectors}" var="sectorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:showFullSectors()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
		</div>
		<div id="divFullSectors" class="field_text" style="display: none;">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.fullSectors}" var="sectorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:hideFullSectors()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
		</div>
	</fieldset>	
</c:if>
<c:if test="${visualizationform.filter.dashboardType ne '2' }">
	<fieldset>
		<legend><span id="topRegionsTitle" class=legend_label></span></legend>
		<div id="divTopRegions" class="field_text">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.topRegions}" var="regionItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${regionItem.key}"/>  <b>($<c:out value="${regionItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:showFullRegions()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
		</div>
		<div id="divFullRegions" class="field_text" style="display: none;">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.fullRegions}" var="regionItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${regionItem.key}"/>  <b>($<c:out value="${regionItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:hideFullRegions()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
		</div>
	</fieldset>	
</c:if>
</div>
</td>
<td width=15>&nbsp;</td>
<td width=689 valign="top">
<table width="689" border="0" cellspacing="0" cellpadding="0" align="center">
<tr>
<td valign="top">

<!--  Start Global variables for all flash files -->
<input type="hidden" id="GlobalFontSize" value="11" />
<input type="hidden" id="GlobalFontFamily" value="Arial" />
<input type="hidden" id="GlobalFontWeight" value="bold" />
<input type="hidden" id="trnMessagePanel" value="<digi:trn jsFriendly='true'>Empty Dataset</digi:trn>" />
<input type="hidden" id="trnMessageEmpty" value="<digi:trn jsFriendly='true'>No data to show</digi:trn>" />
<input type="hidden" id="trnMessageLoadingPanel" value="<digi:trn jsFriendly='true'>Loading</digi:trn>" />
<input type="hidden" id="trnMessageLoading" value="<digi:trn jsFriendly='true'>Loading data...</digi:trn>" />
<!--  End Global variables for all flash files -->

<div id="demo" class="yui-navset">
	<ul class="yui-nav">
		<li><a href="#tab1"><div id="visualizationDiv"><digi:trn>Visualization</digi:trn></div></a></li>
		<c:if test="${visualizationform.filter.dashboardType eq '1' }">
		<li><a href="#tab2"><div><digi:trn>Contact Information</digi:trn></div></a></li>
		<c:if test="${!visualizationform.filter.fromPublicView}">
		<li><a href="#tab3"><div><digi:trn>Additional Notes</digi:trn></div></a></li>
		</c:if>
		</c:if>
	</ul>
	<div class="yui-content">
	<div id="tab1">
		<c:if test="${visualizationform.filter.dashboardType eq '2' }">
			<fieldset class="chartFieldset">
				<legend><span id="RegionProfileTitleLegend" class=legend_label></span></legend>
				<div id="RegionProfileHeader" class="chart_header" style="float:left">
				<digi:trn>Title</digi:trn> <input type="text" id="RegionProfileTitle" value="" size="50">
				<input type="hidden" id="RegionProfileShowFontFamily" value="Verdana"/>
				&nbsp;<digi:trn>Size</digi:trn>
				<select id="RegionProfileFontSize">
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
				</select>
				&nbsp;<input type="checkbox" id="RegionProfileBold"><label for="RegionProfileBold"><digi:trn>Bold</digi:trn></label><br/>
				<input type="checkbox" id="RegionProfileShowLegend" checked="checked"><label for="RegionProfileShowLegend"><digi:trn>Show legend</digi:trn></label>
				&nbsp;<input type="checkbox" id="RegionProfileDivide"><label for="RegionProfileDivide"><digi:trn>Divide by thousands</digi:trn></label>
				&nbsp;<input type="checkbox" id="RegionProfileShowDataLabel"><label for="RegionProfileShowDataLabel"><digi:trn>Show data label</digi:trn></label>
				&nbsp;<input type="checkbox" id="RegionProfileRotateDataLabel"><label for="RegionProfileRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
				<input type="hidden" id="RegionProfileDataAction" value="getRegionProfileGraphData" />
				<input type="hidden" id="RegionProfileDataField" value="region" />
				<input type="hidden" id="RegionProfileItemId" value="${visualizationform.filter.regionId}" />
				<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'RegionProfile')">
				</div>
				<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar_profile', 'RegionProfile', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', 'RegionProfile', true)" title="<digi:trn>Donut Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'RegionProfile', true)" title="<digi:trn>Line Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'RegionProfile', true)" title="<digi:trn>Data View</digi:trn>"/></div>
				<br />
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="RegionProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
		</c:if>
	
			<c:if test="${visualizationform.filter.dashboardType eq '3' }">
			<!-- Show the Sector breakdown or Sub-Sector breakdown if there is a selected Sector -->
			<fieldset class="chartFieldset">
				<legend><span id="SectorProfileTitleLegend" class=legend_label></span></legend>
				<div id="SectorProfileHeader" class="chart_header" style="float:left">
				<digi:trn>Title</digi:trn> <input type="text" id="SectorProfileTitle" value="" size="50">
				<input type="hidden" id="SectorProfileShowFontFamily" value="Verdana"/>
				&nbsp;<digi:trn>Size</digi:trn>
				<select id="SectorProfileFontSize">
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
				</select>
				&nbsp;<input type="checkbox" id="SectorProfileBold"><label for="SectorProfileBold"><digi:trn>Bold</digi:trn></label><br/>
				<input type="checkbox" id="SectorProfileShowLegend" checked="checked"><label for="SectorProfileShowLegend"><digi:trn>Show legend</digi:trn></label>
				&nbsp;<input type="checkbox" id="SectorProfileDivide"><label for="SectorProfileDivide"><digi:trn>Divide by thousands</digi:trn></label>
				&nbsp;<input type="checkbox" id="SectorProfileShowDataLabel"><label for="SectorProfileShowDataLabel"><digi:trn>Show data label</digi:trn></label>
				&nbsp;<input type="checkbox" id="SectorProfileRotateDataLabel"><label for="SectorProfileRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
				<input type="hidden" id="SectorProfileDataAction" value="getSectorProfileGraphData" />
				<input type="hidden" id="SectorProfileDataField" value="sector" />
				<input type="hidden" id="SectorProfileItemId" value="${visualizationform.filter.sectorId}" />
				<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'SectorProfile')">
				</div>
				<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar_profile', 'SectorProfile', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', 'SectorProfile', true)" title="<digi:trn>Donut Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'SectorProfile', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'SectorProfile', true)" title="<digi:trn>Data View</digi:trn>"/></div>
				<br />
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="SectorProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
		</c:if>
	
		<fieldset class="chartFieldset">
			<legend><span id="FundingChartTitleLegend" class=legend_label><digi:trn jsFriendly='true'>ODA historical trend</digi:trn></span></legend>
			<div id="FundingChartHeader" class="chart_header" style="float:left">
			<digi:trn>Title</digi:trn> <input type="text" id="FundingChartTitle" value="<digi:trn jsFriendly='true'>ODA historical trend</digi:trn>" size="50">
			<input type="hidden" id="FundingChartShowFontFamily" value="Verdana"/>
			&nbsp;<digi:trn>Size</digi:trn>
			<select id="FundingChartFontSize">
				<option value="12">12</option>
				<option value="13">13</option>
				<option value="14">14</option>
				<option value="15">15</option>
				<option value="16">16</option>
			</select>
			&nbsp;<input type="checkbox" id="FundingChartBold"><label for="FundingChartBold"><digi:trn>Bold</digi:trn></label><br/>
			<input type="checkbox" id="FundingChartShowLegend" checked="checked"><label for="FundingChartShowLegend"><digi:trn>Show legend</digi:trn></label>
			&nbsp;<input type="checkbox" id="FundingChartDivide"><label for="FundingChartDivide"><digi:trn>Divide by thousands</digi:trn></label>
			&nbsp;<input type="checkbox" id="FundingChartShowDataLabel"><label for="FundingChartShowDataLabel"><digi:trn>Show data label</digi:trn></label>
			&nbsp;<input type="checkbox" id="FundingChartRotateDataLabel"><label for="FundingChartRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
			<input type="hidden" id="FundingChartDataAction" value="getFundingsGraphData" />
			<input type="hidden" id="FundingChartDataField" value="fundingtype" />
			<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'FundingChart')">
			</div>
			<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar', 'FundingChart', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'FundingChart', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'FundingChart', true)" title="<digi:trn>Data View</digi:trn>"/></div>
			<br />
			<br />
			<br />
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="FundingChart">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
			<div align="right">
				<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
			</div> 
		</fieldset>
		<c:if test="${visualizationform.filter.dashboardType eq '1' }">
			<fieldset class="chartFieldset">
				<legend><span id="ODAGrowthTitleLegend" class=legend_label></span></legend>
				<div id="ODAGrowthHeader" class="chart_header" style="float:left">
				<digi:trn>Title</digi:trn> <input type="text" id="ODAGrowthTitle" value="" size="50">
				<input type="hidden" id="ODAGrowthShowFontFamily" value="Verdana"/>
				&nbsp;<digi:trn>Size</digi:trn>
				<select id="ODAGrowthFontSize">
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
				</select>
				&nbsp;<input type="checkbox" id="ODAGrowthBold"><label for="ODAGrowthBold"><digi:trn>Bold</digi:trn></label><br/>
<!--				<span style="display:none">-->
<!--				<input type="checkbox" id="ODAGrowthShowLegend" checked="checked"><label for="ODAGrowthShowLegend"><digi:trn>Show legend</digi:trn></label>-->
<!--				&nbsp;<input type="checkbox" id="ODAGrowthDivide"><label for="ODAGrowthDivide"><digi:trn>Divide by thousands</digi:trn></label>-->
				&nbsp;<input type="checkbox" id="ODAGrowthShowDataLabel" checked="checked"><label for="ODAGrowthShowDataLabel"><digi:trn>Show data label</digi:trn></label>
<!--				&nbsp;<input type="checkbox" id="ODAGrowthRotateDataLabel"><label for="ODAGrowthRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label>-->
<!--				</span>-->
				</br>
				<input type="hidden" id="ODAGrowthDataAction" value="getODAGrowthGraphData" />
				<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'ODAGrowth')">
				</div>
				<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar', 'ODAGrowth')" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'ODAGrowth', true)" title="<digi:trn>Data View</digi:trn>"/></div>
				<br />
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="ODAGrowth">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
		</c:if>
		<fieldset class="chartFieldset">
			<legend><span id="AidPredictabilityTitleLegend" class=legend_label></span></legend>
			<div id="AidPredictabilityHeader" class="chart_header" style="float:left">
			<digi:trn>Title</digi:trn> <input type="text" id="AidPredictabilityTitle" value="" size="50">
			<input type="hidden" id="AidPredictabilityShowFontFamily" value="Verdana"/>
			&nbsp;<digi:trn>Size</digi:trn>
			<select id="AidPredictabilityFontSize">
				<option value="12">12</option>
				<option value="13">13</option>
				<option value="14">14</option>
				<option value="15">15</option>
				<option value="16">16</option>
			</select>
			&nbsp;<input type="checkbox" id="AidPredictabilityBold"><label for="AidPredictabilityBold"><digi:trn>Bold</digi:trn></label><br/>
			<input type="checkbox" id="AidPredictabilityShowLegend" checked="checked"><label for="AidPredictabilityShowLegend"><digi:trn>Show legend</digi:trn></label>
			&nbsp;<input type="checkbox" id="AidPredictabilityDivide"><label for="AidPredictabilityDivide"><digi:trn>Divide by thousands</digi:trn></label>
			&nbsp;<input type="checkbox" id="AidPredictabilityShowDataLabel"><label for="AidPredictabilityShowDataLabel"><digi:trn>Show data label</digi:trn></label>
			&nbsp;<input type="checkbox" id="AidPredictabilityRotateDataLabel"><label for="AidPredictabilityRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
			<input type="hidden" id="AidPredictabilityDataAction" value="getAidPredictabilityGraphData" />
			<input type="hidden" id="AidPredictabilityDataField" value="fundingtype" />
			<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'AidPredictability')">
			</div>
			<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar', 'AidPredictability', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'AidPredictability', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'AidPredictability', true)" title="<digi:trn>Data View</digi:trn>"/></div>
			<br />
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="AidPredictability">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
			<div align="right">
				<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
			</div> 
		</fieldset>
		<fieldset class="chartFieldset">
			<legend><span id="AidTypeTitleLegend" class=legend_label></span></legend>
			<div id="AidTypeHeader" class="chart_header" style="float:left">
			<digi:trn>Title</digi:trn> <input type="text" id="AidTypeTitle" value="" size="50">
			<input type="hidden" id="AidTypeShowFontFamily" value="Verdana"/>
			&nbsp;<digi:trn>Size</digi:trn>
			<select id="AidTypeFontSize">
				<option value="12">12</option>
				<option value="13">13</option>
				<option value="14">14</option>
				<option value="15">15</option>
				<option value="16">16</option>
			</select>
			&nbsp;<input type="checkbox" id="AidTypeBold"><label for="AidTypeBold"><digi:trn>Bold</digi:trn></label><br/>
			<input type="checkbox" id="AidTypeShowLegend" checked="checked"><label for="AidTypeShowLegend"><digi:trn>Show legend</digi:trn></label>
			&nbsp;<input type="checkbox" id="AidTypeDivide"><label for="AidTypeDivide"><digi:trn>Divide by thousands</digi:trn></label>
			&nbsp;<input type="checkbox" id="AidTypeShowDataLabel"><label for="AidTypeShowDataLabel"><digi:trn>Show data label</digi:trn></label>
			&nbsp;<input type="checkbox" id="AidTypeRotateDataLabel"><label for="AidTypeRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
			<input type="hidden" id="AidTypeDataAction" value="getAidTypeGraphData" />
			<input type="hidden" id="AidTypeDataField" value="aidtype" />
			<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'AidType')">
			</div>
			<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar', 'AidType', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', 'AidType', true)" title="<digi:trn>Donut Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'AidType', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'AidType', true)" title="<digi:trn>Data View</digi:trn>"/></div>
			<br />
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="AidType">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
			<div align="right">
				<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
			</div> 
		</fieldset>
		<fieldset class="chartFieldset">
			<legend><span id="FinancingInstrumentTitleLegend" class=legend_label></span></legend>
			<div id="FinancingInstrumentHeader" class="chart_header" style="float:left">
			<digi:trn>Title</digi:trn> <input type="text" id="FinancingInstrumentTitle" value="" size="50">
			<input type="hidden" id="FinancingInstrumentShowFontFamily" value="Verdana"/>
			&nbsp;<digi:trn>Size</digi:trn>
			<select id="FinancingInstrumentFontSize">
				<option value="12">12</option>
				<option value="13">13</option>
				<option value="14">14</option>
				<option value="15">15</option>
				<option value="16">16</option>
			</select>
			&nbsp;<input type="checkbox" id="FinancingInstrumentBold"><label for="FinancingInstrumentBold"><digi:trn>Bold</digi:trn></label><br/>
			<input type="checkbox" id="FinancingInstrumentShowLegend" checked="checked"><label for="FinancingInstrumentShowLegend"><digi:trn>Show legend</digi:trn></label>
			&nbsp;<input type="checkbox" id="FinancingInstrumentDivide"><label for="FinancingInstrumentDivide"><digi:trn>Divide by thousands</digi:trn></label>
			&nbsp;<input type="checkbox" id="FinancingInstrumentShowDataLabel"><label for="FinancingInstrumentShowDataLabel"><digi:trn>Show data label</digi:trn></label>
			&nbsp;<input type="checkbox" id="FinancingInstrumentRotateDataLabel"><label for="FinancingInstrumentRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
			<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'FinancingInstrument')">
			<input type="hidden" id="FinancingInstrumentDataAction" value="getAidTypeGraphData" />
			<input type="hidden" id="FinancingInstrumentDataField" value="aidtype" />
			</div>
			<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar', 'FinancingInstrument', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', 'FinancingInstrument', true)" title="<digi:trn>Donut Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'FinancingInstrument', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'FinancingInstrument', true)" title="<digi:trn>Data View</digi:trn>"/></div>
			<br />
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="FinancingInstrument">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
			<div align="right">
				<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
			</div> 
		</fieldset>
		<c:if test="${visualizationform.filter.dashboardType ne '1' }">
			<fieldset class="chartFieldset">
				<legend><span id="DonorProfileTitleLegend" class=legend_label></span></legend>
				<div id="DonorProfileHeader" class="chart_header" style="float:left">
				<digi:trn>Title</digi:trn> <input type="text" id="DonorProfileTitle" value="" size="50">
				<input type="hidden" id="DonorProfileShowFontFamily" value="Verdana"/>
				&nbsp;<digi:trn>Size</digi:trn>
				<select id="DonorProfileFontSize">
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
				</select>
				&nbsp;<input type="checkbox" id="DonorProfileBold"><label for="DonorProfileBold"><digi:trn>Bold</digi:trn></label><br/>
				<input type="checkbox" id="DonorProfileShowLegend" checked="checked"><label for="DonorProfileShowLegend"><digi:trn>Show legend</digi:trn></label>
				&nbsp;<input type="checkbox" id="DonorProfileDivide"><label for="DonorProfileDivide"><digi:trn>Divide by thousands</digi:trn></label>
				&nbsp;<input type="checkbox" id="DonorProfileShowDataLabel"><label for="DonorProfileShowDataLabel"><digi:trn>Show data label</digi:trn></label>
				&nbsp;<input type="checkbox" id="DonorProfileRotateDataLabel"><label for="DonorProfileRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
				<input type="hidden" id="DonorProfileDataAction" value="getDonorProfileGraphData" />
				<input type="hidden" id="DonorProfileDataField" value="donor" />
				<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'DonorProfile')">
				</div>
				<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar_profile', 'DonorProfile', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', 'DonorProfile', true)" title="<digi:trn>Donut Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'DonorProfile', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'DonorProfile', true)" title="<digi:trn>Data View</digi:trn>"/></div>
				<br />
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="DonorProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
		</c:if>
		<c:if test="${visualizationform.filter.dashboardType ne '3' }">
			<fieldset class="chartFieldset">
				<legend><span id="SectorProfileTitleLegend" class=legend_label></span></legend>
				<div id="SectorProfileHeader" class="chart_header" style="float:left">
				<digi:trn>Title</digi:trn> <input type="text" id="SectorProfileTitle" value="" size="50">
				<input type="hidden" id="SectorProfileShowFontFamily" value="Verdana"/>
				&nbsp;<digi:trn>Size</digi:trn>
				<select id="SectorProfileFontSize">
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
				</select>
				&nbsp;<input type="checkbox" id="SectorProfileBold"><label for="SectorProfileBold"><digi:trn>Bold</digi:trn></label><br/>
				<input type="checkbox" id="SectorProfileShowLegend" checked="checked"><label for="SectorProfileShowLegend"><digi:trn>Show legend</digi:trn></label>
				&nbsp;<input type="checkbox" id="SectorProfileDivide"><label for="SectorProfileDivide"><digi:trn>Divide by thousands</digi:trn></label>
				&nbsp;<input type="checkbox" id="SectorProfileShowDataLabel"><label for="SectorProfileShowDataLabel"><digi:trn>Show data label</digi:trn></label>
				&nbsp;<input type="checkbox" id="SectorProfileRotateDataLabel"><label for="SectorProfileRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
				<input type="hidden" id="SectorProfileDataAction" value="getSectorProfileGraphData" />
				<input type="hidden" id="SectorProfileDataField" value="sector" />
				<input type="hidden" id="SectorProfileItemId" value="${visualizationform.filter.sectorId}" />
				<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'SectorProfile')">
				</div>
				<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar_profile', 'SectorProfile', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', 'SectorProfile', true)" title="<digi:trn>Donut Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'SectorProfile', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'SectorProfile', true)" title="<digi:trn>Data View</digi:trn>"/></div>
				<br />
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="SectorProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
		</c:if>
		<c:if test="${visualizationform.filter.dashboardType ne '2' }">
			<fieldset class="chartFieldset">
				<legend><span id="RegionProfileTitleLegend" class=legend_label></span></legend>
				<div id="RegionProfileHeader" class="chart_header" style="float:left">
				<digi:trn>Title</digi:trn> <input type="text" id="RegionProfileTitle" value="" size="50">
				<input type="hidden" id="RegionProfileShowFontFamily" value="Verdana"/>
				&nbsp;<digi:trn>Size</digi:trn>
				<select id="RegionProfileFontSize">
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
				</select>
				&nbsp;<input type="checkbox" id="RegionProfileBold"><label for="RegionProfileBold"><digi:trn>Bold</digi:trn></label><br/>
				<input type="checkbox" id="RegionProfileShowLegend" checked="checked"><label for="RegionProfileShowLegend"><digi:trn>Show legend</digi:trn></label>
				&nbsp;<input type="checkbox" id="RegionProfileDivide"><label for="RegionProfileDivide"><digi:trn>Divide by thousands</digi:trn></label>
				&nbsp;<input type="checkbox" id="RegionProfileShowDataLabel"><label for="RegionProfileShowDataLabel"><digi:trn>Show data label</digi:trn></label>
				&nbsp;<input type="checkbox" id="RegionProfileRotateDataLabel"><label for="RegionProfileRotateDataLabel"><digi:trn>Rotate data label</digi:trn></label></br>
				<input type="hidden" id="RegionProfileDataAction" value="getRegionProfileGraphData" />
				<input type="hidden" id="RegionProfileDataField" value="region" />
				<input type="hidden" id="RegionProfileItemId" value="${visualizationform.filter.regionId}" />
				<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, 'RegionProfile')">
				</div>
				<div class="dash_graph_opt"><img style="padding-left: 5px" onclick="changeChart(event, 'bar_profile', 'RegionProfile', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', 'RegionProfile', true)" title="<digi:trn>Donut Chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', 'RegionProfile', true)" title="<digi:trn>Line chart</digi:trn>"/><img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', 'RegionProfile', true)" title="<digi:trn>Data View</digi:trn>"/></div>
				<br />
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="RegionProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
		</c:if>
	</div>
	<c:if test="${visualizationform.filter.dashboardType eq '1' }">
	<div id="tab2">
		<digi:trn>No Contact Information available for current filter selection</digi:trn>
	</div>
	<c:if test="${!visualizationform.filter.fromPublicView}">
	<div id="tab3">
	</div>
	</c:if>
	</c:if>
	</div>
</div>

</td>
</tr>
</table>
</td>
</tr>
</table>

</digi:form>

<script type="text/javascript">
<!--

//Filter Javascript
//Attach event to reload organizations from organization group
//Attach event to reload zones from regions
var callbackChildrenCall = {
	  success: function(o) {
		  try {
			    var results = YAHOO.lang.JSON.parse(o.responseText);
			    switch(results.objectType)
			    {
				    case "Organization":
			    		var orgDropdown = document.getElementById("org_dropdown_id");
			    		orgDropdown.options.length = 0;
			    		orgDropdown.options[0] = new Option("<digi:trn>All</digi:trn>", -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
				    case "Sector":
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
			    		subSectorDropdown.options.length = 0;
			    		subSectorDropdown.options[0] = new Option("<digi:trn>All</digi:trn>", -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			subSectorDropdown.options[subSectorDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Region":
			    		var zonesDropdown = document.getElementById("zone_dropdown_id");
			    		zonesDropdown.options.length = 0;
			    		zonesDropdown.options[0] = new Option("<digi:trn>All</digi:trn>", -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			zonesDropdown.options[zonesDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	  case "Config":
				    		var sectorDropdown = document.getElementById("sector_dropdown_id");
				    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
				    		subSectorDropdown.options.length = 0;
				    		sectorDropdown.options.length = 0;
				    		subSectorDropdown.options[0] = new Option("<digi:trn>All</digi:trn>", -1);
				    		sectorDropdown.options[0] = new Option("<digi:trn>All</digi:trn>", -1);
				    		for(var i = 0; i < results.children.length; i++){
				    			sectorDropdown.options[sectorDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
				    		}
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
			  refreshBoxes(o);
			  refreshGraphs();
		  },
		  failure: function(o) {
			  loadingPanel.hide();
		  }
		};

function callbackApplyFilter(e){
	loadingPanel.show();
	YAHOO.util.Connect.setForm('visualizationform');

	var sUrl="/visualization/dataDispatcher.do?action=applyFilter";

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
	document.getElementById("filterOrgGroups").innerHTML = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
	document.getElementById("filterOrganizations").innerHTML = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
	document.getElementById("filterSectorConfiguration").innerHTML = document.getElementById("sector_config_dropdown_id").options[document.getElementById("sector_config_dropdown_id").selectedIndex].text;
	document.getElementById("filterSectors").innerHTML = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
	document.getElementById("filterRegions").innerHTML = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;

}

function applyFilterPopin(e){
	
//var allGraphs = document.getElementsByName("flashContent");
	document.getElementById("decimalsToShow").value = document.getElementById("decimalsToShow_dropdown").options[document.getElementById("decimalsToShow_dropdown").selectedIndex].value;
	document.getElementById("currentYear").value = document.getElementById("year_dropdown").options[document.getElementById("year_dropdown").selectedIndex].value;
	document.getElementById("filterFiscalYear").innerHTML = document.getElementById("year_dropdown").options[document.getElementById("year_dropdown").selectedIndex].value;
	document.getElementById("yearToCompare").value = document.getElementById("yearToCompare_dropdown").options[document.getElementById("yearToCompare_dropdown").selectedIndex].value;
	document.getElementById("yearsInRange").value = document.getElementById("yearsInRange_dropdown").options[document.getElementById("yearsInRange_dropdown").selectedIndex].value;
	document.getElementById("filterYearsRange").innerHTML = document.getElementById("yearsInRange_dropdown").options[document.getElementById("yearsInRange_dropdown").selectedIndex].value;
	document.getElementById("yearsInRangeLine").value = document.getElementById("yearsInRangeLine_dropdown").options[document.getElementById("yearsInRangeLine_dropdown").selectedIndex].value;
	document.getElementById("yearsInRangePie").value = document.getElementById("yearsInRangePie_dropdown").options[document.getElementById("yearsInRangePie_dropdown").selectedIndex].value;
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
	document.getElementById("showMonochrome").value = document.getElementById("show_monochrome").checked;
	
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
	var params = "";
	params = params + "&orgGroupIds=" + getSelectionsFromElement("org_grp_check",false);
	params = params + "&orgIds=" + getSelectionsFromElement("organization_check",false);
	params = params + "&regionIds=" + getSelectionsFromElement("region_check",false);
	params = params + "&zoneIds=" + getSelectionsFromElement("zone_check",false);
	params = params + "&selSectorConfigId=" + getSelectionsFromElement("sector_config_check",false);
	params = params + "&sectorIds=" + getSelectionsFromElement("sector_check",false);
	params = params + "&subSectorIds=" + getSelectionsFromElement("sub_sector_check",false);

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
			if (YAHOO.env.ua.gecko <= 1.91) {
				currentMovie.scrollIntoView(true)
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

	var dashboardType = document.getElementById("dashboardType").value;
	var results = YAHOO.lang.JSON.parse(o.responseText);
	var inner = "";
	var inner2 = "";
	var trnTotalDisbs="<digi:trn jsFriendly='true'>Total Disbursements</digi:trn>: ";
	var trnNumOfProjs="<digi:trn jsFriendly='true'>Total Number of Projects</digi:trn>: ";
	var trnNumOfDons="<digi:trn jsFriendly='true'>Total Number of Donors</digi:trn>: ";
	var trnNumOfSecs="<digi:trn jsFriendly='true'>Total Number of Sectors</digi:trn>: ";
	var trnNumOfRegs="<digi:trn jsFriendly='true'>Total Number of Regions</digi:trn>: ";
	var trnAvgProjSize="<digi:trn jsFriendly='true'>Average Project Size</digi:trn>: ";
	var trnTotalDisbsDescription="<digi:trn jsFriendly='true'>Sum of Disbursements on projets filtered.</digi:trn>";
	var trnNumOfProjsDescription="<digi:trn jsFriendly='true'>Number of Projects filtered.</digi:trn>";
	var trnNumOfDonsDescription="<digi:trn jsFriendly='true'>Number of Donors on projects filtered</digi:trn>";
	var trnNumOfSecsDescription="<digi:trn jsFriendly='true'>Number of Sectors on projects filtered</digi:trn>";
	var trnNumOfRegsDescription="<digi:trn jsFriendly='true'>Number of Regions on projects filtered</digi:trn>";
	var trnAvgProjSizeDescription="<digi:trn jsFriendly='true'>Total Disbursements divided Number of Projects</digi:trn>";
	var valTotalDisbs="";
	var valNumOfProjs="";
	var valNumOfSecs="";
	var valNumOfRegs="";
	var valAvgProjSize="";
	var trnCommitments="<digi:trn jsFriendly='true'>Commitments</digi:trn>";
	var trnDisbursements="<digi:trn jsFriendly='true'>Disbursements</digi:trn>";
	var trnExpenditures="<digi:trn jsFriendly='true'>Expenditures</digi:trn>";
	var trnPledges="<digi:trn jsFriendly='true'>Pledges</digi:trn>";
	
	var trnAidPredictability="<digi:trn jsFriendly='true'>Aid Predictability</digi:trn>";
	var trnAidType="<digi:trn jsFriendly='true'>Aid Type</digi:trn>";
	var trnFinancingInstrument="<digi:trn jsFriendly='true'>Financing Instrument</digi:trn>";
	var trnDonorProfile="<digi:trn jsFriendly='true'>Donor Profile</digi:trn>";
	var trnSectorProfile="<digi:trn jsFriendly='true'>Sector Profile</digi:trn>";
	var trnSubSectorProfile="<digi:trn jsFriendly='true'>Sub-sector breakdown</digi:trn>";
	var trnRegionProfile="<digi:trn jsFriendly='true'>Region Profile</digi:trn>";
	var trnSubRegionProfile="<digi:trn jsFriendly='true'>Zone breakdown</digi:trn>";
	var trnShowTop5="<digi:trn jsFriendly='true'>Show Top 5</digi:trn>"; 
	var trnShowFullList="<digi:trn jsFriendly='true'>Show Full List</digi:trn>"; 
	var trnTopProjects="<digi:trn jsFriendly='true'>Top Projects</digi:trn>";
	var trnTopSectors="<digi:trn jsFriendly='true'>Top Sectors</digi:trn>";
	var trnTopRegions="<digi:trn jsFriendly='true'>Top Regions</digi:trn>"; 
	var trnTopDonors="<digi:trn jsFriendly='true'>Top Donors</digi:trn>"; 
	
	for(var j = 0; j < results.children.length; j++){
		var child = results.children[j];
		switch(child.type){
			case "ProjectsList":
				inner = "<a href='javascript:hideFullProjects()' style='float:right;'>"+trnShowTop5+"</a> <br />";
				for(var i = 0; i < child.list.length; i++){
					inner = inner + (i+1) + ". " + "<a href='/aim/viewActivityPreview.do~pageId=2~activityId=" + child.list[i].id + "~isPreview=1'>" + child.list[i].name + "</a>" + "  <b>($" + child.list[i].value + ")</b> <hr />";
				}
				inner = inner + "<a href='javascript:hideFullProjects()' style='float:right;'>"+trnShowTop5+"</a>";
				var div = document.getElementById("divFullProjects");
				div.innerHTML = inner;
				inner = "";
				for(var i = 0; i < child.top.length; i++){
					//inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";  
					inner = inner + (i+1) + ". " + "<a href='/aim/viewActivityPreview.do~pageId=2~activityId=" + child.top[i].id + "~isPreview=1'>" + child.top[i].name + "</a>" + "  <b>($" + child.top[i].value + ")</b> <hr />";
				}
				inner = inner + "<a href='javascript:showFullProjects()' style='float:right;'>"+trnShowFullList+"</a>";
				var div = document.getElementById("divTopProjects");
				div.innerHTML = inner;
				break;
			case "DonorsList":
				if (dashboardType!=1) {
					inner = "<a href='javascript:hideFullDonors()' style='float:right;'>"+trnShowTop5+"</a> <br />";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + (i+1) + ". " + child.list[i].name + "  <b>($" + child.list[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:hideFullDonors()' style='float:right;'>"+trnShowTop5+"</a>";
					var div = document.getElementById("divFullDonors");
					div.innerHTML = inner;
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>($" + child.top[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullDonors()' style='float:right;'>"+trnShowFullList+"</a>";
					var div = document.getElementById("divTopDonors");
					div.innerHTML = inner;
				}
				break;
			case "SectorsList":
				if (dashboardType!=3) {
					inner = "<a href='javascript:hideFullSectors()' style='float:right;'>"+trnShowTop5+"</a> <br />";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + (i+1) + ". " + child.list[i].name + "  <b>($" + child.list[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:hideFullSectors()' style='float:right;'>"+trnShowTop5+"</a>";
					var div = document.getElementById("divFullSectors");
					div.innerHTML = inner;
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>($" + child.top[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullSectors()' style='float:right;'>"+trnShowFullList+"</a>";
					var div = document.getElementById("divTopSectors");
					div.innerHTML = inner;
				}
				break;
			case "RegionsList":
				if (dashboardType!=2) {
					inner = "<a href='javascript:hideFullRegions()' style='float:right;'>"+trnShowTop5+"</a> <br />";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + (i+1) + ". " + child.list[i].name + "  <b>($" + child.list[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:hideFullRegions()' style='float:right;'>"+trnShowTop5+"</a>";
					var div = document.getElementById("divFullRegions");
					div.innerHTML = inner;
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>($" + child.top[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullRegions()' style='float:right;'>"+trnShowFullList+"</a>";
					var div = document.getElementById("divTopRegions");
					div.innerHTML = inner;
				}
				break;
			case "SelOrgGroupsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
					var div = document.getElementById("org_group_list_id");
					div.innerHTML = inner;
					document.getElementById("filterOrgGroups").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("org_group_dropdown_id").style.display = "none";
					} else {
						document.getElementById("org_group_list_id").style.display = "none";
						document.getElementById("org_group_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelOrgsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
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
			case "SelRegionsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
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
					inner = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("zone_list_id");
					div.innerHTML = inner;
					div.style.display = "";
					document.getElementById("zone_dropdown_id").style.display = "none";
					} else {
						document.getElementById("zone_list_id").style.display = "none";
						document.getElementById("zone_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelSectorConfig":
					if (child.list.length > 0) {
					inner =child.list[0].name;
					var div = document.getElementById("sector_config_list_id");
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
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
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
					inner = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("sub_sector_list_id");
					div.innerHTML = inner;
					div.style.display = "";
					document.getElementById("sub_sector_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sub_sector_list_id").style.display = "none";
						document.getElementById("sub_sector_dropdown_id").style.display = "";
					}
				//}
				break;
			case "TotalComms":
				inner = "<b class='dashboard_total_num'>" + child.value + "</b><br /><digi:trn>Total Commitments</digi:trn>";
				var div = document.getElementById("divTotalComms");
				div.innerHTML = inner;
				inner = "<i><font size='2' color='red'><digi:trn>All amounts in millions</digi:trn> - " + child.curr + "</font></i>";
				var div = document.getElementById("currencyInfo");
				div.innerHTML = inner;
				
				var div = document.getElementById("filterCurrency");
				div.innerHTML = "" + child.curr;
				
				break;
			case "TotalDisbs":
				valTotalDisbs = child.value;
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
				valAvgProjSize = child.value;
				break;
			case "SelOrgContact":
				if (child.list.length ==1) {
					var contact=child.list[0];
					var contactMarkup = new Array();
					contactMarkup.push("<table class=\"inside\">");
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\"><digi:trn>Title</digi:trn>:</td>");
					contactMarkup.push("<td class=\"inside\">");
					contactMarkup.push(contact.title);
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\"><digi:trn>Name</digi:trn>:</td>");
					contactMarkup.push("<td class=\"inside\">");
					contactMarkup.push(contact.name);
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\"><digi:trn>Emails</digi:trn>:</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactEmails=contact.email;
				
					for(var i=0;i<conactEmails.length;i++){
						contactMarkup.push(conactEmails[i].value);
						contactMarkup.push("<br/>");
						
					}
		
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\"><digi:trn>Phones</digi:trn>:</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactPhones=contact.phones;
					for(var i=0;i<conactPhones.length;i++){
						contactMarkup.push(conactPhones[i].value);
						contactMarkup.push("<br/>");
						
					}
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\"><digi:trn>Faxes</digi:trn>:</td>");
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
					$("#tab2").html("<digi:trn>No Contact Information available for current filter</digi:trn>");
				}
				break;
			case "SelAdditionalInfo":
				if (typeof child.additionalInfo.info != 'undefined') {
					var info=child.additionalInfo.info;
					var infoMarkup = new Array();
					infoMarkup.push("<div id=\"saveResultMsg\"></div><table class=\"inside\"><tbody>");
					infoMarkup.push("<tr>");
					infoMarkup.push("<td class=\"inside\"><digi:trn>Background of donor</digi:trn>:</td>");
					infoMarkup.push("<td class=\"inside\">");
					infoMarkup.push("<textarea cols=\"40\" rows=\"3\" id=\"orgBackground\">");
					infoMarkup.push(info.orgBackground);
					infoMarkup.push("</textarea>");
					infoMarkup.push("</td>");
					infoMarkup.push("</tr>");

					infoMarkup.push("<tr>");
					infoMarkup.push("<td class=\"inside\"><digi:trn>Description</digi:trn>:</td>");
					infoMarkup.push("<td class=\"inside\">");
					infoMarkup.push("<textarea cols=\"40\" rows=\"3\" id=\"orgDescription\">");
					infoMarkup.push(info.orgDescription);
					infoMarkup.push("</textarea>");
					infoMarkup.push("</td>");
					infoMarkup.push("</tr>");
					
					infoMarkup.push("<tr>");
					infoMarkup.push("<td class=\"inside\" colspan=\"2\">");
					infoMarkup.push("<input type=\"button\" value=\"<digi:trn>Save</digi:trn>\" onclick=\"saveAdditionalInfo("+info.orgId+")\"/>");
					infoMarkup.push("</td>");
					infoMarkup.push("</tr>");
					infoMarkup.push("</tbody></table>");
					var markup=infoMarkup.join("");
					$("#tab3").html(markup);
				}
				else{
					$("#tab3").html("<digi:trn>No Additional Information available for current filter</digi:trn>");
				}
				break; 
				
		}
	}
	inner = "<a title='" + trnTotalDisbsDescription + "' style='color: black;'>" + trnTotalDisbs + "</a> <b>" + valTotalDisbs + "</b><span class='breadcrump_sep'>|</span>";
	inner = inner + "<a title='" + trnNumOfProjsDescription + "' style='color: black;'>" + trnNumOfProjs + "</a> <b>" + valNumOfProjs + "</b><span class='breadcrump_sep'>|</span>";
	if (dashboardType!=1) {
		inner = inner + "<a title='" + trnNumOfDonsDescription + "' style='color: black;'>" + trnNumOfDons + "</a> <b>" + valNumOfDons + "</b><span class='breadcrump_sep'>|</span>";
	}
	if (dashboardType!=3) {
		inner = inner + "<a title='" + trnNumOfSecsDescription + "' style='color: black;'>" + trnNumOfSecs + "</a> <b>" + valNumOfSecs + "</b><span class='breadcrump_sep'>|</span>";
	}
	if (dashboardType!=2) {
		inner = inner + "<a title='" + trnNumOfRegsDescription + "' style='color: black;'>" + trnNumOfRegs + "</a> <b>" + valNumOfRegs + "</b><span class='breadcrump_sep'>|</span>";
	}
	inner = inner + "<a title='" + trnAvgProjSizeDescription + "' style='color: black;'>" + trnAvgProjSize + "</a> <b>" + valAvgProjSize;
	var div = document.getElementById("divSummaryInfo");
	div.innerHTML = inner;

	var namePlaceholder = document.getElementById("dashboard_name");
	if (dashboardType==1) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("org_grp_check",true)==""){
			if (document.getElementById("org_group_dropdown_id").selectedIndex == 0) {
				name1 = "<digi:trn jsFriendly='true'>ALL Organization Groups</digi:trn>";
			} else {
				name1 = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
			}
		} else {
			name1 = getSelectionsFromElement("org_grp_check",true);
		}
		if (getSelectionsFromElement("organization_check",true)==""){
			if (document.getElementById("org_dropdown_id").selectedIndex == 0) {
				//name2 = "<digi:trn jsFriendly='true'>ALL Organizations</digi:trn>";
			} else {
				name2 = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("organization_check",false).indexOf(',') !=-1) {
				name2 = "<digi:trn jsFriendly='true'>Multiple Organizations</digi:trn>";
			} else {
				name2 = getSelectionsFromElement("organization_check",true);
			}
		}
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	if (dashboardType==3) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("sector_check",true)==""){
			if (document.getElementById("sector_dropdown_id").selectedIndex == 0) {
				name1 = "<digi:trn jsFriendly='true'>ALL Sectors</digi:trn>";
			} else {
				name1 = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
			}
		} else {
			name1 = getSelectionsFromElement("sector_check",true);
		}
		if (getSelectionsFromElement("sub_sector_check",true)==""){
			if (document.getElementById("sub_sector_dropdown_id").selectedIndex == 0) {
				name2 = "<digi:trn jsFriendly='true'>ALL Sub Sectors</digi:trn>";
			} else {
				name2 = document.getElementById("sub_sector_dropdown_id").options[document.getElementById("sub_sector_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("sub_sector_check",false).indexOf(',') !=-1) {
				name2 = "<digi:trn jsFriendly='true'>Multiple Sub Sectors</digi:trn>";
			} else {
				name2 = getSelectionsFromElement("sub_sector_check",true);
			}
		}
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	if (dashboardType==2) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("region_check",true)==""){
			if (document.getElementById("region_dropdown_id").selectedIndex == 0) {
				name1 = "<digi:trn jsFriendly='true'>ALL Regions</digi:trn>";
			} else {
				name1 = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
			}
		} else {
			name1 = getSelectionsFromElement("region_check",true);
		}
		if (getSelectionsFromElement("zone_check",true)==""){
			if (document.getElementById("zone_dropdown_id").selectedIndex == 0) {
				name2 = "<digi:trn jsFriendly='true'>ALL Zones</digi:trn>";
			} else {
				name2 = document.getElementById("zone_dropdown_id").options[document.getElementById("zone_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("zone_check",false).indexOf(',') !=-1) {
				name2 = "<digi:trn jsFriendly='true'>Multiple Zones</digi:trn>";
			} else {
				name2 = getSelectionsFromElement("zone_check",true);
			}
		}
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	
//	div = document.getElementById("fundingChartTitle");
//	inner = "<digi:trn jsFriendly='true'>ODA historical trend</digi:trn>";
	/*if (document.getElementById("commitments_visible").checked==true) {
		inner = inner + trnCommitments + " - ";
		}
	if (document.getElementById("disbursements_visible").checked==true) {
		inner = inner + trnDisbursements + " - ";
		}
	if (document.getElementById("expenditures_visible")!=null){
		if (document.getElementById("expenditures_visible").checked==true) {
			inner = inner + trnExpenditures + " - ";
		}
	}
	if (document.getElementById("pledge_visible")!=null){
		if (document.getElementById("pledge_visible").checked==true) {
			inner = inner + trnPledges;
		}
	}*/
//	div.innerHTML = inner;

	var type = "" + getOptionChecked("transaction_type_");
	var fundType = "";
	if (type=="0") {
		fundType = trnCommitments;
	}
	if (type=="1") {
		fundType = trnDisbursements;
	}
	if (type=="2") {
		fundType = trnExpenditures;
	}
	try
	{
		if (dashboardType==1) {
			var currentYear = document.getElementById("currentYear").value;
			var startYear = document.getElementById("yearToCompare").value;
			if (startYear == "0" || startYear == "" || startYear == null || startYear >= currentYear){
				startYear =  "" + (currentYear - 1);
			}
			var endYear =  "" + currentYear;
			div = document.getElementById("ODAGrowthTitleLegend");
			input = document.getElementById("ODAGrowthTitle");
			value = "<digi:trn jsFriendly='true'>ODA Growth Percentage</digi:trn> " + " - " + fundType +" (" + startYear + "-" + endYear + ")";
			div.innerHTML = value;
			input.value = value;
		}
		div = document.getElementById("AidPredictabilityTitleLegend");
		input = document.getElementById("AidPredictabilityTitle");
		value = trnAidPredictability + " - " + fundType;
		div.innerHTML = value;
		input.value = value;
	
		div = document.getElementById("AidTypeTitleLegend");
		input = document.getElementById("AidTypeTitle");
		value = trnAidType + " - " + fundType;
		div.innerHTML = value;
		input.value = value;
	
		div = document.getElementById("FinancingInstrumentTitleLegend");
		input = document.getElementById("FinancingInstrumentTitle");
		value = trnFinancingInstrument + " - " + fundType;
		div.innerHTML = value;
		input.value = value;
	
		if (dashboardType!=1) {
			div = document.getElementById("DonorProfileTitleLegend");
			input = document.getElementById("DonorProfileTitle");
			value = trnDonorProfile + " - " + fundType;
			div.innerHTML = value;
			input.value = value;
		}
		if (dashboardType !=3 || dashboardType == 3) {
			div = document.getElementById("SectorProfileTitleLegend");
			input = document.getElementById("SectorProfileTitle");
			isSubsector = (document.getElementById("SectorProfileItemId") && document.getElementById("SectorProfileItemId").value != "-1") ? true : false;
			if(isSubsector){
				value = trnSubSectorProfile + " - " + fundType;
			}
			else
			{
				value = trnSectorProfile + " - " + fundType;
			}
			div.innerHTML = value;
			input.value = value;
		}
		if (dashboardType!=2 ||  dashboardType == 2) {
			div = document.getElementById("RegionProfileTitleLegend");
			input = document.getElementById("RegionProfileTitle");
			isSubregion = (document.getElementById("RegionProfileItemId") && document.getElementById("RegionProfileItemId").value != "-1") ? true : false;
			if(isSubregion){
				value = trnSubRegionProfile + " - " + fundType;
			}
			else
			{
				value = trnRegionProfile + " - " + fundType;
			}
			div.innerHTML = value;
			input.value = value;
		}
	}
	catch(e){
		
	}

	var currentYear = document.getElementById("currentYear").value;
	var yearsInRange = document.getElementById("yearsInRange").value;
	var startYear =  "" + (currentYear - yearsInRange + 1);
	var endYear =  "" + currentYear;

	div = document.getElementById("topProjectsTitle");
	if (yearsInRange == 1) {
		inner = trnTopProjects + " (" + startYear + ")";
	} else {
		inner = trnTopProjects + " (" + startYear + "-" + endYear + ")";
	}
	div.innerHTML = inner;
	if (dashboardType!=3) {
		div = document.getElementById("topSectorsTitle");
		if (yearsInRange == 1) {
			inner = trnTopSectors + " (" + startYear + ")";
		} else {
			inner = trnTopSectors + " (" + startYear + "-" + endYear + ")";
		}
		div.innerHTML = inner;
	}
	if (dashboardType!=1) {
		div = document.getElementById("topDonorsTitle");
		if (yearsInRange == 1) {
			inner = trnTopDonors + " (" + startYear + ")";
		} else {
			inner = trnTopDonors + " (" + startYear + "-" + endYear + ")";
		}
		div.innerHTML = inner;
	}
	if (dashboardType!=2) {
		div = document.getElementById("topRegionsTitle");
		if (yearsInRange == 1) {
			inner = trnTopRegions + " (" + startYear + ")";
		} else {
			inner = trnTopRegions + " (" + startYear + "-" + endYear + ")";
		}
		div.innerHTML = inner;
	}
}


//YAHOO.util.Event.addListener("region_dropdown_ids", "change", callbackChildren);
//YAHOO.util.Event.onAvailable("region_dropdown_ids", callbackChildren);
//YAHOO.util.Event.addListener("org_group_dropdown_ids", "change", callbackChildren);
//YAHOO.util.Event.onAvailable("org_group_dropdown_ids", callbackChildren);
//YAHOO.util.Event.addListener("sector_dropdown_ids", "change", callbackChildren);
//YAHOO.util.Event.onAvailable("sector_dropdown_ids", callbackChildren);
YAHOO.util.Event.addListener("region_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("region_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("org_group_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("sector_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("sector_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("sector_config_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("sector_config_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("applyButton", "click", callbackApplyFilter);
YAHOO.util.Event.addListener("applyButtonPopin", "click", applyFilterPopin);
YAHOO.util.Event.addListener("visualizationDiv", "click", refreshGraphs);
YAHOO.util.Event.onDOMReady(initDashboard);
window.onload=initPanel;
var initialized = false;
function initDashboard(){
	var dashboardType = document.getElementById("dashboardType").value;
	changeChart(null, 'bar', 'FundingChart', true);
	changeChart(null, 'bar', 'ODAGrowth');
	changeChart(null, 'bar', 'AidPredictability', true);
	changeChart(null, 'bar', 'AidType', true);
	changeChart(null, 'bar', 'FinancingInstrument', true);
	if (dashboardType!=1) {
		changeChart(null, 'bar_profile', 'DonorProfile', true);
	}
	changeChart(null, 'bar_profile', 'SectorProfile', true);
	changeChart(null, 'bar_profile', 'RegionProfile', true);
	callbackApplyFilter();
}

function  saveAdditionalInfo(orgId){
    var postString		="orgBackground=" + document.getElementById("orgBackground").value+
        "&orgDescription="+document.getElementById("orgDescription").value+"&orgId="+orgId ;
		<digi:context name="url" property="context/visualization/saveOrgInfo.do"/>
        var url = "${url}";
        $("#saveResultMsg").html("<digi:trn>saving information, please wait</digi:trn>...");
        YAHOO.util.Connect.asyncRequest("POST", url, additionalInfoCallback, postString);
    }

    var additionalInfoResponseSuccess = function(o){
    	$("#saveResultMsg").html("<digi:trn>Information was saved</digi:trn>!");
    }

    var additionalInfoResponseFailure = function(o){
    	$("#saveResultMsg").html("<digi:trn>Failed to save information</digi:trn>!");
    }
    var additionalInfoCallback =
        {
        success:additionalInfoResponseSuccess,
        failure:additionalInfoResponseFailure
    };

function getValueToFlash(idContainer, field){
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

	//Get array of graphs
	var allGraphs = document.getElementsByName("flashContent");
	
	//Iterate and refresh the graph
	for(var idx = 0; idx < allGraphs.length; idx++){
		// Get flash object and refresh it by calling internal
		if(allGraphs[idx].children[0].id.toLowerCase() == chartName.toLowerCase()){
			document.getElementById(chartName + "TitleLegend").innerHTML = document.getElementById(chartName + "Title").value; 
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
	if (document.getElementById("show_monochrome").checked){
		palette = "0x000000,0x969696,0x191919,0xAFAFAF,0x323232,0xC8C8C8,0x4B4B4B,0xE1E1E1,0x646464,0xFAFAFA,0x7D7D7D";
	}
	 
	var decimalSeparator = document.getElementById("decimalSeparator").value;
	var groupSeparator = document.getElementById("groupSeparator").value;
	var decimalsToShow = document.getElementById("decimalsToShow").value;
	
	var flashvars = { 
			decimalSeparator: decimalSeparator, 
			groupSeparator: groupSeparator,
			decimalsToShow: decimalsToShow,
			palette: palette, 
			id: container,
			start: (startMovie ? "true" : "false")
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
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries.swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			break;
		case "bar_profile":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_Profile.swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			break;
		case "donut":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/PieChartSeries.swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/PieChart_" + container + ".swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			break;
		case "line":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries.swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries_" + container + ".swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			break;
		case "dataview":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries.swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries_" + container + ".swf" + cache, container, "634", "400", "10.0.0", false, flashvars, params, attributes);
			break;
	}
	updateChartSettings(container, chartType);
	return false;
}

function updateChartSettings(container, chartType){
	var title = document.getElementById(container + "Title") == undefined ? "" : document.getElementById(container + "Title");
	var fontSize = document.getElementById(container + "FontSize") == undefined ? "" : document.getElementById(container + "FontSize");
	var boldTitle = document.getElementById(container + "Bold") == undefined ? "" : document.getElementById(container + "Bold");
	var showLegend = document.getElementById(container + "ShowLegend") == undefined ? "" : document.getElementById(container + "ShowLegend");
	var showDataLabel = document.getElementById(container + "ShowDataLabel") == undefined ? "" : document.getElementById(container + "ShowDataLabel");
	var rotateDataLabel = document.getElementById(container + "RotateDataLabel") == undefined ? "" : document.getElementById(container + "RotateDataLabel");
	var divide = document.getElementById(container + "Divide") == undefined ? "" : document.getElementById(container + "Divide");

	switch(chartType){
	case "bar":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = false;
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

var myTabs = new YAHOO.widget.TabView("demo");
myTabs.selectTab(0);
var loadingPanel = new yuiLoadingPanel();

function showFullProjects(){
	var divFull = document.getElementById("divFullProjects");
	var divTop = document.getElementById("divTopProjects");
	divFull.style.display = "";
	divTop.style.display = "none";
}

function hideFullProjects(){
	var divFull = document.getElementById("divFullProjects");
	var divTop = document.getElementById("divTopProjects");
	divFull.style.display = "none";
	divTop.style.display = "";
}

function showFullSectors(){
	var divFull = document.getElementById("divFullSectors");
	var divTop = document.getElementById("divTopSectors");
	divFull.style.display = "";
	divTop.style.display = "none";
}

function hideFullSectors(){
	var divFull = document.getElementById("divFullSectors");
	var divTop = document.getElementById("divTopSectors");
	divFull.style.display = "none";
	divTop.style.display = "";
}

function showFullRegions(){
	var divFull = document.getElementById("divFullRegions");
	var divTop = document.getElementById("divTopRegions");
	divFull.style.display = "";
	divTop.style.display = "none";
}

function hideFullRegions(){
	var divFull = document.getElementById("divFullRegions");
	var divTop = document.getElementById("divTopRegions");
	divFull.style.display = "none";
	divTop.style.display = "";
}

function showFullDonors(){
	var divFull = document.getElementById("divFullDonors");
	var divTop = document.getElementById("divTopDonors");
	divFull.style.display = "";
	divTop.style.display = "none";
}

function hideFullDonors(){
	var divFull = document.getElementById("divFullDonors");
	var divTop = document.getElementById("divTopDonors");
	divFull.style.display = "none";
	divTop.style.display = "";
}


function reloadGraphs(){
	var dashboardType = document.getElementById("dashboardType").value;
	changeChart('start', 'bar', 'ODAGrowth');
	changeChart('start', 'bar', 'FundingChart', true);
	changeChart('start', 'bar', 'AidPredictability', true);
	changeChart('start', 'bar', 'AidType', true);
	changeChart('start', 'bar', 'FinancingInstrument', true);
	if (dashboardType!=1) {
		changeChart('start', 'bar_profile', 'DonorProfile', true);
	}
	changeChart('start', 'bar_profile', 'SectorProfile', true);
	changeChart('start', 'bar_profile', 'RegionProfile', true);
}

function itemClick(id, type, year){
	  openNewWindow(600, 400);
	  <digi:context name="showList" property="context/module/moduleinstance/showProjectsList.do?" />
	  var url="<%= showList %>&id=" + id + "&type=" + type + "&year=" + year;
	  document.visualizationform.action = url;
	  document.visualizationform.target = popupPointer.name;
	  document.visualizationform.submit();

		//var transaction = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getActivitiesList&id=" + id + "&type=" + type + "&year=" + year, showListPopinCall, null);
}

/*
var showListPopinCall = {
	success: function(o) {
		  try {
			  	var inner = "";
				var results = YAHOO.lang.JSON.parse(o.responseText);
			    for(var i = 0; i < results.children.length; i++){
				    inner = inner + "<li><a href='/aim/viewActivityPreview.do~pageId=2~activityId=" + results.children[i].ID + "~isPreview=1'>" + results.children[i].name + "</a></li>";//<hr />
	    		}

	    		inner = inner + "<input type='button' value='Close' class='buttonx' onclick='hidePopin()' style='margin-right:10px; margin-top:10px;'>"
			    var msg='\n<digi:trn>List of Activities</digi:trn>';
				myPanel.setHeader(msg);
				var element = document.getElementById("listPopin");
				element.style.display 	= "inline";
				myPanel.setBody(element);
				myPanel.show();
				//var allGraphs = document.getElementsByName("flashContent");
				//for(var idx = 0; idx < allGraphs.length; idx++){
				//	allGraphs[idx].style.display = "none";
				//}
				var div = document.getElementById("popinContent3");
				div.innerHTML = inner;
		  	}
			catch (e) {
			    alert("Invalid respose.");
			}
	  },
	  failure: function(o) {//Fail silently
		}
	};
	*/		    
//-->
</script>

