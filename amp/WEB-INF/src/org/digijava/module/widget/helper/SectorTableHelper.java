

package org.digijava.module.widget.helper;

import java.util.List;


public class SectorTableHelper {
    public final static long  EMPTY_ROW_SECTOR_ID=-1;
    public final static long  TOTAL_ROW_SECTOR_ID=-2;
    public final static long  OTHER_ROW_SECTOR_ID=0;
    private String sectorName;
    private List<String> values;
    private boolean applyStyle;
    private boolean emptyRow;
    private Long sectorId;
    
    public Long getDonorAmount() {
        return donorAmount;
    }

    public void setDonorAmount(Long donorAmount) {
        this.donorAmount = donorAmount;
    }
    private Long donorAmount;

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

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
