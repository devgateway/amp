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

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<script type="text/javascript">

<!--
	function listReports()
	{
		document.aimTeamReportsForm.addReport.value="List of Unassigned Reports";
		document.aimTeamReportsForm.action="/updateTeamReports.do";
		document.aimTeamReportsForm.submit();
	}


	function checkall() {
		var selectbox = document.aimTeamReportsForm.checkAll;
		var items = document.aimTeamReportsForm.selReports;
		if (document.aimTeamReportsForm.selReports.checked == true ||
							 document.aimTeamReportsForm.selReports.checked == false) {
				  document.aimTeamReportsForm.selReports.checked = selectbox.checked;
		} else {
			for(i=0; i<items.length; i++){
				document.aimTeamReportsForm.selReports[i].checked = selectbox.checked;
			}
		}
	}

	function validate() {
		if (document.aimTeamReportsForm.selReports.checked != null) {
			if (document.aimTeamReportsForm.selReports.checked == false) {
				alert("Please choose a report to remove");
				return false;
			}
		} else {
			var length = document.aimTeamReportsForm.selReports.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimTeamReportsForm.selReports[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("Please choose a report to remove");
				return false;
			}
		}
		return true;
	}
        function confirmDelete() {
		var valid = validate();
		if (valid == true) {
                    <c:set var="message">
                  <digi:trn key="aim:teamWorkspaceSetup:removeSelectedReports">
                  Are you sure you want to remove selected reports?
                  </digi:trn>
                  </c:set>
			var flag = confirm("${message}");
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

<digi:instance property="aimTeamReportsForm" />

<digi:form action="/updateTeamReports.do" method="post">
<html:hidden property="addReport"/>
<html:hidden property="showReportList"/>
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
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:if test="${aimTeamReportsForm.showReportList == true}">
							<digi:trn key="aim:reportList">Report List</digi:trn>
						</c:if>
						<c:if test="${aimTeamReportsForm.showReportList == false}">
							<digi:trn key="aim:tabsList">Tab List</digi:trn>
						</c:if>						
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
									<c:if test="${aimTeamReportsForm.showReportList == true}">
										<c:set var="selectedTab" value="3" scope="request"/>
									</c:if>
									<c:if test="${aimTeamReportsForm.showReportList == false}">
										<c:set var="selectedTab" value="8" scope="request"/>
									</c:if>									
									<c:set var="selectedSubTab" value="0" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
                                	<div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
									<div align="center">
                                
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">
										<tr><td>
										<digi:errors />
										</td></tr>
										<tr>
											<td valign="top">
												<table width="100%">
													<tr>
														<td colspan="2">
															<font color="blue">
															<digi:trn key="aim:reportTeamSpecific">*T</digi:trn>
															<c:if test="${aimTeamReportsForm.showReportList == true}">
																<digi:trn key="aim:teamReportListViewableByAllWorkspaceMembers">
																	Indicates the report is viewable by all workspace members.
																</digi:trn>
															</c:if>
															<c:if test="${aimTeamReportsForm.showReportList == false}">
																<digi:trn key="aim:teamTabListViewableByAllWorkspaceMembers">
																	Indicates the tab is viewable by all workspace members.
																</digi:trn>
															</c:if>
															</font>
														</td>
													</tr>
													<tr>
													<td colspan="2">
															<font color="blue">
															<digi:trn key="aim:reportMemberSpecific">*M</digi:trn>
															<c:if test="${aimTeamReportsForm.showReportList == true}">
																<digi:trn key="aim:teamReportListViewableBySelected">
																Indicates the report is viewable by selected team members,
																whom the team leader assigns.
																</digi:trn>
															</c:if>
															<c:if test="${aimTeamReportsForm.showReportList == false}">
																<digi:trn key="aim:teamTabListViewableBySelected">
																Indicates the tab is viewable by selected team members,
																whom the team leader assigns.
																</digi:trn>
															</c:if>	
															</font>
														</td>
													</tr>
													<tr>
														<td>
															<img src= "/repository/contentrepository/view/images/make_private.gif" border="0" align="absmiddle" />
                    										<digi:trn key="aim:clicktomakethisprivate">Click here to make this private</digi:trn>															
                    									</td>
														<td>
															<img src= "/repository/contentrepository/view/images/make_public.gif" vspace="2" border="0" align="absmiddle" />
                    										<digi:trn key="aim:clicktomakethispublic">Click here to make this public</digi:trn>
                       									</td>
													</tr>
												</table>
                                                <br />
											</td>
										</tr>

										<tr>
											<td bgColor=#ffffff valign="top">
												<table border=0 cellPadding=0 cellSpacing=0 width="100%">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=0 cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#999999" width="5%">
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																</td>
																<td valign="center" align="center" bgcolor="#999999" style="color:black" width="75%">
																	<b>
																		<c:if test="${aimTeamReportsForm.showReportList == true}">
																			<digi:trn key="aim:reportListWorkspace">List of Reports in the Workspace</digi:trn>
																		</c:if>
																		<c:if test="${aimTeamReportsForm.showReportList == false}">
																			<digi:trn key="aim:tabListWorkspace">List of Tabs in the Workspace</digi:trn>
																		</c:if>
																	</b>
																</td>
																<td valign="center" align="center" bgcolor="#999999" style="color:black" width="20%">
																	<b><digi:trn key="aim:action">Action</digi:trn></b>
																</td>
															</table>
														</td>
													</tr>
													<logic:empty name="aimTeamReportsForm" property="reports">
													<tr bgColor=#f4f4f2>
														<td align="center">
															<c:if test="${aimTeamReportsForm.showReportList == true}">
																<digi:trn key="aim:noReportsPresent">No reports present</digi:trn>
															</c:if>
															<c:if test="${aimTeamReportsForm.showReportList == false}">
																<digi:trn key="aim:noTabsPresent">No tabs present</digi:trn>
															</c:if>															
														</td>
													</tr>
													<tr><td>&nbsp;</td></tr>
													</logic:empty>
													<logic:notEmpty name="aimTeamReportsForm" property="reports">
													<tr>
														<td>
															<table width="100%" cellSpacing=0 cellPadding=2 vAlign="top" align="left"
															bgcolor="#dddddd" id="dataTable">

															<logic:iterate name="aimTeamReportsForm" property="reports" id="reports"
															type="org.digijava.module.aim.helper.ReportsCollection">
																<bean:define id="ampReports" name="reports" property="report"
																type="org.digijava.module.aim.dbentity.AmpReports" />
																<tr bgColor=#f4f4f2>
																	<td width="3%">
																		<html:multibox property="selReports" >
																		<bean:write name="ampReports" property="ampReportId" />
																		</html:multibox>
																	</td>
																	<td width="73%">
																		<bean:write name="ampReports" property="name" />
																	</td>
																	<jsp:useBean id="urlParams" type="java.util.Map"
																	class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="id">
																		<bean:write name="ampReports" property="ampReportId" />
																	</c:set>
																	<logic:equal name="reports" property="teamView" value="false">
																		<td width="3%">
																			<font color="blue">
																			<digi:trn key="aim:reportMemberSpecific">*M</digi:trn>
																			</font>
																		</td>
																		<c:set target="${urlParams}" property="status" value="team" />
																		<td width="24%" align="center">
																			<c:set var="translation">
																				<digi:trn key="aim:clickToMakeThisPublic">Click here to make this public</digi:trn>
																			</c:set>
																			
																			<c:if test="${aimTeamReportsForm.showReportList == true}">
																				<c:set target="${urlParams}" property="returnPage">teamReportList</c:set>
																			</c:if>	
																			<c:if test="${aimTeamReportsForm.showReportList == false}">
																				<c:set target="${urlParams}" property="returnPage">teamDesktopTabList</c:set>
																			</c:if>	
																			
																			<digi:link href="/changeTeamReportStatus.do" name="urlParams" title="${translation}" >
<img hspace="2" title="<digi:trn key="aim:teamReportListMakePublic">Make this public</digi:trn>" src= "/repository/contentrepository/view/images/make_public.gif" border=0>
																			</digi:link>
																		</td>
																	</logic:equal>
																	<logic:equal name="reports" property="teamView" value="true">
																		<td width="3%">
																			<font color="blue">
																			<digi:trn key="aim:reportTeamSpecific">*T</digi:trn>
																			</font>
																		</td>
																		<c:set target="${urlParams}" property="status" value="member" />
																		<td width="24%" align="center">
																			<c:set var="translation">
																				<digi:trn key="aim:clickToMakeThisPrivate">Click here to make this private</digi:trn>
																			</c:set>
																			<digi:link href="/changeTeamReportStatus.do" name="urlParams" title="${translation}" >
																					<img hspace="2" title="<digi:trn key="aim:teamReportListMakePrivate">Make this private</digi:trn>" src= "/repository/contentrepository/view/images/make_private.gif" border=0>
																			</digi:link>
																		</td>
																	</logic:equal>
																	</td>
																</tr>
															</logic:iterate>
															</table>
                                                                                                                        	</td>
													</tr>
                                                                                                        <tr>
                                                                                                        <td>
                                                                                                <table border=0 style="float:left;">
                                                                                                <tr>
                                                                                                  <c:forEach var="page" begin="1" end="${aimTeamReportsForm.totalPages}">
                                                                                                    <td>
                                                                                                    <c:if test="${aimTeamReportsForm.currentPage==page}">

                                                                                                         <c:out value="${page}"/>

                                                                                                    </c:if>
                                                                                                      <c:if test="${aimTeamReportsForm.currentPage!=page}">
                                                                                                     <digi:link href="/teamReportList.do?currentPage=${page}" >
                                                                                                         <c:out value="${page}"/>
                                                                                                          </digi:link>
                                                                                                          </c:if>

                                                                                                       </td>
                                                                                                        </c:forEach>
                                                                                                        </tr>
                                                                                                </table>
                                                                                                <a style="float:right;cursor:pointer;" onclick="window.scrollTo(0,0); return false"><digi:trn key="aim:backtotop">Back to Top</digi:trn> <span style="font-size: 10pt; font-family: Tahoma;">&uarr;</span></a>
                                                                                            </td>
                                                                                            </tr>

													<tr>
														<td align="left">
															<table cellspacing="5" width="100%">
																<tr>
																	<td align="center">	
																		<html:submit  styleClass="dr-menu" property="removeReports"  onclick="return confirmDelete() ">
																			<c:if test="${aimTeamReportsForm.showReportList == true}">
																				<digi:trn key="btn:removeSelectedReports">Remove selected reports</digi:trn>
																			</c:if>
																			<c:if test="${aimTeamReportsForm.showReportList == false}">
																				<digi:trn key="btn:removeSelectedTabs">Remove selected tabs</digi:trn>
																			</c:if>																			
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

