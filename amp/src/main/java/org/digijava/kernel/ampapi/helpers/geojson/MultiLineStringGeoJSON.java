package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.List;

/**
 * A Bean representation of a GeoJSON MultiLineString geometry object.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * For type "MultiLineString", the "coordinates" member must
 * be an array of LineString coordinate arrays.
 * 
 * For type "LineString", the "coordinates" member must
 * be an array of two or more positions.
 */
public class MultiLineStringGeoJSON extends GeometryGeoJSON
{
    public List<List<List<Double>>> coordinates;

    @Override
    public boolean isValid( PositionValidator validator )
    {
        if (coordinates==null) return false;
        if (coordinates.size()==0) return false;
        
        for ( List<List<Double>> lineString : coordinates )
        {
            if (lineString==null) return false;
            if (lineString.size()<2) return false;

            for ( List<Double> position : lineString )
            {
                if ( !validator.isValid(position) ) return false;
            }
        }

        return super.isValid(validator);
    }
}
