package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

/**
 * a @link {@link ReportCell} containing an amount
 * @author Dolghier Constantin, Nadejda Mandrescu
 */
@JsonIgnoreProperties(value = "")
@JsonFilter(EPConstants.JSON_FILTER_AMOUNT_CELL)
public final class AmountCell extends ReportCell {
    
    public AmountCell(BigDecimal value, String formattedValue) {
        super(value, formattedValue);
    }
    
    public BigDecimal extractValue() {
        return (BigDecimal) value;
    }
    
    protected static String format(DecimalFormat formatter, Double value) {
        if (formatter == null || value == null) 
            return value == null ? "" : String.valueOf(value);
        return formatter.format(value);
    }
    
    @Override
    public String extractFormatType() {
        return ReportColumnFormatType.NUMBER.toString();
    }
}
