package org.digijava.kernel.ampapi.helpers.geojson;


/**
 * A Bean representation of a GeoJSON Named CRS object.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * In this case, the value of its "type" member must be the string "name".
 * The value of its "properties" member must be an object containing a "name"
 * member.
 *
 */
public class NamedCRSGeoJSON extends CRSGeoJSON
{
    public NamedCRSPropertiesGeoJSON properties;

    public boolean isValid( PositionValidator validator )
    {
        return properties.isValid(validator);
    }
}
