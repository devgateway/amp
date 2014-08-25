package org.digijava.kernel.ampapi.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import com.sun.jersey.oauth.server.OAuthServerRequest;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.sun.jersey.oauth.signature.OAuthSignature;
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
		TLSUtils.populate(httpRequest);
		return arg0;
	}
}
