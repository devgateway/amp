package org.digijava.module.esrigis.helpers;

public class SimpleDonor {
    
    private String donorname;
    private String donorCode;
    private String donorgroup;
    public String getDonorname() {
        return donorname;
    }
    public void setDonorname(String donorname) {
        this.donorname = donorname;
    }
    public String getDonorgroup() {
        return donorgroup;
    }
    public void setDonorgroup(String donorgroup) {
        this.donorgroup = donorgroup;
    }
    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }
    public String getDonorCode() {
        return donorCode;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s (%s)", this.donorname, this.donorCode);
    }
}
