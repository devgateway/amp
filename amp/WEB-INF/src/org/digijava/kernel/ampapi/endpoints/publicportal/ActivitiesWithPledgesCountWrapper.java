package org.digijava.kernel.ampapi.endpoints.publicportal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FIXME change public portal to read int directly
 *
 * @author Octavian Ciubotaru
 */
public class ActivitiesWithPledgesCountWrapper {

    @JsonProperty("ActivitiesWithPledgesCount")
    private final int count;

    ActivitiesWithPledgesCountWrapper(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
