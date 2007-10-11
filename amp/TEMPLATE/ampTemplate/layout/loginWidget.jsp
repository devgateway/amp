<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<logic:present name="currentMember" scope="session">
<DIV id="menu" align="right" style="">
<div id="gen" style="white-space:nowrap;"
	title='<digi:trn key="aim:clickToLogoutTheSystem">Click here to logout from the system</digi:trn>'>
<c:set var="message">
		<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>

<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>


<digi:link styleClass="head-menu-link" href="/j_acegi_logout"
	module="aim" onclick="return quitRnot1('${msg}')">
	<digi:trn key="aim:logout">Logout</digi:trn>
</digi:link></div>
</DIV>
</logic:present>

<logic:notPresent name="currentMember" scope="session">
<DIV id="menu" align="right">
<div id="gen"
	title='<digi:trn key="aim:aimGoToLogin">Go To Login Page</digi:trn>'>
	<c:set var="message">
				<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
			</c:set>
<digi:link styleClass="head-menu-link" href="/"
	module="aim" onclick="return quitRnot1('${message}')">
	<digi:trn key="aim:aimGoToLogin">Go To Login Page</digi:trn>
</digi:link></div>
</DIV>
</logic:notPresent>
