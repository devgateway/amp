<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimRelatedLinksForm" />
<digi:context name="digiContext" property="context" />

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
	<td valign="top" align="left" width="100%">
		<jsp:include page="teamPagesHeader.jsp"  />
	<td>
</tr>
<tr>
	<td>
		<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
			<tr>
				<td class=r-dotted-lg width=14>&nbsp;</td>
				<td align=left class=r-dotted-lg valign="top" width=750>
					<table cellPadding=5 cellspacing="0" width="100%">
						<tr>
							<td height=33><span class=crumb>
								<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:portfolio">
								Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:trn key="aim:viewAllDocument">
									All Documents
								</digi:trn>
								</span>
							</td>
						</tr>
						<tr>
							<td height=16 valign="center" width=650><span class=subtitle-blue>
								<digi:trn key="aim:relatedDocumentsAndLinks">
									Related Documents and Links
								</digi:trn></span>
							</td>
						</tr>
						<tr>
							<td noWrap width=650 vAlign="top">
									<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2 height="20">
												<table border="0" cellpadding="0" cellspacing="0" width="100%" height="20">
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width="100%" height="20" align="center">
															<digi:trn key="aim:relatedDocumentsAndLinks">
																Related Documents and Links
															</digi:trn>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border valign="top">
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr bgColor=#f4f4f2>
														<td valign="left" align="left" width="50%">
															<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">

									                           	<TR bgcolor="#F4F4F2" height="17"> 

									                              	<TD bgcolor="#C9C9C7" class="box-title">

									                              			&nbsp;

																			<digi:trn key="aim:documents">Documents</digi:trn>

																	</TD>

										                            <TD>

										                            	<IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17">

										                            </TD>

									   	                        </TR>

					      	                    			</TABLE>
														</td>
													</tr>
													<logic:empty name="aimRelatedLinksForm" property="relatedLinks">

													<tr bgColor=#f4f4f2>
														<td colspan=2>
															<digi:trn key="aim:noDocsOrLinksPresent">
															No documents or links present
															</digi:trn>
														</td>
													</tr>	
													</logic:empty>
							
													<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">

													<tr><td>
													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2

														class="box-border-nopadding">	

															<tr bgColor=#dddddb>

																<td valign="center" align="center">

																	<b>

																	<digi:trn key="aim:document">

																	Document

																	</digi:trn>

																	</b>

																</td>

																<td bgColor=#dddddb align="center">

																	<b>

																	<digi:trn key="aim:file">

																		File

																	</digi:trn>

																	</b>

																</td>

															</tr>

												

															<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" 
															type="org.digijava.ampModule.aim.helper.Documents">
															<c:if test="${relatedLink.isFile == true}">
															<tr>
																<td bgColor=#f4f4f2>	

																	<jsp:useBean id="docParams" type="java.util.Map" class="java.util.HashMap"/>			
																	<c:set target="${docParams}" property="docId">	
																		<c:out value="${relatedLink.docId}"/>
																	</c:set>											
																	<c:set target="${docParams}" property="actId">
																		<c:out value="${relatedLink.activityId}"/>
																	</c:set>																							
																	<c:set target="${docParams}" property="pageId" value="0"/>
																	<c:set var="translation">
																		<digi:trn key="aim:clickToViewDocumentDetails">Click here view Document Details</digi:trn>
																	</c:set>
																	<digi:link href="/getDocumentDetails.do" name="docParams" title="${translation}" >
																	<bean:write name="relatedLink" property="title" /></digi:link>
																	&nbsp;
																</td>

																<td bgColor=#f4f4f2>	
																
																<logic:notEmpty name="relatedLink" property="fileName">
																	<bean:define name="relatedLink" property="fileName" id="fileName"/>
															    	<%
																		int index2;
																		String extension = "";
																		index2 = ((String)fileName).lastIndexOf(".");	
																		if( index2 >= 0 ) {
																		   extension = "ampModule/cms/images/extensions/" +
																			((String)fileName).substring(
																						index2 + 1,((String)fileName).length()) + ".gif";
																		}
																    %>
																    	<digi:img skipBody="true" src="<%=extension%>" border="0" 
																		 align="absmiddle"/>	
																		<a href="<%=digiContext%>/cms/downloadFile.do?itemId=<bean:write name="relatedLink" property="docId" />">																
																		<i><bean:write name="relatedLink" property="fileName" /></i></a>
																</logic:notEmpty>		
																</td>
															</tr>
															</c:if>
															</logic:iterate>

														</table>

														</td></tr>

														<tr>

															<td>

																&nbsp;

															</td>

														</tr>

														<tr bgColor=#f4f4f2>

															<td valign="left" align="left">

																<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">

										                           	<TR bgcolor="#F4F4F2" height="17"> 

										                              	<TD bgcolor="#C9C9C7" class="box-title">

										                              			&nbsp;

																				<digi:trn key="aim:links">Links</digi:trn>

																		</TD>

											                            <TD>

											                            	<IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17">

											                            </TD>

										   	                        </TR>

						      	                    			</TABLE>

															</td>

														</tr>

														

														<tr><td>

													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2

														class="box-border-nopadding">	

															<tr bgColor=#dddddb>

																<td valign="center" align="center" >

																	<b>

																	<digi:trn key="aim:document">

																	Document

																	</digi:trn>

																	</b>

																</td>

																<td bgColor=#dddddb align="center">

																	<b>

																	<digi:trn key="aim:link">

																		Link

																	</digi:trn>

																	</b>

																</td>

															</tr>

												

															<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" 

															type="org.digijava.ampModule.aim.helper.Documents">

															<c:if test="${relatedLink.isFile == false}">

															<tr>

																<td bgColor=#f4f4f2>	

																	<jsp:useBean id="docParams2" type="java.util.Map" class="java.util.HashMap"/>			

																	<c:set target="${docParams2}" property="docId">	

																		<c:out value="${relatedLink.docId}"/>

																	</c:set>											

																	<c:set target="${docParams2}" property="actId">

																		<c:out value="${relatedLink.activityId}"/>

																	</c:set>																							

																	<c:set target="${docParams2}" property="pageId" value="0"/>

																	<c:set var="translation">

																		<digi:trn key="aim:clickToViewDocumentDetails">Click here view Document Details</digi:trn>

																	</c:set>

																	<digi:link href="/getDocumentDetails.do" name="docParams2" title="${translation}" >

																	<bean:write name="relatedLink" property="title" /></digi:link>

																	&nbsp;

																</td>

																<td bgColor=#f4f4f2>	

																	<digi:img src="ampModule/aim/images/web-page.gif"/>

																	<a target="_blank" href="<bean:write name="relatedLink" property="url" />">

																	<i><bean:write name="relatedLink" property="url" /></i></a>

																</td>

															</tr>

															</c:if>

															</logic:iterate>

														</table>

														</td></tr>
													</logic:notEmpty>
												</table>
											</td>
										</tr>
										<logic:notEmpty name="aimRelatedLinksForm" property="pages">
											<tr>
												<td>
													<digi:trn key="aim:relLinksPages">
														Pages
													</digi:trn> :
													<logic:iterate name="aimRelatedLinksForm" property="pages" id="pages" 
													type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
														<c:set target="${urlParams1}" property="reset" value="false" />

														<c:if test="${aimRelatedLinksForm.currentPage == pages}">
															<%=pages%>
														</c:if>
														<c:if test="${aimRelatedLinksForm.currentPage != pages}">
															<c:set var="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</c:set>
															<digi:link href="/viewAllDocuments.do" name="urlParams1" title="${translation}" >
																<%=pages%>
															</digi:link>
														</c:if>
														|&nbsp; 
													</logic:iterate>
												</td>
											</tr>
										</logic:notEmpty>											
									</table>
							</td>
						</tr>
						<tr><td>&nbsp;</td></tr>
					</table>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>



