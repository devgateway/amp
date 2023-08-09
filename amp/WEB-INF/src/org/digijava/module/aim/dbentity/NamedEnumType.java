package org.digijava.module.aim.dbentity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;
import org.hibernate.usertype.ParameterizedType;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class NamedEnumType extends EnumType implements ParameterizedType {

    private Class<? extends Enum> enumClass;
    private Method getter;
    private Method inverseGetter;

    @Override
    public void setParameterValues(Properties parameters) {
        super.setParameterValues(parameters);

        String enumClassName = parameters.getProperty(ENUM);
        String valueProperty = parameters.getProperty("valueProperty");

        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
            getter = enumClass.getMethod(valueProperty);
            inverseGetter = enumClass.getMethod("valueOf", String.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new HibernateException("Enum class or getter not found", e);
        }
    }

    @Override
    public Object nullSafeGet(
            ResultSet rs,
            String[] names,
            SharedSessionContractImplementor session,
            Object owner
    ) throws SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull() || value == null) {
            return null;
        } else {
            try {
                return inverseGetter.invoke(enumClass, value);
            } catch (Exception e) {
                throw new HibernateException("Error invoking inverse getter", e);
            }
        }
    }

    @Override
    public void nullSafeSet(
            PreparedStatement st,
            Object value,
            int index,
            SharedSessionContractImplementor session
    ) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            try {
                String jdbcValue = (String) getter.invoke(value);
                st.setString(index, jdbcValue);
            } catch (Exception e) {
                throw new HibernateException("Error invoking getter", e);
            }
        }
    }
}
