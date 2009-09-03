/**
 * LocationPriorityXmlPatcherScheduler.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.scheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.digijava.module.xmlpatcher.dbentity.XmlPatch;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Provides a location prioritized patch scheduler. Reads a scheduler
 *         property called locationPriority, which represents a comma separated
 *         list of locations, in the order of the execution priority. As location you may enter
 *         any part of the URI (the path) to the patch file. For eg. a valid location string is:
 *         categorymanager,repository/admin,gateperm
 * @see org.digijava.module.xmlpatcher.dbentity.XmlPatch#getLocation()
 */
public class LocationPriorityXmlPatcherScheduler extends XmlPatcherScheduler {

	protected List<String> locationPriority;

	public class LocationPriorityXmlPatchComparator implements
			Comparator<XmlPatch> {

		protected Integer getLocationKeyIndex(XmlPatch patch) {
			for (int i = 0; i < locationPriority.size(); i++)
				if (patch.getLocation().contains(locationPriority.get(i)))
					return new Integer(i);
			return new Integer(-1);
		}

		@Override
		public int compare(XmlPatch arg0, XmlPatch arg1) {
			return getLocationKeyIndex(arg0).compareTo(
					getLocationKeyIndex(arg1));
		}

	}

	/**
	 * @param properties
	 * @param naturalOrderedPatches
	 */
	public LocationPriorityXmlPatcherScheduler(Map<String, Object> properties,
			List<XmlPatch> patches) {
		super(properties, patches);
		String locationPriorityString = (String) properties
				.get(XmlPatcherConstants.SchedulerProperties.LOCATION_PRIORITY);
		if (locationPriorityString != null)
			locationPriority = Arrays.asList(locationPriorityString.split(","));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.digijava.module.xmlpatcher.scheduler.XmlPatcherScheduler#
	 * getScheduledPatchCollection()
	 */
	@Override
	public Collection<XmlPatch> getScheduledPatchCollection() {
		Collections.sort(patches, new LocationPriorityXmlPatchComparator());
		return patches;
	}

}
