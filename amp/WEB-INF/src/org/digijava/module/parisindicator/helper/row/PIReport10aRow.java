package org.digijava.module.parisindicator.helper.row;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

/**
 * This class represents each row in the PI 10a report.
 * 
 * @author gabriel
 */
public class PIReport10aRow extends PIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int year;
    // Number of missions to the field that are joint
    private int column1;
    // Total number of missions to the field
    private int column2;
    // Proportion of donor missions that are joint
    private float column3;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public int getColumn1() {
        return column1;
    }

    public void setColumn1(int column1) {
        this.column1 = column1;
    }

    public int getColumn2() {
        return column2;
    }

    public void setColumn2(int column2) {
        this.column2 = column2;
    }
}
