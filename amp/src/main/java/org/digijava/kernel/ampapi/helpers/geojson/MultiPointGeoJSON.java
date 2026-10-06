package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.List;

/**
 * A Bean representation of a GeoJSON MultiPoint geometry object.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * For type "MultiPoint", the "coordinates" member must
 * be an array of positions.
 */
public class MultiPointGeoJSON extends GeometryGeoJSON
{
    public List<List<Double>> coordinates;
    
    public boolean isValid( PositionValidator validator )
    {
        if (coordinates==null) return false;
        if (coordinates.size()<1) return false;

        for ( List<Double> position : coordinates )
        {
            if ( !validator.isValid(position) ) return false;
        }
        return super.isValid(validator);
    }
}
