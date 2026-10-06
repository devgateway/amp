package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Concrete class for formatting and parsing dates in locales supported by Java 8.
 * 
 * @author acartaleanu
 *
 */
public class AmpSimpleDateFormatter extends AmpDateFormatter {
    
    protected AmpSimpleDateFormatter(Locale locale, String pattern) {
        super(locale, pattern);
    }
    
    @Override
    public String format(LocalDate date) {
        return dtf.format(date);
    }

    @Override
    public LocalDate parseDate(String in) {
        return dtf.parse(in, LocalDate::from);
    }
}
