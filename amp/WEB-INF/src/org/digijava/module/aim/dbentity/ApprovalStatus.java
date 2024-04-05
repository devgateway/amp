package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Identifiable;

/**
 *
 * Change ids together with v_approval_status.xml, v_filtered_activity_status.xml.
 *
 * @author Octavian Ciubotaru
 */
public enum ApprovalStatus implements Identifiable {

    /**
     * In use. Not supported by reports. Should be removed in favor of started.
     */
    created("created", 0),
    approved("approved", 1),
    edited("edited", 2),
    startedapproved("startedapproved", 3),
    started("started", 4),
    /**
     * Not in use. Synonym to rejected?
     */
    not_approved("not_approved", 5),
    rejected("rejected", 6);

    private final String dbName;
    private final Integer id;

    ApprovalStatus(String dbName, Integer id) {
        this.dbName = dbName;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getDbName() {
        return dbName;
    }

    @Override
    public Object getIdentifier() {
        return dbName;
    }

    public static ApprovalStatus fromId(Integer id) {
        for (ApprovalStatus value : values()) {
            if (value.id.equals(id)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown approval status: " + id);
    }

    public static boolean isValid(Integer id) {
        for (ApprovalStatus value : values()) {
            if (value.id.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
