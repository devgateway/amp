package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * info about the info which has been used to split the parent column
 * @author Dolghier Constantin
 *
 */
public class NiColSplitCell {
	
	/**
	 * the entity type used.
	 * For dates, please see NiReportsEngine.NI_PSEUDOCOLUMN_XXX constants
	 * for when/if crosstab reports will be supported, it will be the column name (from {@link NiReportsSchema#getColumns()})
	 */
	public final String entityType;
	
	/**
	 * an arbitrary value whose interpretation depends on {@link #entityType}
	 */
	public final ComparableValue<String> info;
	
	public NiColSplitCell(String entityType, ComparableValue<String> info) {
		this.entityType = entityType;
		this.info = info;
	}
}
