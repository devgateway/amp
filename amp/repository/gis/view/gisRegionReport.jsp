<%@page import="org.digijava.module.aim.dbentity.AmpLocation"%>
<%@page import="org.digijava.module.aim.dbentity.AmpActivityLocation"%>
<%@page import="org.digijava.module.aim.dbentity.AmpSector"%>


<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>




<digi:instance property="gisRegReportForm"/>

<%
        String countryName = "";
        String ISO = null;
        java.util.Iterator itr1 = org.digijava.module.aim.util.FeaturesUtil.getDefaultCountryISO().iterator();
        while (itr1.hasNext()) {
            org.digijava.module.aim.dbentity.AmpGlobalSettings ampG = (org.digijava.module.aim.dbentity.AmpGlobalSettings) itr1.next();
            ISO = ampG.getGlobalSettingsValue();
        }

        if (ISO != null && !ISO.equals("")) {
            org.digijava.kernel.dbentity.Country cntry = org.digijava.module.aim.util.DbUtil.getDgCountry(ISO);
            countryName = " " + cntry.getCountryName();
        } else {
            countryName = "";
        }
%>

<digi:ref href="/repository/gis/view/css/gisReport.css" type="text/css" rel="stylesheet" />






<div class="yui-skin-sam" style="width:100%;height:100%;">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;width:100%;">
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn key="gis:regionalview">Regional View</digi:trn>
      </div>
            
    <div id="div1" class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
		<table cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;" border="1" bordercolor="#CCCCCC">
			<tr>
				<td width="120" style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Country</digi:trn>
				</td>
				<td>&nbsp;<%=countryName%></td>
			</tr>
			<tr>
				<td width="120" style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Region</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="regName"/></td>
			</tr>
			<tr>
				<td style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Sector</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="selSectorName"/></td>
			</tr>
			<tr>
				<td style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Year Range</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="startYear"/> - <bean:write name="gisRegReportForm" property="endYear"/></td>
			</tr>
			<tr>
				<td style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Actual Commitments</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualCommitmentsStr"/></td>
			</tr>
			<tr>
				<td style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Actual Disbursements</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualDisbursementsStr"/></td>
			</tr>
			<tr>
				<td style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Actual Expenditures</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualExpendituresStr"/></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<font color="red">
						<digi:trn>Note: all numbers are in</digi:trn> ${gisRegReportForm.selectedCurrency}
					</font>
				</td>
			</tr>
		</table> 
	</div>
</div>
		
		<br>
		
		
		<logic:present name="gisRegReportForm" property="activityLocationFundingList">
			<logic:notEmpty name="gisRegReportForm" property="activityLocationFundingList">

			
			<script language="javascript">
				if (navigator.appName=="Microsoft Internet Explorer") {
					document.write("<div style='width:100%; height:280px; border: 0px; overflow-y:scroll;'>");
				}
			</script>
			
			
					<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;" border="0">
						<thead>
			
						<tr>
								<td colspan="4" style="overflow-x:hidden; background-color: #C7D4DB; color:#000000;" >
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn><b>Overall information</b></digi:trn>
										</div>
									</div>
								</td>
								<td colspan="3" style="overflow-x:hidden; background-color: #C7D4DB; color:#000000;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn><b>For selected sector/region</b></digi:trn>
										</div>
									</div>
								</td>
						</tr>
			
						
						<tr>
								<td width="20%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Activity</digi:trn>
										</div>
											</td>
								<td width="20%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Region</digi:trn>(s)
										</div>
								</td>
								<td width="20%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Sector</digi:trn>(s)
										</div>
								</td>
								<td width="10%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Donor</digi:trn>(s)
										</div>
								</td>
								<td width="10%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Commitments</digi:trn>
										</div>
								</td>
								<td width="10%" style="overflow-x:hidden;background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Disbursements</digi:trn>
										</div>
								</td>
								<td width="10%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Expenditures</digi:trn>
										</div>
								</td>
						</tr>
						</thead>
						
						<tbody style="display:block; display:table-row-group; overflow-y:scroll; overflow-x: hidden; height:250px;" height="250">
							<bean:define name="gisRegReportForm" property="primarySectorSchemeId" id="primarySectorScheme"/>
						
						<logic:iterate name="gisRegReportForm" property="activityLocationFundingList" id="activityLocationFunding">
							<tr>
								<td width="30%" valign="top" style="overflow-x:hidden;" height="20">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<a title="<bean:write name="activityLocationFunding" property="activity.name"/>" href="javascript:showSelActivity(<bean:write name="activityLocationFunding" property="activity.ampActivityId"/>);">
												<bean:write name="activityLocationFunding" property="activity.name"/>
											</a>
										</div>
									</div>
								</td>
								<td width="20%" align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="activity.locations">
												<logic:notEmpty name="activityLocationFunding" property="activity.locations">
													<ul style="margin:0 0 0 20px; padding:0;">
														<logic:iterate name="activityLocationFunding" property="activity.locations" id="iterLocation">
															<li>
																<bean:define id="ampLoc" name="iterLocation" property="location" type="org.digijava.module.aim.dbentity.AmpLocation"/>
																<bean:write name="ampLoc" property="location.name"/>
															</li>
														</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td width="20%" align="left" style="overflow-x:hidden;">
								<%--
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="activity.sectors">
												<logic:notEmpty name="activityLocationFunding" property="activity.sectors">
													<ul style="margin:0 0 0 20px; padding:0;">
													<logic:iterate name="activityLocationFunding" property="activity.sectors" id="iterSector">
														<bean:define id="ampSec" name="iterSector" property="sectorId" type="org.digijava.module.aim.dbentity.AmpSector"/>
														<logic:equal name="ampSec" property="ampSecSchemeId.ampSecSchemeId" value="<%=primarySectorScheme.toString()%>">
															<li>
															<bean:write name="ampSec" property="name"/>
															</li>
														</logic:equal>
													</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								--%>
								<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="activity.orgrole">
												<logic:notEmpty name="activityLocationFunding" property="activity.orgrole">
													<ul style="margin:0 0 0 20px; padding:0;">
														<logic:iterate name="activityLocationFunding" property="topSectors" id="iterTopSectors">
															<li>
																<bean:write name="iterTopSectors"/>
															</li>
														</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td width="20%" align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal"><logic:present name="activityLocationFunding" property="activity.orgrole">
												<logic:notEmpty name="activityLocationFunding" property="activity.orgrole">
													<ul style="margin:0 0 0 20px; padding:0;">
														<logic:iterate name="activityLocationFunding" property="donorOrgs" id="iterOrgrole">
															<li>
																<bean:write name="iterOrgrole"/>
															</li>
														</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td width="10%" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell 
									<logic:present name="activityLocationFunding" property="fmtCommitment">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent name="activityLocationFunding" property="fmtCommitment">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activityLocationFunding" property="fmtCommitment"/>
									</div></div>
								</td>
								<td width="10%" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell 
									<logic:present  name="activityLocationFunding" property="fmtDisbursement">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent  name="activityLocationFunding" property="fmtDisbursement">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activityLocationFunding" property="fmtDisbursement"/>
									</div>
									</div>
								</td>
								<td width="10%" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell 
									<logic:present name="activityLocationFunding" property="fmtExpenditure">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent name="activityLocationFunding" property="fmtExpenditure">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activityLocationFunding" property="fmtExpenditure"/>
									</div>
									</div>
								</td>
								<td style="border-left:1px solid black;">&nbsp;</td>
							</tr>
						</logic:iterate>
						<tr>
							<td colspan="7" height="100%" style="border-top:1px solid #CCCCCC;">&nbsp;
									
							</td>
							<!--
							<td>&nbsp;</td>
							-->
						</tr>
						</tbody>
					</table>
					
					<script language="javascript">
						if (navigator.appName=="Microsoft Internet Explorer") {
							document.write("</div>");
						}
					</script>
					
				

		
			</logic:notEmpty>
		</logic:present>
		<logic:notPresent name="gisRegReportForm" property="activityLocationFundingList">
			<div style="width:100%" align="center">
				<div class="gisReportWarning" style="width:85%">
				 <digi:trn>
				 	The report contains no activities. Please adjust your filter criteria.
				 </digi:trn>
				</div>
			</div>
		</logic:notPresent>
</div>		
		
		<script language="JavaScript">
		function showSelActivity(activityId) {
			var actUrl = "/aim/selectActivityTabs.do~ampActivityId=" + activityId;
			if (window.opener.opener == null) {
				window.open(actUrl, null, null);
			} else {
				window.opener.opener.location.href = actUrl;
				window.opener.opener.focus();
			}
			
		}
		</script>


