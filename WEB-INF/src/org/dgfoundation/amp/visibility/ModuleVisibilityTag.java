/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;


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
	private String type;
	private String parentModule;
	
	public String getParentModule() {
		return parentModule;
	}
	public void setParentModule(String parentModule) {
		this.parentModule = parentModule;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
		AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		
		if(ampTreeVisibility!=null)
		{
			if(!existModuleinDB(ampTreeVisibility) && FeaturesUtil.getModuleVisibility(name)==null)
			{
					FeaturesUtil.insertModuleVisibility(ampTreeVisibility.getRoot().getId(),this.getName());
					
					AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
					ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
					ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
   		   	}
			else 
					if(!checkTypeAndParentOfModule(ampTreeVisibility) || !checkTypeAndParentOfModule2(FeaturesUtil.getModuleVisibility(name))) //parent or type is not ok
						{
							try{
								FeaturesUtil.updateModuleVisibility(ampTreeVisibility.getModuleByNameFromRoot(this.getName()).getId(), parentModule, type);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
							ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
							ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
						}
				
		}
		else return SKIP_BODY;
		
		return EVAL_BODY_BUFFERED;
	}
	public int doEndTag() throws JspException 
    {
		if (bodyContent==null) return  SKIP_BODY;
		if(bodyContent.getString()==null) return SKIP_BODY;
       String bodyText = bodyContent.getString();
       try {
    	   
    	   ServletContext ampContext=pageContext.getServletContext();
    	   HttpSession session=pageContext.getSession();
    	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
    	   
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
   			   
   			pageContext.getOut().print(bodyText);
   		   }
   		   else{
   			//System.out.println("Field MANAGER!!!! module "+this.getName()+" is not ACTIVE");
   			   //the field is not active!!!
   		   }
   		   session.setAttribute("currentModuleTag",null);
       }
       catch (Exception e) {
    	   e.printStackTrace();
       	throw new JspTagException(e.getMessage());
       }
       
       return EVAL_PAGE;//SKIP_BODY 
    }
	
	public boolean isModuleActive(AmpTreeVisibility atv)
	{
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
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
		boolean typeOK=false;
		boolean parentOK=false;
		if(atv!=null)
			moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getName());
		else return typeOK && parentOK;
		if(this.getType()!=null && moduleByNameFromRoot.getType()!=null)
			if(moduleByNameFromRoot.getType().getName().compareTo(this.getType())==0)
				 typeOK=true;//return true; //they are identical
		if(this.getParentModule()!=null && moduleByNameFromRoot.getParent()!=null)
			if(moduleByNameFromRoot.getParent().getName().compareTo(this.getParentModule())==0)
				parentOK=true;
		//if(moduleByNameFromRoot==null) return false;
		return typeOK && parentOK;
	}

	public boolean checkTypeAndParentOfModule2(AmpModulesVisibility atv)
	{
		AmpModulesVisibility moduleByNameFromRoot=null;
		boolean typeOK=false;
		boolean parentOK=false;
		if(atv!=null)
			moduleByNameFromRoot = atv;
		else return typeOK && parentOK;
		if(this.getType()!=null && moduleByNameFromRoot.getType()!=null)
			if(moduleByNameFromRoot.getType().getName().compareTo(this.getType())==0)
				 typeOK=true;//return true; //they are identical
		if(this.getParentModule()!=null && moduleByNameFromRoot.getParent()!=null)
			if(moduleByNameFromRoot.getParent().getName().compareTo(this.getParentModule())==0)
				parentOK=true;
		//if(moduleByNameFromRoot==null) return false;
		return typeOK && parentOK;
	}
	
}
