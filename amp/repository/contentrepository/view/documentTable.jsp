<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

			<table id="team_table" bgcolor="white">
						<thead>
							<tr>
								<th>
                                <digi:trn key="contentrepository:TableHeader:FileName">File Name</digi:trn>   
                                </th>
								<th><digi:trn key="contentrepository:TableHeader:ResourceTitle">Resource Title</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Date">Date</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:ContentType">Content Type</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Description">Description</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Actions">Actions</digi:trn></th>
							</tr>
						</thead>
						<logic:iterate name="documentDataCollection" id="documentData"
							type="org.digijava.module.contentrepository.action.DocumentManager.DocumentData">
							<%
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

					%>
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
									 <bean:write name="documentData" property="name" />
									 <digi:img skipBody="true" src="<%=iconPath%>" border="0"
									align="absmiddle" />
								</td>
								<td>
									<bean:write name="documentData" property="title" />
								</td>
								<td>
									<bean:write name="documentData" property="calendar" />
								</td>
								<td>
									<bean:write name="documentData" property="contentType" />
									
								</td>
								<td>
									<bean:write name="documentData" property="description" />
									<a name="aDocumentUUID" style="display: none"><bean:write name="documentData" property="uuid" /></a>
								</td>
								<td> 
								<a style="display:none" id="fake1" href="http://www.yahoo.com">FOR_SILK</a>
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
								</c:set> 
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="D<bean:write name='documentData' property='uuid' />"
								onClick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name='documentData' property='uuid' />'"

								title="${translation}"><img src= "../ampTemplate/images/check_out.gif" border=0></a>
								

								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerAddVersionHint">Click here to add a new version of this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasVersioningRights" value="true">
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="plus<bean:write name='documentData' property='uuid' />"
								onClick="setType('version'); configPanel(0,'<%=documentData.getTitle() %>','<%=documentData.getDescription() %>','<%=documentData.getUuid() %>');showMyPanel(0, 'addDocumentDiv');"
								title="${translation}"><img src= "../ampTemplate/images/update.gif" border=0></a>
								
								</logic:equal>
								
								<c:set var="translationForWindowTitle">
									<digi:trn key="contentrepository:versionHistoryWindowTitle">Version History</digi:trn>
								</c:set> 
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerVersionsHint">Click here to see a list of versions for this document</digi:trn>
								</c:set> 
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="H<bean:write name='documentData' property='uuid' />"
								onClick="showMyPanelCopy(1,'viewVersions'); requestVersions('<%=documentData.getUuid() %>'); setPanelHeader(1, '${translationForWindowTitle}' +' - '+ '<%= documentData.getTitle() %>');"
								title="${translation}"><img src= "../ampTemplate/images/version_history.gif" border=0></a>
								
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerMakePublicHint">Click here to make this document public</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasMakePublicRights" value="true">
									<c:if test="${ (!documentData.isPublic)||(!documentData.lastVersionIsPublic) }">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Pub<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.MAKE_PUBLIC %>' ,'<%=documentData.getUuid() %>', true);"
									title="${translation}"><img src= "../ampTemplate/images/make_public.gif" border=0></a>
									</c:if>
								</logic:equal>
								
								
								<logic:equal name="documentData" property="isPublic" value="true">
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerUnpublishHint">Click here to unpublish this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasDeleteRightsOnPublicVersion" value="true">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Priv<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.UNPUBLISH %>', '<%=documentData.getUuid() %>');"
									title="${translation}"><img src= "../ampTemplate/images/make_private.gif" border=0></a>
								</logic:equal>
								
								</logic:equal>
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDeleteHint">Click here to delete this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasDeleteRights" value="true">
									<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:underline; color: blue"
									onClick="deleteRow('<%=documentData.getUuid() %>');"
									title="${translation}"><img src= "../ampTemplate/images/trash_12.gif" border=0></a>
								</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</table>
