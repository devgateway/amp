/**
 * 
 */
package org.dgfoundation.amp.menu.dynamic;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.menu.MenuItem;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Viorel Chihai
 */
public class PublicSiteMenu implements DynamicMenu {
    private static Logger logger = Logger.getLogger(PublicSiteMenu.class);
    
    @Override
    public void process(MenuItem menuItem) {
        String publicSiteUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PUBLIC_PORTAL_URL);
        
        if (StringUtils.isNotBlank(publicSiteUrl)) {
            MenuItem parent = menuItem.getParent();
            int pos = parent.getChildren().indexOf(menuItem);
            menuItem = new MenuItem(menuItem.name, menuItem.title, menuItem.tooltip, publicSiteUrl, menuItem.flags,
                    menuItem.requestUrl, menuItem.groupKeys);
            menuItem.setParent(parent);
            parent.getChildren().set(pos, menuItem);
        } else {
            logger.warn(menuItem.name + " cannot be configured: no public site url in global settings");
        }
    }

}
