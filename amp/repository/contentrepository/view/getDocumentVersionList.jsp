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
								<th><digi:trn key="contentrepository:versionhistory:header:version">Version</digi:trn></th>
								<th><digi:trn key="contentrepository:versionhistory:header:type">Type</digi:trn></th>
								<th><digi:trn key="contentrepository:versionhistory:header:resourcename">Resource Name</digi:trn></th>
								<th><digi:trn key="contentrepository:versionhistory:header:date">Date</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Size">Size (MB)</digi:trn></th>
								<th><digi:trn key="contentrepository:versionhistory:header:notes">Notes</digi:trn></th>
								<th><digi:trn key="contentrepository:versionhistory:header:actions">Actions</digi:trn></th>
							</tr>
						</thead>
						<logic:iterate name="crDocumentManagerForm"
							property="otherDocuments" id="documentData"
							type="org.digijava.module.contentrepository.helper.DocumentData">
							
							<tr>
								<td>
									 <bean:write name="documentData" property="versionNumber" />
								</td>
								<td>
									<digi:img skipBody="true" src="${documentData.iconPath}" border="0" alt="${ documentData.contentType }" align="absmiddle"/>
									<div>&nbsp;</div>
								</td>
								<td>
									<c:choose>
									<c:when test="${documentData.webLink == null}">
										&nbsp;<bean:write name="documentData" property="name" />
									</c:when>
									<c:otherwise>
										<c:set var="translation">
											<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to</digi:trn>
										</c:set>
										<a onmouseover="Tip('${translation} ${documentData.webLink}')" onclick="window.open('${documentData.webLink}')" 
											style="cursor:pointer;  color: blue; font-size: 11px"> 
											<bean:write name="documentData" property="name" />
										</a>
									</c:otherwise>	
									 </c:choose>
									  <c:if test="${documentData.isPublic}">
									 	<font size="3px" color="blue">*</font>
									 </c:if>
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
								
								<c:choose>
									<c:when test="${documentData.webLink == null}">
										<c:set var="translation">
											<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
										</c:set>
										
										<a style="cursor:pointer; text-decoration:underline; color: blue"
										onClick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name='documentData' property='uuid' />'"
										title="${translation }"><img src= "/repository/contentrepository/view/images/check_out.gif" border=0></a>
									</c:when>
									<c:otherwise>
										<c:set var="translation">
											<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to </digi:trn>
										</c:set> 
										<a style="cursor:pointer; text-decoration:underline; color: blue"
										onclick="window.open('${documentData.webLink}')"
										onmouseover="Tip('${translation} ${documentData.webLink}')"><img src= "/repository/contentrepository/view/images/link_go.gif" border=0></a>
									</c:otherwise>
								</c:choose>
								</td>
							</tr>
						</logic:iterate>
					</table>
					* The colored row marks the public version
					<br />
				</logic:notEmpty>
				