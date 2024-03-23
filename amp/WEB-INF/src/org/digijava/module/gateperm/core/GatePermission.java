/**
 * GatePermission.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import org.apache.log4j.Logger;
import org.digijava.module.gateperm.exception.NotBoundGateInputException;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * GatePermission.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 25.08.2007
 */
public class GatePermission extends Permission {

    private static Logger    logger = Logger.getLogger(GatePermission.class);
    
    @PropertyListableIgnore
    private static final long serialVersionUID = 5031509197096678164L;

    protected List<String>    gateParameters;

    protected Set<String>     actions;

    protected String      gateTypeName;

    /**
     * Checks if the specified parameter is present in the gate parameter list
     * @param parameter
     * @return true if the parameter is present
     */
    public boolean hasParameter(String parameter) {
    Iterator i=gateParameters.iterator();
    while (i.hasNext()) {
        String element = (String) i.next();
        if(element.equals(parameter)) return true;
    }
    return false;
    }
    
    /**
     * Checks if the specified action is present in the gate action list
     * @param action
     * @return true if the action is present. This does NOT mean the action can be executed!
     */
    public boolean hasAction(String action) {
    Iterator i=actions.iterator();
    while (i.hasNext()) {
        String element = (String) i.next();
        if(element.equals(action)) return true;
    }
    return false;
    }
    
    public GatePermission() {
    gateParameters = new ArrayList<String>();
    actions = new TreeSet<String>();
    dedicated=false;
    }
    
    public GatePermission(boolean dedicated) {
    super(dedicated);
    gateParameters = new ArrayList<String>();
    actions = new TreeSet<String>();
    }

    public Set<String> getActions() {
    return actions;
    }

    public void setActions(Set<String> actions) {
    this.actions = actions;
    }

    public List<String> getGateParameters() {
    return gateParameters;
    }

    public void setGateParameters(List<String> gateParameters) {
    this.gateParameters = gateParameters;
    }

    /**
         * @see org.digijava.module.gateperm.core.Permission#getAllowedActions()
         */
    @Override
    public Set<String> getAllowedActions(Map scope) {
    Queue<String> gateParametersQ =null;
    if(gateParameters.size()>0) gateParametersQ=new ArrayBlockingQueue<String>(gateParameters.size(), true, gateParameters);
    Gate gate = Gate.instantiateGate(scope, gateParametersQ, gateTypeName);
    try {
        if (gate.isOpen())
            return actions;
    } catch (NotBoundGateInputException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException( "NotBoundGateInputException Exception encountered", e);
    }
    return null;
    }

    public String getGateTypeName() {
    return gateTypeName;
    }
    
    public String getGateSimpleName() {
        return gateTypeName.substring(gateTypeName.lastIndexOf('.')+1);
    }
    
    public void setGateTypeName(String gateTypeName) {
    this.gateTypeName = gateTypeName;
    }

    public String toString() {
        return "{"+getGateSimpleName()+"("+gateParameters+")"+actions+"}";
        }


}
