package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.*;

public class AddSectorForm extends ActionForm {
		  
	private Long sectorId = null;  
	private Long parentSectorId = null;
	private String parentSectorName = null;
	private String sectorCode = null;
	private String sectorName = null;
	private String type = null;
	private Long ampOrganisationId = null;
	private String ampOrganisation = null;
	private String description = null;
	private HashMap organisationList = null;
	private String flag = null;
	
	public Long getSectorId() {
		return (this.sectorId);
	}
	
	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}
	
	public String getSectorCode() {
		return (this.sectorCode); 
	}
	
	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
		
	}
	
	public String getSectorName() {
		return (this.sectorName);
	}
	
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName; 
	}
	
	public String getType() {
		return (this.type);
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Long getAmpOrganisationId() {
		return (this.ampOrganisationId);
	}
	
	public void setAmpOrganisationId(Long ampOrganisationId) {
		this.ampOrganisationId = ampOrganisationId;
	}
	
	public String getDescription() {
		return (this.description);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getParentSectorId() {
		return (this.parentSectorId);
	}

	public void setParentSectorId(Long parentSectorId) {
		this.parentSectorId = parentSectorId;	  
	}

	public HashMap getOrganisationList() {
		return (this.organisationList);
	}

	public void setOrganisationList(HashMap organisationList) {
		this.organisationList = organisationList;
	}

	public String getAmpOrganisation() {
		return (this.ampOrganisation);
	}

	public void setAmpOrganisation(String ampOrganisation) {
		this.ampOrganisation = ampOrganisation;
	}

	public String getFlag() {
		return (this.flag);
	}

	public void setFlag(String flag)  {
		this.flag = flag;
	}

	public String getParentSectorName() {
			  return (this.parentSectorName);
	}

	public void setParentSectorName(String parentSectorName) {
			  this.parentSectorName = parentSectorName;
	}
}
