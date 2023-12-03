package org.digijava.module.aim.helper ;
import java.util.Collection;

public class AmpDonors
{
    private String donors;
    private Collection project;
    private String projectName;
    private Collection ampFund;
    private Collection ampGrantFund;
    private Collection ampLoanFund;
    private int subcount;
    private int donorCount;
    private String assistance;
        
    public String getDonors() {
        return donors;
    }

    /**
     * @param string
     */
    public void setDonors(String string) {
        donors = string;
    }

    public String getAssistance() {
        return assistance;
    }

    /**
     * @param string
     */
    public void setAssistance(String string) {
        assistance = string;
    }

    public int getSubcount() {
        return subcount;
    }

    public int getDonorCount() {
        return donorCount;
    }

    /**
     * @param string
     */
    public void setSubcount(int i) {
        subcount = i;
    }

    public void setDonorCount(int i) {
        donorCount = i;
    }

    /**
     * @return
     */
    public Collection getProject() {
        return project;
    }

    public String getProjectName() {
        return projectName;
    }

    public Collection getAmpFund() {
        return ampFund;
    }

    public Collection getAmpLoanFund() {
        return ampLoanFund;
    }

    public Collection getAmpGrantFund() {
        return ampGrantFund;
    }

    /**
     * @param string
     */
    public void setProject(Collection c) {
        project = c;
    }

    public void setProjectName(String s) {
        projectName = s;
    }

    public void setAmpFund(Collection c) {
        ampFund = c;
    }

    public void setAmpLoanFund(Collection c) {
        ampLoanFund = c;
    }

    public void setAmpGrantFund(Collection c) {
        ampGrantFund = c;
    }

    
}
