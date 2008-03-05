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

}
