package org.digijava.module.aim.form ;

import org.apache.struts.action.ActionForm;

import java.util.Collection;
public class MyDesktopForm extends ActionForm
{
    private Collection ampProjects;
    private Collection donor;
    private Collection sector;
    private Collection status;
    private Collection region;
    private Collection currency;
    private String fiscalYear;
    private Long ampStatusId;
    private Long ampSectorId;
    private Long ampOrgId;
    private String ampLocationId;
    private String grandTotal;
    private boolean teamLeadFlag;
    private String ampCurrencyCode;
    private Long ampCurrencyId;
    private Collection pages;
    private String searchCriteria;
    private Long ampCalType;
    private Integer page;
    private Long ampFromYear;
    private Long ampToYear;
    private Collection ampFromYears;
    private Collection ampToYears;
    private Collection ampReports;
    private Collection fiscalYears;
    private String grandTotalFlag;
    private String filterFlag;
    private Collection ampTeamMembers;
    private Collection documents;
    private boolean moreDocuments;
    private Integer reportCount;
    private Integer documentCount;
    private boolean write;
    private Long teamMemberId;
    private Long teamId;
    
    // Added by Akash for activity approval process
    private String workingTeamFlag;
    private int myTaskSize;
    private Collection myTasksColl;
    private String approvalStatus;
    
    private boolean donorFlag;
    
    /* added by Priyajith 
     * for RelatedLinksList */
    private Long selLinks[];
    

    public Collection getPages() 
    {
         return pages;
    }

    public Collection getAmpProjects() 
    {
        return ampProjects;
    }

    public Collection getDonor()
    {
        return donor;
    }

    public Collection getRegion()
    {
        return region;
    }

    public Collection getSector()
    {
        return sector;
    }

    public Collection getStatus()
    {
        return status;
    }

    public Collection getCurrency()
    {
        return currency;
    }

    public String getFiscalYear()
    {
        return fiscalYear;
    }

    public boolean getTeamLeadFlag()
    {
        return teamLeadFlag;
    }

    public boolean getWrite()
    {
        return write;
    }

    public Long getAmpStatusId()
    {
        return ampStatusId;
    }
    
    public Long getAmpOrgId()
    {
        return ampOrgId;
    }

    public Long getAmpSectorId()
    {
        return ampSectorId;
    }

    public Long getAmpCurrencyId()
    {
        return ampCurrencyId;
    }

    public String getAmpLocationId()
    {
        return ampLocationId;
    }

    public String getGrandTotal()
    {
        return grandTotal;
    }
    
    public String getSearchCriteria()
    {
        return searchCriteria;
    }

    public Long getAmpCalType()
    {
        return ampCalType;
    }

    public Long getAmpFromYear()
    {
        return ampFromYear;
    }

    public Long getAmpToYear()
    {
        return ampToYear;
    }

    public String getAmpCurrencyCode()
    {
        return ampCurrencyCode;
    }

    public Integer getPage()
    {
        return page;
    }

    public Collection getAmpFromYears()
    {
        return ampFromYears;
    }

    public Collection getAmpToYears()
    {
        return ampToYears;
    }

    public Collection getFiscalYears()
    {
        return fiscalYears;
    }

    public String getGrandTotalFlag()
    {
        return grandTotalFlag;
    }

    public Collection getAmpReports() 
    {
        return ampReports;
    }


    public Integer getReportCount()
    {
        return reportCount;
    }

    public Integer getDocumentCount()
    {
        return documentCount;
    }

    public void setAmpProjects(Collection ampProjects)
    {
        this.ampProjects = ampProjects;
    }
    
    public String getFilterFlag()
    {
        return filterFlag;
    }

    public Collection getAmpTeamMembers()
    {
        return ampTeamMembers;
    }

    public void setDonor(Collection c)
    {
        donor=c;
    }

    public void setSector(Collection c)
    {
        sector=c;
    }

    public void setStatus(Collection c)
    {
        status=c;
    }

    public void setRegion(Collection c)
    {
        region=c;
    }

    public void setCurrency(Collection c)
    {
        currency=c;
    }

    public void setAmpStatusId(Long ampStatusId)
    {
        this.ampStatusId=ampStatusId;
    }

    public void setAmpLocationId(String ampLocationId)
    {
        this.ampLocationId=ampLocationId;
    }

    public void setAmpCurrencyCode(String ampCurrencyCode)
    {
        this.ampCurrencyCode=ampCurrencyCode;
    }

    public void setAmpOrgId(Long ampOrgId)
    {
        this.ampOrgId=ampOrgId;
    }

    public void setFiscalYear(String s)
    {
        fiscalYear=s;
    }

    public void setTeamLeadFlag(boolean b)
    {
        teamLeadFlag=b;
    }

    public void setWrite(boolean b)
    {
        write=b;
    }

    public void setGrandTotal(String grandTotal)
    {
        this.grandTotal=grandTotal;
    }

    public void setAmpSectorId(Long ampSectorId)
    {
        this.ampSectorId=ampSectorId;
    }

    public void setAmpCurrencyId(Long ampCurrencyId)
    {
        this.ampCurrencyId=ampCurrencyId;
    }

    public void setPages(Collection pages) 
    {
         this.pages = pages;
    }
    
    public void setSearchCriteria(String searchCriteria)
    {
        this.searchCriteria=searchCriteria;
    }

    public void setAmpCalType(Long ampCalType)
    {
        this.ampCalType=ampCalType;
    }

    public void setAmpFromYear(Long ampFromYear)
    {
        this.ampFromYear=ampFromYear;
    }

    public void setAmpToYear(Long ampToYear)
    {
        this.ampToYear=ampToYear;
    }

    public void setPage(Integer page)
    {
        this.page=page;
    }

    public void setAmpFromYears(Collection ampFromYears)
    {
        this.ampFromYears=ampFromYears;
    }

    public void setAmpToYears(Collection ampToYears)
    {
        this.ampToYears=ampToYears;
    }

    public void setFiscalYears(Collection c)
    {
        fiscalYears=c;
    }

    public void setGrandTotalFlag(String s)
    {
        grandTotalFlag=s;
    }

    public void setFilterFlag(String s)
    {
        filterFlag=s;
    }

    public void setAmpReports(Collection ampReports)
    {
        this.ampReports = ampReports;
    }

    public void setAmpTeamMembers(Collection ampTeamMembers)
    {
        this.ampTeamMembers=ampTeamMembers;
    }
    /**
     * @return Returns the documents.
     */
    public Collection getDocuments() {
        return documents;
    }
    /**
     * @param documents The documents to set.
     */
    public void setDocuments(Collection documents) {
        this.documents = documents;
    }
    /**
     * @return Returns the moreDocuments.
     */
    public boolean isMoreDocuments() {
        return moreDocuments;
    }
    /**
     * @param moreDocuments The moreDocuments to set.
     */
    public void setMoreDocuments(boolean moreDocuments) {
        this.moreDocuments = moreDocuments;
    }

    public void setReportCount(Integer reportCount)
    {
        this.reportCount=reportCount;
    }

    public void setDocumentCount(Integer documentCount)
    {
        this.documentCount=documentCount;
    }
    /**
     * @return Returns the teamMemberId.
     */
    public Long getTeamMemberId() {
        return teamMemberId;
    }
    /**
     * @param teamMemberId The teamMemberId to set.
     */
    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
    }
    /**
     * @return Returns the selLinks.
     */
    public Long[] getSelLinks() {
        return selLinks;
    }
    /**
     * @param selLinks The selLinks to set.
     */
    public void setSelLinks(Long[] selLinks) {
        this.selLinks = selLinks;
    }
    /**
     * @return Returns the myTaskSize.
     */
    public int getMyTaskSize() {
        return myTaskSize;
    }
    /**
     * @param myTaskSize The myTaskSize to set.
     */
    public void setMyTaskSize(int myTaskSize) {
        this.myTaskSize = myTaskSize;
    }
    /**
     * @return Returns the workingTeamFlag.
     */
    public String getWorkingTeamFlag() {
        return workingTeamFlag;
    }
    /**
     * @param workingTeamFlag The workingTeamFlag to set.
     */
    public void setWorkingTeamFlag(String workingTeamFlag) {
        this.workingTeamFlag = workingTeamFlag;
    }
    /**
     * @return Returns the myTasksColl.
     */
    public Collection getMyTasksColl() {
        return myTasksColl;
    }
    /**
     * @param myTasksColl The myTasksColl to set.
     */
    public void setMyTasksColl(Collection myTasksColl) {
        this.myTasksColl = myTasksColl;
    }

    /**
     * @return Returns the teamId.
     */
    public Long getTeamId() {
        return teamId;
    }

    /**
     * @param teamId The teamId to set.
     */
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    /**
     * @return Returns the approvalStatus.
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }
    /**
     * @param approvalStatus The approvalStatus to set.
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * @return Returns the donorFlag.
     */
    public boolean isDonorFlag() {
        return donorFlag;
    }

    /**
     * @param donorFlag The donorFlag to set.
     */
    public void setDonorFlag(boolean donorFlag) {
        this.donorFlag = donorFlag;
    }
}
