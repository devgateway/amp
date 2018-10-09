package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeDependencyResolver;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.util.Output;

/**
 * Connection between Activity and Location.
 * This class initially was added to add percentage for Bolivia. AMP-2250
 * @author Irakli Kobiashvili
 *
 */
public class AmpActivityLocation extends AbstractAuditLogger implements Versionable, Serializable, Cloneable {
    //IATI-check: should be exported.
//  @Interchangeable(fieldTitle="ID", id=true)
    private Long id;
    @Interchangeable(fieldTitle = "ID", pickIdOnly=true)
    private AmpActivityVersion activity;
    @Interchangeable(fieldTitle = "Location", pickIdOnly = true, importable = true, uniqueConstraint=true, required = ActivityEPConstants.REQUIRED_ALWAYS, 
            dependencies={
                            InterchangeDependencyResolver.IMPLEMENTATION_LEVEL_PRESENT_KEY,
                            InterchangeDependencyResolver.IMPLEMENTATION_LEVEL_VALID_KEY,
                            InterchangeDependencyResolver.IMPLEMENTATION_LOCATION_PRESENT_KEY
            })
    private AmpLocation location;
    @Interchangeable(fieldTitle="Location Percentage", fmPath = "/Activity Form/Location/Locations/Location Item/locationPercentage", 
            required = "/Activity Form/Location/Locations/Location required validator", percentageConstraint = true, importable = true)
    @Validators (percentage = "/Activity Form/Location/Locations/locationPercentageTotal" )
    private Float locationPercentage;
    @Interchangeable(fieldTitle = "Latitude", fmPath = "/Activity Form/Location/Locations/Location Item/Latitude", importable = true)
    private String latitude;
    @Interchangeable(fieldTitle = "Longitude", fmPath = "/Activity Form/Location/Locations/Location Item/Logitude", importable = true)
    private String longitude;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AmpActivityVersion getActivity() {
        return activity;
    }
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    public AmpLocation getLocation() {
        return location;
    }
    public void setLocation(AmpLocation location) {
        this.location = location;
    }
    public Float getLocationPercentage() {
        return locationPercentage;
    }
    public void setLocationPercentage(Float locationPercentage) {
        this.locationPercentage = locationPercentage;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivityLocation aux = (AmpActivityLocation) obj;
        if (this.location.equalsForVersioning(aux.getLocation())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Output getOutput() {
        Output out = this.location.getOutput();
        out.getOutputs().add(
                new Output(null, new String[] { "Percentage" },
                        new Object[] { this.locationPercentage != null ? this.locationPercentage : new Float(0) }));
        out.getOutputs().add(
                new Output(null, new String[] { "Latitude"},
                        new Object[] { this.latitude != null ? this.latitude : "" }));
        out.getOutputs().add(
                new Output(null, new String[] { "Longitude"},
                        new Object[] { this.longitude != null ? this.longitude : "" }));
        return out;
    }

    @Override
    public Object getValue() {
        String ret = "";
        ret = ret + "-Percentage:" + (this.locationPercentage != null ? this.locationPercentage : new Integer(0));
        ret = ret + "-Latitude:" + (this.latitude != null ? this.latitude.trim() : "");
        ret = ret + "-Longitude:" + (this.longitude != null ? this.longitude.trim() : "");
        return ret;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpActivityLocation aux = (AmpActivityLocation) clone(); 
        aux.activity = newActivity;
        this.setPreviousObjectId(aux.id);
        aux.id = null;
        return aux;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
