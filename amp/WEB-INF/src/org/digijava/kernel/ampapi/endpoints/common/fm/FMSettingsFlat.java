package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.swagger.annotations.ApiModelProperty;

public class FMSettingsFlat {
    
    @ApiModelProperty(value = "list of FM settings in flat format",
            example = "### Use cases\n"
                    + "1. Fully enabled paths => /Activity Form/Organiation/Donor Organization\n"
                    + "\n"
                    + "2. Partial enabled paths => "
                    + "/Activity Form[true]/Organiation[false]/Donor Organization[true]\n")
    private Map<String, Set<String>> modules = new HashMap<>();
    
    @JsonAnyGetter
    public Map<String, Set<String>> getModules() {
        return modules;
    }
    
    public void setModules(Map<String, Set<String>> modules) {
        this.modules = modules;
    }
}
