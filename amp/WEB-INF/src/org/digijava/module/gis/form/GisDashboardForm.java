package org.digijava.module.gis.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;

/**
 * Form for GIS Dashboard actions.
 * @author Irakli Kobiashvili
 *
 */
public class GisDashboardForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private Collection sectorCollection;
    private List availYears;
    //years dropdown fields
    private String selectedFromYear;
	private String selectedToYear;
	private Collection<LabelValueBean> yearsFrom;
	private Collection<LabelValueBean> yearsTo;
	
	private Collection<LabelValueBean> allDonorOrgs;
    private String selectedCurrency;
    
    //dare's adds
    private Collection currencies;
    private String selectedCalendar;
    private Collection calendars;
    private Collection<GroupingElement<HierarchyListableImplementation>> sectorElements;
	private Collection<GroupingElement<AmpTheme>> programElements;
	private Collection<GroupingElement<HierarchyListableImplementation>> donorElements;
    
    private Object[] selectedSectors;
	private Object[] selectedSecondarySectors;
    private Object[] selectedTertiarySectors;
    private Object[] selectedDonorTypes; 
	private Object[] selectedDonorGroups;
	private Object[] selectedDonnorAgency;
	private Object[] selectedNatPlanObj;
	private Object[] selectedPrimaryPrograms;
	private Object[] selectedSecondaryPrograms;

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

	public Collection<LabelValueBean> getAllDonorOrgs () {
		return this.allDonorOrgs;
	}
	
	public void setAllDonorOrgs (Collection<LabelValueBean> allDonorOrgs) {
		this.allDonorOrgs = allDonorOrgs;
	}
	
    public Collection getSectorCollection() {
        return sectorCollection;
    }

    public List getAvailYears() {
        return availYears;
    }

    public void setSectorCollection(Collection sectorCollection) {
        this.sectorCollection = sectorCollection;
    }

    public void setAvailYears(List availYears) {
        this.availYears = availYears;
    }

	public String getSelectedFromYear() {
		return selectedFromYear;
	}

	public void setSelectedFromYear(String selectedFromYear) {
		this.selectedFromYear = selectedFromYear;
	}

	public String getSelectedToYear() {
		return selectedToYear;
	}

	public void setSelectedToYear(String selectedToYear) {
		this.selectedToYear = selectedToYear;
	}

	public Collection<LabelValueBean> getYearsFrom() {
		return yearsFrom;
	}

	public void setYearsFrom(Collection<LabelValueBean> yearsFrom) {
		this.yearsFrom = yearsFrom;
	}

	public Collection<LabelValueBean> getYearsTo() {
		return yearsTo;
	}

	public void setYearsTo(Collection<LabelValueBean> yearsTo) {
		this.yearsTo = yearsTo;
	}

	public Collection<GroupingElement<HierarchyListableImplementation>> getSectorElements() {
		return sectorElements;
	}

	public void setSectorElements(
			Collection<GroupingElement<HierarchyListableImplementation>> sectorElements) {
		this.sectorElements = sectorElements;
	}

	public Collection<GroupingElement<AmpTheme>> getProgramElements() {
		return programElements;
	}

	public void setProgramElements(
			Collection<GroupingElement<AmpTheme>> programElements) {
		this.programElements = programElements;
	}

	public Collection<GroupingElement<HierarchyListableImplementation>> getDonorElements() {
		return donorElements;
	}

	public void setDonorElements(
			Collection<GroupingElement<HierarchyListableImplementation>> donorElements) {
		this.donorElements = donorElements;
	}

	public Object[] getSelectedSectors() {
		return selectedSectors;
	}

	public void setSelectedSectors(Object[] selectedSectors) {
		this.selectedSectors = selectedSectors;
	}

	public Object[] getSelectedSecondarySectors() {
		return selectedSecondarySectors;
	}

	public void setSelectedSecondarySectors(Object[] selectedSecondarySectors) {
		this.selectedSecondarySectors = selectedSecondarySectors;
	}

	public Object[] getSelectedTertiarySectors() {
		return selectedTertiarySectors;
	}

	public void setSelectedTertiarySectors(Object[] selectedTertiarySectors) {
		this.selectedTertiarySectors = selectedTertiarySectors;
	}

	public Object[] getSelectedDonorTypes() {
		return selectedDonorTypes;
	}

	public void setSelectedDonorTypes(Object[] selectedDonorTypes) {
		this.selectedDonorTypes = selectedDonorTypes;
	}

	public Object[] getSelectedDonorGroups() {
		return selectedDonorGroups;
	}

	public void setSelectedDonorGroups(Object[] selectedDonorGroups) {
		this.selectedDonorGroups = selectedDonorGroups;
	}

	public Object[] getSelectedDonnorAgency() {
		return selectedDonnorAgency;
	}

	public void setSelectedDonnorAgency(Object[] selectedDonnorAgency) {
		this.selectedDonnorAgency = selectedDonnorAgency;
	}

	public Object[] getSelectedNatPlanObj() {
		return selectedNatPlanObj;
	}

	public void setSelectedNatPlanObj(Object[] selectedNatPlanObj) {
		this.selectedNatPlanObj = selectedNatPlanObj;
	}

	public Object[] getSelectedPrimaryPrograms() {
		return selectedPrimaryPrograms;
	}

	public void setSelectedPrimaryPrograms(Object[] selectedPrimaryPrograms) {
		this.selectedPrimaryPrograms = selectedPrimaryPrograms;
	}

	public Object[] getSelectedSecondaryPrograms() {
		return selectedSecondaryPrograms;
	}

	public void setSelectedSecondaryPrograms(Object[] selectedSecondaryPrograms) {
		this.selectedSecondaryPrograms = selectedSecondaryPrograms;
	}

	public Collection getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Collection currencies) {
		this.currencies = currencies;
	}

	public Collection getCalendars() {
		return calendars;
	}

	public void setCalendars(Collection calendars) {
		this.calendars = calendars;
	}

	public String getSelectedCalendar() {
		return selectedCalendar;
	}

	public void setSelectedCalendar(String selectedCalendar) {
		this.selectedCalendar = selectedCalendar;
	}

	
}
