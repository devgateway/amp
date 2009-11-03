<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<div id="contactPopin" style="display: none">
    <div id="popinContactContent" class="content">
    </div>
</div>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<style type="text/css">
   .mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}

	#contactPopin .content {
	    overflow:auto;
	    height:455px;
	    background-color:#ffffff;
	    padding:10px;
	}
	.bd a:hover {
  		background-color:#ecf3fd;
		font-size: 10px;
		color: #0e69b3;
		text-decoration: none
	}
	.bd a {
	  	color:black;
	  	font-size:10px;
	}


</style>
<script type="text/javascript">
    <!--
   YAHOOAmp.namespace("YAHOOAmp.amp");

    var myPanelContact = new YAHOOAmp.widget.Panel("newpopins", {
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
    var panelStartContact=0;
    var checkAndCloseContact=false;

      function initContactScript() {
        var msg='\n<digi:trn >Add Contact Information</digi:trn>';
        myPanelContact.setHeader(msg);
        myPanelContact.setBody("");
        myPanelContact.beforeHideEvent.subscribe(function() {
            panelStartContact=1;
        });

        myPanelContact.render(document.body);
        panelStartContact=0;
    }
  -->
</script>

<script type="text/javascript">
     <!--
    var responseSuccess1 = function(o){
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
        var content = document.getElementById("popinContactContent");
        //response = response.split("<!")[0];
        content.innerHTML = response;
        //content.style.visibility = "visible";
        showContactContent();
    }

    var responseFailure1 = function(o){
        alert("Connection Failure!");
    }
    var callback1 =
        {
        success:responseSuccess1,
        failure:responseFailure1
    };

    function showContactContent(){
        var element = document.getElementById("contactPopin");
        element.style.display = "inline";
        if (panelStartContact < 1){
            myPanelContact.setBody(element);
        }

        if (panelStartContact < 2){
            document.getElementById("contactPopin").scrollTop=0;
            myPanelContact.show();
            panelStartContact = 2;
        }
        checkErrorAndCloseContact();
      
    }

    function checkErrorAndCloseContact(){
        if(checkAndCloseContact==true){
            if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
                 myContactClose();
                 refreshPage();
            }
            checkAndCloseContact=false;
        }
    }
         
   

    function  showContactPanelLoading(msg){
        myPanelContact.setHeader(msg);
        var content = document.getElementById("popinContactContent");
        content.innerHTML = '<div style="text-align: center">' +
            '<img src="/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' +
            '<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
        showContactContent();
    }
    function selectContact(params1) {
        myPanelContact.cfg.setProperty("width","800px");
        myPanelContact.cfg.setProperty("height","500px");
        YAHOOAmp.util.Connect.asyncRequest("POST", params1, callback1);
    }

    
    function saveContact(){
        if(validateInfo()){
            //ajax check for duplicate email
            checkForduplicateEmail();
        }
    }


    function checkForduplicateEmail(){ //checks whether such email already exists in db
        var email=document.getElementById('contactEmail').value;
        var id=document.getElementById('contactId').value;
        var url=addActionToURL('addAmpContactInfo.do?action=checkDulicateEmail&email='+email+"&contactId="+id);
        var async=new Asynchronous();
        async.complete=showErrorOrSaveContact;
        async.call(url);
    }

    function showErrorOrSaveContact(status, statusText, responseText, responseXML){
        var root=responseXML.getElementsByTagName('CONTACTS')[0].childNodes[0];
        var contEmail=root.getAttribute('email');
        if(contEmail=='exists'){
            alert('Contact with the given email already exists');
            return false;
        }
        //if emails doesn't exist, save contact.
        addContact();
    }

    function myContactClose(){
        myPanelContact.hide();
        myPanelContact=1;

    }
 

    function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }

    function validateInfo(){
        if(document.getElementById('contactName').value==null || document.getElementById('contactName').value==''){
            alert('Please Enter Name');
            return false;
        }
        if(document.getElementById('contactLastname').value==null || document.getElementById('contactLastname').value==''){
            alert('Please Enter lastname');
            return false;
        }
        if(document.getElementById('contactEmail').value==null || document.getElementById('contactEmail').value==''){
            alert('Please Enter email');
            return false;
        }else if(document.getElementById('contactEmail').value.indexOf('@')==-1){
            alert('Please Enter Correct Email');
            return false;
        }
        if (checkNumber('contactPhone')==false){
            return false;
        }
        if(checkNumber('contactFax')==false){
            return false;
        }
        return true;
    }

    function checkNumber(phoneOrFaxId){
        var phoneOrFax=document.getElementById(phoneOrFaxId);
        var number=phoneOrFax.value;
        var validChars= "0123456789()+ ";
        for (var i = 0;  i < number.length;  i++) {
            var ch = number.charAt(i);
            if (validChars.indexOf(ch)==-1){
                alert('enter correct number');
                phoneOrFax.value=number.substring(0,i);
                return false;
            }
        }
        return true;
    }

 

    



    function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }

          function addContact(){
            <digi:context name="addCont" property="context/addAmpContactInfo.do?action=save"/>;
                 var url="${addCont}"+"&"+getContactParams();
		 var async=new Asynchronous();
                 async.complete=closeContactPopin;
                 async.call(url);

             }
               function closeContactPopin(status, statusText, responseText, responseXML){
                   checkAndCloseContact=true;
                   checkErrorAndCloseContact();
               }
         
        function searchContact(){
            var flg=checkEmptyKeywordContact();
            if(flg){
                var keyword=document.getElementById('keyword').value;
    <digi:context name="searchCont" property="context/addAmpContactInfo.do?action=search" />
                var url = "${searchCont}&keyword="+keyword;
                YAHOOAmp.util.Connect.asyncRequest("POST", url, callback1);
                return true;
            }
            return false;
        }

        function checkEmptyKeywordContact() {
            var flag=true;
            var keyword=document.getElementById('keyword');
            if(trim(keyword.value) == "")
            {
                alert("Please Enter a Keyword....");
                flag=false;
            }
            return flag;
        }

        function addSelectedContacts()
        {
              <digi:context name="addSelCont" property="context/addAmpContactInfo.do?action=addSelectedConts"/>;
                checkAndCloseContact=true;
                var url="${addSelCont}"+"&"+getSelectedContactsParams();
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback1);
        }

              function getContactParams(){
                var title = document.getElementById('contactTitle');
                var titleId=title.options[title.selectedIndex].value;
                var params="";           
                  params+="name="+document.getElementById('contactName').value+
                      "&lastname="+document.getElementById('contactLastname').value +
                      "&email="+document.getElementById('contactEmail').value+
                      "&title="+titleId+
                      "&function="+document.getElementById('contactFunction').value+
                      "&officeaddress="+document.getElementById('contactOfficeaddress').value+            
                      "&organisationName="+document.getElementById('contactOrgName').value+
                      "&phone="+document.getElementById('contactPhone').value+
                      "&fax="+document.getElementById('contactFax').value+
                      "&mobilephone="+document.getElementById('contactMobilephone').value+
                      "&contactId="+document.getElementById('contactId').value+
                      "&temporaryId="+document.getElementById('temporaryId').value;
                  return params;
              }
        function getSelectedContactsParams(){
            var params="";
            var contacts = document.getElementsByName("selContactIds");
            if(contacts!=null){
			var size = contacts.length;
			for(var i=0; i< size; i++){
				if(contacts[i].checked){
					params+="&"+contacts[i].name+"="+contacts[i].value;
				}
			}
		}
                return params;
        }

        function removeContactOrgs(){
            var params=getContactParams();
            if(document.getElementsByName("selContactOrgs")!=null){
                var orgs = document.getElementsByName("selContactOrgs").length;
                for(var i=0; i<  orgs; i++){
                    if(document.getElementsByName("selContactOrgs")[i].checked){
                        params+="&"+document.getElementsByName("selContactOrgs")[i].name+"="+document.getElementsByName("selContactOrgs")[i].value;
                    }
                }
            }
            else{
                var msg="<digi:trn jsFriendly="true">Please select organization(s) to remove</digi:trn>"
                alert(msg);
            }
                <digi:context name="addCont" property="context/addAmpContactInfo.do?action=removeOrganizations"/>;
                    var url="${addCont}"+"&"+params;
                    YAHOOAmp.util.Connect.asyncRequest("POST", url, callback1);

                }
        -->

</script>