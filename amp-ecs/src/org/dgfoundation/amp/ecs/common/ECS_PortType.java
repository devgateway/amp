/**
 * ECS_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.dgfoundation.amp.ecs.common;

public interface ECS_PortType extends java.rmi.Remote {
    public java.lang.String[] getParameters(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public boolean sendError(java.lang.String in0, int in1, java.lang.String in2, org.dgfoundation.amp.ecs.common.ErrorUser[] in3, org.dgfoundation.amp.ecs.common.ErrorScene[][] in4) throws java.rmi.RemoteException;
    public org.dgfoundation.amp.ecs.common.ErrorUser getUser() throws java.rmi.RemoteException;
    public org.dgfoundation.amp.ecs.common.ErrorScene getScene() throws java.rmi.RemoteException;
    public org.dgfoundation.amp.ecs.common.UserScenes getUserScenes() throws java.rmi.RemoteException;
}
