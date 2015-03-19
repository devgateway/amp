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
		//return filterWrongEntries(entries, view);
	}
	
	/**
	 * Filters out wrongly configured entries whose parents are not in the current view
	 * @param entries
	 * @param view current view
	 * @return filtered list of entries
	 */
	/* Note: this method call can be skipped when menu definitions will be correctly verified 
	 * as part of the menu UI administration
	 */
	/*
	private static List<AmpMenuEntry> filterWrongEntries(List<AmpMenuEntry> entries, AmpView view) {
		String warnMsg = "Skipping menu entry '%s' that is enabled for " + view + " while its ancestor '%s' is not.";
		Set<AmpMenuEntry> entriesSet = new HashSet<AmpMenuEntry>(entries);
		for (ListIterator<AmpMenuEntry> iter = entries.listIterator(); iter.hasNext(); ) {
			AmpMenuEntry entry = iter.next();
			AmpMenuEntry parent = entry.getParent();
			while (parent != null) {
				if (entriesSet.contains(parent)) {
					parent = parent.getParent();
				} else {
					break;
				}
			}
			// if we stopped at not null parent, then it is not in the current view set
			if (parent != null) {
				logger.warn(String.format(warnMsg, entry.getName(), parent.getName()));
				iter.remove();
				// set stop parent, i.e. the one that was not yet checked and may be valid
				parent = parent.getParent();
				while (entry != parent) {
					entriesSet.remove(entry);
					entry = entry.getParent();
				}
			}
		}
		return entries;
	}
	*/
	
	/**
	 * @return detects current view type (Public, Team, Admin)
	 */
	public static AmpView getCurrentView() {
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		if (tm == null) 
			return AmpView.PUBLIC;
		if ("yes".equals(TLSUtils.getRequest().getSession().getAttribute("ampAdmin")))
			return AmpView.ADMIN;
		return AmpView.TEAM;
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
