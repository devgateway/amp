package org.digijava.kernel.ampapi.endpoints.util;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingOptions;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.translator.TranslatorWorker;

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
						//the name should be translatable
						if(apiAnnotation.name()!=null && !apiAnnotation.name().equals("")){
							filter.setName(TranslatorWorker.translateText(apiAnnotation.name()));
						}
						
						String endpoint = "/rest/" + p.value();
						
						if (methodPath != null){
							endpoint += methodPath.value();
						}
						filter.setEndpoint(endpoint);
						filter.setUi(apiAnnotation.ui());
						filter.setId(apiAnnotation.id());
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
	 * @return list of GIS settings
	 */
	public static List<SettingOptions> getSettings() {
		//retrieve common settings
		List<SettingOptions> settings = EndpointUtils.getSettings();
		//add GIS specific settings
		settings.add(EndpointUtils.getFundingTypeSettings(GisConstants.MEASURE_TO_NAME_MAP));
		return settings;
	}
	
	public static void applySettings(ReportSpecificationImpl spec, JsonBean config) {
		//apply first common settings, i.e. calendar and currency
		EndpointUtils.applySettings(spec, config);
		
		//now apply GIS custom settings, i.e. selected measures
		if (config.get(EPConstants.SETTINGS) != null) {
			Map<Integer, Object> settings = (Map<Integer, Object>) config.get(EPConstants.SETTINGS);
			List<String> measureOptions = (List<String>)settings.get(SettingsConstants.FUNDING_TYPE_ID);
			if (measureOptions != null)
				for (String measure : measureOptions) 
					spec.addMeasure(new ReportMeasure(measure));
		}
	}


}
