package org.digijava.kernel.job;

/**
 * describes the current state of a "cached" version of a table / view compared to its original<br /> 
 * only <b>Structures</b> are compared, not table contents
 * @author Dolghier Constantin
 *
 */
public enum CachedTableState {
    ORIGINAL_TABLE_MISSING,             // original view/table is missing, e.g. the related AmpColumn is toast
    CACHED_TABLE_MISSING,               // the cached table is totally absent in the database
    CACHED_TABLE_DIFFERENT_STRUCTURE,   // the cached table is present, but its structure is different
    CACHED_TABLE_OK;                        // the cached table is present and has the same structure as the original view/table
}
