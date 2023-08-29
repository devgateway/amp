/**
 * 
 */
package org.dgfoundation.amp.menu.dynamic;

import org.dgfoundation.amp.menu.MenuConstants;
import org.dgfoundation.amp.menu.MenuItem;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;

import java.util.Collection;

/**
 * Builds dynamic workspace menu structure for the current user
 * @author Nadejda Mandrescu
 */
public class WorkspaceMenu implements DynamicMenu {
    
    @Override
    public void process(MenuItem menuItem) {
        if (menuItem.getChildren().size() == 0)
            return;
        MenuItem template = menuItem.getChildren().iterator().next();
        menuItem.getChildren().clear();
        
        Collection<AmpTeamMember> wsList = (Collection<AmpTeamMember>) 
                TLSUtils.getRequest().getSession().getAttribute(Constants.USER_WORKSPACES);
        if (wsList == null || wsList.size() == 0)
            return;
        
        for (AmpTeamMember atm : wsList) {
            MenuItem mi = new MenuItem(MenuConstants.WORKSPACE_ITEM, atm.getAmpTeam().getName(),
                    atm.getAmpTeam().getName(), String.format(template.url, atm.getAmpTeamMemId()), template.flags, 
                    template.requestUrl, template.groupKeys);
            mi.setParent(menuItem);
            menuItem.appendChild(mi);
        }
    }

}
