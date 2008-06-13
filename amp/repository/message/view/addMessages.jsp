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
<c:set var="title">
    <c:choose>
        <c:when test="${not empty messageForm.forwardedMsg}">
            <digi:trn key="message:ForwardMessage">Forward Message</digi:trn>
        </c:when>
        <c:when test="${messageForm.messageId==null}">
            <digi:trn key="message:AddMessage">Add Message</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn key="message:EditMessage">Edit Message</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>

<digi:form action="/messageActions.do">
    <table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
<tr>
<td width="100%">
<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />
</td>
</tr>
<tr>
<td>
<table  cellPadding=0 cellSpacing=0 width=780 border="0">
    <tr>
   <td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
                        ${title}
                       </span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>							
								${title}						
						</span>
					</td>
				</tr>
				<tr>
	<td noWrap vAlign="top">
			<table width="100%" cellspacing="0" cellpadding="0" border="0"  align="center" valign="top">
				<tr>				
					<td>
						<table width="100%" cellspacing="1" cellpadding="4"  align="left" valign="top">
														<tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																
																			<table width="100%" cellspacing="1" cellpadding="5" >																				
																				<tr>
																					<td align="right" width="25%"><digi:trn key="messages:title">Title</digi:trn><font color="red">*</font> </td>
																					<td align="left"><html:text property="messageName" size="53" styleClass="inp-text"/></td>
																				</tr>																																					
																				<tr>
																					<td align="right"><digi:trn key="messages:description">description</digi:trn></td>
																					<td align="left"> <html:textarea name="messageForm" property="description"  rows="3" cols="50" styleClass="inp-text"/></td>
																				</tr>																				
																				<tr>
																					<td align="right" nowrap><digi:trn key="message:priorityLevel">Priority Level</digi:trn></td>
																					<td align="left"> 
																						<html:select property="priorityLevel" styleClass="inp-text">
																							<html:option value="-1"><digi:trn key="message:selectPriorityLevel">Select Priority level </digi:trn></html:option>
																							<html:option value="1"><digi:trn key="message:priorityLeel:low">low</digi:trn> </html:option>
																							<html:option value="2"><digi:trn key="message:priorityLevel:medium">Medium</digi:trn> </html:option>
																							<html:option value="3"><digi:trn key="message:priorityLevel:critical">Critical</digi:trn> </html:option>																							
																						</html:select>																												                                                																																												
																					</td>
																				</tr>					
																					<td align="right" nowrap><digi:trn key="message:setAsAlert">Set as alert</digi:trn></td>
																					<td align="left"> 
																						<html:select property="setAsAlert" styleClass="inp-text">																							
																							<html:option value="0"><digi:trn key="message:no">No</digi:trn> </html:option>
																							<html:option value="1"><digi:trn key="message:yes">Yes</digi:trn> </html:option>																																														
																						</html:select>																												                                                																																												
																					</td>													 																			
																				<tr>
																				</tr>															
																				<tr>
																					<td nowrap="nowrap" valign="top" align="right"><digi:trn key="message:Receevers">Receivers</digi:trn></td>
																                    <td>
																                        <table border="0" >
																                            <tr>
																                                <td valign="top">
																                                    
																                                              <select multiple="multiple" size="5" id="whoIsReceiver"  class="inp-text" style="width:200px">
																												<logic:empty name="messageForm" property="teamMapValues">
																													<option value="-1">No receivers</option>
																												</logic:empty>
																												<logic:notEmpty name="messageForm"  property="teamMapValues" >																								
                                                                                                                	<c:forEach var="team" items="${messageForm.teamMapValues}">
                                                                                                                    	<logic:notEmpty name="team" property="members">
                                                                                                                        	<option value="t:${team.id}" style="font-weight: bold;background:#f4f4f2;font-size:12px;">${team.name}</option>
                                                                                                                            <c:forEach var="tm" items="${team.members}">
                                                                                                                         		<option value="m:${tm.memberId}" style="font:italic;font-size:11px;" >${tm.memberName}</option>
                                                                                                                            </c:forEach>
                                                                                                                        </logic:notEmpty>											                                                		
                                                                                                                    </c:forEach>
																                                                </logic:notEmpty>
																                                            </select>																                                         
																                                </td>
																                                <td>
																                                  <input type="button" onclick="addUserOrTeam();" style="width:80px;font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
																                                  <br><br>
																                       			  <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="removeUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >	
																                                </td>
																                                <td valign="top">
																                                	<html:select multiple="multiple" styleId="selreceivers" name="messageForm" property="receiversIds"  size="5" styleClass="inp-text" style="width:200px">
																                                    	<c:if test="${!empty messageForm.receivers}">
																	                                    	<html:optionsCollection name="messageForm" property="receivers" value="value" label="label" />
																	                                    </c:if>                
																                                    </html:select>  
																                                </td>
																                            </tr>
																                        </table>
																                    </td>
																				</tr>																											
																				<tr>
																					<td colspan="2">
																						<table width="100%" >
																							
																							
																								<tr>
																									<td align="right" width="30%">
																										<c:set var="trnSavetBtn">
																											<digi:trn key="message:btn:save">save</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSavetBtn }" onclick="save('draft');" />
																									</td>
                                                                                                                                                                                                        <c:if test="${empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnSendtBtn">
																											<digi:trn key="message:btn:send">send</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSendtBtn }" onclick="save('send');" />
																									</td>
                                                                                                                                                                                                        </c:if>
                                                                                                                                                                                                        <c:if test="${not empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnFwdtBtn">
																											<digi:trn key="message:btn:fwd">forward</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnFwdtBtn }" onclick="save('send');" />
																									</td>
                                                                                                                                                                                                        </c:if>
																									<td align="left" width="47%">
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
													</table>
										
								
						
					</td>
					<td width="10"/>
				</tr>
			</table>
                           </td>
                       </tr>
                   </table>
               </td>
       </tr></table>
   </td>
</tr></table>
</digi:form>
