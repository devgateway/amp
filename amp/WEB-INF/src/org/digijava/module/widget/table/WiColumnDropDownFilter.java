package org.digijava.module.widget.table;

public class WiColumnDropDownFilter extends WiColumn {
	private FilterItemProvider provider;

	
	
	public void setProvider(FilterItemProvider provider) {
		this.provider = provider;
	}

	public FilterItemProvider getProvider() {
		return provider;
	}
	
	
	
}
