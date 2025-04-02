package org.digijava.kernel.ampapi.endpoints.util;

import org.dgfoundation.amp.ar.MeasureConstants;

import java.util.LinkedHashSet;
import java.util.Set;

public final  class PublicConstants {

    private PublicConstants() {

    }
    /**
     * Set of measures that can be used in Public Portal module as funding type options.
     */
    public static final Set<String> FUNDING_TYPES = new LinkedHashSet<String>() {{
        add(MeasureConstants.ACTUAL_COMMITMENTS);
        add(MeasureConstants.ACTUAL_DISBURSEMENTS);
        add(MeasureConstants.PLEDGES_ACTUAL_PLEDGE);
    }};
}
