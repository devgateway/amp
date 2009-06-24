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


<table cellpadding="2" cellspacing="0" width="100%" style="border-collapse:collapse;">
	<tr>
		<td colspan="2"><%=countryName%></td>
	</tr>
	<tr>
		<td>Region</td>
		<td><bean:write name="gisRegReportForm" property="regName"/></td>
	</tr>
	<tr>
		<td>Sector</td>
		<td><bean:write name="gisRegReportForm" property="selSectorName"/></td>
	</tr>
	<tr>
		<td>Year Range</td>
		<td><bean:write name="gisRegReportForm" property="startYear"/> - <bean:write name="gisRegReportForm" property="endYear"/></td>
	</tr>
	<tr>
		<td>Actual Commitments</td>
		<td><bean:write name="gisRegReportForm" property="actualCommitmentsStr"/></td>
	</tr>
	<tr>
		<td>Actual Disbursements</td>
		<td><bean:write name="gisRegReportForm" property="actualDisbursementsStr"/></td>
	</tr>
	<tr>
		<td>Actual Expenditures</td>
		<td><bean:write name="gisRegReportForm" property="actualExpendituresStr"/></td>
	</tr>
</table> 
