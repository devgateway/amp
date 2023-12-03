<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>

<!-- Individual YUI CSS files -->
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css">
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">

<link rel="stylesheet" href="/TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter.css">
<link href='tabs/fonts/open-sans.css' rel='stylesheet' type='text/css'>

<digi:ref href="css_2/report_html2_view.css" type="text/css" rel="stylesheet" />

<%-- TODO: These styles could be inside the filter so we can reuse "embedded". --%>
<style type="text/css">
	#filter-popup {
		position: static !important;
		margin-left: 5% !important;
	}

	.panel-heading {
		display: none;
	}

	.panel-footer {
		padding: 20px 15px;
	}

	.tab_opt_box_cont {
		font-size: 11px;
		color: #444;
		background-color: #FFF;
	}

	.main_side_cont {
		border-radius: 4px;
		border: 1px solid #428bca;
	}
</style>

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script> 
<script type="text/javascript" src="/repository/aim/view/multilingual/multilingual_scripts.js"></script>

<!-- Individual YUI JS files -->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
<script type="text/javascript" src="<digi:file src="ampModule/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript"
		src="<digi:file src="ampModule/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>

<script type="text/javascript" src="<digi:file src='ampModule/aim/scripts/filters/searchManager.js'/>" ></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/saikuui_reports/js/backbone/underscore.js"></script>
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/backbone/backbone.js" type="text/javascript"></script>
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/jquery/jquery.min.js" type="text/javascript"></script>
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/jquery/jquery-ui.min.js" type="text/javascript"></script>
<script src="/TEMPLATE/ampTemplate/script/common/lib/jquery-ui-i18n.min.js" type="text/javascript"></script>
<script type="text/javascript"
		src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter.js"/>"></script>
<script type="text/javascript" src="<digi:file src='ampModule/aim/scripts/reportWizard/prefilters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='ampModule/aim/scripts/reportWizard/filterWidgetLoader.js'/>"></script>

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>
<jsp:include page="/repository/aim/view/saveReports/dynamicSaveReportsAndFilters.jsp" />

<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> 
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn> 
</c:set>

<script type="text/javascript">

queryValidCbObj	= {
		success: function (o) {
			var valid=o.responseXML.
		       getElementsByTagName("result")[0].getAttribute("valid");
			if(valid=='true'){
				submitQuery ();
			}
			else{
				alert('<digi:trn jsFriendly="true">From date should be greater than to Date</digi:trn>');
			}
			 
		},
		failure: function (o) {
			alert('<digi:trn jsFriendly="true">Error!</digi:trn>');
			
		}
	
	};
			queryCbObj		= {
			success: function (o) {
				var divObj		= document.getElementById("queryLabelsDiv");
				divObj.innerHTML	= o.responseText;
				animToResult();
			},
			failure: function (o) {
				var divObj		= document.getElementById("queryLabelsDiv");
				divObj.innerHTML	= "There was a problem with getting the results. Please try again";
				
			}
		
		};
	
	function animToResult () {
		var attributes = { 
			scroll: { to: [YAHOO.util.Dom.getDocumentScrollTop(), YAHOO.util.Dom.getY("results")] } 
		}; 
		 var anim = new YAHOO.util.Scroll(document.body, attributes, 1); 
		 anim.animate(); 
	}
	
	function showlegend() {
		var contentId = document.getElementById("show_legend_pop_box");
  		contentId.style.display == "block" ? contentId.style.display = "none" : contentId.style.display = "block"; 
	}
	
	function toggleSettings(){
		var currentDisplaySettings = document.getElementById('currentDisplaySettings');
		var displaySettingsButton = document.getElementById('displaySettingsButton');
		if(currentDisplaySettings.style.display == "block"){
			currentDisplaySettings.style.display = "none";
			displaySettingsButton.innerHTML = "${showCurrSettings}";
		}
		else
		{
			currentDisplaySettings.style.display = "block";
			displaySettingsButton.innerHTML = "${hideCurrSettings}";
		}
	}
function validateSubmitQuery () {
		var formName	= "aimReportsFilterPickerForm";
		var filterForm		= document.getElementsByName(formName)[0];
		var fromDate = filterForm.fromDate.value;
		var toDate = filterForm.toDate.value;
		if (fromDate != null && fromDate != '' && toDate != null && toDate != ''){
			var additionalParams	= "fromDate=" + fromDate + "&toDate=" + toDate;
			YAHOO.util.Connect.asyncRequest("POST", "/aim/validateReportsFilterPicker.do" , queryValidCbObj, additionalParams);
		}
		else{
			submitQuery ();
		}
	}
	
	function submitQuery () {
		//debugger;
		var formName	= "aimReportsFilterPickerForm";
		var filterForm		= document.getElementsByName(formName)[0];
		var divObj		= document.getElementById("results");
		divObj.innerHTML	= 
		"<div style='text-align: center'>" + "Please wait..." + 
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";
		YAHOO.util.Connect.setForm(filterForm);
		var additionalParams	= "&doreset=true&queryEngine=true&reportContextId=" + '<%=ReportContextData.getCurrentReportContextId(request, true)%>';
		YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true" + additionalParams, queryCbObj);
		animToResult();
	}
	
	function changeStep(url) {
		var divObj		= document.getElementById("queryLabelsDiv");
		divObj.innerHTML	=
		"<div style='text-align: center'>" + "Please wait..." + 
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";
		
		
		YAHOO.util.Connect.asyncRequest("GET", url, queryCbObj);
	}

	function initializeFilters() {
		filterTabs	= new YAHOO.widget.TabView('tabview_container');
		YAHOO.amptab.afterFiltersLoad();
	}

	YAHOO.util.Event.addListener(window, "load", initializeFilters) ;
</script>		
<br />
<div id="filter-popup" style="font-size: 14px !important; position: unset !important; margin: 0%;"></div>

<div class="content-dir">
	<fieldset class="main_side_cont" style="width: 900px; margin-left: auto; margin-right: auto;">
		<legend><digi:trn>Selected Filters</digi:trn></legend>
		<div id="queryLabelsDiv"><digi:trn>No filters selected so far</digi:trn></div>
	</fieldset>

	<br />
	<div id="results" >

	</div>
</div>

<script type="text/javascript">
	$(document).ready(function () {
	    // TODO: Use a different constructor.
		repFilters = new Filters('', '', '', '', '', '', '', '', '', true);
		repFilters.showFilters('report_wizard', '<%=ReportContextData.getCurrentReportContextId(request, true)%>');
	});
</script>
