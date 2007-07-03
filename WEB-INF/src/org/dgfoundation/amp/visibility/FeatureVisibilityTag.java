/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
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
	
	public int doEndTag() throws JspException 
    {
       String bodyText = bodyContent.getString();
       boolean moduleActive=false;
       boolean featureActive=false;
       try {
       	
       	if(this.getModule()==null) ;//error!!!
       	else
       	{
       		moduleActive=isModuleActive();
       		featureActive=isFeatureActive();
       		if(moduleActive && featureActive) pageContext.getOut().print(bodyText);
       			
       	}
       }
       catch (Exception e) {
       	throw new JspTagException(e.getMessage());
       }
       return EVAL_PAGE; 
    }
	
	public boolean isFeatureActive()
	{
		AmpModulesVisibility aModule=this.getModuleObject();
		Collection features=FeaturesUtil.getModuleFeatures(aModule.getId());
		boolean found=false;
		for(Iterator it=features.iterator();it.hasNext();)
			{
				AmpFeature afeature=(AmpFeature)it.next();
				if(afeature.getName().compareTo(this.getName())==0) {found=true;break;}
			}
		return found;
	}
	
	public boolean isModuleActive()
	{
		//to do: getDefaultTemplate -> templateId
		//to be commented:
		Long templateId=new Long(1);
		Collection modules=FeaturesUtil.getTemplateModules(templateId);
		boolean found=false;
		AmpModulesVisibility aModule;
		for(Iterator it=modules.iterator();it.hasNext();)
			{
				aModule=(AmpModulesVisibility)it.next();
				if(aModule.getName().compareTo(this.getModule())==0) {found=true;break;}
			}
		return found;
	}
	
	public AmpModulesVisibility getModuleObject()
	{
		AmpModulesVisibility aModule=null;
		if(this.getModule()==null) ;//error!!!
		else
		{
//			to do: getDefaultTemplate -> templateId
			//to be commented:
			Long templateId=new Long(1);
			Collection modules=FeaturesUtil.getTemplateModules(templateId);
			boolean found=false;
			for(Iterator it=modules.iterator();it.hasNext();)
				{
					aModule=(AmpModulesVisibility)it.next();
					if(aModule.getName().compareTo(this.getModule())==0) {found=true;break;}
				}
		}
		return aModule;
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
