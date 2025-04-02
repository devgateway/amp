package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.List;

public class LineStringGeoJSON extends GeometryGeoJSON
{
    public List<List<Double>> coordinates;

    /**
     * GeoJSON Specification 1.0
     * 
     * For type "LineString", the "coordinates" member must
     * be an array of two or more positions.
     */
    public boolean isValid( PositionValidator validator )
    {
        if (coordinates==null) return false;
        if (coordinates.size()<2) return false;

        for ( List<Double> position : coordinates )
        {
            if ( !validator.isValid(position) ) return false;
        }
        return true;
    }
}
