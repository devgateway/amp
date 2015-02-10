/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Updates current menu structure based on the current user & state
 *  
 * @author Nadejda Mandrescu
 */
public class MenuItemsProcessor {
	public static List<MenuItem> process(List<MenuItem> items) {
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
				newItem.setChildren(process(item.getChildren()));
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
	
}
