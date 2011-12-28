<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
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

<script language="javascript">
    function confirmDelete() {
      var flag = confirm('<digi:trn key="aim:areyousureremove">Are you sure you want to remove?</digi:trn>');
      return flag;
    }

</script>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<digi:instance property="aimRelatedLinksForm" />
<digi:form action="/relatedLinksList.do" method="post">
<digi:context name="digiContext" property="context" />
<logic:notPresent parameter="subtab"> 
	<bean:define id="subtabId" value="0"/>
</logic:notPresent>
<logic:present parameter="subtab">
	<bean:parameter id="subtabId" name="subtab" />
</logic:present>

<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td  width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<c:choose>
							<c:when test="${subtabId == 0 }">
								<digi:trn key="aim:relatedDocumentsList">
								Related Documents List
								</digi:trn>
							</c:when>
							<c:otherwise>
								<digi:trn key="aim:relatedLinksList">
								Related Links List
								</digi:trn>
							</c:otherwise>
						</c:choose>
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
									<c:set var="selectedTab" value="4" scope="request"/>
									<c:set var="selectedSubTab" value="<%=request.getParameter("subtab") == null ? "0": request.getParameter("subtab") %>" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp"  />
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
                                    <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
	                                    <div align="center">
											<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%" border=0>
										<tr><td>
											<digi:errors />
										</td></tr>
										<tr>
											<td bgColor=#ffffff valign="top">

											<table width="100%" cellPadding=0 cellSpacing=4 vAlign="top" align="left">
											<tr><td bgColor=#ffffff valign="top">
												<c:if test="${subtabId == 0 }">
													<table border=0 cellPadding=4 cellSpacing=0 width="100%" id="dataTable">
														<tr>
															<td  bgColor=#999999 valign="center" align="center" width="35%" style="color:black;">
																<b>
																<digi:trn key="aim:doctitle">
																Title
																</digi:trn>
																</b>
															</td>
															<td  bgColor=#999999 valign="center" align="center" width="35%" style="color:black;">
																<b>
																<digi:trn key="fm:documentfilename">
																Filename
																</digi:trn>
																</b>
															</td>
															<td  bgColor=#999999 align="center" width="30%" style="color:black;">
																<b>
																<digi:trn key="aim:activityName">
																	Activity
																</digi:trn>
																</b>
															</td>
														</tr>
														<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
														<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" indexId="idx"
														type="org.digijava.module.aim.helper.Documents">
	
	
														<c:if test="${relatedLink.isFile == true}">
														<tr>
															<td>
<!--																<input style="vertical-align:middle;" type="checkbox" name="deleteLinks" value='<bean:write name="idx"/>'>-->
                                                                <jsp:useBean id="docParams" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${docParams}" property="uuid">
																	<c:out value="${relatedLink.uuid}"/>
																</c:set>
																<c:set target="${docParams}" property="actId">
																	<c:out value="${relatedLink.activityId}"/>
																</c:set>
																<c:set target="${docParams}" property="pageId" value="1"/>

                                                                <c:if test="${fn:length(relatedLink.title) > 30}" >
                                                                    <digi:link href="/getDocumentDetails.do" name="docParams" title="${relatedLink.title}" >
                                                                        <c:out value="${fn:substring(relatedLink.title, 0, 30)}" />...
                                                                    </digi:link>
                                                                </c:if>
                                                                <c:if test="${fn:length(relatedLink.title) <= 30}" >
                                                                    <digi:link href="/getDocumentDetails.do" name="docParams" title="${relatedLink.title}" >
                                                                        <bean:write name="relatedLink" property="title" />
                                                                    </digi:link>
                                                                </c:if>
                                                             </td>
                                                             <td>
																<c:if test="${relatedLink.isFile == true}">
                                                                    <logic:notEmpty name="relatedLink" property="fileName">
                                                                        <bean:define name="relatedLink" property="fileName" id="fileName"/>
                                                                    </logic:notEmpty>
                                                                    <a href="<%=digiContext%>/contentrepository/downloadFile.do?uuid=<bean:write name="relatedLink" property="uuid" />" title="<c:out value='${relatedLink.fileName}' />">
                                                                        <i>
                                                                            <c:if test="${fn:length(relatedLink.fileName) > 30}" >
                                                                                <c:out value="${fn:substring(relatedLink.fileName, 0, 30)}" />...
                                                                            </c:if>
                                                                            <c:if test="${fn:length(relatedLink.fileName) <= 30}" >
                                                                                <c:out value="${relatedLink.fileName}" />
                                                                            </c:if>
                                                                        </i>
                                                                    </a>
                                                                </c:if>
															</td>
															<td title="<c:out value="${relatedLink.activityName}" />">
																	<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams2}" property="ampActivityId">
																		<bean:write name="relatedLink" property="activityId" />
																	</c:set>
																	<digi:link href="/selectActivityTabs.do" name="urlParams2"
																	title="${translation}">

                                                                    <c:if test="${fn:length(relatedLink.activityName) > 30}" >
                                                                        <c:out value="${fn:substring(relatedLink.activityName, 0, 30)}" />...
                                                                    </c:if>
                                                                    <c:if test="${fn:length(relatedLink.activityName) <= 30}" >
                                                                        <c:out value="${relatedLink.activityName}" />
                                                                    </c:if>
																	</digi:link>

															</td>
														</tr>
														</c:if>
	
														</logic:iterate>
														</logic:notEmpty>
													</table>
												</c:if>
											</td></tr>
											<tr><td bgColor=#ffffff valign="top">
												<c:if test="${subtabId == 1 }">
													<table border=0 cellPadding=4 cellSpacing=0 width="100%" id="dataTable" >
														<tr>
															<td bgColor=#999999 valign="center" align="center" width="30%" style="color:black">
																<b>
																<digi:trn key="aim:doctitle">
																Title
																</digi:trn>
																</b>
															</td>
															<td bgColor=#999999 valign="center" align="center" width="30%" style="color:black">
																<b>
																<digi:trn key="aim:links">
																Links
																</digi:trn>
																</b>
															</td>
															<td bgColor=#999999 align="center" width="30%" style="color:black">
																<b>
																<digi:trn key="aim:activityName">
																	Activity
																</digi:trn>
																</b>
															</td>
														</tr>
														<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
														<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" indexId="idx"
														type="org.digijava.module.aim.helper.Documents">
	
														<c:if test="${relatedLink.isFile == false}">
                                                            <tr>
                                                                <td>
<!--                                                                    <input style="vertical-align:middle;" type="checkbox" name="deleteLinks" value='<bean:write name="idx"/>'>-->
                                                                    <jsp:useBean id="docPars" type="java.util.Map" class="java.util.HashMap"/>
                                                                    <c:set target="${docPars}" property="uuid">
                                                                        <c:out value="${relatedLink.uuid}"/>
                                                                    </c:set>
                                                                    <c:set target="${docPars}" property="actId">
                                                                        <c:out value="${relatedLink.activityId}"/>
                                                                    </c:set>
                                                                    <c:set target="${docPars}" property="pageId" value="1"/>
                                                                    <c:if test="${fn:length(relatedLink.title) > 30}" >
                                                                        <digi:link href="/getDocumentDetails.do" name="docPars" title="${relatedLink.title}" >
                                                                            <c:out value="${fn:substring(relatedLink.title, 0, 30)}" />...
                                                                        </digi:link>
                                                                    </c:if>
                                                                    <c:if test="${fn:length(relatedLink.title) <= 30}" >
                                                                        <digi:link href="/getDocumentDetails.do" name="docPars" title="${relatedLink.title}" >
                                                                            <bean:write name="relatedLink" property="title" />
                                                                        </digi:link>
                                                                    </c:if>
                                                                </td>
                                                                <td>
                                                                    <c:if test="${relatedLink.isFile == false}">
                                                                        <a target="_blank" href="<bean:write name="relatedLink" property="url" />">
                                                                        <i><bean:write name="relatedLink" property="url" /></i></a>
                                                                    </c:if>
                                                                </td>
                                                                <td title="<c:out value="${relatedLink.activityName}" />">
																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="ampActivityId">
																		<bean:write name="relatedLink" property="activityId" />
																	</c:set>
																	<digi:link href="/selectActivityTabs.do" name="urlParams"
																	title="${translation}">

                                                                    <c:if test="${fn:length(relatedLink.activityName) > 30}" >
                                                                        <c:out value="${fn:substring(relatedLink.activityName, 0, 30)}" />...
                                                                    </c:if>
                                                                    <c:if test="${fn:length(relatedLink.activityName) <= 30}" >
                                                                        <c:out value="${relatedLink.activityName}" />
                                                                    </c:if>
																	</digi:link>

                                                                </td>
                                                            </tr>
														</c:if>
														</logic:iterate>
														</logic:notEmpty>
													</table>
												</c:if>

											</td></tr>

												<tr>

													<td>&nbsp;

														

													</td>

												</tr>
												<tr>

													<td align="center">
<!--
														<html:submit onclick="return confirmDelete();" styleClass="buton" property="removeFields"><digi:trn key="aim:addEditActivityDeleteSelected">Delete Selected</digi:trn></html:submit>
-->
													</td>

												</tr>
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

