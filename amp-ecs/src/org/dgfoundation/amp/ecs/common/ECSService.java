/**
 * ECSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.dgfoundation.amp.ecs.common;

public interface ECSService extends javax.xml.rpc.Service {
    public java.lang.String getECSAddress();

    public org.dgfoundation.amp.ecs.common.ECS_PortType getECS() throws javax.xml.rpc.ServiceException;

    public org.dgfoundation.amp.ecs.common.ECS_PortType getECS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
