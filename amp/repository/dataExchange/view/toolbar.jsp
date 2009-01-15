<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
	
	<div class="subtabs">
  
  
        <c:if test="${stepNum==0}">
    		<button id="step${stepNum}_prev_button" type="button" class="toolbar-dis" disabled="disabled">
    			<img height="16" src="/TEMPLATE/ampTemplate/images/prev_dis.png" class="toolbar" />
    			<digi:trn key="btn:previous">Previous</digi:trn>
    		</button>
        </c:if>
        <c:if test="${stepNum!=0}">
            <button id="step${stepNum}_prev_button" type="button" class="toolbar" onclick="navigateTab(-1);">
              <img height="16" src="/TEMPLATE/ampTemplate/images/prev.png" class="toolbar" />
              <digi:trn key="btn:previous">Previous</digi:trn>
            </button>
        </c:if>

        <c:if test="${stepNum==2}">
    		<button id="step${stepNum}_next_button" type="button" class="toolbar-dis" disabled="disabled">
    			<img height="16" src="/TEMPLATE/ampTemplate/images/next_dis.png" class="toolbar" /> 
    			<digi:trn key="btn:next">Next</digi:trn>
    		</button>
        </c:if>
        <c:if test="${stepNum!=2}">
            <button id="step${stepNum}_next_button" type="button" class="toolbar" onclick="navigateTab(1)">
              <img height="16" src="/TEMPLATE/ampTemplate/images/next.png" class="toolbar" /> 
              <digi:trn key="btn:next">Next</digi:trn>
            </button>
        </c:if>
<!--
			<feature:display  name="Filter Button" module="Report and Tab Options">
				<button id="step${stepNum}_add_filters_button" type="button" class="toolbar" onclick="repFilters.showFilters()">
					<img src="/TEMPLATE/ampTemplate/images/add_filters.png" class="toolbar" style="height: 15px;" /> 
					<digi:trn key="btn:repFilters">Filters</digi:trn>
				</button>
			</feature:display>
		<button type="button" class="toolbar-dis" disabled="disabled" name="save" 
								onclick="saveReportEngine.decideToShowTitlePanel()" >
			<img height="16" src="/TEMPLATE/ampTemplate/images/save_dis.png" class="toolbar"/>
			<digi:trn key="rep:wizard:Save">Save</digi:trn>
		</button>
		<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" disabled="disabled" name="save">
			<img src="/TEMPLATE/ampTemplate/images/save_as_dis.png" class="toolbar"/>
			<digi:trn key="rep:wizard:SaveAs">Save As..</digi:trn>
		</button>
		<button id="step${stepNum}_cancel" type="button" class="toolbar" onclick="repManager.cancelWizard();" >
			<img src="/TEMPLATE/ampTemplate/images/cancel.png" class="toolbar" />
			<digi:trn key="btn:wizard:Cancel">Cancel</digi:trn>
		</button>
-->
 	 </div>
