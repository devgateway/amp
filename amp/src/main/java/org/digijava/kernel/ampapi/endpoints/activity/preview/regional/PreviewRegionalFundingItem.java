package org.digijava.kernel.ampapi.endpoints.activity.preview.regional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.activity.preview.serializers.AmountSerializer;
import org.digijava.kernel.ampapi.endpoints.activity.preview.serializers.DateSerializer;
import org.digijava.module.aim.dbentity.AmpCurrency;

import java.util.Date;

public class PreviewRegionalFundingItem {
    private Long id;

    @JsonProperty("adjustment_type")
    private Long adjustmentType;

    @JsonSerialize(using = DateSerializer.class)
    @JsonProperty("transaction_date")
    private Date transactionDate;

    @JsonSerialize(using = AmountSerializer.class)
    @JsonProperty("transaction_amount")
    private Double transactionAmount;
    private AmpCurrency currency;
    @JsonProperty("region_location")
    private Long regionLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(Long adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public AmpCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    public Long getRegionLocation() {
        return regionLocation;
    }

    public void setRegionLocation(Long regionLocation) {
        this.regionLocation = regionLocation;
    }
}
