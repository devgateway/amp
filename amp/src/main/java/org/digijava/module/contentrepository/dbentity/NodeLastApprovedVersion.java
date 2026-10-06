package org.digijava.module.contentrepository.dbentity;

public class NodeLastApprovedVersion extends TeamNodeState{
    
    public NodeLastApprovedVersion() {
        super();
    }
    
    public NodeLastApprovedVersion(String nodeUUID, String lastApprovedVersionUUID) {
        super(nodeUUID,lastApprovedVersionUUID);
    }
    
    public String getClassName() {
        return "lav"; //last approved version
    }
}
