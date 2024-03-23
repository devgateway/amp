package org.dgfoundation.amp.nireports.amp.dimensions;

import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

import java.util.Arrays;

/**
 * 
 * an <i>ni_all_locations_with_levels</i>-backed dimension consisting of all locations [level=0] 
 * @author Viorel Chihai
 *
 */
public final class RawLocationsDimension extends SqlSourcedNiDimension {
    
    public static final RawLocationsDimension INSTANCE = new RawLocationsDimension("rawLocs");
    
    private RawLocationsDimension(String name) {
        super(name, "ni_all_locations_with_levels", Arrays.asList("acvl_id"));
    }

}
