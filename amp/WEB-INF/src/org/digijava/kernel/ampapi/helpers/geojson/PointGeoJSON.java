package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


/**
 * A Bean representation of a GeoJSON Polygon geometry object.
 * 
 * From the GeoJSON Specification version 1.0:
 *
 * For type "Point", the "coordinates" member must be a single position.
 */
@JsonPropertyOrder({"type","coordinates","properties"})
public class PointGeoJSON extends GeometryGeoJSON
{
	public List<Double> coordinates;
	public Map<String,JsonNode> properties;
	public PointGeoJSON() {
		super();
		this.coordinates = new ArrayList<Double>();
		this.properties =  new HashMap<String, JsonNode>();
	}

	@Override
	public boolean isValid( PositionValidator validator )
	{
		return validator.isValid(coordinates) && super.isValid(validator);
	}
}
