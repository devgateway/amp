package org.digijava.kernel.ampapi.endpoints.postgis;

import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureCollectionGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.ampapi.postgis.util.ScoreCalculator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("postgis")
@Api("postgis")
public class PostgisEndPoints {

    private static final Logger logger = Logger.getLogger(PostgisEndPoints.class);
    private static final int MAX_DISTANCE_METERS = 5 * 1000;

    @GET
    @Path("/location/{locationName}/{includeCloseBy}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Retrieve and provide GeoJSON Feature Collection of locations by name.")
    public FeatureCollectionGeoJSON locations(@PathParam("locationName") String locationName,
            @ApiParam("indicates if the close locations are included")
            @PathParam ("includeCloseBy") Boolean includeCloseBy) {
        List<AmpLocator> locations = QueryUtil.getLocationsFromKeyword(locationName.toLowerCase());
        List <Long> idLists = QueryUtil.getIdsList(locations);
        FeatureCollectionGeoJSON featureCollection = new FeatureCollectionGeoJSON();
        for (AmpLocator locator : locations) {
            double score = ScoreCalculator.getScore(locator.getAnglicizedName(), locator.getAnglicizedName(), locator.getDistance());
            featureCollection.features.add(PostgisUtil.getLocation(Double.valueOf(locator.getLatitude()),
                    Double.valueOf(locator.getLongitude()), locator.getName(), score));
            if (includeCloseBy && locator.getDistance() == 0) {
                List <AmpLocator> closeByLocations = QueryUtil.getLocationsWithinDistance(locator.getTheGeometry(), MAX_DISTANCE_METERS,idLists);
                for (AmpLocator closeBy : closeByLocations) {
                    FeatureGeoJSON json = PostgisUtil.getLocation(Double.valueOf(closeBy.getLatitude()),
                            Double.valueOf(closeBy.getLongitude()), closeBy.getName(), 75);
                    json.properties.put("isCloseBy", new TextNode("true"));
                    featureCollection.features.add(json);
                }
                
            }
        }
        Collections.sort(featureCollection.features, new Comparator<FeatureGeoJSON>() {
            @Override
            public int compare(FeatureGeoJSON o1, FeatureGeoJSON o2) {
                Double score1 = o1.properties.get("score").asDouble();
                Double score2 = o1.properties.get("score").asDouble();
                return score1.compareTo(score2);
            }
        });
        return featureCollection;
    }


}
