package org.digijava.kernel.ampapi.endpoints.common.fm;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FMSettingsTree {
    
    @JsonProperty(EPConstants.FM_ENABLED)
    @ApiModelProperty(value = "if the fm path is enabled")
    private Boolean enabled;
    
    @ApiModelProperty(value = "list of FM settings in tree format",
            example = "Fully enabled paths =>\n"
                    + "    ```\n"
                    + "    \"REPORTING\": {\n"
                    + "         \"Measures\": {\n"
                    + "             \"Actual Disbursements\": {},\n"
                    + "             ...\n"
                    + "    ```\n"
                    + "\n"
                    + "Partial enabled paths =>\n"
                    + "    ```\n"
                    + "    \"REPORTING\": {\n"
                    + "         \"__enabled\" : true,\n"
                    + "         \"Measures\": {\n"
                    + "             \"__enabled\" : true,\n"
                    + "             \"Actual Disbursements\": {\n"
                    + "                 \"__enabled\" : true\n"
                    + "             },\n"
                    + "             ...\n"
                    + "    ```\n")
    private Map<String, FMSettingsTree> modules = new HashMap<>();
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    @JsonAnyGetter
    public Map<String, FMSettingsTree> getModules() {
        return modules;
    }
    
    public void setModules(Map<String, FMSettingsTree> modules) {
        this.modules = modules;
    }
}
