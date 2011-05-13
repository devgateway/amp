<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

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
		height:350px;
	}
</style>
<!-- Visualization's Scripts-->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/selector/selector-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/flash/swfobject.js"></script>

<script language="javascript">
<!--

// TODO: Move this to individual script file
$D = YAHOO.util.Dom;
$E = YAHOO.util.Event;

var yuiLoadingPanel = function(conf){
    conf = conf == undefined ? new Array() : conf;
    conf.id = conf.id == undefined ? 'yuiLoadingPanel':confi.id;
    conf.header = conf.header == undefined ? 'Loading, please wait...':conf.header;
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
               '<img src="http://us.i1.yimg.com/us.yimg.com/i/us/per/gr/gp/rel_interstitial_loading.gif" />');
               loadingPanel.render(document.body);
               $D.addClass(loadingPanel.id, 'tcc_lightboxLoader');
               var cancelLink = document.createElement('a');
               $D.setStyle(cancelLink, 'cursor', 'pointer');
               cancelLink.appendChild(document.createTextNode('Cancel'));
               $E.on(cancelLink, 'click', function(e, o){
       	           o.self.loadingPanel.hide();
       	           o.self.cancelEvent.fire();
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

-->
</script>

<digi:instance property="visualizationform"/>
<digi:form action="/filters.do">

<!-- BREADCRUMB START -->
<div class="centering">
 <digi:trn>Dashboards</digi:trn><span class="breadcrump_sep"><b>»</b></span>
 	<c:if test="${visualizationform.filter.dashboardType eq '1' }">
 		<span class="bread_sel"><digi:trn>Donor Profile Dashboard</digi:trn></span>
 	</c:if>
 	<c:if test="${visualizationform.filter.dashboardType eq '2' }">
 		<span class="bread_sel"><digi:trn>Region Profile Dashboard</digi:trn></span>
 	</c:if>
 	<c:if test="${visualizationform.filter.dashboardType eq '3' }">
 		<span class="bread_sel"><digi:trn>Sector Profile Dashboard</digi:trn></span>
 	</c:if>
</div>
<br/>
<!-- BREADCRUMB END -->

<!-- POPUPS START -->
<script language="javascript">
<!--

YAHOO.namespace("YAHOO.amp");

var myPanel = new YAHOO.widget.Panel("newPanel", {
	width:"750px",
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

function showPopin() {
	var msg='\n<digi:trn>Advanced Filters</digi:trn>';
	myPanel.setHeader(msg);
	var element = document.getElementById("dialog2");
	element.style.display 	= "inline";
	myPanel.setBody(element);
	myPanel.show();
	changeTab(0);
	var allGraphs = document.getElementsByName("flashContent");
	for(var idx = 0; idx < allGraphs.length; idx++){
		allGraphs[idx].style.display = "none";
	}
}
function hidePopin() {
	myPanel.hide();
	var allGraphs = document.getElementsByName("flashContent");
	for(var idx = 0; idx < allGraphs.length; idx++){
		allGraphs[idx].style.display = "";
	}
}

function showExport() {
	var msg='\n<digi:trn>Export Options</digi:trn>';
	myPanel.setHeader(msg);
	var element = document.getElementById("exportPopin");
	element.style.display 	= "inline";
	myPanel.setBody(element);
	myPanel.show();
	var allGraphs = document.getElementsByName("flashContent");
	for(var idx = 0; idx < allGraphs.length; idx++){
		allGraphs[idx].style.display = "none";
	}
}
function hideExport() {
	myPanel.hide();
	var allGraphs = document.getElementsByName("flashContent");
	for(var idx = 0; idx < allGraphs.length; idx++){
		allGraphs[idx].style.display = "";
	}
}

function doExport(){
	var options = "?";
	options += "typeOpt=" + getOptionChecked("export_type_");
	options += "&summaryOpt=" + getOptionChecked("export_summary_");
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
	} else {
		<digi:context name="url2" property="/visualization/wordExport.do"/>
		document.visualizationform.action="${url2}" + options ;
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
	document.getElementById("org_group_dropdown_ids").selectedIndex = 0;
	document.getElementById("region_dropdown_ids").selectedIndex = 0;
	document.getElementById("sector_dropdown_ids").selectedIndex = 0;
	removeOptions("org_dropdown_ids");
	removeOptions("zone_dropdown_ids");
	removeOptions("sub_sector_dropdown_ids");
	document.getElementById("decimalsToShow_dropdown").selectedIndex = 2;
	document.getElementById("yearsInRange_dropdown").selectedIndex = 4;
	
	document.getElementById("commitments_visible").checked = true;
	document.getElementById("disbursements_visible").checked = true;
	document.getElementById("expenditures_visible").checked = true;
	document.getElementById("pledge_visible").checked = true;
	document.getElementById("workspace_only").checked = false;
	
	document.getElementById("transaction_type_0").value = true;
	document.getElementById("transaction_type_1").value = false;
	document.getElementById("transaction_type_2").value = false;

	applyFilterPopin();
}

function removeOptions (obj){
	var elem = document.getElementById(obj);
	  var i;
	  for (i = elem.length - 1; i>0; i--) {
	    elem.remove(i);
	  }
}

function changeTab (selected){
	document.getElementById("selGeneral").className = "selector_type";
	document.getElementById("selOrgs").className = "selector_type";
	document.getElementById("selRegions").className = "selector_type";
	document.getElementById("selSectors").className = "selector_type";

	document.getElementById("divGeneralFilter").style.display = "none";
	document.getElementById("divOrganizationsFilter").style.display = "none";
	document.getElementById("divSectorsFilter").style.display = "none";
	document.getElementById("divRegionsFilter").style.display = "none";
	
	switch (selected) {
	case 0:
		document.getElementById("selGeneral").className = "selector_type_sel";
		document.getElementById("divGeneralFilter").style.display = "block";
		break;
	case 1:
		document.getElementById("selOrgs").className = "selector_type_sel";
		document.getElementById("divOrganizationsFilter").style.display = "block";
		break;
	case 2:
		document.getElementById("selRegions").className = "selector_type_sel";
		document.getElementById("divRegionsFilter").style.display = "block";
		break;
	case 3:
		document.getElementById("selSectors").className = "selector_type_sel";
		document.getElementById("divSectorsFilter").style.display = "block";
		break;
	default:
		break;
	}
}

-->
</script>
<table>
<tr>
<td>
<div id="dialog2" class="dialog" title="Advanced Filters">
<div id="popinContent" class="content">
	<div id="selectDiv" class="yui-navset">
		<table class="inside" width="100%" height=400 cellpadding="0" cellspacing="0">
			<tr>
				<td width=40% height=25 align="center" background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class="inside"><b class="ins_header">Grouping Selector</b></td>
				<td width=60% background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class="inside" align="center"><b class="ins_header">Grouping Details</b></td>
			</tr>
			<tr>
			    <td class="inside" style="background-color:#F9F9F9;" valign="top">
			    <ul class="yui-nav" style="width: 70%; position: static;">
					<div id="selGeneral" class="selector_type"><div class="selector_type_cont"><a href="javascript:changeTab(0)">General</a></div></div>
					<div id="selOrgs" class="selector_type"><div class="selector_type_cont"><a href="javascript:changeTab(1)">Organizations</a></div></div>
					<div id="selRegions" class="selector_type"><div class="selector_type_cont"><a href="javascript:changeTab(2)">Regions</a></div></div>
					<div id="selSectors" class="selector_type"><div class="selector_type_cont"><a href="javascript:changeTab(3)">Sectors</a></div></div>
				</ul>
				</td>
				<td class="inside" valign="top" style="background-color:#F9F9F9;">
					<div id="divGeneralFilter">
						<div class="selector_content_org_prof" style="line-height:25px;width:490px;">
						<html:checkbox  property="filter.workspaceOnly" styleId="workspace_only"><b><digi:trn>Show Only Data From This Workspace</digi:trn></b></html:checkbox> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" />  &nbsp;&nbsp;
						<b><digi:trn>Decimals to show</digi:trn>: </b>
							<html:select property="filter.decimalsToShow" styleId="decimalsToShow_dropdown" styleClass="dropdwn_sm" style="width:45px;">
	                            <html:option value="0">0</html:option>
	                            <html:option value="1">1</html:option>
	                            <html:option value="2">2</html:option>
	                            <html:option value="3">3</html:option>
	                            <html:option value="4">4</html:option>
	                            <html:option value="5">5</html:option>
	                        </html:select>
						<hr />
						<b>For Time Series Comparison, what data do you want to show?</b> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
						<html:checkbox  property="filter.commitmentsVisible" styleId="commitments_visible"><digi:trn>Commitments</digi:trn>&nbsp;&nbsp;</html:checkbox>
						<html:checkbox  property="filter.disbursementsVisible" styleId="disbursements_visible"><digi:trn>Disbursements</digi:trn>&nbsp;&nbsp;</html:checkbox>
						<html:checkbox  property="filter.expendituresVisible" styleId="expenditures_visible"><digi:trn>Expenditures</digi:trn>&nbsp;&nbsp;</html:checkbox>
						<html:checkbox  property="filter.pledgeVisible" styleId="pledge_visible"><digi:trn>Pledges</digi:trn>&nbsp;&nbsp;</html:checkbox><br />
						<b>What data should the dashboard show? </b><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
						<fieldset>
                            <html:radio property="filter.transactionType" styleId="transaction_type_0" value="0"><digi:trn>Commitments</digi:trn></html:radio>
                            <html:radio property="filter.transactionType" styleId="transaction_type_1" value="1"><digi:trn>Disbursements</digi:trn></html:radio>
                            <html:radio property="filter.transactionType" styleId="transaction_type_2" value="2"><digi:trn>Expenditures</digi:trn></html:radio>
<!--                            <html:radio property="filter.transactionType" value="3"><digi:trn>Pledges</digi:trn></html:radio>-->
                        </fieldset>
						<hr />
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
								  	<td><b>Currency Type:</b></td>
								  	<td>
								  		<html:select property="filter.currencyId" styleId="currencies_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
	           								<html:optionsCollection property="filter.currencies" value="ampCurrencyId" label="currencyName" />
	       								</html:select> 	
									</td>
								   	<td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
								</tr>
							    <tr>
								    <td><b>Fiscal Calendar:</b></td>
								    <td>
								    	 <html:select property="filter.fiscalCalendarId" styleId="fiscalCalendar_dropdown_Id" styleClass="dropdwn_sm" style="width:145px;">
				                            <html:option value="-1"><digi:trn>None</digi:trn></html:option>
				                            <html:optionsCollection property="filter.fiscalCalendars" label="name" value="ampFiscalCalId" />
				                        </html:select>
									</td>
							    	<td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							 	</tr>
							    <tr>
							    	<td><b>Fiscal Year Start:</b></td>
							    	<td>
							    		 <html:select property="filter.year" styleId="year_dropdown" styleClass="dropdwn_sm" style="width:145px;">
				                            <html:optionsCollection property="filter.years" label="wrappedInstance" value="wrappedInstance" />
				                        </html:select>
									</td>
							    	<td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							  	</tr>
				      			<tr>
								    <td><b>Time Scale:</b></td>
								    <td>
										<html:select property="filter.yearsInRange" styleId="yearsInRange_dropdown" styleClass="dropdwn_sm" style="width:145px;">
				                            <html:option value="1">1</html:option>
				                            <html:option value="2">2</html:option>
				                            <html:option value="3">3</html:option>
				                            <html:option value="4">4</html:option>
				                            <html:option value="5">5</html:option>
				                        </html:select>
									</td>
								    <td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
								</tr>
							</table>
						</div>
					</div>
					<div id="divOrganizationsFilter">
						<div class="selector_content_org_prof" style="line-height:25px;width:490px;">
						<b>Organization Group:</b><br />
							<c:if test="${visualizationform.filter.dashboardType eq '1' }">
								<html:select property="filter.orgGroupIds" styleId="org_group_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
	           						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
	          	 					<html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
	       						</html:select> 
       						</c:if>	
       						<c:if test="${visualizationform.filter.dashboardType ne '1' }">
       							<html:select property="filter.orgGroupIds" multiple="true" styleId="org_group_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;max-height:140;">
	           						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
	          	 					<html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
	       						</html:select> 
       						</c:if>
							<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" />
							<hr />
							<div id="divOrgDrpdwn">
							<b>Organization:</b><br />
							<html:select property="filter.orgIds" multiple="true" styleId="org_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;max-height:140;">
           						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
       						</html:select>	
       						</div>	
						</div>
					</div>
					<div id="divRegionsFilter">
						<div class="selector_content_org_prof" style="line-height:25px;width:490px;">
							<b>Region:</b> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
								<c:if test="${visualizationform.filter.dashboardType eq '2' }">
		    						<html:select property="filter.regionIds" styleId="region_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
	           							<html:option value="-1"><digi:trn>All</digi:trn></html:option>
	           							<html:optionsCollection property="filter.regions" value="id" label="name" />
	       							</html:select>
       							</c:if>	
       							<c:if test="${visualizationform.filter.dashboardType ne '2' }">
		    						<html:select property="filter.regionIds" multiple="true" styleId="region_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
	           							<html:option value="-1"><digi:trn>All</digi:trn></html:option>
	           							<html:optionsCollection property="filter.regions" value="id" label="name" />
	       							</html:select>
       							</c:if>	
							<hr />
							<div id="divZoneDrpdwn">
							<b>Zone:</b><br />
								<html:select property="filter.zoneIds" multiple="true" styleId="zone_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
           							<html:option value="-1"><digi:trn>All</digi:trn></html:option>
       							</html:select>		
							<hr />
							</div>
						</div>
					</div>
					<div id="divSectorsFilter">
						<div class="selector_content_org_prof" style="line-height:25px;width:490px;">
							<b>Sector:</b> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
								<c:if test="${visualizationform.filter.dashboardType eq '3' }">
		    						<html:select property="filter.sectorIds" styleId="sector_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
							           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
							           <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
							    	</html:select>
							   	</c:if>	
       							<c:if test="${visualizationform.filter.dashboardType ne '3' }">
		    						<html:select property="filter.sectorIds" multiple="true" styleId="sector_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
							           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
							           <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
							    	</html:select>
							   	</c:if> 	
							<hr />
							<div id="divSubSectorDrpdwn">
							<b>Sub-Sectors:</b><br />
								<html:select property="filter.subSectorIds" multiple="true" styleId="sub_sector_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
           							<html:option value="-1"><digi:trn>All</digi:trn></html:option>
       							</html:select>	
							<hr />
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	</div>

<input type="button" value="Apply" class="buttonx" style="margin-right:10px; margin-top:10px;" id="applyButtonPopin">
<input type="button" value="Reset to defaults" onclick="resetToDefaults()" class="buttonx" style="margin-right:10px; margin-top:10px;">
<input type="button" value="Close" class="buttonx" onclick="hidePopin()" style="margin-right:10px; margin-top:10px;">


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
				<td class="inside" width="45%" >
				<div class="selector_type"><b><digi:trn>Export Type</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.typeOpt" styleId="export_type_0" value="0" ><digi:trn>PDF</digi:trn>  </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_pdf.gif" "><br />
		            <html:radio property="exportData.typeOpt" styleId="export_type_1" value="1"><digi:trn>Word</digi:trn>   </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_word.gif" "><br />
		        </div>
		        </td>
		        <td class="inside" width="45%" >
				<div class="selector_type"><b><digi:trn>Summary</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.summaryOpt" styleId="export_summary_0" value="0"><digi:trn>Exclude Summary</digi:trn></html:radio><br />
		            <html:radio property="exportData.summaryOpt" styleId="export_summary_1" value="1"><digi:trn>Inculde Summary</digi:trn></html:radio><br />
		        </div>
		        </td>
		    </tr>
		    <tr>
				<td class="inside" width="45%" >
				<div class="selector_type"><b><digi:trn>Funding</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.fundingOpt" styleId="export_funding_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		        <td class="inside" width="45%" >
				<div class="selector_type"><b><digi:trn>Aid Predictability</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidPredicOpt" styleId="export_aid_pred_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		    </tr>
		    <tr>
				<td class="inside" width="45%" >
				<div class="selector_type"><b><digi:trn>Aid Type</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.aidTypeOpt" styleId="export_aid_type_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		        <td class="inside" width="45%" >
				<div class="selector_type"><b><digi:trn>Financing Instrument</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
		            <html:radio property="exportData.financingInstOpt" styleId="export_fin_inst_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
		        </div>
		        </td>
		    </tr>
		    <tr>
				<c:if test="${visualizationform.filter.dashboardType ne '1' }">
    			<td class="inside" width="45%" >
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
    			<td class="inside" width="45%" >
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
    			<td class="inside" width="45%" >
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

<input type="button" value="Export" class="buttonx" onclick="doExport()" style="margin-right:10px; margin-top:10px;">
<input type="button" value="Close" class="buttonx" onclick="hideExport()" style="margin-right:10px; margin-top:10px;">

</div>
</td>
</tr>
</table>

<!-- MAIN CONTENT PART START -->

<html:hidden property="filter.decimalsToShow" styleId="decimalsToShow" />
<html:hidden property="filter.year" styleId="currentYear"/>
<html:hidden property="filter.yearsInRange" styleId="yearsInRange" />
<html:hidden property="filter.dashboardType" styleId="dashboardType" />
<html:hidden property="filter.workspaceOnly" styleId="workspaceOnly"/>
<html:hidden property="filter.commitmentsVisible" styleId="commitmentsVisible"/>
<html:hidden property="filter.disbursementsVisible" styleId="disbursementsVisible" />
<html:hidden property="filter.expendituresVisible" styleId="expendituresVisible" />
<html:hidden property="filter.pledgeVisible" styleId="pledgeVisible"/>
<html:hidden property="filter.transactionType" styleId="transactionType" />
<html:hidden property="filter.currencyId" styleId="currencyId" />
<html:hidden property="filter.fiscalCalendarId" styleId="fiscalCalendarId" />

<div class="dashboard_header">
<!--<div class="dashboard_total"><b class="dashboard_total_num">${visualizationform.summaryInformation.totalCommitments}</b><br /><digi:trn>Total Commitments</digi:trn> ( ${visualizationform.filter.currencyId} )</div>-->
<div class="dashboard_total" id="divTotalComms"></div>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    	<div class="dashboard_name" id="dashboard_name">
    		<c:if test="${visualizationform.filter.dashboardType eq '1' }">
    			<digi:trn>ALL DONORS</digi:trn>
    		</c:if>
    		<c:if test="${visualizationform.filter.dashboardType eq '3' }">
    			<digi:trn>ALL SECTORS</digi:trn>
    		</c:if>
    		<c:if test="${visualizationform.filter.dashboardType eq '2' }">
    			<digi:trn>ALL REGIONS</digi:trn>
    		</c:if>
    	</div>
    </td>
    <td><div class="dash_ico"><img src="/TEMPLATE/ampTemplate/img_2/ico_export.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href="javascript:showExport()" class="l_sm">Export Options</a></div></div></td>
  </tr>
</table>
<div class="dashboard_stat" id="divSummaryInfo" ></div>
<!--<div class="dashboard_stat">Total Disbursements: <div id="divTotalDisbs"></div> <span class="breadcrump_sep">|</span>Total Number of Projects: <div id="divNumOfProjs"></div><span class="breadcrump_sep">|</span>Total Number of Sectors: <div id="divNumOfSecs"></div><span class="breadcrump_sep">|</span>Total Number of Regions: <div id="divNumOfRegs"></div><span class="breadcrump_sep">|</span>Average Project Size: <div id="divAvgProjSize"></div></div>-->

</div>
</center>
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:15px;">
  <tr>
    <td width=296 bgcolor="#F4F4F4" valign="top">
	<div style="background-color:#FFFFFF; height:7px;">&nbsp;</div>
	<div class="dash_left">
	<fieldset>
	<legend><span class=legend_label>Quick Filter</span></legend>
<!--	<html:checkbox property="filter.workspaceOnly" styleId="workspace_only"><digi:trn>Show Only Data From This Workspace</digi:trn></html:checkbox>-->
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
	<input type="button" value="Filter" class="buttonx" style="margin-top:10px;" id="applyButton">
	<hr />
	<div class="tab_opt"><div class="tab_opt_cont"><a href="javascript:showPopin()" class="l_sm">Advanced Filters</a></div></div>
	</center>
</fieldset>
	
<fieldset>
	<legend><span class=legend_label>Top Projects</span></legend>
	<div id="divTopProjects" class="field_text">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.topProjects}" var="projectItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${projectItem.key}"/>  <b>($<c:out value="${projectItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:showFullProjects()" style="float:right;">View Full List</a>
	</div>
	<div id="divFullProjects" class="field_text" style="display: none;">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.fullProjects}" var="projectItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${projectItem.key}"/>  <b>($<c:out value="${projectItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:hideFullProjects()" style="float:right;">View Top List</a>
	</div>
</fieldset>
<c:if test="${visualizationform.filter.dashboardType ne '1' }">
	<fieldset>
		<legend><span class=legend_label>Top Donors</span></legend>
		<div id="divTopDonors" class="field_text">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.topDonors}" var="donorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:showFullDonors()" style="float:right;">View Full List</a>
		</div>
		<div id="divFullDonors" class="field_text" style="display: none;">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.fullDonors}" var="donorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:hideFullDonors()" style="float:right;">View Top List</a>
		</div>
	</fieldset>	
</c:if>
<c:if test="${visualizationform.filter.dashboardType ne '3' }">
	<fieldset>
		<legend><span class=legend_label>Top Sectors</span></legend>
		<div id="divTopSectors" class="field_text">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.topSectors}" var="sectorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:showFullSectors()" style="float:right;">View Full List</a>
		</div>
		<div id="divFullSectors" class="field_text" style="display: none;">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.fullSectors}" var="sectorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:hideFullSectors()" style="float:right;">View Top List</a>
		</div>
	</fieldset>	
</c:if>
<c:if test="${visualizationform.filter.dashboardType ne '2' }">
	<fieldset>
		<legend><span class=legend_label>Top Regions</span></legend>
		<div id="divTopRegions" class="field_text">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.topRegions}" var="regionItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${regionItem.key}"/>  <b>($<c:out value="${regionItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:showFullRegions()" style="float:right;">View Full List</a>
		</div>
		<div id="divFullRegions" class="field_text" style="display: none;">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.fullRegions}" var="regionItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${regionItem.key}"/>  <b>($<c:out value="${regionItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:hideFullRegions()" style="float:right;">View Top List</a>
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
<div id="demo" class="yui-navset">
	<ul class="yui-nav">
		<li><a href="#tab1"><div>Visualization</div></a></li>
		<li><a href="#tab2"><div>Contact Information</div></a></li>
		<li><a href="#tab3"><div>Additional Notes</div></a></li>
	</ul>
	<div class="yui-content">
	<div id="tab1">
		<fieldset>
			<legend><span id="fundingChartTitle" class=legend_label></span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'FundingChart')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'FundingChart')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'FundingChart')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="FundingChart">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span id="aidPredChartTitle" class=legend_label></span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'AidPredictability')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'AidPredictability')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'AidPredictability')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="AidPredictability">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span id="aidTypeChartTitle" class=legend_label></span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'AidType')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'AidType')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'AidType')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'AidType')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="AidType">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span id="finInstChartTitle" class=legend_label></span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'FinancingInstrument')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'FinancingInstrument')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'FinancingInstrument')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'FinancingInstrument')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="FinancingInstrument">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<c:if test="${visualizationform.filter.dashboardType ne '1' }">
 			<fieldset>
				<legend><span id="donorChartTitle" class=legend_label></span></legend>
				<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'DonorProfile')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'DonorProfile')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'DonorProfile')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'DonorProfile')">Data View</a></div>
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="DonorProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
			</fieldset>
		</c:if>
		<c:if test="${visualizationform.filter.dashboardType ne '3' }">
			<fieldset>
				<legend><span id="sectorChartTitle" class=legend_label></span></legend>
				<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'SectorProfile')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'SectorProfile')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'SectorProfile')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'SectorProfile')">Data View</a></div>
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="SectorProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
			</fieldset>
		</c:if>
		<c:if test="${visualizationform.filter.dashboardType ne '2' }">
			<fieldset>
				<legend><span id="regionChartTitle" class=legend_label></span></legend>
				<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'RegionProfile')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'RegionProfile')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'RegionProfile')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'RegionProfile')">Data View</a></div>
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="RegionProfile">
						<a href="http://www.adobe.com/go/getflashplayer">
							<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
			</fieldset>
		</c:if>
	</div>
	<div id="tab2">
		Morbi tincidunt, dui sit amet facilisis feugiat, odio metus gravida ante, ut pharetra massa metus id nunc. Duis scelerisque molestie turpis. Sed fringilla, massa eget luctus malesuada, metus eros molestie lectus, ut tempus eros massa ut dolor. Aenean aliquet fringilla sem. Suspendisse sed ligula in ligula suscipit aliquam. Praesent in eros vestibulum mi adipiscing adipiscing. Morbi facilisis. Curabitur ornare consequat nunc. Aenean vel metus. Ut posuere viverra nulla. Aliquam erat volutpat. Pellentesque convallis. Maecenas feugiat, tellus pellentesque pretium posuere, felis lorem euismod felis, eu ornare leo nisi vel felis. Mauris consectetur tortor et purus.
	</div>
	<div id="tab3">
		Mauris eleifend est et turpis. Duis id erat. Suspendisse potenti. Aliquam vulputate, pede vel vehicula accumsan, mi neque rutrum erat, eu congue orci lorem eget lorem. Vestibulum non ante. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce sodales. Quisque eu urna vel enim commodo pellentesque. Praesent eu risus hendrerit ligula tempus pretium. Curabitur lorem enim, pretium nec, feugiat nec, luctus a, lacus.
		<p>Duis cursus. Maecenas ligula eros, blandit nec, pharetra at, semper at, magna. Nullam ac lacus. Nulla facilisi. Praesent viverra justo vitae neque. Praesent blandit adipiscing velit. Suspendisse potenti. Donec mattis, pede vel pharetra blandit, magna ligula faucibus eros, id euismod lacus dolor eget odio. Nam scelerisque. Donec non libero sed nulla mattis commodo. Ut sagittis. Donec nisi lectus, feugiat porttitor, tempor ac, tempor vitae, pede. Aenean vehicula velit eu tellus interdum rutrum. Maecenas commodo. Pellentesque nec elit. Fusce in lacus. Vivamus a libero vitae lectus hendrerit hendrerit.</p>
	</div>
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
			    		orgDropdown.options[0] = new Option("All", -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
				    case "Sector":
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
			    		subSectorDropdown.options.length = 0;
			    		subSectorDropdown.options[0] = new Option("All", -1);
			    		for(var i = 1; i < results.children.length; i++){
			    			subSectorDropdown.options[i] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Region":
			    		var zonesDropdown = document.getElementById("zone_dropdown_id");
			    		zonesDropdown.options.length = 0;
			    		zonesDropdown.options[0] = new Option("All", -1);
			    		for(var i = 1; i < results.children.length; i++){
			    			zonesDropdown.options[i] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Organizations":
			    		var orgDropdown = document.getElementById("org_dropdown_ids");
			    		orgDropdown.options.length = 0;
			    		orgDropdown.options[0] = new Option("All", -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
				    case "Sectors":
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_ids");
			    		subSectorDropdown.options.length = 0;
			    		subSectorDropdown.options[0] = new Option("All", -1);
			    		for(var i = 1; i < results.children.length; i++){
			    			subSectorDropdown.options[i] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Regions":
			    		var zonesDropdown = document.getElementById("zone_dropdown_ids");
			    		zonesDropdown.options.length = 0;
			    		zonesDropdown.options[0] = new Option("All", -1);
			    		for(var i = 1; i < results.children.length; i++){
			    			zonesDropdown.options[i] = new Option(results.children[i].name, results.children[i].ID);
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
	if (e == undefined){
		parentId = this.value;
		targetId = this.id;
		targetObj = this;
	}
	else
	{
		parentId = e.target.value;
		targetId = e.target.id;
		targetObj = e.target;
	}
	
	var objectType = "";

	switch(targetId){
		case "sector_dropdown_id":
			objectType = "Sector";
			break;
		case "region_dropdown_id":
			objectType = "Region";
			break;
		case "org_group_dropdown_id":
			objectType = "Organization";
			break;
		case "sector_dropdown_ids":
			if (countSelected(targetObj) > 1) {
				document.getElementById("divSubSectorDrpdwn").style.display = "none";
			} else {
				document.getElementById("divSubSectorDrpdwn").style.display = "block";
			}
			objectType = "Sectors";
			break;
		case "region_dropdown_ids":
			if (countSelected(targetObj) > 1) {
				document.getElementById("divZoneDrpdwn").style.display = "none";
			} else {
				document.getElementById("divZoneDrpdwn").style.display = "block";
			}
			objectType = "Regions";
			break;
		case "org_group_dropdown_ids":
			if (countSelected(targetObj) > 1) {
				document.getElementById("divOrgDrpdwn").style.display = "none";
			} else {
				document.getElementById("divOrgDrpdwn").style.display = "block";
			}
			objectType = "Organizations";
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
				  refreshBoxes(o);
				  refreshGraphs();
				}
				catch (e) {
				    //alert("Invalid response.");
				}
				//Get array of graphs
				var allGraphs = document.getElementsByName("flashContent");
				
				//Iterate and refresh the graph
				for(var idx = 0; idx < allGraphs.length; idx++){
					// Get flash object and refresh it by calling internal
					allGraphs[idx].style.display = "";
				}
			  loadingPanel.hide();
		  },
		  failure: function(o) {
			  loadingPanel.hide();
		  }
		};

function callbackApplyFilter(e){
	//Get array of graphs
	var allGraphs = document.getElementsByName("flashContent");
	
	//Iterate and refresh the graph
	for(var idx = 0; idx < allGraphs.length; idx++){
		// Get flash object and refresh it by calling internal
		allGraphs[idx].style.display = "none";
	}
	loadingPanel.show();
	YAHOO.util.Connect.setForm('visualizationform');

	var sUrl="/visualization/dataDispatcher.do?action=applyFilter";

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
}

function applyFilterPopin(e){
	
var allGraphs = document.getElementsByName("flashContent");

	document.getElementById("decimalsToShow").value = document.getElementById("decimalsToShow_dropdown").options[document.getElementById("decimalsToShow_dropdown").selectedIndex].value;
	document.getElementById("currentYear").value = document.getElementById("year_dropdown").options[document.getElementById("year_dropdown").selectedIndex].value;
	document.getElementById("yearsInRange").value = document.getElementById("yearsInRange_dropdown").options[document.getElementById("yearsInRange_dropdown").selectedIndex].value;
	document.getElementById("currencyId").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("fiscalCalendarId").value = document.getElementById("fiscalCalendar_dropdown_Id").options[document.getElementById("fiscalCalendar_dropdown_Id").selectedIndex].value;

	document.getElementById("commitmentsVisible").value = document.getElementById("commitments_visible").checked;
	document.getElementById("disbursementsVisible").value = document.getElementById("disbursements_visible").checked;
	document.getElementById("expendituresVisible").value = document.getElementById("expenditures_visible").checked;
	document.getElementById("pledgeVisible").value = document.getElementById("pledge_visible").checked;
	document.getElementById("workspaceOnly").value = document.getElementById("workspace_only").checked;
	
	if (document.getElementById("transaction_type_0").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_0").value;
	}
	if (document.getElementById("transaction_type_1").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_1").value;
	}
	if (document.getElementById("transaction_type_2").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_2").value;
	}
	
	var params = "";
	params = params + "&orgGroupIds=" + getSelectionsFromElement("org_group_dropdown_ids");
	params = params + "&orgIds=" + getSelectionsFromElement("org_dropdown_ids");
	params = params + "&regionIds=" + getSelectionsFromElement("region_dropdown_ids");
	params = params + "&zoneIds=" + getSelectionsFromElement("zone_dropdown_ids");
	params = params + "&sectorIds=" + getSelectionsFromElement("sector_dropdown_ids");
	params = params + "&subSectorIds=" + getSelectionsFromElement("sub_sector_dropdown_ids");
	
	for(var idx = 0; idx < allGraphs.length; idx++){
		allGraphs[idx].style.display = "none";
	}
	loadingPanel.show();
	YAHOO.util.Connect.setForm('visualizationform');
	var sUrl="/visualization/dataDispatcher.do?action=applyFilter" + params;

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
	hidePopin();
}

function getSelectionsFromElement(elementId){
	var sels = "";
	var cnt = 0;
	var elem=document.getElementById(elementId).options;
	for(i=0;i<elem.length;i++){
		if (elem[i].selected==true){
			if(sels != ""){
				sels = sels + ",";
			}
			sels = sels + elem[i].value;
			cnt++;
		}
	}
	return sels;
}

function refreshGraphs(){

	//Get array of graphs
	var allGraphs = document.getElementsByName("flashContent");
	
	//Iterate and refresh the graph
	for(var idx = 0; idx < allGraphs.length; idx++){
		// Get flash object and refresh it by calling internal
		allGraphs[idx].children[0].refreshGraph();
	}
}
function refreshBoxes(o){

	var dashboardType = document.getElementById("dashboardType").value;
	var results = YAHOO.lang.JSON.parse(o.responseText);
	var inner = "";
	var trnTotalDisbs="<digi:trn jsFriendly='true'>Total Disbursements</digi:trn>: ";
	var trnNumOfProjs="<digi:trn jsFriendly='true'>Total Number of Projects</digi:trn>: ";
	var trnNumOfDons="<digi:trn jsFriendly='true'>Total Number of Donors</digi:trn>: ";
	var trnNumOfSecs="<digi:trn jsFriendly='true'>Total Number of Sectors</digi:trn>: ";
	var trnNumOfRegs="<digi:trn jsFriendly='true'>Total Number of Regions</digi:trn>: ";
	var trnAvgProjSize="<digi:trn jsFriendly='true'>Average Project Size</digi:trn>: ";
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
	var trnRegionProfile="<digi:trn jsFriendly='true'>Region Profile</digi:trn>";
	
	for(var j = 0; j < results.children.length; j++){
		var child = results.children[j];
		switch(child.type){
			case "ProjectsList":
				inner = "<a href='javascript:hideFullProjects()' style='float:right;'>Hide Full List</a> <hr />";
				for(var i = 0; i < child.list.length; i++){
					inner = inner + (i+1) + ". " + child.list[i].name + "  <b>" + child.list[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:hideFullProjects()' style='float:right;'>Hide Full List</a>";
				var div = document.getElementById("divFullProjects");
				div.innerHTML = inner;
				inner = "";
				for(var i = 0; i < child.top.length; i++){
					inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:showFullProjects()' style='float:right;'>Show Full List</a>";
				var div = document.getElementById("divTopProjects");
				div.innerHTML = inner;
				break;
			case "DonorsList":
				if (dashboardType!=1) {
					inner = "<a href='javascript:hideFullDonors()' style='float:right;'>Hide Full List</a> <hr />";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + (i+1) + ". " + child.list[i].name + "  <b>" + child.list[i].value + "</b> <hr />";
					}
					inner = inner + "<a href='javascript:hideFullDonors()' style='float:right;'>Hide Full List</a>";
					var div = document.getElementById("divFullDonors");
					div.innerHTML = inner;
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullDonors()' style='float:right;'>Show Full List</a>";
					var div = document.getElementById("divTopDonors");
					div.innerHTML = inner;
				}
				break;
			case "SectorsList":
				if (dashboardType!=3) {
					inner = "<a href='javascript:hideFullSectors()' style='float:right;'>Hide Full List</a> <hr />";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + (i+1) + ". " + child.list[i].name + "  <b>" + child.list[i].value + "</b> <hr />";
					}
					inner = inner + "<a href='javascript:hideFullSectors()' style='float:right;'>Hide Full List</a>";
					var div = document.getElementById("divFullSectors");
					div.innerHTML = inner;
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullSectors()' style='float:right;'>Show Full List</a>";
					var div = document.getElementById("divTopSectors");
					div.innerHTML = inner;
				}
				break;
			case "RegionsList":
				if (dashboardType!=2) {
					inner = "<a href='javascript:hideFullRegions()' style='float:right;'>Hide Full List</a> <hr />";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + (i+1) + ". " + child.list[i].name + "  <b>" + child.list[i].value + "</b> <hr />";
					}
					inner = inner + "<a href='javascript:hideFullRegions()' style='float:right;'>Hide Full List</a>";
					var div = document.getElementById("divFullRegions");
					div.innerHTML = inner;
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullRegions()' style='float:right;'>Show Full List</a>";
					var div = document.getElementById("divTopRegions");
					div.innerHTML = inner;
				}
				break;
			case "SelOrgGroupsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("org_group_list_id");
					div.innerHTML = inner;
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
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("org_list_id");
					div.innerHTML = inner;
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
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("region_list_id");
					div.innerHTML = inner;
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
			case "SelSectorsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("sector_list_id");
					div.innerHTML = inner;
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
				inner = "<b class='dashboard_total_num'>" + child.value + "</b><br /><digi:trn>Total Commitments</digi:trn>(" + child.curr + ")";
				var div = document.getElementById("divTotalComms");
				div.innerHTML = inner;
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
				
		}
	}
	inner = trnTotalDisbs + "<b>" + valTotalDisbs + "</b><span class='breadcrump_sep'>|</span>";
	inner = inner + trnNumOfProjs + "<b>" + valNumOfProjs + "</b><span class='breadcrump_sep'>|</span>";
	if (dashboardType!=1) {
		inner = inner + trnNumOfDons + "<b>" + valNumOfDons + "</b><span class='breadcrump_sep'>|</span>";
	}
	if (dashboardType!=3) {
		inner = inner + trnNumOfSecs + "<b>" + valNumOfSecs + "</b><span class='breadcrump_sep'>|</span>";
	}
	if (dashboardType!=2) {
		inner = inner + trnNumOfRegs + "<b>" + valNumOfRegs + "</b><span class='breadcrump_sep'>|</span>";
	}
	inner = inner + trnAvgProjSize + "<b>" + valAvgProjSize;
	var div = document.getElementById("divSummaryInfo");
	div.innerHTML = inner;

	var namePlaceholder = document.getElementById("dashboard_name");
	if (dashboardType==1) {
		var name1 = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
		var name2 = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
		namePlaceholder.innerHTML = name1 + "<br/><span style=\"font-size:16px\">" + name2 + "</span>";
	}
	if (dashboardType==3) {
		var name1 = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
		var name2 = document.getElementById("sub_sector_dropdown_id").options[document.getElementById("sub_sector_dropdown_id").selectedIndex].text;
		namePlaceholder.innerHTML = name1 + "<br/><span style=\"font-size:16px\">" + name2 + "</span>";
	}
	if (dashboardType==2) {
		var name1 = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
		var name2 = document.getElementById("zone_dropdown_id").options[document.getElementById("zone_dropdown_id").selectedIndex].text;
		namePlaceholder.innerHTML = name1 + "<br/><span style=\"font-size:16px\">" + name2 + "</span>";
	}

	div = document.getElementById("fundingChartTitle");
	inner = "";
	if (document.getElementById("commitments_visible").checked==true) {
		inner = inner + trnCommitments + " - ";
		}
	if (document.getElementById("disbursements_visible").checked==true) {
		inner = inner + trnDisbursements + " - ";
		}
	if (document.getElementById("expenditures_visible").checked==true) {
		inner = inner + trnExpenditures + " - ";
		}
	if (document.getElementById("pledge_visible").checked==true) {
		inner = inner + trnPledges;
	}
	div.innerHTML = inner;

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
	div = document.getElementById("aidPredChartTitle");
	inner = trnAidPredictability + " - " + fundType;
	div.innerHTML = inner;

	div = document.getElementById("aidTypeChartTitle");
	inner = trnAidType + " - " + fundType;
	div.innerHTML = inner;

	var div = document.getElementById("finInstChartTitle");
	inner = trnFinancingInstrument + " - " + fundType;
	div.innerHTML = inner;

	if (dashboardType!=1) {
		div = document.getElementById("donorChartTitle");
		inner = trnDonorProfile + " - " + fundType;
		div.innerHTML = inner;
	}
	if (dashboardType!=3) {
		div = document.getElementById("sectorChartTitle");
		inner = trnSectorProfile + " - " + fundType;
		div.innerHTML = inner;
	}
	if (dashboardType!=2) {
		div = document.getElementById("regionChartTitle");
		inner = trnRegionProfile + " - " + fundType;
		div.innerHTML = inner;
	}
	
}


YAHOO.util.Event.addListener("region_dropdown_ids", "change", callbackChildren);
YAHOO.util.Event.onAvailable("region_dropdown_ids", callbackChildren);
YAHOO.util.Event.addListener("org_group_dropdown_ids", "change", callbackChildren);
YAHOO.util.Event.onAvailable("org_group_dropdown_ids", callbackChildren);
YAHOO.util.Event.addListener("sector_dropdown_ids", "change", callbackChildren);
YAHOO.util.Event.onAvailable("sector_dropdown_ids", callbackChildren);
YAHOO.util.Event.addListener("region_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("region_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("org_group_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("sector_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("sector_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("applyButton", "click", callbackApplyFilter);
YAHOO.util.Event.addListener("applyButtonPopin", "click", applyFilterPopin);
YAHOO.util.Event.onDOMReady(initDashboard);
window.onload=initPanel;

function initDashboard(){
	//Initialize First Chart
	var dashboardType = document.getElementById("dashboardType").value;
	changeChart(null, 'bar', 'FundingChart');
	changeChart(null, 'bar', 'AidPredictability');
	changeChart(null, 'bar', 'AidType');
	changeChart(null, 'bar', 'FinancingInstrument');
	if (dashboardType!=1) {
		changeChart(null, 'bar', 'DonorProfile');
	}
	if (dashboardType!=3) {
		changeChart(null, 'bar', 'SectorProfile');
	}
	if (dashboardType!=2) {
		changeChart(null, 'bar', 'RegionProfile');
	}
	callbackApplyFilter();
}

function changeChart(e, chartType, container){
	//Get the calling object to select it and remove style.
	if(e != null){
		var caller = e.target || e.srcElement;
		var linkBar = caller.parentNode.getElementsByTagName("A");
	    for(var i in linkBar)
	    {
	    	linkBar[i].className = "";
	    }
	    caller.className = "sel_sm_b";
	}
	var currentYear = document.getElementById("currentYear").value;
	var yearsInRange = document.getElementById("yearsInRange").value;
	var minSlider =  "" + (currentYear - yearsInRange + 1);
	var maxSlider =  "" + currentYear;
	var flashvars = { minSlider: minSlider, maxSlider: maxSlider };

	var params = {};
	var attributes = {};
	attributes.id = container;
	switch(chartType){
		case "bar":
			swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
		case "donut":
			swfobject.embedSWF("/repository/visualization/view/charts/PieChart_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
		case "line":
			swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
		case "dataview":
			swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
	}
	return false;
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


//-->
</script>

