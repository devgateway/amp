<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>

<script language="javascript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/jquery-ui-1.11.0/external/jquery/jquery.js"/>"></script>
<script language="javascript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/jquery-ui-1.11.0/jquery-ui.min.js"/>"></script>
<script language="javascript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/jquery.fancytree.js"/>"></script>



<digi:instance property="aimDataFreezeManagerForm" />
<h1 class="admintitle">
	<digi:trn>Data Freeze Manager</digi:trn>
</h1>
<script language="JavaScript" type="text/javascript">
	
</script>
<style>
.scorecard-container {
	width: 465px;
	margin: 0 auto;
}

.scorecard-container fieldset {
	height: 180px;
}

.step-container {
	display: none;
	height: 300px;
}

.settings-left {
	width: 225px;
	display: inline-block;
	padding-bottom: 10px;
	text-align: right;
}



.settings-right {
	width: 210px;
	text-align: left;
	display: inline-block;
	vertical-align: middle;
	padding-left: 20px
}

.settings-blocks {
	display: inline-block;
	width: 100%;
	padding: 0;
	vertical-align: top;
}

#scorecardErrors {
	color: red;
	font-weight: bold;
}

</style>
<digi:form action="/dataFreezeManager.do" method="post">

	<html:hidden property="action" value="CANCEL" />

	<div id="dataFreezeErrors"></div>

	<div class="scorecard-container">
		<fieldset>
			<legend>
				<digi:trn>Data freeze Settings</digi:trn>
			</legend>
			<div>
				<div class="settings-blocks">
					<div class="settings-left">
						<digi:trn>Enabled</digi:trn>
					</div>
					<div class="settings-right">
						<html:checkbox property="enabled"
							onclick="handleValidationPeriodClick(this);" />
					</div>
				</div>
				<div class="settings-blocks">
					<div class="settings-left">
						<digi:trn>Grace Period</digi:trn>
					</div>
					<div class="settings-right">
						<html:text property="gracePeriod" styleClass="inp-text" />
					</div>
				</div>
				<div class="settings-blocks">
					<div class="settings-left">
						<input id="btnCancel" type="submit"
							onclick="return saveDataFreezeManager();" class="dr-menu"
							value="<digi:trn>Save</digi:trn>" />
					</div>
					<div class="settings-right">
						<input id="btnCancel" type="submit" class="dr-menu"
							value="<digi:trn>Cancel</digi:trn>" />
					</div>
				</div>
			</div>
		</fieldset>

	</div>
</digi:form>


<script language="JavaScript" type="text/javascript"
	src="/repository/aim/view/scripts/common.js"></script>

<script language="JavaScript" type="text/javascript">
	function saveDataFreezeManager() {
		var alertGracePeriod = '<digi:trn>Grace period should be between 1 and 30</digi:trn>';
		debugger;
		if (isEmpty($('input[name="gracePeriod"]').val())
				|| $('input[name="gracePeriod"]').val() < 1
				|| $('input[name="gracePeriod"]').val() > 31) {
			alert(alertGracePeriod);
			return false;
		}

		$('input[name=action]').val("SAVE");
		return true;
	}
</script>