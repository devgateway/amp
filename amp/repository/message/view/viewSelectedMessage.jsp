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






<div class="message"><div class="message_cont">
	<div style="float:right;">Priority: <b>
		<logic:equal name="messageForm" property="priorityLevel" value="0">None</logic:equal>
		<logic:equal name="messageForm" property="priorityLevel" value="1">low</logic:equal>
		<logic:equal name="messageForm" property="priorityLevel" value="2">medium</logic:equal>
		<logic:equal name="messageForm" property="priorityLevel" value="3">Critical</logic:equal>	
		</b><br />
		Date: <b>${messageForm.creationDate}</b></div>
	From: <b>
		<c:set var="senderInfo" value="${fn:split(messageForm.sender,';')}"/>
																					<a  title='<c:out value="${senderInfo[1]}"/>' style="color: #05528B; text-decoration:underline;">
																						<c:out value="${senderInfo[0]}"/>
																					</a>
		
		
		</b><br />
		To: <b>${messageForm.receivesrsNameMail[0].userNeme}</b> (<a href="#" onClick="return false" class="view_all_recipients">view all</a>)<br />

		<div class="msg_all" style="background-color: white; border: 1px solid #EBEBEB; display:none;">
			<table border="0" cellspacing="1" cellpadding="1" width="100%" style="Font-size: 8pt;">
				<tr><td width="50%">
					<b>Member</b>
				</td><td width="50%">
					<b>Workspace</b>
				</td></tr>
				<tr><td colspan=2><hr /></td></tr>
				<c:forEach var="recipient" items="${messageForm.receivesrsNameMail}">
					<tr><td>
						${recipient.userNeme}
					</td><td>
						${recipient.teamName}
					</td></tr>
				</c:forEach>
			</table>
		</div>

<%--
		<table>
			<c:forEach var="recipient" items="${fn:split(messageForm.receiver,';')}">
				<tr><td>
				${recipient}---------
				</td></tr>
			</c:forEach>
		</table>
--%>


<c:if test="${not empty messageForm.objectURL}">

<b><digi:trn key="message:objURL">object URL</digi:trn></b></td>
<a href="${messageForm.objectURL}" target="_blank"><digi:trn key="message:ClickViewDetails">Click here to view details</digi:trn></a>
																				</c:if>


</div>
<div class="message_body">
${messageForm.description}
</div>

	<c:if test="${not empty messageForm.sdmDocument}">
		<%--
		<hr class=hr_3>
		--%>
		<img src="/TEMPLATE/ampTemplate/img_2/ico_attachment.png" width="16" height="16" align=left style="margin-right:3px;"> <b>Attachments</b>:
		<div class="msg_attachments">
			<c:forEach var="item" items="${messageForm.sdmDocument.items}">
					<hr>
					<img src="/TEMPLATE/ampTemplate/img_2/ico_other.gif" align=left style="margin-right:5px;">
					<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
					<c:set target="${urlParamsSort}" property="documentId" value="${messageForm.sdmDocument.id}"/>																																														
					<digi:link module="sdm" href="/showFile.do~activeParagraphOrder=${item.paragraphOrder}" name="urlParamsSort">
						${item.contentTitle}
					</digi:link>
					
				
			</c:forEach>
			</div>
	</c:if>

</div>







<%--


			<table cellspacing="0"  width="100%" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
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
                                                            <td colspan="3" id="demo" nowrap>
                                                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tabrightcorner.gif" align="right" hspace="0"/>
                                                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tableftcorner.gif" align="left" hspace="0"/>
                                                                <div   class="longTab" >
                                                                   ${messageForm.messageName}
                                                                 </div>
                                                            </td>
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
																						<logic:equal name="messageForm" property="priorityLevel" value="1">low</logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="2">medium</logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="3">Critical</logic:equal>																						
																					</td>																					
																				</tr>
																				<tr><td colspan="4"></td></tr>
																				<tr>
																					<td align="left"><b><digi:trn key="message:from">From</digi:trn></b></td>
																					<td align="left" bgcolor="#ffffff" >
																					
																					<c:set var="senderInfo" value="${fn:split(messageForm.sender,';')}"/>
																					<a  title='<c:out value="${senderInfo[1]}"/>' style="color: #05528B; text-decoration:underline;">
																						<c:out value="${senderInfo[0]}"/>
																					</a>
																					</td>
																					<td colspan="2"></td>
																				</tr>
                                                                                <tr><td colspan="5"></td></tr>	
                                                                                <tr>
																					<td align="left"><b><digi:trn key="message:to">to</digi:trn></b></td>
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
																					<td align="left"><b><digi:trn key="message:date">Date</digi:trn></b></td>
																					<td align="left" bgcolor="#ffffff">${messageForm.creationDate}</td>
                                                                                    <td colspan="2"></td>
																				</tr>
																				<tr><td colspan="5"></td></tr>
																				<c:if test="${not empty messageForm.objectURL}">
																					<tr>
																						<td align="left" nowrap width="10%"><b><digi:trn key="message:objURL">object URL</digi:trn></b></td>
                                                                                        <td align="left" bgcolor="#ffffff"><a href="${messageForm.objectURL}" target="_blank"><digi:trn key="message:ClickViewDetails">Click here to view details</digi:trn></a></td>
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
																						<!-- <c:if test="${messageForm.className=='m'}">
																							<c:set var="trnEdittBtn">
																								<digi:trn key="aim:btn:edit">edit</digi:trn>
																							</c:set> 
																							<input type="button" value="${trnEdittBtn }" onclick="edit(${messageForm.msgStateId});" />
																							<c:set var="trnDeletetBtn"><digi:trn key="aim:btn:delete">delete</digi:trn>	</c:set> 
																						<input type="button" value="${trnDeletetBtn }" onclick="deleteAlert(${messageForm.msgStateId});" />
																						</c:if>  --> 																						
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
			</table>--%>

