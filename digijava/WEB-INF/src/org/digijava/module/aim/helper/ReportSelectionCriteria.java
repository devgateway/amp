package org.digijava.module.aim.helper;

import java.util.Collection;

public class ReportSelectionCriteria {

		  private Collection columns;
		  private Long measure;

		  public void setColumns(Collection c) {
					 columns = c;
		  }

		  public void setMeasure(Long l) {
					 measure = l;
		  }

		  public Collection getColumns() { return columns; }

		  public Long getMeasure() { return measure; }

		  
}
