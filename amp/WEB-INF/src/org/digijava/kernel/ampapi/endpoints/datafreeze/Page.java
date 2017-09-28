package org.digijava.kernel.ampapi.endpoints.datafreeze;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
    private Integer totalRecords = 0;
    private List<T> data = new ArrayList<>();

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
