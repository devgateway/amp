package org.digijava.module.widget.dbentity;

public class AmpDaColumnFilter extends AmpDaColumn{

	private static final long serialVersionUID = 1L;
	
	private Long filterItemProvider;

	public void setFilterItemProvider(Long filterItemProvider) {
		this.filterItemProvider = filterItemProvider;
	}

	public Long getFilterItemProvider() {
		return filterItemProvider;
	}
}
