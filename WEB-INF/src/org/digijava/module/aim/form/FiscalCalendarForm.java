package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.Vector;

import org.apache.struts.action.ActionForm;

public class FiscalCalendarForm extends ActionForm {

		  private Collection fiscalCal;
		  private Collection pages;
		  private Vector month;

		  public Collection getFiscalCal() {
					 return (this.fiscalCal);
		  }

		  public void setFiscalCal(Collection fiscalCal) {
					 this.fiscalCal = fiscalCal;
		  }

		  public Collection getPages() {
					 return pages;
		  }

		  public void setPages(Collection pages) {
					 this.pages = pages;
		  }
		  
		  public Vector getMonth() {
			 return month;
		  }

		  public void setMonth(Vector month) {
			 this.month = month;
		  }
}
