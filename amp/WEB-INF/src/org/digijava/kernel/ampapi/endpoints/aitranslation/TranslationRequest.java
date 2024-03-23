package org.digijava.kernel.ampapi.endpoints.aitranslation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@ApiModel("Batch translation request")
public class TranslationRequest {

    /**
     * BCP-47 language code
     */
    @ApiModelProperty(example = "en")
    private String sourceLanguageCode;

    /**
     * BCP-47 language code
     */
    @ApiModelProperty(example = "fr")
    private String targetLanguageCode;

    private List<String> contents;

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public String getTargetLanguageCode() {
        return targetLanguageCode;
    }

    public void setTargetLanguageCode(String targetLanguageCode) {
        this.targetLanguageCode = targetLanguageCode;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }
}
