package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public class GroupColumn extends Column {
	public final List<Column> subColumns;
	
	public GroupColumn(String name, List<Column> subColumns) {
		super(name);
		this.subColumns = Collections.unmodifiableList(new ArrayList<>(subColumns)); 
	}
	
	
	
}
