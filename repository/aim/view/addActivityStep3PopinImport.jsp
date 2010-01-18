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
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<div id="popinImport" style="display: none" >
	<div id="popinImportContent" class="content">
	</div>
</div>

<script type="text/javascript">
  YAHOOAmp.namespace("YAHOOAmp.amp");
	var panelStartImport;

  var myImportPanel = new YAHOOAmp.widget.Panel("importPopinPanel", {
			width:"1000px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });

	function initImportFundingScript() {
		var msg='\n<digi:trn key="aim:importFunding">Import Funding</digi:trn>';
		myImportPanel.setHeader(msg);
		myImportPanel.setBody("");
		myImportPanel.beforeHideEvent.subscribe(function() {
			panelStartImport=1;
		}); 
		myImportPanel.render(document.body);
		panelStartImport = 0;
	}
</script>

<style type="text/css">
	#popinImport .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	
</style>

<script language="JavaScript">
	var checkAndCloseImport = false;
	
    var responseSuccessImport = function(o){
		var response = o.responseText; 
		var content = document.getElementById("popinImportContent");
		content.innerHTML = response;
		showContentImport();
	}

	var responseFailureImport = function(o){ 
	}  
	var callbackImport = 
	{ 
		success:responseSuccessImport, 
		failure:responseFailureImport 
	};
	
	function showContentImport(){
		var element = document.getElementById("popinImport");
		
		
		element.style.display = "inline";
		if (panelStartImport < 1){
			myImportPanel.setBody(element);
		}
		if (panelStartImport < 2){
			document.getElementById("popinImport").scrollTop=0;
			myImportPanel.show();
			panelStartImport = 2;
		}
		checkErrorAndCloseImport();
	}

	function closeImport(){
		var content = document.getElementById("popinImportContent");
		content.innerHTML="";
		myImportPanel.hide();	
	
	}

	function showImportPanelLoading(msg){
		myImportPanel.setHeader(msg);		
		var content = document.getElementById("popinImportContent");
		content.innerHTML = '<div style="text-align: center">' + 
		'<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' + 
		'<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
		showContentImport();
	}

	function myImportFunding(orgId) {		
		
		myImportPanel.cfg.setProperty("width","880px");
		myImportPanel.cfg.setProperty("height","500px");		
		var msg='\n<digi:trn key="aim:importFunding">Import Funding</digi:trn>';
		showImportPanelLoading(msg);
		<digi:context name="importFunding" property="context/module/moduleinstance/importFunding.do"/>
		document.getElementById('orgId').value = orgId;
		var urlParams="<%=importFunding%>";
		var params = "funding.orgId="+ orgId+"&edit=true";
		
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callbackImport );
	}

	function importFormFunding() {
	    document.getElementsByName("aimEditActivityFormPopin")[0].submit();
	}

	function checkErrorAndCloseImport(){
		if(checkAndCloseImport==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				closeImport();
				refreshImport();
			}
			checkAndClose2=false;			
		}
	}
	
</script>