package org.digijava.module.contentrepository.dbentity;
/**
 * used to hold information about node's specific versions
 * @author Dare
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "CR_TEAM_NODE_STATE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "state_clazz", discriminatorType = DiscriminatorType.STRING)
public class TeamNodeState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CR_TEAM_NODE_STATE_seq")
    @SequenceGenerator(name = "CR_TEAM_NODE_STATE_seq", sequenceName = "CR_TEAM_NODE_STATE_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "node_UUID")
    private String nodeUUID;

    @Column(name = "node_version_uuid")
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
