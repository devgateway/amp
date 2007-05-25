/**
 * 
 */
package org.dgfoundation.amp.category;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.helper.CategoryManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class CategoryTagClass extends TagSupport {
	private Logger logger	= Logger.getLogger(CategoryTagClass.class);
	Boolean multiselect	= null;
	Boolean ordered		= null;
	
	String name;
	String keyName;
	String property;
	String styleClass	= null;
	
	Long categoryId		= null;
	String categoryName	= null;
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Boolean getMultiselect() {
		return multiselect;
	}
	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
	}
	public Boolean getOrdered() {
		return ordered;
	}
	public void setOrdered(Boolean ordered) {
		this.ordered = ordered;
	}
	public int doStartTag() {
		return SKIP_BODY;
	}
	public int doEndTag() throws JspException {
		Object bean						= pageContext.findAttribute(name);
		HttpServletRequest request		= (HttpServletRequest)pageContext.getRequest();
		Long valueId					= null;
		String classProperty			= "";
		if (styleClass != null)
			classProperty	= " class='"+ styleClass + "'";
		
		try{
			PropertyDescriptor beanProperty	= new PropertyDescriptor(property, bean.getClass());
			valueId							= (Long)(beanProperty.getReadMethod().invoke(bean,new Object[0]));
			
			
		}
		catch(Exception E){
			logger.info(E);
		}
		
		try{
			JspWriter out					= pageContext.getOut();
			Collection ampCategoryValues;
			if ( categoryId != null )
				ampCategoryValues	= CategoryManagerUtil.getAmpCategoryValueCollection(categoryId, ordered);
			else {
				if (keyName!=null) 
					ampCategoryValues	= CategoryManagerUtil.getAmpCategoryByKey(keyName, ordered);
				else
					ampCategoryValues	= CategoryManagerUtil.getAmpCategoryValueCollection(categoryName, ordered);
			}
			
			if (ampCategoryValues != null && ampCategoryValues.size() > 0) {
				Iterator iterator			= ampCategoryValues.iterator();			
				out.println("<select name='"+property+"' "+classProperty+">");
				out.println("<option value='0' >Please select from below</option>");
				while (iterator.hasNext()) {
					AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
					String outputValue					= CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request, null);
					if ( valueId != null && valueId.longValue()	== ampCategoryValue.getId().longValue() ) {
						out.println("<option value='"+ampCategoryValue.getId()+"' selected='selected' >"+outputValue+"</option>");
					}
					else{
						out.println("<option value='"+ampCategoryValue.getId()+"' >"+outputValue+"</option>");
					}
				}
				out.println("</select>");
			}
		}
		catch(Exception E){
			logger.info(E);
			E.printStackTrace();
		}
		
		return EVAL_PAGE;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String key) {
		this.keyName = key;
	}
	
}
