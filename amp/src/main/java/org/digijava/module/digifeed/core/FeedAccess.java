/**
 * FeedAccess.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 21, 2005
 *
 */
public class FeedAccess {
    private GenericFeedQuery query;
    private FeedInfo info;
    private GenericFeedBinder sampleBinder;
    private GenericFeedRequest sampleRequest;
    

    /**
     * @return Returns the sampleRequest.
     */
    public GenericFeedRequest getSampleRequest() {
        return sampleRequest;
    }


    /**
     * @param sampleRequest The sampleRequest to set.
     */
    public void setSampleRequest(GenericFeedRequest sampleRequest) {
        this.sampleRequest = sampleRequest;
    }


    public FeedAccess(FeedInfo feedInfo, GenericFeedQuery feedQuery, GenericFeedBinder binder, GenericFeedRequest sampleRequest) {
        this.info=feedInfo;
        this.query=feedQuery;
        this.sampleBinder=binder;
        this.sampleRequest=sampleRequest;
    }


    /**
     * @return Returns the binder.
     */
    public GenericFeedBinder getSampleBinder() {
        return sampleBinder;
    }


    /**
     * @param binder The binder to set.
     */
    public void setSampleBinder(GenericFeedBinder binder) {
        this.sampleBinder = binder;
    }


    /**
     * @return Returns the info.
     */
    public FeedInfo getInfo() {
        return info;
    }


    /**
     * @param info The info to set.
     */
    public void setInfo(FeedInfo info) {
        this.info = info;
    }


    /**
     * @return Returns the query.
     */
    public GenericFeedQuery getQuery() {
        return query;
    }


    /**
     * @param query The query to set.
     */
    public void setQuery(GenericFeedQuery query) {
        this.query = query;
    }
    
}
