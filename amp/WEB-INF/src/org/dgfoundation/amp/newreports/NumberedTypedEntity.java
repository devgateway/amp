package org.dgfoundation.amp.newreports;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * class describing a named entity to be used in a report
 * @author Dolghier Constantin
 *
 */
public class NumberedTypedEntity {
	
	/**
	 * the name of the entity, like "Primary Sector"
	 */
	protected final long entityId;
	
	/**
	 * the type of the entity, like "Pledge" or "Activity". 
	 */
	protected final ReportEntityType entityType;
	
	protected final int hashCode; 
	
	/**
	 * 
	 * @param columnName - the name of the column
	 * @param columnType - the type of entity to which the column pertains, like Pledge / Activity 
	 */
	public NumberedTypedEntity(long entityId, ReportEntityType entityType) {
		this.entityId = entityId;
				
		this.entityType = entityType;
		if (this.entityType == null)
			throw new NullPointerException("entityType cannot be null!");
		
		//entityName and entityType are immutable, thus we can generate once their cumulative hashCode and store it
		this.hashCode = new HashCodeBuilder().append(entityId).append(entityType).toHashCode();
	}
		
	public long getEntityId() {
		return this.entityId;
	}
	
	@Override public boolean equals(Object oth) {
		return this.entityId == (((NumberedTypedEntity) oth).entityId) && this.entityType.equals(((NumberedTypedEntity) oth).entityType);
	}
	
	@Override public int hashCode() {
		return this.hashCode; 
	}
	
	@Override public String toString() {
		return String.format("%s %s", this.entityType, this.entityId);
	}
}
