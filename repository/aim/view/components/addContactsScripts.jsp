<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script type="text/javascript">
    function selectContact(params1) {
        myPanel.cfg.setProperty("width","600px");
        myPanel.cfg.setProperty("height","500px");
        var msg='\n<digi:trn >Add Contact Information</digi:trn>';
        showPanelLoading(msg);
        YAHOOAmp.util.Connect.asyncRequest("POST", params1, callback);
    }

    
    function saveContact(){
        if(validateInfo()){
            //ajax check for duplicate email
            checkForduplicateEmail();
        }
    }


    function checkForduplicateEmail(){ //checks whether such email already exists in db
        var email=document.getElementById('contactEmail').value;
        var url=addActionToURL('addAmpContactInfo.do?action=checkDulicateEmail&email='+email);
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
        myclose();
        addContact();
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

            function addContact()
            {
        <digi:context name="addCont" property="context/addAmpContactInfo.do?action=save"/>;
                checkAndClose=true;
                var url="${addCont}"+"&"+getContactParams();
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
                
            }

        function searchContact(){
            var flg=checkEmptyKeywordContact();
            if(flg){
                var keyword=document.getElementById('keyword').value;
    <digi:context name="searchCont" property="context/addAmpContactInfo.do?action=search" />
                var url = "${searchCont}&keyword="+keyword;
                YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
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
                checkAndClose=true;
                var url="${addSelCont}"+"&"+getSelectedContactsParams();
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
        }

              function getContactParams(){
                  var params="";
                  params+="name="+document.getElementById('contactName').value+
                      "&lastname="+document.getElementById('contactLastname').value +
                      "&email="+document.getElementById('contactEmail').value+
                      "&title="+document.getElementById('contactTitle').value+
                      "&organisationName="+document.getElementById('contactOrgName').value+
                      "&phone="+document.getElementById('contactPhone').value+
                      "&fax="+document.getElementById('contactFax').value;
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


        -->

</script>