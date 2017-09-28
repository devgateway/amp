package org.digijava.module.gpi.helper.row;

import java.math.BigDecimal;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

/**
 * This class represents each row in the GPI 6 report.
 * 
 * @author gabriel
 */
public class GPIReport6Row extends GPIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int year;
    // Number of projects using country results framework
    private BigDecimal column1;
    private String column1DisplayValue;
    // Total Number of Projects
    private BigDecimal column2;
    private String column2DisplayValue;
    // Indicator 1
    private float column3;
    private String column3DisplayValue;

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
        if (!(obj instanceof GPIReport6Row))
            return false;
        GPIReport6Row other = (GPIReport6Row) obj;
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

    public String getColumn1DisplayValue() {
        return column1DisplayValue;
    }

    public void setColumn1DisplayValue(String column1DisplayValue) {
        this.column1DisplayValue = column1DisplayValue;
    }

    public String getColumn2DisplayValue() {
        return column2DisplayValue;
    }

    public void setColumn2DisplayValue(String column2DisplayValue) {
        this.column2DisplayValue = column2DisplayValue;
    }

    public String getColumn3DisplayValue() {
        return column3DisplayValue;
    }

    public void setColumn3DisplayValue(String column3DisplayValue) {
        this.column3DisplayValue = column3DisplayValue;
    }
}
