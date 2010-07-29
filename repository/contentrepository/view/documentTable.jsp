<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
			
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>

<logic:notEmpty name="checkBoxToHide" scope="request">
	<bean:define id="checkBoxToHideLocal" value="true"></bean:define>
</logic:notEmpty> 
<logic:empty name="checkBoxToHide" scope="request">
	<bean:define id="checkBoxToHideLocal" value="false"></bean:define>
</logic:empty> 
<table id="team_table" bgcolor="white" width="100%">
						<thead>
							<tr>
								<logic:equal name="checkBoxToHideLocal" value="false">
									<th><digi:trn>Select</digi:trn></th>
								</logic:equal>
								<th><digi:trn>Title</digi:trn></th>
								<th><digi:trn>Type</digi:trn></th>
								<th><digi:trn>Resource Name</digi:trn></th>
								<th><digi:trn>Date</digi:trn></th>
								<th><digi:trn>Year Of Publication</digi:trn></th>
								<th><digi:trn>Size (MB)</digi:trn></th>
								<th><digi:trn>Document Type</digi:trn></th>
								<th><digi:trn>Description</digi:trn></th>
								<th><digi:trn>Actions</digi:trn></th>
							</tr>
						</thead>
						<tbody>
						<logic:iterate name="documentDataCollection" id="documentData"	type="org.digijava.module.contentrepository.helper.DocumentData" indexId="counter">
							<%--
					int index2;
					String documentName = documentData.getName();
					String iconPath = "";
					index2 = ((String) documentName).lastIndexOf(".");
					if (index2 >= 0) {
						iconPath = "module/cms/images/extensions/"
								+ ((String) documentName).substring(index2 + 1,
										((String) documentName).length())
								+ ".gif";
					}

					--%>
							<logic:equal name="documentData" property="isPublic" value="true">
								<c:set var="makePublicCommand">
									<digi:trn key="contentrepository:republish">Rep</digi:trn>
								</c:set>
							</logic:equal>
							<logic:equal name="documentData" property="isPublic" value="false">
								<c:set var="makePublicCommand">
									<digi:trn key="contentrepository:makePublic">Pub</digi:trn>
								</c:set>
							</logic:equal>
							<tr>
								<logic:equal name="checkBoxToHideLocal" value="false">
									<td>
										<input class="selDocs" type=checkbox value="${documentData.uuid}" name="selectedDocs"/>										
	                                    &nbsp;                                                          
		                            </td>
		                        </logic:equal>
								<td>
									<c:if test="${documentData.hasAnyVersionPendingApproval==true &&  documentData.hasApproveVersionRights==true}">
										<span style="color:#00CC00;font-weigth:bold;font-size:9pt;">
											<bean:write name='documentData' property='title'/>
										</span>
									</c:if>									
									<c:if test="${documentData.hasAnyVersionPendingApproval!=true ||  documentData.hasApproveVersionRights!=true}">
										<bean:write name='documentData' property='title'/>
									</c:if>
									&nbsp;
									<logic:equal name="documentData" property="lastVersionIsShared" value="true">
										<span style="color:#00CC00;font-weigth:bold;font-size:9pt;">*</span>
									</logic:equal>
								</td>
								<td>
									<digi:img skipBody="true" src="${documentData.iconPath}" border="0" alt="${documentData.contentType}" align="absmiddle"/>
									&nbsp; 
								</td>
								<td>
									<c:set var="isUrl" scope="page" value="false" />
									<c:set var="translation">
										<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to</digi:trn>
									</c:set>
									<c:if test="${documentData.webLink != null}" >
										<a onmouseover="Tip('${translation} ${documentData.webLink}')" onmouseout="UnTip()" 
												 onclick="window.open('${documentData.webLink}')"
											style="cursor:pointer; color: blue; font-size: 11px">  
									</c:if>
										 <bean:write name="documentData" property="name" />
									<c:if test="${documentData.webLink != null}" >
										<c:set var="isUrl" scope="page" value="true" />
										</a>
									</c:if>
								</td>
								<td>
									<bean:write name="documentData" property="calendar" />
								</td>
								
									<td>
									<bean:write name="documentData" property="yearofPublication" />
								</td>
								
																
								
								<td>
									<bean:write name="documentData" property="fileSize" />
								</td>
								<td>
									${documentData.cmDocType }
								</td>
								<td>
									<bean:write name='documentData' property='description'/>
									<%-- <script type="text/javascript">
										document.write(unescape("<bean:write name='documentData' property='description'/>"));
									</script> --%>
									<a name="aDocumentUUID" style="display: none"><bean:write name="documentData" property="uuid" /></a>
								</td>
								<td nowrap="nowrap"> 
									<a style="display:none" id="fake1" href="http://www.yahoo.com">FOR_SILK</a>
									
									<c:choose>
										<c:when test="${documentData.webLink == null}">
											<c:set var="translation">
												<digi:trn>Click here to download document</digi:trn>
											</c:set> 
											<a style="cursor:pointer; text-decoration:none; color: blue" id="D<bean:write name='documentData' property='uuid' />"
											onClick="downloadFile('<bean:write name='documentData' property='uuid' />');"title="${translation}">
												<digi:trn>Download</digi:trn>
											</a>
										</c:when>
										<c:otherwise>
		
											<a style="cursor:pointer; text-decoration:none; color: blue" id="D<bean:write name='documentData' property='uuid' />"
											onclick="window.open('${documentData.webLink }')" onmouseout="UnTip()"	onmouseover="Tip('<digi:trn>Follow link to </digi:trn> ${documentData.webLink}')">
												<digi:trn>Follow the Link</digi:trn>
											</a>
										</c:otherwise>
									</c:choose>
									
	
	
									<logic:equal name="documentData" property="hasVersioningRights" value="true">
										<span style="color: blue"><strong>|</strong></span>
										<a style="cursor:pointer; text-decoration:none; color: blue" id="plus<bean:write name='documentData' property='uuid' />"
										onClick="setType('version'); configPanel(0,'${documentData.escapedAmpTitle}','${documentData.escapedAmpDescription}', <%=documentData.getCmDocTypeId() %> ,'<%=documentData.getUuid() %>', ${isUrl},'<%=documentData.getYearofPublication() %>' );showMyPanel(0, 'addDocumentDiv');"
										title="<digi:trn>Click here to add a new version of this document</digi:trn>">
											<digi:trn>Add Version</digi:trn>
									</logic:equal>
									
									<c:set var="translationForWindowTitle">
										<digi:trn>Version History</digi:trn>
									</c:set> 
									<logic:equal name="documentData" property="hasShowVersionsRights" value="true">
										<span style="color: blue"><strong>|</strong></span>
										<a style="cursor:pointer; text-decoration:none; color: blue" id="H<bean:write name='documentData' property='uuid' />"
										onClick="showMyPanelCopy(1,'viewVersions'); requestVersions('<%=documentData.getUuid() %>'); setPanelHeader(1, '${translationForWindowTitle}');"
										title="<digi:trn>Click here to see a list of versions for this document</digi:trn>">
											<digi:trn>Versions</digi:trn>
										</a>
									</logic:equal>
	
									<logic:equal name="documentData" property="hasMakePublicRights" value="true">
										<c:if test="${ (!documentData.isPublic) || (!documentData.lastVersionIsPublic) }">
											<span style="color: blue"><strong>|</strong></span>
											<a style="cursor:pointer; text-decoration:none; color: blue" id="Pub<bean:write name='documentData' property='uuid' />"
											onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.MAKE_PUBLIC %>' ,'<%=documentData.getUuid() %>', true);"
											title="<digi:trn>Click here to make this document public</digi:trn>">
												<digi:trn>Make Public</digi:trn>
											</a>
										</c:if>
									</logic:equal>
									
									
									<logic:equal name="documentData" property="isPublic" value="true">
										<logic:equal name="documentData" property="hasDeleteRightsOnPublicVersion" value="true">
											<span style="color: blue"><strong>|</strong></span>
											<a style="cursor:pointer; text-decoration:none; color: blue" id="Priv<bean:write name='documentData' property='uuid' />"
											onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.UNPUBLISH %>', '<%=documentData.getUuid() %>');"
											title="<digi:trn>Click here to unpublish this document</digi:trn>">
												<digi:trn>Unpublish</digi:trn>
											</a>
										</logic:equal>								
									</logic:equal>
									
									<logic:notEmpty name="documentData" property="shareWith">
										<logic:equal name="documentData" property="hasShareRights" value="true">										
											
											<logic:equal name="documentData" property="needsApproval" value="false">
												<logic:equal  name="documentData" property="lastVersionIsShared" value="false">
													<span style="color: blue"><strong>|</strong></span>
													<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue"
													onClick="shareDoc('<%=documentData.getUuid() %>','<%=documentData.getShareWith() %>');" title="<digi:trn>Click here to Share this document</digi:trn>">
														<digi:trn>Share</digi:trn>
													</a>
												</logic:equal>
											</logic:equal>										
											<logic:equal name="documentData" property="needsApproval" value="true">
												<logic:equal name="documentData" property="hasApproveVersionRights" value="true">
													<span style="color: blue;"><strong>|</strong></span>
													<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue;"
													onClick="shareDoc('<%=documentData.getUuid() %>','<%=documentData.getShareWith() %>');" title="<digi:trn>Click here to Share this document</digi:trn>">
													<digi:trn>Approve</digi:trn> </a>
													
													<span style="color: blue"><strong>|</strong></span>
													<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue"
													onClick="rejectDoc('<%=documentData.getUuid() %>','rejectShare');" title="<digi:trn>Click here to Reject this document</digi:trn>">
														<digi:trn>Reject</digi:trn> 
													</a>
												</logic:equal>
											</logic:equal>
											
										</logic:equal>
									</logic:notEmpty>
									
									<logic:equal name="documentData" property="hasUnshareRights" value="true">
										<logic:equal name="documentData" property="isShared" value="true">
											<span style="color: blue;"><strong>|</strong></span>
											<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue;" onClick="unshareDoc('<%=documentData.getUuid() %>');" 
											title="<digi:trn>Click here to UnShare this document</digi:trn>">
												<digi:trn>UnShare</digi:trn>
											</a>
										</logic:equal>									
									</logic:equal>
									
	
									<logic:equal name="documentData" property="hasDeleteRights" value="true">
										<span style="color: blue;"><strong>|</strong></span>
										<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:none; color: blue;"onClick="checkDocumentUuid('<%=documentData.getUuid() %>');deleteRow('<%=documentData.getUuid() %>');"
										title="<digi:trn>Click here to delete this document</digi:trn>"><digi:trn>Delete</digi:trn></a>
									</logic:equal>
								
									<span style="color: blue;"><strong>|</strong></span> 
									<a style="cursor:pointer; text-decoration:none; color: blue;" onClick="showOrgsPanel('<%=documentData.getUuid() %>');" title="<digi:trn>Show Participating Organizations</digi:trn>">
										<digi:trn>Organizations</digi:trn> 
									</a>									
								</td>
							</tr>
						</logic:iterate>
						</tbody>
					</table>
