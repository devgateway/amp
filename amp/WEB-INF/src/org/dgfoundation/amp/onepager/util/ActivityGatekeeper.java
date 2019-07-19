package org.dgfoundation.amp.onepager.util;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.wicket.util.time.Duration;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.kernel.ampapi.exception.ActivityLockNotGrantedException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.ShaCrypt;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;

public class ActivityGatekeeper {
    private static HashMap<String, String> timestamp = new HashMap<String, String>();
    private static HashMap<String, String> keycode = new HashMap<String, String>();
    private static HashMap<String, Long> userEditing = new HashMap<String, Long>();
    private static Boolean lock = false;
    private static final long LOCK_TIMEOUT = 15000; //ms
    
    public final static Integer REFRESH_LOCK_VALID = 0; //activity locked by current user
    public final static Integer REFRESH_LOCK_LOCKED = 1; //activity locked by other user
    public final static Integer REFRESH_LOCK_EXPIRED = 2; //activity lock expired on purpose (eg. fm mode change)

    private static final Logger logger = Logger.getLogger(ActivityGatekeeper.class);
    
    public static Duration getRefreshInterval(){
        return Duration.milliseconds((long) (LOCK_TIMEOUT*0.7));
    }
    
    private static void clearLists(String id){
        synchronized (timestamp) {
            timestamp.remove(id);
            keycode.remove(id);
            userEditing.remove(id);
        }
    }
    
    public static void pageModeChange(String id){
        clearLists(id);
    }

    public static String lockActivity(String id, long userId){
        long currentTime;
        synchronized (timestamp) {
            currentTime = System.currentTimeMillis();
            String time = timestamp.get(id);
            if (time != null){
                long storedTime = Long.parseLong(time);
                if (currentTime - storedTime < LOCK_TIMEOUT)
                    return null; //LOCK in effect
                else{
                    //expired lock
                    clearLists(id);
                }
            }
            timestamp.put(id, String.valueOf(currentTime));
            String hash = ShaCrypt.crypt(id + currentTime);
            keycode.put(id, hash);
            userEditing.put(id, userId);
            return hash;
        }
    }
    
    public static Integer refreshLock(String id, String hash, Long userId){
        long currentTime = System.currentTimeMillis();
        synchronized (timestamp){
            if (verifyLock(id, hash)){
                    timestamp.put(id, String.valueOf(currentTime));
                return REFRESH_LOCK_VALID;
            }
            else{
                if ((keycode.get(id) == null && timestamp.get(id) == null && userEditing.get(id) == null) ||
                    (keycode.get(id) != null && userEditing.get(id).longValue() == userId.longValue()))
                    return REFRESH_LOCK_EXPIRED; //lock was cleared on purpose, page probably getting refreshed now
                else
                    return REFRESH_LOCK_LOCKED;
            }
        }
    }
    
    public static boolean verifyLock(String id, String hash){
        if (keycode.get(id) != null && keycode.get(id).equals(hash))
            return true;
        else
            return false;
    }
    
    public static void unlockActivity(String id, String hash){
        synchronized (timestamp) {
            if (verifyLock(id, hash)){
                clearLists(id);
                logger.debug("activity unlocked");
            }
        }
    }

    public static String buildRedirectLink(String id, long currentUserId) {
        Long editingUserId = ActivityGatekeeper.getUserEditing(String.valueOf(id));
        if (editingUserId == null) {
            logger.error("user editing " + id + " not found in the userEditing list, inserting a dummy value!", new
                    RuntimeException("dummy exception"));
            editingUserId = currentUserId;
        }
        return "/aim/viewActivityPreview.do~activityId=" + id + "~editingUserId=" + editingUserId;
    }
    
    public static Long getUserEditing(String id) {
        return userEditing.get(id);
    }

    public static String buildPermissionRedirectLink(String id) {
        return "/aim/viewActivityPreview.do~activityId=" + id + "~editPermissionError=1";
    }

    public static String buildPreviewUrl(String id) {
        return "/aim/viewActivityPreview.do~activityId=" + id;
    }
    
    /**
     * THIS FUNCTION IS NOT AUTHORITATIVE (e.g. it filters out attempts to cheat the system, but does not check into account all the parameters)
     * TODO: move all the decision-making regarding editing permissions in a single place (like this function)
     * @param activityId
     * @return
     */
    public static boolean allowedToEditActivity(String activityId)
    {
        try
        {
            TeamMember teamMember = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
            if (teamMember == null)
                return false;

            if("Management".toLowerCase().equals(teamMember.getTeamAccessType().toLowerCase()))
                return false;
            
            long ampActivityId = Long.parseLong(activityId);
            
            if (!WorkspaceFilter.isActivityWithinWorkspace(ampActivityId))
                return false;
                        
            return true;

        }
        catch(NumberFormatException ex)
        {
            return true; // allowed to add new activity if the link was generated (a little bit fishy)
        }
        catch(Exception e)
        {
            logger.error("error while trying to decide whether allowed to edit activity", e);
            return false;
        }
    }
    
    /**
     *
     * @param ampActivityId
     * @param ampTeamMemId
     * @param runnable
     */
    public static void doWithLock(Long ampActivityId, Long ampTeamMemId, Runnable runnable) {
        String activityId = ampActivityId == null ? null : ampActivityId.toString();
        String key = null;
        try {
            if (ampActivityId != null) {
                key = ActivityGatekeeper.lockActivity(activityId, ampTeamMemId);
                if (key == null) {
                    Long editingUserId = ActivityGatekeeper.getUserEditing(activityId);
                    throw new ActivityLockNotGrantedException(editingUserId);
                }
            }
            
            PersistenceManager.inTransaction(runnable);
        } finally {
            if (key != null) {
                ActivityGatekeeper.unlockActivity(activityId, key);
            }
        }
    }
    

}
