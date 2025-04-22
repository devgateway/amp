/**
 * @author Adrian Popescu
 */
package org.digijava.module.xmlpatcher.scheduler;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;

import java.util.*;

/**
 * @author Adrian Popescu - Sample scheduler,
 *         returns patches ordered by their location name + file name. 
 * @see org.digijava.module.xmlpatcher.core.XmlPatcherService#processInitEvent(org.digijava.kernel.service.ServiceContext)
 */
public class NaturalLocationOrderXmlPatcherScheduler extends XmlPatcherScheduler {

    
    public static class NaturalLocationOrderPatchComparator implements
            Comparator<AmpXmlPatch> {
        @Override
        public int compare(AmpXmlPatch arg0, AmpXmlPatch arg1) {
            if(arg0.getLocation().compareTo(arg1.getLocation())==0){
                return arg0.getPatchId().compareTo(arg1.getPatchId());
            }
            return arg0.getLocation().compareTo(arg1.getLocation());
        }

    }

    /**
     * @see XmlPatcherScheduler#XmlPatcherScheduler(Map, Set)
     * @param properties
     * @param patches
     */
    public NaturalLocationOrderXmlPatcherScheduler(Map<String, Object> properties,
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
        Collection<AmpXmlPatch> ret = new TreeSet<AmpXmlPatch>(
                new NaturalLocationOrderPatchComparator());
        ret.addAll(patches);
        return ret;
    }

}
