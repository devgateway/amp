package org.dgfoundation.amp.newreports;

/**
 * class describing an entity type which can have columns / measures attached
 * @author Dolghier Constantin
 *
 */
public enum ReportEntityType {
	
	/**
	 * a regular activity
	 */
	ENTITY_TYPE_ACTIVITY,
	
	/**
	 * a pledge
	 */
	ENTITY_TYPE_PLEDGE, 
	
	/**
	 * a South-South-Cooperation project
	 */
	ENTITY_TYPE_SSC, 
	
	/**
	 * a MTEF projection
	 */
	ENTITY_TYPE_MTEF,
	
	/**
	 * "any", for attributes common to all of these - like Sector
	 */
	ENTITY_TYPE_ALL;
	
	public char getAsChar() {
		switch(this) {
			case ENTITY_TYPE_ACTIVITY: return 'A';
			case ENTITY_TYPE_PLEDGE: return 'P';
			case ENTITY_TYPE_SSC: return 'S';
			case ENTITY_TYPE_MTEF: return 'M';
			case ENTITY_TYPE_ALL: return 'Z';
			default:
				throw new RuntimeException("unknown ReportEntityType: " + this);
		}
	}
}
