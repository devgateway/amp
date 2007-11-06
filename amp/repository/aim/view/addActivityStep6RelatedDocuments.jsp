<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<digi:instance property="aimEditActivityForm" />
								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:DocumentsRelated">Document related to the project</digi:trn>">
										<b><digi:trn key="aim:relatedDocuments">Related Documents</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
									<feature:display name="Content Repository" module="Document Management">
										<tr>
											<td bgColor=#f4f4f2>
												<bean:define toScope="request" id="showRemoveButton" value="true" />
												<bean:define toScope="request" id="documentsType" value="<%=org.digijava.module.aim.helper.ActivityDocumentsConstants.RELATED_DOCUMENTS %>" />
												<bean:define toScope="request" id="versioningRights" value="false" />
												<bean:define toScope="request" id="deleteRights" value="false" />
												<bean:define toScope="request" id="crRights" value="true" />
												<jsp:include page="/repository/contentrepository/view/showSelectedDocumentsDM.jsp"/>
											</td>
										</tr>
										<tr>
											<td>
												<c:set var="documentsType"><%=org.digijava.module.aim.helper.ActivityDocumentsConstants.RELATED_DOCUMENTS%></c:set>
												<html:button  styleClass="buton" property="submitButton" onclick="addDocumentsDM(&apos;${documentsType}&apos;)">
														<digi:trn key="btn:addDocumentsFromRepository">Add Documents From Repository</digi:trn>
												</html:button>
											</td>
											<br />
										</tr>
									</feature:display>
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="documentList">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="documentList"
												id="relLinks" type="org.digijava.module.aim.helper.RelatedLinks">
												<bean:define name="relLinks" id="selDocuments" property="relLink" />
												<tr>
													<td>
														<html:multibox property="selDocs">
															<bean:write name="selDocuments" property="id" />
														</html:multibox>
														<bean:write name="selDocuments" property="title" /> - 

														<c:if test="${!empty selDocuments.fileName}">
							   							<bean:define id="fileName" name="selDocuments" 
															property="fileName" />
														    <%
															int index2;
															String extension = "";
															index2 = ((String)fileName).lastIndexOf(".");	
															if( index2 >= 0 ) {
															   extension = "module/cms/images/extensions/" + 
																((String)fileName).substring(
																index2 + 1,((String)fileName).length()) + ".gif";
															
														    %>
														    <digi:img skipBody="true" src="<%=extension%>" border="0" 
															 align="absmiddle"/>	
															 <%}%>
														</c:if>
															<i>
														<bean:write name="selDocuments" property="fileName" /></i>	<br>
														<field:display name="Document Description" feature="Related Documents">
														<logic:notEmpty name="selDocuments" property="description">
															<b>Desc:</b><bean:write name="selDocuments" property="description" />
														</logic:notEmpty>
														</field:display>
														<field:display name="Document Comment" feature="Related Documents">
															<logic:notEmpty name="selDocuments" property="docComment">
															<br />
															<b><digi:trn key="aim:addActivity:relatedDocuments:comments">Comments</digi:trn>:</b>
															<bean:write name="selDocuments" property="docComment" />
															</logic:notEmpty>
														</field:display>
														<field:display name="Document Date" feature="Related Documents">
														<logic:notEmpty name="selDocuments" property="date">
															<br />
															<b>Date:</b><bean:write name="selDocuments" property="date" />
														</logic:notEmpty>
														</field:display>
														<field:display name="Document Type" feature="Related Documents">
														<logic:notEmpty name="selDocuments" property="docType">
															<bean:define name="selDocuments" property="docType" id="docTypeBean" />
															<br />
															<b>Doc type:</b><bean:write name="docTypeBean" property="value" />
														</logic:notEmpty>
														</field:display>
														<field:display name="Document Language" feature="Related Documents">
														<logic:notEmpty name="selDocuments" property="docLanguage">
															<bean:define name="selDocuments" property="docLanguage" id="docLangBean" />
															<br />
															<b><digi:trn key="aim:addActivity:relatedDocuments:doclang">Doc lang</digi:trn>:</b><bean:write name="docLangBean" property="value" />
														</logic:notEmpty>
														</field:display>
													</td>
												</tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=2 cellPadding=2>
														<tr>
															<field:display name="Add Documents Button" feature="Related Documents">
															<td>
																<html:button  styleClass="buton" property="submitButton" onclick="addDocuments()">
																	<digi:trn key="btn:addDocuments">Add Documents</digi:trn>
																</html:button>															
															</td>
															</field:display>
															<field:display name="Remove Documents Button" feature="Related Documents">
															<td>
																<html:button  styleClass="buton" property="submitButton" onclick="return removeSelDocuments()">
																	<digi:trn key="btn:removeDocuments">Remove Documents</digi:trn>
																</html:button>
															</td>
															</field:display>
														</tr>
													</table>
												</td></tr>
											</table>											
										</logic:notEmpty>
										
										<logic:empty name="aimEditActivityForm" property="documentList">
										<field:display name="Add Documents Button" feature="Related Documents">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<html:button  styleClass="buton" property="submitButton" onclick="addDocuments()">
																<digi:trn key="btn:addDocuments">Add Documents</digi:trn>
														</html:button>

													</td>
												</tr>
											</table>
										</field:display>
										</logic:empty>										
