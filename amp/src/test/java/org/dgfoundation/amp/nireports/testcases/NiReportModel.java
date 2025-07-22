package org.dgfoundation.amp.nireports.testcases;

import junit.framework.TestCase;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaDescriber;
import org.dgfoundation.amp.newreports.ReportAreaForTests;

import java.util.List;

/**
 * a fully-rendered-to-text textual representation of a report NiReportData
 * @author Dolghier Constantin
 *
 */
public class NiReportModel {
    
    public final String name;
    public List<String> headers;
    public List<String> warnings;
    public ReportArea body;
    
    public NiReportModel(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
        
    public NiReportModel withHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    public NiReportModel withWarnings(List<String> warnings) {
        this.warnings = warnings;
        return this;
    }

    public NiReportModel withBody(ReportArea body) {
        this.body = body;
        return this;
    }

    public String compare(NiReportModel other) {
        report_error(compare_list("headers", this.headers, other.headers));
        
        report_error(compare_list("warnings", this.warnings, other.warnings));
        
        ReportAreaForTests corBody = (ReportAreaForTests) body;
        ReportAreaForTests.deltaStack.clear();
        report_error(corBody.getDifferenceAgainst(other.body));
        
        return null;
    }
    
    String compare_list(String tag, List<String> cor, List<String> out) {
        if (cor == null)
            return null; // cor not specified -> not checking
        for(int i = 0; i < Math.min(cor.size(), out.size()); i++) {
            String comp = compareCells(out.get(i), cor.get(i));
            if (comp != null)
                report_error(String.format("%s elem %d: %s", tag, i, comp));
        }
        if (cor.size() != out.size())
            report_error(String.format("%s has %d elems instead of %d", tag, out.size(), cor.size()));
        return null;
    }
    
    protected String compareCells(String cellContents, String correctCell) {
        if (cellContents == null)
            cellContents = "<null>";
        
        if (correctCell == null)
            correctCell = "<null>";
        
        TestCase.assertEquals(correctCell, cellContents);
//      if (!correctCell.equals(cellContents))
//          return String.format("%s instead of %s", cellContents, correctCell);
        
        return null;
    }
    
    public String describeInCode() {
        String str = String.format("new NiReportModel(\"%s\")\n\t.withHeaders(%s)\n\t.withWarnings(%s)\n\t.withBody(%s);",
            this.name, listToCode(this.headers), listToCode(this.warnings), new ReportAreaDescriber(null).describeInCode(this.body, 3)
        );
        return str;
    }
    
    //TODO: should add java-escaping of strings in relist callback
    public String listToCode(List<String> list) {
        if (list == null) return null;
        StringBuilder res = new StringBuilder("Arrays.asList(");
        for(int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            res.append("\n\t\t");
            res.append("\"");
            res.append(item);
            res.append("\"");
            if (i != list.size() - 1)
                res.append(",");
        };
        res.append(")");
        return res.toString();
    }
    
    public String report_error(String str) {
        if (str != null)
            throw new RuntimeException(str);
        
        return str;
    }
    
    @Override
    public String toString() {
        return String.format("cor %s", name);
    }
}
