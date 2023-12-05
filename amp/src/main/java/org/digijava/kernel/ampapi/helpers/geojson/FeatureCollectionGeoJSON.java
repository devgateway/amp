package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.ArrayList;
import java.util.List;

/**
 * A Bean representation of a GeoJSON Feature Collection.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * The value corresponding to "features" is an array.
 * Each element in the array is a feature object as
 * defined above.
 */
public class FeatureCollectionGeoJSON extends GeoJSON
{
    public List<FeatureGeoJSON> features;
    
    public FeatureCollectionGeoJSON() {
        super();
        this.features = new ArrayList<>();
    }

    public boolean isValid( PositionValidator validator )
    {
        if ( features==null ) return false;
        if ( features.size()==0 ) return false;

        for ( FeatureGeoJSON feature : features )
        {
            if ( !feature.isValid(validator) ) return false;
        }

        return super.isValid(validator);
    }
}
