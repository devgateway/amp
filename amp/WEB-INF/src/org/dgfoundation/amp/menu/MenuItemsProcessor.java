/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.menu.dynamic.DynamicMenu;
import org.dgfoundation.amp.menu.dynamic.EmailMenu;
import org.dgfoundation.amp.menu.dynamic.LanguageMenu;
import org.dgfoundation.amp.menu.dynamic.WorkspaceMenu;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Updates current menu structure based on the current user & state
 *  
 * @author Nadejda Mandrescu
 */
public class MenuItemsProcessor {
	
	/**
	 * Processes & updates menu items based on current request state like user, translator view, etc  
	 * @param items
	 * @return
	 */
	public static List<MenuItem> processForCurrentRequest(List<MenuItem> items) {
		// TODO for AMP-19518
		Set<String> visibleMenuEntries = null;
		return process(items, visibleMenuEntries);
	}
	
	private static List<MenuItem> process(List<MenuItem> items, Set<String> visibleMenuEntries) {
		List<MenuItem> newList = new ArrayList<MenuItem>();
		for (MenuItem item : items) {
			// add only items that are visible based on FM & custom visibility rules
			// TODO: remove visibleMenuEntries == null when AMP-19518 is done
			if ((visibleMenuEntries == null || visibleMenuEntries.contains(item.name)) && isVisible(item.name)) {
				MenuItem newItem = new MenuItem(item);
				newList.add(newItem);
				newItem.setChildren(process(item.getChildren(), visibleMenuEntries));
				configureCustomDynamicMenu(newItem);
			}
		}
		return newList;
	}
	
	/**
	 * Custom visibility checks
	 * @param menuName
	 * @return 
	 */
	private static boolean isVisible(String menuName) {
		switch(menuName) {
		case MenuConstants.TRANSLATOR_VIEW:
			return !TranslatorWorker.isTranslationMode(TLSUtils.getRequest());
		case MenuConstants.NON_TRANSLATOR_VIEW:
			return TranslatorWorker.isTranslationMode(TLSUtils.getRequest());
		default:
			return true;
		}
	}
	
	/**
	 * Configures dynamic menu entries
	 * @param mi
	 */
	private static void configureCustomDynamicMenu(MenuItem mi) {
		switch(mi.name) {
		case MenuConstants.CHANGE_WORKSPACE:
			(new WorkspaceMenu()).process(mi);
			break;
		default:
			break;
		}
	} 

	/***********************************************************
	 * 					PARTIALY USED
	 ***********************************************************/
	/* This is an abstract approach that may be useful when many dynamic menu structures are built,
	 * but so far it is easier to use it directly to speed things up
	 */
	private static final Map<String, DynamicMenu> dynamicCommon = new HashMap<String, DynamicMenu>() {{
		put(MenuConstants.LANGUAGE, new LanguageMenu());
		put(MenuConstants.PUBLIC_LANGUAGE, new LanguageMenu());
		put(MenuConstants.EMAIL, new EmailMenu());
	}};
	
	private static final Map<String, DynamicMenu> dynamicPerRequest = new HashMap<String, DynamicMenu>() {{
		put(MenuConstants.WORKSPACE_INFO, new WorkspaceMenu());
	}};
	
	/**
	 * Updates menu items structure with dynamic structure that is common
	 * @param items
	 */
	public static void processCommonDynamicItems(List<MenuItem> items) {
		processDynamicEntries(items, dynamicCommon);
	}
	
	/**
	 * Updates menu items structure with dynamic structure that is custom per request
	 * @param items
	 */
	public static void processRequestDynamicItems(List<MenuItem> items) {
		processDynamicEntries(items, dynamicPerRequest);
	}
	
	private static void processDynamicEntries(List<MenuItem> items, Map<String, DynamicMenu> dynamicItems) {
		for (MenuItem item : items) {
			DynamicMenu dm = dynamicItems.get(item.name);
			if (dm != null) {
				dm.process(item);
			}
			processDynamicEntries(item.getChildren(), dynamicItems);
		}
	}
}
