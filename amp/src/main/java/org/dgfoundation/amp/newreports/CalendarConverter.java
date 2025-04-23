package org.dgfoundation.amp.newreports;

import org.dgfoundation.amp.nireports.TranslatedDate;

import java.util.Date;

public interface CalendarConverter {
    
    public TranslatedDate translate(Date date, String prefix);

    public boolean getIsFiscal();

    public String getName();

    public Long getIdentifier();

    public String getDefaultFiscalYearPrefix();
    
    int parseYear(String year, String prefix);
    
    int parseYear(String year);

}
