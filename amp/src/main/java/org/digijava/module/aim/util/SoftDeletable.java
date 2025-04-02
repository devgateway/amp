package org.digijava.module.aim.util;

/**
 * @author acartaleanu
 * Implements objects that can be soft- or pseudo-deleted (hidden from all UI, but still existing in the database)
 */
public interface SoftDeletable {
    public boolean isSoftDeleted();
}
