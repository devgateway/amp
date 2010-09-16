package org.digijava.module.search.form;

import org.apache.struts.action.ActionForm;

public class SearchForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String keyword;
	private int queryType;
	private Long resultsPerPage;
	

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
