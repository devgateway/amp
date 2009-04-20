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
		myPanel.cfg.setProperty("height","500px");
		var msg='\n<digi:trn key="aim:selectOrg">Select Organization</digi:trn>';
		showPanelLoading(msg);
		YAHOOAmp.util.Connect.asyncRequest("POST", params1, callback);
	}

	function checkNumeric(objName,comma,period,hyphen)
	{
		var numberfield = objName;
		if (chkNumeric(objName,comma,period,hyphen) == false)
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

	function chkNumeric(objName,comma,period,hyphen)
	{
// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters
		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = objName;
		var allValid = true;
		var decPoints = 0;
		var allNum = "";

		for (i = 0;  i < checkStr.value.length;  i++)
		{
			ch = checkStr.value.charAt(i);
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
		<c:set var="translation">
			<digi:trn key="aim:chooseOrganizationToAdd">Please choose an organization to add</digi:trn>
		</c:set>
		if(document.aimSelectOrganizationForm.selectedOrganisationFromPages.value != "-1") return true;

		if (document.aimSelectOrganizationForm.selOrganisations.checked != null) { // only one
			if (document.aimSelectOrganizationForm.selOrganisations.checked == false) {
				alert("${translation}");
				return false;
			}
		}
		else { // many
			var length = document.aimSelectOrganizationForm.selOrganisations.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimSelectOrganizationForm.selOrganisations[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("${translation}");
				return false;
			}
		}
		return true;
	}
	
	
	function setOrganization(id) {
		<digi:context name="selOrg" property="/aim/selectOrganizationComponent.do"/>
	    //document.aimSelectOrganizationForm.action = "<%= selOrg %>&id="+id;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    //document.aimSelectOrganizationForm.submit();
	    checkAndClose=true;
		var urlParams="<%=selOrg%>";
		var params="edit=true&orgSelReset=false&subAction=organizationSelected&id="+id;
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback);
		    
		//return true;
	}
	
	function selectOrganization() {
		var flag = validate();
		if (flag == false)
			return false;

		
		<digi:context name="selOrg" property="/aim/selectOrganizationComponent.do"/>
	    //document.aimSelectOrganizationForm.action = "<%= selOrg %>";
		//document.aimSelectOrganizationForm.target = window.opener.name;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    //document.aimSelectOrganizationForm.submit();
	    checkAndClose=true;
		var url = "<%=selOrg %>"
		var params = "?edit=true&orgSelReset=false&subAction=organizationSelected"+getParams();    
		YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback);
		
		return true;
	}

	function resetForm() {
		document.aimSelectOrganizationForm.ampOrgTypeId.value=-1;
		document.aimSelectOrganizationForm.keyword.value="";
		document.aimSelectOrganizationForm.tempNumResults.value=10;
	
	}




	function selectOrganizationPages(page) {
	   <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do" />
	   //var val = "<%=searchOrg%>";
	   //val = val + page;
	   //document.aimSelectOrganizationForm.action = val;

	   document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=page;
	   var urlParams="<%=searchOrg%>";
	   var params="edit=true&orgSelReset=false&subAction=selectPage&page="+page;
	   YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback);
	   //document.aimSelectOrganizationForm.submit();
	   //return true;
	}	
	function getParams(){
		ret="";
		ret+="&selectedOrganisationFromPages="+document.getElementsByName('selectedOrganisationFromPages')[0].value+
		"&keyword="+document.getElementsByName('keyword')[0].value+
		"&ampOrgTypeId="+document.getElementsByName('ampOrgTypeId')[0].value;
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
		if(checkNumeric(document.aimSelectOrganizationForm.tempNumResults	,'','','')==true)
		{
			if (document.aimSelectOrganizationForm.tempNumResults.value == 0) {
				  alert ("Invalid value at 'Number of results per page'");
				  document.aimSelectOrganizationForm.tempNumResults.focus();
				  //return false;
			} else {
				 <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do"/>
			    //document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
			    //document.aimSelectOrganizationForm.submit();
			    var url = "<%=searchOrg %>"
				var params = "?edit=true&subAction=search"+getParams();    
			    YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback);
				//return true;
			}
		}
		else return false;
	}




	function searchAlpha(val) {
		if (document.aimSelectOrganizationForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimEditActivityForm.tempNumResults.focus();
			  //return false;
		} else {
			 <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do"/>
			 //url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";
		     //document.aimSelectOrganizationForm.action = url;
		     //document.aimSelectOrganizationForm.submit();
		     var url = "<%=searchOrg %>"
			 var params = "?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";    
			 YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback);
		     
			 //return true;
		}
	}
		
	function searchAlphaAll(val) {
		if (document.aimSelectOrganizationForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimSelectOrganizationForm.tempNumResults.focus();
			  //return false;
		} else {
			 <digi:context name="searchOrg" property="/aim/selectOrganizationComponent.do"/>
			  //  document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
		      var aux= document.aimSelectOrganizationForm.tempNumResults.value;
		      document.aimSelectOrganizationForm.tempNumResults.value=1000000;
		     //document.aimSelectOrganizationForm.submit();

			   var urlParams="<%=searchOrg%>";
			   var params="?edit=true&subAction=search&tempNumResults=1000000";
			   YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+params, callback);
			      document.aimSelectOrganizationForm.tempNumResults.value=aux;		      
			  //return true;
		}
	}


</script>
