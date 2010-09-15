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


<script langauage="JavaScript">
//	function edit(id) {	
//		window.opener.location.href='${contextPath}/message/messageActions.do?actionType=fillTypesAndLevels&editingMessage=true&msgStateId='+id;
//		window.close();	
//	}
	
//	function deleteAlert (id) {
//		messageForm.action="${contextPath}/message/messageActions.do?editingMessage=false&actionType=removeSelectedMessage&msgStateId="+id;
// 		messageForm.target = "_self";
//  		messageForm.submit();
//  		window.opener.location.reload();
//  		window.close();  					
//	}
	
	
</script>

<digi:form action="/messageActions.do">
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
																								<a  title='<c:out value="${senderInfo[1]}"/>' style="color: #05528B; text-decoration:underline;"><c:out value="${senderInfo[0]}"/></a>
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
			</table>
</digi:form>
