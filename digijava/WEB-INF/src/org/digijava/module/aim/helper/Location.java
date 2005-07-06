/**
 * Location.java
 * @author Priyajith
 */

package org.digijava.module.aim.helper;

import java.io.Serializable;

public class Location implements Serializable 
{
	
	private Long locId;
	private Long countryId;
	private String country;
	private Long regionId;
	private String region;
	private Long zoneId;
	private String zone;
	private Long woredaId;
	private String woreda;
	
	public Location() {
		locId = new Long(-1);
		countryId = new Long(-1);
		country = null;
		regionId = new Long(-1);
		region = null;
		zoneId = new Long(-1);
		zone = null;
		woredaId = new Long(-1);
		woreda = null;
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
	public String getRegion() {
		return region;
	}
	/**
	 * @param region The region to set.
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return Returns the regionId.
	 */
	public Long getRegionId() {
		return regionId;
	}
	/**
	 * @param regionId The regionId to set.
	 */
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	/**
	 * @return Returns the woreda.
	 */
	public String getWoreda() {
		return woreda;
	}
	/**
	 * @param woreda The woreda to set.
	 */
	public void setWoreda(String woreda) {
		this.woreda = woreda;
	}
	/**
	 * @return Returns the woredaId.
	 */
	public Long getWoredaId() {
		return woredaId;
	}
	/**
	 * @param woredaId The woredaId to set.
	 */
	public void setWoredaId(Long woredaId) {
		this.woredaId = woredaId;
	}
	/**
	 * @return Returns the zone.
	 */
	public String getZone() {
		return zone;
	}
	/**
	 * @param zone The zone to set.
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}
	/**
	 * @return Returns the zoneId.
	 */
	public Long getZoneId() {
		return zoneId;
	}
	/**
	 * @param zoneId The zoneId to set.
	 */
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	  public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Location))
			throw new ClassCastException();

		Location loc = (Location) obj;
		boolean equal = false;

		if (((loc.getCountryId() == null && countryId == null) || (loc
				.getCountryId().equals(countryId)))
				&& ((loc.getRegionId() == null && regionId == null) || (loc
						.getRegionId().equals(regionId)))
				&& ((loc.getZoneId() == null && zoneId == null) || (loc
						.getZoneId().equals(zoneId)))
				&& ((loc.getWoredaId() == null && woredaId == null) || (loc
						.getWoredaId().equals(woredaId)))) {
			equal = true;
		}

		return equal;

	}
}
