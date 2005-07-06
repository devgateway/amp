package org.digijava.module.aim.dbentity;

/**
 * author Priyajith 18-oct-04
 */

import java.io.Serializable;

public class AmpTeamPageFilters implements Serializable {

	private AmpPages pages;
	private AmpFilters filters;

	public AmpTeamPageFilters() {
	}

	public AmpTeamPageFilters(AmpPages page, AmpFilters filter) {
		setPages(page);
		setFilters(filter);
	}

	public AmpPages getPages() {
		return pages;
	}

	public AmpFilters getFilters() {
		return filters;
	}

	public void setPages(AmpPages pages) {
		this.pages = pages;
	}

	public void setFilters(AmpFilters filters) {
		this.filters = filters;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		
		if (!(obj instanceof AmpTeamPageFilters)) throw new ClassCastException();
		
		AmpTeamPageFilters tpf = (AmpTeamPageFilters) obj;

		return (tpf.getFilters().getAmpFilterId().equals(filters.getAmpFilterId()) &&
			tpf.getPages().getAmpPageId().equals(pages.getAmpPageId()));
		
	}
}
