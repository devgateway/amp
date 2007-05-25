<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<digi:instance property="cmsSendEmailForm"/>



<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:composeEmail">Compose Email</digi:trn>
				</td>
				<td width="1"><digi:img src="module/cms/images/headerRightEnd.gif" border="0"/></td>				
			</tr>
		</table>
		<%-- end of Table header --%>
	</td>
</tr>
<tr>
	<td class="leftBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>
	<td width="100%" align="center" bgcolor="#F2F4FC">
<digi:errors />
<table>
 <tr><td align=left class="Title"><digi:trn key="cms:composeEmail">Compose Email</digi:trn></td></tr>
 <tr><td>
  <digi:form action="/sendEmail.do" method="post">
   <table>
   <tr >
      <td width="5%"  align="right"><digi:trn key="cms:from">From:</digi:trn></td>
      <td width="80%" align="left"><bean:write name="cmsSendEmailForm" property="fromName" /></td>
   </tr>
   <tr >
      <td width="5%"  align="right" nowrap><digi:trn key="cms:to">To</digi:trn>:</td>
      <td width="80%" align="left"><html:text property="to" size="30"/></td>
   </tr> 
   <tr >
      <td width="5%"  align="right" nowrap><digi:trn key="cms:subject">Subject</digi:trn>:</td>
      <td width="80%" align="left"><html:text property="subject" size="40"/></td>
   </tr> 
   <tr>
      <td width="5%"   align="right" valign="top" nowrap><digi:trn key="cms:message">Message</digi:trn>:</td>
      <td width="80%"  align="left" ><html:textarea property="message" rows="10" cols="50" /></td>
   </tr>
   <tr>
      <td colspan="2">
	<table>
	<tr><td>
	      <td width="100%" align="right" nowrap><digi:trn key="cms:copyToMySelf">Copy to myself</digi:trn>:</td>
      	      <td><html:checkbox property="copyToMySelf" /></td>
	</td></tr>
	</table>
      </td>	
   </tr>
  <tr>
      <td width="5%"  align="right" colspan="2">
		<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
				<td width="100%" class="rowItem" valign="middle" nowrap align="center">
					<input type="button" value="SEND" onClick="this.disabled = true; cmsSendEmailForm.submit()" class="rowItemButton">
				</td>
				<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
			</tr>
		</table>
	  </td>
  </tr>
  </table>
 </digi:form>
 </td>
</tr>
</table>

	</td>
	<td class="rightBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>	
</tr>
<tr>
	<td height="1"><digi:img src="module/cms/images/leftBottom.gif" width="9" height="9" border="0"/></td>
	<td width="100%" class="bottomBorder" height="1"><digi:img src="module/cms/images/tree/spacer.gif" height="9" border="0"/></td>
	<td height="1"><digi:img src="module/cms/images/rightBottom.gif" width="9" height="9" border="0"/></td>	
</tr>
</table>
