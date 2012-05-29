package org.digijava.module.widget.table.filteredColumn;

import java.util.List;

public interface FilterItemProvider {
	
	static final int DONORS_FILTER = 1;
        static final int ORG_GROUPS  = 2;
	
	List<FilterItem> getItems();
	FilterItem getItem(Long id);
	Long getId();
}
