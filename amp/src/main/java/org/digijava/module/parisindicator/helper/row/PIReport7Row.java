package org.digijava.module.parisindicator.helper.row;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

import java.math.BigDecimal;

/**
 * This class represents each row in the PI 7 report.
 * 
 * @author gabriel
 */
public class PIReport7Row extends PIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int year;
    // Aid flows to the government sector scheduled for fiscal year
    private BigDecimal column1;
    // Total Aid flows disbursed to the government sector
    private BigDecimal column2;
    // Proportion of aid to the government sector disbursed within the fiscal
    // year it was scheduled
    private float column3;

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

    public AmpOrgGroup getDonorGroup() {
        return donorGroup;
    }

    public void setDonorGroup(AmpOrgGroup donorGroup) {
        this.donorGroup = donorGroup;
    }
}
