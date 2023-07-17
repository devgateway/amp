package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_INDICATOR_WORKSPACE")
public class AmpIndicatorWorkspace implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INDICATOR_WORKSPACE_seq")
    @SequenceGenerator(name = "AMP_INDICATOR_WORKSPACE_seq", sequenceName = "AMP_INDICATOR_WORKSPACE_seq", allocationSize = 1)
    @Column(name = "amp_indicator_workspace_id")
    private Long indicatorWorkspaceId;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private AmpTeam workspace;

    @ManyToOne
    @JoinColumn(name = "amp_indicator_layer_id")
    private AmpIndicatorLayer indicatorLayer;


    public AmpIndicatorLayer getIndicatorLayer() {
        return indicatorLayer;
    }
    public void setIndicatorLayer(AmpIndicatorLayer indicatorLayer) {
        this.indicatorLayer = indicatorLayer;
    }

    public Long getIndicatorWorkspaceId() {
        return indicatorWorkspaceId;
    }

    public void setIndicatorWorkspaceId(Long indicatorWorkspaceId) {
        this.indicatorWorkspaceId=indicatorWorkspaceId;
    }

    public AmpTeam getWorkspace() {
        return workspace;
    }

    public void setWorkspace(AmpTeam workspace) {
        this.workspace=workspace;
    }
}
