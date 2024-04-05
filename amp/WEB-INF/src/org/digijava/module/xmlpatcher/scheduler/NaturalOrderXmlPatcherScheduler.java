/**
 * NaturalOrderXmlPatcherScheduler.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.scheduler;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;

import java.util.*;

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
            List<AmpXmlPatch> patches) {
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
    public Collection<AmpXmlPatch> getScheduledPatchCollection() {
        // TODO Auto-generated method stub
        Set<AmpXmlPatch> naturalSet = new TreeSet<AmpXmlPatch>();
        naturalSet.addAll(patches);
        return naturalSet;
    }

}
