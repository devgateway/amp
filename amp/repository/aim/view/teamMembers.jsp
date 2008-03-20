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

<script language="JavaScript">
<!--

function checkSelMembers() {
	var msg='';
	if (document.aimTeamMemberForm.selMembers.checked != null) { 
		if (document.aimTeamMemberForm.selMembers.checked == false) {
			msg+='\n <digi:trn key="aim:members:selectMembersToRemove">Please choose a member to remove</digi:trn>';
			alert(msg);
			return false;
		}
	} else { // 
		var length = document.aimTeamMemberForm.selMembers.length;	  
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimTeamMemberForm.selMembers[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			msg+='\n <digi:trn key="aim:members:selectMembersToRemove">Please choose a member to remove</digi:trn>';
			alert(msg);
			return false;					  
		}
	}
	return true;
}	

function addTeamMember(id) {
	<digi:context name="add" property="context/module/moduleinstance/showAddTeamMember.do" />
	document.aimTeamMemberForm.action = "<%= add %>~teamId="+id+"~fromPage=0";
	document.aimTeamMemberForm.target = "_self";
	document.aimTeamMemberForm.submit();
	return true;		  
}
-->
</script>

<digi:errors/>
<digi:instance property="aimTeamMemberForm" />
<digi:form action="/updateTeamMemberList.do" method="post">

<html:hidden property="teamId" />
<%--
<html:hidden property="action" />
--%>

<table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%">
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
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}">
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}">
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</span>
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
									<c:set var="selectedTab" value="4" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
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
												<table border="0" cellPadding=0 cellSpacing=0 width=132>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=115>&nbsp;
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
														<td bgColor=#dddddb align="center">
															<b>
															<digi:trn key="aim:membersForTheTeam">
																Members for the team -  
															</digi:trn>
															<c:out value="${aimTeamMemberForm.teamName}"/></b>
														</td>
													</tr>
													<logic:empty name="aimTeamMemberForm" property="teamMembers">
													<tr>
														<td>
															<digi:trn key="aim:noMembers">
															No members present
															</digi:trn>
														</td>
													</tr>
													<tr>
														<td>
															<html:submit  styleClass="dr-menu" property="addMember">
																<digi:trn key="btn:addMember">Add Member</digi:trn> 
															</html:submit>
															
														</td>
													</tr>
													</logic:empty>
							
													<logic:notEmpty name="aimTeamMemberForm" property="teamMembers">

													<tr>
														<td>
															<table width="100%" cellPadding=3 cellSpacing=1 vAlign="top" align="left" 
															bgcolor="#dddddd">
																<tr>	
																	<td bgcolor="#f4f4f2" width="100">
																		<b>
																		<digi:trn key="aim:workspaceManager">
																		Workspace Manager</digi:trn></b>
																	</td>
																	<c:forEach var="mem" items="${aimTeamMemberForm.teamMembers}">
																		<c:if test="${mem.teamHead == true}">
																		<td bgcolor="#f4f4f2">
																			<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams1}" property="id">
																				<c:out value="${mem.memberId}"/>
																			</c:set>	
																			<c:set target="${urlParams1}" property="action" value="edit" />
																				<c:set var="translation">
																					<digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
																				</c:set>
																				<digi:link href="/viewMemberDetails.do" name="urlParams1" title="${translation}" >
																			<c:out value="${mem.memberName}"/>
																			</digi:link>																		
																		</td>
																		<td bgcolor="#f4f4f2">
																			<c:out value="${mem.email}"/>
																		</td>																		
																		</c:if>
																	</c:forEach>																	
																</tr>
															</table>
														</td>
													</tr>

													<tr>
														<td>
															<table width="100%" cellPadding=3 cellSpacing=1 vAlign="top" align="left"
															bgcolor="#dddddd">
																<tr>
																	<td colspan="2" bgcolor="#dddddd">
																	<b>
																	<digi:trn key="aim:teamMemberName">
																		Name</digi:trn></b>
																	</td>
																	<td bgcolor="#dddddd">
																	<b>
																	<digi:trn key="aim:userId">
																		User Id</digi:trn></b>
																	</td>
																</tr>
																<c:forEach var="mem" items="${aimTeamMemberForm.teamMembers}">
																<c:if test="${mem.teamHead == false}">
																<tr>
																	<td width="3" bgcolor="#f4f4f2">
																		<html:multibox property="selMembers" >
																			<c:out value="${mem.memberId}"/>
																		</html:multibox>																
																	</td>
																	<td width="50%" bgcolor="#f4f4f2">
																		<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="id">
																			<c:out value="${mem.memberId}"/>
																		</c:set>	
																		<c:set target="${urlParams}" property="action" value="edit" />
																		<c:set var="translation">
																			<digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
																		</c:set>
																		<digi:link href="/viewMemberDetails.do" name="urlParams" title="${translation}" >
																			<c:out value="${mem.memberName}"/>
																		</digi:link>
																	</td>
																	<td bgcolor="#f4f4f2">
																		<c:out value="${mem.email}"/>
																	</td>																	
																</tr>
																</c:if>
																</c:forEach>																	
															</table>
														</td>
													</tr>
													<tr>
														<td align="center">
															<table cellspacing="5">
																<tr>
																	<td>	
							<input type="button" value="<digi:trn key='btn:addMember'>Add Member</digi:trn>" class="dr-menu"  onclick="addTeamMember('<c:out value="${aimTeamMemberForm.teamId}"/>')"/>											
																	</td>
																	<td>
																		<html:submit   styleClass="dr-menu" property="removeMember"  onclick="return checkSelMembers()">
																			<digi:trn key="btn:removeSelectedMembers"> Remove Selected Members</digi:trn> 
																		</html:submit>


																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</logic:notEmpty>
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



