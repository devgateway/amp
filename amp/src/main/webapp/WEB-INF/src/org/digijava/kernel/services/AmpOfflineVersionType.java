package org.digijava.kernel.services;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOfflineVersionType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[] {
                StringType.INSTANCE.sqlType()
        };
    }

    @Override
    public Class returnedClass() {
        return AmpOfflineVersion.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session,
            Object owner) throws HibernateException, SQLException {
        String v = (String) StringType.INSTANCE.get(rs, names[0], session);
        return v == null ? null : new AmpOfflineVersion(v);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
            SessionImplementor session) throws HibernateException, SQLException {
        StringType.INSTANCE.set(st, value == null ? null : value.toString(), index, session);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
