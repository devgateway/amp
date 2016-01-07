package org.dgfoundation.amp.newreports;

import java.util.Date;

import org.dgfoundation.amp.nireports.TranslatedDate;

public interface CalendarConverter {
	public TranslatedDate translate(Date date);
	public boolean getIsFiscal();
	public String getName();
	public Long getIdentifier();
}
