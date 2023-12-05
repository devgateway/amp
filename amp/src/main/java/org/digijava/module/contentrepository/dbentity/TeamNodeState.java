package org.digijava.module.contentrepository.dbentity;
/**
 * used to hold information about node's specific versions
 * @author Dare
 *
 */
public class TeamNodeState {
    
    private Long id;
    private String nodeUUID;
    private String versionID;
    
    public TeamNodeState() {        
    }
    
    public TeamNodeState(String nodeUUID, String versionID) {
        this.nodeUUID=nodeUUID;
        this.versionID=versionID;
    }
    
    public String getClassName() {
        return "s"; //state
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNodeUUID() {
        return nodeUUID;
    }
    public void setNodeUUID(String nodeUUID) {
        this.nodeUUID = nodeUUID;
    }
    public String getVersionID() {
        return versionID;
    }
    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }   
    
}
