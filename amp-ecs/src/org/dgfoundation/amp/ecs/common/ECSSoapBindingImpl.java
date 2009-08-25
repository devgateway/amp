/**
 * ECSSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.dgfoundation.amp.ecs.common;

import org.dgfoundation.amp.ecs.webservice.ECSImpl;

public class ECSSoapBindingImpl implements org.dgfoundation.amp.ecs.common.ECS_PortType{
	private ECSImpl ecs = new ECSImpl();
	
    public java.lang.String[] getParameters(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return ecs.getParameters(in0, in1);
    }

    public boolean sendError(java.lang.String in0, int in1, java.lang.String in2, org.dgfoundation.amp.ecs.common.ErrorUser[] in3, org.dgfoundation.amp.ecs.common.ErrorScene[][] in4) throws java.rmi.RemoteException {
        return ecs.sendError(in0, in1, in2, in3, in4);
    }

    public org.dgfoundation.amp.ecs.common.ErrorUser getUser() throws java.rmi.RemoteException {
        return null;
    }

    public org.dgfoundation.amp.ecs.common.ErrorScene getScene() throws java.rmi.RemoteException {
        return null;
    }

    public org.dgfoundation.amp.ecs.common.UserScenes getUserScenes() throws java.rmi.RemoteException {
        return null;
    }

}
