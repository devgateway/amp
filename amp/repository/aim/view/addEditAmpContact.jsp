<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/aim" prefix="aim"%>


<digi:context name="digiContext" property="context"/>
<digi:instance property="addressbookForm"/>
<div id="popin" class="invisible-item">
    <div id="popinContent" class="content">
    </div>
</div>

<style type="text/css">
   #popin .content {
        overflow:auto;
        height:500px;
        background-color:#ffffff;
        padding:10px;
    }
 </style>



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
        context: ["showbtn", "tl", "bl"],
        effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5}
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
	 	var validChars= "0123456789-/()+ ";
	 	for (var i = 0;  i < number.length;  i++) {
	 		var ch = number.charAt(i);
	  		if (validChars.indexOf(ch)==-1){
	  			alert('<digi:trn jsFriendly="true">enter correct number</digi:trn>');	   			
	   			return false;
	  		}
	 	}	 
	 return true;
	}

	function checkPhoneNumberType(type){
	 	var regex='^[a-zA-Z]*$';
	  	if (!type.match(regex)){
	  		alert('<digi:trn jsFriendly="true">only letters are allowed for phone type</digi:trn>');	   			
	   		return false;
	  	}	 		 
	 	return true;
	}

	function saveContact(action){
		if(validateInfo() ){
			
		    <digi:context name="addCont" property="context/addressBook.do?"/>
		    var url="${addCont}";

			if(action=='check'){
				url+="actionType=checkForDuplicationContact";
			}
			else{
				if(action=='new'){
					url+="actionType=saveContact";
				}
				else{
					if(action=='cancel'){
						url+="actionType=viewAddressBook";
					}
					else{
						var selectedContactId=$("input[name='contactIdToOverWrite']:checked").val(); 
						if( selectedContactId==undefined){
							alert("<digi:trn>Please select contact to overwrite</digi:trn>");
							return false;
						}
						document.addressbookForm.contactId.value=selectedContactId;
						url+="actionType=saveContact";
						
					}
					
				}
			}
		    //url+=getParamsData();
		    document.addressbookForm.action = url;
		    document.addressbookForm.target = "_self";
		    document.addressbookForm.submit();
		}
	}
	function cancelSave(){
		
	}

	function validateInfo(){
		var msg='';
		if(document.getElementById('name').value==null || document.getElementById('name').value.trim()==''){
			msg='<digi:trn jsFriendly="true">Please Enter Name</digi:trn>'
			alert(msg);
			return false;
		}
		if(document.getElementById('lastname').value==null || document.getElementById('lastname').value.trim()==''){
			msg='<digi:trn jsFriendly="true">Please Enter lastname</digi:trn>'
			alert(msg);
			return false;
		}
		
		if (!notAchievedMaxAllowed('email',4)) {
			return false;
		}
		if (!notAchievedMaxAllowed('phone',4)) {
			return false;
		}
		if (!notAchievedMaxAllowed('fax',4)) {
			return false;
		}
		//check emails. At least one email should exist
		var emails=$("input[id^='email_']");
    	if(emails!=null){
    		// took regex from wicket validator, since we want them to be the same
    		var regex='^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)';
        	for(var i=0;i < emails.length; i++){
                if(emails[i].value==null || emails[i].value==''){
                	msg='<digi:trn jsFriendly="true">Please Enter email</digi:trn>';
            		alert(msg);
                    return false;
                }
                else{
                    if(!emails[i].value.match(regex)){
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
    			if(phoneNumbers[i].value==''){
    				msg='<digi:trn jsFriendly="true">Please Enter phone</digi:trn>';
            		alert(msg);
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
        			msg='<digi:trn jsFriendly="true">Please enter fax</digi:trn>';
            		alert(msg);
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
		
		var responseAddNewDataSuccess = function(o) {
		 	var xml=o.responseXML;
			var property=xml.getElementsByTagName('property')[0];
			var dataName=property.getAttribute("type");
			var index=property.getAttribute("number")-1;
			var collectionName;
			var place;
			var divProperty;
			if(dataName=='email'){
				collectionName="emails";
				place=document.getElementById("emailsPlace");
				divProperty=createProperty(dataName,collectionName,index);
			}
			else{
				if(dataName=='fax'){
					collectionName="faxes";
					place=document.getElementById("faxesPlace");
					divProperty=createProperty(dataName,collectionName,index);
				}
				else{
					collectionName="phones";
					place=document.getElementById("phonesPlace");
                                        var  phoneTypeList= property.childNodes[0];
                                        if(phoneTypeList!=null){
                                         divProperty=createProperty(dataName,collectionName,index,phoneTypeList.childNodes);   
                                        }
                                        else{
                                            divProperty=createProperty(dataName,collectionName,index);
                                        }
						
				}
			}
			
			if(index==0){
				$("#"+dataName+"BtnEmpty").hide(); 
				var newLink=createAddLink(dataName);
				divProperty.appendChild(newLink);
				
			}
			
			place.appendChild(divProperty);

		};
		
		function createProperty(dataName,collectionName,index,phoneTypeList){
				var propertyDiv=document.createElement("div");
	       		propertyDiv.id="div_"+dataName+"_"+index;
	       		var property=document.createElement("input");
				var deletePropertyLink=document.createElement("a");
				property.name=collectionName+"["+index+"].value";
	       		if(dataName=='phone'){
	       			var select=document.createElement("select");
					select.className="inputx insidex address-title";
					select.id="phoneType_"+index;
					select.name="phones["+index+"].phoneTypeId";
					var optionNone=document.createElement("option");
					optionNone.value="0";
					var labelNone=document.createTextNode("<digi:trn>None</digi:trn>");
					optionNone.appendChild(labelNone);
					select.appendChild(optionNone);
                                        if(phoneTypeList!=null)
					for (var i=0; i<phoneTypeList.length; i++) {
						var option=document.createElement("option");
						option.value=phoneTypeList[i].getAttribute("id");
						var label=document.createTextNode(phoneTypeList[i].getAttribute("value"));
						option.appendChild(label);
						select.appendChild(option);
					}
					propertyDiv.appendChild(select);
					property.id="phoneNum_"+index;
	       		}
	       		else{
	       			property.id=dataName+"_"+index;
	       		}
				property.className="inputx insidex";
				property.size="33";
				deletePropertyLink.href="javascript:removeData('"+dataName+"',"+index+")";
				var deleteImg=document.createElement("img");
				deleteImg.src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif";
				deleteImg.alt="delete";
				deleteImg.svpace="2";
				deleteImg.border="0";
				deletePropertyLink.appendChild(deleteImg);
				propertyDiv.appendChild(property);
				propertyDiv.appendChild(deletePropertyLink);
				return propertyDiv;
				
		}
		 
		var responseAddNewDataFailure = function(o) {};
		 
		var addNewDataCallback = {
		  success:responseAddNewDataSuccess,
		  failure:responseAddNewDataFailure
		};
		var responseRemoveNewDataSuccess = function(o) {
			var xml=o.responseXML;
			var property=xml.getElementsByTagName('property')[0];
			var dataName=property.getAttribute("type");
			var index=property.getAttribute("index");
			var collectionName=property.getAttribute("collectionName");
			var div=document.getElementById("div_"+dataName+"_"+index);
			var addAddLink=false;
			if(index==0){
				addAddLink=true;
			}
			var place=div.parentNode;
			$("#"+dataName+"Btn").die();
			place.removeChild(div); 
			
			var count=$("input[name^='"+collectionName+"[']").each(function(index) { 
				     this.parentNode.id="div_"+dataName+"_"+index;
				     this.nextSibling.href="javascript:removeData('"+dataName+"',"+index+")";
				     this.name=collectionName+"["+index+"].value";
				     if(dataName=="phone"){
				    	 this.id="phoneNum_"+index;
				    	 this.previousSibling.id="phoneType_"+index;
						 this.previousSibling.name="phones["+index+"].phoneTypeId";
				     }
				     else{
				    	  this.id=dataName+"_"+index; 
				     } 
				     if(index==0&&addAddLink){
				    	 var newLink=createAddLink(dataName);
						this.parentNode.appendChild(newLink);
				     }
				  });
			if(count.length==0){
				$("#"+dataName+"BtnEmpty").show(); 
			}
		};
		var responseRemoveNewDataFailure = function(o) {};
		
		var removeNewDataCallback = {
				  success:responseRemoveNewDataSuccess,
				  failure:responseRemoveNewDataFailure
		};
		
		function createAddLink(dataName){
			 var newLink=document.createElement("a");
				newLink.id=dataName+"Btn";
				newLink.className="l_mid_b";
				newLink.href="#";
				var label=document.createTextNode("<digi:trn>Add</digi:trn>");
				newLink.appendChild(label);
				$("#"+dataName+"Btn").on('click', 'a', function() {
					addNewData(dataName);
					return false;
					}); 
				return newLink;
		}
		
		var responseRemoveOrganizationsSuccess = function(o) {
			$("select[name='selOrgs']").children().each(function(){
				if (this.selected == true || this.selected == 'selected') {
					$(this).remove();
				}
			})
			
		};
		var responseRemoveOrganizationsFailure = function(o) {};
		
		var removeOrganizationsCallback = {
				  success:responseRemoveOrganizationsSuccess,
				  failure:responseRemoveOrganizationsFailure
		};
		 
	
    function removeOrgs(){
    	<digi:context name="removeOrg" property="context/addressBook.do?actionType=removeOrganization"/>
    	var url = "<%=removeOrg%>";
    	var params='';
    	$("select[name='selOrgs']").children().each(function(){
        	if (this.selected == true || this.selected == 'selected') {
            	params+="&selOrgs="+this.value;
            }
        })
    	YAHOO.util.Connect.asyncRequest('POST',url, removeOrganizationsCallback, params.substring(1));
    	
        }
           function showPanelLoading(msg){
            myPanel.setHeader(msg);
            var content = document.getElementById("popinContent");
            content.innerHTML = '<div style="text-align: center">' +
                '<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' +
                '<digi:trn jsFriendly="true">Loading...</digi:trn><br/><br/></div>';
                showContent();
        }

        function addNewData(dataName){
            if(notAchievedMaxAllowed(dataName,3)){
            	<digi:context name="addCont" property="context/addressBook.do?actionType=addNewData"/>
            	var url = "<%=addCont%>";
            	YAHOO.util.Connect.asyncRequest('POST',url, addNewDataCallback, "data="+dataName);
            }        	
        }

        function removeData(propertyType, index){ 
        	<digi:context name="delCont" property="context/addressBook.do?actionType=removeData"/>
        	var url = "<%=delCont%>";
        	YAHOO.util.Connect.asyncRequest('POST',url, removeNewDataCallback, "dataName="+propertyType+"&index="+index);
        }

        function notAchievedMaxAllowed(dataName,allow){
            var myArray=null;
            var msg='';
            if(dataName=='email' && $("input[id^='email_']").length >= allow){
                msg='<digi:trn jsFriendly="true">Max Allowed Number Of Emails is 3 </digi:trn>'
            	alert(msg);
                return false;
            }else if(dataName=='phone'  && $("input[id^='phoneNum_']").length >= allow){
            	msg='<digi:trn jsFriendly="true">Max Allowed Number Of Phones is 3 </digi:trn>'
                alert(msg);
            	return false;
            }else if(dataName=='fax' && $("input[id^='fax_']").length >= allow){
            	msg='<digi:trn jsFriendly="true">Max Allowed Number Of Faxes is 3 </digi:trn>'
                alert(msg);
            	return false;
            }
            return true;
        }
        
		// hide loading image
//        addLoadEvent(delBody);
</script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<jsp:include page="/repository/aim/view/addOrganizationPopin.jsp"  />
<!-- Individual YUI CSS files --> 

<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css">
<!-- BREADCRUMP START -->
<div class="breadcrump">
	<div class="centering">
		<div class="breadcrump_cont">
			<span class="sec_name"><digi:trn>Address Book</digi:trn></span>
			<span class="breadcrump_sep">|</span><a class="l_sm"><digi:trn>Tools</digi:trn></a>
			<span class="breadcrump_sep"><b>�</b></span>
			<span class="bread_sel"><digi:trn>Address Book</digi:trn></span>
		</div>
	</div>
</div>
<!-- BREADCRUMP END --> 


<table width="1000" align="center" border="0" cellpadding="0" cellspacing="0">	
	<tbody>
		<tr>
			<td valign="top">
				<div id="content" class="yui-skin-sam"> 
					<div id="demo" class="yui-navset">
						<ul class="yui-nav">
							<li>
								<a href="${contextPath}/aim/addressBook.do?actionType=viewAddressBook&reset=true&tabIndex=1">
									<div title='<digi:trn jsFriendly="true">Existing Contacts</digi:trn>'>
										<digi:trn>Existing Contacts</digi:trn>
									</div>
								</a>
							</li>
							<li class="selected" >
								<a>
									<div>
										<c:if test="${not empty addressbookForm.contactId}">
											<digi:trn>Edit Contact</digi:trn>
										</c:if>
										<c:if test="${empty addressbookForm.contactId}">
											<digi:trn>Add New Contact</digi:trn>
										</c:if>
									</div>
								</a>
							</li>
						</ul>
						<div class="yui-content" style="border: 1px solid rgb(208, 208, 208);" >
							<digi:form action="/addressBook.do?actionType=saveContact" method="post">
								<c:set var="readonly">
									<c:choose>
										<c:when	test="${not empty addressbookForm.probablyDuplicatedContacs}">
											true
										</c:when>
										<c:otherwise>
											false
										</c:otherwise>
									</c:choose>
								</c:set>
								<html:hidden property="contactId"/>
								<div class="required_fields t_mid"><digi:trn>All fields marked with</digi:trn> <font size="2" color="#FF0000">*</font> <digi:trn>are required</digi:trn>.</div>
								<hr />
								<div style="background-color:#F8F8F8; border:1px solid #CCCCCC;">
									<table cellspacing="0" cellpadding="0" width="96%" id="config_table" style="margin:10px;">
										<tbody>
											<tr>
											    <td width="50%" valign="top" class="t_mid"><b style="padding-left:5px;"><digi:trn>Title</digi:trn>: </b><br />
													<c:set var="translation">
											       		<digi:trn>Please select from below</digi:trn>
											       	</c:set>
											     	<category:showoptions multiselect="false" firstLine="${translation}" name="addressbookForm" property="title"  keyName="<%= org.digijava.ampModule.categorymanager.util.CategoryConstants.CONTACT_TITLE_KEY%>" styleClass="nputx insidex address-title" outerid="contactTitle"/>
											    </td>
								    			<td valign="top" class="t_mid"><b style="padding-left:5px;"><digi:trn>Organization</digi:trn>: </b><br />
													<c:set var="translationOrgName">
											       		<digi:trn>If organisation doesn't exist in the system, then it can be added in this textbox</digi:trn>
											       	</c:set>
													<html:text property="organisationName"  size="33" styleClass="inputx insidex" title="${translationOrgName}" /> <br />
													
													<table width="100%" cellspacing="1" cellPadding=5 class="added_org_nc">
													<tr>
													<td>
													<html:select  multiple="multiple" property="selOrgs" size="4" style="width: 300px;">
														<logic:notEmpty name="addressbookForm" property="organizations">
															<logic:iterate name="addressbookForm" property="organizations" id="organization" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
																<html:option value="${organization.ampOrgId}" style="font-family: Arial;font-size:11px;">${organization.name}</html:option>
															</logic:iterate>
														</logic:notEmpty>
							                		</html:select>
							                		</td>
							                		<td>
													<aim:addOrganizationButton showAs="popin" refreshParentDocument="false" collection="organizations" form="${addressbookForm}" styleClass="buttonx_sm btn_save">
														<digi:trn>Add Organizations</digi:trn>
													</aim:addOrganizationButton>
													<c:if test="${not empty addressbookForm.organizations}">
														<input type="button" class="buttonx_sm btn_save" onclick="javascript:removeOrgs();"value='<digi:trn jsFriendly="true">Remove Organization(s)</digi:trn>' />
													</c:if>
													</td>
													</tr>
													</table>
													
												</td>
								    			<td valign="top"></td>
								    		</tr>
											<tr>
												<td colspan="2"><hr /></td>
											</tr>
											<tr>
												<td valign="top" class="t_mid"><b style="padding-left:5px;"><digi:trn>Firstname</digi:trn></b><b style="color: rgb(255, 0, 0);">*</b>:<br />
													<html:text property="name" styleId="name" size="33"	styleClass="inputx insidex" readonly="${readonly}" />
												</td>
												<td valign="top" class="t_mid" id="phonesPlace" style="padding-left:5px;"><b><digi:trn>Phone Number</digi:trn>:</b><br />
													<logic:notEmpty name="addressbookForm" property="phones">
														<c:set var="trnadd">
															<digi:trn>Add</digi:trn>
														</c:set>
														<a id="phoneBtnEmpty" href="#" onclick="addNewData('phone');return false;" class="l_mid_b" style="display:none">${trnadd}</a>
														<logic:iterate name="addressbookForm" property="phones" id="foo" indexId="ctr">
															<div id="div_phone_${ctr}">
																<c:set var="translationNone">
																	<digi:trn>None</digi:trn>
																</c:set> 
																<category:showoptions multiselect="false" firstLine="${translationNone}" name="addressbookForm" property="phones[${ctr}].phoneTypeId" keyName="<%= org.digijava.ampModule.categorymanager.util.CategoryConstants.CONTACT_PHONE_TYPE_KEY%>" styleClass="nputx insidex address-title" outerid="phoneType_${ctr}" />
																<html:text name="addressbookForm" property="phones[${ctr}].value" styleId="phoneNum_${ctr}"  size="33" styleClass="inputx insidex" /> 
																<a href="javascript:removeData('phone',${ctr})">
																	<img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0" />
																</a> 
																<c:if test="${addressbookForm.phonesSize==0 ||  ctr==0}">
																	<c:set var="trnadd">
																		<digi:trn>Add</digi:trn>
																</c:set>
																<a id="phoneBtn" href="#" onclick="addNewData('phone');return false;" class="l_mid_b">${trnadd}</a></c:if>
															</div>
														</logic:iterate>
													</logic:notEmpty> 
													<logic:empty name="addressbookForm" property="phones">
														<c:set var="trnadd">
															<digi:trn>Add</digi:trn>
														</c:set>
														<a id="phoneBtnEmpty" href="#" onclick="addNewData('phone');return false;" class="l_mid_b">${trnadd}</a>
													</logic:empty>
												</td>
												<td></td>
											</tr>
											<tr>
												<td colspan="2"><hr /></td>
											</tr>
											<tr>
												<td valign="top" class="t_mid"><b style="padding-left:5px;"><digi:trn>Lastname</digi:trn></b><b style="color: rgb(255, 0, 0);">*</b>:<br />
											  		<html:text property="lastname" styleId="lastname" size="33"	styleClass="inputx insidex" readonly="${readonly}"/>
											  	</td>
											  	<td valign="top" class="t_mid" id="faxesPlace" style="padding-left:5px;"><b><digi:trn>Fax</digi:trn>:</b><br />  
											  		<logic:notEmpty name="addressbookForm" property="faxes">
											  			<a href="#" id="faxBtnEmpty" onclick="addNewData('fax');return false;" class="l_mid_b" style="display:none"> ${trnadd}</a>
														<logic:iterate name="addressbookForm" property="faxes" id="foo" indexId="ctr">
															<div id="div_fax_${ctr}">
																<html:text name="addressbookForm" property="faxes[${ctr}].value" size="33" styleClass="inputx insidex" styleId="fax_${ctr}"/>																												                    																												                    
																<a href="javascript:removeData('fax',${ctr})"><img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/></a>
																<c:if test="${ctr==0}">
																	<c:set var="trnadd"><digi:trn>Add</digi:trn></c:set>
																	<a href="#" id="faxBtn" onclick="addNewData('fax');return false;" class="l_mid_b"> ${trnadd}</a>
																</c:if>
															</div>
															<br>
														</logic:iterate>
													</logic:notEmpty>
													<logic:empty name="addressbookForm" property="faxes">
											 			<a href="#" id="faxBtnEmpty" onclick="addNewData('fax');return false;" class="l_mid_b"> ${trnadd}</a>
											 		</logic:empty>
											 	</td>
											  	<td >&nbsp;</td>
											</tr>
											<tr>
												<td colspan="2"><hr /></td>
											</tr>
											<tr>
												<td valign="top" class="t_mid" id="emailsPlace"><b style="padding-left:5px;"><digi:trn>Email</digi:trn></b>:<br />
											    	<logic:notEmpty name="addressbookForm" property="emails">
											    	<a href="#" id="emailBtnEmpty" onclick="addNewData('email');return false;" class="l_mid_b" style="display:none">${trnadd}</a>
														<logic:iterate name="addressbookForm" property="emails" id="foo" indexId="ctr">
															<div id="div_email_${ctr}"><html:text name="addressbookForm" property="emails[${ctr}].value" size="33" styleClass="inputx insidex" styleId="email_${ctr}" /> <a href="javascript:removeData('email',${ctr})">
															 	<img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0" /> </a> 
															 	<c:if test="${ctr==0}">
																	<c:set var="trnadd"><digi:trn>Add</digi:trn></c:set>
																	<a href="#" id="emailBtn" class="l_mid_b" onclick="addNewData('email');return false;">${trnadd}</a>
																</c:if>
															</div>
														</logic:iterate>
													</logic:notEmpty> 
													<logic:empty name="addressbookForm" property="emails">
														<c:set var="trnadd"><digi:trn>Add</digi:trn></c:set>
														<a href="#" id="emailBtnEmpty" onclick="addNewData('email');return false;" class="l_mid_b">${trnadd}</a>
													</logic:empty>
												</td>
											  	<td valign="top" class="t_mid" style="padding-left:5px;"><b><digi:trn>Office Address</digi:trn>:</b><br />
													<html:textarea property="officeaddress" styleClass="address_textarea" cols="40" />
												</td>
											  	<td rowspan="2" valign="top"></td>
											</tr>
											<tr>
												<td colspan="2"><hr /></td>
											</tr>
											<tr>
												<td valign="top" class="t_mid">
												  	<b style="padding-left:5px;"><digi:trn>Function</digi:trn>:</b>
												    <br />
									    			<html:text property="function" size="33" styleClass="inputx insidex"/>
								    			</td>
								  				<td align="right" valign="top">&nbsp;</td>
								  			</tr>
										</tbody>
									</table>
								</div>
								<hr />
								<c:choose>
									<c:when test="${not empty addressbookForm.probablyDuplicatedContacs}">
										<center><b style="font-size:12px;"><digi:trn>Already Existing Contacts</digi:trn>:</b><br /></center>
										<table border="0" style="font-size:12px; margin-bottom:15px; margin-top:15px;" align="center" width="100%" class="inside">
											<thead>
												<tr bgcolor=#C0D6E2>
													<td class="inside"></td>
													<td class="inside"><b><digi:trn>FirstName</digi:trn></b></td>
													<td class="inside"><b><digi:trn>LastName</digi:trn></b></td>
													<td class="inside"><b><digi:trn>Email</digi:trn></b></td>
													<td class="inside"><b><digi:trn>Organizations</digi:trn></b></td>
													<td class="inside"><b><digi:trn>Phone</digi:trn></b></td>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="contact" items="${addressbookForm.probablyDuplicatedContacs}">
													<tr>
														<td class="inside" width=25><html:radio property="contactIdToOverWrite" value="${contact.id}"></html:radio></td>
														<td class="inside"><c:out value="${contact.name}"></c:out></td>
														<td class="inside"><c:out value="${contact.lastname}"></c:out></td>
														<td class="inside">
															<ul style="margin:0;">
																<c:forEach var="property" items="${contact.properties}">
																	<c:if test="${property.name=='contact email'}">
																		<li>${property.value}</li>
																	</c:if>
																</c:forEach>
															</ul>
														</td>
														<td class="inside">
															<ul style="margin:0;">
																<c:forEach var="contactOrg" items="${contact.organizationContacts}">
																	<li><c:out value="${contactOrg.organisation.name}"/> </li>
																</c:forEach>
																<c:if test="${not empty contact.organisationName}">
																	<li><c:out value="${contact.organisationName}"/></li>
																</c:if>
															</ul>
														</td>
														<td class="inside">
															<ul style="margin:0;">
																<c:forEach var="property" items="${contact.properties}">
																	<c:if test="${property.name=='contact phone'}">
																		<li>${property.value}</li>
																	</c:if>
																</c:forEach>
															</ul>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<center><input type="button" class="buttonx_sm btn_save" value="<digi:trn>Create new Contact</digi:trn>" onclick="saveContact('new')" />
										<input type="button" class="buttonx_sm btn_save" value="<digi:trn>Overwrite</digi:trn>" onclick="saveContact('overwrite')" />
										<input type="button" class="buttonx_sm btn_save" value="<digi:trn>Cancel</digi:trn>"  onclick="saveContact('cancel')" /></center>
									</c:when>
									<c:otherwise>
                                        <span>
										    <center>
                                                <input type="button" class="buttonx_sm btn_save" value="<digi:trn>Save</digi:trn>" onclick="saveContact('check')" />
                                            </center>
                                        </span>
									</c:otherwise>
								</c:choose>
							</digi:form>
						</div>
					</div>
				</div>
			</td>
		</tr>
	</tbody>
</table>                                                    

<script language="JavaScript" type="text/javascript">
    addLoadEvent(initOrganizationScript);
  </script>