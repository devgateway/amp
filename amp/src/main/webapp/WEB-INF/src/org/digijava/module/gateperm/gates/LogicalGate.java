/**
 * LogicalGate.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.gates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.exception.NotBoundGateInputException;

/**
 * LogicalGate.java Universal logical binding between two GateS, using logical operators. The scope and parameters
 * collections are passed to each gate.
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 23.08.2007
 */
public class LogicalGate extends Gate {

    private static final MetaInfo[] PARAM_INFO = new MetaInfo[] {
        new MetaInfo("leftGateClassName", "the class name for the left gate"),
        new MetaInfo("rightGateClassName", "the class name for the right gate"),
        new MetaInfo("operator", "the boolean operator - AND, OR, XOR") };

    public LogicalGate(Map scope, Queue<String> parameters) {
    super(scope, parameters);
    // TODO Auto-generated constructor stub
    }

    /**
     * @see org.digijava.module.gateperm.core.Gate#description()
     */
    public String description() {
    return new String(
        "Universal logical binding between two GateS, using logical operators. The scope and parameters collections are passed to each gate.");
    }

    public LogicalGate() {
    super();
    }

    protected List<String> parseGateInfo(String gateInfo) {
    // first element will be the gate name
    ArrayList<String> ret = new ArrayList<String>();
    if (gateInfo.indexOf('(') == -1)
        ret.add(gateInfo);
    else {
        String gateName = gateInfo.substring(0, gateInfo.indexOf('('));
        ret.add(gateName);
        String parameters = gateInfo.substring(gateInfo.indexOf('(')+1, gateInfo.length() - 1);
        StringTokenizer st = new StringTokenizer(parameters, "~");
        while (st.hasMoreTokens()) {
        String token = st.nextToken();
        ret.add(token);
        }
    }
    return ret;
    }

    /**
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
    String leftGateClassInfo = (String) parameters.remove();
    String rightGateClassInfo = (String) parameters.remove();

    List<String> leftParsedInfo = parseGateInfo(leftGateClassInfo);
    List<String> rightParsedInfo = parseGateInfo(rightGateClassInfo);

    String operator = parameters.remove();

    String leftGateClassName = leftParsedInfo.remove(0);
    String rightGateClassName = rightParsedInfo.remove(0);
    parameters.addAll(leftParsedInfo);
    parameters.addAll(rightParsedInfo);
    Gate leftGate = Gate.instantiateGate(scope, parameters, leftGateClassName);
    Gate rightGate = Gate.instantiateGate(scope, parameters, rightGateClassName);

    try {
        switch (Integer.parseInt(operator)) {
        case OPERATOR_AND:
        return (leftGate.isOpen() && rightGate.isOpen());
        case OPERATOR_OR:
        return (leftGate.isOpen() || rightGate.isOpen());
        case OPERATOR_XOR:
        return (leftGate.isOpen() ^ rightGate.isOpen());
        default:
        return false;
        }
    } catch (NotBoundGateInputException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException("NotBoundGateInputException Exception encountered", e);
    }
    }

    public static final int OPERATOR_AND = 0;

    public static final int OPERATOR_OR  = 1;

    public static final int OPERATOR_XOR = 2;

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
