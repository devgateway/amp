<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

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
<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">
	<tr>
		<td>
			<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td height=33>
						<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
							<tr>
								<td valign="top">
									<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
										<c:set var="selectedTab" value="1" scope="request"/>
										<jsp:include page="teamSetupMenu.jsp" flush="true" />
										<table width="100%" cellpadding="0" cellspacing="0">
											<tr>
												<td>
													<div>
													<digi:link href="/teamMemberList.do" styleClass="l_sm">&lt;&lt;<digi:trn>Back to Members List</digi:trn></digi:link><br><br>
													<input type="hidden" value="showAddFromTeam" id="addFromWhere"/>
														<jsp:include page="addTeamMemberJSON.jsp" flush="true"/>								
								                     </div>
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</table>						
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>



<%--
<table cellspacing="0" cellpadding="0" width="100%" valign="top" align="left">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td width="100%">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left valign="top" width=750>

			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;					
						<c:set var="translation">
							<digi:trn key="aim:clickToViewWorkspaceMembers">Click here to view Workspace Members</digi:trn>
						</c:set>
						<digi:link href="/teamMemberList.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:workspaceMembers">
						Workspace Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;					
						<digi:trn key="aim:addTeamMembers">
						Add Members
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<div id="demo" >
										<c:set var="selectedTab" value="1" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp"  />
									</div>
									
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top" width="100%">
                                <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
									<jsp:include page="addTeamMember.jsp" />									
			                     </div>		
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>
						</table>	
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
 --%>




