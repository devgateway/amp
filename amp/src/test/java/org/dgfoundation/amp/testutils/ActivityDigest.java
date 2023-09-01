package org.dgfoundation.amp.testutils;

import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * short description of an AmpActivity, used for comparing outputs
 * @author Dolghier Constantin
 *
 */
public class ActivityDigest implements Comparable<ActivityDigest>  
{
    protected String activityName;
    protected String ampId;
    
    public ActivityDigest(String activityName, String ampId)
    {
        this.activityName = activityName;
        this.ampId = ampId;
    }
    
    @Override
    public int compareTo(ActivityDigest other)
    {
        int comp = activityName.compareTo(other.activityName);
        if (comp != 0)
            return comp;
        
        comp = ampId.compareTo(other.ampId);
        if (comp != 0)
            return comp;
        
        return 0;
    }
    
    @Override
    public int hashCode()
    {
        return (17 * activityName.hashCode()) ^ ampId.hashCode();
    }
    
    @Override
    public boolean equals(Object oth)
    {
        if (!(oth instanceof ActivityDigest))
            return false;
        
        ActivityDigest other = (ActivityDigest) oth;
        return this.compareTo(other) == 0;
    }
    
    public boolean matchesActivity(AmpActivityVersion activity)
    {
        return this.activityName.equals(activity.getName()) && this.ampId.equals(activity.getAmpId());
    }   
}
