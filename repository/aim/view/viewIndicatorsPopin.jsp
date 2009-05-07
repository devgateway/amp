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
	    
	function initViewIndicatorsScript() {
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
	
	window.onload=initViewIndicatorsScript();
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
		document.aimViewIndicatorsForm.submit();
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
<script language="JavaScript">
    <!--
    var typeSector=0;
	function addIndicator(){
      typeSector=2;
	  var msg='\n<digi:trn>AMP Add New Indicator</digi:trn>';
	  showPanelLoading(msg);
	  <digi:context name="addIndicator" property="context/module/moduleinstance/addNewIndicator.do" />
	  var url = "<%= addIndicator %>";
	  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);	
  
	}
	
	function editIndicator(id){
	  typeSector=1;
	  var msg='\n<digi:trn>AMP View/Edit Indicator</digi:trn>';
	  showPanelLoading(msg);
	  <digi:context name="viewEditIndicator" property="context/module/moduleinstance/viewEditIndicator.do" />
	  var url = "<%= viewEditIndicator %>?id="+id;
	  YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);	  
	}
	-->

</script>
<script language="javascript">
<!--
function saveIndicator(){
	if(document.getElementById("txtName").value==""){
		<c:set var="translation">
		<digi:trn key="admin:enterName">Please enter name</digi:trn>
		</c:set>
		alert("${translation}");
		return false;
	}

	if(document.getElementById("txtCode").value==""){
		<c:set var="translation">
		<digi:trn key="admin:enterCode">Please enter code</digi:trn>
		</c:set>
		alert("${translation}");
		return false;
	}
	var length = document.aimNewIndicatorForm.selActivitySector.length;		
	var Sector;
	if(!length){
		<c:set var="translation">
		<digi:trn key="admin:addSector">Please add Sectors</digi:trn>
		</c:set>
		alert("${translation}");
		return false;
	}else{
		for(i = 0; i<length; i++){
			Sector = document.aimNewIndicatorForm.selActivitySector[i].value;
			document.getElementById("hdnselActivitySectors").value = Sector;
		}
	}
	<digi:context name="addInd" property="context/module/moduleinstance/viewEditIndicator.do?action=save" />
	checkAndClose=true;
	var url = "<%=addInd%>";
	url += getParams();	
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
}

function validate(field) {
	<c:set var="translation">
	<digi:trn key="admin:chooseSectorToRemove">Please choose a sector to remove</digi:trn>
	</c:set>
	if (field == 2){  // validate sector
		if (document.aimNewIndicatorForm.selActivitySector.checked != null) {
			if (document.aimNewIndicatorForm.selActivitySector.checked == false) {
				alert("${translation}");
				return false;
			}
		} else {
			var length = document.aimNewIndicatorForm.selActivitySector.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimNewIndicatorForm.selActivitySector[i].checked == true) {
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
}

function removeSelSectors() {
	var flag = validate(2);
	if (flag == false) return false;
	<digi:context name="remSecEdit" property="context/module/moduleinstance/removeIndicatorEditSectors.do?edit=true" />
	<digi:context name="remSecAdd"  property="context/module/moduleinstance/removeIndicatorSelSectors.do?edit=true" />
	var url;
	if(typeSector==1){//edit
		url = "<%= remSecEdit %>";
	}
	else{//add
		url = "<%= remSecAdd %>";
	}

	url += getParams()+getSelectedSectors();
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
}

function addSectors() {
	var msg='\n<digi:trn>AMP - Select Sector</digi:trn>';
	showPanelLoading2(msg);
	<digi:context name="addSectorEdit" property="context/module/moduleinstance/editSectorForind.do?edit=true" />
	<digi:context name="addSectorAdd" property="context/module/moduleinstance/selectSectorForind.do?edit=true" />
	var url;
	if(typeSector==1){//edit
		url = "<%= addSectorEdit %>";
	}
	else{//add
		url = "<%= addSectorAdd %>";
	}
	url += getParams();
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);
}
function getParams(){
	var ret="";
	if(document.aimNewIndicatorForm.prjStatus!=null){
		ret += "&prjStatus="+document.aimNewIndicatorForm.prjStatus.value;
	}
	if(document.aimNewIndicatorForm.prgStatus!=null){
		ret += "&prgStatus="+document.aimNewIndicatorForm.prgStatus.value;
	}
	if(document.aimNewIndicatorForm.themeId!=null){
		ret += "&themeId="+document.aimNewIndicatorForm.themeId.value;
	}
	if(document.aimNewIndicatorForm.selActivitySector!=null){
		ret += "&selActivitySector="+document.aimNewIndicatorForm.selActivitySector.value;
	}
	if(document.aimNewIndicatorForm.trType!=null){
		ret += "&trType="+document.aimNewIndicatorForm.trType.value;
	}
	if(document.aimNewIndicatorForm.category!=null){
		ret += "&category="+document.aimNewIndicatorForm.category.value;
	}
	
	ret += "&name="+document.aimNewIndicatorForm.name.value+
	"&description="+document.aimNewIndicatorForm.description.value+
	"&code="+document.aimNewIndicatorForm.code.value+
	"&type="+document.aimNewIndicatorForm.type.value+
	"&date="+document.aimNewIndicatorForm.date.value;//+
	//"&selectedActivityId="+document.aimNewIndicatorForm.selectedActivityId.value;
	return ret;
}
function getSelectedSectors(){
	var ret="";
	if(document.getElementsByName('selActivitySector')!=null){
		var sectors = document.getElementsByName('selActivitySector').length;
		for(var i=0; i<sectors; i++){
			if(document.getElementsByName('selActivitySector')[i].checked){
				ret += "&selActivitySector="+document.getElementsByName('selActivitySector')[i].value;
			}
		}
	}
	return ret;
}
-->
</script>

<script language="JavaScript">
/////////////////////// ADD SECTORS //////////////////
	<!--

	function selectSector() {
		var check = checkSectorEmpty();
		<digi:context name="addSectorEdit" property="context/module/moduleinstance/sectorSelectedForindEdit.do?edit=true"/>;
		<digi:context name="addSectorAdd" property="context/module/moduleinstance/sectorSelectedForind.do?edit=true" />;
		var url;
		if(check)
		{
			if(typeSector==1){//edit
				url = "<%= addSectorEdit %>";
			}
			else{//add
				url = "<%= addSectorAdd %>";
			}
			url += getParamsSector();
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
			closeWindow2();
		}
	}	

	function reloadSector(value) {
		if (value == 1) {
			document.aimNewIndicatorSectorForm.sector.value = -1;
		}
		<digi:context name="selSectorEdit" property="context/module/moduleinstance/editSectorForind.do?edit=true"/>
		<digi:context name="selSectorAdd" property="context/module/moduleinstance/selectSectorForind.do?edit=true"/>
		var url;
		if(typeSector==1){//edit
			url = "<%= selSectorEdit %>";
		}
		else{//add
			url = "<%= selSectorAdd %>";
		}
		url += getParamsSector();
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);									
	}	
	function checkSectorEmpty() {
		var sectorFlag = true;
		if(document.aimNewIndicatorSectorForm.sector.value == -1) {
			alert("Please Select a sector First")
			sectorFlag = false;
		}
		return sectorFlag;
	}
	function checkEmpty() {
		var flag=true;
		if(trim(document.aimNewIndicatorSectorForm.keyword.value) == "")
		{
			alert("Please Enter a Keyword....");
			flag=false;
		}
		if(trim(document.aimNewIndicatorSectorForm.tempNumResults.value) == 0)
		{
			alert("Invalid value at 'Number of results per page'");
			flag=false;
		}

		return flag;
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


	function getParamsSector(){
		var ret="";
		ret += "&sectorReset="+document.getElementsByName('sectorReset')[0].value+
		"&sectorScheme="+document.getElementsByName('sectorScheme')[0].value+
		"&sector="+document.getElementsByName('sector')[0].value;
		return ret;
	}

	-->

</script>
<script language="JavaScript">
/////////////////////// ADD NEW INDICATOR //////////////////
<!--
function addNewIndicator(){
	
	if(document.getElementById("txtName").value==""){
		<c:set var="translation">
		<digi:trn key="admin:enterName">Please enter name</digi:trn>
		</c:set>
		alert("${translation}");
		return false;
	}

	if(document.getElementById("txtCode").value==""){
		<c:set var="translation">
		<digi:trn key="admin:enterCode">Please enter code</digi:trn>
		</c:set>
		alert("${translation}");
		return false;
	}
	if(document.aimNewIndicatorForm.type.value!="A" && document.aimNewIndicatorForm.type.value!="D"){
		<c:set var="translation">
		<digi:trn key="admin:selectIndicatorType">Please Select Indicator Type</digi:trn>
		</c:set>
		alert("${translation}");
		return false;
	}
	var length = document.aimNewIndicatorForm.selActivitySector.length;		
	var Sector;
	
	if(!length){
		<c:set var="translation">
		<digi:trn key="admin:addSector">Please add Sectors</digi:trn>
		</c:set>
		alert("${translation}");
		return false;
	}else{
		for(i = 0; i<length; i++){
			Sector = document.aimNewIndicatorForm.selActivitySector[i].value;
			document.getElementById("hdnselActivitySectors").value = Sector;
		}
	} 
	
	<digi:context name="addInd" property="context/module/moduleinstance/addNewIndicator.do?action=add" />	
	var url = "<%= addInd %>";
	url += getParams();
	checkAndClose=true;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);

}

-->

</script>
