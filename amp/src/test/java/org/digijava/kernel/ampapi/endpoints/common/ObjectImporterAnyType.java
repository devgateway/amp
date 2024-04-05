package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.persistence.InMemoryValueConverter;
import org.digijava.kernel.request.TLSUtils;

/**
 * A simple wrapped for testing when import result generation is not relevant
 *
 * @author Nadejda Mandrescu
 */
public class ObjectImporterAnyType extends ObjectImporter<Object> {

    public ObjectImporterAnyType(InputValidatorProcessor formatValidator,
            TranslationSettings plainEnglish, APIField apiField,
            InMemoryValueConverter valueConverter) {
        super(formatValidator, plainEnglish, apiField, TLSUtils.getSite(), valueConverter);
    }

    @Override
    public Object getImportResult() {
        return new Object();
    }

}
