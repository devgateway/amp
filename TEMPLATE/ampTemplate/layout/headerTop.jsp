


<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>



<digi:context name="displayFlag" property="context/aim/default/displayFlag.do" />

<table cellspacing="0" cellPadding="0" border="0" width="100%" bgcolor="#195C79" vAlign="top"
	height="34">
	<tbody>
   	<tr bgColor="#195C79">
   			
		
			 <logic:notEmpty name="defFlagExist" scope="application">
				<logic:equal name="defFlagExist" scope="application" value="true">
					<td valign="top" width="60">
						<img src="<%=displayFlag%>" border="0" height="34" width="50">
					</td>							
				</logic:equal>
			</logic:notEmpty>
			 <td valign="center" height="34">&nbsp;
   			<digi:link href="/" module="aim" styleClass="heading" onclick="return quitRnot()" title="Aid Management Platform">
					<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn> |
				</digi:link> 
			</td>
   	</tr>
   </tbody>
</table>
