/**
 * 
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.gateperm.core.Gate;

import java.util.Map;
import java.util.Queue;

/**
 * This gate receives only one parameter, a string representation of a boolean
 * eg: "True", "true", "1","false","False","0",etc
 * ... and always returns true if the parameter was "true" or "1"
 * thus, this is a stub gate that performs no logic, it only returns 
 * the parameter boolean value
 * @author mihai
 *
 */
public class BooleanGate extends Gate {

    private static final MetaInfo[] PARAM_INFO=new MetaInfo[] { 
        new MetaInfo("boolean","true or false, 1 or 0")};
    
    /**
     * @param scope
     * @param parameters
     */
    public BooleanGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public BooleanGate() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.digijava.module.gateperm.core.Gate#description()
     */
    @Override
    public String description() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
        String string = parameters.poll();
        if("true".equalsIgnoreCase(string) || "1".equals(string)) return true;
        return false;
    }

    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MetaInfo[] parameterInfo() {
        return PARAM_INFO;
    }

    

}
