package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class WorkspaceForm extends ActionForm {

	private Collection workspaces;

	private Collection pages;

	private int page;

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
