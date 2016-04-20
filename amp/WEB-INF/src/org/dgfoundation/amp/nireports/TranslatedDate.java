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
		this.year = new ComparableValue<>(yearStr, year);
		this.quarter = new ComparableValue<>(String.format("Q%d", quarter), quarter);
		this.month = new ComparableValue<>(monthName, month);
	}

	@Override
	public String toString() {
		return "TranslatedDate [year=" + year + ", month=" + month + ", quarter=" + quarter + "]";
	}

}
