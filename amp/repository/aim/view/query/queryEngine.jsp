<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
  
<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css">
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">

<digi:ref href="css_2/report_html2_view.css" type="text/css" rel="stylesheet" /> 

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
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>
		
		<script type="text/javascript" src="<digi:file src='module/aim/scripts/query/QueryManager.js'/>" ></script>	

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
				var divObj		= document.getElementById("results");
				divObj.innerHTML	= o.responseText;
				animToResult();
			},
			failure: function (o) {
				var divObj		= document.getElementById("results");
				divObj.innerHTML	= '<digi:trn jsFriendly="true">There was a problem with getting the results. Please try again</digi:trn>';
				
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
		"<div style='text-align: center'>" + '<digi:trn jsFriendly="true">Please wait</digi:trn>' +
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";
		YAHOO.util.Connect.setForm(filterForm);
		var additionalParams	= "&doreset=true&queryEngine=true&reportContextId=" + '<%=ReportContextData.getCurrentReportContextId(request, true)%>';
		YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true" + additionalParams, queryCbObj);
		animToResult();
	}
	
	function changeStep(url) {
		var divObj		= document.getElementById("results");
		divObj.innerHTML	= 
		"<div style='text-align: center'>" + '<digi:trn jsFriendly="true">Please wait</digi:trn>' +
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";
		
		
		YAHOO.util.Connect.asyncRequest("GET", url, queryCbObj);
	}

	function initializeFilters() {
		filterTabs	= new YAHOO.widget.TabView('tabview_container');
		
		YAHOO.amptab.afterFiltersLoad();
		
		document.getElementById("filterPickerSubmitButton").onclick	= function() { return false;};
		YAHOO.util.Event.removeListener("filterPickerSubmitButton", "click");
		YAHOO.util.Event.addListener("filterPickerSubmitButton", "click",  validateSubmitQuery);		
		
		YAHOO.util.Event.addListener("filterPickerResetButton", "click", buildLabels);		
		
		buildLabels();
	}

	YAHOO.util.Event.addListener(window, "load", initializeFilters) ;
</script>		
	<br />
	<div id="myFilter" style="height: 480px; overflow: hidden; width: 900px; margin-left: auto; margin-right: auto; border: 1px solid lightgray;" >
		<jsp:include page="/aim/reportsFilterPicker.do" />
	</div>
	<br />
<div class="content-dir">
	<fieldset class="main_side_cont" style="width: 900px; margin-left: auto; margin-right: auto;">
		<legend><digi:trn>Selected Filters</digi:trn></legend>
		<div id="queryLabelsDiv"><digi:trn>No filters selected so far</digi:trn></div>
		<div style="text-align: center;">
			<button class="buttonx_sm" id="refershResultsButton" onclick="submitQuery();"><digi:trn>Refresh Results</digi:trn></button> 
		</div>
	</fieldset>

	<br />
	<div id="results" >

	</div>
</div>