/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuUtils;
import org.digijava.module.aim.dbentity.AmpMenuEntryInAdminView;
import org.digijava.module.aim.dbentity.AmpMenuEntryInPublicView;
import org.digijava.module.aim.dbentity.AmpMenuEntryInTeamView;
import org.digijava.module.aim.dbentity.AmpMenuEntryInView;

/**
 * Detects visible Menu Entries. <br><br>
 * 
 * Note: use this until we have a Menu Management UI 
 * that may have different requirements to handle menu entries visibility.
 * @author Nadejda Mandrescu
 */
public class MenuVisibility extends RuleBasedVisibility {
	
	private Map<AmpView, Set<String>> visibleDataPerView = new TreeMap<AmpView, Set<String>>();
	
	@Override
	protected Class<? extends AmpMenuEntryInView> getClazz() {
		switch(MenuUtils.getCurrentView()) {
		case PUBLIC: return AmpMenuEntryInPublicView.class;
		case ADMIN: return AmpMenuEntryInAdminView.class;
		case TEAM: return AmpMenuEntryInTeamView.class;
		}
		return null;
	}

	protected MenuVisibility() {
	}
	
	@Override
	synchronized
	public Set<String> getEnabledSettings() {
		if (atomicVisibilityChanged.get()) {
			visibleDataPerView = new TreeMap<AmpView, Set<String>>();
		}
		visibleData = visibleDataPerView.get(MenuUtils.getCurrentView());
		return super.getEnabledSettings();
	}
	
//	/** stores menu entries to find the mappings for */
//	private static final Set<String> menuSet = ConstantsUtil.getConstantsSet(MenuConstants.class);
//	
//	/** 
//	 * list of menu entries that do not have FM specific rules to show/hide, 
//	 * however they can have other View or User specific rules and be hidden by those rules
//	 */
//	protected static final List<String> visibleByDefault = Collections.unmodifiableList(Arrays.asList(
//			// TODO:
//			MenuConstants.TRANSLATOR_VIEW
//			));
//	
//	private static final String PUBLIC_CALENDAR_MODULE = "Public Calendar Module";  
//	
//	protected static final Map<String, String> modulesToMenuMap = Collections.unmodifiableMap(
//			new HashMap<String, String>() {{
//		put("Home Page Menu Entry", MenuConstants.HOME_PAGE);
//		put("Public Site", MenuConstants.PUBLIC_SITE);
//		put("Public Documents", MenuConstants.PUBLIC_DOCUMENTS);
//		put("Public Calendar", PUBLIC_CALENDAR_MODULE);
//		put("Calendar", MenuConstants.CALENDAR);
//	}});
//	
//	private static final Map<String, Collection<String>> dependancyTypeAny = Collections.unmodifiableMap(
//			new HashMap<String, Collection<String>>() {{
//		put(MenuConstants.REPORTS_MENU, Arrays.asList(MenuConstants.PUBLIC_REPORTS, MenuConstants.REPORTS));
//	}});
//	
//	// TODO: update to process denepdnacyModules
//	private static final Map<String, Collection<String>> dependancyTypeAll = Collections.unmodifiableMap(
//			new HashMap<String, Collection<String>>() {{
//		put(MenuConstants.PUBLIC_CALENDAR, Arrays.asList(MenuConstants.CALENDAR, PUBLIC_CALENDAR_MODULE));
//	}});
//	
//	@Override
//	public Set<String> getEnabledSettings() {
//		return getCurrentVisibleData();
//	}
//
//	@Override
//	protected List<String> getVisibleByDefault() {
//		return visibleByDefault;
//	}
//
//	@Override
//	protected Set<String> getAllData() {
//		return menuSet;
//	}
//
//	@Override
//	protected Map<String, String> getDataMap(DataMapType dataMapType) {
//		switch(dataMapType) {
//		case MODULES: return modulesToMenuMap;
//		default: return noDataMap;
//		}
//	}
//
//	@Override
//	protected Map<String, Collection<String>> getDependancyMapTypeAny() {
//		return dependancyTypeAny;
//	}
//
//	@Override
//	protected Map<String, Collection<String>> getDependancyMapTypeAll() {
//		return dependancyTypeAll;
//	}

}
