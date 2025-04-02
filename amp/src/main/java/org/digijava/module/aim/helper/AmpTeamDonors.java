package org.digijava.module.aim.helper ;
import java.util.Collection;

public class AmpTeamDonors
{
    private String donorAgency;
    private int donorCount;
    private Collection project;
    private Collection component;
    private Collection totalDonorTermAssistFund;
    private Collection totalDonorFund;
    private String donorCommAmount;
    private String donorDisbAmount;
    private String donorUnDisbAmount;
    private String donorPlannedDisbAmount;
    private String donorExpAmount;
    
    public String getDonorAgency() 
    {
        return donorAgency;
    }

    public String getDonorCommAmount() 
    {
        return donorCommAmount;
    }

    public String getDonorDisbAmount() 
    {
        return donorDisbAmount;
    }

    public String getDonorUnDisbAmount() 
    {
        return donorUnDisbAmount;
    }

    public String getDonorPlannedDisbAmount() 
    {
        return donorPlannedDisbAmount;
    }

    public String getDonorExpAmount() 
    {
        return donorExpAmount;
    }

    public int getDonorCount() 
    {
        return donorCount;
    }

    public Collection getTotalDonorFund() 
    {
        return totalDonorFund;
    }

    public Collection getComponent() 
    {
        return component;
    }

    public Collection getTotalDonorTermAssistFund() 
    {
        return totalDonorTermAssistFund;
    }

    public Collection getProject() 
    {
        return project;
    }

    public void setDonorAgency(String string) 
    {
        donorAgency = string;
    }
    public void setProject(Collection c) 
    {
        project = c;
    }

    public void setComponent(Collection c) 
    {
        component = c;
    }

    public void setTotalDonorFund(Collection c) 
    {
        totalDonorFund = c;
    }

    public void setTotalDonorTermAssistFund(Collection c) 
    {
        totalDonorTermAssistFund = c;
    }

    public void setDonorCommAmount(String s) 
    {
        donorCommAmount = s;
    }

    public void setDonorDisbAmount(String s) 
    {
        donorDisbAmount = s;
    }

    public void setDonorUnDisbAmount(String s) 
    {
        donorUnDisbAmount = s;
    }

    public void setDonorPlannedDisbAmount(String s) 
    {
        donorPlannedDisbAmount = s;
    }

    public void setDonorExpAmount(String s) 
    {
        donorExpAmount = s;
    }

    public void setDonorCount(int i) 
    {
        donorCount = i;
    }
}   
