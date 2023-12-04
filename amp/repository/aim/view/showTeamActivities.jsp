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
			var flag = confirm("<digi:trn>Are you sure you want to remove the selected activities</digi:trn>");
			if(flag == false)
				return false;
			else 
				return true;
		} else {
			return true;
		}		  
	}


	function resetSearch() {
		<digi:context name="searchOrg" property="context/module/moduleinstance/teamActivities.do"/>
		url = "<%= searchOrg %>?reset=true";
	     document.aimTeamActivitiesForm.action = url;
	     document.aimTeamActivitiesForm.submit();
		 return true;

	}

	function searchActivity(teamId) {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/teamActivities.do"/>
		     url = "<%= searchOrg %>?id="+teamId;
		     document.aimTeamActivitiesForm.action = url;
		     document.aimTeamActivitiesForm.submit();
			 return true;
	}

	function deleteActivities(){
		if(confirmDelete() == true){
			document.aimTeamActivitiesForm.submit();
		}
	}

	var enterBinder	= new EnterHitBinder('deleteActBtn');
	enterBinder.map(["actSearchKeyword"], "actSearchBtn");
-->

</script>

<digi:instance property="aimTeamActivitiesForm" />

<table width="1000" cellspacing="0" cellpadding="0" valign="top" align=center>
<tr><td>
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
</td></tr>
<tr><td>
<digi:form action="/removeTeamActivity.do" method="post">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=872 border="0">
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=850>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33>
						<span class="crumb">
						<c:set var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
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
						<digi:trn key="aim:activities">
						Activities
						</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<!--<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<bean:write name="aimTeamActivitiesForm" property="teamName" />
					</span></td>
				</tr>-->
				<!-- filter -->
				<tr>
					<td>
						<table>
							<tr>
								<td width="195">
									<digi:trn>Keyword</digi:trn>&nbsp;
									<html:text property="keyword" styleClass="inp-text" styleId="actSearchKeyword"/>
								</td>
								<td width="120">
									<digi:trn>Results</digi:trn>&nbsp;
									<html:select property="tempNumResults" styleClass="inp-text" onchange="return searchActivity('${aimTeamActivitiesForm.teamId }')">
										<c:if test="${aimTeamActivitiesForm.tempNumResults!=-1}">
											<html:option value="${aimTeamActivitiesForm.tempNumResults}"><digi:trn>Current</digi:trn>: ${aimTeamActivitiesForm.tempNumResults}</html:option>
										</c:if>	
										<html:option value="10">10</html:option>
										<html:option value="20">20</html:option>
										<html:option value="50">50</html:option>
										<html:option value="-1"><digi:trn>All</digi:trn></html:option>
									</html:select>
								</td>
								<td>
									<c:set var="trnResetBtn">
										<digi:trn>Reset</digi:trn>
									</c:set>
									<input type="button" value="${trnResetBtn}" class="dr-menu" onclick="return resetSearch()">
								</td>
								<td>					
									<c:set var="trnGoBtn">
										<digi:trn> GO </digi:trn>
									</c:set>
									<input type="button" value="${trnGoBtn}" class="dr-menu" onclick="return searchActivity('${aimTeamActivitiesForm.teamId }')" id="actSearchBtn">
								</td>
							</tr>
						</table>
					</td>
					
				</tr>
				
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="1000" cellspacing="1" align="center">
					<tr><td noWrap width=700 vAlign="top">
						<table cellpadding="0" cellspacing="1" width="100%" bgcolor="#FFFFFF">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left style="font-size:12px;">	
										<tr>
											<td width=25 bgcolor="#C7D4DB" align=center>
												<input type="checkbox" name="checkAll" onclick="checkall()">
											</td>										
											<td bgColor=#C7D4DB class=box-title height="25" align="center">
												<!-- Table title -->
												<b>
												<digi:trn key="aim:activitiesFor">Activities for </digi:trn>
												<bean:write name="aimTeamActivitiesForm" property="teamName" />
												</b>
												<!-- end table title -->
											</td>
										</tr>
										<tr><td colspan="2">
											<table width="100%" cellspacing="1" cellpadding=2 valign="top" align=left bgcolor="#CCCCCC" border="0">
													<logic:empty name="aimTeamActivitiesForm" property="activities">
													<tr bgcolor="#ffffff">
														<td colspan="2" align="center"><b>
															<digi:trn key="aim:noActivities">
															No activities present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
													<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities" 
													>
													<tr bgcolor="#ffffff">
														<td width="3">
															<html:multibox property="selActivities">
																<bean:write name="activities" property="ampActivityId" />
															</html:multibox>														
														</td>													
														<td width="98%">
															<bean:write name="activities" property="name" />
														</td>
													</tr>
													</logic:iterate>

													<logic:notEmpty name="aimTeamActivitiesForm" property="pages">
													<tr bgcolor="#ffffff">
														<td colspan=2 height="20">
															<digi:trn key="aim:pages">Pages:</digi:trn>
															<logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" 
															type="java.lang.Integer">
															<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams1}" property="page">
																<%=pages%>
															</c:set>
															<c:set target="${urlParams1}" property="id">
																<bean:write name="aimTeamActivitiesForm" property="teamId" />
															</c:set>

															<bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />
													
															<% if (currPage.equals(pages)) { %>
																<%=pages%>
															<%	} else { %>
																
															<c:set var="clickToViewNextPage">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</c:set>	
																<c:set target="${urlParams1}" property="keyword" value="${aimTeamActivitiesForm.keyword}"/>
																<c:set target="${urlParams1}" property="tempNumResults" value="${aimTeamActivitiesForm.tempNumResults}"/>	
																<digi:link href="/teamActivities.do" name="urlParams1" title="${clickToViewNextPage}" >
																	<%=pages%>
																</digi:link>
															<% } %>
															|&nbsp; 
															</logic:iterate>
														</td>
													</tr>
													</logic:notEmpty>	
													<tr bgcolor="#ffffff">
														<td colspan="2" align="center">		
															<html:hidden name="aimTeamActivitiesForm" property="teamId"/>												
															<input type="button" value="<digi:trn key='btn:remove'>Remove</digi:trn>" id="deleteActBtn" class="buttonx" onclick="deleteActivities()">
														</td>
													</tr>																										
													</logic:notEmpty>
													<!-- end page logic -->													
											</table>
										</td></tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7>
												<b style="font-size:12px; padding-left:5px;">
													<digi:trn key="aim:otherLinks">
														Other links
													</digi:trn>
												</b>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width="17"></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%" class="inside">
										<tr>
											<td class="inside">
												<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams}" property="id">
												<bean:write name="aimTeamActivitiesForm" property="teamId" />
												</c:set>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToAssignActivity">
												<digi:trn key="aim:clickToAssignActivity">Click here to Assign Activity</digi:trn>
												</c:set>
												
												<digi:link href="/assignActivity.do" name="urlParams" title="${clickToAssignActivity}" >
													<digi:trn key="aim:assignAnActivity">Assign an activity</digi:trn>
												</digi:link>					
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
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
</digi:form>
</td></tr>
</table>
