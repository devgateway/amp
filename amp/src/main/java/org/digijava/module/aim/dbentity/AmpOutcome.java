package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AmpOutcome implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Set<AmpOutput> outputs= new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<AmpOutput> getOutputs() { return outputs; }
    public void setOutputs(Set<AmpOutput> outputs) { this.outputs = outputs; }
}
