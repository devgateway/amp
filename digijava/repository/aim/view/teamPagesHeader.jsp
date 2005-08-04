<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language=javascript>
<!--
function showUserProfile(id)
{

	<digi:context name="information" property="context/module/moduleinstance/userProfile.do" />
	openURLinWindow("<%= information %>~id="+id,480, 350);
} 	
-->
</script>

<table cellpadding="0" cellspacing="0" height="65" width="100%" background="module/aim/images/bg-header.gif" border=0>
	<tr>
		<td valign="top">
			<table cellpadding="0" cellSpacing="0" height="65" width="772" 
			background="module/aim/images/my-desktop.gif" vAlign="top" class=r-dotted>
				<tr>
					<td width="10">
						&nbsp;&nbsp;&nbsp;
					</td>
					<td align="left">
						<logic:notEmpty name="currentMember" scope="session">										
      	       			<bean:define id="teamMember" name="currentMember" scope="session" 
					 	type="org.digijava.module.aim.helper.TeamMember" />					
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
						</bean:define>
						<a href="javascript:showUserProfile(<c:out value="${teamMember.memberId}"/>)" class="header" title="<%=translation%>">
						 	<bean:write name="teamMember" property="teamName" /> :
							<bean:write name="teamMember" property="memberName" />
						</logic:notEmpty>
						</a>
					</td>
					<td align="right">
						<bean:define id="translation">
							<digi:trn key="aim:clickToLogoutTheSystem">Click here to logout from the system</digi:trn>
						</bean:define>
						<digi:link href="/logout.do" styleClass="up-menu" onclick="return quitRnot()" title="<%=translation%>">
						<digi:trn key="aim:logout">Logout</digi:trn>
						</digi:link>&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
