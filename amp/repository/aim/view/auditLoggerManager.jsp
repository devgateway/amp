<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@page import="org.digijava.module.aim.services.auditcleaner.AuditCleaner"%>
<jsp:include page="/repository/aim/view/scripts/auditFilter.jsp"  />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/compareAcivity.js"/>"></script>
<script language="javascript">

function actionChanged(value){
	var header = document.getElementById("frecuencyHeaderId");
	if (header!= null){
		if (value == 'delete'){
			header.innerHTML= "<digi:trn jsFriendly='true'>Older then</digi:trn>:&nbsp;&nbsp;";
		}
		if (value == 'export'){
			header.innerHTML= "<digi:trn jsFriendly='true'>Selected Period</digi:trn>:&nbsp;&nbsp;";
		}
	}
}

function submitClean(){
	if (document.getElementById("actionId").value == 'delete' && ! confirm("<digi:trn jsFriendly='true'>Do you really want to delete this log information</digi:trn> ?")){
		return;
	}

 <digi:context name="cleanurl" property="context/module/moduleinstance/auditLoggerManager.do?clean=true" />
	document.aimAuditLoggerManagerForm.action = "<%=cleanurl%>";
	document.aimAuditLoggerManagerForm.target = "_self";
	document.aimAuditLoggerManagerForm.submit();
}

function submitFilter() {
	document.aimAuditLoggerManagerForm.submit();
}

function toggleLoggs(){
	log = document.getElementById("login").checked;
	<digi:context name="cleanurl" property="context/module/moduleinstance/auditLoggerManager.do?withLogin=" />
	document.aimAuditLoggerManagerForm.action = "<%=cleanurl%>"+log;
	document.aimAuditLoggerManagerForm.target = "_self";
	document.aimAuditLoggerManagerForm.submit();
}

function submitExport() {
	document.aimAuditLoggerManagerForm.action = "/aim/scorecardManager.do";
	document.aimAuditLoggerManagerForm.submit();
}

function show_hide(divID){
	var divArea = document.getElementById(divID)

	if(divArea.style.visibility =='hidden'){
		divArea.style.visibility = 'visible'
	}else{
		divArea.style.visibility = 'hidden'
	}
}


function showUser(email){
	if (email != ""){
        var param = "~edit=true~email="+email;
		previewWorkspaceframe('/aim/default/userProfile.do',param);
	}
	else{
		var trasnlation = '<digi:trn jsFriendly="true" key="aim:userblankmail">The user does not have a valid email address</digi:trn>';
		alert (trasnlation);
	}
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
function toggleSettings(){
	var currentDisplaySettings = $('#currentDisplaySettings');
	var displaySettingsButton = $('#displaySettingsButton');
	if(currentDisplaySettings.css('display') == "inline-flex"){
		currentDisplaySettings.hide();
		$('#exportScorecard').hide();
		displaySettingsButton.html('<digi:trn jsFriendly="true" key="aim:Showcleanupoptions">Show cleanup options</digi:trn>'+ ' &gt;&gt;');
	}
	else
	{
		currentDisplaySettings.css('display', 'inline-flex');
		$('#exportScorecard').css('display','inline-flex');
		displaySettingsButton.html('<digi:trn jsFriendly="true" key="aim:Hidecleanupoptions">Hide cleanup options</digi:trn>'+ ' &lt;&lt;');
	}
}

function resetSearch() {
	document.getElementById("userId").selectedIndex = 0;
	document.getElementById("teamId").selectedIndex = 0;
	document.getElementById("selectedDateFromText").value="";
	document.getElementById("selectedDateToText").value="";
	document.aimAuditLoggerManagerForm.action = "/aim/auditLoggerManager.do?action=reset";
	document.aimAuditLoggerManagerForm.submit();
}

function exportScorecard () {
	window.location =  "/rest/scorecard/export";
}

function filtersort(filterBy) {
	document.aimAuditLoggerManagerForm.selectedUser.value = document.getElementById("userId").value;
	document.aimAuditLoggerManagerForm.selectedTeam.value = document.getElementById("teamId").value;
	document.aimAuditLoggerManagerForm.selectedDateFrom.value = document.getElementById("selectedDateFromText").value;
	document.aimAuditLoggerManagerForm.selectedDateTo.value = document.getElementById("selectedDateToText").value;
	document.aimAuditLoggerManagerForm.action = "/auditLoggerManager.do?sortBy="+filterBy;
	document.aimAuditLoggerManagerForm.submit();
}


</script>

<h1 class="admintitle"><digi:trn key="aim:AuditLoggerManager">Audit Logger Manager</digi:trn></h1>
<digi:instance property="aimAuditLoggerManagerForm" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<digi:form action="/auditLoggerManager.do" method="post">
<input type="hidden" name="withLogin">
<center>
<div id="auditloggermanagercontainer">
<table cellpadding="0" cellspacing="0">
	<tr>
		<td  valign="top" width="980">
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
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
						<digi:trn key="aim:AuditLoggerManager">
							Audit Logger Manager
						</digi:trn>
						</td>
					<!-- End navigation -->
				</tr>
				<tr bgcolor="#ffffff">
				  <td valign="top">
				  <div style="padding: 3px; width: 100%; background:#fff; font-size: 8pt;">
				  <span style="cursor:pointer; font-style: italic;float:left;color: red;">
				  	<%if(AuditCleaner.getInstance().isRunning()){%>
				  	<%if (AuditCleaner.getInstance().getRemainingdays()!= null){%>
				  		<digi:trn key="aim:auditautocleanup">The automatic cleanup of the audit trail will occur in</digi:trn>
				  		<%=AuditCleaner.getInstance().getRemainingdays()%>
				  		<digi:trn key="aim:days">Days</digi:trn>
				  	<%}
				  	}%>
				  </span>
				  <c:set var="loginTr">
							<digi:trn>Show logins</digi:trn>
						</c:set>
				  <c:if test="${aimAuditLoggerManagerForm.withLogin==true }">
						<input type="checkbox" id="login" onchange="toggleLoggs()" checked="checked">${loginTr}
					</c:if>
					<c:if test="${aimAuditLoggerManagerForm.withLogin==false }">
						<input type="checkbox" id="login" onchange="toggleLoggs()">${loginTr}
					</c:if>

				  <span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton"><digi:trn key="aim:Showcleanupoptions">Show cleanup options</digi:trn> &gt;&gt;<br></span>
                                &nbsp;
								<div style="display:none;background-color:#ffffff;padding:2px" id="currentDisplaySettings" >
                                 <table cellpadding="2" cellspacing="2" border="0" width="250px">
                                 <tr>
                                 	<td align="right">
                                 	<strong><digi:trn>Selected Action:</digi:trn>&nbsp;&nbsp;</strong>
                                	</td>
                                	<td>
                                 	<html:select property="useraction" styleClass="inp-text" styleId="actionId" onchange="actionChanged(this.value);">
                                 		<html:option value="delete"><digi:trn>Delete</digi:trn> </html:option>
                                 		<html:option value="export"><digi:trn>Export</digi:trn> </html:option>
                                 	</html:select>
                                 	</td>
                                 </tr>
                                 <tr>
									<td align="right">
                                 	<strong id="frecuencyHeaderId"><digi:trn>Older then</digi:trn>:&nbsp;&nbsp; </strong>
                                 	</td>
                                 	<td align="left" height="30px">
                                 	<html:select property="frecuency" styleClass="inp-text" >
                                 			<html:option value="30">30 <digi:trn key="aim:days">Days</digi:trn></html:option>
                                 			<html:option value="60">60 <digi:trn key="aim:days">Days</digi:trn></html:option>
                                 			<html:option value="90">90 <digi:trn key="aim:days">Days</digi:trn></html:option>
                                 			<html:option value="180">180 <digi:trn key="aim:days">Days</digi:trn></html:option>
                                 			<html:option value="360">360 <digi:trn key="aim:days">Days</digi:trn></html:option>
                                 	</html:select>
                                 	</td>
                                 </tr>
                                 <tr>
                                	<td align="right">
                                 		<input  class="dr-menu" type="button" onclick="submitClean()" value="<digi:trn>OK</digi:trn>">
                                 		&nbsp;
									</td>

									<td align="left">

																			<input class="dr-menu" type="button" value="<digi:trn>Cancel</digi:trn>" onclick="document.aimAuditLoggerManagerForm.reset();toggleSettings()">
									</td>
                                 </tr>
                                 </table>
                                 </div>
									</div>
                            	</div>
                          		<br>

                          		<jsp:include page="loggerQuickView.jsp" />
										<c:if
												test="${aimAuditLoggerManagerForm.withLogin==false }">
											<input type="button"
												   title="<digi:trn>Click here to view full list of activities compared to its previous versions</digi:trn>"
												   onclick="javascript:compareAll()" class="dr-menu"
												   value="&nbsp;&nbsp;<digi:trn>Compare All</digi:trn>&nbsp;&nbsp;"
												   style="cursor: pointer; font-style: italic; float: right; margin: 0.5% 1.5% 0.5%;">
										</c:if> <br>


										<span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleFilterSettings();" id="displayFilterButton">
														<c:set var="hiddenStyle" value="display:none;"/>
														<c:set var="settingsTitle">
															<digi:trn key="aim:Showfilteroptions">Show Filter options</digi:trn>
														</c:set>
														  <c:if test="${(not empty aimAuditLoggerManagerForm.selectedUser and aimAuditLoggerManagerForm.selectedUser !=-1 )
														  or (not empty aimAuditLoggerManagerForm.selectedTeam and aimAuditLoggerManagerForm.selectedTeam != '-1' )
														  or (not empty aimAuditLoggerManagerForm.selectedDateFrom ) or (not empty aimAuditLoggerManagerForm.selectedDateTo )}">
															  <c:set var="hiddenStyle" value="display:inline-flex;"/>
															  <c:set var="settingsTitle">
																  <digi:trn key="aim:Showfilteroptions">Hide Filter options</digi:trn>
															  </c:set>
														  </c:if>

													  <c:out value="${settingsTitle}"/> </span>
										&nbsp;<br>
										<div style="<c:out value ="${hiddenStyle}"/>background-color:#ffffff;padding:2px; width: 100%" id="currentFilterSettings" >
											<div class="divTable" >
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
														<div class="divTableCell"><html:select property="selectedTeam" styleClass="inp-text" styleId="teamId">
															<html:option value="-1"><digi:trn>Select Team </digi:trn></html:option>
															<html:options property="teamList"></html:options>
														</html:select></div>
													</div>
													<c:set var="dateTr">
														<digi:trn>Date</digi:trn>
													</c:set>

													<div class="divTableRow"  >
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
														<div class="divTableCell"><input class="dr-menu" type="button" value="<digi:trn>Reset</digi:trn>" onclick="document.aimAuditLoggerManagerForm.reset();resetSearch()"></div>
													</div>
												</div>
											</div>
										</div>
										</div>
										</div>
										</div>
										</div>
										<br>
				<table width="100%" height="100%" cellpadding="0" cellspacing="0" bgColor=#ffffff id="auditloggertable">
				<tr>
						<td colspan="2" valign="top" >
						<div>
						<div align="center" style="border-top:1px solid #cccccc;border-bottom:1px solid #cccccc;border-right:1px solid #cccccc;">
						<table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0"  id="dataTable">
							<tr>
								<td width="280" height="22" align="center" valign="center"bgcolor="#C7D4DB" >
								<c:if
									test="${aimAuditLoggerManagerForm.sortBy!='nameasc'}">
									<digi:link style="color:#376091;font-size:14px;" href="#" onclick="javascript:filtersort('nameasc');return false;">
									<b><digi:trn key="aim:name">Name</digi:trn></b>
									</digi:link>
								</c:if>
								<c:if
									test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='nameasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('namedesc'); return false;">
										<b><digi:trn key="aim:name">Name</digi:trn></b>
									</digi:link>
							   </c:if>
							   </td>
								<td valign="center" align="center"bgcolor="#C7D4DB" style="color: black" width="150">
								<c:if test="${aimAuditLoggerManagerForm.sortBy!='typeasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('typeasc'); return false;">
										<b><digi:trn key="aim:objectType">Object Type</digi:trn></b>
									</digi:link>
								</c:if>
								<c:if test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='typeasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('typedesc'); return false;">
										<b><digi:trn key="aim:objectType">Object Type</digi:trn></b>
									</digi:link>
								</c:if>
								</td>
								<td valign="center" align="center"bgcolor="#C7D4DB"
									style="color: black"><c:if
									test="${aimAuditLoggerManagerForm.sortBy!='teamasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('teamasc'); return false;">
										<b><digi:trn key="aim:teamName">Team Name</digi:trn></b>
									</digi:link>
								</c:if> <c:if
									test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='teamasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('teamdesc'); return false;">
										<b><digi:trn key="aim:teamName">Team Name</digi:trn></b>
									</digi:link>
								</c:if></td>
								<td align="center" valign="center"bgcolor="#C7D4DB"
									style="color: black"><c:if
									test="${aimAuditLoggerManagerForm.sortBy!='authorasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('authorasc'); return false;">
										<b><digi:trn key="aim:authorName">Author Name</digi:trn></b>
									</digi:link>
								</c:if> <c:if
									test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='authorasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('authordesc'); return false;">
										<b><digi:trn key="aim:authorName">Author Name</digi:trn></b>
									</digi:link>
								</c:if></td>
								<td width="100" align="center" valign="center"bgcolor="#C7D4DB"
									style="color: black"><c:if
									test="${aimAuditLoggerManagerForm.sortBy!='creationdateasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('creationdateasc'); return false;">
										<b><digi:trn key="aim:creationDateLogger">Creation Date</digi:trn></b>
									</digi:link>
								</c:if> <c:if
									test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='creationdateasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('creationdatedesc'); return false;">
										<b><digi:trn key="aim:creationDateLogger">Creation Date</digi:trn></b>
									</digi:link>
							  </c:if></td>
								<td width="208" align="center" valign="center"bgcolor="#C7D4DB"
									style="color: black"><c:if
									test="${aimAuditLoggerManagerForm.sortBy!='editorasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('editorasc'); return false;">
										<b><digi:trn key="aim:editorName">Editor Name</digi:trn></b>
									</digi:link>
								</c:if> <c:if
									test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='editorasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('editordesc'); return false;">
										<b><digi:trn key="aim:editorName">Editor Name</digi:trn></b>
									</digi:link>
								</c:if></td>
								<td align="center" valign="center"bgcolor="#C7D4DB"
									style="color: black"><c:if
									test="${aimAuditLoggerManagerForm.sortBy!='changedateasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('changedateasc'); return false;">
										<b><digi:trn key="aim:changeDate">Change Date</digi:trn></b>
									</digi:link>
								</c:if> <c:if
									test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='changedateasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('changedatedesc'); return false;">
										<b><digi:trn key="aim:changeDate">Change Date</digi:trn></b>
									</digi:link>
								</c:if></td>
								<td width="129" align="center" valign="center"bgcolor="#C7D4DB"style="color: black">
								<c:if test="${aimAuditLoggerManagerForm.sortBy!='actionasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('actionasc'); return false;">
										<b><digi:trn key="aim:action">Action</digi:trn></b>
									</digi:link>
								</c:if> <c:if
									test="${not empty aimAuditLoggerManagerForm.sortBy && aimAuditLoggerManagerForm.sortBy=='actionasc'}">
									<digi:link style="color:#376091;" href="javascript:filtersort('actiondesc'); return false;">
										<b><digi:trn key="aim:action">Action</digi:trn></b>
									</digi:link>
								</c:if></td>
                                                                <td  align="center" valign="center"bgcolor="#C7D4DB"style="color: black;" nowrap>
                                                                <c:choose>
                                                                    <c:when test="${aimAuditLoggerManagerForm.sortBy!='detailasc'}">
                                                                        <digi:link style="color:#376091;" href="javascript:filtersort('detailasc'); return false;">
                                                                            <b><digi:trn>Additional Details</digi:trn></b>
                                                                        </digi:link>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <digi:link style="color:#376091;" href="javascript:filtersort('detaildesc'); return false;">
                                                                            <b><digi:trn>Additional Details</digi:trn></b>
                                                                        </digi:link>
                                                                    </c:otherwise>
                                                                </c:choose>
                                        </td>
								<c:if test="${aimAuditLoggerManagerForm.withLogin==false }">
									<td width="129" align="center" valign="center"bgcolor="#C7D4DB"style="color: #376091;">
										<b><digi:trn key="aim:viewDiff">View Differences</digi:trn></b>
									</td>
								</c:if>
							</tr>
							<logic:iterate name="aimAuditLoggerManagerForm" property="logs"
								id="log" type="org.digijava.module.aim.dbentity.AmpAuditLogger">
								<tr>
								<td class="auditloggername" width="280" height="18" align="center" title="${log.objectName}">
									<c:choose>
										<c:when test="${fn:length(log.objectName) > 30}" >
											<c:out value="${fn:substring(log.objectName, 0, 30)}" />...
										</c:when>
										<c:otherwise>
											<bean:write name="log" property="objectName"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td align="center" width="150" title="${log.objectTypeTrimmed}">
									<digi:trn key="aim:ObjectType${log.objectTypeTrimmed}"><bean:write name="log" property="objectTypeTrimmed"/></digi:trn>
								</td>
								<td align="center" width="100" title="${log.teamName}">
									<bean:write name="log" property="teamName"/>
								</td>
								<td align="center" width="150">
									<a href="javascript:showUser('<bean:write name="log" property="authorEmail"/>')" style="text-decoration: none" title="${log.authorName}">
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
								<td width="100" align="center" title="${log.loggedDate}">
									<c:choose>
										<c:when test="${fn:length(log.sloggeddate) < 8}" >
											<digi:trn>No Date</digi:trn>
										</c:when>
										<c:otherwise>
											<bean:write name="log" property="sloggeddate"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td width="100" align="center" title="${log.editorName}">
									<bean:write name="log" property="editorName" />
								</td>
								<td width="150" align="center" title="${log.modifyDate}">
									  <bean:write name="log" property="smodifydate"/>
								 </td>
									<td width="100" align="center">
										<logic:equal value="delete" property="action" name="log">
											<digi:trn key="admin:delete">Delete</digi:trn>
										</logic:equal> <logic:equal value="add" property="action" name="log">
											<digi:trn key="admin:add">Add</digi:trn>
										</logic:equal> <logic:equal value="update" property="action" name="log">
											<digi:trn key="admin:update">Update</digi:trn>
										</logic:equal>	<logic:equal value="login" property="action" name="log">
											<digi:trn key="admin:delete">Login</digi:trn>
										</logic:equal>
									</td>
									<td align="center">
									<c:if test="${not empty log.detail}">
										<digi:trn>${log.detail}</digi:trn>
									</c:if>
									<c:if test="${empty log.detail}">
										<digi:trn>No Data</digi:trn>
									</c:if>
								</td>
									<td>
										<c:if test="${not empty log.objectId && log.objectType=='org.digijava.module.aim.dbentity.AmpActivityVersion'}">
											<input type="button" title="<digi:trn>Click here to compare with previous version</digi:trn>" onclick="javascript:viewDifferences(${log.objectId})"
												   class="dr-menu" value="&nbsp;&nbsp;<digi:trn>Compare</digi:trn>&nbsp;&nbsp;">
										</c:if>
									</td>
							</tr>
                          </logic:iterate>
						</table>
						</div>
						</div>
					</td>
				  </tr>

					<tr>
						<td align="left" valign="middle">
						<div style="cursor: pointer; font-family: Arial; text-align: left; text-decoration: none;">
						<br>
						<c:if test="${aimAuditLoggerManagerForm.currentPage > 1}">
							<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlParamsFirst}" property="page" value="1"/>
							<c:set target="${urlParamsFirst}" property="sortBy" value="${aimAuditLoggerManagerForm.sortBy}" />
							<c:set target="${urlParamsFirst}" property="withLogin" value="${aimAuditLoggerManagerForm.withLogin}" />
							<c:set target="${urlParamsFirst}" property="selectedUser" value="${aimAuditLoggerManagerForm.selectedUser}" />
							<c:set target="${urlParamsFirst}" property="selectedTeam" value="${aimAuditLoggerManagerForm.selectedTeam}" />
							<c:set var="translation">
								<digi:trn key="aim:firstpage">First Page</digi:trn>
							</c:set>
							<digi:link href="/auditLoggerManager.do" style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
								<span style="font-size: 8pt; font-family: Tahoma;">&lt;&lt;</span>
							</digi:link>
							<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlParamsPrevious}" property="page" value="${aimAuditLoggerManagerForm.currentPage -1}"/>
							<c:set target="${urlParamsPrevious}" property="sortBy" value="${aimAuditLoggerManagerForm.sortBy}" />
							<c:set target="${urlParamsPrevious}" property="withLogin" value="${aimAuditLoggerManagerForm.withLogin}" />
								<c:set target="${urlParamsFirst}" property="selectedUser" value="${aimAuditLoggerManagerForm.selectedUser}" />
								<c:set target="${urlParamsFirst}" property="selectedTeam" value="${aimAuditLoggerManagerForm.selectedTeam}" />
							<c:set var="translation">
								<digi:trn key="aim:previouspage">Previous Page</digi:trn>
							</c:set>|
							<digi:link href="/auditLoggerManager.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
								<span style="font-size: 8pt; font-family: Tahoma;">
								<digi:trn key="aim:previous">
									Previous
								</digi:trn></span>&nbsp;
							</digi:link>|
						</c:if>
					<c:set var="length" value="${aimAuditLoggerManagerForm.pagesToShow}"></c:set>
					<c:set var="start" value="${aimAuditLoggerManagerForm.offset}"/>
					<logic:iterate name="aimAuditLoggerManagerForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">
						<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams1}" property="sortBy" value="${aimAuditLoggerManagerForm.sortBy}" />
						<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
						<c:set target="${urlParams1}" property="withLogin" value="${aimAuditLoggerManagerForm.withLogin}" />
								<c:set target="${urlParams1}" property="selectedUser" value="${aimAuditLoggerManagerForm.selectedUser}" />
								<c:set target="${urlParams1}" property="selectedTeam" value="${aimAuditLoggerManagerForm.selectedTeam}" />
								<c:set target="${urlParams1}" property="selectedDateFrom" value="${aimAuditLoggerManagerForm.selectedDateFrom}" />
								<c:set target="${urlParams1}" property="selectedDateTo" value="${aimAuditLoggerManagerForm.selectedDateTo}" />
						<c:if test="${aimAuditLoggerManagerForm.currentPage == pages && aimAuditLoggerManagerForm.pagesSize > 1}">
							<font color="#FF0000"><%=pages%></font>
							|
						</c:if>
						<c:if test="${aimAuditLoggerManagerForm.currentPage != pages && aimAuditLoggerManagerForm.pagesSize > 1}">
							<c:set var="translation">
								<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
							</c:set>
							<digi:link href="/auditLoggerManager.do" name="urlParams1" title="${translation}" friendlyUrl="false">
								<%=pages%>
							</digi:link>
							|
						</c:if>
						</logic:iterate>
						<c:if test="${aimAuditLoggerManagerForm.currentPage != aimAuditLoggerManagerForm.pagesSize}">
							<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap" />
							<c:set target="${urlParamsNext}" property="page" value="${aimAuditLoggerManagerForm.currentPage+1}"/>
							<c:set target="${urlParamsNext}" property="sortBy" value="${aimAuditLoggerManagerForm.sortBy}" />
							<c:set target="${urlParamsNext}" property="withLogin" value="${aimAuditLoggerManagerForm.withLogin}" />
								<c:set target="${urlParamsNext}" property="selectedUser" value="${aimAuditLoggerManagerForm.selectedUser}" />
								<c:set target="${urlParamsNext}" property="selectedTeam" value="${aimAuditLoggerManagerForm.selectedTeam}" />
								<c:set target="${urlParamsNext}" property="selectedDateFrom" value="${aimAuditLoggerManagerForm.selectedDateFrom}" />
								<c:set target="${urlParamsNext}" property="selectedDateTo" value="${aimAuditLoggerManagerForm.selectedDateTo}" />
							<c:set var="translation"> <digi:trn key="aim:nextpage">Next Page</digi:trn></c:set>
							<digi:link  href="/auditLoggerManager.do" style="text-decoration=none" name="urlParamsNext" title="${translation}" friendlyUrl="false">
								<span style="font-size: 8pt; font-family: Tahoma;"><digi:trn key="aim:next">Next</digi:trn></span>
							</digi:link>
							<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap" />|

						<c:if test="${aimAuditLoggerManagerForm.pagesSize > aimAuditLoggerManagerForm.pagesToShow}">
							<c:set target="${urlParamsLast}" property="page" value="${aimAuditLoggerManagerForm.pagesSize}" />
							<c:set target="${urlParamsLast}" property="sortBy" value="${aimAuditLoggerManagerForm.sortBy}" />
							<c:set target="${urlParamsLast}" property="withLogin" value="${aimAuditLoggerManagerForm.withLogin}" />
								<c:set target="${urlParamsNext}" property="selectedUser" value="${aimAuditLoggerManagerForm.selectedUser}" />
								<c:set target="${urlParamsNext}" property="selectedTeam" value="${aimAuditLoggerManagerForm.selectedTeam}" />
								<c:set target="${urlParamsNext}" property="selectedDateFrom" value="${aimAuditLoggerManagerForm.selectedDateFrom}" />
								<c:set target="${urlParamsNext}" property="selectedDateTo" value="${aimAuditLoggerManagerForm.selectedDateTo}" />
						</c:if>

						<c:if test="${aimAuditLoggerManagerForm.pagesSize < aimAuditLoggerManagerForm.pagesToShow}">
							<c:set target="${urlParamsLast}" property="sortBy" value="${aimAuditLoggerManagerForm.sortBy}" />
							<c:set target="${urlParamsLast}" property="page" value="${aimAuditLoggerManagerForm.pagesSize}" />
							<c:set target="${urlParamsLast}" property="withLogin" value="${aimAuditLoggerManagerForm.withLogin}" />
								<c:set target="${urlParamsNext}" property="selectedUser" value="${aimAuditLoggerManagerForm.selectedUser}" />
								<c:set target="${urlParamsNext}" property="selectedTeam" value="${aimAuditLoggerManagerForm.selectedTeam}" />
								<c:set target="${urlParamsNext}" property="selectedDateFrom" value="${aimAuditLoggerManagerForm.selectedDateFrom}" />
								<c:set target="${urlParamsNext}" property="selectedDateTo" value="${aimAuditLoggerManagerForm.selectedDateTo}" />
						</c:if>
						<c:set var="translation"><digi:trn key="aim:lastpage">Last Page</digi:trn></c:set>
						<digi:link href="/auditLoggerManager.do" style="text-decoration=none" name="urlParamsLast" title="${translation}" friendlyUrl="false">
							<span style="font-size: 8pt; font-family: Tahoma;">&gt;&gt;</span>
						</digi:link>
					</c:if>
							<c:if test="${aimAuditLoggerManagerForm.withLogin==false }">
							<input type="button"
								   title="<digi:trn>Click to view list of activities compared to its previous versions</digi:trn>"
								   onclick="javascript:compareAll()" class="dr-menu"
								   value="&nbsp;&nbsp;<digi:trn>Compare All</digi:trn>&nbsp;&nbsp;"
								   style="cursor: pointer; font-style: italic; float: right; margin: 0 1% 0.5% 2.8%;">
							</c:if>
					<c:out value="${aimAuditLoggerManagerForm.currentPage}"/>&nbsp;
					<span style="font-size: 8pt; font-family: Tahoma;">
					<digi:trn key="aim:of">of</digi:trn></span>&nbsp;
					<span style="font-size: 8pt; font-family: Tahoma;">
						<c:out value="${aimAuditLoggerManagerForm.pagesSize}"/>
					</span>
						<a style="float: right; cursor: pointer;" onclick="window.scrollTo(0,0); return false">
							<digi:trn key="aim:backtotop">Back to Top</digi:trn>
					</span>
						<span style="font-size: 10pt; font-family: Tahoma;">↑</span></a>
					</td>
					</div>
				</tr>
				</table>
				</td>
 			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</center>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script>
</digi:form>
