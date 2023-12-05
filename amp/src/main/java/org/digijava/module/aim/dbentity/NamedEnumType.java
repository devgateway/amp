package org.digijava.module.aim.dbentity;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.type.EnumType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Octavian Ciubotaru
 */
public class NamedEnumType extends EnumType {
    private final Logger logger = LoggerFactory.getLogger(NamedEnumType.class);

    private BiMap<Enum, String> values = HashBiMap.create();

    @Override
    public void setParameterValues(Properties parameters) {
        parameters.setProperty(NAMED, "true");
        parameters.setProperty(TYPE,String.valueOf(Types.VARCHAR) );

        String valueProperty = parameters.getProperty("valueProperty");

        String enumClassName = parameters.getProperty(ENUM);
        Class<? extends Enum> enumClass;
        try {
            enumClass = ReflectHelper.classForName(enumClassName, this.getClass());
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Enum class not found", e);
        }
        Method getterMethod = ReflectHelper.getterMethodOrNull(enumClass, valueProperty);
        Getter getter = new GetterMethodImpl(enumClass, valueProperty, getterMethod);
        logger.info("Getter is: "+getter.getMethodName());

        for (Enum enumConstant : enumClass.getEnumConstants()) {
            values.put(enumConstant, (String) getter.get(enumConstant));
        }
        parameters.put(EnumType.ENUM, enumClass.getName());
        super.setParameterValues(parameters);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String value = StringType.INSTANCE.nullSafeGet(rs, names[0], session);
        if (value == null || rs.wasNull()) {
            return null;
        } else {
            return values.inverse().get(value);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
        String jdbcValue = value != null ? values.get((Enum) value) : null;
        StringType.INSTANCE.nullSafeSet(st, jdbcValue, index, session);
        if (jdbcValue == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, jdbcValue);
        }
    }
}