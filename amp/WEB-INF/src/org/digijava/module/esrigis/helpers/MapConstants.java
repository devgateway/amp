package org.digijava.module.esrigis.helpers;

import java.util.HashMap;


public class MapConstants {
    //This corresponds to the column maptype in table amp_map_config
    public final static class MapType {
        public final static Integer BASE_MAP = 1;
        public final static Integer MAIN_MAP = 2;
        public final static Integer GEOMETRY_SERVICE = 4;
        public final static Integer ARCGIS_API = 5;
        public final static Integer GEOLOCATOR_SERVICE = 7;
        public final static Integer BASEMAPS_ROOT = 8;
        public final static Integer NATIONAL_LAYER = 9;
        public final static Integer INDICATOR_LAYER = 10;
        public static final Integer WMS_LAYER = 11;
    }
    public final static class MapSubType {
        public final static Integer BASE = 1;
        public final static Integer INDICATOR = 2;
        public final static Integer OSM = 3;
    }   

    public final static HashMap<Integer, String> mapTypeNames = new HashMap<Integer, String>();
    static {
        mapTypeNames.put(MapType.BASE_MAP, "Base Map");
        mapTypeNames.put(MapType.MAIN_MAP, "Main Map");
        mapTypeNames.put(MapType.GEOMETRY_SERVICE, "Geometry Service");
        mapTypeNames.put(MapType.ARCGIS_API, "Javascript API");
        mapTypeNames.put(MapType.GEOLOCATOR_SERVICE, "Geolocator Service");
        mapTypeNames.put(MapType.BASEMAPS_ROOT, "Base Map Root path");
        mapTypeNames.put(MapType.NATIONAL_LAYER, "National Layer");
        mapTypeNames.put(MapType.INDICATOR_LAYER, "Indicator Layers");
        mapTypeNames.put(MapType.WMS_LAYER, "wms");
    }
    
    public final static HashMap<Integer, String> mapSubTypeNames = new HashMap<Integer, String>();
    static {
        mapSubTypeNames.put(MapSubType.BASE, "Base Map");
        mapSubTypeNames.put(MapSubType.INDICATOR, "Indicator Layer");
        mapSubTypeNames.put(MapSubType.OSM, "Open Street Map");
    }

}
