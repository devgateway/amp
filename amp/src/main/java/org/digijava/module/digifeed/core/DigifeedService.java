package org.digijava.module.digifeed.core;

import org.apache.log4j.Logger;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;

import java.util.Iterator;

/**
 * DigifeedService.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Nov 4, 2005
 * @see FeedControl
 */
public class DigifeedService extends AbstractServiceImpl {

    private static Logger logger = Logger.getLogger(DigifeedService.class);

    
    protected void processInitEvent(ServiceContext serviceContext)
            throws ServiceException {
        try {
            String realRootPath=serviceContext.getRealPath("/src/main/webapp/WEB-INF/");
            logger.debug("Computed WEB-INF realPath is "+realRootPath);
            FeedControl.initFeeds(realRootPath);        
            logger.info(this.toString());
            
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

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
