package org.digijava.kernel.ampapi.endpoints.common.model;

import java.util.Set;

/**
 * @author Octavian Ciubotaru
 */
public class OrgGroup {

    private Long id;

    private String name;

    private Long typeId;

    private Set<Long> orgIds;

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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Set<Long> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Set<Long> orgIds) {
        this.orgIds = orgIds;
    }
}
