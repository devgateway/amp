package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PerformanceRuleAttributeOption {

    private String name;
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
            return FMVisibility.isVisible(fmPath, null);
        }
        
        return true;
    }
}
