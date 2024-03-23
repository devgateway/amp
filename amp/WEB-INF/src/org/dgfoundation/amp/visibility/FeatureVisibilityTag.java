/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Map;

/**
 * @author dan
 *
 */
public class FeatureVisibilityTag extends BodyTagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1296936554150626082L;
    private static Logger logger = Logger.getLogger(FeatureVisibilityTag.class);
    private static Object syncObj = new Object();
    
    private String name;
    private String module;
    private String enabled;
    private String hasLevel;
    
    public String getHasLevel() {
        return hasLevel;
    }
    public void setHasLevel(String hasLevel) {
        this.hasLevel = hasLevel;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 
     */
    public FeatureVisibilityTag() {
        super();
    }
    
    @Override
    public int doStartTag() throws JspException {
        return doTag(pageContext.getServletContext(), pageContext.getSession(), FeaturesUtil.getAmpTreeVisibility(pageContext.getServletContext(), pageContext.getSession()));
    }
    
    public int doTag(ServletContext ampContext, HttpSession session, AmpTreeVisibility ampTreeVisibility) {
        if (ampTreeVisibility == null)
            return EVAL_BODY_BUFFERED; // does this make sense? reproducing old behaviour

        if (!existModule(ampTreeVisibility)) return SKIP_BODY;

        if (!existFeatureinDB(ampTreeVisibility)) {
            boolean addResult = addFeatureToDb(ampTreeVisibility, ampContext, session);
            if (!addResult)
                return SKIP_BODY;
        }

       if (!isModuleTheParent(ampTreeVisibility)) {
           FeaturesUtil.updateFeatureWithModuleVisibility(ampTreeVisibility.getModuleByNameFromRoot(this.getModule()).getId(),this.getName());
           rebuildTreeVisibility(ampContext, session, ampTreeVisibility);
       }
       return EVAL_BODY_BUFFERED;       
    }

    private void rebuildTreeVisibility(ServletContext ampContext, HttpSession session,
                                       AmpTreeVisibility ampTreeVisibility) {
        AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility) FeaturesUtil.
                getTemplateById(ampTreeVisibility.getRoot().getId());
        ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
        FeaturesUtil.setAmpTreeVisibility(ampContext, session, ampTreeVisibility);
    }

    /**
     * returns false if asked to add a feature under a non-existing module
     * @param ampTreeVisibility
     * @return
     */
    protected boolean addFeatureToDb(AmpTreeVisibility ampTreeVisibility, ServletContext ampContext, HttpSession session) {
        synchronized (syncObj) {
            if (FeaturesUtil.getFeatureVisibility(name) == null) {
                AmpModulesVisibility moduleByNameFromRoot = ampTreeVisibility.getModuleByNameFromRoot(this.getModule());
                Long id = null;
                if (moduleByNameFromRoot != null) {
                   id = moduleByNameFromRoot.getId();
                   if (FeaturesUtil.getFeatureVisibility(this.getName()) != null) {
                       FeaturesUtil.updateFeatureWithModuleVisibility(ampTreeVisibility.
                               getModuleByNameFromRoot(this.getModule()).getId(), this.getName());
                   }
                   else {    
                       FeaturesUtil.insertFeatureWithModuleVisibility(ampTreeVisibility.getRoot().getId(), id, this
                               .getName(), this.getHasLevel());
                   }
                    rebuildTreeVisibility(ampContext, session, ampTreeVisibility);
                }
                else {
                    logger.debug("Feature: "+this.getName() + " has the parent: "+this.getModule()+ " which doesn't exist in DB");
                    return false;
                }
            }
            return true;
        }
    }
    
    public boolean shouldDisplayFeature() throws JspTagException
    {
        try 
        {
            ServletContext ampContext=pageContext.getServletContext();
            AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(ampContext, pageContext.getSession());
               /* name, feature, enable
                * 
                * if feature is not in the db, error! it has to be already added this feature
                * 
                *if field is not in db insert it with feature as parent
                *
                * is this feature the correct parent? if not -> error!
                * 
                * if field is active then display the content
                */
               
            ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(ampContext, pageContext.getSession());
            if (ampTreeVisibility!=null)
                if (isFeatureActive(ampTreeVisibility))
                {
                    HttpSession session = pageContext.getSession();
                    Map scope=PermissionUtil.getScope(pageContext.getSession());
                    AmpFeaturesVisibility ampFeatureFromTree=ampTreeVisibility.getFeatureByNameFromRoot(getName());
                    TeamMember teamMember   = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
                    if (teamMember!=null )//&& !teamMember.getTeamHead())
                    {
                        PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
                        ServletRequest request = pageContext.getRequest();
                        String actionMode = (String) request.getAttribute(GatePermConst.ACTION_MODE);
                        if (ampFeatureFromTree != null && ampFeatureFromTree.getPermission(false) != null &&
                            PermissionUtil.getFromScope(session, GatePermConst.ScopeKeys.ACTIVITY)!=null &&
                            !ampFeatureFromTree.canDo(GatePermConst.Actions.EDIT.equals(actionMode) ? 
                                    actionMode:GatePermConst.Actions.VIEW,scope))
                        {
                            return false;
                        }
                    }
                    return true;
                }
           }
           catch (Exception e) {               
               e.printStackTrace();
               throw new JspTagException(e.getMessage());
           }
        return false;
    }
    
    public int doEndTag() throws JspException 
    {
       if (bodyContent==null) return  SKIP_BODY;
       if (bodyContent.getString()==null) return SKIP_BODY;
       String featureString = "feature_visibility_" + this.getModule() + "###" + this.getName();
       Boolean shouldDisplay = (Boolean) pageContext.getRequest().getAttribute(featureString); 
       String bodyText = bodyContent.getString();
       try 
       {
           if (shouldDisplay == null)
           {
               shouldDisplay = shouldDisplayFeature();
               pageContext.getRequest().setAttribute(featureString, shouldDisplay);
           }
           if (!shouldDisplay)
               return SKIP_BODY;
           pageContext.getOut().print(bodyText);
           return EVAL_PAGE; //SKIP_BODY
       }
       catch (Exception e) {
           e.printStackTrace();
           throw new JspTagException(e.getMessage());
       }       
    }
    
    public boolean isFeatureActive(AmpTreeVisibility atv)
    {
        AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
        if(currentTemplate!=null)
            if(currentTemplate.getFeatures()!=null)
                for(AmpFeaturesVisibility feature:currentTemplate.getFeatures())
                {
                    if(feature.getName().compareTo(this.getName())==0) 
                    {
                        return true;
                    }
                }
        return false;
    }
    
    public boolean existModule(AmpTreeVisibility atv) {
        
        AmpModulesVisibility moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getModule());
        
        if (moduleByNameFromRoot == null) {
            return false;
        }
        
        return FeaturesUtil.isVisibleModule(this.getModule());
    }
    
    public boolean existFeatureinDB(AmpTreeVisibility atv) {
        AmpFeaturesVisibility featureByNameFromRoot = atv.getFeatureByNameFromRoot(this.getName());
        if(featureByNameFromRoot==null) return false;
        return true;
    }
    
    public boolean isModuleTheParent(AmpTreeVisibility atv)
    {
        AmpTreeVisibility moduleByNameFromRoot = atv.getModuleTreeByNameFromRoot(this.getModule());
        //AmpFeaturesVisibility f=(AmpFeaturesVisibility) featureByNameFromRoot.getRoot();
        if(moduleByNameFromRoot!=null)
            if(moduleByNameFromRoot.getItems()!=null)
                {
                if(moduleByNameFromRoot.getItems().containsKey(this.getName())) return true;
                }
            //else ////System.out.println("errror in FM - feature: "+this.getModule());
        //else ////System.out.println("errror in FM - feature: "+this.getModule());     
        return false;
    }
    
    
    public String getEnabled() {
        return enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }
}
