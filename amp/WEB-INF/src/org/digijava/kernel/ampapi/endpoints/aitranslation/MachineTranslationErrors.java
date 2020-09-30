package org.digijava.kernel.ampapi.endpoints.aitranslation;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_MACHINE_TRANSLATION_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public final class MachineTranslationErrors {

    public static final ApiErrorMessage NOT_ENABLED =
            new ApiErrorMessage(ERROR_CLASS_MACHINE_TRANSLATION_ID, 1, "Machine Translation is not enabled.");

    private MachineTranslationErrors() {
    }
}
