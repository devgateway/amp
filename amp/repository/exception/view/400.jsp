<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="exceptionReportForm"/>


<html>
<body bgcolor="#ffffff">

<digi:form method="post" action="/exceptionReport.do">
<html:hidden property="url" />
<html:hidden property="statusCode" />
<html:hidden property="message" />
<html:hidden property="siteId" />

<table>

<tr>
	<td><b>Error Message is: <font color="red"><bean:write name="exceptionReportForm" property="message" /></font></b></td>
</tr>
<tr>
	<td nowrap><b>URL: <font color="red"><bean:write name="exceptionReportForm" property="url" /></font></b></td>
</tr>
<tr>
	<td width="310px">
	<fieldset>
	<legend><digi:trn key="exception:exceptionReport" >Exception report</digi:trn></legend>
	<table width="310px">
	<tr>
		<td valign="top">
		    <digi:trn key="exception:name" >Name:</digi:trn>
		</td>	
		<td>
		    <html:text property="name" size="30" />
		</td>	

	</tr>
	<tr>
		<td valign="top">
		    <digi:trn key="exception:email" >Email:</digi:trn>
		</td>	
		<td>
		    <html:text property="email" size="30" />	
		</td>	

	</tr>
	<tr>
		<td valign="top">
		    <digi:trn key="exception:comment" >Comment:</digi:trn>
		</td>	
		<td>
		    <html:textarea style="width:370px;height:200px" property="comment" />	
		</td>	

	</tr>
	<tr>
		<td colspan="2" align="right">
		    <html:submit value="Submit"/>
		</td>	

	</tr>
	</table>
	</fieldset>
	</td>

</tr>
</table>
</digi:form>
</body>
</html>
