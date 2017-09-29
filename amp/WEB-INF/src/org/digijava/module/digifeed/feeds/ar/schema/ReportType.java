//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.20 at 12:51:52 PM IST 
//


package org.digijava.module.digifeed.feeds.ar.schema;


/**
 * Java content class for reportType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/mihai/programs/jwsdp-1.6/jaxb/bin/schema.xsd line 26)
 * <p>
 * <pre>
 * &lt;complexType name="reportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="column" type="{http://digijava.org/module/digifeed/feeds/ar/schema.xml}columnType" maxOccurs="unbounded"/>
 *         &lt;element name="hierarchy" type="{http://digijava.org/module/digifeed/feeds/ar/schema.xml}columnType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="measure" type="{http://digijava.org/module/digifeed/feeds/ar/schema.xml}measureType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="hideActivities" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="options" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ReportType {


    /**
     * Gets the value of the Measure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Measure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeasure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.digijava.module.digifeed.feeds.ar.schema.MeasureType}
     * 
     */
    java.util.List getMeasure();

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getType();

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setType(java.math.BigInteger value);

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
     * Gets the value of the Hierarchy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Hierarchy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHierarchy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.digijava.module.digifeed.feeds.ar.schema.ColumnType}
     * 
     */
    java.util.List getHierarchy();

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOptions();

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOptions(java.lang.String value);

    /**
     * Gets the value of the Column property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Column property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.digijava.module.digifeed.feeds.ar.schema.ColumnType}
     * 
     */
    java.util.List getColumn();

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setName(java.lang.String value);

    /**
     * Gets the value of the hideActivities property.
     * 
     */
    boolean isHideActivities();

    /**
     * Sets the value of the hideActivities property.
     * 
     */
    void setHideActivities(boolean value);

    /**
     * Gets the value of the id property.
     * 
     */
    long getId();

    /**
     * Sets the value of the id property.
     * 
     */
    void setId(long value);

}
