package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.util.Identifiable;

public class AmpLocation implements Serializable, Cloneable, Identifiable
{
    private Long ampLocationId ;
    private String iso3Code ;
    private String name ;
    private String description ;
    private String gisCoordinates ;
    private String language ;
    private String version ;
    private String geoCode;
    
    private AmpCategoryValueLocations location;
    
    private AmpCategoryValueLocations regionLocation;
    
    public AmpCategoryValueLocations getLocation() {
        return location;
    }

    public void setLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }

    /**
     * @return the regionLocation
     */
    public AmpCategoryValueLocations getRegionLocation() {
        return regionLocation;
    }

    /**
     * @param regionLocation the regionLocation to set
     */
    public void setRegionLocation(AmpCategoryValueLocations regionLocation) {
        this.regionLocation = regionLocation;
    }

    /**
     * @return
     */
    public Long getAmpLocationId() {
        return ampLocationId;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public String getGisCoordinates() {
        return gisCoordinates;
    }

    /**
     * @return
     */
    public String getIso3Code() {
        return iso3Code;
    }

    /**
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param long1
     */
    public void setAmpLocationId(Long long1) {
        ampLocationId = long1;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setGisCoordinates(String string) {
        gisCoordinates = string;
    }

    /**
     * @param string
     */
    public void setIso3Code(String string) {
        iso3Code = string;
    }

    /**
     * @param string
     */
    public void setLanguage(String string) {
        language = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }


    /**
     * @param string
     */
    public void setVersion(String string) {
        version = string;
    }
    
    public String toString() {
        String ret=new String();
        if(name!=null) ret+=" - "+name;     
        return ret;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    @Override
    public Object getIdentifier() {
        return ampLocationId;
    }
}   
