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

<script langauage="JavaScript">

	function validate(){
		if(document.messageForm.messageName.value.length==0){
			alert('Please Enter Name');
			return false;
		}
		return true;
	}

	function save (event){
		if(!validate()){
			return false;
		}
		if(selectUsers(event)){
			messageForm.action="${contextPath}/message/messageActions.do?actionType=addMessage&toDo="+event;
  			messageForm.target = "_self";
  			messageForm.submit();		
		}	 		
	}
	
	
	function cancel() {
		messageForm.action="${contextPath}/message/messageActions.do?actionType=cancelMessage";
  		messageForm.target = "_self";
  		messageForm.submit();	
	}
	
	function selectUsers(event) {
    	var list = document.getElementById('selreceivers');
    	if (event=='send') {
    		if (list == null || list.length==0) {
        		alert('Please add receivers');
        		return false ;
    		}
    	}    	
    	if(list!=null){
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
    	
    return true;
}
	
	function addUserOrTeam(){
		
		var reslist = document.getElementById('whoIsReceiver');
	    var selreceivers=document.getElementById('selreceivers');
	
	    if (reslist == null) {
	        return false;
	    }
	
	    var index = reslist.selectedIndex;
	    if (index != -1) {
	        for(var i = 0; i < reslist.length; i++) {
	            if (reslist.options[i].selected){
	              if(selreceivers.length!=0){
	                var flag=false;
	                for(var j=0; j<selreceivers.length;j++){
	                  if(selreceivers.options[j].value==reslist.options[i].value && selreceivers.options[j].text==reslist.options[i].text){
	                    flag=true;
	                  }
	                }
	                if(!flag){
	                  addOnption(selreceivers,reslist.options[i].text,reslist.options[i].value);
	                }
	              }else{
	                addOnption(selreceivers,reslist.options[i].text,reslist.options[i].value);
	              }
	            }	
	        }
	    }
	    return false;
		
	}
	
	function addOnption(list, text, value){
    if (list == null) {
        return;
    }
    var option = document.createElement("OPTION");
    option.value = value;
    option.text = text;
    list.options.add(option);
    return false;
}
	
	function removeUserOrTeam() {
		var tobeRemoved=document.getElementById('selreceivers');
		if(tobeRemoved==null){
			return;
		}		
		
		for(var i=tobeRemoved.length-1; i>=0; i--){
			if(tobeRemoved.options[i].selected){
				tobeRemoved.options[i]=null;
			}			
		}			
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
													<table width="100%" cellspacing="1" cellpadding="4" bgcolor="#006699" align="left" valign="top">
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
																					<td align="right"><digi:trn key="messages:title">Title</digi:trn><font color="red">*</font> </td>
																					<td align="left" width="90%"><html:text property="messageName" /></td>
																				</tr>
																				<c:if test="${not empty messageForm.forwardedMsg}">
																			    	<tr>
																			    		<td align="right">forwarded Message:</td>
																			    		<td >
																			    			<table width="75%" align="center" border="0" style="border:1px solid; border-color: #484846;">
																								<tr>
																									<td width="10%"><digi:trn key="message:from">From</digi:trn></td>
																									<td>&nbsp;${messageForm.forwardedMsg.from}</td>
																								</tr>
																								<tr>
																									<td><digi:trn key="message:receiver">Received</digi:trn></td>
																									<td>&nbsp;${messageForm.forwardedMsg.creationDate}</td>
																								</tr>
																								<tr>
																									<td><digi:trn key="message:subject">Subject</digi:trn> </td>
																									<td>${messageForm.forwardedMsg.name}																						
																									</td>
																								</tr>
																								<tr>
																									<td>To:</td>
																									<td>
																										<c:forEach var="receiver" items="${messageForm.forwardedMsg.receivers}"> ${receiver} ,&nbsp;</c:forEach>
																									</td>
																								</tr>
																								<tr>
																									<td><digi:trn key="message:msgDetails">Message Details</digi:trn> </td>
																									<td>&nbsp;${messageForm.forwardedMsg.description}</td>
																								</tr>
																							</table>
																			    		</td>																        	
													                            	</tr>
													                            </c:if>	
																				<tr>
																					<td align="right"><digi:trn key="messages:description">description</digi:trn></td>
																					<td align="left"> <html:textarea name="messageForm" property="description"  rows="2" cols="60"/></td>
																				</tr>																				
																				<tr>
																					<td align="right"><digi:trn key="message:priorityLevel">Priority Level</digi:trn></td>
																					<td align="left"> 
																						<html:select property="priorityLevel" styleClass="inp-text">
																							<html:option value="-1"><digi:trn key="message:selectPriorityLevel">Select Priority level </digi:trn></html:option>
																							<html:option value="1"><digi:trn key="message:priorityLeel:low">low</digi:trn> </html:option>
																							<html:option value="2"><digi:trn key="message:priorityLevel:medium">Medium</digi:trn> </html:option>
																							<html:option value="3"><digi:trn key="message:priorityLevel:critical">Critical</digi:trn> </html:option>																							
																						</html:select>																												                                                																																												
																					</td>
																				</tr>					
																					<td align="right"><digi:trn key="message:setAsAlert">Set as alert</digi:trn></td>
																					<td align="left"> 
																						<html:select property="setAsAlert" styleClass="inp-text">																							
																							<html:option value="2"><digi:trn key="message:no">No</digi:trn> </html:option>
																							<html:option value="1"><digi:trn key="message:yes">Yes</digi:trn> </html:option>																																														
																						</html:select>																												                                                																																												
																					</td>													 																			
																				<tr>
																				</tr>															
																				<tr>
																					<td nowrap="nowrap" valign="top" align="right"><digi:trn key="message:Receevers">Receivers</digi:trn></td>
																                    <td>
																                        <table border="0" style="border:1px solid; border-color: #484846;">
																                            <tr>
																                                <td valign="top">
																                                    <table border="0" width="100%">																                                        
																                                        <tr>
																                                            <td>
																                                              <select multiple="multiple" size="5" id="whoIsReceiver" >
																												<logic:empty name="messageForm" property="teamMapValues">
																													<option value="-1">No receivers</option>
																												</logic:empty>
																												<logic:notEmpty name="messageForm"  property="teamMapValues" >																								
																													<c:forEach var="team" items="${messageForm.teamMapValues}">
																														<logic:notEmpty name="team" property="members">
																															<option value="t:${team.id}" style="font:italic;color:black">---${team.name}---</option>
																                                                			<c:forEach var="tm" items="${team.members}">
																                                                				<option value="m:${tm.memberId}" style="font:italic;color:grey" >${tm.memberName}</option>
																                                                			</c:forEach>
																														</logic:notEmpty>											                                                		
																                                                	</c:forEach>
																                                                </logic:notEmpty>
																                                            </select>
																                                            </td>
																                                        </tr>
																                                    </table>
																                                </td>
																                                <td>
																                                  <input type="button" onclick="addUserOrTeam();" style="font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
																                                </td>
																                                <td valign="top">
																                                    <table border="0" width="100%" cellpadding="0">																                                       
																                                        <tr>
																                                            <td nowrap="nowrap" valign="top">
																                                                <table border="0" width="100%">																                                                   
																                                                    <tr>
																                                                        <td valign="top">																
																                                                            <html:select multiple="multiple" styleId="selreceivers" name="messageForm" property="receiversIds"  size="5" style="width:200px">
																                                              					<c:if test="${!empty messageForm.receivers}">
																	                                                              	<html:optionsCollection name="messageForm" property="receivers" value="value" label="label" />
																	                                                              </c:if>                
																                                                            </html:select>
																                                                        </td>
																                                                        <td valign="top">
																                                                            <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="removeUserOrTeam()" value="<digi:trn key="message:rmbtn">Remove</digi:trn>" >
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
																				</tr>																											
																				<tr>
																					<td colspan="2">
																						<table width="100%" >
																							<c:if test="${not empty messageForm.forwardedMsg}">
																								<tr>
																									<td align="center">
																										<c:set var="trnFwdtBtn">
																											<digi:trn key="message:btn:fwd">forward</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnFwdtBtn }" onclick="save('send');" />
																									</td>
																								</tr>
																							</c:if>
																							<c:if test="${empty messageForm.forwardedMsg}">
																								<tr>
																									<td align="right" width="47%">
																										<c:set var="trnSavetBtn">
																											<digi:trn key="message:btn:save">save</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSavetBtn }" onclick="save('draft');" />
																									</td>
																									<td align="center" width="6%">
																										<c:set var="trnSendtBtn">
																											<digi:trn key="message:btn:send">send</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSendtBtn }" onclick="save('send');" />
																									</td>
																									<td align="left" width="47%">
																										<c:set var="trnCancelBtn">
																											<digi:trn key="message:btn:cancel">Cancel</digi:trn>
																										</c:set>
																										<input type="button" value="${trnCancelBtn}" onclick="cancel();">																																							
																									</td>
																								</tr>
																							</c:if>
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