/**
 * 
 */
package org.dgfoundation.amp.category;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCategoryClass;
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
	
	boolean listView	= true;
	
	String name;
	String keyName;
	String property;
	String styleClass	= null;
	
	Long categoryId		= null;
	String categoryName	= null;
	
	int size			= 3;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
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
		Long valueId					= null; // for single select
		Long [] valueIds				= null; // for multiselect			
		Collection valueIdsColl			= null;
		
		try{
			Collection ampCategoryValues;
			AmpCategoryClass ampCategoryClass	= null;
			
			if ( categoryId != null )
				ampCategoryValues	= CategoryManagerUtil.getAmpCategoryValueCollection(categoryId, ordered);
			else {
				if (keyName!=null) 
					ampCategoryValues	= CategoryManagerUtil.getAmpCategoryByKey(keyName, ordered);
				else
					ampCategoryValues	= CategoryManagerUtil.getAmpCategoryValueCollection(categoryName, ordered);
			}
			
			
			if (ampCategoryValues != null && ampCategoryValues.size() > 0) {
				ampCategoryClass			= ((AmpCategoryValue)ampCategoryValues.toArray()[0]).getAmpCategoryClass();
				boolean isMultiselect		= this.getMultiselect(ampCategoryClass);
				if (isMultiselect) {
				
					/* Getting the ids (there might be more than 1 since it is a multiselect) of the current value of the category */
					try{
						PropertyDescriptor beanProperty	= new PropertyDescriptor(property, bean.getClass());
						valueIds						= (Long[])(beanProperty.getReadMethod().invoke(bean,new Object[0]));
						valueIdsColl					= new HashSet();
						if (valueIds != null) {
							for (int i=0; i<valueIds.length; i++) {
								valueIdsColl.add( valueIds[i] );
							}
						}
					}
					catch(Exception E){
						logger.error(E);
					}
				}
				else {
					/* Getting the id of the current value of the category */
					try{
						PropertyDescriptor beanProperty	= new PropertyDescriptor(property, bean.getClass());
						valueId							= (Long)(beanProperty.getReadMethod().invoke(bean,new Object[0]));
					}
					catch(Exception E){
						logger.error(E);
					}
				}
				
				/* Deciding which type of html input to use for rendering the html page */
				if ( this.getListView() )
						this.printListView(ampCategoryValues, isMultiselect, valueId, valueIdsColl);
				else 
						this.printBoxView( ampCategoryValues, isMultiselect, valueId, valueIdsColl);
				
			}
		}
		catch(Exception E){
			logger.info(E);
			E.printStackTrace();
		}
		
		return EVAL_PAGE;
	}
	private void printListView (Collection ampCategoryValues, boolean isMultiselect, Long valueId, Collection valueIdsColl) throws Exception {
		HttpServletRequest request		= (HttpServletRequest)pageContext.getRequest();
		String classProperty			= "";
		
		
		JspWriter out					= pageContext.getOut();
		String multiselectProperty		= "";
		
		if (styleClass != null)
			classProperty	= " class='"+ styleClass + "'";
		if (isMultiselect) 
			multiselectProperty	= " multiple='multiple' size='"+size+"'";
		
		
		Iterator iterator			= ampCategoryValues.iterator();			
		out.println("<select name='"+property+"' "+classProperty+multiselectProperty+">");
		if ( !isMultiselect )
			out.println("<option value='0' >Please select from below</option>");
		
		while (iterator.hasNext()) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			String outputValue					= CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request, null);
			
			if ( valueId != null && valueId.longValue()	== ampCategoryValue.getId().longValue() || 
					( valueIdsColl != null && valueIdsColl.contains(ampCategoryValue.getId()) ) ) {
				out.println("<option value='"+ampCategoryValue.getId()+"' selected='selected' >"+outputValue+"</option>");
			}
			else{
				out.println("<option value='"+ampCategoryValue.getId()+"' >"+outputValue+"</option>");
			}
			
		}
		out.println("</select>");
	}
	
	private void printBoxView (Collection ampCategoryValues, boolean isMultiselect, Long valueId, Collection valueIdsColl) throws Exception {
		HttpServletRequest request		= (HttpServletRequest)pageContext.getRequest();
		String classProperty			= "";
		String typeProperty				= " type='radio'";
		String nameProperty				= " name='"+property+"'";
		
		JspWriter out					= pageContext.getOut();
		

		
		
		if (styleClass != null)
			classProperty	= " class='"+ styleClass + "'";
		if (isMultiselect) {
			typeProperty			= " type='checkbox'";
		}
		
		out.println("<table>");
		Iterator iterator			= ampCategoryValues.iterator();			
		while (iterator.hasNext()) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			String outputValue					= CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request, null);
			out.println("<tr><td>");
			if ( ( valueId != null && valueId.longValue()	== ampCategoryValue.getId().longValue() ) || 
					( valueIdsColl != null && valueIdsColl.contains(ampCategoryValue.getId()) ) ) {
				out.println("<input "+typeProperty+" value='"+ampCategoryValue.getId()+"' checked='checked' >"+outputValue+"</input>");
			}
			else{
				out.println("<input "+typeProperty+nameProperty+" value='"+ampCategoryValue.getId()+"' >"+outputValue+"</input>");
			}
			out.println("</td></tr>");
			
		}
		out.println("</table>");
	}
	private boolean getMultiselect (AmpCategoryClass categoryClass) {
		if ( multiselect != null ) {
			return multiselect.booleanValue();
		}
		else
			return categoryClass.getisMultiselect();
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
	
	public boolean getListView() {
		return listView;
	}
	public void setListView(boolean listView) {
		this.listView = listView;
	}
	
}
