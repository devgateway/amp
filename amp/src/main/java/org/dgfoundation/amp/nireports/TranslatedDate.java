package org.dgfoundation.amp.nireports;

/**
 * a class which is used for stored the result of translating a LocalDate into some schema-specific calendar and then stripping the "day of month" part
 * Calendars can inflict any type of change to a date, including:
 * <ul>
 * <li>renaming months</li>
 * <li>shifting months (so that, for example, September is the first month of the year)</li>
 * <li>have years with a non-12 number-of-months</li>
 * <li>have years with a "non-number number". For example <i>FY 2013/2014</i> is a valid year name in certain calendars</li>
 * </ul>.
 * Thus, the generic encoding of a year translated into any calendar is as follows: <br />
 * A {@link TranslatedDate} has 3 parts: {@link #year}, {@link #quarter} and {@link #month}. Each of these is a {@link ComparableValue}<String>, thus both arbitrary ordering and arbitrary displaying is supported. <br />
 * A total ordering is achieved by sorting on the 3-tuple ({@link #year}, {@link #quarter}, {@link #month}). 2 instances are considered equal iff the result of comparing them returns 0. <br />
 * @author Dolghier Constantin
 */
public class TranslatedDate implements Comparable<TranslatedDate> {
    public final ComparableValue<String> year;
    public final ComparableValue<String> month;
    public final ComparableValue<String> quarter;
    private final Long rawQuarter;
    private final Long rawYear;

    public TranslatedDate(int year, String yearStr, int quarter, int month, String monthName) {
        this(new ComparableValue<>(yearStr, year), (long) year,
                new ComparableValue<>(String.format("Q%d", quarter), quarter), (long) quarter,
                new ComparableValue<>(monthName, month));
    }
    
    public TranslatedDate(ComparableValue<String> year, Long rawYear, ComparableValue<String> quarter, Long rawQuarter,
            ComparableValue<String> month) {
        this.year = year;
        this.rawYear = rawYear;
        this.month = month;
        this.quarter = quarter;
        this.rawQuarter = rawQuarter;
    }

    public TranslatedDate withMonth(ComparableValue<String> newMonth) {
        return new TranslatedDate(this.year, this.rawYear, this.quarter, rawQuarter, newMonth);
    }

    @Override
    public String toString() {
        return "TranslatedDate [year=" + year + ", month=" + month + ", quarter=" + quarter + "]";
    }

    @Override
    public int compareTo(TranslatedDate o) {
        int deltaYear = year.compareTo(o.year);
        if (deltaYear != 0) return deltaYear;
        
        int deltaQuarter = quarter.compareTo(o.quarter);
        if (deltaQuarter != 0) return deltaQuarter;
        
        int deltaMonth = month.compareTo(o.month);
        return deltaMonth;
    }
    
    @Override
    public int hashCode() {
        return year.hashCode() + 37 * quarter.hashCode() + 967 * month.hashCode();
    }
    
    @Override
    public boolean equals(Object oth) {
        TranslatedDate o = (TranslatedDate) oth;
        return year.equals(o.year) && quarter.equals(o.quarter) && month.equals(o.month);
    }

    public Long getRawQuarter() {
        return rawQuarter;
    }

    public Long getRawYear() {
        return rawYear;
    }
}
