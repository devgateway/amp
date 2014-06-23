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
	 </div>
<!-- FOOTER END  -->
	
<%@include file="footerStaticText.jsp" %>
