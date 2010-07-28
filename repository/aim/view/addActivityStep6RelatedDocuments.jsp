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
									
									<feature:display name="Related Documents" module="Document"></feature:display>
									
									<tr><td>
										<field:display name="Add Documents Button" feature="Related Documents">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
													 
														<html:button  styleClass="dr-menu" property="submitButton" onclick="addDocuments()">
																<digi:trn key="btn:addDocuments">Add Documents</digi:trn>
														</html:button>
													 
														
														<feature:display name="Content Repository" module="Document Management">
																<field:display name="Add Documents From Repository Button" feature="Related Documents">											
																<c:set var="documentsType"><%=org.digijava.module.aim.helper.ActivityDocumentsConstants.RELATED_DOCUMENTS%></c:set>												

																
																<html:button  styleClass="dr-menu" property="submitButton" onclick="addDocumentsDM(&apos;${documentsType}&apos;)">
																		<digi:trn key="btn:addDocumentsFromRepository">Add Documents From Repository</digi:trn>
																</html:button>
																</field:display>
														</feature:display>
													</td>
												</tr>
											</table>
										</field:display>
