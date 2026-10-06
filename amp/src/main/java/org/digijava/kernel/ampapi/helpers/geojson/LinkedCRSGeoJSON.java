package org.digijava.kernel.ampapi.helpers.geojson;


/**
 * A Bean representation of a GeoJSON Linked CRS object object.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * A CRS object may link to CRS parameters on the Web. In this case, the value
 * of its "type" member must be the string "link", and the value of its
 * "properties" member must be a Link object.
 */
public class LinkedCRSGeoJSON extends CRSGeoJSON
{
    public LinkedCRSPropertiesGeoJSON properties;

    public boolean isValid( PositionValidator validator )
    {
        return properties.isValid(validator);
    }
}
