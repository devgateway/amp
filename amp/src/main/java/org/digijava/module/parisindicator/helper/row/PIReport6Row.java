package org.digijava.module.parisindicator.helper.row;

import org.digijava.module.aim.dbentity.AmpOrgGroup;

/**
 * This class represents each row in the PI 6 report.
 * 
 * @author gabriel
 */
public class PIReport6Row extends PIReportAbstractRow {

    private AmpOrgGroup donorGroup;
    private int[] years;

    public AmpOrgGroup getDonorGroup() {
        return donorGroup;
    }

    public void setDonorGroup(AmpOrgGroup donorGroup) {
        this.donorGroup = donorGroup;
    }

    public int[] getYears() {
        return years;
    }

    public void setYears(int[] year) {
        this.years = year;
    }
}
