/**
 * PropertyListable.java (c) 2007 Development Gateway Foundation
 */
package org.dgfoundation.amp;

import org.apache.log4j.Logger;
import org.digijava.module.aim.annotations.reports.IgnorePersistence;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * PropertyListable.java Implemented by Beans that can have their properties Listable
 * 
 * @author mihai
 * @package org.dgfoundation.amp
 * @since 09.09.2007
 */
public abstract class PropertyListable implements Cloneable {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertyListableIgnore {      
    }
    

    
    @Override
    public Object clone() throws CloneNotSupportedException {
    return super.clone();
    }
    
    protected static Logger logger = Logger.getLogger(PropertyListable.class);
    
    
    public abstract String getBeanName();
    
    
    @PropertyListableIgnore
    public String getJspFile() {
    return "/repository/aim/view/listableBean.jsp";
    }
    
    @PropertyListableIgnore
    public Map getPersistencePropertiesMap() {
        ArrayList<Class> annotations    = new ArrayList<Class>();
        annotations.add( PropertyListableIgnore.class );
        annotations.add( IgnorePersistence.class );
        return this.generatePropertiesMap( annotations );
    }
    
    @PropertyListableIgnore
    public String getStringRepresentation()
    {
        Map<String, Object> map = getPropertiesMap();
        StringBuilder bld = new StringBuilder();
        for(String key: map.keySet())
            bld.append(String.format("key: %s has value: %s\n", key, map.get(key)));
        return bld.toString();
    }
    
    /**
     * provides a way to display this bean in HTML. 
     */
    @PropertyListableIgnore
    public Map<String, Object> getPropertiesMap() {
        ArrayList<Class> annotations    = new ArrayList<Class>();
        annotations.add( PropertyListableIgnore.class );
        return this.generatePropertiesMap( annotations );
    }
    
    /**
         * Properties are automatically returned along with their values.
         * CollectionS are unfolded and excluded properties (internally used) are not shown. You may provide a collection of annotations  
         * in order to ignore the getters for the properties you may not want to list externally
         */
    
    @PropertyListableIgnore
    private Map<String, Object> generatePropertiesMap(Collection<Class> annonations) {
    Map<String, Object> ret = new TreeMap<String, Object>();
    BeanInfo beanInfo = null;
    try {
        beanInfo = Introspector.getBeanInfo(this.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        
        for (int i = 0; i < propertyDescriptors.length; i++) {  
            if(propertyDescriptors[i].getName().equals("class")) continue;
            Method m = propertyDescriptors[i].getReadMethod();
            //if(m.isAnnotationPresent(PropertyListableIgnore.class))continue;
            Iterator<Class> iter    = annonations.iterator();
            
            boolean skip        = false;
            while ( iter.hasNext() ) {
                if ( m.isAnnotationPresent(iter.next()) ) {
                    skip        = true;
                    break;
                }
            }
            if ( skip )
                    continue;
            
            Object object = m.invoke(this, new Object[] {});
            
            if ((object == null) || (object instanceof String)
                            && ("".equalsIgnoreCase((String) object))) continue;
            if ( object instanceof Collection && ((Collection)object).size() == 0 )
                            continue;
            //AMP-3372
            /*
            ret.put(propertyDescriptors[i].getName(), object instanceof Collection ? Util.collectionAsString(
                (Collection) object) : object);*/

            //AMP-11638
            Collection selProps = null;
            if (object instanceof Collection) {
                selProps = (Collection) object;
            } else {
                selProps = new ArrayList();
                selProps.add(object);
            }

            ret.put(propertyDescriptors[i].getName(), selProps);
        }
    } catch (IntrospectionException e) {
        logger.error(e.getMessage(), e);
    } catch (IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
    } catch (IllegalAccessException e) {
        logger.error(e.getMessage(), e);
    } catch (InvocationTargetException e) {
        logger.error(e.getMessage(), e);
    }
    return ret;

    }

}
