/**
 * CurrencyConvertorLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.digijava.module.currencyrates.NET.webserviceX.www;

public class CurrencyConvertorLocator extends org.apache.axis.client.Service implements org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertor {

    public CurrencyConvertorLocator() {
    }


    public CurrencyConvertorLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CurrencyConvertorLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CurrencyConvertorSoap
    private java.lang.String CurrencyConvertorSoap_address = "http://www.webservicex.net/CurrencyConvertor.asmx";

    public java.lang.String getCurrencyConvertorSoapAddress() {
        return CurrencyConvertorSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CurrencyConvertorSoapWSDDServiceName = "CurrencyConvertorSoap";

    public java.lang.String getCurrencyConvertorSoapWSDDServiceName() {
        return CurrencyConvertorSoapWSDDServiceName;
    }

    public void setCurrencyConvertorSoapWSDDServiceName(java.lang.String name) {
        CurrencyConvertorSoapWSDDServiceName = name;
    }

    public org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap getCurrencyConvertorSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CurrencyConvertorSoap_address);
            
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCurrencyConvertorSoap(endpoint);
    }

    public org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap getCurrencyConvertorSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoapStub _stub = new org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoapStub(portAddress, this);
            _stub.setPortName(getCurrencyConvertorSoapWSDDServiceName());
            _stub.setTimeout(4 * 1000 * 60);
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCurrencyConvertorSoapEndpointAddress(java.lang.String address) {
        CurrencyConvertorSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoapStub _stub = new org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoapStub(new java.net.URL(CurrencyConvertorSoap_address), this);
                _stub.setPortName(getCurrencyConvertorSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CurrencyConvertorSoap".equals(inputPortName)) {
            return getCurrencyConvertorSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.webserviceX.NET/", "CurrencyConvertor");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.webserviceX.NET/", "CurrencyConvertorSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CurrencyConvertorSoap".equals(portName)) {
            setCurrencyConvertorSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
