	<div class="subtabs">
		<button id="step${stepNum}_prev_button" type="button" class="toolbar"
			onclick="repManager.previousStep();">
			<img src="/TEMPLATE/ampTemplate/images/prev.png" class="toolbar" />
			<digi:trn key="btn:previous">Previous</digi:trn>
		</button>
		<button id="step${stepNum}_next_button" type="button" class="toolbar-dis" 
			onclick="repManager.nextStep()" disabled="disabled">
			<img height="16" src="/TEMPLATE/ampTemplate/images/next_dis.png" class="toolbar" /> 
			<digi:trn key="btn:next">Next</digi:trn>
		</button>
		<button id="step${stepNum}_add_filters_button" type="button" class="toolbar" onclick="repFilters.showFilters()">
			<img src="/TEMPLATE/ampTemplate/images/add_filters.png" class="toolbar" style="height: 15px;" /> 
			<digi:trn key="btn:repFilters">Filters</digi:trn>
		</button>
		<button type="button" class="toolbar-dis" disabled="disabled" name="save" 
								onclick="saveReportEngine.decideToShowTitlePanel()" >
			<img height="16" src="/TEMPLATE/ampTemplate/images/save_dis.png" class="toolbar"/>
			<digi:trn key="rep:wizard:Save">Save</digi:trn>
		</button>
		<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" disabled="disabled" name="save">
			<img src="/TEMPLATE/ampTemplate/images/save_as_dis.png" class="toolbar"/>
			<digi:trn key="rep:wizard:SaveAs">Save As..</digi:trn>
		</button>
		
	</div>