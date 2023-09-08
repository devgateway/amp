package org.digijava.kernel.ampapi.endpoints.activity.field;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityTranslationUtils;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class AmpFieldInfoProvider implements FieldInfoProvider {
    
    private final Object lock = new Object();
    private static  final Logger logger = LoggerFactory.getLogger(AmpFieldInfoProvider.class);
    
    private Map<Class<?>, Map<String, FieldInfo>> classFieldInfo = new HashMap<>();
    
    public Integer getMaxLength(Field field) {
        initializeDeclaringClassOfFieldIfNeeded(field);
        return isFieldInitialized(field) ? getFieldInfo(field).getMaxLength() : null;
    }
    
    @Override
    public boolean isTranslatable(Field field) {
        return TranslationSettings.getCurrent().isTranslatable(field);
    }
    
    @Override
    public TranslationSettings.TranslationType getTranslatableType(Field field) {
        return TranslationSettings.getCurrent().getTranslatableType(field);
    }
    
    public String getType(Field field) {
        initializeDeclaringClassOfFieldIfNeeded(field);
        return isFieldInitialized(field) ? getFieldInfo(field).getType() : null;
    }
    
    private FieldInfo getFieldInfo(Field field) {
        Class clazz = getActualFieldClass(field);
        return classFieldInfo.get(clazz).get(field.getName());
    }
    
    private void initializeDeclaringClassOfFieldIfNeeded(Field field) {
        synchronized (lock) {
            Class clazz = getActualFieldClass(field);
            if (classFieldInfo.get(clazz) == null && InternationalizedModelDescription.isEntity(clazz)) {
                initializeFields(clazz);
            }
        }
    }
    
    private boolean isFieldInitialized(Field field) {
        Class clazz = getActualFieldClass(field);
        return classFieldInfo.containsKey(clazz) && classFieldInfo.get(clazz).get(field.getName()) != null;
    }
    
    private void initializeFields(Class<?> clazz) {
        
        Map<String, FieldInfo> fieldInfoMap = new HashMap<>();
        
        final Map<String, String> dbTypes = new HashMap<>();
        final Map<String, Integer> maxLengths = new HashMap<>();
        
//        ClassMetadata meta = PersistenceManager.getClassMetadata(clazz);
        
//        if (meta == null) {
//            return;
//        }
        try {
            AbstractEntityPersister entityPersister = InternationalizedModelDescription.getPersister(clazz);

//        AbstractEntityPersister entityPersister = (AbstractEntityPersister) meta;
            final String tableName = entityPersister.getTableName();
            PersistenceManager.getSession().doWork(conn -> {
                String allSectorsQuery = "SELECT column_name, data_type, character_maximum_length "
                        + "FROM INFORMATION_SCHEMA.COLUMNS WHERE character_maximum_length IS NOT NULL "
                        + "AND table_name = '" + tableName + "'";
                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        dbTypes.put(rs.getString("column_name"), rs.getString("data_type"));
                        maxLengths.put(rs.getString("column_name"), rs.getInt("character_maximum_length"));
                    }
                    rs.close();
                }
            });

            String[] identityNames = entityPersister.getIdentifierColumnNames();
            if (identityNames.length > 0) {
                String fieldName = entityPersister.getIdentifierPropertyName();
                Field field = FieldUtils.getField(clazz, fieldName, true);
                if (field != null) {
                    String colName = identityNames[0];
                    FieldInfo fieldInfo = getFieldInfo(clazz, dbTypes.get(colName), maxLengths.get(colName), field);
                    fieldInfoMap.put(fieldName, fieldInfo);
                }
            }

            String[] propertyNames = entityPersister.getPropertyNames();
            for (int i = 0; i < propertyNames.length; i++) {
                String[] columnNames = entityPersister.getPropertyColumnNames(i);
                if (columnNames.length > 0) {
                    String fieldName = propertyNames[i];
                    Field field = FieldUtils.getField(clazz, fieldName, true);
                    // it could be possible that the fieldName could be part of a subclass and is not part of clazz
                    if (field != null) {
                        String colName = columnNames[0];
                        FieldInfo fieldInfo = getFieldInfo(clazz, dbTypes.get(colName), maxLengths.get(colName), field);
                        fieldInfoMap.put(fieldName, fieldInfo);
                    }
                }
            }

            classFieldInfo.put(clazz, fieldInfoMap);
        }catch (Exception e)
        {
            logger.error("Error initializing fields: "+e.getMessage(), e);
        }

    }
    
    private FieldInfo getFieldInfo(Class<?> clazz, String dbType, Integer maxLength, Field field) {
        FieldInfo fieldInfo = new FieldInfo(dbType, null);
        
        if (field != null && !ActivityTranslationUtils.isVersionableTextField(field)) {
            fieldInfo.setMaxLength(maxLength);
        }
        
        return fieldInfo;
    }
    
    /**
     * Get the actual field class mapped in Hibernate
     * @param field
     * @return
     */
    @NotNull
    private Class getActualFieldClass(Field field) {
        Class declaringClass = field.getDeclaringClass();
        if (declaringClass.equals(AmpActivityFields.class)) {
            return AmpActivityVersion.class;
        } else if (declaringClass.equals(ObjectReferringDocument.class)) {
            return AmpActivityDocument.class;
        }
        
        return declaringClass;
    }
}
