package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author Viorel Chihai
 *
 * @param <T>
 */
@JsonPropertyOrder({ "data", "totalRecords" })
public class ResultPage<T> {

    @ApiModelProperty("current page of records")
    @JsonProperty(value = "data")
    private List<T> items;

    @ApiModelProperty(value = "total number of records", example = "6")
    @JsonProperty(value = "totalRecords")
    private int totalRecords;

    public ResultPage(List<T> items, int totalRecords) {
        this.items = items;
        this.totalRecords = totalRecords;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public List<T> getItems() {
        return items;
    }
}
