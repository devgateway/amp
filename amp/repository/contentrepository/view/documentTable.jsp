<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

			
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>

<table id="team_table" bgcolor="white">
						<thead>
							<tr>
								<th><digi:trn key="contentrepository:TableHeader:Select">Select</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Title">Title</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Type">Type</digi:trn></th>
								<th>
                                	<digi:trn key="contentrepository:TableHeader:ResourceName">Resource Name</digi:trn>   
                                </th>
								<th><digi:trn key="contentrepository:TableHeader:Date">Date</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Size">Size (MB)</digi:trn></th>
								<%-- <th><digi:trn key="contentrepository:TableHeader:ContentType">Content Type</digi:trn></th> --%>
								<th><digi:trn key="contentrepository:TableHeader:CmDocType">Document Type</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Description">Description</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Actions">Actions</digi:trn></th>
							</tr>
						</thead>
						<logic:iterate name="documentDataCollection" id="documentData"
							type="org.digijava.module.contentrepository.helper.DocumentData" indexId="counter">
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
								</c:set>>
							</logic:equal>
							<tr>
								<td>
									&nbsp;
								</td>
								<td>
									<bean:write name='documentData' property='title'/>
									<%-- <script type="text/javascript">
										document.write(unescape("<bean:write name='documentData' property='title'/>"));
									</script> --%>
								</td>
								<td>
									<digi:img skipBody="true" src="${documentData.iconPath}" border="0" alt="${ documentData.contentType }" align="absmiddle"/>
									&nbsp; 
								</td>
								<td>
									<c:set var="isUrl" scope="page" value="false" />
									<c:set var="translation">
										<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to</digi:trn>
									</c:set>
									<c:if test="${documentData.webLink != null}" >
										<a  onmouseover="Tip('${translation} ${documentData.webLink}')" onmouseout="UnTip()" 
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
									<bean:write name="documentData" property="fileSize" />
								</td>
								<%-- <td>
									<bean:write name="documentData" property="contentType" />
								</td> --%>
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
										<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
									</c:set> 
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="D<bean:write name='documentData' property='uuid' />"
									onClick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name='documentData' property='uuid' />'"
	
									title="${translation}"><img hspace="2" src= "/repository/contentrepository/view/images/check_out.gif" border=0></a>
								</c:when>
								<c:otherwise>
<!--									<c:set var="translation">-->
<!--										<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to </digi:trn>-->
<!--									</c:set> -->
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="D<bean:write name='documentData' property='uuid' />"
									onclick="window.open('${documentData.webLink }')" 
									onmouseout="UnTip()"
									onmouseover="Tip('<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to </digi:trn> ${documentData.webLink}')"><img hspace="2" src= "/repository/contentrepository/view/images/link_go.gif" border=0/></a>
								</c:otherwise>
								</c:choose>
								

<!--								<c:set var="translation">-->
<!--									<digi:trn key="contentrepository:documentManagerAddVersionHint">Click here to add a new version of this document</digi:trn>-->
<!--								</c:set>-->
								<logic:equal name="documentData" property="hasVersioningRights" value="true">
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="plus<bean:write name='documentData' property='uuid' />"
								onClick="setType('version'); configPanel(0,'${documentData.escapedAmpTitle}','${documentData.escapedAmpDescription}', <%=documentData.getCmDocTypeId() %> ,'<%=documentData.getUuid() %>', ${isUrl});showMyPanel(0, 'addDocumentDiv');"
								title="<digi:trn key="contentrepository:documentManagerAddVersionHint">Click here to add a new version of this document</digi:trn>"><img hspace="2" src= "/repository/contentrepository/view/images/update.gif" border=0></a>
								
								</logic:equal>
								
								<c:set var="translationForWindowTitle">
									<digi:trn key="contentrepository:versionHistoryWindowTitle">Version History</digi:trn>
								</c:set> 
<!--								<c:set var="translation">-->
<!--									<digi:trn key="contentrepository:documentManagerVersionsHint">Click here to see a list of versions for this document</digi:trn>-->
<!--								</c:set> -->
								<logic:equal name="documentData" property="hasShowVersionsRights" value="true">
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="H<bean:write name='documentData' property='uuid' />"
								onClick="showMyPanelCopy(1,'viewVersions'); requestVersions('<%=documentData.getUuid() %>'); setPanelHeader(1, '${translationForWindowTitle}');"
								title="<digi:trn key="contentrepository:documentManagerVersionsHint">Click here to see a list of versions for this document</digi:trn>"><img hspace="2" src= "/repository/contentrepository/view/images/version_history.gif" border=0></a>
								</logic:equal>
<!--								<c:set var="translation">-->
<!--									<digi:trn key="contentrepository:documentManagerMakePublicHint">Click here to make this document public</digi:trn>-->
<!--								</c:set>-->
								<logic:equal name="documentData" property="hasMakePublicRights" value="true">
									<c:if test="${ (!documentData.isPublic)||(!documentData.lastVersionIsPublic) }">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Pub<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.MAKE_PUBLIC %>' ,'<%=documentData.getUuid() %>', true);"
									title="<digi:trn key="contentrepository:documentManagerMakePublicHint">Click here to make this document public</digi:trn>"><img hspace="2" src= "/repository/contentrepository/view/images/make_public.gif" border=0></a>
									</c:if>
								</logic:equal>
								
								
								<logic:equal name="documentData" property="isPublic" value="true">
<!--								<c:set var="translation">-->
<!--									<digi:trn key="contentrepository:documentManagerUnpublishHint">Click here to unpublish this document</digi:trn>-->
<!--								</c:set>-->
								<logic:equal name="documentData" property="hasDeleteRightsOnPublicVersion" value="true">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Priv<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.UNPUBLISH %>', '<%=documentData.getUuid() %>');"
									title="<digi:trn key="contentrepository:documentManagerUnpublishHint">Click here to unpublish this document</digi:trn>"><img hspace="2" src= "/repository/contentrepository/view/images/make_private.gif" border=0></a>
								</logic:equal>
								
								</logic:equal>
								
<!--								<c:set var="translation">-->
<!--									<digi:trn key="contentrepository:documentManagerDeleteHint">Click here to delete this document</digi:trn>-->
<!--								</c:set>-->
								<logic:equal name="documentData" property="hasDeleteRights" value="true">
									<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:underline; color: blue"
									onClick="deleteRow('<%=documentData.getUuid() %>');"
									title="<digi:trn key="contentrepository:documentManagerDeleteHint">Click here to delete this document</digi:trn>"><img hspace="2" src= "/repository/contentrepository/view/images/trash_12.gif" border=0></a>
								</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</table>
