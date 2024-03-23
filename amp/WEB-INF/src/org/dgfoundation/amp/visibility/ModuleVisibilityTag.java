/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import org.apache.log4j.Logger;
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
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


/**
 * @author dan
 *
 */
public class ModuleVisibilityTag extends BodyTagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 3079619271981032373L;
    private String name;
    private String enabled;
    private String parentModule;
    private String hasLevel;
    private static final Logger logger = Logger.getLogger(ModuleVisibilityTag.class);
    
    public String getHasLevel() {
        return hasLevel;
    }
    public void setHasLevel(String hasLevel) {
        this.hasLevel = hasLevel;
    }
    public String getParentModule() {
        return parentModule;
    }
    public void setParentModule(String parentModule) {
        this.parentModule = parentModule;
    }
    public String getEnabled() {
        return enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
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
    public ModuleVisibilityTag() {
        super();
        // TODO Auto-generated constructor stub
    }
    public int doStartTag() throws JspException {
        // TODO Auto-generated method stub
        
        ServletContext ampContext=pageContext.getServletContext();
        HttpSession session=pageContext.getSession();
        AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(ampContext, session);
        try
        {
            String cache=(String) ampContext.getAttribute("FMcache");
                   if(ampTreeVisibility!=null)
                    {
                        if(!existModuleinDB(ampTreeVisibility))
                        {//insert without parent??
                            if(parentModule!=null &&  FeaturesUtil.getModuleVisibility(parentModule)==null){
                                logger.error("FM tag ERROR: parentModule:"+ parentModule +" was not found! Please update the tag accordingly!");
                            }else{
                                synchronized (this) {
                                    if(FeaturesUtil.getModuleVisibility(name)==null){
                                        FeaturesUtil.insertModuleVisibility(ampTreeVisibility.getRoot().getId(),this.getName(),this.getHasLevel());
                                        logger.debug("Inserting module: " + this.getName());
                                        AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                                        ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
                                        FeaturesUtil.setAmpTreeVisibility(ampContext, session,ampTreeVisibility);
                                        
                                    }
                                }
                            }
                        }
                        if (!checkTypeAndParentOfModule(ampTreeVisibility))
                            {
                            if(parentModule!=null &&  FeaturesUtil.getModuleVisibility(parentModule)==null){
                                logger.error("FM tag ERROR: parentModule:"+ parentModule +" was not found! Please update the tag accordingly!");
                            }else{
                                    try{
                                        //logger.info("Updating module: "+this.getName() +" with  id:"+ ampTreeVisibility.getModuleByNameFromRoot(this.getName()).getId() +"and his parent "+parentModule);
                                        synchronized (this) {
                                            if(!checkTypeAndParentOfModule2(FeaturesUtil.getModuleVisibility(name))){
                                                logger.debug("Trying to update module: "+this.getName() +" with  id:" +"and his parent "+parentModule);
                                                AmpModulesVisibility moduleAux= ampTreeVisibility.getModuleByNameFromRoot(this.getName());
                                                if(moduleAux!=null)
                                                    if(moduleAux.getId()!=null)
                                                    {
                                                        FeaturesUtil.updateModuleVisibility(moduleAux.getId(), parentModule);
                                                        logger.debug(".........updating module: "+this.getName() +" with  id:" +"and his parent "+parentModule);
                                                    }
                                            }
                                        }
                                    }
                                    catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                                        ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
                                        FeaturesUtil.setAmpTreeVisibility(ampContext, session,ampTreeVisibility);
                                }
                            }
                    }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return EVAL_BODY_BUFFERED;
    }
    public int doEndTag() throws JspException
    {
        if (bodyContent==null) return  SKIP_BODY;
        if(bodyContent.getString()==null) return SKIP_BODY;
       String bodyText = bodyContent.getString();

           
           ServletContext ampContext=pageContext.getServletContext();
           HttpSession session=pageContext.getSession();
           AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(ampContext, session);
           
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
            if(ampTreeVisibility!=null)
           if(isModuleActive(ampTreeVisibility)){
                               Map scope=PermissionUtil.getScope(pageContext.getSession());
                               AmpModulesVisibility ampModulesFromTree=ampTreeVisibility.getModuleByNameFromRoot(getName());
                               TeamMember teamMember    = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
                               String isAdmin = (String) session.getAttribute("ampAdmin");

                if (isAdmin==null && teamMember!=null && !teamMember.getTeamHead()){
                    PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
                    ServletRequest request = pageContext.getRequest();
                    String actionMode = (String) request.getAttribute(GatePermConst.ACTION_MODE);
                    if(ampModulesFromTree!=null && ampModulesFromTree.getPermission(false)!=null &&
                        PermissionUtil.getFromScope(session, GatePermConst.ScopeKeys.ACTIVITY)!=null &&
                        !ampModulesFromTree.canDo(GatePermConst.Actions.EDIT.equals(actionMode)?
                                actionMode:GatePermConst.Actions.VIEW,scope)){
                        return SKIP_BODY;
                    }
                }

            try {
                pageContext.getOut().print(bodyText);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }
           }
       return EVAL_PAGE;
    }
    
    public boolean isModuleActive(AmpTreeVisibility atv)
    {
        AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
        if(currentTemplate!=null)
            if(currentTemplate.getAllItems()!=null)
                for(Iterator it=currentTemplate.getItems().iterator();it.hasNext();)
                {
                    AmpModulesVisibility module=(AmpModulesVisibility) it.next();
                    if(module.getName().compareTo(this.getName())==0) 
                    {
                        return true;
                    }
            
                }
        return false;
    }
    
    
    public boolean existModuleinDB(AmpTreeVisibility atv)
    {
        AmpModulesVisibility moduleByNameFromRoot=null;
        if(atv!=null)
            moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getName());
        if(moduleByNameFromRoot==null) return false;
        return true;
    }
    
    public boolean checkTypeAndParentOfModule(AmpTreeVisibility atv)
    {
        AmpModulesVisibility moduleByNameFromRoot=null;
        //boolean typeOK=false;
        boolean typeOK=true;
        boolean parentOK=false;
        if(atv!=null)
            moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getName());
        else {
            return typeOK && parentOK;
        }

        if(moduleByNameFromRoot==null) {
            return false;
        }
        if(this.getParentModule()!=null && moduleByNameFromRoot.getParent()!=null)
            if(moduleByNameFromRoot.getParent().getName().compareTo(this.getParentModule())==0)
                parentOK=true;
        if(this.getParentModule()==null && moduleByNameFromRoot.getParent()==null)
            parentOK=true;
        return typeOK && parentOK;
    }

    public boolean checkTypeAndParentOfModule2(AmpModulesVisibility atv)
    {
        AmpModulesVisibility moduleByNameFromRoot=null;
        //boolean typeOK=false;
        boolean typeOK=true;
        boolean parentOK=false;
        if(atv!=null)
            moduleByNameFromRoot = atv;
        else return typeOK && parentOK;
        if(moduleByNameFromRoot==null) return false;
        
        if(this.getParentModule()!=null && moduleByNameFromRoot.getParent()!=null)
            if(moduleByNameFromRoot.getParent().getName().compareTo(this.getParentModule())==0)
                parentOK=true;
        
        //if(moduleByNameFromRoot.getParent()==null) return false;
        if(this.getParentModule()==null && moduleByNameFromRoot.getParent()==null)
            parentOK=true;
        return typeOK && parentOK;
    }
    
}
