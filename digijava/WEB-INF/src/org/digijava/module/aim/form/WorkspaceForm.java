package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class WorkspaceForm extends ActionForm {

		  private Collection workspaces;
		  private Collection pages;

		  public Collection getWorkspaces() {
					 return workspaces;
		  }

		  public void setWorkspaces(Collection workspaces) {
					 this.workspaces = workspaces;
		  }

		  public Collection getPages() {
					 return pages;
		  }

		  public void setPages(Collection pages) {
					 this.pages = pages;
		  }
}
