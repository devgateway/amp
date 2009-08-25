/**
 * ECSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.dgfoundation.amp.ecs.common;

public class ECSServiceLocator extends org.apache.axis.client.Service implements org.dgfoundation.amp.ecs.common.ECSService {

    public ECSServiceLocator() {
    }


    public ECSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ECSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ECS
    private java.lang.String ECS_address = "http://ecs.ampdev.net/axis/services/ECS";

    public java.lang.String getECSAddress() {
        return ECS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ECSWSDDServiceName = "ECS";

    public java.lang.String getECSWSDDServiceName() {
        return ECSWSDDServiceName;
    }

    public void setECSWSDDServiceName(java.lang.String name) {
        ECSWSDDServiceName = name;
    }

    public org.dgfoundation.amp.ecs.common.ECS_PortType getECS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ECS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getECS(endpoint);
    }

    public org.dgfoundation.amp.ecs.common.ECS_PortType getECS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.dgfoundation.amp.ecs.common.ECSSoapBindingStub _stub = new org.dgfoundation.amp.ecs.common.ECSSoapBindingStub(portAddress, this);
            _stub.setPortName(getECSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setECSEndpointAddress(java.lang.String address) {
        ECS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.dgfoundation.amp.ecs.common.ECS_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                org.dgfoundation.amp.ecs.common.ECSSoapBindingStub _stub = new org.dgfoundation.amp.ecs.common.ECSSoapBindingStub(new java.net.URL(ECS_address), this);
                _stub.setPortName(getECSWSDDServiceName());
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
        if ("ECS".equals(inputPortName)) {
            return getECS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:org.dgfoundation.amp.ecs", "ECSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:org.dgfoundation.amp.ecs", "ECS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ECS".equals(portName)) {
            setECSEndpointAddress(address);
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
