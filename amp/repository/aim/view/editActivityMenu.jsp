<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<jsp:include page="previewLogframeUtil.jsp" flush="true" />
<div id="mySave" style="display: none">
	<div id="mySaveContent" class="content">
		<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
			<tr>
				<td>
					<br/><br/><br/><br/><br/><br/>
					<p align="center"><img align="top" src="/TEMPLATE/ampTemplate/imagesSource/loaders/save-loader.gif" /></p>
					<p align="center"><b><digi:trn key="aim:savePopup:title">Saving</digi:trn>...</b></p>
					<br/><br/><br/><br/><br/><br/>
				</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript" src="script/jquery-1.3.2.min.js"></script>

<script type="text/javascript">
		YAHOO.namespace("YAHOO.amptab");
		YAHOO.amptab.init = function() {
		    		var tabView = new YAHOO.widget.TabView('tabview_container');
		};
		
	    var mySavePanel = new YAHOO.widget.Panel("newmySave", {
			width:"500px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:false,
		    visible:false,
		    modal:true,
		    draggable:false,
		    context: ["showbtn", "tl", "bl"] 
		    }
		     );
	
		function initScripts() {
			var msgP5="\n<digi:trn key='aim:saving'>Saving</digi:trn>";
			mySavePanel.setHeader(msgP5);
			mySavePanel.setBody("");
			mySavePanel.render(document.body);
			savePanelStart = 0;
		}
	
		function showLoadingSave() {
			//var content = document.getElementById("mySaveContent");
			var element5 = document.getElementById("mySave"); 
			//content.innerHTML = '';
			//if (panelFirstShow == 1){ 
				element5.style.display = "inline";
				mySavePanel.setBody(element5);
				panelFirstShow = 0;
			//}
			document.getElementById("mySaveContent").scrollTop=0;
			mySavePanel.show();
		}
	
	var current = window.onload;
	
	addLoadEvent( function() {
        //current.apply(current);
        if(document.aimEditActivityForm.step.value=="1"){
        	initStep1Scripts();
        }
        if(document.aimEditActivityForm.step.value=="2"){
        	initSectorScript();
        }
        if(document.aimEditActivityForm.step.value=="3"){
            initFundingScript();
            initImportFundingScript();
        }
        if(document.aimEditActivityForm.step.value=="6"){
        	initDocumentsScript();
        }
        if(document.aimEditActivityForm.step.value=="7"){
        	initOrganizationsScript();
        }
        if(document.aimEditActivityForm.step.value=="8"){
			initContactInfoScript();
                        initOrgScript();
		}
		 if(document.aimEditActivityForm.step.value=="10"){
        	initAddIndicatorScript()
        }
        if(document.aimEditActivityForm.step.value=="11"){
        	initCostingScript();
        }
        if(document.aimEditActivityForm.step.value=="17"){
        	initStep17Scripts();
        }
        
        initScripts();
   	});

	$(window).scroll(function()
	{
		//$('#scrollingDiv').animate({"marginTop": ($(window).scrollTop()) + "px", "marginBottom": ($(window).scrollBottom()) + "px"}, {queue: false, duration: 350});
		//alert ($(window).scrollTop());
		if ($(window).scrollTop() > 70) {
			$('#scrollingDiv').animate({"marginTop": ($(window).scrollTop()-70) + "px"}, {queue: false, duration: 250});
        } else {
        	$('#scrollingDiv').animate({"marginTop": ($(window).scrollTop()) + "px"}, {queue: false, duration: 250});
        };
	}); 
</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myContract .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	
</style>



<script language="JavaScript">
<!--
function previewClicked() {
	var flag = validateForm();
	if (flag == true) {
	document.aimEditActivityForm.step.value = "9";
	document.aimEditActivityForm.pageId.value = "1";
	<digi:context name="preview" property="context/module/moduleinstance/previewActivity.do?edit=true&currentlyEditing=true" />
	document.aimEditActivityForm.action = "<%= preview %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	}
}

function saveClicked() {
  var draftStatus=document.getElementById("draftFlag");
  if(draftStatus!=null){
    draftStatus.value=false;
  }
  save();
 
}

function closeClicked() {
	if(canExit()) {
		document.aimEditActivityForm.action = "/showDesktop.do";
	    document.aimEditActivityForm.target = "_self";
	    document.aimEditActivityForm.submit();
	}	    
}

function saveAsDraftClicked() {
  var draftStatus=document.getElementById("draftFlag");
  if(draftStatus!=null){
     draftStatus.value=true;
  }
  save();
}

function save() {
   
  var flag = true;//validateForm();
  if (flag == true) {
   /* document.aimEditActivityForm.saveButton.disabled = true;   	 AMP-2688 */
  	showLoadingSave();
  	<digi:context name="save" property="context/module/moduleinstance/saveActivity.do" />
    document.aimEditActivityForm.action = "<%= save %>?edit=true";
    document.aimEditActivityForm.target = "_self";

    document.aimEditActivityForm.submit();
  }
}

function gotoStep(value) {
  var draftStatus=document.getElementById("draftFlag");
  var flag;
  if(draftStatus!=null && draftStatus.value!="true"
  && document.aimEditActivityForm.step.value<value){
    flag=validateForm();
  }else{
    flag=true;
  }
  if (flag == true) {
    document.aimEditActivityForm.step.value = value;
    <digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= step %>";
    document.aimEditActivityForm.target = "_self";
    document.aimEditActivityForm.submit();
  }
}

function fnGetSurvey(value) {
var draftStatus=document.getElementById("draftFlag");
  var flag;
  if(draftStatus!=null && draftStatus.value!="true"
  && document.aimEditActivityForm.step.value<value){
    flag=true;//validateForm();
  }else{
    flag=true;
  }
  if (flag == true) {
  	document.aimEditActivityForm.step.value = value;
	//<digi:context name="step" property="context/module/moduleinstance/editSurveyList.do?edit=true" />
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	}
}
-->
</script>

<link rel="stylesheet" href="/TEMPLATE/ampTemplate/css/activityform_style.css" type="text/css">

<digi:instance property="aimEditActivityForm" />
<html:hidden property="workingTeamLeadFlag" />
<html:hidden property="pageId" />
<html:hidden property="indicator.currentValDate" />
<html:hidden property="identification.draft" styleId="draftFlag" />
<html:hidden property="identification.wasDraft" />

<!-- To avoid step numbering bug in the future please change getSteps() method
of ActivityUtil class also when change step visibility module/feature name -->
<div id="scrollingDiv" style="top: 193px;">
<table width="300" cellSpacing=0 cellPadding=0 vAlign="top" align="left" border=1 bordercolor="D2E1FF">
	<tr>
		<td align="center" class="editActivityMenuTitle">
			<digi:trn>Activity Sections</digi:trn>
		</td>
	</tr>
	<tr>
		<td>			
			<table border=0 width="300" cellSpacing=4 cellPadding=2 vAlign="top" align="left">				
				<feature:display name="Identification" module="Project ID and Planning">
				<tr>
					<c:if test="${aimEditActivityForm.step != 1}">
					<td nowrap="nowrap">
			            <c:set var="trnClickToAdd">
							<digi:trn key="aim:clickToAdd/UpdateActivityIdentificationFields">Add / Update Activity Identification fields</digi:trn>
			            </c:set>
						<a href="javascript:gotoStep(1)" id="menuElement" title="${trnClickToAdd}">
							<digi:trn key="aim:identification">
							Identification</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 1}">
					<td bgcolor="#0202F0" nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td height="19">
									<span class="textalb">
									<digi:trn key="aim:identification">
									Identification</digi:trn>
								</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<feature:display name="Planning" module="Project ID and Planning">
				<tr>
					<c:if test="${aimEditActivityForm.step != 1}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd1">
							<digi:trn key="aim:clickToAdd/UpdateActivityPlanningFields">Add / Update Activity Planning fields</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(1)" id="menuElement" title="${trnClickToAdd1}">
							<digi:trn key="aim:planning">
							Planning</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 1}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19" nowrap="nowrap">
									<span class="textalb">
										<digi:trn key="aim:planning">
										Planning</digi:trn>
									</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<module:display name="References" parentModule="PROJECT MANAGEMENT">
				<tr>
					<c:if test="${aimEditActivityForm.step != '1_5'}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd1">
							<digi:trn key="aim:editMenu:referenceTitle">References</digi:trn>
						</c:set>
						<a href="javascript:gotoStep('1_5')" id="menuElement" title="${trnClickToAdd1}">
							<digi:trn key="aim:editMenu:References">References</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == '1_5'}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left" border="0">
							<tr>
								<td bgcolor="#0202F0" height="19" nowrap="nowrap">
									<span class="textalb">
										<digi:trn key="aim:editMenu:References">References</digi:trn>
									</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</module:display>
				<feature:display name="Location" module="Project ID and Planning">
				<tr>
					<c:if test="${aimEditActivityForm.step != 2}">
					<td nowrap="nowrap">
						<c:set var="trnClickTohttp://amp-demo.code.ro/aim/addActivity.do~pageId=1~reset=true~action=createAdd2">
							<digi:trn key="aim:clickToAdd/UpdateLocation">Add / Update Location</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(2)" id="menuElement" title="${trnClickToAdd2}">
							<digi:trn key="aim:location">
							Location</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 2}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
									<span class="textalb">
										<digi:trn key="aim:location">
											Location</digi:trn>
									</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<feature:display name="Sectors" module="Project ID and Planning">
				<tr>
					<c:if test="${aimEditActivityForm.step != 2}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd3">
							<digi:trn key="aim:clickToAdd/UpdateSectorsandSubsectors">Add / Update Sectors and Sub sectors</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(2)" id="menuElement" title="${trnClickToAdd3}>">
							<digi:trn key="aim:sectors">
							Sectors</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 2}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:sectors">
							Sectors</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
				<tr>
					<c:if test="${aimEditActivityForm.step != 2}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd4">
							<digi:trn key="aim:clickToAdd/UpdateProgram">Add / Update Program</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(2)" id="menuElement" title="${trnClickToAdd4}">
							<digi:trn key="aim:program">
							Program</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 2}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19" nowrap="nowrap">
						<span class="textalb">
							<digi:trn key="aim:program">
							Program</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</module:display>
			
				<feature:display name="Funding Information"  module="Funding">
				<tr>
					<c:if test="${aimEditActivityForm.step != 3}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd5">
							<digi:trn key="aim:clickToAdd/UpdateFundingDetails">Add / Update Funding details</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(3)" id="menuElement" title="${trnClickToAdd5}">
							<digi:trn key="aim:funding">
							Funding</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 3}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:funding">
							Funding</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<feature:display name="Regional Funding" module="Funding">
				<tr>
					<c:if test="${aimEditActivityForm.step != 4}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd6">
							<digi:trn key="aim:clickToAdd/UpdateRegionalFunding">Add / Update Regional Funding</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(4)" id="menuElement" title="${trnClickToAdd6}">
									<digi:trn key="aim:regionalFunding">
									Regional Funding</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 4}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
								<span class="textalb">
									<digi:trn key="aim:regionalFunding">
									Regional Funding</digi:trn>
								</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<feature:display name="Components" module="Components">
				<tr>
					<c:if test="${aimEditActivityForm.step != 5}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd7">
							<digi:trn key="aim:clickToAdd/UpdateComponents">Add / Update Components</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(5)" id="menuElement" title="${trnClickToAdd7}">
							<digi:trn key="aim:components">
							Components</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 5}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:components">
							Components</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<feature:display name="Issues" module="Issues">
				<tr>
					<c:if test="${aimEditActivityForm.step != 5}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd8">
							<digi:trn key="aim:clickToAdd/UpdateIssues">Add / Update Issues</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(5)" id="menuElement" title="${trnClickToAdd8}">
							<digi:trn key="aim:issues">
							Issues</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 5}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
									<span class="textalb">
										<digi:trn key="aim:issues">
										Issues</digi:trn>
									</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<module:display name="Document" parentModule="PROJECT MANAGEMENT">
				<tr>
					<c:if test="${aimEditActivityForm.step != 6}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd9">
							<digi:trn key="aim:clickToAdd/UpdateDocumentsAndLinks">Add / Update the documents and links</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(6)" id="menuElement" title="${trnClickToAdd9}">
							<digi:trn key="aim:relatedDocuments">
							Related Documents</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 6}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:relatedDocuments">
							Related Documents</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</module:display>
				<module:display name="Organizations" parentModule="PROJECT MANAGEMENT">
				<tr>
					<c:if test="${aimEditActivityForm.step != 7}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd10">
							<digi:trn key="aim:clickToAdd/UpdateOrganizationsInvolved">Add / Update the organizations involved</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(7)" id="menuElement" title="${trnClickToAdd10}">
							<digi:trn key="aim:relatedOrgs">
							Related Organizations</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 7}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:relatedOrgs">
							Related Organizations</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</module:display>
				<module:display name="Contact Information" parentModule="PROJECT MANAGEMENT">
				<tr>
					<c:if test="${aimEditActivityForm.step != 8}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd11">
							<digi:trn key="aim:clickToAdd/UpdateContactPersonDetails">Add / Update the contact person details</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(8)" id="menuElement" title="${trnClickToAdd11}">
							<digi:trn key="aim:contactInformation">
							Contact Information</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 8}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:contactInformation">
							Contact Information</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</module:display>
			    <feature:display  name="Paris Indicator" module="Add & Edit Activity">
			      <tr>
			      <c:if test="${aimEditActivityForm.step != 17}">
			        <td>
			            <c:set var="translation">
							<digi:trn key="aim:clickToAdd/UpdateParisIndicators">Add / Update Paris Indicators</digi:trn>
			            </c:set>
			            <a href="javascript:fnGetSurvey(17)" id="menuElement" title="${translation}">
			              <digi:trn key="aim:editParisIndicators">Paris Indicators</digi:trn>
			            </a>
			        </td>
			        </c:if>
			        
			        <c:if test="${aimEditActivityForm.step == 17}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
										<span class="textalb">
											<digi:trn key="aim:editParisIndicators">Paris Indicators</digi:trn>
										</span>
									</td>
							</tr>
						</table>
					</td>
					</c:if>
			        
			      </tr>
			    </feature:display>
			    <module:display name="M & E" parentModule="MONITORING AND EVALUATING">
				<tr>
					<c:if test="${aimEditActivityForm.step != 10}">
					<td nowrap="nowrap">
						<c:set var="trnClickToAdd12">
							<digi:trn key="aim:clickToGoToMonitoringEvaluation">Monitoring and Evaluation</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(10)" id="menuElement" title="${trnClickToAdd12}">
							<digi:trn key="aim:MandE">
							M & E</digi:trn>
						</a>
					</td>
					</c:if>
			
					<c:if test="${aimEditActivityForm.step == 10}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
										<span class="textalb">
											<digi:trn key="aim:MandE">
												M & E
											</digi:trn>
										</span>
									</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</module:display>
			
				<!-- EU Costs -->
				<feature:display name="Costing" module="Activity Costing">
				<tr>
					<c:if test="${aimEditActivityForm.step != 11}">
					<td nowrap="nowrap">
						<c:set var="translation">
							<digi:trn key="aim:euProjectCosting">EU Project Costing</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(11)" id="menuElement" title="${translation}">
							<digi:trn key="aim:costing">Costing</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 11}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>

								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:costing">Costing</digi:trn>
						</span>
								</td>
						</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
			
			
				
				<field:display name="Add IPA Contract" feature="Contracting">
				<tr>
					<c:if test="${aimEditActivityForm.step != 13}">
					<td nowrap="nowrap">
						<c:set var="translation">
							<digi:trn key="aim:ipaContracting">IPA Contracting</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(13)" id="menuElement" title="${translation}">
							<digi:trn key="aim:ipacontracting">IPA Contracting</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 13}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td bgcolor="#0202F0" height="19">
						<span class="textalb">
							<digi:trn key="aim:ipacontracting">IPA Contracting</digi:trn>
						</span>
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</field:display>
				<tr>
					<td align="center">
					</td>
				</tr>
<!--				<feature:display name="Logframe" module="Previews">-->
<!--					<field:display name="Logframe Preview Button" feature="Logframe" >-->
<!--						<tr>-->
<!--							<td align="center">-->
<!--								<html:button  styleClass="dr-menu" property="logframe" onclick="previewLogFrameClicked()" title="${previewLogframeTitle}">-->
<!--									<digi:trn key="aim:previewLogframe">Preview Logframe</digi:trn>-->
<!--								</html:button>-->
<!--							</td>-->
<!--						</tr>-->
<!--					</field:display>-->
<!--				</feature:display>-->
<!--				<feature:display name="Preview Activity" module="Previews">-->
<!--					<field:display feature="Preview Activity" name="Preview Button">-->
<!--						<tr>-->
<!--							<td align="center">-->
<!--								<html:button  styleClass="dr-menu" property="logframe" onclick="previewClicked()" title="${previewTitle}">-->
<!--									<digi:trn key="aim:preview">Preview</digi:trn>-->
<!--								</html:button>-->
<!--							</td>-->
<!--						</tr>-->
<!--					</field:display>-->
<!--				</feature:display>			-->
<!--				<tr>-->
<!--					<td align="center">-->
<!--						<html:button  styleClass="dr-menu" property="submitButton" onclick="saveClicked()" title="${saveAndSubmitTitle}">-->
<!--							<digi:trn>Save and Submit</digi:trn>-->
<!--						</html:button>-->
<!--					</td>-->
<!--				</tr>-->
<!--				<field:display name="Draft" feature="Identification">-->
<!--				<tr>-->
<!--					<td align="center">-->
<!--						<html:button  styleClass="dr-menu" property="submitButton" onclick="saveAsDraftClicked()" title="${saveAsDraftTitle}">-->
<!--							<digi:trn key="aim:saveAsDraft">Save as draft</digi:trn>-->
<!--						</html:button>-->
<!--					</td>-->
<!--				</tr>-->
<!--				</field:display>-->
<!--				<tr>-->
<!--					<td align="center">-->
<!--						<html:button  styleClass="dr-menu" property="submitButton" onclick="closeClicked()" title="${closeTitle}">-->
<!--							<digi:trn>Close</digi:trn>-->
<!--						</html:button>-->
<!--					</td>-->
<!--				</tr>-->
			</table>
		</td>
	</tr>
</table>
</div>
