package org.digijava.module.aim.form ;

import java.util.Collection ;
import java.util.ArrayList ;
import java.util.Date ;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance ;


public class AmpActivityForm extends MainProjectDetailsForm
{
	private Long id ;
	private Long ampLevelId ;
	private Long ampStatusId ;
	private Long ampSectorId ;
	private Long ampThemeId ;
	private Long ampModalityId ;
	private Long ampLocationId ;
	private Long ampOrgId ;
	private Long ampRoleId ;
	private String ampId ;
	private String objective ;
	private String description ;
	private Collection status ;
	private String language ;
	private String version ;
	private Collection sectors ;
	private Collection theme ;
	private Collection subSectors ;
	private Collection countries;
	private Collection regions;
	private Collection fundingagency ;
	private Collection reportingagency;
	private Collection implagency;
	private Collection relatedins;
	private Collection level ;
	private Collection internalIds ;
	private Collection modality ;
	private String modalityName ;
	private String modalityCode;
	private int flag;
	private int flago;
	private ArrayList sectorList;
	private ArrayList subSectorList;

	private String title ;
	private String pdescription ;
	private String dd;
	private String mm;
	private String yyyy;
	private Long ampPpId;
	private ArrayList progressList;
	private ArrayList documentList;
	private ArrayList locationList;
	private ArrayList notesList;
	private ArrayList orgList;
	private ArrayList orgFAList;
	private ArrayList orgRAList;
	private ArrayList orgIAList;
	private ArrayList orgRIList;
	
	private Long ampKmId;
	private Long ampNotesId;
	private String dname ;
	private String ddescription ;
	private String ndescription ;
	

	
	public Long getId()
	{
		return id ;	
	}

	public Long getAmpLevelId()
	{
		return ampLevelId ;	
	}

	public Long getAmpOrgId()
	{
		return ampOrgId ;	
	}

	public Long getAmpRoleId()
	{
		return ampRoleId ;	
	}

	public Long getAmpStatusId()
	{
		return ampStatusId ;	
	}

	public Long getAmpSectorId()
	{
		return ampSectorId ;	
	}

	public Long getAmpLocationId()
	{
		return ampLocationId ;	
	}

	public Long getAmpThemeId()
	{
		return ampThemeId ;	
	}
	
	public ArrayList getSectorList()
	{
		return sectorList ;	
	}

	public ArrayList getSubSectorList()
	{
		return subSectorList ;	
	}

	
	public Long getAmpModalityId()
	{
		return ampModalityId ;	
	}
	
	public String getAmpId() 
	{
		return ampId;
	}

	public String getLanguage() 
	{
		return language;
	}

	public String getDescription() 
	{
		return description;
	}

	public String getObjective() 
	{
		return objective;
	}

		public Collection getStatus() 
		{
			return status;
		}

		public Collection getTheme() 
		{
			return theme;
		}

		public String getVersion() 
		{
			return version;
		}
	
		public Collection getLevel() 
		{
			return level;
		}

		public int getFlag() 
		{
			return flag;
		}

		public int getFlago() 
		{
			return flago;
		}

		public Collection getModality() {
			return modality;
		}

		public String getModalityName() {
			return modalityName;
		}

		public Collection getSectors() 
		{
			return sectors;
		}

		public Collection getSubSectors() 
		{
			return subSectors;
		}

		public Collection getCountries() 
		{
			return countries;
		}
	
		public Collection getRegions() 
		{
			return regions;
		}

		public Collection getFundingagency()
		{
			return fundingagency ;
		}

		public Collection getImplagency()
		{
			return implagency;
		}

		public Collection getRelatedins()
		{
			return relatedins;
		}
	
		public Collection getInternalIds() {
			return internalIds;
		}

		public String getModalityCode() {
			return modalityCode;
		}

		public Collection getReportingagency()
		{
			return reportingagency;
		}
	
		
	public void setDescription(String description) 
	{
		this.description = description ;
	}


		public void setId(Long id) 
		{
			this.id = id ;
		}

		public void setAmpLevelId(Long ampLevelId) 
		{
			this.ampLevelId = ampLevelId ;
		}

		public void setAmpOrgId(Long ampOrgId) 
		{
			this.ampOrgId = ampOrgId ;
		}

		public void setAmpRoleId(Long ampRoleId) 
		{
			this.ampRoleId = ampRoleId ;
		}

		public void setAmpThemeId(Long ampThemeId) 
		{
			this.ampThemeId = ampThemeId ;
		}
	
		public void setAmpModalityId(Long ampModalityId) 
		{
			this.ampModalityId = ampModalityId ;
		}

		public void setAmpLocationId(Long ampLocationId) 
		{
			this.ampLocationId = ampLocationId ;
		}
		
		public void setAmpStatusId(Long ampStatusId) 
		{
			this.ampStatusId = ampStatusId ;
		}

		public void setAmpSectorId(Long ampSectorId) 
		{
			this.ampSectorId = ampSectorId ;
		}

		public void setSectorList(ArrayList sectorList) 
		{
			this.sectorList = sectorList ;
		}

		public void setSubSectorList(ArrayList subSectorList) 
		{
			this.subSectorList = subSectorList ;
		}

		public void setLanguage(String language) 
		{
			this.language = language;
		}

		public void setObjective(String objective) 
		{
			this.objective = objective ;
		}

		public void setStatus(Collection status) 
		{
			this.status = status ;
		}

		public void setVersion(String version) 
		{
			this.version = version ;
		}

		
		public void setSectors(Collection sectors) 
		{	
			this.sectors = sectors ;
		}

		public void setTheme(Collection theme) 
		{	
			this.theme = theme ;
		}
		
		public void setSubSectors(Collection collection) 
		{
			subSectors = collection;
		}
		
		public void setCountries(Collection countries) 
		{
			this.countries = countries;
		}
		
		public void setRegions(Collection regions) 
		{
			this.regions = regions;
		}
		
	
		public void setFundingagency(Collection fundingagency)
		{
			this.fundingagency = fundingagency ;
		}

		public void setReportingagency(Collection reportingagency)
		{
			this.reportingagency= reportingagency;
		}
		
		
		public void setImplagency(Collection implagency)
		{
			this.implagency= implagency;
		}
		
		public void setAmpId(String string) 
		{
			ampId = string;
		}

		public void setRelatedins(Collection relatedins)
		{
			this.relatedins= relatedins;
		}

		public void setLevel(Collection level)
		{
			this.level = level;
		}

		public void setFlago(int flago)
		{
			this.flago = flago;
		}

		public void setFlag(int flag)
		{
			this.flag = flag;
		}
		
		public void setInternalIds(Collection collection) {
			internalIds = collection;
		}

		

		public void setModality(Collection string) {
			modality = string;
		}

		public void setModalityName(String string) {
			modalityName = string;
		}

				
		public void setModalityCode(String string) {
			modalityCode = string;
		}



/* ------------------------------------------*/
	public Long getAmpPpId()
	{
		return ampPpId ;	
	}

	public Long getAmpKmId()
	{
		return ampKmId ;	
	}

	public Long getAmpNotesId()
	{
		return ampNotesId ;	
	}

	
	public String getPdescription() 
	{
		return pdescription;
	}

	public String getTitle() 
	{
		return title;
	}

	public String getDd() 
	{
		return dd;
	}
	public String getMm() 
	{
		return mm;
	}
	public String getYyyy() 
	{
		return yyyy;
	}

	public ArrayList getProgressList()
	{
		return progressList ;	
	}

	public ArrayList getLocationList()
	{
		return locationList ;	
	}

	public ArrayList getNotesList()
	{
		return notesList ;	
	}

	public ArrayList getOrgList()
	{
		return orgList ;	
	}

	public ArrayList getOrgFAList()
	{
		return orgFAList ;	
	}

	public ArrayList getOrgRAList()
	{
		return orgRAList ;	
	}

	public ArrayList getOrgIAList()
	{
		return orgIAList ;	
	}

	public ArrayList getOrgRIList()
	{
		return orgRIList ;	
	}

	public ArrayList getDocumentList()
	{
		return documentList ;	
	}

	public void setAmpPpId(Long ampPpId) 
	{
		this.ampPpId = ampPpId ;
	}

	public void setAmpKmId(Long ampKmId) 
	{
		this.ampKmId = ampKmId ;
	}

	public void setAmpNotesId(Long ampNotesId) 
	{
		this.ampNotesId = ampNotesId ;
	}

	public void setTitle(String title) 
	{
		this.title = title ;
	}
	

	public void setPdescription(String pdescription) 
	{
		this.pdescription = pdescription ;
	}

	public void setDd(String dd) 
	{
		this.dd = dd ;
	}

	public void setMm(String mm) 
	{
		this.mm = mm ;
	}

	public void setYyyy(String yyyy) 
	{
		this.yyyy = yyyy ;
	}

	public void setDocumentList(ArrayList documentList) 
	{
		this.documentList = documentList ;
	}

	public void setNotesList(ArrayList notesList) 
	{
		this.notesList = notesList ;
	}

	public void setOrgList(ArrayList orgList) 
	{
		this.orgList = orgList ;
	}

	public void setOrgRAList(ArrayList orgRAList) 
	{
		this.orgRAList = orgRAList ;
	}

	public void setOrgFAList(ArrayList orgFAList) 
	{
		this.orgFAList = orgFAList ;
	}

	public void setOrgIAList(ArrayList orgIAList) 
	{
		this.orgIAList = orgIAList ;
	}

	public void setOrgRIList(ArrayList orgRIList) 
	{
		this.orgRIList = orgRIList ;
	}

	public void setProgressList(ArrayList progressList) 
	{
		this.progressList = progressList ;
	}

	public void setLocationList(ArrayList locationList) 
	{
		this.locationList = locationList ;
	}

	/* -----------------------------------------*/

	public String getDname() 
	{
		return dname;
	}

	public void setDname(String dname) 
	{
		this.dname = dname ;
	}

	public String getDdescription() 
	{
		return ddescription;
	}

	public String getNdescription() 
	{
		return ndescription;
	}

	public void setDdescription(String ddescription) 
	{
		this.ddescription = ddescription ;
	}

	public void setNdescription(String ndescription) 
	{
		this.ndescription = ndescription ;
	}	
	
	public static class SectorList {
        private Long ampSectorId;
		private String name;
		public Long getAmpSectorId()
		{
			return ampSectorId ;	
		}
		
		public String getName()
		{
			return name ;	
		}
		
		public void setAmpSectorId(Long ampSectorId) 
		{
			this.ampSectorId = ampSectorId ;
		}

		public void setName(String name) 
		{
			this.name = name;
		}
	}

	public static class SubSectorList {
        private Long ampSubSectorId;
		private String name;
		public Long getAmpSubSectorId()
		{
			return ampSubSectorId ;	
		}
		
		public String getName()
		{
			return name ;	
		}
		
		public void setAmpSubSectorId(Long ampSubSectorId) 
		{
			this.ampSubSectorId = ampSubSectorId ;
		}

		public void setName(String name) 
		{
			this.name = name;
		}
	}

	public static class NotesList {
		private Long ampNotesId;
        private String description;

		public Long getAmpNotesId()
		{
			return ampNotesId ;	
		}

		public String getDescription()
		{
			return description ;	
		}  
		
		public void setDescription(String description) 
		{
			this.description = description ;
		}

		public void setAmpNotesId(Long ampNotesId) 
		{
			this.ampNotesId = ampNotesId ;
		}
	}

	public static class DocumentList {
		private Long ampKmId;
        private String name;
		private String description;
		
		public Long getAmpKmId()
		{
			return ampKmId ;	
		}

		public String getDescription()
		{
			return description ;	
		}  

		public String getName()
		{
			return name ;	
		}  
		
		public void setDescription(String description) 
		{
			this.description = description ;
		}

		public void setAmpKmId(Long ampKmId) 
		{
			this.ampKmId = ampKmId ;
		}

		public void setName(String name) 
		{
			this.name = name ;
		}
	}

	public static class OrgList {
        private Long ampOrgId;
		private Long ampRoleId;
		public Long getAmpOrgId()
		{
			return ampOrgId ;	
		}  

		public Long getAmpRoleId()
		{
			return ampRoleId ;	
		}  
		
		public void setAmpOrgId(Long ampOrgId) 
		{
			this.ampOrgId = ampOrgId ;
		}

		public void setAmpRoleId(Long ampRoleId) 
		{
			this.ampRoleId = ampRoleId ;
		}
	}

	public static class ProgressList {
		private Long ampPpId;
        private String title;
		private String description;
		private Date date;

		public String getTitle()
		{
			return title ;	
		}
		
		public Long getAmpPpId()
		{
			return ampPpId ;	
		}

		public String getDescription()
		{
			return description ;	
		}  

		public Date getDate()
		{
			return date ;	
		}  
		
		public void setTitle(String title) 
		{
			this.title = title ;
		}

		public void setAmpPpId(Long ampPpId) 
		{
			this.ampPpId = ampPpId ;
		}

		public void setDescription(String description) 
		{
			this.description = description ;
		}

		public void setDate(Date date) 
		{
			this.date = date ;
		}
	}

	
	public static class LocationList {
        private Long ampLocationId;
		public Long getAmpLocationId()
		{
			return ampLocationId ;	
		}  
		
		public void setAmpLocationId(Long ampLocationId) 
		{
			this.ampLocationId = ampLocationId ;
		}
	}


}
