package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class TeamPagesForm extends ActionForm {

		  private Collection pages;
		  private Collection filters;
		  private Long selFilters[] = null;
		  private String pageName;
		  private Long pageId;
		  private boolean updated = false;

		  public boolean getUpdated() {
					 return updated;
		  }

		  public void setUpdated(boolean flag) {
					 updated = flag;
		  }
		  
		  public Long[] getSelFilters() { return selFilters; }

		  public void setSelFilters(Long selFilters[]) {
					 this.selFilters = selFilters;
		  }
		  
		  public Long getPageId() { return pageId; }

		  public void setPageId(Long pageId) {
					 this.pageId = pageId;
		  }
		  
		  public Collection getPages() {
					 return pages;
		  }

		  public Collection getFilters() {
					 return filters;
		  }

		  public void setPages(Collection pages) {
					 this.pages = pages;
		  }

		  public void setFilters(Collection filters) {
					 this.filters = filters;
		  }

		  public String getPageName() {
					 return pageName;
		  }

		  public void setPageName(String pageName) {
					 this.pageName = pageName;
		  }

}
