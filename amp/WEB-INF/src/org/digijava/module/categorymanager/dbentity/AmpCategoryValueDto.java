package org.digijava.module.categorymanager.dbentity;

import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.util.Identifiable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents one of the possible values for a certain category with properties reduced only to be showed in the UI
 * @author Diego Rossi
 */
public class AmpCategoryValueDto implements Serializable, Identifiable {
    @PossibleValueId
    private Long id;
    @PossibleValueValue
    private String value;

    public AmpCategoryValueDto(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }


    public String toString() {
        return value;
    }
    public Object getIdentifier() {
        return this.getId();
    }

    public boolean equals(Object o) {
        if (!(o instanceof AmpCategoryValueDto)) {
            return false;
        }
        AmpCategoryValueDto a = (AmpCategoryValueDto) o;
        return this.getId().equals(a.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
