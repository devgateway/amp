package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class for formatting and parsing dates in locales used by AMP.
 * May support locales unsupported by Java8, 
 * e.g. Tetum (with the help of {@link ExoticDateFormatter}
 * 
 * @author acartaleanu
 *
 */
public abstract class AmpDateFormatter {
    
    
    /**
     * Whitelists supported formats, since formats unused in AMP were not kept in mind 
     * when implementing it (reason: too much unrequested work). 
     */
    protected static Set<String> SUPPORTED_FORMATS = generateSupportedPatterns();
    
    public static Set<String> generateSupportedPatterns() {
        List<String> base = Arrays.asList("dd-MMM-yyyy", "dd-MM-yyyy", "MM-dd-yyyy", "MMM-dd-yyyy", "yyyy-MMM-dd", "yyyy-MM-dd");
        List<String> res = new ArrayList<>();
        for (String el : base) {
            res.add(el.replaceAll("-", "/"));
            res.add(el.replaceAll("-", "."));
            res.add(el);
        }
        //LinkedHashSet to preserve order for testing reasons
        Set<String> set = new LinkedHashSet<>(res);
        return Collections.unmodifiableSet(set);
    }
    
    protected final Locale locale;
    protected final String pattern;
    protected final DateTimeFormatter dtf;
    
    protected AmpDateFormatter(Locale locale, String pattern) {
        this.locale = locale;
        this.pattern = pattern;
        dtf = DateTimeFormatter.ofPattern(this.pattern).withLocale(locale);
    }
    
    public String format(Date date) {
        if (date == null) return null;
        LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return format(ld);
    }
    
    public abstract String format(LocalDate date);
    
    public abstract LocalDate parseDate(String in);
    
}
