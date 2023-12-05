/**
 * FeedRequest.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.feeds.ar;

import org.digijava.module.digifeed.core.GenericFeedRequest;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 26, 2005
 *
 */
public class FeedRequest extends GenericFeedRequest {
    protected String idList;

    
    public String getIdList() {
        return idList;
    }


    public void setIdList(String idList) {
        this.idList = idList;
    }


    public FeedRequest() {
        idList=null;
    }

    

    
}
