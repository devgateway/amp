/**
 * Location.java
 * @author Priyajith
 */

package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class Location implements Serializable
{
    
    private Long locId;
    private Long countryId;
    //now ID is the ISO value countryId hold the COUNTRY  ID and newCountryId hold Country ISO
    private String newCountryId;
    private String country;
    private Long regionId;
    private String region;
    private Long zoneId;
    private String zone;
    private Long woredaId;
    private String woreda;
    private String percent;//AMP-2250 
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
        regionId = new Long(-1);
        region = "";
        zoneId = new Long(-1);
        zone = "";
        woredaId = new Long(-1);
        woreda = "";
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
    /**
     * @return Returns the region.
     */
    @Deprecated
    public String getRegion() {
        return region;
    }
    /**
     * @param region The region to set.
     */
    @Deprecated
    public void setRegion(String region) {
        this.region = region;
    }
    /**
     * @return Returns the regionId.
     */
    @Deprecated
    public Long getRegionId() {
        return regionId;
    }
    /**
     * @param regionId The regionId to set.
     */
    @Deprecated
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    /**
     * @return Returns the woreda.
     */
    @Deprecated
    public String getWoreda() {
        return woreda;
    }
    /**
     * @param woreda The woreda to set.
     */
    @Deprecated
    public void setWoreda(String woreda) {
        this.woreda = woreda;
    }
    /**
     * @return Returns the woredaId.
     */
    @Deprecated
    public Long getWoredaId() {
        return woredaId;
    }
    /**
     * @param woredaId The woredaId to set.
     */
    @Deprecated
    public void setWoredaId(Long woredaId) {
        this.woredaId = woredaId;
    }
    /**
     * @return Returns the zone.
     */
    @Deprecated
    public String getZone() {
        return zone;
    }
    /**
     * @param zone The zone to set.
     */
    @Deprecated
    public void setZone(String zone) {
        this.zone = zone;
    }
    /**
     * @return Returns the zoneId.
     */
    @Deprecated
    public Long getZoneId() {
        return zoneId;
    }
    /**
     * @param zoneId The zoneId to set.
     */
    @Deprecated
    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

//    public boolean equals(Object obj) {
//      if (obj == null)
//          return false;
//      if (!(obj instanceof Location))
//          throw new ClassCastException();
//
//      Location loc = (Location) obj;
//      boolean equal = false;
//
//      if (((loc.getCountryId() == null && countryId == null) || (loc
//              .getCountryId().equals(countryId)))
//              && ((loc.getRegionId() == null && regionId == null) || (loc
//                      .getRegionId().equals(regionId)))
//              && ((loc.getZoneId() == null && zoneId == null) || (loc
//                      .getZoneId().equals(zoneId)))
//              && ((loc.getWoredaId() == null && woredaId == null) || (loc
//                      .getWoredaId().equals(woredaId)))) {
//          equal = true;
//      }
//
//      return equal;
//
//  }

//  public int compareTo(Object arg0) {
//      // TODO Auto-generated method stub
//      Location l = (Location)arg0;
//      String lcntry = (l.getCountry() == null) ? "" : l.getCountry();
//      String lregion = (l.getRegion() == null) ? "" : l.getRegion();
//      String lzone = (l.getZone() == null) ? "" : l.getZone();
//      String lworeda = (l.getWoreda() == null) ? "" : l.getWoreda();
//
//      if(country != null)
//          if(country.compareTo(lcntry)!=0) return country.compareTo(lcntry);
//          else
//                if (region != null)
//                    if(region.compareTo(lregion)!=0) return region.compareTo(lregion);
//                    else 
//                        if (zone!=null)
//                            if(zone.compareTo(lzone)!=0) return zone.compareTo(lzone);
//                            else 
//                                if (woreda != null)
//                                    if(woreda.compareTo(lworeda)!=0) return woreda.compareTo(lworeda);
//                                    else return 0;
//                                else return 0;
//                        else return 0;  
//                else return 0; 
//      else return -1;         
//  }
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

    public String getNewCountryId() {
            return newCountryId;
        }

    public void setNewCountryId(String newCountryId) {
            this.newCountryId = newCountryId;
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

    public String getRegionName() {
        AmpCategoryValueLocations ampCVRegion = DynLocationManagerUtil.getAncestorByLayer(
                ampCVLocation, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
        
        if ( ampCVRegion != null )
            return ampCVRegion.getName();
        else
            return null;
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
