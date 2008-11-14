package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;

import org.digijava.kernel.dbentity.Country;

public class AmpLocation implements Serializable
{
	private Long ampLocationId ;
	private String iso3Code ;
	private String name ;
	private String description ;
	private String gisCoordinates ;
	private String language ;
	private String version ;
	private String country ;
	private String region ;
	private String zone;
	private String woreda;
	private Set aidlocation;
	
	private Set activities;

	public Set getActivities() {
		return activities;
	}

	public void setActivities(Set activities) {
		this.activities = activities;
	}

	private Country dgCountry; 
	private AmpRegion ampRegion;
	private AmpZone ampZone;
	private AmpWoreda ampWoreda;

	public Country getDgCountry() {
			  return dgCountry;
	}

	public void setDgCountry(Country dgCountry) {
			  this.dgCountry = dgCountry;
	}

	public AmpRegion getAmpRegion() {
			  return ampRegion;
	}

	public void setAmpRegion(AmpRegion ampRegion) {
			  this.ampRegion = ampRegion;
	}

	public AmpZone getAmpZone() {
			  return ampZone;
	}

	public void setAmpZone(AmpZone ampZone) {
			  this.ampZone = ampZone;
	}

	public AmpWoreda getAmpWoreda() {
			  return ampWoreda;
	}

	public void setAmpWoreda(AmpWoreda ampWoreda) {
			  this.ampWoreda = ampWoreda;
	}
	
	/**
	 * @return
	 */
	public Set getAidlocation() {
		return aidlocation;
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
	public String getCountry() {
		return country;
	}
	
	

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getWoreda() {
		return woreda;
	}

	public void setWoreda(String woreda) {
		this.woreda = woreda;
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
	public String getRegion() {
		return region;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param set
	 */
	public void setAidlocation(Set set) {
		aidlocation = set;
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
	public void setCountry(String string) {
		country = string;
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
	public void setRegion(String string) {
		region = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}
	
	public String toString() {
		String ret=new String();
		if(country!=null) ret+=country;
		if(region!=null) ret+="-"+region;
		if(name!=null) ret+=" - "+name;		
		return ret;
	}

}	
