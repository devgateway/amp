/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpMenuEntry;
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
	public static List<AmpMenuEntry> getMenuEntries(AmpView view, boolean orderByPosition) {
		String query = "select ame from " + AmpMenuEntry.class.getName() 
				+ " ame where ame.%s = true order by ame.id asc"
				+ (orderByPosition ? ", ame.position asc" : "");
		switch(view) {
		case ADMIN: query = String.format(query, "adminView"); break;
		case PUBLIC: query = String.format(query, "publicView"); break;
		case TEAM: query = String.format(query, "teamView"); break;
		}
		
		List<AmpMenuEntry> entries = PersistenceManager.getSession().createQuery(query).list();
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
	
	public static AmpView getCurrentView() {
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		if (tm == null) 
			return AmpView.PUBLIC;
		if ("yes".equals(TLSUtils.getRequest().getSession().getAttribute("ampAdmin")))
			return AmpView.ADMIN;
		return AmpView.TEAM;
	}
}
