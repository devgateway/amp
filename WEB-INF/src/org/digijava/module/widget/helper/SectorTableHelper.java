

package org.digijava.module.widget.helper;

import java.util.List;


public class SectorTableHelper {
    private String sectorName;
    private List<Long> totalYearsValue;
    private List<Long> percentYearsValue;
    
    public String getSectorName() {
        return sectorName;
    }

    public List<Long> getPercentYearsValue() {
        return percentYearsValue;
    }

    public void setPercentYearsValue(List<Long> percentYearsValue) {
        this.percentYearsValue = percentYearsValue;
    }

    public List<Long> getTotalYearsValue() {
        return totalYearsValue;
    }

    public void setTotalYearsValue(List<Long> totalYearsValue) {
        this.totalYearsValue = totalYearsValue;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

}
