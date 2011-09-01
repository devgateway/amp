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

	public static Duration getRefreshInterval(){
		return Duration.milliseconds((long) (LOCK_TIMEOUT*0.7));
	}
	
	private static void clearLists(String id){
		timestamp.remove(id);
		keycode.remove(id);
		userEditing.remove(id);
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
	
	public static boolean refreshLock(String id, String hash){
		long currentTime = System.currentTimeMillis();
		if (verifyLock(id, hash)){
			timestamp.put(id, String.valueOf(currentTime));
			return true;
		}
		else
			return false;
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
	
	private static long getUserEditing(String id){
		return userEditing.get(id);
	}
	
	public static String buildRedirectLink(String id){
		return "/aim/viewActivityPreview.do~public=true~activityId=" + id + "~editError=" + ActivityGatekeeper.getUserEditing(String.valueOf(id));
	}
}
