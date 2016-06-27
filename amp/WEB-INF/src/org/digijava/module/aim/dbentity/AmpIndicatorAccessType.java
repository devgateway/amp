package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpIndicatorAccessType implements Serializable{

	private String label;
    private String value;
    private Long id;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
