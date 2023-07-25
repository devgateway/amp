/**
 * AmpXmlPatchLog.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.dbentity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Provides logging information invocations of this patch. One patch can
 *         have one or several invocations. A log contains the date of
 *         execution, the output of the invocation (possible errors) and the
 *         status of the execution
 */
public class AmpXmlPatchLog implements Serializable {


    protected Long id;

    /**
     * The patch reference to this log
     */
    protected AmpXmlPatch patch;

    /**
     * The log text
     */
    protected StringBuffer log;

    /**
     * The execution date/entry date log
     */
    protected Date date;

    /**
     * True if the execution has encountered errors
     */
    protected Boolean error;

    /**
     * The MD5 checksum of the patch file. May be used to find if the patch file
     * has been changed from one execution to the other.
     */
    protected String fileChecksum;

    /**
     * The amount of time in milliseconds that the patch execution lasted
     */
    protected Long elapsed;

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    protected Integer idx;

    /**
     * Appends a string to the log stringbuffer
     * 
     * @param s
     *            the string
     */
    public void appendToLog(String s) {
        error=true;
        log.append(s);
    }

    /**
     * Appends an exception stacktrace to the log
     * 
     * @param e
     *            the exception
     */
    public void appendToLog(Exception e) {
        error=true;
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        log.append(writer.toString());
    }


    public AmpXmlPatchLog() {
    }

    
    
    public AmpXmlPatchLog(AmpXmlPatch p) {
        this.log = new StringBuffer();
        this.patch = p;
        error=false;
        this.date=new Date(System.currentTimeMillis());
    }

    public AmpXmlPatch getPatch() {
        return patch;
    }

    public void setPatch(AmpXmlPatch patch) {
        this.patch = patch;
    }

    public String getLog() {
        return log.toString();
    }
    
    public String getLogLabel() {
        String ls=log.toString();
        if(ls.indexOf(':')!=-1)
        return ls.substring(0,ls.indexOf(':'));
        else return ls;
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

    /**
     * @return the error
     */
    public Boolean getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(Boolean error) {
        this.error = error;
    }

}
