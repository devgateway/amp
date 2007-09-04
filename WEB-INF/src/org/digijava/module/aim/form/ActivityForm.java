package org.digijava.module.aim.form;

import java.util.List;

import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpActivity;

import java.io.Serializable;

public class ActivityForm extends ActionForm implements Serializable {

	public final String SortByColumn_ActivityName = "activityName";

	private List<AmpActivity> activityList;
	
	private List<AmpActivity> allActivityList;

	private String sortByColumn;

	private String keyword;

	private int page;

	private int pageSize = 10;
	
	private int totalPages;

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


}