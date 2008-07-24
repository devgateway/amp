package org.digijava.module.widget.table;

import java.util.List;

public interface FilterItemProvider {
	List<FilterItem> getItems();
	FilterItem getItem(Long id);
}
