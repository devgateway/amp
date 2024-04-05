package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.activity.DefaultTranslatedFieldReader;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectExporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslatedFieldReader;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;
import org.digijava.kernel.services.AmpFieldsEnumerator;

import java.util.List;

/**
 * @author Viorel Chihai
 */
public class ResourceExporter extends ObjectExporter<AmpResource> {

    public ResourceExporter() {
        super(new DefaultTranslatedFieldReader(),
                AmpFieldsEnumerator.getEnumerator().getResourceFields());
    }

    public ResourceExporter(TranslatedFieldReader translatedFieldReader, List<APIField> fields) {
        super(translatedFieldReader, fields);
    }
}
