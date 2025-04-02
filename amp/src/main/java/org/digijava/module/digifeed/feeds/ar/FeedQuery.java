/**
 * FeedQuery.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.feeds.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.commons.lang.NotImplementedException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.digifeed.core.GenericFeedQuery;
import org.digijava.module.digifeed.core.GenericFeedRequest;

/**
 * This class provides access to the business side object tree for AiDA
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 26, 2005
 * @see org.digijava.module.digifeed.core.GenericFeedQuery
 * 
 */
// get the max id (the last id) - this can be optimized and cached
public class FeedQuery extends GenericFeedQuery {

    public FeedQuery() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.digifeed.core.GenericFeedQuery#query(org.digijava.module.digifeed.core.GenericFeedRequest)
     */
    public List select(GenericFeedRequest r) throws Exception {
        List ret = new ArrayList();
        Session session = PersistenceManager.getSession();

        FeedRequest rr = (FeedRequest) r;

        if ("all".equalsIgnoreCase(rr.getIdList())) {
            Query qry = session.createQuery("from "
                    + AmpReports.class.getName());
            Iterator i = qry.list().iterator();
            while (i.hasNext()) {
                AmpReports element = (AmpReports) i.next();
                ret.add(element);
            }
        } else {
            StringTokenizer st = new StringTokenizer(rr.getIdList(), ",");
            while (st.hasMoreTokens()) {
                String sid = st.nextToken();
                AmpReports reportMeta = (AmpReports) session.get(
                        AmpReports.class, new Long(sid));
                ret.add(reportMeta);
            }
        }

        
        return ret;
    }

    /**
     * @return Returns the last.
     */
    public boolean isLast() {
        return last;
    }

    /**
     * @param last
     *            The last to set.
     */
    public void setLast(boolean last) {
        this.last = last;
    }

    public Integer count(GenericFeedRequest r) throws Exception {
        throw new NotImplementedException(
                "count method is not used by this feed");
    }

    /* (non-Javadoc)
     * @see org.digijava.module.digifeed.core.GenericFeedQuery#saveObject(java.lang.Object, java.util.Map)
     */
    public void saveObject(Object o, Map metadata) {
        
        
    }

}
