/**
 * PatchFile.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.autopatcher.core;

import java.sql.Timestamp;

public class PatchFile {
    private Long id;
    private String absolutePatchName;
    private Timestamp invoked;
    public String getAbsolutePatchName() {
        return absolutePatchName;
    }
    public void setAbsolutePatchName(String absolutePatchName) {
        this.absolutePatchName = absolutePatchName;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Timestamp getInvoked() {
        return invoked;
    }
    public void setInvoked(Timestamp invoked) {
        this.invoked = invoked;
    }
}
