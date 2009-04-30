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
	    
	function initCompTypeScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
	}
	
	window.onload=initCompTypeScript();
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
	    document.aimComponentsTypeForm.submit();
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
	function addType(){
		var msg='\n<digi:trn>AMP - Add Component Type</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="addComponentType" property="context/module/moduleinstance/updateComponentType.do?event=add" />
		var url = "<%= addComponentType %>";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		return true;
	}

	function editType(id){
		var msg='\n<digi:trn>Currency Editor</digi:trn>';
		showPanelLoading(msg);
  
		<digi:context name="editComponentType" property="context/module/moduleinstance/updateComponentType.do?event=edit" />
		var url = "<%= editComponentType %>&id="
		url += id;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	
	-->
</script>
<script language="JavaScript">
function validate(){
  if ((document.aimComponentsTypeFormPopin.name.value).length == 0){
    alert("<digi:trn key="aim:errortypeName">Please Enter the name</digi:trn>");
    document.aimComponentsTypeFormPopin.name.focus();
    return false;
  }
  if ((document.aimComponentsTypeFormPopin.code.value).length == 0){
    alert("<digi:trn key="aim:errortypeCode">Please Enter the code</digi:trn>");
    document.aimComponentsTypeFormPopin.code.focus();
    return false;
  }
  return true;
}

function updateComponentsType(){
  var temp = validate();
  if (temp == true){
    document.aimComponentsTypeFormPopin.addBtn.disabled = true;
    <digi:context name="update" property="context/module/moduleinstance/updateComponentType.do?event=save" />
	var url = "<%=update%>";
	url += getParams();
	checkAndClose=true;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
  }
  
  return temp;
}
function getParams(){
	var ret="";
	ret +="&check="+document.getElementsByName('check')[0].value+
	"&id="+document.getElementsByName('id')[0].value+
	"&name="+document.getElementsByName('name')[0].value+
	"&code="+document.getElementsByName('code')[0].value+
	"&enable="+document.getElementsByName('enable')[0].value;
	
	return ret;	
}

</script>
