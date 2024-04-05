/**
 * XmlPatcherScheduler.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.scheduler;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Provides support for
 *         scheduling of patch execution order. The default behavior is to sort
 *         patches in natural order. However it may be desirable that a
 *         different execution schedule is to be followed.
 *         <p>
 *         The execution schedule does not refer to intrinsic conditions (like
 *         patch dependencies or any other logical conditions that are part of
 *         the XML file).
 *         <p>
 *         Examples of schedulers are:
 *         <ul>
 *         <li>by discovery date
 *         <li>by location priority - some locations/modules may get higher
 *         priority than others. For example you may wish to build a scheduler
 *         where the Category Manager module has higher priority for applying
 *         new patches than other modules
 *         <li>by patch attempts - you may want to try new patches first, that
 *         were not tried previously
 *         <li>by patch status - we can find useful running re-applicable failed
 *         patches before or after open patches
 *         </ul>
 *         The schedule will be available by iterating the collection obtained
 *         by {@link #getScheduledPatchCollection()}.
 */
public abstract class XmlPatcherScheduler {
    List<AmpXmlPatch> patches;
    Map<String, Object> properties;

    /**
     * Builds a new scheduler based on the list of patches coming from the
     * database.
     * 
     * @param patches
     * @param properties
     *            the properties map read by the patcher service. Useful so that
     *            schedulers may receive properties from the digi.xml file
     */
    public XmlPatcherScheduler(Map<String, Object> properties,
            List<AmpXmlPatch> patches) {
        this.patches = patches;
        this.properties = properties;
    }

    /**
     * Override this with a method that would schedule the patches desirably.
     * The iterator of the returned collection will provide the schedule
     * 
     * @return the scheduled patch collection
     */
    public abstract Collection<AmpXmlPatch> getScheduledPatchCollection();
}
