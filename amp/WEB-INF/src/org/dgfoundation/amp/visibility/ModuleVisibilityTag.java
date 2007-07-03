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
	
	public int doEndTag() throws JspException 
    {
       String bodyText = bodyContent.getString();


       try {
       	
    	   if(isModuleActive()) System.out.println("ACTIVE MODULE!!! name:::"+this.getName()+":::enable:::"+this.getEnabled());
    	   else System.out.println("name:::"+this.getName()+":::enable:::"+this.getEnabled());
           pageContext.getOut().print(bodyText);
       }
       catch (Exception e) {
       	throw new JspTagException(e.getMessage());
       }
       return EVAL_PAGE; 
    }
	
	public boolean isModuleActive()
	{
		//to do: getDefaultTemplate -> templateId
		//to be commented:
		Long templateId=new Long(1);
		Collection modules=FeaturesUtil.getTemplateModules(templateId);
		boolean found=false;
		for(Iterator it=modules.iterator();it.hasNext();)
			{
				AmpModulesVisibility aModule=(AmpModulesVisibility)it.next();
				if(aModule.getName().compareTo(this.getName())==0) {found=true;break;}
			}
		return found;
	}
}
