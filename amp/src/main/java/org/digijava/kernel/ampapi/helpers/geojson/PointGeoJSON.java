package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * A Bean representation of a GeoJSON Polygon geometry object.
 * 
 * From the GeoJSON Specification version 1.0:
 *
 * For type "Point", the "coordinates" member must be a single position.
 */
@JsonPropertyOrder({"type","coordinates","properties"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointGeoJSON extends GeometryGeoJSON
{
    public List<Double> coordinates;
    public Map<String,JsonNode> properties;
    public PointGeoJSON() {
        super();
        this.coordinates = new ArrayList<Double>();
        //this.properties =  new HashMap<String, JsonNode>();
    }

    @Override
    public boolean isValid( PositionValidator validator )
    {
        return validator.isValid(coordinates) && super.isValid(validator);
    }
}
