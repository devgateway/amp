/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuUtils;
import org.digijava.module.aim.dbentity.AmpMenuEntryInAdminView;
import org.digijava.module.aim.dbentity.AmpMenuEntryInPublicView;
import org.digijava.module.aim.dbentity.AmpMenuEntryInTeamView;
import org.digijava.module.aim.dbentity.AmpMenuEntryInView;

/**
 * Detects visible Menu Entries. <br><br>
 * 
 * Note: use this until we have a Menu Management UI 
 * that may have different requirements to handle menu entries visibility.
 * @author Nadejda Mandrescu
 */
public class MenuVisibility extends RuleBasedVisibility {
	
	private Map<AmpView, Set<String>> visibleDataPerView = new TreeMap<AmpView, Set<String>>();
	
	@Override
	protected Class<? extends AmpMenuEntryInView> getClazz() {
		switch(MenuUtils.getCurrentView()) {
		case PUBLIC: return AmpMenuEntryInPublicView.class;
		case ADMIN: return AmpMenuEntryInAdminView.class;
		case TEAM: return AmpMenuEntryInTeamView.class;
		}
		return null;
	}

	protected MenuVisibility() {
	}
	
	@Override
	synchronized
	public Set<String> getEnabledSettings() {
		if (atomicVisibilityChanged.get()) {
			visibleDataPerView = new TreeMap<AmpView, Set<String>>();
		}
		visibleData = visibleDataPerView.get(MenuUtils.getCurrentView());
		return super.getEnabledSettings();
	}

}
