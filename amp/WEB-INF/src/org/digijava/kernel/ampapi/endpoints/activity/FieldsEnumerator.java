package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.h2.util.StringUtils;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;

/**
 * AMP Activity Endpoints for Activity Import / Export
 * 
 * @author acartaleanu
 */
public class FieldsEnumerator {
    
    public static final Logger LOGGER = Logger.getLogger(PossibleValuesEnumerator.class);
    public static Map<Field, String > fieldTypes;
    public static Map<Field, Integer> fieldMaxLengths;
    
    private boolean internalUse = false;
    private String iatiIdentifierField;
    private TranslationSettings trnSettings = TranslationSettings.getCurrent();
    
    static {
        fillAllFieldsLengthInformation();
    }
    
    /**
     * Fields Enumerator
     * 
     * @param internalUse flags if additional information for internal use is needed 
     */
    public FieldsEnumerator(boolean internalUse) {
        this.internalUse = internalUse;
        this.iatiIdentifierField = InterchangeUtils.getAmpIatiIdentifierFieldName();
    }
    
    /**
     * gets fields from the type of the field
     * 
     * @param field
     * @param multilingual true if multilingual enabled
     * @return a list of JsonBeans, each a description of @Interchangeable
     *         fields in the definition of the field's class, or field's generic
     *         type, if it's a collection
     */
    private List<JsonBean> getChildrenOfField(Field field, Deque<Interchangeable> intchStack) {
        if (!InterchangeUtils.isCollection(field))
            return getAllAvailableFields(field.getType(), intchStack);
        else
            return getAllAvailableFields(InterchangeUtils.getGenericClass(field), intchStack);
    }
    
    private static Map<String, Field> getInterchangeableFields(Class<?> clazz) {
        Map<String, Field> interFields = new HashMap<String, Field>();
        Class<?> wClass = clazz;
        while (wClass != Object.class) {
            Field[] declaredFields = wClass.getDeclaredFields();
            for (Field field : declaredFields)
                if (field.getAnnotation(Interchangeable.class) != null)
                    interFields.put(field.getName(), field);
            wClass = (Class<?>) wClass.getGenericSuperclass();
        }
        return interFields;
    }
    
    private static void fillFieldsLengthInformation(Class<?> clazz) {
        final Map <String, String> dbTypes = new HashMap<String, String>();
        final Map <String, Integer> maxLengths = new HashMap<String, Integer>();
        ClassMetadata meta = PersistenceManager.getClassMetadata(clazz);
        if (meta == null)
            return;
        AbstractEntityPersister entityPersister = (AbstractEntityPersister)meta;
        String[] propertyNames = entityPersister.getPropertyNames();
        final String tableName = entityPersister.getTableName();
        Map<String, Field> interchangeableFields = getInterchangeableFields(clazz);
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
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
            }
        });
        for (int i = 0; i < propertyNames.length; i++) {
            String[] columnNames = entityPersister.getPropertyColumnNames(i);
            if (columnNames.length > 0)
            {
                String colname = columnNames[0];
                String fieldName = propertyNames[i];
//              System.out.println(propertyNames[i] + "->" + colname + "(" + dbTypes.get(colname)+  "/" + maxLengths.get(colname) +")");
                if (interchangeableFields.get(fieldName) != null) {
                    Field field = interchangeableFields.get(fieldName);
                    fieldTypes.put(field, dbTypes.get(colname)); //maxLengths.get(colname)
                    fieldMaxLengths.put(field, maxLengths.get(colname));
                }
            }
        }
        for (Field field : interchangeableFields.values()) {
            Interchangeable ant = field.getAnnotation(Interchangeable.class);
            if (ant != null && !ant.pickIdOnly() && !InterchangeUtils.isSimpleType(field.getType()))
                fillFieldsLengthInformation(InterchangeUtils.getClassOfField(field));

        }
    }
    
    /**
     * fills the fieldTypes and fieldMaxLengths maps
     */
    private static void fillAllFieldsLengthInformation(){
        if (fieldTypes == null) {
            fieldTypes = new HashMap<Field, String>();
            fieldMaxLengths = new HashMap<Field, Integer>();
            fillFieldsLengthInformation(AmpActivityVersion.class);
        }
    }
    
    /**
     * describes a field in a complex JSON structure
     * see the wiki for details, too many options to be listed here
     * 
     * @param field
     * @return
     */
    private  JsonBean describeField(Field field, Deque<Interchangeable> intchStack, 
            InterchangeableDiscriminator discriminator) {
        Interchangeable interchangeable = intchStack.peek();
        if (interchangeable == null)
            return null;
        
        JsonBean bean = new JsonBean();
        String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());
        bean.set(ActivityEPConstants.FIELD_NAME, fieldTitle);
        if (interchangeable.id()) {
            bean.set(ActivityEPConstants.ID, interchangeable.id());
        }
        
        if (interchangeable.pickIdOnly()) {
            bean.set(ActivityEPConstants.FIELD_TYPE, InterchangeableClassMapper.getCustomMapping(java.lang.Long.class));
        } else {

            Class<?> fieldType = field.getType();
            bean.set(ActivityEPConstants.FIELD_TYPE, InterchangeableClassMapper.containsSimpleClass(fieldType)? 
                    InterchangeableClassMapper.getCustomMapping(fieldType) : ActivityEPConstants.FIELD_TYPE_LIST);

        }
        
        bean.set(ActivityEPConstants.FIELD_LABEL, InterchangeUtils.mapToBean(getLabelsForField(interchangeable.fieldTitle())));
        bean.set(ActivityEPConstants.REQUIRED, InterchangeUtils.getRequiredValue(field, intchStack));
        bean.set(ActivityEPConstants.IMPORTABLE, interchangeable.importable());
        
        if (isFieldIatiIdentifier(fieldTitle)) {
            bean.set(ActivityEPConstants.REQUIRED, ActivityEPConstants.FIELD_ALWAYS_REQUIRED);
            bean.set(ActivityEPConstants.IMPORTABLE, true);
        }
        
        if (interchangeable.percentageConstraint()){
            bean.set(ActivityEPConstants.PERCENTAGE, true);
        }
        List<String> actualDependencies = InterchangeDependencyResolver.getActualDependencies(interchangeable.dependencies());
        if (actualDependencies != null) {
            bean.set(ActivityEPConstants.DEPENDENCIES, actualDependencies);
        }
        
        if (internalUse) {
            bean.set(ActivityEPConstants.FIELD_NAME_INTERNAL, field.getName());
            if (InterchangeUtils.isAmpActivityVersion(field.getType())) {
                bean.set(ActivityEPConstants.ACTIVITY, true);
            }
        }
        
        /* list type */
        
        if (interchangeable.pickIdOnly()) {
            bean.set(ActivityEPConstants.ID_ONLY, true);
        }
        
        if (!InterchangeUtils.isSimpleType(field.getType())) {
            if (InterchangeUtils.isCollection(field)) {
                if(!InterchangeUtils.hasMaxSizeValidatorEnabled(field, intchStack) && interchangeable.multipleValues()) {
                    bean.set(ActivityEPConstants.MULTIPLE_VALUES, true);
                } else {
                    bean.set(ActivityEPConstants.MULTIPLE_VALUES, false);
                }
                
                if (InterchangeUtils.hasPercentageValidatorEnabled(field, intchStack)) {
                    bean.set(ActivityEPConstants.PERCENTAGE_CONSTRAINT, getPercentageConstraint(field, intchStack));
                }
                
                if (InterchangeUtils.hasUniqueValidatorEnabled(field, intchStack)) {
                    bean.set(ActivityEPConstants.UNIQUE_CONSTRAINT, getUniqueConstraint(field, intchStack));
                }
                
                if (InterchangeUtils.hasTreeCollectionValidatorEnabled(field, intchStack)) {
                    bean.set(ActivityEPConstants.TREE_COLLECTION_CONSTRAINT, true);
                }
            }
            
            if (!interchangeable.pickIdOnly() && !InterchangeUtils.isAmpActivityVersion(field.getClass())) {
                List<JsonBean> children = getChildrenOfField(field, intchStack);
                if (children != null && children.size() > 0) {
                    bean.set(ActivityEPConstants.CHILDREN, children);
                }
            }
        }
        
        // only String fields should clarify if they are translatable or not
        if (java.lang.String.class.equals(field.getType())) {
            bean.set(ActivityEPConstants.TRANSLATABLE, trnSettings.isTranslatable(field));
        }
        if (ActivityEPConstants.TYPE_VARCHAR.equals(fieldTypes.get(field)) && fieldMaxLengths.get(field) != null) {
            bean.set(ActivityEPConstants.FIELD_LENGTH, fieldMaxLengths.get(field));
            LOGGER.debug(interchangeable.fieldTitle());
        }
        return bean;
    }
    
    /**
     * @see #getAllAvailableFields(boolean)
     */
    public static List<JsonBean> getAllAvailableFields() {
        return getAllAvailableFields(false);
    }
    
    /**
     * Retrieves the list of available fields, their description within a hierarchical structure 
     * @param internalUse flags that additional info is needed for internal processing
     * @return the list of available fields
     */
    public static List<JsonBean> getAllAvailableFields(boolean internalUse) {
        return (new FieldsEnumerator(internalUse)).getAllAvailableFields(AmpActivityFields.class, 
                new ArrayDeque<Interchangeable>());
    }

    /**
     * Describes each @Interchangeable field of a class
     * 
     * @param clazz the class to be described
     * @return
     */
    private List<JsonBean> getAllAvailableFields(Class<?> clazz, Deque<Interchangeable> intchStack) {
        List<JsonBean> result = new ArrayList<JsonBean>();
        //StopWatch.next("Descending into", false, clazz.getName());
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
            if (interchangeable == null || !internalUse && InterchangeUtils.isAmpActivityVersion(field.getType())) {
                continue;
            }
            intchStack.push(interchangeable);
            if (!InterchangeUtils.isCompositeField(field) || hasFieldDiscriminatorClass(field)) {
                InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
                
                if (FMVisibility.isVisible(interchangeable.fmPath(), intchStack)) {
                    JsonBean descr = describeField(field, intchStack, discriminator);
                    if (descr != null) {
                        result.add(descr);
                    }
                }
            } else {
                InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
                Interchangeable[] settings = discriminator.settings();
                for (int i = 0; i < settings.length; i++) {
                    String fmPath = settings[i].fmPath();
                    if (FMVisibility.isVisible(fmPath, intchStack)) {
                        intchStack.push(settings[i]);
                        JsonBean descr = describeField(field, intchStack, discriminator);
                        if (descr != null) {
                            result.add(descr);
                        }
                        intchStack.pop();
                    }
                }
            }
            intchStack.pop();
        }
        return result;
    }
    
    private static boolean hasFieldDiscriminatorClass(Field field) {
        try {
            return InterchangeUtils.getDiscriminatorClass(field) != null;
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Picks available translations for a string (supposedly field name)
     * 
     * @param fieldName the field name to be translated
     * @return a map from the ISO2 code -> translation in said text
     */
    private static Map<String, String> getLabelsForField(String fieldName) {
        Map<String, String> translations = new HashMap<String, String>();
        try {
            Collection<Message> messages = TranslatorWorker.getAllTranslationOfBody(fieldName, Long.valueOf(3));
            for (Message m : messages) {
                translations.put(m.getLocale(), m.getMessage());
            }
            if (translations.isEmpty()) {
                translations.put("EN", fieldName);
            }
        } catch (WorkerException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return translations;
    }
    
    /**
     * 
     * @param parentInterchangeable 
     * 
     * @param clazz the class to be described
     * @return
     */
    private String getPercentageConstraint(Field field, Deque<Interchangeable> intchStack) {
        Class<?> genericClass = InterchangeUtils.getGenericClass(field);
        Field[] fields = genericClass.getDeclaredFields();
        for (Field f : fields) {
            Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
            if (interchangeable != null && FMVisibility.isVisible(interchangeable.fmPath(), intchStack) 
                    && interchangeable.percentageConstraint()) {
                return InterchangeUtils.underscorify(interchangeable.fieldTitle());
            }
        }
        
        return null;
    }
    
    /**
     * Describes each @Interchangeable field of a class
     * @param parentInterchangeable 
     * 
     * @param clazz the class to be described
     * @return
     */
    private String getUniqueConstraint(Field field, Deque<Interchangeable> intchStack) {
        Class<?> genericClass = InterchangeUtils.getGenericClass(field);
        Field[] fields = genericClass.getDeclaredFields();
        for (Field f : fields) {
            Interchangeable interchangeable = f.getAnnotation(Interchangeable.class);
            if (interchangeable != null && FMVisibility.isVisible(interchangeable.fmPath(), intchStack) 
                    && interchangeable.uniqueConstraint()) {
                return InterchangeUtils.underscorify(interchangeable.fieldTitle());
            }
        }
        
        return null;
    }
    
    /**
     * Decides whether a field stores iati-identifier value
     *  
     * @param fieldName
     * @return true if is iati-identifier
     */
    private boolean isFieldIatiIdentifier(String fieldName) {
        return StringUtils.equals(this.iatiIdentifierField, fieldName);
    }
    
}
