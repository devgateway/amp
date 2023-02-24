package org.digijava.module.aim.helper;

import java.util.Date;

import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;

public class IndicatorValuesBean {
    private Long indicatorValueId;
    private int valueTypeId;
    private String valueType;
    private String value;
    private Date creationDate;
    private Long indicatorId;
    private Long connectionId;

    public IndicatorValuesBean() {

    }

    public IndicatorValuesBean(AmpIndicatorValue value){
        this.indicatorValueId = value.getIndValId();
        this.valueTypeId = value.getValueType();
        this.value = value.getValue().toString();
        this.creationDate = value.getValueDate();
        this.indicatorId = value.getIndicatorConnection().getIndicator().getIndicatorId();
        this.connectionId = value.getIndicatorConnection().getId();
    }
    
    @Deprecated
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

    public Long getIndicatorValueId() {
        return indicatorValueId;
    }

    public void setIndicatorValueId(Long indicatorValueId) {
        this.indicatorValueId = indicatorValueId;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }

}
