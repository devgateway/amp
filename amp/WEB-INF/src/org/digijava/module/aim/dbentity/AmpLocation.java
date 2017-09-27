package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;

public class AmpLocation implements Serializable, Versionable, Cloneable, Identifiable
{
    @Interchangeable(fieldTitle="AMP Location ID", id=true)
    private Long ampLocationId ;
//  @Interchangeable(fieldTitle="ISO3 Code")
    private String iso3Code ;
//  @Interchangeable(fieldTitle="Name")
    private String name ;
//  @Interchangeable(fieldTitle="Description")
    private String description ;
//  @Interchangeable(fieldTitle="GIS Coordinates")
    private String gisCoordinates ;
//  @Interchangeable(fieldTitle="Language")
    private String language ;
//  @Interchangeable(fieldTitle="Version")
    private String version ;
//  @Interchangeable(fieldTitle="Geocode")
    private String geoCode;
    
//  @Interchangeable(fieldTitle="Activities", recursive=true)
    private Set<AmpActivityVersion> activities;
    
    @Interchangeable(fieldTitle = "Location")
    private AmpCategoryValueLocations location;
    
    @Interchangeable(fieldTitle = "Region Location")
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

    public Set<AmpActivityVersion> getActivities() {
        return activities;
    }

    public void setActivities(Set<AmpActivityVersion> activities) {
        this.activities = activities;
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
    public boolean equalsForVersioning(Object obj) {
        AmpLocation aux = (AmpLocation) obj;
        String original = " " + this.getValue();
        String copy = " " + aux.getValue();
        if (original.equals(copy)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        if (this.iso3Code != null && !this.iso3Code.trim().equals("")) {
            out.getOutputs().add(new Output(null, new String[] { "ISO 3 Code" }, new Object[] { this.iso3Code }));
        }
        if (this.name != null && !this.name.trim().equals("")) {
            out.getOutputs().add(new Output(null, new String[] { "Name" }, new Object[] { this.name }));
        }
        if (this.description != null && !this.description.trim().equals("")) {
            out.getOutputs()
                    .add(new Output(null, new String[] { "Description" }, new Object[] { this.description }));
        }
        if (this.gisCoordinates != null && !this.gisCoordinates.trim().equals("")) {
            out.getOutputs().add(new Output(null, new String[] { "GIS" }, new Object[] { this.gisCoordinates }));
        }
        if (this.language != null && !this.language.trim().equals("")) {
            out.getOutputs().add(new Output(null, new String[] { "Lang" }, new Object[] { this.language }));
        }
        if (this.version != null && !this.version.trim().equals("")) {
            out.getOutputs().add(new Output(null, new String[] { "Version" }, new Object[] { this.version }));
        }
        if (this.location != null) {
            if (this.location.getName() != null && !this.location.getName().trim().equals("")) {
                out.getOutputs().add(
                        new Output(null, new String[] { "Name" }, new Object[] { this.location.getName() }));
            }
        }
        return out;
    }

    @Override
    public Object getValue() {
        return this.iso3Code + "-" + this.name + "-" + this.description + "-" + this.gisCoordinates + "-"
                + this.language + "-" + this.version + "-"
                + (this.location != null ? this.location.getName() : "");
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpLocation aux = (AmpLocation) clone();
        if (aux.activities == null) {
            aux.activities = new HashSet<AmpActivityVersion>();
        }
        aux.activities.add(newActivity);
        // this.ampLocationId = null;
        return aux;
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
