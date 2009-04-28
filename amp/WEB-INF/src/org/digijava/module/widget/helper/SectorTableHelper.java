

package org.digijava.module.widget.helper;

import java.util.List;


public class SectorTableHelper {
    private String sectorName;
    private List<String> values;
    private boolean applyStyle;
    private boolean emptyRow;

    public boolean isEmptyRow() {
        return emptyRow;
    }
    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
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

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

}
