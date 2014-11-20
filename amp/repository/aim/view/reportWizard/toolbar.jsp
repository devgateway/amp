<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
	
<c:set var="className" value="buttonx"/>
<c:set var="disabledString" value=" "/>
<c:if test="${stepNum==0}">
	<c:set var="className" value="buttonx_dis"/>
	<c:set var="disabledString" value="disabled='disabled'"/>
</c:if>
	
<div>
	<div style="float:left; margin-top:10px;">
		<c:if test="${!myForm.onePager}">
			<button type="button" class="${className}" ${disabledString} onclick="repManager.previousStep(${stepNum + 1});" id="step${stepNum}_prev_button"/>
				<digi:trn key='btn:previous'>Previous</digi:trn>
			</button>
			<button type="button" value="Next" class="buttonx" onclick="repManager.nextStep(${stepNum + 1})" id="step${stepNum}_next_button"/>
				<digi:trn key="btn:next">Next</digi:trn>
			</button>
		</c:if>
	</div>
	<div style="float:right; z-index: 3000; margin-top:10px; margin-right:10px;" id="toolbarDivStep${stepNum}">
		<feature:display  name="Filter Button" module="Report and Tab Options">
			<button type="button" value="Filetrs" class="buttonx" id="step${stepNum}_add_filters_button" style="margin-right:2px;" onclick="repFilters.showFilters('<%=ReportContextData.getCurrentReportContextId(request, true)%>')"/>
				<digi:trn key="btn:repFilters">Filters</digi:trn>
			</button>
		</feature:display>
		<feature:display  name="Report Settings Button" module="Report and Tab Options">
			<button style="margin-right:2px;" type="button" value="Settings" class="buttonx" id="step${stepNum}_add_settings_button" onclick="repFilters.showSettings()"/>
				<digi:trn key="btn:repFilters">Settings</digi:trn>
			</button>
		</feature:display>
		<button style="margin-right:2px;" type="button" name="save" disabled="disabled" onclick="saveReportEngine.saveOverwrite()" class="buttonx_dis"/>
			<digi:trn key="rep:wizard:Save">Save</digi:trn>
		</button>
		<button style="margin-right:2px;" type="button" name="save" disabled="disabled" onclick="saveReportEngine.saveNoOverwrite()" class="buttonx_dis"/>
			<digi:trn key="rep:wizard:SaveAs">Save As..</digi:trn>
		</button>
		<button style="margin-right:2px;" type="button" value="Cancel" class="buttonx" id="step${stepNum}_cancel" onclick="repManager.cancelWizard();"/>
			<digi:trn key="btn:wizard:Cancel">Cancel</digi:trn>
		</button>
	</div>
</div>