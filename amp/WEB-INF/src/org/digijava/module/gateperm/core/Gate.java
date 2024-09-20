/**
 * Gate.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.PropertyListable;
import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.exception.NotBoundGateInputException;

import javax.servlet.http.HttpServletRequest;

/**
 * Gate.java TODO description here
 *
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 23.08.2007
 */
public abstract class Gate extends PropertyListable {

    public static Logger logger = Logger.getLogger(Gate.class);

    protected Queue<String> parameters;

    protected Map scope;

    protected Map<String, String> state;
    private static final String TEAM_MEMBER_MISSING="Team member not found in scope";
    private static final String REPORT_URL="/reports";

    public Gate(Map scope, Queue<String> parameters) {
        this.scope = scope;
        this.parameters = parameters;
        state = new HashMap<String, String>();
    }

    public Gate() {
        state = new HashMap<String, String>();
    }

    @Override
    public String getBeanName() {
        return this.getClass().getSimpleName();
    }

    public static Gate instantiateGate(String gateTypeName) {
        try {
            Class gateType = Class.forName(gateTypeName);
            Constructor constructor = gateType.getConstructor(new Class[] {});
            Object object = constructor.newInstance(new Object[] {});
            return (Gate) object;
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static Gate instantiateGate(Map scope, Queue<String> parameters,
            String gateTypeName) {
        Gate gate = Gate.instantiateGate(gateTypeName);
        gate.setScope(scope);
        gate.setParameters(parameters);
        return gate;
    }

    public boolean isOpen() throws NotBoundGateInputException {
        boolean generatePublicReports = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.GENERATE_REPORTS_AS_PUBLIC);
        String requestUrl = TLSUtils.getRequest().getRequestURL().toString();
        if (parameterInfo() != null
                && (parameters == null || parameters.size() < parameterInfo().length))
            throw new NotBoundGateInputException(
                    "Not enough gate parameters. Gate " + this.getClass()
                            + " needs " + parameterInfo().length
                            + " parameters");
        if (scope == null)
            throw new NotBoundGateInputException("Scope cannot be null");

        // saving the mandatory keys along with their values
        state.clear();
        for (int i = 0; mandatoryScopeKeys() != null
                && i < mandatoryScopeKeys().length; i++) {
            Object object =  scope.get(mandatoryScopeKeys()[i]);

            if (!scope.containsKey(mandatoryScopeKeys()[i])) {
                if (mandatoryScopeKeys()[i].equals(GatePermConst.ScopeKeys.CURRENT_MEMBER) && (generatePublicReports) && requestUrl.contains(REPORT_URL))
                    {
                        continue;

                }
                throw new NotBoundGateInputException(
                        "Mandatory scope parameter '" + mandatoryScopeKeys()[i]
                                + "' missing for Gate " + this.getClass());
            }
            if(object!=null) state.put(mandatoryScopeKeys()[i].getCategory(), object.toString());
        }

        try {
            if(parameters!=null && parameters.size()>0)
                logger.debug("Parameters: "+parameters);
                boolean b=logic();
                logger.debug("Gate "+this.getClass().getSimpleName()+" "+(b?"approves":"rejects")+" access. Gate state is: "+state);
            return b;
        } catch (Exception e) {
            logger.error("Gate "+this.getClass().getName()+" logic has thrown an exception: ", e);
            if (generatePublicReports && e.getMessage().equalsIgnoreCase(TEAM_MEMBER_MISSING) && requestUrl.contains(REPORT_URL))
            {
                logger.info("However since we are allowing public reports we open the  gate");
                return true;
            }
        }
        return false;
    }

    /**
     * overriden by subclasses to implement Gate logic
     *
     * @return true if the gate is open for the given inputs + parameters
     */
    public abstract boolean logic() throws Exception;

    public abstract MetaInfo[] parameterInfo();

    public abstract MetaInfo[] mandatoryScopeKeys();

    public abstract String description();

    public Queue<String> getParameters() {
        return parameters;
    }

    public void setParameters(Queue<String> parameters) {
        this.parameters = parameters;
    }

    public Map getScope() {
        return scope;
    }

    public void setScope(Map scope) {
        this.scope = scope;
    }

}
