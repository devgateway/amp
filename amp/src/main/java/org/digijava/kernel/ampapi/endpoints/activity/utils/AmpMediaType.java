package org.digijava.kernel.ampapi.endpoints.activity.utils;

import javax.ws.rs.core.MediaType;

/**
 * @author Octavian Ciubotaru
 */
public final class AmpMediaType {

    public static final String POSSIBLE_VALUES_V2_JSON = "application/vnd.possible-values-v2+json";

    public static final MediaType POSSIBLE_VALUES_V2_JSON_TYPE =
            MediaType.valueOf(AmpMediaType.POSSIBLE_VALUES_V2_JSON);

    private AmpMediaType() {
    }
}
