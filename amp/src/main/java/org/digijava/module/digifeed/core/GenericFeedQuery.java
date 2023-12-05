/**
 * IFeedQuery.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Interface providing the feed method that returns the Business side objects from the database, based on a FeedRequest object as
 * the filter
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 21, 2005 
 */
public abstract class GenericFeedQuery implements Cloneable {
    protected String nextStartWidth;     
    protected boolean last=false;
    
    private static final Logger logger = Logger.getLogger(GenericFeedQuery.class);
    
    public GenericFeedQuery() {
        last=true;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /**
     * Returns a list with the Business beans retrieved from the database, for the given FeedRequest object
     * @param r the FeedRequest object
     * @return the initialized list with objects
     * @throws Exception
     */
    public abstract List select(GenericFeedRequest r) throws Exception;
    
    public abstract Integer count(GenericFeedRequest r) throws Exception;

    public Object query(GenericFeedRequest r) throws Exception {
        logger.info("DigiFeed Request: "+r.toString());
        
        if (r.isCount()) return count(r); else return select(r);
    }
    
    public void persist(List l,Map metadata) {
        Iterator i=l.iterator();
        while (i.hasNext()) {
            Object element = (Object) i.next();
            saveObject(element,metadata);
        }
    }
    
    
    public abstract void saveObject(Object o,Map metadata);

    
    /**
     * @return Returns the last.
     */
    public boolean isLast() {
        return last;
    }

    /**
     * @param last The last to set.
     */
    public void setLast(boolean last) {
        this.last = last;
    }

    /**
     * @return Returns the nextStartWidth.
     */
    public String getNextStartWidth() {
        return nextStartWidth;
    }

    /**
     * @param nextStartWidth The nextStartWidth to set.
     */
    public void setNextStartWidth(String nextStartWidth) {
        this.nextStartWidth = nextStartWidth;
    }
}
