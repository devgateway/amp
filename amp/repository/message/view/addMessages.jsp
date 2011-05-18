<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/animation-min.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/autocomplete-min.js"/>"></script>


<digi:instance property="messageForm" />
<html:hidden name="messageForm" property="tabIndex"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.charcounter.js"/>"></script>
<style>
<!--

.yui-skin-sam .yui-ac{position:relative;font-family:arial;font-size: 100%}
.yui-skin-sam .yui-ac-input{position:absolute;width:100%;font-size: 100%}
.yui-skin-sam .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.yui-skin-sam .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.yui-skin-sam .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.yui-skin-sam .yui-ac-content ul{margin:0;padding:0;width:100%;}
.yui-skin-sam .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;FONT-SIZE: 100%;}
.yui-skin-sam .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.yui-skin-sam .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}

#statescontainer .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
} 

-->
</style>
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<script langauage="JavaScript">
    var guestText='---<digi:trn jsFriendly="true">Guest</digi:trn>---';
	var messageHelp='<digi:trn>Message Help</digi:trn>';
	var relatedActs='<digi:trn>Type first letter of activity to view suggestions</digi:trn>';
	var extraReceivers='<digi:trn>Type first letter of contact to view suggestions \n or enter email to send message to</digi:trn>';
	var tmHelp='<digi:trn>A user may appear in more than one workspace.\n Be sure to choose the correct workspace and user within the workspace.</digi:trn>';

    function showMessagesHelpTooltip() {
        var div=document.getElementById("createMessagesHelpTooltip");
    	div.style.display = "block";
    }
	function hideMessagesHelpTooltip(){
  		document.getElementById("createMessagesHelpTooltip").style.display = "none";
	}
    
    
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
    var MyContacts=new Array();
	var orphands=new Array();
	var orpIndex = 0; //teams and team members
	var index = 0; //contact
    for(var i=0; i<list.length;i++){
		if(list.options[i].value.indexOf('m')==0 && list.options[i].id.indexOf('t')!=0){
			orphands[orpIndex]=list.options[i];
			orpIndex++;
		}

		if(list.options[i].value.indexOf('c')==0){
        	MyContacts[index]=list.options[i];
        	index++;
        }
    }
    if(orpIndex!=0){
		registerOrphanMember(orphands);
    }

	//add teams and members
  	addUserOrTeam();//fills out the list with teams and members

  	if(index != 0){
        addOption(list,guestText,"guest");
		for(var j=0; j<index; j++){
			list.options.add(MyContacts[j]);
		}
	}
  }
	
  function validate(){
	        var titleSize=document.messageForm.messageName.value.length;
            var descSize=document.messageForm.description.value.length;
            <c:set var="message">
        		<digi:trn>Please enter name </digi:trn>						
            </c:set>
            <c:set var="msg">
        		${fn:replace(message,'\\n',' ')}
            </c:set>
            <c:set var="quote">'</c:set>
            <c:set var="escapedQuote">\'</c:set>
            <c:set var="msgsq">
        		${fn:replace(msg,quote,escapedQuote)}
            </c:set>
		if(titleSize==0){
			alert('${msgsq}');
			return false;
		}else{
        	if(titleSize>50){
         		alert(' You have entered '+titleSize+' symblos but maximum allowed are 50');
				return false;
         	}
         	if(descSize>500){
         		alert(' You have entered '+descSize+' symblos but maximum allowed are 500');
				return false;
         	}
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
    			<c:set var="message">
            	<digi:trn>Please add receivers </digi:trn>						
                </c:set>
                <c:set var="msg">
            	${fn:replace(message,'\\n',' ')}
                </c:set>
                <c:set var="quote">'</c:set>
                <c:set var="escapedQuote">\'</c:set>
                <c:set var="msgsq">
            	${fn:replace(msg,quote,escapedQuote)}
                </c:set>
        		alert('${msgsq}');
        		return false ;
    		}
    	}    	
    	if(list!=null){
        	//check emails
			for(var i = 0; i < list.length; i++) {
				if(list[i].value.match('^'+'c:')){ //starts with c:
					var receiver=list[i].value.substr(2);
					var email=receiver;
					if(receiver.indexOf("<")!=-1){
						email=receiver.substr(receiver.indexOf("<")+1, receiver.indexOf(">")-receiver.indexOf("<")-1); //cut email from "some text <email>"
					}
					
					var pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
					var expression = new RegExp(pattern)
				    if(expression.test(email)!=true){
					    var trn='<digi:trn>Please provide correct email</digi:trn>';
						alert(trn);
					    return false; 
				    }
					
				}
			}

        	
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
    	
    	return true;
	}
	
	function addContact(contact){
		var list = document.getElementById('selreceivers');
	    if (list == null || contact == null || contact.value == null || contact.value == "") {
	      return;
	    }

		var guestVal=contact.value;
			
		if(guestVal.length>0){
            if($("#selreceivers > option[value^='c:']").length==0){
                addOption(list,guestText,"guest");

            }
			addOption(list,guestVal,'c:'+guestVal);
		}	

		contact.value = "";
	}
	
	function addOption(list, text, value){
	    if (list == null) {
	      return;
	    }
	    var option = document.createElement("OPTION");
	    option.value = value;
	    option.text = text;
	    list.options.add(option);
	    return false;
	}	
	// don't remove or change this line!!!
	document.getElementsByTagName('body')[0].className='yui-skin-sam';

</script>

<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#statesAutoComplete ul,
{
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#contactsAutocomplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#statesAutoComplete div{
	padding: 0px;
	margin: 0px; 
}

#contactsAutocomplete div {
	padding: 0px;
	margin: 0px; 
}

#statesAutoComplete,
#contactsAutocomplete {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#statesAutoComplete,contactsAutocomplete {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#statesInput,
#contactInput {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}

#statesAutoComplete {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

-->
</style>

<c:set var="messageType">
    <c:choose>
        <c:when test="${messageForm.tabIndex==1}">
            <digi:trn>Messages</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==2}">
            <digi:trn>Alerts</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==3}">
            <digi:trn>Approvals</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn>Calendar Events</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>
<c:set var="title">
    <c:choose>
        <c:when test="${not empty messageForm.forwardedMsg}">
            
            <c:choose>
                <c:when test="${messageForm.tabIndex==1}">
            <digi:trn>Forward Message</digi:trn>
        </c:when>
                <c:when test="${messageForm.tabIndex==2}">
                    <digi:trn>Forward Alert</digi:trn>
                </c:when>
                <c:when test="${messageForm.tabIndex==3}">
                    <digi:trn>Forward Approvals</digi:trn>
                </c:when>
                <c:otherwise>
                    <digi:trn>Forward Calendar Events</digi:trn>
                </c:otherwise>
            </c:choose>
            
            
        </c:when>
        <c:when test="${messageForm.messageId==null}">
            <digi:trn>Add Message</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn>Edit Message</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>

<digi:form action="/messageActions.do">
    <table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
		<tr>
			<td width="100%">
				<jsp:include page="/repository/aim/view/teamPagesHeader.jsp"  />
			</td>
		</tr>
		<tr>
		<td>
			<table  cellPadding="0" cellSpacing="0" width="780" border="0">
			    <tr>
				   <td width="14">&nbsp;</td>
					<td align="left" vAlign="top" width="750">
						<table cellPadding="5" cellSpacing="0" width="100%">
							<tr>
								<td height="33">
									<span class="crumb">
										<c:set var="translation">
											<digi:trn>Click here to view MyDesktop</digi:trn>
										</c:set>
										<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
											<digi:trn key="aim:portfolio">Portfolio</digi:trn>
										</digi:link>&nbsp;&gt;&nbsp
				                        <digi:link href="/messageActions.do?actionType=gotoMessagesPage&tabIndex=${messageForm.tabIndex}" styleClass="comment"  >
				                             ${messageType}
				                        </digi:link>
				                       &nbsp;&gt;&nbsp; ${title}
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
								<td><digi:errors/></td>
							</tr>
							<tr>
								<td noWrap vAlign="top">
                                   <table class="contentbox_border" width="100%" cellpadding="0" cellspacing="0">
                                      <tr>				
                                         <td align="center" style="padding: 0px 3px 0px 3px;">
                                         	<table width="100%">
                                            	<tr>
                                                	<td  style="height: 5px;"/>
                                                </tr>
                                                <tr>
                                                	<td style="background-color: #CCDBFF;height: 18px;"/>
                                                </tr>
                                            </table>
                                         </td>
                                      </tr>
                                      <tr>				
										<td>
											<table width="100%" cellspacing="1" cellpadding="0"   valign="top">
                                        		 <tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																<table width="100%" cellspacing="0" cellpadding="3" >																				
																	<tr>
																	   <field:display name="Title Text Box" feature="Create Message Form">
																		 <td align="right" width="25%"><digi:trn key="messages:title">Title</digi:trn><font color="red">*</font> </td>
                                                                         <td align="left"><html:text property="messageName" style="width:320px;" styleClass="inp-text" styleId="titleMax"/></td>
																		</field:display>
																	</tr>																																					
																	<tr>
																	   <field:display name="Description Text Box" feature="Create Message Form">
																		<td align="right"><digi:trn key="message:description">Description</digi:trn></td>
                                                                        <td align="left">
                                                                         	<html:textarea name="messageForm" property="description"  rows="20"  styleClass="inp-text" style="width:320px;" styleId="descMax"/>
                                                                         </td>                                                                         
																	 </field:display>
																	</tr>
																	<tr>
																	  <field:display name="Related Activity Dropdown" feature="Create Message Form">
																		<td align="right" nowrap="nowrap"><digi:trn key="message:relatedActivity">Related Activity</digi:trn></td>
																		<td align="left"  nowrap="nowrap">
                                                                            <div>
                                                                                <div id="statesautocomplete" >
                                                                                    <html:text property="selectedAct" name="messageForm" style="width:320px;font-size:100%"  styleId="statesinput" ></html:text>
                                                                                    <img alt="" src="../ampTemplate/images/help.gif" onmouseover="showMessagesHelpTooltip()" onmouseout="hideMessagesHelpTooltip()" align="top" id="myImage"/>
                                                                                    <div id="statescontainer" style="width:320px;z-index: 100"></div>
                                                                                </div>     
                                                                                <div id="createMessagesHelpTooltip" style="display:none; z-index:10; position:absolute; left:400px;  border: 1px solid silver;">
                                                                                    <TABLE WIDTH='200px' BORDER='0' CELLPADDING='0' CELLSPACING='0'>
                                                                                        <TR style="background-color:#376091"><TD style="color:#FFFFFF" nowrap><digi:trn>Message Help</digi:trn></TD></TR>
                                                                                        <TR style="background-color:#FFFFFF"><TD><digi:trn>Type first letter of activity to view suggestions</digi:trn></TD></TR>
                                                                                    </TABLE>
                                                                                </div>
                                                                            </div>
																		</td>
																	  </field:display>
																	</tr>	
                                                                    <tr>
                                                                    	<td align="right" nowrap="nowrap"><digi:trn key="message:priorityLevel">Priority Level</digi:trn></td>
                                                                        <td align="left"> 
                                                                        	<html:select property="priorityLevel" styleClass="inp-text" style="width:140px">                                         
                                                                        		<html:option value="0"><digi:trn>none</digi:trn> </html:option>                                       
                                                                                <html:option value="1"><digi:trn>low</digi:trn> </html:option>
                                                                                <html:option value="2"><digi:trn>Medium</digi:trn> </html:option>
                                                                                <html:option value="3"><digi:trn>Critical</digi:trn> </html:option>																							
                                                                            </html:select>																												                                                																																												
                                                                         </td>
                                                                    </tr> 
																	<tr>
																		 <field:display name="Set Alert Drop down" feature="Create Message Form">
																			<td align="right" valign="top"><digi:trn key="message:setAsAlert">Set as alert</digi:trn></td>
																			<td align="left">
	                                                                        	<html:select property="setAsAlert" styleClass="inp-text" style="width:140px">																							
																					<html:option value="0"><digi:trn>No</digi:trn> </html:option>
																					<html:option value="1"><digi:trn>Yes</digi:trn> </html:option>																																														
																			  	</html:select>																												                                                																																												
																			</td>
																		</field:display>
																      </tr>	
                                                                       <tr>
                                                                       		<field:display name="Recievers" feature="Create Message Form">
																					<td nowrap="nowrap" align="right">
																						<digi:trn>Receivers</digi:trn>
																						<img src="../ampTemplate/images/help.gif" onmouseover="stm([messageHelp,tmHelp],Style[15])" onmouseout="htm()"/>
																					</td>
																                    <td>
																                        <table border="0" >
																                            <tr>
																                                <td valign="top">
																                                   <select multiple="multiple" size="12" id="whoIsReceiver"  class="inp-text" style="width:200px" >
																										<logic:empty name="messageForm" property="teamMapValues">
																											<option value="-1">No receivers</option>
																										</logic:empty>
																										<logic:notEmpty name="messageForm"  property="teamMapValues" >																								
                                                                                                    	    <option value="all" ><digi:trn key="message:AllTeams">All</digi:trn></option>
                                                                                                               	<c:forEach var="team" items="${messageForm.teamMapValues}">
																														<logic:notEmpty name="team" property="members">
																															<option value="t:${team.id}" style="font-weight: bold;background:#CCDBFF;font-size:11px;">---${team.name}---</option>
																                                                			<c:forEach var="tm" items="${team.members}">
																                                                				<option value="m:${tm.memberId}" style="font:italic;font-size:11px;" id="t:${team.id}">${tm.memberName}</option>
																                                                			</c:forEach>
																														</logic:notEmpty>											                                                		
                                                                                                                </c:forEach>
																                                        </logic:notEmpty>
																                                	</select>																                                         
																                                </td>
																                                <td>
																                                  <input type="button" onclick="MyaddUserOrTeam();" style="width:80px;font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
																                                  <br><br>
																                       			  <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="MyremoveUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >	
																                                </td>
																                                <td valign="top">
															                                		<table>
																                                		<tr height="25px">
																                                			<td>
																                                				<div style="width:220px;">
																			                                		<div id="contactsAutocomplete"">
																			                                			<input type="text" id="contactInput" style="width:220px;font-size:100%">																                                			     
																														<div id="contactsContainer" style="width:220px;"></div>																				 
																													</div>																													
																			                                	</div>
																                                			</td>
																                                			<td nowrap="nowrap">
																                                				<html:button property="" onclick="addContact(document.getElementById('contactInput'))">Add</html:button>
																                                				<img src="../ampTemplate/images/help.gif" onmouseover="stm([messageHelp,extraReceivers],Style[15])" onmouseout="htm()"/>
																                                			</td>
																                                		</tr>
																                                		<tr height="75px">
																                                			<td colspan="2">
																                                				<div>
																			                                		<html:select multiple="multiple" styleId="selreceivers" name="messageForm" property="receiversIds"  size="10" styleClass="inp-text" style="width:220px">
																				                                    	<c:if test="${!empty messageForm.receivers}">
																					                                    	<html:optionsCollection name="messageForm" property="receivers" value="value" label="label" />
																					                                    </c:if>                
																				                                    </html:select>
																			                                	</div>
																                                			</td>
																                                		</tr>
																                                	</table>  
																                                </td>
																                            </tr>
																                        </table>
																                    </td>
																                  </field:display>
																				</tr>																																												
																				<tr>
																					<td colspan="2">
																						<table width="100%" >
																							<tr>
																							 <field:display name="Save button" feature="Create Message Form">
																									<td align="right" width="30%">
																										<c:set var="trnSavetBtn">
																											<digi:trn>Save</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSavetBtn }" onclick="save('draft');" class="dr-menu"/>
																									</td>
																							  </field:display>	
																							   <field:display name="Send button" feature="Create Message Form">
                                                                                                     <c:if test="${empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnSendtBtn">
																											<digi:trn>Send</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSendtBtn }" onclick="save('send');" class="dr-menu"/>
																									</td>
                                                                                                    </c:if>
                                                                                               </field:display>
                                                                                                  <c:if test="${not empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnFwdtBtn">
																											<digi:trn>Forward</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnFwdtBtn }" onclick="save('send');" class="dr-menu" />
																									</td>
                                                                                                    </c:if>
                                                                                                   <field:display name="Cancel button" feature="Create Message Form">
																										<td align="left" width="47%">
																											<c:set var="trnCancelBtn">
																												<digi:trn>Cancel</digi:trn>
																											</c:set>
																											<input type="button" value="${trnCancelBtn}" onclick="cancel();" class="dr-menu">																																							
																										</td>
																									</field:display>
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
                       						</tr>
                   						</table>	
               						</td>
       							</tr>
       						</table>
   						</td>
					</tr>
</table>
																						

<script type="text/javascript">
    	var myArray = [
		<c:forEach var="relAct" items="${messageForm.relatedActivities}">
			 "<bean:write name="relAct" filter="true"/>",
		</c:forEach>
	];

	YAHOO.example.ACJSArray = new function() {
		// Instantiate JS Array DataSource
	    this.oACDS2 = new YAHOO.widget.DS_JSArray(myArray);
	    // Instantiate AutoComplete
	    this.oAutoComp2 = new YAHOO.widget.AutoComplete('statesinput','statescontainer', this.oACDS2);
	    this.oAutoComp2.prehighlightClassName = "yui-ac-prehighlight";
	    this.oAutoComp2.useShadow = true;
	    this.oAutoComp2.forceSelection = true;
            this.oAutoComp2.maxResultsDisplayed = myArray.length;
	    this.oAutoComp2.formatResult = function(oResultItem, sQuery) {
	        var sMarkup = oResultItem[0];
	        return (sMarkup);
	    };
	    };

	    var contactsArray= [
	    	<c:forEach var="cont" items="${messageForm.contacts}">
	        	"<bean:write name="cont" filter="true"/>",
	        </c:forEach>
	   ];

	   YAHOO.example.ACJSArray = new function() {
		   	for(var i=0;i<contactsArray.length;i++){
		    	if(contactsArray[i]!= undefined ){
		        	contactsArray[i]=contactsArray[i].replace("&lt;","<");
		            contactsArray[i]=contactsArray[i].replace("&gt;",">");	
		        }
		    }
	        // Instantiate JS Array DataSource
	        this.oACDS2 = new YAHOO.widget.DS_JSArray(contactsArray);
	        // Instantiate AutoComplete
	        this.oAutoComp2 = new YAHOO.widget.AutoComplete('contactInput','contactsContainer', this.oACDS2);
	        this.oAutoComp2.prehighlightClassName = "yui-ac-prehighlight";
	        this.oAutoComp2.useShadow = true;
	        //this.oAutoComp2.forceSelection = true;
	        this.oAutoComp2.maxResultsDisplayed = contactsArray.length;
	        this.oAutoComp2.formatResult = function(oResultItem, sQuery) {
		        var sMarkup = oResultItem[0];
		        sMarkup=sMarkup.replace("<","&lt;");
		        sMarkup=sMarkup.replace(">","&gt;");
		        return (sMarkup);
		    };
	    };
     
        // attach character counters
        $("#titleMax").charCounter(50,{
	format: " (%1"+ " <digi:trn>characters remaining</digi:trn>)",
	pulse: false});

    $("#descMax").charCounter(500,{
		format: " (%1"+ " <digi:trn>characters remaining</digi:trn>)",
		pulse: false
	});

    $("#descMax").bind("paste", function (event) { 
    	var browser=navigator.appName;
    	if(browser=="Microsoft Internet Explorer"){
    		var textThatNeedsToBePasted = window.clipboardData.getData("Text");
    		var desc = document.getElementById('descMax');
    		if(textThatNeedsToBePasted.length + desc.value.length >500){
    			var msg="<digi:trn jsFriendly='true'>You can not exceed 500 symbols</digi:trn>";
    			alert(msg);
    			window.clipboardData.setData("Text",'');
    		}
        }				
	});

    $("#titleMax").bind("paste", function (event) { 
    	var browser=navigator.appName;
    	if(browser=="Microsoft Internet Explorer"){
    		var textThatNeedsToBePasted = window.clipboardData.getData("Text");
    		var title = document.getElementById('titleMax');
    		if(textThatNeedsToBePasted.length + title.value.length >500){
    			var msg="<digi:trn jsFriendly='true'>You can not exceed 50 symbols</digi:trn>";
    			alert(msg);
    			window.clipboardData.setData("Text",'');
    		}
        }
	});

	
</script>
</digi:form>
