package org.digijava.kernel.ampapi.endpoints.gpi;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * UI should have been formatting on the frontend!
 *
 * @author Octavian Ciubotaru
 */
@Deprecated
public class ApplyThousandsForEntryConverter extends StdConverter<Double, Double> {

    private static boolean inTest;

    @Override
    public Double convert(Double value) {
        if (inTest) {
            return value;
        }
        return FeaturesUtil.applyThousandsForEntry(value);
    }

    static void setInTest(boolean v) {
        inTest = v;
    }
}
