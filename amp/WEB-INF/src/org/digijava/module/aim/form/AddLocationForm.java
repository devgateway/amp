/*
 * AddLocationForm.java
 * @author Akash Sharma
 * Created on 1/02/2005
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AddLocationForm extends ValidatorForm {

		  private Collection country;
		  private Collection region;
		  private Collection zone;
		  private Collection woreda;
		  
		  private Long id;
		  private String name = null;
		  private String iso;
		  private String iso3;
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

//		  private String edLevel = null;
		  private Long categoryLevel	= null;
		  private String edAction 	= null;

		  /** 'yes'- location created, edited-II, deleted
		   *  'on'- edited-II
		   *  'off'- save button clicked in addLocation screen
		   *  'cancl'- cancel button clicked in addLocation screen
		   */
		  private String edFlag = null;
		  private String regionFlag = null;
		  private String zoneFlag = null;
		  private String woredaFlag = null;
                  private boolean countryFlag ;

                    public void setCountryFlag(boolean countryFlag) {
                        this.countryFlag = countryFlag;
                    }

                    public boolean getCountryFlag() {
                        return countryFlag;
                    }

		  public void reset(ActionMapping mapping, HttpServletRequest request) {
			  regionFlag = null;
			  zoneFlag = null;
			  woredaFlag = null;
                          countryFlag=false;
			  if (start) {
				level = "country";
				impLevelValue = new Integer(1);
				countryId = null;
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
  			 	iso = null;
  			 	iso3 = null;
			}
	  	}

		public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
			ActionErrors errors = new ActionErrors();
			if ("cancl".equalsIgnoreCase(edFlag)) {
				edFlag = null;
				name = null;
  			 	code = null;
  			 	geoCode = null;
  			 	gsLat = null;
  			 	gsLong = null;
  			 	description = null;
  			 	iso = null;
  			 	iso3 = null;
			}
			else if ("off".equalsIgnoreCase(edFlag)) {
				if (null == name || "".equals(name))
					errors.add("name", new ActionMessage("error.aim.addLocation.noName"));
				if (categoryLevel != null && categoryLevel.longValue() == 0) {
					//errors = super.validate(mapping, req);
					if (null == iso || "".equals(iso))
						errors.add("iso", new ActionMessage("error.aim.addLocation.noIso"));
					else if (!iso.matches("[a-zA-Z]+"))
						errors.add("iso", new ActionMessage("error.aim.addLocation.noAlphaIso"));
					else if (iso.length() != 2)
						errors.add("iso", new ActionMessage("error.aim.addLocation.wrongIsoLength"));
					if (null == iso3 || "".equals(iso3))
						errors.add("Iso3", new ActionMessage("error.aim.addLocation.noIso3"));
					else if (!iso3.matches("[a-zA-Z]+"))
						errors.add("iso3", new ActionMessage("error.aim.addLocation.noAlphaIso3"));
					else if (iso3.length() != 3)
						errors.add("iso3", new ActionMessage("error.aim.addLocation.wrongIso3Length"));
				}
				edFlag = null;
			}
			return (errors.isEmpty())? null : errors;
		}

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

		public String getIso() {
			return iso;
		}

		public void setIso(String iso) {
			this.iso = iso;
		}

		public String getIso3() {
			return iso3;
		}

		public void setIso3(String iso3) {
			this.iso3 = iso3;
		}

		public Long getCategoryLevel() {
			return categoryLevel;
		}

		public void setCategoryLevel(Long categoryLevel) {
			this.categoryLevel = categoryLevel;
		}
                public  int getCategoryValuesSize(){
                    int size=0;
                    Collection values=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY);
                    if(values!=null){
                        size=values.size();
                    }
                    return size;
                }
}

