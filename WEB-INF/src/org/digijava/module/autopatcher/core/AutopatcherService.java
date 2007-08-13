package org.digijava.module.autopatcher.core;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;

/**
 * AutopatcherService.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since 2007 08 08
 * 
 */
public class AutopatcherService extends AbstractServiceImpl {

	private static Logger logger = Logger.getLogger(AutopatcherService.class);

	
	protected void processInitEvent(ServiceContext serviceContext)
			throws ServiceException {
		try {
			String realRootPath=serviceContext.getRealPath("/WEB-INF/");
			logger.debug("Computed WEB-INF realPath is "+realRootPath);
			
			
			
			logger.info(this.toString());
			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}

	}
	
	public String toString() {
		StringBuffer sb=new StringBuffer("digiFeed has found the following feeds: ");
		return null;
	}
	}

