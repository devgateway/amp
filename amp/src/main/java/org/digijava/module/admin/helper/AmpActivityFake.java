/**
 * 
 */
package org.digijava.module.admin.helper;

import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;

/**
 * @author dan
 *
 */
public class AmpActivityFake implements LoggerIdentifiable{

    /**
     * 
     */
    
    private String name;
    private AmpTeam team;
    private String ampId;
    private Long ampActivityId;
    private String status;
    private boolean draft;
    private boolean frozen = false;
    
    public AmpActivityFake(String name, String ampId, Long ampActivityId) {
        super();
        this.name = name;
        this.ampId = ampId;
        this.ampActivityId = ampActivityId;
    }

    private AmpActivityGroup ampActGroup;
    
    public AmpActivityFake(String name, AmpTeam team, String ampId, Long ampActivityId, AmpActivityGroup ampActGroup) {
        super();
        this.name = name;
        this.team = team;
        this.ampId = ampId;
        this.ampActivityId = ampActivityId;
        this.ampActGroup = ampActGroup;
    }

    public AmpActivityGroup getAmpActGroup() {
        return ampActGroup;
    }

    public void setAmpActGroup(AmpActivityGroup ampActGroup) {
        this.ampActGroup = ampActGroup;
    }

    public AmpActivityFake(String name, AmpTeam team, String ampId, Long ampActivityId) {
        super();
        this.name = name;
        this.team = team;
        this.ampId = ampId;
        this.ampActivityId = ampActivityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmpTeam getTeam() {
        return team;
    }

    public void setTeam(AmpTeam team) {
        this.team = team;
    }

    public String getAmpId() {
        return ampId;
    }

    public void setAmpId(String ampId) {
        this.ampId = ampId;
    }

    public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public AmpActivityFake() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object getIdentifier() {
        // TODO Auto-generated method stub
        return this.getAmpActivityId();
    }

    @Override
    public Object getObjectType() {
        // TODO Auto-generated method stub
        return this.getClass().getName();
    }

    @Override
    public String getObjectName() {
        // TODO Auto-generated method stub
        return "("+this.getAmpId()+") "+this.getName();
    }
         @Override
        public String getObjectFilteredName() {
        return DbUtil.filter(getObjectName());
    }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isDraft() {
            return draft;
        }

        public void setDraft(boolean draft) {
            this.draft = draft;
        }

        public boolean getFrozen() {
            return frozen;
        }

        public void setFrozen(boolean isFrozen) {
            this.frozen = isFrozen;
        }


}
