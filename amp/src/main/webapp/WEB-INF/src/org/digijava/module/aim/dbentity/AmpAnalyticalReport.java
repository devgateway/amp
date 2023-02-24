/*
 * AmpTeam.java
 * Created: 03-Sep-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpAnalyticalReport implements Serializable {

    private static final long serialVersionUID = 1143719618673288389L;

    private Long id;
    private String data;
    private String name;
    private AmpTeamMember owner;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public AmpTeamMember getOwner() {
        return owner;
    }
    public void setOwner(AmpTeamMember owner) {
        this.owner = owner;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}

