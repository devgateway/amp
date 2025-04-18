package org.digijava.kernel.ampapi.helpers.geojson;

/**
 * A Bean representation of a GeoJSON Linked CRS Properties object.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * A link object has one required member: "href", and one optional
 * member: "type".
 * 
 * The value of the required "href" member must be a dereferenceable URI.
 * 
 * The value of the optional "type" member must be a string that hints at the
 * format used to represent CRS parameters at the provided URI. Suggested
 * values are: "proj4", "ogcwkt", "esriwkt", but others can be used.
 */
public class LinkedCRSPropertiesGeoJSON implements Validation
{
    public String href;
    public String type;

    public boolean isValid(PositionValidator validator)
    {
        return href!=null;
    }
}
