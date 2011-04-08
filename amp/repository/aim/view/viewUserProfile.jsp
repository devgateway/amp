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


<script language="JavaScript">
<!--

function load() {
}

function unload() {
}

-->
</script>


<digi:instance property="aimUserDetailForm" />

<digi:errors/>


					<table class="inside" width="100%" cellpadding="0" cellspacing="0" border="1">
						<tr>
							<td colspan="2" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
								<digi:trn key="aim:userProfile">User Profile</digi:trn>
							</td>
						</tr>
						<tr>
							<td class="inside">
								<digi:trn key="aim:firstName">First Name</digi:trn>
							</td>
							<td class="inside">
								 <bean:write name="aimUserDetailForm" property="firstNames" />
							</td>
						</tr>
						<tr>
							<td class="inside">
								<digi:trn key="aim:lastName" >Last Name</digi:trn>
							</td>
							<td class="inside">
								<bean:write name="aimUserDetailForm" property="lastName" />
							</td>
						</tr>
						<tr>
							<td class="inside">
								<digi:trn key="aim:mailingAddress" >Mailing Address	</digi:trn>
							</td>
							<td class="inside">
								<bean:write name="aimUserDetailForm"
								property="mailingAddress" />
							</td>
						</tr>
						<tr>
							<td class="inside">
								<digi:trn key="aim:address" >Address</digi:trn>
							</td>
							<td class="inside">
								<bean:write name="aimUserDetailForm" property="address" />
							</td>
						</tr>
						<tr>
							<td class="inside">
								<digi:trn key="aim:organizationName">Organization Name</digi:trn>
							</td>
							<td class="inside">
								<bean:write name="aimUserDetailForm" property="organizationName" />
							</td>
						</tr>
						<tr>
							<td class="inside"><digi:trn key="aim:autologinlink">Autologin Link</digi:trn></td>
							<td class="inside">
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
							<td class="inside" colspan="2" align="center">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="inside" colspan="2" align="center">
								<digi:trn key="aim:teamsAssociatedWith" >Teams Associated with</digi:trn>
							</td>
						</tr>
						<% int i = 0; %>
						<tr>
							<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
								<digi:trn key="aim:teamName">Team Name</digi:trn>
							</td>
							<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
								<digi:trn key="aim:role">Role</digi:trn>
							</td>
						</tr>
						<c:forEach var="info" items="${aimUserDetailForm.info}">
							<% if ((i%2) == 0) { %>
								<tr>
							<% } %>
							<td class="inside">
										<logic:equal value="Workspace Manager" name="info">
											<digi:trn key="aim:workspaceManager">Workspace Manager</digi:trn>
										</logic:equal>
										<logic:equal value="Workspace Member" name="info">
											<digi:trn key="aim:workspaceMember">Workspace Member</digi:trn>
										</logic:equal>
							</td>
							
							<% if ((i%2) != 0) { %>
								</tr>
								<% }
								i++;
							%>
						
						</c:forEach>
					</table>

<br>					
<div class="buttons" align="center">					
	<input type="button" value="Close" class="buttonx_sm btn" onclick="hidePWorkspaceframe()">
</div>
	
