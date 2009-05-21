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
		
		if(aimReportsFilterPickerForm.approvalStatusSelected)
			aimReportsFilterPickerForm.approvalStatusSelected.selectedIndex=0;
		
		if(aimReportsFilterPickerForm.selectedresponsibleorg)
			aimReportsFilterPickerForm.selectedresponsibleorg.selectedIndex=-1;
			
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
			
		if (aimReportsFilterPickerForm.selectedProjectCategory)
			aimReportsFilterPickerForm.selectedProjectCategory.selectedIndex=-1;

		if (aimReportsFilterPickerForm.selectedBeneficiaryAgency)
			aimReportsFilterPickerForm.selectedBeneficiaryAgency.selectedIndex=-1;
		if (aimReportsFilterPickerForm.selectedExecutingAgency)
			aimReportsFilterPickerForm.selectedExecutingAgency.selectedIndex=-1;
		if (aimReportsFilterPickerForm.selectedImplementingAgency)
			aimReportsFilterPickerForm.selectedImplementingAgency.selectedIndex=-1;
		
		if (aimReportsFilterPickerForm.selectedDonnorAgency)
			aimReportsFilterPickerForm.selectedDonnorAgency.selectedIndex=-1;
		
		
		if (aimReportsFilterPickerForm.jointCriteria){
			aimReportsFilterPickerForm.jointCriteria.checked=false;
		}

		if (aimReportsFilterPickerForm.governmentApprovalProcedures){
			aimReportsFilterPickerForm.governmentApprovalProcedures.checked=false;
		}
	}
