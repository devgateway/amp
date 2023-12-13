//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.12.13 at 05:07:15 PM EAT 
//


package org.digijava.module.xmlpatcher.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for triggerType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="triggerType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="all"/&gt;
 *     &lt;enumeration value="any"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "triggerType")
@XmlEnum
public enum TriggerType {

    @XmlEnumValue("all")
    ALL("all"),
    @XmlEnumValue("any")
    ANY("any");
    private final String value;

    TriggerType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TriggerType fromValue(String v) {
        for (TriggerType c: TriggerType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
