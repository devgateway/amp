package org.digijava.module.contentrepository.dbentity.template;

import java.util.Set;
/**
 * template for document
 * @author Dare
 *
 */
public class TemplateDoc {
    
    private Long id;
    private String name;
    private Set<TemplateField> fields;
    
    public TemplateDoc(){
        
    }
    
    public TemplateDoc(String name){
        this.name=name;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<TemplateField> getFields() {
        return fields;
    }
    public void setFields(Set<TemplateField> fields) {
        this.fields = fields;
    }
    
    
}
