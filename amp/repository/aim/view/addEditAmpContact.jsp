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
<!-- Individual YUI CSS files --> 

<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 


<table width="1000" align="center" border="0" cellpadding="0" cellspacing="0">	
<tbody>
<tr>
<td valign="top" align="left">
<div style="padding-left: 10px; width: 98%; min-width: 680px;"   class="yui-skin-sam" id="content"> 
<div id="demo"  class="yui-navset yui-navset-top ">
<ul  class="yui-nav"> 
	<li>
	<a href="${contextPath}/aim/addressBook.do?actionType=viewAddressBook&reset=true&tabIndex=1">
	<div title='<digi:trn>Existing Contacts</digi:trn>'>
	<digi:trn>Existing Contacts</digi:trn>
	</div>
	</a>
	</li>
	<li class="selected" ><a>
	<div>
	<digi:trn>Add New Contact</digi:trn>
	</div>
	</a>
	</li>
</ul>
<div class="yui-content resource_popin">
<digi:form action="/addressBook.do?actionType=saveContact" method="post">	
<div class="required_fields">All fields marked with <b style="color:#ff0000">*</b> are required</div>
<hr />
	<table cellspacing="0" cellpadding="0" width="100%" id="config_table" style="margin-top:10px;">
<tbody><tr>
    <td class="t_mid"><digi:trn>Title</digi:trn><b style="color: rgb(255, 0, 0);">*</b></td>
    <td>
      <c:set var="translation">
       <digi:trn>Please select from below</digi:trn>
       </c:set>
     <category:showoptions multiselect="false" firstLine="${translation}" name="addressbookForm" property="title"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_TITLE_KEY%>" styleClass="nputx insidex address-title" outerid="contactTitle"/>
     </td>
 
    <td class="t_mid">Organization</td>
    <td valign="top"><html:text property="organisationName"  size="33" styleClass="inputx insidex"/> <aim:addOrganizationButton showAs="popin" refreshParentDocument="false" collection="organizations" form="${addressbookForm}" styleClass="buttonx_sm btn_save">
<digi:trn>Add Organizations</digi:trn>
</aim:addOrganizationButton> 
</tr>
<tr>
<td colspan="2">&nbsp;</td>
<td  colspan="2" align="center">
<c:if test="${not empty addressbookForm.organizations}">
<table width="100%" cellSpacing=1 cellPadding=5 class="added_org_nc">
<c:forEach var="organization" items="${addressbookForm.organizations}">
<tr>
<td width="3px">
<html:multibox property="selOrgs">
<bean:write name="organization" property="ampOrgId" />
</html:multibox></td>
<td align="left" class="l_mid_b"><bean:write name="organization" property="name" /></td>
</tr>
</c:forEach>
<tr>
<td colspan="2">

<input type="button" class="buttonx_sm btn_save" onclick="javascript:removeOrgs();"value='<digi:trn>Remove Organization(s)</digi:trn>' /></td>
</tr>
</table>
</c:if>

</td>
</tr>
<tr>
<td class="t_mid"><digi:trn>Firstname</digi:trn><b style="color: rgb(255, 0, 0);">*</b>:</td>
<td><html:text property="name" styleId="name" size="33" styleClass="inputx insidex"/></td>
<td class="t_mid"><digi:trn>Phone Number</digi:trn>:</td>
<td><logic:notEmpty name="addressbookForm" property="phones">
<logic:iterate name="addressbookForm" property="phones" id="foo" indexId="ctr">
<div><c:set var="translationNone">
<digi:trn>None</digi:trn>
</c:set> 
<category:showoptions multiselect="false" firstLine="${translationNone}" name="addressbookForm" property="phones[${ctr}].phoneTypeId" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_PHONE_TYPE_KEY%>" styleClass="nputx insidex address-title" outerid="phoneType_${ctr}" /> 	 	
<html:text name="addressbookForm" property="phones[${ctr}].value" styleId="phoneNum_${ctr}"  size="33" styleClass="inputx insidex" /> 
<a href="javascript:removeData('phone',${ctr})">
<img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0" /></a> 
<c:if test="${addressbookForm.phonesSize==0 ||  ctr==addressbookForm.phonesSize-1}">
<c:set var="trnadd">
<digi:trn>Add</digi:trn>
</c:set>
<a id="addPhoneBtn" href="#" onclick="addNewData('phone');return false;" class="l_mid_b">${trnadd}</a>
</c:if>
</div>
</logic:iterate>
</logic:notEmpty> 
<logic:empty name="addressbookForm" property="phones">
<c:set var="trnadd">
<digi:trn>Add</digi:trn>
</c:set>
<a id="addPhoneBtn" href="#" onclick="addNewData('phone');return false;" class="l_mid_b">${trnadd}</a>
</logic:empty></td>
</tr>
<tr>
  <td class="t_mid"><digi:trn>Lastname</digi:trn><b style="color: rgb(255, 0, 0);">*</b>:</td>
  <td><html:text property="lastname" styleId="lastname" size="33" styleClass="inputx insidex"/></td>
  <td class="t_mid"><digi:trn>Fax</digi:trn>:</td>
  <td>
  <logic:notEmpty name="addressbookForm" property="faxes">
<logic:iterate name="addressbookForm" property="faxes" id="foo" indexId="ctr">
<html:text name="addressbookForm" property="faxes[${ctr}].value" size="33" styleClass="inputx insidex" styleId="fax_${ctr}"/>																												                    																												                    
<a href="javascript:removeData('fax',${ctr})"><img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/></a>
<c:if test="${ctr==addressbookForm.faxesSize-1}">
<c:set var="trnadd"><digi:trn>Add</digi:trn></c:set>
 <a href="#" id="addFaxBtn" onclick="addNewData('fax');return false;" class="l_mid_b"> ${trnadd}</a>
</c:if>
<br>
</logic:iterate>
</logic:notEmpty>
<logic:empty name="addressbookForm" property="faxes">
 <a href="#" id="addFaxBtn" onclick="addNewData('fax');return false;" class="l_mid_b"> ${trnadd}</a>
</logic:empty>
  </td>
</tr>
<tr>
  <td class="t_mid"><digi:trn>Email</digi:trn>:</td>
  <td valign="top">
  <logic:notEmpty name="addressbookForm" property="emails">
<logic:iterate name="addressbookForm" property="emails" id="foo" indexId="ctr">
<div><html:text name="addressbookForm" property="emails[${ctr}].value" size="33" styleClass="inputx insidex" styleId="email_${ctr}" /> <a href="javascript:removeData('email',${ctr})">
 <img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0" /> </a> 
 <c:if test="${ctr==addressbookForm.emailsSize-1}">
<c:set var="trnadd"><digi:trn>Add</digi:trn></c:set>
<a href="#" id="addEmailBtn" class="l_mid_b" onclick="addNewData('email');return false;">${trnadd}</a>
</c:if>
</div>
</logic:iterate>
</logic:notEmpty> 
<logic:empty name="addressbookForm" property="emails">
<c:set var="trnadd"><digi:trn>Add</digi:trn></c:set>
<a href="#" id="addEmailBtn" onclick="addNewData('email');return false;" class="l_mid_b">${trnadd}</a>
</logic:empty>
</td>
  <td class="t_mid"><digi:trn>Office Address</digi:trn>:</td>
  <td rowspan="2" valign="top"><html:textarea property="officeaddress" styleClass="address_textarea" cols="40" /></td>
</tr>
<tr>
  <td class="t_mid"><digi:trn>Function</digi:trn>:</td>
  <td valign="top"><html:text property="function" size="33" styleClass="inputx insidex"/></td>
  <td align="right" valign="top">&nbsp;</td>
  </tr>
</tbody></table>
<hr /><center><input type="button" class="buttonx_sm btn_save" value="Save" onclick="saveContact()"></center>
</digi:form>
</div>
</div>
</div>
</td>
</tr>
</tbody></table>                                                    

<script language="JavaScript" type="text/javascript">
    addLoadEvent(initOrganizationScript);
  </script>