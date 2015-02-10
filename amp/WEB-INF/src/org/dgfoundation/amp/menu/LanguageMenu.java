/**
 * 
 */
package org.dgfoundation.amp.menu;

import java.util.List;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.translation.util.TranslationManager;

/**
 * Dynamic Language menu sub-items builder 
 * @author Nadejda Mandrescu
 */
public class LanguageMenu implements DynamicMenu {
	private static final String LANGUAGE_URL = "/rest/translations/languages/%s";
	
	@Override
	public void process(MenuItem langMenuItem) {
		List<String[]> locales = TranslationManager.getLocale(PersistenceManager.getSession());
		for (String[] langOption : locales) {
			MenuItem mi = new MenuItem(MenuConstants.LANGUAGE_ITEM, langOption[1], 
					langOption[1], String.format(LANGUAGE_URL, langOption[0]), null);
			langMenuItem.appendChild(mi);
		}
	}
}
