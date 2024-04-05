package org.digijava.module.parisindicator.helper.row;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

import java.math.BigDecimal;

/**
 * This class represents each row in the PI 5a report.
 * 
 * @author gabriel
 */
public class PIReport5bRow extends PIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int year;
    // Aid flows to the government sector that use national procurement procedures
    private BigDecimal column1;
    // Total Aid flows disbursed to the government sector
    private BigDecimal column2;
    // Proportion of aid flows to the government sector using national procurement procedures
    private float column3;
    
    public AmpOrgGroup getDonorGroup() {
        return donorGroup;
    }

    public void setDonorGroup(AmpOrgGroup donorGroup) {
        this.donorGroup = donorGroup;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getColumn1() {
        return column1;
    }

    public void setColumn1(BigDecimal column1) {
        this.column1 = column1;
    }

    public BigDecimal getColumn2() {
        return column2;
    }

    public void setColumn2(BigDecimal column2) {
        this.column2 = column2;
    }

    public float getColumn3() {
        return column3;
    }

    public void setColumn3(float column3) {
        this.column3 = column3;
    }
}
