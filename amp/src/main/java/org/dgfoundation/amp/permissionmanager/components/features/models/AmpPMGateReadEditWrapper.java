/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.io.Serializable;

/**
 * @author dan
 *
 */
public class AmpPMGateReadEditWrapper extends AmpPMReadEditWrapper {


    private String parameter;
    private Class gate;


    public AmpPMGateReadEditWrapper() {
        // TODO Auto-generated constructor stub
    }

    public AmpPMGateReadEditWrapper(String name, Boolean readFlag, Boolean editFlag) {
        super(name, readFlag, editFlag);

    }

    public AmpPMGateReadEditWrapper(String name) {
        super(name);
    }

    public AmpPMGateReadEditWrapper(Long id, String name, Boolean readFlag, Boolean editFlag) {
        super(id, name, readFlag, editFlag);
    }


    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    
    public Class getGate() {
        return gate;
    }

    public void setGate(Class gate) {
        this.gate = gate;
    }

    public AmpPMGateReadEditWrapper(Long id, String name,String parameter, Class gate, Boolean readFlag, Boolean editFlag) {
        super(id, name, readFlag, editFlag);
        this.parameter = parameter;
        this.gate = gate;
    }



}
