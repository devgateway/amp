package org.dgfoundation.amp.nireports.testcases;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.testcases.generic.GregorianTestDateGenerator;

/**
 * Calendar used by the HardcodedReportsTestSchema
 * @author acartaleanu
 *
 */
public class TestCalendar implements CalendarConverter {

	@Override
	public TranslatedDate translate(Date date, String prefix) {
		Date input = new Date();
		LocalDate ld= input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return new GregorianTestDateGenerator(ld.getYear(), ld.getMonth()).toTranslatedDate();
	}

	@Override
	public boolean getIsFiscal() {
		return false;
	}

	@Override
	public String getName() {
		return "test calendar";
	}

	@Override
	public Long getIdentifier() {
		return 1l;
	}

	@Override
	public String getDefaultFiscalYearPrefix() {
		return "FY";
	}

}
