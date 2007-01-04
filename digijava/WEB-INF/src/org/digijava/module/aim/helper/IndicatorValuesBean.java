package org.digijava.module.aim.helper;

import java.util.Date;

import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;

public class IndicatorValuesBean {
    Long indicatorValueId;
    int valueTypeId;
    String valueType;
    String value;
    Date creationDate;
    Long indicatorId;

    public IndicatorValuesBean() {

    }

    public IndicatorValuesBean(AmpThemeIndicatorValue dbIndicatorValue) {
        this.indicatorValueId = dbIndicatorValue.getAmpThemeIndValId();
        this.valueTypeId = dbIndicatorValue.getValueType();
//        this.valueType=(db)?
        this.value = dbIndicatorValue.getValueAmount().toString();
        this.creationDate = dbIndicatorValue.getCreationDate();
        this.indicatorId = dbIndicatorValue.getThemeIndicatorId().getAmpThemeIndId();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Long getIndicatorId() {
        return indicatorId;
    }

    public String getValue() {
        return value;
    }

    public String getValueType() {
        return valueType;
    }

    public int getValueTypeId() {
        return valueTypeId;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public void setValueTypeId(int valueTypeId) {
        this.valueTypeId = valueTypeId;
    }

}
