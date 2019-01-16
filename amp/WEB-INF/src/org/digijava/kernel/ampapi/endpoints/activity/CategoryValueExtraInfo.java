package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extra info provided for a Category Value
 * 
 * @author Nadejda Mandrescu
 */
public class CategoryValueExtraInfo {
    public static final int EXTRA_INFO_START_INDEX = 3; 
    
    @JsonProperty("index")
    private final Integer index;

    public CategoryValueExtraInfo(Integer index) {
        this.index = index;
    }
}
