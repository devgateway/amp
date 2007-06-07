package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class OrgTypeManagerForm extends ActionForm {

	private Collection organisation;
	private Collection pages;
	
	public Collection getOrganisation() {
		 return organisation;
	}

	public void setOrganisation(Collection organisation) {
		 this.organisation = organisation;
	}

	public Collection getPages() {
		 return pages;
	}

	public void setPages(Collection pages) {
		 this.pages = pages;
	}
	
}
