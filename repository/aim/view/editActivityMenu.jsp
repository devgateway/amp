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

<script language="JavaScript">
<!--
function previewClicked() {
	var flag = validateForm();
	if (flag == true) {
	document.aimEditActivityForm.step.value = "9";
	document.aimEditActivityForm.pageId.value = "1";
	<digi:context name="preview" property="context/module/moduleinstance/previewActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= preview %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	}
}

function previewLogFrameClicked() {
	var flag = validateForm();
	if (flag == true) {
	document.aimEditActivityForm.step.value = "9";
	document.aimEditActivityForm.pageId.value = "1";
	openResisableWindow(700, 650);
	<digi:context name="preview" property="context/module/moduleinstance/previewActivity.do?edit=true&logframe=true" />
	document.aimEditActivityForm.action = "<%= preview %>";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
	}
}

function saveClicked() {
	var flag = validateForm();
	if (flag == true) {
	document.aimEditActivityForm.saveButton.disabled = true;
	<digi:context name="save" property="context/module/moduleinstance/saveActivity.do" />
	document.aimEditActivityForm.action = "<%= save %>?edit=true";
	document.aimEditActivityForm.target = "_self";
	var appstatus = document.aimEditActivityForm.approvalStatus.value;
	var wTLFlag   = document.aimEditActivityForm.workingTeamLeadFlag.value;
	if (appstatus == "started") {
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
		else if (confirm("Do you want to submit this activity for approval ?"))
				document.aimEditActivityForm.approvalStatus.value = "created";
	}
	if (appstatus == "approved") {
		if (wTLFlag != "yes")
			document.aimEditActivityForm.approvalStatus.value = "edited";
	}
	else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
}
}

function gotoStep(value) {
	var flag = validateForm();
	if (flag == true) {
		document.aimEditActivityForm.step.value = value;
		<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
		document.aimEditActivityForm.action = "<%= step %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}
}

-->
</script>

<digi:instance property="aimEditActivityForm" />
<html:hidden property="approvalStatus" />
<html:hidden property="workingTeamLeadFlag" />
<html:hidden property="pageId" />
<html:hidden property="currentValDate" />

<table width="209" cellSpacing=0 cellPadding=0 vAlign="top" align="left" border=0>
<tr><td width="209" height="10" background="module/aim/images/top.gif">
</td></tr>
<tr><td>
<table width="209" cellSpacing=4 cellPadding=2 vAlign="top" align="left" 
bgcolor="#006699">
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
	<tr>
		<c:if test="${aimEditActivityForm.step != 2}">
		<td nowrap="nowrap">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<c:set var="trnClickToAdd2">
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
					<td bgcolor="#3399ff" height="19">
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
	<module:display name="Funding">
	<feature:display name="Funding Organizations"  module="Funding">
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
	</module:display>
	<module:display name="Components and Issues">
	<feature:display name="Components" module="Components and Issues">
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
	<feature:display name="Issues" module="Components and Issues">
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
	</module:display>
	<module:display name="Document Management">
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
	<module:display name="Organizations">
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
	</module:display>
	<module:display name="Contact Information">
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
	</module:display>
	
	<module:display name="Trend Analysis and Forecasting">
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
	<module:display name="Activity Costing">
	<tr>
		<c:if test="${aimEditActivityForm.step != 11}">
		<td nowrap="nowrap">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:euProjectCosting">EU Project Costing</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(11)" class="menu" title="<%=translation%>">
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
	</module:display>
	
	
	<tr>
		<td align="center">
		</td>
	</tr>
	<logic:empty name="SA" scope="application">
	<tr>
		<td align="center">
			<input type="button" value='<digi:trn key="aim:previewLogframe">Preview Logframe</digi:trn>' class="buton" onclick="previewLogFrameClicked()" name="logframe">
		</td>
	</tr>
	</logic:empty>
	<tr>
		<td align="center">
			<input type="button" value='<digi:trn key="aim:preview">Preview</digi:trn>' class="buton" onclick="previewClicked()">
		</td>
	</tr>

	<tr>
		<td align="center">
			<input type="button" value='<digi:trn key="aim:save">Save</digi:trn>' name="saveButton" class="buton" onclick="saveClicked()">
		</td>
	</tr>
</table>
</td></tr>
<tr><td width="220" height="10" background="module/aim/images/bottom.gif">
</td></tr>
</table>
