/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuConstants;
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
    private List<String> messageEntryPriority = Arrays.asList(MenuConstants.MESSAGES_TAB,
            MenuConstants.MESSAGES_ALERT_TAB, MenuConstants.MESSAGES_APPROVAL, MenuConstants.MESSAGES_EVENT_TAB, 
            MenuConstants.MESSAGES_DEFAULT_TAB);
    
    private List<String> calendarEntryPriority = Arrays.asList(MenuConstants.CALENDAR_YEARLY,
            MenuConstants.CALENDAR_MONTHLY, MenuConstants.CALENDAR_WEEKLY, MenuConstants.CALENDAR_DAILY);
    
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
    public synchronized Set<String> getEnabledSettings(Long templateId) {
        if (atomicVisibilityChanged.get()) {
            visibleDataPerView = new TreeMap<AmpView, Set<String>>();
        }
        // reset to null if there was any FM change in the meantime
        AmpView currentView = MenuUtils.getCurrentView();
        visibleData = visibleDataPerView.get(currentView);
        if (visibleData == null) {
            logger.error("visibleData for MenuVisibility is null!");
            // rebuild visibleData for the current view or use cached on from visibleDataPerView 
            super.getEnabledSettings(templateId);
            // keep only the 1st FM option for priority lists
            selectFirstPriorityOnly();
        }
        visibleDataPerView.put(currentView, visibleData);
        return visibleData;
    }
    
    /**
     * Filters out duplicate entries (Messages, Calendar), since there can be multiple entries enabled in FM
     * and we have to replicate old functionality, i.e. use the last one selected.
     */
    protected void selectFirstPriorityOnly() {
        selectFirstPriorityOnly(messageEntryPriority.iterator());
        selectFirstPriorityOnly(calendarEntryPriority.iterator());
    }
    
    protected void selectFirstPriorityOnly(Iterator<String> priorityIterator) {
        while (priorityIterator.hasNext()) {
            String data = priorityIterator.next();
            if (visibleData.contains(data)) {
                while (priorityIterator.hasNext()) {
                    data = priorityIterator.next();
                    visibleData.remove(data);
                }
            }
        }
    }
    
}
