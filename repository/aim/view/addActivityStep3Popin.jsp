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
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFundingPopin.js"/>"></script>

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
	var panelStart=0;
	var checkAndClose=false;
	function initFundingScript() {
		var msg='\n<digi:trn key="aim:addSector">Add Sectors</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		
		myPanel.render(document.body);
		
		myPanel2.setHeader(msg);
		myPanel2.setBody("");
		
		myPanel2.render(document.body);

	}
	var myPanel2 = new YAHOOAmp.widget.Panel("newpopins", {
		width:"300px",
		height:"200px",
		fixedcenter: true,
	    constraintoviewport: false,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    });

	//this is called from editActivityMenu.jsp
	//window.onload=initSectorScript();
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
	function showContent2(){
		var element = document.getElementById("popin2");
		element.style.display = "inline";
		myPanel2.setBody(element);
		document.getElementById("popin2").scrollTop=0;
		myPanel2.show();
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
		document.aimEditActivityForm.step.value = "3";
		document.aimEditActivityForm.action = "/aim/addActivity.do?edit=true";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}

	function myclose(){
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
	}
	function myclose2(){
		myPanel2.hide();	
	
	}
	function closeWindow2() {
		myclose2();
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent();
	}

	function myAddFunding(orgId) {		
		myPanel.cfg.setProperty("width","1000px");
		myPanel.cfg.setProperty("height","500px");		
		var msg='\n<digi:trn key="aim:addFunding">Add Funding</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="addFunding" property="context/module/moduleinstance/addFunding.do"/>
		document.getElementById('orgId').value = orgId;
		var urlParams="<%=addFunding%>";
		var params = "orgId="+ orgId+"&edit=true";
		
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
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
		   "&ignoreDistBiggerThanComm="+document.getElementsByName('ignoreDistBiggerThanComm')[0].value+
		   "&funding.isEditFunfing="+document.getElementsByName('funding.isEditFunfing')[0].value+
		   "&funding.assistanceType="+document.getElementsByName('funding.assistanceType')[0].value+
		   "&funding.orgFundingId="+document.getElementsByName('funding.orgFundingId')[0].value+
		   "&funding.modality="+document.getElementsByName('funding.modality')[0].value;
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

    function validateAmounts(){
    	var totalComms	= 0;
    	var totalDisbs	= 0;
    	var rows = getCommitmentRows();
    	for(i=0; rows!=0 && i<rows; i++){
    		var adjLabel="fundingDetail["+i+"].adjustmentType";
			var transAmountLabel="fundingDetail["+i+"].transactionAmount";
			var transTypeLabel="fundingDetail["+i+"].transactionType";
			var amount = 0;
	    	amount = (document.getElementsByName(transAmountLabel)[0].value) * 1;
	    	if ((document.getElementsByName(transTypeLabel)[0].value == 0) && (document.getElementsByName(adjLabel)[0].value == 1)) {
				totalComms	+= amount;
			} else {
				if ((document.getElementsByName(transTypeLabel)[0].value == 1) && (document.getElementsByName(adjLabel)[0].value == 1)) {
					totalDisbs	+= amount;
				}
			}
		}
		if (totalDisbs > totalComms) {
			var Warn="<digi:trn key="aim:addFunding:warn:disbSupCom">Sum of Disbursments is bigger than sum of commitments. Do you wish to proceed?</digi:trn>";
			if(confirm(Warn))
				{
					return true;
				} else {
					return false;	
				}
		} else {
			return true;
		}
   	}
    
	function addMTEFProjection() {
		document.getElementsByName("funding.event")[0].value = "addProjections";
		<digi:context name="addFunding" property="context/module/moduleinstance/addMTEFProjection.do"/>
		
		var urlParams="<%=addFunding%>";
		var params = getParameters();
		
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
	}
	function removeMTEFProjection(index) {
		var flag = confirm("<digi:trn key="aim:addFunding:warn:removeproj">Are you sure you want to remove the selected projection ?</digi:trn>");
		if(flag != false) {
			document.getElementsByName("funding.event")[0].value = "delProjection";
			document.getElementById('transIndexId').value=index;
			<digi:context name="addFunding" property="context/module/moduleinstance/addMTEFProjection.do"/>
			
			var urlParams="<%=addFunding%>";
			var params = getParameters();
			
			YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
			
		}
	}
	function addFundingDetail(type) {

		var flag = validateFundingExchangeRate();
		if (flag == false) return false;

		if (type == 0) {
			
			document.getElementsByName("funding.event")[0].value = "addCommitments";
		} else if (type == 1) {
			document.getElementsByName("funding.event")[0].value = "addDisbursements";
		} else if (type == 2) {
			document.getElementsByName("funding.event")[0].value = "addExpenditures";
		} else if (type == 4) {
			document.getElementsByName("funding.event")[0].value = "addDisbursementOrders";
		}
		<digi:context name="addFunding" property="context/module/moduleinstance/addFundingDetail.do"/>
		
		var urlParams="<%=addFunding%>";
		var params = getParameters();
		
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
	 	
	}
	function useFixedRateClicked(field1,field2) {
		
		var fld1 = document.getElementById(field1);
		var fld2 = document.getElementById(field2);
		
		
		if (fld1.disabled == true) {
			fld1.disabled = false;
			fld2.value=true;
		} else {
			fld1.disabled = true;
			fld2.value=false;
		}
	}

	function trim(s) {
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  	}
	function removeFundingDetail(index,type) {
		<c:set var="translation">
			<digi:trn key="aim:areYouSureRemoveTransaction">Are you sure you want to remove the selected transaction ?</digi:trn>
		</c:set>
		var flag = confirm("${translation}");
		if(flag != false) {
			if (type == 0) {
				document.getElementsByName("funding.event")[0].value = "delCommitments";
			} else if (type == 1) {
				document.getElementsByName("funding.event")[0].value = "delDisbursements";
			} else if (type == 2) {
				document.getElementsByName("funding.event")[0].value = "delExpenditures";
			} else if (type == 4) {
				document.getElementsByName("funding.event")[0].value = "delDisbursementOrders";
			}
			document.getElementById('transIndexId').value=index;
			
			<digi:context name="addFunding" property="context/module/moduleinstance/addFundingDetail.do"/>
			
			var urlParams="<%=addFunding%>";
			var params = getParameters();
			
			YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
			
		}
	}
	var isAlreadySubmitted = false;

	function addNewFunding() {
		if(!isAlreadySubmitted){
		var errmsg1="<digi:trn key="aim:addFunding:errmsg:assitanceType">Type Of Assistance not selected</digi:trn>";
		var errmsg2="\n<digi:trn key="aim:addFunding:errmsg:fundOrgId">Funding Id not entered</digi:trn>";
		var errmsg3="\n<digi:trn key="aim:addFunding:errmsg:financeInstrument">Financing Instrument not selected</digi:trn>";
        var msgEnterAmount="\n<digi:trn key="aim:addFunding:errmsg:enterAmount">Please enter the amount for the transaction</digi:trn>";
		var msgInvalidAmount="\n<digi:trn key="aim:addFunding:errmsg:invalidAmount">Invalid amount entered for the transaction</digi:trn>";
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
		var msgConfirmFunding="<digi:trn key="aim:addFunding:errmsg:confirmFunding">All funding information should be entered in thousands '000'. Do you wish to proceed with your entry?</digi:trn>";
</gs:test>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
		var msgConfirmFunding="";
</gs:test>
		//var msgConfirmFunding ="\n<digi:trn key="aim:addFunding:errmsg:enterDate">Please enter the transaction date for the transaction</digi:trn>";
		var msgEnterDate="\n<digi:trn key="aim:addFunding:errmsg:enterDate">Please enter the transaction date for the transaction</digi:trn>";
		//var msgEnterDate="qsfgqsg";

		var flag = validateFundingTrn(errmsg1,errmsg2,errmsg3,msgEnterAmount,msgInvalidAmount,msgEnterDate,"<%=FormatHelper.getDecimalSymbol()%>","<%=FormatHelper.getGroupSymbol()%>",msgConfirmFunding);
		
		if (flag == false) return false;
		//<digi:context name="fundAdded" property="context/module/moduleinstance/fundingAdded.do?edit=true" />;
		//document.aimEditActivityForm.action = "<%= fundAdded %>";
		//document.aimEditActivityForm.target = "_self";

		<digi:context name="fundAdded" property="context/module/moduleinstance/fundingAdded.do"/>
		
		var urlParams="<%=fundAdded%>";
		var params = getParameters()+"&edit=true";
		checkAndClose=true;
		if (validateAmounts()) {
			YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
			isAlreadySubmitted = true;
			
			return true;
		} else {
			return false;
		}
			//validateFormatUsingSymbos();
		}
	}
	function fnOnEditItem(index, orgId,fundId)	{
		myPanel.cfg.setProperty("width","1000px");
		myPanel.cfg.setProperty("height","500px");		
	
		var msg='\n<digi:trn key="aim:editFunding">Edit Funding</digi:trn>';
		showPanelLoading(msg);

		<digi:context name="editItem" property="context/module/moduleinstance/editFunding.do"/>
		var urlParams="<%=editItem%>";
		var params = "funding.orgId=" + orgId + "&funding.offset=" + index+"&edit=true";;
		document.aimEditActivityForm.prevOrg.value = orgId;
		document.getElementById('fundingId').value = fundId;

		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback );
	}

	function fnOnDeleteItem(orgId,fundId)	{
		<digi:context name="remItem" property="context/module/moduleinstance/removeFunding.do"/>
		document.aimEditActivityForm.action = "<%= remItem %>?fundOrgId=" + orgId + "&fundId=" + fundId+"&edit=true";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}
    function addDisbOrderToContract(indexId) {
        document.getElementById('transIndexId').value = indexId;
		var params = getParameters();
		<digi:context name="editItem" property="context/module/moduleinstance/addDisbOrderToContract.do"/>
		var urlParams="<%=editItem%>";
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback2 );
    }
    function addEvent() {
//    	  document.aimEditActivityForm.event.value = "Add";
//    	  document.aimEditActivityForm.action= "/addDisbOrderToContract.do";
//    	  document.aimEditActivityForm.target = "_self";
//    	  document.aimEditActivityForm.submit();
    	  return true;
    	 
   	 }
    function myAddPropFunding(){
		myPanel.cfg.setProperty("width","600px");
		myPanel.cfg.setProperty("height","200px");
		var msg='\n<digi:trn key="aim:propossedProjectCost">Propossed Project Cost</digi:trn>';
		showPanelLoading(msg);
	    <digi:context name="addProposedFunding" property="context/module/moduleinstance/editProposedFunding.do" />
	    var urlParams="<%=addProposedFunding%>";
	    YAHOOAmp.util.Connect.asyncRequest("POST", urlParams, callback );
    }
    function getPropCostParams(){
		ret="";
		ret += "edit="+document.getElementsByName('edit')[0].value+
		"&funding.proProjCost.funAmount="+document.getElementsByName('funding.proProjCost.funAmount')[0].value+
		"&funding.proProjCost.currencyCode="+document.getElementsByName('funding.proProjCost.currencyCode')[0].value+
		"&funding.proProjCost.funDate="+document.getElementsByName('funding.proProjCost.funDate')[0].value;
		return ret;
    }
    function resetPropFunding(){
    	document.aimEditActivityFormPropCost.funDate.value="";
    	document.aimEditActivityFormPropCost.funAmount.value="";
    }        
    function addNewPropFunding(){
		var fna=aimEditActivityFormPropCost.funAmount.value;
	    var fnd=aimEditActivityFormPropCost.funDate.value;
	    if(fna==""){
	        <c:set var="message">
	        <digi:trn key="aim:enterAmount">Please enter amount</digi:trn>
	        </c:set>
	        alert("${message}");
	        return false;
	      }else if(!checkAmountUsingSymbol(fna,"<%=FormatHelper.getDecimalSymbol()%>","<%=FormatHelper.getGroupSymbol()%>")){
	        <c:set var="message">
	        <digi:trn key="aim:invalidAmountValue">Invalid amount value</digi:trn>
	        </c:set>
	        alert("${message}");
	        return false;
	      }
	      if(fnd==""){
	        <c:set var="message">
	        <digi:trn key="aim:selectDate">Please select date</digi:trn>
	        </c:set>
	        alert("${message}");
	        return false;
	      }
	      //<digi:context name="fundAdded" property="context/module/moduleinstance/addProposedFunding.do" />;
	      //document.aimEditActivityForm.action = "<%= fundAdded %>";
	      //document.aimEditActivityForm.target=window.opener.name;
	      //document.aimEditActivityForm.submit();
	      //window.close();
	      <digi:context name="fundAdded" property="context/module/moduleinstance/addProposedFunding.do" />;
		    var urlParams="<%=fundAdded%>";
		    checkAndClose=true;
		    YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+getPropCostParams(), callback );
		    //document.aimEditActivityForm.submit();
	      return true;
		}
	   function checkAmountUsingSymbol(amount,decimalSymbol,groupSymbol){
		var validChars= "0123456789"+decimalSymbol+groupSymbol;
		for (i = 0;  i < amount.length;  i++) {
			var ch = amount.charAt(i);
			if (validChars.indexOf(ch)==-1){
				return false;
				break
			}
		}
			return true;
	   }
		function  addOrgPopin(param1, param2, param3){
			YAHOOAmp.util.Connect.asyncRequest("POST", param1, callback );
		}
    -->

</script>
