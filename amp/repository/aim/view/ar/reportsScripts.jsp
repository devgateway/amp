<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>

<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/scrollableTable.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/scrollableTableReports.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

	<!-- Jquery Base Library -->
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/jquery/jquery.min.js" type="text/javascript"></script>
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/jquery/jquery-ui.min.js" type="text/javascript"></script>
<script src="/TEMPLATE/ampTemplate/script/common/lib/jquery-ui-i18n.min.js" type="text/javascript"></script>
<c:set var="maxFractionDigits"><%= org.digijava.module.aim.helper.FormatHelper.getDefaultFormat().getMaximumFractionDigits() %></c:set>
<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<!-- script for tree-like view (drilldown reports) -->

<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<!-- dynamic drive ajax tabs -->
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>


<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>

<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicTooltip.js"/>"></script>

<link rel="stylesheet" href="/repository/aim/view/css/css_dhtmlsuite/modal-message.css"/>

<script type="text/javascript">
messageObj = new DHTMLSuite.modalMessage();	// We only create one object of this class
messageObj.setWaitMessage('Loading...');
messageObj.setShadowOffset(5);	// Large shadow

DHTMLSuite.commonObj.setCssCacheStatus(false);

function displayMessage(url)
{
	messageObj.setSource(url);
	messageObj.setCssClassMessageBox(false);
	messageObj.setSize(400,200);
	messageObj.setShadowDivVisible(true);	// Enable shadow for these boxes
	messageObj.display();
}

function displayStaticMessage(messageContent,cssClass,width,height)
{
	messageObj.setHtmlContent(messageContent);
	messageObj.setSize(width,height);
	messageObj.setCssClassMessageBox(cssClass);
	messageObj.setSource(false);	// no html source since we want to use a static message here.
	messageObj.setShadowDivVisible(true);	// Disable shadow for these boxes
	messageObj.display();


}

function closeMessage()
{
	messageObj.close();
}


</script>


<!-- virtual pagination -->

<script type="text/javascript" src="<digi:file src="module/aim/scripts/virtualpaginate.js"/>">

/***********************************************
* Virtual Pagination script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

</script>

<style type="text/css">

/*Sample CSS used for the Virtual Pagination Demos. Modify/ remove as desired*/

.virtualpage, .virtualpage2, .virtualpage3{
/*hide the broken up pieces of contents until script is called. Remove if desired*/
display: none;
}

.paginationstyle{ /*Style for demo pagination divs*/
width: 250px;
text-align: center;
padding: 2px 0;
margin: 10px 0;
}

.paginationstyle select{ /*Style for demo pagination divs' select menu*/
border: 1px solid navy;
margin: 0 15px;
}

.paginationstyle a{ /*Pagination links style*/
padding: 0 5px;
text-decoration: none;
border: 1px solid black;
color: navy;
background-color: white;
}

.paginationstyle a:hover, .paginationstyle a.selected{
color: #000;
background-color: #FEE496;
}

.paginationstyle a.imglinks{ /*Pagination Image links style (class="imglinks") */
border: 0;
padding: 0;
}

.paginationstyle a.imglinks img{
vertical-align: bottom;
border: 0;
}

.paginationstyle a.imglinks a:hover{
background: none;
}

.paginationstyle .flatview a:hover, .paginationstyle .flatview a.selected{ /*Pagination div "flatview" links style*/
color: #000;
background-color: yellow;css
}

</style>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>?version=fantastic_15" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/saveReports.js'/>" ></script>

<!-- END - For DHTML Tab View of Filters -->


<script type="text/javascript">
SaveReportEngine.connectionErrorMessage = 
	"<digi:trn key='aim:reportwizard:connectionProblems' jsFriendly='true'>Apparently there are some connection problems. Please try again in a few moments.</digi:trn>";
SaveReportEngine.savingMessage =
	"<digi:trn key='aim:reportwizard:savingData' jsFriendly='true'>Saving data. Please wait.</digi:trn>";
SaveReportEngine.doneCopyMessage =
	"<digi:trn key='aim:reports:doneCopy' jsFriendly='true'>A copy was saved containing the currently selected filters. </digi:trn>";
SaveReportEngine.checkReportsMessage =
	"<digi:trn key='aim:reports:checkReports' jsFriendly='true'>Check Reports in the main menu. </digi:trn>";
SaveReportEngine.checkTabsMessage =
	"<digi:trn key='aim:reports:checkTabs' jsFriendly='true'>Check the Tab Manager in the main menu. </digi:trn>";
SaveReportEngine.tabSavedMessage =
	"<digi:trn key='aim:reports:tabSaved' jsFriendly='true'>Tab and filters have been saved. </digi:trn>";
SaveReportEngine.reportSavedMessage =
	"<digi:trn key='aim:reports:reportSaved' jsFriendly='true'>Report and filters have been saved.</digi:trn>";
saveReportEngine	= null;	
</script>
<script type="text/javascript">
		dateFilterErrorMsg = "<digi:trn jsFriendly='true'>'From' date must be on or before 'To' date</digi:trn>";
		var currentReportId	= -1;
		<% 
		//We override currentReportId only if the user is logged in
		if (session.getAttribute("currentMember") != null) {%>
			currentReportId = <%=ReportContextData.contextIdExists() ? ReportContextData.getCurrentReportContextId(request, true) : "-1" %>
		<%}%>
		YAHOO.namespace("YAHOO.amptab");
		YAHOO.amptab.init = function() {
		    var tabView = new YAHOO.widget.TabView('tabview_container');
		};

		YAHOO.amptab.handleCloseAbout = function() {
			if(navigator.appName == 'Microsoft Internet Explorer'){
				//window.location.reload();
				//history.go(-1);
			}
		}
		
		YAHOO.amptab.handleClose = function() {
			var filter			= document.getElementById('myFilter');
			filter.innerHTML 	= '';
			var wrapper			= document.getElementById('myFilterWrapper');
			if (filter.parent != null)
					filter.parent.removeChild(filter);
			wrapper.appendChild(filter);
		};

		YAHOO.amptab.handleCloseShowFormat = function() {
			var wrapper			= document.getElementById('myFilterWrapper');
			var filter			= document.getElementById('customFormat');
			if (filter.parent != null)
					filter.parent.removeChild(filter);
			wrapper.appendChild(filter);
		};
	
		var myPanel1 = new YAHOO.widget.Panel("new", {
			width:"750px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
		    draggable:true} );
		
		var myPanel2 = new YAHOO.widget.Panel("new2", {
			width:"400px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"shadow",
		    close:true,
		    visible:false,
		    modal:true,
		    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
		    draggable:true} );

		var myPanel3 = new YAHOO.widget.Panel("new3", {
			width:"300px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
		    draggable:true} );
		    
		    var myPanel4 = new YAHOO.widget.Panel("new4", {
			width:"480px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
		    draggable:true} );

		    var myPanel5 = new YAHOO.widget.Panel("new5", {
				width:"480px",
			    fixedcenter: true,
			    constraintoviewport: true,
			    underlay:"none",
			    close:true,
			    visible:false,
			    modal:true,
			    effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
			    draggable:true} );
	
	myPanel1.hideEvent.subscribe(YAHOO.amptab.handleClose);
	myPanel5.beforeHideEvent.subscribe(YAHOO.amptab.handleCloseAbout);
	myPanel4.beforeHideEvent.subscribe(YAHOO.amptab.handleCloseShowFormat);
		    
	function initScripts() {
	
	    var msg='\n<digi:trn key="rep:filter:filters" jsFriendly="true">Filters</digi:trn>';
		myPanel1.setHeader(msg);
		myPanel1.setBody("");
		myPanel1.render(document.body);
		
		var msgP2='\n<digi:trn key="rep:filter:selectsorter" jsFriendly="true">Please select hierarchy sorter criteria</digi:trn>';

		myPanel2.setHeader(msgP2);
		myPanel2.setBody("");
		myPanel2.render(document.body);
		myPanel2EmptyBody	= true;
		
		var msgP3='\n<digi:trn key="rep:filter:selectRange" jsFriendly="true">Please select range</digi:trn>';
		myPanel3.setHeader(msgP3);
		myPanel3.setBody("");
		myPanel3.render(document.body);
		
		//alert('param.queryEngine: ' + '${param.queryEngine}');
		<c:choose>
		<c:when test="${param.is_a_tab =='true' }">
			var msgP4='\n<digi:trn jsFriendly="true">Please select tab settings</digi:trn>';
		</c:when>
		<c:otherwise>
			var msgP4='\n<digi:trn jsFriendly="true">Please select report settings</digi:trn>';
		</c:otherwise>
		</c:choose>

		myPanel4.setHeader(msgP4);
		myPanel4.setBody("");
		myPanel4.render(document.body);

		var msgP5='\n<digi:trn key="aim:aboutamp" jsFriendly="true">About AMP</digi:trn>';
		myPanel5.setHeader(msgP5);
		myPanel5.setBody("");
		myPanel5.render(document.body);				
	}
	
	function submitFilters(reportContextId) {
		//alert("SUBMITTING FILTERS");
		//debugger;
		if(document.getElementById("workspace_only")!=null)
			document.getElementById("workspaceOnly").value = document.getElementById("workspace_only").checked;
		var filterForm		= document.getElementsByName("aimReportsFilterPickerForm")[0];
		
		if (validateDynamicDateFilters() && validateDateFilters()){
			filterForm.action	= "/aim/reportsFilterPicker.do?apply=true&reportContextId=" + reportContextId;
			filterForm.submit();
		};
	}
	
	function submitSettings(reportContextId) {
		//This is needed for unfreezing the report. applyFormat parameter needs to be set so that the function
		// ReportsFilterPickerForm.reset doesn't reset the currently applied filters
		var filterForm		= document.getElementsByName("aimReportsFilterPickerForm3")[0];
		filterForm.action	= "/aim/reportsFilterPicker.do?applyFormat=true&reportContextId=" + reportContextId;
		filterForm.submit();
	}
	
	function showFilter(reportContextId) {
		document.getElementById("myFilter").innerHTML = '<div align="center" style="font-size: 11px;margin-top:190px;"><img src="/TEMPLATE/ampTemplate/img_2/ajax-loader.gif"/></div>';
		
		var element = document.getElementById("myFilter");
		element.style.display   = "block";
 	 	element.style.height    = "500px";

 	 	myPanel1.setBody(element);
		
 	 	myPanel1.cfg.setProperty("height", "520px" );
 	 	myPanel1.cfg.setProperty("width", "870px" );
		myPanel1.center();
		myPanel1.show();
		
		var timestamp = new Date().getTime();
		
		YAHOO.util.Connect.asyncRequest("GET", "/aim/reportsFilterPicker.do?timestamp=" + timestamp + "&reportContextId=" + reportContextId + "&overwriteBackUrl=" + window.location.pathname , {
			success: function(o) {
				document.getElementById("myFilter").innerHTML	= o.responseText;
				YAHOO.amptab.afterFiltersLoad();
			},
			failure:function (o) {
				document.getElementById("myFilter").innerHTML	= "<digi:trn jsFriendly='true'>There was a problem loading the filters</digi:trn>";
			}
		});
		YAHOO.amptab.init();

		

		<field:display name="Project Category" feature="Identification">
			try {
				addTitleSelect("selectedProjectCategory");
			}
			catch(e)
			{
//				alert(e);
			}
		</field:display>
		
	}

	function addTitleSelect(selectName){
		var categoryOptions = document.getElementsByName(selectName);
		if(categoryOptions[0])
		{
			for(idx = 0; idx < categoryOptions[0].options.length;idx++)
			{
				categoryOptions[0].options[idx].title = categoryOptions[0].options[idx].text;
			}
		}
	}

	function showAbout() {
		YAHOO.amptab.init();
		var element = document.getElementById("customAbout");
		element.style.display = "inline";
		myPanel5.setBody(element);
		myPanel5.center();
		myPanel5.show();
	}
	
	function checkRangeValues(){
	    var actualFrom = document.aimReportsFilterPickerForm2.renderStartYear.value;
        var actualTo = document.aimReportsFilterPickerForm2.renderEndYear.value;
        var msg='\n<digi:trn key="rep:filter:wrongSelecteRange" jsFriendly="true">Default Start Year must be lesser than Default End Year</digi:trn>';
		if(actualFrom>actualTo){
		    alert(msg);
			return false;
		}
		return true;
	}
	
	function showFormat(){
		//YAHOO.amptab.init();
		//var maxFractionDigits=${maxFractionDigits};
		//ResetCustom(maxFractionDigits);
		//initFormatPopup();
		var element = document.getElementById("customFormat");
		element.style.display = "inline";
		myPanel4.setBody(element);
		myPanel4.center();
		myPanel4.show();
	}
	
	function changeRange(){
		var cant = document.aimReportsFilterPickerForm2.countYear.value;
		var actualFrom = document.aimReportsFilterPickerForm2.fromYear.value;
		var actualTo = document.aimReportsFilterPickerForm2.toYear.value;
		var initialYear = document.aimReportsFilterPickerForm2.countYearFrom.value;
		document.aimReportsFilterPickerForm2.fromYear.length=0;
      	document.aimReportsFilterPickerForm2.toYear.length=0;
      	var masterFrom=document.aimReportsFilterPickerForm2.fromYear;
      	var masterTo=document.aimReportsFilterPickerForm2.toYear;
      	masterFrom.options[0]=new Option("All", "-1", false, true);
		for (var i = 1; i <= cant; i++){
		    var year  = parseInt(initialYear)+ i;
		    if(year == actualFrom){
				masterFrom.options[i]=new Option(year, year, false, true);
			}
			else{
				masterFrom.options[i]=new Option(year, year, false, false);
			};
		}
		masterTo.options[0]=new Option("All", "-1", false, true);
		for (var i = 1; i <= cant; i++){
			var year  = parseInt(initialYear)+ i;
		    if(year == actualTo){
				masterTo.options[i]=new Option(year, year, false, true);
			}
			else{
				masterTo.options[i]=new Option(year, year, false, false);
			};
		}
	}
	function hideFilter() {
		myPanel1.hide();
	}
	function showSorter() {
		var element2 = document.getElementById("mySorter");
		if ( element2 != null ) {
			element2.parentNode.removeChild(element2);
			element2.style.display 	= "inline";
			element2.id		= null;
			myPanel2.setBody(element2);
			myPanel2EmptyBody		= false;
		}
		myPanel2.show();
	}
	function hideSorter() {
		myPanel2.hide();
	}
	function resetSorter(button) {
		var form1		= button.form;
		for (var i=0; i<form1.elements.length; i++) {
				var selEl	= form1.elements[i];
				if ( selEl.nodeName.toLowerCase() == "select" ) {
					selEl.selectedIndex	= 0;
					selEl.options[0].selected = true;
				}
		}
		return false;
	}
	
	function showRange(){
		YAHOO.amptab.init();
		var element = document.getElementById("myRange");
		element.style.display = "inline";
		
		myPanel3.setBody(element);
		myPanel3.center();
		myPanel3.show();	
	}
	function hideRange() {
		myPanel3.hide();
	}
	
	function hideFilter() {
		myPanel1.hide();
	}
	function checkProjectId(x){
		var s_len = x.value.length;
		var s_charcode = 0;
	    for (var s_i=0;s_i<s_len;s_i++)
		    {
		     s_charcode = x.value.charCodeAt(s_i);
		     if(!((s_charcode>=48 && s_charcode<=57)))
		      {
		         alert("Only Numeric Values Allowed");
		         x.value='';
		         x.focus();
        		return false;
      		  }
    	}
    	return true;
	}
	

function resetFormat(){
	document.aimReportsFilterPickerForm3.action=document.aimReportsFilterPickerForm3.action+'&resetFormat=true';
	alert(document.aimReportsFilterPickerForm3.action);
	document.aimReportsFilterPickerForm3.submit();
}

function ResetCustom(maxFractionDigits) {
	aimReportsFilterPickerForm3.customDecimalSymbol.value = ",";
	aimReportsFilterPickerForm3.customDecimalSymbolTxt.value = "";
	aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = "true";
	aimReportsFilterPickerForm3.customDecimalPlaces.value = maxFractionDigits;
	aimReportsFilterPickerForm3.customDecimalPlacesTxt.value = "";
	aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = "true"
	aimReportsFilterPickerForm3.customUseGrouping.checked = "true";
	aimReportsFilterPickerForm3.customGroupCharacter.value = ".";
	aimReportsFilterPickerForm3.customGroupCharacterTxt.value = "";
	aimReportsFilterPickerForm3.customGroupSize.value = 3;
	//alert(aimReportsFilterPickerForm3.amountinthousands.checked);
	aimReportsFilterPickerForm3.customAmountinThousands.options.selectedIndex = 0;
	aimReportsFilterPickerForm3.calendar.value =aimReportsFilterPickerForm3.initialCal.value;
	initFormatPopup();
	if(document.aimReportsFilterPickerForm3.renderStartYear){
		document.aimReportsFilterPickerForm3.renderStartYear.value=-1;
	}
	if(document.aimReportsFilterPickerForm3.renderEndYear){
		document.aimReportsFilterPickerForm3.renderEndYear.value=-1;
	}
	if (aimReportsFilterPickerForm3.currency)
		aimReportsFilterPickerForm3.currency.value=aimReportsFilterPickerForm3.defaultCurrency.value;

    if (aimReportsFilterPickerForm3.calendar)
        aimReportsFilterPickerForm3.calendar.value=aimReportsFilterPickerForm3.defaultCalendar.value;

}

function initFormatPopup(){
    var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;

    if (decimalSymbol.toLowerCase()=="custom"){
        document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=false;

    }else{
        document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value="";
        document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=true;
    }

    var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;

    if (customDecimalPlaces.toLowerCase()=="-2"){
        document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled=false;
    }else{
        document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value="";
        document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled=true;
    }


    var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;

    if (!customUseGrouping){
        document.aimReportsFilterPickerForm3.customGroupCharacter.disabled=true;
    }else{
        document.aimReportsFilterPickerForm3.customGroupCharacter.disabled=false;
        }
    var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
    document.aimReportsFilterPickerForm3.customGroupSize.disabled=!customUseGrouping;
    document.aimReportsFilterPickerForm3.customGroupCharacterTxt.disabled=((!customUseGrouping) || ("custom"!=customGroupCharacter.toLowerCase()));

    changeFormat();
}

function changeFormat(){
	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol=("custom"==decimalSymbol.toLowerCase())?document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value:decimalSymbol;
	
	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces=("-2"==customDecimalPlaces.toLowerCase())?document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value:customDecimalPlaces;
	
	var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;

	
	var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter=("custom"==customGroupCharacter.toLowerCase())?document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value:customGroupCharacter;
	
	
	var customGroupSize=document.aimReportsFilterPickerForm3.customGroupSize.value;
	
	var amountDivisor = 1.0;
	
	var selectedValue = document.aimReportsFilterPickerForm3.amountinthousands.value;
	if (selectedValue == '1')
		amountDivisor = 1000.0;
	
	if (selectedValue == '2')
		amountDivisor = 1000.0 * 1000.0;
	
	
	var num=Number(123456789.928 / amountDivisor);

	//debugger;
	var format=new Format(decimalSymbol,customDecimalPlaces,customUseGrouping,customGroupCharacter,customGroupSize);
	document.getElementById("number").innerHTML="<B>"+num.format(format)+"</B>";
	//alert(num.format(format));
	return true;
}

function validateFormat(){

	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol=("custom"==decimalSymbol.toLowerCase())?document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value:decimalSymbol;
	
	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces=("-2"==customDecimalPlaces.toLowerCase())?document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value:customDecimalPlaces;
	
	var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;
	
	var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter=("custom"==customGroupCharacter.toLowerCase())?document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value:customGroupCharacter;
	
	var customGroupSize=document.aimReportsFilterPickerForm3.customGroupSize.value;
	
	if ((decimalSymbol==customGroupCharacter)&&(customUseGrouping)){
	        var msg='<digi:trn jsFriendly="true" key="rep:format:equalsSymbol">Decimal Symbol and group symbol must be diferents</digi:trn>';
			alert(msg);
			return false;
	}
	var validNumbers="0123456789";
	
	if (decimalSymbol=="" || customGroupCharacter==""){
		 var msg='<digi:trn jsFriendly="true" key="rep:format:badSymbolEmpty">Symbols can not be a empty, you can use the space character</digi:trn>';
		alert(msg);
		return false;
	}
	
	
	if ((validNumbers.indexOf(decimalSymbol)!=-1)||(validNumbers.indexOf(customGroupCharacter)!=-1)){
		     var msg='<digi:trn jsFriendly="true" key="rep:format:badSymbolNumber">Symbols can not be a number</digi:trn>';
			alert(msg);
			return false;
	}
	
	if ((customGroupSize < 1) && (document.aimReportsFilterPickerForm3.customUseGrouping.checked == true)) {
		  var msg='<digi:trn jsFriendly="true" key="rep:format:badGorupSize">The value should be greater than zero</digi:trn>';
			alert(msg);
			return false;
	}
	
	
	
	return true;
}


	window.onload=initScripts;


	var msg0="<digi:trn key="rep:pop:pleasewait..." jsFriendly='true'>Loading...</digi:trn>";
	
	var msg1="<digi:trn key="rep:pop:freezeReportHeading" jsFriendly='true'>Freeze Report Heading</digi:trn>";
	
	var msg2="<digi:trn key="rep:pop:unFreezeReportHeading" jsFriendly='true'>Unfreeze Report Heading</digi:trn>";
	
	var msg3="<digi:trn key="rep:pop:freezingReportHeading" jsFriendly='true'> Freezing Report Heading </digi:trn>";
	
	var msg4="<digi:trn key="rep:pop:freezingReportHeading" jsFriendly='true'> Unfreezing Report Heading </digi:trn>";



	function addOnloadEvent(fnc){
	  if ( typeof window.addEventListener != "undefined" )
	    window.addEventListener( "load", fnc, false );
	  else if ( typeof window.attachEvent != "undefined" ) {
	    window.attachEvent( "onload", fnc );
	  }
	  else {
	    if ( window.onload != null ) {
	      var oldOnload = window.onload;
	      window.onload = function ( e ) {
	        oldOnload( e );
	        window[fnc]();
	      };
	    }
	    else
	      window.onload = fnc;
	  }
	}

	
	var scrollingStr=readCookie('report_scrolling');
	var scrolling=(scrollingStr==null)?false:(scrollingStr==new Number(currentReportId).toString() )?true:false;
		
	// For some reason freezeLink.removeListener doesn't work correctly so we need an ugly hack
	var canMakeScroll	= false;
	function scrollCallback (reportContextId) {
		if ( canMakeScroll ) 
				makeScroll();
		else
				hiddeScroll(reportContextId);
	}
	//END	
	
	
	function reloadpage(){
		location.reload();
	}
	
	function sendCookieAndReload (reportContextId){
		showWaitPanel(msg3);
		createCookie('report_scrolling',currentReportId,1);
		submitScroll(reportContextId);
	}
	
	
	function makeScroll (){
		var freezeLink	= new YAHOO.util.Element( "frezzlink" );
		createCookie('report_scrolling',currentReportId,1);
		showScroll();
		//document.getElementById("frezzlink").setAttribute("onClick","hiddeScroll()");
		//document.getElementById("frezzlink").setAttribute("class","settingsLink");
		if ( freezeLink.hasClass("settingsLinkDisable") )
			freezeLink.removeClass("settingsLinkDisable");
		freezeLink.addClass( "settingsLink" );
		canMakeScroll	= false;
		//document.getElementById("frezzlink").innerHTML=msg2;
	}
	
	function hiddeScroll(reportContextId){
		showWaitPanel(msg4);
		eraseCookie('report_scrolling');
		submitScroll(reportContextId);
	}

	function submitScroll(reportContextId) {
		var filterForm		= document.getElementsByName("aimReportsFilterPickerForm3")[0];
		filterForm.action	= "/aim/reportsFilterPicker.do?apply=true&reportContextId=" + reportContextId;
		filterForm.submit();
	}
	
	function showWaitPanel(msgF){
 	 	var wait = new YAHOOAmp.widget.Panel("wait",   
	 	 	{ width:"240px",  
	 	 	fixedcenter:true,  
	 	 	close:false,  
	 	 	draggable:false,  
	 	 	zindex:99, 
	 	 	modal:true, 
	 	 	visible:false,
	 	 	underlay:"shadow"
	 	 	}  
 	 	); 
 	 	
		wait.setHeader(msg0); 
 	 	wait.setBody("<div align='center'>"+msgF+"</div>"); 
 	 	wait.render(document.body);
 	 	wait.show();
	}
	
	var enableLink=function(){
	if (document.getElementById("frezzlink")){
		var freezeLink	= new YAHOO.util.Element( "frezzlink" );
		var reportContextId = freezeLink.getAttribute('reportContextId');
		if (scrolling){
			//document.getElementById("frezzlink").setAttribute("onClick","hiddeScroll()");
			//document.getElementById("frezzlink").setAttribute("class","settingsLink");
			if ( freezeLink.hasClass("settingsLinkDisable") )
				freezeLink.removeClass("settingsLinkDisable");
			freezeLink.addClass( "settingsLink" );
			freezeLink.on("click", function() {scrollCallback(reportContextId);});
			freezeLink.setStyle("cursor", "pointer");
			canMakeScroll	= false;
			//document.getElementById("frezzlink").setAttribute("style","cursor: hand;");
			document.getElementById("frezzlink").innerHTML=msg2;
			showScroll();
		}else{
			//document.getElementById("frezzlink").setAttribute("onClick","makeScroll()");
			//document.getElementById("frezzlink").setAttribute("class","settingsLink");
			if ( freezeLink.hasClass("settingsLinkDisable") )
				freezeLink.removeClass("settingsLinkDisable");			
			freezeLink.addClass( "settingsLink" );
			freezeLink.on("click", function() {sendCookieAndReload(reportContextId);});
			freezeLink.setStyle("cursor", "pointer");
			canMakeScroll	= true;
			//document.getElementById("frezzlink").setAttribute("style","cursor: hand;");
			document.getElementById("frezzlink").innerHTML=msg1;
		}
	}
	}
		addOnloadEvent(enableLink);
		//-----------------------
		function showScroll(){
			var wait = new YAHOO.widget.Panel("wait",   
		        { width:"240px",  
		          fixedcenter:true,  
		          close:false,  
		          draggable:false,  
		          zindex:99, 
		          modal:true, 
		          visible:false,
		          underlay:"shadow"
		        }  
		    ); 

			wait.setHeader(msg0); 
			wait.setBody("<div align='center'>"+msg3+"</div>"); 
			wait.render(document.body);
			wait.show();
			var winH;
			
			if (navigator.appName.indexOf("Microsoft")!=-1) {
				winH = document.body.offsetHeight;
			}else{
				winH=window.innerHeight;
			}
			var call=function(){
				var reporTable=new scrollableTable("reportTable",winH - 320);
				reporTable.debug=false;
				reporTable.maxRowDepth=2;
				reporTable.scroll();
				wait.hide();		
			}
			
				window.setTimeout(call,200);
			}
		
	//----------------------------------------------------------------
	var isscrolling=false;
	  	function frezzreport(reportContextId){ 
	  		//debugger;
  	  		if (isscrolling==false){
  				if ( $('#frezzlinkreport').hasClass("settingsLinkDisable") )
  				$('#frezzlinkreport').removeClass("settingsLinkDisable");
  				$('#frezzlinkreport').addClass( "settingsLink" );
  				//$('#frezzlinkreport').bind("click", showScrollReport);
  				$('#frezzlinkreport').css("cursor", "pointer");
  				canMakeScroll	= false;
  				$('#frezzlinkreport').text(msg2);
  				isscrolling=true;
  				showScrollReport();
  			}else{
  				if ($('#frezzlinkreport').hasClass("settingsLinkDisable"))
  					$('#frezzlinkreport').removeClass("settingsLinkDisable");
  					//$('#frezzlinkreport').bind("click", submitSettings);
					$('#frezzlinkreport').addClass( "settingsLink" );
  					$('#frezzlinkreport').css("cursor", "pointer");
  					canMakeScroll= true;
  					$('#frezzlinkreport').text(msg1);
  					isscrolling=false;
  					loadingreport.show();
  					submitSettings(reportContextId);
  					
  				}
  	  	}

	//-----------------------
	function showScrollReport(){

		var wait = new YAHOO.widget.Panel("wait",   
	        { width:"240px",  
	          fixedcenter:true,  
	          close:false,  
	          draggable:false,  
	          zindex:99, 
	          modal:true, 
	          visible:false,
	          underlay:"shadow"
	        }  
	    ); 

		wait.setHeader(msg0); 
		wait.setBody("<div align='center'>"+msg3+"<br>"+'<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />'+"</div>"); 
		wait.render(document.body);
		wait.show();
		var winH;
		
		if (navigator.appName.indexOf("Microsoft")!=-1) {
			winH = document.body.offsetHeight;
		}else{
			winH=window.innerHeight;
		}
		var call=function(){
			var reporTable=new scrollableTableReports("reportTable", winH - 250);
			reporTable.debug=false;
			reporTable.maxRowDepth=2;
			reporTable.scroll();
			wait.hide();		
		}
		
			window.setTimeout(call,200);
		}
	
	


	function cleanformat() {
		aimReportsFilterPickerForm3.customDecimalSymbol.value = ",";
		aimReportsFilterPickerForm3.customDecimalSymbolTxt.value = "";
		aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = "true";
		aimReportsFilterPickerForm3.customDecimalPlaces.value = <%=org.digijava.module.aim.helper.FormatHelper.getDefaultFormat().getMaximumFractionDigits()%>;
		aimReportsFilterPickerForm3.customDecimalPlacesTxt.value = "";
		aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = "true";
		aimReportsFilterPickerForm3.customUseGrouping.checked = "true";
		aimReportsFilterPickerForm3.customGroupCharacter.value = ".";
		aimReportsFilterPickerForm3.customGroupCharacterTxt.value = "";
		aimReportsFilterPickerForm3.customGroupSize.value = 3;
		initFormatPopup();
	}

	
	var currentRMenu=null;
	function reportOptions(element,id){
		//cargar opciones
		if (currentRMenu!=null){
			currentRMenu.destroy();
			}
		var itemsUrl='/aim/reportMenuAction.do?action=getOptions&id='+id;

		var oMenu = new YAHOO.widget.Menu("reportMenu", { shadow:false, fixedcenter: false,srcElement:element,context:[element, "tl", "bl"] }); 
		$.get(itemsUrl,'',function(data){
		for (var i=0; i < data.childNodes[0].childNodes.length;i++){
			var node=data.childNodes[0].childNodes[i]
			var strUrl=node.getAttribute('url')+id;
			var text=data.childNodes[0].childNodes[i].childNodes[0].nodeValue
			var oItem = oMenu.addItem(new YAHOO.widget.MenuItem(text));

			oItem.cfg.setProperty("url",strUrl);
			oMenu.addItem(oItem);
		}
		oMenu.render(document.body);
		oMenu.show();
		currentRMenu=oMenu;
		}); 
	}
	
</script>
<style type="text/css">
.mask {
  -moz-opacity: 0.8;
  opacity:.80;
  filter: alpha(opacity=80);
  background-color:#2f2f2f;
}

#reportMenu {
	width:70px;
	background-color:#ffffff;
	border:1px solid #376091;
}

#reportMenu ul li a {
	width:70px;
	font-size:9px;
	padding:3px !important;
	text-transform:uppercase;
	text-decoration:none
}

#reportMenu li {
	width:70px;
	list-style-type: none;
    margin: 0;
    padding: 0;
}
#reportMenu ul {
	width:70px;
    margin: 0;
    padding: 0;
}

</style>
