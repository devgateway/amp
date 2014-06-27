package org.digijava.kernel.ampapi.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.helpers.geojson.FeatureCollectionGeoJSON;

@Path("gis")
public class GisEndPoints {

	@GET
	@Path("/cluster")
	@Produces(MediaType.APPLICATION_JSON)
	public final FeatureCollectionGeoJSON getClusteredPointsByAdm(){
		return null;
		
	}
}
