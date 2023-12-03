package org.digijava.kernel.ampapi.endpoints.datafreeze;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class DataFreezeInformation {
    
    @JsonSerialize(using = DataFreezeDateSerializer.class)
    @ApiModelProperty(example = "2017-08-30")
    private Date freezingDate;
    
    private Integer freezingCount;
    
    public DataFreezeInformation(Date freezingDate, Integer freezingCount) {
        this.freezingDate = freezingDate;
        this.freezingCount = freezingCount;
    }
    
    public Date getFreezingDate() {
        return freezingDate;
    }
    
    public void setFreezingDate(Date freezingDate) {
        this.freezingDate = freezingDate;
    }
    
    public Integer getFreezingCount() {
        return freezingCount;
    }
    
    public void setFreezingCount(Integer freezingCount) {
        this.freezingCount = freezingCount;
    }
}
