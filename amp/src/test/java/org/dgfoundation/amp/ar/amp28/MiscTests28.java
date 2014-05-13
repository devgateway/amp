package org.dgfoundation.amp.ar.amp28;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Pledges Form tests
 * @author Dolghier Constantin
 *
 */
public class MiscTests28 extends AmpTestCase
{
	
	private MiscTests28(String name)
	{
		super(name);		
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MiscTests28.class.getName());
		suite.addTest(new MiscTests28("testComparingObjectVisibility"));
		suite.addTest(new MiscTests28("testComparingObjectVisibilityInterClass"));
		return suite;
	}
	
	public void testComparingObjectVisibility()
	{
		AmpFieldsVisibility afv1 = new AmpFieldsVisibility();
		afv1.setId(2l);
		
		AmpFieldsVisibility afvNullId = new AmpFieldsVisibility();
		AmpFieldsVisibility afv2 = new AmpFieldsVisibility();
		afv2.setId(3l);
		
		assertEquals(0, afv1.compareTo(afv1));
		assertEquals(0, afvNullId.compareTo(afvNullId));
		assertTrue(afvNullId.compareTo(afv1) > 0);
		assertTrue(afv1.compareTo(afvNullId) < 0);
		
		assertTrue(afv1.compareTo(afv2) < 0);
		assertTrue(afv2.compareTo(afv1) > 0);
		
		AmpFieldsVisibility[] fields = new AmpFieldsVisibility[] {afv1, afv2, afvNullId};
		for(int i = 0; i < fields.length; i++)
			for(int j = 0; j < fields.length; j++)
			{
				assertEquals(i == j, fields[i].equals(fields[j]));
				assertEquals(i == j, fields[j].equals(fields[i]));
			}
	}
	
	public void testComparingObjectVisibilityInterClass()
	{
		AmpObjectVisibility[] fields = new AmpObjectVisibility[] {new AmpFieldsVisibility(), new AmpFeaturesVisibility(), new AmpModulesVisibility(), new AmpTemplatesVisibility()};
		for(int i = 0; i < fields.length; i++)
			fields[i].setId((i + 1) * 3l);
		
		for(int i = 0; i < fields.length; i++)
			for(int j = 0; j < fields.length; j++)
			{
				assertEquals(fields[i].getClass().getName().compareTo(fields[j].getClass().getName()), fields[i].compareTo(fields[j]));
				assertEquals(fields[j].getClass().getName().compareTo(fields[i].getClass().getName()), fields[j].compareTo(fields[i]));
				assertEquals(i == j, fields[i].equals(fields[j]));
				assertEquals(j == i, fields[j].equals(fields[i]));
			}
	}
	
	
	@Override
    protected void setUp() throws Exception
    {
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}