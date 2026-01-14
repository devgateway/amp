package org.digijava.module.esrigis.helpers;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;

/**
 * IMMUTABLE class holding the digest of an AmpActivityLocation instance
 * @author Dolghier Constantin
 *
 */
public class ActivityLocationDigest
{
    public final Long ampActivityId;
    public final AmpCategoryValueLocations acvl;
    public final String aalLatitude;
    public final String aalLongitude;
    public final Double aalPercentage;
    public ActivityLocationDigest(Long ampActivityId, AmpCategoryValueLocations acvl, String aalLatitude, String aalLongitude, Double aalPercentage)
    {
        this.ampActivityId = ampActivityId;
        this.acvl = acvl;
        this.aalLatitude = aalLatitude;
        this.aalLongitude = aalLongitude;
        this.aalPercentage = aalPercentage;
    }       
}
