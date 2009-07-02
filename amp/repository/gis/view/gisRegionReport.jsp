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
		</table> 
		
		<br>
		
		<div style="height:300px; overflow-y:scroll;">
		<table width="100%" style="border-collapse:collapse;" border="1" bordercolor="#000000">
			<tr>
					<td colspan="3" style="background-color: #4A5A80; color:#FFFFFF; font-weight:bold">Overall information</td>
					<td colspan="3" style="background-color: #4A5A80; color:#FFFFFF; font-weight:bold">For selected sector/region</td>
			</tr>
			<tr>
					<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Activity</td>
					<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Region(s)</td>
					<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Sector(s)</td>
					<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Actual Commitments</td>
					<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Actual Disbursements</td>
					<td style="color:#FFFFFF;font-weight:bold" class="tableHeader">Actual Expenditures</td>
			</tr>
			<logic:iterate name="gisRegReportForm" property="activityLocationFundingList" id="activityLocationFunding">
				<tr>
					<td valign="top">
						<a href="javascript:showSelActivity(<bean:write name="activityLocationFunding" property="activity.ampActivityId"/>);">
							<bean:write name="activityLocationFunding" property="activity.description"/>
							<br>
							(<bean:write name="activityLocationFunding" property="activity.ampId"/>)
						</a>
					</td>
					<td>
						<logic:present name="activityLocationFunding" property="activity.locations">
							<logic:notEmpty name="activityLocationFunding" property="activity.locations">
								<ul>
								<logic:iterate name="activityLocationFunding" property="activity.locations" id="iterLocation">
									<li>
									<bean:define id="ampLoc" name="iterLocation" property="location" type="org.digijava.module.aim.dbentity.AmpLocation"/>
									<bean:write name="ampLoc" property="name"/>
									</li>
								</logic:iterate>
								</ul>
							</logic:notEmpty>
						</logic:present>
					</td>
					<td>
						<logic:present name="activityLocationFunding" property="activity.sectors">
							<logic:notEmpty name="activityLocationFunding" property="activity.sectors">
								<ul>
								<logic:iterate name="activityLocationFunding" property="activity.sectors" id="iterSector">
									<li>
									<bean:define id="ampSec" name="iterSector" property="sectorId" type="org.digijava.module.aim.dbentity.AmpSector"/>
									<bean:write name="ampSec" property="name"/>
									</li>
								</logic:iterate>
								</ul>
							</logic:notEmpty>
						</logic:present>
					
					</td>
					<td><bean:write name="activityLocationFunding" property="fmtCommitment"/></td>
					<td><bean:write name="activityLocationFunding" property="fmtDisbursement"/></td>
					<td><bean:write name="activityLocationFunding" property="fmtExpenditure"/></td>
				</tr>
			</logic:iterate>
		</table>
		</div>
		
		<script language="JavaScript">
		function showSelActivity(activityId) {
			var actUrl = "/aim/selectActivityTabs.do~ampActivityId=" + activityId;
			if (window.opener.opener == null) {
				window.open(actUrl, null, null);
			} else {
				window.opener.opener.location.href = actUrl;
			}
			
		}
		</script>

	</div>
</div>