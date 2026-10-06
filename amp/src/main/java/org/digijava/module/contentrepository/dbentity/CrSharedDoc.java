package org.digijava.module.contentrepository.dbentity;

import org.digijava.module.aim.dbentity.AmpTeam;

public class CrSharedDoc {
    private Long id;
    /**
     * This will store node UUID,which is shared
     */
    private String nodeUUID;
    /**
     * team id to which the resource is shared
     */
    private AmpTeam team;
    /**
     * State will show whether this resource is pending approval to appear as team doc, or it's shared through workspaces,e.t.c
     */
    private Integer state;
    /**
     * stores uuid of the original private document,which was shared and became team doc
     */
    private String sharedPrivateNodeUUID;
    
    /**
     * node's version uuid , which is/was requested to be shared
     */
    private String sharedNodeVersionUUID;
    
    public CrSharedDoc(){}
    
    public CrSharedDoc(String uuid,AmpTeam team, Integer state){
        this.nodeUUID=uuid;
        this.team=team;
        this.state=state;
    }

    public AmpTeam getTeam() {
        return team;
    }

    public void setTeam(AmpTeam team) {
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getNodeUUID() {
        return nodeUUID;
    }

    public void setNodeUUID(String nodeUUID) {
        this.nodeUUID = nodeUUID;
    }

    public String getSharedPrivateNodeUUID() {
        return sharedPrivateNodeUUID;
    }

    public void setSharedPrivateNodeUUID(String sharedPrivateNodeUUID) {
        this.sharedPrivateNodeUUID = sharedPrivateNodeUUID;
    }

    public String getSharedNodeVersionUUID() {
        return sharedNodeVersionUUID;
    }

    public void setSharedNodeVersionUUID(String sharedNodeVersionUUID) {
        this.sharedNodeVersionUUID = sharedNodeVersionUUID;
    }

}
