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
<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>
<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if(tableElement)
	{
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>

<script type="text/javascript">
	function checkall() {
		var selectbox = document.aimTeamActivitiesForm.checkAll;
		var items = document.aimTeamActivitiesForm.selActivities;
		if (items != null) {
			if (document.aimTeamActivitiesForm.selActivities.checked == true || 
								 document.aimTeamActivitiesForm.selActivities.checked == false) {
					  document.aimTeamActivitiesForm.selActivities.checked = selectbox.checked;
			} else {
				for(i=0; i<items.length; i++){
					document.aimTeamActivitiesForm.selActivities[i].checked = selectbox.checked;
				}
			}				  
		}
	}

function checkSelActivities() {
	if (document.aimTeamActivitiesForm.selActivities.checked != null) { 
		if (document.aimTeamActivitiesForm.selActivities.checked == false) {
			alert("Please choose an activity to add");
			return false;
		}
	} else { // 
		var length = document.aimTeamActivitiesForm.selActivities.length;	  
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimTeamActivitiesForm.selActivities[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			alert("Please choose an activity to add");
			return false;					  
		}
	}
	return true;
}	


	function sortMe(val) {
		<digi:context name="sel" property="context/module/moduleinstance/updateTeamActivity.do" />
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

</script>


<digi:instance property="aimTeamActivitiesForm" />
<digi:form action="/updateTeamActivity.do" method="post">

<html:hidden property="teamId" />
<html:hidden property="removeActivity" value="assign" />
<html:hidden property="sort" />
<html:hidden property="sortOrder" />
<html:hidden property="page" />

<table width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToConfigureTeam">Click here to Configure Team</digi:trn>
						</c:set>
						<digi:link href="/configureTeam.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:configureTeam">Configure Team</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;						
						<c:set var="translation">
							<digi:trn key="aim:clickToViewActivityList">Click here to view Activity List</digi:trn>
						</c:set>
						<digi:link href="/teamActivityList.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:activityList">Activity List</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addActivity">
						Add Activity
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="2" scope="request"/>
									<c:set var="selectedSubTab" value="2" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp" flush="true" />								
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
                                <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
                                <div align="center">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">	
										<tr>
											<td bgColor=#ffffff valign="top">
												<table border=0 cellPadding=0 cellSpacing=0 width="100%">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=0 cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#999999">
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																</td>
																<td width="20%" bgcolor="#999999" style="color:black">
																	<b><digi:trn key="aim:ampId">AMP ID</digi:trn></b>
																</td>
																<td valign="center" align="center" bgcolor="#999999">
																	<a  style="color:black" href="javascript:sortMe('activity')" title="Click here to sort by Activity Details">
																		<b><digi:trn key="aim:unassignedActivityList">
																		List of unassigned activities
																	</digi:trn></b>
																	</a>																
																</td>
																<td bgColor="#999999" align="center" width="20%">
																	<a  style="color:black" href="javascript:sortMe('donor')" title="Click here to sort by Donors">
																		<b><digi:trn key="aim:donors">
																			Donors
																		</digi:trn></b>
																	</a>																
																</td></tr>
															</table>
														</td>
													</tr>
													<logic:empty name="aimTeamActivitiesForm" property="activities">
													<tr>
														<td align="center">
															<digi:trn key="aim:noActivitiesPresent">
															No activities present
															</digi:trn>
														</td>
													</tr>	
													</logic:empty>

													<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=0 cellPadding=2 vAlign="top" align="left" id="dataTable">													
																<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities" 
																>
																<tr><td width=3>
																	<html:multibox property="selActivities" >
																		<bean:write name="activities" property="ampActivityId" />
																	</html:multibox>
																</td>
																<td width="20%">
																	<bean:write name="activities" property="ampId" />
																</td>
																<td>
																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="activityId">
																		<bean:write name="activities" property="ampActivityId" />
																	</c:set>
																	<c:set target="${urlParams}" property="pageId" value="4"/>
																	<c:set var="translation">
																		<digi:trn key="aim:clickToViewActivityDetails">
																		Click here to view Activity Details</digi:trn>
																	</c:set>
																	<digi:link href="/viewActivityPreview.do" name="urlParams" 
																	title="${translation}">
																		<bean:write name="activities" property="name" />
																	</digi:link>
																</td>
																<td align="left" width="20%">
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
																	<digi:trn key="aim:newOwner">New owner for the assigned activities:</digi:trn> 
																	<html:select property="memberId">
																	<html:optionsCollection property="members" value="memberId" label="memberName" /> 
																	</html:select>
																		<html:submit  styleClass="dr-menu" property="submitButton"  onclick="return checkSelActivities()">
																			<digi:trn key="btn:addActivityToWorkspace">Add Activity To Workspace</digi:trn> 
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
										<logic:notEmpty name="aimTeamActivitiesForm" property="pages">
											<tr>
												<td>
													<digi:trn key="aim:pages">
														Pages :
													</digi:trn>
													<logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" 
													type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
																			    
														<bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />
														
														<% if (currPage.equals(pages)) { %>
																<%=pages%>
														<%	} else { %>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</c:set>
															<digi:link href="/updateTeamActivity.do" name="urlParams1" title="${translation}" >
																<%=pages%>
															</digi:link>
														<% } %>
														|&nbsp; 
													</logic:iterate>
												</td>
											</tr>
										</logic:notEmpty>	
									</table>
                                </div>
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

</digi:form>

<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>


