/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.features;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.util.FeaturesUtil;


/**
 * @author dan
 *
 */
public class FeatureTrn extends BodyTagSupport {

	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 */
	public FeatureTrn() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int doEndTag() throws JspException 
    {
       String bodyText = bodyContent.getString();
       
       try {
       	
    	   //if(isFeatureActive(name)) pageContext.getOut().print(bodyText);
       	;
       }
       catch (Exception e) {
       	throw new JspTagException(e.getMessage());
       }
       return EVAL_PAGE; 
    }
	
	public boolean isFeatureActive(String featureName)
	{
		Long templateId;
		String templateIdString=FeaturesUtil.getGlobalSettingValue("Feature Template");
		templateId=new Long(Long.parseLong(templateIdString));
		Collection features=FeaturesUtil.getTemplateFeatures(templateId);
		boolean found=false;
		for(Iterator it=features.iterator();it.hasNext();)
			{
				AmpFeature afeature=(AmpFeature)it.next();
				if(afeature.getName().compareTo(featureName)==0) {found=true;break;}
			}
		return found;
	}
}
