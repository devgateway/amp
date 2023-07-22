package org.digijava.module.contentrepository.dbentity;

import org.digijava.module.aim.dbentity.AmpTeam;

import javax.persistence.*;

@Entity
@Table(name = "CR_SHARED_DOC")
@Cacheable
public class CrSharedDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "node_uuid")
    private String nodeUUID;

    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private AmpTeam team;

    @Column(name = "shared_private_res_uuid")
    private String sharedPrivateNodeUUID;

    @Column(name = "shared_node_version_uuid")
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
