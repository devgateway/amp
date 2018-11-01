package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

/**
 * @author Octavian Ciubotaru
 */
public class SaveTabRequest {

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    // tabs are concerned only of currency and calendar type
    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    private List<Map<String, String>> reportData;

    @ApiModelProperty("JQGrid style sort param.")
    private String sidx;

    @ApiModelProperty("JQGrid style sort param.")
    private String sord;

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public List<Map<String, String>> getReportData() {
        return reportData;
    }

    public void setReportData(List<Map<String, String>> reportData) {
        this.reportData = reportData;
    }
}
