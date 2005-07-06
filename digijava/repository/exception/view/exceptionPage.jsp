<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="exceptionReportForm" />


<html>                                                                                      
<script language="JavaScript">
	function showExceptionReport() {
	  <digi:context name="url" property="context/module/moduleinstance/showLayout.do" />
	  document.exceptionReportForm.action = '<%= url %>?layout=<bean:write name="exceptionReportForm" property="layout" />';
	  document.exceptionReportForm.submit();
	}
</script>


<body bgcolor="#ffffff">
	
<digi:form method="post" action="/exceptionReport">
<html:hidden property="url" />
<html:hidden property="statusCode" />
<html:hidden property="message" />
<html:hidden property="siteId" />
<html:hidden property="name" />
<html:hidden property="email" />
<html:hidden property="stackTrace" />

<table>
<tr>
	<td><b><digi:trn key="exception:errorCaption" >We are sorry. Some error occured while server was processing this page. If you want to report this problem, please, press REPORT button in the following form</digi:trn>
	</b></td>
</tr>
<tr><td>
	<input type="button" value="REPORT" onclick="javascript:showExceptionReport()" >
</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>
	Status code: <bean:write name="exceptionReportForm" property="statusCode" />
</td></tr>
<tr><td>&nbsp;</td></tr>
<tr>
	<td>Error Message is: <b><font color="red"><bean:write name="exceptionReportForm" property="message" /></font></b></td>
</tr>
</table>
</digi:form>
</body>
</html>
