package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectExporter;

/**
 * @author Viorel Chihai
 */
public class ResourceExporter extends ObjectExporter<AmpResource> {

    public ResourceExporter() {
        super(AmpFieldsEnumerator.getPublicEnumerator().getResourceFields());
    }
}
