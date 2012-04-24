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
<div id="popinImport" class="invisible-item" >
	<div id="popinImportContent" class="content">
	</div>
</div>

<script type="text/javascript">
  YAHOO.namespace("YAHOO.amp");
	var panelStartImport;

  var myImportPanel = new YAHOO.widget.Panel("importPopinPanel", {
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

	var responseUploadImport = function(o){
		var response = o.responseText; 
		var content = document.getElementById("popinImportContent");
		content.innerHTML = response;
		showContentImport();
	}

	var callbackImport = 
	{ 
		upload:responseUploadImport, 
		success:responseSuccessImport, 
		failure:responseFailureImport 
	}

	
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
		
		YAHOO.util.Connect.asyncRequest("POST", urlParams+"?"+params, callbackImport );
	}

	function importFormFunding() {
		document.getElementsByName("funding.event")[0].value = "importFundingDetail";
		<digi:context name="addFunding" property="context/module/moduleinstance/importFundingDetail.do"/>
		
		var urlParams="<%=addFunding%>";
		var params = getParameters();
		if(!validateImport()) return false;
		YAHOO.util.Connect.setForm(document.getElementsByName("aimEditActivityFormPopin")[0], true);
		YAHOO.util.Connect.asyncRequest("POST", urlParams+"?"+params, callbackImport);
	    
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

//	function importFundingDetail(type) {
//
//		var flag = validateFundingExchangeRate();
//		if (flag == false) return false;
//
//		document.getElementsByName("funding.event")[0].value = "importFundingDetail";
//		<digi:context name="addFunding" property="context/module/moduleinstance/addFundingDetail.do"/>
//		
//		var urlParams="<%=addFunding%>";
//		var params = getParameters();
//		
//		YAHOO.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
//	 	
//	}

	function submitImportForm() {
		<digi:context name="addFunding" property="context/module/moduleinstance/fundingImported.do"/>
		var formAction="<%=addFunding%>";
		document.getElementsByName("aimEditActivityFormPopin")[0].action = formAction;
		document.getElementsByName("aimEditActivityFormPopin")[0].submit();
	}	
	
	function validateImport(){
		var errmsg1="<digi:trn key="aim:addFunding:errmsg:assitanceType">Type Of Assistance not selected</digi:trn>";
		var errmsg2="\n<digi:trn key="aim:addFunding:errmsg:fundOrgId">Funding Id not entered</digi:trn>";
		var errmsg3="\n<digi:trn key="aim:addFunding:errmsg:financeInstrument">Financing Instrument not selected</digi:trn>";
		var errmsg4="\n<digi:trn>Funding status not selected</digi:trn>";
		var errmsg5="\n<digi:trn>No file selected</digi:trn>";

		var fundId = trim(document.getElementById("orgFundingId").value);
		var assistType = trim(document.getElementsByName("funding.assistanceType")[0].value);
		var mod=trim(document.getElementsByName("funding.modality")[0].value);
		var fileImport = document.getElementsByName("fileImport")[0].value
		var fundStatus		= -1;
		var fundStatusTemp	= document.getElementsByName("funding.fundingStatus");
		
		if ( fundStatusTemp != null && fundStatusTemp.length != null && fundStatusTemp.length > 0 ) {
			fundStatus	= fundStatusTemp[0].value;
		}
	
		var errmsg='';
		if (assistType == 0) {
			errmsg+=errmsg1;
		}
	
		if (mod == 0) {
			errmsg+=errmsg3;
		}
		
		if (fundStatus == 0) 
			errmsg+=errmsg4;

		if(fileImport == "")
			errmsg+=errmsg5;
	
		if (errmsg!=''){
			alert (errmsg);
			document.getElementById("orgFundingId").focus();
			return false;
		}
		return true;
	}

	function getProjectionRows(){
		var j=0;
		var projected="mtefProjection["+j+"].projected";
		while(document.getElementsByName(projected)[0]!=null){
			j++;
			projected="mtefProjection["+j+"].projected"
		}
		return j;
	}
	
	function getCommitmentRows(){
		var j=0;
		var adjLabel="fundingDetail["+j+"].adjustmentType";
		while(document.getElementsByName(adjLabel)[0]!=null){
			j++;
			adjLabel="fundingDetail["+j+"].adjustmentType"
		}
		return j;
	}
	
    function getParameters(){
		var ret="";
		ret += "edit="+document.getElementsByName('edit')[0].value+
		   "&funding.dupFunding="+document.getElementsByName('funding.dupFunding')[0].value+
		   "&funding.event="+document.getElementsByName('funding.event')[0].value+
		   "&funding.transIndexId="+document.getElementsByName('funding.transIndexId')[0].value+
		   "&funding.numComm="+document.getElementsByName('funding.numComm')[0].value+
		   "&funding.numDisb="+document.getElementsByName('funding.numDisb')[0].value+
		   "&funding.numExp="+document.getElementsByName('funding.numExp')[0].value+
		   "&funding.numDisbOrder="+document.getElementsByName('funding.numDisbOrder')[0].value+
		   "&funding.numProjections="+document.getElementsByName('funding.numProjections')[0].value+
		   "&editAct="+document.getElementsByName('editAct')[0].value+
		   "&funding.firstSubmit="+document.getElementsByName('funding.firstSubmit')[0].value+
		   "&totDisbIsBiggerThanTotCom="+document.getElementsByName('totDisbIsBiggerThanTotCom')[0].value+
		   "&funding.isEditFunding="+document.getElementsByName('funding.isEditFunding')[0].value+
		   "&funding.assistanceType="+document.getElementsByName('funding.assistanceType')[0].value+
		   "&funding.orgFundingId="+document.getElementsByName('funding.orgFundingId')[0].value+
		   "&funding.modality="+document.getElementsByName('funding.modality')[0].value+
		   "&funding.fundingStatus="+document.getElementsByName('funding.fundingStatus')[0].value;
		var rows = getProjectionRows();
		for(i=0; rows!=0 && i<rows; i++){
			var projLabel="mtefProjection["+i+"].projected";
			var amountLabel="mtefProjection["+i+"].amount";
			var currLabel="mtefProjection["+i+"].currencyCode";
			var dateLabel="mtefProjection["+i+"].projectionDateLabel";
			ret += "&"+projLabel+"="+document.getElementsByName(projLabel)[0].value+
			"&"+amountLabel+"="+document.getElementsByName(amountLabel)[0].value+
			"&"+currLabel+"="+document.getElementsByName(currLabel)[0].value+
			"&"+dateLabel+"="+document.getElementsByName(dateLabel)[0].value;
		}
		rows=getCommitmentRows();
		for(i=0; rows!=0 && i<rows; i++){
			var adjLabel="fundingDetail["+i+"].adjustmentType";
			var transTypeLabel="fundingDetail["+i+"].transactionType";
			var transAmountLabel="fundingDetail["+i+"].transactionAmount";
			var currCodeLabel="fundingDetail["+i+"].currencyCode";
			var transDateLabel="fundingDetail["+i+"].transactionDate";
			var fixedExchRateLabel="fundingDetail["+i+"].fixedExchangeRate";
			var useFixRateLabel="fundingDetail["+i+"].useFixedRate";
			var disdOrderIdLabel="fundingDetail["+i+"].disbOrderId";
			var classificatioLabel="fundingDetail["+i+"].classification";
			var disbOrderRejLabel="fundingDetail["+i+"].disbursementOrderRejected";
			ret += "&"+adjLabel+"="+document.getElementsByName(adjLabel)[0].value+
			"&"+transTypeLabel+"="+document.getElementsByName(transTypeLabel)[0].value+
			"&"+transAmountLabel+"="+document.getElementsByName(transAmountLabel)[0].value+
			"&"+currCodeLabel+"="+document.getElementsByName(currCodeLabel)[0].value+
			"&"+transDateLabel+"="+document.getElementsByName(transDateLabel)[0].value;
			if(document.getElementsByName(fixedExchRateLabel)[0]!=null)
				ret+="&"+fixedExchRateLabel+"="+document.getElementsByName(fixedExchRateLabel)[0].value;
			if(document.getElementsByName(useFixRateLabel)[0]!=null)	
			    ret+="&"+useFixRateLabel+"="+document.getElementsByName(useFixRateLabel)[0].value;
			if(document.getElementsByName(disdOrderIdLabel)[0]!=null)
				ret+="&"+disdOrderIdLabel+"="+document.getElementsByName(disdOrderIdLabel)[0].value;
			if(document.getElementsByName(classificatioLabel)[0]!=null)
				ret+="&"+classificatioLabel+"="+document.getElementsByName(classificatioLabel)[0].value;
			if(document.getElementsByName(disbOrderRejLabel)[0]!=null)
				ret+="&"+disbOrderRejLabel+"="+document.getElementsByName(disbOrderRejLabel)[0].value;

		}
		if(document.getElementsByName('funding.fundingConditions')[0]!=null){
			ret+="&funding.fundingConditions="+document.getElementsByName('funding.fundingConditions')[0].value;
		}
		if(document.getElementsByName('funding.donorObjective')[0]!=null){
			ret+="&funding.donorObjective="+document.getElementsByName('funding.donorObjective')[0].value;
		}

		return ret;
    }  
	
</script>