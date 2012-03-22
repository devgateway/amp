<%@ page pageEncoding="UTF-8" %>
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
<%@ page
	import="org.digijava.module.aim.form.EditActivityForm,java.util.*,org.digijava.module.aim.dbentity.*"%>
<jsp:include page="scripts/newCalendar.jsp"  />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<div id="myDiv" style="display: none"></div>

<style type="text/css">
	.yui-panel .bd{
	  height: 92%;
	  overflow: auto;
	}
</style>


<%
	EditActivityForm eaForm = (EditActivityForm) session.getAttribute("siteampdefaultaimEditActivityForm");
	String defPers = (String) request.getAttribute("defPerspective");
	String defCurr = (String) request.getAttribute("defCurrency");
	int indexC = 0;
	int indexD = 0;
	int indexE = 0;
%>



<script language="JavaScript" type="text/javascript">






	function validateForm() {

		return true;

	}



function checkallIssues() {

	var selectbox = document.aimEditActivityForm.checkAllIssues;

	var items = document.getElementById('selIssues');
		for(i=0; i<items.length; i++){
			document.getElementById('selIssues')[i].checked = selectbox.checked;
		}
		document.getElementById('selIssues').checked = selectbox.checked;
	

}

///CONVERT POPUP TO POPIN CODE 
		//POPIN DEFINITION
		YAHOOAmp.namespace("YAHOOAmp.amptab");
			var myPanel1 = new YAHOOAmp.widget.Panel("new", {
			width:"550px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true} 
		    );
	
	

		//POPIN RENDER FUNCTION
		function showPOPIN(){
			var element = document.getElementById("divContent");
			myPanel1.setBody(element);
			myPanel1.render(document.body);
			myPanel1.show();
		}

		
	//LOAD THE POPUP
		var div=null; 
		var callback = { 
     	   		success: function(o) {
     	   				if (div!=null){
							if(div.parentNode)
								div.parentNode.removeChild(div)
	 	   				}
						div=document.createElement('div');
						div.setAttribute("style","display:inline");
						div.setAttribute("id","divContent");
     	   					
     	   				
     	   				/* 
     	   				 just load the form part  of the popup page (we can improve it using DOM) 
     	   			  	 be sure all needed (script etc) is located into the FORM on the popup page
     	   				*/ 
     	   				div.innerHTML=o.responseText.substr(o.responseText.indexOf("<form"),o.responseText.indexOf("</form>")-o.responseText.indexOf("<form")+8);
     	   				//APPEND THE POPUP FORM TO THE POPIN DIV 
     	   				document.body.appendChild(div);
     	   				//call this to render the popin
     	   				showPOPIN();
     	   				
	        	} 
        }
	    
	  function addIssues(){
			reusableDialog.setHeader('<digi:trn key="aim:addIssue">Add Issue</digi:trn>');
	  			 <digi:context name="addIssue" property="context/module/moduleinstance/showUpdateIssue.do?edit=true" />
 				var connectionObject =YAHOOAmp.util.Connect.asyncRequest('GET', "<%=addIssue%>&issues.issueId=-1",callbackDialog);
        }
       
       
       
       function updateIssues(id) {
    	   reusableDialog.setHeader('<digi:trn key="aim:updateIssue">Update Issue</digi:trn>');
			<digi:context name="addIssue" property="context/module/moduleinstance/showUpdateIssue.do?edit=true" />
			var connectionObject =YAHOOAmp.util.Connect.asyncRequest("GET","<%=addIssue%>&issues.issueId="+id,callbackDialog);
		}
        
        
	function addMeasures(issueId) {
		reusableDialog.setHeader('<digi:trn key="aim:addMeasure">Add Measure</digi:trn>');
		 <digi:context name="addMeasure" property="context/module/moduleinstance/showUpdateMeasure.do?edit=true" />
		 var connectionObject =YAHOOAmp.util.Connect.asyncRequest("GET","<%=addMeasure%>&issues.issueId="+issueId+"&issues.measureId=-1",callbackDialog);
	}


	function updateMeasures(issueId,measureId) {
		reusableDialog.setHeader('<digi:trn key="aim:updateMeasure">Update Measure</digi:trn>');
		<digi:context name="addMeasure" property="context/module/moduleinstance/showUpdateMeasure.do?edit=true" />
		var connectionObject =YAHOOAmp.util.Connect.asyncRequest("GET","<%=addMeasure%>&issues.issueId="+issueId+"&issues.measureId="+measureId,callbackDialog);
	}
	
	
	function addActors(issueId,measureId) {
		reusableDialog.setHeader('<digi:trn key="aim:addActor">Add Actor</digi:trn>');
		<digi:context name="addActors" property="context/module/moduleinstance/showUpdateActors.do?edit=true" />
		var connectionObject =YAHOOAmp.util.Connect.asyncRequest("GET","<%=addActors%>&issues.issueId="+issueId+"&issues.measureId="+measureId+"&issues.actorId=-1",callbackDialog);
	}



	function updateActor(issueId,measureId,actorId) {
		reusableDialog.setHeader('<digi:trn key="aim:updateActor">Update Actor</digi:trn>');
		<digi:context name="addActors" property="context/module/moduleinstance/showUpdateActors.do?edit=true" />
		var connectionObject =YAHOOAmp.util.Connect.asyncRequest("GET","<%=addActors%>&issues.issueId="+issueId+"&issues.measureId="+measureId+"&issues.actorId="+actorId,callbackDialog);
	}

	function validateIssue() {
		if(isEmpty(document.getElementById('issue').value) == true) {	
			var issueError = "<digi:trn jsFriendly='true'>Please enter issue</digi:trn>"; 	
			alert(issueError);			
			addIssueForm.issue.focus();
			return false;
		}
		<field:display feature="Issues" name="Issue Date">
			if(isEmpty(document.getElementById('issueDate').value) == true) {	
				var issueError2 = "<digi:trn jsFriendly='true'>Please enter issue date</digi:trn>"; 	
				alert(issueError2);
				return false;
		}	
		</field:display>
		document.aimEditActivityForm.submit();
		return true;
	}

	function validateMeasure() {
		var meas = document.getElementsByName("issues.measure")[0];
		if(isEmpty(meas.value) == true) {
			var measError = "<digi:trn jsFriendly='true'>Please enter the measure</digi:trn>"; 	
			alert(measError);
			meas.focus();
			return false;
		}
		document.aimEditActivityForm.submit();
		return true;
	}
	
	function validateActor() {
		var actor = document.getElementsByName("issues.actor")[0];
		if(isEmpty(actor.value) == true) {
			var actorError = "<digi:trn jsFriendly='true'>Please enter the actor</digi:trn>"; 	
			alert(actorError);
			actor.focus();
			return false;
		}
		document.aimEditActivityForm.submit();
		return true;

	}

	function clearFieldIssue(){
		document.getElementById('issue').value="";
		<field:display feature="Issues" name="Issue Date">
			document.getElementById('issueDate').value="";
		</field:display>
		return true;
	}

	function clearFieldMeasure(){
		document.getElementsByName("issues.measure")[0].value="";
		return true;
	}

	function clearFieldActor(){
		document.getElementsByName("issues.actor")[0].value="";
		return true;
	}
	
//END POPIN



function removeIssue(idIssue) {

	<digi:context name="removeIssue" property="context/module/moduleinstance/removeIssue.do?edit=true" />

	document.aimEditActivityForm.action = "<%=removeIssue%>&issues.selIssue="+idIssue;

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}




function removeMeasure(issueId) {

	<digi:context name="removeMeasure" property="context/module/moduleinstance/removeMeasure.do?edit=true" />

	document.aimEditActivityForm.action = "<%=removeMeasure%>&issueId="+issueId;

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}

function removeMeasure(issueId,measureId) {

	<digi:context name="removeMeasure" property="context/module/moduleinstance/removeMeasure.do?edit=true" />

	document.aimEditActivityForm.action = "<%=removeMeasure%>&issues.issueId="+issueId+"&issues.measureId="+measureId;

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}




function removeActors(issueId,measureId) {

	<digi:context name="removeActors" property="context/module/moduleinstance/removeActors.do?edit=true" />

	document.aimEditActivityForm.action = "<%=removeActors%>&issueId="+issueId+"&measureId="+measureId;

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}


function removeActor(issueId,measureId,actorId) {
	<digi:context name="removeActors" property="context/module/moduleinstance/removeActors.do?edit=true" />

	document.aimEditActivityForm.action = "<%=removeActors%>&issues.issueId="+issueId+"&issues.measureId="+measureId+"&issues.actorId="+actorId;

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

}


function validatePhyProg() {

	if (document.aimEditActivityForm.selPhyProg.checked != null) {

		if (document.aimEditActivityForm.selPhyProg.checked == false) {

			alert("Please choose a physical progress to remove");

			return false;

		}

	} else {

		var length = document.aimEditActivityForm.selPhyProg.length;

		var flag = 0;

		for (i = 0;i < length;i ++) {

			if (document.aimEditActivityForm.selPhyProg[i].checked == true) {

				flag = 1;

				break;

			}

		}



		if (flag == 0) {

			alert("Please choose a physical progress to remove");

			return false;

		}

	}

	return true;

}



function validateComponents() {
	var anyChecked		= false;
	var checkboxes	= document.forms["aimEditActivityForm"]["components.selComp"];
	if ( checkboxes.length != null && checkboxes.length >= 2 ) {
			for (var i=0; i< checkboxes.length; i++) {
				if ( checkboxes[i].checked ) {
					anyChecked		= true;
					break;
				}
			}		
	}
	else {
		if ( checkboxes.checked )
			anyChecked		= true;
	}
	
	if ( !anyChecked ) {
		alert("<digi:trn>Please choose a component to remove</digi:trn>");
		return false;
	}
	return true;
}



function addPhyProgess(id,comp) {
		<digi:context name="addPhyProg" property="context/module/moduleinstance/showAddPhyProg.do~edit=true" />
		var url = "";
		if (id == -1) {
			url = "<%= addPhyProg %>~comp=" + comp;

		} else {

			url = "<%= addPhyProg %>~comp=" + comp + "~id=" + id;

		}
		reusableDialog.setHeader("<digi:trn key="aim:physicalprogress">Physical Progress</digi:trn>");
		var connectionObject =YAHOOAmp.util.Connect.asyncRequest('GET', url,callbackDialog);
}

       
       
function removeSelPhyProgress() {

	var flag = validatePhyProg();

	if (flag == false) return false;

	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeSelPhyProg.do?edit=true" />

	document.aimEditActivityForm.action = "<%= remPhyProg %>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

	return true;

}

 function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }
    function changeCurrency(){
       var currency=document.getElementById("compFundCurr").value;
    	// the edit=true parameter needs to be added to each request so that AMPActionServlet knows the user hasn't left the Activity Wizard
       var url=addActionToURL('getFundingTotals.do')+'?edit=true&fundingCurrCode='+currency+'&isRegcurr=false'+'&isStepPage=false&edit=true';
       var async=new Asynchronous();
       async.complete=buildFundingTotals;
       async.call(url);
    }
     function buildFundingTotals(status, statusText, responseText, responseXML){
        var root=responseXML.getElementsByTagName('total')[0];
        var comm=document.getElementById("total_comm");
        var disb=document.getElementById("total_disb");
        var expn=document.getElementById("total_expn");
        var curr=root.getAttribute("curr");
        comm.innerHTML="<digi:trn> Commitments - (Total Actual Allocation</digi:trn> "
            +root.getAttribute("comm")+' '+curr+')';
        disb.innerHTML="<digi:trn> Disbursement - (Total actual to date</digi:trn> "
            +root.getAttribute("disb")+' '+curr+')';
        expn.innerHTML="<digi:trn> Expenditure - (Total actual to date</digi:trn>   " +root.getAttribute("expn")+' '+curr+')';

    }
    function totalsPage() {
       if(document.aimEditActivityForm.fundingCurrCode != undefined) {
           var currency = document.aimEditActivityForm.fundingCurrCode.value;
           //alert (currency);
           // the edit=true parameter needs to be added to each request so that AMPActionServlet knows the user hasn't left the Activity Wizard
           var url=addActionToURL('getFundingTotals.do')+'?fundingCurrCode='+currency+'&isRegcurr=false'+'&isStepPage=true&edit=true';
           var async=new Asynchronous();
           async.complete=buildFundingTotalsForPage;
           async.call(url);
       }
    }
       function buildFundingTotalsForPage(status, statusText, responseText, responseXML){
        var root=responseXML.getElementsByTagName('total')[0];
        var comm=document.getElementById("comp_comms");
        var disb=document.getElementById("comp_disb");
        var expn=document.getElementById("comp_expn");
        var compTot=document.getElementById("comp_totalDisb");
        var curr=root.getAttribute("curr");
       var totalComm=root.getAttribute("totalComm");
        comm.innerHTML="<digi:trn> Commitments - (Grand Total Actual Allocation</digi:trn> "+totalComm+" "+curr+")";
        disb.innerHTML="<digi:trn> Disbursement - (Total actual to date</digi:trn> "+root.getAttribute("disb")+' '+curr+')';
        expn.innerHTML="<digi:trn> Expenditure - (Total actual to date</digi:trn>   " +root.getAttribute("expn")+' '+curr+')';
        compTot.innerHTML="<digi:trn key="aim:totalComponentActualDisbursement">Component Grand Total Actual Disbursements</digi:trn>  " +root.getAttribute("comp_disb")+' '+curr+')';

    }

 

 
if(document.addEventListener){
    document.addEventListener("DOMContentLoaded",totalsPage,false);
}
else{
  if (document.attachEvent){
  window.attachEvent('onload', totalsPage);
}
else {
  document.DOMContentLoaded = totalsPage;
}

}

/*

function removeSelComponents()

{

	<digi:context name="rem" property="context/module/moduleinstance/removeSelPhyProg.do?edit=true" />

	document.aimEditActivityForm.action = "<%= rem %>";

	document.aimEditActivityForm.target = "_self";

	document.aimEditActivityForm.submit();

}

*/



function resetAll()

{

	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />

	document.aimEditActivityForm.action = "<%= resetAll %>";

	document.aimEditActivityForm.target = "_self";

	document.aimEditActivityForm.submit();

	return true;

}





function removeSelComponents() {

	var flag = validateComponents();

	if (flag == false) return false;

	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeComponent.do?edit=true" />

	document.aimEditActivityForm.action = "<%= remPhyProg %>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

	return true;

}


</script>

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">


  <html:hidden property="step"/>
  <html:hidden property="reset" />
  <html:hidden property="location.country" />
  <html:hidden property="editAct" />

  <input type="hidden" name="edit" value="true">
  <c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
  </c:set>
      
  <c:if test="${empty aimEditActivityForm.location.selectedLocs}">
    <input type="hidden" name="sizeLocs" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.location.selectedLocs}">
    <input type="hidden" name="sizeLocs" value="${fn:length(aimEditActivityForm.location.selectedLocs)}">
  </c:if>


  <c:if test="${empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
    <input type="hidden" name="sizeNPOPrograms" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
    <input type="hidden" name="sizeNPOPrograms" value="${fn:length(aimEditActivityForm.programs.nationalPlanObjectivePrograms)}">
  </c:if>

   <c:if test="${empty aimEditActivityForm.programs.primaryPrograms}">
    <input type="hidden" name="sizePPrograms" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.programs.primaryPrograms}">
    <input type="hidden" name="sizePPrograms" value="${fn:length(aimEditActivityForm.programs.primaryPrograms)}">
  </c:if>

   <c:if test="${empty aimEditActivityForm.programs.secondaryPrograms}">
    <input type="hidden" name="sizeSPrograms" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.programs.secondaryPrograms}">
    <input type="hidden" name="sizeSPrograms" value="${fn:length(aimEditActivityForm.programs.secondaryPrograms)}">
  </c:if>


  <c:if test="${empty aimEditActivityForm.sectors.activitySectors}">
    <input type="hidden" name="sizeActSectors" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
    <bean:size id ="actSectSize" name="aimEditActivityForm" property="sectors.activitySectors" />
    <input type="hidden" name="sizeActSectors" value="${actSectSize}">
  </c:if>



  <table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
    <tr>
      <td width="100%" vAlign="top" align="left">
        <!--  AMP Admin Logo -->
        <jsp:include page="teamPagesHeader.jsp"  />
        <!-- End of Logo -->
      </td>
    </tr>
    <tr>
      <td width="100%" vAlign="top" align="left" class=r-dotted-lg>
        <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
          <tr>
            <td class=r-dotted-lg width="10">&nbsp;
            
            </td>
            <td align=left vAlign=top>
              <table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
                <tr>
                  <td>
                    <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                      <tr>
                        <td>
                          <span class=crumb>
                              <c:set property="translation" var="ttt">
                                <digi:trn key="aim:clickToViewMyDesktop">
                                Click here to view MyDesktop
                                </digi:trn>
                              </c:set>
                              <c:set var="message">
								<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
								</c:set>
								<c:set var="quote">'</c:set>
								<c:set var="escapedQuote">\'</c:set>
								<c:set var="msg">
									${fn:replace(message,quote,escapedQuote)}
								</c:set>
                              <digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')" title="${trans}">
                                <digi:trn key="aim:portfolio">
                                Portfolio
                                </digi:trn>
                              </digi:link>&nbsp;&gt;&nbsp;
                            
                           
                           <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                               
                               <c:set property="translation" var="trans">
                                   <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                       Click here to goto Add Activity Step ${step.stepActualNumber}
                                   </digi:trn>
                               </c:set>
                               
                               
                               
                               <c:if test="${!index.last}">
                                   
                                   <c:if test="${index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           
                                           
                                           <c:if test="${aimEditActivityForm.editAct == true}">
                                               <digi:trn key="aim:editActivityStep1">
                                                   Edit Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           <c:if test="${aimEditActivityForm.editAct == false}">
                                               <digi:trn key="aim:addActivityStep1">
                                                   Add Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           
                                       </digi:link>
                                        &nbsp;&gt;&nbsp;
                                   </c:if>
                                   <c:if test="${!index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
                                       </digi:link>
                                        &nbsp;&gt;&nbsp;
                                   </c:if>
                               </c:if>
                               
                               
                               
                               <c:if test="${index.last}">
                                   
                                   <c:if test="${index.first}">
                                       
                                       
                                       
                                       <c:if test="${aimEditActivityForm.editAct == true}">
                                           <digi:trn key="aim:editActivityStep1">
                                               Edit Activity - Step 1
                                           </digi:trn>
                                       </c:if>
                                       <c:if test="${aimEditActivityForm.editAct == false}">
                                           <digi:trn key="aim:addActivityStep1">
                                               Add Activity - Step 1
                                           </digi:trn>
                                       </c:if>
                                   </c:if>
                                   
                                   
                                   <c:if test="${!index.first}">
                                       <digi:trn key="aim:addActivityStep${step.stepActualNumber}"> Step ${step.stepActualNumber}</digi:trn>
                                   </c:if>
                                   
                                   
                                   
                               </c:if>
                               
                               
                               
                               
                               
                               
                               
                           </c:forEach>
                            
                           
								
                          </span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td>
                    <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                      <tr>
                        <td height=16 vAlign=center width="100%">
                          <span class=subtitle-blue>
                            <c:if test="${aimEditActivityForm.editAct == false}">
                              <digi:trn key="aim:addNewActivity">
                              Add New Activity
                              </digi:trn>
                            </c:if>
                            <c:if test="${aimEditActivityForm.editAct == true}">
                              <digi:trn key="aim:editActivity">
                              Edit Activity
                              </digi:trn>:
                              ${aimEditActivityForm.identification.title}
                            </c:if>
                          </span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>                
                <tr>
                  <td>
                    <digi:errors />
                  </td>
                </tr>
                <tr><td valign="top">

					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">

						<tr><td width="75%" vAlign="top">

						<table cellPadding=0 cellSpacing=0 width="100%">

							<tr>

								<td width="100%">

									<table cellPadding=0 cellSpacing=0 width="100%" border=0>

										<tr>

											<td width="13" height="20" background="module/aim/images/left-side.gif">

											</td>

											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">

												<digi:trn>Step</digi:trn> ${stepNm} <digi:trn>of</digi:trn>  ${fn:length(aimEditActivityForm.steps)}:
                                                   <feature:display name="Components" module="Components">
                                                     <digi:trn key="aim:activity:Components">Components</digi:trn>
                                                  </feature:display>
                                               <feature:display name="Issues" module="Issues">
                                               	 <digi:trn key="aim:activity:Issues">Issues</digi:trn>
											   </feature:display>
											</td>

											<td width="13" height="20" background="module/aim/images/right-side.gif">

											</td>

										</tr>

									</table>

								</td>

							</tr>

							<tr><td width="100%" bgcolor="#f4f4f2">

							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<feature:display name="Components" module="Components">

							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
									<jsp:include page="addActivityStep5Components.jsp"/>

							</td></tr>

							</feature:display>



								<!-- Issues , Measures and Actions -->

									<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
		
									<feature:display name="Issues" module="Issues">
										<jsp:include page="addActivityStep5Issues.jsp"/>
									</feature:display>
								</td></tr>
									
									<tr>
										<td bgColor=#f4f4f2> &nbsp;</td>
									</tr>
<!--

									<tr><td bgColor=#f4f4f2 align="center">

										<table cellPadding=3>

											<tr>

												<td>

													<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(4)">

														<< <digi:trn key="btn:back">Back</digi:trn>

													</html:submit>

												</td>

												<td>

													<html:submit  styleClass="dr-menu" property="submitButton"  onclick="gotoStep(6)">

															<digi:trn key="btn:next">Next</digi:trn> >>

													</html:submit>

												</td>

												<td>

													<html:reset  styleClass="dr-menu" property="submitButton" onclick="return resetAll()">

														<digi:trn key="btn:reset">Reset</digi:trn>

													</html:reset>

												</td>

											</tr>

										</table>

									</td></tr>
 -->

								</table>



								<!-- end contents -->

							</td></tr>



							</table>

							</td></tr>

						</table>

						</td>

						<td width="25%" vAlign="top" align="right">

						<!-- edit activity form menu -->

							<jsp:include page="editActivityMenu.jsp"  />

						<!-- end of activity form menu -->

						</td></tr>

					</table>

				</td></tr>

				<tr><td>&nbsp;

					

				</td></tr>

			</table>

		</td>

		<td width="10">&nbsp;</td>

	</tr>

</table>

</digi:form>


<script language="javascript">
function editFunding(id){
	reusableDialog.setHeader("<digi:trn key="aim:components">Components</digi:trn>");

	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=showEdit" />
	var connectionObject =YAHOOAmp.util.Connect.asyncRequest('GET', "<%= addComp %>&fundId="+id,callbackDialog);

	//IE 7 BUG :S
	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=showEdit" />
	var cObj = YAHOOAmp.util.Connect.asyncRequest('POST', "<%= addComp %>&fundId="+id, callbackPost);
}


function addComponents()

{
	myPanel1.setHeader("<digi:trn key="aim:components">Components</digi:trn>");
	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=show" />
	var connectionObject =YAHOOAmp.util.Connect.asyncRequest('GET', "<%=addComp%>",callbackDialog);

}

function postComponentForm(action){
	var formObject = document.aimAddComponentForm;
	YAHOOAmp.util.Connect.setForm(formObject);
	var cObj = YAHOOAmp.util.Connect.asyncRequest('POST', action, callbackPost);
}

function addComponent(){
	<digi:context name="addNewComponent" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=addNewComponent" />

	if ( document.getElementById('newCompoenentName').value==""){
		var msg="<digi:trn key="aim:msgErrorNoName">You have to enter the component name</digi:trn>"
		alert(msg);
		return false;
	}
	
	<feature:display name="Admin - Component Type" module="Components">
	if (document.aimAddComponentForm.selectedType.value=="-1"){
		var msg="<digi:trn key="aim:msgErrorSelctType">You have to select the component type</digi:trn>"
		alert(msg);
		return false;
	}
	</feature:display>
	postComponentForm("<%= addNewComponent%>");

	if (document.getElementById('newCompoenentName').value!=''){
		document.switchComponent();
	}
}

function validateEnter(e) {
	eKey = (document.all) ? e.keyCode : e.which;
	if (eKey==13) addComponent();
}


YAHOOAmp.namespace("YAHOOAmp.amptab");
	var reusableDialog = new YAHOOAmp.widget.Dialog("new", {
	width:"850px",
	height:"450px",
	fixedcenter: true,
	constraintoviewport: true,
	underlay:"none",
	close:true,
	visible:false,
	modal:true,
	draggable:true,
	hideaftersubmit:false}
	 
	);

	reusableDialog.beforeHideEvent.subscribe(function() {
	if (div!=null){
		div.parentNode.removeChild(div)
	}
}); 

	var divDialog=null;
	var callbackPost = { 
 	   		success: function(o) {
					//Extract javascript and execute it
					eval(o.responseText.substr(o.responseText.indexOf('<script language=\"JavaScript\"')+30,o.responseText.indexOf("</script")-30-o.responseText.indexOf('<script language=\"JavaScript\"')));
        	} 
    }
	
	var callbackDialog = { 
 	   		success: function(o) {
 	   				if (div!=null){
						if(div.parentNode)
							div.parentNode.removeChild(div)
 	   				}

					div=document.createElement('div');
					div.setAttribute("id","divContent");
					div.style.overflow	= "auto";
					div.style.height	= "100%";
					div.style.display	= "block"
 	   				
 	   				/* 
 	   				 just load the form part  of the popup page (we can improve it using DOM) 
 	   			  	 be sure all needed (script etc) is located into the FORM on the popup page
 	   				*/ 
 	   				div.innerHTML=o.responseText.substr(o.responseText.indexOf("<form"),o.responseText.indexOf("</form>")-o.responseText.indexOf("<form")+8);
 	   				//call this to render the popin
 	   				showPOPINDialog(div);
 	   				
        	} 
    }
	
	function showPOPINDialog( element ){
		if (element == null) {
			element = document.getElementById("myDiv");
			element.style.display = "inline";
		}
		reusableDialog.setBody(element);
		reusableDialog.render(document.body);
		reusableDialog.show();
	}
	function closePopup() {
		div.innerHTML = "";
		reusableDialog.hide();
	}

	function addPhysicalProgress()
	{
		var titleFlag = isEmpty(document.getElementById('phyProgTitle').value);
		var dateFlag = isEmpty(document.getElementById('phyProgRepDate').value);
		if(titleFlag == true & dateFlag == true)
		{
			alert("Please enter Title and Reporting Date");
			document.getElementById('phyProgTitle').focus();
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" Please enter title");
				document.getElementById('phyProgTitle').focus();
			}
			if(dateFlag == true)
			{
				alert(" Please enter Reporting Date");
				document.getElementById('phyProgRepDate').focus();
			}
		}
		if(titleFlag == false && dateFlag == false)
		{
			<digi:context name="addPhyProg" property="context/module/moduleinstance/phyProgSelected.do?edit=true"/>
		   document.addPhysicalProgressForm.action = "<%= addPhyProg %>";
		   document.addPhysicalProgressForm.submit();
		}
	}


	var numComm = <%=indexC%>;
	var numExpn = <%=indexE%>;
	var numDisb = <%=indexD%>;

	var tempComm = numComm;
	var tempDisb = numDisb;
	var tempExpn = numExpn;


	document.addCommitments=function()
	{
		//This method has been modified to clone a generic div and replace the characters '@@' with the div number.
		var ni = document.getElementById('comm');
		var divname = "comm_" + numComm;
		var newdiv = document.getElementById('comm_gen').cloneNode(true);
		newdiv.setAttribute("id",divname);
		while(newdiv.innerHTML.match('@@') != null){
			newdiv.innerHTML = newdiv.innerHTML.replace('@@', numComm);
		}

		var oldSelect = document.getElementById('comm_gen').getElementsByTagName("select")[1];
		var selected = 0;
		for(var j=0; j<oldSelect.options.length ; j++){
			if(oldSelect.options[j].selected)
				{	selected = j; break; }
		}
		var select = newdiv.getElementsByTagName("select")[1];
		select.indexSelected = selected;	

		for(var i=0; i<select.options.length ; i++){
			select.options[i].selected = false;
		}
		select.options[selected].selected=true;
		
		newdiv.style.visibility = "visible";
		ni.appendChild(newdiv);
		numComm++;
		tempComm++;
	}

	document.removeCommitment=function(divname)
	{
		var d = document.getElementById('comm');
		var olddiv = document.getElementById(divname);
		d.removeChild(olddiv);
		tempComm--;
	}

	document.addDisbursement=function()
	{
		//This method has been modified to clone a generic div and replace the characters '@@' with the div number.
		var ni = document.getElementById('disb');
		var divname = "disb_" + numDisb;
		var newdiv = document.getElementById('disb_gen').cloneNode(true);
		newdiv.setAttribute("id",divname);
		while(newdiv.innerHTML.match('@@') != null){
			newdiv.innerHTML = newdiv.innerHTML.replace('@@', numDisb);
		}

		var oldSelect = document.getElementById('disb_gen').getElementsByTagName("select")[1];
		var selected = 0;
		for(var j=0; j<oldSelect.options.length ; j++){
			if(oldSelect.options[j].selected)
				{	selected = j; break; }
		}
		var select = newdiv.getElementsByTagName("select")[1];
		select.indexSelected = selected;	

		for(var i=0; i<select.options.length ; i++){
			select.options[i].selected = false;
		}
		select.options[selected].selected=true;

		
		newdiv.style.visibility = "visible";
		ni.appendChild(newdiv);
		numDisb++;
		tempDisb++;
	}

	document.removeDisbursement=function(divname)
	{
		var d = document.getElementById('disb');
		var olddiv = document.getElementById(divname);
		d.removeChild(olddiv);
		tempDisb--;
	}

	document.addExpenditure=function()
	{
		//This method has been modified to clone a generic div and replace the characters '@@' with the div number.
		var ni = document.getElementById('expn');
		var divname = "expn_" + numExpn;
		var newdiv = document.getElementById('expn_gen').cloneNode(true);
		newdiv.setAttribute("id",divname);
		while(newdiv.innerHTML.match('@@') != null){
			newdiv.innerHTML = newdiv.innerHTML.replace('@@', numExpn);
		}

		var oldSelect = document.getElementById('expn_gen').getElementsByTagName("select")[1];
		var selected = 0;
		for(var j=0; j<oldSelect.options.length ; j++){
			if(oldSelect.options[j].selected)
				{	selected = j; break; }
		}
		var select = newdiv.getElementsByTagName("select")[1];
		select.indexSelected = selected;	

		for(var i=0; i<select.options.length ; i++){
			select.options[i].selected = false;
		}
		select.options[selected].selected=true;
		
		newdiv.style.visibility = "visible";
		ni.appendChild(newdiv);
		numExpn++;
		tempExpn++;
	}

	document.removeExpenditure=function(divname)
	{
		var d = document.getElementById('expn');
		var olddiv = document.getElementById(divname);
		d.removeChild(olddiv);
		tempExpn--;
	}



	document.addComponentsPopup=function()
	{
		var flag = document.validate();
		if (flag == true){
			<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=save"/>
			document.aimAddComponentForm.action = "<%= addComp %>";
			document.aimAddComponentForm.submit();
			closePopup();
		}
		return flag;
	}

	document.validate=function()
	{
	  var msgEnterAmount="<digi:trn key="aim:selectComponent:errmsg:enterAmount">Amount not entered.</digi:trn>";
	  var msgInvalidAmount="<digi:trn key="aim:selectComponent:errmsg:invalidAmount">Invalid amount entered.</digi:trn>";
	  var msgEnterDate="<digi:trn key="aim:selectComponent:errmsg:enterDate">Date not entered.</digi:trn>";
	  var msgEnterTitle="<digi:trn key="aim:selectComponent:errmsg:enterTitle">Please enter title.</digi:trn>";
	  var msgSelectComponent="<digi:trn key="aim:selectComponent:errmsg:selectComponent">Please select a Component before Saving.</digi:trn>";
	  var msgEnterCommitment="<digi:trn key="aim:selectComponent:errmsg:enterCommitment">Commitment not entered.</digi:trn>";
	  var msgEnterExpenditure="<digi:trn key="aim:selectComponent:errmsg:enterExpenditure">Expenditure entered without entering disbursements.</digi:trn>";


		var titleFlag = isEmpty(document.getElementById('newCompoenentName').value);
		if(titleFlag == true) {
			alert(msgEnterTitle);
			return false;
		}

		var x = document.aimAddComponentForm;
		if(document.getElementById('newCompoenentName').value == '')
		{
			alert(msgSelectComponent);
				return false;
		}

		if (tempComm == 0) {
			alert (msgEnterCommitment);
			return false;
		}

		if (tempExpn > 0 && tempDisb == 0) {
			alert (msgEnterExpenditure);
			return false;
		}

		for (index = 0;index < x.elements.length;index++) {
			var str = x.elements[index].name;
			if (str.match("comm_[0-9]*_2")) {
				// validate amount
				if (trim(x.elements[index].value) == "") {
					alert (msgEnterAmount);
					x.elements[index].focus();
					return false;
				}
				if (checkAmount(x.elements[index].value) == false) {
					alert (msgInvalidAmount);
					x.elements[index].focus();
					return false;
				}

			} else if (str.match("comm_[0-9]*_4")) {
				// validate date
				if (trim(x.elements[index].value) == "") {
					alert (msgEnterDate);
					x.elements[index].focus();
					return false;
				}
			} else if (str.match("disb_[0-9]*_2")) {
				// validate amount
				if (trim(x.elements[index].value) == "") {
					alert (msgEnterAmount);
					x.elements[index].focus();
					return false;
				}
				if (checkAmount(x.elements[index].value) == false) {
					alert (msgInvalidAmount);
					x.elements[index].focus();
					return false;
				}
			} else if (str.match("disb_[0-9]*_4")) {
				if (trim(x.elements[index].value) == "") {
					alert (msgEnterDate);
					x.elements[index].focus();
					return false;
				}
			} else if (str.match("expn_[0-9]*_2")) {
				// validate amount
				if (trim(x.elements[index].value) == "") {
					alert (msgEnterAmount);
					x.elements[index].focus();
					return false;
				}
				if (checkAmount(x.elements[index].value) == false) {
					alert (msgInvalidAmount);
					x.elements[index].focus();
					return false;
				}
			} else if (str.match("expn_[0-9]*_4")) {
				if (trim(x.elements[index].value) == "") {
					alert (msgEnterDate);
					x.elements[index].focus();
					return false;
				}
			}

		}
		return true;
	}

	document.checkAmount=function(val){
	  if(val.match("[^0-9., ]")){
	    return false;
	  }
	  return true;
	}

	document.switchComponent=function(){
	if (document.getElementById('newCompoenentName').value!=''){
	  	 document.getElementById('tblId').style.visibility="visible";                         		
	     document.getElementById('tblId').style.position="relative";  
	     //document.aimEditActivityForm.newCompoenentName.value="";
		document.getElementById("txtTitle").innerHTML="<digi:trn key="aim:msgAddfunding">Add funding information for </digi:trn> " + document.getElementById('newCompoenentName').value;
	  
	  }else{
	  	 document.getElementById('tblId').style.visibility="hidden";                         		
	     document.getElementById('tblId').style.position="absolute";  
	  
	  }
	  
	  <feature:display name="Admin - Component Type" module="Components">
	  if (document.getElementById('selectedType').value==-1){
	    document.getElementById('newCompoenentName').disabled=true;
	    document.getElementById('newCompoenentName').style.bgColor="#EEEEEE";
	  }else{
	  </feature:display>
	   document.getElementById('newCompoenentName').disabled=false;
	   document.getElementById('newCompoenentName').style.bgColor="#EEEEEE";
	   <feature:display name="Admin - Component Type" module="Components">
	  }
	  </feature:display>
	}
		
</script>

