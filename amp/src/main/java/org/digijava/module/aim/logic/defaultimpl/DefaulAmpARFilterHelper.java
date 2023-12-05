package org.digijava.module.aim.logic.defaultimpl;

import org.digijava.module.aim.logic.AmpARFilterHelper;

public class DefaulAmpARFilterHelper implements AmpARFilterHelper {

    public String createFromYearQuery(Integer fromYear) {
        return "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
                + "WHERE f.amp_funding_id=fd.amp_funding_id AND date_format(fd.transaction_date,_latin1'%Y')>='"
                + fromYear + "'";
    }

    public String createToYearQuery(Integer toYear) {       
        return "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
                + "WHERE f.amp_funding_id=fd.amp_funding_id AND date_format(fd.transaction_date,_latin1'%Y')<='"
                + toYear + "'";
    }
    
    public String createMonthYearQuery(Integer fromMonth, Integer fromYear, Integer toMonth, Integer toYear) {
        return "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
                + "WHERE f.amp_funding_id=fd.amp_funding_id AND fd.transaction_date>=' "+fromYear+"-"+fromMonth+"" +
                        "-01' AND fd.transaction_date<='"+toYear+"-"+toMonth+"-31'";
    }
    
    public String createFromMonthQuery(Integer fromMonth, Integer fromYear) {
        return "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
                + "WHERE f.amp_funding_id=fd.amp_funding_id AND ((date_format(fd.transaction_date,_latin1'%Y')='"
                + fromYear + "' AND date_format(fd.transaction_date, _latin1'%m')>='"+ fromMonth +"') OR " 
                + "(date_format(fd.transaction_date,_latin1'%Y') > '" + fromYear + "'))";
    }
    
    public String createToMonthQuery(Integer toMonth, Integer toYear) {
        return "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
        + "WHERE f.amp_funding_id=fd.amp_funding_id AND ((date_format(fd.transaction_date,_latin1'%Y')='"
        + toYear + "' AND date_format(fd.transaction_date, _latin1'%m')<='"+ toMonth +"') OR " 
        + "(date_format(fd.transaction_date,_latin1'%Y') < '" + toYear + "'))";
    }

}
