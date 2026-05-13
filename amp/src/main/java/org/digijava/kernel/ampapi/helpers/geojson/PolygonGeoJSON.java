package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.List;

/**
 * A Bean representation of a GeoJSON Polygon geometry object.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * For type "Polygon", the "coordinates" member must
 * be an array of LinearRing coordinate arrays. For
 * Polygons with multiple rings, the first must be the
 * exterior ring and any others must be interior rings
 * or holes.
 * 
 * A LinearRing is closed LineString with 4 or more
 * positions. The first and last positions are equivalent
 * (they represent equivalent points).
 *
 * NOTE: The following is NOT checked.
 * For Polygons with multiple rings, the first must be the
 * exterior ring and any others must be interior rings or holes.
 */
public class PolygonGeoJSON extends GeometryGeoJSON
{
    public List<List<List<Double>>> coordinates;

    @Override
    public boolean isValid( PositionValidator validator )
    {
        if (coordinates==null) return false;
        if (coordinates.size()==0) return false;
        
        for ( List<List<Double>> linearRing : coordinates )
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

        return super.isValid(validator);
    }
}
