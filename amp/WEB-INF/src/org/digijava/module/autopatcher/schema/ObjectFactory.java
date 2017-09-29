//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.08.14 at 06:44:34 PM EEST 
//


package org.digijava.module.autopatcher.schema;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.digijava.module.autopatcher.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
public class ObjectFactory
    extends org.digijava.module.autopatcher.schema.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(16, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static org.digijava.module.autopatcher.schema.impl.runtime.GrammarInfo grammarInfo = new org.digijava.module.autopatcher.schema.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (org.digijava.module.autopatcher.schema.ObjectFactory.class));
    public final static java.lang.Class version = (org.digijava.module.autopatcher.schema.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((org.digijava.module.autopatcher.schema.SqlType.class), "org.digijava.module.autopatcher.schema.impl.SqlTypeImpl");
        defaultImplementations.put((org.digijava.module.autopatcher.schema.PatchType.class), "org.digijava.module.autopatcher.schema.impl.PatchTypeImpl");
        defaultImplementations.put((org.digijava.module.autopatcher.schema.Patch.class), "org.digijava.module.autopatcher.schema.impl.PatchImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://digijava.org/module/autopatcher/schema.xml", "patch"), (org.digijava.module.autopatcher.schema.Patch.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.digijava.module.autopatcher.schema
     * 
     */
    public ObjectFactory() {
        super(grammarInfo);
    }

    /**
     * Create an instance of the specified Java content interface.
     * 
     * @param javaContentInterface
     *     the Class object of the javacontent interface to instantiate
     * @return
     *     a new instance
     * @throws JAXBException
     *     if an error occurs
     */
    public java.lang.Object newInstance(java.lang.Class javaContentInterface)
        throws javax.xml.bind.JAXBException
    {
        return super.newInstance(javaContentInterface);
    }

    /**
     * Get the specified property. This method can only be
     * used to get provider specific properties.
     * Attempting to get an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param name
     *     the name of the property to retrieve
     * @return
     *     the value of the requested property
     * @throws PropertyException
     *     when there is an error retrieving the given property or value
     */
    public java.lang.Object getProperty(java.lang.String name)
        throws javax.xml.bind.PropertyException
    {
        return super.getProperty(name);
    }

    /**
     * Set the specified property. This method can only be
     * used to set provider specific properties.
     * Attempting to set an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param value
     *     the value of the property to be set
     * @param name
     *     the name of the property to retrieve
     * @throws PropertyException
     *     when there is an error processing the given property or value
     */
    public void setProperty(java.lang.String name, java.lang.Object value)
        throws javax.xml.bind.PropertyException
    {
        super.setProperty(name, value);
    }

    /**
     * Create an instance of SqlType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.digijava.module.autopatcher.schema.SqlType createSqlType()
        throws javax.xml.bind.JAXBException
    {
        return new org.digijava.module.autopatcher.schema.impl.SqlTypeImpl();
    }

    /**
     * Create an instance of PatchType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.digijava.module.autopatcher.schema.PatchType createPatchType()
        throws javax.xml.bind.JAXBException
    {
        return new org.digijava.module.autopatcher.schema.impl.PatchTypeImpl();
    }

    /**
     * Create an instance of Patch
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.digijava.module.autopatcher.schema.Patch createPatch()
        throws javax.xml.bind.JAXBException
    {
        return new org.digijava.module.autopatcher.schema.impl.PatchImpl();
    }

}
