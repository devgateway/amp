//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.04 at 11:30:59 PM EEST 
//


package org.dgfoundation.amp.visibility.feed.fm.schema;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.dgfoundation.amp.visibility.feed.fm.schema package. 
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
    extends org.dgfoundation.amp.visibility.feed.fm.schema.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(16, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static org.dgfoundation.amp.visibility.feed.fm.schema.impl.runtime.GrammarInfo grammarInfo = new org.dgfoundation.amp.visibility.feed.fm.schema.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (org.dgfoundation.amp.visibility.feed.fm.schema.ObjectFactory.class));
    public final static java.lang.Class version = (org.dgfoundation.amp.visibility.feed.fm.schema.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((org.dgfoundation.amp.visibility.feed.fm.schema.TemplateType.class), "org.dgfoundation.amp.visibility.feed.fm.schema.impl.TemplateTypeImpl");
        defaultImplementations.put((org.dgfoundation.amp.visibility.feed.fm.schema.FieldType.class), "org.dgfoundation.amp.visibility.feed.fm.schema.impl.FieldTypeImpl");
        defaultImplementations.put((org.dgfoundation.amp.visibility.feed.fm.schema.VisibilityTemplatesType.class), "org.dgfoundation.amp.visibility.feed.fm.schema.impl.VisibilityTemplatesTypeImpl");
        defaultImplementations.put((org.dgfoundation.amp.visibility.feed.fm.schema.FeatureType.class), "org.dgfoundation.amp.visibility.feed.fm.schema.impl.FeatureTypeImpl");
        defaultImplementations.put((org.dgfoundation.amp.visibility.feed.fm.schema.VisibilityTemplates.class), "org.dgfoundation.amp.visibility.feed.fm.schema.impl.VisibilityTemplatesImpl");
        defaultImplementations.put((org.dgfoundation.amp.visibility.feed.fm.schema.ModuleType.class), "org.dgfoundation.amp.visibility.feed.fm.schema.impl.ModuleTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://dgfoundation.org/amp/visibility/feed/fm/schema.xml", "visibilityTemplates"), (org.dgfoundation.amp.visibility.feed.fm.schema.VisibilityTemplates.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.dgfoundation.amp.visibility.feed.fm.schema
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
     * Create an instance of TemplateType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.dgfoundation.amp.visibility.feed.fm.schema.TemplateType createTemplateType()
        throws javax.xml.bind.JAXBException
    {
        return new org.dgfoundation.amp.visibility.feed.fm.schema.impl.TemplateTypeImpl();
    }

    /**
     * Create an instance of FieldType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.dgfoundation.amp.visibility.feed.fm.schema.FieldType createFieldType()
        throws javax.xml.bind.JAXBException
    {
        return new org.dgfoundation.amp.visibility.feed.fm.schema.impl.FieldTypeImpl();
    }

    /**
     * Create an instance of VisibilityTemplatesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.dgfoundation.amp.visibility.feed.fm.schema.VisibilityTemplatesType createVisibilityTemplatesType()
        throws javax.xml.bind.JAXBException
    {
        return new org.dgfoundation.amp.visibility.feed.fm.schema.impl.VisibilityTemplatesTypeImpl();
    }

    /**
     * Create an instance of FeatureType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.dgfoundation.amp.visibility.feed.fm.schema.FeatureType createFeatureType()
        throws javax.xml.bind.JAXBException
    {
        return new org.dgfoundation.amp.visibility.feed.fm.schema.impl.FeatureTypeImpl();
    }

    /**
     * Create an instance of VisibilityTemplates
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.dgfoundation.amp.visibility.feed.fm.schema.VisibilityTemplates createVisibilityTemplates()
        throws javax.xml.bind.JAXBException
    {
        return new org.dgfoundation.amp.visibility.feed.fm.schema.impl.VisibilityTemplatesImpl();
    }

    /**
     * Create an instance of ModuleType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.dgfoundation.amp.visibility.feed.fm.schema.ModuleType createModuleType()
        throws javax.xml.bind.JAXBException
    {
        return new org.dgfoundation.amp.visibility.feed.fm.schema.impl.ModuleTypeImpl();
    }

}
