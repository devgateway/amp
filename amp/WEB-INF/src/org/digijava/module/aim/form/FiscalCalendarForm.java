package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.Vector;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

public class FiscalCalendarForm extends ActionForm {
	
		  private static final long serialVersionUID = 1L;
		  
		  private Collection<AmpFiscalCalendar> fiscalCal;
		  private Collection<Integer> pages;
		  private Vector<String> month;

		  public Collection<AmpFiscalCalendar> getFiscalCal() {
					 return (this.fiscalCal);
		  }

		  public void setFiscalCal(Collection<AmpFiscalCalendar> fiscalCal) {
					 this.fiscalCal = fiscalCal;
		  }

		  public Collection<Integer> getPages() {
					 return pages;
		  }

		  public void setPages(Collection<Integer> pages) {
					 this.pages = pages;
		  }
		  
		  public Vector<String> getMonth() {
			 return month;
		  }

		  public void setMonth(Vector<String> month) {
			 this.month = month;
		  }
}
