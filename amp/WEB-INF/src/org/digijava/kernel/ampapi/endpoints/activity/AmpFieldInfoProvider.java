package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;

/**
 * @author Octavian Ciubotaru
 */
public class AmpFieldInfoProvider implements FieldInfoProvider {

    private Class<?> rootClass;

    private final Object lock = new Object();

    private Map<Field, String> fieldTypes;
    private Map<Field, Integer> fieldMaxLengths;

    public AmpFieldInfoProvider(Class<?> rootClass) {
        this.rootClass = rootClass;
    }

    public Integer getMaxLength(Field field) {
        initializeIfNeeded();
        return fieldMaxLengths.get(field);
    }

    @Override
    public boolean isTranslatable(Field field) {
        return TranslationSettings.getCurrent().isTranslatable(field);
    }

    public String getType(Field field) {
        initializeIfNeeded();
        return fieldTypes.get(field);
    }

    private void initializeIfNeeded() {
        synchronized (lock) {
            if (fieldTypes == null) {
                initialize();
            }
        }
    }

    public void initialize() {
        fieldTypes = new HashMap<>();
        fieldMaxLengths = new HashMap<>();
        fillFieldsLengthInformation(rootClass);
    }

    private void fillFieldsLengthInformation(Class<?> clazz) {
        final Map<String, String> dbTypes = new HashMap<String, String>();
        final Map<String, Integer> maxLengths = new HashMap<String, Integer>();
        ClassMetadata meta = PersistenceManager.getClassMetadata(clazz);
        if (meta == null) {
            return;
        }
        AbstractEntityPersister entityPersister = (AbstractEntityPersister) meta;
        String[] propertyNames = entityPersister.getPropertyNames();
        final String tableName = entityPersister.getTableName().toLowerCase();
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
            if (columnNames.length > 0) {
                String colname = columnNames[0];
                String fieldName = propertyNames[i];
                if (interchangeableFields.get(fieldName) != null) {
                    Field field = interchangeableFields.get(fieldName);
                    fieldTypes.put(field, dbTypes.get(colname)); //maxLengths.get(colname)
                    if (!field.isAnnotationPresent(VersionableFieldTextEditor.class)) {
                        fieldMaxLengths.put(field, maxLengths.get(colname));
                    }
                }
            }
        }
        for (Field field : interchangeableFields.values()) {
            Interchangeable ant = field.getAnnotation(Interchangeable.class);
            if (ant != null && !ant.pickIdOnly() && !InterchangeUtils.isSimpleType(field.getType())) {
                fillFieldsLengthInformation(InterchangeUtils.getClassOfField(field));
            }
        }
    }

    private Map<String, Field> getInterchangeableFields(Class<?> clazz) {
        Map<String, Field> interFields = new HashMap<String, Field>();
        Class<?> wClass = clazz;
        while (wClass != Object.class) {
            Field[] declaredFields = wClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getAnnotation(Interchangeable.class) != null) {
                    interFields.put(field.getName(), field);
                }
            }
            wClass = (Class<?>) wClass.getGenericSuperclass();
        }
        return interFields;
    }
}
