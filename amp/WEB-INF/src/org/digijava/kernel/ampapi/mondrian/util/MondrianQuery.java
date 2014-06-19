package org.digijava.kernel.ampapi.mondrian.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.FilterParam;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * 
 * @author Diego Dimunzio
 *
 */
public class MondrianQuery {
	protected static Logger logger = Logger.getLogger(MondrianQuery.class);
	
	private static String getformatDate(String dates) {
		java.sql.Date date =  new java.sql.Date(FormatHelper.parseDate2(dates).getTime());
		SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd");
        return "'"+fd.format(date)+"'";
    }
	
	private String generatedFilterQuery;
	private void queryAppend(String filter) {
		generatedFilterQuery += " AND amp_activity_id IN (" + filter + ")";
	}
	
	public void createQuery(HttpServletRequest request){
		AmpARFilter filter = new AmpARFilter();
		filter.readRequestData(request, AmpARFilter.FILTER_SECTION_ALL, null);
	
		filter.generateFilterQuery(request, false,true);
		generatedFilterQuery = filter.getGeneratedFilterQuery();
		
		/*Prepare thread local to process the schema*/
		QueryThread.setQuery(generatedFilterQuery);
		Site site = RequestUtils.getSite(request);
		QueryThread.setSite(site);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		QueryThread.setLocale(navigationLanguage);
	}

}
