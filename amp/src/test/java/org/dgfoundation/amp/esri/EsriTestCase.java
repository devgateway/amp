package org.dgfoundation.amp.esri;

import org.dgfoundation.amp.testutils.ActivityComparator;
import org.dgfoundation.amp.testutils.ActivityDigest;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.esrigis.helpers.SimpleLocation;

import java.util.*;


import junit.framework.TestCase;

public class EsriTestCase extends TestCase{

	public EsriTestCase(String name) {
		super(name);
	}
	
	public boolean checkActivitiesList(List<AmpActivityVersion> inActivities, SortedSet<ActivityDigest> corActivities)
	{
		SortedSet<AmpActivityVersion> sortedInActivities = new TreeSet<AmpActivityVersion>(new ActivityComparator());
		sortedInActivities.addAll(inActivities);
		
		assertEquals("wrong number of activities in result", corActivities.size(), sortedInActivities.size());
		
		Iterator<AmpActivityVersion> activitiesIterator = sortedInActivities.iterator();
		Iterator<ActivityDigest> corActivitiesIterator = corActivities.iterator();
		
		// the iterators are both over sets of the same size
		while (activitiesIterator.hasNext())
		{
			AmpActivityVersion act = activitiesIterator.next();
			ActivityDigest actDigest = corActivitiesIterator.next();
			
			assertTrue("activities are not equal", actDigest.matchesActivity(act));
		}
		
		return true;
	}
	
	public boolean checkLocationsList(List<AmpCategoryValueLocations> locs, String... locations)
	{
		String[] sortedLocs = locations;
		Arrays.sort(sortedLocs);
		assertEquals("wrong number of locations in result", sortedLocs.length, locs.size());
		for(int i = 0; i < sortedLocs.length; i++)
		{
			String a = sortedLocs[i];
			String b = locs.get(i).toString();
			assertEquals(a, b);
		}
		
		return true;
	}

	public boolean checkSimpleLocations(List<SimpleLocation> locs, SimpleLocation... corLocations)
	{
		List<SimpleLocation> input = new ArrayList<SimpleLocation>(locs);
		Collections.sort(input);
		
		List<SimpleLocation> corInput = Arrays.asList(corLocations);
		Collections.sort(corInput);
		
		assertEquals("wrong number of locations in result", corInput.size(), input.size());
		for(int i = 0; i < corInput.size(); i++)
		{
			SimpleLocation a = corInput.get(i);
			SimpleLocation b = input.get(i);
			assertEquals(a, b);
		}
		
		return true;
	}

	public SortedSet<ActivityDigest> activityDigestList(ActivityDigest... activities)
	{
		SortedSet<ActivityDigest> out = new TreeSet<ActivityDigest>();
		for(ActivityDigest dig:activities)
			out.add(dig);
		return out;
	}
}
