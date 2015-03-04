/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.menu.dynamic.DynamicMenu;
import org.dgfoundation.amp.menu.dynamic.EmailMenu;
import org.dgfoundation.amp.menu.dynamic.LanguageMenu;
import org.dgfoundation.amp.menu.dynamic.WorkspaceMenu;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.Group;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.kernel.user.User;

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
	
	public static List<MenuItem> processForCurrentRequest(List<MenuItem> items, AmpView view) {
		// TODO for AMP-19518
		Set<String> visibleMenuEntries = null;
		
		return (new MenuItemsProcessor(view)).process(items, visibleMenuEntries);
	}
	
	private AmpView view;
	private TeamMember tm;
	private Set<String> currentUserGroupKeys = new HashSet<String>();
	
	private MenuItemsProcessor(AmpView view) {
		this.view = view;
		this.tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		// pledgers were defined for some reason as a flag instead of a group, 
		// thus we have to deal with it in a custom way
		if (tm != null && Boolean.TRUE.equals(tm.getPledger())) {
			currentUserGroupKeys.add(Group.PLEDGERS);
		}
		if (tm != null && tm.getMemberId() != null) {
			AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId()); 
			Set<Group> userGroups = atm != null && atm.getUser() != null ? atm.getUser().getGroups() : null;
			if (userGroups != null) {
				for(Group group : userGroups) {
					currentUserGroupKeys.add(group.getKey());
				}
			}
		}
	}
	
	protected List<MenuItem> process(List<MenuItem> items, Set<String> visibleMenuEntries) {
		List<MenuItem> newList = new ArrayList<MenuItem>();
		for (MenuItem item : items) {
			// add only items that are visible based on FM & custom visibility rules
			// TODO: remove visibleMenuEntries == null when AMP-19518 is done
			if ((visibleMenuEntries == null || visibleMenuEntries.contains(item.name)) 
					&& isVisible(item.name) && isAllowedUserGroup(item)) {
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
	private boolean isVisible(String menuName) {
		switch(menuName) {
		case MenuConstants.TRANSLATOR_VIEW:
			return !TranslatorWorker.isTranslationMode(TLSUtils.getRequest());
		case MenuConstants.NON_TRANSLATOR_VIEW:
			return TranslatorWorker.isTranslationMode(TLSUtils.getRequest());
		default:
			return true;
		}
	}
	
	private boolean isAllowedUserGroup(MenuItem mi) {
		if (mi.groupKeys == null || mi.groupKeys.size() == 0 
				|| AmpView.ADMIN == view && mi.groupKeys.contains(Group.ADMINISTRATORS)) {
			return true;
		}
		
		Set<String> intersect = new HashSet<String>(currentUserGroupKeys);
		intersect.retainAll(mi.groupKeys);
		return intersect.size() > 0;
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
	 * 					
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
