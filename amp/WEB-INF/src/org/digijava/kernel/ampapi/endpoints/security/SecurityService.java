/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import java.util.ArrayList;
import java.util.List;

import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuItem;
import org.dgfoundation.amp.menu.MenuItemsProcessor;
import org.dgfoundation.amp.menu.MenuStructure;
import org.dgfoundation.amp.menu.MenuUtils;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Security Endpoint related services like 
 * 
 * @author Nadejda Mandrescu
 */
public class SecurityService {

	/**
	 * @return json structure for the current view + user + state menu
	 */
	public static List<JsonBean> getMenu() {
		AmpView currentView = MenuUtils.getCurrentView();
		List<MenuItem> items = MenuStructure.getMenuStructure(currentView);
		items = MenuItemsProcessor.processForCurrentRequest(items);
		
		return convert(items);
	}
	
	/**
	 * Converts menu items to JSON structure
	 * @param items
	 * @return JsonBean of menu items
	 */
	private static List<JsonBean> convert(List<MenuItem> items) {
		List<JsonBean> jsonItems = new ArrayList<JsonBean>();
		for (MenuItem item : items) {
			JsonBean jsonItem = new JsonBean();
			jsonItem.set(EPConstants.MENU_NAME, TranslatorWorker.translateText(item.title));
			if (item.tooltip != null) {
				jsonItem.set(EPConstants.MENU_TOOLTIP, TranslatorWorker.translateText(item.tooltip));
			}
			if (item.url != null) {
				jsonItem.set(EPConstants.MENU_URL, item.url);
			}
			if (item.isPopup) {
				jsonItem.set(EPConstants.MENU_OPEN_POPUP, true);
			}
			if (item.isTab) {
				jsonItem.set(EPConstants.MENU_OPEN_TAB, true);
			}
			if (item.isPost) {
				jsonItem.set(EPConstants.MENU_POST, true);
			}
			if (item.getChildren().size() > 0) {
				jsonItem.set(EPConstants.MENU_CHILDREN, convert(item.getChildren()));
			}
			jsonItems.add(jsonItem);
		}
		return jsonItems;
	}
}
