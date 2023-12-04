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
				alert("Please choose an activity to assign");
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
				alert("Please choose an activity to assign");
				return false;					  
			}				  
		}
		return true;			  
	}

	function sortMe(val) {
		<digi:context name="sel" property="context/module/moduleinstance/updateMemberActivity.do" />
			url = "<%= sel %>" ;
			
			var sval = document.aimMemberActivitiesForm.sort.value;
			var soval = document.aimMemberActivitiesForm.sortOrder.value;
			
			if ( val == sval ) {
				if (soval == "asc")
					document.aimMemberActivitiesForm.sortOrder.value = "desc";
				else if (soval == "desc")
					document.aimMemberActivitiesForm.sortOrder.value = "asc";	
			}
			else
				document.aimMemberActivitiesForm.sortOrder.value = "asc";

			document.aimMemberActivitiesForm.sort.value = val;
			document.aimMemberActivitiesForm.action = url;
			document.aimMemberActivitiesForm.submit();
	}

-->
</script>

<digi:errors/>
<digi:instance property="aimMemberActivitiesForm" />
<digi:form action="/updateMemberActivity.do" method="post">

<html:hidden property="sort" />
<html:hidden property="sortOrder" />
<html:hidden property="teamId" />
<html:hidden property="memberId" />

<jsp:include page="teamPagesHeader.jsp"  />
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>

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
							<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
						</c:set>
						<digi:link href="/teamMemberList.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="id">
							<bean:write name="aimMemberActivitiesForm" property="memberId" />
						</c:set>						
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMemberActivities">Click here to view Member Activities</digi:trn>
						</c:set>
						<digi:link href="/teamMemberActivities.do" name="urlParams" title="${translation}" styleClass="comment">
							<digi:trn key="aim:memActivities">
								Member Activities
							</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addMemberActivity">
						Add Activity for Members
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#3754a1>
								<td vAlign="top" width="100%">
									<jsp:include page="teamSetupMenu.jsp"  />								
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">
									<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams1}" property="id">
										<bean:write name="aimMemberActivitiesForm" property="memberId" />
									</c:set>									
									<table bgColor=#f4f4f2 align="center" valign="top" cellpadding="0" cellspacing="1">
										<tr>
											<td bgColor=#222e5d noWrap>
												&nbsp;
												<c:set var="translation">
													<digi:trn key="aim:clickToViewMemberActivities">Click here to view Member Activities</digi:trn>
												</c:set>
												<digi:link href="/teamMemberActivities.do" name="urlParams1" 
												styleClass="sub-nav2" title="${translation}" >
													<digi:trn key="aim:memberActivities">
														Member Activities
													</digi:trn>
												</digi:link>
												&nbsp;
											</td>											
											<td bgColor=#222e5d noWrap>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewMemberReports">Click here to view Member Reports</digi:trn>
												</c:set>
												&nbsp;
												<digi:link href="/teamMemberReports.do" name="urlParams1" styleClass="sub-nav2" 
												title="${translation}" >
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
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellpadding="0" cellspacing="0" width=167>
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
												<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center">
															<table width="100%" cellspacing="1" cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#dddddd">
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																</td>
																<td width="20%" bgcolor="#dddddd">
																	<b><digi:trn key="aim:ampId">AMP ID</digi:trn></b>
																</td>
																<td valign="center" align="center" bgcolor="#dddddd">
																	<a href="javascript:sortMe('activity')" title="Click here to sort by Activity Details">
																		<b><digi:trn key="aim:listOfTeamActivities">
																		List of team activities
																	</digi:trn></b>
																	</a>																
																</td>
																<td bgColor=#dddddb align="center" width="20%" bgcolor="#dddddd">
																	<a href="javascript:sortMe('donor')" title="Click here to sort by Donors">
																		<b><digi:trn key="aim:donors">
																	Donors
																	</digi:trn></b>
																	</a>																
																</td></tr>
															</table>
														</td>
													</tr>
													<logic:empty name="aimMemberActivitiesForm" property="activities">
													<tr>
														<td>
															<digi:trn key="aim:noActivitiesPresent">
															No activities present
															</digi:trn>
														</td>
													</tr>	
													</logic:empty>

													<logic:notEmpty name="aimMemberActivitiesForm" property="activities">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellspacing="1" cellPadding=2 vAlign="top" align="left"
															bgcolor="#dddddd">
															<logic:iterate name="aimMemberActivitiesForm" property="activities" id="activities" 
															>
																<tr><td width=3 bgcolor="#f4f4f2">
																	<html:multibox property="selActivities" >
																		<bean:write name="activities" property="ampActivityId" />
																	</html:multibox>
																</td>
																<td width="20%" bgcolor="#f4f4f2">
																	<bean:write name="activities" property="ampId" />
																</td>
																<td bgcolor="#f4f4f2">
																	<jsp:useBean id="url" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${url}" property="activityId">
																		<bean:write name="activities" property="ampActivityId" />
																	</c:set>
																	<c:set target="${url}" property="pageId" value="3"/>
																	<c:set var="translation">
																		<digi:trn key="aim:clickToViewActivityDetails">
																		Click here to view Activity Details</digi:trn>
																	</c:set>
																	<digi:link href="/viewActivityPreview.do" name="url" 
																	title="${translation}">
																		<bean:write name="activities" property="name" />
																	</digi:link>
																</td>
																<td align="center" width="20%" bgcolor="#f4f4f2">
																	<bean:write name="activities" property="donors" />
																</td></tr>
															</logic:iterate>
															</table>
														</td>
													</tr>													
													<tr>
														<td align="center">
															<table cellspacing="5">
																<tr>
																	<td>
																		<html:submit styleClass="dr-menu" value="Assign activity" 
																		property="assignActivity" onclick="return validate()"/>
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

</digi:form>



