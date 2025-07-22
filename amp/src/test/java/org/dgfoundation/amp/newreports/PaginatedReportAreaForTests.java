/**
 * 
 */
package org.dgfoundation.amp.newreports;

import org.dgfoundation.amp.newreports.pagination.PaginatedReportArea;

import java.util.Map;

/**
 * 
 * @author Nadejda Mandrescu
 */
public class PaginatedReportAreaForTests extends ReportAreaForTests {

    protected int currentCount = -1;
    protected int totalCount = -1;
        
    public PaginatedReportAreaForTests() {
    }
    
    public PaginatedReportAreaForTests(AreaOwner owner, String...contents) {
        this.owner = owner;
        
        switch(contents.length) {
            case 0:
                return;
                
            default:
                this.contents = buildContents(contents);
        }
    }
    
    
    @Override
    public String getDifferenceAgainst(ReportArea output) {
        String err = super.getDifferenceAgainst(output);
        if (err == null && !PaginatedReportArea.class.isAssignableFrom(output.getClass())) {
            return "not a pagination request, expecting PartialReportArea to be used";
        }
        if (err == null) {
            err = compareCounts(output);
        }
        return err;
    }
    
    public String compareCounts(ReportArea output) {
        /* till this place children content & size are verified =>
         * can simplify on checks and just compare counts
         */
        PaginatedReportArea partial = (PaginatedReportArea) output;
        junit.framework.TestCase.assertEquals("currentCount", this.currentCount, partial.getCurrentLeafActivitiesCount());
        junit.framework.TestCase.assertEquals("totalCount", this.totalCount, partial.getTotalLeafActivitiesCount());
        return null;
    }
    
    public PaginatedReportAreaForTests withCounts(int currentCount, int totalCount) {
        this.currentCount = currentCount;
        this.totalCount = totalCount;
        return this;
    }

    @Override
    public PaginatedReportAreaForTests withOwner(AreaOwner owner) {
        super.withOwner(owner);
        return this;
    }
    
    @Override
    public PaginatedReportAreaForTests withChildren(ReportArea...children) {
        super.withChildren(children);
        return this;
    }

    @Override
    public PaginatedReportAreaForTests withContents(Map<ReportOutputColumn, ReportCell> contents) {
        super.withContents(contents);
        return this;
    }
    
    @Override
    public PaginatedReportAreaForTests withContents(String...contents) {
        super.withContents(contents);
        return this;
    }
    
}
