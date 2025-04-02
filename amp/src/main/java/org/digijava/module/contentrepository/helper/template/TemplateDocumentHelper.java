package org.digijava.module.contentrepository.helper.template;

import java.util.List;

public class TemplateDocumentHelper {
    private Long id;
    private String name;
    private List<TemplateFieldHelper> fields;
    
    public TemplateDocumentHelper(String name){
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

    public List<TemplateFieldHelper> getFields() {
        return fields;
    }

    public void setFields(List<TemplateFieldHelper> fields) {
        this.fields = fields;
    }   
    
}
