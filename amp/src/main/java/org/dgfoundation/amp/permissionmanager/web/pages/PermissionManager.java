/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authentication.pages.SignInPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.util.tester.DummyHomePage;
import org.dgfoundation.amp.permissionmanager.components.features.PermissionManagerFormFeature;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMFieldPermissionViewer;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.digijava.module.um.exception.UMException;

/**
 * @author dan
 *
 */
public class PermissionManager extends AmpPMHeaderFooter {

    /**
     * 
     */
    public PermissionManager() throws Exception{
        // TODO Auto-generated constructor stub
        super();
        
        
        
        HttpSession session = this.getHttpSession();
        if (session.getAttribute("ampAdmin") == null) {
            throw new RedirectToUrlException("/");

        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                throw new RedirectToUrlException("/");
            }
        }
        
        
        //managing workspaces
        Set<AmpTeam> w = new TreeSet<AmpTeam>();
        List<AmpTeam> teams = new ArrayList<AmpTeam>();
        try {
            teams = org.digijava.module.um.util.DbUtil.getList(AmpTeam.class.getName(),"name");
        } catch (UMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        w.addAll(teams);
        final IModel<Set<AmpTeam>> teamsModel = new Model((Serializable)w);
        
        
        Set<AmpPMFieldPermissionViewer> permMaps = PermissionUtil.getAllAmpPMFieldPermissionViewers(AmpModulesVisibility.class);
        final IModel<Set<AmpPMFieldPermissionViewer>> permsModel = new Model((Serializable)permMaps);

        add(new PermissionManagerFormFeature("permission", teamsModel, permsModel, "Permission Manager"));
    }

}
