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
		
		resetElement(aimReportsFilterPickerForm.selectedSectors);
		resetElement(aimReportsFilterPickerForm.selectedSecondarySectors);
		resetElement(aimReportsFilterPickerForm.selectedNatPlanObj);
		resetElement(aimReportsFilterPickerForm.selectedPrimaryPrograms);
        resetElement(aimReportsFilterPickerForm.selectedSecondaryPrograms);
        resetElement(aimReportsFilterPickerForm.selectedBudget);
        resetElement(aimReportsFilterPickerForm.selectedRisks);
        resetElement(aimReportsFilterPickerForm.regionSelected);
        resetElement(aimReportsFilterPickerForm.approvalStatusSelected);
        resetElement(aimReportsFilterPickerForm.lineMinRank);
        resetElement(aimReportsFilterPickerForm.planMinRank);
        resetElement(aimReportsFilterPickerForm.selectedStatuses);
        resetElement(aimReportsFilterPickerForm.selectedFinancingInstruments);
        
 		resetElement(aimReportsFilterPickerForm.selectedTypeOfAssistance);
 		resetElement(aimReportsFilterPickerForm.selectedDonorGroups);
 		resetElement(aimReportsFilterPickerForm.selectedDonorTypes);
 		resetElement(aimReportsFilterPickerForm.selectedProjectCategory);
 		resetElement(aimReportsFilterPickerForm.selectedBeneficiaryAgency);
 		resetElement(aimReportsFilterPickerForm.selectedExecutingAgency);
 		resetElement(aimReportsFilterPickerForm.selectedImplementingAgency);
 		resetElement(aimReportsFilterPickerForm.selectedDonnorAgency);

			
		if (aimReportsFilterPickerForm.jointCriteria){
			aimReportsFilterPickerForm.jointCriteria.checked=false;
		}

		if (aimReportsFilterPickerForm.governmentApprovalProcedures){
			aimReportsFilterPickerForm.governmentApprovalProcedures.checked=false;
		}
		
		if (aimReportsFilterPickerForm.computedYear){
			aimReportsFilterPickerForm.computedYear.selectedIndex=0;
		}
	}

function resetElement( elem ) {
	if (elem != null && elem.length > 1)
		for (var i=0; i<elem.length; i++)
			elem[i].checked = false;
}

function toggleCheckChildren(checkboxEl) {
	var parentTdEl				= checkboxEl.parentNode;
	var descendantCheckboxes	= parentTdEl.getElementsByTagName('input');
	for (var i=0; i<descendantCheckboxes.length; i++ ) {
		descendantCheckboxes[i].checked	= checkboxEl.checked ;
	}
}


function DivManager(divId, propertyObj) {
	this.divEl			= document.getElementById(divId);
	this.propertyObj	= propertyObj;
}

DivManager.prototype.onSelect		= function () {
	if ( this.propertyObj.lastShownDiv	!= null ) 
		this.propertyObj.lastShownDiv.style.display	= "none";
	this.divEl.style.display		= "block";
	this.propertyObj.lastShownDiv	= this.divEl;
	var parentDiv					= this.divEl
	var found						= false;
	for (var i=0; i<4; i++) { //should be more than 5 steps aways
		parentDiv	= parentDiv.parentNode;
		if ( parentDiv.nodeName.toLowerCase() == "div" && parentDiv.id.indexOf("Tab") > 0  ){
			found	= true;
			break;
		}
	}
	if (found)
		getSearchManagerInstanceById( parentDiv.id+"_search" ).setDiv(this.divEl);
	else 
		alert("Not found");
}
