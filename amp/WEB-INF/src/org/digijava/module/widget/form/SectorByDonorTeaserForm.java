package org.digijava.module.widget.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Form for sectors by donor teaser and chart action.
 * @author Irakli Kobiashvili
 *
 */
public class SectorByDonorTeaserForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private Integer imageHeight;
	private Integer imageWidth;
	private String selectedFromYear;
	private String selectedToYear;
	private Long selectedDonor;
	private Collection<AmpOrganisation> donors;
	private Boolean showLegend;
	private Boolean showLabel;
	private Boolean showTitle;
	private Boolean showPercentage;
	private Boolean showAmount;
	//This property hold values that will be shown on years' drop-down
	private Collection<LabelValueBean> yearsFrom;
	private Collection<LabelValueBean> yearsTo;
	//indicates whether amounts are in thousands or not.
	private Boolean amountsInThousands;
    private Long timestamp;
    private String selectedCurrency;

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

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
	
	/**
	 * TODO: interface does not support millions!
	 * @return
	 */
	public Boolean getAmountsInThousands() {
		if(amountsInThousands==null){
			amountsInThousands=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS).equals("true");
		}
		return amountsInThousands;
	}
	public void setAmountsInThousands(Boolean amountsInThousands) {
		this.amountsInThousands = amountsInThousands;
	}
}
