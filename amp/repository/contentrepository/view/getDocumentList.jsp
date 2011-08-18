 <%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>


<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page"
	type="org.digijava.module.contentrepository.form.DocumentManagerForm" />
	
	<logic:notEmpty name="crDocumentManagerForm" property="otherDocuments">
	<bean:define name="crDocumentManagerForm" property="otherDocuments" id="documentDataCollection" type="java.util.Collection" toScope="request" />
	<jsp:include  page="documentTable.jsp" />
	<%-- <table id="team_table">
						<thead>
							<tr>
								<th>File Name</th>
								<th>Resource Title</th>
								<th>Date</th>
								<th>Content Type</th>
								<th>Description</th>
								<th>Actions</th>
							</tr>
						</thead>
						<logic:iterate name="crDocumentManagerForm"
							property="otherDocuments" id="documentData"
							type="org.digijava.module.contentrepository.helper.DocumentData">
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
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
								</c:set>
								<a style="cursor:pointer; text-decoration:underline; color: blue"
								onClick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name='documentData' property='uuid' />'"

								title="${translation }">[<digi:trn key="contentrepository:documentManagerDownload">D</digi:trn>]</a>

								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerAddVersionHint">Click here to add a new version of this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasVersioningRights" value="true">
								<a style="cursor:pointer; text-decoration:underline; color: blue"
								onClick="setType('version'); configPanel(0,'<%=documentData.getTitle() %>','<%=documentData.getDescription() %>','<%=documentData.getUuid() %>');showMyPanel(0, 'addDocumentDiv');"
								title="${translation }">[<digi:trn key="contentrepository:documentManagerAddVersion">+</digi:trn>]</a>
								</logic:equal>
								
								<logic:equal name="documentData" property="showVersionHistory" value="true">
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerVersionsHint">Click here to see a list of versions for this document</digi:trn>
								</c:set>
								<a style="cursor:pointer; text-decoration:underline; color: blue"
								onClick="showMyPanelCopy(1,'viewVersions'); requestVersions('<%=documentData.getUuid() %>'); setPanelHeader(1, '${translationForWindowTitle}' +' - '+ '<%= documentData.getTitle() %>');"
								title="${translation }">[<digi:trn key="contentrepository:documentManagerVersions">H</digi:trn>]</a>
								</logic:equal>
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDeleteHint">Click here to delete this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasDeleteRights" value="true">
									<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:underline; color: blue"
									onClick="deleteRow('<%=documentData.getUuid() %>');"
									title="${translation }">[<digi:trn key="contentrepository:documentManagerDelete">Del</digi:trn>]</a>
								</logic:equal>
								
								</td>
							</tr>
						</logic:iterate>
					</table> --%>
					</div>
					<br />
				</logic:notEmpty>
				