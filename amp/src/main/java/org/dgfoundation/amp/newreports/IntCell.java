/**
 * 
 */
package org.dgfoundation.amp.newreports;

/**
 * a Long-containing cell of a report
 * @author Constantin Dolghier
 */
public class IntCell extends IdentifiedReportCell {
    
    public IntCell(long value, long entityId) {
        super(value, Long.toString(value), entityId, null);
    }
    
    public IntCell(Long value) {
        this(value, -1);
    }
    
    @Override
    public String extractFormatType() {
        return ReportColumnFormatType.NUMBER.toString();
    }
}
