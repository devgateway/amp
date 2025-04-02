package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpIndicatorWorkspace implements Serializable{

    private AmpTeam workspace;
    private AmpIndicatorLayer indicatorLayer;
    private Long indicatorWorkspaceId;

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
