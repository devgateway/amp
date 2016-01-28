package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * a dimension consisting of (TOP_SECTOR[level=0], SUB_SECTOR[level=1], SUB_SUB_SECTOR[level=2]) 
 * @author Dolghier Constantin
 *
 */
public final class ProgramsDimension extends SqlSourcedNiDimension {
	
	public final static ProgramsDimension instance = new ProgramsDimension("progs");
	
	private ProgramsDimension(String name) {
		super(name, "ni_all_programs_with_levels", Arrays.asList("id0", "id1", "id2", "id3", "id4", "id5", "id6", "id7", "id8"));
	}
}