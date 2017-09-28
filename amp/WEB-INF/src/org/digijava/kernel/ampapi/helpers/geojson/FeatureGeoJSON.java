package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;




/**
 * A Bean representation of a GeoJSON Feature.
 * 
 * From the GeoJSON Specification version 1.0:
 * 
 * A feature object must have a member with the name "geometry". The value
 * of the geometry member is a geometry object as defined above or a JSON
 * null value.
 * 
 * A feature object must have a member with the name "properties". The value
 * of the properties member is an object (any JSON object or a JSON null
 * value).
 * 
 * If a feature has a commonly used identifier, that identifier should be
 * included as a member of the feature object with the name "id".
 */
@JsonPropertyOrder({"type","geometry","properties"})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class FeatureGeoJSON extends GeoJSON
{
    public GeoJSON geometry;
    public Map<String,JsonNode> properties;
    
    public String id;
    public FeatureGeoJSON(){
        this.properties =  new HashMap<String, JsonNode>();
    }

    public boolean isValid( PositionValidator validator )
    {
        if (geometry==null ) return false;
        if ( !geometry.isValid(validator) ) return false;

        return super.isValid(validator);
    }
}
