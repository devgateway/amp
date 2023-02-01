package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.kernel.ampapi.endpoints.common.values.providers.LocationPossibleValuesProvider;
import org.digijava.kernel.validators.activity.ImplementationLevelValidator;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.Output;
import org.digijava.module.aim.util.TreeNodeAware;

/**
 * Connection between Activity and Location.
 * This class initially was added to add percentage for Bolivia. AMP-2250
 * @author Irakli Kobiashvili
 *
 */
public class AmpActivityLocation implements Versionable, Serializable, Cloneable,
        TreeNodeAware<AmpCategoryValueLocations> {

    //IATI-check: should be exported.
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    @InterchangeableBackReference
    private AmpActivityVersion activity;

    @PossibleValues(LocationPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = ActivityFieldsConstants.Locations.LOCATION, pickIdOnly = true, importable = true,
            uniqueConstraint = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class),
            dependencies={
                    ImplementationLevelValidator.IMPLEMENTATION_LEVEL_PRESENT_KEY,
                    ImplementationLevelValidator.IMPLEMENTATION_LEVEL_VALID_KEY,
                    ImplementationLevelValidator.IMPLEMENTATION_LOCATION_PRESENT_KEY
            })
    private AmpCategoryValueLocations location;

    @Interchangeable(fieldTitle = "Location Percentage",
            interValidators = @InterchangeableValidator(value = RequiredValidator.class,
                    fmPath = "/Activity Form/Location/Locations/Location percentage required"),
            fmPath = "/Activity Form/Location/Locations/Location Item/locationPercentage",
            percentageConstraint = true, importable = true)
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
    public AmpCategoryValueLocations getLocation() {
        return location;
    }
    public void setLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }
    public Float getLocationPercentage() {
        return locationPercentage;
    }
    public void setLocationPercentage(Float locationPercentage) {
        this.locationPercentage = locationPercentage;
    }

    public AmpActivityLocation() {
    }

    public AmpActivityLocation(AmpActivityVersion activity, AmpCategoryValueLocations location,
                               Float locationPercentage, String latitude, String longitude) {
        this.activity = activity;
        this.location = location;
        this.locationPercentage = locationPercentage;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivityLocation aux = (AmpActivityLocation) obj;
        return location.getId().equals(aux.getLocation().getId());
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<>());
        out.add("ISO 3 Code", location.getIso3());
        out.add("Name", location.getName());
        out.add("Description", location.getDescription());
        out.getOutputs().add(
                new Output(null, new String[] { "Percentage" },
                        new Object[] { this.locationPercentage != null ? this.locationPercentage : new Float(0) }));
        out.add("Latitude", latitude);
        out.add("Longitude", longitude);
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

    @Override
    public AmpAutoCompleteDisplayable<AmpCategoryValueLocations> getTreeNode() {
        return location;
    }
}
