<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/aim"prefix="aim"%>

<digi:context name="digiContext" property="context"/>
<digi:instance property="addressbookForm"/>
<div id="popin" style="display: none">
    <div id="popinContent" class="content">
    </div>
</div>

<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>


	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/yahoo-dom-event.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/connection-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/element-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/yahoo-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/datatable-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/json-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/event-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/paginator-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/container-min.js"></script>
   

	
<script type="text/javascript">
    <!--

    YAHOO.namespace("YAHOO.amp");

    var myPanel = new YAHOO.widget.Panel("newpopins", {
        width:"600px",
        fixedcenter: true,
        constraintoviewport: false,
        underlay:"none",
        close:true,
        visible:false,
        modal:true,
        draggable:true,
        context: ["showbtn", "tl", "bl"]
    });
    var panelStart=0;
    var checkAndClose=false;
    function initOrganizationScript() {
        var msg='\n<digi:trn>Add Organizations</digi:trn>';
        myPanel.setHeader(msg);
        myPanel.setBody("");
        myPanel.beforeHideEvent.subscribe(function() {
            panelStart=1;
        });

        myPanel.render(document.body);
    }
    -->
</script>

<!-- tabs styles -->
<style type="text/css">
  .mask {
        -moz-opacity: 0.8;
        opacity:.80;
        filter: alpha(opacity=80);
        background-color:#2f2f2f;
    }

    #popin .content {
        overflow:auto;
        height:455px;
        background-color:#ffffff;
        padding:10px;
    }

#tabs {
	font-family: Arial,Helvetica,sans-serif;
	font-size: 8pt;
	clear: both;
	text-align: center;
}

#tabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#tabs li { 
	 float: left;
}



#tabs a, #tabs span { 
	font-size: 8pt;
}

#tabs ul li a { 
	background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs ul li span a { 
	background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li span a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs a:hover {
    background: #455786 url(/TEMPLATE/ampTemplate/images/tableftcornerhover.gif) left top no-repeat;  
}

#tabs a:hover span {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}
#tabs a:hover div {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}

#tabs a.active {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 4px 10px 4px 10px;
	text-decoration: none;
	color: #333;
}

#tabs a.active:hover {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 6px 10px 6px 10px;
	text-decoration: none;
	color: #333;
}


#subtabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#subtabs li {
	float: left;
	padding: 0px 4px 0px 4px;
}

#subtabs a, #subtabs span { 
	font-size: 8pt; 
}

#subtabs a {
}

#subtabs ul li span {
	text-decoration: none;
}

#subtabs ul li div span {
	text-decoration: none;
}

#subtabs {
	text-align: center;
	font-family:Arial,Helvetica,sans-serif;
	font-size: 8pt;
	padding: 2px 4px 2px 4px;
	background-color:#CCDBFF;
}

#main {
	clear:both;
	text-align: left;
	border-top: 2px solid #222E5D;
	border-left: 1px solid #666;
	border-right: 1px solid #666;
	padding: 2px 4px 2px 4px;
}
html>body #main {
	width:742px;
}

#mainEmpty {
	border-top: 2px solid #222E5D;
	width: 750px;
	clear:both;
}
html>body #mainEmpty {
	clear:both;
	width:752px;
}

</style>

<script type="text/javascript">

    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
        window.location.reload();
    }


    var responseSuccess = function(o){
        /* Please see the Success Case section for more
         * details on the response object's properties.
         * o.tId
         * o.status
         * o.statusText
         * o.getResponseHeader[ ]
         * o.getAllResponseHeaders
         * o.responseText
         * o.responseXML
         * o.argument
         */
        var response = o.responseText;
        var content = document.getElementById("popinContent");
        //response = response.split("<!")[0];
        content.innerHTML = response;
        //content.style.visibility = "visible";

        showContent();
    }

    var responseFailure = function(o){
        // Access the response object's properties in the
        // same manner as listed in responseSuccess( ).
        // Please see the Failure Case section and
        // Communication Error sub-section for more details on the
        // response object's properties.
        //alert("Connection Failure!");
    }
    var callback =
        {
        success:responseSuccess,
        failure:responseFailure
    };

     function showContent(){
        var element = document.getElementById("popin");
        element.style.display = "inline";
        if (panelStart < 1){
            myPanel.setBody(element);
        }
        if (panelStart < 2){
            document.getElementById("popin").scrollTop=0;
            myPanel.show();
            panelStart = 2;
        }
        checkErrorAndClose();
    }

     function checkErrorAndClose(){
     	if(checkAndClose==true){
        	if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
            	myPanel.hide();
                panelStart=1;
            }
            checkAndClose=false;
            <digi:context name="addCont" property="context/addressBook.do?actionType=addOrganization"/>
		    document.addressbookForm.action = "<%= addCont %>";
            document.addressbookForm.submit();
        }            
     }

	function checkNumber(number){
	 	var validChars= "0123456789()+ ";
	 	for (var i = 0;  i < number.length;  i++) {
	 		var ch = number.charAt(i);
	  		if (validChars.indexOf(ch)==-1){
	  			alert('enter correct number');	   			
	   			return false;
	  		}
	 	}	 
	 return true;
	}

	function checkPhoneNumberType(type){
	 	var regex='^[a-zA-Z]*$';
	  	if (!type.match(regex)){
	  		alert('only letters are allowed for phone type');	   			
	   		return false;
	  	}	 		 
	 	return true;
	}

	function saveContact(){
		if(validateInfo()){
		    <digi:context name="addCont" property="context/addressBook.do?actionType=saveContact"/>
		    var url="<%= addCont %>";
		    //url+=getParamsData();
		    document.addressbookForm.action = url;
		    document.addressbookForm.target = "_self";
		    document.addressbookForm.submit();
		}
	}

	function validateInfo(){
		if(document.getElementById('name').value==null || document.getElementById('name').value==''){
			alert('Please Enter Name');
			return false;
		}
		if(document.getElementById('lastname').value==null || document.getElementById('lastname').value==''){
			alert('Please Enter lastname');
			return false;
		}
		//check emails. At least one email should exist
		var emails=$("input[id^='email_']");
    	if(emails!=null){
        	for(var i=0;i < emails.length; i++){
                if(emails[i].value==null || emails[i].value==''){
                    alert('Please enter email');
                    return false;
                }
                else{
                    if(emails[i].value.indexOf('@')==-1){
                        alert('<digi:trn jsFriendly="true">Please enter valid email</digi:trn>');
                        return false;
                    }
                }
        	}
    	}
    	//phone shouldn't be empty and should contain valid characters
    	//also if phone type is filled, number should be filled too and vice versa
    	var phoneTypes=$("select[id^='phoneType_']");
    	var phoneNumbers=$("input[id^='phoneNum_']");
    	if(phoneNumbers!=null){ //if number is not null, then type also will not be null
    		for(var i=0;i < phoneNumbers.length; i++){
        		if(phoneTypes[i].value=='0' && phoneNumbers[i].value==''){
            		alert('Please enter phone');
            		return false;
        		}else if(phoneTypes[i].value=='0' && phoneNumbers[i].value!=''){
        			alert('Please select phone type');
        			return false;
        		}else if(phoneTypes[i].value!='0' && phoneNumbers[i].value==''){
        			alert('Please enter phone number');
        			return false;
        		}
    		}
    	}
    	
    	if(phoneNumbers!=null){
        	for(var i=0;i < phoneNumbers.length; i++){
        		/*
            	if(checkNumber(phoneNumbers[i].value)==false || checkPhoneNumberType(phoneTypes[i].value)==false){            		
            		return false;
            	}
            	*/
            	if(checkNumber(phoneNumbers[i].value)==false){
            		return false;
            	}
        	}
    	}
    	//check fax
    	var faxes=$("input[id^='fax_']");
    	if(faxes!=null){
    		for(var i=0;i < faxes.length; i++){
        		if(faxes[i].value==''){
        			alert('Please enter fax');
        			return false;
        		}else if(checkNumber(faxes[i].value)==false){
            		return false;
            	}
        	}
    	}
		return true;
	}

	function closeWindow() { //this function closes organizations popin
		myPanel.hide();
	}
	
    function removeOrgs(){
            <digi:context name="addCont" property="context/addressBook.do?actionType=removeOrganization"/>
		    document.addressbookForm.action = "<%= addCont %>";
		    document.addressbookForm.target = "_self";
		    document.addressbookForm.submit();
        }
           function showPanelLoading(msg){
            myPanel.setHeader(msg);
            var content = document.getElementById("popinContent");
            content.innerHTML = '<div style="text-align: center">' +
                '<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' +
                '<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
                showContent();
        }

        function addNewData(dataName){
            if(notAchievedMaxAllowed(dataName)){
            	<digi:context name="addCont" property="context/addressBook.do?actionType=addNewData"/>
            	var url = "<%=addCont%>&data="+dataName;
            	//url += getParamsData();
    		    document.addressbookForm.action = url;
    		    document.addressbookForm.target = "_self";
    		    document.addressbookForm.submit();
            }        	
        }

        function removeData(propertyType, index){ 
        	<digi:context name="delCont" property="context/addressBook.do?actionType=removeData"/>
        	var url = "<%=delCont%>&dataName="+propertyType+"&index="+index;
		    document.addressbookForm.action = url;
		    document.addressbookForm.target = "_self";
		    document.addressbookForm.submit();
        }

        function notAchievedMaxAllowed(dataName){
            var myArray=null;
            var msg='';
            if(dataName=='email' && $("input[id^='email_']").length==3){
                msg='<digi:trn>Max Allowed Number Of Emails is 3 </digi:trn>'
            	alert(msg);
                return false;
            }else if(dataName=='phone'  && $("input[id^='phoneNum_']").length==3){
            	msg='<digi:trn>Max Allowed Number Of Phones is 3 </digi:trn>'
                alert(msg);
            	return false;
            }else if(dataName=='fax' && $("input[id^='fax_']").length==3){
            	msg='<digi:trn>Max Allowed Number Of Faxes is 3 </digi:trn>'
                alert(msg);
            	return false;
            }
            return true;
        }
        
		// hide loading image
//        addLoadEvent(delBody);
</script>
<jsp:include page="/repository/aim/view/addOrganizationPopin.jsp" flush="true" />
<digi:form action="/addressBook.do?actionType=saveContact" method="post">	
	<table bgColor="#ffffff" cellPadding="5" cellSpacing="1" >
		<tr>
			<td width="14">&nbsp;</td>
			<td align="left" vAlign="top" width="752">
				<table bgcolor="#ffffff" cellPadding="0" cellSpacing="0" width="100%">
					<tr>
						<!-- Start Navigation -->
						<td height="33">
							<span class="crumb">
					           <c:set var="translation">
									<digi:trn>Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/showDesktop.do" styleClass="comment" title="${translation}">
									<digi:trn>Portfolio</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:link href="/addressBook.do?actionType=viewAddressBook" styleClass="comment" title="${translation}">
									<digi:trn>Address Book</digi:trn>
								</digi:link>							
								&nbsp;&gt;&nbsp;
								<digi:trn>Add/Edit Contact</digi:trn>
				              </span>
						</td>
						<!-- End navigation -->
					</tr>
					<tr>
						<td height="100%">
							<DIV id="tabs">
								<UL>
							      	<LI>
							           	<span>
						                	<a href="${contextPath}/aim/addressBook.do?actionType=viewAddressBook&reset=true&tabIndex=1">
						                    	<div title='<digi:trn>Existing Contacts</digi:trn>'>
							                 		<digi:trn>Existing Contacts</digi:trn>
						                        </div>
							                </a>
						                </span>
							        </LI>
									<LI>
							           	<a name="node">
					                	<div>
											<digi:trn>Add New Contact</digi:trn>							
					                    </div>
					                </a>
							        </LI>
								</UL>					
							</DIV>
						</td>
					</tr>
					<tr>
						<td height="100%" width="100%">
							<div id="main">
								<table bgcolor="#ffffff" cellPadding="1" cellSpacing="0" width="100%">
								<tr height="100%">
									<td height="100%" width="100%">
										<table bgcolor="#ffffff" cellPadding="5" cellSpacing="0" width="100%" border="0">
											<tr>
												<td><digi:trn>All fields marked with <font size="2" color="#FF0000">*</font> are required.</digi:trn></td>
											</tr>
											<tr>
												<td noWrap width=100% vAlign="top">
													<table bgcolor="#ffffff" border="0" width="100%">
														<tr>
															<td><digi:errors /></td>
														</tr>
														<tr>
															<td>
																<table>
																	<tr>
																		<td noWrap width=690 vAlign="top">
																			<table bgColor="#ffffff" cellPadding="0" cellSpacing="0"class="box-border-nopadding" width="100%">
																				<tr bgColor="#f4f4f2">
																					<td vAlign="top" width="100%">&nbsp;</td>
																				</tr>
																				<tr bgColor="#ffffff">
																					<td valign="top">
																						<table align="center" bgColor="#f4f4f2" cellPadding="0"	cellSpacing="0" border="0">
																							<tr>
																								<td bgColor="#ffffff" class="box-border" width="680">
																									<table border="0" class="box-border" width="100%">
																										<tr bgColor="#dddddb">
																											<td bgColor="#dddddb" height="20" align="center"colspan="5">
																												<STRONG><digi:trn>Add/Edit Contact</digi:trn></STRONG>
																											</td>
																										</tr>
																										<!-- Page Logic -->
																										<tr>
																											<td width="100%">
																												<table border="0" bgColor="#f4f4f2" width="100%">
																													<tr height="5"><td>&nbsp;</td></tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Title</digi:trn></strong> </td>
																														<td align="left">
                                                            	<c:set var="translation">
                                                              	<digi:trn>Please select from below</digi:trn>
                                                              </c:set>
                                                              <category:showoptions multiselect="false" firstLine="${translation}" name="addressbookForm" property="title"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_TITLE_KEY%>" styleClass="selectStyle" outerid="contactTitle"/>
                                                            </td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>First Name</digi:trn></strong><font color="red">*</font></td>
																														<td align="left"><html:text property="name" styleId="name" size="40"/></td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Last Name</digi:trn></strong><font color="red">*</font></td>
																														<td align="left"><html:text property="lastname" styleId="lastname" size="40"/></td>
																													</tr>
																													<tr>
																														<td align="right" valign="top"><strong><digi:trn>Email</digi:trn></strong>
																														<td align="left">
																															<logic:notEmpty name="addressbookForm" property="emails">
																																 <logic:iterate name="addressbookForm" property="emails" id="foo" indexId="ctr">
																																 	<div>
																																 		<html:text name="addressbookForm" property="emails[${ctr}].value" size="40" styleId="email_${ctr}"/>																																 																										 		
																																 			 <a href="javascript:removeData('email',${ctr})"> 
																																		 		<img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/>
																																		 	</a>																	
														
																																 		<c:if test="${ctr==addressbookForm.emailsSize-1}">
																																 			<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      																																		<input id="addEmailBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('email')">
																																 		</c:if>
																																 	</div>																										                    
																												                 </logic:iterate>
																															</logic:notEmpty>
																															<logic:empty name="addressbookForm" property="emails">
																																<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      																															<input id="addEmailBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('email')">
																															</logic:empty>
																														</td>
																													</tr>
																													<tr>
																														<td align="right"><strong><digi:trn>Function</digi:trn></strong></td>
																														<td align="left"><html:text property="function" size="40"/></td>
																													</tr>	
																													<tr>
																														<td align="right"><strong><digi:trn>Organization</digi:trn></strong></td>
																														<td align="left"><html:text property="organisationName" size="40"/></td>
																													</tr>
                                                                                                                    <tr>
                                                                                                                    	<td colspan="2" align="center">
                                                                                                                        	<c:choose>
                                                                                                                            	<c:when test="${empty addressbookForm.organizations}">
                                                                                                                                	<aim:addOrganizationButton showAs="popin" refreshParentDocument="false" collection="organizations"  form="${addressbookForm}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
                                                                                                                                </c:when>
                                                                                                                                <c:otherwise>
                                                                                                                                	<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
                                                                                                                                    	<c:forEach var="organization" items="${addressbookForm.organizations}">
                                                                                                                                        	<tr>
                                                                                                                                               <td width="3px">
	                                                                                                                                               	<html:multibox property="selOrgs">
	                                                                                                                                                	<bean:write name="organization" property="ampOrgId" />
	                                                                                                                                                </html:multibox>
                                                                                                                                            	</td>
                                                                                                                                                <td align="left">
                                                                                                                                                	<bean:write name="organization" property="name" />
                                                                                                                                                </td>
                                                                                                                                             </tr>
                                                                                                                                        </c:forEach>
                                                                                                                                        <tr>
                                                                                                                                            <td colspan="2">
                                                                                                                                                 <aim:addOrganizationButton showAs="popin" refreshParentDocument="false" collection="organizations"  form="${addressbookForm}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
                                                                                                                                                 <input type="button" class="dr-menu" onclick="javascript:removeOrgs();" value='<digi:trn>Remove Organization(s)</digi:trn>' />
                                                                                                                                        	</td>
                                                                                                                                    	</tr>
                                                                                                                                	</table>
                                                                                                                            	</c:otherwise>
                                                                                                                        	</c:choose>
                                                                                                                        </td>
                                                                                                                    </tr>
                                                                                                                   	<tr>
																														<td align="right" valign="top" ><strong><digi:trn>Phone Number</digi:trn></strong></td>
																														<td align="left">
																															<logic:notEmpty name="addressbookForm" property="phones">
																																 <logic:iterate name="addressbookForm" property="phones" id="foo" indexId="ctr">
																																 	<div>
																																 		
																																 		<c:set var="translationNone">
																								              				<digi:trn>None</digi:trn>
																								              			</c:set>
																																 		<category:showoptions multiselect="false" firstLine="${translationNone}" name="addressbookForm" property="phones[${ctr}].phoneTypeId"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_PHONE_TYPE_KEY%>" styleClass="selectStyle" outerid="phoneType_${ctr}"/>
																																 		<%--
																																 			<html:text name="addressbookForm" property="phones[${ctr}].phoneType" size="10" styleId="phoneType_${ctr}"/>																															 																																	 	
																																 		--%>	
																												                    	<html:text name="addressbookForm" property="phones[${ctr}].value" size="26" styleId="phoneNum_${ctr}"/>
																												                    	 <a href="javascript:removeData('phone',${ctr})"><img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/></a>
																												                    	<c:if test="${addressbookForm.phonesSize==0 ||  ctr==addressbookForm.phonesSize-1}">
																												                			<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      																																		<input id="addPhoneBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('phone')">    	
																												                    	</c:if>
																																 	</div>																				                    
																												                 </logic:iterate>
																															</logic:notEmpty>
																															<logic:empty name="addressbookForm" property="phones">
																																<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      																															<input id="addEmailBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('phone')">
																															</logic:empty>
																														</td>
																													</tr>	
																													<tr>
																														<td align="right" valign="top"><strong><digi:trn>Fax</digi:trn></strong></td>
																														<td align="left">
																															<logic:notEmpty name="addressbookForm" property="faxes">
																																 <logic:iterate name="addressbookForm" property="faxes" id="foo" indexId="ctr">
																												                    <html:text name="addressbookForm" property="faxes[${ctr}].value" size="40" styleId="fax_${ctr}"/>																												                    																												                    
																												                    <a href="javascript:removeData('fax',${ctr})"><img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/></a>
																												                    <c:if test="${ctr==addressbookForm.faxesSize-1}">
																												                    	<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      																																	<input id="addFaxBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('fax')">
																												                    </c:if>
																												                  <br>
																												                 </logic:iterate>
																															</logic:notEmpty>
																															<logic:empty name="addressbookForm" property="faxes">
																																<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      																															<input id="addEmailBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('fax')">
																															</logic:empty>
																														</td>
																													</tr>
																													<tr>
																														<td align="right" valign="top"><strong><digi:trn>Office Address</digi:trn></strong></td>
																														<td align="left"><html:textarea property="officeaddress" cols="46" rows="3"/></td>
																													</tr>
																													<tr height="5px"><td colspan="2"/></tr>
																													<tr>
																														<td colspan="4" align="center"><html:button property="" styleClass="dr-menu" onclick="saveContact()"><digi:trn>Save</digi:trn></html:button> </td>			
																													</tr>
																												</table>
																											</td>
																										</tr>
																										<!-- end page logic -->
																									</table>
																								</td>
																							</tr>
																							<tr>
																								<td bgColor=#f4f4f2>&nbsp;</td>
																							</tr>
																						</table>
																					</td>
																					<td noWrap width=10 vAlign="top"></td>
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
							</div>
							
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>	
</digi:form>
<script language="JavaScript" type="text/javascript">
    addLoadEvent(initOrganizationScript);
  </script>