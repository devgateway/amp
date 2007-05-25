/*
 *   AllTests.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: May 11, 2004
 * 	 CVS-ID: $Id$
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.kernel.util;

import junit.framework.TestCase;

public class TestRequestUtils
    extends TestCase {
    private RequestUtils requestUtils = null;

    public TestRequestUtils(String name) {
        super(name);
    }

    public void testGetForwarderAddress() {
        String forwardParam = null;
        boolean includeLocal = true;
        String expectedReturn = null;

        String actualReturn = RequestUtils.getForwarderAddress(forwardParam,
            includeLocal);
        assertEquals("Test 1", expectedReturn, actualReturn);

        actualReturn = RequestUtils.getForwarderAddress(
            "  192.168.0.1, 2.1.4.5", includeLocal);
        assertEquals("Test 2", "192.168.0.1", actualReturn);

        actualReturn = RequestUtils.getForwarderAddress("  192.168.0., 2.1.4.5",
            includeLocal);
        assertNull("Test 3", actualReturn);

        actualReturn = RequestUtils.getForwarderAddress("aaa  192.168.0.1, 2.1.4.5",
            includeLocal);
        assertNull("Test 4", actualReturn);
    }

}