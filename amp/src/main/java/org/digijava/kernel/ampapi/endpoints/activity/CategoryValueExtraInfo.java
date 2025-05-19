package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extra info provided for a Category Value
 * 
 * @author Nadejda Mandrescu
 */
public class CategoryValueExtraInfo {
    public static final int EXTRA_INFO_START_INDEX = 3;
    public static final int EXTRA_INFO_PREFIX_INDEX = 4;
    
    @JsonProperty("index")
    private final Integer index;

    @JsonProperty("workspace-prefix")
    private final String prefix;

    public CategoryValueExtraInfo(Integer index) {
        this.index = index;
        this.prefix = null;
    }

    public CategoryValueExtraInfo(Integer index, String prefix) {
        this.index = index;
        this.prefix = prefix;
    }
}
