package org.digijava.module.gis.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * Form for sectors by donor teaser and chart action.
 * @author Irakli Kobiashvili
 *
 */
public class SectorByDonorTeaserForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private Integer imageHeight;
	private Integer imageWidth;
	private String selectedYear;
	private Long selectedDonor;
	private Collection<AmpOrganisation> donors;
	
	public Collection<AmpOrganisation> getDonors() {
		return donors;
	}
	public void setDonors(Collection<AmpOrganisation> donors) {
		this.donors = donors;
	}
	public Long getSelectedDonor() {
		return selectedDonor;
	}
	public void setSelectedDonor(Long selectedDonor) {
		this.selectedDonor = selectedDonor;
	}
	public String getSelectedYear() {
		return selectedYear;
	}
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}
	public Integer getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}
	public Integer getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}
}
