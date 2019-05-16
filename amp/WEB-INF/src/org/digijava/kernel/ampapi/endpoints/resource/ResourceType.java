package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.module.aim.util.Identifiable;

public enum ResourceType implements Identifiable {

    LINK(0, ResourceEPConstants.LINK),
    FILE(1, ResourceEPConstants.FILE);

    private Integer id;
    private String name;

    ResourceType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getIdentifier() {
        return id;
    }

    public static ResourceType fromId(Integer id) {
        for (ResourceType value : values()) {
            if (value.id.equals(id)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown resource type: " + id);
    }

    public static boolean isValid(Integer id) {
        for (ResourceType value : values()) {
            if (value.id.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
