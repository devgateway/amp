/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;


/**
 * @author dan
 *
 */
public class FeatureVisibilityTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1296936554150626082L;
	private String name;
	private String module;
	private String enabled;
	
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
		// TODO Auto-generated constructor stub
	}
	
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		ServletContext ampContext=pageContext.getServletContext();
 	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
 	   if(ampTreeVisibility!=null)
 	   if(!existFeatureinDB(ampTreeVisibility)){
    		//insert in db;	   
   			   //insert(templateid,moduleId, featurename);
   			   FeaturesUtil.insertFeatureWithModuleVisibility(ampTreeVisibility.getRoot().getId(),ampTreeVisibility.getModuleByNameFromRoot(this.getModule()).getId(),this.getName());
   			   AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
   			   //System.out.println("-------------------------------inserting new feature in database");
   			   ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
   			   ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
   		   }
	   ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
	   if(ampTreeVisibility!=null)
   		   if(!isModuleTheParent(ampTreeVisibility)){
   			   //update(featureId, fieldname);
			   //System.out.println("error!!!! module "+this.getModule()+" is not the parent");
			   
			   FeaturesUtil.updateFeatureWithModuleVisibility(ampTreeVisibility.getModuleByNameFromRoot(this.getModule()).getId(),this.getName());
			   AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
   			   //System.out.println("-------------------------------update the parent of the feature");
   			   ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
   			   ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
		   }
	   ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
	   if(!existModule(ampTreeVisibility)) {
		   //error
		  //System.out.println("error!!!! module "+this.getModule()+" doesn't exist");
		  return SKIP_BODY;
	   }

	   
		return EVAL_BODY_BUFFERED;//super.doStartTag();
	}
	public int doEndTag() throws JspException 
    {
	   if (bodyContent==null) return  SKIP_BODY;
	   if(bodyContent.getString()==null) return SKIP_BODY;
       String bodyText = bodyContent.getString();
       try {
    	   ServletContext ampContext=pageContext.getServletContext();
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
   		   
   		   ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
   		   if(ampTreeVisibility!=null)
   		   if(isFeatureActive(ampTreeVisibility)){
   			pageContext.getOut().print(bodyText);
   		   }
   		   else{
   			//System.out.println("Field MANAGER!!!! ffeature "+this.getName()+" is not ACTIVE");
   			   //the field is not active!!!
   		   }
    	   
       }
       catch (Exception e) {
    	   e.printStackTrace();
       	throw new JspTagException(e.getMessage());
       }
       return EVAL_PAGE;//SKIP_BODY 
    }
	
	public boolean isFeatureActive(AmpTreeVisibility atv)
	{
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
		for(Iterator it=currentTemplate.getFeatures().iterator();it.hasNext();)
		{
			AmpFeaturesVisibility feature=(AmpFeaturesVisibility) it.next();
			if(feature.getName().compareTo(this.getName())==0) 
			{
				return true;
			}
			
		}
		return false;
	}
	
	public boolean existModule(AmpTreeVisibility atv)
	{

		AmpModulesVisibility moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getModule());
		if(moduleByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean existFeatureinDB(AmpTreeVisibility atv)
	{
		AmpFeaturesVisibility featureByNameFromRoot = atv.getFeatureByNameFromRoot(this.getName());
		if(featureByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean isModuleTheParent(AmpTreeVisibility atv)
	{
		AmpTreeVisibility moduleByNameFromRoot = atv.getModuleTreeByNameFromRoot(this.getModule());
		//AmpFeaturesVisibility f=(AmpFeaturesVisibility) featureByNameFromRoot.getRoot();
		if(moduleByNameFromRoot.getItems().containsKey(this.getName())) return true;
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
