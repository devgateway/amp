package org.dgfoundation.amp.forms;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

import java.util.HashMap;
import java.util.Map;

import static org.dgfoundation.amp.forms.LockVerificationResult.*;

public class ItemGateKeeper {
    
    /**
     * Map<Pledge.id, TeamMember.id> for users currently editing pledges
     */
    private final HashMap<String, Long> itemsUnderEdit = new HashMap<>();
    
    /**
     * the last time a pledge-under-edit has been touched. If bigger than 5000ms, allow others to edit pledge
     */
    private final Map<String, Long> lastEditTime = new HashMap<>();
    
    /**
     * how many milliseconds to wait before declaring the patient dead, e.g. the editor has forgot about the fact that he is editing a pledge
     */
    public final long LOCK_TIMEOUT = 5000;

    private static final Logger logger = Logger.getLogger(ItemGateKeeper.class);
        
    public synchronized boolean lockActivity(String id, long userId){
        long currentTime;
        currentTime = System.currentTimeMillis();
        Long time = lastEditTime.get(id);
        if (time != null){
            if (currentTime - time < LOCK_TIMEOUT)
                return false; //LOCK in effect
            else{
                //expired lock
                evictItemInfo(id);
            }
        }
        lastEditTime.put(id, currentTime);
        itemsUnderEdit.put(id, userId);
        return true;
    }
    
    public synchronized boolean refreshLock(String id, long userId){
        long currentTime = System.currentTimeMillis();
        if (verifyLock(id, userId).isActionAllowed()){
            lastEditTime.put(id, currentTime);
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * returns one of {@link #REFRESH_LOCK_ALLOWED}, {@link #REFRESH_LOCK_DISALLOWED}, {@link #REFRESH_LOCK_EXPIRED}
     * @param id
     * @param userId
     * @return
     */
    public synchronized LockVerificationResult verifyLock(String id, long userId){
        long curTime = System.currentTimeMillis();
        Long editingUserId = itemsUnderEdit.get(id);
        Long let = lastEditTime.get(id);
        if (let == null)
            return new LockVerificationResult(REFRESH_LOCK_ALLOWED, editingUserId); // never taken
        
        if (let + LOCK_TIMEOUT < curTime) // taken but expired since then -> free to take it if you want
            return new LockVerificationResult(REFRESH_LOCK_EXPIRED, editingUserId);
        
        // gone till here -> activity under lock, let's see whether it is the request user holding the lock
        // editingUserId is not supposed to be null at this point. If we crash here, THE BUG IS ELSEWHERE
        if (editingUserId == userId)
            return new LockVerificationResult(REFRESH_LOCK_ALLOWED, editingUserId);
        
        return new LockVerificationResult(REFRESH_LOCK_DISALLOWED, editingUserId);
    }
    
    public synchronized void unlockActivity(String id, long userId){
        if (verifyLock(id, userId).isActionAllowed()){
            evictItemInfo(id);
        }
    }
    
    public synchronized LockVerificationResult canCurrentUserEdit(String activityId)
    {
        TeamMember teamMember = TeamMemberUtil.getLoggedInTeamMember();
        if (teamMember == null)
            return null;
        
        return verifyLock(activityId, teamMember.getMemberId());
    }
    
    public synchronized void evictItemInfo(String itemId){
        this.itemsUnderEdit.remove(itemId);
        this.lastEditTime.remove(itemId);
    };
}
