<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="messageForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<style type="text/css">
<!--
.blue {
background: #a5bcf2;
}
.whiteThing {
background: #FFF;
}
-->
</style>

<digi:form action="/messageActions.do">
			<table cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
					<td class="r-dotted-lg" width="10"/>
					<td class="r-dotted-lg" valign="top" align="left">
						<table width="98%" cellspacing="3" cellpadding="1" align="left" valign="top">
						<td/>
						<td>
							<table width="100%" cellspacing="5" cellpadding="3" border="0" valign="top">
								<tr>
									<td width="75%" valign="top">
										<table width="100%" cellspacing="0" cellpadding="0" border="0">
											<tr>
												<td width="100%">
													<table width="100%" cellspacing="0" cellpadding="0" border="0">
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"/>
															<td class="textalb" valign="center" height="20" bgcolor="#006699" align="center">
																${messageForm.messageName}
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"/>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellspacing="1" cellpadding="3" bgcolor="#006699" align="left" valign="top">
														<tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																<table width="95%" border="0" bgcolor="#f4f4f2">
																	<tr>
																		<td/>
																	</tr>
																	<tr>
																		<td>
																			<table width="100%" cellspacing="1" cellpadding="5" bgcolor="#eeeeee">
																				<tr><td colspan="4"></td></tr>
																				<tr>
																					<td colspan="3" align="right">priority</td>
																					<td align="left" bgcolor="#ffffff">
																						<logic:equal name="messageForm" property="priorityLevel" value="0">None</logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="1"><img src="/repository/message/view/images/low.gif" title="low"/></logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="2"><img src="/repository/message/view/images/medium.gif" title="medium"/></logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="3"><img src="/repository/message/view/images/critical.gif" title="Critical"/></logic:equal>																						
																					</td>																					
																				</tr>
																				<tr><td colspan="4"></td></tr>
																				<tr>
																					<td align="left"><b><digi:trn>From</digi:trn></b></td>
																					<td align="left" bgcolor="#ffffff" >
																						<c:set var="senderInfo" value="${fn:split(messageForm.sender,';')}"/>
																						<a  title='<c:out value="${senderInfo[1]}"/>' style="color: #05528B; text-decoration:underline;"><c:out value="${senderInfo[0]}"/></a>
																					</td>
																					<td colspan="2"></td>
																				</tr>
                                                                                <tr><td colspan="5"></td></tr>	
                                                                                <tr>
																					<td align="left"><b><digi:trn>to</digi:trn></b></td>
																					<td align="left" bgcolor="#ffffff">
                                                                                    	<c:if test="${!empty messageForm.receivesrsNameMail}">
                                                                                        	<logic:iterate id="nameMail"  name="messageForm" property="receivesrsNameMail" >
                                                                                          		<a title="${nameMail.teamName}" style="color: #05528B; text-decoration:underline;"><c:out value="${nameMail.userNeme}"/></a>
                                                                                          	</logic:iterate>
                                                                                        </c:if>
																					</td>
																					<td colspan="2"></td>
																				</tr>
																				<tr><td colspan="4"></td></tr>		
																				<tr>
																					<td align="left"><b><digi:trn>Date</digi:trn></b></td>
																					<td align="left" bgcolor="#ffffff">${messageForm.creationDate}</td>
                                                                                    <td colspan="2"></td>
																				</tr>
																				<c:if test="${not empty messageForm.sdmDocument}">
																					<tr><td colspan="5"></td></tr>
																					<tr>
																						<td align="left" nowrap width="10%"><b><digi:trn>Attachments:</digi:trn></b></td>
																						<td align="left" bgcolor="#ffffff">
																							<c:forEach var="item" items="${messageForm.sdmDocument.items}">
																								<div>
																									<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
																									<c:set target="${urlParamsSort}" property="documentId" value="${messageForm.sdmDocument.id}"/>																																														
																									<digi:link module="sdm" href="/showFile.do~activeParagraphOrder=${item.paragraphOrder}" name="urlParamsSort">
																										<img src="/repository/message/view/images/attachment.png" border="0" />
																										${item.contentTitle}
																									</digi:link>
																								</div>
																							</c:forEach>
																						</td>
																						<td colspan="2"></td>
																					</tr>
																				</c:if>
																				<tr><td colspan="5"></td></tr>
																				<c:if test="${not empty messageForm.objectURL}">
																					<tr>
																						<td align="left" nowrap width="10%"><b><digi:trn>object URL</digi:trn></b></td>
																						<td align="left" bgcolor="#ffffff"><a href="${messageForm.objectURL}"><digi:trn key="message:ClickViewDetails">Click here to view details</digi:trn></a></td>
                                                                                        <td colspan="2"></td>
																					</tr>	
																					<tr><td colspan="4"></td></tr>
																				</c:if>																					
                                                                                <tr>																					
                                                                                	<td align="center" colspan="4" >
                                                                                    	<div>${messageForm.description}</div>
                                                                                    </td>
                                                                                </tr>
																				<tr>
																					<td colspan="4" align="center">																		
																						<c:set var="trnCloseBtn"><digi:trn key="aim:btn:close">Close</digi:trn>	</c:set>
																						<input type="button" value="${trnCloseBtn }" onclick="closeWindow()" />
																					</td>                                                                                                                                                                       
																				</tr>																				
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td/>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>	
									</td>
								</tr>
							</table>
						</td>
						<td/>
						</table>
					</td>
					<td width="10"/>
				</tr>
			</table>
</digi:form>
