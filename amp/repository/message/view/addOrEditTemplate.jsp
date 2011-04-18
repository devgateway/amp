<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="messageForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/scripts/messages.js"/>"></script>
<script langauage="JavaScript">
  function MyremoveUserOrTeam(){
  	var orphands=new Array();
    var list = document.getElementById('selreceivers');
	var orpIndex = 0;
    for(var i=0; i<list.length;i++){
      if(list.options[i].value.indexOf('m')==0 && list.options[i].id.indexOf('t')!=0){
         orphands[orpIndex]=list.options[i];
         orpIndex++;
      }
    }
    if(orpIndex!=0){
       registerOrphanMember(orphands);
    }
    removeUserOrTeam();
  }  
  function MyaddUserOrTeam(){
    var list = document.getElementById('selreceivers');
	var orphands=new Array();
	var orpIndex = 0;
    for(var i=0; i<list.length;i++){
      if(list.options[i].value.indexOf('m')==0 && list.options[i].id.indexOf('t')!=0){
         orphands[orpIndex]=list.options[i];
         orpIndex++;
      }
    }
    if(orpIndex!=0){
       registerOrphanMember(orphands);
    }

	//add teams and members
  	addUserOrTeam();//fills out the list with teams and members

  }

	function validate(){
		if(document.messageForm.messageName.value.length==0){
			alert('Please Enter Name');
			return false;
		}
		if(document.getElementById('triggerSelect').value== -1 ){
			alert('Please Select Related Trigger');
			return false;
		}
		return true;
	}

	function save (){
		if(!validate()){
			return false;
		}
		if(selectUsers()){
			messageForm.action="${contextPath}/message/templatesManager.do?actionType=saveTemplate";
  			messageForm.target = "_self";
  			messageForm.submit();		
		}	 		
	}
	
	
	function cancel() {
		messageForm.action="${contextPath}/message/templatesManager.do?actionType=cancel";
  		messageForm.target = "_self";
  		messageForm.submit();	
	}

	function selectUsers() {
    	var list = document.getElementById('selreceivers');
    	if (list == null || list.length==0) {
        	alert('Please add receivers');
        	return false ;
    	}    	    	
    	if(list!=null){
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
    	
    	return true;
	}

</script>
<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#statesautocomplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#statesautocomplete div {
	padding: 0px;
	margin: 0px; 
}



#statesautocomplete,
#statesautocomplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#statesautocomplete {
    z-index:9000; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#statesinput,
#statesinput2 {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}

-->
</style>
<html:hidden name="messageForm" property="templateId"/>
<digi:form action="/templatesManager.do">
			<table width="1000" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
<!--					<td class="r-dotted-lg" width="10"/>-->
<!--					<td class="r-dotted-lg" valign="top" align="left">-->
						<td>
						<table width="100%" cellspacing="0" cellpadding="0" align="left" valign="top">
						<td/>
						<td>
							<table width="100%" cellspacing="0" cellpadding="0" border="0" valign="top">
								<tr>
									<td width="100%" valign="top">
										<table width="100%" cellspacing="0" cellpadding="0">
											<tr>
												<!-- Start Navigation -->
												<td height=33><span class=crumb>
													<c:set var="translation">
														<digi:trn>Click here to goto Admin Home</digi:trn>
													</c:set>
							                        <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
														<digi:trn>Admin Home</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;													
													<c:set var="gotoTemplateAlertsPage">
														<digi:trn>Click here to goto Template Alert Page</digi:trn>
													</c:set>
							                        <digi:link href="/templatesManager.do~actionType=viewTemplates" styleClass="comment" title="${gotoTemplateAlertsPage}" >
														<digi:trn>Templates Manager</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;													
													<digi:trn>Add/Edit Template</digi:trn>													
												</td>
												<!-- End navigation -->
											</tr>											
											<tr>
								               <td height=16 valign="center" width=571 align-center>
									              
								               </td>
							                </tr>
											
											<tr>
												<td width="100%">
													<table width="100%" cellspacing="1" cellpadding="4" align="left" valign="top">
														<tr>
															<td valign="top">
																<table width="100%" border="0" class="inside">
							                                        <tr>			
							                                           <td align="center" class="inside">
							                                         	  <table width="100%" cellpadding="0" cellspacing="0" style="font-size:12px;">
							                                                  <tr>
							                                                	<td style="background-color: #CCDBFF;height: 18px;" align="center"/><span class=subtitle-blue>							
											        <digi:trn><b>Add/Edit Template Wizard</b></digi:trn>						
									              </span>
							                                                  </tr>
							                                              </table>
							                                           </td>
							                                        </tr>
																	<tr>
																		<td valign="top" bgcolor="#f4f4f2" align="center">
																			<table width="100%" cellspacing="3" cellpadding="5" style="font-size:12px;">																				
																				<tr>
																					<td align="right" width="25%"><digi:trn>Template Name</digi:trn><font color="red">*</font> </td>
																					<td align="left" width="90%"><html:text property="messageName" styleClass="inp-text" style="width:600px;"/></td>
																				</tr>																																				
																				<tr>
																					<td align="right"><digi:trn>Text</digi:trn></td>
																					<td align="left"> <html:textarea name="messageForm" property="description" rows="3" cols="75" styleClass="inp-text"/></td>
																				</tr>																				
																				<tr>
																					<td align="right"><digi:trn>related trigger</digi:trn><font color="red">*</font></td>
																					<td align="left"> 
																						<html:select property="selectedTrigger" name="messageForm" styleClass="inp-text" styleId="triggerSelect">
																							<html:option value="-1"><digi:trn>Select from below</digi:trn></html:option>
																							<logic:iterate id="trigger" name="messageForm" property="availableTriggersList">																																															
																								<html:option value="${trigger.value}"><digi:trn>${trigger.label}</digi:trn></html:option>																		
																							</logic:iterate>
																						</html:select> 
																					</td>
																				</tr>													
																				<tr>
																					<td nowrap="nowrap" valign="top" align="right"><digi:trn>Receivers</digi:trn><font color="red">*</font></td>
																                    <td>
																                        <table border="0" >
																                            <tr>
																                                <td valign="top">
																                                    <table border="0" width="100%">																                                        
																                                        <tr>
																                                            <td>
																                                              <select multiple="multiple" size="5" id="whoIsReceiver"  class="inp-text" style="width:200px">
																												<logic:empty name="messageForm" property="teamMapValues">
																													<option value="-1">No receivers</option>
																												</logic:empty>
																												<logic:notEmpty name="messageForm"  property="teamMapValues" >
																												    <option value="all" ><digi:trn key="message:AllTeams">All</digi:trn></option>
																													<c:forEach var="team" items="${messageForm.teamMapValues}">
																														<logic:notEmpty name="team" property="members">
																															<option value="t:${team.id}" style="font-weight: bold;background:#CCDBFF;font-size:11px;">---${team.name}---</option>
																                                                			<c:forEach var="tm" items="${team.members}">
																                                                				<option value="m:${tm.memberId}" id="t:${team.id}" style="font:italic;font-size:11px;">${tm.memberName}</option>
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
																                                  <input type="button" onclick="MyaddUserOrTeam();" style="width:80px;font-family:tahoma;font-size:11px;" value="<digi:trn>Add >></digi:trn>">
																                                  <br><br>
																                       			  <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="MyremoveUserOrTeam()" value="<<<digi:trn>Remove</digi:trn>" >	
																                                </td>
																                                <td valign="top">
																                                    <table border="0" width="100%" cellpadding="0">																                                       
																                                        <tr>
																                                            <td nowrap="nowrap" valign="top">
																                                                <table border="0" width="100%">																                                                   
																                                                    <tr>
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
																                                    </table>
																                                </td>
																                            </tr>
																                        </table>
																                    </td>
																				</tr>																											
																				<tr>
																					<td colspan="2">
																						<table width="100%" >
																							<tr>
																								<td align="right" width="47%">
																									<c:set var="trnSavetBtn">
																										<digi:trn>save</digi:trn>
																									</c:set> 
																									<input type="button" value="${trnSavetBtn }" onclick="save();" />
																								</td>																									
																								<td align="left" width="47%">
																									<c:set var="trnCancelBtn">
																										<digi:trn>Cancel</digi:trn>
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
																		<td>
																			<TABLE width="100%">
																				<TR>
																					<TD COLSPAN="2">
																						<strong><digi:trn>Patterns Reference</digi:trn></strong>
																					</TD>
																				</TR>
																				<TR>
																					<TD nowrap="nowrap" bgcolor="#E9E9E9">
																						<digi:trn> {name} - Stands for the name of the activity &nbsp;</digi:trn>
																						<br />
																					</TD>
																				</TR>
																				<TR>
																					<TD nowrap="nowrap" bgcolor="#E9E9E9">
																						<digi:trn> {Created By} - Stands for the name of the creator of the activity &nbsp;</digi:trn>
																						<br />
																					</TD>
																				</TR>
																				<TR>
																					<TD nowrap="nowrap" bgcolor="#E9E9E9"><digi:trn> {approved by} - Stands for activity approver &nbsp; &nbsp;</digi:trn>
																					<br />
																					</TD>
																				</TR>
																				<TR>
																					<TD nowrap="nowrap" bgcolor="#E9E9E9">
																						<digi:trn> {url} - This will enable you to resolve the URL related to actvity or the new user registeration &nbsp;</digi:trn>
																						<br />
																					</TD>
																				</TR>
																				<TR>
																					<TD nowrap="nowrap" bgcolor="#E9E9E9">
																						<digi:trn> {start date} - Stands for Calendar Event's Start Date &nbsp;</digi:trn>
																						<br />
																					</TD>
																				</TR>
																				<TR>
																					<TD nowrap="nowrap" bgcolor="#E9E9E9">
																						<digi:trn> {end date} - Stands for Calendar Event's End Date &nbsp; &nbsp;</digi:trn>
																						<br />
																					</TD>
																				</TR>
																			</TABLE>
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