package org.digijava.kernel.ampapi.helpers.geojson;

import java.util.List;

/**
 * Interface for objects to be passed to the isValid method of the
 * Validation interface.
 * 
 * @see org.digijava.kernel.ampapi.helpers.geojson.belteshazzar.geojson.Validation
 */
public interface PositionValidator
{
    /**
     * Test if the position is valid.
     * 
     * @param position
     * @return true if position is valid.
     */
    boolean isValid(List<Double> position);

    /**
     * Test if the bounding box is valid.
     * 
     * @param bbox
     * @return true if bbox is valid.
     */
    boolean isValidBB(List<Double> bbox);

    /**
     * Assumes that isValid(p1) and isValid(p2) return true.
     * @param p1
     * @param p2
     * @return true if p1 and p2 are equivalent.
     */
    boolean isEquivalent(List<Double> p1, List<Double> p2);
}
