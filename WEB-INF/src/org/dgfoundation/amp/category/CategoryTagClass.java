/**
 * 
 */
package org.dgfoundation.amp.category;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCategoryClass;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author Alex Gartner
 *
 */
public class CategoryTagClass extends TagSupport implements DynamicAttributes {
	private Logger logger	= Logger.getLogger(CategoryTagClass.class);
	Boolean multiselect	= null;
	Boolean ordered		= null;
	
	boolean listView	= true;
	
	String name			= null;
	String keyName		= null;
	String property		= null;
	String styleClass	= null;
	
	Long categoryId		= null;
	String categoryName	= null;
	
	String firstLine	= null;
	
	StringBuffer outerDynamicAttributes	= new StringBuffer(" ");
	StringBuffer innerDynamicAttributes	= new StringBuffer(" ");
	
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
	
	public void setDynamicAttribute(String uri, String localName, Object value) {
		String name;
		if ( localName.toLowerCase().startsWith("outer") ) {
			name = localName.substring(5);
			outerDynamicAttributes.append(name+"=\""+value+"\" ");
		}
		if ( localName.toLowerCase().startsWith("inner") ) {
			name = localName.substring(5);
			innerDynamicAttributes.append(name+"=\""+value+"\" ");
		}
	}
	public int doStartTag() {
		return SKIP_BODY;
	}
	public int doEndTag() throws JspException {
		Object bean						= pageContext.findAttribute(name);
		Long valueId					= null; // for single select
		//Long [] valueIds				= null; // for multiselect
		Object values					= null; // for multiselect
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
						values							= beanProperty.getReadMethod().invoke(bean,new Object[0]);
						//valueIds						= (Long[])(beanProperty.getReadMethod().invoke(bean,new Object[0]));
						valueIdsColl					= new HashSet();
						if (values != null) {
							if (values instanceof Object[]){//many values
								Object [] valueIds	= (Object []) values;
								for (int i=0; i<valueIds.length; i++) {
									if ( valueIds[i] instanceof Long )
										valueIdsColl.add( (Long)valueIds[i] );
									if ( valueIds[i] instanceof String )
										valueIdsColl.add( new Long ((String)valueIds[i]) );
								}
						
							}else{//only one value
								if ( values instanceof Long )
									valueIdsColl.add( (Long)values );
								if (values instanceof String )
									valueIdsColl.add( new Long ((String)values) );
								
							}
							
							
							}
					}
					catch(Exception E){
						logger.error(E);
						E.printStackTrace();
					}
				}

				else {
					/* Getting the id of the current value of the category */
					try{
						if(bean!=null) {
						BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(bean);
						valueId=(Long) beanWrapperImpl.getPropertyValue(property);
						}
					}
//					try{
//						PropertyDescriptor beanProperty	= new PropertyDescriptor(property, bean.getClass());
//						valueId							= (Long)(beanProperty.getReadMethod().invoke(bean,new Object[0]));
//					}
					catch(Exception E){
						logger.error(E);
						E.printStackTrace();
					}
				}
				
				/* Deciding which type of html input to use for rendering the html page */
				if ( this.getListView() )
						this.printListView(ampCategoryValues, isMultiselect, valueId, valueIdsColl);
				else 
						this.printBoxView( ampCategoryValues, isMultiselect, valueId, valueIdsColl);
				
			}
			else {
				JspWriter out					= pageContext.getOut();
				out.println("<font color='red'>Category not found in database</font>");
				
			}
		}
		catch(Exception E){
			logger.info(E);
			E.printStackTrace();
		}
		
		this.reset();
		return EVAL_PAGE;
	}
	private void printListView (Collection ampCategoryValues, boolean isMultiselect, Long valueId, Collection valueIdsColl) throws Exception {
		HttpServletRequest request		= (HttpServletRequest)pageContext.getRequest();
		String classProperty			= "";
		String fLine					= firstLine;
		
		JspWriter out					= pageContext.getOut();
		String multiselectProperty		= "";
		
		String firstLineSelectedProperty	= "";
		
		if (styleClass != null)
			classProperty	= " class='"+ styleClass + "'";
		if (isMultiselect) 
			multiselectProperty	= " multiple='multiple' size='"+size+"'";
		
		
		Iterator iterator			= ampCategoryValues.iterator();			
		out.println("<select name='"+property+"' "+classProperty+multiselectProperty+outerDynamicAttributes+">");
		
		if ( (valueId != null && valueId.longValue() == 0) || 
				( valueIdsColl != null && valueIdsColl.contains(new Long(0)) ) )
				firstLineSelectedProperty	= "selected='selected'";
		
		if ( !isMultiselect ) {
			if ( fLine == null ) {
				 String pleaseSelectBelow = "aim:pleaseSelectBelow";
				 Site site = RequestUtils.getSite(request);
				 //
				 //requirements for translation purposes
				 TranslatorWorker translator = TranslatorWorker.getInstance();
				 String siteId = site.getSiteId();
				 String locale = RequestUtils.getNavigationLanguage(request).getCode();
				 String translatedText = null;
				 try {
						logger.info("siteID : "+siteId);
						logger.info("locale : "+locale);
						translatedText = TranslatorWorker.translate(pleaseSelectBelow, locale, siteId);
					 } catch (WorkerException e) {
						e.printStackTrace();
					 }
				fLine	= translatedText;
			}
			out.println("<option value='0' "+firstLineSelectedProperty+" >"+fLine+"</option>");
		}
		else {
			if ( fLine != null )
				out.println("<option value='0' "+firstLineSelectedProperty+" >"+fLine+"</option>");
		}
		
		while (iterator.hasNext()) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if (ampCategoryValue!=null){
				String outputValue					= CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request);
				if ( valueId != null && valueId.longValue()	== ampCategoryValue.getId().longValue() || 
						( valueIdsColl != null && valueIdsColl.contains(ampCategoryValue.getId()) ) ) {
					out.println("<option value='"+ampCategoryValue.getId()+"' selected='selected'"+innerDynamicAttributes+" >"+outputValue+"</option>");
				}
				else{
					out.println("<option value='"+ampCategoryValue.getId()+"' "+innerDynamicAttributes+" >"+outputValue+"</option>");
				}
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
		
		out.println("<table " + outerDynamicAttributes + ">");
		
		if ( firstLine != null) {
			String checkedProperty	= "";
			if (valueId != null && valueId.longValue() == 0)
				checkedProperty		= " checked='checked'";
			if (valueIdsColl != null && valueIdsColl.contains(new Long(0)) )
				checkedProperty		= " checked='checked'";
					
			out.println("<tr><td><input value='0' "+typeProperty+checkedProperty+" >"+firstLine+"</input></td></tr>");
		}
		
		Iterator iterator			= ampCategoryValues.iterator();
		
		while (iterator.hasNext()) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			String outputValue					= CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request);
			out.println("<tr><td>");
			if ( ( valueId != null && valueId.longValue()	== ampCategoryValue.getId().longValue() ) || 
					( valueIdsColl != null && valueIdsColl.contains(ampCategoryValue.getId()) ) ) {
				out.println("<input "+typeProperty+nameProperty+" value='"+ampCategoryValue.getId()+"' checked='checked'"+innerDynamicAttributes+" />"+outputValue);
			}
			else{
				out.println("<input "+typeProperty+nameProperty+" value='"+ampCategoryValue.getId()+"' "+innerDynamicAttributes+" />"+outputValue);
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
	
	private void reset () {
		innerDynamicAttributes	= new StringBuffer(" ");
		outerDynamicAttributes	= new StringBuffer(" ");
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
	public String getFirstLine() {
		return firstLine;
	}
	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}
	
	
}
