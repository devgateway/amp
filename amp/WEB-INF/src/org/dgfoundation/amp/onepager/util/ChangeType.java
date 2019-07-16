package org.dgfoundation.amp.onepager.util;

/**
 * The values are used on activity.changeType field
 */
public enum ChangeType {
    MANUAL("Manual"),
    IMPORT("IMPORT"),
    AMP_OFFLINE("AMP Offline");

    private String value;

    ChangeType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
