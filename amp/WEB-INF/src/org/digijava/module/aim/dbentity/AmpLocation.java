package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.util.Output;

public class AmpLocation implements Serializable, Versionable, Cloneable
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
	private String geoCode;
	
	private Set activities;
	
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

	public Set getActivities() {
		return activities;
	}

	public void setActivities(Set activities) {
		this.activities = activities;
	}
	
	@Deprecated
	private Country dgCountry;
	@Deprecated
	private AmpRegion ampRegion;
	@Deprecated
	private AmpZone ampZone;
	@Deprecated
	private AmpWoreda ampWoreda;

	@Deprecated
	public Country getDgCountry() {
			  return dgCountry;
	}

	@Deprecated
	public void setDgCountry(Country dgCountry) {
			  this.dgCountry = dgCountry;
	}

	@Deprecated
	public AmpRegion getAmpRegion() {
			  return ampRegion;
	}

	@Deprecated
	public void setAmpRegion(AmpRegion ampRegion) {
			  this.ampRegion = ampRegion;
	}

	@Deprecated
	public AmpZone getAmpZone() {
			  return ampZone;
	}

	@Deprecated
	public void setAmpZone(AmpZone ampZone) {
			  this.ampZone = ampZone;
	}

	@Deprecated
	public AmpWoreda getAmpWoreda() {
			  return ampWoreda;
	}

	@Deprecated
	public void setAmpWoreda(AmpWoreda ampWoreda) {
			  this.ampWoreda = ampWoreda;
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
			out.getOutputs().add(new Output(null, new String[] { " ISO 3 Code:&nbsp;" }, new Object[] { this.iso3Code }));
		}
		if (this.name != null && !this.name.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Name:&nbsp;" }, new Object[] { this.name }));
		}
		if (this.description != null && !this.description.trim().equals("")) {
			out.getOutputs()
					.add(new Output(null, new String[] { " Description:&nbsp;" }, new Object[] { this.description }));
		}
		if (this.gisCoordinates != null && !this.gisCoordinates.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " GIS:&nbsp;" }, new Object[] { this.gisCoordinates }));
		}
		if (this.language != null && !this.language.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Lang:&nbsp;" }, new Object[] { this.language }));
		}
		if (this.version != null && !this.version.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Version:&nbsp;" }, new Object[] { this.version }));
		}
		if (this.country != null && !this.country.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Country:&nbsp;" }, new Object[] { this.country }));
		}
		if (this.region != null && !this.region.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Region:&nbsp;" }, new Object[] { this.region }));
		}
		if (this.zone != null && !this.zone.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Zone:&nbsp;" }, new Object[] { this.zone }));
		}
		if (this.woreda != null && !this.woreda.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Woreda:&nbsp;" }, new Object[] { this.woreda }));
		}
		if (this.location != null) {
			if (this.location.getName() != null && !this.location.getName().trim().equals("")) {
				out.getOutputs().add(
						new Output(null, new String[] { " Name:&nbsp;" }, new Object[] { this.location.getName() }));
			}
		}
		return out;
	}

	@Override
	public Object getValue() {
		return this.iso3Code + "-" + this.name + "-" + this.description + "-" + this.gisCoordinates + "-"
				+ this.language + "-" + this.version + "-" + this.country + "-" + this.region + "-" + this.zone + "-"
				+ this.woreda + (this.location != null ? this.location.getName() : "");
	}
	
	@Override
	public Object prepareMerge(AmpActivity newActivity) throws CloneNotSupportedException {
		AmpLocation aux = (AmpLocation) clone();
		if (aux.activities == null) {
			aux.activities = new HashSet();
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
}	
