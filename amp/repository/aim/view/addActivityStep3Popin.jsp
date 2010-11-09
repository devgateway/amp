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
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFundingPopin.js"/>"></script>

<script language="JavaScript">
    <!--
   
   
	
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
-->
</script>

