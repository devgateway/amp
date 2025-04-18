package org.digijava.module.translation.exotic;

import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Concrete class for formatting and parsing dates in locales unsupported by Java 8.  
 * @author acartaleanu
 *
 */
public class ExoticDateFormatter extends AmpDateFormatter {
    
    private final Pattern dayPattern;
    private final Pattern monthPattern;
    private final Pattern yearPattern;
    
    protected ExoticDateFormatter(Locale locale, String pattern) {
        super(locale, pattern);
        if (!SUPPORTED_FORMATS.contains(pattern))
            throw new IllegalArgumentException("Format " + pattern + " not supported!");
        dayPattern = Pattern.compile("^\\d+");
        yearPattern = Pattern.compile("\\d{4}+");
        monthPattern = Pattern.compile("[a-zA-Z]{3}+");
    }
    
    @Override
    public String format(LocalDate date) {
        if (this.pattern.contains("MMM")) {
            return formatWithMonthName(date);
        } else {
            return dtf.format(date);
        }
    }
    
    @Override
    public LocalDate parseDate(String in) {
        if (this.pattern.contains("MMM")) {
            return parseWithMonthName(in);
        } else {
            return dtf.parse(in, LocalDate::from);
        }
    }
    
    private LocalDate parseWithMonthName(String in) {
        return LocalDate.of(getYears(in), getMonths(in), getDays(in));
    }
    
    private String formatWithMonthName(LocalDate date) {
        String day = String.format("%d", date.getDayOfMonth());
        String monthName = ExoticMonthNames.forLocale(this.locale).getShortMonthName(date.getMonthValue());
        String year = String.format("%d", date.getYear());
        String res = pattern.replace("dd", day);
        res = res.replace("MMM", monthName);
        res = res.replace("yyyy", year);
        return res;
    }


    private int getDays(String in) {
        String patt = this.pattern.toLowerCase();
        int posD = patt.indexOf("dd");
        Matcher m = dayPattern.matcher(in.substring(posD));
        m.find();
        String day = m.group();
        return Integer.parseInt(day);
    }
    
    private int getMonths(String in) {
        Matcher m = monthPattern.matcher(in);
        m.find();
        String monthName = m.group();
        return ExoticMonthNames.forLocale(locale).getMonthNumber(monthName);
    }
    
    private int getYears(String in) {
        /* To avoid unnecessary complications with
         * parsing 1/aug/2014, 1/2014/aug, and other corner cases, 
         * will just grab the only 4-digit number in the string.
         */
        Matcher m = yearPattern.matcher(in);
        m.find();
        String year = m.group();
        return Integer.parseInt(year);
    }
}
