package org.dgfoundation.amp.nireports.amp.dimensions;

import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

import java.util.Arrays;

/**
 * @author Octavian Ciubotaru
 */
public class IndicatorConnectionDimension extends SqlSourcedNiDimension {

    public final static IndicatorConnectionDimension instance = new IndicatorConnectionDimension("indicators");

    public final static int LEVEL_INDICATOR = 0;
    public final static int LEVEL_INDICATOR_CONN = 1;

    private IndicatorConnectionDimension(String name) {
        super(name, "v_ni_all_indicator_connection_dimension", Arrays.asList("indicator_id", "indicator_connection_id"));
    }
}
