package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class ThemeForm extends ActionForm {

		  private Collection themes;
		  private Collection pages;

		  public Collection getThemes() {
					 return (this.themes);
		  }

		  public void setThemes(Collection themes) {
					 this.themes = themes;
		  }

		  public Collection getPages() {
					 return pages;
		  }

		  public void setPages(Collection pages) {
					 this.pages = pages;
		  }

}

