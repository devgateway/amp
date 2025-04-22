package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.TranslatedDate;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Used by FundingColumnGenerator to create a TranslatedDate from a year+month
 * don't use outside of hardcoded testcases!
 * @author acartaleanu
 *
 */
public class GregorianTestDateGenerator {
    
    LocalDate date;

    public GregorianTestDateGenerator(int year, Month month) {
        date = LocalDate.of(year, month, 1);
    }
    
    public GregorianTestDateGenerator(int year, String month) {
        this.date = parseDate(year, month);
    }
    
    protected static int nrDigits(int year) {
        int nr = 0;
        while (year > 0) {
            nr ++;
            year /= 10;
        }
        return nr;
    }
    
    protected static LocalDate parseDate(int year, String month) {
        char[] y = new char[] {'y', 'y', 'y', 'y', 'y'};
        String yyyy = String.copyValueOf(y, 0, nrDigits(year));
        String dateString = String.format("%d-%s-01", year, month);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(yyyy + "-MMMM-dd", Locale.US);
        return LocalDate.parse(dateString, dtf);
    }
    
    int getQuarter(Month month) {
        switch (month.firstMonthOfQuarter()) {
        case JANUARY:
            return 1;
        case APRIL:
            return 2;
        case JULY:
            return 3;
        case OCTOBER:
            return 4;
        default:
            throw new RuntimeException(".firstMonthOfQuarter returned non-quarter month!");
        }
    }
    
    public TranslatedDate toTranslatedDate() {
        return new TranslatedDate(date.getYear(), String.format("%d", date.getYear()), getQuarter(date.getMonth()), date.getMonth().getValue(), 
                date.getMonth().getDisplayName(TextStyle.FULL, Locale.US));
    }
    
}
