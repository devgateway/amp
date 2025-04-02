package org.digijava.module.fundingpledges.form;

import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDocument;

public class TransientDocumentShim extends DocumentShim{
    private TemporaryDocumentData tdd;
    
    public TransientDocumentShim(TemporaryDocumentData tdd){
        this.populateFrom(tdd);
        this.tdd = tdd;
    }
    
    public TemporaryDocumentData getTdd(){
        return this.tdd;
    }
    
    /**
     * uses the TLS request for JCR access
     * @param pledge
     * @return
     */
    public FundingPledgesDocument serializeAndGetPledgeEntry(FundingPledges pledge){
        tdd.setTitle(this.getTitle()); // completed on-request by Struts
        NodeWrapper wrapper = tdd.saveToRepository(TLSUtils.getRequest());
        this.uuid = wrapper.getUuid();
        return this.buildPledgeEntry(pledge);
    }
    
    @Override public String getGeneralLink(){
        return "#";
    }
}
