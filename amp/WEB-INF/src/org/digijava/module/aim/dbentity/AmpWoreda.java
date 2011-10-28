
/**
  * AmpWoreda.java
  * @author Priyajith
  * Created: 17-Dec-2004
  */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.kernel.dbentity.Country;
/**
 * 
 * Was replaced by AmpCategoryValueLocation
 */
@Deprecated
public class AmpWoreda implements Serializable{
	private Long ampWoredaId;
	private String name;
	private String description;
	private String gsLat;
	private String gsLong;
	private String geoCode;
	private String woredaCode;
	private Country country;
	private AmpZone zone;
  private Set locations;
  /**
	 * @return Returns the ampWoredaId.
	 */
	public Long getAmpWoredaId() {
		return ampWoredaId;
	}
	/**
	 * @param ampWoredaId The ampWoredaId to set.
	 */
	public void setAmpWoredaId(Long ampWoredaId) {
		this.ampWoredaId = ampWoredaId;
	}
	/**
	 * @return Returns the country.
	 */
	public Country getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(Country country) {
		this.country = country;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the geoCode.
	 */
	public String getGeoCode() {
		return geoCode;
	}
	/**
	 * @param geoCode The geoCode to set.
	 */
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}
	/**
	 * @return Returns the gsLat.
	 */
	public String getGsLat() {
		return gsLat;
	}
	/**
	 * @param gsLat The gsLat to set.
	 */
	public void setGsLat(String gsLat) {
		this.gsLat = gsLat;
	}
	/**
	 * @return Returns the gsLong.
	 */
	public String getGsLong() {
		return gsLong;
	}
	/**
	 * @param gsLong The gsLong to set.
	 */
	public void setGsLong(String gsLong) {
		this.gsLong = gsLong;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the zone.
	 */
	public AmpZone getZone() {
		return zone;
	}
	/**
	 * @param zone The zone to set.
	 */
	public void setZone(AmpZone zone) {
		this.zone = zone;
	}
	/**
	 * @return
	 */
	public String getWoredaCode() {
		return woredaCode;
	}

  public Set getLocations() {
    return locations;
  }

  /**
	 * @param string
	 */
	public void setWoredaCode(String string) {
		woredaCode = string;
	}

  public void setLocations(Set locations) {
    this.locations = locations;
  }

}
