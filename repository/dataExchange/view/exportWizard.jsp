<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/element/element-beta.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>">.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/animation-min.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dom-min.js'/>">.</script>

	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/tab/tabview.js'/>" >.</script>

	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/ajaxconnection/connection-min.js'/>" > .</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop.js'/>" >.</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" >.</script>
<!--
    <script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>" ></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" > .</script>
-->
	
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">

	<br>
	<br>

    <digi:instance property="deExportForm" />


  
	<script type="text/javascript">
		YAHOO.namespace("YAHOO.amp.dataExchange");
		YAHOO.amp.dataExchange.numOfSteps	= 3;
		
		YAHOO.amp.dataExchange.tabLabels	= new Array("tab_select_filed", "tab_additional_filed", "tab_filter");
		selectedCols						= new Array();
		selectedHiers						= new Array();
		selectedMeas						= new Array();

		
        function navigateTab(value){
        	YAHOO.amp.dataExchange.tabView.set("activeIndex", YAHOO.amp.dataExchange.tabView.get("activeIndex")+value);
        }
		
		
		function initializeDragAndDrop() {
			var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);

//			var rd				= document.getElementsByName("reportDescription")[0];
//			rd.style.height		= (rd.parentNode.offsetHeight - 40) + "px";
			
			YAHOO.amp.dataExchange.tabView 		= new YAHOO.widget.TabView('wizard_container');
			YAHOO.amp.dataExchange.tabView.addListener("contentReady", continueInitialization);
		}
		function continueInitialization(){

            return;
            
			aimReportWizardForm.reportDescriptionClone.value	= unescape(aimReportWizardForm.reportDescription.value);
			treeObj = new DHTMLSuite.JSDragDropTree();
			treeObj.setTreeId('dhtmlgoodies_tree');
			treeObj.init();
			treeObj.showHideNode(false,'dhtmlgoodies_tree');

	
			var saveBtns		= document.getElementsByName("save");	
			for (var i=0; i<saveBtns.length; i++  ) {
				repManager.addStyleToButton(saveBtns[i]);
			}
      
			for (var i=0; i<YAHOO.amp.dataExchange.numOfSteps; i++) {
				repManager.addStyleToButton("step"+ i +"_prev_button");
				repManager.addStyleToButton("step"+ i +"_next_button");
				repManager.addStyleToButton("step"+ i +"_add_filters_button");
				repManager.addStyleToButton("step"+ i +"_cancel");
			}
			
			columnsDragAndDropObject	= new ColumnsDragAndDropObject('source_col_div');
			columnsDragAndDropObject.createDragAndDropItems();
			new YAHOO.util.DDTarget('source_measures_ul');
			new YAHOO.util.DDTarget('dest_measures_ul');
			new YAHOO.util.DDTarget('source_hierarchies_ul');
			new YAHOO.util.DDTarget('dest_hierarchies_ul');
			measuresDragAndDropObject	= new MyDragAndDropObject('source_measures_ul');
			measuresDragAndDropObject.createDragAndDropItems();
			
     
			for (var i=1; i<YAHOO.amp.dataExchange.numOfSteps; i++) {
				tab		= YAHOO.amp.dataExchange.tabView.getTab(i);
				tab.set("disabled", true);
			}
      
			tab2	= YAHOO.amp.dataExchange.tabView.getTab(2);
			tab2.addListener("beforeActiveChange", generateHierarchies);
			
			ColumnsDragAndDropObject.selectObjsByDbId ("source_col_div", "dest_col_ul", selectedCols);
			generateHierarchies();
			MyDragAndDropObject.selectObjsByDbId ("source_hierarchies_ul", "dest_hierarchies_ul", selectedHiers);
			MyDragAndDropObject.selectObjsByDbId ("source_measures_ul", "dest_measures_ul", selectedMeas);
			
			repFilters					= new Filters("${filterPanelName}", "${failureMessage}", "${filterProblemsMessage}", 
												"${loadingDataMessage}", "${savingDataMessage}", "${cannotSaveFiltersMessage}");
			saveReportEngine			= new SaveReportEngine("${savingMessage}","${failureMessage}");
			
			var dg			= document.getElementById("DHTMLSuite_treeNode1");
			var cn			= dg.childNodes;
			
			for (var i=0; i<cn.length; i++) {
				if ( cn[i].nodeName.toLowerCase()=="input" || cn[i].nodeName.toLowerCase()=="img" ||
					cn[i].nodeName.toLowerCase()=="a" )
					cn[i].style.display		= "none";
			}

			repManager.checkSteps();
		}
		YAHOO.util.Event.addListener(window, "load", initializeDragAndDrop) ;
	</script>


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="85%">
	<tr>
		<td valign="bottom">
				
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		<digi:form action="/export.do?method=export" method="post">
		<span id="formChild" style="display:none;">&nbsp;</span>

        <span class="subtitle-blue">
          &nbsp;Data Exporter
        </span>		
		
		<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv">

		</div>
		<br />
		<div id="wizard_container" class="yui-navset">
    		<ul class="yui-nav">
    			<li id="tab_select_filed" class="selected"><a href="#type_step_div"><div>1. Field Selection</div></a> </li>
    			<li id="tab_additional_filed" class="enabled"><a href="#columns_step_div"><div>2. Additional Fields</div></a> </li>
    			<li id="hierachies_tab_label" class="enabled"><a href="#hierarchies_step_div"><div>3. Team Selection and Filters</div></a> </li>
    		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="tab_select_filed" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
                    <c:set var="stepNum" value="0" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    Select fields for export
				</div>
				<div id="tab_additional_filed"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="1" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    Select additional fields
				</div>
				<div id="tab_filter"  class="yui-tab-content"  style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="2" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    Select filters and team
				</div>
			</div>
		</div>

		</digi:form>
	</td>
	</tr>
</table>

	