<%@ page import="org.digijava.kernel.request.TLSUtils" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript">	

function selectOrg(params1, params2, params3, params4) {
	myPanel.cfg.setProperty("width","600px");
	myPanel.cfg.setProperty("height","550px");
	var msg='\n<digi:trn jsFriendly="true" key="aim:selectOrg">Select Organization</digi:trn>';
	showPanelLoading(msg);
	YAHOO.util.Connect.asyncRequest("POST", params1, callback);
}

var isRtl = <%=TLSUtils.getCurrentLocale().getLeftToRight() == false%>;
var language = '<%=TLSUtils.getCurrentLocale().getCode()%>';
var region = '<%=TLSUtils.getCurrentLocale().getRegion()%>';

var responseSuccessValidation = function(o){
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
	var root=o.responseXML.getElementsByTagName('ORGANIZATIONS')[0].childNodes[0];
    var selOrgs=root.getAttribute('amount');
    if(selOrgs=='0'){
    	<c:set var="translation">
    		<digi:trn>Please choose an organization to add</digi:trn>
    	</c:set>
    	alert("${translation}");
        checkAndClose=false;
        return false;
    }else{
    	processAddOrganizations();
    }		
}	

var responseFailureValidation = function(o){ 
// Access the response object's properties in the 
// same manner as listed in responseSuccess( ). 
// Please see the Failure Case section and 
// Communication Error sub-section for more details on the 
// response object's properties.
	alert("Connection Failure!"); 
}  

var validationCallback = 
{ 
	success:responseSuccessValidation, 
	failure:responseFailureValidation 
};

	function checkNumericField(objName,comma,period,hyphen)
	{
		var numberfield = objName;
		if (checkNumericFieldValue(objName,comma,period,hyphen) == false)
		{
			numberfield.select();
			numberfield.focus();
			return false;
		}
		else
		{
			return true;
		}
	}

	function checkNumericFieldValue(objName, comma, period, hyphen)
	{
// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters
		var checkOK = "0123456789" + comma + period + hyphen;
        var checkStr = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, objName.value);
		var allValid = true;
		var decPoints = 0;
		var allNum = "";

		for (i = 0;  i < checkStr.length;  i++)
		{
			ch = checkStr.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
			if (ch == checkOK.charAt(j))
			break;
			if (j == checkOK.length)
			{
				allValid = false;
				break;
			}
			if (ch != ",")
			allNum += ch;
		}
		if (!allValid)
		{
			alertsay = "Please enter only numbers in the \"Number of results per page\"."
			alert(alertsay);
			return (false);
		}
	}

	function validate() {
		var checkboxes=$("#searchResults").find("input.orgsMultibox:checked");
		if(checkboxes!=null && checkboxes.length>0){
			return true;
		}
		return false;		
	}
	
	
	function setOrganization(id) {
		<digi:context name="selOrg" property="/aim/selectOrganizationComponent.do"/>
	    //document.aimSelectOrganizationForm.action = "<%= selOrg %>&id="+id;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    checkAndClose=true;
		var urlParams="<%=selOrg%>";
		var params="edit=true&orgSelReset=false&subAction=organizationSelected&id="+id;
		YAHOO.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback);
	}

	function closeWindow() {
		myPanel.hide();
	}
	
	function selectOrganization() {

		var flag = validate();
		if (flag == true){
			processAddOrganizations();
		}else{
			<digi:context name="selOrg" property="/aim/selectOrganizationComponent.do?subAction=validate"/>
			document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
		    checkAndClose=true;
			var urlParams="<%=selOrg%>";		
			YAHOO.util.Connect.asyncRequest("POST", urlParams, validationCallback);	
		}
	}

	function processAddOrganizations(){ //dare
		<digi:context name="selOrg" property="/aim/selectOrganizationComponent.do"/>;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
		checkAndClose=true;
		var url = "<%=selOrg %>";
		var params = "?edit=true&orgSelReset=false&subAction=organizationSelected"+getParams(); 
		YAHOO.util.Connect.asyncRequest("POST", url+params, callback);
	}

	function resetForm() {
		document.aimSelectOrganizationForm.ampOrgTypeId.value=-1;
		document.aimSelectOrganizationForm.keyword.value="";
		document.aimSelectOrganizationForm.tempNumResults.value = TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, '10');
	
	}

	function selectOrganizationPages(page) {
	   <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do" />
	   //var val = "<%=searchOrg%>";
	   //val = val + page;
	   //document.aimSelectOrganizationForm.action = val;

	   document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=page;
	   var urlParams="<%=searchOrg%>";
	   var params="edit=true&orgSelReset=false&subAction=selectPage&page="+page;
	   if(document.getElementsByName("selOrganisations")!=null){
			var sectors = document.getElementsByName("selOrganisations").length;
			for(var i=0; i< sectors; i++){
				if(document.getElementsByName("selOrganisations")[i].checked){
					params+="&"+document.getElementsByName("selOrganisations")[i].name+"="+document.getElementsByName("selOrganisations")[i].value;
				}
			}
		}
	   YAHOO.util.Connect.asyncRequest("POST", urlParams, callback,params);
	   //document.aimSelectOrganizationForm.submit();
	   //return true;
	}	
	function getParams(){
		ret="";
		ret+="&selectedOrganisationFromPages="+document.getElementsByName('selectedOrganisationFromPages')[0].value+
		"&keyword="+document.getElementById('keyword').value +
		"&ampOrgTypeId="+ document.aimSelectOrganizationForm.ampOrgTypeId.value +
		"&tempNumResults="+TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, document.getElementById('tempNumResults').value);
		//else if (type==3){//add sectors chosen from the list
		if(document.getElementsByName("selOrganisations")!=null){
			var sectors = document.getElementsByName("selOrganisations").length;
			for(var i=0; i< sectors; i++){
				if(document.getElementsByName("selOrganisations")[i].checked){
					ret+="&"+document.getElementsByName("selOrganisations")[i].name+"="+document.getElementsByName("selOrganisations")[i].value;
				}
			}
		}
	
		return ret;
	}
	function searchOrganization() {
		if(checkNumericField(document.aimSelectOrganizationForm.tempNumResults	,'','','')==true)
		{
            var convertedResults = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, document.aimSelectOrganizationForm.tempNumResults.value);
            if (convertedResults == 0) {
                alert ("Invalid value at 'Number of results per page'");
				  document.aimSelectOrganizationForm.tempNumResults.focus();
				  //return false;
			} else {
				 <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do"/>
			    var url = "<%=searchOrg %>"
				var params = "?edit=true&subAction=search"+getParams();    
			    YAHOO.util.Connect.asyncRequest("POST", url+params, callback);
			    //return true;
			}
		}
		else return false;
	}


	function searchAlpha(val) {
        var convertedResults = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, document.aimSelectOrganizationForm.tempNumResults.value);
		if (convertedResults == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimEditActivityForm.tempNumResults.focus();
			  //return false;
		} else {
			 <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do"/>
			 //url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";
		     //document.aimSelectOrganizationForm.action = url;
		     //document.aimSelectOrganizationForm.submit();
		     var urlParams = "<%=searchOrg %>"
			 var params = "alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";
		     if(document.getElementsByName("selOrganisations")!=null){
					var sectors = document.getElementsByName("selOrganisations").length;
					for(var i=0; i< sectors; i++){
						if(document.getElementsByName("selOrganisations")[i].checked){
							params+="&"+document.getElementsByName("selOrganisations")[i].name+"="+document.getElementsByName("selOrganisations")[i].value;
						}
					}
				}
			 YAHOO.util.Connect.asyncRequest("POST",urlParams,callback,params);
		}
	}
		
	function searchAlphaAll(val) {
        var convertedResults = TranslationManager.convertNumbersToWesternArabicIfNeeded(isRtl, language, region, document.aimSelectOrganizationForm.tempNumResults.value);
		if (convertedResults == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimSelectOrganizationForm.tempNumResults.focus();
			  //return false;
		} else {
			 <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do"/>
			  //  document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
		      var aux= document.aimSelectOrganizationForm.tempNumResults.value;
		      document.aimSelectOrganizationForm.tempNumResults.value=1000000;

			   var urlParams="<%=searchOrg%>";
			   var params="?edit=true&subAction=search&tempNumResults=1000000&viewAll=viewAll";
			   if(document.getElementsByName("selOrganisations")!=null){
					var sectors = document.getElementsByName("selOrganisations").length;
					for(var i=0; i< sectors; i++){
						if(document.getElementsByName("selOrganisations")[i].checked){
							params+="&"+document.getElementsByName("selOrganisations")[i].name+"="+document.getElementsByName("selOrganisations")[i].value;
						}
					}
				}

			   YAHOO.util.Connect.asyncRequest("POST", urlParams, callback,params);
			   document.aimSelectOrganizationForm.tempNumResults.value=aux;
		}
	}


</script>
