/**
 * 
 */
package org.dgfoundation.amp.newreports;


/**
 * A report element that can be either a NamedTypedEntity or an element of type YEAR, QUARTER, MONTH
 * @author Nadejda Mandrescu
 */
public class ReportElement {
	public enum ElementType {
		ENTITY,
		YEAR,
		QUARTER,
		MONTH;
	};
	
	/** Report element type. If it is ENTITY type, then {@link #entity} is specified */
	public final ElementType type;
	/** Report column or measure, if {@link #type} is ENTITY. Otherwise null. */
	public final NamedTypedEntity entity;
	
	/**
	 * Constructs a report element as a NamedTypedEntity
	 * @param entity
	 */
	public ReportElement(NamedTypedEntity entity) {
		this.entity = entity;
		this.type = ElementType.ENTITY;
	}
	
	/**
	 * No entity report element
	 * @param type - anything, except ENTITY
	 */
	public ReportElement(ElementType type) {
		this.type = type;
		this.entity = null;
	}
}
