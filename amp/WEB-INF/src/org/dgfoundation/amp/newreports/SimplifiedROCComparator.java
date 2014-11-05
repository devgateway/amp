package org.dgfoundation.amp.newreports;

import java.util.Comparator;

public class SimplifiedROCComparator implements Comparator<ReportOutputColumn> {

	@Override
	public int compare(ReportOutputColumn o1, ReportOutputColumn o2) {		
		return o1.originalColumnName.compareTo(o2.originalColumnName);
	}

}
