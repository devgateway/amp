/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.List;
import java.util.Objects;


/**
 * A report element that can be either a NamedTypedEntity or an element of type YEAR, QUARTER, MONTH
 * @author Nadejda Mandrescu
 */
public class ReportElement {
	public enum ElementType {
		ENTITY,
		YEAR,
		QUARTER,
		MONTH,
		DATE;
	};
	
	/** Report element type. If it is ENTITY type, then {@link #entity} is specified */
	public final ElementType type;
	/** Report column or measure, if {@link #type} is ENTITY. Otherwise null. */
	public final NamedTypedEntity entity;
	/** Report element hierarchy */
	public final List<String> hierarchyPath;
	
	/**
	 * Constructs a report element as a NamedTypedEntity
	 * @param entity
	 */
	public ReportElement(NamedTypedEntity entity) {
		this(entity, ElementType.ENTITY, null);
	}
	
	public ReportElement(NamedTypedEntity entity, List<String> hierarchyPath) {
		this(entity, ElementType.ENTITY, hierarchyPath);
	}
	
	/**
	 * No entity report element
	 * @param type - anything, except ENTITY
	 */
	public ReportElement(ElementType type) {
		this(null, type, null);
	}
	
	/**
	 * No entity report element, that is under the specified hierarchy path, e.g. quarter under ["2011"] hierarchy path 
	 * @param type - anything, except ENTITY
	 * @param hierarchyPath - a list of parent hierarchies the element is expected to be a part of
	 */
	public ReportElement(ElementType type, List<String> hierarchyPath) {
		this(null, type, hierarchyPath);
	}
	
	private ReportElement(NamedTypedEntity entity, ElementType type, List<String> hierarchyPath) {
		this.entity = entity;
		this.type = type;
		this.hierarchyPath = hierarchyPath;
	}

	@Override
	public boolean equals(Object o) {
		return 	Objects.deepEquals(this, o);
	}
	
	@Override
	public String toString() {
		return "ElementType = " + type + ", NamedTypedEntity =[" + (entity == null ? "null]" : entity.getEntityName() + "] of type " + entity.getEntityType());  
	}
}
