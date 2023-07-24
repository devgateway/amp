package org.dgfoundation.amp.ar.viewfetcher;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.mapping.PersistentClass;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * class holding the description of the i18n fields from a Hibernate model (identified by class name)
 * the constructor scans the Hibernate configuration and reflects on the class name 
 * @author Dolghier Constantin
 *
 */
public class InternationalizedModelDescription {
    
    public final String className;
    
    public final HashMap<String, InternationalizedPropertyDescription> properties = new HashMap<String, InternationalizedPropertyDescription>();
    
    private InternationalizedModelDescription(Class<?> modelClass)
    {
        this.className = modelClass.getName();
        scanClass(modelClass);
    }


protected void scanClass(Class<?> modelClass) {
    if (modelClass.getAnnotation(TranslatableClass.class) == null) {
        throw new RuntimeException("Asked to scan class " + modelClass + ", which is not translatable");
    }

    // Get the table name from the entity class
    Table tableAnnotation = modelClass.getAnnotation(Table.class);
    if (tableAnnotation == null) {
        throw new RuntimeException("No @Table annotation found on entity class " + modelClass);
    }
    String modelTableName = tableAnnotation.name();

    // Get the primary key column name from the entity class
    String keyColumnName = null;
    for (Field field : modelClass.getDeclaredFields()) {
        Id idAnnotation = field.getAnnotation(Id.class);
        if (idAnnotation != null) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                keyColumnName = columnAnnotation.name();
                break;
            }
        }
    }
    if (keyColumnName == null) {
        throw new RuntimeException("No @Id column found on entity class " + modelClass);
    }

    // Get the existing columns in the table from your utility method
    Set<String> existingColumns = SQLUtils.getTableColumns(modelTableName);
    if (!existingColumns.contains(keyColumnName)) {
        throw new RuntimeException(String.format("Could not scan model %s: key column %s does not exist in table %s", modelClass, keyColumnName, modelTableName));
    }

    ArrayList<Field> fields = new ArrayList<>();
    ContentTranslationUtil.getAllFields(fields, modelClass);

    for (Field field : fields) {
        if (field.getAnnotation(TranslatableField.class) != null) {
            // Field is translated, make an entry for it
            if (!field.getType().equals(String.class)) {
                throw new RuntimeException(String.format("Field %s of model %s should be a string", field.getName(), modelClass));
            }
            String propertyName = field.getName();
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation == null) {
                throw new RuntimeException(String.format("No @Column annotation found on field '%s' of model '%s'", propertyName, modelClass));
            }
            String columnName = columnAnnotation.name();
            if (!existingColumns.contains(columnName)) {
                throw new RuntimeException(String.format("Could not init property '%s' of model '%s': data column '%s' does not exist in table '%s'", propertyName, modelClass, columnName, modelTableName));
            }

            properties.put(propertyName, new InternationalizedPropertyDescription(field, propertyName, this.className, modelTableName.toLowerCase(), keyColumnName, columnName));
        }
    }
}
    
//    protected void scanClass(Class<?> modelClass)
//    {
//        //System.out.println("IMD: scanning class " + modelClass);
//
//        if (modelClass.getAnnotation(TranslatableClass.class) == null)
//            throw new RuntimeException("Asked to scan class " + modelClass + ", which is not translatable");
//
//        PersistentClass classMapping = PersistenceManager.getClassMapping(modelClass);
//        Column keyColumn = (Column) (classMapping.getIdentifierProperty().getColumnIterator().next());
//        String keyColumnName = keyColumn.getName();
//        String modelTableName = classMapping.getTable().getName();
//        Set<String> existingColumns = SQLUtils.getTableColumns(modelTableName);
//        boolean idColumnExists = existingColumns.contains(keyColumnName);
//
//        if (!idColumnExists)
//            throw new RuntimeException(String.format("could not scan model %s: key column %s does not exist in table %s", modelClass, keyColumnName, modelTableName));
//
//        ArrayList<Field> fields = new ArrayList<Field>();
//        ContentTranslationUtil.getAllFields(fields, modelClass); // getting into _fields_ the list of all the fields of the class
//
//        for(Field field:fields)
//        {
//            if (field.getAnnotation(TranslatableField.class) != null){
//
//                // field is translated, make an entry for it
//                if (!field.getType().getName().equals("java.lang.String"))
//                    throw new RuntimeException(String.format("field %s of model %s should be a string", field.getName(), modelClass)); // we only allow translating String fields
//                String propertyName = field.getName();
//                Column column = (Column) (classMapping.getProperty(propertyName).getColumnIterator().next());
//                String columnName = column.getName();
//                boolean dataColumnExists = existingColumns.contains(columnName); // sanity check of the Hibernate configuration - we are SERIOUSLY screwed if these are not correct - probably we are wrongly fetching the Hibernate conf
//                if (!dataColumnExists)
//                    throw new RuntimeException(String.format("could not init property %s of model %s: data column %s does not exist in table %s", propertyName, modelClass, columnName, modelTableName));
//
//                properties.put(propertyName, new InternationalizedPropertyDescription(field, propertyName, this.className, modelTableName.toLowerCase(), keyColumnName, columnName));
//            }
//        }
//    }
    
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
