package org.digijava.kernel.ampapi.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteCache;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/***
 * 
 * @author Diego Dimunzio
 * 
 */
public class AuthRequestFilter implements ContainerRequestFilter {
	// Inject request into the filter
	@Context
	private HttpServletRequest httpRequest;

	@Override
	public ContainerRequest filter(ContainerRequest arg0) {
        SiteDomain siteDomain = null;
        //yet to strip the mainPath dynamically, committed hardcoded for testing purposes
        String mainPath="/rest";
        siteDomain = SiteCache.getInstance().getSiteDomain(httpRequest.getServerName(),mainPath);
        httpRequest.setAttribute(org.digijava.kernel.Constants.CURRENT_SITE,siteDomain);
        if (httpRequest.getParameter(EPConstants.LANGUAGE) != null) {
        	Locale locale = new Locale();
        	locale.setCode(httpRequest.getParameter(EPConstants.LANGUAGE));
        	httpRequest.setAttribute(org.digijava.kernel.Constants.NAVIGATION_LANGUAGE, locale);
        }
		TLSUtils.populate(httpRequest);
		return arg0;
	}
}
