<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="visualizationform"/>


<script type="text/javascript">


</script>

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
						<html:checkbox  property="filter.workspaceOnly"><b><digi:trn>Show Only Data From This Workspace</digi:trn></b></html:checkbox> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" />  &nbsp;&nbsp;<input name="" type="checkbox" value="" /> <b>Show Decimals</b>
						<hr />
						<b>For Time Series Comparison, what data do you want to show?</b> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
						<html:checkbox  property="filter.commitmentsVisible"><digi:trn>Commitments</digi:trn>&nbsp;&nbsp;</html:checkbox>
						<html:checkbox  property="filter.disbursementsVisible"><digi:trn>Disbursements</digi:trn>&nbsp;&nbsp;</html:checkbox>
						<html:checkbox  property="filter.expendituresVisible"><digi:trn>Expenditures</digi:trn>&nbsp;&nbsp;</html:checkbox>
						<html:checkbox  property="filter.pledgeVisible"><digi:trn>Pledges</digi:trn>&nbsp;&nbsp;</html:checkbox><br />
						<b>What data should the dashboard show? </b><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
						<fieldset>
                            <html:radio property="filter.transactionType" value="0"><digi:trn>Commitments</digi:trn></html:radio>
                            <html:radio property="filter.transactionType" value="1"><digi:trn>Disbursements</digi:trn></html:radio>
                            <html:radio property="filter.transactionType" value="2"><digi:trn>Expenditures</digi:trn></html:radio>
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
       							<html:select property="filter.orgGroupIds" multiple="true" styleId="org_group_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
	           						<html:option value="-1"><digi:trn>All</digi:trn></html:option>
	          	 					<html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
	       						</html:select> 
       						</c:if>
							<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" />
							<hr />
							<div id="divOrgDrpdwn">
							<b>Organization:</b><br />
							<html:select property="filter.orgIds" multiple="true" styleId="org_dropdown_ids" styleClass="dropdwn_sm" style="width:145px;">
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
	
<center>
<input type="button" value="Apply" class="buttonx" style="margin-right:10px; margin-top:10px;" onclick="applyFilterPopin()">
<input type="button" value="Reset to defaults" class="buttonx" style="margin-right:10px; margin-top:10px;">
<input type="button" value="Close" class="buttonx" onclick="hidePopin()" style="margin-right:10px; margin-top:10px;">
</center>

</td>
</tr>
</table>