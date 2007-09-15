/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import org.digijava.module.gateperm.core.Gate;

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

	private static String [] PARAM_NAMES=new String[] { "boolean" };
	
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
	public boolean logic() {
		String string = parameters.poll();
		if("true".equalsIgnoreCase(string) || "1".equals(string)) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.digijava.module.gateperm.core.Gate#mandatoryScopeKeys()
	 */
	@Override
	public String[] mandatoryScopeKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.digijava.module.gateperm.core.Gate#parameterNames()
	 */
	@Override
	public String[] parameterNames() {
		return PARAM_NAMES;
	}

}
