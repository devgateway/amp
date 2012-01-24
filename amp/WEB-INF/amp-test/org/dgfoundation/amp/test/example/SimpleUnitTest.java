package org.dgfoundation.amp.test.example;

import java.util.Collection;
import java.util.Iterator;

import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.harvest.DBUtil;
import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.util.DbUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SimpleUnitTest extends TestCase {

	public static Test suite() {
		junit.framework.TestSuite suite = new junit.framework.TestSuite();
		suite.addTest(new TestSuite(SimpleUnitTest.class));
		return suite;

	}

	private Collection<AmpOrganisation> orgList;
	private AmpOrganisation organisation;
	private AmpOrganisation organisation2;

	protected void setUp() throws Exception {
		Configuration.initConfig();
		orgList = DbUtil.getAllOrganisation();
		Iterator<AmpOrganisation> iter = orgList.iterator();
		organisation = iter.next();
		organisation2 = iter.next();
	}

	public void testEquals() {
		assertEquals(organisation, organisation);

	}

	public void testNotEquals() {
		assertFalse(organisation.equals(organisation2));

	}

	protected void tearDown() throws Exception {
		orgList = null;
		organisation2 = null;
		organisation = null;
	}

}
