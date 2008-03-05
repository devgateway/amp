package org.digijava.module.aim.logic.boliviaimpl;

import org.digijava.module.aim.logic.AmpARFilterHelper;

public class BoliviaAmpARFilterHelper implements AmpARFilterHelper {

	public String createFromYearQuery(Integer fromYear) {
		return "SELECT a.amp_activity_id " + "from amp_activity a "
		+ "where date_format(a.convenio_date_filter,_latin1'%Y') >= '"
		+ fromYear + "'";
	}

	public String createToYearQuery(Integer toYear) {
		return "SELECT a.amp_activity_id " + "from amp_activity a "
		+ "where date_format(a.convenio_date_filter,_latin1'%Y') <= '"
		+ toYear + "'";
	}

}
