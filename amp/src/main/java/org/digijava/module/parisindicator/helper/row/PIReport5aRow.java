package org.digijava.module.parisindicator.helper.row;

import java.math.BigDecimal;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

/**
 * This class represents each row in the PI 5a report.
 * 
 * @author gabriel
 */
public class PIReport5aRow extends PIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int year;
    // Aid flows to the goverment sector that use national budget execution
    // procedures
    private BigDecimal column1;
    // Aid flows to the goverment sector that use national financial reporting
    // procedures
    private BigDecimal column2;
    // Aid flows to the goverment sector that use national financial auditing
    // procedures
    private BigDecimal column3;
    // ODA that uses all 3 national PFM
    private BigDecimal column4;
    // Total Aid flows disbursed to the government sector
    private BigDecimal column5;
    // Proportion aid flows to the government sector using one of the 3 country
    // PFM systems
    private float column6;
    // Proportion of aid flows to the government sector using all the 3 country
    // PFM systems
    private float column7;
    // This column is for calculating the first percentage (Col 6)
    private BigDecimal column8;

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

    public BigDecimal getColumn3() {
        return column3;
    }

    public void setColumn3(BigDecimal column3) {
        this.column3 = column3;
    }

    public BigDecimal getColumn4() {
        return column4;
    }

    public void setColumn4(BigDecimal column4) {
        this.column4 = column4;
    }

    public BigDecimal getColumn5() {
        return column5;
    }

    public void setColumn5(BigDecimal column5) {
        this.column5 = column5;
    }

    public float getColumn6() {
        return column6;
    }

    public void setColumn6(float column6) {
        this.column6 = column6;
    }

    public float getColumn7() {
        return column7;
    }

    public void setColumn7(float column7) {
        this.column7 = column7;
    }

    public BigDecimal getColumn8() {
        return column8;
    }

    public void setColumn8(BigDecimal column8) {
        this.column8 = column8;
    }
}
