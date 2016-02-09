package org.dgfoundation.amp.newreports;

import org.digijava.kernel.translator.TranslatorWorker;

/**
 * class describing a named entity to be used in a report
 * @author Dolghier Constantin
 *
 */
public abstract class NamedTypedEntity {
	
	/**
	 * the name of the entity, like "Primary Sector"
	 */
	protected final String entityName;
		
	protected final int hashCode;
	
	protected final String description;
	
	/**
	 * 
	 * @param columnName - the name of the column
	 */
	public NamedTypedEntity(String entityName, String description) {
		this.entityName = entityName;
		if (this.entityName == null || this.entityName.isEmpty())
			throw new NullPointerException("columnName cannot be null or empty!");			
		
		//entityName and entityType are immutable, thus we can generate once their cumulative hashCode and store it
		this.hashCode = entityName.hashCode();
		this.description = description;
	}
	
	public String getEntityName() {
		return this.entityName;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getDescriptionTrn() {
		return TranslatorWorker.translateText(this.description);
	}
	@Override public boolean equals(Object oth) {
		return this.entityName.equals(((NamedTypedEntity) oth).entityName);
	}
	
	@Override public int hashCode() {
		return this.hashCode; 
	}	
	
	@Override public String toString() {
		return String.format("entity: %s", this.getEntityName());
	}
}
