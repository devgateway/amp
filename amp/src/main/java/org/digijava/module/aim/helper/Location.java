/**
 * Location.java
 * @author Priyajith
 */

package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable
{
    
    private Long locId;
    private Long countryId;
    private String iso;
    private String country;
    private String percent; //AMP-2250
    private boolean showPercent;
    private String lat;
    private String lon;
    
    private String locationName;
    
    private List<String> ancestorLocationNames;
    private AmpCategoryValueLocations ampCVLocation;
    
    private boolean percentageBlocked   = false;
    private int levelIdx;

    public Location() {
        locId = new Long(-1);
        countryId = new Long(-1);
        country = "";
    }


    public int getLevelIdx() {
        return levelIdx;
    }

    public void setLevelIdx(int levelIdx) {
        this.levelIdx = levelIdx;
    }

    /**
     * @return Returns the country.
     */
    public String getCountry() {
        return country;
    }
    /**
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }
    /**
     * @return Returns the countryId.
     */
    public Long getCountryId() {
        return countryId;
    }
    /**
     * @param countryId The countryId to set.
     */
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    /**
     * @return Returns the locId.
     */
    public Long getLocId() {
        return locId;
    }
    /**
     * @param locId The locId to set.
     */
    public void setLocId(Long locId) {
        this.locId = locId;
    }

    @Override
    public boolean equals (Object o){
        Location l  = (Location)o;
        return ampCVLocation.equals( l.getAmpCVLocation() );
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        if (percent != null) {
            //percent = percent.replace('.', ',');
        }
        if(FormatHelper.parseDouble(percent) >0 )
            this.setShowPercent(true);
        else
            this.setShowPercent(false);

        this.percent = percent;
    }

    public String getIso() {
            return iso;
        }

    public void setIso(String iso) {
            this.iso = iso;
        }

    private void setShowPercent(boolean showPercent) {
        this.showPercent = showPercent;
    }

    public boolean getShowPercent() {
        return this.showPercent;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<String> getAncestorLocationNames() {
        return ancestorLocationNames;
    }

    public void setAncestorLocationNames(List<String> ancestorLocationNames) {
        this.ancestorLocationNames = ancestorLocationNames;
    }

    public AmpCategoryValueLocations getAmpCVLocation() {
        return ampCVLocation;
    }

    public void setAmpCVLocation(AmpCategoryValueLocations ampCVLocation) {
        this.ampCVLocation = ampCVLocation;
    }

    /**
     * @return the percentageBlocked
     */
    public boolean getPercentageBlocked() {
        return percentageBlocked;
    }

    /**
     * @param percentageBlocked the percentageBlocked to set
     */
    public void setPercentageBlocked(boolean percentageBlocked) {
        this.percentageBlocked = percentageBlocked;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
    
}
