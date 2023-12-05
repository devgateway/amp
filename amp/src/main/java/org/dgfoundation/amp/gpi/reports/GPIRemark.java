package org.dgfoundation.amp.gpi.reports;

/**
 * Class for storing gpi remark info used in endpoints (for GPI report 5a)
 * 
 * @author Viorel Chihai
 *
 */
public class GPIRemark {

    private String donorAgency;
    private String date;
    private String remark;
    
    public GPIRemark() {}
    
    public GPIRemark(String donorAgency, String date, String remark) {
        super();
        this.donorAgency = donorAgency;
        this.date = date;
        this.remark = remark;
    }

    public String getDonorAgency() {
        return donorAgency;
    }

    public void setDonorAgency(String donorAgency) {
        this.donorAgency = donorAgency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
