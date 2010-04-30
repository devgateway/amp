<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
	
	<c:set var="className" value="toolbar"/>
	<c:set var="imgName" value="prev.png"/>
	<c:set var="disabledString" value=" "/>
	<c:if test="${stepNum==0}">
		<c:set var="disabledString" value="disabled='disabled'"/>
	</c:if>
		
	<div class="subtabs">
    	  <c:if test="${stepNum==0}">
            <button id="step${stepNum}_prev_button" type="button" class="toolbar-dis" disabled="disabled">
      			<img src="/TEMPLATE/ampTemplate/images/prev_dis.png" class="toolbar" />
    	   		<digi:trn key="btn:previous">Previous</digi:trn>
            </button>
          </c:if>
		  <c:if test="${stepNum!=0}">
            <button id="step${stepNum}_prev_button" type="button" class="toolbar" onclick="navigateTab(-1);">
    			<img src="/TEMPLATE/ampTemplate/images/prev.png" class="toolbar" />
    			<digi:trn key="btn:previous">Previous</digi:trn>
            </button>
          </c:if>
          <c:if test="${stepNum==2}">
	        <button id="step${stepNum}_next_button" type="button" class="toolbar-dis" disabled="disabled"">
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
    	<button id="step${stepNum}_cancel" type="button" class="toolbar" onclick="cancelBack()" name="cancel">
			<img src="/TEMPLATE/ampTemplate/images/cancel.png" class="toolbar" />
			<digi:trn key="btn:wizard:Cancel">Cancel</digi:trn>
		</button>
    	<button id="step${stepNum}_export" name="expodtButton" type="button" class="toolbar" onclick="exportActivity()" >
			<img src="/TEMPLATE/ampTemplate/images/file-export-16x16.png" class="toolbar"/>
			<digi:trn key="btn:wizard:Export">Export</digi:trn>
		</button>
        <button id="step${stepNum}_log" name="logButton" type="button" class="toolbar" onclick="exportLog()" >
          <img src="/TEMPLATE/ampTemplate/images/file-export-16x16.png" class="toolbar"/>
          <digi:trn key="btn:wizard:ExportLog">Log</digi:trn>
        </button>
 	 </div>

