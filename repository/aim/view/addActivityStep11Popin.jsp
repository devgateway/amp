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
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>



<div id="popin" style="display: none">
	<div id="popinContent" class="content">
	</div>
</div>
<div id="popin2" style="display: none">
	<div id="popinContent2" class="content">
	</div>
</div>

<script type="text/javascript">
<!--

		YAHOOAmp.namespace("YAHOOAmp.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
			width:"800px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
		var myPanel2 = new YAHOOAmp.widget.Panel("newpopins", {
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
	    
		var panelStart, panelStart2;
		var checkAndClose2=false;
		var checkAndClose=false;	    
	function initCostingScript() {
		var msg='\n<digi:trn key="aim:selectOrg">Select Organization</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
			if(calendarObjForForm.isVisible()){
				calendarObjForForm.hide();
			}
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
		var msgP2='\n<digi:trn key="aim:selectOrganization">Select Organization</digi:trn>';
		myPanel2.setHeader(msgP2);
		myPanel2.setBody("");
		myPanel2.beforeHideEvent.subscribe(function() {
			panelStart2=1;
		}); 
		myPanel2.render(document.body);
		panelStart2 = 0;
		
	}
	//this is called from editActivityMenu.jsp
	//window.onload=initCostingScript();
-->	
</script>
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
	    background-color:fff; 
	    padding:10px; 
	} 
	#popin2 .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
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

<script language="JavaScript">
    <!--
   
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
				myclose();
				refreshPage();
			}
			checkAndClose=false;			
		}
	}
	function refreshPage(){
		document.aimEditActivityForm.step.value = "11";
		document.aimEditActivityForm.action = "/aim/addActivity.do?edit=true";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();		
	}

	function myclose(){
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeCosting() {
		myclose();
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent();
	}
	function addEUActivity() {
		var msg='\n<digi:trn key="aim:addEditActivity">Add/Edit Activity</digi:trn>';
		showPanelLoading(msg);		
		<digi:context name="addEUActivity" property="context/module/moduleinstance/editEUActivity.do?new" />
		var url = "<%=addEUActivity %>";//?comment=" + "ccd" + "&edit=" + "true";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	function editEUActivity(indexId) {
		var msg='\n<digi:trn key="aim:addEditActivity">Add/Edit Activity</digi:trn>';
		showPanelLoading(msg);		
		<digi:context name="editEUActivity" property="context/module/moduleinstance/editEUActivity.do?editEU&indexId=" />
		var url = "<%=editEUActivity%>";
		url+=indexId;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	function deleteEUActivity(indexId) {
		<digi:context name="editEUActivity" property="context/module/moduleinstance/editEUActivity.do?deleteEU&indexId=" />
		var url = "<%=editEUActivity%>";
		url+=indexId;
		checkAndClose=true;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	function clearDefault(editBox)
	{
		if(editBox.value=='Amount') editBox.value='';
	}	
	function addContributors(){
		<digi:context name="addEUActivity" property="context/module/moduleinstance/editEUActivity.do" />
		var url = "<%=addEUActivity %>?addFields=true&edit=true"
		url+=getParamsActivity()+getSelectedCont();
		
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		
	}	
	function removeContributors(){
		if(checkSelectedCont()){
			<digi:context name="addEUActivity" property="context/module/moduleinstance/editEUActivity.do" />
			var url = "<%=addEUActivity %>?removeFields=true&edit=true"
			url+=getParamsActivity()+getSelectedCont2();
			
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		}
		else{
			<c:set var="translation">
				<digi:trn>Select a Contributor to remove</digi:trn>
			</c:set>
			alert('${translation}');
		}
	}
	function checkSelectedCont(){
		if(document.getElementsByName("deleteContrib")!=null){
			var items = document.getElementsByName("deleteContrib").length;
			for(var i=0; i< items; i++){
				if(document.getElementsByName("deleteContrib")[i].checked){
					return true;
				}
			}
			return false;
		}
		else{
			return false;
		}
		
	}
	function getSelectedCont2(){
		var ret="";
		if(document.getElementsByName("deleteContrib")!=null){
			var items = document.getElementsByName("deleteContrib").length;
			for(var i=0; i< items; i++){
				if(document.getElementsByName("deleteContrib")[i].checked){
					ret+="&"+document.getElementsByName("deleteContrib")[i].name+"="+document.getElementsByName("deleteContrib")[i].value;
				}
				ret+="&"+document.getElementsByName("contrAmount")[i].name+"="+document.getElementsByName("contrAmount")[i].value +
					"&"+document.getElementsByName("contrCurrId")[i].name+"="+document.getElementsByName("contrCurrId")[i].value +
					"&"+document.getElementsByName("contrFinTypeId["+i+"]")[0].name+"="+document.getElementsByName("contrFinTypeId["+i+"]")[0].value +
					"&"+document.getElementsByName("contrDonorId")[i].name+"="+document.getElementsByName("contrDonorId")[i].value +
					"&"+document.getElementsByName("contrDonorName")[i].name+"="+document.getElementsByName("contrDonorName")[i].value +
					"&"+document.getElementsByName("contrFinInstrId["+i+"]")[0].name+"="+document.getElementsByName("contrFinInstrId["+i+"]")[0].value;
			}
		}
		return ret;

	}

	function getSelectedCont(){
		var ret="";
		if(document.getElementsByName("deleteContrib")!=null){
			var items = document.getElementsByName("deleteContrib").length;
			for(var i=0; i< items; i++){
				ret+="&"+document.getElementsByName("deleteContrib")[i].name+"="+document.getElementsByName("deleteContrib")[i].value +
				"&"+document.getElementsByName("contrAmount")[i].name+"="+document.getElementsByName("contrAmount")[i].value +
				"&"+document.getElementsByName("contrCurrId")[i].name+"="+document.getElementsByName("contrCurrId")[i].value +
				"&"+document.getElementsByName("contrFinTypeId["+i+"]")[0].name+"="+document.getElementsByName("contrFinTypeId["+i+"]")[0].value +
				"&"+document.getElementsByName("contrDonorId")[i].name+"="+document.getElementsByName("contrDonorId")[i].value +
				"&"+document.getElementsByName("contrDonorName")[i].name+"="+document.getElementsByName("contrDonorName")[i].value +
				"&"+document.getElementsByName("contrFinInstrId["+i+"]")[0].name+"="+document.getElementsByName("contrFinInstrId["+i+"]")[0].value;
			}
		}
		return ret;

	}
	function getParamsActivity(){
		var ret="";
		ret+="&id="+document.getElementsByName('id')[0].value+ 
			"&name="+document.getElementsByName('name')[0].value;
		if(document.getElementsByName('textId')[0]!=null){
			ret+="&textId="+document.getElementsByName('textId')[0].value;
		}
		
		ret+="&inputs="+document.getElementsByName('inputs')[0].value+
			"&totalCost="+document.getElementsByName('totalCost')[0].value+
			"&totalCostCurrencyId="+document.getElementsByName('totalCostCurrencyId')[0].value+
			"&assumptions="+document.getElementsByName('assumptions')[0].value+
			"&progress="+document.getElementsByName('progress')[0].value+
			"&dueDate="+document.getElementsByName('dueDate')[0].value;
		return ret;
	}
	function saveCosting(){
		if(checkFields()){
			<digi:context name="addEUActivity" property="context/module/moduleinstance/editEUActivity.do" />
			var url = "<%=addEUActivity %>?save=true&edit=true"
			url+=getParamsActivity()+getSelectedCont();
			checkAndClose=true;
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		}
	}
	function checkFields(){
		if(document.getElementsByName("name")[0].value<=0){
			<c:set var="translation">
				<digi:trn>Enter a valid Activity Name</digi:trn>
			</c:set>
			alert('${translation}');
			return false;
		}
		if(document.getElementsByName("totalCost")[0].value<=0){
			<c:set var="translation">
				<digi:trn>Enter a valid Total Cost</digi:trn>
			</c:set>
			alert('${translation}');
			return false;
		}
		if(document.getElementsByName("totalCostCurrencyId")[0].value==-1){
			<c:set var="translation">
				<digi:trn>Enter a valid Currency for Total Cost</digi:trn>
			</c:set>
			alert('${translation}');		
			return false;
		}
		if(document.getElementsByName("deleteContrib")!=null){
			var items = document.getElementsByName("deleteContrib").length;
			for(var i=0; i< items; i++){
				if(document.getElementsByName("contrAmount")[i].value<=0){
					<c:set var="translation">
						<digi:trn>Enter a valid Amount</digi:trn>
					</c:set>
					alert('${translation}');
					return false;
				}
				if(document.getElementsByName("contrCurrId")[i].value==-1){
					<c:set var="translation">
						<digi:trn>Select a Currency</digi:trn>
					</c:set>
					alert('${translation}');
					return false;					
				}
				if(document.getElementsByName("contrFinTypeId["+i+"]")[0].value==0){
					<c:set var="translation">
						<digi:trn>Select Type of Assistance</digi:trn>
					</c:set>
					alert('${translation}');
					return false
				}
				if(document.getElementsByName("contrDonorName")[i].value==""){
					<c:set var="translation">
						<digi:trn>Select a Donor</digi:trn>
					</c:set>
					alert('${translation}');
					return false;
				}
				if(document.getElementsByName("contrFinInstrId["+i+"]")[0].value==0){
					<c:set var="translation">
						<digi:trn>Select a Financing Instrument</digi:trn>
					</c:set>
					alert('${translation}');
					return false;
				}
			}
		} else {
			<c:set var="translation">
				<digi:trn>Please add a contributor</digi:trn>
			</c:set>
			alert('${translation}');
			return false;
		}
		if(document.getElementsByName("dueDate")[0].value==""){
			<c:set var="translation">
				<digi:trn>Enter a valid Due Date</digi:trn>
			</c:set>
			alert('${translation}');
			return false;
		}
		
		return true;		
	}
	var responseSuccess2 = function(o){
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
			var content = document.getElementById("popinContent2");
		    //response = response.split("<!")[0];
			content.innerHTML = response;
		    //content.style.visibility = "visible";
			
			showContent2();
		}
	 
		var responseFailure2 = function(o){ 
		// Access the response object's properties in the 
		// same manner as listed in responseSuccess( ). 
		// Please see the Failure Case section and 
		// Communication Error sub-section for more details on the 
		// response object's properties.
			//alert("Connection Failure!"); 
		}  
	
	var callback2 =
	{ 
		success:responseSuccess2, 
		failure:responseFailure2 
	};
/*	function showContent2(){
		var element = document.getElementById("popin2");
		element.style.display = "inline";
		myPanel2.setBody(element);
		document.getElementById("popin2").scrollTop=0;
		myPanel2.show();
	}*/
	function showContent2(){
		var element = document.getElementById("popin2");
		element.style.display = "inline";
		if (panelStart2 < 1){
			myPanel2.setBody(element);
		}
		if (panelStart2 < 2){
			document.getElementById("popin2").scrollTop=0;
			myPanel2.show();
			panelStart2 = 2;
		}
		checkErrorAndClose2();
	}
	function checkErrorAndClose2(){
		if(checkAndClose2==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				myclose2();
				refreshPage2();
			}
			checkAndClose2=false;			
		}
	}
	function refreshPage2(){
		orgsAdded();
		//document.aimEditActivityForm.step.value = "13";
		//document.aimEditActivityForm.action = "/aim/addActivity.do?edit=true";
		//document.aimEditActivityForm.target = "_self";
		//document.aimEditActivityForm.submit();
	}
	function orgsAdded() {
		//var postString		= generateFields();
		//YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);

		<digi:context name="addEUActivity" property="context/module/moduleinstance/editEUActivity.do?new" />
		var url = "<%=addEUActivity %>?comment=" + "ccd" + "&edit=" + "true";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		
	}

	function myclose2(){
		var content = document.getElementById("popinContent2");
		content.innerHTML="";
		panelStart2=1;
		myPanel2.hide();
	
	}

	function closeWindow() {
		myclose2();
	}

	function showPanelLoading2(msg){
		myPanel2.setHeader(msg);		
		var content = document.getElementById("popinContent2");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent2();
	}
	function selectOrg(params1, params2, params3, params4) {
		var msg='\n<digi:trn key="aim:selectOrg">Select Organization</digi:trn>';
		showPanelLoading2(msg);
		YAHOOAmp.util.Connect.asyncRequest("POST", params1, callback2);
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

	function setValues(id,name){
		var valueHolder = document.getElementsByName('valueHoder')[0].value;
		var nameHolder = document.getElementsByName('nameHolder')[0].value;
		document.getElementById(valueHolder).value=id;
		document.getElementById(nameHolder).value=name;
		myclose2();
	}

	
	function setOrganization(id) {
		<digi:context name="selOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
	    //document.aimSelectOrganizationForm.action = "<%= selOrg %>&id="+id;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    //document.aimSelectOrganizationForm.submit();
	    checkAndClose2=true;
		var urlParams="<%=selOrg%>";
		var params="edit=true&orgSelReset=false&subAction=organizationSelected&id="+id;
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback2);
		    
		//return true;
	}
	
	function selectOrganization() {
		var flag = validate();
		if (flag == false)
			return false;

		
		<digi:context name="selOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
	    //document.aimSelectOrganizationForm.action = "<%= selOrg %>";
		//document.aimSelectOrganizationForm.target = window.opener.name;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    //document.aimSelectOrganizationForm.submit();
	    checkAndClose2=true;
		var url = "<%=selOrg %>"
		var params = "?edit=true&orgSelReset=false&subAction=organizationSelected"+getParams();    
		YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback2);
		
		return true;
	}

	function resetForm() {
		document.aimSelectOrganizationForm.ampOrgTypeId.value=-1;
		document.aimSelectOrganizationForm.keyword.value="";
		document.aimSelectOrganizationForm.tempNumResults.value=10;
	
	}




	function selectOrganizationPages(page) {
	   <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do" />
	   //var val = "<%=searchOrg%>";
	   //val = val + page;
	   //document.aimSelectOrganizationForm.action = val;

	   document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=page;
	   var urlParams="<%=searchOrg%>";
	   var params="edit=true&orgSelReset=false&subAction=selectPage&page="+page;
	   YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback2);
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
				 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
			    //document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
			    //document.aimSelectOrganizationForm.submit();
			    var url = "<%=searchOrg %>"
				var params = "?edit=true&subAction=search"+getParams();    
			    YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback2);
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
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
			 //url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";
		     //document.aimSelectOrganizationForm.action = url;
		     //document.aimSelectOrganizationForm.submit();
		     var url = "<%=searchOrg %>"
			 var params = "?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";    
			 YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback2);
		     
			 //return true;
		}
	}
		
	function searchAlphaAll(val) {
		if (document.aimSelectOrganizationForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimSelectOrganizationForm.tempNumResults.focus();
			  //return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
			  //  document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
		      var aux= document.aimSelectOrganizationForm.tempNumResults.value;
		      document.aimSelectOrganizationForm.tempNumResults.value=1000000;
		     //document.aimSelectOrganizationForm.submit();

			   var urlParams="<%=searchOrg%>";
			   var params="?edit=true&subAction=search&tempNumResults=1000000";
			   YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+params, callback2);
			      document.aimSelectOrganizationForm.tempNumResults.value=aux;		      
			  //return true;
		}
	}
	
	-->

</script>
<bean:define id="langBean" name="org.digijava.kernel.navigation_language" scope="request" type="org.digijava.kernel.entity.Locale" toScope="page" />
<bean:define id="lang" name="langBean" property="code" scope="page" toScope="page" />

<script type="text/javascript">
	var myCalendarModel = new DHTMLSuite.calendarModel();
	
	myCalendarModel.setLanguageCode('<bean:write name="lang" />'); 
	calendarObjForForm = new DHTMLSuite.calendar({callbackFunctionOnDayClick:'getDateFromCalendar',isDragable:false,displayTimeBar:false,calendarModelReference:myCalendarModel}); 
		
	function getDateFromCalendar(inputArray)
	{
		var references = calendarObjForForm.getHtmlElementReferences(); // Get back reference to form field.
		references.dueDate.value = inputArray.year + '-' + inputArray.month + '-' + inputArray.day;
		calendarObjForForm.hide();			
	}	

	function pickDate(buttonObj,inputObject)
	{
		calendarObjForForm.setCalendarPositionByHTMLElement(inputObject,0,inputObject.offsetHeight-80);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,'yyyy-mm-dd');	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('dueDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
	}
</script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />
