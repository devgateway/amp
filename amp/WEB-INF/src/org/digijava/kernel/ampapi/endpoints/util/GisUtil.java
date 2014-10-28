package org.digijava.kernel.ampapi.endpoints.util;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.reports.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class GisUtil {
	private static final Logger logger = Logger.getLogger(GisUtil.class);

	public static String formatDate(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		TimeZone tz = TimeZone.getTimeZone("UTC");

		df.setTimeZone(tz);
		return df.format(d);
	}
	public static List<AvailableMethod> getAvailableMethods(String className){
		List<AvailableMethod> availableFilters=new ArrayList<AvailableMethod>(); 
		try {
			Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
			Class<?> c = Class.forName(className);
			javax.ws.rs.Path p=c.getAnnotation(javax.ws.rs.Path.class);
			String path="/rest/"+p.value();
			Member[] mbrs=c.getMethods();
			for (Member mbr : mbrs) {
				ApiMethod apiAnnotation=
		    			((Method) mbr).getAnnotation(ApiMethod.class);
				if (apiAnnotation != null) {
					final String column = apiAnnotation.column();
					if (EPConstants.NA.equals(column) || visibleColumns.contains(column)) {
						//then we have to add it to the filters list
						javax.ws.rs.Path methodPath = ((Method) mbr).getAnnotation(javax.ws.rs.Path.class);
						AvailableMethod filter = new AvailableMethod();
						filter.setName(apiAnnotation.name());
						String endpoint = "/rest/" + p.value();
						
						if (methodPath != null){
							endpoint += methodPath.value();
						}
						filter.setEndpoint(endpoint);
						filter.setUi(apiAnnotation.ui());
						//we check the method exposed
						if (((Method) mbr).getAnnotation(javax.ws.rs.POST.class) != null){
							filter.setMethod("POST");
						} else {
							if (((Method) mbr).getAnnotation(javax.ws.rs.GET.class) != null){
								filter.setMethod("GET");
							} else {
								if (((Method) mbr).getAnnotation(javax.ws.rs.PUT.class) != null){
									filter.setMethod("PUT");
								} else {
									if (((Method) mbr).getAnnotation(javax.ws.rs.DELETE.class) != null){
										filter.setMethod("DELETE");
									}
								}
							}
						}
						availableFilters.add(filter);
					}
				}
			}
		}
		 catch (ClassNotFoundException e) {
			logger.error("cannot retrieve filters list",e);
			return null;
		}
		return availableFilters;
	}
	
	/**
	 * @return general currency settings
	 */
	public static GisSettingOptions getCurrencySettings() {
		//build currency options
		List<GisSettingOptions.Option> options = new ArrayList<GisSettingOptions.Option>();
		for (AmpCurrency ampCurrency : CurrencyUtil.getActiveAmpCurrencyByName()) {
			GisSettingOptions.Option currencyOption = new GisSettingOptions.Option(ampCurrency.getCurrencyCode(), ampCurrency.getCurrencyName());
			options.add(currencyOption);
		}
		//identifies the base currency 
		String defaultId = EndpointUtils.getDefaultCurrencyCode();
		
		return new GisSettingOptions(GisConstants.CURRENCY_ID, GisConstants.CURRENCY_NAME, defaultId, options);
	}
	
	/**
	 * @return general calendar settings
	 */
	public static GisSettingOptions getCalendarSettings() {
		//build calendar options
		List<GisSettingOptions.Option> options = new ArrayList<GisSettingOptions.Option>();
		for (AmpFiscalCalendar ampCalendar : DbUtil.getAllFisCalenders()) {
			GisSettingOptions.Option calendarOption = new GisSettingOptions.Option(
					String.valueOf(ampCalendar.getAmpFiscalCalId()), ampCalendar.getName());
			options.add(calendarOption);
		}
		//identifies the default calendar 
		String defaultId = EndpointUtils.getDefaultCalendarId();
		
		return new GisSettingOptions(GisConstants.CALENDAR_TYPE_ID, GisConstants.CALENDAR_TYPE_NAME, defaultId, options);
	}
}
