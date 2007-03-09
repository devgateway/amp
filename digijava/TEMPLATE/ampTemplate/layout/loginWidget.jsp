<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<logic:present name="currentMember" scope="session">
<DIV id="menu" align="right">
<div id="gen"
	title='<digi:trn key="aim:clickToLogoutTheSystem">Click here to logout from the system</digi:trn>'>
<digi:link styleClass="head-menu-link" href="/j_acegi_logout"
	module="aim" onclick="return quitRnot()">
	<digi:trn key="aim:logout">Logout</digi:trn>
</digi:link></div>
</DIV>
</logic:present>

<logic:notPresent name="currentMember" scope="session">
<DIV id="menu" align="right">
<div id="gen"
	title='<digi:trn key="aim:aimGoToLogin">Go To Login Page</digi:trn>'>
<digi:link styleClass="head-menu-link" href="/"
	module="aim" onclick="return quitRnot()">
	<digi:trn key="aim:aimGoToLogin">Go To Login Page</digi:trn>
</digi:link></div>
</DIV>
</logic:notPresent>