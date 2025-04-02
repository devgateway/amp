package org.digijava.module.contentrepository.dbentity.template;
/**
 * holds pre-defined possible value for template fields
 * @author Dare
 *
 */
public class PossibleValue {
    private Long id;
    private String value;
    /**
     * owner field
     */
    private TemplateField field;
    
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
    public TemplateField getField() {
        return field;
    }
    public void setField(TemplateField field) {
        this.field = field;
    }
    
}
