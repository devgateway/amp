/**
 * XmlPatchLog.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.dbentity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Provides logging
 *         information invocations of this patch. One patch can have one or
 *         several invocations. A log contains the date of execution, the output
 *         of the invocation (possible errors) and the status of the execution
 */
public class XmlPatchLog implements Serializable {

	protected Long id;

	/**
	 * The patch reference to this log
	 */
	protected XmlPatch patch;

	/**
	 * The log text
	 */
	protected StringBuffer log;

	/**
	 * The execution date/entry date log
	 */
	protected Date date;

	/**
	 * True if the execution was successful
	 * 
	 * @see org.digijava.module.xmlpatcher.util.XmlPatcherConstants
	 */
	protected Boolean successful;

	/**
	 * The MD5 checksum of the patch file. May be used to find if the patch file
	 * has been changed from one execution to the other.
	 */
	protected String fileChecksum;

	/**
	 * The amount of time in milliseconds that the patch execution lasted
	 */
	protected Long elapsed;

	/**
	 * Appends a string to the log stringbuffer
	 * 
	 * @param s
	 *            the string
	 */
	public void appendToLog(String s) {
		log.append(s);
	}

	/**
	 * Appends an exception stacktrace to the log
	 * 
	 * @param e
	 *            the exception
	 */
	public void appendToLog(Exception e) {
		log.append(e.getStackTrace().toString());
	}

	public XmlPatchLog() {
		this.log = new StringBuffer();
	}

	public XmlPatch getPatch() {
		return patch;
	}

	public void setPatch(XmlPatch patch) {
		this.patch = patch;
	}

	public String getLog() {
		return log.toString();
	}

	public void setLog(String log) {
		this.log = new StringBuffer(log);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFileChecksum() {
		return fileChecksum;
	}

	public void setFileChecksum(String fileChecksum) {
		this.fileChecksum = fileChecksum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getElapsed() {
		return elapsed;
	}

	public void setElapsed(Long elapsed) {
		this.elapsed = elapsed;
	}

	public Boolean getSuccessful() {
		return successful;
	}

	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}
}
