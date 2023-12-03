<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.ampModule.aim.util.FeaturesUtil" %>
<%@ page import="org.digijava.ampModule.aim.helper.GlobalSettingsConstants" %>
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

<style>
table {font-family:Arial, Helvetica, sans-serif; font-size:12px;}
table.inside, td.inside,td.report_inside {border-color: #CCC; border-style: solid; font-size:12px;}
table.inside1, td.inside1 {border: 0; font-size:12px;}
table.inside, td.inside_zebra {}
table.inside {border-width: 0 0 1px 1px; border-spacing: 0; border-collapse: collapse;}
td.inside {margin: 0; padding: 4px; border-width: 1px 1px 0 0;}
td.report_inside { border-width: 1px 1px 0 0;}
td.inside_header {background-color:#C7D4DB; color:#000; height:30px; border-color: #fff; border-style: solid; font-size:12px; border-width: 1px 1px 1px 1px; border-spacing: 0; border-collapse: collapse; text-align:center;}
.ins_title {font-size:11px; font-weight:bold; color:#767676; white-space:nowrap;}
.ins_title_reg {font-size:11px; color:#767676; font-weight:bold; font-family:Arial, Helvetica, sans-serif;}
.ins_title_sm {font-size:9px; color:#767676;}
.ins_title_mid {font-size:10px; color:#767676;}
.ins_header {color:#767676; font-size:12px; font-weight:bold;}
.buttonx_sm {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:10px; color:#FFFFFF; font-weight:bold; padding-left:2px; padding-right:2px; padding-top:2px; padding-bottom:2px;}

</style>

<digi:instance property="aimUserDetailForm" />

<digi:errors/>

<c:if test="${not empty aimUserDetailForm.mailingAddress}">
					<table class="inside" width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td colspan="2" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
								<digi:trn key="aim:userProfile">User Profile</digi:trn>
							</td>
						</tr>
						<tr>
							<td width="50%" class="inside">
								<digi:trn key="aim:firstName">First Name</digi:trn>
						  </td>
							<td width="50%" class="inside">
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
								<bean:write name="aimUserDetailForm" property="address" />&nbsp;
							</td>
						</tr>
						<tr>
							<td class="inside">
								<digi:trn key="aim:organizationName">Organization Name</digi:trn>
							</td>
							<td class="inside">
								<bean:write name="aimUserDetailForm" property="organizationName" />&nbsp;
							</td>
						</tr>
						<tr>
							<td class="inside"><digi:trn key="aim:autologinlink">Autologin Link</digi:trn></td>
							<td class="inside">
								<%
								String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTO_LOGIN);
								Long id = ((org.digijava.ampModule.aim.helper.TeamMember) session.getAttribute("currentMember")).getMemberId();
								if( request.getParameter("id") != null && value.equalsIgnoreCase("true") && id.equals(new Long(request.getParameter("id")))){
									org.digijava.kernel.user.User user = (org.digijava.kernel.user.User) session.getAttribute("org.digijava.kernel.user");
									String encryptedPass = org.digijava.kernel.util.ShaCrypt.crypt(user.getEmail() + "_" + user.getPassword());
								%>
									<a href="/<%=request.getContextPath()%>repository/aim/view/autologin.jsp?user=<%=user.getEmail()%>&password=<%=encryptedPass%>&workspaceId=<%=id%>">
										<digi:trn key="aim:autologin"><b>Autologin</b></digi:trn>
									</a>
								<%}%>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="inside" colspan="2" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td class="inside" bgcolor="#C7D4DB" colspan="2" align="center">
								<b><digi:trn key="aim:teamsAssociatedWith">Teams Associated with</digi:trn></b>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div style="overflow: auto; height: 250px">
									<table width="100%" class="inside">
										<tr>
											<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="width:60%;" align="center">
												<b><digi:trn key="aim:teamName">Team Name</digi:trn></b>
											</td>
											<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" align="center">
												<b><digi:trn key="aim:role">Role</digi:trn></b>
											</td>
										</tr>
										
										<c:forEach var="teamMemberDetail" items="${aimUserDetailForm.teamMemberTeamHelpers}">
											<tr>
					                           	<td class="inside" style="width:60%"><c:out value="${teamMemberDetail.teamName}"/></td>
												<td class="inside"><digi:trn>${teamMemberDetail.roleName}</digi:trn></td>
				                       		</tr>
										</c:forEach>
										
									</table>
								</div>
							</td>
						</tr>
						</table>
						</c:if>
<br>					
<div class="buttons" align="center">	
<!-- 
<input type="button" value="<digi:trn>Close</digi:trn>" class="buttonx_sm btn" onclick="hidePWorkspaceframe()">
 -->				
	
</div>
	
