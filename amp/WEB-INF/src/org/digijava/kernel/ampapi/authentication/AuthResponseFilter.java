package org.digijava.kernel.ampapi.authentication;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class AuthResponseFilter implements ContainerResponseFilter {
	protected static final Logger logger = Logger.getLogger(AuthResponseFilter.class);


	@Override
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {
			if("true".equals(TLSUtils.getRequest().getAttribute(SecurityUtil.REMOVE_SESSION))){
				TLSUtils.getRequest().getSession().removeAttribute(Constants.CURRENT_MEMBER);
				TLSUtils.getRequest().removeAttribute(SecurityUtil.REMOVE_SESSION);
				logger.error("Session removed");
			}
		return response;
	}

}
