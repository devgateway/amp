package org.digijava.module.aim.helper;

import java.util.Collection;

public class ReportSelectionCriteria {

		  private Collection columns;
		  private Collection transaction;
		  private Collection adjustment;

		  public void setColumns(Collection c) {
					 columns = c;
		  }

		  public void setTransaction(Collection c) {
					 transaction = c;
		  }

		  public void setAdjustment(Collection c) {
					 adjustment = c;
		  }

		  public Collection getColumns() { return columns; }

		  public Collection getTransaction() { return transaction; }

		  public Collection getAdjustment() { return adjustment; }

		  
}
