<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.um.form.UserEmailForm" %>

<digi:errors/>
<digi:form action='/userEmail.do?action=Email' >
<p><b><font size="5"><digi:trn key="um:enterYourEmail">Enter Your email</digi:trn></font></b></p>
<p><digi:trn key="um:email">Email</digi:trn><html:text property="email"/></p>
<input type="Button" value="Submit" onclick="this.disabled='true';form.submit()"/>
</digi:form>