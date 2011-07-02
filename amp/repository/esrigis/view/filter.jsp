<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/category" prefix="category"%>

<digi:instance property="datadispatcherform"/>
<digi:form action="/mainmap.do">
<table>
	<tr>
	<td>
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
						<fieldset>
							<b><digi:trn>Type of funding for highlighting</digi:trn></b>&nbsp;&nbsp;<br/>
                           <html:radio property="filter.transactionType" styleId="transaction_type_0" value="0"><digi:trn>Commitments</digi:trn></html:radio>
                           <html:radio property="filter.transactionType" styleId="transaction_type_1" value="1"><digi:trn>Disbursements</digi:trn></html:radio>
                           <html:radio property="filter.transactionType" styleId="transaction_type_2" value="2"><digi:trn>Expenditures</digi:trn></html:radio>
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
			      			<tr>
							    <td><b>Project Status:</b></td>
							    <td>
									<category:showoptions
										outerstyle="width: 145px" styleClass="dropdwn_sm"
										property="filter.projectStatusId" size="3"
										name="datadispatcherform" multiselect="false"
										keyName="<%=org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY%>" />
								</td>
							    <td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							</tr>
			      			<tr>
							    <td><b>Type of Assistance:</b></td>
							    <td>
									<category:showoptions
										outerstyle="width: 145px" styleClass="dropdwn_sm"
										name="datadispatcherform"
										property="filter.typeAssistanceId" multiselect="false"
										keyName="<%=org.digijava.module.categorymanager.util.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" />
								</td>
							    <td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							</tr>
			      			<tr>
							    <td><b>Financing Instrument:</b></td>
							    <td>
									<category:showoptions
										outerstyle="width: 145px" styleClass="dropdwn_sm"
										name="datadispatcherform"
										property="filter.financingInstrumentId" multiselect="false"
										keyName="<%=org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" />
								</td>
							    <td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							</tr>
			      			<tr>
							    <td><b>Structure Types:</b></td>
							    <td>
						    		<html:select property="filter.selStructureTypes" styleId="structureTypes_dropdown" styleClass="dropdwn_sm" style="width:145px;" multiple="true">
			                            <html:optionsCollection property="filter.structureTypes" label="name" value="typeId" />
			                        </html:select>
								</td>
							    <td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							</tr>
			      			<!-- 
			      			<tr>
							    <td><b>Funding Limit:</b></td>
							    <td>
							  		<html:text property="filter.fundingLimit" styleId="fundingLimit_value" styleClass="dropdwn_sm" style="width:145px;"/>
									<html:checkbox  property="filter.fundingLimitAbove" styleId="fundingLimit_checkbox">Above this value</html:checkbox>
								</td>
							    <td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							</tr>
							-->
			      			<tr>
							    <td><b>Only on budget projects:</b></td>
							    <td>
									<html:checkbox  property="filter.onBudget" styleId="fundingLimit_checkbox"></html:checkbox>
								</td>
							    <td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div id="divOrganizationsFilter" style="width:490px;">
					<div class="selector_content_org_prof" style="line-height:25px;width:245px;float:left;">
					Funding<br/>
					<b>Organization Group:</b><br />
						<html:select multiple="true" property="filter.orgGroupIds" styleId="org_group_dropdown_ids" styleClass="dropdwn_sm" style="width:200px;">
	        				<html:option value="-1"><digi:trn>All</digi:trn></html:option>
	       	 				<html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
	    				</html:select> 
	    				<br>
						<div id="divOrgDrpdwn">
						<b>Organization:</b><br />
						<html:select multiple="true	" property="filter.orgIds"  styleId="org_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;max-height:140;">
          						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
      						</html:select>	
      					</div>	
					</div>
					<div class="selector_content_org_prof" style="line-height:25px;width:245px;float:right;" >
					Implementing Agency <br/>
					<b>Organization Group:</b><br />
						<html:select multiple="true" property="filter.implOrgGroupIds" styleId="imp_org_group_dropdown_ids" styleClass="dropdwn_sm" style="width:200px;">
	        				<html:option value="-1"><digi:trn>All</digi:trn></html:option>
	       	 				<html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
	    				</html:select> 
	    				<br>
						<div id="divOrgDrpdwn2">
						<b>Organization:</b><br />
						<html:select multiple="true	" property="filter.implOrgIds"  styleId="imp_org_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;max-height:140;">
          						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
      						</html:select>	
      						</div>	
					</div>
					<div class="selector_content_org_prof" style="line-height:25px;width:490px;clear:both;" >
						<hr/>
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
							  	<td><b>Organization Type:</b></td>
							  	<td>
							  		<html:select property="filter.organizationsTypeId" styleId="organization_type_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
		          						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
           								<html:optionsCollection property="filter.organizationsType" value="ampOrgTypeId" label="orgType" />
       								</html:select> 	
								</td>
							   	<td><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div id="divRegionsFilter">
					<div class="selector_content_org_prof" style="line-height:25px;width:490px;">
						<b>Region:</b> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
							<html:select property="filter.regionIds" multiple="true" styleId="region_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
        						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
        						<html:optionsCollection property="filter.regions" value="id" label="name" />
    						</html:select>
      					<hr/>
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
						<b>Sector:</b> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br/>
							<html:select property="filter.sectorIds" styleId="sector_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
					           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
					           <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
					    	</html:select>
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
		<tr>
			<td width=40% height=25 align="center" class="inside" style="background-color:white;">&nbsp;</td>
			<td width=60% class="inside" align="center" style="background-color:white;">
				<input type="button" value="Filter" class="buttonx" style="margin-top:10px;" id="applyButton">
				<input type="button" value="Reset" onclick="resetToDefaults()" class="buttonx" style="margin-right:10px; margin-top:10px;">
			</td>
		</tr>
	</table>
</td>
</tr>
</table>
<html:hidden property="filter.decimalsToShow" styleId="decimalsToShow" />
<html:hidden property="filter.year" styleId="currentYear"/>
<html:hidden property="filter.yearsInRange" styleId="yearsInRange" />
<html:hidden property="filter.workspaceOnly" styleId="workspaceOnly"/>
<html:hidden property="filter.commitmentsVisible" styleId="commitmentsVisible"/>
<html:hidden property="filter.disbursementsVisible" styleId="disbursementsVisible" />
<html:hidden property="filter.expendituresVisible" styleId="expendituresVisible" />
<html:hidden property="filter.pledgeVisible" styleId="pledgeVisible"/>
<html:hidden property="filter.transactionType" styleId="transactionType" />
<html:hidden property="filter.currencyId" styleId="currencyId" />
<html:hidden property="filter.fiscalCalendarId" styleId="fiscalCalendarId" />
</digi:form>
<script type="text/javascript">

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
		case "imp_org_group_dropdown_ids":
			if (countSelected(targetObj) > 1) {
				document.getElementById("divOrgDrpdwn2").style.display = "none";
			} else {
				document.getElementById("divOrgDrpdwn2").style.display = "block";
			}
			objectType = "ImplementingOrganizations";
			break;
	}

	if (parentId != "" && objectType != ""){
		var transaction = YAHOO.util.Connect.asyncRequest('GET', "/esrigis/datadispatcher.do?jsonobject=true&objectType=" + objectType + "&parentId=" + parentId, callbackChildrenCall, null);
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
				  getActivities(true);
				  getStructures(true);
				}
				catch (e) {
				    alert("Invalid response.");
				}
				//Get array of graphs
				//var allGraphs = document.getElementsByName("flashContent");
				
				//Iterate and refresh the graph
				//for(var idx = 0; idx < allGraphs.length; idx++){
					// Get flash object and refresh it by calling internal
					//allGraphs[idx].style.display = "";
				//}
			  hideLoading();
		  },failure: function(o) {
			  hideLoading();
		  }
		};


function callbackApplyFilter(e){
	document.getElementById("decimalsToShow").value = document.getElementById("decimalsToShow_dropdown").options[document.getElementById("decimalsToShow_dropdown").selectedIndex].value;
	document.getElementById("currentYear").value = document.getElementById("year_dropdown").options[document.getElementById("year_dropdown").selectedIndex].value;
	document.getElementById("yearsInRange").value = document.getElementById("yearsInRange_dropdown").options[document.getElementById("yearsInRange_dropdown").selectedIndex].value;
	document.getElementById("currencyId").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("fiscalCalendarId").value = document.getElementById("fiscalCalendar_dropdown_Id").options[document.getElementById("fiscalCalendar_dropdown_Id").selectedIndex].value;
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
	params = params + "&implOrgGroupIds=" + getSelectionsFromElement("imp_org_group_dropdown_ids");
	params = params + "&organizationsTypeId=" + getSelectionsFromElement("organization_type_dropdown_ids");
	params = params + "&orgIds=" + getSelectionsFromElement("org_dropdown_ids");
	params = params + "&regionIds=" + getSelectionsFromElement("region_dropdown_ids");
	params = params + "&zoneIds=" + getSelectionsFromElement("zone_dropdown_ids");
	params = params + "&sectorIds=" + getSelectionsFromElement("sector_dropdown_ids");
	params = params + "&subSectorIds=" + getSelectionsFromElement("sub_sector_dropdown_ids");
	
	
//	showLoading();
	YAHOO.util.Connect.setForm('datadispatcherform');
	var sUrl="/esrigis/datadispatcher.do?applyfilter=true" + params;
	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
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
var callbackChildrenCall = {
		  success: function(o) {
			  try {
				    var results = YAHOO.lang.JSON.parse(o.responseText);
				    switch(results.objectType)
				    {
					    case "ImplementingOrganizations":
				    		var orgDropdown = document.getElementById("imp_org_dropdown_ids");
				    		orgDropdown.options.length = 0;
				    		orgDropdown.options[0] = new Option("All", -1);
				    		for(var i = 0; i < results.children.length; i++){
				    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
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
				    	case "Orgtype":
				    		var orgDropdown = document.getElementById("org_dropdown_ids");
				    		orgDropdown.options.length = 0;
				    		orgDropdown.options[0] = new Option("All", -1);
				    		for(var i = 0; i < results.children.length; i++){
				    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
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



YAHOO.util.Event.addListener("region_dropdown_ids", "change", callbackChildren);
YAHOO.util.Event.onAvailable("region_dropdown_ids", callbackChildren)
YAHOO.util.Event.addListener("org_group_dropdown_ids", "change", callbackChildren);
YAHOO.util.Event.onAvailable("org_group_dropdown_ids", callbackChildren);
YAHOO.util.Event.addListener("imp_org_group_dropdown_ids", "change", callbackChildren);
YAHOO.util.Event.onAvailable("imp_org_group_dropdown_ids", callbackChildren);
YAHOO.util.Event.addListener("sector_dropdown_ids", "change", callbackChildren);
YAHOO.util.Event.onAvailable("sector_dropdown_ids", callbackChildren);
YAHOO.util.Event.addListener("applyButton", "click", callbackApplyFilter);
YAHOO.util.Event.onDOMReady(changeTab(0));


function resetToDefaults(){
	var allGraphs = document.getElementsByName("flashContent");
	for(var idx = 0; idx < allGraphs.length; idx++){
		allGraphs[idx].style.display = "none";
	}
	showLoading();

	//document.getElementById("org_type_dropdown_ids").selectedIndex = 0;
	document.getElementById("org_group_dropdown_ids").selectedIndex = 0;
	document.getElementById("region_dropdown_ids").selectedIndex = 0;
	document.getElementById("sector_dropdown_ids").selectedIndex = 0;
	removeOptions("org_dropdown_ids");
	removeOptions("zone_dropdown_ids");
	removeOptions("sub_sector_dropdown_ids");
	document.getElementById("decimalsToShow_dropdown").selectedIndex = 2;
	document.getElementById("yearsInRange_dropdown").selectedIndex = 4;
	
	document.getElementById("workspace_only").checked = false;
	
	document.getElementById("transaction_type_0").value = true;
	document.getElementById("transaction_type_1").value = false;
	document.getElementById("transaction_type_2").value = false;
	document.getElementById("org_group_dropdown_ids").selectedIndex = 0;
	document.getElementById("region_dropdown_ids").selectedIndex = 0;
	document.getElementById("sector_dropdown_ids").selectedIndex = 0;
	removeOptions("org_dropdown_ids");
	removeOptions("zone_dropdown_ids");
	removeOptions("sub_sector_dropdown_ids");
	callbackApplyFilter();
}

function removeOptions (obj){
	var elem = document.getElementById(obj);
	  var i;
	  for (i = elem.length - 1; i>0; i--) {
	    elem.remove(i);
	  }
}

//-->
</script>
