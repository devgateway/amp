package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.dimension.ARDimension;

public class PercentageHelperData {
    String columnType;
    String item;
    Long itemId;
    double percentage;
    Class dimensionClass;
    
    
    boolean hierarchyPurpoase;
    
    
    
    
    public PercentageHelperData(String columnType, String item, Long itemId,
            double percentage, Class dimensionClass, boolean hierarchyPurpoase) {
        super();
        this.columnType = columnType;
        this.item = item;
        this.itemId = itemId;
        this.percentage = percentage;
        this.dimensionClass = dimensionClass;
        this.hierarchyPurpoase = hierarchyPurpoase;
    }
    public boolean hasAsDescendent(PercentageHelperData phd) {
        return hasAsDescendent(phd.getItemId());
    }
    public boolean hasAsDescendent(Long id) {
        if ( this.itemId == null  || this.dimensionClass == null || id == null )
            return false;
        return ARDimension.areItemsLinked(this.itemId, id, this.dimensionClass);
    }
    
    
    /**
     * @return the columnType
     */
    public String getColumnType() {
        return columnType;
    }
    /**
     * @param columnType the columnType to set
     */
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }
    /**
     * @param item the item to set
     */
    public void setItem(String item) {
        this.item = item;
    }
    /**
     * @return the itemId
     */
    public Long getItemId() {
        return itemId;
    }
    /**
     * @param itemId the itemId to set
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    /**
     * @return the percentage
     */
    public double getPercentage() {
        return percentage;
    }
    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the dimensionClass
     */
    public Class getDimensionClass() {
        return dimensionClass;
    }

    /**
     * @param dimensionClass the dimensionClass to set
     */
    public void setDimensionClass(Class dimensionClass) {
        this.dimensionClass = dimensionClass;
    }
    
    /**
     * @return the hierarchyPurpoase
     */
    public boolean getHierarchyPurpoase() {
        return hierarchyPurpoase;
    }
    /**
     * @param hierarchyPurpoase the hierarchyPurpoase to set
     */
    public void setHierarchyPurpoase(boolean hierarchyPurpoase) {
        this.hierarchyPurpoase = hierarchyPurpoase;
    }
    @Override
    public String toString() {
        return this.columnType + " | " + this.item + " | " + this.itemId + " | " + this.percentage ;
    }
    
}
