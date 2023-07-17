package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Identifiable;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_ACTIVITY_GROUP")
public class AmpActivityGroup implements Serializable, Identifiable, Cloneable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_activity_group_seq")
    @SequenceGenerator(name = "amp_activity_group_seq", sequenceName = "amp_activity_group_seq", allocationSize = 1)    @Column(name = "amp_activity_group_id")
    private Long ampActivityGroupId;

    @Version
    @Interchangeable(fieldTitle = "Version", importable = true)
    @JsonProperty(value = "version", access = JsonProperty.Access.READ_ONLY)
    private Long version;
    @JsonIgnore
    @Column(name = "autoClosedOnExpiration")
    private Boolean autoClosedOnExpiration;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_activity_last_version_id", insertable = false, updatable = false)
    private AmpActivityVersion ampActivityLastVersion;
    @JsonIgnore
    @OneToMany(mappedBy = "activityGroup", cascade = CascadeType.ALL, orphanRemoval = true)
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
