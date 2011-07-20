<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
	
<c:set var="className" value="buttonx"/>
<c:set var="disabledString" value=" "/>
<c:if test="${stepNum==0}">
	<c:set var="className" value="buttonx_dis"/>
	<c:set var="disabledString" value="disabled='disabled'"/>
</c:if>
	
<div>
	<div style="float:left;">
		<c:if test="${!myForm.onePager}">
			<button type="button" class="${className}" ${disabledString} onclick="repManager.previousStep(${stepNum + 1});" id="step${stepNum}_prev_button"/>
				<digi:trn key='btn:previous'>Previous</digi:trn>
			</button>
			<button type="button" value="Next" class="buttonx" onclick="repManager.nextStep(${stepNum + 1})" id="step${stepNum}_next_button"/>
				<digi:trn key="btn:next">Next</digi:trn>
			</button>
		</c:if>
	</div>
	<div style="float:right; z-index: 3000;" id="toolbarDivStep${stepNum}">
		<feature:display  name="Filter Button" module="Report and Tab Options">
			<button type="button" value="Filetrs" class="buttonx" id="step${stepNum}_add_filters_button" onclick="repFilters.showFilters()"/>
				<digi:trn key="btn:repFilters">Filters</digi:trn>
			</button>
		</feature:display>
		<button type="button" name="save" disabled="disabled" onclick="saveReportEngine.decideToShowTitlePanel()" class="buttonx_dis"/>
			<digi:trn key="rep:wizard:Save">Save</digi:trn>
		</button>
		<button type="button" name="save" disabled="disabled" onclick="saveReportEngine.showTitlePanel()" class="buttonx_dis"/>
			<digi:trn key="rep:wizard:SaveAs">Save As..</digi:trn>
		</button>
		<button type="button" value="Cancel" class="buttonx" id="step${stepNum}_cancel" onclick="repManager.cancelWizard();"/>
			<digi:trn key="btn:wizard:Cancel">Cancel</digi:trn>
		</button>
	</div>
</div>