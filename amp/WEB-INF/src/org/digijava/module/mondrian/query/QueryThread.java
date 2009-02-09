package org.digijava.module.mondrian.query;

public class QueryThread {
	@SuppressWarnings("unused")
	private static String query;
	private static ThreadLocal<String> tl = new ThreadLocal<String>();

	public static String getQuery() {
		return tl.get();
	}

	public static void setQuery(String query) {
		tl.set(query);
	}

}
