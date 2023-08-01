/**
 * 
 */
package org.dgfoundation.amp.menu.dynamic;

import org.dgfoundation.amp.menu.MenuItem;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.translation.util.TranslationManager;

import java.util.List;

/**
 * Dynamic Language menu sub-items builder 
 * @author Nadejda Mandrescu
 */
public class LanguageMenu implements DynamicMenu {
    
    @Override
    public void process(MenuItem langMenuItem) {
        if (langMenuItem.getChildren().size() == 0)
            return;
        MenuItem template = langMenuItem.getChildren().iterator().next();
        langMenuItem.getChildren().clear();
        
        List<String[]> locales = TranslationManager.getLocale(PersistenceManager.getRequestDBSession());
        for (String[] langOption : locales) {
            MenuItem mi = new MenuItem(template.name, langOption[1], 
                    langOption[1], String.format(template.url, langOption[0]), template.flags, template.requestUrl, template.groupKeys);
            mi.setParent(langMenuItem);
            langMenuItem.appendChild(mi);
        }
    }
}
