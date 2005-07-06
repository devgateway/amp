/*
 * AddLocationForm.java
 * @author Akash Sharma
 * Created on 1/02/2005
 */
package org.digijava.module.aim.form;

import org.apache.struts.action.*;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

public class AddLocationForm extends ActionForm {

		  private Collection country;
		  private Collection region;
		  private Collection zone;
		  private Collection woreda;
		  
		  private Long id;
		  private String name = null;
		  private String code;
		  private String description;
		  private String gsLat;
		  private String gsLong;
		  private String geoCode;
		  private String countryId;
		  private Long regionId;
		  private Long zoneId;
		  private Long woredaId;
		  
		  private String level = null;
		  private Integer impLevelValue;
		  private boolean start;
		  
		  private String edLevel = null;
		  private String edAction = null;
		  private String edFlag = null;
		  private String regionFlag = null;
		  private String zoneFlag = null;
		  private String woredaFlag = null;
		  		  
		  public String getRegionFlag() {
			return regionFlag;
		  }

		  public void setRegionFlag(String regionFlag) {
			this.regionFlag = regionFlag;
		  }
		  
		  public String getZoneFlag() {
			return zoneFlag;
		  }

		  public void setZoneFlag(String zoneFlag) {
			this.zoneFlag = zoneFlag;
		  }
		  
		  public String getWoredaFlag() {
			return woredaFlag;
		  }

		  public void setWoredaFlag(String woredaFlag) {
			this.woredaFlag = woredaFlag;
		  }
		  
		  public String getEdFlag() {
			return edFlag;
		  }

		  public void setEdFlag(String edFlag) {
			this.edFlag = edFlag;
		  }
		  
		  public String getEdLevel() {
			return edLevel;
		  }

		  public void setEdLevel(String edLevel) {
			this.edLevel = edLevel;
		  }
		  
		  public String getEdAction() {
			return edAction;
		  }

		  public void setEdAction(String edAction) {
			this.edAction = edAction;
		  }
		  
		  public String getLevel() {
			return level;
		  }

		  public void setLevel(String level) {
			this.level = level;
		  }
		  
		  public boolean isStart() {
			return start;
		  }

		  public void setStart(boolean start) {
			this.start = start;
		  }
		  		  
		  public Integer getImpLevelValue() {
			return impLevelValue;
		  }

		  public void setImpLevelValue(Integer impLevelValue) {
			this.impLevelValue = impLevelValue;
		  }
		  		  
		  public Collection getCountry() {
					 return (this.country);
		  }

		  public void setCountry(Collection country) {
					 this.country = country;
		  }
		  
		  public Collection getRegion() {
			 return region;
		  }

		  public void setRegion(Collection region) {
			 this.region = region;
		  }
		  
		  public Collection getZone() {
			 return zone;
		  }

		  public void setZone(Collection zone) {
			 this.zone = zone;
		  }
		  
		  public Collection getWoreda() {
			 return woreda;
		  }

		  public void setWoreda(Collection woreda) {
			 this.woreda = woreda;
		  }
		  
		  public Long getId() {
			 return id;
 }

		  public void setId(Long id) {
			 this.id = id;
		  }

		  public String getName() {
			 return name;
		  }

		  public void setName(String name) {
		  	this.name = name;
		  }
		  
		  public String getCountryId() {
			 return countryId;
		  }

		  public void setCountryId(String countryId) {
			 this.countryId = countryId;
		  }
		  
		  public String getCode() {
			 return code;
		  }

		  public void setCode(String code) {
		  	this.code = code; 
		  }
		  
		  public String getDescription() {
			 return description;
		  }

		  public void setDescription(String description) {
			 this.description = description;
		  }
		  
		  public String getGsLat() {
			 return gsLat;
		  }

		  public void setGsLat(String gsLat) {
		  	this.gsLat = gsLat;
		  }
		  
		  public String getGsLong() {
			 return gsLong;
		  }

		  public void setGsLong(String gsLong) {
		  		this.gsLong = gsLong;
		  }
		  
		  public String getGeoCode() {
			 return geoCode;
		  }

		  public void setGeoCode(String geoCode) {
		  	this.geoCode = geoCode;
		  }
		  
		  public Long getRegionId() {
			 return regionId;
		  }

		  public void setRegionId(Long regionId) {
			 this.regionId = regionId;
		  }
		  
		  public Long getZoneId() {
			 return zoneId;
		  }

		  public void setZoneId(Long zoneId) {
			 this.zoneId = zoneId;
		  }
		  
		  public Long getWoredaId() {
			 return woredaId;
		  }

		  public void setWoredaId(Long woredaId) {
			 this.woredaId = woredaId;
		  }
		  
		  public void reset(ActionMapping mapping, HttpServletRequest request) {
		  	regionFlag = null;
		  	zoneFlag = null;
		  	woredaFlag = null;
		  	
			if (start) {
				level = "country";
				impLevelValue = new Integer(1);
				countryId = "";
				regionId = new Long(-1);
				zoneId = new Long(-1);
				woredaId = new Long(-1);
				region = null;
				zone = null;
				woreda = null;
			}
			if (edFlag != null && edFlag.equals("yes")) {
  			 	name = null;
  			 	code = null;
  			 	geoCode = null;
  			 	gsLat = null;
  			 	gsLong = null;
  			 	description = null;
			}
		  }
}

