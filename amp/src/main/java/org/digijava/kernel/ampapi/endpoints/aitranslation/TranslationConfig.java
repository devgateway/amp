package org.digijava.kernel.ampapi.endpoints.aitranslation;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class TranslationConfig {

    @ApiModelProperty("If the value is false then translation API is disabled and will not perform translations. "
            + "If value is true, then translation API is enabled.")
    private final boolean enabled;

    @ApiModelProperty("Maximum number of characters per month that will be translated. Once this API reached this "
            + "limit, it will stop translating. If this value is 0 or negative, there is no limit.")
    private final int maxChars;

    public TranslationConfig(boolean enabled, int maxChars) {
        this.enabled = enabled;
        this.maxChars = maxChars;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getMaxChars() {
        return maxChars;
    }
}
