/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Single point of reference for all FM settings groups 
 * @author Nadejda Mandrescu
 */
public class FMSettingsMediator {
	protected static final Logger logger = Logger.getLogger(FMSettingsMediator.class);
	
	public static final String FMGROUP_COLUMNS = "COLUMNS";
	public static final String FMGROUP_MEASURES = "MEASURES";
	public static final String FMGROUP_MODULES = "MODULES";
	public static final String FMGROUP_GIS = "GIS";
	public static final String FMGROUP_DASHBOARDS = "DASHBOARDS";

	/** stores all fm groups classes that are manageable via this proxy */
	private static Map<String, Class<? extends FMSettings>> registeredFMGroups = initFMGroups();
	
	/** stores all instances of fm settings per template */
	private static Map<Long, Map<String, FMSettings>> templateToFMGroupMap =
			Collections.synchronizedMap(new HashMap<Long, Map<String, FMSettings>>());
			
	private static Map<String, Class<? extends FMSettings>> initFMGroups() {
		Map<String, Class<? extends FMSettings>> groups = new HashMap<String, Class<? extends FMSettings>>();
		
		groups.put(FMGROUP_COLUMNS, ColumnsVisibility.class);
		groups.put(FMGROUP_MEASURES, MeasuresVisibility.class);
		groups.put(FMGROUP_MODULES, ModulesVisibility.class);
		groups.put(FMGROUP_GIS, GisFMSettings.class);
		groups.put(FMGROUP_DASHBOARDS, DashboardsFMSettings.class);
		
		return Collections.synchronizedMap(groups);
	}
	
	/**
	 * Retrieves a set of enabled settings for the given FM group name
	 * @param fmGroupName
	 * @return
	 */
	public static Set<String> getEnabledSettings(String fmGroupName) {
		long currentTemplateId = FeaturesUtil.getCurrentTemplateId();
		Map<String, FMSettings> templateGroup = getTemplate(currentTemplateId);
		FMSettings fmGroup = getFMSettings(templateGroup, fmGroupName);
		
		if (fmGroup != null) {
			return fmGroup.getEnabledSettings();
		}
		return Collections.emptySet();
	}
	
	/**
	 * Identify the template group & create it if doesn't exist yet
	 * @param id
	 * @return
	 */
	synchronized
	private static Map<String, FMSettings> getTemplate(Long id) {
		Map<String, FMSettings> templateGroup = templateToFMGroupMap.get(id);
		if (templateGroup == null) {
			templateGroup = Collections.synchronizedMap(new HashMap<String, FMSettings>());
			templateToFMGroupMap.put(id, templateGroup);
		}
		return templateGroup;
	}
	
	/**
	 * Identifies fmGroup settings object for the given group name within a specific template group
	 * @param templateGroup
	 * @param fmGroupName
	 * @return
	 */
	synchronized private static FMSettings getFMSettings(Map<String, FMSettings> templateGroup, String fmGroupName) {
		FMSettings fmGroup = templateGroup.get(fmGroupName);
		if (fmGroup == null) {
			Class<? extends FMSettings> clazz = registeredFMGroups.get(fmGroupName);
			if (clazz != null) {
				try {
					fmGroup = clazz.newInstance();
					templateGroup.put(fmGroupName, fmGroup);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
		return fmGroup;
	}

}
