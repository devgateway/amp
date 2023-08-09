package org.digijava.module.aim.dbentity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class NamedEnumType extends EnumType {
    private final Logger logger = LoggerFactory.getLogger(NamedEnumType.class);
    private BiMap<Enum, String> values = HashBiMap.create();

    @Override
    public void setParameterValues(Properties parameters) {
        parameters.setProperty(NAMED, "true");

        String valueProperty = parameters.getProperty("valueProperty");

        String enumClassName = parameters.getProperty(ENUM);
        Class<? extends Enum> enumClass;
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Enum class not found", e);
        }

        String getterMethodName = "get" + valueProperty.substring(0, 1).toUpperCase() + valueProperty.substring(1);
        Method getter;
        try {
            getter = enumClass.getMethod(getterMethodName);
            logger.info("Getter is: "+getter.getName());
        } catch (NoSuchMethodException e) {
            throw new HibernateException("Getter method not found for enum value", e);
        }
        for (Enum enumConstant : enumClass.getEnumConstants()) {
            try {
                Object value = getter.invoke(enumConstant);
                values.put(enumConstant, value.toString());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new HibernateException("Error invoking getter method", e);
            }
        }
        super.setParameterValues(parameters);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull() || value == null) {
            return null;
        } else {
            return values.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(value))
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        String jdbcValue = value != null ? values.get(value) : null;
        if (jdbcValue == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, jdbcValue);
        }
    }
}
