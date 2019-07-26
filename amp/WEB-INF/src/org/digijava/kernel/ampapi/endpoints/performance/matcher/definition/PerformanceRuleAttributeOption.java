package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PerformanceRuleAttributeOption {

    @ApiModelProperty(example = "fundingClassificationDate")
    private String name;

    @ApiModelProperty(example = "Funding Classification Date")
    private String label;

    private String fmPath;

    public PerformanceRuleAttributeOption(String name) {
        this(name, name);
    }

    public PerformanceRuleAttributeOption(String name, String label) {
        this(name, label, null);
    }
    
    public PerformanceRuleAttributeOption(String name, String label, String fmPath) {
        this.name = name;
        this.label = label;
        this.fmPath = fmPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }
    
    @JsonProperty(PerformanceRuleConstants.JSON_ATTRIBUTE_TRANSLATED_LABEL)
    @ApiModelProperty(example = "Funding Classification Date")
    public String getTranslatedLabel() {
        return TranslatorWorker.translateText(label);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonIgnore
    public String getFmPath() {
        return fmPath;
    }

    public void setFmPath(String fmPath) {
        this.fmPath = fmPath;
    }
    
    public boolean isVisible() {
        if (!StringUtils.isBlank(fmPath)) {
            return FMVisibility.isVisible(fmPath);
        }
        
        return true;
    }
}
