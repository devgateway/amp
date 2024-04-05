package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Identifiable;

import java.io.Serializable;
import java.util.Set;

public class AmpActivityGroup implements Serializable, Identifiable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    @JsonIgnore
    private Long ampActivityGroupId;

    @Interchangeable(fieldTitle = "Version", importable = true)
    @JsonProperty(value = "version", access = JsonProperty.Access.READ_ONLY)
    private Long version;

    @JsonIgnore
    private AmpActivityVersion ampActivityLastVersion;
    
    @JsonIgnore
    private boolean autoClosedOnExpiration = false;
    
    @JsonIgnore
    private Set<AmpActivityVersion> activities;

    public Long getAmpActivityGroupId() {
        return ampActivityGroupId;
    }

    public void setAmpActivityGroupId(Long ampActivityGroupId) {
        this.ampActivityGroupId = ampActivityGroupId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public AmpActivityVersion getAmpActivityLastVersion() {
        return ampActivityLastVersion;
    }

    public void setAmpActivityLastVersion(AmpActivityVersion ampActivityLastVersion) {
        this.ampActivityLastVersion = ampActivityLastVersion;
    }

    public Set<AmpActivityVersion> getActivities() {
        return activities;
    }

    public void setActivities(Set<AmpActivityVersion> activities) {
        this.activities = activities;
    }
    
    public void setAutoClosedOnExpiration(boolean autoClosedOnExpiration)
    {
        this.autoClosedOnExpiration = autoClosedOnExpiration;
    }
    
    public boolean getAutoClosedOnExpiration()
    {
        return autoClosedOnExpiration;
    }
    
    @Override
    @JsonIgnore
    public Object getIdentifier() {
        return this.getAmpActivityGroupId();
    }

    @Override
    public AmpActivityGroup clone() {
        try {
            return (AmpActivityGroup) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
