/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.Output;

import java.util.Date;

/**
 * @author aartimon@dginternational.org
 * @since Apr 27, 2011
 */
@TranslatableClass (displayName = "Activity Version")
public class AmpActivityVersion extends AmpActivityFields implements Versionable{
    
    /**
     * 
     * NOTE:
     *    All new fields should be added in {@link AmpActivityFields}
     *    
     */
    protected String rejectMessage;
    

    public AmpActivityVersion() {
    }

    public AmpActivityVersion(Long ampActivityId, String name, Date updatedDate, AmpTeamMember modifiedBy, String ampid) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        //this.budget=budget;
        this.updatedDate=updatedDate;
        this.modifiedBy = modifiedBy;
        this.ampId=ampid;
    }

    public AmpActivityVersion(Long ampActivityId, String name, String ampid) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        this.ampId=ampid;
    }

    /* Note, archived should be Boolean to support null values */
    public AmpActivityVersion(Long ampActivityId, String name, String ampid, Boolean archived) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        this.ampId=ampid;
        this.archived = archived;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object getValue() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Output getOutput() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        throw new AssertionError("Not implemented");
    }
    
    public static String sqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpActivityVersion.class, "name").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpActivityVersion.class, "name").getSQLFunctionCall(idSource + ".ampActivityId");
    }
    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public void addLocation(AmpActivityLocation activityLocation) {
        activityLocation.setActivity(this);
        getLocations().add(activityLocation);
    }
}