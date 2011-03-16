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

	function exportXSL() {
		var teams = "";
		$(".teamsOnpage").each(function(i, element) {
			teams += "~team=" + element.id;
		});
		<digi:context name="exportUrl" property="context/module/moduleinstance/exportWorkspaceManager2XSL.do"/>;
		document.aimWorkspaceForm.action = "${exportUrl}" + teams;
		document.aimWorkspaceForm.target = "_blank";
		document.aimWorkspaceForm.submit();
	}
</script>
<DIV id="TipLayer"	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
	
<digi:instance property="aimWorkspaceForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<table cellPadding=0 cellspacing="0" width="1000" border="0" align=center>
				<tr>
					<!-- Start Navigation -->
					<td height=33 bgcolor=#F2F2F2><span class=crumb>
						<c:set var="translation">
							<digi:trn>Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn>Admin Home</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn><b>Workspace Manager</b></digi:trn>	
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height="16" vAlign="center">
                    	<span class=subtitle-blue><digi:trn><b>Workspace Manager</b></digi:trn></span>
						<hr />
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="center">
						<digi:errors />
					</td>
				</tr>

			<tr>
			<td>
			<table cellspacing="0" class="report_indicator" cellpadding="0" border="0" align="center" width="1000">
				<tbody>
					<tr>
						<td valign="top">
						<div class="tab_opt_box">
						<div class="tab_opt">
						<div class="tab_opt_cont"><a href="#" target="_blank"
							onclick="exportXSL(); return false;" class="l_sm"> <img
							border="0" src="/TEMPLATE/ampTemplate/img_2/ico-excel.png"></a>&nbsp;<a
							href="#" onclick="exportXSL(); return false;" class="l_sm">Export
						to Excel</a> &nbsp;&nbsp; <a href="#"
							onclick="window.print(); return false;" class="l_sm"><img
							border="0" src="/TEMPLATE/ampTemplate/img_2/ico-print.png"></a>&nbsp;<a
							href="#" onclick="window.print(); return false;" class="l_sm">Print</a>
						</div>
						</div>
						</div>
						</td>
					</tr>
				</tbody>
			</table>
			<td>
			</tr>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			


			<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellSpacing="1" border="0">
					<tr><td noWrap width="750" vAlign="top">
						<table bgColor="#d7eafd" cellPadding="1" cellSpacing="1" width="100%" valign="top">
							<tr bgColor="#ffffff">
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn>Teams</digi:trn>
											<!-- end table title -->
										</td></tr>
										<tr><td>&nbsp;</td></tr>
										
										<digi:form action="/workspaceManager.do?page=1" method="post">
										<tr><td class="box-title" align="center">
											<!-- Table title -->
											<table width="100%" >
												<tr>
													<td>
													<digi:trn>keyword</digi:trn>:&nbsp;
										              <html:text property="keyword" style="font-family:verdana;font-size:11px;"/>
													</td>
													
													<td align="center">
														<digi:trn>Workspace Type</digi:trn>:&nbsp;
														<html:select property="workspaceType" styleClass="inp-text">
															<html:option value="all"><digi:trn>All</digi:trn></html:option>
															<html:option value="team"><digi:trn>Team</digi:trn></html:option>
															<html:option value="management"><digi:trn>Management</digi:trn></html:option>
															<html:option value="computed"><digi:trn>Computed</digi:trn></html:option>
														</html:select>
													</td>
													
													<td align="center">
														<digi:trn>Results</digi:trn>:&nbsp;
														<html:select property="numPerPage" styleClass="inp-text">
															<html:option value="-1"><digi:trn>All</digi:trn></html:option>
															<html:option value="5">5</html:option>
															<html:option value="10">10</html:option>
															<html:option value="20">20</html:option>
															<html:option value="50">50</html:option>
														</html:select>
													</td>													
													<td align="right">
													<c:set var="translation">
										                <digi:trn>Show</digi:trn>
										            </c:set>
										            <input type="submit" value="${translation}"  class="dr-menu" style="font-family:verdana;font-size:11px;" onclick="return resetPage()"/>
													</td>
												</tr>
											</table>
											<!-- end table title -->
										</td></tr>
										</digi:form>
										<tr><td>&nbsp;</td></tr>
										<tr><td>
											<table width="100%" cellspacing="1" cellpadding="4" valign="top" align="left" bgcolor="#d7eafd">
													<logic:empty name="aimWorkspaceForm" property="workspaces">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn>No teams present</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimWorkspaceForm" property="workspaces">
													<logic:iterate name="aimWorkspaceForm" property="workspaces"id="workspaces" type="org.digijava.module.aim.dbentity.AmpTeam">
													<tr class="teamsOnpage" id="${workspaces.ampTeamId}">
														<td bgcolor="#ffffff">
															<c:set var="teamWrk" value="${workspaces}" target="request" scope="request" />
															<jsp:include page="teamDetailsPopup.jsp" />
														</td>
														<td bgcolor="#ffffff" width="70" align="center">
										 					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams}" property="teamId">
															<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
															</c:set>
															[ <digi:link href="/teamMembers.do" name="urlParams" title="${translation}" >
																<digi:trn>Members</digi:trn>
															</digi:link> ]
														</td>
														<td bgcolor="#ffffff" width="70" align="center">
															<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams1}" property="id">
															<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set var="translation">
																<digi:trn>Click here to view Activities</digi:trn>
															</c:set>
															[ <digi:link href="/teamActivities.do" name="urlParams1" title="${translation}" >
																<digi:trn>Activities</digi:trn>
															</digi:link> ]
														</td>
														<td bgcolor="#ffffff" width="65" align="center">
															<jsp:useBean id="urlParams22" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams22}" property="tId">
																<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set target="${urlParams22}" property="event" value="edit" />
															<c:set target="${urlParams22}" property="dest" value="admin" />
															
															<c:set var="translation">
																<digi:trn>Click here to Edit Workspace</digi:trn>
															</c:set>
															[ <digi:link href="/getWorkspace.do" name="urlParams22" title="${translation}" >
																<digi:trn>Edit</digi:trn>
															</digi:link> ]
														</td>
														<td bgcolor="#ffffff" width="75" align="center">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="tId">
																<bean:write name="workspaces" property="ampTeamId" />
															</c:set>
															<c:set target="${urlParams4}" property="event" value="delete"/>
															<c:set var="translation">
																<digi:trn>Click here to Delete Workspace</digi:trn>
															</c:set>
															[ <digi:link href="/deleteWorkspace.do" name="urlParams4"
																title="${translation}" onclick="return onDelete()">
																<digi:trn>Delete</digi:trn>
																</digi:link> ]
														</td>
														<td bgcolor="#ffffff" align="center" nowrap>
															[<a href="JavaScript:openNpdSettingsWindow(${workspaces.ampTeamId});">
																<digi:trn>Npd Settings</digi:trn>
															</a>]
														</td>
													</tr>
													</logic:iterate>

													<!-- page logic for pagination -->
													<logic:notEmpty name="aimWorkspaceForm" property="pages">
													<tr bgcolor="#ffffff">
														<td colspan="5">
															<digi:trn>Pages :</digi:trn>
															<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
															<logic:iterate name="aimWorkspaceForm" property="pages" id="pages"
															type="java.lang.Integer">
															<c:set target="${urlParams3}" property="page"><%=pages%></c:set>
															<c:set var="translation">
																<digi:trn>Click here to view All pages</digi:trn>
															</c:set>
															<c:if test="${aimWorkspaceForm.page==pages}">
																<font color="red"><%=pages%></font> |&nbsp;
															</c:if>
															<c:if test="${aimWorkspaceForm.page!=pages}">
																<digi:link href="/workspaceManager.do" name="urlParams3"title="${translation}" ><%=pages%></digi:link> |&nbsp;
															</c:if>
															</logic:iterate>
														</td>
													</tr>
													</logic:notEmpty>
													<!-- end page logic for pagination -->

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
									<table cellpadding="0" cellspacing="0" width="10"0>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
												</c:set>
												<digi:link href="/createWorkspace.do?dest=admin" title="${translation}" >
												<digi:trn key="aim:addTeam">
												Add Teams
												</digi:trn>
												</digi:link>
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
