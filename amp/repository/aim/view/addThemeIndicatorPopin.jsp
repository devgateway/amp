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

<jsp:include page="scripts/newCalendar.jsp" flush="true" />
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
		var myPanel2 = new YAHOOAmp.widget.Panel("newpopins", {
			width:"550px",
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
	    
	function initThemeIndicatorScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
		var msgP2='\n<digi:trn>Add Sector</digi:trn>';
		myPanel2.setHeader(msgP2);
		myPanel2.setBody("");
		myPanel2.beforeHideEvent.subscribe(function() {
			panelStart2=1;
		}); 
		myPanel2.render(document.body);
		panelStart2 = 0;
	}
	
	window.onload=initThemeIndicatorScript();
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
		var response = o.responseText; 
		var content = document.getElementById("popinContent");
		content.innerHTML = response;
		showContent();
	}
 
	var responseFailure = function(o){ 
		alert("Connection Failure!"); 
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
		document.aimThemeForm.submit();
	}

	function myclose(){
		var content = document.getElementById("popinContent");
		content.innerHTML="";
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent();
	}

	function addIndicator()
	{
		var msg='\n<digi:trn>Add Indicator</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="selCreateInd" property="context/module/moduleinstance/selectCreateIndicators.do" />
		var url = "<%=selCreateInd %>?addIndicatorForStep9=true";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		
	}
	function addIndicator(id){
		myPanel.cfg.setProperty("width","800px");
		//myPanel.cfg.setProperty("height","200px");
		var msg='\n<digi:trn>AMP Assign New Indicator</digi:trn>';
		showPanelLoading(msg);		
		<digi:context name="addIndicator" property="context/module/moduleinstance/assignNewIndicator.do" />
		var url = "<%= addIndicator %>?parentid=" + id + "&type=program";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}


	function editIndicator(id,parentid,type){
		var msg='\n<digi:trn>AMP Add/Edit data</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="viewEditIndicator" property="context/module/moduleinstance/viewEditIndicator.do" />
		var url = "<%=viewEditIndicator%>?id=" + id + "&parentid="+parentid+"&type=program&event=edit";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}

	function addData(id){
		myPanel.cfg.setProperty("width","600px");
		//myPanel.cfg.setProperty("height","200px");
		var msg='\n<digi:trn>AMP Add/Edit data</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do" />
		var url = "<%= addEditIndicator %>?parent=" + id;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
			
	////////////////
	var responseSuccess2 = function(o){
			var response = o.responseText; 
			var content = document.getElementById("popinContent2");
			content.innerHTML = response;
			showContent2();
		}
	 
		var responseFailure2 = function(o){ 
			alert("Connection Failure!"); 
		}  
	
	var callback2 =
	{ 
		success:responseSuccess2, 
		failure:responseFailure2 
	};

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
		  <digi:context name="justSubmit" property="context/module/moduleinstance/addEditData.do?action=justSubmit" /> 
		  var url = "<%=justSubmit%>";  
		  url += getParamsData();
		  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);	
	}

	function myclose2(){
		var content = document.getElementById("popinContent2");
		content.innerHTML="";
		panelStart2=1;
		myPanel2.hide();
	
	}

	function closeWindow2() {
		myclose2();
	}

	function showPanelLoading2(msg){
		myPanel2.setHeader(msg);		
		var content = document.getElementById("popinContent2");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent2();
	}
	
	-->

</script>

<script language="javascript">
///////////////// SEARCH INDICATORS EVENTS//////////////////////////
	function setOverImg(index){
	  document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-righthover1.gif"
	}
	
	function setOutImg(index){
	  document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif"
	}
	
	function sortByVal(value){
	  if(value!=null){
	    <digi:context name="viewIndicators" property="context/module/moduleinstance/viewIndicators.do" />
	    document.getElementById("sortBy").value=value;
	    document.aimThemeFormIndPopin.submit();
	  }
	}
	
	function validateSearch() {
		if (document.aimThemeFormIndPopin.indid.checked != null) { 
			if (document.aimThemeFormIndPopin.indid.checked == false) {
				alert("Please choose an indicator to assign");
				return false;
			}
		}
		else { // many
			var length = document.aimThemeFormIndPopin.indid.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimThemeFormIndPopin.indid[i].checked == true) {
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				alert("Please choose an indicator to assign");
				return false;
			}
		}
		return true;
	}
	
	function selectIndicators() {
		var flag = validateSearch();
		if (flag == false)
			return false;
		<digi:context name="selInd" property="context/module/moduleinstance/addThemeIndicator.do"/>
		var url = "<%= selInd %>"+"?event=assignIndicators";
		url += "&" + getParamsInd();
		checkAndClose=true;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	
	
	function editIndicator(id,type){
	  <digi:context name="viewEditIndicator" property="context/module/moduleinstance/viewEditIndicator.do" />
	  openURLinWindow("<%= viewEditIndicator %>?id="+id+"&type="+type,500, 300);
	}
	
	
	function checkNumeric(objName,comma,period,hyphen){
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
	
	function chkNumeric(objName,comma,period,hyphen){
	
	// only allow 0-9 be entered, plus any values passed
	// (can be in any order, and don't have to be comma, period, or hyphen)
	// if all numbers allow commas, periods, hyphens or whatever,
	// just hard code it here and take out the passed parameters
	
		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = objName;
		var allValid = true;
		var decPoints = 0;
		var allNum = "";
		for (i = 0;  i < checkStr.value.length;  i++)	{
			ch = checkStr.value.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
				if (ch == checkOK.charAt(j))
					break;
			if (j == checkOK.length){
				allValid = false;
				break;
			}
			if (ch != ",")
				allNum += ch;
		}
		if (!allValid){
			alertsay = "Please enter only numbers in the \"Number of results per page\"."
			alert(alertsay);
			return (false);
		}
	}
	
	function selectIndicatorsPages(page) {
		document.aimThemeFormIndPopin.selectedindicatorFromPages.value=page;
		<digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?edit=true"/>
		var url = "<%= searchInd %>";
		url += "&" + getParamsInd();
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		//document.aimThemeFormIndPopin.submit();
	}	
	
	function searchindicators() {
		if(checkNumeric(document.aimThemeFormIndPopin.tempNumResults	,'','','')==true){
			if (document.aimThemeFormIndPopin.tempNumResults.value == 0) {
				alert ("Invalid value at 'Number of results per page'");
				document.aimThemeFormIndPopin.tempNumResults.focus();
				return false;
			} else {
				<digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?action=search"/>
				document.aimThemeFormIndPopin.selectedindicatorFromPages.value=1;
				document.aimThemeFormIndPopin.alpha.value="";
				var url = "<%= searchInd %>";
				url += "&"+getParamsInd();
				YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
				//document.aimThemeFormIndPopin.submit();
				return true;
			}
		}
		else{ 
			return false;
		}
	}
	
	
	function searchAlpha(val) {
		if (document.aimThemeFormIndPopin.tempNumResults.value == 0) {
			alert ("Invalid value at 'Number of results per page'");
			document.aimEditActivityForm.tempNumResults.focus();
			return false;
		} else {
			<digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?"/>
			var url = "<%= searchInd %>" ;
			document.aimThemeFormIndPopin.alpha.value=val;
			document.aimThemeFormIndPopin.selectedindicatorFromPages.value=1;
			url += "&" + getParamsInd(); 
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		}
	}
	
	function searchAlphaAll(val) {
		if (document.aimThemeFormIndPopin.tempNumResults.value == 0) {
			alert ("Invalid value at 'Number of results per page'");
			document.aimThemeFormIndPopin.tempNumResults.focus();
			return false;
		} else {
			<digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?"/>
			var url  = "<%= searchInd %>";
		    var aux= document.aimThemeFormIndPopin.tempNumResults.value;
		    document.aimThemeFormIndPopin.tempNumResults.value=1000000;
		    url  += getParamsInd();
		    YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		    document.aimThemeFormIndPopin.tempNumResults.value=aux;
			return true;
		}
	}
		
	function clearform(){
		<digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?action=clear"/>
		var url = "<%= searchInd %>";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		//document.aimThemeFormIndPopin.submit();
	}
	
	function viewall(){
		<digi:context name="searchInd" property="context/module/moduleinstance/searchindicators.do?action=viewall"/>
		document.aimThemeFormIndPopin.selectedindicatorFromPages.value=1;
		document.aimThemeFormIndPopin.alpha.value="";
		var url = "<%= searchInd %>";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	function getParamsInd(){
		var ret="";
		ret += "step="+document.getElementsByName('step')[0].value+
			"&item="+document.getElementsByName('item')[0].value+
			"&selectedindicatorFromPages="+document.getElementsByName('selectedindicatorFromPages')[0].value+
			"&alpha="+document.getElementsByName('alpha')[0].value+
			"&sectorName="+document.getElementsByName('sectorName')[0].value+
			"&keyword="+document.getElementsByName('keyword')[0].value+
			"&tempNumResults="+document.getElementsByName('tempNumResults')[0].value;
		if(document.getElementsByName('indid')!=null){
			var indicators = document.getElementsByName('indid').length;
			for(var i=0; i<indicators; i++){
				if(document.getElementsByName('indid')[i].checked){
					ret += "&indid="+document.getElementsByName('indid')[i].value;
				}
			}
		}
		
		return ret;
	}
</script>
<script language="JavaScript">
////////////////////  Add/Edit Data ////////////// 
    function chkNumeric(frmContrl){
        var regEx=/^[0-9]*\.?[0-9]*$/;
        var errorMsg="<digi:trn>Please enter numeric value only</digi:trn>";
        if(!frmContrl.value.match(regEx)){
            alert(errorMsg);
            frmContrl.value = "";
            frmContrl.focus();
            return false;
        }
    }
function addNewData(){
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=addIndValue" />
  var url = "<%=addEditIndicator%>";
  url += getParamsData();
  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
}


function deleteData(ind){
  var flag = confirm("Delete this data?");
  if(flag == true){
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=delIndValue" />
  var url = "<%=addEditIndicator%>&index="+ind;
  url += getParamsData();
  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
  }
}

function saveIndicator(id){
	if (!validation()){
        return false;
	}
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=save" /> 
  var url = "<%=addEditIndicator%>";
  url += getParamsData();
  checkAndClose=true;
  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
}

function selectLocation(index){
	
  
  <digi:context name="justSubmit" property="context/module/moduleinstance/addEditData.do?action=justSubmit" /> 
  var url = "<%=justSubmit%>&index="+index;  
  url += getParamsData();
  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);    

  <digi:context name="selLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?action=justSubmit"/>  
  var url = "<%=selLoc%>&index="+index;
  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);
  
}

function validation(){
	var values=document.getElementsByTagName("select");
    var msg='<digi:trn jsFriendly="true">Please ensure that you enter at least 1 base and 1 target value</digi:trn>';
    var msgDate='<digi:trn jsFriendly="true">Please specify date</digi:trn>';
    var dates=document.getElementsByName("creationDate");
    for (var j=0;j<dates.length;j++){
        var date=dates[j];
        if(date.value==''){
            date.focus();
            alert(msgDate);
            return false;
        }

    }
	var baseValue=0;
	var actualValue=0;
	var targetValue=0;
	if(values!=null){
		for (var i=0;i<values.length;i++){
			
			if (values[i].selectedIndex==1){
				baseValue++;
			}else if(values[i].selectedIndex==2){
				targetValue++;
			}
		}	
	}
	//for every actual value we should have base and target values
	if(baseValue==0||targetValue==0){
        alert(msg);
		return false;	
	}
	return true;
}
function getParamsData(){
	var ret="";
	if(document.getElementsByName('valueType')!=null){
		var elem = document.getElementsByName('valueType').length;
		for(var i=0; i<elem; i++){
			ret +="&valueType="+document.getElementsByName('valueType')[i].value+
				"&valAmount="+(document.getElementsByName('valAmount')[i].value==""?0:document.getElementsByName('valAmount')[i].value)+
				"&creationDate="+document.getElementsByName('creationDate')[i].value;
		} 
	}
	return ret;	
}
</script>
<script language="javascript">
////////////////////////// SELECT LOCATION ////////////////////////////
	function closeWindow2() {
		myclose2();
	}
	
	function selectLocation2(){
	  document.getElementById("indAction").value="add";
	  //document.aimThemeFormLocPopin.target=window.opener.name;
	  //document.aimThemeFormLocPopin.submit();
	  //window.opener.document.aimThemeFormLocPopin.submit();
	  //window.close();
	  <digi:context name="justSubmit" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?" /> 
	  var url = "<%=justSubmit%>";  
	  url += getParamsLocation();
	  checkAndClose2=true;
	  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);    	  

	  
	}

	function countryChanged() {
		  document.aimThemeFormLocPopin.fill.value = "region";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  var url = "<%=selectLoc%>";
		  url += getParamsLocation();
		  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);
	}

	function regionChanged() {
	     document.aimThemeFormLocPopin.fill.value = "zone";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  var url = "<%=selectLoc%>";
		  url += getParamsLocation();
		  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);
	}

	function zoneChanged() {
		  document.aimThemeFormLocPopin.fill.value = "woreda";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  var url = "<%=selectLoc%>";
		  url += getParamsLocation();
		  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);
	}
	
	function levelChanged() {
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  var url = "<%=selectLoc%>";
		  url += getParamsLocation();
		  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);
				  
	}
	function getParamsLocation(){
		var ret="";
		ret += "&fill="+document.aimThemeFormLocPopin.fill.value;
		ret += "&action="+document.getElementById("indAction").value;
		if(document.getElementsByName('locationLevelIndex')[0]!=null){
			ret += "&locationLevelIndex="+document.getElementsByName('locationLevelIndex')[0].value;
		}
		if(document.getElementsByName('impRegion')[0]!=null){
			ret += "&impRegion="+document.getElementsByName('impRegion')[0].value;
		}

		if(document.getElementsByName('impZone')[0]!=null){
			ret += "&impZone="+document.getElementsByName('impZone')[0].value;
		}
		if(document.getElementsByName('impWoreda')[0]!=null){
			ret += "&impWoreda="+document.getElementsByName('impWoreda')[0].value;
		}
		
		return ret;
	}
</script>