package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

/**
 * 
 * @author Constantin Dolghier
 *
 */
public class FundingPledgesDocument extends ObjectReferringDocument implements Identifiable{
    
    private Long id;
    private FundingPledges pledgeid;
    private String title;

    public FundingPledges getPledgeid() {
        return pledgeid;
    }
    public void setPledgeid(FundingPledges pledgeid) {
        this.pledgeid = pledgeid;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    @Override
    protected void detach() {
        pledgeid.getDocuments().remove(this);
        this.pledgeid = null;
    }
    
    @Override public Object getIdentifier(){
        return id;
    }
    
    @Override public String toString(){
        return String.format("Pledge Document with uuid %s and title %s", this.getUuid(), this.getTitle());
    }
    
}
