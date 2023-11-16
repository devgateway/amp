package org.digijava.kernel.ampapi.endpoints.aitranslation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
@ApiModel("Represents a long running operation")
public final class TranslationOperation {

    @ApiModelProperty("If the value is false, it means that the operation is still in progress. If value is true,"
            + " then either error or result value is present.")
    private final boolean done;

    @ApiModelProperty("The error result of the operation in case of failure or cancellation.")
    private final String error;

    @ApiModelProperty("The normal response of the operation in case of success.")
    private final Map<String, String> result;

    public static TranslationOperation inProgress() {
        return new TranslationOperation(false, null, null);
    }

    public static TranslationOperation withError(String error) {
        return new TranslationOperation(true, error, null);
    }

    public static TranslationOperation withResult(Map<String, String> result) {
        return new TranslationOperation(true, null, result);
    }

    private TranslationOperation(boolean done, String error, Map<String, String> result) {
        this.done = done;
        this.error = error;
        this.result = result;
    }

    public boolean isDone() {
        return done;
    }

    public String getError() {
        return error;
    }

    public Map<String, String> getResult() {
        return result;
    }
}
