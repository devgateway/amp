package org.digijava.module.aim.helper ;
import java.util.Collection;

public class Project
{
    private String name;
    private String termName;
    private Collection ampFund;
    private Collection component;
    private Collection termAssist;
    private String projCommAmount;
    private String projDisbAmount;
    private String projPlannedDisbAmount;
    private String projExpAmount;
    private String projUnDisbAmount;
    private Long ampActivityId;
    private String donor ;
    private String title ;
    private String status ;
    private String level ;

    private Collection donors ;
    private Collection regions ;
    private Collection sectors;
    
    private String startDate ;
    private String closeDate ;
    private String acCommitment;
    private String acDisbursement;
    private String acUnDisbursement;
    private Collection assistance;

    private String description ;
    private String descriptionPDFXLS ;
    private String signatureDate;
    private String plannedCompletionDate;
    
    private String actualCompletionDate;
    private Collection progress ;
    private Collection issues ;
    private Collection measures ;
    private Collection responsibleActor;

    private int count;
    private int rowspan;

    public Long getAmpActivityId() 
    {
        return ampActivityId;
    }

    public void setAmpActivityId(Long l) 
    {
        ampActivityId = l;
    }

    public String getName() {
        return name;
    }

    public String getTermName() {
        return termName;
    }

    public String getProjCommAmount() {
        return projCommAmount;
    }

    public String getProjDisbAmount() {
        return projDisbAmount;
    }

    public String getProjPlannedDisbAmount() {
        return projPlannedDisbAmount;
    }

    public String getProjExpAmount() {
        return projExpAmount;
    }

    public String getProjUnDisbAmount() {
        return projUnDisbAmount;
    }

    public Collection getTermAssist() {
        return termAssist;
    }

    public String getSignatureDate() {
        return signatureDate;
    }

    public String getPlannedCompletionDate() {
        return plannedCompletionDate;
    }

    public Collection getProgress() {
        return progress;
    }

    public Collection getIssues() {
        return issues;
    }

    public Collection getMeasures() {
        return measures;
    }

    public Collection getResponsibleActor() {
        return responsibleActor;
    }

    public String getDescription() {
        return description;
    }
    
    public String getDescriptionPDFXLS() {
        return descriptionPDFXLS;
    }
    
    /**
     * @param string
     */

    public void setName(String s) {
        name = s;
    }

    public void setTermName(String s) {
        termName = s;
    }

    public void setProjCommAmount(String s) {
        projCommAmount = s;
    }

    public void setProjDisbAmount(String s) {
        projDisbAmount = s;
    }

    public void setProjPlannedDisbAmount(String s) {
        projPlannedDisbAmount = s;
    }

    public void setProjExpAmount(String s) {
        projExpAmount = s;
    }

    public void setProjUnDisbAmount(String s) {
        projUnDisbAmount = s;
    }

        /**
     * @param string
     */
    public void setAmpFund(Collection c) {
        ampFund=c;
    }

    public void setTermAssist(Collection c) {
        termAssist=c;
    }

    public Collection getAmpFund() {
        return ampFund;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int i) {
        count = i;
    }
    
    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int i) {
        rowspan = i;
    }

    public Collection getDonors() 
        {
            return donors;
        }

        public void setDonors(Collection c) 
        {
            donors = c;
        }

        public Collection getRegions() 
        {
            return regions;
        }

        public void setRegions(Collection c) 
        {
            regions = c;
        }

        public Collection getSectors() 
        {
            return sectors;
        }

        public void setSectors(Collection c) 
        {
            sectors = c;
        }

        public String getLevel() 
        {
            return level;
        }
    
        public void setLevel(String string) 
        {
            level = string;
        }

    public String getTitle() 
    {
        return title;
    }
    
    public void setTitle(String string) 
    {
        title = string;
    }

    public String getStatus() 
    {
        return status;
    }
    
    public void setStatus(String string) 
    {
        status = string;
    }

    public String getStartDate() 
    {
        return startDate;
    }

    public void setStartDate(String string) 
    {
        startDate = string;
    }

    public String getCloseDate() 
    {
        return closeDate;
    }

    public void setCloseDate(String string) 
    {
        closeDate = string;
    }

    public String getAcCommitment() {
        return acCommitment;
    }

    /**
     * @return
     */
    public String getAcDisbursement() {
        return acDisbursement;
    }

    /**
     * @return
     */
    public String getAcUnDisbursement() {
        return acUnDisbursement;
    }

    public Collection getAssistance(){  
        return assistance;
    }

    /**
     * @param string
     */
    public void setAssistance(Collection c) {
        assistance = c;
    }

    public void setAcCommitment(String string) {
        acCommitment = string;
    }

    /**
     * @param string
     */
    public void setAcDisbursement(String string) {
        acDisbursement = string;
    }

    /**
     * @param string
     */
    public void setAcUnDisbursement(String string) {
        acUnDisbursement = string;
    }

    public void setComponent(Collection c) 
    {
        component = c;
    }

    public Collection getComponent() 
    {
        return component;
    }

    public void setDescription(String s) {
        description = s;
    }

    public void setDescriptionPDFXLS(String s) {
        descriptionPDFXLS = s;
    }
    
    public void setResponsibleActor(Collection c) {
        responsibleActor = c;
    }

    public void setProgress(Collection c) {
        progress=c;
    }

    public void setIssues(Collection c) {
        issues=c;
    }

    public void setMeasures(Collection c) {
        measures=c;
    }

    public void setSignatureDate(String s) 
    {
        signatureDate = s;
    }

    public void setPlannedCompletionDate(String s) 
    {
        plannedCompletionDate = s;
    }

    /**
     * @return Returns the actualCompletionDate.
     */
    public String getActualCompletionDate() {
        return actualCompletionDate;
    }

    /**
     * @param actualCompletionDate The actualCompletionDate to set.
     */
    public void setActualCompletionDate(String actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    /**
     * @return Returns the donor.
     */
    public String getDonor() {
        return donor;
    }

    /**
     * @param donor The donor to set.
     */
    public void setDonor(String donor) {
        this.donor = donor;
    }
    
    
}
    
