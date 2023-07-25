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
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.type.EnumType;

/**
 * @author Octavian Ciubotaru
 */
public class NamedEnumType extends EnumType {

    private BiMap<Enum, String> values = HashBiMap.create();

    @Override
    public void setParameterValues(Properties parameters) {
        parameters.setProperty(NAMED, "true");

        String valueProperty = parameters.getProperty("valueProperty");

        String enumClassName = parameters.getProperty(ENUM);
        Class<? extends Enum> enumClass;
        try {
            enumClass = ReflectHelper.classForName(enumClassName, this.getClass()).asSubclass(Enum.class);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Enum class not found", e);
        }
        Method getterMethod = ReflectHelper.findGetterMethod(enumClass, valueProperty);
        Getter getter = new GetterMethodImpl(enumClass, valueProperty, getterMethod);

        for (Enum enumConstant : enumClass.getEnumConstants()) {
            values.put(enumConstant, (String) getter.get(enumConstant));
        }
        super.setParameterValues(parameters);
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session,
                              Object owner) throws SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull() || value == null) {
            return null;
        } else {
            return values.inverse().get(value);
        }
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SessionImplementor session) throws HibernateException, SQLException {
        String jdbcValue = value != null ? values.get((Enum) value) : null;
        if (jdbcValue == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, jdbcValue);
        }
    }
}