package org.digijava.module.search.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.digijava.module.common.util.DateTimeUtil;

public class SearchForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String keyword;
	private int queryType;
	private Long resultsPerPage;
	private int actSearchKey; //Serach key For Activity Amp-5542
	private boolean searchByDate = false;
	private String fromDate ;
	private String toDate ;
	private int dateSelection;

	public int getDateSelection() {
		return dateSelection;
	}

	public void setDateSelection(int dateSelection) {
		this.dateSelection = dateSelection;
	}


	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public boolean isSearchByDate() {
		return searchByDate;
	}

	public void setSearchByDate(boolean searchByDate) {
		this.searchByDate = searchByDate;
	}

	public int getActSearchKey() {
		return actSearchKey;
	}

	public void setActSearchKey(int actSearchKey) {
		this.actSearchKey = actSearchKey;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setResultsPerPage(Long resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public Long getResultsPerPage() {
		return resultsPerPage;
	}

}
