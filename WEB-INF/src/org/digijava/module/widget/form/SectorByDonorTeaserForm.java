package org.digijava.module.widget.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
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
	private Boolean showLegend;
	private Boolean showLabel;
	private Boolean showTitle;
	private Boolean showPercentage;
	private Boolean showAmount;
	//This property hold values that will be shown on years' drop-down
	private Collection<LabelValueBean> years;
	
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
	public Boolean getShowLegend() {
		return showLegend;
	}
	public void setShowLegend(Boolean showLegend) {
		this.showLegend = showLegend;
	}
	public Boolean getShowLabel() {
		return showLabel;
	}
	public void setShowLabel(Boolean showLebel) {
		this.showLabel = showLebel;
	}
	public Boolean getShowTitle() {
		return showTitle;
	}
	public void setShowTitle(Boolean showTitle) {
		this.showTitle = showTitle;
	}
	public Boolean getShowPercentage() {
		return showPercentage;
	}
	public void setShowPercentage(Boolean showPercentage) {
		this.showPercentage = showPercentage;
	}
	public Boolean getShowAmount() {
		return showAmount;
	}
	public void setShowAmount(Boolean showAmount) {
		this.showAmount = showAmount;
	}
	public Collection<LabelValueBean> getYears() {
		return years;
	}
	public void setYears(Collection<LabelValueBean> years) {
		this.years = years;
	}
}
