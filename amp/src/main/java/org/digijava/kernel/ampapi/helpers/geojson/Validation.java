package org.digijava.kernel.ampapi.helpers.geojson;

/**
 * GeoJSON validation interface for checking that the tree of objects is
 * valid.
 */
public interface Validation
{
    /**
     * Checks the validity of this GeoJSON object.
     * 
     * @param validator The object for validating the positions in the GeoJSON
     * object tree.
     * @return true if the GeoJSON object is valid, false if not
     */
    boolean isValid(PositionValidator validator);
}
