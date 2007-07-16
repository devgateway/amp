/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;


/**
 * @author dan
 *
 */
public class FieldVisibilityTag extends BodyTagSupport {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7009665621191882475L;
	private String name;
	private String feature;
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
	public FieldVisibilityTag() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int doEndTag() throws JspException 
    {
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
    	   if(! existFeature(ampTreeVisibility)) {
    		   //error
    		  System.out.println("error!!!! feature "+this.getFeature()+" doesn't exist");
    		  return SKIP_BODY;
    	   }
   		   if(!existFieldinDB(ampTreeVisibility)){
    		//insert in db;	   
   			   //insert(featureId, fieldname);
   			   //TODO default has to be visibile!!!!
   			   FeaturesUtil.insertFieldWithFeatureVisibility(ampTreeVisibility.getRoot().getId(),ampTreeVisibility.getFeatureByNameFromRoot(this.getFeature()).getId(),this.getName());;
   			   
   			   AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
   			   System.out.println("-------------------------------"+currentTemplate.getId());
   			   ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
   			   ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
   		   }
   		   ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
   		   if(!isFeatureTheParent(ampTreeVisibility)){
   			   //update(featureId, fieldname);
			   System.out.println("error!!!! feature "+this.getFeature()+" is not the parent");
			   FeaturesUtil.updateFieldWithFeatureVisibility(ampTreeVisibility.getFeatureByNameFromRoot(this.getFeature()).getId(),this.getName());
   			   AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
   			   System.out.println("-------------------------------"+currentTemplate.getId());
   			   ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
   			   ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);

		   }
   		   ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
   		   if(isFieldActive (ampTreeVisibility)){
   			pageContext.getOut().print(bodyText);
   		   }
   		   else{
   			System.out.println("Field MANAGER!!!! field "+this.getName()+" is not ACTIVE");
   			   ;//the field is not active!!!
   		   }
    	   
       }
       catch (Exception e) {
    	   e.printStackTrace();
       	throw new JspTagException(e.getMessage());
       }
       return EVAL_PAGE;//SKIP_BODY 
    }
	
	public boolean isFieldActive(AmpTreeVisibility atv)
	{
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
		for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
		{
			AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
			if(field.getName().compareTo(this.getName())==0) 
			{
				return true;
			}
			
		}
		return false;
	}
	
	public boolean existFeature(AmpTreeVisibility atv)
	{

		AmpFeaturesVisibility featureByNameFromRoot = atv.getFeatureByNameFromRoot(this.getFeature());
		if(featureByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean existFieldinDB(AmpTreeVisibility atv)
	{

		AmpFieldsVisibility fieldByNameFromRoot = atv.getFieldByNameFromRoot(this.getName());
		if(fieldByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean isFeatureTheParent(AmpTreeVisibility atv)
	{
		AmpTreeVisibility featureByNameFromRoot = atv.getFeatureTreeByNameFromRoot(this.getFeature());
		//AmpFeaturesVisibility f=(AmpFeaturesVisibility) featureByNameFromRoot.getRoot();
		if(featureByNameFromRoot.getItems().containsKey(this.getName())) return true;
		return false;
	}
	
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
}
