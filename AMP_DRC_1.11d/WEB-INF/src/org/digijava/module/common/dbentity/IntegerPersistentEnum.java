
package org.digijava.module.common.dbentity;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.NullableType;


/**
 * Provides a base class for persistable, type-safe, comparable,
 * and serializable enums persisted as integers.
 *
 * <p>Create a subclass of this class implementing the enumeration:
 * <pre>package com.foo;
 *
 * public final class Gender extends PersistentCharacterEnum {
 *  public static final Gender MALE = new Gender("male", 0);
 *  public static final Gender FEMALE = new Gender("female", 1);
 *  public static final Gender UNDETERMINED = new Gender("undetermined", 2);
 *
 *  public Gender() {}
 *
 *  private Gender(String name, int persistentValue) {
 *   super(name, persistentValue);
 *  }
 * }
 * </pre>
 * Note that a no-op default constructor must be provided.</p>
 *
 * <p>Use this enumeration in your mapping file as:
 * <pre>&lt;property name="gender" type="com.foo.Gender"&gt;</pre></p>
 *
 * <p><code>
 * $Id: IntegerPersistentEnum.java,v 1.1 2008-10-28 20:44:40 ddimunzio Exp $
 * </pre></p>
 *
 * @version $Revision: 1.1 $
 * @author &Oslash;rjan Nygaard Austvold
 */
public abstract class IntegerPersistentEnum extends PersistentEnum {
    /**
     * Default constructor.  Hibernate need the default constructor
     * to retrieve an instance of the enum from a JDBC resultset.
     * The instance will be converted to the correct enum instance
     * in {@link #nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)}.
     */
    protected IntegerPersistentEnum() {
        // no-op -- instance will be tossed away once the equivalent enum is found.
    }

    /**
     * Constructs an enum with given persistent integer value, name of this persistent
     * class is the string value of persistentInteger
     * @param persistentInteger
     */

    protected IntegerPersistentEnum(int persistentInteger) {
    	this(String.valueOf(persistentInteger), persistentInteger);
    }

    /**
     * Constructs an enum with the given name and persistent representation.
     *
     * @param name name of enum.
     * @param persistentInteger persistent representation of the enum.
     */
    protected IntegerPersistentEnum(String name, int persistentInteger) {
        super(name, new Integer(persistentInteger));
    }


    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object other) {
        if(other == this) {
            return 0;
        }
        if(other instanceof IntegerPersistentEnum) {
            Integer thisCode = (Integer) getEnumCode();
            Integer anotherCode = (Integer) ((PersistentEnum) other).
                getEnumCode();
            return thisCode.compareTo(anotherCode);
        } else {
            return this.getClass().getName().compareTo(other.getClass().getName());
        }
    }


    /**
     * @see PersistentEnum#getNullableType()
     */
    protected NullableType getNullableType() {
        return Hibernate.INTEGER;
    }
}
