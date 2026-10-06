package org.digijava.module.parisindicator.helper.row;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

import java.math.BigDecimal;

/**
 * This class represents each row in the PI 9 report.
 * 
 * @author gabriel
 */
public class PIReport9Row extends PIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int year;
    // Budget support aid flows provided in the context of programme based
    // approach
    private BigDecimal column1;
    // Other aid flows provided in the context of programme based approach
    private BigDecimal column2;
    // Total aid flows provided
    private BigDecimal column3;
    // Proportion of aid flows provided in the context of programme based
    // approach
    private float column4;

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

    public AmpOrgGroup getDonorGroup() {
        return donorGroup;
    }

    public void setDonorGroup(AmpOrgGroup donorGroup) {
        this.donorGroup = donorGroup;
    }

    public BigDecimal getColumn3() {
        return column3;
    }

    public void setColumn3(BigDecimal column3) {
        this.column3 = column3;
    }

    public float getColumn4() {
        return column4;
    }

    public void setColumn4(float column4) {
        this.column4 = column4;
    }
}
