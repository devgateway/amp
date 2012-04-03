package org.dgfoundation.amp.onepager.util;

import java.util.HashMap;

import org.apache.wicket.util.time.Duration;
import org.digijava.kernel.util.ShaCrypt;

public class ActivityGatekeeper {
	private static HashMap<String, String> timestamp = new HashMap<String, String>();
	private static HashMap<String, String> keycode = new HashMap<String, String>();
	private static HashMap<String, Long> userEditing = new HashMap<String, Long>();
	private static Boolean lock = false;
	private static final long LOCK_TIMEOUT = 5000; //ms
	
	public final static Integer REFRESH_LOCK_VALID = 0; //activity locked by current user
	public final static Integer REFRESH_LOCK_LOCKED = 1; //activity locked by other user
	public final static Integer REFRESH_LOCK_EXPIRED = 2; //activity lock expired on purpose (eg. fm mode change)

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
		}
		
		String hash = ShaCrypt.crypt(id + currentTime);;
		keycode.put(id, hash);
		userEditing.put(id, userId);
		return hash;
	}
	
	public static Integer refreshLock(String id, String hash, Long userId){
		long currentTime = System.currentTimeMillis();
		if (verifyLock(id, hash)){
			timestamp.put(id, String.valueOf(currentTime));
			return REFRESH_LOCK_VALID;
		}
		else{
			if ((keycode.get(id) == null && timestamp.get(id) == null && userEditing.get(id) == null) ||
				(keycode.get(id) != null && userEditing.get(id) == userId))
				return REFRESH_LOCK_EXPIRED; //lock was cleared on purpose, page probably getting refreshed now
			else
				return REFRESH_LOCK_LOCKED;
		}
			
	}
	
	public static boolean verifyLock(String id, String hash){
		if (keycode.get(id) != null && keycode.get(id).compareTo(hash) == 0)
			return true;
		else
			return false;
	}
	
	public static void unlockActivity(String id, String hash){
		synchronized (timestamp) {
			if (verifyLock(id, hash)){
				clearLists(id);
			}
		}
	}
	
	private static Long getUserEditing(String id){
		return userEditing.get(id);
	}
	
	public static String buildRedirectLink(String id){
		return "/aim/viewActivityPreview.do~public=true~activityId=" + id + "~pageId=2~editError=" + ActivityGatekeeper.getUserEditing(String.valueOf(id));
	}
}
