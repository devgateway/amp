package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpOrgGroup;

import java.util.Collection;

public class OrgGroupManagerForm extends ActionForm {

	private Collection<AmpOrgGroup> organisation;
	private Collection pages;
	private Integer currentPage=new Integer(1);
	private String sortBy;
	private String currentAlpha;
	private String[] alphaPages = null;
	private String alpha;
	private Collection<AmpOrgGroup> orgsForCurrentAlpha;
	
	public Collection<AmpOrgGroup> getOrgsForCurrentAlpha() {
		return orgsForCurrentAlpha;
	}

	public void setOrgsForCurrentAlpha(Collection<AmpOrgGroup> orgsForCurrentAlpha) {
		this.orgsForCurrentAlpha = orgsForCurrentAlpha;
	}

	public Collection<AmpOrgGroup> getOrganisation() {
		 return organisation;
	}

	public void setOrganisation(Collection<AmpOrgGroup> organisation) {
		 this.organisation = organisation;
	}

	public Collection getPages() {
		 return pages;
	}

	public void setPages(Collection pages) {
		 this.pages = pages;
	}

	public Integer getCurrentPage() {
		if(currentPage==null) return new Integer(1);
		return currentPage;
	}

	public void setCurrentPage(Integer currentpage) {
		this.currentPage = currentpage;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getCurrentAlpha() {
		return currentAlpha;
	}

	public void setCurrentAlpha(String currentAlpha) {
		this.currentAlpha = currentAlpha;
	}

	public String[] getAlphaPages() {
		return alphaPages;
	}

	public void setAlphaPages(String[] alphaPages) {
		this.alphaPages = alphaPages;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	
}
