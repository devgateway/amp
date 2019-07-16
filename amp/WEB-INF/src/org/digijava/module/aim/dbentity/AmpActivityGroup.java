package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Identifiable;

public class AmpActivityGroup implements Serializable, Identifiable, Cloneable {
    private static final long serialVersionUID = 1L;

    private Long ampActivityGroupId;

    @Interchangeable(fieldTitle = "Version", importable = true)
    private Long version;

    private AmpActivityVersion ampActivityLastVersion;

    private boolean autoClosedOnExpiration = false;
    
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
    
    @Override public Object getIdentifier() {
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
