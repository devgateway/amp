package org.digijava.module.contentrepository.dbentity;

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
