//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.08.14 at 06:44:34 PM EEST 
//


package org.digijava.module.autopatcher.schema;


/**
 * Java content class for patchType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/mihai/programs/jwsdp-1.6/jaxb/bin/autopatcher.xsd line 11)
 * <p>
 * <pre>
 * &lt;complexType name="patchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dependencies" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keywords" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="databaseTargets" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sql" type="{http://digijava.org/module/autopatcher/schema.xml}sqlType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="author" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="creationDate" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="featureAdded" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="keyName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="reApplicable" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="retryIfFailure" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PatchType {


    /**
     * Gets the value of the databaseTargets property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDatabaseTargets();

    /**
     * Sets the value of the databaseTargets property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDatabaseTargets(java.lang.String value);

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDescription();

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDescription(java.lang.String value);

    /**
     * Gets the value of the dependencies property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDependencies();

    /**
     * Sets the value of the dependencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDependencies(java.lang.String value);

    /**
     * Gets the value of the reApplicable property.
     * 
     */
    boolean isReApplicable();

    /**
     * Sets the value of the reApplicable property.
     * 
     */
    void setReApplicable(boolean value);

    /**
     * Gets the value of the keywords property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getKeywords();

    /**
     * Sets the value of the keywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setKeywords(java.lang.String value);

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuthor();

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuthor(java.lang.String value);

    /**
     * Gets the value of the retryIfFailure property.
     * 
     */
    boolean isRetryIfFailure();

    /**
     * Sets the value of the retryIfFailure property.
     * 
     */
    void setRetryIfFailure(boolean value);

    /**
     * Gets the value of the Sql property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Sql property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSql().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.digijava.module.autopatcher.schema.SqlType}
     * 
     */
    java.util.List getSql();

    /**
     * Gets the value of the keyName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getKeyName();

    /**
     * Sets the value of the keyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setKeyName(java.lang.String value);

    /**
     * Gets the value of the featureAdded property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFeatureAdded();

    /**
     * Sets the value of the featureAdded property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFeatureAdded(java.lang.String value);

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getCreationDate();

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setCreationDate(java.util.Calendar value);

}
