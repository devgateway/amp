package org.dgfoundation.amp.newreports;

import java.time.LocalDate;
import java.util.Map;

import org.dgfoundation.amp.algo.AmpCollections;

/**
 * a @link {@link ReportCell} containing a number of dates 
 * @author Dolghier Constantin
 *
 */
public final class DateCell extends IdentifiedReportCell {

    public DateCell(Comparable<?> comparableToken, String formattedValue, long entityId, Map<Long, LocalDate> entitiesIdsValues) {
        super(comparableToken, formattedValue, entityId, AmpCollections.remap(entitiesIdsValues, date -> String.valueOf(date), null));
    }
    
    @Override
    public String extractFormatType() {
        return ReportColumnFormatType.DATE.toString();
    }
}
