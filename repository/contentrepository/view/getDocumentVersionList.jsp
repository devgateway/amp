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
	
	<table>
						<thead>
							<tr>
								<th><digi:trn key="contentrepository:versionTableHead">Version</digi:trn></th>
								<th><digi:trn key="contentrepository:typeTableHead">Type</digi:trn></th>
								<th><digi:trn key="contentrepository:fileNameTableHead">File Name</digi:trn></th>
								<th><digi:trn key="contentrepository:dateTableHead">Date</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Size">Size (MB)</digi:trn></th>
								<th><digi:trn key="contentrepository:notesTableHead">Notes</digi:trn></th>
								<th><digi:trn key="contentrepository:actionsTableHead">Actions</digi:trn></th>
							</tr>
						</thead>
						<logic:iterate name="crDocumentManagerForm"
							property="otherDocuments" id="documentData"
							type="org.digijava.module.contentrepository.action.DocumentManager.DocumentData">
							
							<tr>
								<td>
									 <bean:write name="documentData" property="versionNumber" />
									 <c:if test="${documentData.isPublic}">
									 	<font color="blue">*</font>
									 </c:if>
								</td>
								<td>
									<digi:img skipBody="true" src="${documentData.iconPath}" border="0" alt="${ documentData.contentType }" align="absmiddle"/>
									<div>&nbsp;</div>
								</td>
								<td>
									 <bean:write name="documentData" property="name" />
								</td>
								<td>
									<bean:write name="documentData" property="calendar" />
								</td>
								<td>
									<bean:write name="documentData" property="fileSize" />
								</td>
								<td>
									<bean:write name="documentData" property="notes" />
									<a name="aDocumentUUID" style="display: none"><bean:write name="documentData" property="uuid" /></a>
								</td>
								<td> 
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
								</c:set>
								
								<a style="cursor:pointer; text-decoration:underline; color: blue"
								onClick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name='crDocumentManagerForm' property='uuid' />'"
								title="${translation }"><img src= "../ampTemplate/images/check_out.gif" border=0></a>
								
								</td>
							</tr>
						</logic:iterate>
					</table>
					* The colored row marks the public version
					<br />
				</logic:notEmpty>
				