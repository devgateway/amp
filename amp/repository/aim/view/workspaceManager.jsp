<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm('<digi:trn key="admin:workSpaceManager.deleteQuestion">Delete this workspace?</digi:trn>');
		return flag;
	}
	function openNpdSettingsWindow(ampTeamId){
		var url=addActionToURL('npdSettingsAction.do');
        url+='?actionType=viewCurrentSettings&ampTeamId='+ampTeamId;
        openURLinResizableWindow(url,600,400);
	}
	function addActionToURL(actionName){
		var fullURL=document.URL;
		var lastSlash=fullURL.lastIndexOf("/");
		var partialURL=fullURL.substring(0,lastSlash);
		return partialURL+"/"+actionName;
	}

	function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
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

<style type="text/css">
		.jcol{												
		padding-left:10px;												 
		}
		.jlien{
			text-decoration:none;
		}
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




<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
	
<digi:instance property="aimWorkspaceForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td   width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
                                          <span class=subtitle-blue>
                                          <digi:trn key="aim:workspaceManager">
                                          Workspace Manager
                                          </digi:trn></span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width="750" vAlign="top">
						<table bgColor=#ffffff cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">

									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>
										

										<tr><td>&nbsp;</td></tr>
										
										<digi:form action="/workspaceManager.do" method="post" >
										<tr><td class="box-title" >
											<!-- Table title -->
											<div style="width:752px;">
											<table >
												
												<tr>
													
													
												<td >
												<div style = "heigth:30px; width:610px;"> 	
													<div style="float:left; width:300px; ">
														<digi:trn key="aim:workspaceType">Workspace Type</digi:trn>:&nbsp;
														<html:select property="workspaceType" styleClass="inp-text" style="width:100px;" onchange="submit()">
															<html:option value="all"><digi:trn key="aim:all">All</digi:trn></html:option>
															<html:option value="team"><digi:trn key="aim:team">Team</digi:trn></html:option>
															<html:option value="management"><digi:trn key="aim:management">Management</digi:trn></html:option>
															<html:option value="computed"><digi:trn key="aim:computed">Computed</digi:trn></html:option>
														</html:select>
													 </div>
                                                  <div style="float:left; width:300px; ">
													  <digi:trn key="aim:keyword">
										                 keyword
										              </digi:trn>:&nbsp;
										              <html:text property="keyword" style="font-family:verdana;font-size:11px; "/>
													 <c:set var="translation">
										                <digi:trn key="aim:showButton">
										                Show
										                </digi:trn>
										             </c:set>
										             <input type="submit" value="${translation}"  class="dr-menu" style="font-family:verdana;font-size:11px;" />
												  </div>
												</div>	
												</td>
													
													
												</tr>
											</table>
											</div>
											
											<!-- end table title -->
										</td></tr>
										</digi:form>
										<tr><td>&nbsp;</td></tr>
										<tr><td>
										<div style="border:1px solid #999999;" >
										<div style= "background-color:#999999; color:#000; font-weight:bold; padding-top:5px; height:15px; width:752px;">
										<center>	TEAMS </center></div>
										<div style="overflow:auto;width:752px;height:250px;max-height:220px; " >
											<table width="100%" id="dataTable" cellspacing=0 cellpadding=4 valign=top  align=left >
												
													<logic:empty name="aimWorkspaceForm" property="workspaces">
													<tr >
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noTeams">
															No teams present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimWorkspaceForm" property="workspaces">
													<logic:iterate name="aimWorkspaceForm" property="workspaces"
													id="workspaces" type="org.digijava.module.aim.dbentity.AmpTeam">
													<tr>
														<td >
															<c:set var="teamWrk" value="${workspaces}" target="request" scope="request" />
															<jsp:include page="teamDetailsPopup.jsp" />
														</td>
														<td  width="70" align="center">
										 					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams}" property="teamId">
															<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
															</c:set>
															 <digi:link href="/teamMembers.do" name="urlParams" title="${translation}" >
																<digi:trn key="aim:workspaceManagerMembersLink">
																	Members
																</digi:trn>
															</digi:link> 
														</td>
														<td  width="70" align="center">
															<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams1}" property="id">
															<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewActivities">Click here to view Activities</digi:trn>
															</c:set>
															 <digi:link href="/teamActivities.do" name="urlParams1" title="${translation}" >
																<digi:trn key="aim:workspaceManagerActivitiesLink">
																	Activities
																</digi:trn>
															</digi:link> 
														</td>
														<td  width="65" align="center">
															<jsp:useBean id="urlParams22" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams22}" property="tId">
																<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set target="${urlParams22}" property="event" value="edit" />
															<c:set target="${urlParams22}" property="dest" value="admin" />
															
															<c:set var="translation">
																<digi:trn key="aim:clickToEditWorkspace">Click here to Edit Workspace</digi:trn>
															</c:set>
															 <digi:link href="/getWorkspace.do" name="urlParams22" title="${translation}" >
																<digi:trn key="aim:workspaceManagerEditLink">
																
																<img vspace="2" border="0" align="absmiddle" src="/repository/message/view/images/edit.gif"/>
																	Edit
																</digi:trn>
															</digi:link> 
														</td>
														<td  width="75" align="center">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="tId">
																<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set target="${urlParams4}" property="event" value="delete"/>
															<c:set var="translation">
																<digi:trn key="aim:clickToDeleteWorkspace">Click here to Delete Workspace</digi:trn>
															</c:set>
															 <digi:link href="/deleteWorkspace.do" name="urlParams4"
																title="${translation}" onclick="return onDelete()">
																<digi:trn key="aim:workspaceManagerDeleteLink">
															
																	<img vspace="2" border="0" align="absmiddle" src="/repository/message/view/images/trash_12.gif"/>
																	Delete
																</digi:trn>
																</digi:link> 
														</td>
														<td  align="center" nowrap>
															<a href="JavaScript:openNpdSettingsWindow(${workspaces.ampTeamId});">
																<digi:trn key="aim:npdSettings:EditNpdSettings">Npd Settings</digi:trn>
															</a>
														</td>
													</tr>
													</logic:iterate>

													<tr><td>&nbsp;</td></tr>

													</logic:notEmpty>
													<!-- end page logic -->
											</table>
											</div>
											</div>
										</td>
									
									</tr>
									<tr><td>
										<digi:form action="/workspaceManager.do" method="post" >
										<div style=" width:752px;  ">
											<div style= " float:left; width:600px;" >
											<!-- page logic for pagination -->
													
													
														
															<digi:trn key="aim:workspaceManagerPages">
																<!-- Pages : -->
															</digi:trn>
															
															<div style=" width:300px; ">&nbsp;
															<logic:notEmpty name="aimWorkspaceForm" property="pages">
															<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
															<logic:iterate name="aimWorkspaceForm" property="pages" id="pages"
															type="java.lang.Integer">
															

															<div style="float:left; width:10px;  padding:3px;border:1px solid #999999; ">
															<c:set target="${urlParams3}" property="page"><%=pages%></c:set>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewAllPages">Click here to view All pages</digi:trn>
															</c:set>
															<digi:link href="/workspaceManager.do" name="urlParams3"
															title="${translation}" ><%=pages%></digi:link>
															</div>
															<div style="float:left;">&nbsp;</div>		
															
															</logic:iterate>
																</logic:notEmpty>
															</div>
														
													
													
													<!-- end page logic for pagination -->
												 </div>
											<div style= " float:left; width:100px;" >
											<digi:trn key="aim:results">Results</digi:trn>:&nbsp;
													<html:select property="numPerPage" styleClass="inp-text" onchange="submit()" >
														<html:option value="-1"><digi:trn key="aim:all">All</digi:trn></html:option>
														<html:option value="5">5</html:option>
														<html:option value="10">10</html:option>
														<html:option value="20">20</html:option>
														<html:option value="50">50</html:option>
												</html:select>
											</div>
										</div>
										</digi:form>
										</td>
									</tr>	
									</table>

								</td>
							</tr>
						</table>
					</td>

					<td noWrap width="100%" vAlign="top" style="padding-top:52px; _padding-top:63px;">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#999999 class="box-title" style = "color:#000;"> 
												<digi:trn key="aim:otherLinks">
												Links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r2.gif" height="17" width=17>
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
												<div style="width:100px; ">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
												</c:set>
												<digi:link href="/createWorkspace.do?dest=admin" title="${translation}" >
												<digi:trn key="aim:addTeam">
												Add Teams
												</digi:trn>
												</digi:link>
												</div>
											</td>
										</tr>
																				
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
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
					<script language="javascript">
												setStripsTable("dataTable", "tableEven", "tableOdd");
												setHoveredTable("dataTable", false);
										</script>




