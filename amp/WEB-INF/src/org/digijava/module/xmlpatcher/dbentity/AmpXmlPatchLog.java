/**
 * AmpXmlPatchLog.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.dbentity;

import javax.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_XML_PATCH_LOG_seq")
    @SequenceGenerator(name = "AMP_XML_PATCH_LOG_seq", sequenceName = "AMP_XML_PATCH_LOG_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patch_id")
    private AmpXmlPatch patch;

    @Column(name = "log", columnDefinition = "text")
    private StringBuffer log;

    @Column(name = "date")
    private Date date;

    @Column(name = "error")
    private Boolean error;

    @Column(name = "file_checksum")
    private String fileChecksum;

    @Column(name = "elapsed")
    private Long elapsed;


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
