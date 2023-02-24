/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpMenuEntryInView;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;

/**
 * Menu utility methods 
 * 
 * @author Nadejda Mandrescu
 */
public class MenuUtils {
    private static Logger logger = Logger.getLogger(MenuUtils.class);
    
    /**
     * @return list of menu entries for the current request view
     */
    public static List<AmpMenuEntryInView> getMenuEntries(AmpView view, boolean orderByPosition) {
        String query = "select ame from " + AmpMenuEntryInView.class.getName()
                + " ame where ame.viewType=:viewType"
                + (orderByPosition ? " order by ame.position asc" : "");
        
        List<AmpMenuEntryInView> entries = PersistenceManager.getSession().createQuery(query)
                .setParameter("viewType", view).list();
        return entries;
    }
    
    /**
     * @return detects current view type (Public, Team, Admin)
     */
    public static AmpView getCurrentView() {
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (tm == null) {
            return AmpView.PUBLIC;
        }
        if (isAmpAdmin()) {
            return AmpView.ADMIN;
        }
        return AmpView.TEAM;
    }

    public static boolean isAmpAdmin() {
        return "yes".equals(TLSUtils.getRequest().getSession().getAttribute("ampAdmin"));
    }

    /**
     * @return current menu items list
     */
    public static List<MenuItem> getCurrentRequestMenuItems() {
        // detect current view
        AmpView currentView = getCurrentView();
        // retrieve menu structure for the current view
        List<MenuItem> items = MenuStructure.getMenuStructure(currentView);
        // process menu structure for the current request, i.e. filter out anything hidden by FM or lack of user rights
        return MenuItemsProcessor.processForCurrentRequest(items, currentView);
    }
}
