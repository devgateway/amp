<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"%>


<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" >.</script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" >.</script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>	
	
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/saveReports.js'/>"></script>

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
	
<!-- script for tree-like view (drilldown reports) -->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<!-- dynamic drive ajax tabs -->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>

<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicTooltip.js"/>"></script>

<link rel="stylesheet" href="/repository/aim/view/css/css_dhtmlsuite/modal-message.css" />

<script type="text/javascript">
	messageObj = new DHTMLSuite.modalMessage(); // We only create one object of this class
	messageObj.setWaitMessage('Loading message - please wait....');
	messageObj.setShadowOffset(5); // Large shadow

	DHTMLSuite.commonObj.setCssCacheStatus(false);

	function displayMessage(url) {
		messageObj.setSource(url);
		messageObj.setCssClassMessageBox(false);
		messageObj.setSize(400, 200);
		messageObj.setShadowDivVisible(true); // Enable shadow for these boxes
		messageObj.display();
	}

	function displayStaticMessage(messageContent, cssClass, width, height) {
		messageObj.setHtmlContent(messageContent);
		messageObj.setSize(width, height);
		messageObj.setCssClassMessageBox(cssClass);
		messageObj.setSource(false); // no html source since we want to use a static message here.
		messageObj.setShadowDivVisible(true); // Disable shadow for these boxes
		messageObj.display();
	}

	function closeMessage() {
		alert('closeMessage');
		messageObj.close();
	}
</script>



<script type="text/javascript">
	SaveReportEngine.connectionErrorMessage = '<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>';
	SaveReportEngine.savingMessage = '<digi:trn key="aim:reportwizard:savingData">Saving data. Please wait.</digi:trn>';
	saveReportEngine = null;
</script>

<script type="text/javascript">
	
	YAHOO.namespace("YAHOO.amptab");
	var tabView = null;
	YAHOO.amptab.init = function() {
		if (tabView == null) {
			tabView = new YAHOO.widget.TabView('tabview_container');
		}
	};

	YAHOO.amptab.handleClose = function() {
		//alert('handleClose()');
		//var wrapper = document.getElementById('myFilterWrapper');
		var wrapper = document;
		var filter = document.getElementById('filterContainer');
		$("div[id^='filter_']").hide();// IE fix
		if (filter.parent != null) {
			filter.parent.removeChild(filter);
		}
		//wrapper.appendChild(filter);
	};

	var gisPanel = new YAHOO.widget.Panel("new", {
		width :"750px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	
	gisPanel.beforeHideEvent.subscribe(YAHOO.amptab.handleClose);
	//myPanel5.beforeHideEvent.subscribe(YAHOO.amptab.handleCloseAbout);

	function initScripts() {
		//alert('initScripts');

		var msg = '\n<digi:trn key="rep:filter:filters">Filters</digi:trn>';
		gisPanel.setHeader(msg);
		gisPanel.setBody("<p>initScripts</p>");
		gisPanel.render(document.body);
	}
	function setSelectedValues(txtSelectedValuesObj,selname){
		var selectedArray = new Array();
		    $("input[name='"+selname+"']:checked").each(function(index) {
		 	   selectedArray[index] = $(this).val();
		 });
		 if(selectedArray.length>0)
		 txtSelectedValuesObj.value = selectedArray;
		
	}
	
	
	function resetPIFilters() {
		/*
		 $("input[name='selectedDonnorAgency']:checked").attr('checked', false);
		 $("input[name='selectedDonorGroups']:checked").attr('checked', false);
		 $("input[name='selectedStatuses']:checked").attr('checked', false);
		 $("input[name='selectedFinancingIstruments']:checked").attr('checked', false);
		 $("input[name='selectedSectors']:checked").attr('checked', false);
		 $("input[name='selectedSecondarySectors']:checked").attr('checked', false);
		 $("input[name='selectedTertiarySectors']:checked").attr('checked', false);
		 $("input[name='selectedNatPlanObj']:checked").attr('checked', false);
		 $("input[name='selectedPrimaryPrograms']:checked").attr('checked', false);
		 $("input[name='selectedSecondaryPrograms']:checked").attr('checked', false);
		 $("input[name='selectedDonorTypes']:checked").attr('checked', false);
		 $(".root_checkbox").attr('checked', false);
		 */
		 $("#gisFilterForm").find("input[type='checkbox']").attr('checked', false);
		 $("select[name='selectedTypeOfAssistance']").val("-1");
		 $("select[name='mapModeFin']").val("fundingData");
		 $("select[name='fundingType']").val("commitment");
		 $("select[name='mapLevel']").val(-1);		
	}
	function showFilterDiv(divId,searchId){
		var divEl=document.getElementById(divId);
		getSearchManagerInstanceById(searchId).setDiv(divEl); 
		$("div[id^='filter_']").hide();
		$('#'+divId).show();
	}
	

	function submitFilters() {
		//alert('submitfilters');
        var filterForm = document.getElementsByName("gisDashboardForm")[0];
        filterForm.selectedStartYear.value = document.getElementById("selectedStartYear").options[document.getElementById("selectedStartYear").selectedIndex].value;
        filterForm.selectedEndYear.value = document.getElementById("selectedEndYear").options[document.getElementById("selectedEndYear").selectedIndex].value;
        filterForm.selectedCalendar.value = document.getElementById("selectedCalendar").options[document.getElementById("selectedCalendar").selectedIndex].value;
        filterForm.selectedCurrency.value = document.getElementById("selectedCurrency").options[document.getElementById("selectedCurrency").selectedIndex].value;
        
        //Donors
        var txtSelectedValuesObj = filterForm.selectedDonors;
        setSelectedValues(txtSelectedValuesObj,'selectedDonors');     
       

        //groups
        var txtSelectedValuesObj = filterForm.selectedDonorGroups;
        setSelectedValues(txtSelectedValuesObj,'selectedDonorGroups');      

        //sectors
        var txtSelectedValuesObj = filterForm.selectedSectors;
        setSelectedValues(txtSelectedValuesObj,'selectedSectors');

      

        
        //filterForm.submit();
	}

	function change_donor() {
        var selObj = document.getElementById("selectedDonorGroups");
        if(selObj.options[0].selected) {
            for (i = 1; i < selObj.options.length; i++) {
                selObj.options[i].selected = false;
            }
        }
    }

	var elementIsSet = false;
	function showFilter() {
		//alert('showFilter');
		YAHOO.amptab.init();
		if (!elementIsSet) {
			elementIsSet = true;
			var element = document.getElementById("filterContainer");
			element.style.display = "inline";
			gisPanel.setBody(element);
			gisPanel.center();
		}
		gisPanel.show();
	}

	function checkRangeValues() {
		var actualFrom = document.aimReportsFilterPickerForm2.renderStartYear.value;
		var actualTo = document.aimReportsFilterPickerForm2.renderEndYear.value;
		var msg = '\n<digi:trn key="rep:filter:wrongSelecteRange">Default Start Year must be lesser than Default End Year</digi:trn>';
		if (actualFrom > actualTo) {
			alert(msg);
			return false;
		}
		return true;
	}

	function hideFilter() {
		gisPanel.hide();
	}

</script>


<style type="text/css">
.mask {
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity =   80);
	background-color: #2f2f2f;
}
</style>