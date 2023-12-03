<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimTeamMemberForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>
<script language=javascript>
function showUserProfile(id){
	var param = "~edit=true~id="+id;
    previewWorkspaceframe('/aim/default/userProfile.do',param);
	
}
</script>


<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33>
						<span class=crumb>					
					<c:set var="clickToViewAdmin">
					<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="clickToViewWorkspaceManager">
						<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
						</c:set>
						<digi:link href="/workspaceManager.do" styleClass="comment" title="${clickToViewWorkspaceManager}" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<!--<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<bean:write name="aimTeamMemberForm" property="teamName" />
					</span></td>
				</tr>-->
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr><td noWrap width=750 vAlign="top">
						<table cellpadding="0" cellspacing="1" width="100%" border="0">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
								
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
										<tr><td bgColor=#C7D4DB class=box-title height="25" align="center" style="font-size:12px; font-weight:bold;">
											<!-- Table title -->
											<digi:trn key="aim:membersFor">Members for </digi:trn>
											<bean:write name="aimTeamMemberForm" property="teamName" />
											<!-- end table title -->
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#cccccc" style="font-size:12px;">
													<digi:errors/>
													<logic:empty name="aimTeamMemberForm" property="teamMembers">
														<tr bgcolor="#ffffff">
															<td colspan="3" align="center">
																<b><digi:trn>No workspace members present</digi:trn></b>
															</td>
														</tr>
													</logic:empty>
													<logic:notEmpty name="aimTeamMemberForm" property="teamMembers">
														<logic:iterate name="aimTeamMemberForm" property="teamMembers" id="teamMembers"
														type="org.digijava.ampModule.aim.helper.TeamMember">
														<tr bgcolor=#ffffff>
															<td>
																<logic:equal name="teamMembers" property="teamHead" value="true">*
																</logic:equal>
																<a href="javascript:showUserProfile(<bean:write name="teamMembers" property="memberId" />)">	
																	<bean:write name="teamMembers" property="memberName" />
																</a>											
															</td>
															<td align="center" width="80">  
																<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams}" property="id">
																	<bean:write name="teamMembers" property="memberId" />
																</c:set>
																<c:set target="${urlParams}" property="action" value="edit" />
																<c:set var="clickToEditTeamMemberDetails">
																	<digi:trn key="aim:clickToEditTeamMemberDetails"> Click here to Edit Team Member Details</digi:trn>
																</c:set>
																[ <digi:link href="/getTeamMemberDetails.do" name="urlParams" title="${clickToEditTeamMemberDetails}" >
																	<digi:trn key="aim:showTeamMembersEdit">
																		Edit
																	</digi:trn>
																</digi:link> ]
															</td>
															<td align="center" width="80"> 
																<c:set target="${urlParams}" property="action" value="delete" />
																
																<c:set var="clickToDeleteTeamMember">
																<digi:trn key="aim:clickToDeleteTeamMember">Click here to Delete Team Member</digi:trn>
																</c:set>
																[ <digi:link href="/getTeamMemberDetails.do" name="urlParams" title="${clickToDeleteTeamMember}" >
																	<digi:trn key="aim:showTeamMembersDelete">
																		Delete
																	</digi:trn>
																</digi:link> ]															
															</td>
														</tr>
														</logic:iterate>
														<tr bgcolor="#ffffff"><td colspan="3">
															*
															<digi:trn key="aim:workspaceManager">	
															Workspace Manager
															</digi:trn>
														</td></tr>
													</logic:notEmpty>
													<!-- end page logic -->
											
											</table>
										</td></tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<td width=20>&nbsp;</td>
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="100%" border="0">	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7>
												<digi:trn key="aim:otherLinks">
												<b style="padding-left:5px; font-size:12px;">Other links</b>
												</digi:trn>
											</td>
											<td background="ampModule/aim/images/corner-r.gif" height="17" width="17"></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%" class=inside>
										<tr>
											<td class=inside>
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams1}" property="teamId">
													<bean:write name="aimTeamMemberForm" property="teamId" />
												</c:set>
												<c:set var="clickToAddTeamMember">
												<digi:trn key="aim:clickToAddWorkspaceMember">Click here to Add Workspace Member</digi:trn>
												</c:set>
												<c:set target="${urlParams1}" property="fromPage" value="1"/>
												<digi:link href="/showAddTeamMember.do" name="urlParams1" title="${clickToAddTeamMember}" >
													<digi:trn key="aim:addWorkspaceMember">Add Workspace Member </digi:trn>
												</digi:link>
											</td>
										</tr>
										
										<tr>
											<td class=inside>
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
													<c:set var="clickToViewAdmin">
														<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
													</c:set>
												<digi:link href="/admin.do" title="${clickToViewAdmin}" >
												<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

