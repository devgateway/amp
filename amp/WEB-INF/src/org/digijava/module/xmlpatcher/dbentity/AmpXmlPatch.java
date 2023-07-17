/**
 * AmpXmlPatch.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ListIndexBase;

import javax.persistence.*;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org This represents the
 *         reference of an XML patch file. It holds key information about the
 *         execution and state of the patch
 */
@Entity
@Table(name = "AMP_XML_PATCH")
@Cacheable
public class AmpXmlPatch implements Serializable, Comparable<AmpXmlPatch> {
    /**
     * This is the id of the patch. This is the actual name of the XML file,
     * without its path
     */
    @Id
    @Column(name = "patch_id")
    private String patchId;
    /**
     * This is the location of the patch, the dir where this patch has been read
     * from, relative to AMP app root
     */
    @Column(name = "location")
    private String location;
    /**
     * The date when the patch has been first found by the patcher module
     */
    @Column(name = "discovered")
    private Date discovered;

    @Column(name = "state")
    private Short state;

    @OneToMany(mappedBy = "patch", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<AmpXmlPatchLog> logs;

    public AmpXmlPatch() {
        logs= new ArrayList<>();
    }

    /**
     * Produces a new patch object using the patchId(file name) and location
     * (directory of discovery). Marks the discovery time and flags the patch as
     * OPEN
     * 
     * @param patchId
     *            the name of the XML patch file
     * @param location
     *            the directory of the patch file relative to AMP root
     */
    public AmpXmlPatch(String patchId, String location) {
        this.patchId = patchId;
        this.location = location;
        this.setState(XmlPatcherConstants.PatchStates.OPEN);
        this.discovered = new Date(System.currentTimeMillis());
    }

    public String getPatchId() {
        return patchId;
    }

    public void setPatchId(String patchId) {
        this.patchId = patchId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDiscovered() {
        return discovered;
    }

    public void setDiscovered(Date discovered) {
        this.discovered = discovered;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public List<AmpXmlPatchLog> getLogs() {
        if (logs!=null){ 
            return logs;
        }
        return new ArrayList<AmpXmlPatchLog>();
    }

    public void setLogs(List<AmpXmlPatchLog> logs) {
        this.logs = logs;
    }

    @Override
    public int compareTo(AmpXmlPatch o) {
        return this.getPatchId().compareTo(o.getPatchId());
    }
    
    @Override
    public String toString()
    {
        return this.location + this.patchId + "(state:" + this.state + ")";
    }
    
    @Override
    public int hashCode()
    {
        return (this.location + this.patchId).hashCode();
    }
    
    @Override
    public boolean equals(Object oth)
    {
        if (!(oth instanceof AmpXmlPatch))
            return false;
        AmpXmlPatch other = (AmpXmlPatch) oth;
        return this.location.equals(other.location) && (this.patchId.equals(other.patchId));
    }

}
