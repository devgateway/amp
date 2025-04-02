package org.digijava.kernel.ampapi.endpoints.common.model;

import java.util.Set;

/**
 * @author Octavian Ciubotaru
 */
public class OrgType {

    private Long id;

    private String name;

    private Set<Long> groupIds;

    private String filterId;

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

    public Set<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Set<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }
}
