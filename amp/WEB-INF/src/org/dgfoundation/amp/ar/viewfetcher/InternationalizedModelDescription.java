package org.dgfoundation.amp.ar.viewfetcher;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * class holding the description of the i18n fields from a Hibernate model (identified by class name)
 * the constructor scans the Hibernate configuration and reflects on the class name 
 * @author Dolghier Constantin
 *
 */
public class InternationalizedModelDescription {
    @PersistenceContext
    private EntityManager entityManager;
    
    public final String className;
    
    public final HashMap<String, InternationalizedPropertyDescription> properties = new HashMap<String, InternationalizedPropertyDescription>();
    
    private InternationalizedModelDescription(Class<?> modelClass)
    {
        this.className = modelClass.getName();
        scanClass(modelClass);
    }

//  public static java.sql.Connection connection = initConnection();
//  
//  private static java.sql.Connection initConnection()
//  {
//      try
//      {
//          return PersistenceManager.getJdbcConnection();
//      }
//      catch(Exception e)
//      {
//          throw new RuntimeException("Could not get RAW JDBC Connection", e);
//      }
//  }
public EntityPersister getPersister(final Class<?> modelClazz) {
      entityManager=  PersistenceManager.getSession().getEntityManagerFactory().createEntityManager();
    final MetamodelImpl metamodel = (MetamodelImpl) entityManager.getMetamodel();
    final EntityPersister entityPersister = metamodel.entityPersister(modelClazz);
    if (entityPersister instanceof SingleTableEntityPersister) {
        return entityPersister;
    } else {
        throw new IllegalArgumentException(modelClazz + " does not map to a single table.");
    }
}

    public String getColumnNameByPropertyName(String propertyName,  Class<?> modelClazz) {
        int propertyIndex = getPersister(modelClazz).getEntityMetamodel().getPropertyIndex(propertyName);
        if (propertyIndex != -1) {
            String[] columnNames = ((SingleTableEntityPersister)getPersister(modelClazz)).getPropertyColumnNames(propertyIndex);
            // In a single table inheritance strategy, typically there will be only one column name per property
            if (columnNames.length > 0) {
                return columnNames[0];
            } else {
                throw new IllegalArgumentException("Property does not have a column mapping: " + propertyName);
            }
        } else {
            throw new IllegalArgumentException("Property name not found: " + propertyName);
        }
    }

    protected void scanClass(Class<?> modelClass)
    {
        //System.out.println("IMD: scanning class " + modelClass);
        
        if (modelClass.getAnnotation(TranslatableClass.class) == null)
            throw new RuntimeException("asked to scan class " + modelClass + ", which is translatable");
            
        String keyColumnName = Arrays.stream(((SingleTableEntityPersister)getPersister(modelClass)).getKeyColumnNames()).iterator().next();
        String modelTableName = ((SingleTableEntityPersister)getPersister(modelClass)).getTableName();
        Set<String> existingColumns = SQLUtils.getTableColumns(modelTableName);
        boolean idColumnExists = existingColumns.contains(keyColumnName);
                
        if (!idColumnExists)
            throw new RuntimeException(String.format("could not scan model %s: key column %s does not exist in table %s", modelClass, keyColumnName, modelTableName));
        
        ArrayList<Field> fields = new ArrayList<Field>();
        ContentTranslationUtil.getAllFields(fields, modelClass); // getting into _fields_ the list of all the fields of the class
                
        for(Field field:fields)
        {
            if (field.getAnnotation(TranslatableField.class) != null){

                // field is translated, make an entry for it
                if (!field.getType().getName().equals("java.lang.String"))
                    throw new RuntimeException(String.format("field %s of model %s should be a string", field.getName(), modelClass)); // we only allow translating String fields
                String propertyName = field.getName();
                String columnName = getColumnNameByPropertyName(propertyName, modelClass);
                boolean dataColumnExists = existingColumns.contains(columnName); // sanity check of the Hibernate configuration - we are SERIOUSLY screwed if these are not correct - probably we are wrongly fetching the Hibernate conf
                if (!dataColumnExists)
                    throw new RuntimeException(String.format("could not init property %s of model %s: data column %s does not exist in table %s", propertyName, modelClass, columnName, modelTableName));

                properties.put(propertyName, new InternationalizedPropertyDescription(field, propertyName, this.className, modelTableName.toLowerCase(), keyColumnName, columnName));
            }
        }
    }
    
    /**
     * global repository for models - used for caching the results of scanning a class
     */
    private final static Map<String, InternationalizedModelDescription> globalRepository = Collections.synchronizedMap(new HashMap<String, InternationalizedModelDescription>());
    private final static Object imdLock = new Object();
    
    public static InternationalizedModelDescription getForClass(Class<?> clazz)
    {
        synchronized(imdLock)
        {
            String className = clazz.getName();
            if (!globalRepository.containsKey(className))
                globalRepository.put(className, new InternationalizedModelDescription(clazz));
            return globalRepository.get(className);
        }
    }

    public static InternationalizedPropertyDescription getForProperty(Class<?> clazz, String propertyName)
    {
        return getForClass(clazz).properties.get(propertyName);
    }
}
