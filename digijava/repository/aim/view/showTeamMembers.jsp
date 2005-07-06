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
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
						</bean:define>
						<digi:link href="/workspaceManager.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:members">
						Members
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<bean:write name="aimTeamMemberForm" property="teamName" />
					</span></td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">
									&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="95%" border=0>	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width="100%">
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title height=20 align="center">
															<!-- Table title -->
															<digi:trn key="aim:membersFor">Members for </digi:trn>
															<bean:write name="aimTeamMemberForm" property="teamName" />
															<!-- end table title -->
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
													<tr bgColor=#dddddb>
														<!-- header -->
														<td bgColor=#dddddb align="left" width="60%" height=20>
															<b>
															<digi:trn key="aim:memberName">Name</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="left" colspan="3" height=20>
															<b>
															<digi:trn key="aim:memberRole">Role</digi:trn>
															</b>
														</td>
														<!-- end header -->
													</tr>
													<!-- Page Logic -->
													<logic:empty name="aimTeamMemberForm" property="teamMembers">
														<tr>
															<td colspan="4">
																<b>No team members present</b>
															</td>
														</tr>
													</logic:empty>
													<logic:notEmpty name="aimTeamMemberForm" property="teamMembers">
														<logic:iterate name="aimTeamMemberForm" property="teamMembers" id="teamMembers"
														type="org.digijava.module.aim.helper.TeamMember">
														<tr bgcolor=#f4f4f2 height="20">
															<td width="60%">	
																<bean:write name="teamMembers" property="memberName" />
															</td>
															<td width="34%">
																<bean:write name="teamMembers" property="roleName" />
															</td>
															<td align="center"> 
																<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams}" property="id">
																	<bean:write name="teamMembers" property="memberId" />
																</c:set>
																<c:set target="${urlParams}" property="action" value="edit" />
																<bean:define id="translation">
																	<digi:trn key="aim:clickToEditTeamMemberDetails">Click here to Edit Team Member Details</digi:trn>
																</bean:define>
																[ <digi:link href="/getTeamMemberDetails.do" name="urlParams" title="<%=translation%>" >
																	<digi:trn key="aim:showTeamMembersEdit">
																		Edit
																	</digi:trn>
																</digi:link> ]
															</td>
															<td align="center"> 
																<c:set target="${urlParams}" property="action" value="delete" />
																<bean:define id="translation">
																	<digi:trn key="aim:clickToDeleteTeamMember">Click here to Delete Team Member</digi:trn>
																</bean:define>
																[ <digi:link href="/getTeamMemberDetails.do" name="urlParams" title="<%=translation%>" >
																	<digi:trn key="aim:showTeamMembersDelete">
																		Delete
																	</digi:trn>
																</digi:link> ]															
															</td>
														</tr>
														</logic:iterate>
													</logic:notEmpty>
													<!-- end page logic -->
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>
					</td>
					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams1}" property="teamId">
													<bean:write name="aimTeamMemberForm" property="teamId" />
												</c:set>
												<bean:define id="translation">
													<digi:trn key="aim:clickToAddTeamMember">Click here to Add Team Member</digi:trn>
												</bean:define>

												<c:set target="${urlParams1}" property="fromPage" value="1"/>
												<digi:link href="/showAddTeamMember.do" name="urlParams1" title="<%=translation%>" >
													<digi:trn key="aim:addTeamMember">Add Team Member </digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToAddRoles">Click here to Add Roles</digi:trn>
												</bean:define>
												<digi:link href="/roles.do" title="<%=translation%>" >
												<digi:trn key="aim:roles">Add Roles</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</bean:define>
												<digi:link href="/admin.do" title="<%=translation%>" >
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

