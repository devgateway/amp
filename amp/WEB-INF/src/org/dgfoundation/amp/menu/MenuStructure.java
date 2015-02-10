/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpMenuEntry;

/**
 * Keeps all possible menu entries for a specific public/admin/team view, 
 * in order to avoid it's identification on each request unless the structure has changed. 
 * 
 * @author Nadejda Mandrescu
 */
public class MenuStructure {
	private static Logger logger = Logger.getLogger(MenuStructure.class);
	
	private static Map<AmpView, MenuStructure> structurePerView = initViews();
	
	private static Map<AmpView, MenuStructure> initViews() {
		Map<AmpView, MenuStructure> viewsMap = new TreeMap<AmpView, MenuStructure>();
		for (AmpView view : AmpView.values()) {
			viewsMap.put(view, new MenuStructure(MenuUtils.getMenuEntries(view, true), view));
		}
		return Collections.synchronizedMap(viewsMap);
	}
	
	/**
	 * Retrieves menu structure for the given view
	 * @param view
	 * @return unmodifiable list of all menu items for the given view
	 */
	public static List<MenuItem> getMenuStructure(AmpView view) {
		return structurePerView.get(view).getMenuItems();
	}
	
	/**
	 * Recreate menu structures
	 */
	public static void recreate() {
		logger.info("Request to rebuild default menu structure");
		synchronized (structurePerView) {
			for (AmpView view : AmpView.values()) {
				structurePerView.put(view, new MenuStructure(MenuUtils.getMenuEntries(view, true), view));
			}
		} 
	}
	
	private List<MenuItem> menuItems;
	private MenuStructure(List<AmpMenuEntry> orderedEntries, AmpView view) {
		String warnMsg = "Skipping menu entry '%s' that is enabled for " + view + " view while its ancestor '%s' is not.";
		MenuItem root = new MenuItem("root", null, null, null, null);
		Map<AmpMenuEntry, MenuItem> itemsMap = new HashMap<AmpMenuEntry, MenuItem>();
		itemsMap.put(null, root);
		for (AmpMenuEntry ame : orderedEntries) {
			MenuItem mi = new MenuItem(ame.getName(), ame.getTitle(), ame.getTooltip(), ame.getUrl(), ame.getFlags());
			itemsMap.put(ame, mi);
		}
		for (AmpMenuEntry ame : orderedEntries) {
			MenuItem item = itemsMap.get(ame);
			MenuItem parentItem = itemsMap.get(ame.getParent());
			if (parentItem == null) {
				// null ame parent is defined by "root", thus no NullPointer is expected
				logger.warn(String.format(warnMsg, ame.getName(), ame.getParent().getName()));
			} else {
				parentItem.appendChild(item);
				item.setParent(parentItem);
			}
		}
		MenuItemsProcessor.processCommonDynamicItems(root.getChildren());
		menuItems = Collections.unmodifiableList(root.getChildren());
	}
	
	/**
	 * @return unmodifiable list of all menu items 
	 */
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Configures dynamic menu entries
	 * @param mi
	 */
	/* candidate for removal
	private void configureCommonDynamicMenu(MenuItem mi) {
		switch(mi.name) {
		case MenuConstants.LANGUAGE:
		case MenuConstants.PUBLIC_LANGUAGE:
			(new LanguageMenu()).process(mi);
			break;
		default:
			break;
		}
	}
	*/
}

