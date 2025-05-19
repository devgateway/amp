package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;

import java.io.Serializable;

public class AmpIndicatorTaggedValue implements Serializable, Cloneable {

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    private AmpIndicatorValue overallValue;

    @Interchangeable(fieldTitle = "Tag")
    private String tag;

    @Interchangeable(fieldTitle = "Value")
    private Double value;

    public AmpIndicatorTaggedValue() {
    }

    public AmpIndicatorTaggedValue(String tag, Double value) {
        this.tag = tag;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpIndicatorValue getOverallValue() {
        return overallValue;
    }

    public void setOverallValue(AmpIndicatorValue overallValue) {
        this.overallValue = overallValue;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }
}
