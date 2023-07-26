/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.visibility.data.DataVisibility;
import org.digijava.kernel.user.Group;
import org.digijava.module.aim.dbentity.AmpMenuEntryInView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keeps all possible menu entries for a specific public/admin/team view, 
 * in order to avoid it's identification on each request unless the structure has changed. 
 * 
 * @author Nadejda Mandrescu
 */
public class MenuStructure {
    private static Logger logger = LoggerFactory.getLogger(MenuStructure.class);
    
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
        DataVisibility.notifyVisibilityChanged();
    }
    
    private List<MenuItem> menuItems;
    private <T> MenuStructure(List<T> orderedEntries, AmpView view) {
        String warnMsg = "Skipping menu entry '%s' that is enabled for " + view + " view while its ancestor '%s' is not.";
        MenuItem root = new MenuItem("root", null, null, null, null, null, null);
        Map<T, MenuItem> itemsMap = new HashMap<T, MenuItem>();
        itemsMap.put(null, root);
        for (T t : orderedEntries) {

            AmpMenuEntryInView ame = (AmpMenuEntryInView) t;
            Set<Group> groups = ImmutableSet.copyOf(ame.getGroups());
            Set<String> groupKeys = new HashSet<String>();
            for (Group group : groups) {
                groupKeys.add(group.getKey());
            }
            MenuItem mi = new MenuItem(ame.getName(), ame.getTitle(), ame.getTooltip(), ame.getUrl(), ame.getFlags(), ame.getRequestUrl(), groupKeys);
            itemsMap.put(t, mi);
        }
        for (T t : orderedEntries) {
            AmpMenuEntryInView ame = (AmpMenuEntryInView) t; 
            MenuItem item = itemsMap.get(t);
            MenuItem parentItem = itemsMap.get(ame.getParent());
            if (parentItem == null) {
                // null "ame" parent is defined by "root", thus no NullPointer is expected
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
    
}

