package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

/**
 * 
 * @author Constantin Dolghier
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_FUNDING_PLEDGES_DOCUMENT")
public class FundingPledgesDocument extends ObjectReferringDocument implements Identifiable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_FUNDING_PLEDGES_DOCUMENT_seq")
    @SequenceGenerator(name = "AMP_FUNDING_PLEDGES_DOCUMENT_seq", sequenceName = "AMP_FUNDING_PLEDGES_DOCUMENT_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pledge_id")
    private FundingPledges pledgeid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "uuid", nullable = false)
    private String uuid;


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
