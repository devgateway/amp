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

function saveAsDraftClicked() {
  var draftStatus=document.getElementById("draftFlag");
  if(draftStatus!=null){
     draftStatus.value=true;
  }
  save();
}

function save() {
  var flag = validateForm();
  if (flag == true) {
   /* document.aimEditActivityForm.saveButton.disabled = true;   	 AMP-2688 */
    <digi:context name="save" property="context/module/moduleinstance/saveActivity.do" />
    document.aimEditActivityForm.action = "<%= save %>?edit=true";
    document.aimEditActivityForm.target = "_self";
    /* ===========   	 AMP-2143
    var appstatus = document.aimEditActivityForm.approvalStatus.value;
    var wTLFlag   = document.aimEditActivityForm.workingTeamLeadFlag.value;
    var msg='';
    if (appstatus == "started") {
      msg+='<digi:trn key="aim:saveActivity:started">Do you want to submit this activity for approval ?</digi:trn>';
      if (wTLFlag == "yes") {
        //if (confirm("Do you want to approve this activity ?"))
        document.aimEditActivityForm.approvalStatus.value = "approved";
      }
      else if (confirm(msg))
      document.aimEditActivityForm.approvalStatus.value = "created";
    }
    if (appstatus == "approved") {
      if (wTLFlag != "yes")
      document.aimEditActivityForm.approvalStatus.value = "edited";
    }
    else if (wTLFlag == "yes") {
      msg+='<digi:trn key="aim:saveActivity:approved">Do you want to approve this activity ?</digi:trn>';
      if (appstatus == "created" || appstatus == "edited") {
        if (confirm(msg))
        document.aimEditActivityForm.approvalStatus.value = "approved";
      }
    }
    */
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

function fnGetSurvey() {
	<digi:context name="step" property="context/module/moduleinstance/editSurveyList.do?edit=true" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}
-->
</script>

<digi:instance property="aimEditActivityForm" />
<html:hidden property="workingTeamLeadFlag" />
<html:hidden property="pageId" />
<html:hidden property="currentValDate" />
<html:hidden property="draft" styleId="draftFlag" />


<table border=0 width="300" cellSpacing=0 cellPadding=0 vAlign="top" align="left" border=0>
	<tr>
		<td width="300" height="10" background="module/aim/images/top.gif" >
		</td>
	</tr>
	<tr>
		<td>			
			<table border=0 width="300" cellSpacing=4 cellPadding=2 vAlign="top" align="left" bgcolor="#006699">				
				<feature:display name="Identification" module="Project ID and Planning">
				<tr>
					<c:if test="${aimEditActivityForm.step != 1}">
					<td nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			            <c:set var="trnClickToAdd">
							<digi:trn key="aim:clickToAdd/UpdateActivityIdentificationFields">Add / Update Activity Identification fields</digi:trn>
			            </c:set>
						<a href="javascript:gotoStep(1)" class="menu" title="${trnClickToAdd}">
							<digi:trn key="aim:identification">
							Identification</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 1}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
								<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
									<span class="textalb">
									<digi:trn key="aim:identification">
									Identification</digi:trn>
								</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd1">
							<digi:trn key="aim:clickToAdd/UpdateActivityPlanningFields">Add / Update Activity Planning fields</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(1)" class="menu" title="${trnClickToAdd1}">
							<digi:trn key="aim:planning">
							Planning</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 1}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19" nowrap="nowrap">
									<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
									<span class="textalb">
										<digi:trn key="aim:planning">
										Planning</digi:trn>
									</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<feature:display name="References" module="References">
				<tr>
					<c:if test="${aimEditActivityForm.step != '1_5'}">
					<td nowrap="nowrap">
						<IMG alt=Link height="10" src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd1">
							<digi:trn key="aim:editMenu:referenceTitle">References</digi:trn>
						</c:set>
						<a href="javascript:gotoStep('1_5')" class="menu" title="${trnClickToAdd1}">
							<digi:trn key="aim:editMenu:References">References</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == '1_5'}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left" border="0">
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19" nowrap="nowrap">
									<IMG alt=Link height="10" src="../ampTemplate/images/arrow-th-BABAB9.gif" width="15">
									<span class="textalb">
										<digi:trn key="aim:editMenu:References">References</digi:trn>
									</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<feature:display name="Location" module="Project ID and Planning">
				<tr>
					<c:if test="${aimEditActivityForm.step != 2}">
					<td nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickTohttp://amp-demo.code.ro/aim/addActivity.do~pageId=1~reset=true~action=createAdd2">
							<digi:trn key="aim:clickToAdd/UpdateLocation">Add / Update Location</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(2)" class="menu" title="${trnClickToAdd2}">
							<digi:trn key="aim:location">
							Location</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 2}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
									<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
									<span class="textalb">
										<digi:trn key="aim:location">
											Location</digi:trn>
									</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd3">
							<digi:trn key="aim:clickToAdd/UpdateSectorsandSubsectors">Add / Update Sectors and Sub sectors</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(2)" class="menu" title="${trnClickToAdd3}>">
							<digi:trn key="aim:sectors">
							Sectors</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 2}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:sectors">
							Sectors</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd4">
							<digi:trn key="aim:clickToAdd/UpdateProgram">Add / Update Program</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(2)" class="menu" title="${trnClickToAdd4}">
							<digi:trn key="aim:program">
							Program</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 2}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19" nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:program">
							Program</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd5">
							<digi:trn key="aim:clickToAdd/UpdateFundingDetails">Add / Update Funding details</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(3)" class="menu" title="${trnClickToAdd5}">
							<digi:trn key="aim:funding">
							Funding</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 3}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:funding">
							Funding</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd6">
							<digi:trn key="aim:clickToAdd/UpdateRegionalFunding">Add / Update Regional Funding</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(4)" class="menu" title="${trnClickToAdd6}">
									<digi:trn key="aim:regionalFunding">
									Regional Funding</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 4}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
								<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
								<span class="textalb">
									<digi:trn key="aim:regionalFunding">
									Regional Funding</digi:trn>
								</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd7">
							<digi:trn key="aim:clickToAdd/UpdateComponents">Add / Update Components</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(5)" class="menu" title="${trnClickToAdd7}">
							<digi:trn key="aim:components">
							Components</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 5}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:components">
							Components</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd8">
							<digi:trn key="aim:clickToAdd/UpdateIssues">Add / Update Issues</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(5)" class="menu" title="${trnClickToAdd8}">
							<digi:trn key="aim:issues">
							Issues</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 5}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
									<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
									<span class="textalb">
										<digi:trn key="aim:issues">
										Issues</digi:trn>
									</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd9">
							<digi:trn key="aim:clickToAdd/UpdateDocumentsAndLinks">Add / Update the documents and links</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(6)" class="menu" title="${trnClickToAdd9}">
							<digi:trn key="aim:relatedDocuments">
							Related Documents</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 6}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:relatedDocuments">
							Related Documents</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</module:display>
				<tr>
					<c:if test="${aimEditActivityForm.step != 7}">
					<td nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd10">
							<digi:trn key="aim:clickToAdd/UpdateOrganizationsInvolved">Add / Update the organizations involved</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(7)" class="menu" title="${trnClickToAdd10}">
							<digi:trn key="aim:relatedOrgs">
							Related Organizations</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 7}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:relatedOrgs">
							Related Organizations</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				<tr>
					<c:if test="${aimEditActivityForm.step != 8}">
					<td nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd11">
							<digi:trn key="aim:clickToAdd/UpdateContactPersonDetails">Add / Update the contact person details</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(8)" class="menu" title="${trnClickToAdd11}">
							<digi:trn key="aim:contactInformation">
							Contact Information</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 8}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:contactInformation">
							Contact Information</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
			    <feature:display  name="Paris Indicator" module="Add & Edit Activity">
			      <tr>
			        <td>
			          <IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			            <c:set var="translation">
							<digi:trn key="aim:clickToAdd/UpdateParisIndicators">Add / Update Paris Indicators</digi:trn>
			            </c:set>
			            <a href="javascript:fnGetSurvey()" class="menu" title="${translation}">
			              <digi:trn key="aim:editParisIndicators">Paris Indicators</digi:trn>
			            </a>
			        </td>
			      </tr>
			    </feature:display>
			    <module:display name="M & E" parentModule="MONITORING AND EVALUATING">
				<tr>
					<c:if test="${aimEditActivityForm.step != 10}">
					<td nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="trnClickToAdd12">
							<digi:trn key="aim:clickToGoToMonitoringEvaluation">Monitoring and Evaluation</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(10)" class="menu" title="${trnClickToAdd12}">
							<digi:trn key="aim:MandE">
							M & E</digi:trn>
						</a>
					</td>
					</c:if>
			
					<c:if test="${aimEditActivityForm.step == 10}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
									<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
										<span class="textalb">
											<digi:trn key="aim:MandE">
												M & E
											</digi:trn>
										</span>
									</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
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
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="translation">
							<digi:trn key="aim:euProjectCosting">EU Project Costing</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(11)" class="menu" title="${translation}">
							<digi:trn key="aim:costing">Costing</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 11}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:costing">Costing</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
			
			
				<!-- Level Linker -->
				<feature:display name="Level Links" module="Activity Levels">
				<tr>
					<c:if test="${aimEditActivityForm.step != 12}">
					<td nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="translation">
							<digi:trn key="aim:levelLinksTitle">Link an activity level to another level</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(12)" class="menu" title="${translation}">
							<digi:trn key="aim:levelLinks">Level Links</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 12}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:levelLinks">Level Links</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
			
			
				<feature:display name="Contracting" module="Contracting">
				<tr>
					<c:if test="${aimEditActivityForm.step != 13}">
					<td nowrap="nowrap">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<c:set var="translation">
							<digi:trn key="aim:ipaContracting">IPA Contracting</digi:trn>
						</c:set>
						<a href="javascript:gotoStep(13)" class="menu" title="${translation}">
							<digi:trn key="aim:ipacontracting">IPA Contracting</digi:trn>
						</a>
					</td>
					</c:if>
					<c:if test="${aimEditActivityForm.step == 13}">
					<td nowrap="nowrap">
						<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0>
							<tr>
								<td width="10" height="19" background="module/aim/images/left-arc.gif">
								</td>
								<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:ipacontracting">IPA Contracting</digi:trn>
						</span>
								</td>
								<td width="10" height="19"  background="module/aim/images/right-arc.gif">
								</td>
							</tr>
						</table>
					</td>
					</c:if>
				</tr>
				</feature:display>
				<tr>
					<td align="center">
					</td>
				</tr>
				<feature:display name="Logframe" module="Previews">
					<field:display name="Logframe Preview Button" feature="Logframe" >
						<tr>
							<td align="center">
								<html:button  styleClass="dr-menu" property="logframe" onclick="previewLogFrameClicked()">
									<digi:trn key="aim:previewLogframe">Preview Logframe</digi:trn>
								</html:button>
							</td>
						</tr>
					</field:display>
				</feature:display>
				<feature:display name="Preview Activity" module="Previews">
					<field:display feature="Preview Activity" name="Preview Button">
						<tr>
							<td align="center">
								<html:button  styleClass="dr-menu" property="logframe" onclick="previewClicked()">
									<digi:trn key="aim:preview">Preview</digi:trn>
								</html:button>
							</td>
						</tr>
					</field:display>
				</feature:display>			
				<tr>
					<td align="center">
						<html:button  styleClass="dr-menu" property="submitButton" onclick="saveClicked()">
							<digi:trn key="aim:save">Save</digi:trn>
						</html:button>
					</td>
				</tr>
				<field:display name="Draft" feature="Identification">
				<tr>
					<td align="center">
						<html:button  styleClass="dr-menu" property="submitButton" onclick="saveAsDraftClicked()">
							<digi:trn key="aim:saveAsDraft">Save as draft</digi:trn>
						</html:button>
					</td>
				</tr>
				</field:display>
			</table>
		</td>
	</tr>
	<tr>
		<td width="300" height="10" background="module/aim/images/bottom.gif">
		</td>
	</tr>
</table>
