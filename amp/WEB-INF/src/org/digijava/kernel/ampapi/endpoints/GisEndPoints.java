package org.digijava.kernel.ampapi.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.util.FilterParam;
import org.digijava.kernel.ampapi.endpoints.util.FiltersParams;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureCollectionGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;

import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Class that holds entrypoing for GIS api methods
 * 
 * @author ddimunzio@developmentgateway.org jdeanquin@developmentgateway.org
 * 
 */
@Path("gis")
public class GisEndPoints {
    /**
     * Returns Aggregate ADM info by ADM Level
     * 
     * @param filter
     *            adminLevel to filter, the json should look like {
     *            "FiltersParams":
     *            {"params":[{"filterName":"adminLevel","filterValue"
     *            :["Region"]}] } }
     * 
     *            Available regions
     * @return
     */
    @GET
    @Path("/cluster")
    @Produces(MediaType.APPLICATION_JSON)
    public final FeatureCollectionGeoJSON getClusteredPointsByAdm(
            @QueryParam("filter") @DefaultValue("{\"FiltersParams\":{\"params\":[{\"filterName\":\"adminLevel\",\"filterValue\":[\"Region\"]}]}}") final FiltersParams filter) {
        // TODO we should validate that the param is actually valid
        if (filter.getParams().size() != 1) {
            // we should only receive one filter
        } else {
            FilterParam f = filter.getParams().get(0);
            if (!f.getFilterName().equals("adminLevel")) {
                //we should receive adminLevel filter
            }else{
                if(f.getFilterValue()==null || f.getFilterValue().size()!=1){
                    //we shouldn't receive more than one value
                }else{
                    if(!QueryUtil.getAdminLevels().contains(f.getFilterValue().get(0)) ){
                        //the value should be a valid one 
                    }
                }
            }

        }

        List<ClusteredPoints> c = QueryUtil.getClusteredPoints(filter
                .getParams().get(0).getFilterValue().get(0));
        FeatureCollectionGeoJSON result = new FeatureCollectionGeoJSON();
        for (ClusteredPoints clusteredPoints : c) {
            result.features.add(getPoint(new Double(clusteredPoints.getLat()),
                    new Double(clusteredPoints.getLon()),
                    clusteredPoints.getActivityids(),
                    clusteredPoints.getAdmin()));
        }

        return result;
    }

    private FeatureGeoJSON getPoint(Double lat, Double lon,
            List<Long> activityid, String adm) {
        FeatureGeoJSON fgj = new FeatureGeoJSON();
        PointGeoJSON pg = new PointGeoJSON();
        pg.coordinates.add(lat);
        pg.coordinates.add(lon);
        pg.properties.put("activityid",
                new TextNode(Util.toCSStringForIN(activityid)));
        pg.properties.put("adm", new TextNode(adm));

        fgj.geometry = pg;
        return fgj;
    }

}
