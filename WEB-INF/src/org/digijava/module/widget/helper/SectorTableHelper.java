

package org.digijava.module.widget.helper;

import java.util.List;


public class SectorTableHelper {
    private String sectorName;
    private List<Long> totalYearsValue;
    private List<Long> percentYearsValue;
    private boolean applyStyle;
    private boolean emptyRow;

    public boolean isEmptyRow() {
        return emptyRow;
    }

    public void setEmptyRow(boolean emptyRow) {
        this.emptyRow = emptyRow;
    }

    public boolean isApplyStyle() {
        return applyStyle;
    }

    public void setApplyStyle(boolean applyStyle) {
        this.applyStyle = applyStyle;
    }
    
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
