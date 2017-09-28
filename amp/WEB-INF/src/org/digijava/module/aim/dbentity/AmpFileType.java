package org.digijava.module.aim.dbentity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Stores allowed mimetype
 * 
 * @author Viorel Chihai
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AmpFileType {

    Long id;

    @JsonProperty("name")
    String name;

    @JsonProperty("description")
    String description;

    @JsonProperty("mimeTypes")
    List<String> mimeTypes;
    
    @JsonProperty("extensions")
    List<String> extensions;
    

    public AmpFileType() {
    }

    public AmpFileType(String name, String description, List<String> mimeTypes, List<String> extensions) {
        super();
        this.name = name;
        this.description = description;
        this.mimeTypes = mimeTypes;
        this.extensions = extensions;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getExtensions() {
        return extensions;
    }
    
    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }
    
}
