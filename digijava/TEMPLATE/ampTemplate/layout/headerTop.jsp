<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<digi:context name="displayFlag" property="context/module/moduleinstance/displayFlag.do" />

<table cellspacing="0" cellPadding="0" border="0" width="100%" bgcolor="#323232" vAlign="top"
	height="34">
	<tbody>
   	<tr bgColor="#323232">
   		<td valign="top" align="left" width="36" height="34"> 
   			<digi:link href="/" site="amp" onclick="return quitRnot()">
         		<digi:img src="images/amp-logo.gif" border="0" alt="AMP" />
				</digi:link>
			</td>
			<td valign="center" height="34">&nbsp;
   			<digi:link href="/" styleClass="heading" onclick="return quitRnot()" title="Aid Management Platform">
					<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn>
				</digi:link>
			</td>
			<logic:notEmpty name="defFlagExist" scope="application">
				<logic:equal name="defFlagExist" scope="application" value="true">
					<td valign="top" width="60">
						<img src="<%=displayFlag%>" border="0" height="34" width="50">
					</td>							
				</logic:equal>
			</logic:notEmpty>
   	</tr>
   </tbody>
</table>
