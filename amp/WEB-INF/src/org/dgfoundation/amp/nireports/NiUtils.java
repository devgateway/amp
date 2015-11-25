package org.dgfoundation.amp.nireports;

public class NiUtils {
	
	public static void failIf(boolean b, String msg) {
		if (b) throw new RuntimeException(msg);	
	}
}
