package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.algo.AmpCollections;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * a ReportData-to-String outputter useful for debugging and/or testcases
 * @author Dolghier Constantin
 *
 */
public class DebugOutputReportDataVisitor implements ReportDataVisitor<String> {

    int depth = 0;
    
    @Override
    public String visitLeaf(ColumnReportData crd) {
        Map<String, String> children = AmpCollections.remap(crd.contents, CellColumn::getHierName, ColumnContents::debugString, true);
        StringBuilder res = new StringBuilder(prefix() + "crd " + crd.toString() + " {");
        
        boolean first = true;
        for(Map.Entry<String, String> child:children.entrySet()) {
            //if (!first) res.append(prefix() + ",");
            res.append(String.format("\n%s%s -> %s", prefix(1), child.getKey(), child.getValue()));
            first = false;
        }
        res.append(String.format("\n%s}", prefix()));
        return res.toString();
    }

    @Override
    public String visitGroup(GroupReportData grd) {
        depth ++;
        List<String> children = grd.subreports.stream().map(z -> z.accept(this)).collect(Collectors.toList());
        depth --;
        StringBuilder res = new StringBuilder(prefix() + grd.toString() + ":");
        for(String child:children)
            res.append("\n" + child);
        return res.toString();
//      return String.format("%s%s:%s", prefix(), grd.toString(), children.toString());
    }
    
    protected String prefix() {
        return prefix(0);
    }
    
    protected String prefix(int _d) {
        int d = depth + _d;
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < d; i++)
            res.append("  ");
        return res.toString();
    }

}
