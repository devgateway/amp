package org.digijava.module.aim.helper ;
import java.util.Collection;

public class multiReport
{
    private Long ampTeamId;
    private Long ampActivityId;
    private String teamName;
    private Collection donors;
    private Collection totalTeamFund;
    private Collection totalTeamTermAssistFund;
    private String teamCommAmount;
    private String teamDisbAmount;
    private String teamExpAmount;
    private String teamUnDisbAmount;
    private String teamPlannedDisbAmount;

    private String donorAgency;
    private int donorCount;
    private Collection project;
    private Collection hierarchy;
    private String donorCommAmount;
    private String donorDisbAmount;
    private String donorExpAmount;
    private String donorGrantCommAmount;
    private String donorGrantDisbAmount;
    private String donorGrantExpAmount;
    private String donorLoanCommAmount;
    private String donorLoanDisbAmount;
    private String donorLoanExpAmount;


    private String sector;
    private Collection ampDonors;
    private Collection fiscalYrs;
    private Collection fundTotal;
    private Collection fundLoanTotal;
    private Collection fundGrantTotal;

    private Collection totalSectorTermAssistFund;
    private Collection totalSectorFund;
    private String sectorCommAmount;
    private String sectorDisbAmount;
    private String sectorUnDisbAmount;
    private String sectorPlannedDisbAmount;
    private String sectorExpAmount;
        
    
    private int count;
    
    private int yearCount;

    public Long getAmpActivityId() 
    {
        return ampActivityId;
    }

    public void setAmpActivityId(Long l) 
    {
        ampActivityId = l;
    }
    public Collection getDonors() {
        return donors;
    }

    public Long getAmpTeamId() {
        return ampTeamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamCommAmount() {
        return teamCommAmount;
    }

    public String getTeamDisbAmount() {
        return teamDisbAmount;
    }

    public String getTeamUnDisbAmount() {
        return teamUnDisbAmount;
    }

    public String getTeamPlannedDisbAmount() {
        return teamPlannedDisbAmount;
    }

    public String getTeamExpAmount() {
        return teamExpAmount;
    }

    public Collection getTotalTeamFund() {
        return totalTeamFund;
    }

    public Collection getTotalTeamTermAssistFund() {
        return totalTeamTermAssistFund;
    }

    public void setTotalTeamTermAssistFund(Collection collection) {
        totalTeamTermAssistFund = collection;
    }

    public void setTotalTeamFund(Collection collection) {
        totalTeamFund = collection;
    }

    public void setTeamCommAmount(String s) {
        teamCommAmount = s;
    }

    public void setTeamName(String s) {
        teamName = s;
    }

    public void setTeamDisbAmount(String s) {
        teamDisbAmount = s;
    }

    public void setTeamUnDisbAmount(String s) {
        teamUnDisbAmount = s;
    }

    public void setTeamPlannedDisbAmount(String s) {
        teamPlannedDisbAmount = s;
    }

    public void setTeamExpAmount(String s) {
        teamExpAmount = s;
    }

    public void setDonors(Collection collection) {
        donors = collection;
    }

    /**
     * @return
     */
    public Collection getAmpDonors() {
        return ampDonors;
    }

    public String getDonorAgency() {
        return donorAgency;
    }

    public String getDonorCommAmount() {
        return donorCommAmount;
    }

    public String getDonorDisbAmount() {
        return donorDisbAmount;
    }

    public String getDonorExpAmount() {
        return donorExpAmount;
    }

    public String getDonorGrantCommAmount() {
        return donorGrantCommAmount;
    }

    public String getDonorGrantDisbAmount() {
        return donorGrantDisbAmount;
    }

    public String getDonorGrantExpAmount() {
        return donorGrantExpAmount;
    }

    public String getDonorLoanCommAmount() {
        return donorLoanCommAmount;
    }

    public String getDonorLoanDisbAmount() {
        return donorLoanDisbAmount;
    }

    public String getDonorLoanExpAmount() {
        return donorLoanExpAmount;
    }

    
    /**
     * @param collection
     */
    public void setAmpDonors(Collection collection) {
        ampDonors = collection;
    }

    /**
     * @return
     */
    public Collection getFiscalYrs() {
        return fiscalYrs;
    }

    /**
     * @param collection
     */
    public void setFiscalYrs(Collection collection) {
        fiscalYrs = collection;
    }

    public void setAmpTeamId(Long l) {
        ampTeamId = l;
    }

    /**
     * @return
     */
    public Collection getFundTotal() {
        return fundTotal;
    }

    /**
     * @param collection
     */
    public void setFundTotal(Collection collection) {
        fundTotal = collection;
    }

    /**
     * @return
     */
    public Collection getFundGrantTotal() {
        return fundGrantTotal;
    }

    /**
     * @param collection
     */
    public void setFundGrantTotal(Collection collection) {
        fundGrantTotal = collection;
    }

    /**
     * @return
     */
    public Collection getFundLoanTotal() {
        return fundLoanTotal;
    }

    /**
     * @param collection
     */
    public void setFundLoanTotal(Collection collection) {
        fundLoanTotal = collection;
    }

    /**
     * @return
     */
    public int getCount() {
        return count;
    }

    public int getDonorCount() {
        return donorCount;
    }

    /**
     * @param i
     */
    public void setCount(int i) {
        count = i;
    }

    public void setDonorCount(int i) {
        donorCount = i;
    }

    /**
     * @return
     */
    public String getSector() {
        return sector;
    }

    /**
     * @param string
     */
    public void setSector(String string) {
        sector = string;
    }

    public void setDonorAgency(String string) {
        donorAgency = string;
    }
    public void setProject(Collection c) {
        project = c;
    }
    public Collection getProject() {
        return project;
    }

    public int getYearCount() {
        return yearCount;
    }

    /**
     * @param i
     */
    public void setYearCount(int i) {
        yearCount = i;
    }

    public void setDonorGrantCommAmount(String s) {
        donorGrantCommAmount = s;
    }

    public void setDonorGrantDisbAmount(String s) {
        donorGrantDisbAmount = s;
    }

    public void setDonorGrantExpAmount(String s) {
        donorGrantExpAmount = s;
    }

    public void setDonorLoanCommAmount(String s) {
        donorLoanCommAmount = s;
    }

    public void setDonorLoanDisbAmount(String s) {
        donorLoanDisbAmount = s;
    }

    public void setDonorLoanExpAmount(String s) {
        donorLoanExpAmount = s;
    }

    public void setDonorCommAmount(String s) {
        donorCommAmount = s;
    }

    public void setDonorDisbAmount(String s) {
        donorDisbAmount = s;
    }

    public void setDonorExpAmount(String s) {
        donorExpAmount = s;
    }

    public String getSectorCommAmount() 
    {
        return sectorCommAmount;
    }

    public String getSectorDisbAmount() 
    {
        return sectorDisbAmount;
    }

    public String getSectorUnDisbAmount() 
    {
        return sectorUnDisbAmount;
    }

    public String getSectorPlannedDisbAmount() 
    {
        return sectorPlannedDisbAmount;
    }

    public String getSectorExpAmount() 
    {
        return sectorExpAmount;
    }

    public Collection getTotalSectorFund() 
    {
        return totalSectorFund;
    }

    public Collection getTotalSectorTermAssistFund() 
    {
        return totalSectorTermAssistFund;
    }

    public void setTotalSectorFund(Collection c) 
    {
        totalSectorFund = c;
    }

    public void setTotalSectorTermAssistFund(Collection c) 
    {
        totalSectorTermAssistFund = c;
    }

    public void setSectorCommAmount(String s) 
    {
        sectorCommAmount = s;
    }

    public void setSectorDisbAmount(String s) 
    {
        sectorDisbAmount = s;
    }

    public void setSectorUnDisbAmount(String s) 
    {
        sectorUnDisbAmount = s;
    }

    public void setSectorPlannedDisbAmount(String s) 
    {
        sectorPlannedDisbAmount = s;
    }

    public void setSectorExpAmount(String s) 
    {
    
        sectorExpAmount = s;
    }

    public Collection getHierarchy() 
    {
        return hierarchy;
    }

    public void setHierarchy(Collection c) 
    {
        hierarchy = c;
    }

    
}
