

package org.digijava.module.widget.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.helper.ActivitySectorDonorFunding;


public class SectorDonorWidgetReportForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private Long donorId;
    private String startYear;
    private String endYear;
    private String sectorName;
    private String donorName;
    private String actualCommitmentsStr;
    private String actualDisbursementsStr;
    private String actualExpendituresStr;
    private Collection<ActivitySectorDonorFunding> actSectorDonorFundingInfo;
    private Long[] sectorIds;
    private String selectedCurrency;

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public Long[] getSectorIds() {
        return sectorIds;
    }

    public void setSectorIds(Long[] sectorIds) {
        this.sectorIds = sectorIds;
    }

    public String getActualCommitmentsStr() {
        return actualCommitmentsStr;
    }

    public Collection<ActivitySectorDonorFunding> getActSectorDonorFundingInfo() {
        return actSectorDonorFundingInfo;
    }

    public void setActSectorDonorFundingInfo(Collection<ActivitySectorDonorFunding> actSectorDonorFundingInfo) {
        this.actSectorDonorFundingInfo = actSectorDonorFundingInfo;
    }

    public void setActualCommitmentsStr(String actualCommitmentsStr) {
        this.actualCommitmentsStr = actualCommitmentsStr;
    }

    public String getActualDisbursementsStr() {
        return actualDisbursementsStr;
    }

    public void setActualDisbursementsStr(String actualDisbursementsStr) {
        this.actualDisbursementsStr = actualDisbursementsStr;
    }

    public String getActualExpendituresStr() {
        return actualExpendituresStr;
    }

    public void setActualExpendituresStr(String actualExpendituresStr) {
        this.actualExpendituresStr = actualExpendituresStr;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }
    

}
