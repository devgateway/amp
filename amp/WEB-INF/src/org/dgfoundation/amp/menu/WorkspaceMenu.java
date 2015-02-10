/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.Collection;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;

/**
 * Builds dynamic workspace menu structure for the current user
 * @author Nadejda Mandrescu
 */
public class WorkspaceMenu implements DynamicMenu {
	private static final String WORKSPACE_SWITCH_URL = "/selectTeam.do?id=%s";
	
	@Override
	public void process(MenuItem menuItem) {
		Collection<AmpTeamMember> wsList = (Collection<AmpTeamMember>) 
				TLSUtils.getRequest().getSession().getAttribute(Constants.USER_WORKSPACES);
		if (wsList == null || wsList.size() == 0)
			return;
		
		for (AmpTeamMember atm : wsList) {
			MenuItem mi = new MenuItem(MenuConstants.WORKSPACE_ITEM, atm.getAmpTeam().getName(),
					atm.getAmpTeam().getName(), String.format(WORKSPACE_SWITCH_URL, atm.getAmpTeamMemId()), null);
			menuItem.appendChild(mi);
		}
	}

}
