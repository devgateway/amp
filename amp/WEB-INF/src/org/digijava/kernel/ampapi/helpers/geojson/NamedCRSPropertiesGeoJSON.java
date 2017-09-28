package org.digijava.kernel.ampapi.helpers.geojson;

/**
 * A Bean representation of a GeoJSON Named CRS Properties object.
 * 
 * From the GeoJSON Specification version 1.0:
 *
 * The value of that "name" member must be a string identifying a coordinate
 * reference system. OGC CRS URNs such as "urn:ogc:def:crs:OGC:1.3:CRS84" shall
 * be preferred over legacy identifiers such as "EPSG:4326"
 */
public class NamedCRSPropertiesGeoJSON implements Validation
{
    public String name;

    public boolean isValid(PositionValidator validator)
    {
        return name!=null;
    }
}
