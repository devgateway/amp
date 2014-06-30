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
	 * "any", for attributes common to all of these - like Sector
	 */
	ENTITY_TYPE_ALL
}
