<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ page import="org.digijava.module.aim.helper.GlobalSettingsConstants" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<!--<link href="css/global.css" rel="stylesheet" type="text/css">-->
<!--<link href="/TEMPLATE/ampTemplate/css/styles.css" rel="stylesheet" type="text/css"></link>-->

<script language="JavaScript">
<!--

function load() {
}

function unload() {
}

-->
</script>

<!--<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />-->
<digi:instance property="aimUserDetailForm" />

<digi:errors/>

<!--<style type="text/css">-->
<!--	.text1 {-->
<!--	  font-size:9px;-->
<!--	  font:Arial;-->
<!--	  color:#666666;-->
<!--	  text-decoration: none;-->
<!--	  font-style: normal;-->
<!--	}-->
<!--	-->
<!--	TD -->
<!--	{-->
<!--		cellpadding: 0px;-->
<!--		cellspacing: 0px;-->
<!--		padding: 0px;-->
<!--		margin: 0px;-->
<!--		FONT-WEIGHT: normal; -->
<!--		FONT-SIZE: 10px;-->
<!--		border-color:#FFFFFF;-->
<!--	}-->
<!--	-->
<!--	TR{-->
<!--		padding:0px; -->
<!--		margin:0px;-->
<!--		border-color:#FFFFFF;-->
<!--	}-->
<!---->
<!--	TABLE {-->
<!--		border-spacing: 0;-->
<!--		border-color:#FFFFFF;-->
<!--		padding: 0;-->
<!--		margin: 0;-->
<!--	}-->
<!--</style>-->

<table width="100%" cellSpacing=2 cellPadding=2 vAlign="top" border=0 height="500px">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=2 cellSpacing=2 width="100%"  border=0 height="500px">
			<tr>
				<td align=left vAlign=top>
					<table width="100%" cellSpacing="2" cellPadding="2" vAlign="top"
					bgcolor="#aaaaaa">
						<tr>
							<td colspan="2"  align="center" class="text1">
								<b><digi:trn key="aim:userProfile">User Profile</digi:trn></b>
							</td>
						</tr>
						<tr>
							<td  bgcolor="#f4f4f2" width="150" class="text1">
								<digi:trn key="aim:firstName">First Name</digi:trn>
							</td>
							<td bgcolor="#f4f4f2" class="text1">
								 <bean:write name="aimUserDetailForm" property="firstNames" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="text1">
								<digi:trn key="aim:lastName" >Last Name</digi:trn>
							</td>
							<td bgcolor="#f4f4f2" class="text1">
								<bean:write name="aimUserDetailForm" property="lastName" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="text1">
								<digi:trn key="aim:mailingAddress" >Mailing Address	</digi:trn>
							</td>
							<td bgcolor="#f4f4f2" class="text1">
								<bean:write name="aimUserDetailForm"
								property="mailingAddress" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="text1">
								<digi:trn key="aim:address" >Address</digi:trn>
							</td>
							<td bgcolor="#f4f4f2" class="text1">
								<bean:write name="aimUserDetailForm" property="address" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="text1">
								<digi:trn key="aim:organizationName">Organization Name</digi:trn>
							</td>
							<td bgcolor="#f4f4f2" class="text1">
								<bean:write name="aimUserDetailForm" property="organizationName" />
							</td>
						</tr>
						<tr>
							<td><digi:trn key="aim:autologinlink"><b>Autologin Link</b></digi:trn></td>
							<td>
								<%
								String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTO_LOGIN);
								Long id = ((org.digijava.module.aim.helper.TeamMember) session.getAttribute("currentMember")).getMemberId();
								if( request.getParameter("id") != null && value.equalsIgnoreCase("true") && id.equals(new Long(request.getParameter("id")))){
									org.digijava.kernel.user.User user = (org.digijava.kernel.user.User) session.getAttribute("org.digijava.kernel.user");
									String encryptedPass = org.digijava.kernel.util.ShaCrypt.crypt(user.getEmail() + "_" + user.getPassword());
								%>
									<a href="/<%=request.getContextPath()%>repository/aim/view/autologin.jsp?user=<%=user.getEmail()%>&password=<%=encryptedPass%>&workspaceId=<%=id%>">
										<digi:trn key="aim:autologin"><b>Autologin</b></digi:trn>
									</a>
								<%}%>
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" colspan="2" class="text1">
								<table width="97%" cellPadding=2 cellSpacing="2" bgcolor="#aaaaaa" align="center" border=0>
									<tr>
										<td align="center" bgcolor="#dddddd" colspan="2" class="text1">
											<digi:trn key="aim:teamsAssociatedWith" ><b>Teams Associated with</b>	</digi:trn>
										</td>
									</tr>
									<% int i = 0; %>
									<c:forEach var="info" items="${aimUserDetailForm.info}">
									<tr>
										<td bgcolor="#f4f4f2"  width="300px" class="text1">
											<% if ((i%2) == 0) { %>
												<digi:trn key="aim:teamName">Team Name</digi:trn>
											<% } else { %>
												<digi:trn key="aim:role">Role</digi:trn>
											<% }
											   i++;
											%>
										</td>
										<td bgcolor="#f4f4f2" class="text1" width="500px">
											<c:out value="${info}"/>
										</td>
									</tr>
									</c:forEach>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr align="center">
				<td>
					<input type="button" value="Close" class="dr-menu" onclick="hidePWorkspaceframe()">
				</td>
			</tr>
		</table>
	</td></tr>
</table>
