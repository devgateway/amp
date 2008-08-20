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
		<td width="206">
		<p align="center"><strong><digi:trn
			key="aim:aidmanagementplatform">Aid Management Platform (AMP)</digi:trn></strong></p>
		<p align="center"><digi:trn
			key="aim:aidmanagementplatformversionshort">Version 1.11c</digi:trn></p>
		</td>
		<td width="258"><img width="220" height="149"
			src="/TEMPLATE/ampTemplate/images/dgf_logo.jpg"></td>
	</tr>
	<tr>
		<td colspan="2">
		<p><digi:trn key="aim:aidmanagementplatform">Aid Management Platform (AMP)</digi:trn>
		<digi:trn key="aim:aidmanagementplatformversion">Version 1.11c Build 9. August
		2008.</digi:trn> <digi:trn key="aim:ampdevelopmentcredits">Developed in partnership with OECD, UNDP, WB, Government of
		Ethiopia and Development Gateway Foundation.</digi:trn></p>
		</td>
	</tr>
	<tr>
		<td colspan="2"><digi:trn
			key="aim:aidmanagementplatformtrademark">The Development Gateway and the The Development
		Gateway logo are trademarks for The Development Gateway Foundation.</digi:trn>
		All Rights Reserved.</td>
	</tr>
</table>