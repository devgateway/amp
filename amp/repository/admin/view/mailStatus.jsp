<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>


<digi:instance property="mailStatusForm" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr>
		<td colspan="2" class="yellow" valign="top" align="center">
	<TABLE border="0" >
        <TR>
			<td align="center" valign="top" colspan=2>
				<fieldset>
				<legend><digi:trn key="admin:mailStatus">Mail Status</digi:trn></legend>
				 <table>
				    <tr>
					<td colspan=2 width="5%" align="right" nowrap><b><digi:trn key="admin:statisticLast">Mail service statistics for last {minute} minute(s)</digi:trn></b></td>
				    </tr>
				    <tr>
					<td width="5%" align="right" nowrap>Total number of emails having system errors:</td>
	  			        <td><b><bean:write name="mailStatusForm" property="errorMails" /></b></td>
				    </tr>
				    <tr>
				      <td width="5%" align="right" nowrap>Availability of SMTP service:</td>
					<logic:equal name="mailStatusForm" property="smtpAvailable" value="true">
						<td><b><digi:trn key="admin:mailStatusAvailable">Available</digi:trn></b></td>
					</logic:equal>
					<logic:notEqual name="mailStatusForm" property="smtpAvailable" value="true">
						<td><b><digi:trn key="admin:mailStatusNotAvailable">Not Available</digi:trn></b></td>
					</logic:notEqual>
				    </tr>
			    </table>			   
			</fieldset> 
			</td>
		</TR>

	</TABLE >
		</td>
	</tr>
</table>