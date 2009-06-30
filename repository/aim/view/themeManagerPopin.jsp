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

<c:set var="msgValidNumbers">
    <digi:trn key="aim:msgEnterNumericValues">Please enter numeric values for field </digi:trn>
</c:set>

<c:set var="programInernalFinancing">
	<digi:trn key="aim:Internal"> Internal financing </digi:trn>
</c:set>

<c:set var="programExternalFinancing">
	<digi:trn key="aim:External"> External financing </digi:trn> 
</c:set>

<c:set var="programTotalFinancing">
	<digi:trn key="aim:TotasFinance"> Total financing  </digi:trn> 
</c:set>

<c:set var="msgEnterPgrName">
<digi:trn key="aim:sgEnterPgrName">Please enter the program name </digi:trn>
</c:set>

<c:set var="msgEnterPgrType">
    <digi:trn key="aim:msgEnterPgrType">Please Select a  program type</digi:trn>
</c:set>

<c:set var="msgEnterCode">
    <digi:trn key="aim:msgEnterPgrCode">Please enter the program code</digi:trn>
</c:set>

<div id="popin" style="display: none">
	<div id="popinContent" class="content">
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
	var panelStart;
	var checkAndClose=false;	    
	    
	function initThemeMangarScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
	}
	
	window.onload=initThemeMangarScript();
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
	function addProgram()
	{
		var msg='\n<digi:trn>AMP - Add NewProgram</digi:trn>';
		showPanelLoading(msg);
		
		<digi:context name="addNewTh" property="context/module/moduleinstance/addTheme.do?event=add"/>
		var url = "<%=addNewTh%>";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		return true;
	}
    function editProgram(id){
      var msg='\n<digi:trn>Edit Program</digi:trn>';
      <digi:context name="editTh" property="context/module/moduleinstance/addTheme.do?event=edit"/>
      var url = "<%= editTh%>&themeId=" + id;;
      YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
}
	function addSubProgram(rutId,id,level,name)
	{
		<digi:context name="subProgram" property="context/module/moduleinstance/addTheme.do?event=addSubProgram"/>
		var url = "<%= subProgram %>&themeId=" + id + "&indlevel=" + level + "&indname=" + name + "&rootId=" + rutId;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	-->
</script>
<script language="JavaScript">
	<!--
		
	function validateAdd() 
	{
		var validNumbers='0123456789,.'
		if (trim(document.aimThemeFormPopin.programName.value).length == 0) 
		{
			alert("${msgEnterPgrName}" );
			document.aimThemeFormPopin.programName.focus();
			return false;
		}	
		if (trim(document.aimThemeFormPopin.programCode.value).length == 0) 
		{
			alert("${msgEnterCode}");
			document.aimThemeFormPopin.programCode.focus();
			return false;
		}		
		if (document.aimThemeFormPopin.programTypeCategValId.value == 0) 
		{
			alert("${msgEnterPgrType}");
			document.aimThemeFormPopin.programTypeCategValId.focus();
			return false;
		}		

		if(document.aimThemeFormPopin.programInernalFinancing != null){
			document.aimThemeFormPopin.programInernalFinancing.value=trim(document.aimThemeFormPopin.programInernalFinancing.value);
			if (document.aimThemeFormPopin.programInernalFinancing.value.length > 0){
					var text=document.aimThemeFormPopin.programInernalFinancing.value;
					for (i=0; i < text.length; i++){
						if (validNumbers.indexOf(text.charAt(i)) ==-1){
						alert("${msgValidNumbers} ${programInernalFinancing}");
						document.aimThemeFormPopin.programInernalFinancing.focus();
						return false;
						}
					}			
				}else{
				document.aimThemeFormPopin.programInernalFinancing.value=0;
			} 
	
			document.aimThemeFormPopin.programExternalFinancing.value=trim(document.aimThemeFormPopin.programExternalFinancing.value);
			if (document.aimThemeFormPopin.programExternalFinancing.value.length > 0){
				var text=document.aimThemeFormPopin.programExternalFinancing.value;
				for (i=0; i < text.length; i++){
					if (validNumbers.indexOf(text.charAt(i)) ==-1){
					alert("${msgValidNumbers} ${programExternalFinancing}");
					document.aimThemeFormPopin.programExternalFinancing.focus();
					return false;
					}
				}			
			} else{
				document.aimThemeFormPopin.programExternalFinancing.value=0;
				}
	
			document.aimThemeFormPopin.programTotalFinancing.value=trim(document.aimThemeFormPopin.programTotalFinancing.value);
				if (document.aimThemeFormPopin.programTotalFinancing.value.length > 0){
				var text=document.aimThemeFormPopin.programTotalFinancing.value;
				for (i=0; i < text.length; i++){
					if (validNumbers.indexOf(text.charAt(i)) ==-1){
					alert("${msgValidNumbers} ${programTotalFinancing}");
					document.aimThemeFormPopin.programTotalFinancing.focus();
					return false;
					}
				}
			} else{
				document.aimThemeFormPopin.programTotalFinancing.value=0;
			}
		}
			
		return true;
	}
	function getParams(){
		var ret = "";
		ret += "event="+document.getElementsByName('event')[0].value+
			"&themeId="+document.getElementsByName('themeId')[0].value+
			"&parentId="+document.getElementsByName('parentId')[0].value+
			"&prgLevel="+document.getElementsByName('prgLevel')[0].value+
			"&programName="+document.getElementsByName('programName')[0].value+
			"&programDescription="+document.getElementsByName('programDescription')[0].value;
			if(document.getElementsByName('programInernalFinancing')[0]!=null){
				ret += "&programInernalFinancing="+document.getElementsByName('programInernalFinancing')[0].value+
					"&programExternalFinancing="+document.getElementsByName('programExternalFinancing')[0].value+
					"&programTotalFinancing="+document.getElementsByName('programTotalFinancing')[0].value;
			}
			ret += "&programLeadAgency="+document.getElementsByName('programLeadAgency')[0].value+
			"&programCode="+document.getElementsByName('programCode')[0].value+
			"&programTypeCategValId="+document.getElementsByName('programTypeCategValId')[0].value+
			"&programTargetGroups="+document.getElementsByName('programTargetGroups')[0].value+
			"&programBackground="+document.getElementsByName('programBackground')[0].value+
			"&programObjectives="+document.getElementsByName('programObjectives')[0].value+
			"&programOutputs="+document.getElementsByName('programOutputs')[0].value+
			"&programBeneficiaries="+document.getElementsByName('programBeneficiaries')[0].value+
			"&programEnvironmentConsiderations="+document.getElementsByName('programEnvironmentConsiderations')[0].value;
		
		return ret;		
	}

	function saveProgram(){
		var temp = validateAdd();
		if (temp == true){
			<digi:context name="addThm" property="context/module/moduleinstance/addTheme.do?"/>
			var url = "<%=addThm%>";
			url += getParams();
			var msg='\n<digi:trn>AMP - Add NewProgram</digi:trn>';
			showPanelLoading(msg);
			checkAndClose=true;
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
				
		}
		return true;
	}	
-->

</script>
