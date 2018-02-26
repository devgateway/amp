package org.dgfoundation.amp.onepager.converters;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;

public class CoordinateDoubleConverter extends DoubleConverter {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * It is enough to have 5 decimal digits for GIS numbers. 
     * See: https://en.wikipedia.org/wiki/Decimal_degrees
     */
    public static final int DOUBLE_FRACTION_PRECISION = 8;
    
    public static final IConverter<Double> INSTANCE = new CoordinateDoubleConverter();

    @Override
    public NumberFormat getNumberFormat(Locale locale) {
        NumberFormat numberFormat = super.getNumberFormat(locale);
        numberFormat.setMaximumFractionDigits(DOUBLE_FRACTION_PRECISION);
        
        return (NumberFormat) numberFormat.clone();
    }
    
}
