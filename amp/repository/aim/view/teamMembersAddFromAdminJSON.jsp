<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

	function defaultPermsSelected() {
		document.aimTeamMemberForm.readPerms.disabled=true;
		document.aimTeamMemberForm.writePerms.disabled=true;
		document.aimTeamMemberForm.deletePerms.disabled=true;
	}

	function userSpecificPermsSelected() {
		document.aimTeamMemberForm.readPerms.disabled=false;
		document.aimTeamMemberForm.writePerms.disabled=false;
		document.aimTeamMemberForm.deletePerms.disabled=false;
	}
	
</script>

<digi:instance property="aimTeamMemberForm" />

<digi:form action="/addTeamMember.do" method="post">

<jsp:include page="teamPagesHeader.jsp" flush="true" />

<digi:errors/>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=1000 align=center>
	<tr>
		<td align=left vAlign=top width=450>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33>
						<span class="crumb">
							<c:set var="trn1">
								<digi:trn>Click here to goto Admin Home</digi:trn>
							</c:set>
							<digi:link href="/admin.do" styleClass="comment" title="${trn1}" >
								<digi:trn>Admin Home</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
							<c:set var="trn2">
								<digi:trn>Click here to goto workspace Manager</digi:trn>
							</c:set>
							<digi:link href="/workspaceManager.do" styleClass="comment" title="${trn2}" >
								<digi:trn>Workspace Manager</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
							<digi:trn>Add Members</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<!--<tr>
					<td height="16" vAlign="middle" width="571">
                    	<span class=subtitle-blue><digi:trn>Assign Members</digi:trn></span>
					</td>
				</tr>-->
				<tr>
					<td noWrap width=100% vAlign="top">
						<table width="100%" cellspacing="1" cellpadding=""="1">
							<tr>
								<td noWrap width="450" vAlign="top">
									<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="100%" style="font-size:12px;">
										<tr>
											<td width=50% align=right style="padding-bottom:15px;">
												<strong><digi:trn>Team Name</digi:trn>:</strong>&nbsp; <bean:write name="aimTeamMemberForm" property="teamName" />									
											</td>
											<td width=50% style="padding-bottom:15px; padding-left: 7px;">
												<strong><digi:trn>Workspace Manager</digi:trn>:</strong>&nbsp; 
												<c:if test="${not empty aimTeamMemberForm.teamHead}">
													${aimTeamMemberForm.teamHead.user.firstNames}&nbsp;${aimTeamMemberForm.teamHead.user.lastName} - &lt; ${aimTeamMemberForm.teamHead.user.email} &gt;
												</c:if>
											</td>
										</tr>										
										<tr>
											<td valign="top" colspan="2">
												<input type="hidden" value="showAddFromAdmin" id="addFromWhere"/>
												<jsp:include page="addTeamMemberJSON.jsp" flush="true"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
