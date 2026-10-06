/**
 * CurrencyDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.digijava.module.currencyrates.fxtop;

public class CurrencyDescription  implements java.io.Serializable {
    private java.lang.String isocode;

    private java.lang.String lang;

    private java.lang.String countryDescription;

    private java.lang.String currencyLabel;

    private java.lang.String modeC;

    private java.lang.String nbDec;

    private java.lang.String comment;

    public CurrencyDescription() {
    }

    public CurrencyDescription(
           java.lang.String isocode,
           java.lang.String lang,
           java.lang.String countryDescription,
           java.lang.String currencyLabel,
           java.lang.String modeC,
           java.lang.String nbDec,
           java.lang.String comment) {
           this.isocode = isocode;
           this.lang = lang;
           this.countryDescription = countryDescription;
           this.currencyLabel = currencyLabel;
           this.modeC = modeC;
           this.nbDec = nbDec;
           this.comment = comment;
    }


    /**
     * Gets the isocode value for this CurrencyDescription.
     * 
     * @return isocode
     */
    public java.lang.String getIsocode() {
        return isocode;
    }


    /**
     * Sets the isocode value for this CurrencyDescription.
     * 
     * @param isocode
     */
    public void setIsocode(java.lang.String isocode) {
        this.isocode = isocode;
    }


    /**
     * Gets the lang value for this CurrencyDescription.
     * 
     * @return lang
     */
    public java.lang.String getLang() {
        return lang;
    }


    /**
     * Sets the lang value for this CurrencyDescription.
     * 
     * @param lang
     */
    public void setLang(java.lang.String lang) {
        this.lang = lang;
    }


    /**
     * Gets the countryDescription value for this CurrencyDescription.
     * 
     * @return countryDescription
     */
    public java.lang.String getCountryDescription() {
        return countryDescription;
    }


    /**
     * Sets the countryDescription value for this CurrencyDescription.
     * 
     * @param countryDescription
     */
    public void setCountryDescription(java.lang.String countryDescription) {
        this.countryDescription = countryDescription;
    }


    /**
     * Gets the currencyLabel value for this CurrencyDescription.
     * 
     * @return currencyLabel
     */
    public java.lang.String getCurrencyLabel() {
        return currencyLabel;
    }


    /**
     * Sets the currencyLabel value for this CurrencyDescription.
     * 
     * @param currencyLabel
     */
    public void setCurrencyLabel(java.lang.String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }


    /**
     * Gets the modeC value for this CurrencyDescription.
     * 
     * @return modeC
     */
    public java.lang.String getModeC() {
        return modeC;
    }


    /**
     * Sets the modeC value for this CurrencyDescription.
     * 
     * @param modeC
     */
    public void setModeC(java.lang.String modeC) {
        this.modeC = modeC;
    }


    /**
     * Gets the nbDec value for this CurrencyDescription.
     * 
     * @return nbDec
     */
    public java.lang.String getNbDec() {
        return nbDec;
    }


    /**
     * Sets the nbDec value for this CurrencyDescription.
     * 
     * @param nbDec
     */
    public void setNbDec(java.lang.String nbDec) {
        this.nbDec = nbDec;
    }


    /**
     * Gets the comment value for this CurrencyDescription.
     * 
     * @return comment
     */
    public java.lang.String getComment() {
        return comment;
    }


    /**
     * Sets the comment value for this CurrencyDescription.
     * 
     * @param comment
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CurrencyDescription)) return false;
        CurrencyDescription other = (CurrencyDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.isocode==null && other.getIsocode()==null) || 
             (this.isocode!=null &&
              this.isocode.equals(other.getIsocode()))) &&
            ((this.lang==null && other.getLang()==null) || 
             (this.lang!=null &&
              this.lang.equals(other.getLang()))) &&
            ((this.countryDescription==null && other.getCountryDescription()==null) || 
             (this.countryDescription!=null &&
              this.countryDescription.equals(other.getCountryDescription()))) &&
            ((this.currencyLabel==null && other.getCurrencyLabel()==null) || 
             (this.currencyLabel!=null &&
              this.currencyLabel.equals(other.getCurrencyLabel()))) &&
            ((this.modeC==null && other.getModeC()==null) || 
             (this.modeC!=null &&
              this.modeC.equals(other.getModeC()))) &&
            ((this.nbDec==null && other.getNbDec()==null) || 
             (this.nbDec!=null &&
              this.nbDec.equals(other.getNbDec()))) &&
            ((this.comment==null && other.getComment()==null) || 
             (this.comment!=null &&
              this.comment.equals(other.getComment())));
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
        if (getIsocode() != null) {
            _hashCode += getIsocode().hashCode();
        }
        if (getLang() != null) {
            _hashCode += getLang().hashCode();
        }
        if (getCountryDescription() != null) {
            _hashCode += getCountryDescription().hashCode();
        }
        if (getCurrencyLabel() != null) {
            _hashCode += getCurrencyLabel().hashCode();
        }
        if (getModeC() != null) {
            _hashCode += getModeC().hashCode();
        }
        if (getNbDec() != null) {
            _hashCode += getNbDec().hashCode();
        }
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CurrencyDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:FxtopAPI", "CurrencyDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isocode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Isocode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lang");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Lang"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("countryDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CountryDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currencyLabel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CurrencyLabel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modeC");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ModeC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nbDec");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NbDec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
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
