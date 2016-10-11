/**
 * 
 */
package org.dgfoundation.amp.menu.dynamic;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.dgfoundation.amp.menu.MenuConstants;
import org.dgfoundation.amp.menu.MenuItem;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;

/**
 * Builds dynamic Visualization Dashboards menu structure for the current session scope
 * @author Nadejda Mandrescu
 */
public class VisualizationDashboardsMenu implements DynamicMenu {

	@Override
	public void process(MenuItem menuItem) {
		MenuItem template = null;
		for (MenuItem mi : menuItem.getChildren()) {
			if (MenuConstants.VISUALIZATION_DASHBOARDS_ITEM.equals(mi.name)) {
				template = mi;
				break;
			}
		}
		if (template != null) {
			menuItem.getChildren().remove(template);
			
			TreeMap<Long, String> idToNameDashboards = (TreeMap<Long, String>) TLSUtils.getRequest().getSession().getAttribute(Constants.MENU_DASHBOARDS);
			if (idToNameDashboards != null) {
				for (Entry<Long, String> pair : idToNameDashboards.entrySet()) {
					String url = String.format(template.url, pair.getKey());
					menuItem.appendChild(new MenuItem(template.name, pair.getValue(), 
							null, url, template.flags, template.requestUrl, template.groupKeys));
					
				}
			}
		}
	}
	
}
