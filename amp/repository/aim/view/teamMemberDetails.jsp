<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<digi:errors/>
<digi:instance property="aimTeamMemberForm" />
<digi:form action="/saveMemberDetails.do" method="post">
<digi:context name="digiContext" property="context" />

<html:hidden property="teamId" />
<html:hidden property="teamMemberId" />
<html:hidden property="action" />
<html:hidden property="userId" />
<html:hidden property="name" />

<table width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
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
							<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
						</c:set>
						<digi:link href="/teamMemberList.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:memberDetails">
						Member Details
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#3754a1>
								<td vAlign="top" width="100%">
									<jsp:include page="teamSetupMenu.jsp" flush="true" />								
								</td>
							</tr>
							
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">
									<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams}" property="id">
										<bean:write name="aimTeamMemberForm" property="teamMemberId" />
									</c:set>
									<table bgColor=#f4f4f2 align="center" valign="top" cellPadding=0 cellspacing=1>
										<tr>
											<td bgColor=#222e5d noWrap>
												&nbsp;
												<c:set var="translation">
													<digi:trn key="aim:clickToViewMemberActivities">Click here to view Member Activities</digi:trn>
												</c:set>
												<digi:link href="/teamMemberActivities.do" name="urlParams" 
												styleClass="sub-nav2" title="${translation}" >
													<digi:trn key="aim:memberActivities">
														Member Activities
													</digi:trn>
												</digi:link>
												&nbsp;
											</td>											
											<td bgColor=#222e5d noWrap>
											&nbsp;
												<c:set var="translation">
													<digi:trn key="aim:clickToViewMemberReports">Click here to view Member Reports</digi:trn>
												</c:set>
												<digi:link href="/teamMemberReports.do" name="urlParams" styleClass="sub-nav2" title="${translation}" >
													<digi:trn key="aim:memberRepor">
														Member Reports
													</digi:trn>
												</digi:link>
												&nbsp;
											</td>										
										</tr>
									</table>
								</td>
							</tr>							
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=117>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=100>
															<digi:trn key="aim:workspaceMembers">
															Workspace Members 
															</digi:trn>
														</td>
														<td background="module/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border valign="top">
												<table border=0 cellPadding=3 cellSpacing=1 class=box-border width="100%">
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center"><b>
															<digi:trn key="aim:memberDetails">
															Member Details
															</digi:trn></b>
														</td>
													</tr>
													<tr>
														<td>
															<table width="100%" border="0" cellspacing="1" cellPadding="3" bgcolor="#dddddd">
																<tr>
																	<td align="right" width="50%" bgcolor="#f4f4f2">
																		<b><digi:trn key="aim:memberName">Name</digi:trn></b>
																	</td>
																	<td align="left" width="50%" bgcolor="#f4f4f2">
																		<bean:write name="aimTeamMemberForm" property="name" />
																	</td>
																</tr>
																<tr>
																	<td align="right" width="50%" bgcolor="#f4f4f2">
																		<b><digi:trn key="aim:memberRole">Role</digi:trn></b>
																	</td>
																	<td align="left" width="50%" bgcolor="#f4f4f2">
																		<html:select property="role">
																			<%@include file="teamMemberRolesDropDown.jsp" %>
																			<%-- <html:optionsCollection name="aimTeamMemberForm" 
																			property="ampRoles" value="ampTeamMemRoleId" label="role" /> --%>
																		</html:select>
																	</td>
																</tr>
																<tr>	
																	<td align="center" colspan="2" bgcolor="#f4f4f2" cellPadding="1">
																		<table cellspacing="5">
																			<tr bgcolor="#f4f4f2">
																				<td>
																					<html:checkbox property="readPerms">
																						<digi:trn key="aim:readPerms">
																							Read
																						</digi:trn>
																					</html:checkbox>					
																				</td>
																			</tr>
																			<tr bgcolor="#f4f4f2">
																				<td>
																					<html:checkbox property="writePerms">
																						<digi:trn key="aim:writePerms">
																							Add / Update
																						</digi:trn>	
																					</html:checkbox>					
																				</td>
																			</tr>
																			<tr bgcolor="#f4f4f2">
																				<td>
																					<html:checkbox property="deletePerms">
																						<digi:trn key="aim:deletePerms">
																							Delete
																						</digi:trn>
																					</html:checkbox>						
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr bgcolor="#ffffff">
																	<td align="center" colspan="2">
																		<table width="100%" cellspacing="5">
																			<tr>
																				<td width="50%" align="right">
																					<c:set var="translation"><digi:trn key="aim:teamWorkspaceSetup:members:save">save</digi:trn> </c:set>
																					<html:submit value="${translation}" styleClass="dr-menu"/>
																				</td>	
																				<td width="50%" align="left">	
																					<c:set var="translation"><digi:trn key="aim:teamWorkspaceSetup:members:cancel">cancel</digi:trn> </c:set>
																					<html:reset value="${translation}" styleClass="dr-menu" onclick="javascript:history.go(-1)"/>
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
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
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

</digi:form>




