package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivity;

public class ActivityForm extends ActionForm implements Serializable {

	public final String SortByColumn_ActivityName = "activityName";

	private List<AmpActivity> activityList;
	
	private List<AmpActivity> allActivityList;

	private String sortByColumn;

	private String keyword;
	private String lastKeyword;
	
	private int tempNumResults = 10;
	
	private int page;

	private int pageSize = 10;
	
	private int totalPages;

    private int pagesToShow;
    private int offset;
    private Integer currentPage;
    private Collection pages = null;
    
	/**
	 * @return Returns the activityList.
	 */
	public List<AmpActivity> getActivityList() {
		return activityList;
	}

	/**
	 * @param activityList
	 *            The activityList to set.
	 */
	public void setActivityList(List<AmpActivity> activityList) {
		this.activityList = activityList;
	}

	public void setSortByColumn(String sortByColumn) {
		this.sortByColumn = sortByColumn;
	}

	public String getSortByColumn() {
		return sortByColumn;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}
	
	public int getTempNumResults() {
		return tempNumResults;
	}

	public void setTempNumResults(int tempNumResults) {
		this.tempNumResults = tempNumResults;
	}


	public void setPage(int page) {
		this.page = page;
		
	}
	
	public int getPage() {
		return page;
	}

	public void setAllActivityList(List<AmpActivity> allActivityList) {
		this.allActivityList = allActivityList;
	}

	public List<AmpActivity> getAllActivityList() {
		return allActivityList;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalPages() {
		return totalPages;
	}
    /**
     * 
     * @return pagesToShow
     */
    public int getPagesToShow() {
    	return pagesToShow;
    }
    
    /**
     * 
     * @param pagesToShow
     */
    
    public void setPagesToShow(int pagesToShow) {
    	this.pagesToShow = pagesToShow;
    }
    /**
     * 
     * @return value of pagination star
     */
    public int getOffset() {
    	int value;
    	if (getCurrentPage()> (this.getPagesToShow()/2)){
    		value = (this.getCurrentPage() - (this.getPagesToShow()/2))-1;
    	}
    	else {
    		value = 0;
    	}
    	setOffset(value);
    	return offset;
    }
    
    /**
     * 
     * @param offset
     */
    public void setOffset(int offset) {
    	this.offset = offset;
    }
    
    /**
	 * @return Returns the currentPage.
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage The currentPage to set.
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	
	public Collection getPages() {
	   return pages;
	}
	  
	public void setPages(Collection pages) {
	    this.pages = pages;
	  }

	public String getLastKeyword() {
		return lastKeyword;
	}

	public void setLastKeyword(String lastKeyword) {
		this.lastKeyword = lastKeyword;
	}


}