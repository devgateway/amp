var dateFilterErrorMsg = null; 
function resetFilter(){
		resetRootCheckboxes();
		if (aimReportsFilterPickerForm.text)
			aimReportsFilterPickerForm.text.value="";

		if (aimReportsFilterPickerForm.indexString)
			aimReportsFilterPickerForm.indexString.value="";
			
		if (aimReportsFilterPickerForm.searchMode)
			aimReportsFilterPickerForm.searchMode.value="0";

		if (aimReportsFilterPickerForm.fromDate)
			aimReportsFilterPickerForm.fromDate.value="";
		
		if (aimReportsFilterPickerForm.toDate)
			aimReportsFilterPickerForm.toDate.value="";
			
		if (aimReportsFilterPickerForm.currency)
			aimReportsFilterPickerForm.currency.value=aimReportsFilterPickerForm.defaultCurrency.value;
		
		if (aimReportsFilterPickerForm.fromYear)		
			aimReportsFilterPickerForm.fromYear.selectedIndex=0;
		
		if (aimReportsFilterPickerForm.toYear)
			aimReportsFilterPickerForm.toYear.selectedIndex=0;
			
		if (aimReportsFilterPickerForm.fromMonth)
			aimReportsFilterPickerForm.fromMonth.selectedIndex=0;
			
		if (aimReportsFilterPickerForm.toMonth)
			aimReportsFilterPickerForm.toMonth.selectedIndex=0;
		
		resetElement(aimReportsFilterPickerForm.selectedSectors);
		resetElement(aimReportsFilterPickerForm.selectedSecondarySectors);
        resetElement(aimReportsFilterPickerForm.selectedTertiarySectors);
        resetElement(aimReportsFilterPickerForm.selectedQuaternarySectors);
        resetElement(aimReportsFilterPickerForm.selectedQuinarySectors);
		resetElement(aimReportsFilterPickerForm.selectedNatPlanObj);
		resetElement(aimReportsFilterPickerForm.selectedPrimaryPrograms);
        resetElement(aimReportsFilterPickerForm.selectedSecondaryPrograms);
        resetElement(aimReportsFilterPickerForm.selectedBudgets);
        resetElement(aimReportsFilterPickerForm.selectedWorkspaces);
        resetElement(aimReportsFilterPickerForm.selectedRisks);
        resetElement(aimReportsFilterPickerForm.regionSelected);
        resetElement(aimReportsFilterPickerForm.approvalStatusSelected);
        resetElement(aimReportsFilterPickerForm.lineMinRanks);
        resetElement(aimReportsFilterPickerForm.selectedStatuses);
        resetElement(aimReportsFilterPickerForm.selectedFinancingInstruments);
        resetElement (aimReportsFilterPickerForm.selectedContractingAgencyGroups);
        resetElement (aimReportsFilterPickerForm.selectedContractingAgency);
        aimReportsFilterPickerForm.justSearch.checked = false;
       
        
        resetElement(aimReportsFilterPickerForm.selectedTypeOfAssistance);
        resetElement(aimReportsFilterPickerForm.selectedExpenditureClasses);
        resetElement(aimReportsFilterPickerForm.selectedPerformanceAlertLevels);
        resetElement(aimReportsFilterPickerForm.selectedModeOfPayment);
        resetElement(aimReportsFilterPickerForm.selectedProjectImplUnit);
 		resetElement(aimReportsFilterPickerForm.selectedDonorGroups);
 		resetElement(aimReportsFilterPickerForm.selectedDonorTypes);
 		resetElement(aimReportsFilterPickerForm.selectedProjectCategory);
 		resetElement(aimReportsFilterPickerForm.selectedBeneficiaryAgency);
 		resetElement(aimReportsFilterPickerForm.selectedExecutingAgency);
 		resetElement(aimReportsFilterPickerForm.selectedImplementingAgency);
 		resetElement(aimReportsFilterPickerForm.selectedDonnorAgency);
 		resetElement(aimReportsFilterPickerForm.selectedresponsibleorg);
 		resetElement(aimReportsFilterPickerForm.selectedComponentFundingOrg);
 		resetElement(aimReportsFilterPickerForm.selectedComponentSecondResponsibleOrg);

 		resetElement(aimReportsFilterPickerForm.selectedConcensionalityLevel);

 		resetElement(aimReportsFilterPickerForm.disbursementOrders);
 		resetElement(aimReportsFilterPickerForm.selectedBudgets);
 		resetElement(aimReportsFilterPickerForm.lineMinRanks);
 		if (document.getElementById("workspace_only")!=null){
 			document.getElementById("workspace_only").checked = false;
 		}

		if (aimReportsFilterPickerForm.computedYear){
			aimReportsFilterPickerForm.computedYear.selectedIndex=0;
		}
		
		if (aimReportsFilterPickerForm.actualAppYear)
			aimReportsFilterPickerForm.actualAppYear.selectedIndex = 0;
		
		resetDateOptions();
	}

function resetElement( elem ) {
	if (elem != null && elem.length > 1)
		for (var i=0; i<elem.length; i++) {
			elem[i].checked = false;
			toggleCheckChildren(elem[i]);
		}
	else 
		if (elem!=null){
			elem.checked	= false;
			toggleCheckChildren(elem);
		}
}

function resetRootCheckboxes() {
	var allCheckboxes	= document.getElementsByTagName("input");
	for (var i=0; i<allCheckboxes.length; i++) {
		var inputYuiEl	= new YAHOO.util.Element(allCheckboxes[i]);
		if ( inputYuiEl.hasClass("root_checkbox") ) {
			allCheckboxes[i].checked = false;
		}
	}
}

YAHOO.namespace("YAHOO.amptab");

YAHOO.amptab.afterFiltersLoad   = function (){
	//alert('ss');
	var selColor    = "#BFD2DF";
	 	donorsPropertyObj               = new RowManagerProperty(null, "filter_rows_donors", selColor);
	 	relAgenciesPropertyObj  = new RowManagerProperty(null, "filter_rows_rel_agencies", selColor);
	 	sectorsPropertyObj              = new RowManagerProperty(null, "filter_rows_sectors", selColor);
	 	programsPropertyObj             = new RowManagerProperty(null, "filter_rows_programs", selColor);
	 	financingLocPropertyObj         = new RowManagerProperty(null, "filter_rows_finanacing_loc", selColor);
	 	otherCriteriaPropertyObj        = new RowManagerProperty(null, "filter_rows_other_criteria", selColor);
	 	
	 	YAHOO.amptab.initDisplayOfMemberSelectors("donorsTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("relAgenciesTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("sectorsTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("programsTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("financingLocTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("otherCriteriaTab");
	 	
	 	enableDisableDateOptions();
	 	
	 };
	 	
YAHOO.amptab.initDisplayOfMemberSelectors       = function(bigDivId) {
	var bigDivEl            = document.getElementById(bigDivId);
	 	//alert("aici" + bigDivEl);
	 	if (bigDivEl != null) {
		 	var listOfDivs  = bigDivEl.getElementsByTagName("div");
		 	if (listOfDivs != null && listOfDivs.length > 0) {
		 		for (var i=0; i<listOfDivs.length; i++) {
		 			var divYuiEl    = new YAHOO.util.Element(listOfDivs[i]);
		 			if ( divYuiEl.hasClass("grouping_selector_wrapper") ) {
		 				var buttonEl    = listOfDivs[i].getElementsByTagName("button")[0];
		 					if(typeof(buttonEl) != "undefined") buttonEl.click();
		 					break;
		 				}
		 		}
		 	}
		}
	};

/**
 * recursively goes from an element upto the top of the filters. Top of filters is detected through an ugly heuristic: if elem.nodeName is NOT one of (li, ul, ol, table, tr, td, tbody)
 * @param elem
 */
function uncheckAllParents(elem){
	var acceptableElems = ['ul', 'ol', 'li', 'table', 'tbody', 'tr', 'td'];
	while (elem != null){
		if ($.inArray(elem.nodeName.toLowerCase(), acceptableElems) < 0)
			return;
		if (elem.nodeName.toLowerCase() == 'li'){
			$(elem).children('table').find('input[type="checkbox"]').prop('checked', false);
			/**
			 * this selector will only check a single element, because of the HTML structure:
			 * <li>
			 * 		<table>
			 * 		<ul>
			 * 			<li>
			 * 			<li>
			 * 			<li>
			 */
		}
		elem = elem.parentNode;
	}
}

function toggleCheckChildren(checkboxEl){
	
	var parentTdEl = $(checkboxEl).closest('li').get(0); // the <li> parent of the checkbox
	$(parentTdEl).find('input').prop('checked', checkboxEl.checked); // all the children of the just-checked checkbox are set/unset child according to the parent
	
	if (!checkboxEl.checked) { // checkbox has just been unselected -> unselected all parents
		uncheckAllParents(parentTdEl.parentNode); // this will only be relevant for hierarchical lists like locations / programs / sectors
	}
	
	var curGroup = checkboxEl.name;
	var groupsToClean = new Array(); // a side-effect of running showOrHideCheckboxesInGroup is that all the children are set - resetting them as an afterthought. Ugly hack, but code will be dropped in couple of months anyway
	while (curGroup != null){
		// hide all child-category checkboxes which have a parent which is unselected in the current category
		// if current category has no selected item -> this is equivalent to "all selected"
		showOrHideCheckboxesInGroup(curGroup, groupsToClean);
		curGroup = getRelatedTab(curGroup);
	};
	
	for(var i = 0; i < groupsToClean.length; i++) // clean up side-effect
		$('input:checkbox[name="' + groupsToClean[i] + '"]').prop('checked', false);
}

function showOrHideCheckboxesInGroup(parentGroup, groupsToClean){
	var childGroup = getRelatedTab(parentGroup);
	if (childGroup == null) 
		return; // this is a leaf-level category -> nothing related to refresh
	var selectedIds = new Object(); // values of selected parent elements: poor man's HashMap
	var anySelected = false; // whether selectedIds has any value, since JS's map.keys.length() is slow, consumes memory and does not work on IE8- 
	$('input:checkbox[name="' + parentGroup + '"]:checked').each(function(){
		selectedIds[this.value] = "ahem"; // mark as having been checked
		anySelected = true; 
	});
	groupsToClean.push(childGroup);
	$('input:checkbox[name="' + childGroup + '"]').each(function(){
		var parentValue = $(this).attr('parentid');
		var shouldBeShown = (!anySelected) || (parentValue && selectedIds.hasOwnProperty(parentValue));
		if (shouldBeShown){
			$(this).prop('checked', true);
			$(this).closest('li').css('display', 'block');
		}else{
			$(this).prop('checked', false);
			$(this).closest('li').css('display', 'none');
		}
	});	
}

/**
 * returns the name of the checkboxes which are logical children of a given type of checkboxes
 * (type] of checkbox = input[type='checkbox'].name)
 * @param name
 * @returns
 */
function getRelatedTab (name) {
	if (name =='selectedDonorTypes') {
		return  'selectedDonorGroups';
	}
	else if (name == 'selectedDonorGroups') {
	return 'selectedDonnorAgency';	
	}
	else if(name== 'selectedContractingAgencyGroups') {
		return 'selectedContractingAgency';
	}
	else {
		return null;
	}
	
}
	 
function DivManager(divId, propertyObj) {
	 	this.divEl                      = document.getElementById(divId);
	 	this.propertyObj        = propertyObj;
	 }
	
DivManager.prototype.onSelect           = function () {
	 	if ( this.propertyObj.lastShownDiv      != null )
	 		this.propertyObj.lastShownDiv.style.display     = "none";
	 	this.divEl.style.display                = "block";
	 	this.propertyObj.lastShownDiv   = this.divEl;
	 	var parentDiv                                   = this.divEl;
	 	var found                                               = false;
	 	for (var i=0; i<4; i++) { //should be more than 5 steps aways
		 	parentDiv       = parentDiv.parentNode;
		 	if ( parentDiv.nodeName.toLowerCase() == "div" && parentDiv.id.indexOf("Tab") > 0  ){
		 		found   = true;
		 		break;
		 	}
	 	}
	 	if (found){
	 		getSearchManagerInstanceById( parentDiv.id+"_search" ).setDiv(this.divEl);
	 	}
	 };
	 

	 function changeDateFilteringGroup(group){
	 	var selectFields = $("#" + group.name + "_1").find("select[id^='filter_input']");
	 	selectFields = selectFields.add(  $("#" + group.name + "_1").find("input[id^='filter_input']"));
	 	
	 	var dateFields = $("#" + group.name + "_0").find(".dateInputMarker");
	 	dateFields = dateFields.add( $("#" + group.name + "_0").find("a"));
	 	
	 	if(group.value == "1"){//dynamic filter selected
	 		enableDisableInputs(selectFields, dateFields);
	 	}else{//date fields filter selected
	 		enableDisableInputs(dateFields, selectFields);
	 	}
	 }
	 
	 function disabler(event) {
		    event.preventDefault();
		    return false;
	}
	 
	 function enableDisableInputs(toEnable, toDisable){
	 	toDisable.each(
	 		function(){
	 			$(this).attr("disabled", "disabled");
	 			
	 			if( $(this).attr("href") ){
	 				$(this).bind("click",disabler);	
	 			}
	 			
	 		}		
	 	);	
	 	toEnable.each(
	 			function(){
	 				$(this).removeAttr("disabled");
	 				if( $(this).attr("href") ){
	 					$(this).unbind("click",disabler);
	 				}
	 			}		
	 		);	
	 }
	 
	 function enableDisableDateOptions(){

		 var groups = $("input[name^='optGroupDateFilter_']:radio");
		 groups.each(
				 function(){
					 if ($(this).attr("checked")){
						 changeDateFilteringGroup(this);
					 }
				 }
		 );
	 }
	 
	 function toggleWorkspaceLinkedVisibility() {
		 // var testFunction       = function (e) {
	 	 //                       if ( e.name == "publicReport")
	 	 //                               return true;
	 	 //                       return false;
	 	 //               };              
	 	 //               var publicRepEl        = YAHOO.util.Dom.getElementBy(testFunction, "input", "type_step_div");
		 var publicRepEl                = document.getElementById("publicReportChkBox");
		 var workSpaceLinkedEl  = document.getElementById("workspaceLinkedHolder");

		 if ( workSpaceLinkedEl != null ) {
			 if ( publicRepEl.checked ) 
				 workSpaceLinkedEl.style.display = "block";
			 else
	 	 	 	workSpaceLinkedEl.style.display = "none";
		 	}
	 }


	 function resetDateOptions(){
		 var groups = $("input[name^='optGroupDateFilter_']:radio");
		 groups.each(
				 function(){

					 var selectFields = $("#" + this.name + "_" + this.value).find("select[id^='filter_input']");
					 selectFields.each(function(){
						 this.selectedIndex = 0;
					 });
					 
					 var inputFields = $("#" + this.name + "_" + this.value).find(".dateInputMarker");
					 inputFields = inputFields.add(  $("#" + this.name + "_" + this.value).find("input[id^='filter_input']")); 
					 inputFields.each(function(){
						 this.value = "";
					 });

					 if (this.value == 0){
						 $(this).attr("checked","checked");
						 changeDateFilteringGroup(this);
					 }else{
						 $(this).removeAttr("checked");
					 }
				 }
		 );
	 }
	 
		function validateDynamicDateFilters(){
			var wrongMsg = null;
			$("input[id^='filter_input_amount_']:text").each(function () { 
				var re =new RegExp(/[^0-9]/g);
				if(!$(this).attr("disabled") && this.value && re.test(this.value)){
					$(this).focus();
					wrongMsg = $("#" + this.id + "_error").val();
				}
			});
			if (wrongMsg){
				alert(wrongMsg);
				return false;
			}
			return true;
		}
	 
	 
	 function validateDateFilters(){
		 var wrongMsg = null;
		 var inputFields = $(document).find(".dateInputMarker");
		 //assuming that the date fields come in pairs (from-to)
		 for(var i=0; i < inputFields.length - 1; i+=2){
			 var fromStr = inputFields[i].value;
			 var toStr = inputFields[i+1].value;
			 
			 if(!validateFromToDates(fromStr, toStr)){
					wrongMsg = dateFilterErrorMsg; 
					
					var label = $(inputFields[i]).next("input:hidden").val();
					
					if(label){
						wrongMsg = wrongMsg + " ( " + label + " )";
					}
					
					break;
			 }
		 }
			if (wrongMsg){
				alert(wrongMsg);
				return false;
			}
			return true;
		 
	 }
	 
	 //this function depends on newCalendar.jsp
	 function validateFromToDates(fromStr, toStr){
		 if (!fromStr || !toStr) return true;
		 var fromArray = dateStringToObject (fromStr, dateFormat );
		 var toArray = dateStringToObject (toStr, dateFormat );
		 
		 var from = new Date(fromArray.year, fromArray.month - 1, fromArray.day);
		 var to = new Date(toArray.year, toArray.month - 1, toArray.day);
		 
		 return (from <= to);
		 
	 }