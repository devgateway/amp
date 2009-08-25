/**
 * UserScenes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.dgfoundation.amp.ecs.common;

public class UserScenes  implements java.io.Serializable {
    private java.lang.Object[] scenes;

    private org.dgfoundation.amp.ecs.common.ErrorUser user;

    public UserScenes() {
    }

    public UserScenes(
           java.lang.Object[] scenes,
           org.dgfoundation.amp.ecs.common.ErrorUser user) {
           this.scenes = scenes;
           this.user = user;
    }


    /**
     * Gets the scenes value for this UserScenes.
     * 
     * @return scenes
     */
    public java.lang.Object[] getScenes() {
        return scenes;
    }


    /**
     * Sets the scenes value for this UserScenes.
     * 
     * @param scenes
     */
    public void setScenes(java.lang.Object[] scenes) {
        this.scenes = scenes;
    }


    /**
     * Gets the user value for this UserScenes.
     * 
     * @return user
     */
    public org.dgfoundation.amp.ecs.common.ErrorUser getUser() {
        return user;
    }


    /**
     * Sets the user value for this UserScenes.
     * 
     * @param user
     */
    public void setUser(org.dgfoundation.amp.ecs.common.ErrorUser user) {
        this.user = user;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserScenes)) return false;
        UserScenes other = (UserScenes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.scenes==null && other.getScenes()==null) || 
             (this.scenes!=null &&
              java.util.Arrays.equals(this.scenes, other.getScenes()))) &&
            ((this.user==null && other.getUser()==null) || 
             (this.user!=null &&
              this.user.equals(other.getUser())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getScenes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getScenes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getScenes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUser() != null) {
            _hashCode += getUser().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserScenes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:org.dgfoundation.amp.ecs", "UserScenes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scenes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "scenes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:org.dgfoundation.amp.ecs", "ErrorUser"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
