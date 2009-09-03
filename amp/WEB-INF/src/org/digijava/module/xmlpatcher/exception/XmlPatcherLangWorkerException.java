/**
 * XmlPatcherLangWorkerException.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.exception;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatcherLangWorkerException extends XmlPatcherWorkerException {

	/**
	 * 
	 */
	public XmlPatcherLangWorkerException() {
		// TODO Auto-generated constructor stub
	}
	
	public XmlPatcherLangWorkerException(Exception e) {
		super(e.getCause());
	}
	

	/**
	 * @param arg0
	 */
	public XmlPatcherLangWorkerException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public XmlPatcherLangWorkerException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public XmlPatcherLangWorkerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
