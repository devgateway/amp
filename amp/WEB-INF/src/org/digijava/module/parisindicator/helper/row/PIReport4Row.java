package org.digijava.module.parisindicator.helper.row;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

import java.math.BigDecimal;

/**
 * This class represents each row in the PI 4 report.
 * 
 * @author gabriel
 */
public class PIReport4Row extends PIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int year;
    // Volume of technical co-operation for capacity development provided
    // through co-ordinated programmes
    private BigDecimal column1;
    // Total volume of technical co-operation provided
    private BigDecimal column2;
    // % of TC for capacity development provided through coordinated programmes
    // consistent with national development strategies
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((column1 == null) ? 0 : column1.hashCode());
        result = prime * result + ((column2 == null) ? 0 : column2.hashCode());
        result = prime * result + new Float(column3).intValue();
        result = prime * result + ((donorGroup == null) ? 0 : donorGroup.hashCode());
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PIReport4Row))
            return false;
        PIReport4Row other = (PIReport4Row) obj;
        if (column1 == null) {
            if (other.column1 != null)
                return false;
        } else if (!column1.equals(other.column1))
            return false;
        if (column2 == null) {
            if (other.column2 != null)
                return false;
        } else if (!column2.equals(other.column2))
            return false;
        if (donorGroup == null) {
            if (other.donorGroup != null)
                return false;
        } else if (!donorGroup.equals(other.donorGroup))
            return false;
        if (year != other.year)
            return false;
        return true;
    }
}
