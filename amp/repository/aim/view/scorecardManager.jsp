<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>

<digi:instance property="aimScorecardManagerForm" />
<h1 class="admintitle">
	<digi:trn>Scorecard Settings</digi:trn>
</h1>
<digi:form action="/scorecardManager.do" method="post">
	<html:hidden property="action" value="UPDATE"/>
	<table  style="min-height:150px;border-collapse: separate; border-spacing: 5px; padding: 5px;">
		<tr>
			<td><digi:trn>Validation Period</digi:trn></td>
			<td><html:checkbox property="validationPeriod"  onclick="handleValidationPeriodClick(this);"/></td>
		</tr>
		<tr>
			<td><digi:trn>Validation Time</digi:trn></td>
			<td><html:select property="validationTime">
					<html:option value=""><digi:trn>Select value...</digi:trn>
					</html:option>
					<html:option value="1">1 <digi:trn>Week</digi:trn>
					</html:option>
					<html:option value="2">2 <digi:trn>Weeks</digi:trn>
					</html:option>
					<html:option value="3">3 <digi:trn>Weeks</digi:trn>
					</html:option>
					<html:option value="4">4 <digi:trn>Weeks</digi:trn>
					</html:option>
				</html:select></td>
		</tr>
		<tr>
			<td align="right" class="addUserButContainer"><c:set
					var="btnSubmit">
					<digi:trn key="btn:submit">Submit</digi:trn>
				</c:set> <html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return saveScoreCardSetting();" /></td>
			<td align="left" class="addUserButContainer"><c:set
					var="btnCancel">
					<digi:trn key="btn:cancel">Cancel</digi:trn>
				</c:set> <html:submit value="${btnCancel}" styleClass="dr-menu"
					onclick="return cancel()" /></td>

		</tr>


	</table>
</digi:form>
<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>

<script type="text/javascript">

function saveScoreCardSetting() {
  return  validate();

}
function validate(){
	if( $("input[name=validationPeriod]").is(":checked") && isEmpty($('select[name="validationTime"]').val())){
		alert("<digi:trn jsFriendly='true'>Please select validation period</digi:trn>");
		return false;
	}
	return true;
}
function handleValidationPeriodClick(chkbox){
	if(!chkbox.checked){
		$('select[name="validationTime"]').val("");
	}

}

</script>