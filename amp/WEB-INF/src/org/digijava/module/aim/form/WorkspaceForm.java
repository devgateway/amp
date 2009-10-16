package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class WorkspaceForm extends ActionForm {

	private Collection workspaces;

	private Collection pages;

	private int page;
	
	private String workspaceType="all";
	
	private int numPerPage=-1;
	
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public String getWorkspaceType() {
		return workspaceType;
	}

	public void setWorkspaceType(String workspaceType) {
		this.workspaceType = workspaceType;
	}

	/**
	 * @return Returns the page.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page The page to set.
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return Returns the pages.
	 */
	public Collection getPages() {
		return pages;
	}

	/**
	 * @param pages The pages to set.
	 */
	public void setPages(Collection pages) {
		this.pages = pages;
	}

	/**
	 * @return Returns the workspaces.
	 */
	public Collection getWorkspaces() {
		return workspaces;
	}

	/**
	 * @param workspaces The workspaces to set.
	 */
	public void setWorkspaces(Collection workspaces) {
		this.workspaces = workspaces;
	}
	
}
