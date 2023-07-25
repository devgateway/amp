package org.digijava.module.common.dbentity;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * The class persists enumerations as integer values.
 * 
 * @author Catalin Andrei
 * @since Oct 1, 2008
 */
public class IntegerPersistentEnum implements UserType, ParameterizedType{

    private static final int[] SQL_TYPES = new int[]{Types.INTEGER};
    private Class targetClass;

    /**
     * @see org.hibernate.usertype.ParameterizedType#setParameterValues(java.util.Properties)
     */
    public void setParameterValues(Properties parameters) {
        String targetClassName = parameters.getProperty("targetClass");
        try {
            targetClass = Class.forName(targetClassName);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Class " + targetClassName + " not found ", e);
        }
    }

    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    /**
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class returnedClass() {
        return targetClass;
    }

    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        return (x == y);
    }

    /**
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }



    /**
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    /**
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable() {
        return false;
    }

    /**
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    /**
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
     */
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    /**
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

//    @Override
//    public Object nullSafeGet(ResultSet rs, String[] names,
//            SessionImplementor session, Object owner)
//            throws HibernateException, SQLException {
//        int value = rs.getInt(names[0]);
//        return rs.wasNull() ? null : Enum.get(value);
//    }

//    @Override
//    public void nullSafeSet(PreparedStatement st, Object value, int index,
//            SessionImplementor session) throws HibernateException, SQLException {
//        if (value == null) {
//            st.setNull(index, Types.INTEGER);
//        } else {
//            st.setInt(index, ((Enum)value).getValue());
//        }
//
//    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        int value = resultSet.getInt(strings[0]);
        return resultSet.wasNull() ? null : Enum.get(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o == null) {
            preparedStatement.setNull(i, Types.INTEGER);
        } else {
            preparedStatement.setInt(i, ((Enum)o).getValue());
        }
    }

}
