/**
 * 
 */
package org.digijava.module.categorymanager.tags;

import org.apache.log4j.Logger;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Alex Gartner
 *
 */
public class CategoryTagClass extends TagSupport implements DynamicAttributes {
    private Logger logger   = Logger.getLogger(CategoryTagClass.class);
    Boolean multiselect = null;
    Boolean ordered     = null;
    
    boolean listView    = true;
    
    String name         = null;
    String keyName      = null;
    String property     = null;
    String styleClass   = null;
    
    Long categoryId     = null;
    String categoryName = null;
    
    String firstLine    = null;
    String styleId = null;
    
    String onchange = null;
    
    private Long tag            = null;
    
    StringBuffer outerDynamicAttributes = new StringBuffer(" ");
    StringBuffer innerDynamicAttributes = new StringBuffer(" ");
    
    int size            = 3;
    
    public String getOnchange(){
        return onchange;
    }
    
    public void setOnchange(String onchange){
        this.onchange = onchange;
    }
    
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
        Object bean                     = pageContext.findAttribute(name);
        Long valueId                    = null; // for single select
        //Long [] valueIds              = null; // for multiselect
        Object values                   = null; // for multiselect
        Set<Long> valueIdsColl          = null;
        
//      HttpServletRequest thisRequest  = (HttpServletRequest)pageContext.getRequest();
        
        try{
            Collection<AmpCategoryValue> ampCategoryValues;
            AmpCategoryClass ampCategoryClass   = null;

            if ( categoryId != null )
                ampCategoryValues   = CategoryManagerUtil.getAmpCategoryValueCollection(categoryId, ordered);
            else {
                if (keyName!=null) 
                    ampCategoryValues   = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(keyName, ordered);
                else
                    ampCategoryValues   = CategoryManagerUtil.getAmpCategoryValueCollection(categoryName, ordered);
            }
            
            if (ampCategoryValues != null && getTag() != null) {
                Set<AmpCategoryValue> tagged = CategoryManagerUtil
                        .getTaggedCategoryValues(getTag(), keyName);
                /* This is just a reminder that lines below should be commented in case the initialization problem occurs
                ampCategoryValues = tagged;
                */
                if (tagged == null)
                    ampCategoryValues.clear();
                else
                    ampCategoryValues.retainAll(tagged);
            }

            if (ampCategoryValues != null) {
                boolean isMultiselect = false;
                if(ampCategoryValues.size() > 0){
                    for (Iterator iterator = ampCategoryValues.iterator(); iterator.hasNext();) {
                        AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
                        if (ampCategoryValue!=null){
                            ampCategoryClass = ampCategoryValue.getAmpCategoryClass();
                            break; 
                        }
                    }
                    isMultiselect = this.getMultiselect(ampCategoryClass);
                }
                if (isMultiselect) {
                
                    /* Getting the ids (there might be more than 1 since it is a multiselect) of the current value of the category */
                    try{
                        if ( this.property != null ) {
                            BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(bean);
                            values = beanWrapperImpl.getPropertyValue(property);
                            //PropertyDescriptor beanProperty   = new PropertyDescriptor(property, bean.getClass());
                            //values                            = beanProperty.getReadMethod().invoke(bean,new Object[0]);
                            //valueIds                      = (Long[])(beanProperty.getReadMethod().invoke(bean,new Object[0]));
                            valueIdsColl                    = new HashSet();
                            if (values != null) {
                                if (values instanceof Object[]){//many values
                                    Object [] valueIds  = (Object []) values;
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
                    }
                    catch(Exception E){
                        logger.error(E.getMessage(), E);
                    }
                }else {
                    /* Getting the id of the current value of the category */
                    try {
                        if (bean != null && this.property != null) {
                            BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(
                                    bean);
                            Object o = beanWrapperImpl
                                    .getPropertyValue(property);
                            try {
                                valueId = (Long) o;
                            } catch (ClassCastException e) {
                                logger.error("Expected Long"
                                        + ". Was given " + o.toString()
                                        + " of type "
                                        + o.getClass().getSimpleName());
                                e.printStackTrace();
                            }
                        }
                    }
                    catch(Exception E){
                        logger.error(E.getMessage(), E);
                    }
                }
                
                /* Deciding which type of html input to use for rendering the html page */
                if ( this.getListView() )
                        this.printListView(ampCategoryValues, isMultiselect, valueId, valueIdsColl, styleId);
                else 
                        this.printBoxView( ampCategoryValues, isMultiselect, valueId, valueIdsColl, styleId);
                
            }
            else {
                JspWriter out                   = pageContext.getOut();
                out.println("<font color='red'>Category not found in database</font>");
                
            }
        }
        catch(Exception E){
            logger.info(E.getMessage(), E);
        }
        
        this.reset();
        return EVAL_PAGE;
    }
    private void printListView (Collection ampCategoryValues, boolean isMultiselect, Long valueId, Collection valueIdsColl, String styleId) throws Exception {
        String classProperty            = "";
        String fLine                    = firstLine;
        
        JspWriter out                   = pageContext.getOut();
        String multiselectProperty      = "";
        String styleIdProperty = "";
        
        String firstLineSelectedProperty    = "";
        String onChangeProperty = "";
        
        if (styleClass != null)
            classProperty   = " class='"+ styleClass + "'";
        if (isMultiselect) 
            multiselectProperty = " multiple='multiple' size='"+size+"'";
        
        if (onchange != null)
            onChangeProperty = "onChange = " + onchange;
        
        if (styleId != null) {
            styleIdProperty = " id='" + styleId + "'";
        }
        
        
        Iterator iterator           = ampCategoryValues.iterator();
        String nameField            = (property!=null)?property:name;
        out.println("<select name='" + nameField + "' " + onChangeProperty + classProperty + multiselectProperty + outerDynamicAttributes + styleIdProperty + ">");
        
        if ( (valueId != null && valueId.longValue() == 0) || 
                ( valueIdsColl != null && valueIdsColl.contains(new Long(0)) ) )
                firstLineSelectedProperty   = "selected='selected'";
        
        if ( !isMultiselect ) {
            if ( fLine == null ) {
                 //String pleaseSelectBelow = "aim:pleaseSelectBelow"; not used any more because of hash keys
                 //
                 //requirements for translation purposes
                 String translatedText = TranslatorWorker.translateText("Please select from below");
                fLine   = translatedText;
            }
            out.println("<option value='0' "+firstLineSelectedProperty+" >"+fLine+"</option>");
        }
        else {
            if ( fLine != null )
                out.println("<option value='0' "+firstLineSelectedProperty+" >"+fLine+"</option>");
        }
        
        while (iterator.hasNext()) {
            AmpCategoryValue ampCategoryValue   = (AmpCategoryValue)iterator.next();
            if (ampCategoryValue!=null){
                String outputValue                  = CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue);
                if ( valueId != null && valueId.longValue() == ampCategoryValue.getId().longValue() || 
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
    
    private void printBoxView (Collection<AmpCategoryValue> ampCategoryValues, boolean isMultiselect, Long valueId, Collection valueIdsColl, String styleId) throws Exception {
//      String classProperty            = "";
        String typeProperty             = " type='radio'";
        String nameProperty             = " name='"+property+"'";
//      String styleIdProperty          = "";

        JspWriter out                   = pageContext.getOut();

        
        
/*      if (styleClass != null)
            classProperty   = " class='"+ styleClass + "'";*/
        if (isMultiselect) {
            typeProperty            = " type='checkbox'";
        }
        
/*      if (styleId != null) {
            styleIdProperty = " id='" + styleId + "'";
        }*/
        
        out.println("<table " + outerDynamicAttributes + ">");
        
        if ( firstLine != null) {
            String checkedProperty  = "";
            if (valueId != null && valueId.longValue() == 0)
                checkedProperty     = " checked='checked'";
            if (valueIdsColl != null && valueIdsColl.contains(new Long(0)) )
                checkedProperty     = " checked='checked'";
                    
            out.println("<tr><td><input value='0' "+typeProperty+checkedProperty+" >"+firstLine+"</input></td></tr>");
        }
        
        Iterator iterator           = ampCategoryValues.iterator();
        
        while (iterator.hasNext()) {
            AmpCategoryValue ampCategoryValue   = (AmpCategoryValue)iterator.next();
            String outputValue                  = CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue);
            out.println("<tr><td>");
            if ( ( valueId != null && valueId.longValue()   == ampCategoryValue.getId().longValue() ) || 
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
        innerDynamicAttributes  = new StringBuffer(" ");
        outerDynamicAttributes  = new StringBuffer(" ");
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
    public void setTag(Long tag) {
        this.tag = tag;
    }
    public Long getTag() {
        return tag;
    }
    public String getStyleId() {
        return styleId;
    }
    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    
    
}
