package org.digijava.kernel.ampapi.endpoints.gpi;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

/**
 * @author Octavian Ciubotaru
 */
public class GpiFormParameters {

    @ApiModelProperty(value = "The output. Used only for indicator 1.", allowableValues = "1, 2")
    private Integer output;

    @JsonProperty(GPIReportConstants.HIERARCHY_PARAMETER)
    @ApiModelProperty(value = "The hierarchy used.", allowableValues = "donor-agency, donor-group")
    private String hierarchy;

    @ApiModelProperty("Page number, starting from 0. Default is 0.")
    private Integer page;

    @ApiModelProperty(value = "The number of records per page to return. "
            + "The default value will be set to the number configured in AMP. "
            + "Set it to -1 to get the unlimited records (all records).", example = "10")
    private Integer recordsPerPage;

    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.SettingsPH")
    private Map<String, Object> settings;

    @JsonProperty(EPConstants.FILTERS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    public static GpiFormParameters fromString(String value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(value, GpiFormParameters.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot deserialize: " + value, e);
        }
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Integer getOutput() {
        return output;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }

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

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
