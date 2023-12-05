package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpLocation;

import java.io.Serializable;

public class AmpPrgIndicatorValue implements Serializable
{
    //TODO INDIC rename this bean to IndicatorValue - remove Prg and AMP from name.
    private Long indicatorValueId;
    private String creationDate;
    private int valueType;
    private Double valAmount;
    private AmpLocation location;

    /**
     * @return Returns the indicatorValueId.
     */
    public Long getIndicatorValueId() {
        return indicatorValueId;
    }
    /**
     * @param indicatorValueId The indicatorValueId to set.
     */
    public void setIndicatorValueId(Long indicatorValueId) {
        this.indicatorValueId = indicatorValueId;
    }
    /**
     * @return Returns the creationDate.
     */
    public String getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * @return Returns the valueType.
     */
    public int getValueType() {
        return valueType;
    }
    /**
     * @param valueType The valueType to set.
     */
    public void setValueType(int valueType) {
        this.valueType = valueType;
    }
    /**
     * @return Returns the valAmount.
     */
    public Double getValAmount() {
        return valAmount;
    }
    /**
     * @param valAmount The valAmount to set.
     */
    public void setValAmount(Double valAmount) {
        this.valAmount = valAmount;
    }
    public AmpLocation getLocation() {
        return location;
    }
    public void setLocation(AmpLocation location) {
        this.location = location;
    }
}
