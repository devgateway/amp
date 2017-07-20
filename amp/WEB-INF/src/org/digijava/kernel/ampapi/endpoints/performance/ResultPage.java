package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * 
 * @author Viorel Chihai
 *
 * @param <T>
 */
@JsonPropertyOrder({ "data", "totalRecords" })
public class ResultPage<T> {

    @JsonProperty(value = "data")
    private List<T> items = new ArrayList<>();

    @JsonProperty(value = "totalRecords")
    private int totalRecords = 0;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
