package org.dgfoundation.amp.codegenerators;

import java.util.List;

import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiReportsEngineForTesting;
import org.dgfoundation.amp.nireports.TestcasesReportsSchema;
import org.dgfoundation.amp.testutils.ActivityIdsFetcher;

/**
 * Abstract scaffold for classes that generate data for hardcoded reports schema. 
 * @author acartaleanu
 *
 */
public abstract class CodeGenerator {
	
	public abstract String generate();
	
	public static String escape(String a){
		if (a != null)
			return "\"" + a + "\"";
		return "null";
	}
	
	public static String pad(String a, int length) {
		if (a.length() > length)
			return a;
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < length - a.length(); i++)
			bld.append(" ");
		return a + bld.toString();
	}
	
	
	/**
	 * runs a given lambda in the context of a fully initialized NiReports engine,
	 *  which will have its activity filters overridden to generate ids corresponding to a given list of names in English
	 * @param activityNames
	 * @param runnable
	 */
	protected void runInEngineContext(List<String> activityNames, ExceptionConsumer<NiReportsEngine> runnable) {
		TestcasesReportsSchema.workspaceFilter = new ActivityIdsFetcher(activityNames);
		NiReportsEngineForTesting engine = new NiReportsEngineForTesting(TestcasesReportsSchema.instance, runnable);
		engine.execute(); // will run runnable in the engine's context
	}
	
	
	
}
