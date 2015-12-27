package org.dgfoundation.amp.nireports;

import java.util.function.Supplier;

public class NiUtils {
	
	public static void failIf(boolean b, String msg) {
		if (b) throw new RuntimeException(msg);	
	}
	
	public static void failIf(boolean b, Supplier<String> msg) {
		if (b)
			throw new RuntimeException(msg.get());
	}
}
