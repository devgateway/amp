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
	    
	function initCurrencyManagerScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
	}
	
	window.onload=initCurrencyManagerScript();
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
		document.aimCurrencyForm.submit();
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
	function addNewCurrency() {
		var msg='\n<digi:trn>Currency Editor</digi:trn>';
		showPanelLoading(msg);
		
		<digi:context name="add" property="context/module/moduleinstance/updateCurrency.do" />
		var url = "<%= add %>~doAction=new~closeFlag=false";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}

	function editCurrency(code) {
		var msg='\n<digi:trn>Currency Editor</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="add" property="context/module/moduleinstance/updateCurrency.do" />
		var url = "<%= add %>~closeFlag=false~doAction=show~currencyCode=";
		url += code;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	-->
</script>

<script language="JavaScript">

function validateData() {
  if (isEmpty(document.aimCurrencyFormPopin.currencyCode.value) ||
  checkCode(document.aimCurrencyFormPopin.currencyCode.value)== false) {
    alert("Invalid currency code entered");
    document.aimCurrencyFormPopin.currencyCode.focus();
    return false;
  }
  if (isEmpty(document.aimCurrencyFormPopin.currencyName.value) ||
  checkName(document.aimCurrencyFormPopin.currencyName.value)== false) {
    alert("Invalid currency name entered");
    document.aimCurrencyFormPopin.currencyName.focus();
    return false;
  }
  var cn=document.getElementById("lstCountry");
  if(cn!=null){
    if(cn.value==-1){
      alert("Please select country");
      cn.focus();
      return false;
    }
  }
  return true;
}

function saveCurrency() {
  var valid = validateData();
  if (valid != false) {
    <digi:context name="back" property="context/module/moduleinstance/saveCurrency.do" />
    var url = "<%= back %>";
    url += "?" + getParams();
    checkAndClose = true;
    YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
  }
  return valid;

}

function checkRate(val){
  if(val.match("[^0-9.,]")){
    return false;
  }
  return true;
}

function checkName(val){
  if(val.match("[^a-zA-ZÁ-ÿ ]")){
    return false;
  }
  return true;
}

function checkCode(val){
  if(val.match("[^A-Z]")){
    return false;
  }else if(val.length>3){
    return false;
  }
  return true;
}

function closePopup() {
	  closeWindow();
}
function getParams(){
	ret="";
	ret += "id="+document.getElementsByName('id')[0].value+
	"&closeFlag="+document.getElementsByName('closeFlag')[0].value+
	"&currencyCode="+document.getElementsByName('currencyCode')[0].value+
	"&currencyName="+document.getElementsByName('currencyName')[0].value+
	"&countryId="+document.getElementsByName('countryId')[0].value;
	return ret;
}
</script>
