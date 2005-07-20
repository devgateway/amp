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
		var selectbox = document.aimTeamActivitiesForm.checkAll;
		var items = document.aimTeamActivitiesForm.selActivities;
		if (document.aimTeamActivitiesForm.selActivities.checked == true || 
							 document.aimTeamActivitiesForm.selActivities.checked == false) {
				  document.aimTeamActivitiesForm.selActivities.checked = selectbox.checked;
		} else {
			for(i=0; i<items.length; i++){
				document.aimTeamActivitiesForm.selActivities[i].checked = selectbox.checked;
			}
		}
	}

	function validate() {
		if (document.aimTeamActivitiesForm.selActivities.checked != null) {
			if (document.aimTeamActivitiesForm.selActivities.checked == false) {
				alert("Please choose an activity to remove");
				return false;
			}				  
		} else {
			var length = document.aimTeamActivitiesForm.selActivities.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimTeamActivitiesForm.selActivities[i].checked == true) {
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
			return true;
		}		  

	}
	
	function sortMe(val) {
		<digi:context name="sel" property="context/module/moduleinstance/teamActivityList.do" />
			url = "<%= sel %>" ;
			
			var sval = document.aimTeamActivitiesForm.sort.value;
			var soval = document.aimTeamActivitiesForm.sortOrder.value;
			
			if ( val == sval ) {
				if (soval == "asc")
					document.aimTeamActivitiesForm.sortOrder.value = "desc";
				else if (soval == "desc")
					document.aimTeamActivitiesForm.sortOrder.value = "asc";	
			}
			else
				document.aimTeamActivitiesForm.sortOrder.value = "asc";

			document.aimTeamActivitiesForm.sort.value = val;
			document.aimTeamActivitiesForm.action = url;
			document.aimTeamActivitiesForm.submit();
	}
	
	function page(val) {
		<digi:context name="sel" property="context/module/moduleinstance/teamActivityList.do" />
			url = "<%= sel %>?page=" + val ;
			document.aimTeamActivitiesForm.action = url;
			document.aimTeamActivitiesForm.submit();
	}

-->

</script>

<digi:instance property="aimTeamActivitiesForm" />
<digi:form action="/updateTeamActivity.do" method="post">

<html:hidden property="sort" />
<html:hidden property="sortOrder" />
<html:hidden property="page" />

<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" valign="top" align="left">
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
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;						
						<digi:trn key="aim:activityList">Activity List</digi:trn>
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
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">	
										<tr><td>
											<digi:errors />
										</td></tr>
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=177>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=160>
															<digi:trn key="aim:activityListManager">&nbsp;
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
												<table border=0 cellPadding=0 cellSpacing=1 class=box-border-nopadding width="100%">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#dddddd">
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																</td>
																<td valign="center" align="center" bgcolor="#dddddd">
																	<a href="javascript:sortMe('activity')" title="Click here to sort by Activity Details">
																		<b><digi:trn key="aim:activityList">Activity List</digi:trn></b>
																	</a>
																</td>
																<td bgColor=#dddddb align="center" width="20%" bgcolor="#dddddd">
																	<a href="javascript:sortMe('donor')" title="Click here to sort by Donors">
																		<b><digi:trn key="aim:donors">Donors</digi:trn></b>
																	</a>
																</td></tr>
															</table>
														</td>
													</tr>
													<logic:empty name="aimTeamActivitiesForm" property="activities">
													<tr>
														<td align="center">
															<table width="100%" cellSpacing=2 cellPadding=3 vAlign="top" align="center"
															bgcolor="#f4f4f2">
																<tr><td bgcolor="#f4f4f2" align="center">
																	<digi:trn key="aim:noActivitiesPresent">
																		No activities present
																	</digi:trn>
																</td></tr>
															</table>														
														</td>
													</tr>	
													<tr>`
														<td align="center">
															<table width="100%" cellSpacing=2 cellPadding=3 vAlign="top" align="center"
															bgcolor="#ffffff">
																<tr><td bgcolor="#ffffff" align="center">
																	<html:submit styleClass="dr-menu" value="Add Activity To Workspace" property="addActivity" />
																</td></tr>
															</table>
														</td>
													</tr>	
													</logic:empty>
							
													<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
															bgcolor="#dddddd">
															<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities" 
															type="org.digijava.module.aim.helper.Activity">
																<tr><td width=3 bgcolor="#f4f4f2">
																	<html:multibox property="selActivities">
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
																<td align="center" width="20%" bgcolor="#f4f4f2">
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
																		<html:submit styleClass="dr-menu" value="Add Activity To Workspace" 
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
												</table>
											</td>
										</tr>
										<logic:notEmpty name="aimTeamActivitiesForm" property="pages">
											<tr>
												<td>
													<digi:trn key="aim:pages">
														Pages :
													</digi:trn>
														<logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" type="java.lang.Integer">
													  	<bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />
														
														<% if (currPage.equals(pages)) { %>
																<%=pages%>
														<%	} else { %>
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</bean:define>
															<a href="javascript:page(<%=pages%>)" title="<%=translation%>"><%=pages%></a>
														<% } %>
														|&nbsp; 
													</logic:iterate>
												</td>
											</tr>
										</logic:notEmpty>											
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
