package org.digijava.module.gis.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;

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

	
}
