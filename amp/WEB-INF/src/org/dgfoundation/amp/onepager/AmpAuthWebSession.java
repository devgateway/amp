/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.FMFormCache;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author aartimon@dginternational.org 
 * @since Jan 3, 2011
 */
public class AmpAuthWebSession extends AuthenticatedWebSession {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(AmpAuthWebSession.class);

    private boolean translatorMode;
    private boolean fmMode;
    private TeamMember currentMember;
    private AmpTeamMember ampCurrentMember;
    private Site site;
    private String isAdmin;
    private Long formType;
    
    FMFormCache formCache   = null;
    
    public String getIsAdmin() {
        if (isAdmin == null){
            isAdmin = (String) getHttpSession().getAttribute("ampAdmin");
        }
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public AmpAuthWebSession(final Request request) {
        super(request);
        fmMode = false;
        translatorMode = false;
        HttpServletRequest wRequest = (HttpServletRequest) request.getContainerRequest();
        HttpSession httpSession = wRequest.getSession();
        currentMember = (TeamMember)httpSession.getAttribute("currentMember");
        isAdmin = (String)httpSession.getAttribute("ampAdmin");
        if (currentMember != null)
            ampCurrentMember = TeamMemberUtil.getAmpTeamMemberCached(currentMember.getMemberId());
        else
            ampCurrentMember = null;
        
        SiteDomain siteDomain = SiteCache.getInstance().getSiteDomain(wRequest.getServerName(), null);
        site = siteDomain.getSite();
    }

    public void reset(){
        formType = ActivityUtil.ACTIVITY_TYPE_PROJECT;
        currentMember = (TeamMember) getHttpSession().getAttribute("currentMember");
        isAdmin = (String) getHttpSession().getAttribute("ampAdmin");
        if (currentMember != null)
            ampCurrentMember = TeamMemberUtil.getAmpTeamMemberCached(currentMember.getMemberId());
        else
            ampCurrentMember = null;
        dirty();
    }

    public HttpSession getHttpSession() {
        return ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
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
                ampCurrentMember = TeamMemberUtil.getAmpTeamMemberCached(currentMember.getMemberId());
        }
        return ampCurrentMember;
    }
    public void setAmpCurrentMember(AmpTeamMember ampCurrentMember) {
        this.ampCurrentMember = ampCurrentMember;
    }

    public TeamMember getCurrentMember() {
        if (currentMember == null){
            currentMember = (TeamMember) getHttpSession().getAttribute("currentMember");
        }
        return currentMember;
    }
    public void setCurrentMember(TeamMember currentMember) {
        this.currentMember = currentMember;
    }

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
    

    public FMFormCache getFormCache() {
        return formCache;
    }

    public void setFormCache(FMFormCache formCache) {
        this.formCache = formCache;
    }

    public Long getFormType() {
        return formType;
    }

    public void setFormType(Long formType) {
        this.formType = formType;
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
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                 roles.add(grantedAuthority.getAuthority());
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
