package org.digijava.module.aim.dbentity;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

import java.util.Date;

/**
 * an item which holds information regarding a funding detail (e.g. currently AmpFundingDetail or AmpFundingMTEFProjection)
 * it fixes the fact that MTEFProjection are not kept as funding items in AMP - hence this interface tries to unify them
 * @author Dolghier Constantin
 *
 */
public interface FundingInformationItem {

    public Double getTransactionAmount();
    public Double getAbsoluteTransactionAmount();
    public AmpCurrency getAmpCurrencyId();
    public Date getTransactionDate();
    public Date getReportingDate();
    public Date getUpdatedDate();
    public AmpOrganisation getRecipientOrg();
    public AmpRole getRecipientRole();
    public Integer getTransactionType();
    public AmpCategoryValue getAdjustmentType();
    public String getDisbOrderId();
    public Double getFixedExchangeRate();
    public IPAContract getContract();
    public String getExpCategory();
    public FundingPledges getPledgeid();
    public Float getCapitalSpendingPercentage();
    public Long getDbId();
    public void setTransactionAmount(Double transactionAmount);
    public Boolean getDisasterResponse();
    String getDisbursementId();
    public AmpCategoryValue getExpenditureClass();
    
    //method used to detect if the record has changed while editing an activity
    public Long getCheckSum();
    public void setCheckSum(Long checkSum);
    public void setUpdatedDate(Date dateUpdated);
}
