package org.digijava.kernel.ampapi.endpoints.postgis;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;

public class PostgisUtil {
    protected static final Logger logger = Logger.getLogger(PostgisUtil.class);

    public static FeatureGeoJSON getLocation(Double lat, Double lon, String name, double score) {
        FeatureGeoJSON fgj = new FeatureGeoJSON();
        PointGeoJSON pg = new PointGeoJSON();
        pg.coordinates.add(lon);
        pg.coordinates.add(lat);
        fgj.properties.put("name", new TextNode(name));
        fgj.properties.put("score", new DoubleNode(score));
        fgj.geometry = pg;
        return fgj;
    }
}
