<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ page import="org.digijava.module.um.form.UserEmailForm" %>

<digi:errors/>
<digi:form action='/userEmail.do?action=Email' >
<p><b><font size="5"><digi:trn key="um:enterYourEmail">Enter Your email</digi:trn></font></b></p>
<p><digi:trn key="um:email">Email</digi:trn><html:text property="email"/></p>
<input type="Button" value="Submit" onclick="this.disabled='true';form.submit()"/>
</digi:form>