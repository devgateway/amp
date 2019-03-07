package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.gpi.ApplyThousandsForEntryConverter;
import org.digijava.kernel.ampapi.endpoints.gpi.ApplyThousandsForVisibilityConverter;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateDeserializer;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmpGPINiAidOnBudget implements Serializable {

    private static final long serialVersionUID = -8747493117052602462L;

    @JsonProperty("id")
    private Long ampGPINiAidOnBudgetId;

    @JsonProperty("currencyCode")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "currencyCode",
            resolver = EntityResolver.class, scope = AmpCurrency.class)
    @JsonIdentityReference(alwaysAsId = true)
    @ApiModelProperty(example = "USD")
    @NotNull
    private AmpCurrency currency;

    @JsonProperty("donorId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampOrgId",
            resolver = EntityResolver.class, scope = AmpOrganisation.class)
    @JsonIdentityReference(alwaysAsId = true)
    @ApiModelProperty(value = "Donor Agency", example = "41")
    @NotNull
    private AmpOrganisation donor;

    @ApiModelProperty(example = "50000")
    @NotNull @Min(0)
    @JsonSerialize(converter = ApplyThousandsForVisibilityConverter.class)
    @JsonDeserialize(converter = ApplyThousandsForEntryConverter.class)
    private Double amount;

    @JsonSerialize(using = ISO8601DateSerializer.class)
    @JsonDeserialize(using = ISO8601DateDeserializer.class)
    @ApiModelProperty(example = "2018-11-29")
    @NotNull
    private Date indicatorDate;

    public Long getAmpGPINiAidOnBudgetId() {
        return ampGPINiAidOnBudgetId;
    }

    public void setAmpGPINiAidOnBudgetId(Long ampGPINiAidOnBudgetId) {
        this.ampGPINiAidOnBudgetId = ampGPINiAidOnBudgetId;
    }

    public AmpCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    public AmpOrganisation getDonor() {
        return donor;
    }

    public void setDonor(AmpOrganisation donor) {
        this.donor = donor;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getIndicatorDate() {
        return indicatorDate;
    }

    public void setIndicatorDate(Date indicatorDate) {
        this.indicatorDate = indicatorDate;
    }

}
