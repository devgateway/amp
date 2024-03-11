<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/resources/tld/fn.tld" prefix="fn"%>

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
				<digi:trn>Data Freeze Settings</digi:trn>
			</legend>
			<div>
				<div class="settings-blocks">
					<div class="settings-left">
						<digi:trn>Enabled</digi:trn>
						<img src="../ampTemplate/images/help.gif" border="0"
							title="<digi:trn key="admin:dataFreezeManager">Block users from editing financial information for the subsequent quarter after which it was entered</digi:trn>">
					</div>
					<div class="settings-right">
						<html:checkbox property="enabled" />
					</div>
				</div>
				<div class="settings-blocks">
					<div class="settings-left">
						<digi:trn>Grace Period</digi:trn>
						<img src="../ampTemplate/images/help.gif" border="0"
							title="<digi:trn key="admin:dataFreezeManager">Number of days after the quarter in which the user can continue to edit data. To disable grace period enter 0</digi:trn>">
					</div>
					<div class="settings-right">
						<html:text property="gracePeriod" styleClass="inp-text" />
					</div>
				</div>
				<div class="settings-blocks">
					<div style="text-align: center">
						<input id="btnCancel" type="submit"
							onclick="return saveDataFreezeManager();" class="dr-menu"
							value="<digi:trn>Save</digi:trn>" />
							<input id="btnCancel"
							type="submit" class="dr-menu" value="<digi:trn>Cancel</digi:trn>" />
					</div>
				</div>
			</div>
		</fieldset>

	</div>
</digi:form>


<script language="JavaScript" type="text/javascript"
	src="/repository/aim/view/scripts/common.js"></script>

<script language="JavaScript" type="text/javascript">
$( document ).ready(function() {
	$('input[name="gracePeriod"]').attr('disabled',!$('input[name="enabled"]').is(':checked'));
	
	$('input[name="enabled"]').on("click",function(){
		$('input[name="gracePeriod"]').attr('disabled',!$('input[name="enabled"]').is(':checked'));
		$('input[name="gracePeriod"]').val('');
	});
	
	//only allow numbers
	$('input[name="gracePeriod"]').on("keydown",function () {
		// Allow only backspace and delete
		if ( event.keyCode == 46 || event.keyCode == 8 ) {
		}
		else {
			// Ensure that it is a number and stop the keypress
				if(!( (event.keyCode >=48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105) )){
					event.preventDefault();	
					}	
				}
	});
});	

	function saveDataFreezeManager() {
		var alertGracePeriod = '<digi:trn jsFriendly="true">Grace period should be between 0 and 30</digi:trn>';
		if ($('input[name="enabled"]').is(':checked') && (  isEmpty($('input[name="gracePeriod"]').val())
				|| $('input[name="gracePeriod"]').val() < 0
				|| $('input[name="gracePeriod"]').val() > 30)) {
			alert(alertGracePeriod);
			return false;
		}

		$('input[name=action]').val("SAVE");
		return true;
	}
</script>