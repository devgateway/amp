package org.digijava.kernel.ampapi.endpoints.gis.services;

/**
 * POJO class holding info for geodata (acvl) expanded from geocode 
 * @author acartaleanu
 *
 */
public class GeoDataSkeleton {
    
    public final String geoCode;
    public final String locationName;
    public final String latitude;
    public final String longitude;
    
    public GeoDataSkeleton(String geoCode, String locationName, String latitude, String longitude) {
        this.locationName = locationName;
        this.geoCode = geoCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
