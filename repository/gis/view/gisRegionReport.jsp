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
      <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn key="gis:regionalview">Regional View</digi:trn>
      </div>
            
    <div id="div1" class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
		<table cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;" border="1" bordercolor="#000000">
			<tr>
				<td width="120" style="color:#FFFFFF;font-weight:bold" class="tableHeader">Country</td>
				<td>&nbsp;<%=countryName%></td>
			</tr>
			<tr>
				<td width="120" style="color:#FFFFFF;font-weight:bold" class="tableHeader">Region</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="regName"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Sector</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="selSectorName"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Year Range</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="startYear"/> - <bean:write name="gisRegReportForm" property="endYear"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Actual Commitments</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualCommitmentsStr"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Actual Disbursements</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualDisbursementsStr"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Actual Expenditures</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualExpendituresStr"/></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><font color="red">Note: all numbers are in USD</font></td>
			</tr>
		</table> 
	</div>
</div>
		
		<br>
		
		
		<logic:present name="gisRegReportForm" property="activityLocationFundingList">
			<logic:notEmpty name="gisRegReportForm" property="activityLocationFundingList">
		
		

					<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;" border="0">
						<thead>
			
						<tr>
								<td colspan="4" style="overflow-x:hidden; background-color: #4A5A80; color:#FFFFFF;" >
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											Overall information
										</div>
									</div>
								</td>
								<td colspan="3" style="overflow-x:hidden; background-color: #4A5A80; color:#FFFFFF;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											For selected sector/region
										</div>
									</div>
								</td>
						</tr>
			
						
						<tr>
								<td width="20%" style="overflow-x:hidden; background-color: #222E5D; color:#FFFFFF;">
			
										<div class="gisReportTableBevelCell">
											Activity
										</div>
			
								</td>
								<td width="20%" style="overflow-x:hidden; background-color: #222E5D; color:#FFFFFF;">
			
										<div class="gisReportTableBevelCell">
											Region(s)
										</div>
			
								</td>
								<td width="20%" style="overflow-x:hidden; background-color: #222E5D; color:#FFFFFF;">
			
										<div class="gisReportTableBevelCell">
											Sector(s)
										</div>
			
								</td>
								<td width="10%" style="overflow-x:hidden; background-color: #222E5D; color:#FFFFFF;">
			
										<div class="gisReportTableBevelCell">
											Donor(s)
										</div>
			
								</td>
								<td width="10%" style="overflow-x:hidden; background-color: #222E5D; color:#FFFFFF;">
			
										<div class="gisReportTableBevelCell">
											Commitments
										</div>
			
								</td>
								<td width="10%" style="overflow-x:hidden; background-color: #222E5D; color:#FFFFFF;">
			
										<div class="gisReportTableBevelCell">
											Disbursements
										</div>
			
								</td>
								<td width="10%" style="overflow-x:hidden; background-color: #222E5D; color:#FFFFFF;">
			
										<div class="gisReportTableBevelCell">
											Expenditures
										</div>
			
								</td>
								<!--
								<td style="border-left:1px solid black;">&nbsp;&nbsp;&nbsp;</td>
								-->
						</tr>
						</thead>
						
						<tbody style="overflow-y:scroll; overflow-x: hidden;" height="250">
						
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
																<bean:write name="ampLoc" property="name"/>
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
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="activity.sectors">
												<logic:notEmpty name="activityLocationFunding" property="activity.sectors">
													<ul style="margin:0 0 0 20px; padding:0;">
													<logic:iterate name="activityLocationFunding" property="activity.sectors" id="iterSector">
														<li>
														<bean:define id="ampSec" name="iterSector" property="sectorId" type="org.digijava.module.aim.dbentity.AmpSector"/>
														<bean:write name="ampSec" property="name"/>
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
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="activity.orgrole">
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
									</div>
		
									</div>
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
							<td colspan="7" height="100%" style="border-top:1px solid black;">
									&nbsp;
							</td>
							<!--
							<td>&nbsp;</td>
							-->
						</tr>
						</tbody>
					</table>

		
			</logic:notEmpty>
		</logic:present>
		<logic:notPresent name="gisRegReportForm" property="activityLocationFundingList">
			<div style="width:100%" align="center">
				<div class="gisReportWarning" style="width:85%">The report contains no activities. Please adjust your filter criteria.</div>
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


