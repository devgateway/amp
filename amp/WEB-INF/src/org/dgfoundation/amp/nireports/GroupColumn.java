package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a column with subcolumns. The column has no cells of its own
 * @author Dolghier Constantin
 *
 */
public class GroupColumn extends Column {
	public final List<Column> subColumns;
	
	public GroupColumn(String name, List<Column> subColumns, GroupColumn parent) {
		super(name, parent);
		this.subColumns = Collections.unmodifiableList(new ArrayList<>(subColumns)); 
	}
}
