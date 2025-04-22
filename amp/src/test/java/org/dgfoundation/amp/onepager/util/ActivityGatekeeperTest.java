package org.dgfoundation.amp.onepager.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityGatekeeperTest {

    @Test
    public void testLockDifferentUser() {
        String key = ActivityGatekeeper.lockActivity("1", 1);
        Assertions.assertNotNull(key);
        Assertions.assertTrue(ActivityGatekeeper.verifyLock("1", key));
        String key2 = ActivityGatekeeper.lockActivity("1", 2);// lock not aquired
        Assertions.assertNull(key2);
        ActivityGatekeeper.unlockActivity("1", key);
        Assertions.assertFalse(ActivityGatekeeper.verifyLock("1", key));
    }

    @Test
    public void testLockSecondTimeSameUserDifferentSession() {
        String key = ActivityGatekeeper.lockActivity("1", 1);
        Assertions.assertNotNull(key);
        String key2 = ActivityGatekeeper.lockActivity("1", 1);
        Assertions.assertNull(key2);
        ActivityGatekeeper.unlockActivity("1", key);
        Assertions.assertFalse(ActivityGatekeeper.verifyLock("1", key));
    }

    @Test
    public void testLockNullActivityId() {
        String key = ActivityGatekeeper.lockActivity(null, 1);
        Assertions.assertNotNull(key);
        String key2 = ActivityGatekeeper.lockActivity(null, 2);
        Assertions.assertNull(key2);
        ActivityGatekeeper.unlockActivity(null, key);
        Assertions.assertFalse(ActivityGatekeeper.verifyLock(null, key));
    }
}
