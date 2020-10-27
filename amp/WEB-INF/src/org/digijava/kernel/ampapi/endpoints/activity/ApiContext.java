package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Date;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;

public class ApiContext {
    
    private ServletContext sessionServletContext;
    
    private TeamMember teamMember;
    
    private User user;
    
    private Site site;
    
    private AmpTreeVisibility ampTreeVisibility;
    
    private Date ampTreeVisibilityModificationDate;
    
    private StringBuffer requestURL;
    
    private String rootPath;
    
    private ClientMode clientMode;
    
    public ServletContext getSessionServletContext() {
        return sessionServletContext;
    }
    
    public void setSessionServletContext(ServletContext sessionServletContext) {
        this.sessionServletContext = sessionServletContext;
    }
    
    public TeamMember getTeamMember() {
        return teamMember;
    }
    
    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Site getSite() {
        return site;
    }
    
    public void setSite(Site site) {
        this.site = site;
    }
    
    public AmpTreeVisibility getAmpTreeVisibility() {
        return ampTreeVisibility;
    }
    
    public void setAmpTreeVisibility(AmpTreeVisibility ampTreeVisibility) {
        this.ampTreeVisibility = ampTreeVisibility;
    }
    
    public Date getAmpTreeVisibilityModificationDate() {
        return ampTreeVisibilityModificationDate;
    }
    
    public void setAmpTreeVisibilityModificationDate(Date ampTreeVisibilityModificationDate) {
        this.ampTreeVisibilityModificationDate = ampTreeVisibilityModificationDate;
    }
    
    public StringBuffer getRequestURL() {
        return requestURL;
    }
    
    public void setRequestURL(StringBuffer requestURL) {
        this.requestURL = requestURL;
    }
    
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
    
    public String getRootPath() {
        return rootPath;
    }
    
    public ClientMode getClientMode() {
        return clientMode;
    }
    
    public void setClientMode(ClientMode clientMode) {
        this.clientMode = clientMode;
    }
}
