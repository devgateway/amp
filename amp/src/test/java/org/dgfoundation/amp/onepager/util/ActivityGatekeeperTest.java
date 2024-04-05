package org.dgfoundation.amp.onepager.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ActivityGatekeeperTest {
    
    @Test
    public void testLockDifferentUser() {
        String key = ActivityGatekeeper.lockActivity("1", 1);
        assertNotNull(key);
        assertTrue(ActivityGatekeeper.verifyLock("1", key));
        String key2 = ActivityGatekeeper.lockActivity("1", 2);// lock not aquired
        assertNull(key2);
        ActivityGatekeeper.unlockActivity("1", key);
        assertFalse(ActivityGatekeeper.verifyLock("1", key));
    }
    
    @Test
    public void testLockSecondTimeSameUserDifferentSession() {
        String key = ActivityGatekeeper.lockActivity("1", 1);
        assertNotNull(key);
        String key2 = ActivityGatekeeper.lockActivity("1", 1);
        assertNull(key2);
        ActivityGatekeeper.unlockActivity("1", key);
        assertFalse(ActivityGatekeeper.verifyLock("1", key));
    }
    
    @Test
    public void testLockNullActivityId() {
        String key = ActivityGatekeeper.lockActivity(null, 1);
        assertNotNull(key);
        String key2 = ActivityGatekeeper.lockActivity(null, 2);
        assertNull(key2);
        ActivityGatekeeper.unlockActivity(null, key);
        assertFalse(ActivityGatekeeper.verifyLock(null, key));
    }
}