/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.GisUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

/**
 * Common utility methods for all endpoints
 * @author Nadejda Mandrescu
 */
public class EndpointUtils {
	protected static final Logger logger = Logger.getLogger(EndpointUtils.class);

	/**
	 * @return current user application settings 
	 */
	public static AmpApplicationSettings getAppSettings() {
		HttpServletRequest request = TLSUtils.getRequest();
		if (request != null) {
			TeamMember tm = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
			if (tm != null)
				return DbUtil.getTeamAppSettings(tm.getTeamId());
		}
		return null;
	}
	
	/**
	 * @return default currency code for public or logged in user
	 */
	public static String getDefaultCurrencyCode() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null)
			return appSettings.getCurrency().getCurrencyCode();
		return CurrencyUtil.getDefaultCurrency().getCurrencyCode();
	}
	
	/**
	 * @return default calendar id for public or logged in user
	 */
	public static String getDefaultCalendarId() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null)
			return String.valueOf(appSettings.getFiscalCalendar().getIdentifier());
		return String.valueOf(DbUtil.getBaseFiscalCalendar());
	}
	
	/**
	 * @return report default START year selection
	 */
	public static String getDefaultReportStartYear() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null && appSettings.getReportStartYear()!=null && appSettings.getReportStartYear() > 0)
			return String.valueOf(appSettings.getReportStartYear());
		return FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
	} 
	
	/**
	 * @return report default END year selection
	 */
	public static String getDefaultReportEndYear() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null &&appSettings.getReportEndYear()!=null&& appSettings.getReportEndYear() > 0)
			return String.valueOf(appSettings.getReportEndYear());
		return FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
	}
	
	/**
	 * Retrieves a common specification configuration based on the incoming json request
	 * 
	 * @param config
	 * @return report specification
	 */
	public static ReportSpecificationImpl getReportSpecification(JsonBean config, String reportName) {
		// identify report type
		String typeCode = getSingleValue(config, EPConstants.REPORT_TYPE, EPConstants.DEFAULT_REPORT_TYPE);
		Integer reportType = EPConstants.REPORT_TYPE_ID_MAP.get(typeCode);
		if (reportType == null) {
			reportType = ArConstants.DONOR_TYPE;
		}
		
		return new ReportSpecificationImpl(reportName, reportType);
	}
	
	/**
	 * Generates a report based on a given specification
	 * 
	 * @param spec - report specification
	 * @return GeneratedReport that stores all report info and report output
	 */
	public static GeneratedReport runReport(ReportSpecification spec) {
		return runReport(spec, ReportAreaImpl.class);
	}
	
	/**
	 * Generates a report based on a given specification and wraps the output
	 * into the specified class
	 * 
	 * @param spec report specification
	 * @param clazz any class that extends {@link ReportAreaImpl}
	 * @return GeneratedReport that stores all report info and report output
	 */
	public static GeneratedReport runReport(ReportSpecification spec, Class<? extends ReportAreaImpl> clazz) {
		MondrianReportGenerator generator = new MondrianReportGenerator(clazz, 
				ReportEnvironment.buildFor(TLSUtils.getRequest()));
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			logger.error("error running report", e);
		}
		return report;
	}
	
	/**
	 * Retrieves the value associated to the specified key if available 
	 * or returns the default
	 * 
	 * @param formParams
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <T> T getSingleValue(JsonBean formParams, String key, T defaultValue) {
		if (formParams.get(key) != null)
			return (T) formParams.get(key);
		return defaultValue;
	}
	
	
	public static List<JsonBean> getApiStateList(String type) {
		List<JsonBean> maps = new ArrayList<JsonBean>();

		try {
			List<AmpApiState> l = QueryUtil.getMapList(type);
			for (AmpApiState map : l) {
				maps.add(getJsonBeanFromApiState(map, Boolean.FALSE));
			}
			return maps;
		} catch (DgException e) {
			logger.error("Cannot get maps list", e);
			throw new WebApplicationException(e);
		}
	}
	private static JsonBean getJsonBeanFromApiState(AmpApiState map, Boolean getBlob) {
		JsonBean jMap = new JsonBean();

		jMap.set("id", map.getId());
		jMap.set("title", map.getTitle());
		jMap.set("description", map.getDescription());
		if (getBlob) {
			jMap.set("stateBlob", map.getStateBlob());
		}
		jMap.set("created", GisUtil.formatDate(map.getCreatedDate()));
		if(map.getLastAccesedDate()!=null){
			jMap.set("lastAccess", GisUtil.formatDate(map.getLastAccesedDate()));
		}
		return jMap;
	}	
	
	public static AmpApiState getSavedMap(Long mapId) throws AmpApiException {
		Session s = PersistenceManager.getSession();
		AmpApiState map = (AmpApiState) s.load(AmpApiState.class, mapId);
		map.setLastAccesedDate(new Date());
		s.merge(map);
		return map;
	}

	public static JsonBean getApiState(Long mapId) {
		JsonBean jMap = null;
		try {
			AmpApiState map = getSavedMap(mapId);
			jMap = getJsonBeanFromApiState(map, Boolean.TRUE);
		} catch (ObjectNotFoundException e) {
			jMap = new JsonBean();
		} catch (AmpApiException e) {
			logger.error("cannot get map by id " + mapId, e);
			throw new WebApplicationException(e);
		}
		return jMap;
	}	
	
	public static JsonBean saveApiState(final JsonBean pMap,String type) {
		Date creationDate = new Date();
		JsonBean mapId = new JsonBean();

		AmpApiState map = new AmpApiState();
		map.setTitle(pMap.getString("title"));
		map.setDescription(pMap.getString("description"));
		map.setStateBlob(pMap.getString("stateBlob"));
		map.setCreatedDate(creationDate);
		map.setUpdatedDate(creationDate);
		map.setLastAccesedDate(creationDate);
		map.setType(type);
		try {
			Session s = PersistenceManager.getSession();
			s.save(map);
			s.flush();
			mapId.set("mapId", map.getId());
		} catch (Exception e) {
			logger.error("Cannot Save map", e);
			throw new WebApplicationException(e);
		}
		return mapId;
	}
	
	public static List<AvailableMethod> getAvailableMethods(String className){
	return getAvailableMethods(className,false);
	}
	/**
	 * 
	 * @param className
	 * @return
	 */
	public static List<AvailableMethod> getAvailableMethods(String className,boolean includeColumn){
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
						filter.setFilterType(apiAnnotation.filterType());
						if (includeColumn) {
							filter.setColumn(apiAnnotation.column());
						}
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
						//special check if the method shoud be added
						Boolean shouldCheck=false;
						Boolean result=false;
						if (apiAnnotation.visibilityCheck().length() > 0) {
							shouldCheck=true;
							try {
								Method shouldAddApiMethod = c.getMethod(apiAnnotation.visibilityCheck(), null);
								shouldAddApiMethod.setAccessible(true);
								 result =(Boolean) shouldAddApiMethod.invoke(c.newInstance(), null);
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException
									| IllegalArgumentException | InvocationTargetException e) {
							} catch (InstantiationException e) {
								logger.error(e);
							}
						}
						if (shouldCheck) {
							if (result) {
								availableFilters.add(filter);
								shouldCheck = false;
							}
						} else {
							availableFilters.add(filter);
						}
					}
				}
			}
		}
		 catch (ClassNotFoundException e) {
			GisUtil.logger.error("cannot retrieve filters list",e);
			return null;
		}
		return availableFilters;
	}
	
	
	/**
	 * Set locale to US in order to avoid number formating issues
	 * @return
	 */
	public static DecimalFormat getCurrencySymbols(){
		Locale locale  = new Locale("en", "US");
		String pattern = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		FormatHelper.getDecimalFormat();
		DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
		decimalFormat.applyPattern(pattern);
		return decimalFormat;
	}
}
