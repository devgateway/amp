package org.dgfoundation.ecs.core;


import org.dgfoundation.ecs.keeper.ErrorKeeper;
import org.dgfoundation.ecs.keeper.ErrorKeeperRAM;
import org.dgfoundation.ecs.keeper.ErrorReporting;

/**
 * 
 * @author Arty
 * 
 */
public final class ECS {
	private ECSRunner ecsrunner;
	private ErrorKeeper ekeeper;
	private ErrorReporting errorReporting;

	private String serverName;
	
	public ECS(String serverName) {
		this.serverName = serverName;
		ekeeper = new ErrorKeeperRAM(serverName);
		errorReporting = new ErrorReporting(ekeeper); 
		ecsrunner = new ECSRunner(serverName, ekeeper); //thread don't move init
	}
	
	public void start() {
		ecsrunner.start();
	}

	public void stop() {
		if (ecsrunner == null)
			ecsrunner = new ECSRunner(serverName, ekeeper);
			
		ecsrunner.stopEcs();

		if (ekeeper instanceof ErrorKeeperRAM)
			((ErrorKeeperRAM)ekeeper).getRetryThread().stopRetryThread();
	}

	public ErrorReporting getErrorReporting() {
		/*
		if (errorReporting == null)
			errorReporting = new ErrorReporting(ekeeper);
			*/
		return errorReporting;
	}
	

	public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char         b;
        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        String       t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
                if (b == '<') {
                    sb.append('\\');
                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
                sb.append("\\r");
                break;
            default:
                if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
                               (c >= '\u2000' && c < '\u2100')) {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }

	//quote
	public static String q(String s){
		return quote(s);
	}
	//tuple
	public static String t(String field, String value){
		return to(field, value)+",";
	}
	//tuple omit comma
	public static String to(String field, String value){
		return q(field) + ":" + q(value);
	}
}
