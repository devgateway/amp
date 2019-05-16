package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.util.Locale;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.EasternArabicService;

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
        if (EasternArabicService.getInstance().isLocaleEasternArabic(TLSUtils.getCurrentSystemLocale())) {
            return EasternArabicService.getInstance().convertWesternArabicToEasternArabic(dtf.format(date));
        }
    
        return dtf.format(date);
    }
    
    @Override
    public LocalDate parseDate(String in) {
        if (EasternArabicService.getInstance().isLocaleEasternArabic(TLSUtils.getCurrentSystemLocale())) {
            return dtf.parse(EasternArabicService.getInstance().convertEasternArabicToWasternArabic(in),
                    LocalDate::from);
        }
        
        return dtf.parse(in, LocalDate::from);
    }
    
}
