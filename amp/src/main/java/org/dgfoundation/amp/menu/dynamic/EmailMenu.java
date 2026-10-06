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
 * @author Nadejda Mandrescu
 */
public class EmailMenu implements DynamicMenu {
    private static Logger logger = Logger.getLogger(EmailMenu.class);
    
    @Override
    public void process(MenuItem menuItem) {
        String email = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SUPPORT_EMAIL);
        if (StringUtils.isNotBlank(email)) {
            MenuItem parent = menuItem.getParent();
            int pos = parent.getChildren().indexOf(menuItem);
            menuItem = new MenuItem(menuItem.name, menuItem.title, menuItem.tooltip,
                    String.format(menuItem.url, email.trim()),
                    menuItem.flags, menuItem.requestUrl, menuItem.groupKeys);
            parent.getChildren().set(pos, menuItem);
        } else {
            logger.warn(menuItem.name + " cannot be configured: no email specified");
            menuItem.getParent().getChildren().remove(menuItem);
        }
    }

}
