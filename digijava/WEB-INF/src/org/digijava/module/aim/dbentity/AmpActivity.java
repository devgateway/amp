package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.user.User;


public class AmpActivity
		  implements Comparable, Serializable {

	private Long ampActivityId ;
	private String ampId ;
	private String name ;
	private String description ;
	private String objective ;
    private String documentSpace;
	private AmpStatus status ;
	private String language ;
	private String version ;
	private String calType; 	// values GREGORIAN, ETH_CAL, ETH_FISCAL_CAL
	private String condition ;
	private Date activityApprovalDate;  // defunct
	private Date activityStartDate ;    // defunct
	private Date activityCloseDate ;    // defunct
	private Date originalCompDate;      // defunct
	private Set sectors ;
	private Set locations ;
	private Set orgrole;
	private AmpLevel level ;
	private Set internalIds ;
	private Set funding ;
	private Set progress;
	private Set documents ;
	private Set notes;
	private Set issues;
	private AmpModality modality ;
	private AmpTheme themeId;
	private String programDescription;
	private AmpTeam team;
	private Set member;
	private Country country;
	private String contactName ; // use contFirstName and contLastName instead.
								 // The field is defunct

	// Donor contact information
	private String contFirstName;
	private String contLastName;
	private String email;

	// MOFED contact information
	private String mofedCntFirstName;
	private String mofedCntLastName;
	private String mofedCntEmail;

	private String comments ;
	private String statusReason;
	private Set components;

	private Date proposedStartDate;
	private Date actualStartDate;
	private Date proposedApprovalDate;
	private Date actualApprovalDate;
	private Date actualCompletionDate;
	private Set closingDates;

	private User author;                        // use activityCreator instead
														  // This field is defunct

	private AmpTeamMember activityCreator;
	private Date createdDate;
	private Date updatedDate;

	//private Set teamList;
	private String contractors;

	private Set regionalFundings;

	private String approvalStatus;

	// Aid Harmonization Survey Set
	private Set survey;

	/**
	 * @return
	 */
	public String getAmpId() {
		return ampId;
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
	public Set getFunding() {
		return funding;
	}



	/**
	 * @return
	 */
	public Set getInternalIds() {
		return internalIds;
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
	public AmpLevel getLevel() {
		return level;
	}

	/**
	 * @return
	 */
	public Set getLocations() {
		return locations;
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
	public String getObjective() {
		return objective;
	}

	/**
	 * @return
	 */
	public Set getOrgrole() {
		return orgrole;
	}

	/**
	 * @return
	 */
	public Set getSectors() {
		return sectors;
	}

	public Set getIssues() {
		return issues;
	}

	/**
	 * @return
	 */
	public AmpStatus getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public Date getActivityStartDate() {
		return activityStartDate;
	}

	public String getCondition() {
		return condition;
	}

	public Date getActivityCloseDate() {
		return activityCloseDate;
	}

	/**
	 * @param string
	 */
	public void setAmpId(String string) {
		ampId = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param set
	 */
	public void setFunding(Set set) {
		funding = set;
	}


	/**
	 * @param set
	 */
	public void setInternalIds(Set set) {
		internalIds = set;
	}

	public void setIssues(Set set) {
		issues = set;
	}

	/**
	 * @param string
	 */
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * @param level
	 */
	public void setLevel(AmpLevel level) {
		this.level = level;
	}

	/**
	 * @param set
	 */
	public void setLocations(Set set) {
		locations = set;
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
	public void setObjective(String string) {
		objective = string;
	}

	/**
	 * @param set
	 */
	public void setOrgrole(Set set) {
		orgrole = set;
	}


	/**
	 * @param set
	 */
	public void setSectors(Set set) {
		sectors = set;
	}

	/**
	 * @param status
	 */
	public void setStatus(AmpStatus status) {
		this.status = status;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

	public void setActivityStartDate(Date date) {
		activityStartDate = date;
	}

	public void setActivityCloseDate(Date date) {
		activityCloseDate = date;
	}

	public void setCondition(String string) {
		condition = string;
	}

	/**
	 * @return
	 */
	public Long getAmpActivityId() {
		return ampActivityId;
	}

	/**
	 * @param long1
	 */
	public void setAmpActivityId(Long long1) {
		ampActivityId = long1;
	}

	/**
	 * @return
	 */
	public Set getProgress() {
		return progress;
	}

	/**
	 * @return
	 */
	public Set getDocuments() {
		return documents;
	}

	/**
	 * @return
	 */
	public Set getNotes() {
		return notes;
	}


	/**
	 * @param string
	 */
	public void setProgress(Set progress) {
		this.progress = progress;
	}

	/**
	 * @param string
	 */
	public void setDocuments(Set documents) {
		this.documents = documents;
	}

	/**
	 * @param string
	 */
	public void setNotes(Set notes) {
		this.notes = notes;
	}

	public AmpModality getModality() {
			return modality;
	}

	public void setModality(AmpModality modality) {
			this.modality = modality;
	}
	public AmpTheme getThemeId() {
		return themeId;
	}

	public void setThemeId(AmpTheme themeId) {
		this.themeId = themeId;
	}

	public AmpTeam getTeam() {
		return team;
	}

	public void setTeam(AmpTeam team) {
		this.team = team;
	}

	public Set getMember() {
		return member;
	}

	public void setMember(Set member) {
		this.member = member;
	}
	/**
	 * @return
	 */
	public Date getOriginalCompDate() {
		return originalCompDate;
	}

	/**
	 * @param date
	 */
	public void setOriginalCompDate(Date date) {
		originalCompDate = date;
	}

	/**
	 * @return
	 */
	public String getCalType() {
		return calType;
	}

	/**
	 * @param string
	 */
	public void setCalType(String string) {
		calType = string;
	}

	/**
	 * @return
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	public int compareTo(Object o) {
			  if (!(o instanceof AmpActivity)) throw new ClassCastException();

			  AmpActivity act = (AmpActivity) o;
			  return (this.name.trim().toLowerCase().
									compareTo(act.name.trim().toLowerCase()));

	}
	/**
	 * @return
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @return
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param string
	 */
	public void setComments(String string) {
		comments = string;
	}

	/**
	 * @param string
	 */
	public void setContactName(String string) {
		contactName = string;
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
	 * @return Returns the components.
	 */
	public Set getComponents() {
		return components;
	}
	/**
	 * @param components The components to set.
	 */
	public void setComponents(Set components) {
		this.components = components;
	}
	/**
	 * @return Returns the activityApprovalDate.
	 */
	public Date getActivityApprovalDate() {
		return activityApprovalDate;
	}
	/**
	 * @param activityApprovalDate The activityApprovalDate to set.
	 */
	public void setActivityApprovalDate(Date activityApprovalDate) {
		this.activityApprovalDate = activityApprovalDate;
	}
	/**
	 * @return Returns the actualApprovalDate.
	 */
	public Date getActualApprovalDate() {
		return actualApprovalDate;
	}
	/**
	 * @param actualApprovalDate The actualApprovalDate to set.
	 */
	public void setActualApprovalDate(Date actualApprovalDate) {
		this.actualApprovalDate = actualApprovalDate;
	}
	/**
	 * @return Returns the actualCompletionDate.
	 */
	public Date getActualCompletionDate() {
		return actualCompletionDate;
	}
	/**
	 * @param actualCompletionDate The actualCompletionDate to set.
	 */
	public void setActualCompletionDate(Date actualCompletionDate) {
		this.actualCompletionDate = actualCompletionDate;
	}
	/**
	 * @return Returns the actualStartDate.
	 */
	public Date getActualStartDate() {
		return actualStartDate;
	}
	/**
	 * @param actualStartDate The actualStartDate to set.
	 */
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	/**
	 * @return Returns the proposedApprovalDate.
	 */
	public Date getProposedApprovalDate() {
		return proposedApprovalDate;
	}
	/**
	 * @param proposedApprovalDate The proposedApprovalDate to set.
	 */
	public void setProposedApprovalDate(Date proposedApprovalDate) {
		this.proposedApprovalDate = proposedApprovalDate;
	}
	/**
	 * @return Returns the proposedStartDate.
	 */
	public Date getProposedStartDate() {
		return proposedStartDate;
	}
	/**
	 * @param proposedStartDate The proposedStartDate to set.
	 */
	public void setProposedStartDate(Date proposedStartDate) {
		this.proposedStartDate = proposedStartDate;
	}
	/**
	 * @return Returns the closingDates.
	 */
	public Set getClosingDates() {
		return closingDates;
	}
	/**
	 * @param closingDates The closingDates to set.
	 */
	public void setClosingDates(Set closingDates) {
		this.closingDates = closingDates;
	}
	/**
	 * @return Returns the author.
	 */
	public User getAuthor() {
		return author;
	}
	/**
	 * @param author The author to set.
	 */
	public void setAuthor(User author) {
		this.author = author;
	}
	/**
	 * @return Returns the createdDate.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate The createdDate to set.
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return Returns the updatedDate.
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate The updatedDate to set.
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
	 * @return Returns the activityCreator.
	 */
	public AmpTeamMember getActivityCreator() {
		return activityCreator;
	}

	/**
	 * @param activityCreator The activityCreator to set.
	 */
	public void setActivityCreator(AmpTeamMember activityCreator) {
		this.activityCreator = activityCreator;
	}
	/**
	 * @return Returns the mofedCntEmail.
	 */
	public String getMofedCntEmail() {
		return mofedCntEmail;
	}
	/**
	 * @param mofedCntEmail The mofedCntEmail to set.
	 */
	public void setMofedCntEmail(String mofedCntEmail) {
		this.mofedCntEmail = mofedCntEmail;
	}
	/**
	 * @return Returns the mofedCntFirstName.
	 */
	public String getMofedCntFirstName() {
		return mofedCntFirstName;
	}
	/**
	 * @param mofedCntFirstName The mofedCntFirstName to set.
	 */
	public void setMofedCntFirstName(String mofedCntFirstName) {
		this.mofedCntFirstName = mofedCntFirstName;
	}
	/**
	 * @return Returns the mofedCntLastName.
	 */
	public String getMofedCntLastName() {
		return mofedCntLastName;
	}
	/**
	 * @param mofedCntLastName The mofedCntLastName to set.
	 */
	public void setMofedCntLastName(String mofedCntLastName) {
		this.mofedCntLastName = mofedCntLastName;
	}

//Commented by Mikheil - in general, Hibernate classes do not need to overrride
//this method, because it may lead to incorrect functinoality
/*
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();

		if (!(obj instanceof AmpActivity))
			throw new ClassCastException();

		AmpActivity act = (AmpActivity) obj;
		return this.ampActivityId.equals(act.getAmpActivityId());
	}
    */

	/**
	 * @return Returns the regionalFundings.
	 */
	public Set getRegionalFundings() {
		return regionalFundings;
	}

	/**
	 * @param regionalFundings The regionalFundings to set.
	 */
	public void setRegionalFundings(Set regionalFundings) {
		this.regionalFundings = regionalFundings;
	}



	/**
	 * @return Returns the approvalStatus.
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approval_status The approval_status to set.
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return Returns the survey.
	 */
	public Set getSurvey() {
		return survey;
	}

    public String getDocumentSpace() {
        return documentSpace;
    }

    /**
	 * @param survey The survey to set.
	 */
	public void setSurvey(Set survey) {
		this.survey = survey;
	}

    public void setDocumentSpace(String documentSpace) {
        this.documentSpace = documentSpace;
    }
}
