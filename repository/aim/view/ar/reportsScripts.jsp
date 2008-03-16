<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<%@page import="org.digijava.module.aim.form.ReportsFilterPickerForm"%>
<link rel="stylesheet" href="<digi:file src="module/aim/css/newamp.css"/>" />
<link rel="stylesheet" href="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.css"/>" />

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



<script type="text/javascript">
		var myPanel1 = new YAHOO.widget.Panel("new", {
		    fixedcenter: false,
		    constraintoviewport: true,
		    underlay:"shadow",
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
		    
		    
	function initScripts() {
	    var msg='\n<digi:trn key="rep:filter:selectFilter">Select filters</digi:trn>';
		myPanel1.setHeader(msg);
		myPanel1.setBody("Empty");
		myPanel1.render(document.body);
		
		var msgP2='\n<digi:trn key="rep:filter:selectsorter">Please select hierarchy sorter criteria</digi:trn>';
;
		myPanel2.setHeader(msgP2);
		myPanel2.setBody("Empty");
		myPanel2.render(document.body);
	}
	
	function showFilter() {
		var element = document.getElementById("myFilter");
		element.style.display = "inline";
		myPanel1.setBody(element);
		myPanel1.center();
		myPanel1.show();
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
