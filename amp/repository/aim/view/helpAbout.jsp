<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<%
//org.digijava.kernel.request.SiteDomain siteDomain = (org.digijava.kernel.request.SiteDomain) session.getAttribute("site");
//request.setAttribute(org.digijava.kernel.Constants.CURRENT_SITE, siteDomain);
//String siteUrl = org.digijava.kernel.util.SiteUtils.getSiteURL(siteDomain, request.getScheme(), request.getServerPort(), request.getContextPath());
//request.setAttribute(org.digijava.kernel.Constants.REQUEST_ALREADY_PROCESSED, siteUrl+"/repository/help/view/helpAbout.jsp");
%>

<table width="474" border="0">
	<tr>
		<td width="257">
		<p align="center" style="font-family: Arial, Helvetica, sans-serif; font-size: 16px;"><strong><digi:trn
			key="aim:aidmanagementplatform">Aid Management Platform (AMP)</digi:trn></strong></p>
		<p align="center"> Version <tiles:getAsString name="version"/></p>
		</td>
		<td width="220"><img width="220" height="149"
			src="/TEMPLATE/ampTemplate/images/dgf_logo.jpg"></td>
	</tr>
	<tr>
		<td colspan="2">
		<p style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;"><digi:trn key="aim:aidmanagementplatform">Aid Management Platform (AMP)</digi:trn>
		 Version <tiles:getAsString name="version"/> <tiles:getAsString name="build_version"/>
		<digi:trn key="aim:ampdevelopmentcredits">Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and Development Gateway Foundation.</digi:trn>
		</p>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<p style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;"><digi:trn key="aim:aidmanagementplatformtrademark">The Development Gateway and the The Development Gateway logo are trademarks for The Development Gateway Foundation.</digi:trn>
		<digi:trn key="aim:ampallrightreserved">All Rights Reserved.</digi:trn></p>
		</td>
	</tr>
</table>