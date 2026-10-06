package org.digijava.kernel.ampapi.endpoints.gis.services;

/**
 * POJO class holding info for structures expanded
 * not a map since the underlying data is a m2m (activity <-> structure) 
 * @author acartaleanu
 *
 */
public class StructuresDataSkeleton {
    
    public final Long act_id;
    public final String title;
    public final String description;
    public final String latitude;
    public final String longitude;
    
    
    public StructuresDataSkeleton(Long act_id, String title, String description, String latitude, String longitude) {
        this.act_id = act_id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
