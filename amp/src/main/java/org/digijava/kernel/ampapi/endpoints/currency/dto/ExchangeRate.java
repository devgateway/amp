package org.digijava.kernel.ampapi.endpoints.currency.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateSerializer;

import java.util.Date;

/**
 * @author Octavian Ciubotaru
 */
@ApiModel(description = "Exchange rate for a specific day")
public class ExchangeRate {

    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-06-18")
    private Date date;

    @ApiModelProperty(example = "1.16")
    private Double rate;

    public ExchangeRate(Date date, Double rate) {
        this.date = date;
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public Double getRate() {
        return rate;
    }
}
