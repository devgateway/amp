package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.List;

public class Programs {
    Long id;
    String description;
    List<Programs> programs;
    public Programs(){
        
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Programs> getPrograms() {
        return programs;
    }
    public void setPrograms(List<Programs> programs) {
        this.programs = programs;
    }
    
}
