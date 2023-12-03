<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>

<script language="javascript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/jquery-ui-1.11.0/external/jquery/jquery.js"/>"></script>
<script language="javascript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/jquery-ui-1.11.0/jquery-ui.min.js"/>"></script>
<script language="javascript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/jquery.fancytree.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<digi:file src="ampModule/aim/css/ui.fancytree.css"/>">

<style>
.scorecard-container {
	width:550px; 
	margin: 0 auto;
}

.step-container {
	display:none;
	min-height: 300px;
	margin-bottom: 10px;
}

.settings-left {
    width:225px;
    display: inline-block;  
    padding-bottom: 10px;
    text-align: right;
}

.summary-text-left {
	text-align: left;
	padding-left: 30px;
	vertical-align: top;
	margin-right:30px;
}

.settings-right {
    width:210px;
    text-align: left;
    display: inline-block; 
    vertical-align: middle;
    padding-left:20px
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

.nodonors-left {float: left; width: 200px; vertical-align: middle}
.nodonors-right { display: inline-block; width: 200px; vertical-align: middle}
.nodonors-center { display: inline-block; width: 100px; padding-left:20px;}

</style>

<script type="text/javascript">
	var alertValidationTime = '<digi:trn jsFriendly="true">Please select validation time</digi:trn>';
	var alertValidationPeriod = '<digi:trn jsFriendly="true">Please check Validation Period if you have selected Validation Time</digi:trn>';
	var alertValidationPercentage = '<digi:trn jsFriendly="true">Please enter a valid percentage (0.01 - 100.00)</digi:trn>';
	var alertValidationQuarter = '<digi:trn jsFriendly="true">Please select at least one Quarter</digi:trn>';
</script>

<digi:instance property="aimScorecardManagerForm" />
<h1 class="admintitle">
	<digi:trn>Scorecard Manager</digi:trn>
</h1>
<digi:form action="/scorecardManager.do" method="post">

<html:hidden property="action" value="CANCEL"/>

<div id="scorecardErrors">
</div>

<div class="scorecard-container">
	<div>
		<div id="Step1" class="step-container">
			<fieldset>
  				<legend><digi:trn>Scorecard Settings</digi:trn></legend>
  				<div>
  					<div class="settings-blocks">
  						<div class="settings-left"><digi:trn>Validation Period</digi:trn></div>
  						<div class="settings-right"><html:checkbox property="validationPeriod"  onclick="handleValidationPeriodClick(this);"/></div>
  					</div>
  					<div class="settings-blocks">
  						<div class="settings-left"><digi:trn>Validation Time</digi:trn></div>
  						<div class="settings-right">
  							<html:select property="validationTime" onchange="handleValidationTimeChange(this);">
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
							</html:select>
						</div>
  					</div>
  					<div class="settings-blocks">
  						<div class="settings-left"><digi:trn>Percentage Threshold</digi:trn></div>
  						<div class="settings-right"><html:text property="percentageThreshold" styleClass="inp-text"/></div>
  					</div>
  					<div class="settings-blocks">
  						<div class="settings-left"><digi:trn>Activity Status</digi:trn></div>
  						<div class="settings-right"><br/>
	  						<html:select property="selectedCategoryValues" multiple="true" size="6">
								<html:optionsCollection name="aimScorecardManagerForm" property="categoryValues" value="id" label="value" />
							</html:select>
							<input id="btnUnselect" type="button" class="dr-menu" name="actUnselect" onclick="unselectAllActivities()" value="<digi:trn>Unselect All</digi:trn>"/>
  						</div>
  					</div>
					<div class="settings-blocks">
						<div class="settings-left"><digi:trn>Quarters</digi:trn></div>
						<div class="settings-right"><br/>
							<html:select property="selectedQuarters" multiple="true">
								<html:options name="aimScorecardManagerForm" property="quarters" />
							</html:select>
						</div>
					</div>
  				</div>
			</fieldset>
		</div>
			
		<div id="Step2" class="step-container">
			<fieldset>
				<legend><digi:trn>Donors to exclude</digi:trn></legend>
				<div>
					<div id="filters-container" class="panel panel-primary">
					  <div id="donorsTree"></div>
					  <div style="display:none;"><digi:trn>Selected keys</digi:trn>: <span id="echoSelection3"></span></div>
					</div>
				</div>
			</fieldset>
		</div>
			
		<div id="Step3" class="step-container">
		<fieldset>
  				<legend><digi:trn>Donors with no updates</digi:trn></legend>
				<div>
					<div class="nodonors-left">
						<select id="allDonors" multiple="true" size="15" style="width:210px"></select>
					</div>
					<div class="nodonors-center">
						<input class="dr-menu" type="button" onclick="addDonors();" value="<digi:trn>Add</digi:trn> >>"> 
						<div style="padding-top:10px"></div>
						<input class="dr-menu" type="button" onclick="removeDonors();" value=">> <digi:trn>Remove</digi:trn>"> 
					</div>
					<div class="nodonors-right">
						<select id="noUpdateDonors" multiple="true" size="15" style="width:210px"></select>
					</div>
				</div>
			</fieldset>
		</div>
			
		<div id="Step4" class="step-container">
		<fieldset>
  				<legend><digi:trn>Summary</digi:trn></legend>
				<div>
					<div class="settings-blocks">
						<div class="settings-left summary-text-left">
							<digi:trn>Validation Period</digi:trn>: 
							<b><span id="validationYes" style="display:none;"><digi:trn>Yes</digi:trn></span>
							<span id="validationNo" style="display:none;"><digi:trn>No</digi:trn></span></b>
						</div>
						<div class="settings-right">
							<digi:trn>Validation Time</digi:trn>: <b><span id="validationTime"></span>
							<span id="validationNone" style="display:none;"><digi:trn>None</digi:trn></span></b>
						</div>
					</div>
					<div class="settings-blocks">
						<div class="settings-left summary-text-left">
							<digi:trn>Activity status</digi:trn>: <br/>
							<b><span id="summaryAcitivityStatus"></span>
							<span id="validationActivityStatusesNone" style="display:none;"><digi:trn>None</digi:trn></span>
							</b>
						</div>
						<div class="settings-right">
							<digi:trn>Percentage Threshold</digi:trn>: <b><span id="percentageThreshold"></span></b>
						</div>
					</div>
					<div class="settings-blocks">
						<div class="settings-left summary-text-left">
							<digi:trn>Quarters</digi:trn>: <b><span id="summaryQuarters"></span></b>
						</div>
					</div>
					<div class="settings-blocks summary-text-left">
						<div class="settings-left summary-text-left">
						<digi:trn>Excluded donors</digi:trn>: <br/>
						<select id="summaryExcludedDonors" size="8" style="width:210px;" style="display:none;" disabled></select>
						</div>
						<div class="settings-right">
						<digi:trn>Donors with no updates</digi:trn>: <br/>
						<select id="summaryNoUpdateDonors" size="8" style="width:210px;" style="display:none;" disabled></select>
					</div>
				</div>
			</fieldset>
		</div>
	</div>
	<div style="text-align:center">
		<input id="btnPrev" type="button" class="dr-menu" name="" onclick="handleWizardPrevious()" style="display:none" value="<digi:trn>Previous</digi:trn>"/>
		<input id="btnNext" type="button" class="dr-menu" name="Step2"  onclick="handleWizardNext()" value="<digi:trn>Next</digi:trn>"/>
		<input id="btnSubmit" type="button" class="dr-menu" style="display:none" onclick="exportScorecard()" value="<digi:trn>Export</digi:trn>"/>
		<input id="btnCancel" type="submit" class="dr-menu" value="<digi:trn>Cancel</digi:trn>" />
	</div>
</div>
</digi:form>


<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/scorecard.js"/>"></script>

<script type="text/javascript">
	cleanupValidationTime();
</script>