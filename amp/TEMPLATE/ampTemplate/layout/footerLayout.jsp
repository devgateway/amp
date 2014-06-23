<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="org.digijava.module.aim.helper.GlobalSettingsConstants" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>



<!-- FOOTER START -->
	<div class="footer">
			AMP <b><tiles:getAsString name="version"/></b> build <b><tiles:getAsString name="build_version"/></b> - <digi:trn>Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and DGF</digi:trn>
			<logic:notEmpty name="currentMember" scope="session">
				<digi:secure actions="ADMIN">
            		<a href='<digi:site property="url"/>/admin/'>Admin</a>
            		<a href='<digi:site property="url"/>/admin/switchDevelopmentMode.do'><digi:trn key="admin:devMode">User/Dev Mode</digi:trn></a>
       			</digi:secure>
			</logic:notEmpty>
	
	 </div>
<%@include file="footerStaticText.jsp" %>


