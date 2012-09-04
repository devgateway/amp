package org.digijava.module.aim.helper;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ProposedProjCost;

public class Activity
    implements Comparable {

  private Long activityId;
  private String name;
  private String donors;
  private String ampId;
  private Integer budget;
  private Boolean humanitarianAid;
  	private AmpTeam team;
  
    //montenegro mission:
	private String projectImpact=null;
	private String activitySummary=null;
	private String contractingArrangements=null;
	private String condSeq=null;
	private String linkedActivities=null;
	private String conditionality=null;
	private String projectManagement=null;
		
	  
  
  private String description;
  private String objective;
  private String purpose;
  private String results;
  private boolean objMore;
  private String status;
  private String statusReason;
  private Collection sectors;
  private Collection projectIds;
  private Collection locations;
  private Collection impLevel;
  private Collection impLocation;
  private Collection relOrgs;
  private String origAppDate;
  private String revAppDate;
  private String origStartDate;
  private String revStartDate;
  private String currCompDate;
  private String propCompDate;
  private String contractingDate;
  private String disbursmentsDate;

  private Collection revCompDates;
  private Collection assistanceType;
  private Collection modalities;
  private Set uniqueModalities;
  private String modality;
  private String modalityCode;
  private Collection actPrograms;
  private String program;
  private String programDescription;
  private String contFirstName;
  private String contLastName;
  private String email;
  private String dnrCntTitle;
  private String dnrCntOrganization;
  private String dnrCntPhoneNumber;
  private String dnrCntFaxNumber;

  private String mfdContFirstName;
  private String mfdContLastName;
  private String mfdContEmail;
  private String mfdCntTitle;
  private String mfdCntOrganization;
  private String mfdCntPhoneNumber;
  private String mfdCntFaxNumber;
  
  private String prjCoFirstName;
  private String prjCoLastName;
  private String prjCoEmail;
  private String prjCoTitle;
  private String prjCoOrganization;
  private String prjCoPhoneNumber;
  private String prjCoFaxNumber;
	
  private String secMiCntFirstName;
  private String secMiCntLastName;
  private String secMiCntEmail;
  private String secMiCntTitle;
  private String secMiCntOrganization;
  private String secMiCntPhoneNumber;
  private String secMiCntFaxNumber;

  private String actAthFirstName;
  private String actAthLastName;
  private String actAthEmail;
  private String actAthAgencySource;
  private String createdDate;
  private ProposedProjCost propProjCost;
  private String contractors;
  private Date updatedDate;
  private AmpTeamMember modifiedBy;

  private String acChapter;
  private String accessionInstrument;

  private String FY;
  private String vote;
  private String subVote;
  private String subProgram;
  private String projectCode;
  private String financialInstrument;
  private String financialInstrumentString;
  private Boolean governmentApprovalProcedures;
  private Boolean jointCriteria;
  
  private String crisNumber;
  
  private String approvalStatus;
  private Boolean draft;
  
  private AmpTeamMember approvedBy;
  private Date approvalDate;
  
  private String lessonsLearned;

  private AmpTeamMember createdBy;
  
  private String projectCategory;

  public String getAccessionInstrument() {
    return accessionInstrument;
  }

  public void setAccessionInstrument(String accessionInstrument) {
    this.accessionInstrument = accessionInstrument;
  }

  public String getAcChapter() {
    return acChapter;
  }

  public void setAcChapter(String acChapter) {
    this.acChapter = acChapter;
  }



  public AmpTeamMember getModifiedBy() {
	return modifiedBy;
}

public void setModifiedBy(AmpTeamMember modifiedBy) {
	this.modifiedBy = modifiedBy;
}

public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  /**
   * @return Returns the programDescription.
   */
  public String getProgramDescription() {
    return programDescription;
  }

  /**
   * @param programDescription The programDescription to set.
   */
  public void setProgramDescription(String programDescription) {
    this.programDescription = programDescription;
  }

  public Activity() {
    activityId = null;
    name = null;
    donors = null;
  }

  public void setActivityId(Long actId) {
    activityId = actId;
  }

  public void setName(String nam) {
    name = nam;
  }

  public void setDonors(String don) {
    donors = don;
  }

  public Long getActivityId() {
    return activityId;
  }

  public String getName() {
    return name;
  }

  public String getDonors() {
    return donors;
  }

  public int compareTo(Object o) {
    if (! (o instanceof Activity))
      throw new ClassCastException();

    Activity act = (Activity) o;
    if (this.name != null) {
      if (act.name != null) {
        return (this.name.trim().toLowerCase().compareTo(act.name.trim()
            .toLowerCase()));
      }
      else {
        return (this.name.trim().toLowerCase().compareTo(""));
      }

    }
    else {
      if (act.name != null) {
        return ("".compareTo(act.name.trim().toLowerCase()));
      }
      else {
        return (0);
      }
    }
  }

  /**
   * @return Returns the assistanceType.
   */
  public Collection getAssistanceType() {
    return assistanceType;
  }

  /**
   * @param assistanceType The assistanceType to set.
   */
  public void setAssistanceType(Collection assistanceType) {
    this.assistanceType = assistanceType;
  }

  /**
   * @return Returns the currCompDate.
   */
  public String getCurrCompDate() {
    return currCompDate;
  }

  /**
   * @param currCompDate The currCompDate to set.
   */
  public void setCurrCompDate(String currCompDate) {
    this.currCompDate = currCompDate;
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


  public Collection getImpLevel() {
	return impLevel;
}

public void setImpLevel(Collection impLevel) {
	this.impLevel = impLevel;
}

/**
   * @return Returns the locations.
   */
  public Collection getLocations() {
    return locations;
  }

  /**
   * @param locations The locations to set.
   */
  public void setLocations(Collection locations) {
    this.locations = locations;
  }

  /**
   * @return Returns the objective.
   */
  public String getObjective() {
    return objective;
  }

  /**
   * @param objective The objective to set.
   */
  public void setObjective(String objective) {
    this.objective = objective;
  }

  /**
   * @return Returns the origAppDate.
   */
  public String getOrigAppDate() {
    return origAppDate;
  }

  /**
   * @param origAppDate The origAppDate to set.
   */
  public void setOrigAppDate(String origAppDate) {
    this.origAppDate = origAppDate;
  }

  /**
   * @return Returns the origStartDate.
   */
  public String getOrigStartDate() {
    return origStartDate;
  }

  /**
   * @param origStartDate The origStartDate to set.
   */
  public void setOrigStartDate(String origStartDate) {
    this.origStartDate = origStartDate;
  }

  /**
   * @return Returns the projectIds.
   */
  public Collection getProjectIds() {
    return projectIds;
  }

  /**
   * @param projectIds The projectIds to set.
   */
  public void setProjectIds(Collection projectIds) {
    this.projectIds = projectIds;
  }

  /**
   * @return Returns the relOrgs.
   */
  public Collection getRelOrgs() {
    return relOrgs;
  }

  /**
   * @param relOrgs The relOrgs to set.
   */
  public void setRelOrgs(Collection relOrgs) {
    this.relOrgs = relOrgs;
  }

  /**
   * @return Returns the revAppDate.
   */
  public String getRevAppDate() {
    return revAppDate;
  }

  /**
   * @param revAppDate The revAppDate to set.
   */
  public void setRevAppDate(String revAppDate) {
    this.revAppDate = revAppDate;
  }

  /**
   * @return Returns the revStartDate.
   */
  public String getRevStartDate() {
    return revStartDate;
  }

  /**
   * @param revStartDate The revStartDate to set.
   */
  public void setRevStartDate(String revStartDate) {
    this.revStartDate = revStartDate;
  }

  /**
   * @return Returns the sectors.
   */
  public Collection getSectors() {
    return sectors;
  }

  /**
   * @param sectors The sectors to set.
   */
  public void setSectors(Collection sectors) {
    this.sectors = sectors;
  }

  /**
   * @return Returns the status.
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status The status to set.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return Returns the statusReason.
   */
  public String getStatusReason() {
    return statusReason;
  }

  /**
   * @param statusReason The statusReason to set.
   */
  public void setStatusReason(String statusReason) {
    this.statusReason = statusReason;
  }

  /**
   * @return Returns the modality.
   */
  public String getModality() {
    return modality;
  }

  /**
   * @param modality The modality to set.
   */
  public void setModality(String modality) {
    this.modality = modality;
  }

  /**
   * @return Returns the modalityCode.
   */
  public String getModalityCode() {
    return modalityCode;
  }

  /**
   * @param modalityCode The modalityCode to set.
   */
  public void setModalityCode(String modalityCode) {
    this.modalityCode = modalityCode;
  }

  /**
   * @return Returns the objMore.
   */
  public boolean isObjMore() {
    return objMore;
  }

  /**
   * @param objMore The objMore to set.
   */
  public void setObjMore(boolean objMore) {
    this.objMore = objMore;
  }

  /**
   * @return Returns the program.
   */
  public String getProgram() {
    return program;
  }

  /**
   * @param program The program to set.
   */
  public void setProgram(String program) {
    this.program = program;
  }

  /**
   * @return Returns the modalities.
   */
  public Collection getModalities() {
    return modalities;
  }

  /**
   * @param modalities The modalities to set.
   */
  public void setModalities(Collection modalities) {
    this.modalities = modalities;
  }

  /**
   * @return Returns the contFirstName.
   */
  public String getContFirstName() {
    return contFirstName;
  }

  /**
   * @param contFirstName The contFirstName to set.
   */
  public void setContFirstName(String contFirstName) {
    this.contFirstName = contFirstName;
  }

  /**
   * @return Returns the contLastName.
   */
  public String getContLastName() {
    return contLastName;
  }

  /**
   * @param contLastName The contLastName to set.
   */
  public void setContLastName(String contLastName) {
    this.contLastName = contLastName;
  }

  /**
   * @return Returns the email.
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email The email to set.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return Returns the contractors.
   */
  public String getContractors() {
    return contractors;
  }

  /**
   * @param contractors The contractors to set.
   */
  public void setContractors(String contractors) {
    this.contractors = contractors;
  }

  /**
   * @return Returns the actAthEmail.
   */
  public String getActAthEmail() {
    return actAthEmail;
  }

  /**
   * @param actAthEmail The actAthEmail to set.
   */
  public void setActAthEmail(String actAthEmail) {
    this.actAthEmail = actAthEmail;
  }

  /**
   * @return Returns the actAthFirstName.
   */
  public String getActAthFirstName() {
    return actAthFirstName;
  }

  /**
   * @param actAthFirstName The actAthFirstName to set.
   */
  public void setActAthFirstName(String actAthFirstName) {
    this.actAthFirstName = actAthFirstName;
  }

  /**
   * @return Returns the actAthLastName.
   */
  public String getActAthLastName() {
    return actAthLastName;
  }

  /**
   * @param actAthLastName The actAthLastName to set.
   */
  public void setActAthLastName(String actAthLastName) {
    this.actAthLastName = actAthLastName;
  }

  /**
   * @return Returns the mfdContEmail.
   */
  public String getMfdContEmail() {
    return mfdContEmail;
  }

  /**
   * @param mfdContEmail The mfdContEmail to set.
   */
  public void setMfdContEmail(String mfdContEmail) {
    this.mfdContEmail = mfdContEmail;
  }

  /**
   * @return Returns the mfdContFirstName.
   */
  public String getMfdContFirstName() {
    return mfdContFirstName;
  }

  /**
   * @param mfdContFirstName The mfdContFirstName to set.
   */
  public void setMfdContFirstName(String mfdContFirstName) {
    this.mfdContFirstName = mfdContFirstName;
  }

  /**
   * @return Returns the mfdContLastName.
   */
  public String getMfdContLastName() {
    return mfdContLastName;
  }

  /**
   * @param mfdContLastName The mfdContLastName to set.
   */
  public void setMfdContLastName(String mfdContLastName) {
    this.mfdContLastName = mfdContLastName;
  }

  /**
   * @return Returns the createdDate.
   */
  public String getCreatedDate() {
    return createdDate;
  }

  /**
   * @param createdDate The createdDate to set.
   */
  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public boolean equals(Object obj) {
    if (obj == null)
      throw new NullPointerException();

    if (! (obj instanceof Activity))
      throw new ClassCastException();

    Activity act = (Activity) obj;
    return this.activityId.equals(act.getActivityId());
  }

  /**
   * @return Returns the ampId.
   */
  public String getAmpId() {
    return ampId;
  }

  /**
   * @param ampId The ampId to set.
   */
  public void setAmpId(String ampId) {
    this.ampId = ampId;
  }

  /**
   * @return Returns the revCompDates.
   */
  public Collection getRevCompDates() {
    return revCompDates;
  }

  public ProposedProjCost getPropProjCost() {
    return propProjCost;
  }

  public Collection getActPrograms() {
    return actPrograms;
  }

  /**
   * @param revCompDates The revCompDates to set.
   */
  public void setRevCompDates(Collection revCompDates) {
    this.revCompDates = revCompDates;
  }

  public void setPropProjCost(ProposedProjCost propProjCost) {
    this.propProjCost = propProjCost;
  }

  public Set getUniqueModalities() {
    return uniqueModalities;
  }

  public void setUniqueModalities(Set uniqueModalities) {
    this.uniqueModalities = uniqueModalities;
  }

  public Integer getBudget() {
    return budget;
  }

  public void setBudget(Integer budget) {
    this.budget = budget;
  }

  public String getActAthAgencySource() {
    return actAthAgencySource;
  }

  public void setActAthAgencySource(String actAthAgencySource) {
    this.actAthAgencySource = actAthAgencySource;
  }

  public String getContractingDate() {
    return contractingDate;
  }

  public void setContractingDate(String contractingDate) {
    this.contractingDate = contractingDate;
  }

  public String getDisbursmentsDate() {
    return disbursmentsDate;
  }

  public void setDisbursmentsDate(String disbursmentsDate) {
    this.disbursmentsDate = disbursmentsDate;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public String getResults() {
    return results;
  }

  public void setResults(String results) {
    this.results = results;
  }

  public void setActPrograms(Collection actPrograms) {
    this.actPrograms = actPrograms;
  }

  public String getDnrCntFaxNumber() {
    return dnrCntFaxNumber;
  }

  public void setDnrCntFaxNumber(String dnrCntFaxNumber) {
    this.dnrCntFaxNumber = dnrCntFaxNumber;
  }

  public String getDnrCntOrganization() {
    return dnrCntOrganization;
  }

  public void setDnrCntOrganization(String dnrCntOrganization) {
    this.dnrCntOrganization = dnrCntOrganization;
  }

  public String getDnrCntPhoneNumber() {
    return dnrCntPhoneNumber;
  }

  public void setDnrCntPhoneNumber(String dnrCntPhoneNumber) {
    this.dnrCntPhoneNumber = dnrCntPhoneNumber;
  }

  public String getDnrCntTitle() {
    return dnrCntTitle;
  }

  public void setDnrCntTitle(String dnrCntTitle) {
    this.dnrCntTitle = dnrCntTitle;
  }

  public String getMfdCntFaxNumber() {
    return mfdCntFaxNumber;
  }

  public void setMfdCntFaxNumber(String mfdCntFaxNumber) {
    this.mfdCntFaxNumber = mfdCntFaxNumber;
  }

  public String getMfdCntOrganization() {
    return mfdCntOrganization;
  }

  public void setMfdCntOrganization(String mfdCntOrganization) {
    this.mfdCntOrganization = mfdCntOrganization;
  }

  public String getMfdCntPhoneNumber() {
    return mfdCntPhoneNumber;
  }

  public void setMfdCntPhoneNumber(String mfdCntPhoneNumber) {
    this.mfdCntPhoneNumber = mfdCntPhoneNumber;
  }

  public String getMfdCntTitle() {
    return mfdCntTitle;
  }

  public void setMfdCntTitle(String mfdCntTitle) {
    this.mfdCntTitle = mfdCntTitle;
  }

  public String getFinancialInstrument() {
    return financialInstrument;
  }

  public void setFinancialInstrument(String financialInformation) {
	this.financialInstrument=financialInformation;
  }

  public String getFY() {
    return FY;
  }

  public void setFY(String fy) {
    FY = fy;
  }

  public Boolean getGovernmentApprovalProcedures() {
    return governmentApprovalProcedures;
  }

  public void setGovernmentApprovalProcedures(Boolean
                                              governmentApprovalProcedures) {
    this.governmentApprovalProcedures = governmentApprovalProcedures;
  }

  public Boolean getJointCriteria() {
    return jointCriteria;
  }

  public void setJointCriteria(Boolean jointCriteria) {
    this.jointCriteria = jointCriteria;
  }

  public String getProjectCode() {
    return projectCode;
  }

  public void setProjectCode(String projectCode) {
    this.projectCode = projectCode;
  }

  public String getSubProgram() {
    return subProgram;
  }

  public void setSubProgram(String subProgram) {
    this.subProgram = subProgram;
  }

  public String getSubVote() {
    return subVote;
  }

  public void setSubVote(String subVote) {
    this.subVote = subVote;
  }

  public String getVote() {
    return vote;
  }

  public void setVote(String vote) {
    this.vote = vote;
  }

  public String getFinancialInstrumentString() {
    return financialInstrumentString;
  }

  public AmpTeamMember getCreatedBy() {
    return createdBy;
  }

  public void setFinancialInstrumentString(String financialInstrumentString) {
    this.financialInstrumentString = financialInstrumentString;
  }

  public void setCreatedBy(AmpTeamMember createdBy) {
    this.createdBy = createdBy;
  }
  
  	

	public String getApprovalStatus() {
		return approvalStatus;
	}
	
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Boolean getDraft() {
		return draft;
	}
	
	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public String getPropCompDate() {
		return propCompDate;
	}

	public void setPropCompDate(String propCompDate) {
		this.propCompDate = propCompDate;
	}

	public String getLessonsLearned() {
		return lessonsLearned;
	}

	public void setLessonsLearned(String lessonsLearned) {
		this.lessonsLearned = lessonsLearned;
	}

	public String getProjectImpact() {
		return projectImpact;
	}

	public void setProjectImpact(String projectImpact) {
		this.projectImpact = projectImpact;
	}

	public String getActivitySummary() {
		return activitySummary;
	}

	public void setActivitySummary(String activitySummary) {
		this.activitySummary = activitySummary;
	}

	public String getContractingArrangements() {
		return contractingArrangements;
	}

	public void setContractingArrangements(String contractingArrangements) {
		this.contractingArrangements = contractingArrangements;
	}

	public String getCondSeq() {
		return condSeq;
	}

	public void setCondSeq(String condSeq) {
		this.condSeq = condSeq;
	}

	public String getLinkedActivities() {
		return linkedActivities;
	}

	public void setLinkedActivities(String linkedActivities) {
		this.linkedActivities = linkedActivities;
	}

	public String getConditionality() {
		return conditionality;
	}

	public void setConditionality(String conditionality) {
		this.conditionality = conditionality;
	}

	public String getProjectManagement() {
		return projectManagement;
	}

	public void setProjectManagement(String projectManagement) {
		this.projectManagement = projectManagement;
	}

	public String getPrjCoFirstName() {
		return prjCoFirstName;
	}

	public void setPrjCoFirstName(String prjCoFirstName) {
		this.prjCoFirstName = prjCoFirstName;
	}

	public String getPrjCoLastName() {
		return prjCoLastName;
	}

	public void setPrjCoLastName(String prjCoLastName) {
		this.prjCoLastName = prjCoLastName;
	}

	public String getPrjCoEmail() {
		return prjCoEmail;
	}

	public void setPrjCoEmail(String prjCoEmail) {
		this.prjCoEmail = prjCoEmail;
	}

	public String getPrjCoTitle() {
		return prjCoTitle;
	}

	public void setPrjCoTitle(String prjCoTitle) {
		this.prjCoTitle = prjCoTitle;
	}

	public String getPrjCoOrganization() {
		return prjCoOrganization;
	}

	public void setPrjCoOrganization(String prjCoOrganization) {
		this.prjCoOrganization = prjCoOrganization;
	}

	public String getPrjCoPhoneNumber() {
		return prjCoPhoneNumber;
	}

	public void setPrjCoPhoneNumber(String prjCoPhoneNumber) {
		this.prjCoPhoneNumber = prjCoPhoneNumber;
	}

	public String getPrjCoFaxNumber() {
		return prjCoFaxNumber;
	}

	public void setPrjCoFaxNumber(String prjCoFaxNumber) {
		this.prjCoFaxNumber = prjCoFaxNumber;
	}

	public String getSecMiCntFirstName() {
		return secMiCntFirstName;
	}

	public void setSecMiCntFirstName(String secMiCntFirstName) {
		this.secMiCntFirstName = secMiCntFirstName;
	}

	public String getSecMiCntLastName() {
		return secMiCntLastName;
	}

	public void setSecMiCntLastName(String secMiCntLastName) {
		this.secMiCntLastName = secMiCntLastName;
	}

	public String getSecMiCntEmail() {
		return secMiCntEmail;
	}

	public void setSecMiCntEmail(String secMiCntEmail) {
		this.secMiCntEmail = secMiCntEmail;
	}

	public String getSecMiCntTitle() {
		return secMiCntTitle;
	}

	public void setSecMiCntTitle(String secMiCntTitle) {
		this.secMiCntTitle = secMiCntTitle;
	}

	public String getSecMiCntOrganization() {
		return secMiCntOrganization;
	}

	public void setSecMiCntOrganization(String secMiCntOrganization) {
		this.secMiCntOrganization = secMiCntOrganization;
	}

	public String getSecMiCntPhoneNumber() {
		return secMiCntPhoneNumber;
	}

	public void setSecMiCntPhoneNumber(String secMiCntPhoneNumber) {
		this.secMiCntPhoneNumber = secMiCntPhoneNumber;
	}

	public String getSecMiCntFaxNumber() {
		return secMiCntFaxNumber;
	}

	public void setSecMiCntFaxNumber(String secMiCntFaxNumber) {
		this.secMiCntFaxNumber = secMiCntFaxNumber;
	}

	public AmpTeam getTeam() {
		return team;
	}

	public void setTeam(AmpTeam team) {
		this.team = team;
	}

	public Boolean isHumanitarianAid() {
		return humanitarianAid;
	}

	public void setHumanitarianAid(Boolean humanitarianAid) {
		this.humanitarianAid = humanitarianAid;
	}

	public AmpTeamMember getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(AmpTeamMember approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	public Boolean getHumanitarianAid() {
		return humanitarianAid;
	}

	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}

	public Collection getImpLocation() {
		return impLocation;
	}

	public void setImpLocation(Collection impLocation) {
		this.impLocation = impLocation;
	}

	public String getCrisNumber() {
		return crisNumber;
	}

	public void setCrisNumber(String crisNumber) {
		this.crisNumber = crisNumber;
	} 
}
