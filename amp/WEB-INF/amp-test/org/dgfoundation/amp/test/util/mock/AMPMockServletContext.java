package org.dgfoundation.amp.test.util.mock;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import mondrian.tui.MockServletContext;

public class AMPMockServletContext extends MockServletContext {

	public String getRealPath(String path) {
		return new java.io.File(AMPMockServletContext.class.getClassLoader().getResource("").getPath()).getParent().toString();
	}

	public static AMPMockServletContext getWrapeerServletContext(ServletConfig config) {
		AMPMockServletContext ctx = new AMPMockServletContext();
		return ctx;

	}
}
