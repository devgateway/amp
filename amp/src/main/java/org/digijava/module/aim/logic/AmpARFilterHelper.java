package org.digijava.module.aim.logic;

public interface AmpARFilterHelper {

    String createFromYearQuery(Integer fromYear);

    String createToYearQuery(Integer toYear);
    
    String createFromMonthQuery(Integer fromMonth, Integer fromYear);

    String createToMonthQuery(Integer toMonth, Integer toYear);

//  String createMonthYearQuery(Integer fromMonth, Integer fromYear, Integer toMonth, Integer toYear);

}
