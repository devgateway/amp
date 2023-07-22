package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.services.AmpOfflineVersion;

/**
 * @author Octavian Ciubotaru
 */
import javax.persistence.*;

@Entity
@Table(name = "amp_offline_compatible_version_range")
public class AmpOfflineCompatibleVersionRange {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_offline_compatible_version_range_seq")
    @SequenceGenerator(name = "amp_offline_compatible_version_range_seq", sequenceName = "amp_offline_compatible_version_range_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @JsonProperty("from-version")

    @Column(name = "from_version", nullable = false)
    private AmpOfflineVersion fromVersion;
    @JsonProperty("to-version")

    @Column(name = "to_version", nullable = false)
    private AmpOfflineVersion toVersion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpOfflineVersion getFromVersion() {
        return fromVersion;
    }

    public void setFromVersion(AmpOfflineVersion fromVersion) {
        this.fromVersion = fromVersion;
    }

    public AmpOfflineVersion getToVersion() {
        return toVersion;
    }

    public void setToVersion(AmpOfflineVersion toVersion) {
        this.toVersion = toVersion;
    }
}
