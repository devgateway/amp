/**
 * GatePermission.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.digijava.module.gateperm.exception.NotBoundGateInputException;

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

    protected String	  gateTypeName;

    public GatePermission() {
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
	Queue<String> gateParametersQ = new ArrayBlockingQueue<String>(gateParameters.size(), true, gateParameters);
	Gate gate = Gate.instantiateGate(scope, gateParametersQ, gateTypeName);
	try {
	    if (gate.isOpen())
	        return actions;
	} catch (NotBoundGateInputException e) {
	    logger.error(e);
	    throw new RuntimeException( "NotBoundGateInputException Exception encountered", e);
	}
	return null;
    }

    public String getGateTypeName() {
	return gateTypeName;
    }

    public void setGateTypeName(String gateTypeName) {
	this.gateTypeName = gateTypeName;
    }



}
