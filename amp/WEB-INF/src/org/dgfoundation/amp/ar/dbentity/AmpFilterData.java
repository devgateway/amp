package org.dgfoundation.amp.ar.dbentity;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.PropertyListable.PropertyListableIgnore;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.reports.IgnorePersistence;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.Identifiable;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.*;

@Entity
@Table(name = "AMP_FILTER_DATA")
public class AmpFilterData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_filter_data_seq")
    @SequenceGenerator(name = "amp_filter_data_seq", sequenceName = "AMP_FILTER_DATA_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_class_name")
    private String propertyClassName;

    @Column(name = "element_class_name")
    private String elementClassName;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_report_id", nullable = false)
    private AmpReports ampReport;
    private FilterDataSetInterface filterRelObj;

    
    
    private static Logger logger = Logger.getLogger(AmpFilterData.class);
    private static final String [] primitiveTypes = {   String.class.getName(), 
                                                    Integer.class.getName(), 
                                                    Long.class.getName(),
                                                    Float.class.getName(),
                                                    Double.class.getName(),
                                                    Boolean.class.getName()};
    
    private static final List<String> primitiveTypesList    = Arrays.asList( primitiveTypes );
    
    public AmpFilterData () {;}
    public AmpFilterData (FilterDataSetInterface filterRelObj, String propertyName, String propertyClassName,
                            String elementClassName, String value) {
        this.filterRelObj           = filterRelObj;
        this.propertyName       = propertyName;
        this.propertyClassName  = propertyClassName;
        this.elementClassName   = elementClassName;
        this.value              = value;
        this.id                 = null;
    }

    @Override
    public String toString() {
        return "report:" + filterRelObj + "; propertyName:" + propertyName + "; propertyClassName:" + propertyClassName + 
        "; elementClassName:" + elementClassName +  "; value:" + value;
    }
    
    public void populateField (Object parentObject) throws Exception {
            Field field = parentObject.getClass().getDeclaredField( this.propertyName );
            if ( field == null )
                return;
            PropertyDescriptor pd       = new PropertyDescriptor(this.propertyName, parentObject.getClass() );
            Object fieldObj             = pd.getReadMethod().invoke(parentObject, new Object[0]);
            
            Object element              = this.instantiateFilterObject( parentObject.getClass() );
            
            if ( Collection.class.isAssignableFrom(field.getType()) ) {
                if ( this.elementClassName == null || this.elementClassName.length() == 0 ) {
                    throw new Exception ("Cannot have null as elementClassName when the field property is a collection");                   
                }
                Collection<Object> col;
                if ( fieldObj == null ) {
                    Class colClass          = Class.forName(this.propertyClassName);
                    col                     = (Collection<Object>)colClass.newInstance();
                    Object [] params        = new Object[1];
                    params[0]               = col;
                    pd.getWriteMethod().invoke(parentObject, params);
                }
                else
                    col                     = (Collection<Object>)fieldObj;
                
                
                col.add(element);
            }
            else 
                pd.getWriteMethod().invoke(parentObject, element);
    } 
    
    public Object instantiateFilterObject (Class parentObjectClass) throws Exception {
        Field field = parentObjectClass.getDeclaredField( this.propertyName );
        if ( field == null )
            throw new Exception ("There is no field with name '" + this.propertyName + "' in class '" +  parentObjectClass.getName() + "'");
        Class myClass   = null;
        if ( this.elementClassName!=null && this.elementClassName.length()>0 ) 
            myClass     = Class.forName( this.elementClassName );
        else 
            myClass     = field.getType();
        
        if ( Identifiable.class.isAssignableFrom(myClass) ) {
            Session session = PersistenceManager.getRequestDBSession();
            Object ret      = session.load(myClass, Long.parseLong(this.value) );
            return ret;
        }
        
        Iterator<String> iter       = AmpFilterData.primitiveTypesList.iterator();
        while ( iter.hasNext() ) {
            String primitiveClassString     = iter.next();
            if ( primitiveClassString.toLowerCase().contains(myClass.getName().toLowerCase()) ) {
                Class primitiveClass    = Class.forName( primitiveClassString );
                Constructor constructor = primitiveClass.getConstructor( String.class );
                Object ret              = constructor.newInstance( this.value );
                return ret;
            }
        }
        
        throw new Exception ( myClass + " is neither Identifiable nor of a supported primitive type");
    }
    
    /**
     * reads properties from an object and pumps them into a destination FilterDataSetInterface
     * @param fds
     * @param srcObj
     * @return
     */
    public static Set<AmpFilterData> createFilterDataSet (FilterDataSetInterface fds, Object srcObj) {
        Field[] fields                  = srcObj.getClass().getDeclaredFields();
        HashSet<AmpFilterData> fdSet        = new HashSet<AmpFilterData>();
        
        if ( fields != null && fields.length > 0 ) {
            for (int i=0; i<fields.length; i++) {
                Class fieldClass        = fields[i].getType();
                PropertyDescriptor pd;
                Object fieldObj;
                try {
                    pd              = new PropertyDescriptor(fields[i].getName(), srcObj.getClass() );
                    fieldObj        = pd.getReadMethod().invoke(srcObj, new Object[0]);
                    if (fieldObj == null)
                        continue;
                } catch (Exception e) {
                    //e.printStackTrace();
                    logger.warn(e.getMessage());
                    continue;
                }
                /**
                 * We check here if this field's getter is annotated with PropertyListableIgnore. On true -> skip
                 */
                Method readMethod   = pd.getReadMethod();
                if ( readMethod.getAnnotation(PropertyListableIgnore.class) != null || 
                        readMethod.getAnnotation(IgnorePersistence.class) != null ) 
                    continue;
                /** 
                 * We check here if the field is actually a collection of objects, like sectors for example
                 */
                if ( fieldObj instanceof Collection ) {
                    Iterator<? extends Object> iter = ((Collection<? extends Object>)fieldObj).iterator();
                    while (iter.hasNext()) {
                        Object element  = iter.next();
                        if (element != null) {
                            String elClassName  = element.getClass().getName();
                            int indexOfDollar   = elClassName.indexOf("_$$");
                            if ( indexOfDollar < 0)
                                indexOfDollar   = elClassName.indexOf("$$");
                                
                            if ( indexOfDollar >= 0 )
                                elClassName     = elClassName.substring(0, indexOfDollar); 
                            
                            AmpFilterData fd    = fds.newAmpFilterData( fds, fields[i].getName(), 
                                    fieldObj.getClass().getName(), elClassName, 
                                    objectValue(element) );
                            fdSet.add( fd );
                        }
                    }
                }
                
                if ( fieldObj instanceof Identifiable) {
                    AmpFilterData fd = fds.newAmpFilterData ( fds, fields[i].getName(), fieldObj.getClass().getName(), 
                            null, objectValue(fieldObj) ) ;
                    fdSet.add( fd );
                }
                
                else 
                    if ( primitiveTypesList.contains(fieldObj.getClass().getName()) ) {
                        AmpFilterData fd = fds.newAmpFilterData ( fds, fields[i].getName(), fieldObj.getClass().getName(), 
                        null, objectValue(fieldObj) ) ;
                        fdSet.add( fd );
                    }
                
            }
        }
        return fdSet;
    }
    
    public static String objectValue (Object obj) {
        if (obj instanceof Identifiable) {
            Object identifier   = ((Identifiable)obj).getIdentifier();
            return identifier.toString();
        }
        else 
            return obj.toString();
    }
    
    public static void deleteOldFilterData ( Long ampReportId ) {
//      Session sess    = null;
//      Transaction tx  = null;
        try {
//          sess    = PersistenceManager.openNewSession();
    //      tx      = sess.beginTransaction();
            String qryStr   = "select a from "
                + AmpFilterData.class.getName() + " a "
                + "where (a.filterRelObj=:report)";
            Query query     = PersistenceManager.getSession().createQuery(qryStr).setLong("report", ampReportId);
            List<AmpFilterData> results = query.list();
            
            boolean cleared = false;
            for(AmpFilterData afd:results){
                if (!cleared) {
                    cleared = true;
                    afd.getFilterRelObj().getFilterDataSet().clear();
                } 
                afd.setFilterRelObj(null);
                PersistenceManager.getSession().delete(afd);
            }
            //tx.commit();
            
        } catch (Exception e) {
            throw new RuntimeException(e);
            //logger.error("Rolling back transaction of deleting exising filters before saving new ones");
            //tx.rollback();
            //e.printStackTrace();
        }
//      finally{
//          if ( sess != null ) {
//              try {
//                  sess.close();
//              }
//              catch (HibernateException e) {
//                  logger.error("Unable to close manually opened hibernate session");
//              }
//          }
//      }
        
    } 
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public FilterDataSetInterface getFilterRelObj() {
        return filterRelObj;
    }
    public void setFilterRelObj(FilterDataSetInterface filterRelObj) {
        this.filterRelObj = filterRelObj;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    public String getPropertyClassName() {
        return propertyClassName;
    }
    public void setPropertyClassName(String propertyClassName) {
        this.propertyClassName = propertyClassName;
    }
    public String getElementClassName() {
        return elementClassName;
    }
    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public AmpReports getAmpReport() {
        return ampReport;
    }

    public void setAmpReport(AmpReports ampReport) {
        this.ampReport = ampReport;
    }
}
