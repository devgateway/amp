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

<script type="text/javascript">

	queryCbObj		= {
		success: function (o) {
			var divObj		= document.getElementById("results");
			divObj.innerHTML	= o.responseText;
			var attributes = { 
				scroll: { to: [0, YAHOO.util.Dom.getY("results")] } 
			}; 
			 var anim = new YAHOO.util.Scroll(document.body, attributes, 2); 
			 anim.animate(); 
		},
		failure: function (o) {
			var divObj		= document.getElementById("results");
			divObj.innerHTML	= "There was a problem with getting the results. Please try again";
			
		}
	
	};
	
	function submitQuery () {
		var divObj		= document.getElementById("results");
		divObj.innerHTML	= 
		"<div style='text-align: center'>" + "Please wait..." + 
		"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";
		
		var formName	= "aimReportsFilterPickerForm";
		YAHOO.util.Connect.setForm( document.getElementsByName(formName)[0] );
		var additionalParams	= "&doreset=true&queryEngine=true";
		YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true" + additionalParams, queryCbObj);
	}

	function initializeFilters() {
		filterTabs	= new YAHOO.widget.TabView('tabview_container');
		
		YAHOO.amptab.afterFiltersLoad();
		
		document.getElementById("filterPickerSubmitButton").onclick	= function() { return false;};
		YAHOO.util.Event.removeListener("filterPickerSubmitButton", "click");
		YAHOO.util.Event.addListener("filterPickerSubmitButton", "click", submitQuery);
		
		buildLabels();
	}

	YAHOO.util.Event.addListener(window, "load", initializeFilters) ;
</script>		
	<br />
	<div id="myFilter" style="height: 480px; overflow: hidden; width: 900px; margin-left: auto; margin-right: auto; border: 1px solid lightgray;" >
		<jsp:include page="/aim/reportsFilterPicker.do" />
	</div>
	<br />	
	<fieldset class="main_side_cont" style="width: 900px; margin-left: auto; margin-right: auto;">
		<legend>Selected Filters</legend>
		<div id="queryLabelsDiv"><digi:trn>No filters selected so far</digi:trn></div>
	</fieldset>
	
	
	
<div id="results" >

</div>