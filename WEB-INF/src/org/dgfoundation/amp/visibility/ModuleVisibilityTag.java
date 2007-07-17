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

	private String name;
	private String enabled;
	
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
		//String bodyText = bodyContent.getString();
		if(!existModuleinDB(ampTreeVisibility)){
    		//insert in db;	   
   			   //insert(templateid, modulename);
   			   
   			   FeaturesUtil.insertModuleVisibility(ampTreeVisibility.getRoot().getId(),this.getName());
   			   
   			   AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
   			   ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
   			   ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
   			   
   		   }
		
		return EVAL_BODY_BUFFERED;//super.doStartTag();
	}
	public int doEndTag() throws JspException 
    {
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
   		   

   		   if(isModuleActive(ampTreeVisibility)){
   			   
   			pageContext.getOut().print(bodyText);
   		   }
   		   else{
   			System.out.println("Field MANAGER!!!! module "+this.getName()+" is not ACTIVE");
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
		AmpModulesVisibility moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getName());
		if(moduleByNameFromRoot==null) return false;
		return true;
	}
	
	
}
