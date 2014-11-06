package org.dgfoundation.amp.newreports;

import java.util.Comparator;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;

public class SimplifiedROCComparator implements Comparator<ReportOutputColumn> {

	@Override
	public int compare(ReportOutputColumn o1, ReportOutputColumn o2) {		
		//int delta = o1.originalColumnName.compareTo(o2.originalColumnName);
		int delta = MondrianReportsTestCase.generateDisplayedName(o1).compareTo(MondrianReportsTestCase.generateDisplayedName(o2));
		if (delta != 0) return delta;
		return delta;
//		delta = TeamMemberUtil.nullCompare(o1.parentColumn, o2.parentColumn);
//		if (delta != 0) return delta;
//		if (o1.parentColumn == null)
//			return 0;
//		return compare(o1.parentColumn, o2.parentColumn);
	}
}
