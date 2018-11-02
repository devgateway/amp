package org.digijava.kernel.ampapi.endpoints.reports.saiku;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryModel implements Cloneable {

    @ApiModelProperty("Page number, starting from 1. Use 0 to retrieve only pagination information, "
            + "without any records. Default to 0.")
    private Integer page;

    @ApiModelProperty("Number of records per page to return. Default will be set to the number configured in AMP. "
            + "Set it to -1 to get the unlimited records, that will provide all records.")
    private Integer recordsPerPage;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    private List<SortParam> sorting;

    @ApiModelProperty("Second currency for report. Supported only in excel exports.")
    private String secondCurrency;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(Integer recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public List<SortParam> getSorting() {
        return sorting;
    }

    public void setSorting(List<SortParam> sorting) {
        this.sorting = sorting;
    }

    public String getSecondCurrency() {
        return secondCurrency;
    }

    public void setSecondCurrency(String secondCurrency) {
        this.secondCurrency = secondCurrency;
    }

    @Override
    public final QueryModel clone() {
        try {
            return (QueryModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
