package org.digijava.module.aim.helper;

public class MEIndicatorValue {
    private Long indId;
    private double value;
    private String type;
    private String indicatorName;
    
    /**
     * @return Returns the indicatorName.
     */
    public String getIndicatorName() {
        return indicatorName;
    }
    /**
     * @param indicatorName The indicatorName to set.
     */
    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return Returns the value.
     */
    public double getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(double value) {
        this.value = value;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof MEIndicatorValue) {
            MEIndicatorValue meVal = (MEIndicatorValue) obj;
            return meVal.getIndicatorName().equals(indicatorName) 
                    && meVal.getType().equals(type);
        } else throw new ClassCastException();
    }
    /**
     * @return Returns the indId.
     */
    public Long getIndId() {
        return indId;
    }
    /**
     * @param indId The indId to set.
     */
    public void setIndId(Long indId) {
        this.indId = indId;
    }
}
