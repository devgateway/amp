/**
 * DiscoveryTimeXmlPatcherScheduler.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.scheduler;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.digijava.module.xmlpatcher.dbentity.XmlPatch;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Sample scheduler,
 *         returns patches ordered by their discovery time. This is the datetime
 *         when the patch has been discovered by the discovery process invoked
 *         before the actual patching process takes place.
 * @see org.digijava.module.xmlpatcher.core.XmlPatcherService#processInitEvent(org.digijava.kernel.service.ServiceContext)
 */
public class DiscoveryTimeXmlPatcherScheduler extends XmlPatcherScheduler {

	/**
	 * Comparator designed to query the discovered property of XmlPatch
	 * 
	 * @see org.digijava.module.xmlpatcher.dbentity.XmlPatch#getDiscovered()
	 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
	 * 
	 */
	public static class DiscoveryTimeXmlPatchComparator implements
			Comparator<XmlPatch> {
		@Override
		public int compare(XmlPatch arg0, XmlPatch arg1) {
			return arg0.getDiscovered().compareTo(arg1.getDiscovered());
		}

	}

	/**
	 * @see XmlPatcherScheduler#XmlPatcherScheduler(Map, Set)
	 * @param properties
	 * @param patches
	 */
	public DiscoveryTimeXmlPatcherScheduler(Map<String, Object> properties,
			List<XmlPatch> patches) {
		super(properties, patches);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.digijava.module.xmlpatcher.scheduler.XmlPatcherScheduler#
	 * getScheduledPatchCollection()
	 */
	@Override
	public Collection<XmlPatch> getScheduledPatchCollection() {
		Collection<XmlPatch> ret = new TreeSet<XmlPatch>(
				new DiscoveryTimeXmlPatchComparator());
		ret.addAll(patches);
		return ret;
	}

}
