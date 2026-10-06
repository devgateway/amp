package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.List;

/**
 * A Bean representation of a GeoJSON Geometry Collection.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * A geometry collection must have a member with the name "geometries". The
 * value corresponding to "geometries" is an array. Each element in this array
 * is a GeoJSON geometry object.
 */
public class GeometryCollectionGeoJSON extends GeoJSON
{
    public List<GeometryGeoJSON> geometries;

    @Override
    public boolean isValid( PositionValidator validator )
    {
        for ( GeometryGeoJSON geom : geometries )
        {
            if ( !geom.isValid(validator) ) return false;
        }
        return super.isValid(validator);
    }
}
