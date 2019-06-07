package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TestValueConverter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;

/**
 * A simple wrapped for testing when import result generation is not relevant
 *
 * @author Nadejda Mandrescu
 */
public class ObjectImporterAnyType extends ObjectImporter<Object> {

    public ObjectImporterAnyType(InputValidatorProcessor formatValidator,
            InputValidatorProcessor businessRulesValidator, TranslationSettings plainEnglish, APIField apiField,
            TestValueConverter valueConverter) {
        super(formatValidator, businessRulesValidator, plainEnglish, apiField, valueConverter);
    }

    @Override
    public Object getImportResult() {
        return new Object();
    }

}
