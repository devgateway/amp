/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * @author aartimon@dginternational.org 
 * @since Jan 3, 2011
 */
public class AmpAuthWebSession extends AuthenticatedWebSession {
	private static Logger logger = Logger.getLogger(AmpAuthWebSession.class);
	private static Request request;

	private boolean translatorMode;
	private boolean fmMode;
	private TeamMember currentMember;
	private AmpTeamMember ampCurrentMember;
	private HttpSession httpSession;
	private Site site;
	
	
	public AmpAuthWebSession(final Request request) {
		super(request);
		fmMode = false;
		translatorMode = false;
		
		ServletWebRequest wRequest = (ServletWebRequest) request;
		httpSession = wRequest.getHttpServletRequest().getSession();
		currentMember = (TeamMember)httpSession.getAttribute("currentMember");
		if (currentMember != null)
			ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());
		else
			ampCurrentMember = null;
		
		
		WebRequestCycle cycle    = (WebRequestCycle)RequestCycle.get();
	    HttpServletRequest hsRequest   = cycle.getWebRequest().getHttpServletRequest();
		SiteDomain siteDomain = SiteCache.getInstance().getSiteDomain(hsRequest.getServerName(), null);
		site = siteDomain.getSite();
	}

	
	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public AmpTeamMember getAmpCurrentMember() {
		if (ampCurrentMember == null){
			getCurrentMember(); //may initialize currentMember if it's needed
			if (currentMember != null)
				ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());
		}
		return ampCurrentMember;
	}
	public void setAmpCurrentMember(AmpTeamMember ampCurrentMember) {
		this.ampCurrentMember = ampCurrentMember;
	}

	public TeamMember getCurrentMember() {
		if (currentMember == null){
			currentMember = (TeamMember)httpSession.getAttribute("currentMember");
		}
		return currentMember;
	}
	public void setCurrentMember(TeamMember currentMember) {
		this.currentMember = currentMember;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	/*
	public AmpAuthWebSession(final AuthenticatedWebApplication application, final Request request) {
		super(application, request);
	}
	*/
	public boolean isTranslatorMode() {
		return translatorMode;
	}

	public void setTranslatorMode(boolean translatorMode) {
		this.translatorMode = translatorMode;
	}
	
	public void switchTranslatorMode(){
		this.translatorMode = !translatorMode;
	}

	public boolean isFmMode() {
		return fmMode;
	}

	public void setFmMode(boolean fmMode) {
		this.fmMode = fmMode;
	}

	public void switchFMMode(){
		this.fmMode = !fmMode;
	}
	/**
     * Attempts to authenticate a user that has provided the given username and password.
     * @param username current username
     * @param password current password
     * @return <code>true</code> if authentication succeeds, <code>false</code> otherwise
     */
    public boolean authenticate(String username, String password) {
        String u = username == null ? "" : username;
        String p = password == null ? "" : password;

        // Create an Acegi authentication request.
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u, p);

        // Attempt authentication.
        try {
            AuthenticationManager authenticationManager =
                ((OnePagerApp) getApplication()).getAuthenticationManager();
            Authentication authResult = authenticationManager.authenticate(authRequest);
            setAuthentication(authResult);

            logger.info("Login by user '" + username + "'.");
            return true;

        } catch (BadCredentialsException e) {
        	logger.info("Failed login by user '" + username + "'.");
            setAuthentication(null);
            return false;

        } catch (AuthenticationException e) {
        	logger.error("Could not authenticate a user", e);
            setAuthentication(null);
            throw e;

        } catch (RuntimeException e) {
        	logger.error("Unexpected exception while authenticating a user", e);
            setAuthentication(null);
            throw e;
        }
    }

    /**
     * @return the currently logged in user, or null when no user is logged in
     */
    public /*YourAppUserDetails*/ Object getUser() {
    	//TODO:Check amp user bean and switch return type
    	//TODO:
        /*YourAppUserDetails*/Object user = null;
        if (isSignedIn2()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user = authentication.getPrincipal();
        }
        return user;
    }

    /**
     * Returns the current user roles.
     * @return current user roles
     */
    public Roles getRoles() {
        if (isSignedIn2()) {
            Roles roles = new Roles();
            // Retrieve the granted authorities from the current authentication. These correspond one on
            // one with user roles.
            GrantedAuthority[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            for (int i = 0; i < authorities.length; i++) {
                GrantedAuthority authority = authorities[i];
                roles.add(authority.getAuthority());
            }
            return roles;
        }
        return null;
    }

    /**
     * Signout, invalidates the session. After a signout, you should redirect the browser to the home page.
     */
    public void signout() {
    	/*   	
        YourAppUserDetails user = getUser();
        if (user != null) {
        	logger.info("Logout by user '" + user.getUsername() + "'.");
        }
        */
        setAuthentication(null);
        invalidate();
    }

    /**
     * Sets the acegi authentication.
     * @param authentication the authentication or null to clear
     */
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * @return the current YourApp session
     */
    public static AmpAuthWebSession getYourAppSession() {
        return (AmpAuthWebSession) Session.get();
    }
    
    public boolean isSignedIn2(){
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth == null)
    		return false;
    	else 
    		return auth.isAuthenticated();
    }

}