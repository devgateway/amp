/**
 * DigifeedConfig.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;

import java.util.Iterator;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 30, 2005
 * @deprecated
 */
public class DigifeedConfig {
    public DigifeedConfig() {
        //kicks the reflection based feed init
//      FeedControl.initFeeds();
    }
    
    public String toString() {
        StringBuffer sb=new StringBuffer("digiFeed has found the following feeds: ");
        if (FeedControl.getFeeds()==null) return "digiFeed: Feed config initialization failure! The feed config list is null!";
        else 
        {
            Iterator i=FeedControl.getFeeds().iterator();
            while (i.hasNext()) {
                FeedAccess element = (FeedAccess) i.next();
                sb.append(element.getInfo().getName());
                if (i.hasNext()) sb.append(", ");
            }
            return sb.toString();
        }
    }
}
