/**
 * ScriptingGate.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import org.digijava.module.gateperm.core.Gate;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * ScriptingGate.java
 * Gate that receives as its single parameter a BSH (BeanShell) script. 
 * The script has one variable, the Map named scope. See http://www.beanshell.org
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 31.08.2007
 */
public class ScriptingGate extends Gate {
    
    	private static final String[] PARAM_NAMES=new String[] {"bshScriptBody"};

	/** TODO description here
	 * @param scope
	 * @param parameters
	 */
	public ScriptingGate(Map scope, Queue<String> parameters) {
		super(scope, parameters);
		// TODO Auto-generated constructor stub
	}

	public ScriptingGate() {
	    super();
	}
	
	/** @see org.digijava.module.gateperm.core.Gate#description()
	 */
	@Override
	public String description() {
		return "Gate that receives as its single parameter a BSH (BeanShell) script." +
				" The script has one variable, the Map named 'scope'. " +
				"You can create custom gate logic directly in the admin interface. See http://www.beanshell.org";
	}

	/** @see org.digijava.module.gateperm.core.Gate#logic()
	 */
	@Override
	public boolean logic() {
		Interpreter i=new Interpreter();
		try {
			i.set("scope", scope);
			String script = parameters.poll();
			Boolean open = (Boolean) i.eval(script);
			return open.booleanValue();
		} catch (EvalError e) {
			logger.error("ScriptingGate script evaluation error: "+e);
			throw new RuntimeException( "EvalError Exception encountered", e);
		}
	
	}

	/** @see org.digijava.module.gateperm.core.Gate#parameterNames()
	 */
	@Override
	public String[] parameterNames() {
		return PARAM_NAMES;
	}



	@Override
	public String[] mandatoryScopeKeys() {
	    return null;
	}

}
