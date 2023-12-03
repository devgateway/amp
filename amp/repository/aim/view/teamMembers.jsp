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
		else{
			return confirm("<digi:trn jsFriendly='true'>Are you sure you want to remove selected member(s)?</digi:trn>");
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
		else{
			return confirm("<digi:trn jsFriendly='true'>Are you sure you want to remove selected member(s)?</digi:trn>");
		}
	}
	return true;
}	

function addTeamMember(id) {
	<digi:context name="add" property="context/ampModule/moduleinstance/showAddTeamMember.do" />
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
<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">
	<tr>
		<td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
		</td>
	</tr>
	<tr>
	<td>
									<c:set var="selectedTab" value="1" scope="request"/>
										
									<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
										<tr>
										<td height=33>
											<div class="breadcrump_cont">
												<span class="sec_name">
													<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
												</span>
												
												<span class="breadcrump_sep">|</span>
												<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
													<digi:trn key="aim:portfolio">Portfolio</digi:trn>
												</digi:link>
												<span class="breadcrump_sep"><b>�</b></span>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
												</c:set>
												<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
												<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
												<span class="breadcrump_sep"><b>�</b></span>
												<span class="bread_sel"><digi:trn key="aim:members">Members</digi:trn></span>
											</div>
										</td>
									</tr>
										<tr>
											<td valign="top">
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
										
													<jsp:include page="teamSetupMenu.jsp" flush="true" />
										
										
											<div style="padding: 10px;">
												<table border="0" cellpadding="0" cellspacing="0">
													<tr>
														<td nowrap>
															<span class="team_name" style="font-size:11px;"><digi:trn key="aim:teamname">Team Name</digi:trn>:</span>
														</td>
														<td nowrap width="45%">
															<span class="team_name" style="font-size:11px;"><strong><c:out value="${aimTeamMemberForm.teamName}"/></strong></span>
														</td>
														<td nowrap>
															<span class="w_manager" style="font-size:11px;"><digi:trn key="aim:workspaceManager">Workspace Manager</digi:trn>:&nbsp;</span>
														</td>
														<td nowrap width="45%">
															<table border="0" cellpadding="0" cellspacing="0" style="font-size:11px">
																<c:forEach var="mem" items="${aimTeamMemberForm.teamMembers}">
																	
																		<tr>
																			<td nowrap>
																				<strong>
																					<c:if test="${mem.teamHead == true}">
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
																						(<c:out value="${mem.email}"/>)
																					</c:if>
																				</strong>
																		</td>
																	</tr>
																</c:forEach>
															</table>
														</td>
													</tr>
												</table>
											</div>

										
										<table class="inside members normal" width="970" cellpadding="0" cellspacing="0">
											<tr>
											  	<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">&nbsp;</td>
											    <td width=50% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Name</digi:trn></b></td>
											    <td width=50% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>User ID</digi:trn></b></td>
											    <%--
											    <td width=30% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"><b class="ins_title">Role</b></td>
											    --%>
											</tr>
											<logic:empty name="aimTeamMemberForm" property="teamMembers">
												<tr>
													<td colspan="4">
														<digi:trn key="aim:noMembers">No members present</digi:trn>
													</td>
												</tr>
											</logic:empty>
											<logic:notEmpty name="aimTeamMemberForm" property="teamMembers">
												<%--
											<tr>
											  <td class="inside" valign="top"><input type="checkbox" name="checkbox" value="checkbox" /></td>
											    <td class="inside" valign="top"><span class="desktop_project_name normal">aaa aaa</span></td>
											    <td class="inside" valign="top"><span class="desktop_project_name normal">aaa@aaa.org</span></td>
											    
											    <td class="inside" valign="top"><select name="" class="inputx insidex"/><option value="1">Workspace Member</option></select><input name="role" type="submit" id="role" value="Change Role" /></td>
											    
											</tr>
											--%>
											
													<c:forEach var="mem" items="${aimTeamMemberForm.teamMembers}">
														<c:if test="${mem.teamHead == false}">
															<tr>
																<td class="inside" valign="top">
																	<html:multibox property="selMembers" >
																	<c:out value="${mem.memberId}"/>
																	</html:multibox>																
																</td>
																<td class="inside" valign="top">
																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="id">
																	<c:out value="${mem.memberId}"/>
																	</c:set>	
																	<c:set target="${urlParams}" property="action" value="edit" />
																	<c:set var="translation">
																	<digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
																	</c:set>
																	<digi:link href="/viewMemberDetails.do" name="urlParams" title="${translation}" styleClass="l_sm">
																	<c:out value="${mem.memberName}"/>
																	</digi:link>
																</td>
																<td class="inside" valign="top">
																	<c:out value="${mem.email}"/>
																</td>																	
															</tr>
														</c:if>
													</c:forEach>
											
											</logic:notEmpty>
											</table>
											<br />
											<div class="buttons" align="center">
												<input type="button" value="<digi:trn key='btn:addMember'>Add Member</digi:trn>" class="buttonx_sm btn"  onclick="addTeamMember('<c:out value="${aimTeamMemberForm.teamId}"/>')"/>
												<html:submit styleClass="buttonx_sm btn" property="removeMember"  onclick="return checkSelMembers()">
													<digi:trn key="btn:removeSelectedMembers"> Remove Selected Members</digi:trn> 
												</html:submit>
											</div>
			
										
										</div>
										</div>											
												
											</td>
										</tr>
									</table>
										
</td></tr>
</table>
</digi:form>





