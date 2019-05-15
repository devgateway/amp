package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.util.Locale;

import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.helper.EasternArabicUtils;

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
        if (EasternArabicUtils.isLocaleEasternArabic(SiteUtils.getCurrentSystemLocale())) {
            return EasternArabicUtils.convertWesternArabicToEasternArabic(dtf.format(date));
        }
    
        return dtf.format(date);
    }
    
    @Override
    public LocalDate parseDate(String in) {
        if (EasternArabicUtils.isLocaleEasternArabic(SiteUtils.getCurrentSystemLocale())) {
            return dtf.parse(EasternArabicUtils.convertEasternArabicToWasternArabic(in), LocalDate::from);
        }
        
        return dtf.parse(in, LocalDate::from);
    }
    
}
