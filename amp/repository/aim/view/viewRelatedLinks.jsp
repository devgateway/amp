<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="javascript">
    function confirmDelete() {
      var flag = confirm('<digi:trn jsFriendly="true" key="aim:areyousureremove">Are you sure you want to remove?</digi:trn>');
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

<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">
	<tr>
		<td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
		</td>
	</tr>
	<tr>
		<td>
					<c:set var="selectedTab" value="4" scope="request"/>
					<c:set var="selectedSubTab" scope="request">
						<%=request.getParameter("subtab") == null ? "0": request.getParameter("subtab") %>
					</c:set>	
					
					
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
									<span class="breadcrump_sep"><b>�</b></span>
									<c:set var="translation">
										<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
									</c:set>
									<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
									<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
									<span class="breadcrump_sep"><b>�</b></span>
									<span class="bread_sel">
										<c:choose>
											<c:when test="${subtabId == 0 }">
												<digi:trn key="aim:relatedDocumentsList">Related Documents List</digi:trn>
											</c:when>
											<c:otherwise>
												<digi:trn key="aim:relatedLinksList">Related Links List</digi:trn>
											</c:otherwise>
										</c:choose>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td valign="top">
								<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
									<jsp:include page="teamSetupMenu.jsp"  />



						<table class="inside" width="100%" cellpadding="0" cellspacing="0">
							<tr>
							    <td width=30% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
							    	<b class="ins_title">
							    		<digi:trn key="aim:doctitle">Title</digi:trn>
							    	</b>
							    </td>
							    <td width=40% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
							    	<b class="ins_title">
							    		<c:if test="${subtabId == 0 }">
							    			<digi:trn key="fm:documentfilename">Filename</digi:trn>
							    		</c:if>
							    		<c:if test="${subtabId == 1 }">
							    			<digi:trn key="aim:links">Links</digi:trn>
							    		</c:if>
							    	</b>
							    </td>
							    <td width=30% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
							    	<b class="ins_title">
							    		<digi:trn key="aim:activityName">Activity</digi:trn>
							    	</b>
							    </td>
							</tr>
							<!-- Documents -->
							<c:if test="${subtabId == 0 }">
								<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
									<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" indexId="idx" type="org.digijava.ampModule.aim.helper.Documents">
										<c:if test="${relatedLink.isFile == true}">
											<tr>	
												<td class="inside">
													<jsp:useBean id="docParams" type="java.util.Map" class="java.util.HashMap"/>
                            						<c:set target="${docParams}" property="uuid">
														<c:out value="${relatedLink.uuid}"/>
													</c:set>
													<c:set target="${docParams}" property="actId">
														<c:out value="${relatedLink.activityId}"/>
													</c:set>
													<c:set target="${docParams}" property="pageId" value="1"/>
													<c:choose>
						                            	<c:when test="${fn:length(relatedLink.title) > 30}" >
						                            		<digi:link href="/getDocumentDetails.do" name="docParams" title="${relatedLink.title}" styleClass="l_sm">
						                              			<c:out value="${fn:substring(relatedLink.title, 0, 30)}" />...
						                              		</digi:link>
						                            	</c:when>
						                            	<c:otherwise>
						                            		<digi:link href="/getDocumentDetails.do" name="docParams" title="${relatedLink.title}" styleClass="l_sm">
						                              			<bean:write name="relatedLink" property="title" />
						                              		</digi:link>
						                            	</c:otherwise>
						                            </c:choose>
                          						</td>
                          						<td class="inside">
                          							<logic:notEmpty name="relatedLink" property="fileName">
						                                <bean:define name="relatedLink" property="fileName" id="fileName"/>
						                            </logic:notEmpty>
						                            <a href="<%=digiContext%>/contentrepository/downloadFile.do?uuid=<bean:write name="relatedLink" property="uuid" />" title="<c:out value='${relatedLink.fileName}' />" class="l_sm">
						                            	<c:choose>
						                                    <c:when test="${fn:length(relatedLink.fileName) > 30}" >
						                                        <c:out value="${fn:substring(relatedLink.fileName, 0, 30)}" />...
						                                    </c:when>
						                                    <c:otherwise>
						                                        <c:out value="${relatedLink.fileName}" />
						                                    </c:otherwise>
														</c:choose>
						                            </a>
						                        </td>
                        
                         	 					<td class="inside" title="<c:out value="${relatedLink.activityName}" />">
													<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams2}" property="activityId">
														<bean:write name="relatedLink" property="activityId" />
													</c:set>
													<c:set target="${urlParams2}" property="public">
														true
													</c:set>
													<c:set target="${urlParams2}" property="pageId">
														2
													</c:set>
													<digi:link href="/viewActivityPreview.do" name="urlParams2" title="${translation}" styleClass="l_sm">
														<c:choose>
						                              		<c:when test="${fn:length(relatedLink.activityName) > 30}" >
																<c:out value="${fn:substring(relatedLink.activityName, 0, 30)}" />...
															</c:when>
						                              		<c:otherwise>
						                                  		<c:out value="${relatedLink.activityName}" />
															</c:otherwise>
														</c:choose>
													</digi:link>
                         						</td>
                         
                       						</tr>
										</c:if>
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="aimRelatedLinksForm" property="relatedLinks">
									<tr><td class="inside" colspan="3" align="center"><digi:trn>No Documents</digi:trn></td></tr>
								</logic:empty>
							</c:if>
							<!-- end of Documents -->
							
							<!-- Links -->
							
							<c:if test="${subtabId == 1 }">
								<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
									<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" indexId="idx" type="org.digijava.ampModule.aim.helper.Documents">
										<c:if test="${relatedLink.isFile == false}">
											<tr>
						                      	<td class="inside">
						                        	<jsp:useBean id="docPars" type="java.util.Map" class="java.util.HashMap"/>
						                          <c:set target="${docPars}" property="uuid">
						                          	<c:out value="${relatedLink.uuid}"/>
						                          </c:set>
						                          <c:set target="${docPars}" property="actId">
						                          	<c:out value="${relatedLink.activityId}"/>
						                          </c:set>
						                          <c:set target="${docPars}" property="pageId" value="1"/>
						                          	<c:choose>
						                          		<c:when test="${fn:length(relatedLink.title) > 30}" >
						                          			<digi:link href="/getDocumentDetails.do" name="docPars" title="${relatedLink.title}" styleClass="l_sm">
						                            			<c:out value="${fn:substring(relatedLink.title, 0, 30)}" />...
						                            		</digi:link>
						                          		</c:when>
						                          		<c:otherwise>
						                          			<digi:link href="/getDocumentDetails.do" name="docPars" title="${relatedLink.title}" styleClass="l_sm">
						                            			<bean:write name="relatedLink" property="title" />
						                            		</digi:link>
						                          		</c:otherwise>
						                          	</c:choose>
						                        </td>
						                        <td class="inside">
						                        	<c:if test="${relatedLink.isFile == false}">
						                          	<a target="_blank" href="<bean:write name="relatedLink" property="url" />" class="l_sm">
						                            	<bean:write name="relatedLink" property="url" />
						                            </a>
						                          </c:if>
						                        </td>
                        
						                        <td title="<c:out value="${relatedLink.activityName}" />" class="inside">
													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams}" property="ampActivityId">
														<bean:write name="relatedLink" property="activityId" />
													</c:set>
													<c:set target="${urlParams}" property="public">
														true
													</c:set>
													<c:set target="${urlParams}" property="pageId">
														2
													</c:set>
													<digi:link href="/viewActivityPreview.do" name="urlParams" title="${translation}" styleClass="l_sm">
														<c:choose>
									                    	<c:when test="${fn:length(relatedLink.activityName) > 30}" >
									                    		<c:out value="${fn:substring(relatedLink.activityName, 0, 30)}" />...
									                    	</c:when>
									                    	<c:otherwise>
									                    		<c:out value="${relatedLink.activityName}" />
									                    	</c:otherwise>
									                    </c:choose>
													</digi:link>
												</td>
						                     </tr>
										</c:if>
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="aimRelatedLinksForm" property="relatedLinks">
									<tr><td class="inside" colspan="3" align="center"><digi:trn>No Links</digi:trn></td></tr>
								</logic:empty>
							</c:if>	
							
							<!-- end of Links -->
							<tr><td colspan="3"><digi:errors /></td></tr>
						</table>
						
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
