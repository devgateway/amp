<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"%>
<link rel="stylesheet" href="<digi:file src="module/aim/css/newamp.css"/>" />
	

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" ></script>


<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
	style="visibility: hidden; position: absolute; z-index: 1000; top: -100;"></DIV>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

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
<%-- <link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>"> --%>


<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/treeview.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
 
 <script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event/event-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/treeview/treeview-min.js"></script>
    

<script type="text/javascript">
	messageObj = new DHTMLSuite.modalMessage(); // We only create one object of this class
	messageObj.setWaitMessage('Loading...');
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


<!-- virtual pagination -->
<script type="text/javascript"
	src="<digi:file src="module/aim/scripts/virtualpaginate.js"/>">
	/***********************************************
	 * Virtual Pagination script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
	 * This notice MUST stay intact for legal use
	 * Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
	 ***********************************************/
</script>

<style type="text/css">
/*Sample CSS used for the Virtual Pagination Demos. Modify/ remove as desired*/
.virtualpage,.virtualpage2,.virtualpage3 {
	/*hide the broken up pieces of contents until script is called. Remove if desired*/
	display: none;
}

.paginationstyle { /*Style for demo pagination divs*/
	width: 250px;
	text-align: center;
	padding: 2px 0;
	margin: 10px 0;
}

.paginationstyle select {
	/*Style for demo pagination divs' select menu*/
	border: 1px solid navy;
	margin: 0 15px;
}

.paginationstyle a { /*Pagination links style*/
	padding: 0 5px;
	text-decoration: none;
	border: 1px solid black;
	color: navy;
	background-color: white;
}

.paginationstyle a:hover,.paginationstyle a.selected {
	color: #000;
	background-color: #FEE496;
}

.paginationstyle a.imglinks {
	/*Pagination Image links style (class="imglinks") */
	border: 0;
	padding: 0;
}

.paginationstyle a.imglinks img {
	vertical-align: bottom;
	border: 0;
}

.paginationstyle a.imglinks a:hover {
	background: none;
}

.paginationstyle .flatview a:hover,.paginationstyle .flatview a.selected
	{ /*Pagination div "flatview" links style*/
	color: #000;
	background-color: yellow;
}
</style>



<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/saveReports.js'/>"></script>

<!-- END - For DHTML Tab View of Filters -->

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>

<script type="text/javascript">
	SaveReportEngine.connectionErrorMessage = '<digi:trn jsFriendly="true" key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>';
	SaveReportEngine.savingMessage = '<digi:trn jsFriendly="true" key="aim:reportwizard:savingData">Saving data. Please wait.</digi:trn>';
	saveReportEngine = null;
</script>

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amptab");
	YAHOO.amptab.init = function() {
		var tabView = new YAHOO.widget.TabView('tabview_container');
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

	var myPanel1 = new YAHOO.widget.Panel("new", {
		width :"750px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel2 = new YAHOO.widget.Panel("new2", {
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"shadow",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel3 = new YAHOO.widget.Panel("new3", {
		width :"300px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel4 = new YAHOO.widget.Panel("new3", {
		width :"450px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel5 = new YAHOO.widget.Panel("new5", {
		width :"480px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	myPanel1.beforeHideEvent.subscribe(YAHOO.amptab.handleClose);
	//myPanel5.beforeHideEvent.subscribe(YAHOO.amptab.handleCloseAbout);

	function initScripts() {
		//alert('initScripts');

		var msg = '\n<digi:trn jsFriendly="true" key="rep:filter:filters">Filters</digi:trn>';
		myPanel1.setHeader(msg);
		myPanel1.setBody("<p>initScripts</p>");
		myPanel1.render(document.body);
	}
	function setSelectedValues(txtSelectedValuesObj,selname){
		var selectedArray = new Array();
		    $("input[name='"+selname+"']:checked").each(function(index) {
		 	   selectedArray[index] = $(this).val();
		 });
		 if(selectedArray.length>0)
		 txtSelectedValuesObj.value = selectedArray;
		
	}
	
	
	function resetGPIFilters() {
		 $("input[name='selectedDonors']:checked").attr('checked', false);
		 $("input[name='selectedDonorTypes']:checked").attr('checked', false);
		 $("input[name='selectedDonorGroups']:checked").attr('checked', false);
		 $("input[name='selectedStatuses']:checked").attr('checked', false);
		 $("input[name='selectedFinancingIstruments']:checked").attr('checked', false);
		 $("input[name='selectedSectors']:checked").attr('checked', false);
		  var filterForm = document.getElementsByName("gpiForm")[0]; 
		  var selectedArray = new Array();
 			filterForm.selectedDonors.value= selectedArray;
 			filterForm.selectedDonorTypes.value= selectedArray;
 			filterForm.selectedDonorGroups.value= selectedArray;
 			filterForm.selectedStatuses.value= selectedArray;
 			filterForm.selectedSectors.value= selectedArray;
 			filterForm.selectedFinancingIstruments.value= selectedArray;
 			filterForm.selectedStartYear.value = filterForm.defaultStartYear.value ;
 	        filterForm.selectedEndYear.value = filterForm.defaultEndYear.value;
 	        filterForm.selectedCalendar.value = filterForm.defaultCalendar.value;
 	        filterForm.selectedCurrency.value = filterForm.defaultCurrency.value;
			$('#selectedStartYear option[value='+filterForm.defaultStartYear.value+']').attr('selected', 'selected');
 			$('#selectedEndYear option[value='+filterForm.defaultEndYear.value+']').attr('selected', 'selected');
 			$('#selectedCalendar option[value='+filterForm.selectedCalendar.value+']').attr('selected', 'selected');
 			$('#selectedCurrency option[value='+filterForm.selectedCurrency.value+']').attr('selected', 'selected');
	}
	function showFilterDiv(divId,searchId){
		var divEl=document.getElementById(divId);
		getSearchManagerInstanceById(searchId).setDiv(divEl); 
		$("div[id^='filter_']").hide();
		$('#'+divId).show();
	}
	

	function submitFilters() {
		//alert('submitfilters');
        var filterForm = document.getElementsByName("gpiForm")[0];
        filterForm.selectedStartYear.value = document.getElementById("selectedStartYear").options[document.getElementById("selectedStartYear").selectedIndex].value;
        filterForm.selectedEndYear.value = document.getElementById("selectedEndYear").options[document.getElementById("selectedEndYear").selectedIndex].value;
        filterForm.selectedCalendar.value = document.getElementById("selectedCalendar").options[document.getElementById("selectedCalendar").selectedIndex].value;
        filterForm.selectedCurrency.value = document.getElementById("selectedCurrency").options[document.getElementById("selectedCurrency").selectedIndex].value;
        
        //Donors
        var txtSelectedValuesObj = filterForm.selectedDonors;
        setSelectedValues(txtSelectedValuesObj,'selectedDonors');
            
      	//Donor Types
        var txtSelectedValuesObj = filterForm.selectedDonorTypes;
        setSelectedValues(txtSelectedValuesObj,'selectedDonorTypes');

        //groups
        var txtSelectedValuesObj = filterForm.selectedDonorGroups;
        setSelectedValues(txtSelectedValuesObj,'selectedDonorGroups');
      

        //status
        var txtSelectedValuesObj = filterForm.selectedStatuses;
        setSelectedValues(txtSelectedValuesObj,'selectedStatuses');
  

        //instruments
        var txtSelectedValuesObj = filterForm.selectedFinancingIstruments;
        setSelectedValues(txtSelectedValuesObj,'selectedFinancingIstruments');
 


        //sectors
        var txtSelectedValuesObj = filterForm.selectedSectors;
        setSelectedValues(txtSelectedValuesObj,'selectedSectors');

      

        //filterForm.selectedFinancingIstruments.value = document.getElementsByName("selectedFinancingIstruments")[0].value;
        filterForm.submit();
	}

	function change_donor() {
        var selObj = document.getElementById("selectedDonorGroups");
        if(selObj.options[0].selected) {
            for (i = 1; i < selObj.options.length; i++) {
                selObj.options[i].selected = false;
            }
        }
    }

	function showFilter() {
		//alert('showFilter');
		YAHOO.amptab.init();
		var element = document.getElementById("filterContainer");
		//var element = document.getElementById("myFilter");
		element.style.display = "inline";
		//alert(element.innerHTML);
		myPanel1.setBody(element);
		myPanel1.center();
		myPanel1.show();
	}

	function checkRangeValues() {
		var actualFrom = document.aimReportsFilterPickerForm2.renderStartYear.value;
		var actualTo = document.aimReportsFilterPickerForm2.renderEndYear.value;
		var msg = '\n<digi:trn jsFriendly="true" key="rep:filter:wrongSelecteRange">Default Start Year must be lesser than Default End Year</digi:trn>';
		if (actualFrom > actualTo) {
			alert(msg);
			return false;
		}
		return true;
	}

	function hideFilter() {
		myPanel1.hide();
	}

	function resetFormat() {
		form3.action = form3.action + '&resetFormat=true';
		alert(form3.action);
		form3.submit();
	}

	addLoadEvent(initScripts);
	var msg0 = '<digi:trn jsFriendly="true">Loading...</digi:trn>';
	var msg1 = '<digi:trn key="rep:pop:freezeReportHeading" jsFriendly="true">Freeze Report Heading</digi:trn>';
	var msg2 = '<digi:trn key="rep:pop:unFreezeReportHeading" jsFriendly="true">Unfreeze Report Heading</digi:trn>';
	var msg3 = '<digi:trn key="rep:pop:freezingReportHeading" jsFriendly="true">Freezing Report Heading</digi:trn>';

	var scrolling = readCookie('report_scrolling');
	scrolling = (scrolling == null) ? false : (scrolling == "true") ? true
			: false;

	//-----------------------
	function showScroll() {
		var wait = new YAHOO.widget.Panel("wait", {
			width :"240px",
			fixedcenter :true,
			close :false,
			draggable :false,
			zindex :99,
			modal :true,
			visible :false,
			underlay :"shadow"
		});

		wait.setHeader(msg0);
		wait.setBody("<div align='center'>" + msg3 + "</div>");
		wait.render(document.body);
		wait.show();
		var winH;

		if (navigator.appName.indexOf("Microsoft") != -1) {
			winH = document.body.offsetHeight;
		} else {
			winH = window.innerHeight;
		}
		var call = function() {
			var reporTable = new scrollableTable("reportTable", winH - 320);
			reporTable.debug = false;
			reporTable.maxRowDepth = 1;
			reporTable.scroll();
			wait.hide();
		}
		window.setTimeout(call, 200);
	}

	function openPrinter() {
		window.open('about:blank','Popup_Window','width=800,height=600,Resizable=yes,Menubar=yes,Scrollbars=yes');
        document.gpiForm.target = 'Popup_Window';
        document.gpiForm.printPreview.value = true;
        document.gpiForm.submit();
    }

	function exportPDFs() {
		if(document.getElementById("reportIsEmpty") != null) {
			alert("<digi:trn jsFriendly='true'>No survey data found.</digi:trn>");
		} else {
	        document.getElementsByName('exportPDF')[0].value = true;
	        document.getElementsByName('gpiForm')[0].submit();
		}
    }

	function exportXLSs() {
		if(document.getElementById("reportIsEmpty") != null) {
			alert("<digi:trn jsFriendly='true'>No survey data found.</digi:trn>");
		} else {
			document.getElementsByName('exportXLS')[0].value = true;
	        document.getElementsByName('gpiForm')[0].submit();
		}
    }

    function resetExport() {
    	document.gpiForm.exportPDF.value = false;
    	document.gpiForm.exportXLS.value = false;
    	document.gpiForm.printPreview.value = false;
    	document.gpiForm.target = '_self';
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