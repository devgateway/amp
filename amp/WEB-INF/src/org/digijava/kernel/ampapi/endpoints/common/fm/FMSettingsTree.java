package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.swagger.annotations.ApiModelProperty;

public class FMSettingsTree {
    
    @ApiModelProperty(value = "list of FM settings listed as a tree",
            example = "Detail tree & fully enabled paths =>\n"
                    + "    ```\n"
                    + "    \"REPORTING\": {\n"
                    + "         \"Measures\": {\n"
                    + "             \"Actual Disbursements\": {},\n"
                    + "             ...\n"
                    + "    ```\n"
                    + "\n"
                    + "Detail tree & fully enabled paths =>\n"
                    + "    ```\n"
                    + "    \"REPORTING\": {\n"
                    + "         \"__enabled\" : true, // omitted if fullEnabledPaths are requested (same below)\n"
                    + "         \"Measures\": {\n"
                    + "             \"__enabled\" : true,\n"
                    + "             \"Actual Disbursements\": {\n"
                    + "                 \"__enabled\" : true\n"
                    + "             },\n"
                    + "             ...\n"
                    + "    ```\n")
    private Map<String, Object> modules = new HashMap<>();
    
    @JsonAnyGetter
    public Map<String, Object> getModules() {
        return modules;
    }
    
    public void setModules(Map<String, Object> modules) {
        this.modules = modules;
    }
}
