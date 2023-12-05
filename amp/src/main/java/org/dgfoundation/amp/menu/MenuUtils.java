/**
 * 
 */
package org.dgfoundation.amp.menu;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpMenuEntryInView;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.hibernate.query.Query;

import javax.servlet.http.HttpSession;
import java.util.List;

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
//        String nativeQuery = "SELECT * FROM AMP_MENU_ENTRY_VIEW ame WHERE ame.view_type = :viewType";
        Query<AmpMenuEntryInView> q ;
        q=PersistenceManager.getRequestDBSession().createQuery(query, AmpMenuEntryInView.class);
        q.setParameter("viewType", view);
        List<AmpMenuEntryInView> res = q.list();
        System.out.println(res);
        return res;
    }
    
    /**
     * @return detects current view type (Public, Team, Admin)
     */
    public static AmpView getCurrentView() {
        HttpSession session =TLSUtils.getRequest().getSession();
        TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
        System.out.println(tm);
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
        try {

        // detect current view
        AmpView currentView = getCurrentView();
        // retrieve menu structure for the current view
            System.out.println(currentView);
        List<MenuItem> items = MenuStructure.getMenuStructure(currentView);
            System.out.println(items);
        // process menu structure for the current request, i.e. filter out anything hidden by FM or lack of user rights
        return MenuItemsProcessor.processForCurrentRequest(items, currentView);
        }catch (Throwable e)
        {
            logger.error("Error during menu initialization: ", e);
            throw e;
        }
    }
}
