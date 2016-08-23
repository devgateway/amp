package org.dgfoundation.amp.nireports;

/**
 * a class which is used for stored the result of translating a LocalDate into some user-specified calendar
 * @author Dolghier Constantin
 *
 */
public class TranslatedDate {
	public final ComparableValue<String> year;
	public final ComparableValue<String> month;
	public final ComparableValue<String> quarter;
	
	public TranslatedDate(int year, String yearStr, int quarter, int month, String monthName) {
		this(new ComparableValue<>(yearStr, year), new ComparableValue<>(String.format("Q%d", quarter), quarter), new ComparableValue<>(monthName, month));
	}
	
	public TranslatedDate(ComparableValue<String> year, ComparableValue<String> quarter, ComparableValue<String> month) {
		this.year = year;
		this.month = month;
		this.quarter = quarter;
	}

	public TranslatedDate withMonth(ComparableValue<String> newMonth) {
		return new TranslatedDate(this.year, this.quarter, newMonth);
	}

	@Override
	public String toString() {
		return "TranslatedDate [year=" + year + ", month=" + month + ", quarter=" + quarter + "]";
	}

}
