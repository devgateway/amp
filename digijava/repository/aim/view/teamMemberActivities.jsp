<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script type="text/javascript">

<!--

	function checkall() {
		var selectbox = document.aimMemberActivitiesForm.checkAll;
		var items = document.aimMemberActivitiesForm.selActivities;
		if (document.aimMemberActivitiesForm.selActivities.checked == true || 
							 document.aimMemberActivitiesForm.selActivities.checked == false) {
				  document.aimMemberActivitiesForm.selActivities.checked = selectbox.checked;
		} else {
			for(i=0; i<items.length; i++){
				document.aimMemberActivitiesForm.selActivities[i].checked = selectbox.checked;
			}
		}
	}

	function validate() {
		if (document.aimMemberActivitiesForm.selActivities.checked != null) {
			if (document.aimMemberActivitiesForm.selActivities.checked == false) {
				alert("Please choose an activity to remove");
				return false;
			}				  
		} else {
			var length = document.aimMemberActivitiesForm.selActivities.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimMemberActivitiesForm.selActivities[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose an activity to remove");
				return false;					  
			}				  
		}
		return true;			  
	}

	function confirmDelete() {
		var valid = validate();
		if (valid == true) {
			var flag = confirm("Are you sure you want to remove the selected activities");
			if(flag == false)
				return false;
			else 
				return true;
		} else {
			return false;
		}		  

	}

-->

</script>


<digi:errors/>
<digi:instance property="aimMemberActivitiesForm" />
<digi:form action="/updateMemberActivity.do" method="post">

<html:hidden property="memberId" />

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</bean:define>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
						</bean:define>
						<digi:link href="/teamMemberList.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:memberActivities">
						Member Activities
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Team Workspace Setup</span>
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
									<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams1}" property="id">
										<bean:write name="aimMemberActivitiesForm" property="memberId" />
									</c:set>									
									<table bgColor=#f4f4f2 align="center" valign="top" cellPadding=0 cellspacing=1>
										<tr>
											<td bgColor=#222e5d noWrap>
												&nbsp;
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewMemberActivities">Click here to view Member Activities</digi:trn>
												</bean:define>
												<digi:link href="/teamMemberActivities.do" name="urlParams1" 
												styleClass="sub-nav2" title="<%=translation%>" >
													<digi:trn key="aim:memberActivities">
														Member Activities
													</digi:trn>
												</digi:link>
												&nbsp;
											</td>											
											<td bgColor=#222e5d noWrap>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewMemberReports">Click here to view Member Reports</digi:trn>
												</bean:define>
												&nbsp;
												<digi:link href="/teamMemberReports.do" name="urlParams1" styleClass="sub-nav2" 
												title="<%=translation%>" >
													<digi:trn key="aim:memberRepor">Member Reports</digi:trn>
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
												<table border="0" cellPadding=0 cellSpacing=0 width=167>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=150>
															<digi:trn key="aim:activityListManager">
															Activity List Manager
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
												<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center">
															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#dddddd">
																	<c:if test="${!empty aimMemberActivitiesForm.activities}">	
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																	</c:if>
																</td>
																<td valign="center" align="center" bgcolor="#dddddd">
																	<b><digi:trn key="aim:activityList">Activity List</digi:trn>
																	for the member <bean:write name="aimMemberActivitiesForm" 
																	property="memberName" /></b>
																</td>
																<td bgColor=#dddddb align="center" width="15%" bgcolor="#dddddd">
																	<b><digi:trn key="aim:donors">
																	Donors
																	</digi:trn></b>
																</td></tr>
															</table>														
														</td>
													</tr>
													<logic:empty name="aimMemberActivitiesForm" property="activities">
													<tr>
														<td align="center">
															<span class="note">
															<digi:trn key="aim:noActivitiesPresent">
															No activities present
															</digi:trn></span>
														</td>
													</tr>	
													<tr>
														<td align="center">
															<html:submit styleClass="dr-menu" value="Add Activity To Member's Workspace" 
															property="addActivity" />
														</td>
													</tr>	
													</logic:empty>

													<logic:notEmpty name="aimMemberActivitiesForm" property="activities">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
															bgcolor="#dddddd">
															<logic:iterate name="aimMemberActivitiesForm" property="activities" id="activities" 
															type="org.digijava.module.aim.helper.Activity">
																<tr><td width=3 bgcolor="#f4f4f2">
																	<html:multibox property="selActivities" >
																		<bean:write name="activities" property="activityId" />
																	</html:multibox>
																</td>
																<td bgcolor="#f4f4f2">
																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="id">
																		<bean:write name="activities" property="activityId" />
																	</c:set>
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToViewActivityDetails">
																		Click here to view Activity Details</digi:trn>
																	</bean:define>
																	<digi:link href="/getActivityDetails.do" name="urlParams" title="<%=translation%>" 
																	target="_blank">
																		<bean:write name="activities" property="name" />
																	</digi:link>
																</td>
																<td align="center" width="15%" bgcolor="#f4f4f2">
																	<bean:write name="activities" property="donors" />
																</td></tr>
															</logic:iterate>
															</table>
														</td>
													</tr>													
													<tr>
														<td align="center" colspan=2>
															<table cellspacing="5">
																<tr>
																	<td>
																		<html:submit styleClass="dr-menu" value="Add Activity To Member's Workspace" 
																		property="addActivity" />
																	</td>
																	<td>	
																		<html:submit styleClass="dr-menu" value="Remove selected activities" 
																		property="removeActivity" onclick="return confirmDelete()"/>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</logic:notEmpty>
													
							
													<%--
													<logic:notEmpty name="aimMemberActivitiesForm" property="activities">
													<logic:iterate name="aimMemberActivitiesForm" property="activities" id="activities" 
													type="org.digijava.module.aim.dbentity.AmpActivity">
													<tr>
														<td>
															<html:multibox property="selActivities" >
																<bean:write name="activities" property="ampActivityId" />
															</html:multibox> &nbsp;
															<bean:write name="activities" property="name" />
														</td>
													</tr>
													</logic:iterate>
													<tr>
														<td align="center">
															<table cellspacing="5">
																<tr>
																	<td>
																		<html:submit styleClass="dr-menu" value="Add activity" 
																		property="addActivity" />
																	</td>
																	<td>	
																		<html:submit styleClass="dr-menu" value="Remove selected activities" 
																		property="removeActivity" />
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</logic:notEmpty>

													--%>
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

</digi:form>
