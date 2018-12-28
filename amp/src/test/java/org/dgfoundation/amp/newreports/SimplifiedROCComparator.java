package org.dgfoundation.amp.newreports;

import java.util.Comparator;

import org.dgfoundation.amp.newreports.ReportOutputColumn;

public class SimplifiedROCComparator implements Comparator<ReportOutputColumn> {

    @Override
    public int compare(ReportOutputColumn o1, ReportOutputColumn o2) {
        //int delta = o1.originalColumnName.compareTo(o2.originalColumnName);
        int delta = generateDisplayedName(o1).compareTo(generateDisplayedName(o2));
        if (delta != 0) return delta;
        return delta;
//      delta = TeamMemberUtil.nullCompare(o1.parentColumn, o2.parentColumn);
//      if (delta != 0) return delta;
//      if (o1.parentColumn == null)
//          return 0;
//      return compare(o1.parentColumn, o2.parentColumn);
    }
    
    public static String generateDisplayedName(ReportOutputColumn colKey) {
        //return colKey.getHierarchicalName();
        if (colKey == null)
            return "null";
        if (colKey.parentColumn == null)
            return removeNiPrefix(colKey.originalColumnName == null ? "<null>" : colKey.originalColumnName);
        return removeNiPrefix(String.format("%s-%s", generateDisplayedName(colKey.parentColumn), colKey.originalColumnName));
    }

    static String removeNiPrefix(String in) {
        if (in == null) return in;
        return in;
//      if (in.startsWith("Funding-"))
//          return in.substring("Funding-".length());
//      if (in.startsWith("Total Measures-"))
//          return "Totals-" + in.substring("Total Measures-".length());
//      return in;
    }
}

