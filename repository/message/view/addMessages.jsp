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

<digi:instance property="messageForm" />
<html:hidden name="messageForm" property="tabIndex"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.charcounter.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<style>
<!--

.yui-skin-sam .yui-ac{position:relative;font-family:arial;font-size:100%;}
.yui-skin-sam .yui-ac-input{position:absolute;width:100%;}
.yui-skin-sam .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.yui-skin-sam .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.yui-skin-sam .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.yui-skin-sam .yui-ac iframe{opacity:0;filter:alpha(opacity=0);padding-right:.3em;padding-bottom:.3em;}
.yui-skin-sam .yui-ac-content ul{margin:0;padding:0;width:100%;}
.yui-skin-sam .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;list-style:none;zoom:1;}
.yui-skin-sam .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.yui-skin-sam .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}


#myContainer .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
}
-->
</style>

<script langauage="JavaScript">
	var messageHelp='<digi:trn>Message Help</digi:trn>';
	var relatedActs='<digi:trn>Type first letter of activity to view suggestions</digi:trn>';

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
        var titleSize=document.messageForm.messageName.value.length;
        var descSize=document.messageForm.description.value.length;
        var msg='<digi:trn jsFriendly="true">Please enter name </digi:trn>';
        if(titleSize==0){
            alert(msg);
            return false;
        }
        if(descSize>500){
            alert(' You have entered '+descSize+' symblos but maximum allowed are 500');
            return false;
        }
	var hiddenField=document.getElementById('myHidden');
	var relActName=document.getElementById('myInput');
	if(relActName.value!=null && relActName.value!='' && (hiddenField.value==null || hiddenField.value=='')){
		alert('Related activity with given name not exists');		
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

	function removeAttachment(attachmentOrder){
		messageForm.action="${contextPath}/message/messageActions.do?actionType=removeAttachment&attachmentOrder="+attachmentOrder;
  		messageForm.target = "_self";
  		messageForm.submit();
	}

	function validateFile(){
		var fileToBeAttached=document.getElementById('fileUploaded');
		if(fileToBeAttached.value==null || fileToBeAttached.value==''){
			var msg='<digi:trn>Please select file to attach</digi:trn>';
			alert(msg);
			return false;
		}
		return true;
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
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
    	
    	return true;
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

#myAutoComplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#myAutoComplete div {
	padding: 0px;
	margin: 0px; 
}

#myAutoComplete,
#myAutoComplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#myAutoComplete {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#myInput,
#myInput2 {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}

#myAutoComplete {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

-->
</style>

<!-- for browse button -->
<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 100px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
-->
</style>

<script langauage="JavaScript">	
	
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn>Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}	
	}

</script>


<c:set var="messageType">
    <c:choose>
        <c:when test="${messageForm.tabIndex==1}">
            <digi:trn key="message:Messages">Messages</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==2}">
            <digi:trn key="message:Alerts">Alerts</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==3}">
            <digi:trn key="message:approvals">Approvals</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn key="message:ebents">Calendar Events</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>
<c:set var="title">
    <c:choose>
        <c:when test="${not empty messageForm.forwardedMsg}">            
            <c:choose>
                <c:when test="${messageForm.tabIndex==1}">
            <digi:trn key="message:ForwardMessage">Forward Message</digi:trn>
        </c:when>
                <c:when test="${messageForm.tabIndex==2}">
                    <digi:trn key="message:ForwardAlert">Forward Alert</digi:trn>
                </c:when>
                <c:when test="${messageForm.tabIndex==3}">
                    <digi:trn key="message:forwardApprovals">Forward Approvals</digi:trn>
                </c:when>
                <c:otherwise>
                    <digi:trn key="message:ForwardEvents">Forward Calendar Events</digi:trn>
                </c:otherwise>
            </c:choose>
            
            
        </c:when>
        <c:when test="${messageForm.messageId==null}">
            <digi:trn key="message:AddMessage">Add Message</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn key="message:EditMessage">Edit Message</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>

<digi:form action="/messageActions.do?actionType=attachFilesToMessage" method="post" enctype="multipart/form-data">
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
								<td height=33>
									<span class=crumb>
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
								<td height="16" vAlign="center" width="571">
									<span class="subtitle-blue">							
											${title}						
									</span>
								</td>
							</tr>
							<tr>
								<td><digi:errors/> </td>
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
                                                                        <td align="left"> <html:textarea name="messageForm" property="description"  rows="3"  styleClass="inp-text" style="width:320px;"  styleId="descMax"/></td>
																	 </field:display>
																	</tr>
																	<tr>
																	  <field:display name="Related Activity Dropdown" feature="Create Message Form">
																		<td align="right" nowrap="nowrap" valign="top"><digi:trn key="message:relatedActivity">Related Activity</digi:trn></td>
																		<td align="left" nowrap="nowrap">
																			<div id="myAutoComplete">
																				<html:text property="relatedActivityName" name="messageForm" styleId="myInput" style="width:320px;font-size:100%"></html:text>
																				<img src="../ampTemplate/images/help.gif" onmouseover="stm([messageHelp,relatedActs],Style[15])" onmouseout="htm()" align="top" id="myImage"/>
																		    	<div id="myContainer" style="width:315px;"></div>																		    	
																		   	</div>
																		   	<html:hidden property="selectedActId" styleId="myHidden"/>											
																		</td>
																	  </field:display>																			
																	</tr>
																	<c:if test="${not empty messageForm.sdmDocument}">
																		<c:forEach var="attachedDoc" items="${messageForm.sdmDocument.items}">
																			<tr>
																				<td/>
																				<td >
																					<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
																					<c:if test="${not empty messageForm.sdmDocument.id}">
																						<c:set target="${urlParamsSort}" property="documentId" value="${messageForm.sdmDocument.id}"/>
																					</c:if>																					
																					<digi:link module="sdm" href="/showFile.do~activeParagraphOrder=${attachedDoc.paragraphOrder}" name="urlParamsSort">
																						<img src="/repository/message/view/images/attachment.png" border="0" />
																						${attachedDoc.contentTitle}
																					</digi:link>
																					<a href="javascript:removeAttachment(${attachedDoc.paragraphOrder})" title="Click Here To Remove Attachment" ><img  src="/repository/message/view/images/trash_12.gif" border=0"/></a>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td/>
																		<td>
																			<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
																				<input id="fileUploaded" name="fileUploaded" type="file" class="file"/>
																			</div>
																			<input type="submit" value="upload" class="dr-menu" align="right" onclick="return validateFile()"/>
																		</td>
																	</tr>
																																		
                                                                    <tr>
                                                                    	<td align="right" nowrap="nowrap"><digi:trn key="message:priorityLevel">Priority Level</digi:trn></td>
                                                                        <td align="left"> 
                                                                        	<html:select property="priorityLevel" styleClass="inp-text" style="width:140px">                                         
                                                                        		<html:option value="0"><digi:trn key="message:priorityLevel:none">none</digi:trn> </html:option>                                       
                                                                                <html:option value="1"><digi:trn key="message:priorityLevel:low">low</digi:trn> </html:option>
                                                                                <html:option value="2"><digi:trn key="message:priorityLevel:medium">Medium</digi:trn> </html:option>
                                                                                <html:option value="3"><digi:trn key="message:priorityLevel:critical">Critical</digi:trn> </html:option>																							
                                                                            </html:select>																												                                                																																												
                                                                         </td>
                                                                    </tr> 
																	<tr>
																		 <field:display name="Set Alert Drop down" feature="Create Message Form">
																			<td align="right" valign="top"><digi:trn>Set As</digi:trn></td>
																			<td align="left"> 
	                                                                            <html:select property="setAs" styleClass="inp-text" style="width:140px">																							
																					<html:option value="message"><digi:trn>Message</digi:trn> </html:option>
																					<html:option value="alert"><digi:trn>Alert</digi:trn> </html:option>
																					<html:option value="approval"><digi:trn>Approval</digi:trn> </html:option>
																			  	</html:select>																												                                                																																												
																			</td>
																		</field:display>
																      </tr>	
                                                                       <tr>
                                                                       		<field:display name="Recievers" feature="Create Message Form">
																					<td nowrap="nowrap" valign="top" align="right"><digi:trn key="message:Receevers">Receivers</digi:trn></td>
																                    <td>
																                        <table border="0" >
																                            <tr>
																                                <td valign="top">
																                                   <select multiple="multiple" size="5" id="whoIsReceiver"  class="inp-text" style="width:200px" >
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
																                                	<html:select multiple="multiple" styleId="selreceivers" name="messageForm" property="receiversIds"  size="5" styleClass="inp-text" style="width:200px">
																                                    	<c:if test="${!empty messageForm.receivers}">
																	                                    	<html:optionsCollection name="messageForm" property="receivers" value="value" label="label" />
																	                                    </c:if>                
																                                    </html:select>  
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
																											<digi:trn key="messages:btn:save">Save</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSavetBtn }" onclick="save('draft');" class="dr-menu"/>
																									</td>
																							  </field:display>	
																							   <field:display name="Send button" feature="Create Message Form">
                                                                                                     <c:if test="${empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnSendtBtn">
																											<digi:trn key="messages:btn:send">Send</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSendtBtn }" onclick="save('send');" class="dr-menu"/>
																									</td>
                                                                                                    </c:if>
                                                                                               </field:display>
                                                                                                  <c:if test="${not empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnFwdtBtn">
																											<digi:trn key="messages:btn:fwd">Forward</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnFwdtBtn }" onclick="save('send');" class="dr-menu" />
																									</td>
                                                                                                    </c:if>
                                                                                                   <field:display name="Cancel button" feature="Create Message Form">
																										<td align="left" width="47%">
																											<c:set var="trnCancelBtn">
																												<digi:trn key="message:btn:cancel">Cancel</digi:trn>
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
 
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/autocomplete.js"/>"></script>
<script type="text/javascript">
	var myArray = [
		<c:forEach var="relAct" items="${messageForm.relatedActivities}">
		 {name: "<bean:write name="relAct" property="name" filter="true"/>",id: <bean:write name="relAct" property="actId" filter="true"/>},
		</c:forEach>     
	];

	YAHOO.example.ItemSelectHandler = function() {
	    // Use a LocalDataSource
	    var oDS = new YAHOO.util.LocalDataSource(myArray);
	    oDS.responseSchema = {fields : ["name", "id"]};

	    // Instantiate the AutoComplete
	    var oAC = new YAHOO.widget.AutoComplete("myInput", "myContainer", oDS);
	    oAC.resultTypeList = false;
	    
	    // Define an event handler to populate a hidden form field
	    // when an item gets selected
	    var myHiddenField = YAHOO.util.Dom.get("myHidden");
	    var myHandler = function(sType, aArgs) {
	        var myAC = aArgs[0]; // reference back to the AC instance
	        var elLI = aArgs[1]; // reference to the selected LI element
	        var oData = aArgs[2]; // object literal of selected item's result data
	        
	        // update hidden form field with the selected item's ID	        
	        myHiddenField.value = oData.id;
	    };	   
	    oAC.itemSelectEvent.subscribe(myHandler);	    

	    return {
	        oDS: oDS,
	        oAC: oAC
	    };
	}();
	
        
    // attach character counters
    $("#titleMax").charCounter(50,{
		format: " (%1"+ " <digi:trn key="message:charactersRemaining">characters remaining</digi:trn>)",
		pulse: false});
    $("#descMax").charCounter(500,{
		format: " (%1"+ " <digi:trn key="message:charactersRemaining">characters remaining</digi:trn>)",
		pulse: false});       

</script>
</digi:form>

<script type="text/javascript">
	initFileUploads();
</script>
