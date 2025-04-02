

package org.digijava.module.categorymanager.util;

import java.util.List;

/**
 *
 * @author medea
 */
public class PossibleValue {
    @Override
    public String toString() {
        return "PossibleValue [id=" + id + ", value=" + value + ", disable="
                + disable + ", labelCategories=" + labelCategories
                + ", deleted=" + deleted + "]";
    }

    private Long id;
    private String value;
    private boolean disable;
    private List<LabelCategory> labelCategories;
    private boolean deleted;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //whether the value has been marked for deletion in the form
    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<LabelCategory> getLabelCategories() {
        return labelCategories;
    }

    public void setLabelCategories(List<LabelCategory> labelCategories) {
        this.labelCategories = labelCategories;
    }

    
    //whether the value is softdeleted from DB
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    

}
