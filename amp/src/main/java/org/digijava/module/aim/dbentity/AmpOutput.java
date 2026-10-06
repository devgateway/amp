package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpOutput implements Serializable {
    private Long id;
    private String name;
    private String description;
    private AmpOutcome outcome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AmpOutcome getOutcome() { return outcome; }
    public void setOutcome(AmpOutcome outcome) { this.outcome = outcome; }
}
