package org.dgfoundation.amp.newreports;

import java.util.Date;

import org.dgfoundation.amp.nireports.TranslatedDate;

public interface CalendarConverter {
    
    public TranslatedDate translate(Date date, String prefix);

    public boolean getIsFiscal();

    public String getName();

    public Long getIdentifier();

    public String getDefaultFiscalYearPrefix();
    
    int parseYear(String year, String prefix);
    
    int parseYear(String year);

}
