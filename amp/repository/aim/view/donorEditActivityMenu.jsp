<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>





<script language="JavaScript">

<!--

function previewClicked() {
	document.aimEditActivityForm.step.value = "9";
	document.aimEditActivityForm.pageId.value = "1";
	<digi:context name="preview" property="context/ampModule/moduleinstance/previewActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= preview %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
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
	document.aimEditActivityForm.saveButton.disabled = true;
    document.aimEditActivityForm.saveAsDraftButton.disabled = true;
	<digi:context name="save" property="context/ampModule/moduleinstance/saveActivity.do" />
	document.aimEditActivityForm.action = "<%= save %>";
	document.aimEditActivityForm.target = "_self";
	var appstatus = document.aimEditActivityForm.approvalStatus.value;
	var wTLFlag   = document.aimEditActivityForm.workingTeamLeadFlag.value;
	if (appstatus == "started") {
		msg += '<digi:trn key="aim:saveActivity:started" jsFriendly="true">Do you want to submit this activity for approval ?</digi:trn>';
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}else if (confirm(msg))
				document.aimEditActivityForm.approvalStatus.value = "created";
	}

	if (appstatus == "approved") {
		if (wTLFlag != "yes")
			document.aimEditActivityForm.approvalStatus.value = "edited";
	}else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			msg+= '<digi:trn key="aim:saveActivity:approved" jsFriendly="true">Do you want to approve this activity ?</digi:trn>';
			if (confirm(msg))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
}


function gotoStep(value) {
  var draftStatus=document.getElementById("draftFlag");
  var flag;
  if(draftStatus!=null && draftStatus.value!="true"){
    flag=validateForm();
  }else{
    flag=true;
  }
  if (flag == true) {
    document.aimEditActivityForm.step.value = value;
    <digi:context name="step" property="context/ampModule/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= step %>";
    document.aimEditActivityForm.target = "_self";
    document.aimEditActivityForm.submit();
  }
}



function fnGetSurvey() {
	<digi:context name="step" property="context/ampModule/moduleinstance/editSurveyList.do?edit=true" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}



-->

</script>



<digi:instance property="aimEditActivityForm" />

<html:hidden property="workingTeamLeadFlag" />
<html:hidden property="draft" styleId="draftFlag" />

<input type="hidden" name="edit" value="true">

<html:hidden property="pageId" />

<html:hidden property="approvalStatus" />

<table width="209" cellspacing="0" cellpadding="0" vAlign="top" align="left" border="0">

<tr><td width="209" height="10" background="ampModule/aim/images/top.gif">

</td></tr>



<tr><td>

<table width="209" cellSpacing=4 cellPadding=2 vAlign="top" align="left"

bgcolor="#006699">

	<tr>

		<c:if test="${aimEditActivityForm.step != '3'}">

		<td>

			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>

			<c:set var="translation">

				<digi:trn key="aim:clickToAdd/UpdateFundingDetails">Add / Update Funding details</digi:trn>

			</c:set>

			<a href="javascript:gotoStep(3)" class="menu" title="${translation}">

				<digi:trn key="aim:funding">

				Funding</digi:trn>

			</a>

		</td>

		</c:if>

		<c:if test="${aimEditActivityForm.step == '3'}">

		<td>

			<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left border="0">

				<tr>

					<td width="10" height="19" background="ampModule/aim/images/left-arc.gif">

					</td>

					<td bgcolor="#3399ff" height="19">

			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>

			<span class="textalb">

				<digi:trn key="aim:funding">

				Funding</digi:trn>

			</span>

					</td>

					<td width="10" height="19"  background="ampModule/aim/images/right-arc.gif">

					</td>

				</tr>

			</table>

		</td>

		</c:if>

	</tr>

	<tr>

        <feature:display  name="Paris Indicator" ampModule="Add & Edit Activity">
		<c:if test="${aimEditActivityForm.step != '17'}">

		<td>

			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<c:set var="translation">
			<digi:trn key="aim:clickToAdd/ParisIndicator">Add / Update Paris Indicator</digi:trn>
			</c:set>
			<a href="javascript:fnGetSurvey()" class="menu" title="${translation}">
				<digi:trn key="aim:editParisIndicators">Paris Indicators</digi:trn>
			</a>

		</td>

		</c:if>
        </feature:display>

		<c:if test="${aimEditActivityForm.step == '17'}">

		<td>

			<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left border="0">

				<tr>

					<td width="10" height="19" background="ampModule/aim/images/left-arc.gif">

					</td>

					<td bgcolor="#3399ff" height="19">

						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>

						<span class="textalb">

							<digi:trn key="aim:editParisIndicators">Paris Indicators</digi:trn>

						</span>

					</td>

					<td width="10" height="19"  background="ampModule/aim/images/right-arc.gif">

					</td>

				</tr>

			</table>

		</td>

		</c:if>

	</tr>

	<tr>

		<td align="center">

		</td>

	</tr>

	<tr>

		<td align="center">

			<html:button  styleClass="dr-menu" property="submitButton" onclick="previewClicked()">
				<digi:trn key="btn:preview">Preview</digi:trn>
			</html:button>

		</td>

	</tr>

	<tr>
		<td align="center">
			<html:button  styleClass="dr-menu" property="saveButton"  onclick="saveClicked()">
				<digi:trn key="btn:save">Save</digi:trn>
			</html:button>
		</td>
	</tr>
	<tr>
		<td align="center">
			<html:button  styleClass="dr-menu" property="saveAsDraftButton"  onclick="saveAsDraftClicked()">
				<digi:trn key="btn:saveAsDraft">Save as draft</digi:trn>
			</html:button>
		</td>
	</tr>
</table>

</td></tr>

<tr><td width="209" height="10" background="ampModule/aim/images/bottom.gif">

</td></tr>

</table>




