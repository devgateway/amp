package org.digijava.kernel.ampapi.endpoints.gis;

import org.digijava.kernel.ampapi.endpoints.gis.services.AdmLevel;

/**
 * @author Octavian Ciubotaru
 */
public class Cluster {

    private Long id;

    private String title;

    private AdmLevel adminLevel;

    public Cluster(Long id, String title, AdmLevel adminLevel) {
        this.id = id;
        this.title = title;
        this.adminLevel = adminLevel;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public AdmLevel getAdminLevel() {
        return adminLevel;
    }
}
