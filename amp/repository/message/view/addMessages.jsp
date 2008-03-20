<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:instance property="messageForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script langauage="JavaScript" type="text/javascript">

	function save (){
			messageForm.action="${contextPath}/message/messageActions.do?actionType=addMessage";
  			messageForm.target = "_self";
  			messageForm.submit();	
		 		
	}
	
	function cancel() {
		messageForm.action="${contextPath}/message/messageActions.do?actionType=cancelMessage";
  		messageForm.target = "_self";
  		messageForm.submit();	
	}
	
	function defineRecieverType() {
		var recType=document.getElementsByName('receiverType')[0].value;
		if(recType=='TM') {			
			messageForm.action="${contextPath}/message/messageActions.do?actionType=loadTeamsForTeamMembers";
  			messageForm.target = "_self";
  			messageForm.submit();
		} else {			
			return 	reloadRecievers();	
		}
	}
	
	function reloadRecievers() {
		var recType=document.getElementsByName('receiverType')[0].value;
		messageForm.action="${contextPath}/message/messageActions.do?actionType=loadReceiversList&receiverType="+recType;
  		messageForm.target = "_self";
  		messageForm.submit();
		
	}

</script>

<digi:form action="/messageActions.do">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
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
																add/edit Alert Wizard
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
																			<table width="100%" cellspacing="1" cellpadding="5" bgcolor="#ffffff">
																				<tr>
																					<td align="right"><digi:trn key="messages:name">Alert Name</digi:trn></td>
																					<td align="left"><html:text property="messageName" /></td>
																				</tr>
																				<tr>
																					<td align="right"><digi:trn key="messages:description">description</digi:trn></td>
																					<td align="left"> <html:textarea name="messageForm" property="description"  rows="2" cols="60"/></td>
																				</tr>
																				<!-- 
																				<tr>
																					<td align="right"><digi:trn key="aim:messagetype">Message type</digi:trn></td>
																					<td align="left"> 
																						<c:set var="translation">
																							<digi:trn key="aim:SelectMessageType">Select Message Type</digi:trn>
																						</c:set>
									                                                	<category:showoptions firstLine="${translation}" name="aimMessageForm" property="messageType"  keyName="<%= org.digijava.module.aim.helper.MessageConstants.MESSAGE_TYPE_KEY %>" styleClass="inp-text" />																																										
																					</td>
																				</tr>
																				-->
																				<tr>
																					<td align="right"><digi:trn key="message:priorityLevel">Priority Level</digi:trn></td>
																					<td align="left"> 
																						<html:select property="priorityLevel" styleClass="inp-text">
																							<html:option value="-1"><digi:trn key="message:selectPriorityLevel">Select Priority level </digi:trn></html:option>
																							<html:option value="1"><digi:trn key="message:priorityLeel:low">low</digi:trn> </html:option>
																							<html:option value="2"><digi:trn key="message:priorityLevel:normal">normal</digi:trn> </html:option>
																							<html:option value="3"><digi:trn key="message:priorityLevel:high">high</digi:trn> </html:option>
																							<html:option value="4"><digi:trn key="message:priorityLevel:moderate">moderate</digi:trn> </html:option>
																						</html:select>																												                                                																																												
																					</td>
																				</tr>																			 																			
																					
																				<tr>
																					<td align="right"><digi:trn key="message:RecieverType">Receiver Type</digi:trn></td>
																					<td align="left">
																						<html:select property="receiverType" onchange="defineRecieverType()" styleClass="inp-text">
																							<html:option value="-1">Select Receiver Type</html:option>
																							<html:option value="ALL"><digi:trn key="message:receiverType:all">All</digi:trn> </html:option>
																							<html:option value="TEAM"><digi:trn key="message:receiverType:team">Team</digi:trn> </html:option>
																							<html:option value="TM"><digi:trn key="message:receiverType:teamMember">Team Member</digi:trn> </html:option>
																							<html:option value="USER"><digi:trn key="message:receiverType:user">Users</digi:trn> </html:option>
																						</html:select>
																					</td>
																				</tr>
																				<logic:equal name="messageForm" property="receiverType" value="TM">
																							<tr>
																							<td align="right">Select team</td>
																							<td align="left"> 
																								<html:select property="selectedTeamId" name="messageForm" styleClass="inp-text" onchange="reloadRecievers()">
																									<html:option value="-1">
																										<digi:trn key="message:selectTeam">Select Team</digi:trn>
																									</html:option>
																									<logic:notEmpty name="messageForm" property="teamsForTeamMembers">
																										<bean:define id="rec" name="messageForm" property="teamsForTeamMembers" type="java.util.List"/>
		                                																<html:options collection="rec" property="value" labelProperty="label"/>																																					
																									</logic:notEmpty>					
																								</html:select>
																							</td>
																						</tr>
																				</logic:equal>																				
																				<tr>
																					<td align="right"><digi:trn key="message:Receevers">Receivers</digi:trn></td>
																					<td align="left">
																						<html:select property="receiverId" name="messageForm" styleClass="inp-text">
																							<html:option value="-1">
																								<digi:trn key="message:addReceiver">Select Receiver</digi:trn>
																							</html:option>
																							<logic:notEmpty name="messageForm" property="receivers">
																									<bean:define id="rec" name="messageForm" property="receivers" type="java.util.List"/>
                                																	<html:options collection="rec" property="value" labelProperty="label"/>																																					
																								</logic:notEmpty>																						
																																															
																						</html:select>
																					</td>
																				</tr>																				
																																																							
																				<tr>
																					<td colspan="2">
																						<table width="100%">
																							<tr>
																								<td align="right">
																									<c:set var="trnSavetBtn">
																										<digi:trn key="message:btn:save">save</digi:trn>
																									</c:set> 
																									<input type="button" value="${trnSavetBtn }" onclick="save();" />
																								</td>
																								<td align="left">
																									<c:set var="trnCancelBtn">
																										<digi:trn key="message:btn:cancel">Cancel</digi:trn>
																									</c:set>
																									<input type="button" value="${trnCancelBtn}" onclick="cancel();">																																							
																								</td>
																							</tr>
																						</table>
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