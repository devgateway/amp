package org.digijava.kernel.ampapi.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class Language {

    private String id;

    private String name;

    @JsonProperty("ltr-direction")
    private boolean ltrDirection;
    
    public Language(String id, String name, boolean ltrDirection) {
        this.id = id;
        this.name = name;
        this.ltrDirection = ltrDirection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLtrDirection() {
        return ltrDirection;
    }

    public void setLtrDirection(boolean ltrDirection) {
        this.ltrDirection = ltrDirection;
    }
}
