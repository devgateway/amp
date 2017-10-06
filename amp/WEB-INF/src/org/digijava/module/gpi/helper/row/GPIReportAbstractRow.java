package org.digijava.module.gpi.helper.row;

/**
 * This class represents the basic row information for each report.
 * 
 * @author gabriel
 */
public abstract class GPIReportAbstractRow {

    private int id;
    private String donorGroupName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDonorGroupName() {
        return donorGroupName;
    }

    public void setDonorGroupName(String donorGroupName) {
        this.donorGroupName = donorGroupName;
    }
}
