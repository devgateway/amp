<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<jsp:include page="/repository/aim/view/scripts/auditFilter.jsp"  />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/compareAcivity.js"/>"/>
<script language="javascript">
function showUser(email){
	if (email != ""){
		<digi:context name="information" property="context/module/moduleinstance/userProfile.do" />
        var param = "~edit=true~email="+email;
		previewWorkspaceframe("${information}",param);
	}
	else{
		var trasnlation = '<digi:trn key="aim:userblankmail" jsFriendly="true">The user does not have a valid email address</digi:trn>'
		alert (trasnlation);
	}
}

function resetSearch() {
	document.getElementById("userId").selectedIndex = 0;
	document.getElementById("selectedDateFromText").value="";
	document.getElementById("selectedDateToText").value="";
	document.aimTeamAuditListForm.action = "/aim/teamAuditList.do?action=reset";
	document.aimTeamAuditListForm.submit();
}
function submitFilter() {
	document.aimTeamAuditListForm.submit();
}

</script>
<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>
<digi:instance property="aimTeamAuditListForm" />
<digi:form action="/teamAuditList.do" method="post">
<table width="100%" cellpadding="0" cellspacing="0" vAlign="top" align="left">
	<tr>
		<td width="100%" vAlign="top" align="left">
			<jsp:include page="teamPagesHeader.jsp"  />
		</td>
	</tr>
	<tr>
	<td>
	
									<c:set var="selectedTab" value="7" scope="request"/>
									<c:set var="selectedSubTab" value="0" scope="request"/>
										
									<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
										<tr>
											<td>
												<div class="breadcrump_cont">
													<span class="sec_name">
														<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
													</span>
													
													<span class="breadcrump_sep">|</span>
													<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
														<digi:trn key="aim:portfolio">Portfolio</digi:trn>
													</digi:link>
													<span class="breadcrump_sep"><b>»</b></span>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
													</c:set>
													<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
													<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
													<span class="breadcrump_sep"><b>»</b></span>
													<span class="bread_sel"><digi:trn key="aim:AuditTrialList">Audit Trial List</digi:trn></span>
												</div>						
											</td>
										</tr>
										<tr>
											<td valign="top">
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">	
										
									<jsp:include page="teamSetupMenu.jsp"  />

													<input type="button" value="Compare All"
														   onclick="javascript:compareAll()" class="dr-menu"
														   style="cursor: pointer; font-style: italic; float: right; margin: 0.5% 1.5% 0.5%;">

													<span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleFilterSettings();" id="displayFilterButton">
					<c:set var="hiddenStyle" value="display:none;"/>
					<c:set var="settingsTitle">
						<digi:trn key="aim:Showfilteroptions">Show Filter options</digi:trn>
					</c:set>
					  <c:if test="${(not empty aimTeamAuditListForm.selectedUser and aimTeamAuditListForm.selectedUser !=-1 )
					   or (not empty aimTeamAuditListForm.selectedTeam )
					  or (not empty aimTeamAuditListForm.selectedDateFrom ) or (not empty aimTeamAuditListForm.selectedDateTo )}">
						  <c:set var="hiddenStyle" value="display:inline-flex;"/>
						  <c:set var="settingsTitle">
							  <digi:trn key="aim:Showfilteroptions">Hide Filter options</digi:trn>
						  </c:set>
					  </c:if>

				  <c:out value="${settingsTitle}"/> </span>
													&nbsp;<br>
													<div style="<c:out value ="${hiddenStyle}"/>background-color:#ffffff;padding:2px; width: 100%" id="currentFilterSettings" >
														<div class="divTable">
															<div class="divTableBody">
																<div class="divTableRow">
																	<div class="divTableCell divTableCellLeft" ><digi:trn>User:</digi:trn></div>
																	<div class="divTableCell"><html:select property="selectedUser" styleClass="inp-text" styleId="userId">
																		<html:option value="-1"><digi:trn>Select User</digi:trn> </html:option>
																		<html:optionsCollection property="userList" value="id" label="name"></html:optionsCollection>
																	</html:select></div>
																</div>
																<div class="divTableRow">
																	<div class="divTableCell divTableCellLeft"><digi:trn>Team:</digi:trn></div>
																	<div class="divTableCell"><html:text property="teamName" styleClass="inp-text" styleId="teamId" readonly="true">
																	</html:text></div>
																</div>
																<c:set var="dateTr">
																	<digi:trn>Date</digi:trn>
																</c:set>
																<div class="divTableRow" >
																	<div class="divTableCell divTableCellLeft" ><c:out value="${dateTr}"/> <digi:trn>From</digi:trn>:</div>
																	<div id="dateFrom">
																		<div class="divTableCell" id="selectedDateFrom"><html:text property="selectedDateFrom" styleClass="inp-text" readonly="true" styleId="selectedDateFromText"/>
																			<a id="date2" href='javascript:pickDateById2("dateFrom","selectedDateFromText",true,"tl")'>
																				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"/>
																			</a>
																			<a id="clear2" href='javascript:clearDate("selectedDateFromText")'>
																				<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this date"/>
																			</a>
																		</div>
																	</div>
																</div>
																<div class="divTableRow">
																	<div class="divTableCell divTableCellLeft" ><c:out value="${dateTr}"/> <digi:trn>To</digi:trn>:</div>
																	<div id="dateTo">
																		<div class="divTableCell" id="selectedDateTo">                                 	<html:text property="selectedDateTo" styleClass="inp-text" readonly="true" styleId="selectedDateToText"/>
																			<a id="date2" href='javascript:pickDateById2("dateTo","selectedDateToText",true,"tl")'>
																				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"/>
																			</a>
																			<a id="clear2" href='javascript:clearDate("selectedDateToText")'>
																				<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this date"/>
																			</a>
																		</div>
																	</div>
																</div>
																<div class="divTableRow">
																	<div class="divTableCell divTableCellLeft"><input  class="dr-menu" type="button" onclick="submitFilter()" value="<digi:trn>Apply</digi:trn>"></div>
																	<div class="divTableCell"><input class="dr-menu" type="button" value="<digi:trn>Reset</digi:trn>" onclick="document.aimTeamAuditListForm.reset();resetSearch()"></div>
																</div>
															</div>
														</div>
													</div>
												</div>
												</div>
												</div>
												</div>
												<br>


												<table class="inside" width="970" cellpadding="0" cellspacing="0">
								<tr>
							    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
							    	<b class="ins_title">
							    		<c:if test="${aimTeamAuditListForm.sortBy!='nameasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=nameasc" styleClass="l_sm">
													<digi:trn key="aim:name">Name</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='namedesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='nameasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=namedesc" styleClass="l_sm">
													<digi:trn key="aim:name">Name</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
												</digi:link>
							  			</c:if>
							  		</b>
							  	</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='typeasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=typeasc" styleClass="l_sm">
													<digi:trn key="aim:objectType">Object Type</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='typedesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='typeasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=typedesc" styleClass="l_sm">
													<digi:trn key="aim:objectType">Object Type</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
												</digi:link>
											</c:if>
										</b>
							    </td>
							    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
							    	<b class="ins_title">
								    	<c:if test="${aimTeamAuditListForm.sortBy!='teamasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=teamasc" styleClass="l_sm">
													<digi:trn key="aim:teamName">Team Name</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='teamdesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='teamasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=teamdesc" styleClass="l_sm">
													<digi:trn key="aim:teamName">Team Name</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='authorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=authorasc" styleClass="l_sm">
													<digi:trn key="aim:authorName">Author Name</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='authordesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='authorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=authordesc" styleClass="l_sm">
													<digi:trn key="aim:authorName">Author Name</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='creationdateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=creationdateasc" styleClass="l_sm">
													<digi:trn key="aim:creationDateLogger">Creation Date</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='creationdatedesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='creationdateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=creationdatedesc" styleClass="l_sm">
													<digi:trn key="aim:creationDateLogger">Creation Date</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
                                                </digi:link>
								  		</c:if>
							  		</b>
							  	</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='editorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=editorasc" styleClass="l_sm">
													<digi:trn key="aim:editorName">Editor Name</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='editordesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='editorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=editordesc" styleClass="l_sm">
													<digi:trn key="aim:editorName">Editor Name</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='changedateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=changedateasc" styleClass="l_sm">
													<digi:trn key="aim:changeDate">Change Date</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='changedatedesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='changedateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=changedatedesc" styleClass="l_sm">
													<digi:trn key="aim:changeDate">Change Date</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='actionasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=actionasc" styleClass="l_sm">
													<digi:trn key="aim:action">Action</digi:trn>
                                                    <c:if test="${aimTeamAuditListForm.sortBy=='actiondesc'}">
                                                        <img id="activityColumnImg" src="/repository/aim/images/up.gif" />
                                                    </c:if>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='actionasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=actiondesc" styleClass="l_sm">
													<digi:trn key="aim:action">Action</digi:trn>
                                                    <img id="activityColumnImg" src="/repository/aim/images/down.gif" />
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif"
										class="inside"><b class="ins_title"> <digi:trn
											key="aim:action"> View differences</digi:trn>
									</b></td>
								</tr>

													<logic:iterate name="aimTeamAuditListForm" property="logs" id="log" type="org.digijava.module.aim.dbentity.AmpAuditLogger">
									<tr>
										<td width="280" class="inside" title="${log.objectName}">
											<c:choose>
												<c:when test="${fn:length(log.objectName) > 30}" >
													<c:out value="${fn:substring(log.objectName, 0, 30)}" />...
												</c:when>
												<c:otherwise>
													<bean:write name="log" property="objectName"/>
												</c:otherwise>
											</c:choose>
										</td>
										<td class="inside" width="150" title="${log.objectTypeTrimmed}">
											<bean:write name="log" property="objectTypeTrimmed"/>									
										</td>
										<td class="inside" width="100" title="${log.teamName}">
											<c:choose>
												<c:when test="${fn:length(log.objectName) > 10}" >
													<c:out value="${fn:substring(log.teamName, 0, 10)}" />...
												</c:when>
												<c:otherwise>
													<bean:write name="log" property="teamName"/>
												</c:otherwise>
											</c:choose>									
										</td>
										<td class="inside" width="150">
											<a href="javascript:showUser('<bean:write name="log" property="authorEmail"/>')" style="text-decoration: none" title="${log.authorName}" class="l_sm">
												<c:choose>
													<c:when test="${fn:length(log.objectName) > 8}" >
														<c:out value="${fn:substring(log.authorName, 0, 8)}" />...
													</c:when>
													<c:otherwise>
														<bean:write name="log" property="authorName"/>
													</c:otherwise>
												</c:choose>
											</a>								  
										</td>
										<td width="100" class="inside" title="${log.loggedDate}">
											<bean:write name="log" property="sloggeddate"/>
										</td>
										
										<td width="100" class="inside" title="${log.editorName}">
											<bean:write name="log" property="editorName" />								  
										</td>
										<td width="150" class="inside" title="${log.modifyDate}">
											<bean:write name="log" property="smodifydate"/>								  
										</td>
										<td width="100" class="inside">
											<logic:equal value="delete" property="action" name="log">
												<digi:trn key="admin:delete">Delete</digi:trn>
											</logic:equal>
											<logic:equal value="add" property="action" name="log">
												<digi:trn key="admin:add">Add</digi:trn>
											</logic:equal>
											<logic:equal value="update" property="action" name="log">
												<digi:trn key="admin:update">Update</digi:trn>
											</logic:equal>								  
										</td>
										<td width="50" class="inside">
											<div style="text-align:center">
												<input type="button" value="<digi:trn>Compare</digi:trn>" onclick="javascript:viewDifferences(${log.objectId})" class="dr-menu">
											</div>
										</td>
									</tr>
								</logic:iterate>
							</table>
							
							<!-- Pagination -->
							<div class="paging" style="font-size:11px;">
							<c:if test="${aimTeamAuditListForm.currentPage > 1}">
								<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParamsFirst}" property="page" value="1"/>
								<c:set target="${urlParamsFirst}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
								<c:set var="translation">
									<digi:trn key="aim:firstpage">First Page</digi:trn>
								</c:set>
								<digi:link href="/teamAuditList.do" name="urlParamsFirst" title="${translation}" styleClass="l_sm">
									&lt;&lt;
								</digi:link>
								<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParamsPrevious}" property="page" value="${aimTeamAuditListForm.currentPage -1}"/>
								<c:set target="${urlParamsPrevious}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
								<c:set var="translation">
									<digi:trn key="aim:previouspage">Previous Page</digi:trn>
								</c:set>|
								<digi:link href="/teamAuditList.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" styleClass="l_sm">
									<digi:trn key="aim:previous">Previous</digi:trn>
								</digi:link>|
							</c:if>
							
							
							<c:set var="length" value="${aimTeamAuditListForm.pagesToShow}"></c:set>
							<c:set var="start" value="${aimTeamAuditListForm.offset}"/>
							<logic:iterate name="aimTeamAuditListForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
								<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams1}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
								<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
								<c:if test="${aimTeamAuditListForm.currentPage == pages && aimTeamAuditListForm.pagesSize > 1}">
									<b class="paging_sel"><%=pages%></b>
									|						
								</c:if>
								<c:if test="${aimTeamAuditListForm.currentPage != pages && aimTeamAuditListForm.pagesSize > 1}">
									<c:set var="translation">
										<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
									</c:set>
									<digi:link href="/teamAuditList.do" name="urlParams1" title="${translation}" styleClass="l_sm">
									<%=pages%>							
									</digi:link>
									|
								</c:if>
							</logic:iterate>
							<c:if test="${aimTeamAuditListForm.currentPage != aimTeamAuditListForm.pagesSize && aimTeamAuditListForm.pagesSize != 0}">
								<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap" />
								<c:set target="${urlParamsNext}" property="page" value="${aimTeamAuditListForm.currentPage+1}"/>
								<c:set target="${urlParamsNext}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
								<c:set var="translation"> 
								<digi:trn key="aim:nextpage">Next Page</digi:trn>
								</c:set>
								<digi:link  href="/teamAuditList.do" style="text-decoration=none" name="urlParamsNext" title="${translation}" styleClass="l_sm">
									<digi:trn key="aim:next">Next</digi:trn>
								</digi:link>
								<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap" />|
								
								<c:if test="${aimTeamAuditListForm.pagesSize > aimTeamAuditListForm.pagesToShow}">
									<c:set target="${urlParamsLast}" property="page" value="${aimTeamAuditListForm.pagesSize}" />
									<c:set target="${urlParamsLast}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
								</c:if>
								<c:if test="${aimTeamAuditListForm.pagesSize < aimTeamAuditListForm.pagesToShow}">
									<c:set target="${urlParamsLast}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
									<c:set target="${urlParamsLast}" property="page" value="${aimTeamAuditListForm.pagesSize}" />
									<c:set var="translation">
										<digi:trn>Last Page</digi:trn>
									</c:set>
									<digi:link href="/teamAuditList.do" style="text-decoration=none" name="urlParamsLast" title="${translation}" styleClass="l_sm">
										&gt;&gt;
									</digi:link>
								</c:if> 
							</c:if>
							<c:out value="${aimTeamAuditListForm.currentPage}"/>&nbsp; 
							<digi:trn key="aim:of">
								of
							</digi:trn>&nbsp;
							
							<c:out value="${aimTeamAuditListForm.pagesSize}"/>
							
							</div>
							<!-- end of Pagination -->
			
										
										
						</div>
						</div>											
					</td>
				</tr>
			</table>	
								
		</td>
	</tr>
</table>

</digi:form>
