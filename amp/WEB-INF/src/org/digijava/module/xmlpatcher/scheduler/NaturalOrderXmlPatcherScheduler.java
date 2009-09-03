/**
 * NaturalOrderXmlPatcherScheduler.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.scheduler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.digijava.module.xmlpatcher.dbentity.XmlPatch;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Provides natural
 *         ordering for patches. This is the default behavior if no scheduler is
 *         explicitly defined in the digi service entity.
 */
public class NaturalOrderXmlPatcherScheduler extends XmlPatcherScheduler {

	/**
	 * @param properties
	 * @param patches
	 */
	public NaturalOrderXmlPatcherScheduler(Map<String, Object> properties,
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
		// TODO Auto-generated method stub
		Set<XmlPatch> naturalSet = new TreeSet<XmlPatch>();
		naturalSet.addAll(patches);
		return naturalSet;
	}

}
