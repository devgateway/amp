package org.dgfoundation.amp.newreports;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * class describing a named entity to be used in a report
 * @author Dolghier Constantin
 *
 */
public class NumberedTypedEntity {
    
    /**
     * the id of the entity, like "Primary Sector"
     */
    protected final long entityId;
    

    protected final int hashCode; 
    
    /**
     * 
     * @param columnName - the name of the column
     * @param columnType - the type of entity to which the column pertains, like Pledge / Activity 
     */
    public NumberedTypedEntity(long entityId) {
        this.entityId = entityId;
                        
        //entityName and entityType are immutable, thus we can generate once their cumulative hashCode and store it
        this.hashCode = new HashCodeBuilder().append(entityId).toHashCode();
    }
        
    public long getEntityId() {
        return this.entityId;
    }
    
    @Override public boolean equals(Object oth) {
        return this.entityId == (((NumberedTypedEntity) oth).entityId);
    }
    
    @Override public int hashCode() {
        return this.hashCode; 
    }
    
    @Override public String toString() {
        return String.format("entity_id %d", this.entityId);
    }
}
