package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.activity.DefaultTranslatedFieldReader;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectExporter;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;

/**
 * @author Viorel Chihai
 */
public class ResourceExporter extends ObjectExporter<AmpResource> {

    public ResourceExporter() {
        super(new DefaultTranslatedFieldReader(),
                AmpFieldsEnumerator.getEnumerator().getResourceFields());
    }
}
