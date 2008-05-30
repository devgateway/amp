<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>

<link rel="stylesheet" href="<digi:file src="module/aim/css/newamp.css"/>" />
<link rel="stylesheet" href="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.css"/>" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>



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
<link rel="stylesheet"
	href="<digi:file src="module/aim/scripts/ajaxtabs.css"/>" />


<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicTooltip.js"/>"></script>

<link rel="stylesheet"
	href="<digi:file src="module/aim/view/css/css_dhtmlsuite/modal-message.css"/>" />

<script type="text/javascript">
messageObj = new DHTMLSuite.modalMessage();	// We only create one object of this class
messageObj.setWaitMessage('Loading message - please wait....');
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

<script type="text/javascript" src="virtualpaginate.js">

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
background-color: yellow;
}

</style>

<!-- New DTHML Filters -->
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>

<!-- For DHTML Tab View of Filters -->
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dom-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/element/element-beta.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/tab/tabview.js'/>" >.</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/tooltip/wz_tooltip.js'/>" > .</script>

<style type="text/css"> 
	#tabview_container .yui-nav{

	}
	#tabview_container .yui-nav li {
		margin-right:0;
 	}

	#tabview_container .yui-content { border-top-width: 2px; border-top-style: solid; border-top-color: #006699;}
	
	#tabview_container .yui-nav li a { 
			float:left;
			cursor:pointer; 
			text-decoration: none; 
			font-size: 8pt; 
			color:#fff;
			font-weight: bold;
			background-color: white;
			margin:0pt 0px 0pt 0pt;
			background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	}
	
	#tabview_container li.selected a{ 
			float:left;
			cursor:pointer; 
			text-decoration: none; 
			font-size: 8pt; 
			font-weight: bold;
			background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
			color: white;
	}
	#tabview_container li a div{ 
			background:url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) no-repeat scroll right top;
			padding: 6px 10px 6px 10px;
	}
		
	#tabview_container li.selected a div{ 
			background:url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) no-repeat scroll right top;
			padding: 6px 10px 6px 10px;
	}
	#tabview_container .yui-nav li a:hover {
	    background: #455786 url(/TEMPLATE/ampTemplate/images/tableftcornerhover.gif) left top no-repeat;  
	}
	
	#tabview_container .yui-nav li a:hover div {
	    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
	}
	#tabview_container .yui-content {
		border-top:1px solid #000;
	}
	</style>

<!-- END - For DHTML Tab View of Filters -->

<script type="text/javascript">
		YAHOO.namespace("YAHOO.amptab");
		YAHOO.amptab.init = function() {
		    		var tabView = new YAHOO.widget.TabView('tabview_container');
		};
		YAHOO.amptab.handleClose = function() {
			var wrapper			= document.getElementById('myFilterWrapper');
			var filter			= document.getElementById('myFilter');
			if (filter.parent != null)
					filter.parent.removeChild(filter);
			wrapper.appendChild(filter);
		};
	
		var myPanel1 = new YAHOO.widget.Panel("new", {
			width:"700px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true} );
		var myPanel2 = new YAHOO.widget.Panel("new2", {
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"shadow",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true} );

		var myPanel3 = new YAHOO.widget.Panel("new3", {
			width:"300px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true} );
		    
		    var myPanel4 = new YAHOO.widget.Panel("new3", {
			width:"450px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true}
		     );
	
	myPanel1.beforeHideEvent.subscribe(YAHOO.amptab.handleClose);
		    
	function initScripts() {
	    var msg='\n<digi:trn key="rep:filter:selectFilter">Select filters</digi:trn>';
		myPanel1.setHeader(msg);
		myPanel1.setBody("");
		myPanel1.render(document.body);
		
		var msgP2='\n<digi:trn key="rep:filter:selectsorter">Please select hierarchy sorter criteria</digi:trn>';
;
		myPanel2.setHeader(msgP2);
		myPanel2.setBody("");
		myPanel2.render(document.body);
		
		var msgP3='\n<digi:trn key="rep:filter:selectRange">Please select range</digi:trn>';
		myPanel3.setHeader(msgP3);
		myPanel3.setBody("");
		myPanel3.render(document.body);
		
		var msgP4='\n<digi:trn key="rep:filter:SetFormat">Please select format</digi:trn>';
		myPanel4.setHeader(msgP4);
		myPanel4.setBody("");
		myPanel4.render(document.body);
				
				
	}
	
	function showFilter() {
		YAHOO.amptab.init();
		var element = document.getElementById("myFilter");
		element.style.display = "inline";
		
		myPanel1.setBody(element);
		myPanel1.center();
		myPanel1.show();
		
		initCalendar();
		
		
	}
	function checkRangeValues(){
	    var actualFrom = document.aimReportsFilterPickerForm2.renderStartYear.value;
        var actualTo = document.aimReportsFilterPickerForm2.renderEndYear.value;
        var msg='\n<digi:trn key="rep:filter:wrongSelecteRange">Default Start Year must be lesser than Default End Year</digi:trn>';
		if(actualFrom>actualTo){
		    alert(msg);
			return false;
		}
		return true;
	}
	
	function showFormat(){
		initFormatPopup();
		YAHOO.amptab.init();
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
		for (i=1; i<=cant; i++){
		    var year  = parseInt(initialYear)+ i;
		    if(year == actualFrom){
				masterFrom.options[i]=new Option(year, year, false, true);
			}
			else{
				masterFrom.options[i]=new Option(year, year, false, false);
			}	
		}
		masterTo.options[0]=new Option("All", "-1", false, true);
		for (i=1; i<=cant; i++){
			var year  = parseInt(initialYear)+ i;
		    if(year == actualTo){
				masterTo.options[i]=new Option(year, year, false, true);
			}
			else{
				masterTo.options[i]=new Option(year, year, false, false);
			}	
		}
	}
	function hideFilter() {
		myPanel1.hide();
	}
	function showSorter() {
		var element2 = document.getElementById("mySorter");
		element2.style.display = "inline";
		myPanel2.setBody(element2);
		myPanel2.show();
	}
	function hideSorter() {
		myPanel2.hide();
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
	
	function resetFilter(){
		if (aimReportsFilterPickerForm.text)
			aimReportsFilterPickerForm.text.value="";

		if (aimReportsFilterPickerForm.indexString)
			aimReportsFilterPickerForm.indexString.value="";
			
		if (aimReportsFilterPickerForm.fromDate)
			aimReportsFilterPickerForm.fromDate.value="";
		
		if (aimReportsFilterPickerForm.toDate)
			aimReportsFilterPickerForm.toDate.value="";
			
		if (aimReportsFilterPickerForm.currency)
			aimReportsFilterPickerForm.currency.value=aimReportsFilterPickerForm.defaultCurrency.value;
		
		if (aimReportsFilterPickerForm.fromYear)		
			aimReportsFilterPickerForm.fromYear.selectedIndex=0;
		
		if (aimReportsFilterPickerForm.toYear)
			aimReportsFilterPickerForm.toYear.selectedIndex=0
			
		if (aimReportsFilterPickerForm.fromMonth)
			aimReportsFilterPickerForm.fromMonth.selectedIndex=0;
			
		if (aimReportsFilterPickerForm.toMonth)
			aimReportsFilterPickerForm.toMonth.selectedIndex=0;
		
		if (aimReportsFilterPickerForm.selectedSectors)
			aimReportsFilterPickerForm.selectedSectors.selectedIndex=-1;
		if (aimReportsFilterPickerForm.selectedSecondarySectors)
			aimReportsFilterPickerForm.selectedSecondarySectors.selectedIndex=-1;
                    
          
          if (aimReportsFilterPickerForm.selectedNatPlanObj)
              aimReportsFilterPickerForm.selectedNatPlanObj.selectedIndex=-1;
          if (aimReportsFilterPickerForm.selectedPrimaryPrograms)
              aimReportsFilterPickerForm.selectedPrimaryPrograms.selectedIndex=-1;
          if (aimReportsFilterPickerForm.selectedSecondaryPrograms)
              aimReportsFilterPickerForm.selectedSecondaryPrograms.selectedIndex=-1;
			
		if (aimReportsFilterPickerForm.selectedBudget)
			aimReportsFilterPickerForm.selectedBudget.checked=false;
		
		/*if (aimReportsFilterPickerForm.selectedDonors)
			aimReportsFilterPickerForm.selectedDonors.selectedIndex=-1; */
		
		if (aimReportsFilterPickerForm.selectedRisks)
			aimReportsFilterPickerForm.selectedRisks.selectedIndex=-1;
			
		if (aimReportsFilterPickerForm.regionSelected)
			aimReportsFilterPickerForm.regionSelected.selectedIndex=0;
		
		if (aimReportsFilterPickerForm.lineMinRank)
			aimReportsFilterPickerForm.lineMinRank.selectedIndex=0;

		if (aimReportsFilterPickerForm.planMinRank)
			aimReportsFilterPickerForm.planMinRank.selectedIndex=0;
		
		if (aimReportsFilterPickerForm.selectedStatuses)
			aimReportsFilterPickerForm.selectedStatuses.selectedIndex=-1;
		
		if (aimReportsFilterPickerForm.selectedFinancingInstruments)
			aimReportsFilterPickerForm.selectedFinancingInstruments.selectedIndex=-1;
		if (aimReportsFilterPickerForm.selectedTypeOfAssistance)
			aimReportsFilterPickerForm.selectedTypeOfAssistance.selectedIndex=-1;
			
		if (aimReportsFilterPickerForm.selectedDonorGroups)
			aimReportsFilterPickerForm.selectedDonorGroups.selectedIndex=-1;
			
		if (aimReportsFilterPickerForm.selectedDonorTypes)
			aimReportsFilterPickerForm.selectedDonorTypes.selectedIndex=-1;
			
		if (aimReportsFilterPickerForm.selectedBeneficiaryAgency)
			aimReportsFilterPickerForm.selectedBeneficiaryAgency.selectedIndex=-1;
		if (aimReportsFilterPickerForm.selectedExecutingAgency)
			aimReportsFilterPickerForm.selectedExecutingAgency.selectedIndex=-1;
		if (aimReportsFilterPickerForm.selectedImplementingAgency)
			aimReportsFilterPickerForm.selectedImplementingAgency.selectedIndex=-1;
		
		if (aimReportsFilterPickerForm.jointCriteria){
			aimReportsFilterPickerForm.jointCriteria[0].checked=false;
			aimReportsFilterPickerForm.jointCriteria[1].checked=false;
		}

		if (aimReportsFilterPickerForm.governmentApprovalProcedures){
			aimReportsFilterPickerForm.governmentApprovalProcedures[0].checked=false;
			aimReportsFilterPickerForm.governmentApprovalProcedures[1].checked=false;
		}
	}
	


function resetFormat(){
	document.aimReportsFilterPickerForm3.action=document.aimReportsFilterPickerForm3.action+'&resetFormat=true';
	alert(document.aimReportsFilterPickerForm3.action);
	document.aimReportsFilterPickerForm3.submit();
}


function initFormatPopup(){
		
		
		var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		
		if (decimalSymbol=="CUSTOM"){
			document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=false;
		
		}else{
			document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value="";
			document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled=true;
		}
	
		var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		
		if (customDecimalPlaces=="CUSTOM"){
			document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled=false;
		}else{
			document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value="";
			document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled=true;
		}

	
		var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;
	
		document.aimReportsFilterPickerForm3.customGroupCharacter.disabled=!customUseGrouping;
		var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		document.aimReportsFilterPickerForm3.customGroupSize.disabled=!customUseGrouping;
		document.aimReportsFilterPickerForm3.customGroupCharacterTxt.disabled=((!customUseGrouping) || ("CUSTOM"!=customGroupCharacter));
	
		changeFormat();
}

function changeFormat(){
	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol=("CUSTOM"==decimalSymbol)?document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value:decimalSymbol;
	
	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces=("CUSTOM"==customDecimalPlaces)?document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value:customDecimalPlaces;
	
	var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;

	
	var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter=("CUSTOM"==customGroupCharacter)?document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value:customGroupCharacter;
	
	
	var customGroupSize=document.aimReportsFilterPickerForm3.customGroupSize.value;
	
	
	var num=Number(123456789.928);


	var format=new Format(decimalSymbol,customDecimalPlaces,customUseGrouping,customGroupCharacter,customGroupSize);
	document.getElementById("number").innerHTML="<B>"+num.format(format)+"</B>";
	//alert(num.format(format));
	return true;
}

function validateFormat(){

	var decimalSymbol=document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol=("CUSTOM"==decimalSymbol)?document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value:decimalSymbol;
	
	var customDecimalPlaces=document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces=("CUSTOM"==customDecimalPlaces)?document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value:customDecimalPlaces;
	
	var customUseGrouping=document.aimReportsFilterPickerForm3.customUseGrouping.checked;
	
	var customGroupCharacter=document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter=("CUSTOM"==customGroupCharacter)?document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value:customGroupCharacter;
	
	var customGroupSize=document.aimReportsFilterPickerForm3.customGroupSize.value;
	
	if ((decimalSymbol==customGroupCharacter)&&(customUseGrouping)){
	        var msg='<digi:trn key="rep:format:equalsSymbol">Decimal Symbol and group symbol must be diferents</digi:trn>';
			alert(msg);
			return false;
	}
	var validNumbers="0123456789";
	
	if (decimalSymbol=="" || customGroupCharacter==""){
		 var msg='<digi:trn key="rep:format:badSymbolEmpty">Symbols can not be a empty, you can use the space character</digi:trn>';
		alert(msg)
		return false;
	}
	
	
	if ((validNumbers.indexOf(decimalSymbol)!=-1)||(validNumbers.indexOf(customGroupCharacter)!=-1)){
		     var msg='<digi:trn key="rep:format:badSymbolNumber">Symbols can not be a number</digi:trn>';
			alert(msg);
			return false;
	}
	
	if (customGroupSize < 1){
		  var msg='<digi:trn key="rep:format:badGorupSize">The value should be greater than zero</digi:trn>';
			alert(msg);
			return false;
	}
	
	
	
	return true;
}

	window.onload=initScripts;
</script>





<style type="text/css">
.mask {
  -moz-opacity: 0.8;
  opacity:.80;
  filter: alpha(opacity=80);
  background-color:#2f2f2f;
}
</style>

























