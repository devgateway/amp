package org.digijava.module.contentrepository.dbentity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("pv")
public class TeamNodePendingVersion extends TeamNodeState{
    
    public String getClassName() {
        return "pv";
    }
    
    public TeamNodePendingVersion() {
        super();
    }
    
    public TeamNodePendingVersion(String nodeUUID,String versionID){
        super(nodeUUID,versionID);
    }
}
