package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.List;

/**
 * A Bean representation of a GeoJSON MultiPolygon geometry object.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * For type "MultiPolygon", the "coordinates" member must be an array of
 * Polygon coordinate arrays.
 */
public class MultiPolygonGeoJSON extends GeometryGeoJSON
{
    public List<List<List<List<Double>>>> coordinates;

    public boolean isValid( PositionValidator validator )
    {
        if (coordinates==null) return false;
        if (coordinates.size()==0) return false;

        for ( List<List<List<Double>>> polygon : coordinates )
        {
            if (polygon==null) return false;
            if (polygon.size()==0) return false;
            
            for ( List<List<Double>> linearRing : polygon )
            {
                if (linearRing==null) return false;
                if (linearRing.size()<4) return false;

                for ( List<Double> position : linearRing )
                {
                    if ( !validator.isValid(position) ) return false;
                }
                
                List<Double> first = linearRing.get(0);
                List<Double> last = linearRing.get(linearRing.size()-1);
                
                if ( !validator.isEquivalent(first,last) ) return false;
            }
        }
        
        return super.isValid(validator);
    }
}
