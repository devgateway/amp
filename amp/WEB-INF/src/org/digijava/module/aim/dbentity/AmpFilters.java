
/**
 * author Priyajith
 * 18-oct-04
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

@Deprecated
public class AmpFilters implements Comparable, Serializable {

		  private Long ampFilterId;
		  private String filterName;
		  private Set pages;

		  public Long getAmpFilterId() { return ampFilterId; }
		  public String getFilterName() { return filterName; }
		  public Set getPages() { return pages; }

		  public void setAmpFilterId(Long ampFilterId) {
					 this.ampFilterId = ampFilterId;
		  }

		  public void setFilterName(String filterName) {
					 this.filterName = filterName;
		  }

		  public void setPages(Set pages) {
					 this.pages = pages;
		  }

		  public int compareTo(Object o) {
					 if (!(o instanceof AmpFilters)) throw new ClassCastException();

					 AmpFilters f = (AmpFilters) o;
					 return (this.filterName.compareTo(f.filterName));
		  }		  
}
